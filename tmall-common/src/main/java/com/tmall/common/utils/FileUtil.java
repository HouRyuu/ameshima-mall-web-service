package com.tmall.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileUtil {

    public static String compressImgToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 更改后缀的图无法压缩
        Thumbnails.of(inputStream).outputFormat("jpeg")
                .imageType(BufferedImage.TYPE_INT_ARGB).scale(1.00)
                .outputQuality(0.5F).toOutputStream(baos);
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static String compressImgToBase64(String base64) throws IOException {
        if (!base64.startsWith("data:image")) {
            return base64;
        }
        base64 = base64.substring(base64.indexOf(",") + 1);
        return compressImgToBase64(new ByteArrayInputStream(Base64.getDecoder().decode(base64)));
    }

}
