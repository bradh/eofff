package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class GroupsListBox extends AbstractContainerBox {

    public static final FourCC GRPL_ATOM = new FourCC("grpl");

    public GroupsListBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "GroupsListBox";
    }
}
