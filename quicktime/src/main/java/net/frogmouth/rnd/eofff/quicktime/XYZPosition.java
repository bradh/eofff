package net.frogmouth.rnd.eofff.quicktime;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ISO639Language;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class XYZPosition extends BaseBox {

    // This is ©xyz in LATIN-1.
    public static final FourCC XYZPosition_ATOM = new FourCC(0xA978797A);

    private ISO639Language language;
    private String value;

    public XYZPosition() {
        super(XYZPosition_ATOM);
    }

    public ISO639Language getLanguage() {
        return language;
    }

    public void setLanguage(ISO639Language language) {
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getFullName() {
        return "GoPro Location";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
        writer.writeUnsignedInt16(bytes.length);
        writer.writePackedLanguageCode(language);
        writer.write(bytes);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("lang: ");
        sb.append(language);
        sb.append(", value: ");
        sb.append(value);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += ISO639Language.BYTES;
        size += value.getBytes(StandardCharsets.US_ASCII).length;
        return size;
    }
}
