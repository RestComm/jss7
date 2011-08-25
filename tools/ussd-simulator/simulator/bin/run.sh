#!/bin/sh

# In case we need it.
cygwin=false;
darwin=false;
linux=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;

    Darwin*)
        darwin=true
        ;;
        
    Linux)
        linux=true
        ;;
esac

DIRNAME=`dirname $0`
PROGNAME=`basename $0`

# Force IPv4 on Linux systems since IPv6 doesn't work correctly with jdk5 and lower
if [ "$linux" = "true" ]; then
   JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true"
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
    [ -n "$JAVAC_JAR" ] &&
        JAVAC_JAR=`cygpath --unix "$JAVAC_JAR"`
fi

# Setup TEST_CORE
if [ "x$TEST_CORE" = "x" ]; then
    # get the full path (without any relative bits)
    TEST_CORE=`cd $DIRNAME; pwd`
fi
export TEST_CORE



#Setup the JVM
if [ "x$JAVA" = "x" ]; then
    if [ "x$JAVA_HOME" != "x" ]; then
	JAVA="$JAVA_HOME/bin/java"
    else
	JAVA="java"
    fi
fi




RUN_CLASSPATH="$TEST_CORE/target/classes:$TEST_CORE/lib/appframework.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/concurrent.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/java-getopt.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/log4j.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/opencsv.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/swing-layout.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/swing-worker.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/javolution.jar"

#now SS7
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/stream.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/asn.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/mtp-api.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/sccp-api.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/sccp-impl.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/tcap-api.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/tcap-impl.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/map-api.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/map-impl.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/m3ua-api.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/m3ua-impl.jar"
RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/ussd-simulator.jar"


#log4j
#RUN_CLASSPATH="$RUN_CLASSPATH:$TEST_CORE/lib/log4j.xml"

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
    TEST_CORE=`cygpath --path --windows "$TEST_CORE"`
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    RUN_CLASSPATH=`cygpath --path --windows "$RUN_CLASSPATH"`
fi




#
warn() {
    echo "${PROGNAME}: $*"
}

#
# Helper to puke.
#
die() {
    warn $*
    exit 1
}


usage(){

echo "Usage:"
echo "bin.sh  thats it..."

}


executeTest(){
      # Display our environment
      echo "========================================================================="
      echo ""
      echo "  JBoss Bootstrap Environment"
      echo ""
      echo "  RUN_HOME : $TEST_CORE"
      echo ""
      echo "  JAVA     : $JAVA"
      echo ""
      echo "  JAVA_OPTS: $JAVA_OPTS"
      echo ""
      echo "  CLASSPATH: $RUN_CLASSPATH"
      echo ""
      echo "  MAVEN    : mvn"
      echo ""
      echo "  OPTS     : $*"
      echo ""
      echo "========================================================================="
      echo ""


      #echo "Preparing test tool jar..."
      #mvn -f $TEST_CORE/pom.xml clean install
      #mvn -f $TEST_CORE/pom.xml install

      echo ""
      echo "========================================================================="
      echo ""
      echo "JAR and dependencies are ready, executing test session"
      echo ""
      echo "========================================================================="

      "$JAVA" $JAVA_OPTS \
	-classpath "$RUN_CLASSPATH" \
	org.mobicents.protocols.ss7.ussdsimulator.UssdsimulatorApp $*


}






#if [ "$#" = "0" ]; then
#   usage
#   die
#fi

executeTest $*
