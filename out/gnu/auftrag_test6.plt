reset
set term png size 870,1178
set output 'Auftrag Test6.png'
set xrange [0:870]
set yrange [0:1078]
set size ratio -1
 
set title "\
Auftrag Test6\n\
Benötigte Länge: 107,80cm\n\
Genutzte Fläche: 74,73%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
0 0 240 198 "Auftrag A" 1
240 0 588 120 "Auftrag B" 2
0 198 850 738 "Auftrag C" 3
220 738 670 848 "Auftrag D" 4
0 848 620 948 "Auftrag E" 5
0 738 220 848 "Auftrag F" 6
0 948 130 1078 "Auftrag G" 7
EOD
 
$anchor <<EOD
# x y
240 120
588 0
850 198
220 848
670 738
620 848
0 1078
130 948
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
