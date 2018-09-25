package com.jhh.dc.baika.common.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明：pdf文档转图片
 * 第三方依赖：pdfbox-2.0.8.jar
 * <dependency>
 *  <groupId>org.apache.pdfbox</groupId>
 *  <artifactId>pdfbox</artifactId>
 *  <version>2.0.8</version>
 * </dependency>
 *
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年01月08日 上午11:56] 创建方法 by guyi
 */
public class PdfToImage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfToImage.class);

    private static final String PAGE_INDEX_SEPARATOR = ",";

    /**
     * pdf转图片
     * @param srcFile pdf转换源文件
     * @param format 待输出图片的格式，默认jpg
     * @param dpi 分辨率，默认96
     * @param merge 是否合并为一张，默认为每页生成一张图片
     * @throws IOException
     */
    public static InputStream pdfToImage(String srcFile, String format, Float dpi, Boolean merge) throws IOException, URISyntaxException {
        InputStream pdfStream = pdfToStream(srcFile);
        List<BufferedImage> images = pdfToImage(pdfStream, dpi == null ? 96f : dpi);
        pdfStream.close();
        if(images == null || images.isEmpty()){
            return null;
        }
        //默认为jpg
        format = format == null || format.isEmpty() ? "png" : format;
        StringBuilder sb = new StringBuilder();
        //合并多张图片为一张
        merge = merge == null ? false : merge;
        if(merge) {
            BufferedImage image = mergeImages(images);
            images.clear();
            images.add(image);
        }
        //保存到本地
       /* for (int i = 0, len = images.size(); i < len; i++) {
            //输出格式: [文件夹路径]/[pdf文件名]_0001.jpg
            ImageIO.write(images.get(i), format, new File(
                    sb.append(destPath).append(File.separator)
                            .append(pdfFileName).append("_").append(String.format("%04d", i + 1))
                            .append(".").append(format).toString()));
            sb.setLength(0);
        }*/
       //测试流的形式读出
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(out);
        ImageIO.write(images.get(0),format,imageOut);
        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        return inputStream;
    }

    private static BufferedImage mergeImages(List<BufferedImage> images){
        int width = 0, height = 0;
        for(BufferedImage image : images){
            width = image.getWidth() > width ? image.getWidth() : width;
            height += image.getHeight();
        }
        BufferedImage pdfImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = pdfImage.createGraphics();
        height = 0;
        for(BufferedImage image :images){
            g2d.drawImage(image, (width - image.getWidth()) / 2, height, image.getWidth(), image.getHeight(), null);
            height += image.getHeight();
        }
        g2d.dispose();
        return pdfImage;
    }

    private static List<BufferedImage> pdfToImage(InputStream file, float dpi) throws IOException {
        List<BufferedImage> imgList = null;
        PDDocument pdDocument = null;
        BufferedImage image;
        try {
            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);

            int numPages = pdDocument.getNumberOfPages();
            imgList = new ArrayList<BufferedImage>();
            for (int i = 0; i < numPages; i++) {
                image = renderer.renderImageWithDPI(i, dpi);
                if (null != image) {
                    imgList.add(image);
                }
            }
        } catch (IOException e) {
            LOGGER.error("convert pdf pages to images failed.", e);
            //FIXME
            throw e;
        } finally {
            try {
                if (null != pdDocument) {
                    pdDocument.close();
                }
            } catch (IOException e) {
                LOGGER.error("close IO failed when convert pdf pages to images.", e);
                //FIXME
                throw e;
            }
        }
        return imgList;
    }
    private static InputStream pdfToStream(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(30000);
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        return inputStream;
    }



    public static void main(String[] args) throws IOException, URISyntaxException {
//        String srcFile = "./data/output/Organize_PlatSafeSign_finish.pdf";
        String srcFile = "C:\\Users\\xingwu\\Desktop/wKgBP1pyfuOANVW3AAp_c-Rwmug770.pdf";
//        String destPath = "./data/output/";
        PdfToImage.pdfToImage(srcFile, "png", 96f, true);
    }
}