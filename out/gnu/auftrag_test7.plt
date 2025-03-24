reset
set term png size 870,1080
set output 'Auftrag Test7.png'
set xrange [0:870]
set yrange [0:980]
set size ratio -1
 
set title "\
Auftrag Test7\n\
Benötigte Länge: 98,00cm\n\
Genutzte Fläche: 82,21%"
 
set style fill transparent solid 0.5 border
set key noautotitle
 
$data <<EOD
# x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
450 540 690 738 "Auftrag A" 1
0 650 348 770 "Auftrag B" 2
0 0 850 540 "Auftrag C" 3
0 540 450 650 "Auftrag D" 4
130 770 750 870 "Auftrag E" 5
130 870 350 980 "Auftrag F" 6
0 770 130 900 "Auftrag G" 7
EOD
 
$anchor <<EOD
# x y
450 738
690 540
348 650
850 0
750 770
130 980
350 870
0 900
EOD
 
plot \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \
               with boxxy linecolor var, \
'$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \
               with labels font "arial,9", \
'$anchor' using 1:2 with circles lc rgb "red"
