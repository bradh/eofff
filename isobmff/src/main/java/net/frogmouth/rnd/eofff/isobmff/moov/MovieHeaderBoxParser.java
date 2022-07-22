package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(MovieHeaderBoxParser.class);

    public MovieHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("mvhd");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MovieHeaderBox box = new MovieHeaderBox(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (version == 0x01) {
            box.setCreationTime(parseContext.readUnsignedInt64());
            box.setModificationTime(parseContext.readUnsignedInt64());
            box.setTimescale(parseContext.readUnsignedInt32());
            box.setDuration(parseContext.readUnsignedInt64());
        } else { // version == 0
            box.setCreationTime(parseContext.readUnsignedInt32());
            box.setModificationTime(parseContext.readUnsignedInt32());
            box.setTimescale(parseContext.readUnsignedInt32());
            box.setDuration(parseContext.readUnsignedInt32());
        }
        box.setRate((parseContext.readInt32() >> 16));
        box.setVolume(parseContext.readUnsignedInt16() >> 8);
        parseContext.readUnsignedInt16();
        parseContext.readUnsignedInt32();
        parseContext.readUnsignedInt32();
        int[] matrix = new int[9];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = parseContext.readInt32();
        }
        box.setMatrix(matrix);
        for (int i = 0; i < 6; i++) {
            parseContext.readInt32();
        }
        box.setNextTrackId(parseContext.readUnsignedInt32());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
