package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.stsd.VisualSampleEntry;

public class AVCSampleEntry extends VisualSampleEntry {

    public AVCSampleEntry(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "AVCSampleEntry";
    }
}
