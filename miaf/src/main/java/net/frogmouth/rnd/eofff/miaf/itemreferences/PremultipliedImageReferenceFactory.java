package net.frogmouth.rnd.eofff.miaf.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Factory for PremultipliedImageReference.
 *
 * <p>This should not be invoked directly - it is for the service provider.
 */
@com.google.auto.service.AutoService(ItemReferenceFactory.class)
public class PremultipliedImageReferenceFactory implements ItemReferenceFactory {

    @Override
    public FourCC getFourCC() {
        return PremultipliedImageReference.PREM_ATOM;
    }

    @Override
    public SingleItemReferenceBox makeItemReference(FourCC referenceType) {
        return new PremultipliedImageReference();
    }
}
