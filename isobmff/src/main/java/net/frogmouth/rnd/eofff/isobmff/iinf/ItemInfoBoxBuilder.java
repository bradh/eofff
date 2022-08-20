package net.frogmouth.rnd.eofff.isobmff.iinf;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;

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
        ItemInfoBox box = new ItemInfoBox();
        box.setVersion(version);
        box.setFlags(flags);
        for (ItemInfoEntry entry : this.entries) {
            box.addItem(entry);
        }
        return box;
    }
}
