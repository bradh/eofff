package net.frogmouth.rnd.eofff.dash.emsg;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class EventMessageBox extends FullBox {

    private long timescale;
    private long presentationTime;
    private long eventDuration;
    private long id;
    private String schemeIdUri;
    private String value;
    private byte[] messageData;

    public EventMessageBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "Event Message";
    }

    public long getTimescale() {
        return timescale;
    }

    public void setTimescale(long timescale) {
        this.timescale = timescale;
    }

    public long getPresentationTime() {
        return presentationTime;
    }

    public void setPresentationTime(long presentationTime) {
        this.presentationTime = presentationTime;
    }

    public long getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(long eventDuration) {
        this.eventDuration = eventDuration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSchemeIdUri() {
        return schemeIdUri;
    }

    public void setSchemeIdUri(String schemeIdUri) {
        this.schemeIdUri = schemeIdUri;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte[] getMessageData() {
        return messageData;
    }

    public void setMessageData(byte[] messageData) {
        this.messageData = messageData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        sb.append(System.lineSeparator());
        sb.append("\ttimescale: ");
        sb.append(String.format("0x%08x (%d)", timescale, timescale));
        if (getVersion() == 1) {
            sb.append(System.lineSeparator());
            sb.append("\tpresentation_time: ");
            sb.append(String.format("0x%016x (%d)", presentationTime, presentationTime));
        }
        sb.append(System.lineSeparator());
        sb.append("\tevent_duration: ");
        sb.append(String.format("0x%08x (%d)", eventDuration, eventDuration));
        sb.append(System.lineSeparator());
        sb.append("\treference_id: ");
        sb.append(String.format("0x%08x", getId()));
        if (this.schemeIdUri != null) {
            sb.append(System.lineSeparator());
            sb.append("\tscheme_id_uri: ");
            sb.append(schemeIdUri);
            sb.append(" = ");
            sb.append(getValue());
        }
        for (int i = 0; i < messageData.length; i++) {
            if ((i % 16) == 0) {
                sb.append(System.lineSeparator());
                sb.append("\t");
            }
            sb.append(String.format("0x%02x, ", messageData[i]));
        }
        return sb.toString();
    }
}
