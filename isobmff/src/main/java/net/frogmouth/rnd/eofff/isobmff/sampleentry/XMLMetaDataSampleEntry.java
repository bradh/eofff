package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * XML Metadata Sample Entry (metx)
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.3.3
 */
public class XMLMetaDataSampleEntry extends MetaDataSampleEntry implements SampleEntry {

    public static final FourCC METX_ATOM = new FourCC("metx");

    private String contentEncoding;
    // These are actuallly lists, so maybe rework this?
    private String namespace;
    private String schemaLocation;

    public XMLMetaDataSampleEntry() {
        super(METX_ATOM);
    }

    @Override
    public String getFullName() {
        return "XMLMetaDataSampleEntry";
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
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
        sb.append(", namespace=");
        sb.append(namespace);
        sb.append(", schema_location=");
        sb.append(schemaLocation);
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
        size += namespace.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        size += schemaLocation.getBytes(StandardCharsets.UTF_8).length;
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
        stream.writeNullTerminatedString(namespace);
        stream.writeNullTerminatedString(schemaLocation);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }
}
