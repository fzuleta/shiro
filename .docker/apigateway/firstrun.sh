#!/usr/bin/env bash
myFile="/firstrun.exists"
if [ -e "$myFile" ]; then
    sleep 10

else
    touch "$myFile"
    sleep 10
fi

cd /opt
java -jar $MAINJAR