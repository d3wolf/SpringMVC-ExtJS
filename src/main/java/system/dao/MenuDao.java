package system.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import system.model.MenuNode;

public interface MenuDao extends CrudRepository<MenuNode, Integer>{

	public List<MenuNode> getMenuNodeByText(String text);
	
	public MenuNode getMenuNodeById(Integer id);
	
	public List<MenuNode> getMenuNodeByParent(MenuNode parent);
}
