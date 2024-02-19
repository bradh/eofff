package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.grpl.AbstractEntityToGroupBox;
import net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroup;

public class BurstEntityToGroupBox extends AbstractEntityToGroupBox implements EntityToGroup {

    public static final FourCC BRST_ATOM = new FourCC("brst");

    public BurstEntityToGroupBox() {
        super(BRST_ATOM);
    }

    @Override
    public String getFullName() {
        return "Burst";
    }
}
