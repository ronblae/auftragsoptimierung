package eingabe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Eingabe {
    /**
     * Liest die Daten aus der angegebenen Datei und speichert die Daten in einem Aufgabenrecord.
     *
     * @param dateiPfad der Name der einzulesenden Datei
     * @return ein Aufgabenrecord mit den ausgelesenen Daten.
     */
    public static Aufgabe leseDaten(String dateiPfad) {
        String bearbeitungTitel;
        int rollenBreite;
        int optimierungsTiefe;
        List<Auftrag> auftraege = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(dateiPfad))) {

            String line;

            line = bufferedReader.readLine();
            bearbeitungTitel = line.substring(3);

            line = bufferedReader.readLine();
            String[] lineParts = line.split(" ");

            rollenBreite = Integer.parseInt(lineParts[0]);
            optimierungsTiefe = Integer.parseInt(lineParts[1]);

            while ((line = bufferedReader.readLine()) != null) {
                lineParts = line.split(", ");
                int breite = Integer.parseInt(lineParts[0]);
                int hoehe = Integer.parseInt(lineParts[1]);
                int id = Integer.parseInt(lineParts[2]);
                String beschreibung = lineParts[3];

                auftraege.add(new Auftrag(breite, hoehe, id, beschreibung));
            }

            return new Aufgabe(bearbeitungTitel, rollenBreite, optimierungsTiefe, auftraege);

        } catch (IOException e) {
            System.err.println("Die Eingabedatei hat nicht das richtige Format.");
            throw new RuntimeException();
        }
    }
}
