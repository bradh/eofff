/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package net.frogmouth.rnd.eofff.cicp;

/**
 * Colour Primaries.
 *
 * <p>ColourPrimaries indicates the chromaticity coordinates of the source colour primaries in terms
 * of the CIE 1931 definition of x and y as specified by ISO 11664-1.
 */
public enum ColourPrimary {
    RESERVED(0, "Reserved for future use"),
    BT_709(1, "BT.709"),
    /**
     * Unspecified.
     *
     * <p>Image characteristics are unknown or are determined by the application.
     */
    UNSPECIFIED(2, "Unspecified"),
    BT_470_M(4, "BT.470 System M (historical)"),
    BT_470_B_G(5, "BT.470 System B, G (historical)"),
    BT_601(6, "BT.601"),
    SMPTE_240(7, "SMPTE 240"),
    GENERIC_FILM(8, "Generic film (colour filters using illuminant C)"),
    BT_2020(9, "BT.2020, BT.2100"),
    XYZ(10, "SMPTE 428 (CIE 1921 XYZ)"),
    SMPTE_431(11, "SMPTE RP 431-2"),
    SMPTE_432(12, "SMPTE EG 432-1"),
    EBU_3213(22, "EBU Tech. 3213-E");

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
    private ColourPrimary(int encodedValue, String description) {
        this.encodedValue = encodedValue;
        this.description = description;
    }

    /**
     * Encoded value.
     *
     * @return the encoded value, as used to represent this colour primary
     */
    public int getEncodedValue() {
        return encodedValue;
    }

    /**
     * Description.
     *
     * @return descriptive text for this colour primary, intended for human display.
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
    public static ColourPrimary lookup(final int value) {
        for (ColourPrimary cp : ColourPrimary.values()) {
            if (cp.encodedValue == value) {
                return cp;
            }
        }
        return RESERVED;
    }
}
