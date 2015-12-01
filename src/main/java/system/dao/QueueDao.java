package system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import system.model.ProcessingQueue;

@Repository
public interface QueueDao extends CrudRepository<ProcessingQueue, Integer> {

	/**
	 * 可以直接解析方法名创建查询，也可以使用Query
	 * @param name
	 * @return
	 */
	@Query("select queue from ProcessingQueue queue where queue.name=:name")
	public List<ProcessingQueue> getQueueByName(@Param("name") String name);
	
	public ProcessingQueue getQueueById(Integer id);
}
