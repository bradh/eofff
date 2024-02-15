package net.frogmouth.rnd.eofff.demos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;

public class ItemDataBoxBuilder {

    private final ByteArrayOutputStream arrayBuilder = new ByteArrayOutputStream();

    public ItemDataBoxBuilder() {}

    public ItemDataBoxBuilder addData(byte[] data) throws IOException {
        arrayBuilder.write(data);
        return this;
    }

    public ItemDataBox build() {
        byte[] bytes = arrayBuilder.toByteArray();

        ItemDataBox box = new ItemDataBox();
        box.setData(bytes);
        return box;
    }
}
