package cupid.common.utils;

public class StringUtils {

    public static String removeWhitespace(String str) {
        return str.replaceAll("\\s+", "");
    }
}
