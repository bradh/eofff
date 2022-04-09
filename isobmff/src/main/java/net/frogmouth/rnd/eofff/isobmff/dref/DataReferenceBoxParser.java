package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.mdhd.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataReferenceBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(DataReferenceBoxParser.class);

    public DataReferenceBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("dref");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        DataReferenceBox box = new DataReferenceBox(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        // TODO: need to parse out the subordinate boxes.
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
