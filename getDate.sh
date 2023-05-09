#!/bin/bash

#From the content of allMoves.csv, extract the date
mkdir -p ./tmp
echo "writing dates_depart_arrival.csv"
cat $1 | cut -d ";" -f 5,6 | awk -F ";" '{print $1;print $2;}' | cut -d " " -f 1 >./tmp/dates_depart_arrival

echo "writing dates.csv"
awk -F "/" '{ t = $1; $1 = $3; $2 = $2 ; $3 = t ; print; } ' ./tmp/dates_depart_arrival | tr " " "_" | sort -t '_' -n -k 1,3 | uniq >./tmp/dates

echo "writing dates2.csv"
cat ./tmp/dates | tr "_" " " >./tmp/dates2

echo "writing datesOrdonnees.csv"
awk ' { t = $1; $1 = $3; $2 = $2 ; $3 = t ; print; } ' ./tmp/dates2  >./tmp/datesOrdonnees

echo "writing datesSort.csv"
awk '{
    isDDMMYYYY = "true";#non empty String is true, there is no boolean type in awk
    if (($1==12 && $2==31 && $3==2013)||$3 > 2013){
        isDDMMYYYY = "";# empty String is false
    }
    if (isDDMMYYYY) {
        date=$3" "$2" "$1;
    } else {
        date=$3" "$1" "$2;
    }
    print date;
}' ./tmp/datesOrdonnees | sort -t ' ' -k 1n -k 2n -k 3n  > ./tmp/datesSort.csv 
#specify that the sort should be done first on field 1, then field 2, and finally on field 3
#The n flag after each field number specifies that each field should be sorted numerically.
echo "writing datesSort2.csv"
awk '{
    t = $1; $1 = $3; $2 = $2 ; $3 = t ; print;
}' ./tmp/datesSort.csv| tr " " "/"> ./tmp/datesSort2.csv

echo "writing dates_vs_step.csv"
j=-1
for i in $(cat ./tmp/datesSort2.csv); do
    echo $i | tr "\n" " "
    echo $j #echo = print("[something]"+"\n")
    ((j = j + 1))
done >./tmp/dates_vs_step

echo "writing files mois"
cat ./tmp/datesSort2.csv | tr "/" " " | awk '{ print $3" "$2}' | sort | uniq -c | awk '{print $3"/"$2" "$1}'> ./tmp/mois
#Rappel:
#The > symbol is used for output redirection -> overwrite
#The >> symbol is used for output redirection -> append
#The | symbol is used for piping
#The (( )) double parentheses are used for arithmetic expansion, which allows you to perform arithmetic operations on numeric values.

