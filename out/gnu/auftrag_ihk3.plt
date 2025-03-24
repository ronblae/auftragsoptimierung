reset
set term png size 900,797
set output 'Auftrag IHK3.png'
set xrange [0:900]
set yrange [0:697]
set size ratio -1
 
set title "\
Auftrag IHK3\n\
Benötigte Länge: 69,70cm\n\
Genutzte Fläche: 85,04%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
420 297 520 397 "Auftrag A" 1
420 0 630 297 "Auftrag B" 2
0 0 420 297 "Auftrag C" 3
0 397 880 697 "Auftrag D" 4
630 0 840 297 "Auftrag E" 5
0 297 100 397 "Auftrag F" 6
EOD
 
$anchor <<EOD
# x y
420 397
520 297
0 697
880 397
630 297
840 0
100 297
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
