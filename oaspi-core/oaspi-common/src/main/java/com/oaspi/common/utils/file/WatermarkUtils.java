package com.oaspi.common.utils.file;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.oaspi.common.utils.SecurityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片、pdf水印
 *
 * @author: yg
 * @date: 2024/12/06 15:09
 **/
public class WatermarkUtils {

    /**
     * 给图片添加水印，测试好用
     * @param sourceImage BufferedImage
     * @return BufferedImage
     */
    public static BufferedImage addWatermarkToImage(BufferedImage sourceImage) {
        // 创建水印图层,保持原图尺寸
        BufferedImage watermarked = new BufferedImage(
                sourceImage.getWidth(),
                sourceImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // 获取Graphics2D对象并设置渲染提示
        Graphics2D g2d = watermarked.createGraphics();
        g2d.drawImage(sourceImage, 0, 0, null);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 设置水印文字内容
        String watermarkText = SecurityUtils.getLoginUser().getUsername() + " " +
                SecurityUtils.getLoginUser().getUser().getPhonenumber();

        // 设置水印样式
        Font font = new Font("微软雅黑", Font.PLAIN, sourceImage.getWidth() / 30);
        g2d.setFont(font);

        // 设置半透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

        // 计算文字大小以确定位置
        FontMetrics metrics = g2d.getFontMetrics(font);
        int textWidth = metrics.stringWidth(watermarkText);
        int textHeight = metrics.getHeight();

        // 在图片上以45度角绘制多行水印
        g2d.rotate(Math.toRadians(45), sourceImage.getWidth() / 2, sourceImage.getHeight() / 2);

        // 设置水印颜色
        g2d.setColor(new Color(0, 0, 0, 128));

        // 绘制水印网格
        int xStep = textWidth + 100;  // 水印之间的横向间距
        int yStep = textHeight + 100; // 水印之间的纵向间距

        for (int x = -sourceImage.getWidth(); x < sourceImage.getWidth() * 2; x += xStep) {
            for (int y = -sourceImage.getHeight(); y < sourceImage.getHeight() * 2; y += yStep) {
                g2d.drawString(watermarkText, x, y);
            }
        }

        g2d.dispose();
        return watermarked;
    }

    /**
     * 给pdf添加水印，测试好用
     * @param document PDDocument
     * @throws IOException
     */
    public static void addWatermarkToPdf(PDDocument document) throws IOException {
        // 获取水印文本
        String watermarkText = SecurityUtils.getLoginUser().getUsername() + " " +
                SecurityUtils.getLoginUser().getUser().getPhonenumber();
        // 遍历所有页面添加水印
        for (PDPage page : document.getPages()) {
            PDRectangle pageSize = page.getMediaBox();
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();
            // 创建水印层
            try (PDPageContentStream contentStream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                // 设置水印透明度
                PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                graphicsState.setNonStrokingAlphaConstant(0.3f);
                contentStream.setGraphicsStateParameters(graphicsState);
                // 设置水印颜色为灰色
                contentStream.setNonStrokingColor(100, 100, 100);
                // 使用中文字体
                PDFont font = getAvailableFont(document);
                float fontSize = 26;
                // 计算文本宽度用于定位
                float textWidth = font.getStringWidth(watermarkText) / 1000 * fontSize;
                // 在页面上以网格方式添加多个倾斜水印
                double rotation = Math.PI / 4; // 45度角
                int xStep = 200;  // 水印横向间距
                int yStep = 200;  // 水印纵向间距

                for (int x = 0; x < pageWidth + textWidth; x += xStep) {
                    for (int y = 0; y < pageHeight + fontSize; y += yStep) {
                        addWatermarkText(contentStream, font, fontSize, watermarkText,
                                x - textWidth / 2, y, rotation);

                        // 保存当前图形状态
                        contentStream.saveGraphicsState();
                        // 移动到目标位置
                        contentStream.transform(Matrix.getTranslateInstance(x - textWidth / 2, y));
                        // 应用旋转
                        contentStream.transform(Matrix.getRotateInstance(rotation, 0, 0));
                        // 开始绘制文本
                        contentStream.beginText();
                        contentStream.setFont(font, fontSize);
                        contentStream.newLineAtOffset(0, 0);
                        contentStream.showText(watermarkText);
                        contentStream.endText();
                        // 恢复图形状态
                        contentStream.restoreGraphicsState();
                    }
                }

                // 添加页面边缘水印
                float margin = 40;
                float smallFontSize = 8;
                // 上边缘水印
                contentStream.saveGraphicsState();
                contentStream.transform(Matrix.getTranslateInstance(margin, pageHeight - margin));
                contentStream.beginText();
                contentStream.setFont(font, smallFontSize);
                contentStream.newLineAtOffset(0, 0);
                contentStream.showText(watermarkText);
                contentStream.endText();
                contentStream.restoreGraphicsState();
                // 下边缘水印
                contentStream.saveGraphicsState();
                contentStream.transform(Matrix.getTranslateInstance(margin, margin));
                contentStream.beginText();
                contentStream.setFont(font, smallFontSize);
                contentStream.newLineAtOffset(0, 0);
                contentStream.showText(watermarkText);
                contentStream.endText();
                contentStream.restoreGraphicsState();

                // 边缘水印
                addWatermarkText(contentStream, font, smallFontSize, watermarkText,
                        margin, pageHeight - margin, 0);
                addWatermarkText(contentStream, font, smallFontSize, watermarkText,
                        margin, margin, 0);
            }
        }
    }

    /**
     * 添加水印文本内容
     * @param contentStream PDPageContentStream
     * @param font 字体
     * @param fontSize 字体大小
     * @param text 文本
     * @param x x坐标
     * @param y y坐标
     * @param rotation 旋转角度
     * @throws IOException
     */
    private static void addWatermarkText(PDPageContentStream contentStream, PDFont font, float fontSize,
                                         String text, float x, float y, double rotation) throws IOException {
        contentStream.saveGraphicsState();
        // 先移动到目标位置
        contentStream.transform(Matrix.getTranslateInstance(x, y));
        // 再进行旋转
        if (rotation != 0) {
            contentStream.transform(Matrix.getRotateInstance(rotation, 0, 0));
        }
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(0, 0);
        contentStream.showText(text);
        contentStream.endText();

        contentStream.restoreGraphicsState();
    }

    /**
     * 获取可用的字体
     */
    private static PDFont getAvailableFont(PDDocument document) throws IOException {
        // Windows常见中文字体路径
        String[] windowsFontPaths = {
                "C:/Windows/Fonts/msyh.ttf",      // 微软雅黑
                "C:/Windows/Fonts/simhei.ttf",    // 黑体
                "C:/Windows/Fonts/simsun.ttc",    // 宋体
                "C:/Windows/Fonts/simfang.ttf",   // 仿宋
        };

        // Linux常见中文字体路径
        String[] linuxFontPaths = {
                "/usr/share/fonts/truetype/wqy/wqy-microhei.ttc",    // 文泉驿微米黑
                "/usr/share/fonts/truetype/arphic/uming.ttc",        // AR PL UMing
                "/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc" // Noto Sans CJK
        };

        // 尝试加载Windows中文字体
        for (String fontPath : windowsFontPaths) {
            try {
                File fontFile = new File(fontPath);
                if (fontFile.exists()) {
                    return PDType0Font.load(document, fontFile);
                }
            } catch (Exception e) {
                // 继续尝试下一个字体
                continue;
            }
        }

        // 尝试加载Linux中文字体
        for (String fontPath : linuxFontPaths) {
            try {
                File fontFile = new File(fontPath);
                if (fontFile.exists()) {
                    return PDType0Font.load(document, fontFile);
                }
            } catch (Exception e) {
                // 继续尝试下一个字体
                continue;
            }
        }

        // 尝试从classpath加载内置字体
        try {
            InputStream fontStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("fonts/simsun.ttc");
            if (fontStream != null) {
                return PDType0Font.load(document, fontStream);
            }
        } catch (Exception e) {
            // 如果内置字体加载失败，继续往下走
        }

        // 如果所有中文字体都无法加载，使用PDFBox内置的字体作为后备方案
        // 注意：内置字体不支持中文，中文会显示为方块
        return PDType1Font.HELVETICA;
    }

    /**
     * 处理不支持中文显示的情况
     */
    private static String handleUnsupportedText(String text, PDFont font) {
        if (font instanceof PDType1Font) {
            // 如果使用的是不支持中文的字体，将中文字符替换为拼音或英文
            return text.replaceAll("[\u4e00-\u9fa5]", "X");
        }
        return text;
    }

    /**
     * 给图片添加水印
     *
     * @param imagePath     图片路径 (G:/1.png,支持jpg、png格式) 生成的水印将替换原先的图片
     * @param watermarkText 水印文字
     */
    public static boolean addTextWatermarkToImage(String imagePath, String watermarkText) {
        boolean flag = true;
        try {
            // 加载原始图片
            BufferedImage originalImage = ImgUtil.read(imagePath);
            // 创建水印文字图片
            Graphics2D g2d = originalImage.createGraphics();
            g2d.setFont(new Font("宋体", Font.BOLD, 30));
            // 设置半透明的浅灰色
            Color semiTransparentGray = new Color(192, 192, 192, 128); // 最后一个参数是透明度（0-255）
            g2d.setColor(semiTransparentGray);
//            g2d.setColor(Color.LIGHT_GRAY); // 设置水印颜色
            g2d.drawString(watermarkText, 10, 50);
            g2d.dispose();
            // 输出图片到文件
            File outFile = FileUtil.file(imagePath);
            ImgUtil.write(originalImage, outFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 给PDF添加水印
     *
     * @param pdfPath       PDF文件路径(G:/1.pdf) 生成的水印将替换原先的pdf
     * @param watermarkText 水印文字
     */
    public static boolean addTextWatermarkToPdf(String pdfPath, String watermarkText) {
        boolean flag = true;
        try {
            Color color = Color.LIGHT_GRAY;
            PDDocument document = PDDocument.load(new File(pdfPath));

            for (PDPage page : document.getPages()) {
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    // 加载支持中文的字体
                    Resource resource = new ClassPathResource("fonts/simsun.ttf");
                    PDType0Font customFont = PDType0Font.load(document, resource.getInputStream());

                    // 设置字体和大小
                    contentStream.setFont(customFont, 46);

                    // 设置透明度
                    PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                    graphicsState.setNonStrokingAlphaConstant(0.5f);
                    contentStream.setGraphicsStateParameters(graphicsState);

                    // 计算文本宽度
                    float stringWidth = customFont.getStringWidth(watermarkText) / 1000 * 36;

                    // 获取页面尺寸
                    PDRectangle pageSize = page.getMediaBox();
                    float x = (pageSize.getWidth() - stringWidth) / 2;
                    float y = (pageSize.getHeight() + 36) / 2;

                    // 设置颜色
                    PDColor pdColor = new PDColor(new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f}, PDDeviceRGB.INSTANCE);
                    contentStream.setNonStrokingColor(pdColor);

                    // 添加水印文本
                    contentStream.beginText();
                    contentStream.newLineAtOffset(x, y);
                    contentStream.showText(watermarkText);
                    contentStream.endText();
                }
            }
            document.save(pdfPath);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
   