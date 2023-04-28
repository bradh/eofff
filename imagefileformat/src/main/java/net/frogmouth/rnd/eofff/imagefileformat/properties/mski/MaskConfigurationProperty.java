package net.frogmouth.rnd.eofff.imagefileformat.properties.mski;

import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class MaskConfigurationProperty extends ItemFullProperty {
    public static final FourCC MSKC_ATOM = new FourCC("mskC");

    private int bitDepth;

    @Override
    public long getBodySize() {
        return Byte.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeByte(bitDepth);
    }

    public MaskConfigurationProperty() {
        super(MSKC_ATOM);
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    @Override
    public String getFullName() {
        return "MaskConfigurationProperty";
    }
}
