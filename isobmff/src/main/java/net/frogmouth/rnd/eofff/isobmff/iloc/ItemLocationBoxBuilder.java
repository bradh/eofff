package net.frogmouth.rnd.eofff.isobmff.iloc;

import java.util.ArrayList;
import java.util.List;

public class ItemLocationBoxBuilder {

    private int version;
    private int flags;
    private final List<ILocItem> items = new ArrayList<>();
    private int offsetSize = 4;
    private int lengthSize = 4;
    private int baseOffsetSize = 4;
    private int indexSize = 4;

    public ItemLocationBoxBuilder() {}

    public ItemLocationBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public ItemLocationBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public ItemLocationBoxBuilder withItem(ILocItem entry) {
        this.items.add(entry);
        return this;
    }

    public ItemLocationBoxBuilder withOffsetSize(int size) {
        this.offsetSize = size;
        return this;
    }

    public ItemLocationBoxBuilder withLengthSize(int size) {
        this.lengthSize = size;
        return this;
    }

    public ItemLocationBoxBuilder withBaseOffsetSize(int size) {
        this.baseOffsetSize = size;
        return this;
    }

    public ItemLocationBoxBuilder withIndexSize(int size) {
        this.indexSize = size;
        return this;
    }

    public ItemLocationBoxBuilder addItem(ILocItem item) {
        this.items.add(item);
        return this;
    }

    public ItemLocationBox build() {
        ItemLocationBox box = new ItemLocationBox();
        box.setVersion(version);
        box.setFlags(flags);
        box.setOffsetSize(offsetSize);
        box.setLengthSize(lengthSize);
        box.setBaseOffsetSize(baseOffsetSize);
        box.setIndexSize(indexSize);
        for (ILocItem item : this.items) {
            box.addItem(item);
        }
        return box;
    }
}
