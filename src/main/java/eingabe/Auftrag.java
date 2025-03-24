package eingabe;

/**
 * Stellt einen einzelnen Auftrag dar.
 *
 * @param breite       die Breite des Rechtecks
 * @param hoehe        die Höhe des Rechtecks
 * @param id           die ID des Auftrags
 * @param beschreibung eine Beschreibung des Auftrags
 */
public record Auftrag(int breite, int hoehe, int id, String beschreibung) {
}
