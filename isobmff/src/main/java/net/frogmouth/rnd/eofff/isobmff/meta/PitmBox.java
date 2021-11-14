package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

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
