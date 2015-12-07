package spider.service;

import java.io.IOException;

public interface DoubanMovieListService {

	/**
	 * 爬去电影url
	 * @param urlIndex
	 * @throws IOException
	 */
	public abstract void parseMovieList(String urlIndex) throws IOException;
	
	/**
	 * 加入待爬取队列
	 * @return
	 */
	public Integer push2MovieCrawlerUrlQueue();

}