package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.jmisb.api.klv.ArrayBuilder;

public class ItemDataBoxBuilder {

    private ArrayBuilder arrayBuilder = new ArrayBuilder();

    public ItemDataBoxBuilder() {}

    public ItemDataBoxBuilder addData(byte[] data) {
        arrayBuilder.append(data);
        return this;
    }

    public ItemDataBox build() {
        byte[] bytes = arrayBuilder.toBytes();
        int size = Integer.BYTES + FourCC.BYTES + bytes.length;
        ItemDataBox box = new ItemDataBox(size, new FourCC("idat"));
        box.setData(bytes);
        return box;
    }
}
