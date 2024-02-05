package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class DataEntryBaseBox extends FullBox {

    public static final int MEDIA_DATA_IN_SAME_FILE_FLAG = 0x000001;

    public DataEntryBaseBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "DataEntryBaseBox";
    }

    public boolean isSupportedVersion(int version) {
        return (version == 0);
    }
}
