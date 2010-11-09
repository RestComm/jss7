@echo off
rem -------------------------------------------------------------------------
rem JBoss JVM Launcher
rem -------------------------------------------------------------------------

rem $Id: twiddle.bat 88145 2009-05-04 12:24:46Z ispringer $

if not "%ECHO%" == ""  echo %ECHO%
if "%OS%" == "Windows_NT"  setlocal

set MAIN_JAR_NAME=twiddle.jar
set MAIN_CLASS=org.jboss.console.twiddle.Twiddle

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%
set PROGNAME=run.bat
if "%OS%" == "Windows_NT" set PROGNAME=%~nx0%

rem Find MAIN_JAR, or we can't continue

set MAIN_JAR=%DIRNAME%\lib\%MAIN_JAR_NAME%
if exist "%MAIN_JAR%" goto FOUND_MAIN_JAR
echo Could not locate %MAIN_JAR%. Please check that you are in the
echo bin directory when running this script.
goto END

:FOUND_MAIN_JAR

if not "%JAVA_HOME%" == "" goto HAVE_JAVA_HOME

set JAVA=java

echo JAVA_HOME is not set.  Unexpected results may occur.
echo Set JAVA_HOME to the directory of your local JDK to avoid this message.
goto SKIP_SET_JAVA_HOME

:HAVE_JAVA_HOME

set JAVA=%JAVA_HOME%\bin\java

:SKIP_SET_JAVA_HOME

rem only include jbossall-client.jar in classpath, if
rem JBOSS_CLASSPATH was not yet set
if not "%JBOSS_CLASSPATH%" == "" GOTO HAVE_JB_CP
    rem jboss
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%MAIN_JAR%
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/getopt.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/log4j.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jboss-jmx.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jboss-common-client.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jmx-invoker-adaptor-client.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jmx-client.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jnp-client.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jboss-serialization.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jboss-minimal.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/javaee-api.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jboss-security-spi.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jboss-transaction-spi.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/concurrent.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/dom4j.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/jbossx-security-client.jar
    
    rem mobicents
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/cli-twiddle.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/sccp-api.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/sccp-impl.jar
    set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%/lib
	set JBOSS_CLASSPATH=%JBOSS_CLASSPATH%;%DIRNAME%lib/sccp-twiddle.properties

:HAVE_JB_CP
rem Set conf file path
set SCCP_TWIDDLE_CONF=%DIRNAME%lib\sccp-twiddle.properties

set JAVA_OPTS=%JAVA_OPTS% -Djboss.boot.loader.name=%PROGNAME%

"%JAVA%" %JAVA_OPTS% -classpath "%JBOSS_CLASSPATH%" %MAIN_CLASS% -c file:/%SCCP_TWIDDLE_CONF% %*

:END
