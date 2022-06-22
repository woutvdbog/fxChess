package domain;

import domain.Piece.Type;

public class Board {

	private Piece[][] board;
	
	public Board(String FEN) {;
		initBoard(FEN);
	}
	
	public Piece[][] getPieces() {
		return this.board;
	}
	
	private void initBoard(String FEN) {
		board = interpretFEN(FEN);
	}
	
	public void updateBoard(Piece[][] board) {
		this.board = board;
	}
	
	private Piece[][] interpretFEN(String FEN) {
		
		Piece[][] tempBoard = new Piece[8][8];
		
		int rowIndex = 0;
		int colIndex = 0;
		
		String[] rows = FEN.split("/");
		
		for(String row : rows) {
			for(char c : row.toCharArray()) {
				if(Character.isDigit(c)) {
					colIndex += Character.getNumericValue(c);
				} else {
					boolean white = Character.toString(c).matches("[A-Z]");
					
					Type piece = null;
					
					switch(Character.toLowerCase(c)) {
					case 'k':
						piece = Piece.Type.KING;
						break;
					case 'q':
						piece = Piece.Type.QUEEN;
						break;
					case 'b':
						piece = Piece.Type.BISHOP;
						break;
					case 'n':
						piece = Piece.Type.KNIGHT;
						break;
					case 'r':
						piece = Piece.Type.ROOK;
						break;
					case 'p':
						piece = Piece.Type.PAWN;
						break;
					}
					
					if(piece != null) {
						tempBoard[rowIndex][colIndex] = new Piece(piece, colIndex, rowIndex, white);
						colIndex++;
					}
				}
			}
			colIndex = 0;
			rowIndex++;
		}
		
		return tempBoard;
	}

}
