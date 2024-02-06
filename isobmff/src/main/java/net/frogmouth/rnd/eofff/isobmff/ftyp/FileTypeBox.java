package net.frogmouth.rnd.eofff.isobmff.ftyp;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * File Type Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 4.3.
 */
public class FileTypeBox extends GeneralTypeBox {

    public static final FourCC FTYP_ATOM = new FourCC("ftyp");

    public FileTypeBox() {
        super(FTYP_ATOM);
    }

    @Override
    public String getFullName() {
        return "FileTypeBox";
    }
}
