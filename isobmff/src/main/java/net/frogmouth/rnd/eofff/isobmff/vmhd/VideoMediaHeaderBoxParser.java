package net.frogmouth.rnd.eofff.isobmff.vmhd;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoMediaHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(VideoMediaHeaderBoxParser.class);

    public VideoMediaHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("vmhd");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        VideoMediaHeaderBox box = new VideoMediaHeaderBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setGraphicsmode(parseContext.readUnsignedInt16());
        int red = parseContext.readUnsignedInt16();
        int green = parseContext.readUnsignedInt16();
        int blue = parseContext.readUnsignedInt16();
        box.setOpcolor(new int[] {red, green, blue});
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
