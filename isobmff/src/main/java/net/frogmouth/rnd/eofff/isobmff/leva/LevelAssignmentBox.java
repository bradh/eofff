package net.frogmouth.rnd.eofff.isobmff.leva;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Level Assignment Box (leva).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.13.
 */
public class LevelAssignmentBox extends FullBox {
    public static final FourCC LEVA_ATOM = new FourCC("leva");

    private final List<LevelAssignment> levels = new ArrayList<>();

    public LevelAssignmentBox() {
        super(LEVA_ATOM);
    }

    @Override
    public String getFullName() {
        return "LevelAssignmentBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (LevelAssignment level : levels) {
            size += level.getSize();
        }
        return size;
    }

    public List<LevelAssignment> getLevels() {
        return levels;
    }

    public void addLevel(LevelAssignment level) {
        levels.add(level);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt8(levels.size());
        for (LevelAssignment level : levels) {
            level.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("level_count=");
        sb.append(levels.size());
        for (LevelAssignment level : levels) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            sb.append(level.toString());
        }
        return sb.toString();
    }
}
