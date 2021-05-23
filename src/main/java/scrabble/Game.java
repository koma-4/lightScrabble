package scrabble;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Game implements Runnable{
	
	
	final String instructionsMessage = 
			"Добро пожаловать в упрощённую версию игры Scrabble! \n"
			+ "Ознакомьтесь с правилами игры! \n"
			+ "В свой ход Вы можете совершить следующие действия: \n"
			+ "1) Пропустить: Вы пропускаете свой ход \n"
			+ "2) Перемешать: Вы пропускаете свой ход, но \n"
			+ "к следующему ходу ваш набор фишек будет изменён \n"
			+ "3) Подтвердить ход: \n"
			+ "Выбираете букву, затем кликаете на место, в которое её \n"
			+ "необходимо поставить. \n"
			+ "После составления слова подтвердите корректность, \n"
			+ "нажав на кнопку (Подтвердить ход). \n"
			+ "Кнопка (Назад): Если во время хода Вы передумали, нажмите кнопку.  \n"
			+ "Это отменит все совершенные за ход действия. \n"
			+ "Кнопка (Инструкция): Появится инструкция \n"
					+  "Кнопка (Осталось): Появится информация о количестве оставшихся фишек \n"
			+ "Нажмите OK для начала игры!";
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
	

	

	private void resetBoard(GameBoard tempBoard, GameBoard actualBoard) {
		Square[][]  currBoard = tempBoard.getCurrentBoard();
		Square[][] oldBoard = actualBoard.getCurrentBoard();
		for (int row = 0; row < currBoard.length; row++) {
			for (int col = 0; col < currBoard[row].length; col++) {
				Square sq = currBoard[row][col];
				Square oldSq = oldBoard[row][col];
				sq.setContent(oldSq.getContent());
				sq.repaint();
			}
		}
	}
	
	private String getUsername(String player) {
		String tgt = JOptionPane.showInputDialog(null, player + ", Имя:");
		if (tgt==null) return getUsername(player);
		else return tgt;
	}

	private String getNumberOfPlayers() {
		String num = JOptionPane.showInputDialog(null, "Введите количество игроков одной цифрой(2-4):");
		if (num.equals("2") || num.equals("3") || num.equals("4")) return num;
		else return getNumberOfPlayers();
	}
		
	public void run() {
		JOptionPane.showMessageDialog(null, instructionsMessage,
				"Инструкция", JOptionPane.INFORMATION_MESSAGE);

		String numberOfPlayers = getNumberOfPlayers();
		String name1 = getUsername("Игрок 1");
		String name2 = getUsername("Игрок 2");
		String name3 = "";
		String name4 = "";
		if (numberOfPlayers.equals("3")) name3 = getUsername("Игрок 3");
		if (numberOfPlayers.equals("4")) {
			name3 = getUsername("Игрок 3");
			name4 = getUsername("Игрок 4");
		}

		final JFrame frame = new JFrame("Игра началась!");
		frame.setLocation(500, 500);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));


		try {
			int scaledWidth = 5;
            int scaledHeight = 2;
            String inputImagePath = "/Users/annakomno/IdeaProjects/lightScrabble/src/main/Scrabble-logo.png";
            Game.resize(inputImagePath, scaledWidth, scaledHeight);
 
			BufferedImage myPicture = ImageIO.read(new File(inputImagePath));
			
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			JPanel picPanel = new JPanel();
			   
			picPanel.add(picLabel);
			frame.add(picPanel);
		} catch (Exception ignored) {
		}

		
		//инициализация игроков
		final LetterBag letterBag = new LetterBag("/Users/annakomno/IdeaProjects/lightScrabble/src/main/letters.txt");

		final Player p1 = new Player(name1, letterBag.drawTiles(7), true);
		final Player p2 = new Player(name2, letterBag.drawTiles(7), false);
		Player p3 = null;
		Player p4 = null;
		if(!name3.isEmpty()) p3 = new Player(name3, letterBag.drawTiles(7), false);
		if(!name4.isEmpty()) p4 = new Player(name4, letterBag.drawTiles(7), false);
		
		
		//их баллы, очередность
		final JPanel scoreBoard = new JPanel();
		scoreBoard.setLayout(new GridLayout(1, 5));
		final JLabel score1 = new JLabel("\t\t"+ p1.getName() + " имеет " + p1.getScore() + " очков");
		final JLabel score2 = new JLabel("\t\t"+ p2.getName() + " имеет " + p2.getScore() + " очков");
		final JLabel turn = new JLabel("\t\t  Это " + p1.getName() + " делает ход");
		final JLabel score3 = new JLabel("");
		final JLabel score4 = new JLabel("");
		scoreBoard.add(score1);
		scoreBoard.add(score2);
		scoreBoard.add(turn);
		if (p4 != null) {
			score3.setText("\t\t"+ p3.getName() + " имеет " + p3.getScore() + " очков");
			score4.setText("\t\t"+ p4.getName() + " имеет " + p4.getScore() + " очков");
			scoreBoard.add(score3);
			scoreBoard.add(score4);
		}
		if (p3 != null && p4 == null) {
			score3.setText("\t\t"+ p3.getName() + " имеет " + p3.getScore() + " очков");
			scoreBoard.add(score3);
		}


		

		final Square selectedLetter = new Square(-1, -1);
		final List<Square> squaresToSubmit = new LinkedList<Square>();
		
		
		//порядок добавления букв на доску
		final JPanel tileBenchPanel = new JPanel();
		Player currPlayer;
		if (p1.isMyTurn()) currPlayer = p1;
		else currPlayer = p2;
		if(p3 != null && p3.isMyTurn()) currPlayer = p3;
		if(p4 != null && p4.isMyTurn()) currPlayer = p4;
		for (int i = 0; i < currPlayer.getBenchSize(); i++) {

			char c = currPlayer.getLetter(i);
			final JButton b = new JButton(Character.toString(c));
			tileBenchPanel.add(b);

			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean blank = b.getText().equals("");
					boolean selected = selectedLetter.hasContent();
					if (!blank && !selected) {
						selectedLetter.setContent(b.getText().charAt(0));
						b.setText("");
					}
				}
			});
		}

		final GameBoard board = new GameBoard("/Users/annakomno/IdeaProjects/lightScrabble/src/main/russian.dictionary", letterBag);
		final GameBoard tempBoard = new GameBoard("/Users/annakomno/IdeaProjects/lightScrabble/src/main/russian.dictionary", letterBag);
		Square[][]  currBoard = tempBoard.getCurrentBoard();
		for (Square[] squares : currBoard) {
			for (final Square sq : squares) {
				sq.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if ((!sq.hasContent()) && (selectedLetter.hasContent())) {
							sq.setContent(selectedLetter.getContent());
							sq.repaint();
							squaresToSubmit.add(sq);
							selectedLetter.setContent((char) -1);
						}
					}
				});
			}
		}
		
		final JPanel gameButtonPanel = new JPanel();
		final JButton undo = new JButton("Назад");
		final Player finalP = p3;
		final Player finalP1 = p4;
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetBoard(tempBoard, board);
				selectedLetter.setContent((char)-1);
				tileBenchPanel.removeAll();
				squaresToSubmit.clear();
				Player currPlayer = null;
				if (p1.isMyTurn()) currPlayer = p1;
				if (p2.isMyTurn()) currPlayer = p2;
				if (finalP != null && finalP.isMyTurn()) currPlayer = finalP;
				if (finalP1 != null && finalP1.isMyTurn()) currPlayer = finalP1;
				for (int i = 0; i < currPlayer.getBenchSize(); i++) {
					char c = currPlayer.getLetter(i);
					final JButton b = new JButton(Character.toString(c));
					tileBenchPanel.add(b);
					
					b.addActionListener( new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!(b.getText().equals("")) && 
									(!selectedLetter.hasContent())) {
								selectedLetter.setContent(b.getText().charAt(0));
								b.setText("");
							}
						}
					});
				}
				frame.getContentPane().validate();
				frame.getContentPane().repaint();
				
			}
		});


		final JButton pass = new JButton("Пропустить");
		pass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				resetBoard(tempBoard, board);
				
				selectedLetter.setContent((char)(-1));
				Player currPlayer = null;
				if (p1.isMyTurn()) currPlayer = p2;
				if(p2.isMyTurn()) {
					if(finalP != null) currPlayer = finalP;
					else currPlayer = p1;
				}
				if(finalP != null && finalP.isMyTurn()) {
					if(finalP1 != null) currPlayer = finalP1;
					else currPlayer = p1;
				}
				if (finalP1 != null && finalP1.isMyTurn()) {
					currPlayer = p1;
				}
				turn.setText("Это " + currPlayer.getName() + " делает ход");
				
				tileBenchPanel.removeAll();
				for (int i = 0; i < currPlayer.getBenchSize(); i++) {
					char c = currPlayer.getLetter(i);
					final JButton b = new JButton(Character.toString(c));
					tileBenchPanel.add(b);
					b.addActionListener( new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!(b.getText().equals("")) &&
									(!selectedLetter.hasContent())) {
								selectedLetter.setContent(b.getText().charAt(0));
								b.setText("");
							}
						}
					});
				}
				if (finalP1 != null && finalP1.isMyTurn()) {  //кто следующий?
					p1.setMyTurn(true);
				}
				if (finalP!= null && finalP.isMyTurn()) {
					if (finalP1 != null) {
						finalP1.setMyTurn(true);
						finalP.setMyTurn(false);
					}
					else p1.setMyTurn(true);
				}
				if(p2.isMyTurn()) {
					if (finalP!= null) {
						finalP.setMyTurn(true);
						p2.setMyTurn(false);
					}
					else p1.setMyTurn(true);
				}
				if (p1.isMyTurn()) {
					p2.setMyTurn(true);
					p1.setMyTurn(false);
					if (finalP1 != null && finalP1.isMyTurn()) {
						p1.setMyTurn(true);
						p2.setMyTurn(false);
						finalP1.setMyTurn(false);
					}
					if (finalP != null && finalP.isMyTurn()) {
						p1.setMyTurn(true);
						p2.setMyTurn(false);
						finalP.setMyTurn(false);
					}
				}
				frame.getContentPane().validate();
				frame.getContentPane().repaint();

				
				
			}
		});
		

		final JButton swap = new JButton("Перемешать");
		swap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				resetBoard(tempBoard, board);
				selectedLetter.setContent((char)(-1));
				Player currPlayer = null;
				if (p1.isMyTurn()) currPlayer = p2;
				if(p2.isMyTurn()) {
					if(finalP != null) currPlayer = finalP;
					else currPlayer = p1;
				}
				if(finalP != null && finalP.isMyTurn()) {
					if(finalP1 != null) currPlayer = finalP1;
					else currPlayer = p1;
				}
				if (finalP1 != null && finalP1.isMyTurn()) {
					currPlayer = p1;
				}
				List<Character> newLetters = letterBag.swapTiles(currPlayer.getAll());
				currPlayer.clear();
				currPlayer.addLetters(newLetters);
				turn.setText("Это " + currPlayer.getName() + " делает ход");
				tileBenchPanel.removeAll();
				selectedLetter.setContent((char)-1);
				for (int i = 0; i < currPlayer.getBenchSize(); i++) {
					char c = currPlayer.getLetter(i);
					final JButton b = new JButton(Character.toString(c));
					tileBenchPanel.add(b);
					
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
				}
				if (finalP1 != null && finalP1.isMyTurn()) {  //кто следующий?
					p1.setMyTurn(true);
				}
				if (finalP!= null && finalP.isMyTurn()) {
					if (finalP1 != null) {
						finalP1.setMyTurn(true);
						finalP.setMyTurn(false);
					}
					else p1.setMyTurn(true);
				}
				if(p2.isMyTurn()) {
					if (finalP!= null) {
						finalP.setMyTurn(true);
						p2.setMyTurn(false);
					}
					else p1.setMyTurn(true);
				}
				if (p1.isMyTurn()) {
					p2.setMyTurn(true);
					p1.setMyTurn(false);
					if (finalP1 != null && finalP1.isMyTurn()) {
						p1.setMyTurn(true);
						p2.setMyTurn(false);
						finalP1.setMyTurn(false);
					}
					if (finalP != null && finalP.isMyTurn()) {
						p1.setMyTurn(true);
						p2.setMyTurn(false);
						finalP.setMyTurn(false);
					}
				}
				frame.getContentPane().validate();
				frame.getContentPane().repaint();
				
				
			}
		});
		

		
		final JButton submit = new JButton("Подтвердить ход");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (squaresToSubmit.isEmpty()) {
					JOptionPane.showMessageDialog(null,"Сделайте ход "
							+ "перед его подтверждением",
							"Ход невозможен", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				boolean firstTurn = ((p1.getScore()==0 && p2.getScore()==0));
				int pointsScored = board.addWord(squaresToSubmit, firstTurn);
				
				if (pointsScored > 0) {
					Player currPlayer = null;
					if (p1.isMyTurn()) currPlayer = p1;
					if (p2.isMyTurn()) currPlayer = p2;
					if (finalP != null && finalP.isMyTurn()) currPlayer = finalP;
					if (finalP1 != null && finalP1.isMyTurn()) currPlayer = finalP1;
					currPlayer.addToScore(pointsScored);
					score1.setText("\t" + p1.getName() + " имеет " + p1.getScore() + " очков");
					score2.setText("\t" + p2.getName() + " имеет " + p2.getScore() + " очков");
					if(finalP1!= null) {
						score3.setText("\t"+ finalP.getName() + " имеет " + finalP.getScore() + " очков");
						score4.setText("\t"+ finalP1.getName() + " имеет " + finalP1.getScore() + " очков");

					}
					if(finalP != null && finalP1 == null) {
						score3.setText("\t\t"+ finalP.getName() + " имеет " + finalP.getScore() + " очков");
					}
					
					List<Character> lettersUsed = new ArrayList<Character>();
					for (Square s: squaresToSubmit) {
						lettersUsed.add(s.getContent());
					}
					
					squaresToSubmit.clear();
					selectedLetter.setContent((char)(-1));
					currPlayer.useLetters(lettersUsed);
					currPlayer.addLetters(letterBag.drawTiles(lettersUsed.size()));
					
					//выигрыш
					if (currPlayer.getBenchSize()==0) {
						int pOneWinner = p1.getScore();
						String winner = p1.getName();
						if (p2.getScore() > p1.getScore()) {
							pOneWinner = p2.getScore();
							winner = p2.getName();
						}
						if (finalP != null) {
							if (finalP.getScore() > pOneWinner) {
								pOneWinner = finalP.getScore();
								winner = finalP.getName();
							}
						}
						if (finalP1 != null) {
							if (finalP1.getScore() > pOneWinner) {
								pOneWinner = finalP1.getScore();
								winner = finalP1.getName();
							}
						}
						winner = "Победитель " + winner + "!\n";
						winner += p1.getName() + " имеет " + p1.getScore() + " очков \n"
								+ p2.getName() + " имеет " + p2.getScore() + " очков";
						if (finalP != null) {
							winner += finalP.getName() + " имеет " + finalP.getScore() + " очков";
						}
						if (finalP1 != null) {
							winner += "\n" + finalP1.getName() + " имеет " + finalP1.getScore() + " очков";
						}
						JOptionPane.showMessageDialog(null, winner, "Игра завершена",
								JOptionPane.INFORMATION_MESSAGE);
						System.exit(1);
					}
					
					lettersUsed.clear();
					


                    //очерёдность
					if (finalP1 != null && finalP1.isMyTurn()) {
						p1.setMyTurn(true);
					}
					if (finalP!= null && finalP.isMyTurn()) {
						if (finalP1 != null) {
							finalP1.setMyTurn(true);
							finalP.setMyTurn(false);
						}
						else p1.setMyTurn(true);
					}
					if(p2.isMyTurn()) {
						if (finalP!= null) {
							finalP.setMyTurn(true);
							p2.setMyTurn(false);
						}
						else p1.setMyTurn(true);
					}
					if (p1.isMyTurn()) {
						p2.setMyTurn(true);
						p1.setMyTurn(false);
						if (finalP1.isMyTurn()) {
							p1.setMyTurn(true);
							p2.setMyTurn(false);
							finalP1.setMyTurn(false);
						}
					}
					if (p1.isMyTurn()) currPlayer = p1;
					if (p2.isMyTurn()) currPlayer = p2;
					if (finalP != null && finalP.isMyTurn()) currPlayer = finalP;
					if (finalP1 != null && finalP1.isMyTurn()) currPlayer = finalP1;
					
					turn.setText("Это " + currPlayer.getName() + " делает ход");
					
					tileBenchPanel.removeAll();
					for (int i = 0; i < currPlayer.getBenchSize(); i++) {
						char c = currPlayer.getLetter(i);
						final JButton b = new JButton(Character.toString(c));
						tileBenchPanel.add(b);
						
						b.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!(b.getText().equals(""))&&
										(!selectedLetter.hasContent())) {
									selectedLetter.setContent(b.getText().charAt(0));
									b.setText("");
								}
							}
						});
					}
					
					//перерисовка
					frame.getContentPane().validate();
					frame.getContentPane().repaint();

				} else {
					JOptionPane.showMessageDialog(null,"Неверный ход. Попробуйте ещё",
							"Неверный ход", JOptionPane.ERROR_MESSAGE);
					undo.doClick();
				}
			}
		});
		
		
		final JButton instructions = new JButton("Инструкция");
		instructions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,instructionsMessage,
						"Инструкция", JOptionPane.INFORMATION_MESSAGE);
			}
			
			
		});
		
		final JButton checkTilesLeft = new JButton("Осталось");
		checkTilesLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Осталось " + letterBag.getTilesLeft() +
						" фишек в игре",
						"Осталось", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		

		frame.add(scoreBoard);
		frame.add(tempBoard);
		frame.add(tileBenchPanel);

		gameButtonPanel.add(undo);
		gameButtonPanel.add(submit);
		gameButtonPanel.add(pass);
		gameButtonPanel.add(swap);
		gameButtonPanel.add(instructions);
		gameButtonPanel.add(checkTilesLeft);

		frame.add(gameButtonPanel);

		frame.validate();
		frame.setResizable(true);
		frame.setSize(750, 1000);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}



	private static void resize(String inputImagePath, int scaledWidth,
			int scaledHeight) throws IOException {
		File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
     }

     //словарь
	public static class Dictionary {

		private final Set<String> allWords = new TreeSet<String>();


		public Dictionary(TokenScanner ts) throws IOException {
			if (ts == null) throw new IllegalArgumentException();
			while(ts.hasNext()) {
				String s = ts.next();
				if (TokenScanner.isWord(s)) {
					allWords.add(s.toLowerCase());
				}
			}
		}

		public boolean isWord(String word) {
			if (word==null) return false;
			if (TokenScanner.isWord(word)) {
				return allWords.contains(word.toLowerCase());
			} else return false;
		}
	}
}
