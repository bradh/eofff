package net.frogmouth.rnd.eofff.cenc.iaux;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class ItemAuxiliaryInformationBoxParser extends ItemFullPropertyParser
        implements PropertyParser {

    private static final Logger LOG =
            LoggerFactory.getLogger(ItemAuxiliaryInformationBoxParser.class);

    public ItemAuxiliaryInformationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemAuxiliaryInformationBox.IAUX_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemAuxiliaryInformationBox box = new ItemAuxiliaryInformationBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        box.setFlags(parseFlags(parseContext));
        box.setAuxInfoType(parseContext.readFourCC());
        box.setAuxInfoTypeParameter(parseContext.readUnsignedInt32());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
