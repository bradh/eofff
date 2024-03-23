package net.frogmouth.rnd.eofff.imagefileformat.properties.oinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import net.frogmouth.rnd.eofff.nalvideo.lhevc.oinf.OperatingPointsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class OperatingPointsInformationPropertyParser extends ItemFullPropertyParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(OperatingPointsInformationPropertyParser.class);

    public OperatingPointsInformationPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return OperatingPointsInformationProperty.OINF_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        OperatingPointsInformationProperty box = new OperatingPointsInformationProperty();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as unknown property.", version);
            return parseAsUnknownProperty(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        OperatingPointsRecord opInfo = OperatingPointsRecord.parseFrom(parseContext, boxSize);
        box.setOpInfo(opInfo);
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
