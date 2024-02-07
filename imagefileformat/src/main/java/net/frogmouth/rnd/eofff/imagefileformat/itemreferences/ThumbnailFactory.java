package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Factory for Thumbnail.
 *
 * <p>This should not be invoked directly - it is for the service provider.
 */
@com.google.auto.service.AutoService(ItemReferenceFactory.class)
public class ThumbnailFactory implements ItemReferenceFactory {

    @Override
    public FourCC getFourCC() {
        return Thumbnail.THMB_ATOM;
    }

    @Override
    public SingleItemReferenceBox makeItemReference(FourCC referenceType) {
        return new Thumbnail();
    }
}
