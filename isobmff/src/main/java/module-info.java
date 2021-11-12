module net.frogmouth.rnd.eofff.isobmff {
    requires jdk.incubator.foreign;
    
    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;
    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.isobmff.ftyp.FtypBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxParser;
                    
}
