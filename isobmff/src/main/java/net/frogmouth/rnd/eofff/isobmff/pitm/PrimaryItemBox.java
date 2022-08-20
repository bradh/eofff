package net.frogmouth.rnd.eofff.isobmff.pitm;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Primary Item Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.11.4.
 */
public class PrimaryItemBox extends FullBox {
    public static final FourCC PITM_ATOM = new FourCC("pitm");
    private long itemID;

    public PrimaryItemBox() {
        super(PITM_ATOM);
    }

    @Override
    public String getFullName() {
        return "PrimaryItemBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if (getVersion() == 0) {
            size += Short.BYTES;
        } else {
            size += Integer.BYTES;
        }
        return size;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        if ((getVersion() == 0) && (itemID >= (1 << 16))) {
            setVersion(1);
        }
        this.writeBoxHeader(stream);
        if (getVersion() == 0) {
            stream.writeShort((short) itemID);
        } else {
            stream.writeInt((int) itemID);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': item_ID=");
        sb.append(getItemID());
        sb.append("");
        return sb.toString();
    }
}
