package net.frogmouth.rnd.eofff.isobmff.mfhd;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieFragmentHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(MovieFragmentHeaderBoxParser.class);

    public MovieFragmentHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MovieFragmentHeaderBox.MFHD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MovieFragmentHeaderBox box = new MovieFragmentHeaderBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setSequenceNumber(parseContext.readUnsignedInt32());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
