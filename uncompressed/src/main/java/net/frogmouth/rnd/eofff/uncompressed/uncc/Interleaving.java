package net.frogmouth.rnd.eofff.uncompressed.uncc;

public enum Interleaving {
    /**
     * Reserved value.
     *
     * <p>This enumeration value is not valid, and typically indicates a non-conformant file. This
     * value should not be intentionally used.
     */
    Reserved(0xFF, "Reserved"),

    /**
     * Component Interleaving.
     *
     * <p>For a given component, values for all pixels of a tile shall be located sequentially in
     * the sample data. Component values shall be located in the order the components were declared.
     */
    Component(0, "Component Interleaving"),

    /**
     * Pixel Interleaving.
     *
     * <p>For a given pixel, the component values shall be located one after the other, in the order
     * the components are declared, with the first component of a pixel following the last component
     * of the previous pixel.
     *
     * <p>Pixel Interleaving is only permitted when all components are at the same resolution.
     */
    Pixel(1, "Pixel Interleaving"),

    /**
     * Mixed Interleaving.
     *
     * <p>This value shall only be used if sampling_type field value is not 0, if sampling_type
     * value explicitly allows this interleaving mode and if the ‘U’ and ‘V’ components are
     * consecutive in the component list, i.e. ‘V’ component immediately follows ‘U’ component or
     * ‘U’ component immediately follows ‘V’ component.
     *
     * <p>For all components other than ‘U’ and ‘V’, component values for all pixels shall be
     * located as specified for Component Interleaving in the order they are declared. The ‘U’ and
     * ‘V’ component values shall be located as specified by Pixel Interleaving; the first value for
     * component ‘U’, if declared first, or ‘V’ otherwise, shall be located after the last value of
     * the component preceding ‘U’ or ‘V’ if any, or first in the tile otherwise.
     *
     * <p>NOTE This mode is typically used to store YUV 420 data with all Y components followed by
     * interleaved U and V components.
     */
    Mixed(2, "Mixed Interleaving"),

    /**
     * Row Interleaving.
     *
     * <p>For a given row, component values for each component shall be located sequentially, in the
     * order the components are declared. Each non-first row in the tile shall be located after the
     * previous row. If multiple tiles are used, the first component of the first row of a tile
     * shall be located after the last component of the last row of the previous tile.
     *
     * <p>Row Interleaving is only permitted when all components are at the same resolution.
     */
    Row(3, "Row Interleaving"),

    /**
     * Tile-Component Interleaving.
     *
     * <p>For each component, in the order they are declared, the values for each component for all
     * tiles shall be located sequentially, with values within the tile located per each individual
     * tile.
     *
     * <p>This mode shall not be used if only a single tile is defined.
     */
    TileComponent(4, "Tile-Component Interleaving"),

    /**
     * Multi-Y Pixel Interleaving.
     *
     * <p>Component values shall be organized as with interleave_type=1 (Pixel Interleaved), and the
     * list of components shall contain multiple ‘Y’ components representing the multiple ‘Y’
     * component values associated with a single pair of ‘U’ and ’V’ component values.
     *
     * <p>The ‘U’ and ‘V’ components shall only be listed once in the list of components.
     *
     * <p>There are no restrictions on the order of declaration of the ‘Y’ components; derived
     * specifications may further restrict this.
     *
     * <p>Regardless of how the multiple ‘Y’ components are interleaved with the ‘U’ and ‘V’
     * components, the ‘Y’ component values shall be stored in left- to-right, top-to-bottom order.
     *
     * <p>If the chrominance components are subsampled according to 4:2:2 subsampling
     * (sampling_type=1), the component list describes two horizontally consecutive pixels, and thus
     * shall include two ‘Y’ components, representing the two ‘Y’ values associated with a single
     * pair of ‘U’ and ‘V’ component values. For example, {‘U’, ‘Y’, ‘V’, ‘Y’} for sampling_type=1
     * (YUV 4:2:2) indicates that the Y value of the second pixel shall be located after the ‘V’
     * component value.
     *
     * <p>If the chrominance components are subsampled according to 4:1:1 subsampling, the component
     * list describes four horizontally consecutive pixels, and thus shall include four Y
     * components, representing the four Y values associated with a single pair of U and V component
     * values. For example, {‘U’, ‘Y’, ‘Y’, ‘V’, ‘Y’, ‘Y’} for sampling_type=3 (YUV 4:1:1) indicates
     * that the ‘Y’ value of the second pixel shall be located after the ‘Y’ value of the first
     * pixel, that the ‘Y’ value of the third pixel shall be located after the ‘V’ component value
     * and that the ‘Y’ value of the fourth pixel shall be located after the ‘Y’ value of the third
     * pixel.
     *
     * <p>This interleave mode shall not be used for other forms of chrominance subsampling.
     */
    MultiY(5, "Multi-Y Pixel Interleaving");

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
    private Interleaving(int encodedValue, String description) {
        this.encodedValue = encodedValue;
        this.description = description;
    }

    /**
     * Encoded value.
     *
     * @return the encoded value, as used to represent this interleaving type
     */
    public int getEncodedValue() {
        return encodedValue;
    }

    /**
     * Description.
     *
     * @return descriptive text for this interleaving type, intended for human display.
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
    public static Interleaving lookup(final int value) {
        for (Interleaving interleaving : Interleaving.values()) {
            if (interleaving.encodedValue == value) {
                return interleaving;
            }
        }
        return Reserved;
    }
}
