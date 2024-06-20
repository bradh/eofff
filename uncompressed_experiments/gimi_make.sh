TOOL="/usr/lib/jvm/java-21-openjdk-amd64/bin/java --enable-preview -cp target/uncompressed_experiments-0.1.0-SNAPSHOT.jar net.frogmouth.rnd.eofff.uncompressed_experiments.GIMISum"
NITF2GIMI="/usr/lib/jvm/java-21-openjdk-amd64/bin/java --enable-preview -cp target/uncompressed_experiments-0.1.0-SNAPSHOT.jar net.frogmouth.rnd.eofff.uncompressed_experiments.NITF2GIMI"
SOURCEFILE="/home/bradh/gdal_hacks/ACT Government/Imagery/Aerial Photography/ACT2020_wgs_84_trimmed.tif"


COPYRIGHT="CC-BY Australian Capital Territory and MetroMap"
TARGET_BASE_DIR="/home/bradh/testbed20/silvereye/"
EXPERIMENTAL_TARGET_BASE_DIR=$TARGET_BASE_DIR/experimental
METADATA="--dateTime=2020-05-20T04:30:35"

CAPELLACOPYRIGHT="CC-BY 4.0. Capella Space Synthetic Aperture Radar (SAR) Open Dataset from https://registry.opendata.aws/capella_opendata."

echo
echo "## Primary files"

# echo
# echo "### HEIC file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed_no_security.heic with options:
# echo $METADATA
# $TOOL --codec=HEIC $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed_no_security.heic 

echo
echo "### Uncompressed HEIF file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed.heif with options:
echo $SECURITY_MARKINGS
echo $METADATA
$TOOL --codec=UNCOMPRESSED $SECURITY_MARKINGS $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed.heif

# echo
# echo "### JPEG200 HEIF file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed.hej2 with options:
# echo $SECURITY_MARKINGS
# echo $METADATA
# $TOOL --codec=JPEG2000 $SECURITY_MARKINGS $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed.hej2

# echo
# echo "## Non-conformant files - exploration ideas"
# echo
# echo "### AVIF (AV1) HEIF file:" $EXPERIMENTAL_TARGET_BASE_DIR/ACT2020_wgs_84_trimmed.avif with options:
# echo $SECURITY_MARKINGS
# echo $METADATA
# $TOOL --codec=AVIF $SECURITY_MARKINGS $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$EXPERIMENTAL_TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed.avif

# echo
# echo "## Monochrome SAR files"
# echo
# echo "Derived from CC-BY 4.0. Capella Space Synthetic Aperture Radar (SAR) Open Dataset from https://registry.opendata.aws/capella_opendata."
# echo Imagery is central Paris. Expect layover of the Eiffel Tower to be to the south.
# echo
# echo "### Uncompressed HEIF file:" $TARGET_BASE_DIR/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.heif
# $NITF2GIMI --copyright="$CAPELLACOPYRIGHT" "/home/bradh/tempdata/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.ntf" "$TARGET_BASE_DIR"/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.heif

# echo
# echo "### Uncompressed HEIF file:" $TARGET_BASE_DIR/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600_tiled.heif
# $NITF2GIMI --copyright="$CAPELLACOPYRIGHT" --tiled=256 "/home/bradh/tempdata/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.ntf" "$TARGET_BASE_DIR"/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600_tiled.heif
