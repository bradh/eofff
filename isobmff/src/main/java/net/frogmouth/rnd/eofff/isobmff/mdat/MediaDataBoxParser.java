package net.frogmouth.rnd.eofff.isobmff.mdat;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class MediaDataBoxParser extends BoxParser {

    public MediaDataBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MediaDataBox.MDAT_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MediaDataBox box = new MediaDataBox();
        box.setInitialOffset(parseContext.getCursorPosition());
        // box.setDataOffset(parseContext.getCursorPosition());
        //  box.setDataLength(initialOffset + boxSize - parseContext.getCursorPosition());
        byte[] data =
                parseContext.getBytes(boxSize - (parseContext.getCursorPosition() - initialOffset));
        // byte[] data = parseContext.getBytes(boxSize - (Integer.BYTES + FourCC.BYTES));
        box.setData(data);
        /*
        if (box.getDataLength() > 1000) {
            ByteBuffer bb = parseContext.getByteBuffer(box.getDataOffset(), box.getDataLength());
            File file =
                    new File(
                            String.format(
                                    "mdat_%s_%s.hevc", box.getDataOffset(), box.getDataLength()));
            try {
                FileChannel wChannel = new FileOutputStream(file, false).getChannel();
                wChannel.write(bb);

                while (bb.position() < box.getDataLength()) {
                    int nalLength = bb.getInt();
                    System.out.println("reading " + nalLength);
                    ByteBuffer nalu = bb.slice(bb.position(), nalLength);
                    wChannel.write(nalu);
                    bb.position(bb.position() + nalLength);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        */
        return box;
    }
}
