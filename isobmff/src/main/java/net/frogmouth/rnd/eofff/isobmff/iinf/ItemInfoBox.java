package net.frogmouth.rnd.eofff.isobmff.iinf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;

/**
 * Item Information Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.11.6.
 */
public class ItemInfoBox extends FullBox {
    public static final FourCC IINF_ATOM = new FourCC("iinf");
    private final List<ItemInfoEntry> items = new ArrayList<>();

    public ItemInfoBox() {
        super(IINF_ATOM);
    }

    @Override
    public String getFullName() {
        return "ItemInfoBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
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
        if ((getVersion() == 0) && (items.size() >= (1 << 16))) {
            setVersion(1);
        }
        this.writeBoxHeader(stream);
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
