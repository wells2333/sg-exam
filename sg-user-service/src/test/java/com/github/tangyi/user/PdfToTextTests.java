package com.github.tangyi.user;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * 提取 PDF 文本内容
 */
public class PdfToTextTests {

	@Test
	public void testPdfToText() throws Exception {
		String pdfPath = "";
		if (StringUtils.isEmpty(pdfPath)) {
			return;
		}

		File file = new File(pdfPath);
		Assertions.assertNotNull(file);

		PDDocument document = PDDocument.load(file);
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String text = pdfStripper.getText(document);
		System.out.println("文本内容：" + text);
		document.close();
	}
}
