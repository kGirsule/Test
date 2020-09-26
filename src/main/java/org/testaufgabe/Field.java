package org.testaufgabe;




import java.util.*;
import java.util.LinkedList;

public class Field
{
    private int tiefe, breite;
    private Object[][] field;
    private Reader scan;

    public Field(int tiefe, int breite)
    {
        this.tiefe = tiefe;
        this.breite = breite;
        field = new Object[tiefe][breite];
        scan = new Reader();
    }

    public void emptyfield()
    {
        for(int zeile = 0; zeile < tiefe; zeile++) {
            for(int spalte = 0; spalte < breite; spalte++) {
                field[zeile][spalte] = null;
            }
        }
    }

    public void clear(Position position)
    {
        field[position.gibZeile()][position.gibSpalte()] = null;
    }

    public void place(Object charakter, int zeile, int spalte)
    {
        place(charakter, new Position(zeile, spalte));
    }

    public void place(Object akteur, Position position)
    {
        field[position.gibZeile()][position.gibSpalte()] = akteur;
    }

    public Object giveObjektAt(Position position)
    {
        return giveObjektAt(position.gibZeile(), position.gibSpalte());
    }

    public Object giveObjektAt(int zeile, int spalte)
    {
        if(field[zeile][spalte] == null){
            return null;
        }
        else {
            return field[zeile][spalte];
        }
    }

    public int gibTiefe()
    {
        return tiefe;
    }

    public int gibBreite()
    {
        return breite;
    }

    private List<Position> freieNachbarpositionen(Position position)
    {
        List<Position> frei = new LinkedList<>();
        List<Position> nachbarn = nachbarpositionen(position);
        for(Position naechste : nachbarn) {
            if(giveObjektAt(naechste) == null) {
                frei.add(naechste);
            }
        }
        return frei;
    }

    public List<Position> nachbarpositionen(Position position)
    {
        assert position != null : "Keine Position an nachbarpostionen uebergeben";
        // Die Liste der zurueckzuliefernden Positionen
        List<Position> positionen = new LinkedList<>();
        if(position != null) {
            int zeile = position.gibZeile();
            int spalte = position.gibSpalte();
            for(int zDiff = -1; zDiff <= 1; zDiff++) {
                int naechsteZeile = zeile + zDiff;
                if(naechsteZeile >= 0 && naechsteZeile < tiefe) {
                    for(int sDiff = -1; sDiff <= 1; sDiff++) {
                        int naechsteSpalte = spalte + sDiff;
                        // Ungueltige Positionen und Ausgangsposition ausschliessen.
                        if(naechsteSpalte >= 0 && naechsteSpalte < breite && (zDiff != 0 || sDiff != 0)) {
                            positionen.add(new Position(naechsteZeile, naechsteSpalte));
                        }
                    }
                }
            }
        }
        return positionen;
    }

    public ArrayList<Position> nachbarlisteneu(Position p){
        ArrayList<Position> neu = new ArrayList<>();
        int zeile = p.gibZeile();
        int spalte = p.gibSpalte();
        for(int zDiff = -1; zDiff <= 1; zDiff++) { //Für Gange die 1 durch Range ersetzen???
            int naechsteZeile = zeile + zDiff;
            if(naechsteZeile >= 0 && naechsteZeile < tiefe) {
                for(int sDiff = -1; sDiff <= 1; sDiff++) {
                    int naechsteSpalte = spalte + sDiff;
                    // Ungueltige Positionen und Ausgangsposition ausschliessen.
                    if(naechsteSpalte >= 0 && naechsteSpalte < breite && (zDiff != 0 || sDiff != 0)) {
                        neu.add(new Position(naechsteZeile, naechsteSpalte));
                    }
                }
            }
        }
        return neu;
    }

    //neueMethode Positionen in Reichweite x
    public ArrayList<Position> positionInRange(Position p, int range){
        int zeile = p.gibZeile();
        int spalte = p.gibSpalte();
        ArrayList<Position> neu = new ArrayList<>();
        for(int zDiff = -range; zDiff <= range; zDiff++) { //Für Gange die 1 durch Range ersetzen???
            int naechsteZeile = zeile + zDiff;
            if(naechsteZeile >= 0 && naechsteZeile < tiefe) {
                for(int sDiff = -range; sDiff <= range; sDiff++) {
                    int naechsteSpalte = spalte + sDiff;
                    // Ungueltige Positionen und Ausgangsposition ausschliessen.
                    if(naechsteSpalte >= 0 && naechsteSpalte < breite ) { //&& (zDiff != 0 || sDiff != 0
                        neu.add(new Position(naechsteZeile, naechsteSpalte));
                    }
                }
            }
        }
        return neu;
    }

    public ArrayList<Akteur> enemysInRange(Position p, int range){
        int zeile = p.gibZeile();
        int spalte = p.gibSpalte();
        ArrayList<Akteur> neu = new ArrayList<>();
        for(int zDiff = -range; zDiff <= range; zDiff++) { //Für Gange die 1 durch Range ersetzen???
            int naechsteZeile = zeile + zDiff;
            if(naechsteZeile >= 0 && naechsteZeile < tiefe) {
                for(int sDiff = -range; sDiff <= range; sDiff++) {
                    int naechsteSpalte = spalte + sDiff;
                    // Ungueltige Positionen und Ausgangsposition ausschliessen.
                    if(naechsteSpalte >= 0 && naechsteSpalte < breite && (zDiff != 0 || sDiff != 0)) {
                        if(giveObjektAt(naechsteZeile, naechsteSpalte) != null){
                            neu.add((Akteur) giveObjektAt(naechsteZeile,naechsteSpalte));
                        }
                    }
                }
            }
        }
        return neu;
    }

}