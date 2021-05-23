package scrabble;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.io.Reader;


public class TokenScanner implements Iterator<String> {
    private int c;
    private final Reader r;


    public TokenScanner(java.io.Reader in) throws IOException {
        if (in==null) throw new IllegalArgumentException();
        this.r = in;
        c = r.read();

    }


    public static boolean isWordCharacter(int c) {
        return (Character.isLetter(c) || ((char)c)=='\'');
    }



    public static boolean isWord(String s) {
        if (s==null || s.length()<=0) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!(isWordCharacter(s.codePointAt(i)))) {
                return false;
            }
        }
        return true;
    }


    public boolean hasNext() {
        return c != -1;
    }


    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        StringBuilder answer = new StringBuilder();
        boolean isWordChar = isWordCharacter(c);

        try {
            if (isWordChar) {
                while(isWordCharacter(c) && c != -1) {
                    answer.append((char) c);
                    c = r.read();
                }
            } else {
                while(!isWordCharacter(c) && c != -1) {
                    answer.append((char) c);
                    c = r.read();
                }
            }
        } catch (IOException e) {
            throw new NoSuchElementException();
        }
        return answer.toString();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}

