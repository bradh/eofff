Test files
==========

The PNG is a rendering of the corresponding test file.

** test_siff_rgb_component_rgan.mp4

SIFF demonstration file.

Uncompressed primary image item, RGB component interleaved. Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240
pixels high. The order of colours (left to right, top to bottom) is red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

Includes region annotation (rectangle and pixel) on the yellow box.

Includes user description (`udes`) properties for the overall image and the region annotation.

Includes MIMD security metadata at the top (file) level.

Includes ST 1603 timestamp and (limited) time transfer metadata.

Includes ST 1204 Core ID.

Metadata:

``` txt
MIMD
    Version: 1
    CompositeProductSecurity: REF<Security>(2, 1)
    SecurityOptions: LIST[Security]
        Security: [Security]
            MIMD Id: [2, 1]
            ClassifyingMethod: US-1
            Classification: UNCLASSIFIED//
Nano Time Transfer Pack
    Nano Precision Time Stamp: 1679802942290000000 ns
    Time Transfer Local Set: Time Transfer
        Document Version: ST 1603.2
        Time Transfer Parameters: Parameters
            Reference Source: Reference Source status is unknown
            Correction Method: Unknown or No Correction
            Time Transfer Method: Unknown Time Transfer Method
CoreID: 0102:3641-A9E2-624F-4346-8F8E-666A-9BAA-08BD:2A
```

** test_uncompressed_2vuy.mp4

House fire, YCbCr 4:2:2, packed Cb Y0 Cr Y1

Derived from "controlled_burn_1080p.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

Conforms to "2vuy" profile.

** test_uncompressed_abgr.mp4

ABGR 32-bit packed.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

Conforms to "abgr" profile.

** test_uncompressed_bgr.mp4

BGR 24-bit packed.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

** test_uncompressed_bgr_sbpm.mp4

As for test_uncompressed_bgr, but has bad pixels (6 rows, 3 columns, and 8 other pixels in a block). Bad rows are white. Bad columns and individual pixels are black.

Has corresponding SensorBadPixelsMapBox property.

** test_uncompressed_i420.mp4

Street scene, YCbCr 4:2:0, planar YCbCr.

Derived from "pedestrian_area_1080p25.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

Conforms to "i420" profile.

** test_uncompressed_nv12.mp4

Street scene, YCbCr NV12 format.

Derived from "pedestrian_area_1080p25.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

Conforms to "nv12" profile.

** test_uncompressed_nv21.mp4

Street scene, YCbCr NV12 format.

Derived from "pedestrian_area_1080p25.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

Conforms to "nv21" profile.

** test_uncompressed_rgb3.mp4

RGB 24-bit packed.

Horizontal bars (top-to-bottom order: red, green, blue, white in equal proportions).

Conforms to "rgb3" profile.

** test_uncompressed_rgb555_block_be.mp4

RGB 5-5-5 packed into 2 bytes. Block big endian, padding MSB.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

** test_uncompressed_rgb555_block_be_pad_lsb.mp4

RGB 5-5-5 packed into 2 bytes. Block big endian, padding LSB.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

** test_uncompressed_rgb555_block_le.mp4

RGB 5-5-5 packed into 2 bytes. Block little endian, padding MSB.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

** test_uncompressed_rgb555_block_le_pad_lsb.mp4

RGB 5-5-5 packed into 2 bytes. Block little endian, padding LSB.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

** test_uncompressed_rgb565_block_be.mp4

RGB 5-6-5 packed into 2 bytes. Block big endian.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

** test_uncompressed_rgb565_block_le.mp4

RGB 5-6-5 packed into 2 bytes. Block little endian.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

** test_uncompressed_rgba.mp4

RGBA 32-bit packed.

Horizontal bars (top-to-bottom order: red, green, blue, white in equal proportions).

** test_uncompressed_rgb_component.mp4

RGB component interleaved.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

** test_uncompressed_rgb_palette.mp4

RGB palette image.

Visual representation is grid of rectangles (four columns, three rows), where each rectangle is 320 pixels wide by 240 pixels high. The order of colours (left to right, top to bottom) is
red, green, blue, black, white, dark gray, cyan, magenta, light gray, yellow, pink, orange.

Includes `cpal` Palette Box.

** test_uncompressed_v308.mp4

Building / landscape scene, YCbCr 4:4:4 format, packed Cr Y Cb.

Derived from "in_to_tree_444_720p50.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

Conforms to "v308" profile.

** test_uncompressed_vyuy.mp4

House fire, YCbCr 4:2:2, packed Cr Y0 Cb Y1

Derived from "controlled_burn_1080p.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

Conforms to "vyuy" profile.

** test_uncompressed_yuv2.mp4

House fire, YCbCr 4:2:2, packed Y0 Cb Y1 Cr

Derived from "controlled_burn_1080p.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

Conforms to "yuv2" profile.

** test_uncompressed_yuv422.mp4

House fire, YCbCr 4:2:2, planar

Derived from "controlled_burn_1080p.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

** test_uncompressed_yuv444.mp4

Building / landscape scene, YCbCr 4:4:4 format, component interleave.

Derived from "in_to_tree_444_720p50.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

** test_uncompressed_yvyu.mp4

House fire, YCbCr 4:2:2, packed Y0 Cr Y1 Cb

Derived from "controlled_burn_1080p.y4m" standard test file sourced from <https://media.xiph.org/video/derf/>

Conforms to "yvyu" profile.
