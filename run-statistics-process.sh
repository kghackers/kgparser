# declare required variables
# todo: we should be able to override them from the command line
LOG4J_XML_FILE_PATH=log4j2.xml
KGSTATS_JAR_FILE_PATH=kgstatsSrv/target/kgstats-srv-1.0.jar
SPRING_CONFIG_LOCATION=kgstatsSrv/src/main/resources/application.actions.properties
DATABASE_USER=root
DATABASE_PASSWORD=root
DATABASE_NAME=actions
INPUT_CONFIG_FILE_NAME=/d/kg/config.json
OUTPUT_CONFIG_FILE_NAME=/d/kg/config-output.json
GENERATE_STATISTICS_DIRECTORY=/d/kg/stats/

# download statistics from Klavogonki to JSON files
java \
-Dfile.encoding=UTF8 \
-Dlog4j.configurationFile=$LOG4J_XML_FILE_PATH \
-jar $KGSTATS_JAR_FILE_PATH \
DOWNLOAD_PLAYER_DATA \
$INPUT_CONFIG_FILE_NAME \
$OUTPUT_CONFIG_FILE_NAME

# create MySQL database
mysql \
-u$DATABASE_USER \
-p$DATABASE_PASSWORD \
-e "create database ${DATABASE_NAME};"

# import statistics to database
# spring.config.location can contain file path or the directory (directory should end with /)
# see https://docs.spring.io/spring-boot/docs/1.0.1.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files
java \
-Dfile.encoding=UTF8 \
-Dlog4j.configurationFile=$LOG4J_XML_FILE_PATH \
-Dspring.profiles.active=database \
-Dspring.config.location=$SPRING_CONFIG_LOCATION \
-jar $KGSTATS_JAR_FILE_PATH \
IMPORT_JSON_TO_DATABASE \
$OUTPUT_CONFIG_FILE_NAME

# add indices to the database

# generate statistics from database

mkdir $GENERATE_STATISTICS_DIRECTORY
mkdir $GENERATE_STATISTICS_DIRECTORY/js
mkdir $GENERATE_STATISTICS_DIRECTORY/xlsx

# spring.config.location can contain file path or the directory (directory should end with /)
# see https://docs.spring.io/spring-boot/docs/1.0.1.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files
java \
-Dfile.encoding=UTF8 \
-Dlog4j.configurationFile=$LOG4J_XML_FILE_PATH \
-Dspring.profiles.active=database \
-Dspring.config.location=$SPRING_CONFIG_LOCATION \
-jar $KGSTATS_JAR_FILE_PATH \
GENERATE_STATISTICS_FROM_DATABASE \
$OUTPUT_CONFIG_FILE_NAME

# copy static files from sources of kgstatsWeb to generated statistics
# todo: copy ignore html, zip and xlsx

# zip the statistics site

# create database dump

# drop MySQL database
mysql \
-u$DATABASE_USER \
-p$DATABASE_PASSWORD \
-e "drop database ${DATABASE_NAME};"
