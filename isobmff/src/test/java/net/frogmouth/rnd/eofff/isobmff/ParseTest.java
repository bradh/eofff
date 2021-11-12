package net.frogmouth.rnd.eofff.isobmff;

import net.frogmouth.rnd.eofff.isobmff.ftyp.FtypBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;

public class ParseTest {

    public ParseTest() {
    }

    private Path getExample() {
        String fileName = "example.heic";
        return getPathFromResourceName(fileName);
    }

    private Path getPathFromResourceName(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return Paths.get(classLoader.getResource(fileName).getPath());
    }

    @Test
    public void test() throws IOException {
        List<Box> boxes = new ArrayList<>();
        Path testFile = getExample();
        MemorySegment segment = MemorySegment.mapFile(testFile, 0, Files.size(testFile), FileChannel.MapMode.READ_ONLY,
                ResourceScope.newImplicitScope());
        ByteBuffer byteBuffer = segment.asByteBuffer();
        while (byteBuffer.hasRemaining()) {
            long offset = byteBuffer.position();
            long boxSize = byteBuffer.getInt();
            byte[] labelBytes = new byte[4];
            byteBuffer.get(labelBytes);
            String boxName = new String(labelBytes, StandardCharsets.US_ASCII);
            BoxParser parser = BoxFactoryManager.getParser(boxName);
            Box box = parser.parse(byteBuffer, offset, boxSize, boxName);
            boxes.add(box);
        }
        for (Box box : boxes) {
            System.out.println(box.toString());
        }
        assertEquals(boxes.size(), 7);
        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof FtypBox);
        FtypBox ftyp = (FtypBox)box0;
        assertEquals(ftyp.getBoxName(), "ftyp");
        assertEquals(ftyp.getMajorBrand(), "mif1");
        assertEquals(ftyp.getMinorVersion(), 0);
        assertEquals(ftyp.getFourCC(), "ftyp");
        assertEquals(ftyp.getCompatibleBrands().size(), 3);
        assertEquals(ftyp.getCompatibleBrands().get(0), "mif1");
        assertEquals(ftyp.getCompatibleBrands().get(1), "heic");
        assertEquals(ftyp.getCompatibleBrands().get(2), "hevc");
    }
}
