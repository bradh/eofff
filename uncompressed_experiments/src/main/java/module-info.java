/**
 * Uncompressed code experiments for EOFFF
 *
 * <p>This is not intended to be a re-usable module, but rather a demonstration of how other
 * capabilities can be used.
 */
module net.frogmouth.rnd.eofff.uncompressed_experiments {
    requires net.frogmouth.rnd.eofff.imagefileformat;
    requires net.frogmouth.rnd.eofff.isobmff;
    // requires net.frogmouth.rnd.eofff.miaf;
    requires net.frogmouth.rnd.eofff.nalvideo;
    requires net.frogmouth.rnd.eofff.uncompressed;
    requires org.slf4j;
    requires org.apache.commons.io;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.slf4j2.impl;
    requires java.desktop;
    requires com.aayushatharva.brotli4j;
    requires tiff;
    requires org.threeten.extra;
    requires info.picocli;
}
