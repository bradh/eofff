package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFStringItem extends GPMFItem {

    private final List<String> strings = new ArrayList<>();
    private final int sampleSize;
    private final int repeat;

    public GPMFStringItem(FourCC fourCC, int sampleSize, int repeat) {
        super(fourCC);
        this.sampleSize = sampleSize;
        this.repeat = repeat;
    }

    public String getString() {
        if (strings.size() > 0) {
            return strings.get(0);
        }
        return null;
    }

    @Override
    void parse(ParseContext context) {
        if ((sampleSize == 1) && (repeat > 1)) {
            byte[] bytes = context.getBytes(repeat);
            String s = new String(bytes, StandardCharsets.ISO_8859_1);
            strings.add(s);
        } else {
            for (int r = 0; r < repeat; r++) {
                byte[] bytes = context.getBytes(sampleSize);
                String s = new String(bytes, StandardCharsets.ISO_8859_1);
                strings.add(s);
            }
        }
        int residual = (repeat * sampleSize) % Integer.BYTES;
        if (residual != 0) {
            context.skipBytes(Integer.BYTES - residual);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getStringBuilder(nestingLevel);
        if (strings.size() == 1) {
            sb.append(strings.get(0));
        } else {
            sb.append(strings);
        }
        return sb.toString();
    }
}
