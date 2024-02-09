package net.frogmouth.rnd.eofff.isobmff.iprp;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ItemPropertyContainerBoxParser extends BoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemPropertyContainerBoxParser.class);

    public ItemPropertyContainerBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemPropertyContainerBox.IPCO_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemPropertyContainerBox box = new ItemPropertyContainerBox();
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            long initialPropertyPosition = parseContext.getCursorPosition();
            long propertyBoxSize = parseContext.readUnsignedInt32();
            FourCC propertyBoxName = parseContext.readFourCC();
            LOG.trace("Got property: {}", propertyBoxName);
            PropertyParser parser = PropertyFactoryManager.getParser(propertyBoxName);
            box.addProperty(
                    parser.parse(
                            parseContext,
                            initialPropertyPosition,
                            propertyBoxSize,
                            propertyBoxName));
        }
        return box;
    }
}