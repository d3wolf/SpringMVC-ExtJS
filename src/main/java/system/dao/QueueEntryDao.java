package system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import system.model.ProcessingQueue;
import system.model.QueueEntry;

public interface QueueEntryDao extends CrudRepository<QueueEntry, Integer> {

	@Query("select count(*) from QueueEntry where queueRef=:queue")
	public Long getQueueEntryCountByQueueRef(@Param("queue")ProcessingQueue queue);
	
	public List<QueueEntry> getQueueEntryByQueueRef(ProcessingQueue queue);
}
