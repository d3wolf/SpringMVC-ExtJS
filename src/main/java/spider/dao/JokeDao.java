package spider.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import spider.model.Joke;

public interface JokeDao  extends PagingAndSortingRepository<Joke, Integer>{

	public Joke getJokeById(Integer id);
	
	@Query("select max(joke.id) from Joke joke")
	public Integer getJokeMaxId();
	
	public List<Joke> findJokeByDataId(Integer dataId);
}
