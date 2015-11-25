package wechat.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface MessageService {

	/**
	 * 从request中获取信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, String> getRequestMsg(HttpServletRequest request) throws Exception;

	/**
	 * 构造回复text消息
	 * <?xml version="1.0" encoding="UTF-8"?> <xml>
	 * <ToUserName><![CDATA[gh_93cb47ad258c]]></ToUserName>
	 * <FromUserName><![CDATA[on7l3wAlOj0LzOozq6PxFfn84MZI]]></FromUserName>
	 * <CreateTime><![CDATA[1442912458]]></CreateTime>
	 * <MsgType><![CDATA[text]]></MsgType>
	 * <Content><![CDATA[message]]></Content> </xml>
	 * 
	 * @param map
	 * @param content
	 * @return
	 */
	public abstract String constructReplyText(Map<String, String> map);

}