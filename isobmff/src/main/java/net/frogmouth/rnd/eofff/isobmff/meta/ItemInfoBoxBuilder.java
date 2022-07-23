package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ItemInfoBoxBuilder {

    private int version;
    private int flags;
    private final List<ItemInfoEntry> entries = new ArrayList<>();

    public ItemInfoBoxBuilder() {}

    public ItemInfoBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public ItemInfoBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public ItemInfoBoxBuilder withItemInfo(ItemInfoEntry entry) {
        this.entries.add(entry);
        return this;
    }

    public ItemInfoBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        if ((version == 1) || (entries.size() >= (1 << 16))) {
            size += Integer.BYTES;
        } else {
            size += Short.BYTES;
        }
        for (ItemInfoEntry entry : this.entries) {
            size += entry.getSize();
        }
        ItemInfoBox box = new ItemInfoBox(size, new FourCC("iinf"));
        box.setVersion(version);
        box.setFlags(flags);
        for (ItemInfoEntry entry : this.entries) {
            box.addItem(entry);
        }
        return box;
    }
}
