reset
set term png size 900,980
set output 'Auftrag IHK2.png'
set xrange [0:900]
set yrange [0:880]
set size ratio -1
 
set title "\
Auftrag IHK2\n\
Benötigte Länge: 88,00cm\n\
Genutzte Fläche: 92,98%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
297 297 397 397 "Auftrag A" 1
0 420 297 630 "Auftrag B" 2
0 0 297 420 "Auftrag C" 3
594 0 894 880 "Auftrag D" 4
0 630 297 840 "Auftrag E" 5
297 397 594 817 "Auftrag H" 8
297 0 594 297 "Auftrag J" 10
EOD
 
$anchor <<EOD
# x y
397 297
297 420
594 880
894 0
0 840
297 630
297 817
594 397
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
