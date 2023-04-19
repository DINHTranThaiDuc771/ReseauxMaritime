@echo off
call javac -encoding utf8 "@compile.list"

if "%~1"=="" (
    echo Missing argument. Usage: %0 [file path]
    echo Read ./testData/testMoves.csv by default...
    call java -cp ./bin Model.Model "./testData/testMoves.csv"
    goto :eof
)

if not exist "%~1" (
    echo Error: File not found: %1
    goto :eof
)
call java -cp ./bin Model.Model "%~1"


echo Fin de l'execution.
goto :eof
