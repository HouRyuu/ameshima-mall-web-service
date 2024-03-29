package com.ameshima.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class FileUtil {

    public static String compressImgToBase64(InputStream inputStream, String contentType) throws IOException {
        if ("image/gif".equals(contentType)) {
            return "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 更改后缀的图无法压缩
        Thumbnails.of(inputStream).outputFormat("jpeg")
                .imageType(BufferedImage.TYPE_INT_ARGB).scale(1.00)
                .outputQuality(0.5F).toOutputStream(baos);
        return "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static String compressImgToBase64(String base64) throws IOException {
        if (!base64.startsWith("data:image")) {
            return base64;
        }
        String fileType = base64.substring(5, base64.indexOf(";"));
        if ("image/gif".equals(fileType)) {
            return base64;
        }
        base64 = base64.substring(base64.indexOf(",") + 1);
        return compressImgToBase64(new ByteArrayInputStream(Base64.getDecoder().decode(base64)), fileType);
    }

    public static String uploadImgWithBase64(String base64) throws IOException {
        if (!base64.startsWith("data:image")) {
            return base64;
        }
        String fileType = base64.substring(5, base64.indexOf(";"));
        return compressImg(
                new ByteArrayInputStream(
                        Base64.getDecoder().decode(base64.substring(base64.indexOf(",") + 1))),
                fileType
        );
    }

    public static String compressImg(InputStream inputStream, String contentType) throws IOException {
        String fileType = contentType.substring(contentType.indexOf("/") + 1);
        String filePath = String.format("/img/%s.%s", CommonUtil.getUuid(), fileType);
        File file = new File(String.format("%s%s", System.getProperty("user.home"), filePath));
        file.createNewFile();
        if ("image/gif".equals(contentType)) {
            FileUtils.writeByteArrayToFile(file, StreamUtils.copyToByteArray(inputStream));
        } else {
            Thumbnails.of(inputStream)
                    .scale(1.00)
                    .outputQuality(0.5F)
                    .outputFormat(fileType)
                    .toFile(file);
        }
        return "http://www.ameshima-mall.com" + filePath;
    }

}
