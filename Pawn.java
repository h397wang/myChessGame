package com.example.mychessgame;

import java.util.ArrayList;

import com.example.mychessgame.VirtualBoard.Piece;

public class Pawn extends Piece{
	
	int enPassantAvailable = 0; // since en passant must be done the very next move
								// use this instead of a boolean, initiliazing to 0 is fine..
	
	public Pawn(Color _color, int _row, int _col) {
		super(_color, _row, _col, Type.PAWN);
	}
	
	/*
	 * Copy constructor
	 */
	public Pawn(Pawn pawn){
		super(pawn);
		enPassantAvailable = pawn.enPassantAvailable;
	}
	
	// special case for resuming the game
	public Pawn(int _row, int _col, Color _color, int enPassant){
		super(_color, _row, _col, Type.PAWN);
		enPassantAvailable = enPassant;
	}
	
	@Override
	public boolean validateMove(int newRow, int newCol) {
		
		int deltaCol = newCol - col;
		int deltaRow = newRow - row;
		int forwardDirection = (color == Color.BLACK ? 1:-1);
		int correctDirection = forwardDirection * deltaRow; // must be + 1 ..
		Piece[][] board = getBoardPieces(); // should be done for all later...
		
		if (correctDirection == 1){ // forward move
			if (Math.abs(deltaCol) == 0 && board[newRow][newCol] == null // take free space ahead
					|| (Math.abs(deltaCol) == 1 && board[newRow][newCol] != null // capture enemy diagnally
					&& board[newRow][newCol].getColor() != color)){ 
				return true;
			}else if (Math.abs(deltaCol) == 1 && board[newRow][newCol] == null // diagnal move
					&& board[row][newCol] != null && board[row][newCol].getColor() != color // check for enpassant
					&& ((Pawn)board[row][newCol]).getType() == Piece.Type.PAWN
					&& ((Pawn)board[row][newCol]).enPassantAvailable == VirtualBoard.moveCounter -1){
							
					// SPECIAL CASE for enPassant
					// only modifyin the vb is needed
					board[row][newCol] = null;
					return true;
			}
		
		}else if (correctDirection == 2 && board[row + (deltaRow < 0? -1:1)][col] == null
					&& deltaCol == 0 && !hasMoved()){ 	// take space two ahead

					enPassantAvailable = getMoveCounter();
					return true;
		}
		
		return false;	
	}
	
	/**
	 * Pawns can be promoted to any other piece (other than the King) when they reach the other end
	 * The user will have to input what kind of piece they want (although I assume a Queen?)
	 * will automatically promote to a queen just cause
	 * the virtual board might have to be responsible...
	 * however the vbs promote function cant be called from validateMove because the normal
	 * changes made afterwards would over ride and modify it, plus the reference needs to be deleted
	 * 
	 */
	
	
	
	// called from the king, consider the pawn as a threat
	@Override
	public ArrayList<Integer[]> getDangerZone() {
		// does not account for enPassant because the dangerZone is only relevant to the king
		ArrayList<Integer[]> dangerZone = new ArrayList<Integer []>();
		int theRow = 0;
		int theCol = 0;
		Piece[][] board = getBoardPieces();
		
		// actually i could return an empty list..
		if (color == Piece.Color.BLACK){
			if (row == 7){
				return null;
			}
			theRow = row+1;	
		}else{
			if (row == 0){
				return null;
			}
			theRow = row -1;
		}
		
		
		// the row has already been modified
		if (col != 0){  
			
				Integer [] temp = {theRow,col-1};
				dangerZone.add(temp);
			
		}
		if (col != 7){
			
				Integer [] temp = {theRow,col+1};
				dangerZone.add(temp);
			
		}
		
		return dangerZone;
	}
	
	private boolean hasMoved(){
		if (color == Piece.Color.BLACK){
			if (row == 1){
				return false;
			}else{
				return true;
			}
		}else{
			if (row == 6){
				return false;
			}else{
				return true;
			}
		}
	}
}
