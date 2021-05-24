
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.*;
import scrabble.*;

public class GameBoardTest {

    private final LetterBag letterBag = new LetterBag("/src/main/letters.txt");

    @Test
    public void testFirstWordNotInCenter() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs = new ArrayList<Square>();
        sqs.add(new Square(7, 6, 'А'));
        sqs.add(new Square(8, 6, 'Т'));
        assertEquals("Надо в центр --> -1", -1, g.addWord(sqs, true));
    }

    @Test
    public void testFirstWordOneLetter() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs = new ArrayList<Square>();
        sqs.add(new Square(7, 7, 'А'));
        assertEquals("Нет такого --> -1", -1, g.addWord(sqs, true));
    }


    @Test
    public void testFirstWordStartsInCenter() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs = new ArrayList<Square>();
        sqs.add(new Square(7, 7, 'А'));
        sqs.add(new Square(7, 8, 'Д'));
        assertEquals("AД --> 6 points", 6, g.addWord(sqs, true));
    }

    @Test
    public void testFirstWordEndsInCenter() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs = new ArrayList<Square>();
        sqs.add(new Square(7, 6, 'А'));
        sqs.add(new Square(7, 7, 'Д'));
        assertEquals(6, g.addWord(sqs, true));
    }

    @Test
    public void testFirstWordEndsInCenterColumns() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs = new ArrayList<Square>();
        sqs.add(new Square(7, 7, 'А'));
        sqs.add(new Square(8, 7, 'Д'));
        assertEquals(6, g.addWord(sqs, true));
    }

    @Test
    public void testFirstWordMiddleInCenter() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs = new ArrayList<Square>();
        sqs.add(new Square(7, 6, 'Г'));
        sqs.add(new Square(7, 7, 'А'));
        sqs.add(new Square(7, 8, 'Д'));
        assertEquals(12, g.addWord(sqs, true));
    }

    @Test
    public void testFirstWordMiddleInCenterColumns() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs = new ArrayList<Square>();
        sqs.add(new Square(6, 7, 'Г'));
        sqs.add(new Square(7, 7, 'А'));
        sqs.add(new Square(8, 7, 'Д'));
        assertEquals(12, g.addWord(sqs, true));
    }

    @Test
    public void testFirstWordNotAWord() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs = new ArrayList<Square>();
        sqs.add(new Square(6, 7, 'Г'));
        sqs.add(new Square(7, 7, 'Д'));
        sqs.add(new Square(8, 7, 'Д'));
        assertEquals(-1, g.addWord(sqs, true));
    }

    @Test
    public void testSecondWordInvalidPlacement1() {   //нет такого слова
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs1 = new ArrayList<Square>();
        sqs1.add(new Square(6, 7, 'Г'));
        sqs1.add(new Square(7, 7, 'А'));
        sqs1.add(new Square(8, 7, 'Д'));
        g.addWord(sqs1, true);

        ArrayList<Square> sqs2 = new ArrayList<Square>();
        sqs2.add(new Square(6, 7, 'М'));
        sqs2.add(new Square(7, 8, 'Т'));
        assertEquals("Invalid Placement", -1, g.addWord(sqs2, false));
    }

    @Test
    public void testSecondWordInvalidPlacement2() {   //нет якоря
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs1 = new ArrayList<Square>();
        sqs1.add(new Square(6, 7, 'Г'));
        sqs1.add(new Square(7, 7, 'А'));
        sqs1.add(new Square(8, 7, 'Д'));
        g.addWord(sqs1, true);
        ArrayList<Square> sqs2 = new ArrayList<Square>();
        sqs2.add(new Square(8, 9, 'А'));
        sqs2.add(new Square(8, 10, 'Д'));
        assertEquals("Invalid Placement", -1, g.addWord(sqs2, false));
    }


    @Test
    public void testSecondWordValid() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs1 = new ArrayList<Square>();
        sqs1.add(new Square(6, 7, 'Г'));
        sqs1.add(new Square(7, 7, 'А'));
        sqs1.add(new Square(8, 7, 'Д'));
        g.addWord(sqs1, true);
        ArrayList<Square> sqs2 = new ArrayList<Square>();
        sqs2.add(new Square(7, 8, 'Д'));
        assertEquals(6, g.addWord(sqs2, false));
    }


    @Test
    public void testSecondWordCrossing() {
        GameBoard g = new GameBoard("/src/main/smallDictionary.txt", letterBag);
        ArrayList<Square> sqs1 = new ArrayList<Square>();
        sqs1.add(new Square(6, 7, 'Д'));
        sqs1.add(new Square(8, 7, 'О'));
        ArrayList<Square> sqs2 = new ArrayList<Square>();
        sqs2.add(new Square(7, 6, 'Г'));
        sqs2.add(new Square(7, 7, 'Н'));
        sqs2.add(new Square(7, 8, 'О'));
        sqs2.add(new Square(7, 9, 'М'));
        g.addWord(sqs2, true);

        assertEquals(8, g.addWord(sqs1, false));
    }

}





