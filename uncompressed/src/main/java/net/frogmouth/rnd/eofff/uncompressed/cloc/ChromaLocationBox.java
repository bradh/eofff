package net.frogmouth.rnd.eofff.uncompressed.cloc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Chroma Location Box.
 *
 * <p>This depends on chroma sample location as defined in ISO/IEC 23091-2:2021.
 *
 * <p>See ISO/IEC 23001-17 (DIS) Section 6.1.8.
 */
public class ChromaLocationBox extends ItemFullProperty {

    public static final FourCC CLOC_ATOM = new FourCC("cloc");

    private int chromaLocation;

    public ChromaLocationBox() {
        super(CLOC_ATOM);
    }

    @Override
    public String getFullName() {
        return "ChromaLocationBox";
    }

    /**
     * Chroma location.
     *
     * @return chroma location as an integer value.
     */
    public int getChromaLocation() {
        return chromaLocation;
    }

    /**
     * Set the chroma location.
     *
     * @param chromaLocation chroma location as an integer value.
     */
    public void setChromaLocation(int chromaLocation) {
        this.chromaLocation = chromaLocation;
    }

    @Override
    public long getBodySize() {
        return Byte.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeByte(chromaLocation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        sb.append(chromaLocation);
        return sb.toString();
    }
}
