package domain;

import java.util.List;

public class Player {

	private String name;
	private boolean isWhite;
	private List<Piece> capturedPieces;
	
	public Player(String name, boolean isWhite) {
		this.name = name;
		this.isWhite = isWhite;
	}

	public List<Piece> getCapturedPieces() {
		return capturedPieces;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public String getName() {
		return name;
	}

}
