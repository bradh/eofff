package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class DataEntryUrnBox extends DataEntryBox {

    public DataEntryUrnBox(long size) {
        super(size, new FourCC("urn "));
    }

    @Override
    public boolean isSupportedVersion(int version) {
        return (version == 0);
    }

    void setName(String readNullDelimitedString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setLocation(String readNullDelimitedString) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
