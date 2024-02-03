package net.frogmouth.rnd.eofff.gopro;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * @author bradh
 */
public abstract class GoProStringValueBox extends BaseBox {

    protected String value;

    public GoProStringValueBox(FourCC name) {
        super(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
        writer.write(bytes);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append(value);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        return value.length();
    }
}
