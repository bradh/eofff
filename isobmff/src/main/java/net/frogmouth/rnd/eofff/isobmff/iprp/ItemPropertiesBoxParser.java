package net.frogmouth.rnd.eofff.isobmff.iprp;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ItemPropertiesBoxParser extends BoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemPropertiesBoxParser.class);

    public ItemPropertiesBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemPropertiesBox.IPRP_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemPropertiesBox box = new ItemPropertiesBox();
        Box propertiesBox = parseContext.parseBox();
        if (!(propertiesBox instanceof ItemPropertyContainerBox)) {
            LOG.error(
                    "Err, wrong box type, expecting ItemPropertyContainerBox, but got {}",
                    propertiesBox.getFourCC().toString());
            return box;
        }
        box.setItemProperties((ItemPropertyContainerBox) propertiesBox);
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            Box associationBox = parseContext.parseBox();
            if (!(associationBox instanceof ItemPropertyAssociation)) {
                LOG.error(
                        "Err, wrong box type, expecting ItemPropertyAssociation, but got {}",
                        associationBox.getFourCC().toString());
                return box;
            }
            box.addItemPropertyAssociation((ItemPropertyAssociation) associationBox);
        }
        return box;
    }
}