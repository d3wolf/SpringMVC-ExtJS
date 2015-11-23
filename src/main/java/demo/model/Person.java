package demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import common.model.BaseObject;

@Entity
@Table(name = "person")
@Inheritance
public class Person extends BaseObject{

	/**
	 * 当查询的时候返回的实体类是一个对象实例，是hibernate动态通过反射生成的反射的Class.forName("className").
	 * newInstance();需要对应的类提供一个无参构造函数;否则报错org.hibernate.InstantiationException: No default constructor for entity: demo.model.Person
	 */
	public Person() {
	}

	public Person(String name) {
		this.name2 = name;
	}

	@Column
	private String name2;

}
