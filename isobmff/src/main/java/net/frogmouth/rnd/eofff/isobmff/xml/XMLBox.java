package net.frogmouth.rnd.eofff.isobmff.xml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class XMLBox extends FullBox {
    public static final FourCC XML_ATOM = new FourCC("xml ");

    private String xml;

    public XMLBox() {
        super(XML_ATOM);
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public long getBodySize() {
        return xml.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public String getFullName() {
        return "XMLBox";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeNullTerminatedString(xml);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("xml=");
        sb.append(xml);
        return sb.toString();
    }
}
