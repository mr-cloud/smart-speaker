#!/bin/bash
BASEDIR=$(cd $(dirname $0); cd ..; pwd -P)
FEATURE="uni.mlgb.onlyapp.shit.Application"

pidnum=`ps -ef|grep "$FEATURE"|grep -v grep|wc -l`
if [ $pidnum -lt 1 ]
	then
		echo "no program killed."
	else
	    for pid in `ps -ef|grep "$FEATURE"|grep -v grep|awk '{print $2}'`
		do
			kill -9 $pid
			echo "$pid stoped."
		done
		sleep 1
		echo "program stoped."
fi
