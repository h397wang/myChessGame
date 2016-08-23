package com.example.mychessgame;

import java.util.ArrayList;
import java.util.Stack;








import com.example.mychessgame.VirtualBoard.Piece.Color;
import com.example.mychessgame.VirtualBoard.Piece.Type;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class VirtualBoard {
	
	private boolean isSingleMode = false;
	private Context context;
	final int imgResourceEnum = 16;
	
	// these two member variables needed to be static so that they could be returned..
	// holds a reference to a Piece object, null for an empty space
	protected static Piece [][] board = new Piece[MainActivity.NUM_ROWS][MainActivity.NUM_COLS];
	protected static int moveCounter = 0; // white goes first, so even is white, odd is black
	
	private ImageView tiles[][];

	private ArrayList<Piece [][]> previousBoardStates = new ArrayList<Piece [][]>(); 
	private boolean isPieceSelected = false;
	private Piece pieceSelected = null;
	int rowSelected = 0; int colSelected = 0;
	
	King whiteKing = null; King blackKing = null;
	
	// must be null or it crashes, green for selected piece, red for king in check
	private ImageView redTile = null; 
	private ImageView greenTile = null;
	
	public VirtualBoard(Context _context, ImageView _tiles[][]){
		context = _context;
		tiles = _tiles;
		newGame();
	}
	
	
	public Piece[][] getBoardArray(){
		return board;
	}
	
	
	public boolean isSingleMode(){
		return isSingleMode;
	}
	
	public void setGameMode(boolean isSingle){
		isSingleMode = isSingle; 
	}
	
	
	public int getMoveCounter(){
		return moveCounter;
	}
	
	public ArrayList<Piece [][]> getGameStack(){
		return previousBoardStates;
	}
	
	public Color getTurn(){
		if (moveCounter%2 == 0){
			return Color.WHITE;
		}else{
			return Color.BLACK;
		}
	}
	public Color getPreviousTurn(){
		if (moveCounter%2 == 1){
			return Color.WHITE;
		}else{
			return Color.BLACK;
		}
	}
	
	/*
	 * Called from onResume() of the GameFragment 
	 * Inputs: 
	 * Piece [][] theBoard: Array of pieces representing the state of the board to be restored
	 * int _moveCounter: counter indicating player turns
	 * Copy the pieces from the input board to the current board
	 * Call updateBoard() after
	 */
	public void resumeGame(Piece [][] theBoard, int _moveCounter, ArrayList<Piece [][]> previous){
	
		moveCounter = _moveCounter;
	
		
		for (int i = 0; i < MainActivity.NUM_ROWS; i++){
			for (int j = 0; j < MainActivity.NUM_COLS; j++){ 
				board[i][j] = PieceFactory.copyPiece(theBoard[i][j]); // populate the board
				if (board[i][j] != null){
					
					// get reference to the kings
					if (board[i][j].type == Piece.Type.KING){
						if (board[i][j].getColor() == Piece.Color.WHITE){		
							whiteKing = (King) board[i][j];
						}else{
							blackKing = (King) board[i][j];
						}
					}
				}
			}	
		}
		
		previousBoardStates.clear();
		for (int stackSize = 0; stackSize < previous.size(); stackSize++){
			Piece [][] temp = new Piece[8][8];
			
			for (int i = 0; i < MainActivity.NUM_ROWS; i++){
				for (int j = 0; j < MainActivity.NUM_COLS; j++){ 
					temp[i][j] = PieceFactory.copyPiece(previous.get(stackSize)[i][j]); // populate the board
					if (temp[i][j] != null){
					
					// get reference to the kings
						if (temp[i][j].type == Piece.Type.KING){
							if (temp[i][j].getColor() == Piece.Color.WHITE){		
								whiteKing = (King) temp[i][j];
							}else{
								blackKing = (King) temp[i][j];
							}
						}
					}
				}	
			}
			
			previousBoardStates.add(temp);
		}
		
		// this will take care of the tiles high lights i believe
		updateBoard();
		
	}
	
	
	// king that won 
	public void winner(King king){
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	      alertDialogBuilder.setMessage("Checkmate, the winner is : " + king.getColor().toString());
	      
	      alertDialogBuilder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	        	 	
	            newGame();
	            updateBoard();
	         }
	      });
	      
	      alertDialogBuilder.setNegativeButton("Okay",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	            // do nothing
	        	 
	         }
	      });
	      
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();	}
	
	public void confirmReset(View view){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	      alertDialogBuilder.setMessage("Are you sure you want to restart?");
	      
	      alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {

	            newGame();
	            updateBoard();
	         }
	      });
	      
	      alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	            // do nothing
	         }
	      });
	      
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
	   }
  
	 
	/*
	 * Called from confirmReset() to reset the game 
	 * Clear highlighted tiles and recreate the board of pieces
	 * Reset neccesary game variables
	 * 
	 */
	public void newGame(){
		
		if (redTile != null){
			redTile.clearColorFilter();
		}
		
		if (greenTile != null ){
			greenTile.clearColorFilter();
			greenTile = null;
		}
		
		// no need to clear the board because the factor will return null
		for (int i = 0; i < MainActivity.NUM_ROWS; i++){
			for (int j = 0; j < MainActivity.NUM_COLS; j++){ 
				board[i][j] = PieceFactory.createPiece(i,j); // populate the board
				if (board[i][j] != null){
					if (board[i][j].type == Piece.Type.KING){
						if (board[i][j].getColor() == Piece.Color.WHITE){		
							whiteKing = (King) board[i][j];
						}else{
							blackKing = (King) board[i][j];
						}
					}
				}
			}	
		}
		
		// reset neccesary game variables
		moveCounter = 0;
		isPieceSelected = false;
		
		// stack that keeps track of previous board states
		previousBoardStates.clear();
		
		// calling updateBoard() here fucks things up for some reason..

	}
	
	/*
	 * Called from the button listener
	 * May be called consecutively
	 * Reverts the state of the board backwards by one move by popping off the stack
	 * Copies the pieces from the buffer board to the current board
	 */
	public void undo(){
		
		if (moveCounter == 0){ // no moves to undo
			return;
		}
		
		// update the contents of the virtual board 
		if (!previousBoardStates.isEmpty()){
			int size = previousBoardStates.size();
			Piece [][] tempBoard = previousBoardStates.get(size-1);
			
			for (int i = 0; i < MainActivity.NUM_ROWS; i ++){
				for (int j = 0; j < MainActivity.NUM_COLS; j++){
					board[i][j] = PieceFactory.copyPiece(tempBoard[i][j]);
	
					// get reference to the kings
					if (board[i][j] != null && board[i][j].getType() == Piece.Type.KING){
						if (board[i][j].getColor() == Piece.Color.WHITE){		
							whiteKing = (King) board[i][j];
						}else{
							blackKing = (King) board[i][j];
						}
					}					
				}
			}
			// I need to update references for whiteKing and blackKing
			
			previousBoardStates.remove(size -1);
		}else{
			
		}
		// clear the red tile here?
		if (redTile != null){
			redTile.clearColorFilter();
		}
		// update the GUI 
		updateBoard();
		moveCounter--;
	}
	
	
	/*
	 * Update the GUI based on the contents of the virtual board
	 * Highlights the King's tile if it is in check, or unhighlights it
	 */
	public void updateBoard(){
		for (int row = 0; row < MainActivity.NUM_ROWS; row++){
			for (int col = 0; col < MainActivity.NUM_COLS; col++){
				if (board[row][col] == null){ // blank space
					if ((row + col) % 2 == 0){ // white space
						tiles[row][col].setImageResource(R.drawable.blank_white);
					}else{ //black space
						tiles[row][col].setImageResource(R.drawable.blank_black);
					}
				}else{ // not a blank space
					Piece piece = board[row][col];
					int pngId = R.drawable.blank_black; // initialized to a valid value
					
					if (piece.getType() == Type.PAWN){
						pngId = R.drawable.white_pawn_white;
					}else if (piece.getType() == Type.BISHOP){
						pngId = R.drawable.white_bishop_white;
					}else if (piece.getType() == Type.KNIGHT){
						pngId = R.drawable.white_knight_white;
					}else if (piece.getType() == Type.ROOK){
						pngId = R.drawable.white_rook_white;
					}else if (piece.getType() == Type.QUEEN){
						pngId = R.drawable.white_queen_white;
					}else if (piece.getType() == Type.KING){
						pngId = R.drawable.white_king_white;
					}
					if ((row + col) % 2 == 1){ // black background
						pngId -= 1; // based on enumeration of image resources
					}
					if (piece.getColor() == Color.BLACK){
						pngId -= imgResourceEnum; // based on enumeration of image resources
					}
					
					if (tiles[row][col] != null){
						tiles[row][col].setImageResource(pngId);
					}
				}
			}
		}
		// this has to go before the red, or else greenTile and redTile may hold the same reference
		// and the tile will not be red as required
		if (greenTile != null){
			greenTile.clearColorFilter();
		}
		
		// I guess this is where we check for checkmates...
		if (whiteKing.isInCheck()){
			redTile = tiles[whiteKing.getRow()][whiteKing.getCol()];
			redTile.setColorFilter(android.graphics.Color.RED, Mode.MULTIPLY);
			if (whiteKing.isCheckMate()){
				// end game
				// create a pop up dialog
				winner(blackKing);
			}
		}else if (blackKing.isInCheck()){
			redTile = tiles[blackKing.getRow()][blackKing.getCol()];
			redTile.setColorFilter(android.graphics.Color.RED, Mode.MULTIPLY);
			Log.e("wtf", "Black king should be red");
			if (blackKing.isCheckMate()){
				// end game
				winner(whiteKing);
			}
		}else{ // neither of them are in check
			if (redTile != null){
				redTile.clearColorFilter();
			}
		}	
		

	}
	
	
	/*
	 * Called from the imageTouchListener when a tile has been selected
	 * Input: row and col of the tile selected
	 * Responsible for the GUI, calls movePice to handle the contents of the virtual board
	 */
	public void selectTile(int row, int col){
		
		if (isPieceSelected == false){ 

			pieceSelected = board[row][col];
			
			if (pieceSelected != null){ // selected tile is not a blank space
	
				if (getTurn() == pieceSelected.getColor()){ // selecting your own piece
					
					// add a tint to the piece that was selected
					rowSelected = row; colSelected = col;
					greenTile = tiles[row][col];
					// update greenTile
					
					greenTile.setColorFilter(android.graphics.Color.GREEN, Mode.MULTIPLY);
					
					isPieceSelected = true;
					
				}else{ // selecting an opponent's piece				
					Toast.makeText(context, "THAT'S NOT YOUR PIECE", Toast.LENGTH_SHORT).show();					
				}
			}
		}else{ // a piece was selected already, so this an attempt at a placement
		
			if (movePiece(pieceSelected, row, col) == true){
				// 
							
			}else{ // invalid move, do nothing, restart the selection process?
				Toast.makeText(context, "INVALID MOVE", Toast.LENGTH_SHORT).show();
				// for whatever reasons...
			}
			
			greenTile.clearColorFilter();
			// this must be called AFTER the tint has been cleared?
			updateBoard();	
			isPieceSelected = false;
			
		}
		
	}
	
	
	/**
	 * Called from selectTile...
	 * Checks if it's a valid move based on the type of piece, by calling the piece's validate method
	 * Updates positions and variables if successful, otherwise do nothing and return false
	 * Updates the list of previous moves
	 * Increment the move counter
	 * @param pieceToBeMoved cannot be a null, check for blank space done prior
	 * @param newRow 
	 * @param newCol
	 * 
	 * Returns true for a successful move...
	 */
	public boolean movePiece(Piece pieceToBeMoved, int newRow, int newCol){
		
		int row = pieceToBeMoved.row;
		int col = pieceToBeMoved.col;
		
	
		// create a copy of the current board...and store it for undos
		Piece[][] tempBoard = new Piece[MainActivity.NUM_ROWS][MainActivity.NUM_COLS];
		
		for (int i = 0; i < MainActivity.NUM_ROWS; i ++){
			for (int j = 0; j < MainActivity.NUM_COLS; j++){
				tempBoard[i][j] = PieceFactory.copyPiece(board[i][j]);
			}
		}
		
		// leave this be...
		if(board[newRow][newCol] != null && board[newRow][newCol].getColor() == pieceToBeMoved.getColor()){ // cant consume your own piece...
			// check if it's the king, to do later
			if (pieceToBeMoved.type != Piece.Type.KING){ // check for castling
				Toast.makeText(context, "Invalid Move, can't consume your own", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		if (pieceToBeMoved.validateMove(newRow, newCol) == true){ // the move is valid for that one piece
		
			// under normal circumstances..
			board[newRow][newCol] = pieceToBeMoved; // update the array of pieces
			board[row][col] = null; // evacuate this space
			pieceToBeMoved.setNewPosition(newRow, newCol);
			
			moveCounter++;	// better to increment it here actually.
			previousBoardStates.add(tempBoard);// add onto the stack first before undo
			
			// something is wrong here...
			// if moving the piece exposes the king then we have to undo that and return false
			if (pieceToBeMoved.getColor() == Piece.Color.BLACK){
				if (pieceToBeMoved.getType() == Piece.Type.KING){
					blackKing = (King)pieceToBeMoved;	// i think im losing reference..
				} // so this has to be assinged
				
				if (blackKing.isInCheck()){
					undo();
					Toast.makeText(context, "This move exposes your king", Toast.LENGTH_SHORT).show();
					// todo: send a toast detailing exposure of king
					
					return false;
				}
			}else{
				if (pieceToBeMoved.getType() == Piece.Type.KING){
					whiteKing = (King)pieceToBeMoved;	// i think im losing reference..
				} // so this has to be assinged
				
				if (whiteKing.isInCheck()){
					
					undo();
					// todo: send a toast detailing exposure of king
					return false;
				}
			}
			
			// check for promotion
			int promotionRank = (pieceToBeMoved.getColor() == Color.BLACK ? 7:0);
			if (pieceToBeMoved.type == Type.PAWN && pieceToBeMoved.getRow() == promotionRank){
				promote(pieceToBeMoved);
			}
			
			
			
			return true;
		}else{ // the move was not valid for that piece
			return false;
		}		
	}
	
	/**
	 * 
	 * Called from movePiece()
	 * @param p Piece which is guaranteed to be a pawn
	 * Replaces the pawn with a queen, if it has reached its last rank
	 */
	 private void promote(Piece p){
		 int row = p.getRow();
		 int col = p.getCol();
		 
		 Color turn = p.getColor();
		 board[row][col] = new Queen(turn, row, col);
		 board[row][col].setNewPosition(row, col);
		 
	 }
	 
	/*
	 * Not sure whether or not making it a nested static class is right...
	 * inheritance didnt work as expected..
	 * 
	 */
	public static abstract class Piece {
		
		
		protected Type type;
		protected Color color; 
		protected int row, col;

		public abstract boolean validateMove(int row, int col);
		public abstract ArrayList<Integer []> getDangerZone();
		
		public Piece(Piece p){
			color = p.color;
			col = p.col;
			row = p.row;
			type = p.type;
		}
		
		public Piece(Color _color, int _row, int _col, Type _type){
			color = _color;
			col = _col;
			type = _type;
			row = _row;		
		}
		

		public enum Color{
			WHITE , BLACK
		}
		
		public enum Type{
			PAWN,ROOK,KNIGHT,BISHOP,QUEEN,KING
		}
		
		
		public Type getType(){
			return type;
		}
		public Color getColor() {
			return color;
		}

		public int getCol() {
			return col;
		}

		public int getRow() {
			return row;
		}


		public void setNewPosition(int newRow, int newCol) {
		
			row = newRow;
			col = newCol;
			
		}
		
		
		// only benefit of having piece as a nested class is so these two variables 
		// can be returned
		
		protected int getMoveCounter(){
			return moveCounter;
		}
		
		protected Piece[][] getBoardPieces(){
			return board;
		}
		
		
	}

}
