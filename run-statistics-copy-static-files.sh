echo "Arguments: <minPlayerId> <maxPlayerId> <threadsCount>"
echo ""

echo "Environment variables values: "
echo "ROOT_WORKING_DIR: $ROOT_WORKING_DIR"
echo "LOG4J_XML_FILE_PATH: $LOG4J_XML_FILE_PATH"
echo "KGSTATS_SRV_JAR_FILE_PATH: $KGSTATS_SRV_JAR_FILE_PATH"
echo "KGSTATS_SRV_SQL_DIR: $KGSTATS_SRV_SQL_DIR"
echo "KGSTATS_WEB_ROOT_DIR: $KGSTATS_WEB_ROOT_DIR"
echo "SPRING_CONFIG_LOCATION: $SPRING_CONFIG_LOCATION"
echo "GENERATE_STATISTICS_CONFIG_LOCATION: $GENERATE_STATISTICS_CONFIG_LOCATION"
echo "DATABASE_USER: $DATABASE_USER"
echo "DATABASE_PASSWORD: $DATABASE_PASSWORD"
echo "DATABASE_NAME: $DATABASE_NAME"
echo ""

# declare required variables
# todo: set defaults, see https://stackoverflow.com/a/2013589/8534088
# todo: we should be able to override them from the command line
#ROOT_WORKING_DIR=/d/kg/

#LOG4J_XML_FILE_PATH=log4j2.xml

#KGSTATS_SRV_JAR_FILE_PATH=kgstatsSrv/target/kgstats-srv-1.0.jar
# todo: maybe take kgstatsSrv statics from target?
#KGSTATS_SRV_SQL_DIR=kgstatsSrv/src/main/resources/sql
# todo: maybe take kgstatsWeb statics from target?
#KGSTATS_WEB_ROOT_DIR=kgstatsWeb/src/main/webapp
#SPRING_CONFIG_LOCATION=kgstatsSrv/src/main/resources/application.actions.properties

#DATABASE_USER=root
#DATABASE_PASSWORD=root
#DATABASE_NAME=actions

DATABASE_DUMP_FILE_NAME=database.sql
DATABASE_DUMP_FILE_PATH=$ROOT_WORKING_DIR/$DATABASE_DUMP_FILE_NAME

JSON_FILES_DIRECTORY_NAME=/json
JSON_FILES_DIRECTORY_PATH=$ROOT_WORKING_DIR/$JSON_FILES_DIRECTORY_NAME

INPUT_CONFIG_FILE_NAME=config.json
INPUT_CONFIG_FILE_PATH=$ROOT_WORKING_DIR/$INPUT_CONFIG_FILE_NAME

OUTPUT_CONFIG_FILE_NAME=config-output.json
OUTPUT_CONFIG_FILE_PATH=$ROOT_WORKING_DIR/$OUTPUT_CONFIG_FILE_NAME

GENERATE_STATISTICS_DIRECTORY_NAME=/stats
GENERATE_STATISTICS_DIRECTORY_PATH=$ROOT_WORKING_DIR/$GENERATE_STATISTICS_DIRECTORY_NAME
STATIC_DIR=$GENERATE_STATISTICS_DIRECTORY_PATH

# zip files
JSON_DATA_ZIP_FILE_NAME=json.zip
STATISTICS_ZIP_FILE_NAME=statistics.zip
DATABASE_DUMP_ZIP_FILE_NAME=database.zip

# s3 buckets
S3_BUCKET_TEST=klavostat-test
S3_BUCKET_DATA=klavostat-data

# create config json file from input arguments
# todo: add input arguments validation, although it will already work in SpringBoot app
MIN_PLAYER_ID=$1
MAX_PLAYER_ID=$2
THREADS_COUNT=$3

# copy static files from sources of kgstatsWeb to generated statistics
cp -R $KGSTATS_WEB_ROOT_DIR/css $STATIC_DIR
cp -R $KGSTATS_WEB_ROOT_DIR/img $STATIC_DIR

# make sure /js is a directory
mkdir -p "$STATIC_DIR/js"

# make sure /xlsx is a directory
mkdir -p "$STATIC_DIR/xlsx"

# todo: after we exclude the generated files from sources, copy js recursively

# cp -R $KGSTATS_WEB_ROOT_DIR/js $STATIC_DIR
cp "$KGSTATS_WEB_ROOT_DIR/js/players-by-rank-chart.js" "$STATIC_DIR/js"
cp "$KGSTATS_WEB_ROOT_DIR/js/stats-data.js" "$STATIC_DIR/js"
cp "$KGSTATS_WEB_ROOT_DIR/js/stats-top-table.js" "$STATIC_DIR/js"

# todo: old tops must be copied from archive S3 buckets, not from sources
cp -R $KGSTATS_WEB_ROOT_DIR/2020-12-09 $STATIC_DIR

echo "Copied static files from $KGSTATS_WEB_ROOT_DIR to $STATIC_DIR."
