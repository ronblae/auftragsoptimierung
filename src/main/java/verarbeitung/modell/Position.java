package verarbeitung.modell;

import eingabe.Auftrag;

import java.util.ArrayList;
import java.util.List;

import static verarbeitung.modell.Ausrichtung.VERTIKAL;

public class Position {

    private final Auftrag auftrag;
    private final Punkt ankerPunkt;
    private final List<Punkt> andockPunkte;
    private final Ausrichtung ausrichtung;

    /**
     * Eine Position stellt die Position eines Auftrags dar anhand seines Ankerpunkts und Ausrichtung.
     * Die daraus resultierenden Ankerpunkte werden ebenso gespeichert.
     *
     * @param auftrag     der betreffende Auftrag
     * @param ankerPunkt  der Ankerpunkt des Auftrags
     * @param ausrichtung die Ausrichtung des Auftrags
     */
    public Position(Auftrag auftrag, Punkt ankerPunkt, Ausrichtung ausrichtung) {
        this.auftrag = auftrag;
        this.ankerPunkt = ankerPunkt;
        this.ausrichtung = ausrichtung;

        andockPunkte = new ArrayList<>();
        andockPunkte.add(new Punkt(ankerPunkt.x(), ankerPunkt.y() + deltaY()));
        andockPunkte.add(new Punkt(ankerPunkt.x() + deltaX(), ankerPunkt.y()));
    }

    /**
     * Liefert in Abhängigkeit seiner Ausrichtung die Länge des Auftrags in x-Richtung.
     *
     * @return die Länge des Auftrags in x-Richtung
     */
    public int deltaX() {
        return ausrichtung == VERTIKAL ? auftrag.breite() : auftrag.hoehe();
    }

    /**
     * Liefert in Abhängigkeit seiner Ausrichtung die Länge des Auftrags in y-Richtung.
     *
     * @return die Länge des Auftrags in y-Richtung
     */
    public int deltaY() {
        return ausrichtung == VERTIKAL ? auftrag.hoehe() : auftrag.breite();
    }

    public Auftrag getAuftrag() {
        return auftrag;
    }

    public Punkt getAnkerPunkt() {
        return ankerPunkt;
    }

    public List<Punkt> getAndockPunkte() {
        return andockPunkte;
    }

    public Ausrichtung getAusrichtung() {
        return ausrichtung;
    }
}
