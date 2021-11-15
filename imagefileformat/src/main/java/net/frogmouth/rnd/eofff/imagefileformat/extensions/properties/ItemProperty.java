package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ItemProperty extends BaseBox implements AbstractItemProperty {

    public ItemProperty(long size, FourCC property) {
        super(size, property);
    }
}
