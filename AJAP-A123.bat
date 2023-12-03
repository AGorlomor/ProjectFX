:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: SCRIPT CST8221 - Fall 2023
:: ---------------------------------------------------------------------
:: Begin of Script (A13 - F23)
:: ---------------------------------------------------------------------

CLS

:: LOCAL VARIABLES ....................................................

SET LIBDIR=lib/javafx/lib
SET SRCDIR=src
SET BINDIR=bin
SET BINERR=jap-javac.err
SET JARNAME=jap.jar
SET JAROUT=jap-jar.out
SET JARERR=jap-jar.err
SET DOCDIR=Docs
SET DOCPACK=src/main/java\Projects
SET PACKAGE=Projects
SET DOCERR=jap-javadoc.err
SET MAINCLASSSRC=src/main/java/Projects/Main.java
SET MAINCLASSBIN=target/classes/Projects/Main.class
SET IMAGES=target/classes
SET RESOURCES=src/main/resources


@echo off

ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @          F A L L - 2 0 2 3         @  @"
ECHO "@          ###    ##        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@        ###    ##                                                  @"
ECHO "@        ##    ###                 ###                              @"
ECHO "@         ##    ###                ###                              @"
ECHO "@           ##    ##               ###   #####  ##     ##  #####    @"
ECHO "@         (     (      ((((()      ###       ## ###   ###      ##   @"
ECHO "@     ((((     ((((((((     ()     ###   ######  ###  ##   ######   @"
ECHO "@        ((                ()      ###  ##   ##   ## ##   ##   ##   @"
ECHO "@         ((((((((((( ((()         ###   ######    ###     ######   @"
ECHO "@         ((         ((           ###                               @"
ECHO "@          (((((((((((                                              @"
ECHO "@   (((                      ((        /-----------------------\    @"
ECHO "@    ((((((((((((((((((((() ))         |  COMPUTATIONAL MODEL  |    @"
ECHO "@         ((((((((((((((((()           \-----------------------/    @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

ECHO "[LABS SCRIPT ---------------------]"


ECHO "0. Preconfiguring ................."
:: mkdir "%BINDIR%/%RESOURCES%"
:: xcopy "%SRCDIR%/%RESOURCES%" "%BINDIR%/%RESOURCES%" /Y
:: mkdir "%BINDIR%/%IMAGES%"
:: xcopy "%SRCDIR%/%IMAGES%" "%BINDIR%/%IMAGES%" /Y
mkdir "%BINDIR%"
mkdir "%BINDIR%\%PACKAGE%"
copy "%SRCDIR%\%PACKAGE%\*.png" "%BINDIR%\%PACKAGE%"
copy "%SRCDIR%\%PACKAGE%\*.gif" "%BINDIR%\%PACKAGE%"

ECHO "1. Compiling ......................"
javac -Xlint -cp ".;%LIBDIR%/*" %MAINCLASSSRC% -d bin
::javac -Xlint -cp ".;%SRCDIR%" %MAINCLASSSRC% -d %BINDIR% 2> %BINERR%
::javac -Xlint -cp ".;%SRCDIR%" %MAINCLASSSRC% -d %BINDIR%

:: ECHO "Running (outside JAR) ........................."
:: start java -cp ".;bin;/SOFT/copy/dev/java/javafx/lib/*" CST8221.Main

ECHO "2. Creating Jar ..................."
cd bin
jar cvfe CST8221.jar Lab . > labs-jar.out
::jar cvfe %JARNAME% %MAINCLASSBIN% . > ../%JAROUT% 2> ../%JARERR%
::jar cvfe %JARNAME% %MAINCLASSBIN% . 

ECHO "3. Creating Javadoc ..............."
cd ..
::javadoc -cp ".;bin;/SOFT/copy/dev/java/javafx/lib/*;/SOFT/COPY/db/derby/lib/*;/SOFT/COPY/dev/LIBS/jar/javax.servlet.jar" --module-path "C:\SOFT\COPY\dev\LIBS\javafx\lib" --add-modules javafx.controls -d doc -sourcepath src -subpackages CST8221 2> labs-javadoc.err
::javadoc -cp ".;%BINDIR%;../%LIBDIR%" --module-path "%LIBDIR%" -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% 2> %DOCERR%
javadoc -cp ".;%BINDIR%;../%LIBDIR%" --module-path "%LIBDIR%" -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% 

ECHO "4. Running Jar ...................."
cd bin
::start java --module-path "/SOFT/COPY/dev/LIBS/javafx/lib;/SOFT/COPY/db/derby/lib" --add-modules javafx.controls,javafx.fxml -jar CST8221.jar
start java --module-path "../%LIBDIR%" -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (A13 - F23)
:: ---------------------------------------------------------------------
