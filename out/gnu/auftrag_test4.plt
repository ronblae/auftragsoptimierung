reset
set term png size 870,1408
set output 'Auftrag Test4.png'
set xrange [0:870]
set yrange [0:1308]
set size ratio -1
 
set title "\
Auftrag Test4\n\
Benötigte Länge: 130,80cm\n\
Genutzte Fläche: 61,59%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
0 0 240 198 "Auftrag A" 1
0 198 348 318 "Auftrag B" 2
0 318 850 858 "Auftrag C" 3
0 858 450 968 "Auftrag D" 4
0 968 620 1068 "Auftrag E" 5
0 1068 220 1178 "Auftrag F" 6
0 1178 130 1308 "Auftrag G" 7
EOD
 
$anchor <<EOD
# x y
240 0
348 198
850 318
450 858
620 968
220 1068
0 1308
130 1178
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
