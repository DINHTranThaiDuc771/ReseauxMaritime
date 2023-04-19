#!/bin/bash

# Compile Java code
javac -encoding utf8 @compile.list

if [ -z "$1" ]; then
    echo "Missing argument. Usage: $0 [file path]"
    echo "Divide ./testData/testMoves.csv by default..."
    if [ -d "./data" ]; then
        echo "Delete..."
        cd ./data
        rm -f *.csv
        cd ..
    else
        mkdir "./data"
    fi
    java -cp ./bin Executable.DiviseCSVScanner "./testData/testMoves.csv"
    exit 0
fi

if [ ! -f "$1" ]; then
    echo "Error: File not found: $1"
    exit 1
fi

if [ -d "./data" ]; then
    echo "Delete..."
    cd ./data
    rm -f *.csv
    cd ..
else
    mkdir "./data"
fi

# Run Java program with the given file path argument
echo "Lancement du programme..."
java -cp ./bin Executable.DiviseCSVScanner "$1"

echo "Fin de l'execution."
exit 0
