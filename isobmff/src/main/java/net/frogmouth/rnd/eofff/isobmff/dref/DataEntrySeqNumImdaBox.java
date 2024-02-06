package net.frogmouth.rnd.eofff.isobmff.dref;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class DataEntrySeqNumImdaBox extends DataEntryBaseBox {

    public static final FourCC SNIM_ATOM = new FourCC("snim");

    public DataEntrySeqNumImdaBox() {
        super(SNIM_ATOM);
    }

    @Override
    public String getFullName() {
        return "DataEntrySeqNumImdaBox";
    }

    @Override
    public long getBodySize() {
        return 0;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        return sb.toString();
    }
}
