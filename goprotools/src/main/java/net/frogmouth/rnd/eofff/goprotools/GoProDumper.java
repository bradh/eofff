package net.frogmouth.rnd.eofff.goprotools;

import java.io.IOException;

/**
 * @author bradh
 */
public class GoProDumper {

    public static void main(String[] args) throws IOException {
        GoProParser parser = new GoProParser(args[0]);
        // parser.dumpGPMF();
        // parser.dumpTimingTrack();
        /*
        parser.dumpBoxes();
        parser.dumpH265Files();
        parser.findMetadataTrack();
        */
        GoProCleaner cleaner = new GoProCleaner(args[0]);
        cleaner.cleanFile();
        cleaner.writeOutFile("dump_gopro.mp4");
    }
}
