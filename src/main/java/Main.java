import ausgabe.Ausgabe;
import eingabe.Aufgabe;
import eingabe.Auftrag;
import eingabe.Eingabe;
import verarbeitung.Optimierung;
import verarbeitung.modell.LoesungsKnoten;
import verarbeitung.modell.Position;
import verarbeitung.modell.Punkt;
import verarbeitung.modell.Zustand;

import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Das Programm liest eine Textdatei mit Daten zu rechteckigen Aufträgen ein und optimiert deren Verteilung auf
     * einer Fläche mit fester Breite und variabler Länge so, dass die Länge und die dadurch nicht verbrauchte Fläche
     * minimal ist.
     * <p>
     * Sollte die Anzahl der Aufträge die angegebene Optimierungstiefe überschreiten, werden die Aufträge in Paketen
     * der Größe der Optimierungstiefe aufgeteilt und dem Algorithmus getrennt übergeben.
     * <p>
     * Die daraus resultierenden Ergebnisse werden zum Schluss wieder zusammengefügt, bevor jeweils eine Textdatei und
     * ein Gnuplot-Skript im Ordner /io/out erstellt wird.
     *
     * @param args erwartet einen absoluten oder relativen Pfad zur Eingabedatei
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Ungültige Parameter. Bitte verwenden Sie den Befehl java -jar Auftragsoptimierung.jar <Dateipfad>");
        }

        long startZeit = System.currentTimeMillis();

        String dateiPfad = args[0];
        List<LoesungsKnoten> loesungen = new ArrayList<>();
        Aufgabe aufgabe = Eingabe.leseDaten(dateiPfad);

        if (!istAufgabeGueltig(aufgabe)) {
            System.out.println("Ein oder mehr Aufträge passen nicht auf die Rolle!");
            return;
        }

        System.out.println("Starte Optimierung mit Optimierungstiefe: " + aufgabe.optimierungsTiefe());

        while (!aufgabe.auftraege().isEmpty()) {
            List<Auftrag> auftraege = new ArrayList<>();

            for (int i = 0; i < aufgabe.optimierungsTiefe(); i++) {
                if (!aufgabe.auftraege().isEmpty()) {
                    auftraege.add(aufgabe.auftraege().remove(0));
                }
            }

            LoesungsKnoten loesung = Optimierung.optimiere(auftraege, aufgabe.rollenBreite());
            System.out.println("Beste Lösungshöhe: " + loesung.getHoehe() + "mm\n");
            loesungen.add(loesung);
        }

        LoesungsKnoten loesung = vereineLoesungen(loesungen);

        Ausgabe.erstelleAusgabeDateien(aufgabe, loesung);

        long endZeit = System.currentTimeMillis();
        double endZeitInSekunden = (endZeit - startZeit) / 1000.0;
        int endZeitInMinuten = 0;
        while (endZeitInSekunden >= 60) {
            endZeitInMinuten++;
            endZeitInSekunden -= 60;
        }
            System.out.printf("\nAusführungszeit: %d Minuten und %.3f Sekunden%n%n", endZeitInMinuten, endZeitInSekunden);
    }

    /**
     * Überprüft, ob alle Aufträge auf die Rolle passen.
     *
     * @param aufgabe die betrachtete Aufgabe
     * @return true, wenn alle Aufträge auf die Rolle passen, sonst false
     */
    private static boolean istAufgabeGueltig(Aufgabe aufgabe) {
        return aufgabe.auftraege().stream()
                .noneMatch(auftrag -> auftrag.breite() > aufgabe.rollenBreite() && auftrag.hoehe() > aufgabe.rollenBreite());
    }

    /**
     * Da die Lösungen alle von einem Startpunkt (0, 0) ausgehen, müssen sie für das Zusammenfügen jeweils auf die
     * maximale Höhe ihres Vorgängers gesetzt werden.
     *
     * @param loesungen eine Liste von optimierten Lösungen
     * @return eine kombinierte Lösung
     */
    private static LoesungsKnoten vereineLoesungen(List<LoesungsKnoten> loesungen) {
        List<Position> kombiniertePositionen = new ArrayList<>();
        int aktuelleHoehe = 0;

        for (LoesungsKnoten loesung : loesungen) {
            for (Position position : loesung.getZustand().getPositionen()) {
                Position verschobenePosition = new Position(
                        position.getAuftrag(),
                        new Punkt(
                                position.getAnkerPunkt().x(),
                                position.getAnkerPunkt().y() + aktuelleHoehe
                        ),
                        position.getAusrichtung()
                );
                kombiniertePositionen.add(verschobenePosition);
            }

            aktuelleHoehe += loesung.getHoehe();
        }

        Zustand kombinierterZustand = new Zustand(kombiniertePositionen);
        return new LoesungsKnoten(kombinierterZustand);
    }
}
