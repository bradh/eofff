package net.frogmouth.rnd.eofff.isobmff.grpl;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnknownEntityToGroupParser implements EntityToGroupParser {
    private static final Logger LOG = LoggerFactory.getLogger(UnknownEntityToGroupParser.class);

    public UnknownEntityToGroupParser() {}

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "UnknownEntityToGroupParser getFourCC() should not be called");
    }

    @Override
    public EntityToGroup parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        LOG.warn("Unable to parse unknown entity to group '{}'", boxName.toString());
        parseContext.skipBytes(initialOffset + boxSize - parseContext.getCursorPosition());
        return new DefaultEntityToGroupBox(boxName);
    }
}
