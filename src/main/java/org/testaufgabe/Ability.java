package org.testaufgabe;



/**
 * Die Klasse Ability erzugt für Aktuere Fähigkeiten.
 * Sie regelt den Namen, die REichweite, den aktuellen und den Max-Countdown
 *
 * @author Katharina Girsule
 * @version 31.5.2020
 */
public class Ability {
    private String name;
    private int range;
    private final int MAXCOOLDOWN;
    private int currentcooldown;

    /**
     * Erzeugt eine Fähigkeit mit den Angegebenen Namen, Reichweite und Max-Cooldown.
     * Jede Fähigkeit ist zu beginn verfügbar
     * @param name gibt den Namen der Fähigkeit an
     * @param range gibt die Reichweite der Fähigkeit an
     * @param cooldown gibt die Abklingzeit der Fähigkeit an
     */
    public Ability(String name, int range, int cooldown){
        setName(name);
        setRange(range);
        this.MAXCOOLDOWN = cooldown;
        setCurrentcooldown(0);
    }

    /**
     * Liefert die Reichweite als String zurück
     * @return String "self" ist die Reichweite der Fähigkeit 0 und eine "zahl" wenn sie größer ist
     */
    public String getRangeString() {
        if(this.range == 0){
            return "self";
        }
        else return ""+ this.range + "  ";
    }

    /**
     * Liefert die Reichweite als Integer zurück
     * @return range
     */
    public int getRange(){
        return this.range;
    }

    /**
     * Setter für die Reichseite
     * @param range gibt die neue Reichweite an
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Gibt als String zurück ob eine Fähigkeit einsetzbar ist oder nicht
     * @return String "avaible" wenn die Fähigkeit einsetzbar ist oder "unavaible" wenn nicht
     */
    public String getCurrentCooldown() {
        if(getCurrentcooldown() == 0){
            return "avaible  ";
        }
        else return "unavaible";
    }

    /**
     * Gibt den aktuellen Stand der abklingzeit zurück
     * @return currentcooldown
     */
    public int getCurrentcooldown(){
        return this.currentcooldown;
    }

    /**
     * Setzt die aktuelle Abklingzeit um eines herunter,
     * sofern die Abklingzeit nicht ohnehin schon bei 0 ist
     * Kann jede Runde an jedem Akteur aufgerufen werden.
     */
    public void setCurrentcooldown(){
        if(this.currentcooldown > 0){
            this.currentcooldown--;
        }
    }

    /**
     * Setzt die aktuelle Abklingzeit auf den parameter a,
     * kann benutzt werden um die Abklingzeit nach der Nutzung
     * der Fähigkeit wieder zu erhöhen.
     * @param a setzt die Abklingzeit
     */
    public void setCurrentcooldown(int a){
        this.currentcooldown = a;
    }

    /**
     * Gibt den Namen der Fähigkeit zurücl
     * @return name der Fähigkeit
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setzt den Namen der Fähigkeit
     * @param name der Fähigkeit
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die als Max Festgelegte Abklingzeit zurück
     * @return Max Festgelegte Abklingzeit zurück
     */
    public int getMAXCOOLDOWN(){
        return this.MAXCOOLDOWN;
    }

}

