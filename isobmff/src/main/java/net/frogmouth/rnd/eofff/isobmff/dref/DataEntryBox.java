package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public abstract class DataEntryBox extends FullBox {

    public static final int MEDIA_DATA_IN_SAME_FILE_FLAG = 0x000001;

    public DataEntryBox(long size, FourCC name) {
        super(size, name);
    }

    public abstract boolean isSupportedVersion(int version);
}
