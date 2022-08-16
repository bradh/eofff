package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Item Information Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.11.6.
 */
public class ItemInfoBox extends FullBox {
    private final List<ItemInfoEntry> items = new ArrayList<>();

    public ItemInfoBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "ItemInfoBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        if ((getVersion() == 1) || (items.size() >= (1 << 16))) {
            size += Integer.BYTES;
        } else {
            size += Short.BYTES;
        }
        for (ItemInfoEntry entry : items) {
            size += entry.getSize();
        }
        return size;
    }

    public List<ItemInfoEntry> getItems() {
        return new ArrayList<>(this.items);
    }

    public void addItem(ItemInfoEntry item) {
        this.items.add(item);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        if ((getVersion() == 0) && (items.size() >= (1 << 16))) {
            setVersion(1);
        }
        stream.write(getVersionAndFlagsAsBytes());
        if (getVersion() == 0) {
            stream.writeShort((short) this.items.size());
        } else {
            stream.writeInt(this.items.size());
        }
        for (ItemInfoEntry itemInfoEntry : this.items) {
            itemInfoEntry.writeTo(stream);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': item_count=");
        sb.append(getItems().size());
        for (ItemInfoEntry item : getItems()) {
            sb.append("\n");
            sb.append("\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
