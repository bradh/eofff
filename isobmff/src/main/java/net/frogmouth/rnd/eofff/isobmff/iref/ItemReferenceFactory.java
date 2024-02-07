package net.frogmouth.rnd.eofff.isobmff.iref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public interface ItemReferenceFactory {
    public FourCC getFourCC();

    public SingleItemReferenceBox makeItemReference(FourCC referenceType);
}
