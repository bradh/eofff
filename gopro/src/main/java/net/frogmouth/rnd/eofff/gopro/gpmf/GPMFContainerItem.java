package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFContainerItem extends GPMFItem {

    private final int length;
    private final List<GPMFItem> items = new ArrayList<>();

    public GPMFContainerItem(FourCC fourCC, int numBytes) {
        super(fourCC);
        this.length = numBytes;
    }

    @Override
    void parse(ParseContext parseContext) {
        long limit = parseContext.getCursorPosition() + length;
        while (parseContext.getCursorPosition() < limit) {
            FourCC fourCC = parseContext.readFourCC();
            byte type = parseContext.readByte();
            int sampleSize = parseContext.readByte();
            int repeat = parseContext.readInt16();
            GPMFItem item = GPMFItemFactory.getItem(fourCC, type, sampleSize, repeat);
            item.parse(parseContext);
            items.add(item);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nestingLevel; i++) {
            sb.append("    ");
        }
        sb.append(this.getFourCC());
        sb.append(": ");
        sb.append("length ");
        sb.append(length);
        sb.append("\n");
        for (GPMFItem item : items) {
            if ((item != null) && (item.getFourCC().asUnsigned() != 0)) {
                sb.append(item.toString(nestingLevel + 1));
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
