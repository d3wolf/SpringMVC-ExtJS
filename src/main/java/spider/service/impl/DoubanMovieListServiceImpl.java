package spider.service.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import spider.dao.DoubanMovieCrawlUrlDao;
import spider.model.DoubanMovieCrawlUrl;
import spider.model.FetchedPage;
import spider.service.DoubanMovieListService;
import spider.service.JsoupService;
import system.service.QueueService;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class DoubanMovieListServiceImpl implements DoubanMovieListService {

	private static final Logger logger = Logger.getLogger(DoubanMovieListServiceImpl.class.getName());

	private static final String urlPattern = "http://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=4000000&page_start=";

	@Autowired
	private JsoupService jsoupService;
	@Autowired
	private DoubanMovieCrawlUrlDao urlDao;

	/* (non-Javadoc)
	 * @see spider.service.impl.DoubanMovieListService#parseMovieList(java.lang.String)
	 */
	public void parseMovieList(String urlIndex) throws IOException {
		String url = urlPattern + urlIndex;
		logger.info("crawl url: " + url);

		FetchedPage fetchedPage = jsoupService.fetchUrl(url);

		if (fetchedPage.getStatusCode() == 200) {
			Document doc = fetchedPage.getContent();
			String content = doc.select("body").text();
			if (fetchedPage.getContentType().contains("json")) {
				// {"subjects":[{"rate":"8.1","cover_x":1408,"is_beetle_subject":false,"title":"暗杀","url":"http:\/\/movie.douban.com\/subject\/25823132\/","playable":true,"cover":"http:xxx","id":"25823132","cover_y":2009,"is_new":false},{"rate":"7.5","cover_x":2134,"is_beetle_subject":false,"title":"爱的成人式","url":"http:xxx","playable":false,"cover":"http:\xxx","id":"26140405","cover_y":3058,"is_new":true}]}
				JSONObject contentObj = JSONObject.fromObject(content);
				Object listObj = contentObj.get("subjects");
				JSONArray movieArr = JSONArray.fromObject(listObj);
				logger.debug(String.format("find %s movies info", movieArr.size()));

				Iterator<?> it = movieArr.iterator();
				while (it.hasNext()) {
					Object movieObj = it.next();
					JSONObject jo = JSONObject.fromObject(movieObj);
					String title = jo.get("title").toString();
					String MovieUrl = jo.get("url").toString();
					String rate = jo.get("rate").toString();
					String dataId = jo.get("id").toString();
					String coverUrl = jo.get("cover").toString();

					List<DoubanMovieCrawlUrl> urlById = urlDao.findDoubanMovieCrawlUrlByDataId(dataId);
					if(urlById != null && urlById.size()>0){
						logger.debug("url已经存在, dataId: " + dataId);
					}else{
						Float rateInt = 0f;
						try {
							rateInt = Float.parseFloat(rate);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
								
						DoubanMovieCrawlUrl crawlUrl =  DoubanMovieCrawlUrl.newDoubanMovieCrawlUrl(MovieUrl, dataId, title, coverUrl, rateInt);
						urlDao.save(crawlUrl);
						
						logger.debug("save crawl url: " + crawlUrl);
					}

				}
			} else {
				logger.debug("can't handle content type: " + fetchedPage.getContentType());
			}
		} else {
			logger.debug("status code error: " + fetchedPage.getStatusCode());
		}
	}
	
	@Autowired
	private QueueService queueService;
	
	public Integer push2MovieCrawlerUrlQueue(){
		String queueName = "doubanMovieCrawlerUrlQueue";
		
		String targetClass = "";
		String targetMethod = "";
		
		int count = 0;
		
		List<DoubanMovieCrawlUrl> list = (List<DoubanMovieCrawlUrl>) urlDao.findAll();
		for(DoubanMovieCrawlUrl crawlUrl : list){
			JSONObject jo = new JSONObject();
			jo.put("oid", crawlUrl.toString());
			
			String arguments = jo.toString();
			
			queueService.addEntry(queueName, targetClass, targetMethod, arguments);
			count ++;
		}
		
		return count;
	}
}
