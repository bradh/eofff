package net.frogmouth.rnd.eofff.jpegxl.fileformat;

import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SignatureBox extends BaseBox {
    public static final FourCC JXL_ATOM = new FourCC("JXL ");
    private long data = 0x0D0A870A;

    public SignatureBox() {
        super(JXL_ATOM);
    }

    public long getData() {
        return this.data;
    }

    public void setData(long data) {
        this.data = data;
    }

    @Override
    public String getFullName() {
        return "JPEGXL Signature Box";
    }
}
