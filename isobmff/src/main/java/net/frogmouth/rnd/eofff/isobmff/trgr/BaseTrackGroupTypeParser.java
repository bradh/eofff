package net.frogmouth.rnd.eofff.isobmff.trgr;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTrackGroupTypeParser implements TrackGroupTypeParser {

    private static final Logger LOG = LoggerFactory.getLogger(BaseTrackGroupTypeParser.class);

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "BaseTrackGroupTypeParser getFourCC() should not be called directly");
    }

    @Override
    public TrackGroupTypeBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        return parseAsBaseTrackGroupType(parseContext, initialOffset, boxSize, boxName);
    }

    protected boolean isSupportedVersion(int version) {
        return version == 0x00;
    }

    protected TrackGroupTypeBox parseAsBaseTrackGroupType(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC track_group_type) {
        parseContext.setCursorPosition(initialOffset);
        BaseTrackGroupTypeBox baseTrackGroupTypeBox = new BaseTrackGroupTypeBox(track_group_type);
        this.parseVersionAndFlags(
                parseContext, baseTrackGroupTypeBox, initialOffset, boxSize, track_group_type);
        baseTrackGroupTypeBox.setTrackGroupId(parseContext.readUnsignedInt32());
        return baseTrackGroupTypeBox;
    }

    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }

    protected boolean parseVersionAndFlags(
            ParseContext parseContext,
            BaseTrackGroupTypeBox trackGroupTypeBox,
            long initialOffset,
            long boxSize,
            FourCC track_group_type) {
        int version = parseContext.readByte();
        trackGroupTypeBox.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base track group type.", version);
            return true;
        }
        trackGroupTypeBox.setFlags(parseFlags(parseContext));
        return false;
    }
}
