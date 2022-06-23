package domain;

import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Piece {

	private ImageView image;
	private boolean firstMove;
	
	public enum Type {
		EMPTY('0'),
		KING('k'),
		QUEEN('q'),
		BISHOP('b'),
		KNIGHT('n'),
		ROOK('r'),
		PAWN('p');

		private char c;
		
		Type(char c) {
			this.c = c;
		}
		
		public char getChar() {
			return this.c;
		}
	}
	
	private Type type;
	private boolean white;
	private double x;
	private double y;
	
	public Piece(Type type, int x, int y, boolean white) {
		this.type = type;
		this.white = white;
		setX(x);
		setY(y);
		setFirstMove(true);
		this.image = new ImageView(new Image(getClass().getResource("/images/" + this.getType().getChar() + (this.isWhite() ? "1" : "") + ".png").toExternalForm()));
	}

	public boolean isWhite() {
		return white;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public void draw() {
		image.setX(x * 80);
		image.setY(y * 80);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}

	public ImageView getImageView() {
		return image;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

}
