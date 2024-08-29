package net.frogmouth.rnd.eofff.ogc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class ModelTransformationPropertyParser implements PropertyParser {

    private static final Logger LOG =
            LoggerFactory.getLogger(ModelTransformationPropertyParser.class);

    public ModelTransformationPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return ModelTransformationProperty.MTXF_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        int version = parseContext.readByte();
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        int flags = parseFlags(parseContext);
        ModelTransformationProperty box = new ModelTransformationProperty();
        box.setVersion(version);
        box.setFlags(flags);
        if (flags == 0x00) {
            // TODO: 3D case
        } else if (flags == 0x01) {
            box.setM00(parseContext.readDouble64());
            box.setM01(parseContext.readDouble64());
            box.setM03(parseContext.readDouble64());
            box.setM10(parseContext.readDouble64());
            box.setM11(parseContext.readDouble64());
            box.setM13(parseContext.readDouble64());
            return box;
        }
        return null;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }
}
