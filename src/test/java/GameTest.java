import static org.junit.Assert.*;
import org.junit.*;
import scrabble.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class GameTest {
    private final LetterBag letterBag = new LetterBag("/src/main/letters.txt");
    @Test
    public void testAddToBoard(){
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        List<Character> startBench = Arrays.asList('А', 'Б', 'В', 'Е', 'Е', 'М', 'Р');
        Player currPlayer = new Player("Петя", startBench, true);
        char c = currPlayer.getLetter(1);
        final JButton b = new JButton(Character.toString(c));
        final JPanel tileBenchPanel = new JPanel();
        tileBenchPanel.add(b);
        final Square selectedLetter = new Square(7,7);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(b.getText().equals("")) &&
                        (!selectedLetter.hasContent())) {
                    selectedLetter.setContent(b.getText().charAt(0));
                    b.setText("");
                }
            }
        });
        b.doClick();
        assertEquals('Б',selectedLetter.getContent());
        assertEquals("", b.getText());
    }

}
