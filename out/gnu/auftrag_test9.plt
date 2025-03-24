reset
set term png size 870,1080
set output 'Auftrag Test9.png'
set xrange [0:870]
set yrange [0:980]
set size ratio -1
 
set title "\
Auftrag Test9\n\
Benötigte Länge: 98,00cm\n\
Genutzte Fläche: 82,21%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
0 560 198 800 "Auftrag A" 1
110 0 230 348 "Auftrag B" 2
230 0 770 850 "Auftrag C" 3
0 0 110 450 "Auftrag D" 4
770 0 870 620 "Auftrag E" 5
0 450 220 560 "Auftrag F" 6
0 850 130 980 "Auftrag G" 7
EOD
 
$anchor <<EOD
# x y
0 800
198 560
110 348
230 850
770 620
870 0
220 450
0 980
130 850
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
