package spider.service;

import java.io.IOException;

import common.BaseException;

public interface DoubanMovieDetailService {

	/**
	 * 爬电影详细信息
	 * @param crawlOid
	 * @throws BaseException
	 * @throws IOException
	 */
	public abstract void crawlMovieDetail(String crawlOid) throws BaseException, IOException;
	
}