# declare required variables
# todo: we should be able to override them from the command line
LOG4J_XML_FILE_PATH=log4j2.xml
KGSTATS_SRV_JAR_FILE_PATH=kgstatsSrv/target/kgstats-srv-1.0.jar
# todo: maybe take kgstatsSrv statics from target?
KGSTATS_SRV_SQL_DIR=kgstatsSrv/src/main/resources/sql
# todo: maybe take kgstatsWeb statics from target?
KGSTATS_WEB_ROOT_DIR=kgstatsWeb/src/main/webapp
SPRING_CONFIG_LOCATION=kgstatsSrv/src/main/resources/application.actions.properties
DATABASE_USER=root
DATABASE_PASSWORD=root
DATABASE_NAME=actions
INPUT_CONFIG_FILE_NAME=/d/kg/config.json
OUTPUT_CONFIG_FILE_NAME=/d/kg/config-output.json
GENERATE_STATISTICS_DIRECTORY=/d/kg/stats/
STATIC_DIR=$GENERATE_STATISTICS_DIRECTORY

## download statistics from Klavogonki to JSON files
java \
-Dfile.encoding=UTF8 \
-Dlog4j.configurationFile=$LOG4J_XML_FILE_PATH \
-jar $KGSTATS_SRV_JAR_FILE_PATH \
DOWNLOAD_PLAYER_DATA \
$INPUT_CONFIG_FILE_NAME \
$OUTPUT_CONFIG_FILE_NAME

echo "Downloaded players data from Klavogonki."

echo "Output file $OUTPUT_CONFIG_FILE_NAME:"
cat $OUTPUT_CONFIG_FILE_NAME

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
$OUTPUT_CONFIG_FILE_NAME

# add indices to the database
ADD_INDEXES_FILE_PATH=$KGSTATS_SRV_SQL_DIR/add-indexes.sql

mysql \
-u$DATABASE_USER \
-p$DATABASE_PASSWORD \
$DATABASE_NAME < \
$ADD_INDEXES_FILE_PATH

echo "Added indexes from file $ADD_INDEXES_FILE_PATH to database $DATABASE_NAME."

## generate statistics from database
mkdir -p $GENERATE_STATISTICS_DIRECTORY
mkdir -p $GENERATE_STATISTICS_DIRECTORY/js
mkdir -p $GENERATE_STATISTICS_DIRECTORY/xlsx

# spring.config.location can contain file path or the directory (directory should end with /)
# see https://docs.spring.io/spring-boot/docs/1.0.1.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files
java \
-Dfile.encoding=UTF8 \
-Dlog4j.configurationFile=$LOG4J_XML_FILE_PATH \
-Dspring.profiles.active=database \
-Dspring.config.location=$SPRING_CONFIG_LOCATION \
-jar $KGSTATS_SRV_JAR_FILE_PATH \
GENERATE_STATISTICS_FROM_DATABASE \
$OUTPUT_CONFIG_FILE_NAME

echo "Generated statistics files from database $DATABASE_NAME to directory $GENERATE_STATISTICS_DIRECTORY."

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
# todo: implement

# zip the json files
# todo: implement

# zip the statistics site
# todo: implement

# zip the database dump
# todo: implement

# drop MySQL database
mysql \
-u$DATABASE_USER \
-p$DATABASE_PASSWORD \
-e "drop database ${DATABASE_NAME};"

echo "Dropped database $DATABASE_NAME."
