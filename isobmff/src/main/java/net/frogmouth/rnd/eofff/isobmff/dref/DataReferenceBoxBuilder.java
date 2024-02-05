package net.frogmouth.rnd.eofff.isobmff.dref;

import java.util.ArrayList;
import java.util.List;

public class DataReferenceBoxBuilder {

    private final List<DataEntryBaseBox> dataEntries = new ArrayList<>();

    public DataReferenceBoxBuilder() {}

    public DataReferenceBoxBuilder withDataReference(DataEntryBaseBox entry) {
        this.dataEntries.add(entry);
        return this;
    }

    public DataReferenceBoxBuilder withLocalFileReference() {
        DataEntryBaseBox entry = new DataEntryUrlBox();
        entry.setFlags(DataEntryBaseBox.MEDIA_DATA_IN_SAME_FILE_FLAG);
        this.dataEntries.add(entry);
        return this;
    }

    public DataReferenceBox build() {
        DataReferenceBox box = new DataReferenceBox();
        for (DataEntryBaseBox entry : dataEntries) {
            box.addDataReference(entry);
        }
        return box;
    }
}
