package scrabble;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

public class Player extends JComponent {

    private final List<Character> letterBench;
    private final String name;
    private int score;
    private boolean isMyTurn;

    public Player(String name, List<Character> startBench, boolean isMyTurn) {
        this.name = name;
        letterBench = new ArrayList<Character>(startBench);
        score = 0;
        this.isMyTurn = isMyTurn;


    }


    public boolean isMyTurn() {
        return isMyTurn;
    }


    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }


    public char getLetter(int index) {
        return letterBench.get(index);
    }


    public List<Character> getAll() {
        return new ArrayList<Character>(letterBench);
    }


    public void clear() {
        letterBench.clear();
    }


    public void addToScore(int points) {
        score += points;
    }


    public int getBenchSize() {
        return letterBench.size();
    }


    public void useLetters(List<Character> toUse) {
        for (char c : toUse) {
            this.useLetter(c);
        }

    }


    private void useLetter(Character c) {
        letterBench.remove(c);
    }


    public void addLetters(List<Character> toAdd) {
        letterBench.addAll(toAdd);
    }



    public String getName() {
        return name;
    }


    public int getScore() {
        return score;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("here");
        g.fillRect(0, 0, 100, 100000);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        for (char c: letterBench) {
            g.drawString(Character.toString(c), 0, 17);
        }



    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(245, 35);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(245, 35);
    }
}

