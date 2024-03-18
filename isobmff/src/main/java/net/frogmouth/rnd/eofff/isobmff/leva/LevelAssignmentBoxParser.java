package net.frogmouth.rnd.eofff.isobmff.leva;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class LevelAssignmentBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(LevelAssignmentBoxParser.class);

    public LevelAssignmentBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return LevelAssignmentBox.LEVA_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        LevelAssignmentBox box = new LevelAssignmentBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        int levelCount = parseContext.readUnsignedInt8();
        for (int i = 0; i < levelCount; i++) {
            LevelAssignment level = new LevelAssignment();
            level.setTrackID(parseContext.readUnsignedInt32());
            int paddingAndAssignment = parseContext.readUnsignedInt8();
            level.setPaddingFlag((paddingAndAssignment & 0x80) == 0x80);
            level.setAssignmentType(paddingAndAssignment & 0x7F);
            switch (level.getAssignmentType()) {
                case 0:
                    level.setGroupingType(parseContext.readFourCC());
                    break;
                case 1:
                    level.setGroupingType(parseContext.readFourCC());
                    level.setGroupingTypeParameter(parseContext.readUnsignedInt32());
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    level.setSubTrackID(parseContext.readUnsignedInt32());
                    break;
                default:
                    break;
            }
            box.addLevel(level);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
