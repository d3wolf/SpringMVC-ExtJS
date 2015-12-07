package common.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import common.BaseException;

/**
 * 所有模型的超类
 */
@MappedSuperclass
public abstract class BaseObject {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Version
	@Autowired
	private Integer updatecount;

	private Date createTimestamp = new Date();

	private Date modefyTimestamp = new Date();

	@Column(nullable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.INSERT)
	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	@Version
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.ALWAYS)
	public Date getModefyTimestamp() {
		return modefyTimestamp;
	}

	public void setModefyTimestamp(Date modefyTimestamp) {
		this.modefyTimestamp = modefyTimestamp;
	}

	/**
	 * 返回oid(&ltClassName>:&ltid>, e.g. common.model.BaseObject:1)
	 */
	public String toString() {
		return this.getClass().getName() + ":" + id;
	}

}
