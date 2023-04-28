package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: move to isobmff module
public abstract class AbstractEntityToGroupBoxParser extends FullBoxParser {

    protected static final Logger LOG = LoggerFactory.getLogger(AlbumCollectionParser.class);

    public AbstractEntityToGroupBoxParser() {}

    @Override
    public abstract FourCC getFourCC();

    protected Box parseEntityToGroupBox(
            ParseContext parseContext,
            AbstractEntityToGroupBox box,
            long initialOffset,
            long boxSize,
            FourCC boxName) {
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

    protected boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
