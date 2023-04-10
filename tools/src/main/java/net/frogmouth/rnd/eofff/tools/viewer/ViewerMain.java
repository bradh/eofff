package net.frogmouth.rnd.eofff.tools.viewer;

import java.io.IOException;

/**
 * @author bradh
 */
public class ViewerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            args =
                    new String[] {
                        // "/home/bradh/Uncompressed Test Files/Uncompressed Test
                        // Files/uncC_3_rgb_interleaved_tiled.heif"
                        "test_siff_rgb_grid.mp4"
                    };
        }
        Parser viewer = new Parser(args);
        viewer.show();
    }
}
