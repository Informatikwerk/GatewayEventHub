#!/bin/sh

echo Backup db script started...
echo DATE=`date '+%Y_%m_%d_%H_%M_%S'`

echo Number of GEH db backup dumps:
NUM_GEH_DB=$(find /media/app/data/backupDumps/gatewayeventhub* -maxdepth 0 | wc -l)
echo $NUM_GEH_DB

if [ "$NUM_GEH_DB" -gt 3 ]
	then echo WARNING: You have over 3 backup dumps!
fi

echo Making GEH DB Backup as db dump
mysqldump -h 127.0.0.1 --port 3312 -u root gatewayeventhub  --max_allowed_packet=512M > /media/app/data/backupDumps/gatewayeventhub`date '+%Y_%m_%d_%H_%M_%S'`.sql
echo GEH DB Backup done!

echo ...backup done!