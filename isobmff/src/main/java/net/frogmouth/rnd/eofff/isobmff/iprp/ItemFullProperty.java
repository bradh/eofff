package net.frogmouth.rnd.eofff.isobmff.iprp;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Abstract Item property derived from full box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.11.14.
 *
 * @see ItemProperty
 */
public class ItemFullProperty extends FullBox implements AbstractItemProperty {

    public ItemFullProperty(FourCC name) {
        super(name);
    }
}
