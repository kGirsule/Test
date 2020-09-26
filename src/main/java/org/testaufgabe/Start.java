package org.testaufgabe;



import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Die Klasse Start regelt den regulären Spielablauf
 *
 * @author Katharina Girsule
 * @version 7.6.2020
 */
public class Start {
    private final Field field;
    private int round;

    private final Simulationsansicht ansicht;
    private final ArrayList<Spielerklassen> players;
    private final ArrayList<Enemys> enemys;
    private final Reader scan;


    /**
     * Erstellt ein neues Spiel
     * @param tiefe Feldtiefe
     * @param breite Feldbreite
     */
    public Start(int tiefe, int breite) throws IOException {

        scan = new Reader();
        field = new Field(tiefe, breite);
        ansicht = new Simulationsansicht(tiefe, breite);
        players = new ArrayList<>();
        enemys = new ArrayList<>();
        setColours();
        enterRoom();

    }

    /**
     * Setzt die Farben für alle Akteure
     */
    private void setColours(){
        ansicht.setzeFarbe(Mage.class, Color.blue);
        ansicht.setzeFarbe(Knight.class, Color.orange);
        ansicht.setzeFarbe(Rouge.class, Color.yellow);
        ansicht.setzeFarbe(Ranger.class, Color.green);
        ansicht.setzeFarbe(Enemys.class, Color.red);
    }

    /**
     * Stellt Anfangszustand her
     */
    private void enterRoom() {
        putPlayers();
        putEnemys();
        rounds();
    }

    /**
     * Lässt solange Spieler und Gegner gegeneinander antreten, bis eine Seite vollkommen tot ist
     */
    private void rounds() {
        round = 1;
        while(enemyalive()&&playeralive()){
            ansicht.zeigeStatus(round, charakterStatus(), field);

            actPlayers();
            anyDeaths();
            if(players.size() == 0){
                System.out.println("Game Over, you've lost");
                break;
            }
            else if(enemys.size()==0){
                System.out.println("Congratulations, you've won!");
                break;
            }


            actEnemys();
            anyDeaths();
            if(players.size() == 0){
                System.out.println("Game Over, you've lost");
                break;
            }
            else if(enemys.size()==0){
                System.out.println("Congratulations, you've won!");
                break;
            }

            round++;
        }
        System.out.println("See u again");

    }

    /**
     * Geht die Liste aller Gegner durch,
     * und regelt alles was pro Runde für jeden Gegner passieren soll
     */
    private void actEnemys(){
        Iterator <Enemys> it = enemys.iterator();
        while(it.hasNext()){
            Enemys enemy = it.next();
            if (enemy.isAlive()) {
                if(enemy.isImmobilized()){
                    enemy.setImmobilized(false);
                }
                else {
                    if(!enemy.isKnight()){
                        enemy.act(positionsplayers());
                    }
                }
                enemy.setAttackingavaible(true);
            }
            else{
                enemy.giveField().clear(enemy.givePosition());
                it.remove();
            }
            anyDeaths();
        }
    }

    /**
     * Geht die Liste aller Spieler-Charaktere durch,
     * und regelt alles was pro Runde für jeden Spieler-Charakter passieren soll
     */
    private void actPlayers() {
        Iterator <Spielerklassen> it = players.iterator();
        while(it.hasNext()){
            Spielerklassen player = it.next();
            if (player.isAlive()) {
                player.setInvinciple(false);
                player.setAvoiding(false);
                player.setHavetoattack(false);
                player.getAbility2().setCurrentcooldown();
                player.getAbility3().setCurrentcooldown();
                doSomething(player);
                ansicht.zeigeStatus(round, charakterStatus(), field);
                doSomething(player);
                ansicht.zeigeStatus(round, charakterStatus(), field);
                player.setImmobilized(false);
            }
            else{
                player.giveField().clear(player.givePosition());
                it.remove();
            }
            anyDeaths();
        }
    }

    /**
     * Lässt den Spieler entscheiden was er machen möchte
     * @param player der entsprechende Charakter den der Spieler gerade spielt
     */
    private void doSomething(Spielerklassen player)  {
        player.attackingPosibilityslong();
        System.out.println("What should " + player.getClass().getSimpleName() + " do?");
        System.out.println();
        System.out.println("press 1 for moving, 2 for attacking");
        String answer = scan.scans();
        switch (answer) {
            case "1":
                if (!player.isImmobilized()) {
                    player.moving();
                }

                break;
            case "2":
                player.attack();
                break;
            default:
                System.out.println("not a legal statement, please repeat");
                doSomething(player);
                break;
        }
    }

    /**
     * Gibt zurück ob noch irgendein Gegner am leben ist
     * @return ob noch irgendein Gegner am leben ist
     */
    private boolean enemyalive(){
        for (Enemys e: enemys) {
            if(e.isAlive()){
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt zurück ob noch irgendein Spieler am leben ist
     * @return ob noch irgendein Spieler am leben ist
     */
    private boolean playeralive(){
        for (Spielerklassen player: players) {
            if(player.isAlive()){
                return true;
            }
        }
        return false;
    }

    /**
     * Setzt einen Vertreter jeder Subklasse auf dessen Startposition im Feld
     */
    private void putPlayers() {
        players.add(new Mage(field, new Position(2, 0 ), 7,3,
                new Ability("magic missle", 2,0),
                new Ability("fire ball  ",2,2),
                new Ability("ice block  ",0,3)));

        players.add(new Knight(field, new Position(5, 0), 18, 2,
                new Ability("strike    ", 2,0),
                new Ability("taunt     ",0,2),
                new Ability("inspire   ",0,3)));

        players.add(new Rouge(field, new Position(8, 0), 10, 4,
                new Ability("twinstrike   ", 1,0),
                new Ability("poison blades",0,2),
                new Ability("invisibility",0,3)));

        players.add(new Ranger(field, new Position(11, 0), 13,3,
                new Ability("bulls eye    ", 3,0),
                new Ability("pinning arrow",3,2),
                new Ability("rain arrows  ",3,3)));

    }

    /**
     * Verteilt 10 Gegner per Zufall auf dem Feld
     */
    private void putEnemys()  {
        Position p;
        for (int i = 0; i < 10; i++) {
            int a = ThreadLocalRandom.current().nextInt(2, field.gibTiefe());
            int b = ThreadLocalRandom.current().nextInt(2, field.gibBreite());
            p = new Position(a, b);
            if (field.giveObjektAt(p) == null) {
                enemys.add(new Enemys(field, p,10,2, new Ability("Floor1",2,0) ));
            }
            else{
                i--;
            }
        }
    }

    /**
     * Checkt ab ob es irgendwelche Charaktere gibt welche gestorben sind
     * und nimmt diese vom Feld
     */
    private void anyDeaths(){
        Iterator <Spielerklassen> it = players.iterator();
        while(it.hasNext()){
            Spielerklassen player = it.next();
            if (!player.isAlive()) {
                player.giveField().clear(player.givePosition());
                it.remove();
            }
        }
        Iterator <Enemys> it2 = enemys.iterator();
        while(it2.hasNext()){
            Enemys enemys = it2.next();
            if (!enemys.isAlive()) {
                enemys.giveField().clear(enemys.givePosition());
                it2.remove();
            }
        }
        ansicht.zeigeStatus(round, charakterStatus(), field);
    }

    /**
     * Gibt eine Liste mit den Positionen aller Spielercharakter aus
     * @return Liste mit den Positionen aller Spielercharakter
     */
    private ArrayList<Position> positionsplayers(){
        ArrayList<Position> positionsPlayers = new ArrayList<>();
        Iterator <Spielerklassen> it = players.iterator();
        while (it.hasNext()){
            Spielerklassen sp = it.next();
            if(sp.isAlive()){
                positionsPlayers.add(sp.givePosition());
            }
            else {
                sp.giveField().clear(sp.givePosition());
                it.remove();
            }
        }
        /*for (Spielerklassen player : players) {
            if (player.isAlive()) {
                positionsPlayers.add(player.givePosition());
            }
        }*/
        return positionsPlayers;
    }

    /**
     * Gibt den Status der HP aller Spielercharaktere als String zurück
     * @return Status der HP aller Spielercharaktere als String zurück
     */
    private String charakterStatus(){
        if(players.size() == 4){
            return players.get(0).getClass().getSimpleName() + ": " + players.get(0).getHp() + " HP   " +
                    players.get(1).getClass().getSimpleName() + ": " + players.get(1).getHp() + " HP   " +
                    players.get(2).getClass().getSimpleName() + ": " + players.get(2).getHp() + " HP   " +
                    players.get(3).getClass().getSimpleName() + ": " + players.get(3).getHp() + " HP   " ;
        }
        else if(players.size() == 3){
            return players.get(0).getClass().getSimpleName() + ": " + players.get(0).getHp() + " HP   " +
                    players.get(1).getClass().getSimpleName() + ": " + players.get(1).getHp() + " HP   " +
                    players.get(2).getClass().getSimpleName() + ": " + players.get(2).getHp() + " HP   " ;
        }
        else if(players.size() == 2){
            return players.get(0).getClass().getSimpleName() + ": " + players.get(0).getHp() + " HP   " +
                    players.get(1).getClass().getSimpleName() + ": " + players.get(1).getHp() + " HP   " ;
        }
        else if(players.size() == 1){
            return players.get(0).getClass().getSimpleName() + ": " + players.get(0).getHp() + " HP   " ;
        }
        else {
            return "All your charakters are dead.";
        }
    }
}
