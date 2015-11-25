package wechat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wechat.service.MessageService;
import wechat.service.SignService;
import wechat.service.WXCommonService;

@Controller
public class WXMainController {

	private static final Logger logger = Logger.getLogger(WXMainController.class.getName());

	@Autowired
	private SignService signService;

	@Autowired
	private MessageService msgService;

	@RequestMapping(value = { "/wechat.do" }, method = RequestMethod.GET)
	public void coreJoinGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			// 微信加密签名
			String signature = request.getParameter("signature");
			// 时间戳
			String timestamp = request.getParameter("timestamp");
			// 随机数
			String nonce = request.getParameter("nonce");
			// 随机字符串
			String echostr = request.getParameter("echostr");

			logger.info("收到验证请求：");
			logger.info("　　signature=" + signature);
			logger.info("　　timestamp=" + timestamp);
			logger.info("　　nonce=" + nonce);
			logger.info("　　echostr=" + echostr);

			PrintWriter out = response.getWriter();
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
			if (signService.checkSignature(signature, timestamp, nonce)) {
				out.print(echostr);
			}else{
				logger.info("该请求不是来自微信");
			}
			out.close();
			out = null;

		} catch (Exception e) {
		}
	}

	@Autowired
	private WXCommonService commonService;

	@RequestMapping(value = { "/wechat.do" }, method = RequestMethod.POST)
	public void coreJoinPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		String replyTextMsg = "";
		try {
			Map<String, String> map = msgService.getRequestMsg(request);
			String content = map.get("Content");
			logger.info(String.format("send time:%s, from:%s, massage:%s", map.get("CreateTime"), map.get("FromUserName"), content));

			String userName = map.get("FromUserName");
			String jokeMsg = commonService.getNextJokeByUserName(userName);

			Map<String, String> replyMap = new HashMap<String, String>();
			replyMap.put("ToUserName", map.get("FromUserName"));
			replyMap.put("FromUserName", map.get("ToUserName"));
			replyMap.put("CreateTime", "" + System.currentTimeMillis());
			replyMap.put("MsgType", "text");
			replyMap.put("Content", jokeMsg);

			replyTextMsg = msgService.constructReplyText(replyMap);

		} catch (Exception e1) {
			replyTextMsg = "";
			e1.printStackTrace();
		}
		logger.debug("replyTextMsg: " + replyTextMsg);
		out.print(replyTextMsg);

	}

}
