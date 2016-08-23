package com.example.mychessgame;

import java.util.ArrayList;

import com.example.mychessgame.VirtualBoard.Piece;

public class Knight extends Piece{
	

	
	public Knight(Color _color, int _row, int _col) {
		super(_color, _row, _col, Type.KNIGHT);
	}
	
	public Knight(Knight p){
		super(p);
	}

	@Override
	/*
	 * int coordinates representing the tile to be considered. Values between 0 and 7
	 * (non-Javadoc)
	 * @see com.example.mychessgame.Piece#validateMove(int, int)
	 */
	public boolean validateMove(int x, int y) {
		
		// delta 2 in the x, delta 1 in the y
		if (Math.abs(x - row) == 2 && Math.abs(y - col) == 1){
			return true;
		}else if (Math.abs(x - row) == 1 && Math.abs(y - col) == 2){
			return true;
		}else{ // not an L shape
			return false;
		}
	}

	@Override
	// errors in implementation
	// doesnt have to be nulll... for the knight, any squares it can move to
	public ArrayList<Integer[]> getDangerZone() {
		
		ArrayList<Integer[]> dangerZone = new ArrayList<Integer []>();
		
		for (int currentRow = 0; currentRow < 8; currentRow++){
			for (int currentCol = 0; currentCol < 8; currentCol++){
				
				int deltaRow = currentRow - row;
				int deltaCol = currentCol - col;
				
				if (Math.abs(deltaRow) + Math.abs(deltaCol) == 3
						&& (Math.abs(deltaRow) == 2 || Math.abs(deltaCol) == 2)){
				
					
						Integer [] temp = {currentRow, currentCol};
						dangerZone.add(temp);
					
				}
			}
		}
		return dangerZone;
	}
}
