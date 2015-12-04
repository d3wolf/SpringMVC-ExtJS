package spider.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import spider.model.DoubanMovieCrawlUrl;

public interface DoubanMovieCrawlUrlDao extends CrudRepository<DoubanMovieCrawlUrl, Integer>{

	public List<DoubanMovieCrawlUrl> findDoubanMovieCrawlUrlByDataId(String dataId);
}
