package system.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import system.dao.QueueDaoBase;
import system.model.ProcessingQueue;
import common.dao.impl.BaseDaoImpl;


@Repository("QueueDaoImpl")
public class QueueDaoBaseImpl extends BaseDaoImpl<ProcessingQueue> implements QueueDaoBase {

	public ProcessingQueue getQueue(String name) {
		ProcessingQueue queue = super.get("from ProcessingQueue where name=?", new Object[] { name });
		return queue;
	}

	public ProcessingQueue createQueue(String name) {
		ProcessingQueue queue = ProcessingQueue.newProcessingQueue(name);
		super.save(queue);
		return queue;
	}

	public List<ProcessingQueue> getAllQueue() {
		
		return super.find("from ProcessingQueue");
	}

}
