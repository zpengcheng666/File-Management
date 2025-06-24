package com.oaspi.common.utils.file;

import org.apache.pdfbox.pdmodel.PDDocument;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

/**
 * 添加水印并生成压缩包
 * &#064;Author：zpc
 * &#064;Package：com.oaspi.common.utils.file
 * &#064;Project：docmanage-backend
 * &#064;name：AddWatermarkAndWriteToZip
 * &#064;Date：2025/1/14  下午2:57
 * &#064;Filename：AddWatermarkAndWriteToZip
 */
public class AddWatermarkAndWriteToZip {
    public static void addWatermarkAndWriteToZip(InputStream inputStream, ZipOutputStream zipOut, String fileName) throws IOException {
        if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg") || fileName.toLowerCase().endsWith(".png")) {
            // 添加图片水印
            BufferedImage image = ImageIO.read(inputStream);
            BufferedImage watermarkedImage = WatermarkUtils.addWatermarkToImage(image);
            ImageIO.write(watermarkedImage, "png", zipOut);
        } else if (fileName.toLowerCase().endsWith(".pdf")) {
            // 添加PDF水印
            PDDocument document = PDDocument.load(inputStream);
            WatermarkUtils.addWatermarkToPdf(document);
            document.save(zipOut);
            document.close();
        } else {
            // 其他类型的文件直接写入输出流
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, bytesRead);
            }
            zipOut.flush();
        }
    }
}
