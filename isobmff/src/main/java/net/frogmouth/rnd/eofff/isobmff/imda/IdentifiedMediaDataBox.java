package net.frogmouth.rnd.eofff.isobmff.imda;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Identified media data box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.1.4
 */
public class IdentifiedMediaDataBox extends BaseBox {
    public static final FourCC IMDA_ATOM = new FourCC("imda");
    private long imda_identifier;
    private byte[] data;

    public IdentifiedMediaDataBox() {
        super(IMDA_ATOM);
    }

    @Override
    public String getFullName() {
        return "IdentifiedMediaDataBox";
    }

    @Override
    public long getBodySize() {
        return data.length;
    }

    public long getIdentifier() {
        return imda_identifier;
    }

    public void setIdentifier(long imdaIdentifier) {
        this.imda_identifier = imdaIdentifier;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(imda_identifier);
        stream.write(data);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("imda_identifier: ");
        sb.append(imda_identifier);
        sb.append(", data (length=");
        sb.append(data.length);
        sb.append(")");
        return sb.toString();
    }
}
