package navigator.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import common.model.BaseObject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class MenuNode extends BaseObject {

	@ManyToOne(targetEntity = MenuNode.class)
	@JoinColumn
	private MenuNode parent;

	//该属性不会数据持久化
	@Transient  
	private boolean leaf;
	
	private String text;
	private String internal;
	private String iconCls;
	private String actionUrl;
	
	public static MenuNode newMenuNode(String text, String internal, String actionUrl, MenuNode parent){
		MenuNode  menuNode= new MenuNode();
		menuNode.setText(text);
		menuNode.setInternal(internal);
		menuNode.setParent(parent);
		menuNode.setActionUrl(actionUrl);
	//	menuNode.setLeaf(isLeaf);
		
		return menuNode;
	}

	@Column
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column
	public String getInternal() {
		return internal;
	}

	public void setInternal(String internal) {
		this.internal = internal;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	@Column
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	@Column
	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public MenuNode getParent() {
		return parent;
	}

	public void setParent(MenuNode parent) {
		this.parent = parent;
	}

}
