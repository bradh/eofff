package net.frogmouth.rnd.eofff.isobmff.iprp;

import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ItemProperty extends BaseBox implements AbstractItemProperty {

    public ItemProperty(FourCC property) {
        super(property);
    }
}
