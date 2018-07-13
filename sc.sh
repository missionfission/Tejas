#!/bin/sh
echo "enter the absolute path of the folder"
read path1

echo "enter the number of kernels"
read kernels

echo "enter the number of threads"
read threads

I=1


terminal=`tty`

while [ $I -lt $threads ]
do
	J=0
	while [ $J -lt $kernels ]
	do
		src=$path1"/"$I"_"$J".txt"
		dst=$path1"/0_"$J".txt"
		exec<$src
		while read line
		do
			echo $line>>$dst
		done
		#echo "\n"
		J=`expr $J + 1`
	done
	I=`expr $I + 1`
done

exec<$terminal

exit
