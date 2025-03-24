package verarbeitung.modell;

import eingabe.Auftrag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Zustand {

    private final List<Position> positionen;

    public Zustand(List<Position> positionen) {
        this.positionen = positionen;
    }

    /**
     * Fügt dem Zustand eine neue Position hinzu. Es wird überprüft, ob dies einen gültigen Zustand ergibt, bevor
     * sie eingefügt wird.
     *
     * @param neuePosition die einzufügende Position
     * @param rollenBreite die Breite der Rolle für die Überprüfung
     * @return true, wenn die Position angenommen wurde, sonst false
     */
    public boolean addPosition(Position neuePosition, int rollenBreite) {
        if (istGueltig(neuePosition, rollenBreite)) {
            positionen.add(neuePosition);
            positionen.sort(Comparator.comparingInt(position -> position.getAuftrag().id()));
            return true;
        }
        return false;
    }

    /**
     * Überprüft, ob eine neue Position innerhalb der Rolle liegt und sich mit keiner anderen Position
     * überschneidet.
     *
     * @param neuePosition die neu einzufügende Position
     * @param rollenBreite die Breite der Rolle
     * @return true, wenn beide Bedingungen zutreffen, sonst false
     */
    private boolean istGueltig(Position neuePosition, int rollenBreite) {
        boolean innerhalbRollenBreite = neuePosition.getAndockPunkte().stream()
                .mapToInt(Punkt::x)
                .noneMatch(xWert -> xWert > rollenBreite);

        boolean keineUeberschneidungen = positionen.stream()
                .noneMatch(position -> ueberschneidenSich(position, neuePosition));

        return innerhalbRollenBreite && keineUeberschneidungen;
    }

    /**
     * Überprüft, ob sich zwei Positionen überschneiden, indem ihre Grenzen links, rechts, oben und unten
     * miteinander verglichen werden.
     *
     * @param p1 die erste Position
     * @param p2 die zweite Position
     * @return true, wenn sich die Positionen überschneiden, sonst false
     */
    private boolean ueberschneidenSich(Position p1, Position p2) {
        int links1 = p1.getAnkerPunkt().x();
        int rechts1 = links1 + p1.deltaX();
        int unten1 = p1.getAnkerPunkt().y();
        int oben1 = unten1 + p1.deltaY();

        int links2 = p2.getAnkerPunkt().x();
        int rechts2 = links2 + p2.deltaX();
        int unten2 = p2.getAnkerPunkt().y();
        int oben2 = unten2 + p2.deltaY();

        boolean xUeberlappt = rechts1 > links2 && rechts2 > links1;
        boolean yUeberlappt = oben1 > unten2 && oben2 > unten1;

        return xUeberlappt && yUeberlappt;
    }

    /**
     * Der Zustand ist eine Lösung, wenn alle Aufträge verbraucht wurden.
     *
     * @param anzahlAuftraege die Gesamtzahl der Aufträge
     * @return true, wenn es so viele Positionen wie Aufträge gibt, sonst false
     */
    public boolean istLoesung(int anzahlAuftraege) {
        return positionen.size() == anzahlAuftraege;
    }

    /**
     * Erstellt eine Kopie des Zustandes, zur weiteren Verwendung in einem folgenden Lösungsknoten.
     *
     * @return die Kopie des Zustandes
     */
    public Zustand getZustandsKopie() {
        return new Zustand(new ArrayList<>(positionen));
    }

    /**
     * Filtert die Liste der Aufträge nach Aufträgen, die noch nicht verwendet wurden.
     *
     * @param auftraege die Liste der Aufträge
     * @return eine Liste mit nicht verwendeten Aufträgen
     */
    public List<Auftrag> getOffeneAuftraege(List<Auftrag> auftraege) {
        List<Auftrag> benutzteAuftraege = positionen.stream()
                .map(Position::getAuftrag)
                .toList();

        return auftraege.stream()
                .filter(auftrag -> !benutzteAuftraege.contains(auftrag))
                .toList();
    }

    /**
     * Sucht alle noch nicht verwendeten Andockpunkte, indem sie mit den Ankerpunkten verglichen werden.
     *
     * @return alle noch nicht verwendeten Andockpunkte des Zustands.
     */
    public List<Punkt> getOffeneAndockPunkte() {
        List<Punkt> alleAndockPunkte = positionen.stream()
                .flatMap(position -> position.getAndockPunkte().stream())
                .distinct()
                .toList();

        List<Punkt> alleAnkerpunkte = positionen.stream()
                .map(Position::getAnkerPunkt)
                .toList();

        return alleAndockPunkte.stream()
                .filter(andockPunkt -> !alleAnkerpunkte.contains(andockPunkt))
                .toList();
    }

    public List<Position> getPositionen() {
        return positionen;
    }
}
