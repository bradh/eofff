/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package net.frogmouth.rnd.eofff.cicp;

/**
 * Transfer characteristics.
 *
 * <p>Transfer Characteristics either indicates the reference opto-electronic transfer
 * characteristic function of the source picture as a function of a source input linear optical
 * intensity input L<sub>c</sub> with a nominal real-valued range of 0 to 1 or indicates the inverse
 * of the reference electro-optical transfer characteristic function as a function of an output
 * linear optical intensity Lo with a nominal real-valued range of 0 to 1.
 */
public enum TransferCharacteristics {
    RESERVED(0, "For future use"),

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
    SMPTE_240(7, "SMPTE 240 M"),
    LINEAR(8, "Linear"),
    LOG_100(9, "Logarithmic (100 : 1 range)"),
    LOG_100_SQRT10(10, "Logarithmic (100 * Sqrt(10) : 1 range)"),
    IEC_61966(11, "IEC 61966-2-4"),
    BT_1361(12, "BT.1361"),
    SRGB(13, "sRGB or sYCC"),
    BT_2020_10_BIT(14, "BT.2020 10-bit systems"),
    BT_2020_12_BIT(15, "BT.2020 12-bit systems"),
    SMPTE_2084(16, "SMPTE ST 2084, ITU BT.2100 PQ"),
    SMPTE_428(17, "SMPTE ST 428"),
    HLG(18, "BT.2100 HLG, ARIB STD-B67");

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
    private TransferCharacteristics(int encodedValue, String description) {
        this.encodedValue = encodedValue;
        this.description = description;
    }

    /**
     * Encoded value.
     *
     * @return the encoded value, as used to represent this transfer characteristics
     */
    public int getEncodedValue() {
        return encodedValue;
    }

    /**
     * Description.
     *
     * @return descriptive text for this transfer characteristic, intended for human display.
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
    public static TransferCharacteristics lookup(final int value) {
        for (TransferCharacteristics tc : TransferCharacteristics.values()) {
            if (tc.encodedValue == value) {
                return tc;
            }
        }
        return RESERVED;
    }
}
