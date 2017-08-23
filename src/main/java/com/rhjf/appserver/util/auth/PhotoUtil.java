package com.rhjf.appserver.util.auth;
 
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bouncycastle.util.encoders.Base64;




/**
 * @desc  身份证信息转�?
 * @author ltl
 * @date   2017�?3�?15�?
 */
public class PhotoUtil {
	public static void main(String[] args) {
		String idPhoto = "";//Base64编码字符�? --取鉴权返回的idPhoto字段�?
		idPhoto = idPhoto.toString().replaceAll("\u0020", "").replaceAll("\n", "").replaceAll("\r", "");
		
		System.out.println(idPhoto);
		
//		byte[] bytes = Base64.getDecoder().decode(idPhoto.getBytes());
		byte[] bytes = Base64.decode(idPhoto.getBytes());
		ByteArrayInputStream in = new ByteArrayInputStream(bytes); // 将b作为输入流；
		BufferedImage image2;
		try {
			// 将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
			image2 = ImageIO.read(in);
			String desFile = "D:\\pp02.jpg";
			ImageIO.write(image2, "jpg", new File(desFile));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
