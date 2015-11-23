package navigator.service;

import java.util.List;
import java.util.Map;

import navigator.model.MenuNode;

public interface MenuService {

	/**
	 * 根据id找节点
	 * @param id
	 * @return
	 */
	public MenuNode findNodeById(Integer id);
	
	/**
	 * 根据text属性找节点
	 * @param text
	 * @return
	 */
	public List<MenuNode> getRootNodesByText(String text);
	
	/**
	 * 获取当前id所有子节点
	 * @param parent
	 * @return
	 */
	public List<MenuNode> getChildNodes(Integer parent);
	
	/**
	 * 新增子节点
	 * @param parent
	 * @return
	 */
	public MenuNode createChildNode(Integer parent);
	
	/**
	 * 移除当前节点和所有子节点
	 * @param id
	 * @return
	 */
	public boolean deleteNodeAndChildren(Integer id);
	
	/**
	 * 更新node属性
	 * @param id
	 * @return
	 */
	public boolean updateNodeAttribute(Integer id, Map<String, Object> params);
	
}
