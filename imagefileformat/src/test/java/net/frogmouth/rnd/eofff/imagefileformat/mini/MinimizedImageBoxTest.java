package net.frogmouth.rnd.eofff.imagefileformat.mini;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MinimizedImageBoxTest {

    private List<Box> boxes;

    public MinimizedImageBoxTest() {}

    private Path getExample() {
        String fileName = "mini.avif";
        return getPathFromResourceName(fileName);
    }

    private Path getPathFromResourceName(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return Paths.get(classLoader.getResource(fileName).getPath());
    }

    @BeforeTest
    public void parseFile() throws IOException {
        Path testFile = getExample();
        FileParser fileParser = new FileParser();
        boxes = fileParser.parse(testFile);
    }

    @Test
    public void checkRightNumberOfBoxes() {
        assertEquals(boxes.size(), 2);
    }

    @Test
    public void checkFileType() {
        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof FileTypeBox);
        FileTypeBox ftyp = (FileTypeBox) box0;
        assertEquals(ftyp.getMajorBrand().asUnsigned(), new FourCC("mif3").asUnsigned());
        assertEquals(ftyp.getMinorVersion(), new FourCC("avif").asUnsigned());
    }

    @Test
    public void checkMinimizedImageBox() throws FileNotFoundException, IOException {
        Box box1 = boxes.get(1);
        assertTrue(box1 instanceof MinimizedImageBox);
        MinimizedImageBox mini = (MinimizedImageBox) box1;
        assertEquals(mini.getVersion(), 0);
        assertFalse(mini.isExplicitCodecTypesFlag());
        assertFalse(mini.isFloatFlag());
        assertTrue(mini.isFullRangeFlag());
        assertFalse(mini.isAlphaFlag());
        assertFalse(mini.isExplicitCICPFlag());
        assertFalse(mini.isHdrFlag());
        assertFalse(mini.isIccFlag());
        assertFalse(mini.isExifFlag());
        assertFalse(mini.isXmpFlag());
        assertEquals(mini.getChromaSubsampling(), MinimizedImageBox.ChromaSubsampling.None);
        assertEquals(mini.getOrientationMinus1(), 0);
        assertEquals(mini.getWidth(), 256);
        assertEquals(mini.getHeight(), 256);
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos1);
        mini.writeTo(writer);
        byte[] writtenBytes = baos1.toByteArray();
        assertEquals(writtenBytes.length, 6828);
        File outputFile1 = new File("box.dat");
        try (FileOutputStream outputStream1 = new FileOutputStream(outputFile1)) {
            // outputStream.write(config);
            outputStream1.write(writtenBytes);
        }
        byte[] config = mini.getMainItemCodecConfig();
        byte[] data = mini.getMainItemData();
        File outputFile = new File("mini.dat");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            // outputStream.write(config);
            outputStream.write(data);
        }
        String[] cmd = {"aomdec", "--output=out.y4m", "mini.dat"};
        Process r = Runtime.getRuntime().exec(cmd);
        byte[] errorBytes = r.getErrorStream().readAllBytes();
        System.out.println(new String(errorBytes, StandardCharsets.UTF_8));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box1.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        System.out.println(bytes.length);
    }
}
