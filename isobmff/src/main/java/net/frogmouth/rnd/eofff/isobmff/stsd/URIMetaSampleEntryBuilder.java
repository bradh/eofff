package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class URIMetaSampleEntryBuilder {

    private int dataReferenceIndex;
    private final List<Box> nestedBoxes = new ArrayList<>();

    public URIMetaSampleEntryBuilder() {}

    public URIMetaSampleEntryBuilder withDataReferenceIndex(int index) {
        this.dataReferenceIndex = index;
        return this;
    }

    public URIMetaSampleEntryBuilder withNestedBox(Box box) {
        this.nestedBoxes.add(box);
        return this;
    }

    public URIMetaSampleEntry build() {
        int size = Integer.BYTES + FourCC.BYTES;
        size += 6 * Byte.BYTES;
        size += Short.BYTES;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        URIMetaSampleEntry box = new URIMetaSampleEntry();
        box.setDataReferenceIndex(dataReferenceIndex);
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
