package net.frogmouth.rnd.eofff.imagefileformat.properties.udes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class UserDescriptionProperty extends ItemFullProperty {
    public static final FourCC UDES_ATOM = new FourCC("udes");

    private String lang;
    private String descriptiveName;
    private String description;
    private String tags;

    public UserDescriptionProperty() {
        super(UDES_ATOM);
    }

    @Override
    public long getBodySize() {
        int size = 0;
        if (lang != null) {
            size += lang.getBytes(StandardCharsets.UTF_8).length;
        }
        size += Byte.BYTES;
        if (descriptiveName != null) {
            size += descriptiveName.getBytes(StandardCharsets.UTF_8).length;
        }
        size += Byte.BYTES;
        if (description != null) {
            size += description.getBytes(StandardCharsets.UTF_8).length;
        }
        size += Byte.BYTES;
        if (tags != null) {
            size += tags.getBytes(StandardCharsets.UTF_8).length;
        }
        size += Byte.BYTES;
        return size;
    }

    @Override
    public String getFullName() {
        return "UserDescriptionProperty";
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescriptiveName() {
        return descriptiveName;
    }

    public void setDescriptiveName(String descriptiveName) {
        this.descriptiveName = descriptiveName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeNullTerminatedString(lang);
        writer.writeNullTerminatedString(this.descriptiveName);
        writer.writeNullTerminatedString(description);
        writer.writeNullTerminatedString(tags);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getBaseStringBuilder(nestingLevel);
        sb.append("lang=");
        sb.append(getLang());
        sb.append(", name=");
        sb.append(getDescriptiveName());
        sb.append(", description=");
        sb.append(getDescription());
        sb.append(", tags=");
        sb.append(getTags());
        return sb.toString();
    }
}
