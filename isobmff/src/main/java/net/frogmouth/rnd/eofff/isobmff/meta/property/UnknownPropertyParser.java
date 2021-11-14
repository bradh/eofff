package net.frogmouth.rnd.eofff.isobmff.meta.property;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnknownPropertyParser extends PropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(UnknownPropertyParser.class);

    public UnknownPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "Not supported yet."); // To change body of generated methods, choose Tools |
        // Templates.
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        LOG.warn("Unable to parse unknown property type '{}'", boxName.toString());
        parseContext.skipBytes(initialOffset + boxSize - parseContext.getCursorPosition());
        return null;
    }
}
