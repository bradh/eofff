package net.frogmouth.rnd.eofff.jpeg2000.fileformat.cmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ComponentMappingBox extends BaseBox {
    public static final FourCC CMAP_ATOM = new FourCC("cmap");

    private final List<ComponentMapping> componentMappings = new ArrayList<>();

    public ComponentMappingBox() {
        super(CMAP_ATOM);
    }

    @Override
    public long getBodySize() {
        return (componentMappings.size() * ComponentMapping.BYTES);
    }

    public List<ComponentMapping> getComponentMappings() {
        return componentMappings;
    }

    public void addComponentMapping(ComponentMapping componentMapping) {
        componentMappings.add(componentMapping);
    }

    @Override
    public String getFullName() {
        return "J2KComponentMapping";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        for (ComponentMapping componentMapping : componentMappings) {
            componentMapping.writeTo(writer);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (ComponentMapping mapping : componentMappings) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(mapping.toString());
        }
        return sb.toString();
    }
}
