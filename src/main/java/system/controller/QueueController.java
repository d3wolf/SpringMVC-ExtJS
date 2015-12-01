package system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import system.model.ProcessingQueue;
import system.model.QueueEntry;
import system.service.QueueService;

@Controller
@RequestMapping("/queue")
public class QueueController {

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
