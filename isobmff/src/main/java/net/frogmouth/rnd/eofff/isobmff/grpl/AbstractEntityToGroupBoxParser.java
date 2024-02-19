package net.frogmouth.rnd.eofff.isobmff.grpl;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEntityToGroupBoxParser implements EntityToGroupParser {

    protected static final Logger LOG =
            LoggerFactory.getLogger(AbstractEntityToGroupBoxParser.class);

    public AbstractEntityToGroupBoxParser() {}

    @Override
    public abstract FourCC getFourCC();

    public EntityToGroup parseEntityToGroupBox(
            ParseContext parseContext,
            AbstractEntityToGroupBox box,
            long initialOffset,
            long boxSize,
            FourCC boxName) {
        int version = parseContext.readByte();
        box.setVersion(version);
        box.setFlags(parseFlags(parseContext));
        box.setGroupId(parseContext.readUnsignedInt32());
        long numEntitiesInGroup = parseContext.readUnsignedInt32();
        for (int i = 0; i < numEntitiesInGroup; ++i) {
            long id = parseContext.readUnsignedInt32();
            box.addEntity(id);
        }
        return box;
    }

    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }
}
