package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ItemInfoEntryBuilder {

    private int version;
    private int flags;
    private int item_id;
    private int itemType;
    private String itemUriType;

    public ItemInfoEntryBuilder() {}

    public ItemInfoEntryBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public ItemInfoEntryBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public ItemInfoEntryBuilder withItemId(int itemId) {
        this.item_id = itemId;
        return this;
    }

    public ItemInfoEntryBuilder withItemType(int itemType) {
        this.itemType = itemType;
        return this;
    }

    public ItemInfoEntryBuilder withItemType(String itemType) {
        FourCC fourCC = new FourCC(itemType);
        this.itemType = fourCC.hashCode();
        return this;
    }

    public ItemInfoEntryBuilder withItemUriType(String uri) {
        this.itemUriType = uri;
        return this;
    }

    public ItemInfoEntry build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        // TODO: fix length here!
        ItemInfoEntry box = new ItemInfoEntry(size, new FourCC("infe"));
        box.setVersion(version);
        // TODO: handle version 0 and 1
        box.setFlags(flags);
        box.setItemID(item_id);
        box.setItemProtectionIndex(0);
        box.setItemType(itemType);
        box.setItemUriType(itemUriType);
        return box;
    }
}
