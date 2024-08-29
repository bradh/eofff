package net.frogmouth.rnd.eofff.uncompressed.cmpd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;

/**
 * Component Definition Box.
 *
 * <p>See ISO/IEC 23001-17 (DIS) Section 5.1.2.
 */
public class ComponentDefinitionBox extends ItemProperty {

    public static final FourCC CMPD_ATOM = new FourCC("cmpd");

    private final List<ComponentDefinition> componentDefinitions = new ArrayList<>();

    public ComponentDefinitionBox() {
        super(CMPD_ATOM);
    }

    @Override
    public String getFullName() {
        return "ComponentDefinitionBox";
    }

    public List<ComponentDefinition> getComponentDefinitions() {
        return new ArrayList<>(this.componentDefinitions);
    }

    public void addComponentDefinition(ComponentDefinition componentDefinition) {
        this.componentDefinitions.add(componentDefinition);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (ComponentDefinition componentDefinition : this.componentDefinitions) {
            size += componentDefinition.getNumberOfBytes();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(this.componentDefinitions.size());
        for (ComponentDefinition componentDefinition : this.componentDefinitions) {
            componentDefinition.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        List<String> components = new ArrayList<>();
        this.componentDefinitions.forEach(
                definition -> {
                    components.add(definition.toString());
                });
        sb.append(String.join(",", components));
        return sb.toString();
    }
}
