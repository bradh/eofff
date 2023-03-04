package net.frogmouth.rnd.eofff.uncompressed.cmpd;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Component Definition.
 *
 * <p>This represents one of the component types within a Component Definition Box.
 *
 * <p>As an example, consider an RGB image. The Component Definition Box ({@code cmpd}) contains
 * three component definitions (one for each of Red, Green and Blue).
 */
public class ComponentDefinition {

    private final int componentType;
    private final String componentTypeUri;

    /**
     * Constructor.
     *
     * @param component_type the component type as an integer value.
     * @param component_type_uri the URI if the component type is a user defined component type.
     */
    public ComponentDefinition(int component_type, String component_type_uri) {
        this.componentType = component_type;
        this.componentTypeUri = component_type_uri;
    }

    /**
     * Component type.
     *
     * @return the component type as an integer value.
     */
    public int getComponentType() {
        return componentType;
    }

    /**
     * Component type URI.
     *
     * <p>This is used if the component type is a user defined component type.
     *
     * @return the URI, or null if the component type is not a user defined component type
     */
    public String getComponentTypeUri() {
        return componentTypeUri;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("component=");
        sb.append(getComponentTypeDescription());
        sb.append(" (");
        sb.append(componentType);
        sb.append(")");
        if (componentTypeUri != null) {
            sb.append(", URI:");
            sb.append(this.componentTypeUri);
        }
        return sb.toString();
    }

    private String getComponentTypeDescription() {
        if (this.componentType >= 0x8000) {
            return "User-defined component";
        }
        return switch (this.componentType) {
            case 0 -> "Monochrome component";
            case 1 -> "Luma component (Y)";
            case 2 -> "Chroma component (Cb)";
            case 3 -> "Chroma component (Cr)";
            case 4 -> "Red component (R)";
            case 5 -> "Green component (G)";
            case 6 -> "Blue component (B)";
            case 7 -> "Alpha / transparency component (A)";
            case 8 -> "Depth component (D)";
            case 9 -> "Disparity component (Disp)";
            case 10 -> "Palette component (P)";
            case 11 -> "Filter Array component (FA)";
            case 12 -> "Padded component";
            default -> "ISO/IEC reserved";
        };
    }

    /**
     * Serialise the component definition to a stream writer.
     *
     * @param stream the stream to write to
     * @throws IOException if writing fails
     */
    void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt16(componentType);
        if (this.componentType >= 0x8000) {
            stream.writeNullTerminatedString(componentTypeUri);
        }
    }

    /**
     * Length of the serialised component definition.
     *
     * @return the number of bytes in the serialised representation.
     */
    public long getNumberOfBytes() {
        if (this.componentType >= 0x8000) {
            return Short.BYTES + this.componentTypeUri.getBytes(StandardCharsets.UTF_8).length + 1;
        } else {
            return Short.BYTES;
        }
    }
}
