package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Simple Text Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.5.3
 */
public class SimpleTextSampleEntry extends PlainTextSampleEntry {

    public static final FourCC STXT_ATOM = new FourCC("stxt");

    private String contentEncoding;
    private String mimeFormat;

    public SimpleTextSampleEntry() {
        super(STXT_ATOM);
    }

    @Override
    public String getFullName() {
        return "SimpleTextSampleEntry";
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getMimeFormat() {
        return mimeFormat;
    }

    public void setMimeFormat(String mimeFormat) {
        this.mimeFormat = mimeFormat;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(getDataReferenceIndex());
        sb.append(", content_encoding=");
        if (contentEncoding.isEmpty()) {
            sb.append("(none)");
        } else {
            sb.append(contentEncoding);
        }
        sb.append(", mime_format=");
        sb.append(mimeFormat);
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += contentEncoding.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        size += mimeFormat.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        stream.writeNullTerminatedString(contentEncoding);
        stream.writeNullTerminatedString(mimeFormat);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }
}
