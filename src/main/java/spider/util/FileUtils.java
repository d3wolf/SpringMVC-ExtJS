package spider.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {

	/**
	 * 下载网络文件
	 * 
	 * @param urlString
	 * @param filename
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static boolean download(String urlString, String filename, String savePath) throws IOException {
		boolean result = true;

		File file = new File(savePath + "\\" + filename);

	//	if (!file.exists()) {
			// 输入流
			InputStream is = null;
			OutputStream os = null;
			try {
				// 构造URL
				URL url = new URL(urlString);
				// 打开连接
				URLConnection con = url.openConnection();
				// 设置代理，否则可能403
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
				// 设置请求超时为5s
				con.setConnectTimeout(5 * 1000);
				is = con.getInputStream();

				// 1K的数据缓冲
				byte[] bs = new byte[1024];
				// 读取到的数据长度
				int len;
				// 输出的文件流
				File sf = new File(savePath);
				if (!sf.exists()) {
					sf.mkdir();
				}
				os = new FileOutputStream(sf.getPath() + "\\" + filename);
				// 开始读取
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 完毕，关闭所有链接
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
			}
	//	} else {
	//		result = false;
	//	}

		return result;
	}

	public static void main(String[] args) {
		String picUrl = "xxxxx.xxxx.jpeg";
		String extName = picUrl.substring(picUrl.lastIndexOf("."));
		System.out.println(extName);
	}
}
