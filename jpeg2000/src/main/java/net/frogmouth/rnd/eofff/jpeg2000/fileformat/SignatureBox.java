package net.frogmouth.rnd.eofff.jpeg2000.fileformat;

import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SignatureBox extends BaseBox {
    public static final FourCC JP_ATOM = new FourCC("jP  ");
    private long data = 0x0D0A870A;

    public SignatureBox() {
        super(JP_ATOM);
    }

    public long getData() {
        return this.data;
    }

    public void setData(long data) {
        this.data = data;
    }

    @Override
    public String getFullName() {
        return "JPEG2000 Signature Box";
    }

    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("0x");
        sb.append(HexFormat.of().toHexDigits((int) data));
        return sb.toString();
    }
}
