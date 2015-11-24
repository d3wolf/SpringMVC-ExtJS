package wechat.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import wechat.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	public Map<String, String> getRequestMsg(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		InputStream is = request.getInputStream();
		Document doc = reader.read(is);
		Element root = doc.getRootElement();
		
		@SuppressWarnings("unchecked")
		List<Element> llist = root.elements();
		for (Element e : llist) {
			map.put(e.getName(), e.getText());
		}
		is.close();
		return map;
	}

	public String constructReplyText(Map<String, String> map) {
		Element root = DocumentHelper.createElement("xml");
		Document document = DocumentHelper.createDocument(root);

		Element ToUserName = root.addElement("FromUserName");
		ToUserName.addCDATA(map.get("FromUserName"));

		Element FromUserName = root.addElement("ToUserName");
		FromUserName.addCDATA(map.get("ToUserName"));

		Element CreateTime = root.addElement("CreateTime");
		CreateTime.addCDATA(map.get("CreateTime"));

		Element MsgType = root.addElement("MsgType");
		MsgType.addCDATA(map.get("MsgType"));

		Element Content = root.addElement("Content");
		Content.addCDATA(map.get("Content"));

		return document.asXML();
	}

}
