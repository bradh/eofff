package net.frogmouth.rnd.eofff.isobmff.meta.property;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class ItemFullProperty extends FullBox implements AbstractItemProperty {

    public ItemFullProperty(long size, FourCC name) {
        super(size, name);
    }
}
