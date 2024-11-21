#export ROOT_WORKING_DIR=/d/kg/
export ROOT_WORKING_DIR=/c/kg/2024.11.11

export LOG4J_XML_FILE_PATH=./log4j2.xml

export KGSTATS_SRV_JAR_FILE_PATH=kgstatsSrv/target/kgstats-srv-1.0.jar
# todo: maybe take kgstatsSrv statics from target?
export KGSTATS_SRV_SQL_DIR=kgstatsSrv/src/main/resources/sql
# todo: maybe take kgstatsWeb statics from target?
export KGSTATS_WEB_ROOT_DIR=kgstatsWeb/src/main/webapp
export SPRING_CONFIG_LOCATION=kgstatsSrv/src/main/resources/application.actions.properties
export GENERATE_STATISTICS_CONFIG_LOCATION=kgstatsSrv/src/main/resources/statistics/statistics-generator-config-generate-all.json

export DATABASE_USER=root
export DATABASE_PASSWORD=root
export DATABASE_NAME=actions
