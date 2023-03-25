package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class RegionItemParser {

    public RegionItem parse(ParseContext parseContext) {
        int version = parseContext.readByte();
        int flags = parseContext.readByte();
        if ((flags & 0x01) == 0x01) {
            return parseLongForm(parseContext);
        } else {
            return parseShortForm(parseContext);
        }
    }

    private RegionItem parseLongForm(ParseContext parseContext) {
        throw new UnsupportedOperationException("Long form (flags 0x01) is not supported yet.");
    }

    private RegionItem parseShortForm(ParseContext parseContext) {
        RegionItem regionItem = new RegionItem();
        regionItem.setReferenceWidth(parseContext.readUnsignedInt16());
        regionItem.setReferenceHeight(parseContext.readUnsignedInt16());
        int numRegions = parseContext.readByte();
        for (int i = 0; i < numRegions; i++) {
            GeometryType geometryType = GeometryType.fromEncodedValue(parseContext.readByte());
            switch (geometryType) {
                case POINT:
                    regionItem.addRegion(parsePointShortForm(parseContext));
                    break;
                case RECTANGLE:
                    regionItem.addRegion(parseRectangleShortForm(parseContext));
                    break;
                case ELLIPSE:
                    regionItem.addRegion(parseEllipseShortForm(parseContext));
                    break;
                case POLYGON:
                    regionItem.addRegion(parsePolygonShortForm(parseContext));
                    break;
                case REFERENCED_MASK:
                    regionItem.addRegion(parseReferencedMaskShortForm(parseContext));
                    break;
                case INLINE_MASK:
                    throw new UnsupportedOperationException("Inline masks are not yet supported");
                case POLYLINE:
                    regionItem.addRegion(parsePolylineShortForm(parseContext));
                    break;
                default:
                    throw new AssertionError(geometryType.name());
            }
        }

        return regionItem;
    }

    private Point parsePointShortForm(ParseContext parseContext) {
        int x = parseContext.readInt16();
        int y = parseContext.readInt16();
        Point point = new Point(x, y);
        return point;
    }

    private Rectangle parseRectangleShortForm(ParseContext parseContext) {
        int x = parseContext.readInt16();
        int y = parseContext.readInt16();
        int width = parseContext.readUnsignedInt16();
        int height = parseContext.readUnsignedInt16();
        Rectangle rectangle = new Rectangle(x, y, width, height);
        return rectangle;
    }

    private Ellipse parseEllipseShortForm(ParseContext parseContext) {
        int x = parseContext.readInt16();
        int y = parseContext.readInt16();
        int radiusX = parseContext.readUnsignedInt16();
        int radiusY = parseContext.readUnsignedInt16();
        Ellipse ellipse = new Ellipse(x, y, radiusX, radiusY);
        return ellipse;
    }

    private Polygon parsePolygonShortForm(ParseContext parseContext) {
        Polygon poly = new Polygon();
        parsePolyShortForm(parseContext, poly);
        return poly;
    }

    private Polyline parsePolylineShortForm(ParseContext parseContext) {
        Polyline poly = new Polyline();
        parsePolyShortForm(parseContext, poly);
        return poly;
    }

    private void parsePolyShortForm(ParseContext parseContext, Poly poly) {
        int pointCount = parseContext.readUnsignedInt16();
        for (int i = 0; i < pointCount; i++) {
            poly.addPoint(parsePointShortForm(parseContext));
        }
    }

    private ReferencedMask parseReferencedMaskShortForm(ParseContext parseContext) {
        int x = parseContext.readInt16();
        int y = parseContext.readInt16();
        int width = parseContext.readUnsignedInt16();
        int height = parseContext.readUnsignedInt16();
        ReferencedMask referencedMask = new ReferencedMask(x, y, width, height);
        return referencedMask;
    }
}
