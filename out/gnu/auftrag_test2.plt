reset
set term png size 300,500
set output 'Auftrag Test2.png'
set xrange [0:300]
set yrange [0:400]
set size ratio -1
 
set title "\
Auftrag Test2\n\
Benötigte Länge: 40,00cm\n\
Genutzte Fläche: 100,00%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
200 100 300 200 "Auftrag A" 1
100 100 200 200 "Auftrag B" 2
200 0 300 100 "Auftrag C" 3
0 100 100 200 "Auftrag D" 4
100 0 200 100 "Auftrag E" 5
0 0 100 100 "Auftrag F" 6
200 300 300 400 "Auftrag G" 7
100 300 200 400 "Auftrag H" 8
200 200 300 300 "Auftrag I" 9
0 300 100 400 "Auftrag J" 10
100 200 200 300 "Auftrag K" 11
0 200 100 300 "Auftrag L" 12
EOD
 
$anchor <<EOD
# x y
300 100
300 0
200 400
300 300
100 400
300 200
0 400
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
