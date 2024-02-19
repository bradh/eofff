package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.grpl.AbstractEntityToGroupBox;
import net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroup;

public class AlbumCollectionEntityToGroupBox extends AbstractEntityToGroupBox
        implements EntityToGroup {

    public static final FourCC ALBC_ATOM = new FourCC("albc");

    public AlbumCollectionEntityToGroupBox() {
        super(ALBC_ATOM);
    }

    @Override
    public String getFullName() {
        return "AlbumCollection";
    }
}
