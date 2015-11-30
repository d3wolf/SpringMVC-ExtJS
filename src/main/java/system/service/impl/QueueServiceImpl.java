package system.service.impl;

import java.util.List;

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

	@Autowired
	private QueueDao queueDao;
	
	@Autowired
	private QueueEntryDao entryDao;
	
	public ProcessingQueue getOrCreateQueue(String name){
		List<ProcessingQueue> queues = queueDao.getQueueByName(name);
		ProcessingQueue queue = null;
		if(queues == null || queues.size() == 0){
			queue = ProcessingQueue.newProcessingQueue(name);
			queue = queueDao.save(queue);
		}else{
			queue = queues.get(0);
		}
		
		return queue;
	}

	
	public List<ProcessingQueue> getAllQueue(){
		List<ProcessingQueue> queues = (List<ProcessingQueue>) queueDao.findAll();
		for(ProcessingQueue queue : queues){
			Long entryCount = entryDao.getQueueEntryCountByQueueRef(queue);
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
		return entryDao.getQueueEntryCountByQueueRef(queue);
	}

}
