package net.frogmouth.rnd.eofff.uncompressed.uncc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/** Component entry within a uncompressed frame configuration. */
public class Component {

    public static final int BYTES = Short.BYTES + Byte.BYTES + Byte.BYTES + Byte.BYTES;

    private final int componentIndex;
    private final int componentBitDepthMinusOne;
    private final int componentFormat;
    private final int componentAlignSize;

    public Component(
            int component_index,
            int component_bit_depth_minus_one,
            int component_format,
            int component_align_size) {
        this.componentIndex = component_index;
        this.componentBitDepthMinusOne = component_bit_depth_minus_one;
        this.componentFormat = component_format;
        this.componentAlignSize = component_align_size;
    }

    public int getComponentIndex() {
        return componentIndex;
    }

    public int getComponentBitDepthMinusOne() {
        return componentBitDepthMinusOne;
    }

    public int getComponentFormat() {
        return componentFormat;
    }

    public int getComponentAlignSize() {
        return componentAlignSize;
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt16(componentIndex);
        stream.writeByte(this.componentBitDepthMinusOne);
        stream.writeByte(this.componentFormat);
        stream.writeByte(this.componentAlignSize);
    }
}
