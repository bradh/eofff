package net.frogmouth.rnd.eofff.gopro;

import java.io.IOException;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class SETT extends BaseBox {

    private byte[] values;

    public static final FourCC SETT_ATOM = new FourCC("SETT");

    public SETT() {
        super(SETT_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro SETT";
    }

    public byte[] getValues() {
        return values;
    }

    public void setValues(byte[] values) {
        this.values = values;
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
