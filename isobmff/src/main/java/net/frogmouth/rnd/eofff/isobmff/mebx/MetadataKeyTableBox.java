package net.frogmouth.rnd.eofff.isobmff.mebx;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MetadataKeyTableBox extends AbstractContainerBox {

    public static final FourCC KEYS_ATOM = new FourCC("keys");

    public MetadataKeyTableBox() {
        super(KEYS_ATOM);
    }

    @Override
    public String getFullName() {
        return "MetadataKeyTableBox";
    }
}
