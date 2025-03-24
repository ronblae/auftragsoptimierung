reset
set term png size 870,950
set output 'Auftrag Test10.png'
set xrange [0:870]
set yrange [0:850]
set size ratio -1
 
set title "\
Auftrag Test10\n\
Benötigte Länge: 85,00cm\n\
Genutzte Fläche: 94,78%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
100 458 298 698 "Auftrag A" 1
100 110 220 458 "Auftrag B" 2
330 0 870 850 "Auftrag C" 3
220 0 330 450 "Auftrag D" 4
0 110 100 730 "Auftrag E" 5
0 0 220 110 "Auftrag F" 6
100 698 230 828 "Auftrag G" 7
EOD
 
$anchor <<EOD
# x y
298 458
220 110
330 850
870 0
220 450
0 730
100 828
230 698
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
