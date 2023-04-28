package net.frogmouth.rnd.eofff.uncompressed.cmpd;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentDefinitionBoxParser implements PropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentDefinitionBoxParser.class);

    public ComponentDefinitionBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ComponentDefinitionBox.CMPD_ATOM;
    }

    @Override
    public ComponentDefinitionBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ComponentDefinitionBox box = new ComponentDefinitionBox();
        int component_count = parseContext.readUnsignedInt16();
        for (int i = 0; i < component_count; i++) {
            int component_type = parseContext.readUnsignedInt16();
            String component_type_uri = null;
            if (component_type >= 0x8000) {
                component_type_uri = parseContext.readNullDelimitedString(boxSize);
            }
            ComponentDefinition componentDefinition =
                    new ComponentDefinition(component_type, component_type_uri);
            box.addComponentDefinition(componentDefinition);
        }
        return box;
    }
}
