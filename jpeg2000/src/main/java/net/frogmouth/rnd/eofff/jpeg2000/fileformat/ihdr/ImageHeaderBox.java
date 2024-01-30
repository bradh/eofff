package net.frogmouth.rnd.eofff.jpeg2000.fileformat.ihdr;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Image Header Box.
 *
 * <p>See ISO/IEC 15444-1:2019 Section I.5.3.1
 */
public class ImageHeaderBox extends BaseBox {
    public static final FourCC IHDR_ATOM = new FourCC("ihdr");
    private long height;
    private long width;
    private int nc;
    private short bpc;
    private short c;
    private short unk;
    private short ipr;

    public ImageHeaderBox() {
        super(IHDR_ATOM);
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public int getNumberOfComponents() {
        return nc;
    }

    public void setNumberOfComponents(int nc) {
        this.nc = nc;
    }

    public short getBitsPerComponent() {
        return bpc;
    }

    public void setBitsPerComponent(short bpc) {
        this.bpc = bpc;
    }

    public short getCompression() {
        return c;
    }

    public void setCompression(short c) {
        this.c = c;
    }

    public short getColourspaceUnknown() {
        return unk;
    }

    public void setColourspaceUnknown(short unk) {
        this.unk = unk;
    }

    public short getIPR() {
        return ipr;
    }

    public void setIPR(short ipr) {
        this.ipr = ipr;
    }

    @Override
    public String getFullName() {
        return "Image Header Box";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        size += Integer.BYTES;
        size += Short.BYTES;
        size += Byte.BYTES;
        size += Byte.BYTES;
        size += Byte.BYTES;
        size += Byte.BYTES;
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(height);
        stream.writeUnsignedInt32(width);
        stream.writeUnsignedInt16(nc);
        stream.writeByte(bpc);
        stream.writeByte(c);
        stream.writeByte(unk);
        stream.writeByte(ipr);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("HEIGHT: ");
        sb.append(height);
        sb.append(", WIDTH: ");
        sb.append(width);
        sb.append(", NC: ");
        sb.append(nc);
        sb.append(", BPC: ");
        sb.append(bpc);
        sb.append(", C: ");
        sb.append(c);
        sb.append(", UNK: ");
        sb.append(unk);
        sb.append(", IPR: ");
        sb.append(ipr);
        return sb.toString();
    }
}
