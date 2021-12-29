package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlternativesParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(AlternativesParser.class);

    public AlternativesParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("altr");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AlternativesBox box = new AlternativesBox(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setGroupId(parseContext.readUnsignedInt32());
        long numEntitiesInGroup = parseContext.readUnsignedInt32();
        for (int i = 0; i < numEntitiesInGroup; ++i) {
            long id = parseContext.readUnsignedInt32();
            box.addEntity(id);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
