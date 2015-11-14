#!/bin/sh
#
### $Id: run.sh abhayani@redhat.com $ ###
#
# Mobicents Media Server Control Script
#
# To use this script run it as root - it will switch to the specified user
#
# Here is a little (and extremely primitive) startup/shutdown script
# for RedHat systems. It assumes that Mobicents Media Server lives in /usr/local/mms,
# it's run by user 'mobicents' and JDK binaries are in /usr/local/jdk/bin.
# All this can be changed in the script itself. 
#
# Either modify this script for your requirements or just ensure that
# the following variables are set correctly before calling the script.

#define where jboss is - this is the directory containing directories log, bin, conf etc
MMS_HOME=${MMS_HOME:-"/home/abhayani/workarea/mobicents/svn/trunk/servers/media/core/server-standalone/target/mms-standalone-pojo"}

#define the user under which jboss will run, or use 'RUNASIS' to run as the current user
MMS_USER=${MMS_USER:-"RUNASIS"}

#make sure java is in your path
JAVAPTH=${JAVAPTH:-"/home/abhayani/workarea/java/sdk/6.0/jdk1.6.0_18/bin"}

#if MMS_HOST specified, use -b to bind mms services to that address
MMS_BIND_ADDR=${MMS_HOST:+"-b $MMS_HOST"}

#define the classpath for the shutdown class
MMSCP=${MMSCP:-"$MMS_HOME/bin/run.jar"}

#define the script to use to start jboss
MMSSH=${MMSSH:-"$MMS_HOME/bin/run.sh $MMS_BIND_ADDR"}

if [ "$MMS_USER" = "RUNASIS" ]; then
  SUBIT=""
else
  SUBIT="su - $MMS_USER -c "
fi

if [ -n "$MMS_CONSOLE" -a ! -d "$MMS_CONSOLE" ]; then
  # ensure the file exists
  touch $MMS_CONSOLE
  if [ ! -z "$SUBIT" ]; then
    chown $MMS_USER $MMS_CONSOLE
  fi 
fi

if [ -n "$MMS_CONSOLE" -a ! -f "$MMS_CONSOLE" ]; then
  echo "WARNING: location for saving console log invalid: $MMS_CONSOLE"
  echo "WARNING: ignoring it and using /dev/null"
  MMS_CONSOLE="/dev/null"
fi

#define what will be done with the console log
MMS_CONSOLE=${MMS_CONSOLE:-"/dev/null"}

MMS_CMD_START="cd $MMS_HOME/bin; $MMSSH"
MMS_CMD_STOP=${MMS_CMD_STOP:-"java -classpath $MMSCP org.jboss.Shutdown --shutdown"}

if [ -z "`echo $PATH | grep $JAVAPTH`" ]; then
  export PATH=$PATH:$JAVAPTH
fi

if [ ! -d "$MMS_HOME" ]; then
  echo MMS_HOME does not exist as a valid directory : $MMS_HOME
  exit 1
fi

echo MMS_CMD_START = $MMS_CMD_START

case "$1" in
start)
    cd $MMS_HOME/bin
    if [ -z "$SUBIT" ]; then
        eval $MMS_CMD_START >${MMS_CONSOLE} 2>&1 & 
        echo $$ > $MMS_HOME/log/mms.pid
    else
        $SUBIT "$MMS_CMD_START >${MMS_CONSOLE} 2>&1 &" 
	echo $$ > $MMS_HOME/log/mms.pid
    fi
    ;;
stop)
    if [ -z "$SUBIT" ]; then
        $MMS_CMD_STOP
    else
        $SUBIT "$MMS_CMD_STOP"
    fi 
    ;;
restart)
    $0 stop
    $0 start
    ;;
*)
    echo "usage: $0 (start|help)"
esac
