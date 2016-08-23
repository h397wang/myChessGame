package com.example.mychessgame;

import java.util.ArrayList;

import com.example.mychessgame.VirtualBoard.Piece;

public class Rook extends Piece{
	
	private boolean hasMoved = false;
	
	public Rook(Color _color,int _row, int _col){
		super(_color, _row, _col, Type.ROOK);

	}
	
	public Rook(Rook p){
		super(p);
		hasMoved = p.hasMoved;
	
	}
	@Override
	public boolean validateMove(int newRow, int newCol) {
		
		int deltaRow = newRow - row;
		int deltaCol = newCol - col;
		
		if (deltaRow != 0 && deltaCol == 0){
			int incrementRow = deltaRow/Math.abs(deltaRow);
			for (int i = row + incrementRow; i != newRow; i += incrementRow){
				if (getBoardPieces()[i][col] != null){
					return false;
				}
			}
			hasMoved = true;
			return true;
		}else if (deltaRow == 0 && deltaCol != 0){
			int incrementCol = deltaCol/Math.abs(deltaCol);
			for (int i = col +incrementCol; i != newCol; i += incrementCol){
				if (getBoardPieces()[row][i] != null){
					return false;
				}
			}
			hasMoved = true;
			return true;
		}else{
			return false;
		}
	}
	
	/*
	 * Called when everything has been validated
	 * Adjusts the position of the rook
	 * Input int deltaCol: difference in position, should be either 3 or 4
	 */
	public void castle(int deltaCol){
		if (Math.abs(deltaCol) == 3){
			
		}else if (Math.abs(deltaCol) == 4){
			
		}
	}
	
	public boolean hasMoved(){
		return hasMoved;
	}

	@Override
	public ArrayList<Integer[]> getDangerZone() {
		ArrayList<Integer[]> dangerZone = new ArrayList<Integer []>();
		
		
		for (int i = row+1, j = col; i < MainActivity.NUM_ROWS; i++){
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
		
		for (int i = row-1, j = col; i >= 0; i--){
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
		for (int i = row, j = col+1; j < MainActivity.NUM_COLS; j++){
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
		for (int i = row, j = col-1; j >= 0; j--){
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
