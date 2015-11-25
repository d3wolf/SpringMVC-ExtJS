package demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import demo.service.PCDMappingService;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@RequestMapping("combo.do")
	public String getDemoCombo() {
		return "demo/combo";
	}
	
	@Autowired
	private PCDMappingService pcdService;
	
	@RequestMapping(value="province.do")
	public void getProvince(HttpServletResponse response) throws IOException, Exception{
		String ap = (String)pcdService.getAllProvinces();
		
		response.setContentType("text/html;charset=UTF-8");//处理乱码
		response.getWriter().write(ap);
	}
	
	@RequestMapping(value="city.do")
	public void getCities(HttpServletResponse response, String pid) throws IOException, Exception{
		String ap = (String)pcdService.getCitiesByProvince(pid);
		
		response.setContentType("text/html;charset=UTF-8");//处理乱码
		response.getWriter().write(ap);
	}
	
	@RequestMapping(value="district.do")
	public void getDistricts(HttpServletResponse response, String cid) throws IOException, Exception{
		String ap = (String)pcdService.getDistrictsByCity(cid);
		
		response.setContentType("text/html;charset=UTF-8");//处理乱码
		response.getWriter().write(ap);
	}
	
	@RequestMapping("wizard.do")
	public String getDemoWizard() {
		return "demo/wizard";
	}
	
}
