package spider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import spider.model.Joke;
import spider.service.JokeService;

/**
 * 主要用来显示spider抓取结果的controller
 * @author Wolf
 *
 */
@Controller
public class SpiderResultController {

	@Autowired
	private JokeService jokeService;
	
	@RequestMapping("showJoke.do")
	public String getShowJokePage(){
		
		return "spider/showJoke";
	}
	
	@RequestMapping("/joke/show.do")
	@ResponseBody//返回json
	public List<Joke> showJoke(@RequestParam("page")Integer page){
		List<Joke> jokes = jokeService.getJokeFromDB(page);
		
		return jokes;
	}
}
