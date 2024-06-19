package net.frogmouth.rnd.eofff.cenc.senc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class SampleEncryptionBox extends FullBox {
    public static final FourCC SENC_ATOM = new FourCC("senc");

    public SampleEncryptionBox() {
        super(SENC_ATOM);
    }

    @Override
    public long getBodySize() {
        int size = 0;
        // TODO
        return size;
    }

    @Override
    public String getFullName() {
        return "SampleEncryptionBox";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        // TODO
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        // TODO
        return sb.toString();
    }
}
