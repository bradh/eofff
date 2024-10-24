package net.frogmouth.rnd.ngiis.eventmessage;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class EventMessageInstanceBox extends FullBox {

    public static final FourCC EMIB_ATOM = new FourCC("emib");

    public EventMessageInstanceBox() {
        super(EMIB_ATOM);
    }

    @Override
    public String getFullName() {
        return "EventMessageInstanceBox";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        // TODO
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        // TODO
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = 0;
        // TDO
        return size;
    }
}
