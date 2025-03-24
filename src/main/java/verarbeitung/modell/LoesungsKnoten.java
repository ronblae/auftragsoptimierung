package verarbeitung.modell;

import eingabe.Auftrag;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class LoesungsKnoten {

    private final Zustand zustand;
    private final int hoehe;

    /**
     * Die Höhe wird anhand des höchsten Andockpunkts festgemacht.
     *
     * @param zustand die derzeitige Verteilung der Aufträge auf der Rolle
     */
    public LoesungsKnoten(Zustand zustand) {
        this.zustand = zustand;

        hoehe = zustand.getOffeneAndockPunkte().stream()
                .mapToInt(Punkt::y)
                .max().orElseThrow();
    }

    /**
     * Diese Methode berechnet eine Priorität für den Lösungsknoten.
     * Je niedriger diese Priorität ausfällt, desto näher ist der Knoten an einer guten Lösung dran.
     *
     * @param rollenBreite die Breite der Rolle
     * @return die errechnete Priorität
     */
    public double berechnePrioritaet(int rollenBreite) {
        double flaeche = hoehe * rollenBreite;
        double benutzteFlaeche = zustand.getPositionen().stream()
                .map(Position::getAuftrag)
                .mapToInt(auftrag -> auftrag.breite() * auftrag.hoehe())
                .sum();

        double benutzteFlaecheInProzent = benutzteFlaeche / flaeche;

        return hoehe * (1 - benutzteFlaecheInProzent);
    }

    /**
     * Findet vom derzeitigen Zustand aus alle möglichen nächsten Schritte.
     *
     * @param rollenBreite die Breite der Rolle
     * @param auftraege    die Liste der Aufträge
     * @return eine Liste von hierauf folgenden Lösungsknoten
     */
    public List<LoesungsKnoten> getMoeglicheLoesungsKnoten(int rollenBreite, List<Auftrag> auftraege) {
        List<Auftrag> offeneAuftraege = zustand.getOffeneAuftraege(auftraege);
        List<Punkt> offeneAndockPunkte = zustand.getOffeneAndockPunkte();

        List<Position> neuePositionen = offeneAuftraege.stream()
                .flatMap(auftrag -> offeneAndockPunkte.stream()
                        .flatMap(andockPunkt -> Stream.of(Ausrichtung.values())
                                .map(ausrichtung -> new Position(auftrag, andockPunkt, ausrichtung))))
                .toList();

        return neuePositionen.stream()
                .map(position -> erstelleLoesungsKnotenMitNeuerPosition(position, rollenBreite))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Erstellt aus dem derzeitigen Zustand den nächsten Lösungsknoten mit der übergebenden Position.
     * Wenn die neue Position einen ungültigen Zustand ergibt, wird null zurückgegeben.
     *
     * @param neuePosition die nächste Position
     * @param rollenBreite die Breite der Rolle
     * @return einen hierauf folgenden Lösungsknoten, wenn möglich, sonst null
     */
    private LoesungsKnoten erstelleLoesungsKnotenMitNeuerPosition(Position neuePosition, int rollenBreite) {
        Zustand zustandsKopie = zustand.getZustandsKopie();
        if (zustandsKopie.addPosition(neuePosition, rollenBreite)) {
            return new LoesungsKnoten(zustandsKopie);
        }
        return null;
    }

    /**
     * Ein Lösungsknoten ist am Ende seines Weges, wenn alle Aufträge verwendet wurden.
     *
     * @param anzahlAuftraege die Anzahl der verwendeten Aufträge
     * @return true, wenn alle Aufträge verwendet wurden, sonst false
     */
    public boolean istLoesung(int anzahlAuftraege) {
        return zustand.istLoesung(anzahlAuftraege);
    }

    public Zustand getZustand() {
        return zustand;
    }

    public int getHoehe() {
        return hoehe;
    }
}
