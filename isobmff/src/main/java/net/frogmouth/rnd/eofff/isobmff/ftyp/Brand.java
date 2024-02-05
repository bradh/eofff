package net.frogmouth.rnd.eofff.isobmff.ftyp;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class Brand extends FourCC {

    /**
     * ISO Media File Brand.
     *
     * <p>This is {@code isom}, indicating conformance with the first version of MPEG-4.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.2 for interpretation.
     */
    public static final Brand ISOM = new Brand("isom");

    /**
     * AVC Extensions Brand.
     *
     * <p>This is {@code avc}, indicating conformance with the ‘AVC Extensions’ in ISO/IEC
     * 14496-12:2015(E) subclauses 8.6.4 and 8.9.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.3 for interpretation.
     */
    public static final Brand AVC1 = new Brand("avc1");

    /**
     * ISO Version 2 Media File Brand.
     *
     * <p>This is {@code iso2}, indicating conformance with the second version of MPEG-4.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.4 for interpretation.
     */
    public static final Brand ISO2 = new Brand("iso2");

    /**
     * MPEG-7 Media File Brand.
     *
     * <p>This is {@code mp71}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.5 for interpretation.
     */
    public static final Brand MP71 = new Brand("mp71");

    /**
     * ISO3 Media File Brand.
     *
     * <p>This is {@code iso3}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.6 for interpretation.
     */
    public static final Brand ISO3 = new Brand("iso3");

    /**
     * ISO4 Media File Brand.
     *
     * <p>This is {@code iso4}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.7 for interpretation.
     */
    public static final Brand ISO4 = new Brand("iso4");

    /**
     * ISO5 Media File Brand.
     *
     * <p>This is {@code iso5}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.8 for interpretation.
     */
    public static final Brand ISO5 = new Brand("iso5");

    /**
     * ISO6 Media File Brand.
     *
     * <p>This is {@code iso6}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.9 for interpretation.
     */
    public static final Brand ISO6 = new Brand("iso6");

    /**
     * ISO7 Media File Brand.
     *
     * <p>This is {@code iso7}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.10 for interpretation.
     */
    public static final Brand ISO7 = new Brand("iso7");

    /**
     * ISO8 Media File Brand.
     *
     * <p>This is {@code iso8}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.11 for interpretation.
     */
    public static final Brand ISO8 = new Brand("iso8");

    /**
     * ISO9 Media File Brand.
     *
     * <p>This is {@code iso9}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.12 for interpretation.
     */
    public static final Brand ISO9 = new Brand("iso9");

    /**
     * ISOA Media File Brand.
     *
     * <p>This is {@code isoa}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.13 for interpretation.
     */
    public static final Brand ISOA = new Brand("isoa");

    /**
     * ISOB Media File Brand.
     *
     * <p>This is {@code isob}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.14 for interpretation.
     */
    public static final Brand ISOB = new Brand("isob");

    /**
     * RELO Media File Brand.
     *
     * <p>This is {@code relo}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.15 for interpretation.
     */
    public static final Brand RELO = new Brand("relo");

    /**
     * ISOC Media File Brand.
     *
     * <p>This is {@code isoc}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.16 for interpretation.
     */
    public static final Brand ISOC = new Brand("isoc");

    /**
     * COMP Media File Brand.
     *
     * <p>This is {@code comp}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.17 for interpretation.
     */
    public static final Brand COMP = new Brand("comp");

    /**
     * UNIF Media File Brand.
     *
     * <p>This is {@code unif}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section E.18 for interpretation.
     */
    public static final Brand UNIF = new Brand("unif");

    /**
     * MIF1 Image File Brand.
     *
     * <p>This is (@code mif1).
     *
     * <p>See ISO/IEC 23008-12:2022(E) Section 10.2.2 for interpretation.
     */
    public static Brand MIF1 = new Brand("mif1");

    /**
     * MIF2 Image File Brand.
     *
     * <p>This is (@code mif2).
     *
     * <p>See ISO/IEC 23008-12:2022(E) Section 10.2.3 for interpretation.
     */
    public static Brand MIF2 = new Brand("mif2");

    /**
     * MIAF Image File Brand.
     *
     * <p>This is (@code miaf).
     *
     * <p>See ISO/IEC 23000-22:2019(E) Section 10.1 for interpretation.
     */
    public static Brand MIAF = new Brand("miaf");

    public Brand(int code) {
        super(code);
    }

    public Brand(String string) {
        super(string);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Brand other = (Brand) obj;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
