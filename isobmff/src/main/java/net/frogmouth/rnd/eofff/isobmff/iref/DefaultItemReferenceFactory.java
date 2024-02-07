package net.frogmouth.rnd.eofff.isobmff.iref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Default Item Reference Factory.
 *
 * <p>Factory for item references where no match is found.
 */
public class DefaultItemReferenceFactory implements ItemReferenceFactory {

    public DefaultItemReferenceFactory() {}

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "Default item reference getFourCC() should be overridden");
    }

    @Override
    public SingleItemReferenceBox makeItemReference(FourCC referenceType) {
        return new DefaultItemReference(referenceType);
    }
}
