package net.frogmouth.rnd.eofff.isobmff.ftyp;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * File Type Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 4.3.
 */
public class FileTypeBox extends GeneralTypeBox {

    public FileTypeBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "File Type Box";
    }
}
