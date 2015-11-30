package system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import common.Constants;
import common.model.BaseObject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class QueueEntry extends BaseObject {

	@ManyToOne(targetEntity = ProcessingQueue.class)
	@JoinColumn
	private ProcessingQueue queueRef;

	private String arguments;

	private String entryState = Constants.QueueConstants.ENTRY_STATE_READY;

	private String message;

	private String targetClass;

	private String targetMethod;
	
	public static QueueEntry newQueueEntry(ProcessingQueue queue, String targetClass, String targetMethod, String arguments){
		QueueEntry entry = new QueueEntry();
		entry.setQueueRef(queue);
		entry.setTargetClass(targetClass);
		entry.setTargetMethod(targetMethod);
		entry.setArguments(arguments);
		
		return entry;
	}

	@Column
	public ProcessingQueue getQueueRef() {
		return queueRef;
	}

	public void setQueueRef(ProcessingQueue queueRef) {
		this.queueRef = queueRef;
	}

	@Column
	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	@Column
	public String getEntryState() {
		return entryState;
	}

	public void setEntryState(String entryState) {
		this.entryState = entryState;
	}

	@Column
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column
	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	@Column
	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

}
