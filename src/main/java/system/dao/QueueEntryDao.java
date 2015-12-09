package system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import system.model.ProcessingQueue;
import system.model.QueueEntry;

public interface QueueEntryDao extends PagingAndSortingRepository<QueueEntry, Integer> {

	@Query("select count(*) from QueueEntry where queueRef=:queue")
	public Long getQueueEntryCountByQueueRef(@Param("queue")ProcessingQueue queue);
	
	public List<QueueEntry> getQueueEntryByQueueRef(ProcessingQueue queue);
	
	@Query("select entry from QueueEntry entry where entry.queueRef=:queue and entry.entryState=:entryState order by entry.createTimestamp asc")
	public List<QueueEntry> getEntriesByEntryState(@Param("queue")ProcessingQueue queue, @Param("entryState")String entryState);
}
