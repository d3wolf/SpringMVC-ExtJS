package common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 所有模型的超类
 */
@MappedSuperclass
public class BaseObject {

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
	
	public String toString(){
		return this.getClass().getName() + ":" + id;
	}
}
