package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class GPMF extends BaseBox {

    public static final FourCC GPMF_ATOM = new FourCC("GPMF");

    private List<GPMFItem> items = new ArrayList<>();

    public GPMF() {
        super(GPMF_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro Metadata Format";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        for (GPMFItem item : items) {
            item.writeTo(writer);
        }
    }

    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (GPMFItem item : items) {
            if (item != null) {
                sb.append("\n");
                sb.append(item.toString(nestingLevel + 1));
            }
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        // TODO - implement properly
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (GPMFItem item : items) {
            try {
                item.writeTo(streamWriter);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return baos.toByteArray().length;
    }

    public void addItem(GPMFItem item) {
        items.add(item);
    }
}
