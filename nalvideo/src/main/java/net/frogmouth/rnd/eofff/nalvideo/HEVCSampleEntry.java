package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

public class HEVCSampleEntry extends VisualSampleEntry {

    public HEVCSampleEntry(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "HEVCSampleEntry";
    }
}
