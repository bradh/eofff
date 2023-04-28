package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class AlbumCollection extends AbstractEntityToGroupBox {

    public static final FourCC ALBC_ATOM = new FourCC("albc");

    public AlbumCollection() {
        super(ALBC_ATOM);
    }

    @Override
    public String getFullName() {
        return "AlbumCollection";
    }
}
