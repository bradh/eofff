package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class ItemFullProperty extends FullBox implements AbstractItemProperty {

    public ItemFullProperty(FourCC name) {
        super(name);
    }
}
