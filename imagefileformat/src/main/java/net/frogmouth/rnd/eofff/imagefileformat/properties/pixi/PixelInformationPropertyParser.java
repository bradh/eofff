package net.frogmouth.rnd.eofff.imagefileformat.properties.pixi;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class PixelInformationPropertyParser extends ItemFullPropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(PixelInformationPropertyParser.class);

    public PixelInformationPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return PixelInformationProperty.PIXI_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        PixelInformationProperty box = new PixelInformationProperty();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as unknown property.", version);
            return parseAsUnknownProperty(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        int numChannels = parseContext.readUnsignedInt8();
        for (int i = 0; i < numChannels; i++) {
            box.addChannel(parseContext.readUnsignedInt8());
        }
        if (version == 1) {
            for (int i = 0; i < numChannels; i++) {
                SupplementalChannelInfo supplementalChannelInfo = new SupplementalChannelInfo();
                int infoBits = parseContext.readUnsignedInt8();
                int channelIDC = infoBits >> 5;
                supplementalChannelInfo.setChannelIDC(channelIDC);
                int channelDataType = (infoBits & 0x18) >> 3;
                supplementalChannelInfo.setChannelDataType(channelDataType);
                if (box.hasSubsampling()) {
                    // TODO: parse out
                }
                if ((infoBits & 0b00000100) != 0) {
                    String channelLabel = parseContext.readNullDelimitedString(boxSize);
                    supplementalChannelInfo.setChannelLabel(channelLabel);
                }
                box.addExtraInfo(supplementalChannelInfo);
            }
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
