package net.frogmouth.rnd.eofff.isobmff.iref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Factory for ItemLocationReference.
 *
 * <p>This should not be invoked directly - it is for the service provider.
 */
@com.google.auto.service.AutoService(ItemReferenceFactory.class)
public class ItemLocationReferenceFactory implements ItemReferenceFactory {

    @Override
    public FourCC getFourCC() {
        return ItemLocationReference.ILOC_ATOM;
    }

    @Override
    public SingleItemReferenceBox makeItemReference(FourCC referenceType) {
        return new ItemLocationReference();
    }
}
