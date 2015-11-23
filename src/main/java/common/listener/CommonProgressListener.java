package common.listener;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class CommonProgressListener {
	public void test(){
		
		ServletFileUpload upload = new ServletFileUpload();
		ProgressListener a = null;
		upload.setProgressListener(a);
	}
}
