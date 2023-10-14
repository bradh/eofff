/**
 * Tools and integration tests for EOFFF.
 *
 * <p>This is not intended to be a re-usable module, but rather a demonstration of how other
 * capabilities can be used.
 */
module net.frogmouth.rnd.eofff.tools {
    requires jakarta.xml.bind;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires net.frogmouth.rnd.eofff.imagefileformat;
    requires net.frogmouth.rnd.eofff.nalvideo;
    requires net.frogmouth.rnd.eofff.sidd;
    requires net.frogmouth.rnd.eofff.uncompressed;
    requires org.slf4j;
    requires org.jmisb.api;
    requires java.desktop;
    requires java.logging;
    requires net.frogmouth.rnd.eofff.yuv;
    requires org.codice.imaging.nitf.core;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;
    uses net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;
}
