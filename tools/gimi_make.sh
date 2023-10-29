TOOL="/usr/lib/jvm/java-17-openjdk-amd64/bin/java --add-modules=jdk.incubator.foreign -cp target/tools-0.1.0-SNAPSHOT.jar net.frogmouth.rnd.eofff.tools.gimi.GIMISum"
NITF2GIMI="/usr/lib/jvm/java-17-openjdk-amd64/bin/java --add-modules=jdk.incubator.foreign -cp target/tools-0.1.0-SNAPSHOT.jar net.frogmouth.rnd.eofff.tools.gimi.NITF2GIMI"
SOURCEFILE="/home/bradh/gdal_hacks/ACT Government/Imagery/Aerial Photography/ACT2020_wgs_84_trimmed.tif"


COPYRIGHT="CC-BY Australian Capital Territory and MetroMap"
SECURITY_MARKINGS="--securityLevel=SECRETIVE-ISH --caveat=ButterPopcorn --caveat=LowPlaces  --relTo=US --relTo=AUS --relTo=UK"
TARGET_BASE_DIR="/home/bradh/ogc-developer-events/2023/Open-Standards-Code-Sprint/publicly-releasable-sample-data/bradh"
EXPERIMENTAL_TARGET_BASE_DIR=$TARGET_BASE_DIR/experimental
METADATA="--missionId=Mission3 --dateTime=2020-05-20T04:30:35"

CAPELLACOPYRIGHT="CC-BY 4.0. Capella Space Synthetic Aperture Radar (SAR) Open Dataset from https://registry.opendata.aws/capella_opendata."

echo
echo "## Primary files"

echo
echo "### HEIC file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed.heic with options:
echo $SECURITY_MARKINGS
echo $METADATA
$TOOL --codec=HEIC $SECURITY_MARKINGS $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed.heic 

echo
echo "### HEIC file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed_unrestricted.heic with options:
echo "--securityLevel=UNRESTRICTED"
echo $METADATA
$TOOL --codec=HEIC --securityLevel=UNRESTRICTED --copyright="$COPYRIGHT" $METADATA "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed_unrestricted.heic 

echo
echo "### HEIC file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed_totally_sec.heic with options:
echo "--securityLevel=TOTALLY SECRET"
echo $METADATA
$TOOL --codec=HEIC --securityLevel=TOTALLY\ SECRET --copyright="$COPYRIGHT" $METADATA "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed_totally_sec.heic 

echo
echo "### HEIC file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed_no_security.heic with options:
echo $METADATA
$TOOL --codec=HEIC --noSecurity $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed_no_security.heic 

echo
echo "### HEIC file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed_no_corners.heic with options:
echo $SECURITY_MARKINGS
$TOOL --codec=HEIC $SECURITY_MARKINGS --noCornerPoints --copyright="$COPYRIGHT" "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed_no_corners.heic 

echo
echo "### Uncompressed HEIF file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed.heif with options:
echo $SECURITY_MARKINGS
echo $METADATA
$TOOL --codec=UNCOMPRESSED $SECURITY_MARKINGS $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed.heif

echo
echo "### JPEG200 HEIF file:" $TARGET_BASE_DIR/ACT2020_wgs_84_trimmed.hej2 with options:
echo $SECURITY_MARKINGS
echo $METADATA
$TOOL --codec=JPEG2000 $SECURITY_MARKINGS $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed.hej2

echo
echo "## Non-conformant files - exploration ideas"
echo
echo "### JPEG HEIF file:" $EXPERIMENTAL_TARGET_BASE_DIR/ACT2020_wgs_84_trimmed.hejp with options:
echo $SECURITY_MARKINGS
echo $METADATA
$TOOL --codec=JPEG $SECURITY_MARKINGS $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$EXPERIMENTAL_TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed.hejp

echo
echo "### AVIF (AV1) HEIF file:" $EXPERIMENTAL_TARGET_BASE_DIR/ACT2020_wgs_84_trimmed.avif with options:
echo $SECURITY_MARKINGS
echo $METADATA
$TOOL --codec=AVIF $SECURITY_MARKINGS $METADATA --copyright="$COPYRIGHT" "$SOURCEFILE" "$EXPERIMENTAL_TARGET_BASE_DIR"/ACT2020_wgs_84_trimmed.avif

echo
echo "## Monochrome SAR files"
echo
echo "Derived from CC-BY 4.0. Capella Space Synthetic Aperture Radar (SAR) Open Dataset from https://registry.opendata.aws/capella_opendata."
echo Imagery is central Paris. Expect layover of the Eiffel Tower to be to the south.
echo
echo "### Uncompressed HEIF file:" $TARGET_BASE_DIR/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.heif
$NITF2GIMI --copyright="$CAPELLACOPYRIGHT" "/home/bradh/tempdata/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.ntf" "$TARGET_BASE_DIR"/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.heif

echo
echo "### Uncompressed HEIF file:" $TARGET_BASE_DIR/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600_tiled.heif
$NITF2GIMI --copyright="$CAPELLACOPYRIGHT" --tiled=256 "/home/bradh/tempdata/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.ntf" "$TARGET_BASE_DIR"/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600_tiled.heif
