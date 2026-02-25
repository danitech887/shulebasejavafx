@echo off
setlocal enabledelayedexpansion

REM --- CONFIGURATION ---
set "APP_NAME=ShuleBase"
set "MAIN_CLASS=application.Launcher"
REM Pointing to the SDK found in your lib folder
set "JAVAFX_SDK=lib\javafx-sdk-24.0.1\lib"
set "LIB_DIR=lib"
set "SRC_DIR=src"
set "OUT_DIR=out_build"
set "DIST_DIR=dist"
set "INPUT_DIR=input_jars"

REM --- CLEANUP ---
if exist "%OUT_DIR%" rmdir /s /q "%OUT_DIR%"
if exist "%DIST_DIR%" rmdir /s /q "%DIST_DIR%"
if exist "%INPUT_DIR%" rmdir /s /q "%INPUT_DIR%"
mkdir "%OUT_DIR%"
mkdir "%INPUT_DIR%"

REM --- COMPILE ---
echo Compiling sources...
if exist sources.txt del sources.txt
for /R "%SRC_DIR%" %%f in (*.java) do (
    set "JAVA_FILE=%%f"
    set "JAVA_FILE=!JAVA_FILE:\=/!"
    echo "!JAVA_FILE!">> sources.txt
)

REM Build classpath string from lib directory for external jars (like mysql, itext)
set "CP="
for %%i in ("%LIB_DIR%\*.jar") do set "CP=!CP!;%%i"

javac --module-path "%JAVAFX_SDK%" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing -d "%OUT_DIR%" -cp "%CP%" @sources.txt
if %errorlevel% neq 0 (
    echo Compilation failed!
    del sources.txt
    pause
    exit /b 1
)
del sources.txt

REM --- COPY RESOURCES ---
echo Copying resources...
if exist "%SRC_DIR%\resources" (
    xcopy "%SRC_DIR%\resources" "%OUT_DIR%\resources" /s /e /i /y
)

REM --- CREATE JAR ---
echo Creating JAR...
jar cfe "%INPUT_DIR%\%APP_NAME%.jar" %MAIN_CLASS% -C "%OUT_DIR%" .

REM --- COPY LIBS ---
echo Copying dependencies...
for %%i in ("%LIB_DIR%\*.jar") do (
    copy "%%i" "%INPUT_DIR%\" >nul
)

REM --- JPACKAGE ---
echo Packaging...
jpackage ^
  --name "%APP_NAME%" ^
  --input "%INPUT_DIR%" ^
  --main-jar "%APP_NAME%.jar" ^
  --main-class "%MAIN_CLASS%" ^
  --module-path "%JAVAFX_SDK%" ^
  --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing ^
  --java-options "--enable-native-access=javafx.graphics" ^
  --dest "%DIST_DIR%" ^
  --type app-image ^
  --win-console

echo Done! Executable is in %DIST_DIR%
pause