package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ItemDataBoxBuilder {

    private byte[] data;

    public ItemDataBoxBuilder() {}

    public ItemDataBoxBuilder withData(byte[] data) {
        this.data = data.clone();
        return this;
    }

    public ItemDataBox build() {
        int size = Integer.BYTES + FourCC.BYTES + data.length;
        ItemDataBox box = new ItemDataBox(size, new FourCC("idat"));
        box.setData(data);
        return box;
    }
}
