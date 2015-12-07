package common;

public class BaseException extends Exception {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5067408662058478582L;

	public BaseException() {
		super();
	}

	public BaseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BaseException(String arg0) {
		super(arg0);
	}

	public BaseException(Throwable arg0) {
		super(arg0);
	}
}
