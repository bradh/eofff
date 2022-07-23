package net.frogmouth.rnd.eofff.isobmff.meta;

import static net.frogmouth.rnd.eofff.isobmff.BaseBox.intToBytes;
import static net.frogmouth.rnd.eofff.isobmff.BaseBox.shortToBytes;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Primary Item Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.11.4.
 */
public class PitmBox extends FullBox {
    private long itemID;

    public PitmBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "PrimaryItemBox";
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        if ((getVersion() == 0) && (itemID >= (1 << 16))) {
            setVersion(1);
        }
        stream.write(getVersionAndFlagsAsBytes());
        if (getVersion() == 0) {
            stream.write(shortToBytes((short) itemID));
        } else {
            stream.write(intToBytes((int) itemID));
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
