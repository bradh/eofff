package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
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
            int length = repeat;
            for (int i = 0; i < repeat; i++) {
                if (bytes[i] == 0x00) {
                    length = i;
                    break;
                }
            }
            String s = new String(bytes, 0, length, StandardCharsets.ISO_8859_1);
            strings.add(s);
        } else {
            for (int r = 0; r < repeat; r++) {
                byte[] bytes = context.getBytes(sampleSize);
                int length = sampleSize;
                for (int i = 0; i < sampleSize; i++) {
                    if (bytes[i] == 0x00) {
                        length = i;
                        break;
                    }
                }
                String s = new String(bytes, 0, length, StandardCharsets.ISO_8859_1);
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

    @Override
    void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBase(writer);
        if ((sampleSize == 1) && (repeat > 1)) {
            String s = strings.get(0);
            byte[] stringBytes = s.getBytes(StandardCharsets.US_ASCII);
            writer.write(stringBytes);
            for (int i = 0; i < (repeat - stringBytes.length); i++) {
                writer.writeByte(0); // sample size adjustment
            }

        } else {
            for (int r = 0; r < repeat; r++) {
                String s = strings.get(r);
                byte[] stringBytes = s.getBytes(StandardCharsets.US_ASCII);
                writer.write(stringBytes);
                for (int i = 0; i < (sampleSize - stringBytes.length); i++) {
                    writer.writeByte(0); // sample size adjustment
                }
            }
        }
        int residual = (sampleSize * repeat) % Integer.BYTES;
        if (residual != 0) {
            for (int i = 0; i < (Integer.BYTES - residual); i++) {
                writer.writeByte(0); // padding;
            }
        }
    }

    @Override
    protected int getType() {
        return 99; // 'c'
    }

    @Override
    protected int getSampleSize() {
        return this.sampleSize;
    }

    @Override
    protected int getRepeat() {
        return this.repeat;
    }
}
