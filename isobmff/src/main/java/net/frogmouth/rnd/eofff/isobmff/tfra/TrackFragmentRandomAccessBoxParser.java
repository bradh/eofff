package net.frogmouth.rnd.eofff.isobmff.tfra;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.stsz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TrackFragmentRandomAccessBoxParser extends FullBoxParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(TrackFragmentRandomAccessBoxParser.class);

    public TrackFragmentRandomAccessBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TrackFragmentRandomAccessBox.TFRA_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackFragmentRandomAccessBox box = new TrackFragmentRandomAccessBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setTrackID(parseContext.readUnsignedInt32());
        long lengths = parseContext.readUnsignedInt32();
        box.setLengthSizeOfTrafNum((int) ((lengths & 0x30) >> 4));
        box.setLengthSizeOfTrunNum((int) ((lengths & 0x0C) >> 2));
        box.setLengthSizeOfSampleNum((int) (lengths & 0x03));
        long numberOfEntry = parseContext.readUnsignedInt32();
        for (long i = 0; i < numberOfEntry; i++) {
            TrackFragmentEntry entry = new TrackFragmentEntry();
            if (version == 1) {
                entry.setTime(parseContext.readUnsignedInt64());
                entry.setMoofOffset(parseContext.readUnsignedInt64());
            } else {
                entry.setTime(parseContext.readUnsignedInt32());
                entry.setMoofOffset(parseContext.readUnsignedInt32());
            }
            entry.setTrafNumber(
                    parseContext.readUnsignedInt((box.getLengthSizeOfTrafNum() + 1) * 8));
            entry.setTrunNumber(
                    parseContext.readUnsignedInt((box.getLengthSizeOfTrafNum() + 1) * 8));
            entry.setSampleDelta(
                    parseContext.readUnsignedInt((box.getLengthSizeOfSampleNum() + 1) * 8));
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
