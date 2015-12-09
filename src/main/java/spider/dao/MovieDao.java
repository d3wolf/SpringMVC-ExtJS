package spider.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import spider.model.Movie;

public interface MovieDao extends PagingAndSortingRepository<Movie, Integer> {

	public List<Movie> findMovieByDataId(String dataId);
}
