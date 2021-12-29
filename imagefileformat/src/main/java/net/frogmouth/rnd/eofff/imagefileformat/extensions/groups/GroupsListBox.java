package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class GroupsListBox extends AbstractContainerBox {

    public GroupsListBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "GroupsListBox";
    }
}
