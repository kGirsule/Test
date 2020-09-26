package org.testaufgabe;



import java.util.ArrayList;
/**
 * Eine instanzierbare Subklasse von Spielerklassen.
 * Sie regelt alles, was nur der Magier können sollte
 *
 * @author Katharina Girsule
 * @version 31.5.2020
 */

public class Mage extends Spielerklassen {

    /**
     * Subklasse Magier, implementiert die speziellen Fähigkeiten des Magiers
     * @param field das aktuell belegte Feld
     * @param position die aktuelle Position
     * @param hp die HP
     * @param movement die Beweglichkeit
     * @param ability1 Fähigkeit 1
     * @param ability2 Fähigkeit 2
     * @param ability3 Fähigkeit 3
     */
    public Mage(Field field, Position position, int hp, int movement, Ability ability1, Ability ability2, Ability ability3) {
        super(field,position,hp, movement, ability1,ability2,ability3);
    }

    /**
     * Überschreibt die Methode aus der Superklasse Spielerklasse
     * @return gibt die allgemeinen Daten der Superklasse zu Fähigkeit 1 aus, und zusätzlich noch eine genaue Beschreibung von Fähigkeit 1
     */
    public String descriptionAb1(){
        return super.descriptionAb1()+"\t"+" You can attack a single Target once";
    }

    /**
     * Überschreibt die Methode aus der Superklasse Spielerklasse
     * @return gibt die allgemeinen Daten der Superklasse zu Fähigkeit 2 aus, und zusätzlich noch eine genaue Beschreibung von Fähigkeit 2
     */
    public String descriptionAb2(){
        return super.descriptionAb2()+"\t"+" You can attack two enemys in the area of effect";
    }

    /**
     * Überschreibt die Methode aus der Superklasse Spielerklasse
     * @return gibt die allgemeinen Daten der Superklasse zu Fähigkeit 3 aus, und zusätzlich noch eine genaue Beschreibung von Fähigkeit 3
     */
    public String descriptionAb3(){
        return super.descriptionAb3()+"\t"+" The Mage won't take any damage until the start of the next turn";
    }

    /**
     * Führt Fähigkeit 1 aus
     * @param spot der Punkt an welchem Fähigkeit 1 ausgeführt werden soll
     */
    protected void attackingAb1(Position spot){
        if(giveField().giveObjektAt(spot) != null){
            Akteur a = (Akteur) giveField().giveObjektAt(spot);
            a.takesDamage(getDamage());
            a.takesDamage(getDamage());
        }
    }

    /**
     * Führt Fähigkeit 2 aus
     * @param spot der Punkt an welchem Fähigkeit 2 ausgeführt werden soll
     */
    protected void attackingAb2(Position spot){
        ArrayList<Akteur> takeDamage = giveField().enemysInRange(spot, getAbility2().getRange());
        for (Akteur a:takeDamage) {
            if(isInvinciple()){
                a.setInvinciple(false);
            }
            a.takesDamage(getDamage());
            if(a instanceof Rouge){
                a.setInvinciple(true);
            }
        }
        getAbility2().setCurrentcooldown(getAbility2().getMAXCOOLDOWN());
    }

    /**
     * Führt Fähigkeit 3 aus
     * @param spot der Punkt an welchem Fähigkeit 3 ausgeführt werden soll
     */
    protected void attackingAb3(Position spot){
        setInvinciple(true);
        getAbility3().setCurrentcooldown(getAbility3().getMAXCOOLDOWN());
    }

}
