:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: ASSIGNMENTS - CST8221 - Spring 2023
:: ---------------------------------------------------------------------
:: Begin of Script (Assignments - S23)
:: ---------------------------------------------------------------------

CLS

:: LOCAL VARIABLES ....................................................

SET JAVAFXDIR=lib/javafx/lib
SET SRCDIR=src
SET BINDIR=bin
SET BINOUT=jap-javac.out
SET BINERR=jap-javac.err
SET JARNAME=jap.jar
SET JAROUT=jap-jar.out
SET JARERR=jap-jar.err
SET DOCDIR=doc
SET DOCPACK=Projects
SET DOCOUT=jap-javadoc.out
SET DOCERR=jap-javadoc.err
SET MAINCLASSSRC=src\main\java\Projects\Main.java
SET MAINCLASSBIN=Main
SET MODULELIST=javafx.controls,javafx.fxml
SET IMAGES = Images

@echo on

ECHO "                                                                     "
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @        S P R I N G - 2 0 2 3       @  @"
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
ECHO "@   (((                      ((          /-----------------\        @"
ECHO "@    ((((((((((((((((((((() ))           |     TM          |        @"
ECHO "@         ((((((((((((((((()             \-----------------/        @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "                                                                     "

:: EXECUTION STEPS  ...................................................

ECHO "0. Preconfiguring ................."
mkdir "%BINDIR%"
mkdir "%BINDIR%\%PACKAGE%"
mkdir "%BINDIR%\%SRCDIR%\%IMAGES%"
copy "%SRCDIR%\%IMAGES%\*.png" "%BINDIR%\%SRCDIR%\%IMAGES%"

ECHO "1. Compiling ......................"
javac -Xlint -cp ".;%SRCDIR%" %MAINCLASSSRC% -d %BINDIR% 2> %BINERR%
pause

ECHO "2. Creating Jar ..................."
cd bin
jar %JARNAME% %MAINCLASSBIN% . > %JAROUT% 2> %JARERR%
pause

ECHO "3. Creating Javadoc ..............."
cd ..
javadoc -cp ".;%BINDIR%;%JAVAFXDIR%" --module-path "%JAVAFXDIR%" --add-modules %MODULELIST% -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% > %DOCOUT% 2> %DOCERR%

cd bin
ECHO "4. Running Jar ...................."
start java --module-path "%JAVAFXDIR%" --add-modules %MODULELIST% -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (Assignments - S23)
:: ---------------------------------------------------------------------
