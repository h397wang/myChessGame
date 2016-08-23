package com.example.mychessgame;

import com.example.mychessgame.VirtualBoard.Piece;
import com.example.mychessgame.VirtualBoard.Piece.Color;
import com.example.mychessgame.VirtualBoard.Piece.Type;

/*
 * Might move this into the piece class itself
 * 
 */
public abstract class PieceFactory {
	
	/*
	 * Required to resume the game, revive the pieces based on the shared preference info
	 * 
	 */
	public static Piece revivePiece(int _row, int _col, Color _color, Type _type){
		
		Piece output = null;
		
		if (_type  == Type.BISHOP){
			output = new Bishop (_color, _row, _col);
		}else if (_type  == Type.KNIGHT){
			output = new Knight (_color, _row, _col);
		}else if (_type  == Type.ROOK){
			output = new Rook (_color, _row, _col);
		}else if (_type  == Type.QUEEN){
			output = new Queen (_color, _row, _col);
		}
		
		return output;
	}
	
	
	
	/*
	 * Required to store previous states of the game, so a copy of the board has to be
	 * created every turn
	 * 
	 */
	public static Piece copyPiece(Piece piece){
		
		Piece output = null;
		
		if (piece == null){
			return null;
		}
		
		if (piece.getType() == Type.PAWN){
			output = new Pawn( (Pawn) piece);
		}else if (piece.getType() == Type.BISHOP){
			output = new Bishop ( (Bishop) piece);
		}else if (piece.getType() == Type.KNIGHT){
			output = new Knight ( (Knight) piece);
		}else if (piece.getType() == Type.ROOK){
			output = new Rook ( (Rook) piece);
		}else if (piece.getType() == Type.QUEEN){
			output = new Queen ( (Queen) piece);
		}else if (piece.getType() == Type.KING){
			output = new King ( (King) piece);
		}
		
		return output;
	}
	/**
	 * Call this method at the beginning of the game to create all of our pieces
	 * It will know what kind of piece to create based on the coordinates of the piece
	 * @param color_type
	 * @return
	 */
	public static Piece createPiece(int row, int col){
		
		switch(row){ // refers to rows
		case 7: // i got it mixed up lol
			switch(col){ // refers to cols
			case 0:
				return new Rook(Color.WHITE, row, col);
			case 1:
				return new Knight(Color.WHITE,row,  col);
			case 2:	
				return new Bishop(Color.WHITE, row, col);
			case 3:
				return new Queen(Color.WHITE, row, col);
			case 4: 
				return new King(Color.WHITE, row, col);
			case 5:
				return new Bishop(Color.WHITE, row ,col);
			case 6: 
				return new Knight(Color.WHITE, row, col);
			case 7 :
				return new Rook(Color.WHITE, row, col);
			}
		case 1:
			return new Pawn(Color.BLACK, row, col);
		case 6:
			return new Pawn(Color.WHITE, row, col);
		case 0:
			switch(col){
			case 0:
				return new Rook(Color.BLACK, row, col);
			case 1:
				return new Knight(Color.BLACK, row,col);
			case 2:	
				return new Bishop(Color.BLACK, row, col);
			case 3:
				return new Queen(Color.BLACK, row, col);
			case 4: 
				return new King(Color.BLACK, row, col);
			case 5:
				return new Bishop(Color.BLACK, row, col);
			case 6: 
				return new Knight(Color.BLACK, row, col);
			case 7 :
				return new Rook(Color.BLACK, row, col);
			}
		default: // for an empty square
			return null;
		}
	}
	
}
