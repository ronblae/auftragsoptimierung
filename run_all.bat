@echo off
setlocal enabledelayedexpansion

set "inputFolder=input"
set "jarFile=Auftragsoptimierung.jar"

if not exist "%inputFolder%" (
    echo Der Ordner "%inputFolder%" existiert nicht.
    exit /b 1
)

if not exist "%jarFile%" (
    echo Die Datei "%jarFile%" existiert nicht.
    exit /b 1
)

for %%f in ("%inputFolder%\*") do (
    echo Verarbeite Datei: %%f
    java -jar %jarFile% %%f
)

echo Alle Dateien wurden verarbeitet.
pause
