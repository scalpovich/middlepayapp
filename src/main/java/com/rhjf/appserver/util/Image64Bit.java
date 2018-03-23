package com.rhjf.appserver.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.sun.jersey.core.util.Base64;

/**
 * @author hadoop
 */
public class Image64Bit {

	/**
	 * // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 
	 * @param imgFilePath  图片路径
	 * @return    图片的base64字符串
	 */
	public static String getImageStr(String imgFilePath) {
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码
		return new String(Base64.encode(data));
	}

	/**
	 * 根据图片base64字符串生成图片
	 * 对字节数组字符串进行Base64解码并生成图片
	 * @param imgStr     图片base64字符串
	 * @param imgFilePath  保存图片的路径
	 * @throws IOException  图片io异常
	 */
	public static void geneRateImage(String imgStr, String imgFilePath) throws IOException {
		// 图像数据为空
		if (imgStr == null){
			return;
		}
		// Base64解码
		byte[] bytes = Base64.decode(imgStr);
		for (int i = 0; i < bytes.length; ++i) {
			// 调整异常数据
			if (bytes[i] < 0) {
				bytes[i] += 256;
			}
		}
		// 生成jpeg图片
		OutputStream out = new FileOutputStream(imgFilePath);
		out.write(bytes);
		out.flush();
		out.close();
	}

	/**
	 * 
	 * // 将网络图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 
	 * @param imageUrl  图片url地址
	 * @return  返回图片base64 字符串
	 */
	public static String encodeImageToBase64(URL imageUrl) {
		ByteArrayOutputStream outputStream = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(imageUrl);
			outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", outputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 对字节数组Base64编码
		String imageStr = null;
		if(outputStream!=null){
			imageStr =  new String(Base64.encode(outputStream.toByteArray()));
		}
		return imageStr;
	}

	public static void main(String[] args) throws IOException {
		String string = encodeImageToBase64(new URL("http://img.bizhi.sogou.com/images/2012/03/14/124196.jpg"));
		String path = "d:/a.jpg";
		geneRateImage(string, path);
	}
}
