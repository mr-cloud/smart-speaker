#!/bin/bash

# Get home directory
BASEDIR=$(cd $(dirname $0); cd ..; pwd -P)
FEATURE="uni.mlgb.onlyapp.shit.Application"
JAVA_OPTS="-Xms1024m -Xmx1024m -XX:MaxPermSize=256m"

# Set log directory for log4j
LOG_DIR=/export/Logs

# Set Java environment
JAVA_ENV="-Dlog-dir=$LOG_DIR"

cd "${BASEDIR}/resources"

setsid java $JAVA_OPTS $JAVA_ENV -DBASEDIR=${BASEDIR} -Dhome=${BASEDIR}/resources -Djava.ext.dirs=$BASEDIR/lib $FEATURE &
