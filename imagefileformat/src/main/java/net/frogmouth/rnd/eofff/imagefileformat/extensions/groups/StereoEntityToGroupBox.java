package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.grpl.AbstractEntityToGroupBox;
import net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroup;

public class StereoEntityToGroupBox extends AbstractEntityToGroupBox implements EntityToGroup {

    public static final FourCC STER_ATOM = new FourCC("ster");

    public StereoEntityToGroupBox() {
        super(STER_ATOM);
    }

    @Override
    public String getFullName() {
        return "StereoGroup";
    }
}
