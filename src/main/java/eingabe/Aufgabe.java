package eingabe;

import java.util.List;

/**
 * Stellt die Aufgabe dar, die gelöst werden soll.
 *
 * @param bearbeitungTitel  der Titel der Aufgabe
 * @param rollenBreite      die Breite der zu besetzenden Fläche
 * @param optimierungsTiefe die maximale Anzahl an Aufträgen für den Algorithmus auf einmal
 * @param auftraege         die zu verteilenden Rechtecke
 */
public record Aufgabe(String bearbeitungTitel, int rollenBreite, int optimierungsTiefe, List<Auftrag> auftraege) {
}
