package net.frogmouth.rnd.eofff.isobmff.ftyp;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class Brand extends FourCC {

    /**
     * ISO Media File Brand.
     *
     * <p>This is {@code isom}, indicating conformance with the first version of MPEG-4.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.2 for interpretation.
     */
    public static final Brand ISOM = new Brand("isom");

    /**
     * AVC Extensions Brand.
     *
     * <p>This is {@code avc}, indicating conformance with the ‘AVC Extensions’ in ISO/IEC
     * 14496-12:2015(E) subclauses 8.6.4 and 8.9.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.3 for interpretation.
     */
    public static final Brand AVC1 = new Brand("avc1");

    /**
     * ISO Version 2 Media File Brand.
     *
     * <p>This is {@code iso2}, indicating conformance with the second version of MPEG-4.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.4 for interpretation.
     */
    public static final Brand ISO2 = new Brand("iso2");

    /**
     * MPEG-7 Media File Brand.
     *
     * <p>This is {@code mp71}.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.5 for interpretation.
     */
    public static final Brand MP71 = new Brand("mp71");

    /**
     * ISO3 Media File Brand.
     *
     * <p>This is {@code iso3}.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.6 for interpretation.
     */
    public static final Brand ISO3 = new Brand("iso3");

    /**
     * ISO4 Media File Brand.
     *
     * <p>This is {@code iso4}.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.7 for interpretation.
     */
    public static final Brand ISO4 = new Brand("iso4");

    /**
     * ISO5 Media File Brand.
     *
     * <p>This is {@code iso5}.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.8 for interpretation.
     */
    public static final Brand ISO5 = new Brand("iso5");

    /**
     * ISO6 Media File Brand.
     *
     * <p>This is {@code iso6}.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.9 for interpretation.
     */
    public static final Brand ISO6 = new Brand("iso6");

    /**
     * ISO7 Media File Brand.
     *
     * <p>This is {@code iso7}.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.10 for interpretation.
     */
    public static final Brand ISO7 = new Brand("iso7");

    /**
     * ISO8 Media File Brand.
     *
     * <p>This is {@code iso8}.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.11 for interpretation.
     */
    public static final Brand ISO8 = new Brand("iso8");

    /**
     * ISO9 Media File Brand.
     *
     * <p>This is {@code iso9}.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section E.12 for interpretation.
     */
    public static final Brand ISO9 = new Brand("iso9");

    public Brand(int code) {
        super(code);
    }

    public Brand(String string) {
        super(string);
    }
}
