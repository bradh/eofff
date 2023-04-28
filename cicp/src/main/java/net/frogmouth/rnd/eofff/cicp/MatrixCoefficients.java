package net.frogmouth.rnd.eofff.cicp;

/**
 * Matrix Coefficients.
 *
 * <p>MatrixCoefficients describes the matrix coefficients used in deriving luma and chroma signals
 * from the green, blue and red or X, Y and Z primaries,
 */
public enum MatrixCoefficients {
    IDENTITY(0, "Identity matrix"),
    BT_709(1, "BT.709"),
    UNSPECIFIED(2, "Unspecified"),
    RESERVED(3, "(Reserved)"),
    FCC(4, "US FCC 73.628"),
    BT_470_B_G(5, "BT.470 System B, G (historical)"),
    BT_601(6, "BT.601"),
    SMPTE_240(7, "SMPTE 240 M"),
    SMPTE_YCGCO(8, "YCgCo"),
    BT_2020_NCL(9, "BT.2020 non-constant luminance, BT.2100 YCbCr"),
    BT_2020_CL(10, "BT.2020 constant luminance"),
    SMPTE_2085(11, "SMPTE ST 2085 YDzDx"),
    CHROMAT_NCL(12, "Chromaticity-derived non-constant luminance"),
    CHROMAT_CL(13, "Chromaticity-derived constant luminance"),
    ICTCP(14, "BT.2100 ICtCp");

    private final int encodedValue;
    private final String description;

    /**
     * Constructor.
     *
     * <p>This is private to the enumeration initialisation.
     *
     * @param encodedValue the encoded (8-bit integer) value.
     * @param description descriptive text, intended for human display
     */
    private MatrixCoefficients(int encodedValue, String description) {
        this.encodedValue = encodedValue;
        this.description = description;
    }

    /**
     * Encoded value.
     *
     * @return the encoded value, as used to represent these matrix coefficients
     */
    public int getEncodedValue() {
        return encodedValue;
    }

    /**
     * Description.
     *
     * @return descriptive text for these matrix coefficients, intended for human display.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Look up an enumeration value from the encoded value.
     *
     * @param value the encoded value
     * @return the corresponding enumeration value, or RESERVED if not known.
     */
    public static MatrixCoefficients lookup(final int value) {
        for (MatrixCoefficients mc : MatrixCoefficients.values()) {
            if (mc.encodedValue == value) {
                return mc;
            }
        }
        return RESERVED;
    }
}
