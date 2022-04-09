/**
 * Implementation of ISO/IEC 23009-1 "Information technology — Dynamic adaptive streaming over HTTP
 * (DASH) — Part 1: Media presentation description and segment formats".
 *
 * <p>This document primarily specifies formats for the Media Presentation Description and Segments
 * for dynamic adaptive streaming delivery of MPEG media over HTTP. It is applicable to streaming
 * services over the Internet.
 */
module net.frogmouth.rnd.eofff.dash {
    requires jakarta.xml.bind;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.dash.emsg.EventMessageBoxParser;

    exports net.frogmouth.rnd.eofff.dash.emsg;

    opens net.frogmouth.rnd.eofff.dash.mpd.gen to
            jakarta.xml.bind;
}
