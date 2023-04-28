package net.frogmouth.rnd.eofff.jpegxl.fileformat;

import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class BitstreamReconstructionDataBox extends BaseBox {
    public static final FourCC JBRD_ATOM = new FourCC("jbrd");
    public byte[] data;

    public BitstreamReconstructionDataBox() {
        super(JBRD_ATOM);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String getFullName() {
        return "JPEGXL Bitstream Reconstruction Data Box";
    }
}
