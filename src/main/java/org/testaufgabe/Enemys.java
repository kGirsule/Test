package org.testaufgabe;




import java.util.ArrayList;

/**
 * Die Klasse Enemys regelt alles was nur Gegner-Charaktere können sollten.
 *
 * @author Katharina Girsule
 * @version 6.6.2020
 */
public class Enemys extends Akteur {
    private boolean attackingavaible = true;
    private Reader reader;

    /**
     * Subklasse von Akteur, regelt alles was nur Enemys betrifft
     * @param field das aktuell belegte Feld
     * @param position die aktuelle Position
     * @param hp die HP
     * @param movement die Beweglichkeit
     * @param ability1 Fähigkeit 1
     */
    public Enemys(Field field, Position position, int hp, int movement, Ability ability1) {
        super(field,position,hp, movement, ability1);
        reader = new Reader();
    }

    /**
     * Ist ein Spieler in Attack-Reichweite, so wird dieser angegriffen,
     * ansonsten bewegt sich der Akteur in Richtung des nächstbesten Spielers
     * @param players liste mit den Positionen aller Spierler-Charaktere
     */
    public void act(ArrayList<Position> players){
        boolean donesomething = false;
        ArrayList<Position> a = giveField().positionInRange(givePosition(), getAbility1().getRange());

        for (Position prange : a) {
            for(Position pplayer : players){
                if(prange.equals(pplayer)){
                    if(attackingavaible){
                        donesomething = true;
                        attacking(pplayer);
                    }

                }
            }
        }

        if(!donesomething){
            moving(differenz(players));
        }

    }

    /**
     * Bewegt sich in die Richtung des anvisierten Spielers
     * @param destination position des anvisierten Spielers
     */
    private void moving(Position destination){

        if(destination.gibSpalte() < givePosition().gibSpalte()){ //nach links gehen, um gleiche spalten muss ich mir keine sorgen machen,
            // da bei einer solchen nähe sonst angegriffen worden wäre
            Position newPosition  = new Position(givePosition().gibZeile(), givePosition().gibSpalte()-getMovement());

            if(destination.gibZeile() < givePosition().gibZeile()){ //nach oben gehen
                if(givePosition().gibZeile()-1 >= 0){
                    newPosition = new Position(givePosition().gibZeile()-1, givePosition().gibSpalte()-getMovement());
                }
            }
            else if(destination.gibZeile() > givePosition().gibZeile()){ //nach unten gehen
                if(givePosition().gibZeile()+1 <= giveField().gibTiefe())
                    newPosition = new Position(givePosition().gibZeile()+1, givePosition().gibSpalte()-getMovement());
            }

            if(giveField().giveObjektAt(newPosition) == null){
                putPosition(newPosition);
            }
            else {
                if(destination.gibZeile()-1 >= 0){
                    if(giveField().giveObjektAt((givePosition().gibZeile()-1),givePosition().gibSpalte()-getMovement()) == null){
                        putPosition( new Position((givePosition().gibZeile()-1), givePosition().gibSpalte()-getMovement()));
                    }

                }
                else if(destination.gibZeile()+1 <= giveField().gibTiefe()){
                    if(giveField().giveObjektAt((givePosition().gibZeile()+1), givePosition().gibSpalte()-getMovement()) == null){
                        putPosition( new Position(givePosition().gibZeile()+1, givePosition().gibSpalte()-getMovement() ));
                    }
                }

            }
        }
        else { //nach rechts gehen
            Position newPosition = new Position(givePosition().gibZeile(), givePosition().gibSpalte()+getMovement());
            if(destination.gibZeile() < givePosition().gibZeile()){ //nach oben gehen
                if(givePosition().gibZeile()-1 >= 0){
                    newPosition = new Position(givePosition().gibZeile()-1, givePosition().gibSpalte()-getMovement());
                }
            }
            else if(destination.gibZeile() > givePosition().gibZeile()){ //nach unten gehen
                if(givePosition().gibZeile()+1 <= giveField().gibTiefe())
                    newPosition = new Position(givePosition().gibZeile()+1, givePosition().gibSpalte()-getMovement());
            }
            if(giveField().giveObjektAt(newPosition) == null){
                putPosition(newPosition);
            }
            else {
                if(destination.gibZeile()-1 >= 0){
                    if(giveField().giveObjektAt((givePosition().gibZeile()-1),givePosition().gibSpalte()+getMovement()) == null){
                        putPosition( new Position((givePosition().gibZeile()-1), givePosition().gibSpalte()+getMovement()));
                    }

                }
                else if(destination.gibZeile()+1 <= giveField().gibTiefe()){
                    if(giveField().giveObjektAt((givePosition().gibZeile()+1), givePosition().gibSpalte()+getMovement()) == null){
                        putPosition( new Position(givePosition().gibZeile()+1, givePosition().gibSpalte()+getMovement() ));
                    }
                }

            }
        }

    }

    /**
     * Gibt die Position des für den jeweiligen Enemys nähersten Spieler-Charakters zurück
     * @param players liste mit den Positionen aller Spielercharakteren
     * @return Position des nähersten Spieler-Charakters
     */
    private Position differenz(ArrayList<Position> players){
        Position position = givePosition();
        int difz = 100;
        int difs = 100;
        for (Position player: players) {
            if(Math.abs(player.gibSpalte()-givePosition().gibSpalte()) < difs
                    || Math.abs(player.gibZeile()-givePosition().gibZeile())< difz){
                difz = Math.abs(player.gibZeile()-givePosition().gibZeile());
                difs = Math.abs(player.gibSpalte()-givePosition().gibSpalte());
                position = new Position(player.gibZeile(),player.gibSpalte());
            }
        }
        return  position;
    }

    /**
     * Wird aufgerufen wenn ein anfreifbarer Spielercharakter in der Nähe ist und
     * greift ebendiesen an. Anschließend wird geregelt dass der entsprechende Feind
     * kein zweites mal angreift.
     * @param player die Position des angreifbaren Spielers
     */
    private void attacking(Position player){
        if(giveField().giveObjektAt(player) != null){
            Akteur a = (Akteur) giveField().giveObjektAt(player);
            if(a.isAvoiding()){
                if(reader.randomint() == 0){
                    System.out.println(a.getClass().getSimpleName() + " got Attackt by the Enemy, but he succesfully " +
                            "avoided damage due to the Knights ability");
                }
                else {
                    a.takesDamage(getDamage());
                    System.out.println(a.getClass().getSimpleName() + " tried to avoid the attack, but he was not sucessful");
                }
            }
            else {
                a.takesDamage(getDamage());
                System.out.println(a.getClass().getSimpleName() + " got Attackt by the Enemy!");
            }

        }
        attackingavaible = false;
    }

    /**
     * Setzt die boolean ob ein Angriff derzeit möglich ist
     * @param newAvaible setzt neu ob ein Angriff derzeit möglich ist.
     */
    public void setAttackingavaible(boolean newAvaible){
        attackingavaible = newAvaible;
    }

    /**
     * Methode die checkt ob sich der Ritter in Attack-Reichweite befindet und ob dessen Ability2 aktiviert ist.
     * Ist sie aktiviert, so wird der Ritter automatisch angegriffen und sonst niemand mehr.
     * @return
     */
    public boolean isKnight(){
        ArrayList<Akteur> takeDamage = giveField().enemysInRange(givePosition(), getAbility1().getRange());
        for (Akteur a:takeDamage) {
            if(a instanceof Knight){
                if(a.isHavetoattack()){
                    attacking(a.givePosition());
                    System.out.println("Enemy was forced to attack the Knight.");
                    return true;
                }
            }
        }
        return false;
    }

}
