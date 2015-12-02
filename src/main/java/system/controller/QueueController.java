package system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import system.model.ProcessingQueue;
import system.model.QueueEntry;
import system.service.QueueService;
import system.service.impl.QueueServiceImpl;

@Controller
@RequestMapping("/queue")
public class QueueController {

	private static final Logger logger = Logger.getLogger(QueueController.class.getName());
	
	@RequestMapping("manage.do")
	public String getQueueManageHome() {
		return "system/queueMng";
	}
	
	@RequestMapping("detail.do")
	public String showQueueEntryManage(){
		return "system/queueDetailMng";
	}
	
	@Autowired
	private QueueService queueService;
	
	@RequestMapping("createQueue.do")
	@ResponseBody
	public Map<String, Object> createQueue(@RequestParam("name")String name){
		Map<String,Object> map = new HashMap<String,Object>();  
		
		ProcessingQueue queue = queueService.getQueueByName(name);
		if(queue != null){
			map.put("failure", "true");
			map.put("msg", "无法创建队列，名称已经存在");
		}else{
			queue = queueService.createQueue(name);
			map.put("success", "true");
			map.put("msg", queue);
		}
		
		logger.info("createQueue: " + map);
		
		return map;
	}
	
	@RequestMapping("deleteQueue.do")
	@ResponseBody
	public Map<String, Object> deleteQueue(@RequestParam("id")Integer id){
		Map<String,Object> map = new HashMap<String,Object>();  
		
		Integer deletedEntries = queueService.deleteQueueAndEntries(id);
	
		map.put("success", "true");
		map.put("msg", "删除条目:"+deletedEntries);
		
		logger.info("createQueue: " + map);
		
		return map;
	}
	
	@RequestMapping("getQueues.do")
	@ResponseBody
	public Map<String, Object> getQueues(){
		List<ProcessingQueue> queues = queueService.getAllQueue();
		Map<String,Object> map = new HashMap<String,Object>();  
        map.put("rows", queues);
        map.put("total", queues.size());
        
        return map;
	}
	
	@RequestMapping("getQueueEntries.do")
	@ResponseBody
	public Map<String, Object> getQueueEntries(@RequestParam("id")Integer id){
		List<QueueEntry> entries = queueService.getEntriesByQueueId(id);
		Map<String,Object> map = new HashMap<String,Object>();  
        map.put("rows", entries);
        map.put("total", entries.size());
        
        return map;
	}
}
