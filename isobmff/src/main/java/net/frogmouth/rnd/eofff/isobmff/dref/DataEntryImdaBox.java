package net.frogmouth.rnd.eofff.isobmff.dref;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class DataEntryImdaBox extends DataEntryBaseBox {

    private long imda_ref_identifier;
    public static final FourCC IMDT_ATOM = new FourCC("imdt");

    public DataEntryImdaBox() {
        super(IMDT_ATOM);
    }

    public long getImdaRefIdentifier() {
        return imda_ref_identifier;
    }

    public void setImdaRefIdentifier(long identifier) {
        this.imda_ref_identifier = identifier;
    }

    @Override
    public String getFullName() {
        return "DataEntryImdaBox";
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(imda_ref_identifier);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("imda_ref_identifier: ");
        sb.append(imda_ref_identifier);
        return sb.toString();
    }
}
