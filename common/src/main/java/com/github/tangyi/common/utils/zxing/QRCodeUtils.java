package com.github.tangyi.common.utils.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * 二维码工具类
 *
 * @author tangyi
 * @date 2019/3/22 15:24
 */
public class QRCodeUtils {

	// 二维码尺寸
	private static final int QRCODE_SIZE = 360;

	// LOGO宽度
	private static final int LOGO_WIDTH = 60;

	// LOGO高度
	private static final int LOGO_HEIGHT = 60;

	/**
	 * 生成二维码，写到本地
	 *
	 * @param text     二维码需要包含的信息
	 * @param width    二维码宽度
	 * @param height   二维码高度
	 * @param filePath 图片保存路径
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void generateQRCodeImage(String text, int width, int height, String filePath)
			throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
		Path path = FileSystems.getDefault().getPath(filePath);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}

	/**
	 * 生成二维码，返回字节流
	 *
	 * @param text   二维码需要包含的信息
	 * @param width  二维码宽度
	 * @param height 二维码高度
	 */
	public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
		return pngOutputStream.toByteArray();
	}

	/**
	 * 生成二维码，返回字节流
	 *
	 * @param text   二维码需要包含的信息
	 */
	public static byte[] getQRCodeImage(String text) throws WriterException, IOException {
		return getQRCodeImage(text, QRCODE_SIZE, QRCODE_SIZE);
	}
}