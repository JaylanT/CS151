import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MancalaModel {

	private int[] board;
	private int turn;
	private ArrayList<ChangeListener> listeners;

	public static final int KALAH_1 = 6;
	public static final int KALAH_2 = 13;

	public MancalaModel() {
		board = new int[14];
		turn = 0;
		listeners = new ArrayList<ChangeListener>();
	}
	
	public void attach(ChangeListener l) {
		listeners.add(l);
	}

	public void update(int position) {
		move(position);
		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
		print();
	}

	public void setGamePieces(int pieces) {
		for (int i = 0; i < 14; i++) {
			if (i != KALAH_1 && i != KALAH_2) {
				board[i] = pieces;
			}
		}
	}

	public int getTurn() {
		return turn % 2;
	}

	public int[] getBoard() {
		return board.clone();
	}
	
	public void move(int position) {
		int pieces = board[position];
		board[position] = 0;
		for (int i = position + 1; pieces > 0; i++, pieces--) {
			// player 1's turn
			if (getTurn() == 0) {
				if (i == KALAH_2) {
					i = 0;
				}
				if (pieces == 1) {
					if (i >= 0 && i < KALAH_1) {
						// landed on empty house of player 1's board
						if (board[i] == 0 && board[12 - i] != 0) {
							board[KALAH_1]++;
							board[i] = -1;
							board[KALAH_1] += board[12 - i];
							board[12 - i] = 0;
						}
					}
					// landed on player 1's Kalah
					else if (i == KALAH_1) {
						turn++;
					}
				}
			}
			// player 2's turn
			else {
				if (i == KALAH_1) {
					i++;
				} else if (i > KALAH_2) {
					i = 0;
				}
				if (pieces == 1) {
					if (i > KALAH_1 && i < KALAH_2) {
						// landed on empty house of player 2's board
						if (board[i] == 0 && board[12 - i] != 0) {
							board[KALAH_2]++;
							board[i] = -1;
							board[KALAH_2] += board[12 - i];
							board[12 - i] = 0;
						}
					}
					// landed on player 2's Kalah
					else if (i == KALAH_2) {
						turn++;
					}
				}
			}
			board[i]++;
		}
		if (isGameOver()) {
			findWinner();
		}
		turn++;
	}

	private boolean isGameOver() {
		boolean gameOver = false;
		// check player 1's side
		int total1 = 0;
		for (int i = 0; i < KALAH_1; i++) {
			total1 += board[i];
		}
		int total2 = 0;
		for (int i = 7; i < KALAH_2; i++) {
			total2 += board[i];
		}
		if (total1 == 0 || total2 == 0) {
			gameOver = true;
		}
		return gameOver;
	}

	private void findWinner() {
		for (int i = 0; i < KALAH_1; i++) {
			board[KALAH_1] += board[i];
			board[i] = 0;
		}
		for (int i = 7; i < KALAH_2; i++) {
			board[KALAH_2] += board[i];
			board[i] = 0;
		}
		System.out.println("Results");
		System.out.println("Player 1:" + board[KALAH_1]);
		System.out.println("Player 2:" + board[KALAH_2]);
	}

	public void print() {
		String row1 = "";
		String row2 = "";
		for (int i = 0; i < 14; i++) {
			if (i >= 7) {
				row2 = board[i] + " " + row2;
			} else {
				row1 = row1 + board[i] + " ";
			}
		}
		System.out.println(row2);
		System.out.println("  " + row1);
		if (getTurn() == 0) {
			System.out.println("\nPlayer 1's turn (0-5)");
		} else {
			System.out.println("\nPlayer 2's turn (7-12)");
		}
	}
}