package net.frogmouth.rnd.eofff.jpeg2000.fileformat.cmap;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ComponentMappingBoxParser extends BaseBoxParser {

    @Override
    public FourCC getFourCC() {
        return ComponentMappingBox.CMAP_ATOM;
    }

    @Override
    public ComponentMappingBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ComponentMappingBox cmap = new ComponentMappingBox();
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            int cmp = parseContext.readUnsignedInt16();
            int mtyp = parseContext.readUnsignedInt8();
            int pcol = parseContext.readUnsignedInt8();
            ComponentMapping mapping = new ComponentMapping(cmp, mtyp, pcol);
            cmap.addComponentMapping(mapping);
        }
        return cmap;
    }
}
