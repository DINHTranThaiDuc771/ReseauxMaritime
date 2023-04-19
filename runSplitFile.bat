@echo off
IF NOT EXIST "./data/"         ( mkdir "./data" )
call javac -encoding utf8 "@compile.list"

echo Lancement du programme...
call java -cp ./bin Executable.DiviseCSVScanner

echo Fin de l'execution.
goto :eof