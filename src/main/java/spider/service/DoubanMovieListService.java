package spider.service;

import java.io.IOException;

public interface DoubanMovieListService {

	public abstract void parseMovieList(String urlIndex) throws IOException;

}