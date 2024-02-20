package net.frogmouth.rnd.eofff.isobmff.kind;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Kind Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.10.4.
 */
public class KindBox extends FullBox {

    private String schemeURI;
    private String value;
    public static final FourCC KIND_ATOM = new FourCC("kind");

    public KindBox() {
        super(KIND_ATOM);
    }

    @Override
    public String getFullName() {
        return "KindBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += schemeURI.getBytes(StandardCharsets.UTF_8).length;
        size += value.getBytes(StandardCharsets.UTF_8).length;
        return size;
    }

    public String getSchemeURI() {
        return schemeURI;
    }

    public void setSchemeURI(String schemeURI) {
        this.schemeURI = schemeURI;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeNullTerminatedString(schemeURI);
        stream.writeNullTerminatedString(value);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("schemeURI=");
        sb.append(schemeURI.toString());
        sb.append(", value=");
        sb.append(value);
        return sb.toString();
    }
}
