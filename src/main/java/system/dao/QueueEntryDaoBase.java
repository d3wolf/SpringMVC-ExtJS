package system.dao;

import system.model.ProcessingQueue;
import system.model.QueueEntry;

public interface QueueEntryDaoBase {
	public QueueEntry createQueueEntry(ProcessingQueue queue, String targetClass, String targetMethod, String arguments);
	
	public Long getQueueEntryCount(ProcessingQueue queue);
}
