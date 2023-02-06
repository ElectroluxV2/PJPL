#!/bin/bash
clear

current_month=$(date +%m);
current_year=$(date +%Y);
first_month_of_summer_semester=3;
last_month_of_summer_semester=8;

if [ "$current_month" -gt $first_month_of_summer_semester ] && [ "$current_month" -lt $last_month_of_summer_semester ]
then
   current_semester="$((current_year - 1))/$current_year letni"
elif [ "$current_month" -lt $first_month_of_summer_semester ]
then
   current_semester="$((current_year - 1))/$current_year zimowy"
else
  current_semester="$current_year/$((current_year + 1)) zimowy"
fi

java -jar scrapper-*.jar -st "Informatyka,Gdańsk" -se "$current_semester"
