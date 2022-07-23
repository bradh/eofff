package net.frogmouth.rnd.eofff.isobmff.meta;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ItemInfoEntryBuilder {

    private int version;
    private int flags;
    private int item_id;
    private String item_name;
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

    public ItemInfoEntryBuilder withItemName(String itemName) {
        this.item_name = itemName;
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
        if ((version == 2) && (item_id < (1 << 16))) {
            size += Short.BYTES;
        } else {
            size += Integer.BYTES;
        }
        size += Short.BYTES + Integer.BYTES;
        if (item_name != null) {
            size += item_name.getBytes(StandardCharsets.US_ASCII).length;
        }
        size += Byte.BYTES;
        if (this.itemType == new FourCC("mime").hashCode()) {
            // TODO
        } else if (this.itemType == new FourCC("uri ").hashCode()) {
            size += itemUriType.getBytes(StandardCharsets.US_ASCII).length;
            size += Byte.BYTES;
        }
        ItemInfoEntry box = new ItemInfoEntry(size, new FourCC("infe"));
        box.setVersion(version);
        // TODO: handle version 0 and 1
        box.setFlags(flags);
        box.setItemID(item_id);
        box.setItemProtectionIndex(0);
        box.setItemType(itemType);
        box.setItemName(item_name);
        box.setItemUriType(itemUriType);
        return box;
    }
}
