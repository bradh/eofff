package net.frogmouth.rnd.eofff.uncompressed.cpal;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class PaletteComponent {

    private final int componentIndex;
    private final int componentBitDepthMinusOne;
    private final int componentFormat;

    public PaletteComponent(int index, int bit_depth_minus_one, int format) {
        this.componentIndex = index;
        this.componentBitDepthMinusOne = bit_depth_minus_one;
        this.componentFormat = format;
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

    void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt16(this.componentIndex);
        stream.writeByte(this.componentBitDepthMinusOne);
        stream.writeByte(this.componentFormat);
    }

    public int getNumberOfBytes() {
        return Short.BYTES + Byte.BYTES + Byte.BYTES;
    }

    @Override
    public String toString() {
        return "{"
                + componentIndex
                + ", "
                + componentBitDepthMinusOne
                + ", "
                + componentFormat
                + '}';
    }
}
