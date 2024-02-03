package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFContainerItem extends GPMFItem {

    private final int length;
    private final List<GPMFItem> items = new ArrayList<>();
    private FourCC lastFourCC;

    public GPMFContainerItem(FourCC fourCC, int numBytes) {
        super(fourCC);
        this.length = numBytes;
    }

    @Override
    void parse(ParseContext parseContext) {
        long limit = parseContext.getCursorPosition() + length;
        GPMFStringItem lastType = null;
        while (parseContext.getCursorPosition() < limit) {
            FourCC fourCC = parseContext.readFourCC();
            if (!fourCC.toString().matches("[A-Z][A-Z|0-9]+$")) {
                System.out.println("broken GPMF 4CC, last good: " + lastFourCC.toString());
            }
            lastFourCC = fourCC;
            byte type = parseContext.readByte();
            int sampleSize = parseContext.readByte();
            int repeat = parseContext.readInt16();
            if (type == 63) {
                String structure = lastType.getString();
                GPMFComplexItem item = new GPMFComplexItem(fourCC, sampleSize, repeat, structure);
                item.parse(parseContext);
                items.add(item);
            } else {
                GPMFItem item = GPMFItemFactory.getItem(fourCC, type, sampleSize, repeat);
                item.parse(parseContext);
                if ((item.getFourCC().toString().equals("TYPE"))
                        && (item instanceof GPMFStringItem stringItem)) {
                    lastType = stringItem;
                }
                items.add(item);
            }
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nestingLevel; i++) {
            sb.append("    ");
        }
        sb.append(getFourCC());
        sb.append(": ");
        sb.append("length ");
        sb.append(length);
        for (GPMFItem item : items) {
            if ((item != null) && (item.getFourCC().asUnsigned() != 0)) {
                sb.append("\n");
                sb.append(item.toString(nestingLevel + 1));
            }
        }
        if (getFourCC().toString().equals("STRM")) {
            GPMFComplexItem gps9 = null;
            GPMFSignedIntMultiItem gps5 = null;
            GPMFItem scale = null;
            GPMFItem units = null;
            for (GPMFItem item : items) {
                if (item.getFourCC().toString().equals("GPS9")
                        || (item.getFourCC().toString().equals("GPS5"))) {
                    if (item instanceof GPMFComplexItem complexItem) {
                        gps9 = complexItem;
                    } else if (item instanceof GPMFSignedIntMultiItem multiIntItem) {
                        gps5 = multiIntItem;
                    } else {
                        System.out.println("huh?");
                    }
                }
                if (item.getFourCC().toString().equals("SCAL")) {
                    scale = item;
                }
                if (item.getFourCC().toString().equals("UNIT")) {
                    units = item;
                }
            }
            if (gps9 != null) {
                sb.append("\n");
                for (int i = 0; i < nestingLevel + 1; i++) {
                    sb.append("    ");
                }
                List<Integer> scaleFactors = null;
                if (scale instanceof GPMFSignedIntMultiItem signedIntScaleList) {
                    scaleFactors = signedIntScaleList.getValues();
                }
                sb.append("GPS: ");
                for (List<Object> objects : gps9.getEntries()) {
                    sb.append("\n");
                    for (int i = 0; i < nestingLevel + 2; i++) {
                        sb.append("    ");
                    }
                    for (int i = 0; i < objects.size(); i++) {
                        int scaleFactor = 1;
                        if (scaleFactors != null) {
                            scaleFactor = scaleFactors.get(i % scaleFactors.size());
                        }
                        if (objects.get(i) instanceof Number n) {
                            sb.append(n.doubleValue() / scaleFactor);
                            sb.append(", ");
                        } else {
                            sb.append("[TODO], ");
                        }
                    }
                    sb.append(" | ");
                }
            }
            // TODO: gps5
        }
        return sb.toString();
    }
}
