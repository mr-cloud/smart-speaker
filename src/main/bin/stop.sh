#!/bin/bash
BASEDIR=`dirname $0`/../lib
BASEDIR=`(cd "$BASEDIR"; pwd)`
FEATURE=com.jd.app.bp.data.ana.sku.stats.Main
 
pidnum=`ps -ef|grep "$BASEDIR $FEATURE"|grep -v grep|wc -l`
if [ $pidnum -lt 1 ]
	then
		echo "no program killed."
	else
	    for pid in `ps -ef|grep "$BASEDIR $FEATURE"|grep -v grep|awk '{print $2}'`
		do
			kill -15 $pid
			echo "$pid stoped."
		done
		sleep 1
		echo "program stoped."
fi
