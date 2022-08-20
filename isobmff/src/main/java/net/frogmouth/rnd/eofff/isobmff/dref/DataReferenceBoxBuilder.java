package net.frogmouth.rnd.eofff.isobmff.dref;

import java.util.ArrayList;
import java.util.List;

public class DataReferenceBoxBuilder {

    private final List<DataEntryBox> dataEntries = new ArrayList<>();

    public DataReferenceBoxBuilder() {}

    public DataReferenceBoxBuilder withReference(DataEntryBox entry) {
        this.dataEntries.add(entry);
        return this;
    }

    public DataReferenceBoxBuilder withLocalFileReference() {
        DataEntryBox entry = new DataEntryUrlBox();
        entry.setFlags(DataEntryBox.MEDIA_DATA_IN_SAME_FILE_FLAG);
        this.dataEntries.add(entry);
        return this;
    }

    public DataReferenceBox build() {
        DataReferenceBox box = new DataReferenceBox();
        for (DataEntryBox entry : dataEntries) {
            box.addDataEntryBox(entry);
        }
        return box;
    }
}
