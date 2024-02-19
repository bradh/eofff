package net.frogmouth.rnd.eofff.imagefileformat.ccst;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class CodingConstraintsBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(CodingConstraintsBoxParser.class);

    private static final int ALL_REF_PICS_INTRA_FLAG = (1 << 31);
    private static final int INTRA_PRED_USED_FLAG = (1 << 30);
    private static final int MAX_REF_PER_PIC_SHIFT = 26;

    public CodingConstraintsBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return CodingConstraintsBox.CCST_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CodingConstraintsBox box = new CodingConstraintsBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        int v = parseContext.readInt32();
        box.setAllRefPicsIntra((v & ALL_REF_PICS_INTRA_FLAG) == ALL_REF_PICS_INTRA_FLAG);
        box.setIntraPredUsed((v & INTRA_PRED_USED_FLAG) == INTRA_PRED_USED_FLAG);
        box.setMaxRefPerPic((byte) ((v >> MAX_REF_PER_PIC_SHIFT) & 0x0F));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
