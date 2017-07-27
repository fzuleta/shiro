#!/usr/bin/env bash
myFile="/firstrun.exists"
if [ -e "$myFile" ]; then
    echo "Waiting 5 seconds to give some time to eventstore to be setup"
    sleep 10

else
    touch "$myFile"
    echo "Waiting 5 seconds to give some time to eventstore to be setup"
    sleep 10
fi

cd /opt
java -jar $MAINJAR