package net.frogmouth.rnd.eofff.nalvideo.btrt;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MPEG4BitRateBoxParser extends BaseBoxParser {
    public MPEG4BitRateBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("btrt");
    }

    @Override
    public MPEG4BitRateBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MPEG4BitRateBox box = new MPEG4BitRateBox(boxName);
        box.setBufferSizeDB(parseContext.readUnsignedInt32());
        box.setMaxBitrate(parseContext.readUnsignedInt32());
        box.setAvgBitrate(parseContext.readUnsignedInt32());
        return box;
    }
}
