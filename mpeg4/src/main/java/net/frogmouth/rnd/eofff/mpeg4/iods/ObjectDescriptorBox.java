package net.frogmouth.rnd.eofff.mpeg4.iods;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ObjectDescriptorBox extends FullBox {

    public static final FourCC IODS_ATOM = new FourCC("iods");

    public ObjectDescriptorBox() {
        super(IODS_ATOM);
    }

    @Override
    public String getFullName() {
        return "ObjectDescriptorBox";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        // TODO
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = 0;
        // TODO
        return size;
    }
}
