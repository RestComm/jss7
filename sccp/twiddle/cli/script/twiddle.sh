#!/bin/sh
### ====================================================================== ###
##                                                                          ##
##  JBoss Shutdown Script                                                   ##
##                                                                          ##
### ====================================================================== ###

### $Id: twiddle.sh 88145 2009-05-04 12:24:46Z ispringer $ ###

DIRNAME=`pwd`
PROGNAME=`basename $0`
GREP="grep"

#
# Helper to complain.
#
die() {
    echo "${PROGNAME}: $*"
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
    [ -n "$JBOSS_HOME" ] &&
        JBOSS_HOME=`cygpath --unix "$JBOSS_HOME"`
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi


# Setup the JVM
if [ "x$JAVA_HOME" != "x" ]; then
    JAVA=$JAVA_HOME/bin/java
else
    JAVA="java"
fi

#debug
#JAVA_OPTS="$JAVA_OPTS -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y"

# Setup the classpath

#if [ "x$JBOSS_CLASSPATH" = "x" ]; then
 	

 	#jboss
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/twiddle.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/getopt.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/log4j.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-jmx.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-common-client.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jmx-invoker-adaptor-client.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jmx-client.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jnp-client.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-serialization.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-minimal.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/javaee-api.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-security-spi.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jboss-transaction-spi.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/concurrent.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/dom4j.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/jbossx-security-client.jar"
    
    #mobicents
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/cli-twiddle.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/sccp-api.jar"
    JBOSS_CLASSPATH="$JBOSS_CLASSPATH:${DIRNAME}/lib/sccp-impl.jar"
    
   
#fi

SCCP_TWIDDLE_CONF="$DIRNAME/lib/sccp-twiddle.properties"


# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    JBOSS_CLASSPATH=`cygpath --path --windows "$JBOSS_CLASSPATH"`
	SCCP_TWIDDLE_CONF=`cygpath --path --windows "$SCCP_TWIDDLE_CONF"`
fi
 # Display our environment
 #     echo "========================================================================="
 #     echo ""
 #     echo "  CLI Bootstrap Environment"
 #     echo ""
 #     echo "  DIR      : $DIRNAME"
 #     echo ""
 #     echo "  JAVA     : $JAVA"
 #     echo ""
 #     echo "  JAVA_OPTS: $JAVA_OPTS"
 #     echo ""
 #     echo "  CLASSPATH: $JBOSS_CLASSPATH"
 #     echo ""
 #     echo "  OPTS     : $*"
 #     echo ""
 #     echo "  CONF     : $SCCP_TWIDDLE_CONF"
 #     echo ""
 #     echo "========================================================================="
 #     echo ""

# Execute the JVM
exec "$JAVA" \
    $JAVA_OPTS \
    -Dprogram.name="$PROGNAME" \
    -classpath $JBOSS_CLASSPATH \
    org.jboss.console.twiddle.Twiddle -c file:///$SCCP_TWIDDLE_CONF $@ 
	#
