package net.frogmouth.rnd.eofff.isobmff.tkhd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TrackHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TrackHeaderBoxParser.class);

    public TrackHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TrackHeaderBox.TKHD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackHeaderBox box = new TrackHeaderBox();
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
            box.setTrackId(parseContext.readUnsignedInt32());
            parseContext.readUnsignedInt32();
            box.setDuration(parseContext.readUnsignedInt64());
        } else { // version == 0
            box.setCreationTime(parseContext.readUnsignedInt32());
            box.setModificationTime(parseContext.readUnsignedInt32());
            box.setTrackId(parseContext.readUnsignedInt32());
            parseContext.readUnsignedInt32();
            box.setDuration(parseContext.readUnsignedInt32());
        }
        parseContext.readUnsignedInt32(); // reserved[0]
        parseContext.readUnsignedInt32(); // reserved[1]
        box.setLayer(parseContext.readUnsignedInt16());
        box.setAlternateGroup(parseContext.readUnsignedInt16());
        box.setVolume(parseContext.readUnsignedInt16());
        parseContext.readUnsignedInt16(); // reserved
        int[] matrix = new int[9];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = parseContext.readInt32();
        }
        box.setMatrix(matrix);
        box.setWidth(parseContext.readUnsignedInt32());
        box.setHeight(parseContext.readUnsignedInt32());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
