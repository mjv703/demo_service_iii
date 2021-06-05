package com.medicai.pillpal.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//import com.google.zxing.oned.EAN13Writer ;

public class BarcodeService {

    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        EAN13Writer barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, 300, 150);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static void main(String[] args) {
        try {
            BufferedImage bImage = generateEAN13BarcodeImage("123456789012");
            ImageIO.write(bImage, "gif", new File("../image.gif"));
            ImageIO.write(bImage, "jpg", new File("../image.png"));
            ImageIO.write(bImage, "bmp", new File("../image.bmp"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
