package system.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import system.model.MenuNode;
import system.service.MenuService;
import common.Constants;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping("manage.do")
	public String manageMenu(){
		
		return "system/menuMng";
	}
	
	@RequestMapping("root.do")
	@ResponseBody//返回json
	public List<MenuNode> getRootNodes(){
		List<MenuNode> nodes = menuService.getRootNodesByText("test");
		
		return nodes;
	}
	
	@RequestMapping("child.do")
	@ResponseBody//返回json
	public List<MenuNode> getChildNodes(@RequestParam("node")Integer id,HttpServletResponse response,HttpServletRequest request) throws IOException{
		List<MenuNode> childNodes = menuService.getChildNodes(id);
		return childNodes;
	}
	
	@RequestMapping("add.do")
	@ResponseBody
	public MenuNode addChildNode(@RequestParam("id")Integer id){
		MenuNode child = menuService.createChildNode(id);

		return child;
	}
	
	@RequestMapping("remove.do")
	@ResponseBody
	public MenuNode deleteNodeAndChildren(@RequestParam("id")Integer id,HttpServletResponse response){
		boolean removed = menuService.deleteNodeAndChildren(id);
		if(!removed){
			throw new IllegalArgumentException("不能移除顶层节点");
		}
		return null;
	}
	
	@RequestMapping("save.do")
	@ResponseBody
	public MenuNode saveNodeAttribute(@RequestParam("id")Integer id,String text, String internal, String actionUrl){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.MenuConstants.TEXT, text);
		map.put(Constants.MenuConstants.INTERNAL, internal);
		map.put(Constants.MenuConstants.ACTION_URL, actionUrl);
		
		menuService.updateNodeAttribute(id, map);

		return null;
	}
	

}
