package com.rhjf.appserver.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.Hashtable;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;

public class MatrixToImageWriter {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private static final String format = "jpg";// 二维码的图片格式
	// 二维码尺寸
	private static final int QRCODE_SIZE = 500;
	
	// LOGO宽度
	private static final int WIDTH = 60;
	// LOGO高度
	private static final int HEIGHT = 60;
	
	public static BufferedImage toBufferedImage(BitMatrix matrix , String imgPath , boolean needCompress) throws Exception {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		insertImage(image, imgPath, needCompress);
		return image;
	}

	public static void writeToFile(BitMatrix matrix, File outputFile , String imgpath  , boolean needCompress)throws Exception {
		BufferedImage image = toBufferedImage(matrix , imgpath , needCompress);
		
		boolean flag = ImageIO.write(image, format, outputFile);
		if (!flag) {
			throw new IOException("Could not write an image of format " + format + " to " + outputFile);
		}
	}

//	public static void writeToStream(BitMatrix matrix, String format,OutputStream stream) throws IOException {
//		BufferedImage image = toBufferedImage(matrix);
//		if (!ImageIO.write(image, format, stream)) {
//			throw new IOException("Could not write an image of format " + format);
//		}
//	}

	
	private static void insertImage(BufferedImage source, String imgPath,boolean needCompress) throws Exception {
		File file = new File(imgPath);
		if (!file.exists()) {
//			System.err.println("" + imgPath + "   该文件不存在！");
			return;
		}
		Image src = ImageIO.read(new File(imgPath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) { // 压缩LOGO
			if (width > WIDTH) {
				width = WIDTH;
			}
			if (height > HEIGHT) {
				height = HEIGHT;
			}
			Image image = src.getScaledInstance(width, height,Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_SIZE - width) / 2;
		int y = (QRCODE_SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}
	
	
	/**
	 *    
	 * @param content   要生成的二维码内容
	 * @param imgpath   要插入的图片路径
	 * @param outpath   二维码生成后输出路径
	 * @param flag      logo 是否压缩
	 * @throws Exception  
	 */
	public static void encode(String content , File outputFile,  String imgpath  , boolean needCompress) 
			 { 
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码

		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
			MatrixToImageWriter.writeToFile(bitMatrix, outputFile ,imgpath , needCompress);
		} catch (WriterException e) {
			
			e.printStackTrace();
		} catch (Exception e) { 
			
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)  {
		String text = "http://www.shanglianchu.cn:8181/appserver/YMFPayServlet?qrCode=D2F4151380189687920170527085843408497012"; // 二维码内容

		// 生成二维码
		File outputFile = new File("d:" + File.separator + "1111new." + format);
		
		MatrixToImageWriter.encode(text, outputFile, "", true);
		
	}
}
