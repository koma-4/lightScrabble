package scrabble;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JOptionPane;


// "мешок" с буквами, наполняю считыванием файла

public class LetterBag {
    private final Map<Character, Integer> letterToAmountLeft = new TreeMap<Character, Integer>();
    private final Map<Character, Integer> letterToPointValue = new TreeMap<Character, Integer>();


    public LetterBag(String filename) {
        try {
            readFile(filename);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"File Not Found",
                    "Letters File Not Found", JOptionPane.ERROR_MESSAGE);
            System.exit(1);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Error",
                    "Problem with Letters File", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

    }


    private void readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {

            String[] list = line.split("/");
            char letter = list[0].toCharArray()[0];
            int pointValue = Integer.parseInt(list[1]);
            int amount = Integer.parseInt(list[2]);

            letterToAmountLeft.put(letter, amount);
            letterToPointValue.put(letter, pointValue);
        }
        reader.close();
    }


    public int getPointValue(char c) {
        return letterToPointValue.get(c);
    }


    public int getTilesLeft() {
        int sum = 0;
        for (int i : letterToAmountLeft.values()) {
            sum += i;
        }
        return sum;
    }


    private char drawTile() {
        Random r = new Random();
        int size = this.getTilesLeft();
        if (size < 1) throw new RuntimeException("No Tiles Left");

        int pick = r.nextInt(size);
        char toDraw = (char)(-1);
        for (char c: letterToAmountLeft.keySet()) {
            int amountLeft = letterToAmountLeft.get(c);
            pick -= letterToAmountLeft.get(c);
            if (pick <= 0) {
                toDraw = c;
                letterToAmountLeft.put(c, amountLeft-1);
                break;
            }

        }
        return toDraw;
    }


    public List<Character> drawTiles(int amount) {
        int amountLeft = this.getTilesLeft();
        if (amount > amountLeft) {
            amount = amountLeft;
        }

        List<Character> tgt = new ArrayList<Character>();
        for (int i = 0; i < amount; i++) {

            tgt.add(this.drawTile());
        }
        return tgt;
    }


    public List<Character> swapTiles(List<Character> oldLetters) {
        for (char c: oldLetters) {
            int oldValue = letterToAmountLeft.get(c);
            letterToAmountLeft.put(c, oldValue+1);
        }
        return this.drawTiles(oldLetters.size());

    }

}
