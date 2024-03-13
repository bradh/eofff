package net.frogmouth.rnd.eofff.isobmff.stpp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.BaseSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;

/**
 * XML Subtitle Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.6.3.
 */
public class XMLSubtitleSampleEntry extends BaseSampleEntry implements SampleEntry {

    private List<String> namespaces = new ArrayList<>();
    private List<String> schemaLocations = new ArrayList<>();
    private List<String> auxiliaryMimeTypes = new ArrayList<>();

    public static final FourCC STPP_ATOM = new FourCC("stpp");

    public XMLSubtitleSampleEntry() {
        super(STPP_ATOM);
    }

    @Override
    public String getFullName() {
        return "XMLSubtitleSampleEntry";
    }

    public List<String> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List<String> namespaces) {
        this.namespaces = namespaces;
    }

    public List<String> getSchemaLocations() {
        return schemaLocations;
    }

    public void setSchemaLocations(List<String> schemaLocations) {
        this.schemaLocations = schemaLocations;
    }

    public List<String> getAuxiliaryMimeTypes() {
        return auxiliaryMimeTypes;
    }

    public void setAuxiliaryMimeTypes(List<String> auxiliaryMimeTypes) {
        this.auxiliaryMimeTypes = auxiliaryMimeTypes;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        stream.writeUtf8List(namespaces);
        stream.writeUtf8List(schemaLocations);
        stream.writeUtf8List(auxiliaryMimeTypes);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(getDataReferenceIndex());
        sb.append(", namespace=");
        sb.append(namespaces);
        sb.append(", schema_location=");
        sb.append(schemaLocations);
        sb.append(", auxiliary_mime_types=");
        sb.append(auxiliaryMimeTypes);
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += (String.join(" ", namespaces).getBytes(StandardCharsets.UTF_8).length);
        size += (String.join(" ", schemaLocations).getBytes(StandardCharsets.UTF_8).length);
        size += (String.join(" ", auxiliaryMimeTypes).getBytes(StandardCharsets.UTF_8).length);
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }
}
