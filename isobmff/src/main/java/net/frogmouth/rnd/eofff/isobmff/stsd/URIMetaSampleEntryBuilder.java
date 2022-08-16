package net.frogmouth.rnd.eofff.isobmff.stsd;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class URIMetaSampleEntryBuilder extends AbstractContainerBoxBuilder<URIMetaSampleEntry> {

    private int dataReferenceIndex;

    public URIMetaSampleEntryBuilder() {}

    public URIMetaSampleEntryBuilder withDataReferenceIndex(int index) {
        this.dataReferenceIndex = index;
        return this;
    }

    @Override
    public URIMetaSampleEntry build() {
        int size = Integer.BYTES + FourCC.BYTES;
        size += 6 * Byte.BYTES;
        size += Short.BYTES;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        URIMetaSampleEntry box = new URIMetaSampleEntry(new FourCC("urim"));
        box.setDataReferenceIndex(dataReferenceIndex);
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
