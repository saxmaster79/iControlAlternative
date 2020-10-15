package com.github.icontrolalternative;

public class FlStringConverter {


    public String hexToString(String hexString) {
        if (hexString == null || !hexString.startsWith("FL")) {
            return "";
        }
        String src = hexString.substring(4);
        StringBuilder converted = new StringBuilder(src.length() / 2);
        for (int i = 0; (i + 1) < src.length(); i = i + 2) {
            int read = Integer.parseInt(src.substring(i, i + 2), 16);    // convert the String to hex integer
            converted.append((char) read);
        }
        return converted.toString();
    }
}
