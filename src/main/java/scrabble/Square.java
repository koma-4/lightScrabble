package scrabble;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;


public class Square extends JComponent implements Comparable<Square>  {
    private char content;
    private final int row;
    private final int col;
    private final int SIZE = 35;



    public Square(int row, int col) {
        this(row, col, (char)-1);
    }


    public Square(int row, int col, char content) {
        this.row = row;
        this.col = col;
        this.content = content;
    }


    public void setContent(char content) {
        this.content = content;
    }


    public boolean hasContent() {
        return content!=((char)-1);
    }


    public char getContent() {
        return content;
    }


    public int getRow() {
        return row;
    }


    public int getColumn() {
        return col;
    }


    // для GameBoard.addWord метода
    @Override
    public int compareTo(Square o) {
        if (this.row!=o.getRow()) {
            if (this.row > o.getRow()) return 1;
            return -1;
        } else if (this.col != o.getColumn()) {
            if (this.col > o.getColumn()) return 1;
            return -1;
        } else { return 0;}
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (row==0 && col==3 ||row==0 && col==11 ||row==2 && col==6 ||row==2 && col==8 ||row==3 && col==0 ||
                row==3 && col==7 ||row==3 && col==14 ||row==6 && col==2 || row==6 && col==6 ||row==6 && col==8 ||
                row==6 && col==12 ||row==7 && col==3 ||row==7 && col==11 ||row==8 && col==2 ||row==8 && col==6 ||
                row==8 && col==8 ||row==8 && col==12 ||row==11 && col==0 ||row==11 && col==7 ||row==11 && col==14 ||
                row==12 && col==6 ||row==12 && col==8 ||row==14 && col==3 ||row==14 && col==11) {
            g.setColor(Color.blue);
        } else if (row==0 && col==0 || row==0 && col==7 || row==14 && col==14 || row==0 && col==14|| row==14 && col==0
                || row==14 && col==7) {
            g.setColor(Color.RED);
        } else if (row==7 && col==7 || row==1 && col==1 || row==2 && col==2 || row==3 && col==3 || row==4 && col==4 ||
                row==1 && col==13 || row==2 && col==12 || row==3 && col==11 || row==4 && col==10 || row==13 && col==1 ||
                row==12 && col==2 || row==11 && col==3 || row==10 && col==4 || row==13 && col==13 || row==12 && col==12
                || row==11 && col==11 || row==10 && col==10 ) {
            g.setColor(Color.MAGENTA);
        } else if (row==1 && col==5 ||row==1 && col==9 ||row==5 && col==1 ||row==5 && col==5 ||row==5 && col==9 ||
                row==5 && col==13 ||row==9 && col==1 ||row==9 && col==5 ||row==9 && col==9 ||row==9 && col==13 ||
                row==13 && col==5 ||row==13 && col==9) {
            g.setColor(Color.green);
        }
        else {
            g.setColor(Color.black);
        }
        g.drawRect(5, 0, SIZE + 5, SIZE-2);
        if (this.hasContent()) {
            g.setColor(Color.black);
            g.setFont(new Font("Verdana", Font.PLAIN, 24));
            g.drawString(Character.toString(content), SIZE/3, SIZE-(SIZE/3));
        }
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SIZE, SIZE);
    }


    @Override
    public Dimension getMinimumSize() {
        return new Dimension(SIZE, SIZE);
    }
}


