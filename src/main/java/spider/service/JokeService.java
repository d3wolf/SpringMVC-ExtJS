package spider.service;

import java.io.IOException;
import java.util.List;

import spider.model.Joke;

public interface JokeService {

	/**
	 * 从第三方位置获取到joke
	 * @param urlIndex
	 * @return
	 * @throws IOException
	 */
	public List<Joke> getJokeBy3rd(String urlIndex) throws IOException;
		
	/**
	 * 将joke持久化
	 * @param jokes
	 * @return
	 */
	public int persistJoke(List<Joke> jokes);
	
	/**
	 * 从数据库取出joke，分页
	 * @param page
	 * @return
	 */
	public List<Joke> getJokeFromDB(Integer page);
	
	/**
	 * 根据id索引获取Joke
	 * @param id
	 * @return
	 */
	public Joke getJokeById(Integer id);
	/**
	 * 获取到下一个Joke,将id++获取
	 * @param joke
	 * @return
	 */
	public Joke getNextJoke(Joke joke);
}
