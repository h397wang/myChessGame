package com.example.mychessgame;

import java.util.ArrayList;

import com.example.mychessgame.VirtualBoard.Piece;

public class Queen extends Piece{
		
	public Queen(Color _color, int _row, int _col) {
		super(_color, _row, _col, Type.QUEEN);
	}
	
	public Queen(Queen p){
		super(p);	
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
			return true;
		}else if (deltaRow == 0 && deltaCol != 0){
			int incrementCol = deltaCol/Math.abs(deltaCol);
			for (int i = col +incrementCol; i != newCol; i += incrementCol){
				if (getBoardPieces()[row][i] != null){
					return false;
				}
			}
			return true;
		}else if (Math.abs(deltaRow) == Math.abs(deltaCol)){
			// check for obstacles in the way
			
			int incrementRow = deltaRow/Math.abs(deltaRow);
			int incrementCol = deltaCol/Math.abs(deltaCol);
			
			if (Math.abs(deltaRow) == Math.abs(deltaCol)){
				// check for obstacles in the way
				for (int i= row + incrementRow, j = col + incrementCol; i != newRow; i += incrementRow, j += incrementCol){
					if (getBoardPieces()[i][j] != null){ // obstacle detected
						return false;
					}
				}
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
		
	}

	@Override
	public ArrayList<Integer[]> getDangerZone() {
		
		ArrayList<Integer[]> dangerZone = new ArrayList<Integer []>();
		
		// rook aspect 
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
		
		// bishop aspect
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
