package net.frogmouth.rnd.eofff.isobmff.tref;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackReferenceBoxBuilder {

    private final List<TrackReferenceTypeBox> references = new ArrayList<>();

    public TrackReferenceBoxBuilder() {}

    public TrackReferenceBoxBuilder withReference(TrackReferenceTypeBox reference) {
        this.references.add(reference);
        return this;
    }

    public TrackReferenceBox build() {
        int size = Integer.BYTES + FourCC.BYTES;
        for (TrackReferenceTypeBox reference : references) {
            size += reference.getSize();
        }
        TrackReferenceBox box = new TrackReferenceBox(size, new FourCC("tref"));
        for (TrackReferenceTypeBox reference : references) {
            box.addEntry(reference);
        }
        return box;
    }
}
