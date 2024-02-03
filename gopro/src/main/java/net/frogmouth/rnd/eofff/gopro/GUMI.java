package net.frogmouth.rnd.eofff.gopro;

import java.io.IOException;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class GUMI extends BaseBox {

    private byte[] values;

    public static final FourCC GUMI_ATOM = new FourCC("GUMI");

    public GUMI() {
        super(GUMI_ATOM);
    }

    public byte[] getValues() {
        return values;
    }

    public void setValues(byte[] values) {
        this.values = values;
    }

    @Override
    public String getFullName() {
        return "GoPro GUMI";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.write(values);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append(HexFormat.of().withPrefix(" 0x").formatHex(values));
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        return values.length;
    }
}
