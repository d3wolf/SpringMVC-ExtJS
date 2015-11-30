package system.dao.impl;

import org.springframework.stereotype.Repository;

import common.dao.impl.BaseDaoImpl;
import system.dao.QueueEntryDaoBase;
import system.model.ProcessingQueue;
import system.model.QueueEntry;

@Repository("QueueEntryDaoImpl")
public class QueueEntryDaoBaseImpl extends BaseDaoImpl<QueueEntry> implements QueueEntryDaoBase {

	
	public QueueEntry createQueueEntry(ProcessingQueue queue, String targetClass, String targetMethod, String arguments){
		QueueEntry entry =  QueueEntry.newQueueEntry(queue, targetClass, targetMethod, arguments);
		
		super.save(entry);
		
		return entry;
	}

	public Long getQueueEntryCount(ProcessingQueue queue) {
		Long count = super.count("select count(*) from QueueEntry where queueRef=?", new Object[]{queue});
		return count;
	}
}
