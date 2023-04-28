package net.frogmouth.rnd.eofff.jpegxl.fileformat;

import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class CodestreamBox extends BaseBox {
    public static final FourCC JXLC_ATOM = new FourCC("jxlc");
    public byte[] data;

    public CodestreamBox() {
        super(JXLC_ATOM);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String getFullName() {
        return "JPEGXLCodestream Box";
    }
}
