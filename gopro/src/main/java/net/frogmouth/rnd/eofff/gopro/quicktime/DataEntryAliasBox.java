package net.frogmouth.rnd.eofff.gopro.quicktime;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.dref.DataEntryBaseBox;

/**
 * Data entry (aka data reference) alias box.
 *
 * <p>This content is inferred, not documented.
 */
public class DataEntryAliasBox extends DataEntryBaseBox {

    public static final FourCC ALIS_ATOM = new FourCC("alis");

    public DataEntryAliasBox() {
        super(ALIS_ATOM);
    }

    @Override
    public String getFullName() {
        return "DataEntryAliasBox";
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

    @Override
    public long getBodySize() {
        return 0;
    }
}
