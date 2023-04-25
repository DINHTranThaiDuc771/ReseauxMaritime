#!/bin/bash

javac -encoding utf8 "@compile.list"

if [ -z "$1" ]; then
    echo "Missing argument. Usage: $0 [file path]"
    echo "Read ./testData/testMoves.csv by default..."
    ./getDate.sh ./testData/testMoves.csv
    if [ -d "./dataHistorique/" ]; then
        echo "Reconstruct..."
        cd "./dataHistorique"
        rm -f *.txt
        cd ..
    else
        mkdir "./dataHistorique"
    fi

    java -cp ./bin Model.Model "./testData/testMoves.csv"
    exit 0
fi

if [ ! -f "$1" ]; then
    echo "Error: File not found: $1"
    exit 1
fi
./getDate.sh "$1"
if [ -d "./dataHistorique/" ]; then
    echo "Delete..."
    cd "./dataHistorique"
    rm -f *.txt
    cd ..
else
    mkdir "./dataHistorique"
fi
java -cp ./bin Model.Model "$1"
echo "Fin de l'execution."
exit 0
