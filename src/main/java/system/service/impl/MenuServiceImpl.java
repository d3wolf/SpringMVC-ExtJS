package system.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import system.dao.MenuDao;
import system.model.MenuNode;
import system.service.MenuService;

import common.Constants;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuJpaDao;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<MenuNode> getRootNodesByText(String text) {
		List<MenuNode> nodes = menuJpaDao.getMenuNodeByText(text);
		return nodes;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<MenuNode> getChildNodes(Integer parentId) {
		MenuNode parent = menuJpaDao.getMenuNodeById(parentId);
		List<MenuNode> children = menuJpaDao.getMenuNodeByParent(parent);
		for (MenuNode child : children) {
			if (nodeHasChildren(child)) {
				child.setLeaf(false);
			} else {
				child.setLeaf(true);
			}
		}
		return children;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public MenuNode findNodeById(Integer id) {
		return menuJpaDao.getMenuNodeById(id);
	}

	public MenuNode createChildNode(Integer parentId) {
		MenuNode parent = menuJpaDao.getMenuNodeById(parentId);
		MenuNode child = MenuNode.newMenuNode(parent.getText() + "-child", parent.getInternal() + "-child", "", parent);
		menuJpaDao.save(child);
		return child;
	}

	public boolean deleteNodeAndChildren(Integer id) {
		if (id == 1) {
			return false;
		}
		MenuNode parent = menuJpaDao.getMenuNodeById(id);
		if (parent != null) {
			deleteNodeAndChildren(parent);
		}

		return true;
	}

	/**
	 * 递归移除当前节点和子节点以及子节点的子节点
	 */
	private void deleteNodeAndChildren(MenuNode node) {
		List<MenuNode> children = menuJpaDao.getMenuNodeByParent(node);
		for (MenuNode child : children) {
			deleteNodeAndChildren(child);
		}
		menuJpaDao.delete(node);
	}

	public boolean updateNodeAttribute(Integer id, Map<String, Object> params) {
		MenuNode node = menuJpaDao.getMenuNodeById(id);
		if (StringUtils.isNotEmpty((String) params.get(Constants.MenuConstants.TEXT))) {
			node.setText((String) params.get(Constants.MenuConstants.TEXT));
		}
		if (StringUtils.isNotEmpty((String) params.get(Constants.MenuConstants.INTERNAL))) {
			node.setInternal((String) params.get(Constants.MenuConstants.INTERNAL));
		}
		if (StringUtils.isNotEmpty((String) params.get(Constants.MenuConstants.ACTION_URL))) {
			node.setActionUrl((String) params.get(Constants.MenuConstants.ACTION_URL));
		}
		// menuDao.executeHql("update MenuNode node set node.display=?", new
		// Object[]{});
		menuJpaDao.save(node);
		return true;
	}

	/**
	 * node有子节点
	 * 
	 * @param node
	 * @return
	 */
	private boolean nodeHasChildren(MenuNode node) {
		List<MenuNode> children = menuJpaDao.getMenuNodeByParent(node);
		return children.size() > 0;
	}

}
