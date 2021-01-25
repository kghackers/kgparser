echo "Arguments: <minPlayerId> <maxPlayerId> <threadsCount>"
echo ""

echo "Environment variables values: "
echo "ROOT_WORKING_DIR: $ROOT_WORKING_DIR"
echo "LOG4J_XML_FILE_PATH: $LOG4J_XML_FILE_PATH"
echo "KGSTATS_SRV_JAR_FILE_PATH: $KGSTATS_SRV_JAR_FILE_PATH"
echo "KGSTATS_SRV_SQL_DIR: $KGSTATS_SRV_SQL_DIR"
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

echo "minPlayerId: $MIN_PLAYER_ID"
echo "maxPlayerId: $MAX_PLAYER_ID"
echo "threadsCount: $THREADS_COUNT"

jq \
-n \
--arg minPlayerId $MIN_PLAYER_ID \
--arg maxPlayerId $MAX_PLAYER_ID \
--arg threadsCount $THREADS_COUNT \
--arg jsonFilesRootDir $JSON_FILES_DIRECTORY_PATH \
--arg statisticsPagesRootDir $GENERATE_STATISTICS_DIRECTORY_PATH \
'{"minPlayerId": $minPlayerId|tonumber, "maxPlayerId": $maxPlayerId|tonumber, "threadsCount": $threadsCount|tonumber, "jsonFilesRootDir": $jsonFilesRootDir, "statisticsPagesRootDir": $statisticsPagesRootDir}' \
> $INPUT_CONFIG_FILE_PATH

echo "Generated input config file $INPUT_CONFIG_FILE_PATH:"
cat $INPUT_CONFIG_FILE_PATH

## download statistics from Klavogonki to JSON files
java \
-Dfile.encoding=UTF8 \
-Dlog4j.configurationFile=$LOG4J_XML_FILE_PATH \
-jar $KGSTATS_SRV_JAR_FILE_PATH \
DOWNLOAD_PLAYER_DATA \
$INPUT_CONFIG_FILE_PATH \
$OUTPUT_CONFIG_FILE_PATH

echo "Downloaded players data from Klavogonki."

echo "Output file $OUTPUT_CONFIG_FILE_PATH:"
cat $OUTPUT_CONFIG_FILE_PATH

## create MySQL database
mysql \
-u$DATABASE_USER \
-p$DATABASE_PASSWORD \
-e "create database ${DATABASE_NAME};"

echo "Created database $DATABASE_NAME"

## import statistics to database
## spring.config.location can contain file path or the directory (directory should end with /)
## see https://docs.spring.io/spring-boot/docs/1.0.1.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files
java \
-Dfile.encoding=UTF8 \
-Dlog4j.configurationFile=$LOG4J_XML_FILE_PATH \
-Dspring.profiles.active=database \
-Dspring.config.location=$SPRING_CONFIG_LOCATION \
-jar $KGSTATS_SRV_JAR_FILE_PATH \
IMPORT_JSON_TO_DATABASE \
$OUTPUT_CONFIG_FILE_PATH

# add indices to the database
ADD_INDEXES_FILE_PATH=$KGSTATS_SRV_SQL_DIR/add-indexes.sql

mysql \
-u$DATABASE_USER \
-p$DATABASE_PASSWORD \
$DATABASE_NAME < \
$ADD_INDEXES_FILE_PATH

echo "Added indexes from file $ADD_INDEXES_FILE_PATH to database $DATABASE_NAME."

## generate statistics from database
mkdir -p $GENERATE_STATISTICS_DIRECTORY_PATH
mkdir -p $GENERATE_STATISTICS_DIRECTORY_PATH/js
mkdir -p $GENERATE_STATISTICS_DIRECTORY_PATH/xlsx

# spring.config.location can contain file path or the directory (directory should end with /)
# see https://docs.spring.io/spring-boot/docs/1.0.1.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files
java \
-Dfile.encoding=UTF8 \
-Dlog4j.configurationFile=$LOG4J_XML_FILE_PATH \
-Dspring.profiles.active=database \
-Dspring.config.location=$SPRING_CONFIG_LOCATION \
-jar $KGSTATS_SRV_JAR_FILE_PATH \
GENERATE_STATISTICS_FROM_DATABASE \
$OUTPUT_CONFIG_FILE_PATH

echo "Generated statistics files from database $DATABASE_NAME to directory $GENERATE_STATISTICS_DIRECTORY_PATH."

# copy static files from sources of kgstatsWeb to generated statistics
cp -R $KGSTATS_WEB_ROOT_DIR/css $STATIC_DIR
cp -R $KGSTATS_WEB_ROOT_DIR/img $STATIC_DIR

# todo: after we exclude the generated files from sources, copy js recursively
# cp -R $KGSTATS_WEB_ROOT_DIR/js $STATIC_DIR
cp $KGSTATS_WEB_ROOT_DIR/js/players-by-rank-chart.js $STATIC_DIR/js
cp $KGSTATS_WEB_ROOT_DIR/js/stats-data.js $STATIC_DIR/js
cp $KGSTATS_WEB_ROOT_DIR/js/stats-top-table.js $STATIC_DIR/js

# todo: old tops must be copied from archive S3 buckets, not from sources
cp -R $KGSTATS_WEB_ROOT_DIR/2020-12-09 $STATIC_DIR

echo "Copied static files from $KGSTATS_WEB_ROOT_DIR to $STATIC_DIR."

# create database dump
mysqldump -u$DATABASE_USER -p$DATABASE_PASSWORD $DATABASE_NAME > $DATABASE_DUMP_FILE_PATH

echo "Dumped database $DATABASE_NAME to file $DATABASE_DUMP_FILE_PATH."

# parse data download start date/time from output config file
DATA_DOWNLOAD_START_DATE=$(jq -r .dataDownloadStartDateString $OUTPUT_CONFIG_FILE_PATH)

echo "Parsed data download start date $DATA_DOWNLOAD_START_DATE from output config file $OUTPUT_CONFIG_FILE_PATH."

# create directory for zip files
ZIP_FILES_DIRECTORY_PATH=$ROOT_WORKING_DIR/$DATA_DOWNLOAD_START_DATE
mkdir -p "$ZIP_FILES_DIRECTORY_PATH"

echo "Created zip files directory $ZIP_FILES_DIRECTORY_PATH."

# copy input and output config files to zip files directory
cp $INPUT_CONFIG_FILE_PATH "$ZIP_FILES_DIRECTORY_PATH"
echo "Copied input config file $INPUT_CONFIG_FILE_PATH to zip files directory $ZIP_FILES_DIRECTORY_PATH."

cp $OUTPUT_CONFIG_FILE_PATH "$ZIP_FILES_DIRECTORY_PATH"
echo "Copied output config file $OUTPUT_CONFIG_FILE_PATH to zip files directory $ZIP_FILES_DIRECTORY_PATH."

# zip uses the relative paths, therefore we have to navigate to root directory
cd $JSON_FILES_DIRECTORY_PATH || exit

echo "Change to root working directory $JSON_FILES_DIRECTORY_PATH."

# zip the json files
JSON_FILES_CURRENT_DOWNLOAD_DIRECTORY_PATH=$JSON_FILES_DIRECTORY_PATH/$DATA_DOWNLOAD_START_DATE

JSON_DATA_ZIP_FILE_PATH=$ZIP_FILES_DIRECTORY_PATH/$JSON_DATA_ZIP_FILE_NAME
zip -r "$JSON_DATA_ZIP_FILE_PATH" ./"$DATA_DOWNLOAD_START_DATE"

echo "Zipped directory $JSON_FILES_CURRENT_DOWNLOAD_DIRECTORY_PATH to zip file $JSON_DATA_ZIP_FILE_PATH."

# zip uses the relative paths, therefore we have to navigate to root directory
cd $ROOT_WORKING_DIR || exit

echo "Change to root working directory $ROOT_WORKING_DIR."

# zip the statistics site
STATISTICS_ZIP_FILE_PATH=$ZIP_FILES_DIRECTORY_PATH/$STATISTICS_ZIP_FILE_NAME
zip -r "$STATISTICS_ZIP_FILE_PATH" ./$GENERATE_STATISTICS_DIRECTORY_NAME

echo "Zipped directory $GENERATE_STATISTICS_DIRECTORY_PATH to zip file $STATISTICS_ZIP_FILE_PATH."

# zip the database dump
DATABASE_DUMP_ZIP_FILE_PATH=$ZIP_FILES_DIRECTORY_PATH/$DATABASE_DUMP_ZIP_FILE_NAME
zip "$DATABASE_DUMP_ZIP_FILE_PATH" ./$DATABASE_DUMP_FILE_NAME

echo "Zipped database dump file $DATABASE_DUMP_FILE_PATH to zip file $DATABASE_DUMP_ZIP_FILE_PATH."

# copy zip to klavostat-data bucket under date/time directory
aws s3 cp "$ZIP_FILES_DIRECTORY_PATH" "s3://$S3_BUCKET_DATA/$DATA_DOWNLOAD_START_DATE" --recursive

echo "Copied zip files directory $ZIP_FILES_DIRECTORY_PATH to S3 bucket $S3_BUCKET_DATA."

# delete old files from klavostat-test bucket
aws s3 rm s3://$S3_BUCKET_TEST --recursive

echo "Deleted all files from S3 bucket $S3_BUCKET_TEST."

# copy generated statistics to klavostat-test bucket
aws s3 cp $GENERATE_STATISTICS_DIRECTORY_PATH s3://$S3_BUCKET_TEST --recursive

echo "Copied statistics directory $GENERATE_STATISTICS_DIRECTORY_PATH to S3 bucket $S3_BUCKET_TEST."

# drop MySQL database
mysql \
-u$DATABASE_USER \
-p$DATABASE_PASSWORD \
-e "drop database ${DATABASE_NAME};"

echo "Dropped database $DATABASE_NAME."
