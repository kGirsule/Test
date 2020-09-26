package org.testaufgabe;



/**
 * Eine instanzierbare Subklasse von Spielerklassen.
 * Sie regelt alles, was nur der Bogenschütze können sollte
 *
 * @author Katharina Girsule
 * @version 31.5.2020
 */

public class Ranger extends Spielerklassen {


    /**
     * Subklasse Magier, implementiert die speziellen Fähigkeiten des Bogenschützens
     * @param field das aktuell belegte Feld
     * @param position die aktuelle Position
     * @param hp die HP
     * @param movement die Beweglichkeit
     * @param ability1 Fähigkeit 1
     * @param ability2 Fähigkeit 2
     * @param ability3 Fähigkeit 3
     */
    public Ranger(Field field, Position position, int hp, int movement, Ability ability1, Ability ability2, Ability ability3) {
        super(field,position,hp, movement, ability1,ability2,ability3);
    }

    /**
     * Überschreibt die Methode aus der Superklasse Spielerklasse
     * @return gibt die allgemeinen Daten der Superklasse zu Fähigkeit 1 aus, und zusätzlich noch eine genaue Beschreibung von Fähigkeit 1
     */
    public String descriptionAb1(){
        return super.descriptionAb1()+"\t"+" You can attack a single ranged Target once";
    }

    /**
     * Überschreibt die Methode aus der Superklasse Spielerklasse
     * @return gibt die allgemeinen Daten der Superklasse zu Fähigkeit 2 aus, und zusätzlich noch eine genaue Beschreibung von Fähigkeit 2
     */
    public String descriptionAb2(){
        return super.descriptionAb2()+"\t"+" Your Enemy cant move for one round";
    }

    /**
     * Überschreibt die Methode aus der Superklasse Spielerklasse
     * @return gibt die allgemeinen Daten der Superklasse zu Fähigkeit 3 aus, und zusätzlich noch eine genaue Beschreibung von Fähigkeit 3
     */
    public String descriptionAb3(){
        return super.descriptionAb3()+"\t"+" You can attack 3 times at once";
    }

    /**
     * Führt Fähigkeit 1 aus
     * @param spot der Punkt an welchem Fähigkeit 1 ausgeführt werden soll
     */
    protected void attackingAb1(Position spot){
        if(giveField().giveObjektAt(spot) != null){
            Akteur a = (Akteur) giveField().giveObjektAt(spot);
            a.takesDamage(getDamage());
        }
    }

    /**
     * Führt Fähigkeit 2 aus
     * @param spot der Punkt an welchem Fähigkeit 2 ausgeführt werden soll
     */
    protected void attackingAb2(Position spot){
        if(giveField().giveObjektAt(spot) != null){
            Akteur a = (Akteur) giveField().giveObjektAt(spot);
            a.setImmobilized(true);
        }
        getAbility2().setCurrentcooldown(getAbility2().getMAXCOOLDOWN());
    }

    /**
     * Führt Fähigkeit 3 aus
     * @param spot der Punkt an welchem Fähigkeit 3 ausgeführt werden soll
     */
    protected void attackingAb3(Position spot) {
        if (giveField().giveObjektAt(spot) != null) {
            Akteur a = (Akteur) giveField().giveObjektAt(spot);
            a.takesDamage(getDamage());
        }
        if (giveField().giveObjektAt(newPosition(givePosition(),getAbility3().getRange())) != null) {
            Akteur a = (Akteur) giveField().giveObjektAt(spot);
            a.takesDamage(getDamage());
        }
        if ( giveField().giveObjektAt(newPosition(givePosition(),getAbility3().getRange()))!= null) {
            Akteur a = (Akteur) giveField().giveObjektAt(spot);
            a.takesDamage(getDamage());
        }
        getAbility3().setCurrentcooldown(getAbility3().getMAXCOOLDOWN());
    }
}


