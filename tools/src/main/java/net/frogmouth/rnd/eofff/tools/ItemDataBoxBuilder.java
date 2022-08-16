package net.frogmouth.rnd.eofff.tools;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBox;
import org.jmisb.api.klv.ArrayBuilder;

public class ItemDataBoxBuilder {

    private final ArrayBuilder arrayBuilder = new ArrayBuilder();

    public ItemDataBoxBuilder() {}

    public ItemDataBoxBuilder addData(byte[] data) {
        arrayBuilder.append(data);
        return this;
    }

    public ItemDataBox build() {
        byte[] bytes = arrayBuilder.toBytes();

        ItemDataBox box = new ItemDataBox(new FourCC("idat"));
        box.setData(bytes);
        return box;
    }
}
