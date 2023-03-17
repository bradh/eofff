package net.frogmouth.rnd.eofff.uncompressed.uncc;

/**
 * Sampling type.
 *
 * <p>All components in a frame either have the same dimensions or use pre-defined sampling modes,
 * indicated by the {@code sampling_type} field.
 */
public enum SamplingType {
    /**
     * Reserved value.
     *
     * <p>This enumeration value is not valid, and typically indicates a non-conformant file. This
     * value should not be intentionally used.
     */
    Reserved(0xFF, "Reserved"),

    /**
     * No subsampling.
     *
     * <p>All components have the same width and height as the frame.
     *
     * <p>NOTE The tile width and height are not restricted. Derived specifications may further
     * restrict this, for example to enforce width and height to be multiples of 2 in case Y, U and
     * V components are present.
     */
    NoSubsampling(0, "No subsampling"),

    /**
     * YCbCr 4:2:2 subsampling.
     *
     * <p>This value shall only be set if all three component_type values ‘Y’, ‘U’ and ‘V’ are
     * present in the component list and if the tile width is a multiple of 2.
     *
     * <p>If this value is used, the interleave_type field shall be set to 0 (Component
     * Interleaving), 2 (Mixed Interleaving) or 5 (Multi-Y Pixel Interleaving).
     *
     * <p>The width and height of the ‘Y’ component are the width and height of the frame. The
     * height of the ‘U’ and ‘V’ component is the same as the height of the ‘Y’ component. The width
     * of the ‘U’ and ‘V’ component is half the width of the ‘Y’ component. Components of other
     * types may be present, and have the same dimension as the ‘Y’ component.
     *
     * <p>Pixels {x,y} and {x+1,y}, with x%2==0, share the same component values ‘U’ and ‘V’.
     */
    YCbCr422(1, "YCbCr 4:2:2 subsampling"),

    /**
     * YCbCr 4:2:0 subsampling.
     *
     * <p>This value shall only be set if all three component_type values ‘Y’, ‘U’ and ‘V’ are
     * present in the component list and if both tile width and height are multiple of 2.
     *
     * <p>If this value is used, the interleave_type field shall be set to 0 (Component
     * Interleaving) or 2 (Mixed Interleaving).
     *
     * <p>The width and height of the ‘Y’ component are the width and height of the frame. The width
     * of the ‘U’ and ‘V’ component is half the width of the ‘Y’ component’. The height of the ‘U’
     * and ‘V’ component is half the height of the ‘Y’ component. Components of other types may be
     * present, and have the same dimension as the ‘Y’ component.
     *
     * <p>Pixels {x,y}, {x+1,y}, {x,y+1} and {x+1,y+1} with x%2==0 and y%2==0, share the same
     * component values ‘U’ and ‘V’.
     */
    YCbCr420(2, "YCbCr 4:2:0 subsampling"),

    /**
     * YCbCr 4:1:1 subsampling.
     *
     * <p>This value shall only be set if all three component_type values ‘Y’, ‘U’ and ‘V’ are
     * present in the component list and if tile width is a multiple of 4.
     *
     * <p>If this value is used, the interleave_type field shall be set to 0 (Component
     * Interleaving), 2 (Mixed Interleaving) or 5 (Multi-Y Pixel Interleaving).
     *
     * <p>The width and height of the ‘Y’ component are the width and height of the frame. The
     * height of the ‘U’ and ‘V’ component is the same as the height of the ‘Y’ component. The width
     * of the ‘U’ and ‘V’ component is the width of the ‘Y’ component divided by 4. Components of
     * other types may be present, and have the same dimension as the ‘Y’ component.
     *
     * <p>Pixels {x,y} , {x+1,y} , {x+2,y} , {x+3,y}, with x%4==0, share the same component values
     * ‘U’ and ‘V’.
     */
    YCbCr411(3, "YCbCr 4:1:1 subsampling");

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
    private SamplingType(int encodedValue, String description) {
        this.encodedValue = encodedValue;
        this.description = description;
    }

    /**
     * Encoded value.
     *
     * @return the encoded value, as used to represent this sampling type
     */
    public int getEncodedValue() {
        return encodedValue;
    }

    /**
     * Description.
     *
     * @return descriptive text for this sampling type, intended for human display.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Look up an enumeration value from the encoded value.
     *
     * @param value the encoded value
     * @return the corresponding enumeration value.
     */
    public static SamplingType lookup(final int value) {
        for (SamplingType samplingType : SamplingType.values()) {
            if (samplingType.encodedValue == value) {
                return samplingType;
            }
        }
        return Reserved;
    }
}
