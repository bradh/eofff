package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class ItemInfoBox extends FullBox {
    private final List<ItemInfoEntry> items = new ArrayList<>();

    public ItemInfoBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "ItemInfoBox";
    }

    public List<ItemInfoEntry> getItems() {
        return new ArrayList<>(this.items);
    }

    public void addItem(ItemInfoEntry item) {
        this.items.add(item);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        if ((getVersion() == 0) && (items.size() >= (2 << 16))) {
            setVersion(1);
        }
        stream.write(getVersionAndFlagsAsBytes());
        if (getVersion() == 0) {
            stream.write(shortToBytes((short) this.items.size()));
        } else {
            stream.write(intToBytes(this.items.size()));
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
