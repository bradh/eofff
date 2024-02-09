package net.frogmouth.rnd.eofff.isobmff.saio;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SampleAuxiliaryInformationOffsetsBoxParser extends FullBoxParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(SampleAuxiliaryInformationOffsetsBoxParser.class);

    public SampleAuxiliaryInformationOffsetsBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SampleAuxiliaryInformationOffsetsBox.SAIO_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleAuxiliaryInformationOffsetsBox box = new SampleAuxiliaryInformationOffsetsBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        box.setFlags(parseFlags(parseContext));
        if ((box.getFlags() & 0x01) == 0x01) {
            box.setAuxInfoType(parseContext.readFourCC());
            box.setAuxInfoTypeParameter(parseContext.readUnsignedInt32());
        }
        long entry_count = parseContext.readUnsignedInt32();
        if (box.getVersion() == 0) {
            for (int i = 0; i < entry_count; i++) {
                box.addOffset(parseContext.readUnsignedInt32());
            }
        } else {
            for (int i = 0; i < entry_count; i++) {
                box.addOffset(parseContext.readUnsignedInt64());
            }
        }
        return box;
    }
}
