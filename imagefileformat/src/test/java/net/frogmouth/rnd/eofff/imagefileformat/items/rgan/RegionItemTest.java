package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import jdk.incubator.foreign.MemorySegment;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.testng.annotations.Test;

/** Unit tests for Region Item */
public class RegionItemTest {

    public RegionItemTest() {}

    private final byte[] RGAN_POINT_BYTES =
            new byte[] {
                0x00, 0x00, 0x27, 0x10, 0x1f, 0x40, 0x01, 0x00, 0x07, (byte) 0xd0, 0x0b, (byte) 0xb8
            };

    private final byte[] RGAN_RECTANGLE_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x27,
                0x10,
                0x1f,
                0x40,
                0x01,
                0x01,
                0x07,
                (byte) 0xd0,
                0x0b,
                (byte) 0xb8,
                0x00,
                0x64,
                0x01,
                (byte) 0x90
            };

    private final byte[] RGAN_ELLIPSE_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x27,
                0x10,
                0x1f,
                0x40,
                0x01,
                0x02,
                0x07,
                (byte) 0xd0,
                0x0b,
                (byte) 0xb8,
                0x00,
                0x64,
                0x01,
                (byte) 0x90
            };

    @Test
    public void makePoint() throws IOException {
        RegionItem rgan = new RegionItem();
        rgan.setReferenceWidth(10000);
        rgan.setReferenceHeight(8000);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 0);
        Region pointRegion = new Point(2000, 3000);
        rgan.addRegion(pointRegion);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.POINT);
        Point point = (Point) rgan.getRegions().get(0);
        assertEquals(point.getX(), 2000);
        assertEquals(point.getY(), 3000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_POINT_BYTES);
    }

    @Test
    public void parsePoint() throws IOException {
        ParseContext parseContext = new ParseContext(MemorySegment.ofArray(RGAN_POINT_BYTES));
        RegionItemParser parser = new RegionItemParser();
        RegionItem rgan = parser.parse(parseContext);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.POINT);
        Point point = (Point) rgan.getRegions().get(0);
        assertEquals(point.getX(), 2000);
        assertEquals(point.getY(), 3000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_POINT_BYTES);
    }

    @Test
    public void makeRectangle() throws IOException {
        RegionItem rgan = new RegionItem();
        rgan.setReferenceWidth(10000);
        rgan.setReferenceHeight(8000);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 0);
        Region rectangleRegion = new Rectangle(2000, 3000, 100, 400);
        rgan.addRegion(rectangleRegion);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.RECTANGLE);
        Rectangle rectangle = (Rectangle) rgan.getRegions().get(0);
        assertEquals(rectangle.getX(), 2000);
        assertEquals(rectangle.getY(), 3000);
        assertEquals(rectangle.getWidth(), 100);
        assertEquals(rectangle.getHeight(), 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_RECTANGLE_BYTES);
    }

    @Test
    public void parseRectangle() throws IOException {
        ParseContext parseContext = new ParseContext(MemorySegment.ofArray(RGAN_RECTANGLE_BYTES));
        RegionItemParser parser = new RegionItemParser();
        RegionItem rgan = parser.parse(parseContext);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.RECTANGLE);
        Rectangle rectangle = (Rectangle) rgan.getRegions().get(0);
        assertEquals(rectangle.getX(), 2000);
        assertEquals(rectangle.getY(), 3000);
        assertEquals(rectangle.getWidth(), 100);
        assertEquals(rectangle.getHeight(), 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_RECTANGLE_BYTES);
    }

    @Test
    public void makeEllipse() throws IOException {
        RegionItem rgan = new RegionItem();
        rgan.setReferenceWidth(10000);
        rgan.setReferenceHeight(8000);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 0);
        Region ellipseRegion = new Ellipse(2000, 3000, 100, 400);
        rgan.addRegion(ellipseRegion);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.ELLIPSE);
        Ellipse ellipse = (Ellipse) rgan.getRegions().get(0);
        assertEquals(ellipse.getX(), 2000);
        assertEquals(ellipse.getY(), 3000);
        assertEquals(ellipse.getRadiusX(), 100);
        assertEquals(ellipse.getRadiusY(), 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_ELLIPSE_BYTES);
    }

    @Test
    public void parseEllipse() throws IOException {
        ParseContext parseContext = new ParseContext(MemorySegment.ofArray(RGAN_ELLIPSE_BYTES));
        RegionItemParser parser = new RegionItemParser();
        RegionItem rgan = parser.parse(parseContext);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.ELLIPSE);
        Ellipse ellipse = (Ellipse) rgan.getRegions().get(0);
        assertEquals(ellipse.getX(), 2000);
        assertEquals(ellipse.getY(), 3000);
        assertEquals(ellipse.getRadiusX(), 100);
        assertEquals(ellipse.getRadiusY(), 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_ELLIPSE_BYTES);
    }

    private final byte[] RGAN_POLYGON_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x27,
                0x10,
                0x1f,
                0x40,
                0x01,
                0x03,
                0x00,
                0x04,
                0x00,
                0x64,
                0x01,
                0x5e,
                0x02,
                0x58,
                0x01,
                0x5e,
                0x02,
                0x58,
                0x02,
                (byte) 0xee,
                0x00,
                0x64,
                0x02,
                (byte) 0xee
            };

    @Test
    public void makePolygon() throws IOException {
        RegionItem rgan = new RegionItem();
        rgan.setReferenceWidth(10000);
        rgan.setReferenceHeight(8000);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 0);
        Polygon polygon = new Polygon();
        polygon.addPoint(new Point(100, 350));
        polygon.addPoint(new Point(600, 350));
        polygon.addPoint(new Point(600, 750));
        polygon.addPoint(new Point(100, 750));
        rgan.addRegion(polygon);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.POLYGON);
        Polygon polygonRegion = (Polygon) rgan.getRegions().get(0);
        assertEquals(polygonRegion.getPoints().size(), 4);
        assertEquals(polygonRegion.getPoints().get(0).getX(), 100);
        assertEquals(polygonRegion.getPoints().get(0).getY(), 350);
        assertEquals(polygonRegion.getPoints().get(1).getX(), 600);
        assertEquals(polygonRegion.getPoints().get(1).getY(), 350);
        assertEquals(polygonRegion.getPoints().get(2).getX(), 600);
        assertEquals(polygonRegion.getPoints().get(2).getY(), 750);
        assertEquals(polygonRegion.getPoints().get(3).getX(), 100);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_POLYGON_BYTES);
    }

    @Test
    public void parsePolygon() throws IOException {
        ParseContext parseContext = new ParseContext(MemorySegment.ofArray(RGAN_POLYGON_BYTES));
        RegionItemParser parser = new RegionItemParser();
        RegionItem rgan = parser.parse(parseContext);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.POLYGON);
        Polygon polygonRegion = (Polygon) rgan.getRegions().get(0);
        assertEquals(polygonRegion.getPoints().size(), 4);
        assertEquals(polygonRegion.getPoints().get(0).getX(), 100);
        assertEquals(polygonRegion.getPoints().get(0).getY(), 350);
        assertEquals(polygonRegion.getPoints().get(1).getX(), 600);
        assertEquals(polygonRegion.getPoints().get(1).getY(), 350);
        assertEquals(polygonRegion.getPoints().get(2).getX(), 600);
        assertEquals(polygonRegion.getPoints().get(2).getY(), 750);
        assertEquals(polygonRegion.getPoints().get(3).getX(), 100);
        assertEquals(polygonRegion.getPoints().get(3).getY(), 750);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_POLYGON_BYTES);
    }

    private final byte[] RGAN_REFERENCED_MASK_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x27,
                0x10,
                0x1f,
                0x40,
                0x01,
                0x04,
                0x07,
                (byte) 0xd0,
                0x0b,
                (byte) 0xb8,
                0x00,
                0x64,
                0x01,
                (byte) 0x90
            };

    @Test
    public void makeReferencedMask() throws IOException {
        RegionItem rgan = new RegionItem();
        rgan.setReferenceWidth(10000);
        rgan.setReferenceHeight(8000);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 0);
        Region referencedMaskRegion = new ReferencedMask(2000, 3000, 100, 400);
        rgan.addRegion(referencedMaskRegion);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.REFERENCED_MASK);
        ReferencedMask mask = (ReferencedMask) rgan.getRegions().get(0);
        assertEquals(mask.getX(), 2000);
        assertEquals(mask.getY(), 3000);
        assertEquals(mask.getWidth(), 100);
        assertEquals(mask.getHeight(), 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_REFERENCED_MASK_BYTES);
    }

    @Test
    public void parseReferencedMask() throws IOException {
        ParseContext parseContext =
                new ParseContext(MemorySegment.ofArray(RGAN_REFERENCED_MASK_BYTES));
        RegionItemParser parser = new RegionItemParser();
        RegionItem rgan = parser.parse(parseContext);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.REFERENCED_MASK);
        ReferencedMask mask = (ReferencedMask) rgan.getRegions().get(0);
        assertEquals(mask.getX(), 2000);
        assertEquals(mask.getY(), 3000);
        assertEquals(mask.getWidth(), 100);
        assertEquals(mask.getHeight(), 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_REFERENCED_MASK_BYTES);
    }

    private final byte[] RGAN_POLYLINE_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x27,
                0x10,
                0x1f,
                0x40,
                0x01,
                0x06,
                0x00,
                0x04,
                0x00,
                0x64,
                0x01,
                0x5e,
                0x02,
                0x58,
                0x01,
                0x5e,
                0x02,
                0x58,
                0x02,
                (byte) 0xee,
                0x00,
                0x64,
                0x02,
                (byte) 0xee
            };

    @Test
    public void makePolyline() throws IOException {
        RegionItem rgan = new RegionItem();
        rgan.setReferenceWidth(10000);
        rgan.setReferenceHeight(8000);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 0);
        Polyline polyline = new Polyline();
        polyline.addPoint(new Point(100, 350));
        polyline.addPoint(new Point(600, 350));
        polyline.addPoint(new Point(600, 750));
        polyline.addPoint(new Point(100, 750));
        rgan.addRegion(polyline);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.POLYLINE);
        Polyline polylineRegion = (Polyline) rgan.getRegions().get(0);
        assertEquals(polylineRegion.getPoints().size(), 4);
        assertEquals(polylineRegion.getPoints().size(), 4);
        assertEquals(polylineRegion.getPoints().get(0).getX(), 100);
        assertEquals(polylineRegion.getPoints().get(0).getY(), 350);
        assertEquals(polylineRegion.getPoints().get(1).getX(), 600);
        assertEquals(polylineRegion.getPoints().get(1).getY(), 350);
        assertEquals(polylineRegion.getPoints().get(2).getX(), 600);
        assertEquals(polylineRegion.getPoints().get(2).getY(), 750);
        assertEquals(polylineRegion.getPoints().get(3).getX(), 100);
        assertEquals(polylineRegion.getPoints().get(3).getY(), 750);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_POLYLINE_BYTES);
    }

    @Test
    public void parsePolyline() throws IOException {
        ParseContext parseContext = new ParseContext(MemorySegment.ofArray(RGAN_POLYLINE_BYTES));
        RegionItemParser parser = new RegionItemParser();
        RegionItem rgan = parser.parse(parseContext);
        assertEquals(rgan.getReferenceWidth(), 10000);
        assertEquals(rgan.getReferenceHeight(), 8000);
        assertEquals(rgan.getRegions().size(), 1);
        assertEquals(rgan.getRegions().get(0).getGeometryType(), GeometryType.POLYLINE);
        Polyline polylineRegion = (Polyline) rgan.getRegions().get(0);
        assertEquals(polylineRegion.getPoints().size(), 4);
        assertEquals(polylineRegion.getPoints().get(0).getX(), 100);
        assertEquals(polylineRegion.getPoints().get(0).getY(), 350);
        assertEquals(polylineRegion.getPoints().get(1).getX(), 600);
        assertEquals(polylineRegion.getPoints().get(1).getY(), 350);
        assertEquals(polylineRegion.getPoints().get(2).getX(), 600);
        assertEquals(polylineRegion.getPoints().get(2).getY(), 750);
        assertEquals(polylineRegion.getPoints().get(3).getX(), 100);
        assertEquals(polylineRegion.getPoints().get(3).getY(), 750);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, RGAN_POLYLINE_BYTES);
    }
}
