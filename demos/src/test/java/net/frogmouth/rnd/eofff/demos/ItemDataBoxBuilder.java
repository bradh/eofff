package net.frogmouth.rnd.eofff.demos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;

public class ItemDataBoxBuilder {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public ItemDataBoxBuilder() {}

    public ItemDataBoxBuilder addData(byte[] data) throws IOException {
        baos.write(data);
        return this;
    }

    public ItemDataBox build() {
        ItemDataBox box = new ItemDataBox();
        box.setData(baos.toByteArray());
        return box;
    }
}
