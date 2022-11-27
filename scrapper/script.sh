#!/bin/bash
# Check is Lock File exists, if not create it and set trap on exit
 if { set -C; 2>/dev/null >~/manlocktest.lock; }; then
         trap "rm -f ~/manlocktest.lock" EXIT
 else
         echo "Lock file exists… exiting"
         exit
 fi

NOWYEAR=$(date +%Y);
NOWMONTHDAY=$(date +%m%d);
SUMMERBREAK=0215;
SPRINGBREAK=0820;

if [[ 10#$NOWMONTHDAY -lt 10#$SUMMERBREAK ]] || [[ 10#$NOWMONTHDAY -gt 10#$SPRINGBREAK ]];
then
	RESULT="$NOWYEAR/$((NOWYEAR+1)) zimowy";
else
	RESULT="$((NOWYEAR-1))/$NOWYEAR letni";
fi

java -jar scrapper-*.jar -st "stacjonarne Gdańsk" -se "$RESULT"
