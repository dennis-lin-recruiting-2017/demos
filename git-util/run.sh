#!/bin/sh

#echo $1 $2 $3 $4 $5 $6 $7 $8 $9

mvn exec:java -Dexec.mainClass="com.dennislin.demos.git_util.Main" -Dexec.args="$1 $2 $3 $4 $5 $6 $7 $8 $9"
