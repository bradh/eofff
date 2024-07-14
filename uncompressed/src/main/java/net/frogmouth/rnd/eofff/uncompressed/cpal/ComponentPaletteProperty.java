package net.frogmouth.rnd.eofff.uncompressed.cpal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * Component Palette Box.
 *
 * <p>See ISO/IEC 23001-17:2024 Section 6.1.2.
 */
public class ComponentPaletteProperty extends ItemFullProperty {

    public static final FourCC CPAL_ATOM = new FourCC("cpal");

    private final List<PaletteComponent> components = new ArrayList<>();
    private byte[][] componentValues;

    public ComponentPaletteProperty() {
        super(CPAL_ATOM);
    }

    @Override
    public String getFullName() {
        return "ComponentPaletteBox";
    }

    public List<PaletteComponent> getComponents() {
        return new ArrayList<>(this.components);
    }

    public void addComponent(PaletteComponent component) {
        this.components.add(component);
    }

    public byte[][] getComponentValues() {
        return componentValues;
    }

    public void setComponentValues(byte[][] componentValues) {
        this.componentValues = componentValues;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        for (PaletteComponent component : this.components) {
            size += component.getNumberOfBytes();
        }
        size += Integer.BYTES;
        // We only support single byte values (possibly padded).
        size += (componentValues.length * componentValues[0].length);
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt16(this.components.size());
        for (PaletteComponent component : this.components) {
            component.writeTo(stream);
        }
        stream.writeUnsignedInt32(componentValues.length);
        for (byte[] componentValue : componentValues) {
            for (int j = 0; j < componentValue.length; j++) {
                stream.writeByte(componentValue[j]);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': ");
        List<String> defns = new ArrayList<>();
        this.components.forEach(
                definition -> {
                    defns.add(definition.toString());
                });
        sb.append(String.join(", ", defns));
        // TODO: values
        return sb.toString();
    }
}
