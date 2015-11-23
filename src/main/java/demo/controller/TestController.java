package demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/test.do")
public class TestController {



	@RequestMapping
	public String getNewName(@RequestParam("userName") String userName, HttpServletRequest request) {
		request.setAttribute("newUserName", "hello, "+userName);

		return "test";
	}

}
