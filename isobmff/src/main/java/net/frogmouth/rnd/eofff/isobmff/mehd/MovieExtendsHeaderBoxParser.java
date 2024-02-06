package net.frogmouth.rnd.eofff.isobmff.mehd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class MovieExtendsHeaderBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(MovieExtendsHeaderBoxParser.class);

    public MovieExtendsHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MovieExtendsHeaderBox.MEHD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MovieExtendsHeaderBox box = new MovieExtendsHeaderBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (version == 1) {
            box.setFragmentDuration(parseContext.readUnsignedInt64());
        } else { // version == 0
            box.setFragmentDuration(parseContext.readUnsignedInt32());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
