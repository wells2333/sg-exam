package com.github.tangyi.common.utils.zxing;

import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

/**
 * @author tangyi
 * @date 2019/3/22 15:27
 */
public class QRCode implements QRCodeImage {
    BufferedImage bufImg;

    public QRCode(BufferedImage bufImg) {
        this.bufImg = bufImg;
    }

    @Override
    public int getHeight() {
        return bufImg.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        return bufImg.getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return bufImg.getWidth();
    }
}

