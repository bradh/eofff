package net.frogmouth.rnd.eofff.isobmff.iprp;

import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Abstract Item property derived from base box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.11.14.
 *
 * @see ItemFullProperty
 */
public class ItemProperty extends BaseBox implements AbstractItemProperty {

    public ItemProperty(FourCC property) {
        super(property);
    }
}
