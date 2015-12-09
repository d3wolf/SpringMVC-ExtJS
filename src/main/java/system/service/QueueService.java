package system.service;

import java.util.List;

import org.springframework.data.domain.Page;

import system.model.ProcessingQueue;
import system.model.QueueEntry;
import common.BaseException;

public interface QueueService {

	/**
	 * 获取队列，没有就创建
	 * 
	 * @param name
	 * @return
	 */
	public ProcessingQueue getOrCreateQueue(String name);

	/**
	 * 获取所有队列
	 * 
	 * @return
	 */
	public List<ProcessingQueue> getAllQueue();

	/**
	 * 创建队列
	 * 
	 * @param name
	 * @return
	 */
	public ProcessingQueue createQueue(String name);

	/**
	 * 根据名称获取队列
	 * 
	 * @param name
	 * @return
	 */
	public ProcessingQueue getQueueByName(String name);

	/**
	 * 执行队列
	 * 
	 * @param queue
	 */
	public void processQueue(ProcessingQueue queue) throws BaseException;

	/**
	 * 增加队列条目
	 * 
	 * @param queueName
	 * @param targetClass
	 * @param targetMethod
	 * @param arguments
	 * @return
	 */
	public QueueEntry addEntry(String queueName, String targetClass, String targetMethod, String arguments);

	/**
	 * 增加队列条目
	 * 
	 * @param queue
	 * @param targetClass
	 * @param targetMethod
	 * @param arguments
	 * @return
	 */
	public QueueEntry addEntry(ProcessingQueue queue, String targetClass, String targetMethod, String arguments);

	/**
	 * 获取队列条目
	 * 
	 * @param 条目id
	 * @return
	 */
	public QueueEntry getEntry(Integer id);

	/**
	 * 根据队列id获取所有条目
	 * 
	 * @param queueId
	 * @return
	 */
	public List<QueueEntry> getEntriesByQueueId(Integer queueId);

	/**
	 * 获取队列条目数
	 * 
	 * @param queue
	 * @return
	 */
	public Long getEntryCount(ProcessingQueue queue);

	/**
	 * 更改队条目
	 * 
	 * @param entry
	 * @return
	 */
	public QueueEntry resetEntry(QueueEntry entry);

	/**
	 * 删除队列和条目
	 * 
	 * @param queueId
	 * @return
	 */
	public Integer deleteQueueAndEntries(Integer queueId);

	/**
	 * 根据状态获取条目
	 * 
	 * @param queue
	 * @return
	 */
	public List<QueueEntry> getEntriesByEntryState(ProcessingQueue queue, String entryState);

	/**
	 * 根据id获取队列
	 * @param id
	 * @return
	 */
	public ProcessingQueue getQueueById(Integer id);
	
	/**
	 * 删除或更新条目
	 * @param entry
	 */
	public void updateEntry(QueueEntry entry);
	
	/**
	 * 分页查询条目
	 * @param queueId
	 * @param page
	 * @param limit
	 * @return
	 */
	public Page<QueueEntry> getEntriesByPage(Integer queueId, Integer page, Integer limit);
}
