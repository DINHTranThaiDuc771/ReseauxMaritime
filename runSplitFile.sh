#!/bin/bash

if [ ! -d "./data/" ]; then
    mkdir "./data"
fi

javac -encoding utf8 "@compile.list"

echo "Lancement du programme..."
java -cp ./bin Executable.DiviseCSVScanner

echo "Fin de l'execution."
