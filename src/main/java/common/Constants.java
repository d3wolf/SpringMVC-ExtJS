package common;

public class Constants {

	public static final String USER_NAME_KEY = "username";
	
	public static final class MenuConstants{
		public static final String TEXT = "text";
		public static final String INTERNAL = "internal";
		public static final String ACTION_URL = "actionUrl";
	}
	
	public static final class ProcessConstants{
		/** 进度条参数-完成百分比*/
		public static final String processPercentageAttribute = "processPercentage";
		/** 进度条参数-进度消息*/
		public static final String processMessageAttribute = "processMessage";  
	}
	
	public static final class QueueConstants{
		/** 队列状态- STARTED*/
		public static final String QUEUE_STATE_STARTED = "STARTED";
		/** 条目状态*/
		public static final String ENTRY_STATE_RUNNING = "RUNNING";
		/** 条目状态*/
		public static final String ENTRY_STATE_SEVERE = "SEVERE";
		/** 条目状态*/
		public static final String ENTRY_STATE_READY = "READY";
		/** 条目状态*/
		public static final String ENTRY_STATE_CLOSED = "CLOSED";
		
	}
}
