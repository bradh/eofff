package net.frogmouth.rnd.eofff.uncompressed.uncc;

/**
 * Component Format.
 *
 * <p>The binary representation of each value in a component.
 */
public enum ComponentFormat {
    /**
     * Reserved value.
     *
     * <p>This enumeration value is not valid, and typically indicates a non-conformant file. This
     * value should not be intentionally used.
     */
    Reserved(0xFF, "Reserved"),

    /**
     * Unsigned integer.
     *
     * <p>The value is an unsigned integer, coded on component_bit_depth bits (e.g. 8 bit unsigned).
     */
    UnsignedInteger(0, "Unsigned integer"),

    /**
     * IEEE 754 floating point.
     *
     * <p>Component value is an IEEE 754 binary float number coded on component_bit_depth bits
     * (e.g., if component_bit_depth is 16, then the component value is coded as IEEE 754
     * “binary16”). For this component format, component_bit_depth values shall be 16, 32, 64, 128
     * or 256; other values are forbidden.
     */
    FloatingPoint(1, "Floating point"),

    /**
     * IEEE 754 complex floating point.
     *
     * <p>Component value is a complex number coded on component_bit_depth bits, where the first
     * component_bit_depth/2 bits shall represent the real part and the next component_bit_depth/2
     * bits shall represent the imaginary part. Each part shall be coded as an IEEE 754 binary float
     * of the size component_bit_depth/2. For this component format, component_bit_depth values
     * shall be 32, 64, 128 or 256; other values are forbidden.
     */
    ComplexFloatingPoint(2, "Complex floating point"),

    /**
     * Signed integer.
     *
     * <p>The value is a signed integer, coded on component_bit_depth bits (e.g. 8 bit signed).
     */
    SignedInteger(3, "Signed integer");

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
    private ComponentFormat(int encodedValue, String description) {
        this.encodedValue = encodedValue;
        this.description = description;
    }

    /**
     * Encoded value.
     *
     * @return the encoded value, as used to represent this component format
     */
    public int getEncodedValue() {
        return encodedValue;
    }

    /**
     * Description.
     *
     * @return descriptive text for this component format intended for human display.
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
    public static ComponentFormat lookup(final int value) {
        for (ComponentFormat componentFormat : ComponentFormat.values()) {
            if (componentFormat.encodedValue == value) {
                return componentFormat;
            }
        }
        return Reserved;
    }
}
