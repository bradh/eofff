package net.frogmouth.rnd.eofff.isobmff.iref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/** Fallback item reference. */
public class DefaultItemReference extends SingleItemReferenceBox {

    public DefaultItemReference(FourCC referenceType) {
        super(referenceType);
    }

    @Override
    public String getFullName() {
        return "Unsupported item reference";
    }
}
