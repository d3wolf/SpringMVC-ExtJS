package spider.service.impl;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import spider.dao.MovieDao;
import spider.model.DoubanMovieCrawlUrl;
import spider.model.FetchedPage;
import spider.model.Movie;
import spider.service.DoubanMovieDetailService;
import spider.service.JsoupService;
import system.service.QueueService;

import common.BaseException;
import common.service.BaseService;

@Service
public class DoubanMovieDetailServiceImpl implements DoubanMovieDetailService {
	
	private static final Logger logger = Logger.getLogger(DoubanMovieDetailServiceImpl.class.getName());
	
	@Autowired
	private BaseService baseService;
	@Autowired
	private JsoupService jsoupService;
	@Autowired
	private QueueService queueService;
	@Autowired
	private MovieDao movieDao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void crawlMovieDetail(String arguments) throws BaseException, IOException {
		JSONObject jo = JSONObject.fromObject(arguments);
		String oid = jo.getString("oid");
		Object obj = baseService.getObjectByOid(oid);
		if (obj != null && obj instanceof DoubanMovieCrawlUrl) {
			DoubanMovieCrawlUrl crawlUrl = (DoubanMovieCrawlUrl) obj;
			String dataId = crawlUrl.getDataId();
			String url = crawlUrl.getUrl();
			String title = crawlUrl.getTitle();
			String coverUrl = crawlUrl.getCoverUrl();
			Float rate = crawlUrl.getRate();
			
			Movie movie = null;
			List<Movie> movies = movieDao.findMovieByDataId(dataId);
			if(movies != null && movies.size()>0){
				movie = movies.get(0);
			}else {
				movie = new Movie();
			}
			movie.setDataId(dataId);
			movie.setTitle(title);
			movie.setRate(rate);
			getContentFromUrl(url, movie);
			getCoverFromUrl(coverUrl, movie);
			
			movieDao.save(movie);
			
		} else {
			throw new BaseException("object is not a DoubanMovieCrawlUrl");
		}
	}

	/**
	 * 从详细信息页面获取信息,目前仅获取简介,评分和封面已经在list获取
	 * @param url
	 * @param movie
	 * @throws IOException
	 */
	private void getContentFromUrl(String url, Movie movie) throws IOException {
		logger.info("crawl url: " + url);

		FetchedPage fetchedPage = jsoupService.fetchUrl(url);
		if (fetchedPage.getStatusCode() == 200) {
			Document doc = fetchedPage.getContent();
			
			if(doc.getElementsByClass("all hidden") != null){
				//:not(selector) 查找与选择器不匹配的元素
				String	summary = doc.select("div.indent > span:not(.short)").html();
				movie.setSummary(summary);
			}
		}
	}
	
	private void getCoverFromUrl(String url, Movie movie) throws IOException{
		String filename = movie.getDataId() + ".jpg";
		String cover  = "E:\\attachs\\doubanMovieCover\\"+filename;
		spider.util.FileUtils.download(url, filename, "E:\\attachs\\doubanMovieCover\\");
		movie.setCover(cover);
	}
	
}
