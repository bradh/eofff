package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

// TODO: move to isobmff module
public class GroupsListBox extends AbstractContainerBox {

    public static final FourCC GRPL_ATOM = new FourCC("grpl");

    public GroupsListBox() {
        super(GRPL_ATOM);
    }

    public GroupsListBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "GroupsListBox";
    }
}
