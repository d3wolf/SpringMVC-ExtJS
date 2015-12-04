package spider.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spider.model.Joke;
import spider.service.DoubanMovieListService;
import spider.service.JokeService;
import common.Constants;

@Controller
@RequestMapping("/spider")
public class SpiderController {

	private static final Logger logger = Logger.getLogger(SpiderController.class.getName());

	@Autowired
	private JokeService jokeService;

	@RequestMapping("main.do")
	private String getSpiderMainPage() {
		return "spider/main";
	}

	@RequestMapping("joke.do")
	public void getJokesByPage(@RequestParam("urlIndex") String urlIndex, HttpServletResponse response) throws IOException {
		List<Joke> jokes = jokeService.getJokeBy3rd(urlIndex);
		logger.debug("找到Joke条数:" + jokes.size());
		int count = jokeService.persistJoke(jokes);
		logger.debug("保存Joke条数:" + count);

		JSONObject jo = new JSONObject();
		jo.put("find", jokes.size());
		jo.put("save", count);

		response.setContentType("text/html;charset=UTF-8");// 处理乱码
		response.getWriter().write(jo.toString());
	}

	@RequestMapping("jokes.do")
	public void getJokesByPages(@RequestParam("urlIndex") int urlIndex, HttpServletRequest request, HttpServletResponse response) throws IOException {

		JSONArray ja = new JSONArray();
		for (int i = 1; i <= urlIndex; i++) {
			//需要加入进度条的参数
			float processPercentage = (float)i / (float)urlIndex;
			request.getSession().setAttribute(Constants.ProcessConstants.processPercentageAttribute, processPercentage);
			request.getSession().setAttribute(Constants.ProcessConstants.processMessageAttribute, "processing " + i + " of " + urlIndex);

			List<Joke> jokes = jokeService.getJokeBy3rd("" + i);
			logger.debug("找到Joke条数:" + jokes.size());
			int count = jokeService.persistJoke(jokes);
			logger.debug("保存Joke条数:" + count);

			JSONObject jo = new JSONObject();
			jo.put("find", jokes.size());
			jo.put("save", count);
			jo.put("page", i);

			ja.add(jo);
		}
		response.setContentType("text/html;charset=UTF-8");// 处理乱码
		response.getWriter().write(ja.toString());
	}

	@RequestMapping("process.do")
	public void getProcessInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Float processPercentage = (Float) request.getSession().getAttribute(Constants.ProcessConstants.processPercentageAttribute);
		Object processMessage = request.getSession().getAttribute(Constants.ProcessConstants.processMessageAttribute);
		boolean finished = false;
		if(processPercentage != null){
			if (processPercentage >= 1) {
				finished = true;
				request.getSession().removeAttribute(Constants.ProcessConstants.processPercentageAttribute);  
	            request.getSession().removeAttribute(Constants.ProcessConstants.processMessageAttribute);
			}
			
			JSONObject jo = new JSONObject();
			jo.put("percentage", processPercentage);
			jo.put("msg", processMessage);
			jo.put("finished", finished);
			jo.put("success", true);
			
			logger.debug("process status: " + jo);
			
			response.setContentType("text/html;charset=UTF-8");// 处理乱码
			response.getWriter().write(jo.toString());
		}else{
			request.getSession().removeAttribute(Constants.ProcessConstants.processPercentageAttribute);  
            request.getSession().removeAttribute(Constants.ProcessConstants.processMessageAttribute);	
		}
	}
	
	@Autowired
	private DoubanMovieListService movieListService;
	
	@RequestMapping("movieList.do")
	public void getMovieListByPages(@RequestParam("urlIndex") int urlIndex, HttpServletRequest request, HttpServletResponse response) throws IOException {

		for (int i = 1; i <= urlIndex; i++) {
			//需要加入进度条的参数
			request.getSession().setAttribute("Movie_"+Constants.ProcessConstants.processPercentageAttribute, 0f);
			request.getSession().setAttribute("Movie_"+Constants.ProcessConstants.processMessageAttribute, "processing " + i + " of " + urlIndex);

			movieListService.parseMovieList(""+urlIndex);
			
			float processPercentage = (float)i / (float)urlIndex;
			request.getSession().setAttribute("Movie_"+Constants.ProcessConstants.processPercentageAttribute, processPercentage);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@RequestMapping("processMovieList.do")
	public void getMovieListProcessInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Float processPercentage = (Float) request.getSession().getAttribute("Movie_"+Constants.ProcessConstants.processPercentageAttribute);
		Object processMessage = request.getSession().getAttribute("Movie_"+Constants.ProcessConstants.processMessageAttribute);
		boolean finished = false;
		if(processPercentage != null){
			if (processPercentage >= 1) {
				finished = true;
				request.getSession().removeAttribute("Movie_"+Constants.ProcessConstants.processPercentageAttribute);  
	            request.getSession().removeAttribute("Movie_"+Constants.ProcessConstants.processMessageAttribute);
			}
			
			JSONObject jo = new JSONObject();
			jo.put("percentage", processPercentage);
			jo.put("msg", processMessage);
			jo.put("finished", finished);
			jo.put("success", true);
			
			logger.debug("process status: " + jo);
			
			response.setContentType("text/html;charset=UTF-8");// 处理乱码
			response.getWriter().write(jo.toString());
		}else{
			request.getSession().removeAttribute("Movie_"+Constants.ProcessConstants.processPercentageAttribute);  
            request.getSession().removeAttribute("Movie_"+Constants.ProcessConstants.processMessageAttribute);	
		}
	}
}
