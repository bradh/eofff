package net.frogmouth.rnd.eofff.cenc.auxr;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Factory for AuxiliaryReference.
 *
 * <p>This should not be invoked directly - it is for the service provider.
 */
@com.google.auto.service.AutoService(ItemReferenceFactory.class)
public class AuxiliaryReferenceFactory implements ItemReferenceFactory {

    @Override
    public FourCC getFourCC() {
        return AuxiliaryReference.AUXR_ATOM;
    }

    @Override
    public SingleItemReferenceBox makeItemReference(FourCC referenceType) {
        return new AuxiliaryReference();
    }
}
