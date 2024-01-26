package net.frogmouth.rnd.eofff.nalvideo;

import java.util.HexFormat;

public class FormatUtils {

    private static final String INDENT = "    ";

    public static void addIndent(int nestingLevel, StringBuilder sb) {
        for (int i = 0; i < nestingLevel; i++) {
            sb.append(INDENT);
        }
    }

    public static void addByteArrayAsHex(byte[] bytes, StringBuilder sb) {
        String hexString = HexFormat.ofDelimiter(" ").withPrefix("0x").formatHex(bytes);
        sb.append(hexString);
    }
}
