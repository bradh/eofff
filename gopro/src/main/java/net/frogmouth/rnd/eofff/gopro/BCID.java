package net.frogmouth.rnd.eofff.gopro;

import java.io.IOException;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class BCID extends BaseBox {

    private byte[] values;

    public static final FourCC BCID_ATOM = new FourCC("BCID");

    public BCID() {
        super(BCID_ATOM);
    }

    public byte[] getValue() {
        return values;
    }

    public void setValue(byte[] value) {
        this.values = value;
    }

    @Override
    public String getFullName() {
        return "GoPro BCID";
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
