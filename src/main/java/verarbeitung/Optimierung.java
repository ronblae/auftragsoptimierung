package verarbeitung;

import eingabe.Auftrag;
import verarbeitung.modell.*;

import java.util.*;

import static verarbeitung.modell.Ausrichtung.HORIZONTAL;
import static verarbeitung.modell.Ausrichtung.VERTIKAL;

public class Optimierung {

    /**
     * Findet eine optimale Lösung für das Verteilen von Rechtecken auf der Rolle, sodass die
     * Rollenhöhe minimal wird.
     * <p>
     * Der Algorithmus arbeitet mit einer PriorityQueue, die immer die vielversprechendste Lösung nach vorne packt.
     * Der günstigste Lösungspfad wird hierbei durch eine Kombination der bereits erreichten Höhe und dem Anteil
     * der verbrauchten Fläche ermittelt.
     * Dieser Pfad generiert weitere mögliche Pfade und fügt sie der Queue hinzu.
     * <p>
     * Wenn eine Lösung gefunden wurde, werden alle Lösungsknoten, die diese Höhe bereits erreicht haben, aus der
     * Queue eliminiert. Jede neu gefundene Lösung wird mit der alten abgeglichen, um die beste Lösung zu finden.
     * <p>
     * Wenn die Queue leer ist, wurde die Lösung gefunden.
     *
     * @param auftraege    die rechteckigen Aufträge
     * @param rollenBreite die Breite der Rolle
     * @return ein Lösungsknoten, der die optimale Verteilung der Aufträge enthält
     */
    public static LoesungsKnoten optimiere(List<Auftrag> auftraege, int rollenBreite) {
        Queue<LoesungsKnoten> knoten = new PriorityQueue<>(Comparator.comparingDouble(loesungsKnoten -> loesungsKnoten.berechnePrioritaet(rollenBreite)));

        initialisiere(auftraege, knoten, rollenBreite);

        LoesungsKnoten loesung = null;
        int loesungsHoehe = Integer.MAX_VALUE;

        while (!knoten.isEmpty()) {
            LoesungsKnoten aktuellerKnoten = knoten.poll();

            if (aktuellerKnoten.istLoesung(auftraege.size())) {
                loesung = aktuellerKnoten;
                loesungsHoehe = aktuellerKnoten.getHoehe();
                knoten.removeIf(loesungsKnoten -> loesungsKnoten.getHoehe() >= aktuellerKnoten.getHoehe());
                System.out.println("Neue beste Lösung gefunden: " + loesungsHoehe + "mm");
            }

            List<LoesungsKnoten> moeglicheKnoten = aktuellerKnoten.getMoeglicheLoesungsKnoten(rollenBreite, auftraege);

            for (LoesungsKnoten loesungsKnoten : moeglicheKnoten) {
                if(loesungsKnoten.getHoehe() < loesungsHoehe) {
                    knoten.add(loesungsKnoten);
                }
            }
        }

        return loesung;
    }

    /**
     * Befüllt die PriorityQueue mit allen Aufträgen in jeder Lage am Startpunkt (0, 0)
     *
     * @param auftraege    die zu verteilenden Aufträge
     * @param knoten       die PriorityQueue mit den Lösungsknoten
     * @param rollenBreite die Breite der Rolle
     */
    private static void initialisiere(List<Auftrag> auftraege, Queue<LoesungsKnoten> knoten, int rollenBreite) {
        Punkt startPunkt = new Punkt(0, 0);
        for (Auftrag auftrag : auftraege) {
            erstelleLoesungsKnotenNachAusrichtung(auftrag, startPunkt, HORIZONTAL, knoten, rollenBreite);
            erstelleLoesungsKnotenNachAusrichtung(auftrag, startPunkt, VERTIKAL, knoten, rollenBreite);
        }
    }

    private static void erstelleLoesungsKnotenNachAusrichtung(Auftrag auftrag, Punkt startPunkt, Ausrichtung ausrichtung, Queue<LoesungsKnoten> knoten, int rollenBreite) {
        Zustand zustand = new Zustand(new ArrayList<>());
        Position position = new Position(auftrag, startPunkt, ausrichtung);

        if (zustand.addPosition(position, rollenBreite)) {
            knoten.add(new LoesungsKnoten(zustand));
        }
    }
}
