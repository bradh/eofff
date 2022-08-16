package net.frogmouth.rnd.eofff.isobmff.tref;

import java.util.ArrayList;
import java.util.List;

public class TrackReferenceBoxBuilder {

    private final List<TrackReferenceTypeBox> references = new ArrayList<>();

    public TrackReferenceBoxBuilder() {}

    public TrackReferenceBoxBuilder withReference(TrackReferenceTypeBox reference) {
        this.references.add(reference);
        return this;
    }

    public TrackReferenceBox build() {

        TrackReferenceBox box = new TrackReferenceBox();
        for (TrackReferenceTypeBox reference : references) {
            box.addEntry(reference);
        }
        return box;
    }
}
