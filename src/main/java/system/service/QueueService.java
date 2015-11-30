package system.service;

import java.util.List;

import system.model.ProcessingQueue;
import system.model.QueueEntry;

public interface QueueService {
	
	/**
	 * 获取队列，没有就创建
	 * @param name
	 * @return
	 */
	public ProcessingQueue getOrCreateQueue(String name);
	/**
	 * 根据名字获取队列
	 * @param name
	 * @return
	 */
	public ProcessingQueue getQueue(String name);
	
	/**
	 * 获取所有队列
	 * @return
	 */
	public List<ProcessingQueue> getAllQueue();
	
	/**
	 * 创建队列
	 * @param name
	 * @return
	 */
	public ProcessingQueue createQueue(String name);
	
	/**
	 * 执行队列
	 * @param queue
	 */
	public void processQueue(ProcessingQueue queue);
	
	/**
	 * 增加队列条目
	 * @param queueName
	 * @param targetClass
	 * @param targetMethod
	 * @param arguments
	 * @return
	 */
	public Boolean addEntry(String queueName, String targetClass, String targetMethod, String[] arguments);
	
	/**
	 * 增加队列条目
	 * @param queue
	 * @param targetClass
	 * @param targetMethod
	 * @param arguments
	 * @return
	 */
	public Boolean addEntry(ProcessingQueue queue, String targetClass, String targetMethod, String[] arguments);
	
	/**
	 * 获取队列条目
	 * @param 条目id
	 * @return
	 */
	public QueueEntry getEntry(Integer id);
	
	/**
	 * 获取队列条目数
	 * @param queue
	 * @return
	 */
	public Long getEntryCount(ProcessingQueue queue);
	
	/**
	 * 更改队条目
	 * @param entry
	 * @return
	 */
	public QueueEntry resetEntry(QueueEntry entry);

}
