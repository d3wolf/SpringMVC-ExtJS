package spider.service.impl;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spider.model.DoubanMovieCrawlUrl;
import spider.model.FetchedPage;
import spider.model.Movie;
import spider.service.DoubanMovieDetailService;
import spider.service.JsoupService;

import common.BaseException;
import common.service.BaseService;

@Service
public class DoubanMovieDetailServiceImpl implements DoubanMovieDetailService {
	
	private static final Logger logger = Logger.getLogger(DoubanMovieDetailServiceImpl.class.getName());
	
	@Autowired
	private BaseService baseService;
	@Autowired
	private JsoupService jsoupService;

	public void crawlMovieDetail(String crawlOid) throws BaseException, IOException {
		Object obj = baseService.getObjectByOid(crawlOid);
		if (obj != null && obj instanceof DoubanMovieCrawlUrl) {
			DoubanMovieCrawlUrl crawlUrl = (DoubanMovieCrawlUrl) obj;
			String dataId = crawlUrl.getDataId();
			String url = crawlUrl.getUrl();
			String title = crawlUrl.getTitle();
			String coverUrl = crawlUrl.getCoverUrl();
			Float rate = crawlUrl.getRate();
			
			Movie movie = new Movie();
			movie.setDataId(dataId);
			movie.setTitle(title);
			movie.setRate(rate);
			getContentFromUrl(url, movie);
			
			System.out.println("########"+movie.getSummary());
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
}
