package system.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import system.dao.QueueDao;
import system.dao.QueueEntryDao;
import system.model.ProcessingQueue;
import system.model.QueueEntry;
import system.service.QueueService;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class QueueServiceImpl implements QueueService {

	private static final Logger logger = Logger.getLogger(QueueServiceImpl.class.getName());

	@Autowired
	private QueueDao queueDao;

	@Autowired
	private QueueEntryDao entryDao;

	public ProcessingQueue getOrCreateQueue(String name) {
		List<ProcessingQueue> queues = queueDao.getQueueByName(name);
		ProcessingQueue queue = null;
		if (queues == null || queues.size() == 0) {
			queue = ProcessingQueue.newProcessingQueue(name);
			queue = queueDao.save(queue);
		} else {
			queue = queues.get(0);
		}

		return queue;
	}


	public ProcessingQueue createQueue(String name) {
		ProcessingQueue queue = null;
		queue = ProcessingQueue.newProcessingQueue(name);
		queue = queueDao.save(queue);

		return queue;
	}
	
	public ProcessingQueue getQueueByName(String name){
		List<ProcessingQueue> queues = queueDao.getQueueByName(name);
		ProcessingQueue queue = null;
		if (queues == null || queues.size() == 0) {
		} else {
			queue = queues.get(0);
		}
		
		return queue;
	}

	public List<ProcessingQueue> getAllQueue() {
		List<ProcessingQueue> queues = (List<ProcessingQueue>) queueDao.findAll();
		logger.debug("getAllQueue,queue size: " + queues.size());

		for (ProcessingQueue queue : queues) {
			Long entryCount = entryDao.getQueueEntryCountByQueueRef(queue);
			queue.setEntryCount(entryCount);
		}

		return queues;
	}

	public List<QueueEntry> getEntriesByQueueId(Integer queueId) {
		ProcessingQueue queue = queueDao.getQueueById(queueId);
		List<QueueEntry> list = entryDao.getQueueEntryByQueueRef(queue);

		return list;
	}

	public Integer deleteQueueAndEntries(Integer queueId){
		Integer entries = 0;
		
		ProcessingQueue queue = queueDao.getQueueById(queueId);
		List<QueueEntry> list = entryDao.getQueueEntryByQueueRef(queue);
		for(QueueEntry entry : list){
			entryDao.delete(entry);
			entries ++ ;
		}
		queueDao.delete(queue);
		
		return entries;
	}
	
	
	public void processQueue(ProcessingQueue queue) {
		// TODO Auto-generated method stub

	}

	public QueueEntry addEntry(String queueName, String targetClass, String targetMethod, String arguments) {
		ProcessingQueue queue = getOrCreateQueue(queueName);
		
		QueueEntry entry = QueueEntry.newQueueEntry(queue, targetClass, targetMethod, arguments);
		entryDao.save(entry);

		return entry;
	}

	public QueueEntry addEntry(ProcessingQueue queue, String targetClass, String targetMethod, String arguments) {
		QueueEntry entry = QueueEntry.newQueueEntry(queue, targetClass, targetMethod, arguments);
		entryDao.save(entry);

		return entry;
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
		return entryDao.getQueueEntryCountByQueueRef(queue);
	}
	

}
