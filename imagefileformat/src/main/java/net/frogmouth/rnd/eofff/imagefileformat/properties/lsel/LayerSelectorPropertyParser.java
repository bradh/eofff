package net.frogmouth.rnd.eofff.imagefileformat.properties.lsel;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class LayerSelectorPropertyParser extends ItemFullPropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(LayerSelectorPropertyParser.class);

    public LayerSelectorPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return LayerSelectorProperty.LSEL_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        LayerSelectorProperty box = new LayerSelectorProperty();
        box.setLayerID(parseContext.readUnsignedInt16());
        return box;
    }
}
