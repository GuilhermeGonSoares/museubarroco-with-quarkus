package com.pibic.shared;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageHelper {
    public static InputStream getBase64ContentStream(String base64Image) {
        String imageData = base64Image.replaceFirst("^data:image/[a-zA-Z]+;base64,", "");
        byte[] imageBytes = Base64.getDecoder().decode(imageData);
        return new ByteArrayInputStream(imageBytes);
    }

    public static String getImageName(String name, String base64Image) {
        String cleanName = name.replaceAll("[^a-zA-Z0-9 ]", "").replace(" ", "");
        return cleanName + "-" + UUID.randomUUID().toString() + "." + getContentType(base64Image);
    }

    private static String getContentType(String base64Image) {
        Pattern pattern = Pattern.compile("^data:image/([a-zA-Z]+);base64,");
        Matcher matcher = pattern.matcher(base64Image);
        return matcher.find() ? matcher.group(1) : "jpg";
    }
}
