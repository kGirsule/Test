package org.testaufgabe;



import javax.print.DocFlavor;

/**
 * Die abstrakte Klasse Akteur ist die Mutterklasse für alle Spielerklassen und Enemy-Klassen
 * Sie regelt alles was jeder Charakter auf dem Feld haben sollte, also ob der jeweilige Charakter
 * am leben ist, wo er sich befindet, auf welchem Feld er sich befindet, die HP, Beweglichkeit, Damage
 * und spezial-Effekte wie unverwundbarkeit oder lähmung. Außerdem sollte jeder Akteur eine Fähigkeit haben.
 *
 * @author Katharina Girsule
 * @version 31.5.2020
 */
public abstract class Akteur {

    private boolean alive;
    private Field field;
    private Position position;
    int hp = 0;
    int movement = 0;
    int damage = 1;
    private boolean invinciple = false;
    private boolean immobilized = false;
    private boolean avoiding = false;
    private boolean havetoattack = false;
    private Ability ability1;

    /**
     * Mutterklasse sämtlicher Spieler-Klassen und Gegner-Klassen.
     * Außerdem ist jeder Akteru default erstmal am leben.
     * Da ich nicht wusste so sonst einbauen, hier noch eine Exeption,
     * falls die Ability einmal leer sein sollte
     * @param field das aktuell belegte Feld
     * @param position die aktuelle Position
     * @param hp die HP
     * @param movement die Beweglichkeit
     * @param ability1 Fähigkeit 1
     */
    public Akteur(Field field, Position position, int hp, int movement, Ability ability1){
        this.field = field;
        setHp(hp);
        setMovement(movement);
        putPosition(position);
        setAlive(true);
        try{
            setAbility1(ability1);
        }
        catch (IllegalArgumentException ex){
            System.out.println("Please type in the name of your Ability again:");
            Reader r = new Reader();
            String name = r.scans();
            System.out.printf("Please type in the name:");
            int range;
            try{
                range = r.scani();
            }
            catch (IllegalArgumentException exe){
                range = r.scani();
            }
            setAbility1(new Ability(name, range,0));
        }
    }

    /**
     * Setzt Fähigkeit eins, wirft bei einem leeren Parameter IllegalArgumentException
     * @param ability1 die Fähigkeit 1
     */
    private void setAbility1(Ability ability1) {
        if(ability1 == null){
            throw new IllegalArgumentException("Ability 1 can't be empty");
        }
        else {
            this.ability1 = ability1;
        }
    }

    /**
     * Gibt Fähigkeit 1 zurück
     * @return Fähigkeit 1
     */
    public Ability getAbility1() {
        return ability1;
    }

    /**
     * Gibt zuück wie viel Schaden der jeweilige Akteur machen kann
     * @return jeweiligen Schaden welcher gemacht werden kann
     */
    public int getDamage(){
        return this.damage;
    }

    /**
     * Kann einen neuen Wert für die Damage festlegen
     * @param damage gibt an wie hoch der neue Wert für die Damage sein soll
     */
    public void setDamage(int damage){
        this.damage = damage;
    }

    /**
     * Setter um einen Aktuer sterben zu lassen
     * @param alive wenn der Akteur stirbt
     */
    public void setAlive(Boolean alive){
        this.alive = alive;
    }

    /**
     * Gibt zurück ob der jeweilige Akteur noch am leben ist
     * @return boolean ob Akteur noch am leben ist
     */
    public boolean isAlive(){
        return this.alive;
    }

    /**
     * gibt den derzeitgen HP-Status zurück
     * @return aktuelle HP
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * Setzt die aktuelle HP neu
     * @param hp setzt die neue Hp
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Gibt zurück wie weit sich der Akteur bewegen kann
     * @return int wie weit sich der Akteur bewegen kann
     */
    public int getMovement() {
        return this.movement;
    }

    /**
     * Setzt neu wie schnell der Akteur ist
     * @param movement setzt wie weit der Charakter sich von nun an bewegen kann
     */
    public void setMovement(int movement) {
        this.movement = movement;
    }

    /**
     * Wenn der Akteur angegriffen wird setzt er die neue HP (sofer nicht "unverwundbar",
     * wenn der Akteur durch den Angriff stirbt, so wird dessen Status auf "tot" gesetzt
     * @param damage wie viel damage der Akteur nimmt
     */
    public void takesDamage(int damage){
        if(!isInvinciple()){
            setHp(this.getHp()-damage);
        }
        if(this.getHp() <= 0){
            setAlive(false);
            this.giveField().clear(this.givePosition());
        }
    }

    /**
     * Setzt den Akteur auf die jeweils neue Position
     * @param newPosition gibt die neue Position des Akteurs an
     */
    public void putPosition(Position newPosition)
    {
        if(position != null) {
            field.clear(position);
        }
        position = newPosition;
        field.place(this, newPosition);
    }

    /**
     * Gibt das aktuelle Feld zurück
     * @return das aktuelle Feld
     */
    public Field giveField(){
        return this.field;
    }

    /**
     * gibt die aktuelle Position zurück
     * @return aktuelle Position
     */
    public Position givePosition(){
        return this.position;
    }

    /**
     * Setzt den Spezialeffekt "unbesigbar"
     * @param i true oder false für den Spezialeffekt
     */
    public void setInvinciple(boolean i){
        this.invinciple = i;
    }

    /**
     * gibt zurück ob der Spezialeffekt "unbesiegbar" aktiviert ist
     * @return ob der Spezialeffekt "unbesiegbar" aktiviert ist
     */
    public boolean isInvinciple(){
        return invinciple;
    }

    /**
     * gibt zurück ob der Spezialeffekt "gelähmt" aktiviert ist
     * @return ob der Spezialeffekt "gelähmt" aktiviert ist
     */
    public boolean isImmobilized() {
        return immobilized;
    }
    /**
     * Setzt den Spezialeffekt "gelähmt"
     * @param immobilized true oder false für den Spezialeffekt
     */
    public void setImmobilized(boolean immobilized) {
        this.immobilized = immobilized;
    }

    /**
     * Wird von der dritten Fähigkeit des Ritters aktiviert,
     * gibt Spielercharaktere Chance Damage zu vermeiden
     * @return möglichkeit Damage zu vermeiden
     */
    public boolean isAvoiding(){
        return this.avoiding;
    }

    /**
     * Wird vom Ritter aufgerufen und gibt jeden Spielercharakter in Reichweite
     * eine Chance Damage zu vermeiden
     * @param avoiding setzt ob avoiding gerade aktiviert oder deaktiviert ist
     */
    public void setAvoiding(boolean avoiding){
        this.avoiding = avoiding;
    }

    /**
     * Wird von der dritten Fähigkeit des Ritters aktiviert
     * und zwingt Enemys ihn zu attackieren
     * @return zwingt Enemys den Ritter zu attackieren
     */
    public boolean isHavetoattack(){
        return this.havetoattack;
    }

    /**
     * Wird vom Ritter aufgerufen und zwingt Enemys ihn zu attackieren
     * @param havetoattack setzt ob Enemys zwangsweise den Ritter attackieren müssen oder nicht
     */
    public void setHavetoattack(boolean havetoattack){
        this.havetoattack = havetoattack;
    }
}
