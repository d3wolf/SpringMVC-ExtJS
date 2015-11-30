package system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import common.Constants;
import common.model.BaseObject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ProcessingQueue extends BaseObject {

	private Integer executeInterval = 200;

	private String queueState = Constants.QueueConstants.QUEUE_STATE_STARTED;

	private String name;

	private Boolean enabled = true;

	private Boolean deleteWhenEmpty = false;
	@Transient
	private Long entryCount;

	public Long getEntryCount() {
		return entryCount;
	}

	public void setEntryCount(Long entryCount) {
		this.entryCount = entryCount;
	}

	public static ProcessingQueue newProcessingQueue(String name) {
		ProcessingQueue queue = new ProcessingQueue();
		queue.setName(name);
		return queue;
	}

	@Column
	public Integer getExecuteInterval() {
		return executeInterval;
	}

	public void setExecuteInterval(Integer interval) {
		this.executeInterval = interval;
	}

	@Column
	public String getQueueState() {
		return queueState;
	}

	public void setQueueState(String queueState) {
		this.queueState = queueState;
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Column
	public Boolean getDeleteWhenEmpty() {
		return deleteWhenEmpty;
	}

	public void setDeleteWhenEmpty(Boolean deleteWhenEmpty) {
		this.deleteWhenEmpty = deleteWhenEmpty;
	}

}
