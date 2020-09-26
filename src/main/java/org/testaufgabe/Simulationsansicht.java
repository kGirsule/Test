package org.testaufgabe;



import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Eine grafische Ansicht des Simulationsfeldes.
 * Die Ansicht zeigt für jede Position ein gefärbtes Rechteck,
 * das den jeweiligen Inhalt repräsentiert, und hat eine
 * vorgebene Hintergrundfarbe.
 * Die Farben für die verschiedenen Tierarten können mit
 * der Methode setzeFarbe definiert werden.
 *
 * @author David J. Barnes und Michael Kölling
 * @version 2016.02.29
 */
public class Simulationsansicht extends JFrame
{
    // Die Farbe für leere Positionen
    private static final Color LEER_FARBE = Color.white;

    // Die Farbe für Objekte ohne definierte Farbe
    private static final Color UNDEF_FARBE = Color.gray;

    private String round = "Round: ";
    private String hpStatus ="";
    private JLabel roundlabel, hplabel;

    private Feldansicht feldansicht;

    JPanel headPanel = new JPanel(new GridLayout(3, 1));
    JPanel footPanel = new JPanel(new GridLayout(3,1));

    private String text = "        0       1       2        3       4       5        6        7       8       9      10      " +
            "11     12      13     14     15      16      17     18     19";

    private Map<Class, Color> farben;



    /**
     * Erzeuge eine Ansicht mit der gegebenen Breite und Höhe.
     * @param hoehe   die Höhe der Simulation
     * @param breite  die Breite der Simulation
     */
    public Simulationsansicht(int hoehe, int breite) throws IOException {


        farben = new LinkedHashMap<>();

        roundlabel= new JLabel(round, JLabel.CENTER);
        headPanel.add(roundlabel, BorderLayout.NORTH);
        headPanel.add(new JLabel("", JLabel.LEFT), BorderLayout.CENTER);
        headPanel.add(new JLabel(text, JLabel.LEFT));



        hplabel = new JLabel(hpStatus, JLabel.CENTER);
        footPanel.add(new JLabel(text, JLabel.LEFT), BorderLayout.NORTH);
        footPanel.add(new JLabel("", JLabel.LEFT), BorderLayout.CENTER);
        footPanel.add(hplabel, BorderLayout.SOUTH);



        setLocation(50, 50);


        feldansicht = new Feldansicht(hoehe, breite);





        Container inhalt = getContentPane();

        inhalt.add(headPanel,BorderLayout.NORTH);
        JLabel test1 = new JLabel("<html><body>0<br><br> 1<br><br> 2<br><br> 3<br><br> 4<br><br> 5<br><br> " +
                "6 <br><br> 7  <br><br> 8 <br><br> 9 <br><br> 10  <br><br> 11 <br> <br> 12 <br> <br> 13  <br> <br> 14 </body></html>");
        test1.setFont(new Font(test1.getName(), Font.BOLD, 11));
        inhalt.add(test1,BorderLayout.WEST);

        inhalt.add(feldansicht, BorderLayout.CENTER);

        JLabel test = new JLabel("<html><body>0<br><br>1<br><br>2<br><br>3<br><br>4<br><br>5<br><br> " +
                "6 <br><br>7  <br><br>8 <br><br>9 <br><br>10  <br><br> 11 <br> <br> 12 <br> <br> 13  <br> <br> 14 </body></html>");
        test.setFont(new Font(test.getName(), Font.BOLD, 11));

        inhalt.add(test,BorderLayout.EAST);
        inhalt.add(footPanel, BorderLayout.SOUTH);

        setAlwaysOnTop(true);
        pack();
        setVisible(true);

    }



    /**
     * Definiere eine Farbe für die gegebene Tierklasse.
     * @param charakterklasse  das Klassenobjekt der Tierklasse
     * @param farbe       die zu benutzende Farbe für die Tierklasse
     */
    public void setzeFarbe(Class charakterklasse, Color farbe)
    {
        farben.put(charakterklasse, farbe);
    }

    public static String transformStringToHtml(String strToTransform) {
        String ans = "<html>";
        String br = "<br>";
        String[] lettersArr = strToTransform.split("");
        for (String letter : lettersArr) {
            ans += letter + br;
        }
        ans += "</html>";
        return ans;
    }
    /**
     * @return  die definierte Farbe für die gegebene Tierklasse
     */
    private Color gibFarbe(Class charakterklasse)
    {
        Color farbe = farben.get(charakterklasse);
        if(farbe == null) {
            // für die gegebene Klasse ist keine Farbe definiert
            return UNDEF_FARBE;
        }
        else {
            return farbe;
        }
    }

    /**
     * Zeige den aktuellen Zustand des Feldes.
     *
     * @param field     das Feld, das angezeigt werden soll
     */
    public void zeigeStatus(int r, String a, Field field)
    {
        if(!isVisible())
            setVisible(true);

        roundlabel.setText(round + r);
        hplabel.setText(a);

        feldansicht.zeichnenVorbereiten();

        for(int zeile = 0; zeile < field.gibTiefe(); zeile++) {
            for(int spalte = 0; spalte < field.gibBreite(); spalte++) {
                Object akteur = field.giveObjektAt(zeile, spalte);
                if(akteur != null) {

                    feldansicht.zeichneMarkierung(spalte, zeile, gibFarbe(akteur.getClass()));
                }
                else {
                    feldansicht.zeichneMarkierung(spalte, zeile, LEER_FARBE);
                }
            }
        }



        feldansicht.repaint();
    }

    /**
     * Liefere eine grafische Ansicht eines rechteckigen Feldes.
     * Dies ist eine geschachtelte Klasse (eine Klasse, die
     * innerhalb einer anderen Klasse definiert ist), die eine
     * eigene grafische Komponente für die Benutzungsschnittstelle
     * definiert. Diese Komponente zeigt das Feld an.
     * Dies ist fortgeschrittene GUI-Technik - Sie können sie
     * für Ihr Projekt ignorieren, wenn Sie wollen.
     */
    private class Feldansicht extends JPanel
    {
        private static final long serialVersionUID = 20060330L;
        private final int DEHN_FAKTOR = 30;

        private int feldBreite, feldHoehe;
        private int xFaktor, yFaktor;
        Dimension size;
        private Graphics g;
        private Image feldImage;

        /**
         * Erzeuge eine neue Komponente zur Feldansicht.
         */
        public Feldansicht(int hoehe, int breite)
        {
            feldHoehe = hoehe;
            feldBreite = breite;
            size = new Dimension(1, 1);
        }

        /**
         * Der GUI-Verwaltung mitteilen, wie groß wir sein wollen.
         * Der Name der Methode ist durch die GUI-Verwaltung festgelegt.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(feldBreite * DEHN_FAKTOR,
                    feldHoehe * DEHN_FAKTOR);
        }

        /**
         * Bereite eine neue Zeichenrunde vor. Da die Komponente
         * in der Größe geändert werden kann, muss der Maßstab neu
         * berechnet werden.
         */
        public void zeichnenVorbereiten()
        {
            if(! size.equals(getSize())) {  // Größe wurde geändert...
                size = getSize();
                feldImage = feldansicht.createImage(size.width, size.height);
                g = feldImage.getGraphics();

                xFaktor = size.width / feldBreite;
                if(xFaktor < 1) {
                    xFaktor = DEHN_FAKTOR;
                }
                yFaktor = size.height / feldHoehe;
                if(yFaktor < 1) {
                    yFaktor = DEHN_FAKTOR;
                }
            }
        }

        /**
         * Zeichne an der gegebenen Position ein Rechteck mit
         * der gegebenen Farbe.
         */
        public void zeichneMarkierung(int x, int y, Color farbe)
        {
            g.setColor(farbe);
            g.fillRect(x * xFaktor, y * yFaktor, xFaktor-1, yFaktor-1);
        }

        /**
         * Die Komponente für die Feldansicht muss erneut angezeigt
         * werden. Kopiere das interne Image in die Anzeige.
         * Der Name der Methode ist durch die GUI-Verwaltung festgelegt.
         */
        public void paintComponent(Graphics g)
        {
            if(feldImage != null) {
                Dimension aktuelleGroesse = getSize();
                if(size.equals(aktuelleGroesse)) {
                    g.drawImage(feldImage, 0, 0, null);
                }
                else {
                    // Größe des aktuellen Images anpassen.
                    g.drawImage(feldImage, 0, 0, aktuelleGroesse.width,
                            aktuelleGroesse.height, null);
                }
            }
        }
    }
}

