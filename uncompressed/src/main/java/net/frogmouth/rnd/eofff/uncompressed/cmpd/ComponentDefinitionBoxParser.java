package net.frogmouth.rnd.eofff.uncompressed.cmpd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
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
        long component_count = parseContext.readUnsignedInt32();
        for (long i = 0; i < component_count; i++) {
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
