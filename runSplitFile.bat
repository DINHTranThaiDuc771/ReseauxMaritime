@echo off
call javac -encoding utf8 "@compile.list"

if "%~1"=="" (
    echo Missing argument. Usage: %0 [file path]
    echo Divide ./testData/testMoves.csv by default...
    if exist "./data/" (
        echo Delete...
        cd ./data
        del "*.csv"
        cd ..
    ) else (
        mkdir "./data"
    )
    call java -cp ./bin Executable.DiviseCSVScanner "./testData/testMoves.csv"
    goto :eof
)
if not exist "%~1" (
    echo Error: File not found: %1
    goto :eof
)



if exist "./data/" (
    echo Delete...
    cd ./data
    del "*.csv"
    cd ..
) else (
    mkdir "./data"
)


echo Lancement du programme...
call java -cp ./bin Executable.DiviseCSVScanner "%~1"

echo Fin de l'execution.
goto :eof
