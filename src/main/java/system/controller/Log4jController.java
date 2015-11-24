package system.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import system.service.Log4jService;

@Controller
@RequestMapping("/log")
public class Log4jController {

	private static final Logger logger = Logger.getLogger(Log4jController.class.getName());

	@RequestMapping("manage.do")
	public String getLogMngPage() {

		return "system/log4jMng";
	}

	@Autowired
	private Log4jService log4jService;
	
	
	@RequestMapping(value="getTarget.do")
	public void getTargets(HttpServletResponse response) throws IOException, Exception{
		List<String> list = log4jService.getCurrentLoggerNames();
		
		JSONArray ja = new JSONArray();
		for(String name : list){
			JSONObject jo = new JSONObject();
			jo.put("id", name);
			jo.put("name", name);
			ja.add(jo);
		}
		
		JSONObject targets = new JSONObject();
		targets.put("root", ja);
		
		response.setContentType("text/html;charset=UTF-8");//处理乱码
		response.getWriter().write(targets.toString());
	}

	@RequestMapping("getLevel.do")
	public void getTargetLogLevel(@RequestParam(value="target", required=true) String target, HttpServletResponse response) throws Exception {
		String levelStr = log4jService.getLevelStrByTarget(target);

		JSONObject jo = new JSONObject();
		jo.put("success", true);
		jo.put("levelStr", levelStr);

		logger.debug("get level:[" + target + "] -> " + jo);

		response.setContentType("text/html;charset=UTF-8");// 处理乱码
		response.getWriter().write(jo.toString());
	}

	@RequestMapping("setLevel.do")
	public void setTargetLogLevel(@RequestParam(value="target", required=true) String target, @RequestParam(value="levelStr", required=true) String levelStr, HttpServletResponse response)
			throws Exception {
		Level level = log4jService.getLevelByLevelStr(levelStr);
		log4jService.setTargetLevel(target, level);

		JSONObject jo = new JSONObject();
		jo.put("success", true);
		jo.put("msg", "set " + levelStr + " ok");

		logger.debug("set level:[" + target + "] -> " + jo);

		response.setContentType("text/html;charset=UTF-8");// 处理乱码
		response.getWriter().write(jo.toString());
	}

}
