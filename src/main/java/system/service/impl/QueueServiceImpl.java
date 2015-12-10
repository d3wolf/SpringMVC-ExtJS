package system.service.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import spider.model.Joke;
import system.SpringContextUtil;
import system.dao.QueueDao;
import system.dao.QueueEntryDao;
import system.model.ProcessingQueue;
import system.model.QueueEntry;
import system.service.QueueService;
import common.BaseException;
import common.Constants;

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
	
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public ProcessingQueue getQueueByName(String name){
		List<ProcessingQueue> queues = queueDao.getQueueByName(name);
		ProcessingQueue queue = null;
		if (queues == null || queues.size() == 0) {
		} else {
			queue = queues.get(0);
		}
		
		return queue;
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<ProcessingQueue> getAllQueue() {
		List<ProcessingQueue> queues = (List<ProcessingQueue>) queueDao.findAll();
		logger.debug("getAllQueue,queue size: " + queues.size());

		for (ProcessingQueue queue : queues) {
			Long entryCount = entryDao.getQueueEntryCountByQueueRef(queue);
			queue.setEntryCount(entryCount);
		}

		return queues;
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
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
	
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public ProcessingQueue getQueueById(Integer id){
		ProcessingQueue queue = queueDao.getQueueById(id);
		return queue;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void processQueue(ProcessingQueue queue) throws BaseException {
		List<QueueEntry> entries = getEntriesByEntryState(queue, Constants.QueueConstants.ENTRY_STATE_READY);
		for(QueueEntry entry : entries){
			logger.info("processing entry:" + entry);
			String targetClass = entry.getTargetClass();
			String targetMethod = entry.getTargetMethod();
			String arguments = entry.getArguments();
			try {
				Class<?> clazz =  Class.forName(targetClass);
				Class<?>[] interfaces = clazz.getInterfaces();
				Class<?> beanType = null;
				for(Class<?> itf : interfaces){
					Method tempMethod = clazz.getMethod(targetMethod, String.class);
					if(tempMethod != null){//这里应该找到直接接口
						beanType = itf;
						break;
					}
				}
				Object invokeTester = SpringContextUtil.getBean(beanType);//只能从spring容器获取实例，否则自动装配的bean不会实例化
				Method method = clazz.getMethod(targetMethod, String.class);
				method.invoke(invokeTester, arguments);
			//	Method method = ReflectionUtils.findMethod(clazz,targetMethod,String.class);
			//	ReflectionUtils.invokeMethod(method,invokeTester, arguments);
 			} catch (Exception e) {
 				e.printStackTrace();
				throw new BaseException(e);
			}
			
			entry.setEntryState(Constants.QueueConstants.ENTRY_STATE_CLOSED);
			updateEntry(entry);
			
			Integer interval = queue.getExecuteInterval();
			try {
				logger.info("sleep period:" + interval);
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				throw new BaseException(e);
			}
		}
	}
	
	public void updateEntry(QueueEntry entry){
		Boolean delete = entry.getDeleteWhenExecuted();
		if(delete){
			logger.info("deleting entry:" + entry);
			entryDao.delete(entry);
		}else{
			entryDao.save(entry);
			logger.info("update entry:" + entry);
		}
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

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Long getEntryCount(ProcessingQueue queue) {
		return entryDao.getQueueEntryCountByQueueRef(queue);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<QueueEntry> getEntriesByEntryState(ProcessingQueue queue, String entryState){
		return entryDao.getEntriesByEntryState(queue, entryState);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Page<QueueEntry> getEntriesByPage(Integer queueId, Integer page, Integer limit){
		page = page>0?page-1:page;
		Pageable pageable = new PageRequest(page-1, limit);
		Page<QueueEntry> entries = entryDao.findAll(pageable);
		
		return entries;
	}

}
