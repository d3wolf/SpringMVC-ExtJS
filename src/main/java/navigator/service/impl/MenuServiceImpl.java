package navigator.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import navigator.model.MenuNode;
import navigator.service.MenuService;
import common.Constants;
import common.dao.BaseDao;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private BaseDao<MenuNode> menuDao;

	@Transactional(rollbackFor = Exception.class)
	public List<MenuNode> getRootNodesByText(String text) {
		List<MenuNode> nodes = menuDao.find("from MenuNode where text=?", new Object[] { text });
		return nodes;
	}

	@Transactional(rollbackFor = Exception.class)
	public List<MenuNode> getChildNodes(Integer parentId) {
		MenuNode parent = findSingleNode(parentId);
		List<MenuNode> children = menuDao.find("from MenuNode node where node.parent=?", new Object[] { parent });
		for(MenuNode child : children){
			if(nodeHasChildren(child)){
				child.setLeaf(false);
			}else{
				child.setLeaf(true);
			}
		}
		return children;
	}

	@Transactional(rollbackFor = Exception.class)
	public MenuNode findNodeById(Integer id) {
		return findSingleNode(id);
	}

	/**
	 * 用来查找的单个对象
	 * @param id
	 * @return
	 */
	private MenuNode findSingleNode(Integer id) {
		List<MenuNode> nodes = menuDao.find("from MenuNode where id=?", new Object[] { id });

		MenuNode node = null;
		if (nodes.size() > 0) {
			node = nodes.get(0);
		}
		return node;
	}

	@Transactional(rollbackFor = Exception.class)
	public MenuNode createChildNode(Integer parentId) {
		MenuNode parent = findSingleNode(parentId);
		MenuNode child = MenuNode.newMenuNode(parent.getText() + "-child", parent.getInternal() + "-child", "", parent);
		menuDao.save(child);
		return child;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean deleteNodeAndChildren(Integer id) {
		if(id==1){
			return false;
		}
		MenuNode parent = findSingleNode(id);
		if(parent != null){
			deleteNodeAndChildren(parent);
		}
		
		return true;
	}
	
	/**
	 * 递归移除当前节点和子节点以及子节点的子节点
	 */
	private void deleteNodeAndChildren(MenuNode node){
		List<MenuNode> children = menuDao.find("from MenuNode node where node.parent=?", new Object[] { node });
		for(MenuNode child : children){
			deleteNodeAndChildren(child);
		}
		menuDao.delete(node);
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean updateNodeAttribute(Integer id,Map<String, Object> params) {
		MenuNode node = findSingleNode(id);
		if(StringUtils.isNotEmpty((String) params.get(Constants.MenuConstants.TEXT))){
			node.setText((String) params.get(Constants.MenuConstants.TEXT));
		}
		if(StringUtils.isNotEmpty((String) params.get(Constants.MenuConstants.INTERNAL))){
			node.setInternal((String) params.get(Constants.MenuConstants.INTERNAL));
		}
		if(StringUtils.isNotEmpty((String) params.get(Constants.MenuConstants.ACTION_URL))){
			node.setActionUrl((String) params.get(Constants.MenuConstants.ACTION_URL));
		}
	//	menuDao.executeHql("update MenuNode node set node.display=?", new Object[]{});
		menuDao.update(node);
		return true;
	}

	/**
	 * node有子节点
	 * @param node
	 * @return
	 */
	private boolean nodeHasChildren(MenuNode node) {
		List<MenuNode> children = menuDao.find("from MenuNode node where node.parent=?", new Object[] { node });
		return children.size()>0;
	}

}
