package net.frogmouth.rnd.eofff.jpeg2000;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class J2KHeaderItemPropertyParser implements PropertyParser {

    @Override
    public J2KHeaderItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        J2KHeaderItemProperty box = new J2KHeaderItemProperty();

        J2KChannelDefinition cdef = new J2KChannelDefinition();
        parseContext.readInt32();
        FourCC cdef4cc = parseContext.readFourCC();
        if (!cdef4cc.equals(J2KChannelDefinition.CDEF_ATOM)) {
            System.out.println("expected cdef, but got " + cdef4cc.toString());
        }
        int numEntries = parseContext.readUnsignedInt16();
        for (int i = 0; i < numEntries; i++) {
            int cn = parseContext.readUnsignedInt16();
            int typ = parseContext.readUnsignedInt16();
            int asoc = parseContext.readUnsignedInt16();
            J2KChannel channel = new J2KChannel(cn, typ, asoc);
            cdef.addChannel(channel);
        }
        box.setChannels(cdef);

        /*
        J2KComponentMapping cmap = new J2KComponentMapping();
        int contentLength = parseContext.readInt32() - Integer.BYTES - FourCC.BYTES;
        FourCC cmap4cc = parseContext.readFourCC();
        if (!cmap4cc.equals(J2KComponentMapping.CMAP_ATOM)) {
            System.out.println("expected cmap, but got " + cdef4cc.toString());
        }
        if ((contentLength < 0) || (contentLength % 4 != 0)) {
            System.out.println("expected cmap content to be multiple of 4 bytes");
        }
        for (int i = 0; i < contentLength % 4; i++) {
            int cmp = parseContext.readUnsignedInt16();
            int mtyp = parseContext.readByte();
            int pcol = parseContext.readByte();
            ComponentMapping componentMapping = new ComponentMapping(cmp, mtyp, pcol);
            cmap.addComponentMapping(componentMapping);
        }
        box.setComponents(cmap);
        */
        return box;
    }

    @Override
    public FourCC getFourCC() {
        return J2KHeaderItemProperty.J2KH_ATOM;
    }
}
