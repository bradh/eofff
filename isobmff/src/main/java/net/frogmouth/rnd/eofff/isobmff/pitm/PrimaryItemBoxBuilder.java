package net.frogmouth.rnd.eofff.isobmff.pitm;

public class PrimaryItemBoxBuilder {

    private int version;
    private int flags;
    private long itemId;

    public PrimaryItemBoxBuilder() {}

    public PrimaryItemBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public PrimaryItemBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public PrimaryItemBoxBuilder withItemId(long itemId) {
        this.itemId = itemId;
        return this;
    }

    public PrimaryItemBox build() {
        PrimaryItemBox box = new PrimaryItemBox();
        box.setVersion(version);
        box.setFlags(flags);
        box.setItemID(itemId);
        return box;
    }
}
