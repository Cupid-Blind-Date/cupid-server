package cupid.image.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtils {

    public static String getExtension(String name) {
        if (name == null || !name.contains(".")) {
            return ""; // 확장자 없음
        }
        return name.substring(name.lastIndexOf('.') + 1).toLowerCase(); // 확장자만 반환
    }
}
