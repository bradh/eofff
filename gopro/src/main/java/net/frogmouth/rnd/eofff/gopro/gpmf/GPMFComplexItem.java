package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFComplexItem extends GPMFItem {

    private final int sampleSize;
    private final int repeat;
    private final String structure;
    private List<List<Object>> entries = new ArrayList<>();

    public GPMFComplexItem(FourCC fourCC, int sampleSize, int repeat, String structure) {
        super(fourCC);
        this.sampleSize = sampleSize;
        this.repeat = repeat;
        this.structure = structure;
    }

    public List<List<Object>> getEntries() {
        return entries;
    }

    @Override
    void parse(ParseContext parseContext) {
        for (int i = 0; i < repeat; i++) {
            List<Object> entry = new ArrayList<>();
            structure
                    .chars()
                    .forEach(
                            c -> {
                                switch (c) {
                                    case 66 -> { // 'B'
                                        int b = parseContext.readUnsignedInt8();
                                        entry.add(b);
                                    }
                                    case 70 -> { // 'F'
                                        FourCC fourCC = parseContext.readFourCC();
                                        entry.add(fourCC);
                                    }
                                    case 83 -> { // 'S'
                                        int s = parseContext.readUnsignedInt16();
                                        entry.add(s);
                                    }
                                    case 102 -> { // 'f'
                                        float f = parseContext.readDouble32();
                                        entry.add(f);
                                    }
                                    case 108 -> { // 'l'
                                        int l = parseContext.readInt32();
                                        entry.add(l);
                                    }
                                    default ->
                                            throw new UnsupportedOperationException(
                                                    "TODO: case " + c);
                                }
                            });
            entries.add(entry);
        }
        int bodyBytes = sampleSize * repeat;
        int residual = bodyBytes % Integer.BYTES;
        if (residual != 0) {
            parseContext.skipBytes(Integer.BYTES - residual);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getStringBuilder(nestingLevel);
        sb.append(entries);
        return sb.toString();
    }
}
