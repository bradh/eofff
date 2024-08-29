package net.frogmouth.rnd.eofff.ogc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class ModelTiePointsPropertyParser implements PropertyParser {

    private static final Logger LOG = LoggerFactory.getLogger(ModelTiePointsPropertyParser.class);

    public ModelTiePointsPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return ModelTiePointsProperty.TIEP_ATOM;
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
        if (flags == 0x00) {
            ModelTiePoints3DProperty box = new ModelTiePoints3DProperty();
            box.setVersion(version);
            box.setFlags(flags);
            int numPoints = parseContext.readUnsignedInt16();
            for (int i = 0; i < numPoints; i++) {
                TiePoint3D tiePoint = TiePoint3D.parseFrom(parseContext);
                box.addTiePoint3D(tiePoint);
            }
            return box;
        } else if (flags == 0x01) {
            ModelTiePointsProperty box = new ModelTiePointsProperty();
            box.setVersion(version);
            box.setFlags(flags);
            int numPoints = parseContext.readUnsignedInt16();
            for (int i = 0; i < numPoints; i++) {
                TiePoint tiePoint = TiePoint.parseFrom(parseContext);
                box.addTiePoint(tiePoint);
            }
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
