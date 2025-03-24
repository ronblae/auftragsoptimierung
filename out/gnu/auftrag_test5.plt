reset
set term png size 870,1188
set output 'Auftrag Test5.png'
set xrange [0:870]
set yrange [0:1088]
set size ratio -1
 
set title "\
Auftrag Test5\n\
Benötigte Länge: 108,80cm\n\
Genutzte Fläche: 74,04%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
348 0 588 198 "Auftrag A" 1
0 0 348 120 "Auftrag B" 2
0 308 850 848 "Auftrag C" 3
0 198 450 308 "Auftrag D" 4
0 848 620 948 "Auftrag E" 5
620 848 840 958 "Auftrag F" 6
0 958 130 1088 "Auftrag G" 7
EOD
 
$anchor <<EOD
# x y
348 198
588 0
0 120
850 308
450 198
0 948
620 958
840 848
0 1088
130 958
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
