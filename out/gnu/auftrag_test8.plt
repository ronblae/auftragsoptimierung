reset
set term png size 870,1080
set output 'Auftrag Test8.png'
set xrange [0:870]
set yrange [0:980]
set size ratio -1
 
set title "\
Auftrag Test8\n\
Benötigte Länge: 98,00cm\n\
Genutzte Fläche: 82,21%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
0 620 240 818 "Auftrag A" 1
100 0 220 348 "Auftrag B" 2
330 0 870 850 "Auftrag C" 3
220 0 330 450 "Auftrag D" 4
0 0 100 620 "Auftrag E" 5
0 850 220 960 "Auftrag F" 6
220 850 350 980 "Auftrag G" 7
EOD
 
$anchor <<EOD
# x y
0 818
240 620
100 348
330 850
870 0
220 450
0 960
220 980
350 850
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
