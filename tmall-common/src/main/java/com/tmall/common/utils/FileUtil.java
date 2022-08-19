package com.tmall.common.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileUtil {

    public static String compressImgToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(inputStream).outputFormat("jpeg")
                .imageType(BufferedImage.TYPE_INT_ARGB).scale(1)
                .outputQuality(0.5F).toOutputStream(baos);
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
    }

}
