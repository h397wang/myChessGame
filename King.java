package com.example.mychessgame;

import java.util.ArrayList;

import android.util.Log;

import com.example.mychessgame.VirtualBoard.Piece;

public class King extends Piece{
	
	private boolean hasMoved = false;
	//private boolean inCheck = false;
	
	public King(Color _color, int _row, int _col) {
		super(_color, _row, _col, Type.KING);
	}
	
	public King(King p){
		super(p);	
		hasMoved = p.hasMoved;
	}
	
	// for resuming the game
	public King(int _row, int _col, Color _color, boolean _hasMoved){
		super(_color, _row, _col, Type.KING);
		hasMoved = _hasMoved;
		
	}
	public boolean hasMoved(){
		return hasMoved;
	}
	
	@Override
	public boolean validateMove(int newRow, int newCol) {
		
		int deltaRow = newRow - row;
		int deltaCol = newCol - col;
		
		if (deltaRow == 0 && deltaCol == 0){ // selecting itself is obvs not valid
			return false;
		}else if (isInCheck(newRow, newCol)){ // this position puts the king in check
			return false;
		}else if (Math.abs(newRow - row) <= 1 && Math.abs(newCol - col) <= 1){
				if (getBoardPieces()[newRow][newCol] == null){ // empty space is valid
					hasMoved = true;
					return true;
				}else if (getBoardPieces()[newRow][newCol].getColor() != color){
					hasMoved = true;
					return true;
				}else{// prevent the king from consuming his own pieces
					return false; 
				}
		}else if (Math.abs(deltaCol) == 2){
			
			if (canCastle(newRow, newCol)){
				castle(newRow, newCol);		
				hasMoved = true;
				return true;
			}else{
				return false;
			}

		}else{
			return false;
		}
	}
	
	/*
	 * Input: the position of the tile that the kng has selected
	 * The position of the king and the rook need to be adjusted on the game board
	 * To castle either select the or the tile...
	 */
	public void castle(int newRow, int newCol){
		int deltaCol = newCol - col; // +- 2
		int direction = deltaCol / Math.abs(deltaCol); // +- 1
		
		// update the position of the rook, as well as the virtual board
		// get the correct rook
		int rookCol = (direction < 0 ? 0:7);
		Rook rook = (Rook) getBoardPieces()[newRow][rookCol];
		rook.col = newCol - direction; // only the col needs updating
		
		// update the board
		getBoardPieces()[newRow][rookCol] = null;
		getBoardPieces()[newRow][rook.col] = rook;
		
		// the position of the king is already taken care of 
	}
	
	/*
	 * Input, position that the king is to end up
	 * Called at any attempt to castle as well as from the isCheckMate function
	 */
	public boolean canCastle(int newRow, int newCol){
		
		if (hasMoved){
			return false;
		}
		
		int deltaCol = newCol - col;
		int direction = deltaCol / Math.abs(deltaCol); // +- 1
		int rookCol = (direction < 0 ? 0:7);
		
		if (getBoardPieces()[newRow][rookCol].type == Piece.Type.ROOK // check for rook
				&& !((Rook) getBoardPieces()[newRow][rookCol]).hasMoved()){ // which has not moved
				
			// spaces in between are unoccupied and that they are not positions of check
			// there are actually only two spaces to be checked

			if (getBoardPieces()[row][col + direction] == null
					&& getBoardPieces()[row][col + 2*direction] == null
					&& !isInCheck(row, col + direction)
					&& !isInCheck(row, col + 2*direction)){ // if it's occupied
					return true;
			}else{ // unoccupied, so check if it's a position that puts the king in check
				return false;
			}
						
		}
		
		// check if an ally piece can capture the enemy piece...
		
		// check if other pieces can cover the position
		
		// btw consider putting in a timer?
		
		return true;
	}
	
	/*
	 * This function is to be called every time an enemy piece is moved, before moveCounter is incremented?
	 * to see whether or not the king is put in check. Also called from canCastle()
	 * Input: position to be checked, not redudant
	 * Output: returns true if the 
	 * Implementation: We have to check the dangerZone of every single enemy piece, because
	 * the piece that was just move could be cause it's ally to threaten the king
	 */
	public boolean isInCheck(int theRow, int theCol){
		// first get a reference to all enemy pieces
		ArrayList<Piece> enemyPieces = getEnemyPieces();
		// and check if the king currently resides in their dangerZones
		for (int i = 0; i < enemyPieces.size(); i++){
			ArrayList<Integer[]> dangerZone = enemyPieces.get(i).getDangerZone();
			
			if (dangerZone != null){
				for (int j = 0; j < dangerZone.size(); j++){
					Integer [] pos = dangerZone.get(j);
					if (pos[0] == theRow && pos[1] == theCol){ // the king is in check by this piece
					
						return true;
					}
				}
			}
		}	
		return false;
	}
	
	public boolean isInCheck(){
		ArrayList<Piece> enemyPieces = getEnemyPieces();
		// and check if the king currently resides in their dangerZones
		String log4 = String.format("Number of enemies %d" , enemyPieces.size());
		Log.w("CHECK", log4);
		
		for (int i = 0; i < enemyPieces.size(); i++){
			
			Piece enemy = enemyPieces.get(i);
			ArrayList<Integer[]> dangerZone = enemy.getDangerZone();
			
			String log3 = String.format("Position of enemy %d, %d" , enemy.getRow(), enemy.getCol());
			Log.w("CHECK", log3);
			
			if (dangerZone != null){
				
			String log = String.format("dangerZone size %d", dangerZone.size());
			Log.w("CHECK" , log);
			
			for (int j = 0; j < dangerZone.size(); j++){
				Integer [] pos = dangerZone.get(j);
							
				String log2 = String.format("dangerZone row: %d, col: %d", pos[0], pos[1]);
				Log.w("CHECK" , log2);
		
				if (pos[0] == row && pos[1] == col){ // the king is in check by this piece
					Log.w("CHECK", String.format("Check by this piece: row %d, col %d", enemy.getRow(), enemy.getCol()));
					return true;
				}
			}
			}
		}	
		//Log.w("CHECK", "NOT CHECK");
		return false;
	}
	
	
	private ArrayList<Piece> getThreats(){
		ArrayList<Piece> threats = new ArrayList<Piece>();
		ArrayList<Piece> enemies = getEnemyPieces();
		for (int i = 0; i < enemies.size(); i++){
			ArrayList<Integer[]> dangerZone = enemies.get(i).getDangerZone();
			
			if (dangerZone != null){
				for (int j = 0; j < dangerZone.size(); j++){
					Integer [] pos = dangerZone.get(j);
					if (pos[0] == row && pos[1] == col){ // the king is in check by this piece
						threats.add(enemies.get(i));
				
					}
				}
			}
		}
		return threats;
	}
	
	
	private ArrayList<Piece> getAllyPieces(){
		ArrayList<Piece> allyPieces = new ArrayList<Piece>();
		
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (getBoardPieces()[i][j] != null && getBoardPieces()[i][j].getColor() == color){
					allyPieces.add(getBoardPieces()[i][j]);
				}
			}
		}
		
		return allyPieces;
	}
	
	private ArrayList<Piece> getEnemyPieces(){
		ArrayList<Piece> enemyPieces = new ArrayList<Piece>();
		
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (getBoardPieces()[i][j] != null && getBoardPieces()[i][j].getColor() != color){
					enemyPieces.add(getBoardPieces()[i][j]);
				}
			}
		}
		return enemyPieces;
	}
	@Override
	public ArrayList<Integer[]> getDangerZone() {

		ArrayList<Integer[]> dangerZone = new ArrayList<Integer []>();
		
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				int deltaRow = i - row;
				int deltaCol = j - col;
				if ((Math.abs(deltaRow) == 0 && Math.abs(deltaCol) == 1)
						|| (Math.abs(deltaRow) == 1 && Math.abs(deltaCol) == 0)
						|| Math.abs(deltaCol) == 1 && Math.abs(deltaRow) == 1){
					// that's all that's needed
					Integer [] temp = {i,j};
					dangerZone.add(temp);
				}
			}
		}
		
		return dangerZone;
	}
	
		
	/*
	 * 
	 * Called only if the king is already in check...
	 * Check for all possible moves the king can make including castling
	 * Check if any pieces can capture the threat, if it is single, provided that 
	 * moving the ally piece does not expose the king.
	 * Check if any piece can block the threat..
	 */
	 public boolean isCheckMate(){
		 // check all available positions to move to...use the kings member function
		 // 8 spaces to check
		 Piece[][] board = getBoardPieces();
		 
		 
		 // check for all possible moves... the king can make...
		 for (int i = row - 1; i < row + 1; i++){
			 for (int j = col - 1; j < col + 1; j++){
				 if (i >= 0 && i <= 7&& j >= 0 && j <= 7){
					 if (board[i][j] == null){
						 if (!isInCheck(i,j)){ // not in check at this position
							 return false; // not mate
						 }
					 }else if (board[i][j].getColor() != getColor()){
						 if (!isInCheck(i, j)){ // capable of consuming another piece to get here
							 return false; // not mate
						 }
					 }
				 }
			 }
		 }
		 
		 Log.e("MATE", "cannot move anywhere");
		 // check if castling would suffice..
		 if (!hasMoved){ // check for castling...
			 if (canCastle(row, col+2) || canCastle(row, col - 2)){ 
				 return false; // it has castling options...
			 }
		 }
		 
		 Log.e("MATE", "no castling options");
		 
		 // check if allies can capture the single piece threatening the king
		 // you cant capture 2 pieces at once so check if threat.size() == 1
		 
		 ArrayList<Piece> allies = getAllyPieces();
		 ArrayList<Piece> threats = getThreats();
		 
		 
		 if (threats.size() == 1){
			 Piece threat = threats.get(0);
			 for (int i = 0; i < allies.size(); i++){
				 // also need to check that the piece being moved doesnt expose the king...
				 
				 // get the dangerzones of the ally pieces and check if the threat
				 // falls into their danger zone
				 Piece ally = allies.get(i);
				 ArrayList<Integer []>dangerZone = ally.getDangerZone();
				 
				for (int j = 0; j < dangerZone.size(); j++){
					Integer [] pos = dangerZone.get(j);
					
					if (pos[0] == threat.row && pos[1] == threat.col){ 
						// this ally piece can capture the threat
						Log.e("MATE", String.format("rescue from ally at %d, %d", ally.getRow(), ally.getCol()));
						Log.e("MATE", String.format("position of threat %d, %d", threat.getRow(), threat.getCol()));
						
						return false; // hence not checkmate
					}
				}
			 }
			 
			 Log.e("MATE", "No ally pieces can capture the threat");
			 // check for blocks, block the threat
			 // you can only block the path of a queen, rook or bishop
			 ArrayList<Integer []> blockingPoints = new ArrayList<Integer []>();
			 if (threat.getType() == Type.BISHOP 
					 || threat.getType() == Type.ROOK
					 || threat.getType() == Type.QUEEN){
				// first get an arrayList of positions that represent possible blocking points
				int deltaRow = row - threat.getRow();
				int deltaCol = col - threat.getCol();
				
				int dirRow = 0;
				if (deltaRow != 0){
					dirRow = Math.abs(deltaRow)/deltaRow;
				}
				int dirCol = 0;
				if (deltaCol != 0){
					dirCol = Math.abs(deltaCol)/deltaCol;
				}
				
				// bishop characteristic...
				if (deltaRow != 0 && deltaCol != 0){
					for (int r = threat.getRow() + dirRow; r != row; r += dirRow){
						for (int c = threat.getCol() + dirRow; c != col; c += dirCol){
							Integer [] pos = new Integer[2];
							pos[0] = r;
							pos[1] = c;
							blockingPoints.add(pos);
						}
					}
				}
				
				// rook characteristics
				if (deltaRow == 0 && deltaCol != 0){
					for (int c = threat.getCol(); c != col; c += dirCol){
						Integer [] pos = new Integer[2];
						pos[0] = row;
						pos[1] = c;
						blockingPoints.add(pos);
					}
				}else if (deltaRow != 0 && deltaCol == 0){
					for (int r = threat.getRow(); r != row; r += dirRow){
						Integer [] pos = new Integer[2];
						pos[0] = r;
						pos[1] = col;
						blockingPoints.add(pos);
					}
				}
			 }	
				
				
			for (int i = 0; i < allies.size(); i++){
				 Piece ally = allies.get(i);
				 if (ally.getType() == Piece.Type.KING){ // king cant block for itself...
					 continue;
				 }
				 ArrayList<Integer []> dangerZone = ally.getDangerZone();
				 for (int z = 0; z < dangerZone.size(); z++){
					 for (int p = 0; p < blockingPoints.size(); p++){
						 if (dangerZone.get(z)[0] == blockingPoints.get(p)[0]
								 && dangerZone.get(z)[1] == blockingPoints.get(p)[1]){ 
							 // this ally can block the threat
							 Log.e("MATE", String.format("This ally can block %d, %d", ally.getRow(),ally.getCol()));
							 return false;
						 }
					 }
				 }
			}
			Log.e("MATE", "No ally pieces can block the threat");
			
		 }else if (threats.size() == 2){
			 // only option is to move the king
		 }
		 
		 
		 return true;
	 }
	 
	 
}
