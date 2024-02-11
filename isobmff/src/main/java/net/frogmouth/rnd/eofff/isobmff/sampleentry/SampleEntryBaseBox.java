package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleEntryBaseBox extends BaseBox implements SampleEntry {

    public SampleEntryBaseBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "Unimplemented sample entry";
    }
}
