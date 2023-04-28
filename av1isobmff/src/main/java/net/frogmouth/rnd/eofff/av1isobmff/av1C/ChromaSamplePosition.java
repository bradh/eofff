package net.frogmouth.rnd.eofff.av1isobmff.av1C;

import static net.frogmouth.rnd.eofff.cicp.MatrixCoefficients.RESERVED;

/**
 * Chroma Sample Position.
 *
 * <p>Specifies the chroma sample position for subsampled streams.
 */
public enum ChromaSamplePosition {
    /**
     * Unknown.
     *
     * <p>Unknown (in this case the source video transfer function must be signaled outside the AV1
     * bitstream).
     */
    UNKNOWN(0, "Unknown"),

    /**
     * Vertical.
     *
     * <p>Horizontally co-located with (0, 0) luma sample, vertical position in the middle between
     * two luma samples
     */
    VERTICAL(1, "Vertical"),

    /**
     * Co-located.
     *
     * <p>Co-located with (0, 0) luma sample.
     */
    COLOCATED(2, "Co-located"),

    /**
     * Reserved.
     *
     * <p>This value is not meaningful, and should not be intentionally created.
     */
    RESERVED(3, "Reserved");

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
    private ChromaSamplePosition(int encodedValue, String description) {
        this.encodedValue = encodedValue;
        this.description = description;
    }

    /**
     * Encoded value.
     *
     * @return the encoded value, as used to represent this position.
     */
    public int getEncodedValue() {
        return encodedValue;
    }

    /**
     * Description.
     *
     * @return descriptive text for this position, intended for human display.
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
    public static ChromaSamplePosition lookup(final int value) {
        for (ChromaSamplePosition csp : ChromaSamplePosition.values()) {
            if (csp.encodedValue == value) {
                return csp;
            }
        }
        return RESERVED;
    }
}
