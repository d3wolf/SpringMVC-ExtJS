package demo.service.impl;

import java.io.File;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import demo.service.PCDMappingService;

@Service
public class PCDMappingServiceImpl implements PCDMappingService {

	private static final Logger logger = Logger.getLogger(PCDMappingServiceImpl.class.getName());

	private static File file = null;
	private static Document doc = null;
	private static Long FILE_MODIFY_TIME = 0L;

	static {
		file = new File(PCDMappingServiceImpl.class.getClassLoader().getResource("demo/service/impl/PCD.xml").getPath().replace("%20", " "));
		logger.debug("find PCD.xml file:"+file.exists());
		// System.out.println("path: " + path);
	}

	/**
	 * 检查文件是否有更新
	 * 
	 * @return
	 */
	private static boolean isUpdateFile() {
		long lastModifyTime = file.lastModified();
		if (lastModifyTime > FILE_MODIFY_TIME) {
			FILE_MODIFY_TIME = lastModifyTime;
			return true;
		}
		return false;
	}

	/**
	 * 加载测试管理配置文件返回Document XML对象，有更新则再次读取文件，无更新则取现有缓存
	 * 
	 * @return
	 */
	public static Document loadFailureModeReasonXML() {
		SAXReader reader = new SAXReader();
		if (isUpdateFile() || doc == null) {
			try {
				doc = reader.read(file);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}

		return doc;
	}

	public String getAllProvinces() throws JSONException {
		logger.debug("getAllProvinces");
		Document doc = loadFailureModeReasonXML();
		String xpath = "/PCD/Provinces/Province";
		
		@SuppressWarnings("unchecked")
		List<Element> partClsEls = doc.selectNodes(xpath);

		JSONObject allInfo = new JSONObject();

		JSONArray array = new JSONArray();
		for (Element pEl : partClsEls) {
			String id = pEl.attributeValue("ID");
			String name = pEl.attributeValue("ProvinceName");
			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("name", name);

			array.add(jo);
			// System.out.println("abc:"+pEl.attributeValue("ProvinceName"));
		}
		allInfo.put("province", array);

		logger.debug(allInfo);

		return allInfo.toString();
	}

	public String getCitiesByProvince(String pid) {
		logger.debug("get cities by pid: " + pid);
		
		JSONObject allInfo = new JSONObject();
		if("".equals(pid) || pid == null){
			allInfo.put("city", new JSONArray());
			return allInfo.toString();
		}
		Document doc = loadFailureModeReasonXML();
		String xpath = String.format("/PCD/Cities/City[@PID=%s]", pid);

		@SuppressWarnings("unchecked")
		List<Element> cEls = doc.selectNodes(xpath);


		JSONArray array = new JSONArray();
		for (Element cEl : cEls) {
			String id = cEl.attributeValue("ID");
			String name = cEl.attributeValue("CityName");
			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("name", name);

			array.add(jo);
		}
		allInfo.put("city", array);

		logger.debug(allInfo);

		return allInfo.toString();
	}

	public String getDistrictsByCity(String cid) {
		logger.debug("get districts by String cid: " + cid);
		
		JSONObject allInfo = new JSONObject();
		if("".equals(cid) || cid == null){
			return allInfo.toString();
		}
		Document doc = loadFailureModeReasonXML();
		String xpath = String.format("/PCD/Districts/District[@CID=%s]", cid);

		@SuppressWarnings("unchecked")
		List<Element> cEls = doc.selectNodes(xpath);

		JSONArray array = new JSONArray();
		for (Element cEl : cEls) {
			String id = cEl.attributeValue("ID");
			String name = cEl.attributeValue("DistrictName");
			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("name", name);

			array.add(jo);
		}
		allInfo.put("district", array);

		logger.debug(allInfo);

		return allInfo.toString();
	}

}
