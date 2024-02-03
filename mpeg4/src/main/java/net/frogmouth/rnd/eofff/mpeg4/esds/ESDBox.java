package net.frogmouth.rnd.eofff.mpeg4.esds;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ESDBox extends FullBox {

    private int tag = 0x03;
    private int length;
    private int es_id;
    private DecoderConfigurationDescriptor decConfigDescr;
    private byte[] bytes;
    public static final FourCC ESDS_ATOM = new FourCC("esds");

    public ESDBox() {
        super(ESDS_ATOM);
    }

    @Override
    public String getFullName() {
        return "ESDBox";
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getES_ID() {
        return es_id;
    }

    public void setES_ID(int es_id) {
        this.es_id = es_id;
    }

    public DecoderConfigurationDescriptor getDecConfigDescr() {
        return decConfigDescr;
    }

    public void setDecConfigDescr(DecoderConfigurationDescriptor decConfigDescr) {
        this.decConfigDescr = decConfigDescr;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeByte(tag);
        // TODO: we need to write variable length integer properly
        writer.writeByte(length);
        writer.writeUnsignedInt16(es_id);
        writer.write(bytes);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("tag: ");
        sb.append(tag);
        sb.append(", length: ");
        sb.append(length);
        sb.append(", ES_ID: ");
        sb.append(es_id);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Byte.BYTES;
        // TODO: we need to do variable length handling here
        size += Byte.BYTES;
        size += Short.BYTES;
        size += bytes.length;
        return size;
    }
}
