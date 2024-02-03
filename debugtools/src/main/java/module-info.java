/**
 * Debug tools for EOFFF
 *
 * <p>This is not intended to be a re-usable module, but rather a demonstration of how other
 * capabilities can be used.
 */
module net.frogmouth.rnd.eofff.tools {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires net.frogmouth.rnd.eofff.nalvideo;
    requires net.frogmouth.rnd.eofff.ts26_244;
    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.slf4j2.impl;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;
}
