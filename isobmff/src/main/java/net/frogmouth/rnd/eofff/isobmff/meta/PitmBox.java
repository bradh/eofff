package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Primary Item Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.11.4.
 */
public class PitmBox extends FullBox {
    private long itemID;

    public PitmBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "PrimaryItemBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
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
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        if ((getVersion() == 0) && (itemID >= (1 << 16))) {
            setVersion(1);
        }
        stream.write(getVersionAndFlagsAsBytes());
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
