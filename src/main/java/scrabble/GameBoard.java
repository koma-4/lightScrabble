package scrabble;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class GameBoard extends JPanel {


	public static final int BOARD_SIZE = 15;
	public static final int SQUARE_SIZE = 35;
	public static final int CENTER = 7;
	private final Square[][] board = new Square[BOARD_SIZE][BOARD_SIZE];
	private Game.Dictionary dict;
	private final LetterBag bagOfTiles;


	public GameBoard(String dictionaryFile, LetterBag letterBag) {

		if (dictionaryFile == null || letterBag == null) {
			JOptionPane.showMessageDialog(null, "File Not Found",
					"Dictionary File Not Found", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		this.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

		//создание игровой доски
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				Square sq = new Square(row, column);
				board[row][column] = sq;
				this.add(sq);
			}
		}

		//считывание словаря
		try {
			this.dict = new Game.Dictionary(new TokenScanner(new FileReader(dictionaryFile)));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "File Not Found",
					"Dictionary File Not Found", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		//получение "мешка" букв
		bagOfTiles = letterBag;


	}

	public Square[][] getCurrentBoard() {
		return board;
	}

	private void addSquareToBoard(Square s) {
		board[s.getRow()][s.getColumn()] = s;
	}


	private void addToBoard(List<Square> sqs) {
		for (Square sq : sqs) {
			addSquareToBoard(sq);
		}
	}


	public int addWord(List<Square> sqs, boolean firstTurn) {
		for (Square sq : sqs) {
			int row = sq.getRow();
			int col = sq.getColumn();
			if (row < 0 || row >= BOARD_SIZE
					|| col < 0 || col >= BOARD_SIZE) return -1;
		}

		Collections.sort(sqs);


		Square first = sqs.get(0);
		int firstRow = first.getRow();
		int firstCol = first.getColumn();
		boolean sameRow = false;


		Set<Integer> indices = new TreeSet<Integer>();
		for (int i = 1; i < sqs.size(); i++) {
			int row = sqs.get(i).getRow();
			int col = sqs.get(i).getColumn();
			if (board[row][col].hasContent() ||
					board[firstRow][firstCol].hasContent()) {
				return -1;
			}

			//Горизонтальное будет слово или вертикальное?
			if (i == 1) {
				if (row == firstRow) {
					indices.add(col);
					sameRow = true;
					indices.add(first.getColumn());
				} else if (col == firstCol) {
					indices.add(row);
					sameRow = false;
					indices.add(first.getRow());
				} else {
					return -1;
				}
			} else {
				if (sameRow) {
					indices.add(col);
					if (row != firstRow) return -1;
				} else {
					indices.add(row);
					if (col != firstCol) return -1;
				}
			}
		}


		//для слова из 2х букв
		if (sqs.size() == 1) {
			sameRow = true;
			indices.add(firstCol);
		}

		//получение слова
		StringBuilder s = new StringBuilder();
		int index = -1;
		int firstIndex = -1;
		Iterator<Integer> iter = indices.iterator();
		int previous = -1;

		//нужно, для начисления бонуса за нахождение в ячейке
		int points = 0;
		List<String> list2 = Arrays.asList("00", "07", "014", "70", "714", "140", "147", "1414",
				"11", "22", "33", "44", "77", "212", "311", "410", "113", "104", "113",
				"122", "131", "1010", "1111", "1212", "1313", "15", "19", "51", "55", "59",
				"513", "91", "95", "99", "913", "135", "139", "03", "011", "26", "28", "30", "37",
				"314", "62", "66", "68", "612", "73", "711", "82", "86", "88", "812", "110", "117", "1114",
				"126", "128", "143", "1411");
		ArrayList<String> bonus1 = new ArrayList<String>(list2);

		Set<Integer> indicesNoPoints = new TreeSet<Integer>();

		int bonus;
		int wordBonus = 1;
		String coord;

		while (iter.hasNext()) {
			index++;
			int a = iter.next();

			if (previous != -1) {
				if (a != previous + 1) {
					if (sameRow) {
						for (int n = 1; n < a - previous; n++) {
							if (!(board[firstRow][previous + n].hasContent())) return -1;
							s.append(board[firstRow][previous + n].getContent());
							indicesNoPoints.add(previous + n);

							coord = (String.valueOf(firstRow) + String.valueOf(previous + n));
							bonus = 1;
							if (bonus1.contains(coord)) {
								if (bonus1.indexOf(coord) < 8) {
									wordBonus = wordBonus * 3;
								}
								if (bonus1.indexOf(coord) > 7 && bonus1.indexOf(coord) < 25) {
									wordBonus = wordBonus * 2;
								}
								if (bonus1.indexOf(coord) > 24 && bonus1.indexOf(coord) < 37) {
									if (wordBonus == 1) bonus = 3;
								}
								if (bonus1.indexOf(coord) > 36) {
									if (wordBonus == 1) bonus = 2;
								}
							}
							points = points +
									bagOfTiles.getPointValue(board[firstRow][previous + n].getContent()) * bonus;
						}
					} else {
						for (int n = 1; n < a - previous; n++) {
							if (!(board[previous + n][firstCol].hasContent())) return -1;
							s.append(board[previous + n][firstCol].getContent());
							indicesNoPoints.add(previous + n);

							coord = (String.valueOf(previous + n) + String.valueOf(firstCol));
							bonus = 1;
							if (bonus1.contains(coord)) {
								if (bonus1.indexOf(coord) < 8) {
									wordBonus = wordBonus * 3;
								}
								if (bonus1.indexOf(coord) > 7 && bonus1.indexOf(coord) < 25) {
									wordBonus = wordBonus * 2;
								}
								if (bonus1.indexOf(coord) > 24 && bonus1.indexOf(coord) < 37) {
									if (wordBonus == 1) bonus = 3;
								}
								if (bonus1.indexOf(coord) > 36) {
									if (wordBonus == 1) bonus = 2;
								}
							}
							points = points +
									bagOfTiles.getPointValue(board[previous + n][firstCol].getContent()) * bonus;
						}


					}

				}
			} else firstIndex = a;


			s.append(sqs.get(index).getContent());
			previous = a;


			if (sameRow) {
				coord = (String.valueOf(firstRow) + String.valueOf(a));
			} else {
				coord = (String.valueOf(a) + String.valueOf(firstCol));
			}
			bonus = 1;
			if (bonus1.contains(coord)) {
				if (bonus1.indexOf(coord) < 8) {
					wordBonus = wordBonus * 3;
				}
				if (bonus1.indexOf(coord) > 7 && bonus1.indexOf(coord) < 25) {
					wordBonus = wordBonus * 2;
				}
				if (bonus1.indexOf(coord) > 24 && bonus1.indexOf(coord) < 37) {
					bonus = 3;
				}
				if (bonus1.indexOf(coord) > 36) {
					bonus = 2;
				}
			}
			points = points + bagOfTiles.getPointValue(sqs.get(index).getContent()) * bonus;

		}
		points = points * wordBonus;


		char[] asChar = s.toString().toCharArray();

		if (s.toString().length() == 7) points = points + 50; //доп. 50 очков по правилам


		int startRow = (sameRow) ? firstRow : firstIndex;
		int startCol = (!sameRow) ? firstCol : firstIndex;


		//вспомогательная функция
		int result = addWordHelper(asChar, startRow, startCol, (!sameRow), firstTurn, indicesNoPoints);

		//добавление слова, подсчёт баллов
		if (result > -1) {
			addToBoard(sqs);
			return points + result;

		} else return -1;
	}


	private int addWordHelper(char[] word, int startRow, int startCol,
							  boolean vertical, boolean firstTurn, Set<Integer> indicesNoPoints) {

		int startIndex = (vertical) ? startRow : startCol;
		int otherIndex = (!vertical) ? startRow : startCol;

		if (firstTurn) {
			if (!(CENTER < startIndex + word.length && CENTER >= startIndex
					&& otherIndex == CENTER)) return -1;
			if (word.length == 0) return -1;

		} else {

			for (int i = startIndex; i < startIndex + word.length; i++) {
				if (vertical) {
					if (board[i][startCol].hasContent()) {
						if (word[i - startIndex] != board[i][startCol].getContent()) return -1;
					}
				} else {
					if (board[startRow][i].hasContent()) {
						if (word[i - startIndex] != board[startRow][i].getContent()) {
							return -1;
						}
					}
				}
			}

			boolean foundSquare = false;

			//поиск якоря
			for (int i = startIndex; i <= startIndex + word.length; i++) {

				if (vertical) {

					if (i == startIndex) {                      //якорь-первая буква
						if (i - 1 >= 0) {
							Square s = board[i - 1][startCol];
							if (s.hasContent()) {
								foundSquare = true;
								indicesNoPoints.add(i - 1);
								break;
							}
						}
					}

					if (i == startIndex + word.length - 1) {         //якорь-последняя буква
						if (i + 1 < BOARD_SIZE) {
							Square s = board[i + 1][startCol];
							if (s.hasContent()) {
								foundSquare = true;
								indicesNoPoints.add(i + 1);
								break;
							}
						}
					}

					if (startCol != 0) {                    //якорь - буква спрва
						Square s = board[i][startCol - 1];
						if (s.hasContent()) {
							foundSquare = true;
							break;
						}
					}

					if (startCol != BOARD_SIZE - 1) {         //якорь - буква слева
						Square s = board[i][startCol + 1];
						if (s.hasContent()) {
							foundSquare = true;
							break;
						}
					}
				} else {
					if (i == startIndex) {       //якорь - буква слева
						if (i - 1 >= 0) {
							Square s = board[startRow][i - 1];
							if (s.hasContent()) {
								foundSquare = true;
								indicesNoPoints.add(i - 1);
								break;
							}
						}
					}

					if (i == startIndex + word.length - 1) {    //якорь - буква справа
						if (i + 1 < BOARD_SIZE) {
							Square s = board[startRow][i + 1];
							if (s.hasContent()) {
								foundSquare = true;
								indicesNoPoints.add(i + 1);
								break;
							}
						}
					}

					if (startRow != 0) {           //якорь - буква сверху
						Square s = board[startRow - 1][i];
						if (s.hasContent()) {
							foundSquare = true;
							break;
						}
					}

					if (startRow != BOARD_SIZE - 1) {
						Square s = board[startRow + 1][i];  //якорь - буква снизу
						if (s.hasContent()) {
							foundSquare = true;
							break;
						}
					}

				}
			}


			if (!(foundSquare)) {
				return -1;
			}

		}
		return findWordsToCheck(word, startRow,
				startCol, vertical, indicesNoPoints);
	}


	private int findWordsToCheck(char[] word, int startRow,
								 int startCol, boolean isVertical, Set<Integer> indicesNoPoints) {


		int points = 0;

		//слово по-вертикали
		if (isVertical) {

			for (int i = startRow; i < startRow + word.length; i++) {
				String horizontal = "" + word[i - startRow];

				//слева
				for (int j = startCol - 1; j >= 0; j--) {
					if (indicesNoPoints.contains(i)) break;
					Square next = board[i][j];
					if (next.hasContent()) {
						horizontal = next.getContent() + horizontal;
					} else break;
				}

				//справа
				for (int j = startCol + 1; j < 15; j++) {
					if (indicesNoPoints.contains(i)) break;
					Square next = board[i][j];
					if (next.hasContent()) {
						horizontal += next.getContent();
					} else break;
				}
				if (horizontal.length() > 1) {
					if (!dict.isWord(horizontal)) {
						return -1;
					}
				}
			}

			String vertical = new String(word);

			//сверху
			for (int up = startRow - 1; up >= 0; up--) {
				Square next = board[up][startCol];
				if (next.hasContent()) {
					vertical = next.getContent() + vertical;
					points = points + getBonus(up, startCol, points);
				} else break;
			}

			//снизу
			for (int up = startRow + word.length; up < 15; up++) {
				Square next = board[up][startCol];
				if (next.hasContent()) {
					vertical += next.getContent();
					points = points + getBonus(up, startCol, points);
				} else break;
			}
			if (vertical.length() < 2 || !dict.isWord(vertical)) {
				return -1;
			}

		} else {


			String horizontal = new String(word);

			//слева
			for (int side = startCol - 1; side >= 0; side--) {
				Square next = board[startRow][side];
				if (next.hasContent()) {
					horizontal = next.getContent() + horizontal;
					points = points + getBonus(startRow, side, points);
				} else break;
			}

			//справа
			for (int side = startCol + word.length; side < 15; side++) {
				Square next = board[startRow][side];
				if (next.hasContent()) {
					points = points + getBonus(startRow, side, points);
					horizontal += next.getContent();
				} else break;
			}

			if (horizontal.length() < 2 || !dict.isWord(horizontal)) {
				return -1;
			}

			for (int i = startCol; i < startCol + word.length; i++) {
				String vertical = "" + word[i - startCol];

				//сверху
				for (int j = startRow - 1; j >= 0; j--) {
					if (indicesNoPoints.contains(i)) break;
					Square next = board[j][i];
					if (next.hasContent()) {
						vertical = next.getContent() + vertical;
					} else break;
				}

				//снизу
				for (int j = startRow + 1; j < 15; j++) {
					if (indicesNoPoints.contains(i)) break;
					Square next = board[j][i];
					if (next.hasContent()) {
						vertical += next.getContent();
					} else break;
				}
				if (vertical.length() > 1) {
					if (!dict.isWord(vertical)) {
						return -1;
					}
				}
			}


		}
		return points;

	}

	private int getBonus(int row, int col, int points) {
		int wordBonus = 1;
		int bonus = 1;
		List<String> list2 = Arrays.asList("00", "07", "014", "70", "714", "140", "147", "1414",
				"11", "22", "33", "44", "77", "212", "311", "410", "113", "104", "113",
				"122", "131", "1010", "1111", "1212", "1313", "15", "19", "51", "55", "59",
				"513", "91", "95", "99", "913", "135", "139", "13", "111", "26", "28", "30", "37",
				"314", "62", "66", "68", "612", "73", "711", "82", "86", "88", "812", "110", "117", "1114",
				"126", "128", "143", "1411");
		ArrayList<String> bonus1 = new ArrayList<String>(list2);
		String coord = String.valueOf(row) + String.valueOf(col);
		if (bonus1.contains(coord)) {
			if (bonus1.indexOf(coord) < 8) {
				wordBonus = wordBonus * 3;
			}
			if (bonus1.indexOf(coord) > 7 && bonus1.indexOf(coord) < 25) {
				wordBonus = wordBonus * 2;
			}
			if (bonus1.indexOf(coord) > 24 && bonus1.indexOf(coord) < 37) {
				if (wordBonus == 1) bonus = 3;
			}
			if (bonus1.indexOf(coord) > 36) {
				if (wordBonus == 1) bonus = 2;
			}
		}
		return (points + bagOfTiles.getPointValue(board[row][col].getContent()) * bonus) * wordBonus;
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}


	@Override
	public Dimension getPreferredSize() {
		return new Dimension(BOARD_SIZE * SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
	}


	@Override
	public Dimension getMinimumSize() {
		return new Dimension(BOARD_SIZE * SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
	}
}

 