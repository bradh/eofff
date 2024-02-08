@SuppressWarnings("module") // That is not a software version number - its the name of the standards
module net.frogmouth.rnd.eofff.jpeg2000 {
    requires com.google.auto.service;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;
    uses net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.jpeg2000.fileformat.JP2HeaderBoxParser,
            net.frogmouth.rnd.eofff.jpeg2000.fileformat.SignatureBoxParser,
            net.frogmouth.rnd.eofff.jpeg2000.fileformat.cmap.ComponentMappingBoxParser,
            net.frogmouth.rnd.eofff.jpeg2000.fileformat.ihdr.ImageHeaderBoxParser,
            net.frogmouth.rnd.eofff.jpeg2000.fileformat.jp2c.ContiguousCodestreamBoxParser;
    provides net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser with
            net.frogmouth.rnd.eofff.jpeg2000.J2KHeaderItemPropertyParser;

    exports net.frogmouth.rnd.eofff.jpeg2000;
    exports net.frogmouth.rnd.eofff.jpeg2000.fileformat;
    exports net.frogmouth.rnd.eofff.jpeg2000.fileformat.ihdr;
    exports net.frogmouth.rnd.eofff.jpeg2000.fileformat.jp2c;
}
