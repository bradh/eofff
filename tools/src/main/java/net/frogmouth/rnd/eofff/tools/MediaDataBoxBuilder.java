package net.frogmouth.rnd.eofff.tools;

import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import org.jmisb.api.klv.ArrayBuilder;

public class MediaDataBoxBuilder {

    private final ArrayBuilder arrayBuilder = new ArrayBuilder();

    public MediaDataBoxBuilder() {}

    public MediaDataBoxBuilder addData(byte[] data) {
        arrayBuilder.append(data);
        return this;
    }

    public MediaDataBox build() {
        byte[] bytes = arrayBuilder.toBytes();

        MediaDataBox box = new MediaDataBox();
        box.setData(bytes);
        return box;
    }
}
