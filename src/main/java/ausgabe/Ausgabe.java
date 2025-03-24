package ausgabe;

import eingabe.Aufgabe;
import verarbeitung.modell.LoesungsKnoten;
import verarbeitung.modell.Position;
import verarbeitung.modell.Punkt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ausgabe {

    /**
     * Schreibt die Lösung in eine Textdatei und erstellt ein Skript für gnuplot.
     *
     * @param aufgabe die ursprüngliche Aufgabe
     * @param loesung die zu speichernde Lösung
     */
    public static void erstelleAusgabeDateien(Aufgabe aufgabe, LoesungsKnoten loesung) {
        double laenge = loesung.getHoehe();
        int genutzteFlaeche = loesung.getZustand().getPositionen().stream()
                .map(Position::getAuftrag)
                .mapToInt(auftrag -> auftrag.breite() * auftrag.hoehe())
                .sum();
        int vorhandeneFlaeche = loesung.getHoehe() * aufgabe.rollenBreite();
        double genutzteFlaecheInProzent = (double) genutzteFlaeche * 100 / vorhandeneFlaeche;
        String titel = aufgabe.bearbeitungTitel().toLowerCase().replace(" ", "_");

        String txtText = erstelleTxtText(aufgabe, loesung, laenge, genutzteFlaecheInProzent);
        String pltText = erstellePltText(aufgabe, loesung, laenge, genutzteFlaecheInProzent);

        printTo("out/txt", titel + ".out", txtText);
        printTo("out/gnu", titel + ".plt", pltText);
    }

    /**
     * Erstellt einen Text nach Vorgabe mit der Lösung.
     *
     * @param aufgabe                  die ursprüngliche Aufgabe
     * @param loesung                  die gefundene Lösung
     * @param laenge                   die Länge der besetzten Fläche
     * @param genutzteFlaecheInProzent die genutzte Fläche in Prozent
     * @return der erstellte Text
     */
    private static String erstelleTxtText(Aufgabe aufgabe, LoesungsKnoten loesung, double laenge, double genutzteFlaecheInProzent) {
        StringBuilder txtText = new StringBuilder(String.format("""
                // %s
                Benötigte Länge: %.2fcm
                Genutzte Fläche: %.2f%%
                \s
                Positionierung der Kundenaufträge:
                """, aufgabe.bearbeitungTitel(), laenge / 10, genutzteFlaecheInProzent));

        for (Position position : loesung.getZustand().getPositionen()) {
            int linksUntenX = position.getAnkerPunkt().x();
            int linksUntenY = position.getAnkerPunkt().y();
            int rechtsObenX = position.getAnkerPunkt().x() + position.deltaX();
            int rechtsObenY = position.getAnkerPunkt().y() + position.deltaY();

            txtText.append("\t")
                    .append(linksUntenX).append(" ")
                    .append(linksUntenY).append(" ")
                    .append(rechtsObenX).append(" ")
                    .append(rechtsObenY).append(" - ")
                    .append(position.getAuftrag().id()).append(" - ")
                    .append(position.getAuftrag().beschreibung()).append("\n");
        }

        txtText.append("\n\nVerbleibende Andockpunkte:\n");

        for (Punkt punkt : loesung.getZustand().getOffeneAndockPunkte()) {
            txtText.append("\t")
                    .append(punkt.x()).append(" ")
                    .append(punkt.y()).append("\n");
        }

        return txtText.toString();
    }

    /**
     * Erstellt ein Skript für gnuplot nach Vorgabe mit der Lösung.
     *
     * @param aufgabe                  die ursprüngliche Aufgabe
     * @param loesung                  die gefundene Lösung
     * @param laenge                   die Länge der besetzten Fläche
     * @param genutzteFlaecheInProzent die genutzte Fläche in Prozent
     * @return das erstellte Skript
     */
    private static String erstellePltText(Aufgabe aufgabe, LoesungsKnoten loesung, double laenge, double genutzteFlaecheInProzent) {
        StringBuilder pltText = new StringBuilder(String.format("""
                        reset
                        set term png size %d,%d
                        set output '%s'
                        set xrange [0:%d]
                        set yrange [0:%d]
                        set size ratio -1
                        \s
                        set title "\\
                        %s\\n\\
                        Benötigte Länge: %.2fcm\\n\\
                        Genutzte Fläche: %.2f%%"
                        \s
                        set style fill transparent solid 0.5 border
                        set key noautotitle
                        \s
                        $data <<EOD
                        # x_LU y_LU x_RO y_RO Auftragsbeschreibung ID
                        """, aufgabe.rollenBreite(), (int) laenge + 100, aufgabe.bearbeitungTitel() + ".png", aufgabe.rollenBreite(),
                loesung.getHoehe(), aufgabe.bearbeitungTitel(), laenge / 10, genutzteFlaecheInProzent));

        for (Position position : loesung.getZustand().getPositionen()) {
            int linksUntenX = position.getAnkerPunkt().x();
            int linksUntenY = position.getAnkerPunkt().y();
            int rechtsObenX = position.getAnkerPunkt().x() + position.deltaX();
            int rechtsObenY = position.getAnkerPunkt().y() + position.deltaY();

            pltText.append(linksUntenX).append(" ")
                    .append(linksUntenY).append(" ")
                    .append(rechtsObenX).append(" ")
                    .append(rechtsObenY).append(" \"")
                    .append(position.getAuftrag().beschreibung()).append("\" ")
                    .append(position.getAuftrag().id()).append("\n");
        }

        pltText.append("""
                EOD
                \s
                $anchor <<EOD
                # x y
                """);

        for (Punkt punkt : loesung.getZustand().getOffeneAndockPunkte()) {
            pltText.append(punkt.x()).append(" ")
                    .append(punkt.y()).append("\n");
        }

        pltText.append("""
                EOD
                \s
                plot \\
                '$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):(($3-$1)/2):(($4-$2)/2):6 \\
                               with boxxy linecolor var, \\
                '$data' using (($3-$1)/2+$1):(($4-$2)/2+$2):5 \\
                               with labels font "arial,9", \\
                '$anchor' using 1:2 with circles lc rgb "red"
                """);

        return pltText.toString();
    }

    /**
     * Schreibt den übergebenen Text in den angegebenen Pfad.
     *
     * @param pfad  der zu beschreibende Pfad
     * @param titel der Titel der Datei
     * @param text  der zu speichernde Text
     */
    private static void printTo(String pfad, String titel, String text) {
        Path dateiPfad = Paths.get(pfad, titel);
        try {
            Files.createDirectories(dateiPfad.getParent());
            Files.writeString(dateiPfad, text);
            System.out.println("Datei erstellt: " + dateiPfad.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }
}
