package org.testaufgabe;



import java.util.ArrayList;

/**
 * Die abstrakte Klasse Spielerklassen ist eine Subklasse von Akteur.
 * Sie behandelt Besonderheiten der Spieler welche die Gegner nicht in
 * dieser Form haben/brauchen.
 *
 * @author Katharina Girsule
 * @version 31.5.2020
 */
public abstract class Spielerklassen extends Akteur {

    private Reader scan;
    private Ability ability2;
    private Ability ability3;

    /**
     * Subklasse von Akteur, gibt die entsprechenden Parameter an die Superklasse weiter
     * Außerdem implementiert Spielerklasse Ability2 und 3, welche nicht alle Spieler
     * zusätzlich zu Ability1 haben sollten.
     * @param field das aktuell belegte Feld
     * @param position die aktuelle Position
     * @param hp die HP
     * @param movement die Beweglichkeit
     * @param ability1 Fähigkeit 1
     * @param ability2 Fähigkeit 2
     * @param ability3 Fähigkeit 3
     */
    public Spielerklassen(Field field, Position position, int hp, int movement, Ability ability1, Ability ability2, Ability ability3)  {
        super(field,position, hp, movement,ability1);
        scan = new Reader();
        setAbility2(ability2);
        setAbility3(ability3);
    }

    /**
     * Setzt Fähigkeit zwei
     * @param ability2 die Fähigkeit 2
     */
    private void setAbility2(Ability ability2) {
        this.ability2 = ability2;
    }

    /**
     * Setzt Fähigkeit drei
     * @param ability3 die Fähigkeit 3
     */
    private void setAbility3(Ability ability3) {
        this.ability3 = ability3;
    }

    /**
     * Gibt Fähigkeit 2 zurück
     * @return Fähigkeit 2
     */
    public Ability getAbility2() {
        return ability2;
    }

    /**
     * Gibt Fähigkeit 3 zurück
     * @return Fähigkeit 3
     */
    public Ability getAbility3() {
        return ability3;
    }

    /**
     * Regelt das die richtige Fähigkeiten-Methode aufgerufen wird
     * @param ability mit welcher Fähigkeit schlussendlich angegriffen wird
     * @param spot an welcher Position angegriffen wird
     */
    public void attack(Ability ability, Position spot)  {
        if (ability.equals(getAbility1())) {
            attackingAb1(spot);
        } else if (ability.equals(getAbility2())) {
            attackingAb2(spot);
        } else if (ability.equals(getAbility3())) {
            attackingAb3(spot);
        }
    }

    /**
     * Führt Fähigkeit 1 aus
     * @param spot der Punkt an welchem Fähigkeit 1 ausgeführt werden soll
     */
    abstract void attackingAb1(Position spot);

    /**
     * Führt Fähigkeit 2 aus
     * @param spot der Punkt an welchem Fähigkeit 2 ausgeführt werden soll
     */
    abstract void attackingAb2(Position spot);

    /**
     * Führt Fähigkeit 3 aus
     * @param spot der Punkt an welchem Fähigkeit 3 ausgeführt werden soll
     */
    abstract void attackingAb3(Position spot);

    /**
     * Wird aufgerufen wenn der Spieler die Möglichkeit "Angriff" auswählt.
     * Regelt erst mit welcher Fähigkeit angegriffen wird,
     * anschließend an welcher Position der Angriff erfolgt
     * Ruft die Methoden chooseAttackAbility() und attack(Ability ability, Position spot) auf.
     */
    public void attack() {
        Ability attack = chooseAttackAbility();
        System.out.println("Where would you like to place your attack?");
        Position spot;
        if(attack.getRange() == 0){
            attack(attack, this.givePosition());
        }
        else{

            spot = newPosition(this.givePosition(), attack.getRange());

            attack(attack, spot);
        }
    }

    /**
     * Regelt mit welcher Fähigkeit angegriffen wird und gibt diese Fähigkeit anschließend zurück
     * @return ausgewählte Fähigkeit
     */
    public Ability chooseAttackAbility(){
        System.out.println("Which ability do you wanna attack with?");
        attackingPosibilitysshort();
        String answer = scan.scans();
        switch (answer){
            case "1":
                return getAbility1();
            case "2":
                if(getAbility2().getCurrentcooldown() == 0){
                    return getAbility2();
                }
                else{
                    System.out.println("This ability is still unavaible, please choose a different ability");
                    System.out.println();
                }
                break;
            case "3":
                if(getAbility3().getCurrentcooldown() == 0){
                    return getAbility3();
                }
                else{
                    System.out.println("This ability is still unavaible, please choose a different ability");
                    System.out.println();
                }
                break;
            case "4":
                attackingPosibilityslong();
                System.out.println();
                break;
            default:
                System.out.println("not a legal statement, please repeat");
                System.out.println();
                break;
        }
        return chooseAttackAbility();
    }

    /**
     * Wird aufgerufen wenn der Spieler sich dazu entscheiden seinen Charakter zu nur zu bewegen
     * Regelt das sich der Charakter bewegt und das die entsprechend ausgewählte Position
     * nicht bereits besetzt ist
     */
    public void moving() {
        System.out.println("Please type in where u wanna move to");
        Position newPosition = newPosition(this.givePosition(), this.getMovement());

        if(giveField().giveObjektAt(newPosition) != null){
            System.out.println("This Position is already taken, you cant move there");
            moving();
        }
        else {
            putPosition(newPosition);
        }
    }

    /**
     * Gibt eine neue Position in reichweite zurück.
     * Wird sowohl für Angriffs-Fähigkeiten als auch für move() benutzt
     * @param spot gibt die aktuelle Position an.
     * @param range gibt entweder die Reichweite der Fähigkeit an, oder wie weit der jeweilie Charakter sich bewegen kann.
     * @return neue Position wenn sie in Reichweite ist. Ansonsten wird der rekursive aufruf der Methode zurück gegeben.
     */
    public Position newPosition(Position spot, int range)  {
        System.out.println("Type in the name of the colum:");

        int spalte = scan.scani();

        System.out.println("And the line please:");

        int zeile = scan.scani();


        Position newPosition = new Position(zeile, spalte);

        ArrayList<Position> a = giveField().positionInRange(spot, range);
        for (Position p : a) {
            if(p.equals(newPosition)) {
                return newPosition;
            }
        }

        System.out.println("This Position is not in range, please type in another Position:");
        return newPosition(spot, range);
    }

    /**
     * Gibt dem Spieler einen kurzen Überblick über den aktuelle Abklingstatus seier Fähigkeiten
     */
    public void attackingPosibilitysshort(){
        System.out.println("You can attack with the the following abilitys:");
        System.out.println("(1) " + getAbility1().getName()+ ": "+ "\t"+getAbility1().getCurrentCooldown());
        System.out.println("(2) " +getAbility2().getName()+ ": "+ "\t"+getAbility2().getCurrentCooldown());
        System.out.println("(3) " +getAbility3().getName()+ ": "+ "\t"+getAbility3().getCurrentCooldown());
        System.out.println("(4) for the long description again");
        System.out.println();
    }

    /**
     * Gibt den aktuellen Abklingsstatus aller Fähigkeiten und deren genaue Beschreibung aus
     */
    public void attackingPosibilityslong(){
        System.out.println();
        System.out.println("The " + getClass().getSimpleName()+" can attack with the the following abilitys:");
        System.out.println(getAbility1().getName()+ ": "+ "\t"+getAbility1().getCurrentCooldown()+ "\t" +descriptionAb1());
        System.out.println(getAbility2().getName()+ ": "+ "\t"+getAbility2().getCurrentCooldown() + "\t"+descriptionAb2());
        System.out.println(getAbility3().getName()+ ": "+ "\t"+getAbility3().getCurrentCooldown() + "\t"+descriptionAb3());
        System.out.println();
        System.out.println("The " + getClass().getSimpleName() + "'s Movement is: " + getMovement());
        System.out.println();
    }

    /**
     * Überschriebene Methode, gibt Infos zu Fähigkeit 1 aus.
     * Wird in den jeweiligen Subklassen noch überschrieben, da sich die Eigenschaften jeder Subklasse unterscheiden
     * @return String über Fähigkeit 1
     */
    public String descriptionAb1(){
        return "\t" +"Cooldowntime: " + getAbility1().getMAXCOOLDOWN() + "\t\t" +"Attackrange: " + getAbility1().getRangeString();
    }

    /**
     * Überschriebene Methode, gibt Infos zu Fähigkeit 2 aus.
     * Wird in den jeweiligen Subklassen noch überschrieben, da sich die Eigenschaften jeder Subklasse unterscheiden
     * @return String über Fähigkeit 2
     */
    public String descriptionAb2(){
        return "\t" +"Cooldowntime: " + getAbility2().getMAXCOOLDOWN() + "\t\t"+"Attackrange: " + getAbility2().getRangeString();
    }

    /**
     * Überschriebene Methode, gibt Infos zu Fähigkeit 3 aus.
     * Wird in den jeweiligen Subklassen noch überschrieben, da sich die Eigenschaften jeder Subklasse unterscheiden
     * @return String über Fähigkeit 3
     */
    public String descriptionAb3(){
        return "\t" +"Cooldowntime: " + getAbility3().getMAXCOOLDOWN() + "\t\t"+"Attackrange: " + getAbility3().getRangeString();
    }

}

