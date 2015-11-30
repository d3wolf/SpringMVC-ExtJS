package system.dao;

import java.util.List;

import system.model.ProcessingQueue;

public interface QueueDaoBase {

	public ProcessingQueue getQueue(String name);
	
	public List<ProcessingQueue> getAllQueue();
	
	public ProcessingQueue createQueue(String name);

}