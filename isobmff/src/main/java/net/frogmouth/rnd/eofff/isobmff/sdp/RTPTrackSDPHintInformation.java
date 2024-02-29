package net.frogmouth.rnd.eofff.isobmff.sdp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * RTP Track SDP Hint Information (sdp ).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.4.3.
 */
public class RTPTrackSDPHintInformation extends BaseBox implements Box {

    public static final FourCC SDP_ATOM = new FourCC("sdp ");

    private String sdpText;

    public RTPTrackSDPHintInformation() {
        super(SDP_ATOM);
    }

    @Override
    public String getFullName() {
        return "RTPTrackSDPHintInformation";
    }

    public String getSdpText() {
        return sdpText;
    }

    public void setSdpText(String sdpText) {
        this.sdpText = sdpText;
    }

    @Override
    public long getBodySize() {
        return sdpText.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.write(sdpText.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getBaseStringBuilder(nestingLevel);
        sb.append("sdptext=");
        sb.append("\n");
        sb.append(sdpText);
        return sb.toString();
    }
}
