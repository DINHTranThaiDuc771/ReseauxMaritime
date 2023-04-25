#From the content of allMoves.csv, extract the date
cat ./testData/allMoves.csv | cut -d ";" -f 5 | cut -d " " -f 1 |tr "/" " " >./tmp/datesdepart
awk ' { t = $1; $1 = $3; $2 = $2 ; $3 = t ; print; } ' ./tmp/datesdepart | tr " " "_" | sort | uniq >./tmp/dates
cat ./tmp/dates | tr "_" " " >./tmp/dates2
awk ' { t = $1; $1 = $3; $2 = $2 ; $3 = t ; print; } ' ./tmp/dates2  >./tmp/datesOrdonnees
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
}' ./tmp/datesOrdonnees | sort > ./tmp/datesSort.csv
awk '{
    t = $1; $1 = $3; $2 = $2 ; $3 = t ; print;
}' ./tmp/datesSort.csv| tr " " "/"> ./tmp/datesSort2.csv
j=0
for i in $(cat ./tmp/datesSort2.csv); do
    echo $i | tr "\n" " "
    echo $j #echo = print("[something]"+"\n")
    ((j = j + 1))
done >./tmp/dates_vs_step

#Rappel:
#The > symbol is used for output redirection -> overwrite
#The >> symbol is used for output redirection -> append
#The | symbol is used for piping
#The (( )) double parentheses are used for arithmetic expansion, which allows you to perform arithmetic operations on numeric values.

