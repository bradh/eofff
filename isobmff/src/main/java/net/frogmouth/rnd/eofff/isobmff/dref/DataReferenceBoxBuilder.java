package net.frogmouth.rnd.eofff.isobmff.dref;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class DataReferenceBoxBuilder {

    private final List<DataEntryBox> dataEntries = new ArrayList<>();

    public DataReferenceBoxBuilder() {}

    public DataReferenceBoxBuilder withReference(DataEntryBox entry) {
        this.dataEntries.add(entry);
        return this;
    }

    public DataReferenceBoxBuilder withLocalFileReference() {
        DataEntryBox entry = new DataEntryUrlBox(12);
        entry.setFlags(DataEntryBox.MEDIA_DATA_IN_SAME_FILE_FLAG);
        this.dataEntries.add(entry);
        return this;
    }

    public DataReferenceBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (DataEntryBox entry : dataEntries) {
            size += entry.getSize();
        }
        DataReferenceBox box = new DataReferenceBox(size, new FourCC("dref"));
        for (DataEntryBox entry : dataEntries) {
            box.addDataEntryBox(entry);
        }
        return box;
    }
}
