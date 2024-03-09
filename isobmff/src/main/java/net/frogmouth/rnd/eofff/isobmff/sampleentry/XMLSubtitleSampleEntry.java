package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * XML Subtitle Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.6.3
 */
public class XMLSubtitleSampleEntry extends SubtitleSampleEntry implements SampleEntry {

    public static final FourCC STPP_ATOM = new FourCC("stpp");

    private String namespace;
    private String schemaLocation;
    private String auxiliaryMimeTypes;

    public XMLSubtitleSampleEntry() {
        super(STPP_ATOM);
    }

    @Override
    public String getFullName() {
        return "SubtitleSampleEntry";
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

    public String getAuxiliaryMimeTypes() {
        return auxiliaryMimeTypes;
    }

    public void setAuxiliaryMimeTypes(String auxiliaryMimeTypes) {
        this.auxiliaryMimeTypes = auxiliaryMimeTypes;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(getDataReferenceIndex());
        sb.append(", namespace=");
        sb.append(this.namespace);
        sb.append(", schema_location=");
        if (schemaLocation.isEmpty()) {
            sb.append("(none)");
        } else {
            sb.append(schemaLocation);
        }
        sb.append(", auxiliary_mime_types=");
        if (auxiliaryMimeTypes.isEmpty()) {
            sb.append("(none)");
        } else {
            sb.append(auxiliaryMimeTypes);
        }
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += namespace.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        size += schemaLocation.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        size += auxiliaryMimeTypes.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        stream.writeNullTerminatedString(namespace);
        stream.writeNullTerminatedString(schemaLocation);
        stream.writeNullTerminatedString(auxiliaryMimeTypes);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }
}
