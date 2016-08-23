package com.example.mychessgame;

import java.util.ArrayList;

import com.example.mychessgame.VirtualBoard.Piece;

public class Bishop extends Piece{
	
	// called from a new game.
	public Bishop(Color _color, int _row, int _col){
		super(_color, _row, _col, Type.BISHOP);
		
	}
	
	// required to store all previous states on stack
	// copy constructor...
	public Bishop(Bishop p){
		super(p);	
	}

	@Override
	public boolean validateMove(int newRow, int newCol) {
		
		int deltaRow = newRow - row;
		int deltaCol = newCol - col;
		if (deltaRow == 0 || deltaCol == 0){ // needs this here or else I divide by 0
			return false;
		}
		int i = 0;
		int j = 0;
		int incrementRow = deltaRow/Math.abs(deltaRow);
		int incrementCol = deltaCol/Math.abs(deltaCol);
		
		if (Math.abs(deltaRow) == Math.abs(deltaCol)){
			// check for obstacles in the way
			for (i= row + incrementRow, j = col + incrementCol; i != newRow; i += incrementRow, j += incrementCol){
				if (getBoardPieces()[i][j] != null){ // obstacle detected
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}

	@Override
	public ArrayList<Integer[]> getDangerZone() {
		ArrayList<Integer[]> dangerZone = new ArrayList<Integer []>();
		
		// check four diagonals
		for (int i = row+1, j = col+1; i < MainActivity.NUM_ROWS && j < MainActivity.NUM_COLS; i++, j++){
			if (getBoardPieces()[i][j] == null){
				Integer[] temp = {i,j};
				dangerZone.add(temp);
			}else if (getBoardPieces()[i][j].getColor() != color){
				Integer[] temp = {i,j};
				dangerZone.add(temp);
				break;
			}else{
				break;
			}
		}
		
		for (int i = row+1, j = col-1; i < MainActivity.NUM_ROWS && j >= 0; i++, j--){
			if (getBoardPieces()[i][j] == null){
				Integer[] temp = {i,j};
				dangerZone.add(temp);
			}else if (getBoardPieces()[i][j].getColor() != color){
				Integer[] temp = {i,j};
				dangerZone.add(temp);
				break;
			}else{
				break;
			}
		}
		
		for (int i = row-1, j = col+1; i >= 0 && j < MainActivity.NUM_COLS; i--, j++){
			if (getBoardPieces()[i][j] == null){
				Integer[] temp = {i,j};
				dangerZone.add(temp);
			}else if (getBoardPieces()[i][j].getColor() != color){
				Integer[] temp = {i,j};
				dangerZone.add(temp);
				break;
			}else{
				break;
			}
		}
		for (int i = row-1, j = col-1; i >= 0 && j >= 0; i--, j--){
			if (getBoardPieces()[i][j] == null){
				Integer[] temp = {i,j};
				dangerZone.add(temp);
			}else if (getBoardPieces()[i][j].getColor() != color){
				Integer[] temp = {i,j};
				dangerZone.add(temp);
				break;
			}else{
				break;
			}
		}
		
		return dangerZone;
	}
}
