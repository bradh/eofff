package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ILocBoxBuilder {

    private int version;
    private int flags;
    private final List<ILocItem> items = new ArrayList<>();
    private int offsetSize = 4;
    private int lengthSize = 4;
    private int baseOffsetSize = 4;
    private int indexSize = 4;

    public ILocBoxBuilder() {}

    public ILocBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public ILocBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public ILocBoxBuilder withItem(ILocItem entry) {
        this.items.add(entry);
        return this;
    }

    public ILocBoxBuilder withOffsetSize(int size) {
        this.offsetSize = size;
        return this;
    }

    public ILocBoxBuilder withLengthSize(int size) {
        this.lengthSize = size;
        return this;
    }

    public ILocBoxBuilder withBaseOffsetSize(int size) {
        this.baseOffsetSize = size;
        return this;
    }

    public ILocBoxBuilder withIndexSize(int size) {
        this.indexSize = size;
        return this;
    }

    public ILocBoxBuilder addItem(ILocItem item) {
        this.items.add(item);
        return this;
    }

    public ILocBox build() {
        ILocBox box = new ILocBox(new FourCC("iloc"));
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
