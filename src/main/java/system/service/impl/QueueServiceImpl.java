package system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import system.dao.QueueDaoBase;
import system.dao.QueueEntryDaoBase;
import system.model.ProcessingQueue;
import system.model.QueueEntry;
import system.service.QueueService;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class QueueServiceImpl implements QueueService {

	@Autowired
	private QueueDaoBase queueDao;
	
	@Autowired
	private QueueEntryDaoBase entryDao;
	
	public ProcessingQueue getOrCreateQueue(String name){
		ProcessingQueue queue = queueDao.getQueue(name);
		if(queue == null){
			queue = queueDao.createQueue(name);
		}
		
		return queue;
	}

	public ProcessingQueue getQueue(String name) {
		ProcessingQueue queue = queueDao.getQueue(name);
		return queue;
	}

	public ProcessingQueue createQueue(String name) {
		
		return queueDao.createQueue(name);
	}
	
	public List<ProcessingQueue> getAllQueue(){
		List<ProcessingQueue> queues = queueDao.getAllQueue();
		for(ProcessingQueue queue : queues){
			Long entryCount = entryDao.getQueueEntryCount(queue);
			queue.setEntryCount(entryCount);
		}
		return queues;
	}
	
	public void processQueue(ProcessingQueue queue) {
		// TODO Auto-generated method stub

	}

	public Boolean addEntry(String queueName, String targetClass, String targetMethod, String[] arguments) {
		ProcessingQueue queue = getOrCreateQueue(queueName);
	
		return null;
	}

	public Boolean addEntry(ProcessingQueue queue, String targetClass, String targetMethod, String[] arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueueEntry getEntry(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueueEntry resetEntry(QueueEntry entry) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getEntryCount(ProcessingQueue queue) {
		return entryDao.getQueueEntryCount(queue);
	}

}
