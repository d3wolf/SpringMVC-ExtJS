package user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import common.model.BaseObject;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseObject {
	/**
	 * 当查询的时候返回的实体类是一个对象实例，是hibernate动态通过反射生成的反射的Class.forName("className").
	 * newInstance();需要对应的类提供一个无参构造函数;否则报错org.hibernate.InstantiationException: No default constructor for entity
	 */
	public User() {

	}

	public User(String name) {
		this.name = name;
	}

	private String name;

	@Column(name = "name", length = 10, nullable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}