module net.frogmouth.rnd.eofff.uncompressed {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires com.google.auto.service;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser with
            net.frogmouth.rnd.eofff.uncompressed.cloc.ChromaLocationBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.cmpc.CompressionConfigurationItemPropertyParser,
            net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.cpal.ComponentPalettePropertyParser,
            net.frogmouth.rnd.eofff.uncompressed.depi.DepthInfoBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.disi.DisparityInformationBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.icef.GenericallyCompressedUnitsItemInfoParser,
            net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.sbpm.SensorBadPixelsMapBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoItemPropertyParser,
            net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBoxParser;

    exports net.frogmouth.rnd.eofff.uncompressed.cloc;
    exports net.frogmouth.rnd.eofff.uncompressed.cmpc;
    exports net.frogmouth.rnd.eofff.uncompressed.cmpd;
    exports net.frogmouth.rnd.eofff.uncompressed.cpal;
    exports net.frogmouth.rnd.eofff.uncompressed.cpat;
    exports net.frogmouth.rnd.eofff.uncompressed.depi;
    exports net.frogmouth.rnd.eofff.uncompressed.disi;
    exports net.frogmouth.rnd.eofff.uncompressed.icef;
    exports net.frogmouth.rnd.eofff.uncompressed.itai;
    exports net.frogmouth.rnd.eofff.uncompressed.pmdp;
    exports net.frogmouth.rnd.eofff.uncompressed.sbpm;
    exports net.frogmouth.rnd.eofff.uncompressed.taic;
    exports net.frogmouth.rnd.eofff.uncompressed.uncc;
}
