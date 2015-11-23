package system.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

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

	@RequestMapping("getLevel.do")
	public void getTargetLogLevel(@RequestParam("target") String target, HttpServletResponse response) throws IOException {
		String levelStr = log4jService.getLevelStrByTarget(target);

		JSONObject jo = new JSONObject();
		jo.put("success", true);
		jo.put("levelStr", levelStr);

		logger.debug("get level:[" + target + "] -> " + jo);

		response.setContentType("text/html;charset=UTF-8");// 处理乱码
		response.getWriter().write(jo.toString());
	}

	@RequestMapping("setLevel.do")
	public void setTargetLogLevel(@RequestParam("target") String target, @RequestParam("levelStr") String levelStr, HttpServletResponse response)
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
