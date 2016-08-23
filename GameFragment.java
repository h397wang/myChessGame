package com.example.mychessgame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.example.mychessgame.VirtualBoard.Piece;
import com.example.mychessgame.VirtualBoard.Piece.Color;
import com.example.mychessgame.VirtualBoard.Piece.Type;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GameFragment extends Fragment{
	
	VirtualBoard virtualBoard;
	LinearLayout layout;
	boolean isSingleMode;
	
	public static final GameFragment newInstance(boolean isSingleMode){
			GameFragment f = new GameFragment();
			Bundle b = new Bundle();
			b.putBoolean("isSingleMode", isSingleMode);
			f.setArguments(b);
			return f;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle b) {
		
		// not sure ifthis is required
		//super.onCreateView(inflater, container, b); 
		Log.e("DEBUG", "OnCreateView called ");
		
		// sets this flag, it's final
		isSingleMode = getArguments().getBoolean("isSingleMode");
		Log.e("DEBUG", "fetched game Mode: " + Boolean.toString(isSingleMode));
		
		
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        
        layout = (LinearLayout)rootView.findViewById(R.id.layout1);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout [] rows = new LinearLayout[MainActivity.NUM_ROWS];
        ImageView [][] tiles = new ImageView[MainActivity.NUM_ROWS][MainActivity.NUM_COLS];
         
        virtualBoard = drawNewBoard(rows, tiles, layout, rootView);
        
        // moved to onStart() to differentiate between the two playing modes
        virtualBoard.setGameMode(isSingleMode);
        layout.setOnTouchListener(new OnImageTouchListener(virtualBoard));
        
		Button undoButton = new Button(rootView.getContext());
		undoButton.setText("UNDO");// UNDO
		layout.addView(undoButton);
		undoButton.setOnClickListener(new UndoButtonListener(virtualBoard));
         
		Button resetButton = new Button(rootView.getContext());
		resetButton.setText("RESET");
		layout.addView(resetButton);
		resetButton.setOnClickListener(new ResetButtonListener(virtualBoard));
			
        return rootView;
    }
	
	
	
	public void onResume(){
		// seems to be called when the app has been killed and then restored
		// as in tapping on the app icon on the screen
		super.onResume();
		Log.e("DEBUG", "GameFragment has been resumed");
		
		
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		//editor.clear();
		
		
		if (sharedPref.getBoolean(Boolean.toString(isSingleMode) + "_" + "hasPaused", false) == false){
			Log.e("DEBUG", "nothing saved, default new game");
 
			return;
		}
		
		Piece [][] resumedBoard = new Piece[8][8];
		// get values stored in shared preferences...
		
		// get the gameMode, this should already be defined at this point...
		//isSingleMode = getArguments().getBoolean("isSingleMode");
			
		// get the current board, depending on which gameMode it's in
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				String key = Boolean.toString(isSingleMode) + "_" + Integer.toString(i) + "_" +  Integer.toString(j); // row_col
				String value =  sharedPref.getString(key, "null");
				editor.remove(key);
				
				if (value != "null"){ // not a blank space
					// parse the string
					Log.e("DEBUG", "value" + value);
					String [] info = value.split("_");
					Piece.Color color = Color.valueOf(info[0]);
					Piece.Type type = Type.valueOf(info[1]);
					if (type == Type.PAWN){
						int enPassant = Integer.valueOf(info[2]);
						Pawn pawn = new Pawn(i,j,color,enPassant);
						resumedBoard[i][j] = pawn;
					}else if (type == Type.KING){
						boolean hasMoved = Boolean.valueOf(info[2]);
						King king = new King(i,j,color,hasMoved);
						resumedBoard[i][j] = king;
					}else{
						resumedBoard[i][j] = PieceFactory.revivePiece(i, j, color, type);
					}			
				}
			}
		}
		
		// get the previous game states so that undos can be done
		// we could move this into methods other than reusme and pause for efficiency
		ArrayList<Piece [][]> previousStates = new ArrayList<Piece [][]>();
		// this is all being skipped...because its currently returning 0
		Log.e("DEBUG", Integer.toString(sharedPref.getInt(Boolean.toString(isSingleMode) + "_" + "stackSize", 0)));
		
		for (int stackNum = 0; stackNum < sharedPref.getInt(Boolean.toString(isSingleMode) + "_" + "stackSize", 0); stackNum++){
			
			Piece [][] tempBoard = new Piece[8][8];
			
			for (int i = 0; i < 8; i++){
				for (int j = 0; j < 8; j++){
					String key = Integer.toString(stackNum) + "_" + Boolean.toString(isSingleMode) 
							+ "_" + Integer.toString(i) + "_" +  Integer.toString(j); // row_col
					String value =  sharedPref.getString(key, "null");
					// not fetching anything
				
					
					if (value != "null"){ // not a blank space
						
						// parse the string
						Log.e("DEBUG previous", "value" + value);
						String [] info = value.split("_");
						Piece.Color color = Color.valueOf(info[0]);
						Piece.Type type = Type.valueOf(info[1]);
						if (type == Type.PAWN){
							int enPassant = Integer.valueOf(info[2]);
							Pawn pawn = new Pawn(i,j,color,enPassant);
							tempBoard[i][j] = pawn;
						}else if (type == Type.KING){
							boolean hasMoved = Boolean.valueOf(info[2]);
							King king = new King(i,j,color,hasMoved);
							tempBoard[i][j] = king;
						}else{
							tempBoard[i][j] = PieceFactory.revivePiece(i, j, color, type);
						}			
					}
					
					editor.remove(key);
					
				}
			}
			
			previousStates.add(tempBoard);
		}
		
		Log.e("DEBUG", "size of stack: " + Integer.toString(previousStates.size()));
		
		// set the moveCounter here I guess, no need to clear this..
		String key = Boolean.toString(isSingleMode) + "_" + "moveCounter";
		int moveCounter = sharedPref.getInt(key, 0);
		//Log.e("DEBUG", "moveCounter" + Integer.toString(moveCounter));
		virtualBoard.resumeGame(resumedBoard, moveCounter, previousStates);
	}
	
	
	// what's happenning right now is that both versions load the same game..
	// two versions of the game would have to be stored
	// so these functions might need to be pushed down...
	// such that a flag is appended to the keys to indicate which game mode we are in
	// unfortunately there's going to be a lot of copy pasta...
	public void onPause(){
		//called when I turn off my phone while the fragment is active..
		// this is the best place, because the other methods arent garanteed to be called
		super.onPause();
		Log.e("DEBUG", "onPause has been called");
			
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear(); //clearing the data of the othe game mode
		
		
		editor.putBoolean(Boolean.toString(isSingleMode) +  "_" +  "hasPaused", true);
		
		/*
		 * this is how it's all encoded...
		 * "isSingleMode_row_col": _color_piece_enPassant or hasMoved
		 * 
		 */
		for (int i = 0; i < 8; i ++){
			for (int j = 0; j < 8; j++){
				Piece piece = virtualBoard.getBoardArray()[i][j];
				if (piece != null){

					String key = Boolean.toString(isSingleMode) + "_" + Integer.toString(i) + "_" + Integer.toString(j); // row_col
					String value = piece.getColor().name() + "_" +  piece.getType().name(); // add the piece type
					
					if (piece.getType() == Piece.Type.PAWN){
						Pawn pawn = (Pawn) piece;
						value += "_" + Integer.toString(pawn.enPassantAvailable);
						
					}else if (piece.getType() == Piece.Type.KING){
						King king = (King)piece;
						value += "_" +  String.valueOf(king.hasMoved()); // "true" or "false"
					}
					
					editor.putString(key, value);
				}
			}
		}
		
		

		
		
		// key: stackNum_isSingleMode_row_col
		// value: color_type_*
		// we also have to store the previous moves...so undos can be done
		
		ArrayList<Piece [][]> theStack = virtualBoard.getGameStack();
		editor.putInt(Boolean.toString(isSingleMode) + "_" + "stackSize", theStack.size());
		
		for (int stackNum = 0; stackNum < theStack.size(); stackNum++){
			Piece [][] currentBoard = theStack.get(stackNum);
			
			for (int i = 0; i < 8; i ++){
				for (int j = 0; j < 8; j++){
					Piece piece = currentBoard[i][j];
					if (piece != null){
						
						// key: stackNum_gameMode_row_col
						// value: color_type_*
						
						String key = Integer.toString(stackNum) + "_" + Boolean.toString(isSingleMode) + "_" + 
									Integer.toString(i) + "_" + Integer.toString(j); 
						String value = piece.getColor().name() + "_" +  piece.getType().name(); // add the piece type
						
						if (piece.getType() == Piece.Type.PAWN){
							Pawn pawn = (Pawn) piece;
							value += "_" + Integer.toString(pawn.enPassantAvailable);
							
						}else if (piece.getType() == Piece.Type.KING){
							King king = (King)piece;
							value += "_" +  String.valueOf(king.hasMoved()); // "true" or "false"
						}
						
						editor.putString(key, value);
						
					}
					
					
				}
			}
		}
		
		// integer for the moveCounter must be stored: 
		// key: "moveCounter" 
		// value (int): moveCounter
		String key = Boolean.toString(isSingleMode) + "_" + "moveCounter"; 
		editor.putInt(key, virtualBoard.getMoveCounter());
		
		
		editor.commit();
		
	}
	
	 
	 
	 /**
      * 
      * Called at the beginning of the game or upon reset
      * Draws all pieces in their starting positions
      * @param rows: Array of LinearLayout, each represents a row
      * @param tiles: 8 x 8 array of ImageView objects, each displaying the blank space or image of a Piece
      * Returns a reference to a new VirtualBoard object
      */
     private VirtualBoard drawNewBoard(LinearLayout [] rows, ImageView [][] tiles, LinearLayout layout, View rootView){
     	
     	VirtualBoard board = new VirtualBoard(rootView.getContext(), tiles);
     	int pngId;
     	
     	for (int i = 0; i < MainActivity.NUM_ROWS; i++){
     		rows[i] = new LinearLayout(rootView.getContext());       		
         	rows[i].setOrientation(LinearLayout.HORIZONTAL);
         	layout.addView(rows[i]);
         	
         	for (int j = 0; j < MainActivity.NUM_COLS; j++){
         		
         		pngId = getPngId(i,j);
         		tiles[i][j] = new ImageView(rootView.getContext());
         		tiles[i][j].setImageResource(pngId); // get the name right
         		tiles[i][j].setTag(pngId);
         		LayoutParams params = new LayoutParams(MainActivity.SQUARE_SIZE, MainActivity.SQUARE_SIZE);
         		tiles[i][j].setLayoutParams(params);
         		tiles[i][j].setScaleType(ImageView.ScaleType.FIT_XY);
         		tiles[i][j].setAdjustViewBounds(false);
         		rows[i].addView(tiles[i][j]);	
         	}      	
     	}      	
     	return board;
     }   
     
     
     /**
      * Called by drawNewBoard when setting up a game
      * Every imageView has a tag associated with it to store the id of the appropriate image
      * Used when we create a new game, returns the png ids according to the start position of pieces
      * @param row
      * @param col
      * @return the png id
      * this will be revamped soon..
      */
     private int getPngId(int row, int col){
     	
     	if (row == 1){ // black pawn
     		if (col%2 == 0){ // on black tile
     			return R.drawable.black_pawn_black;
     		}else{
     			return R.drawable.black_pawn_white;
     		}
     	}else if (row == 6){
     		if (col%2 ==0){
     			return R.drawable.white_pawn_white;
     		}else{
     			return R.drawable.white_pawn_black;
     		}
     	}
     	
     	if (row == 0){
     		switch(col){
     		case 0:
     			return R.drawable.black_rook_white;
     		case 1:
     			return R.drawable.black_knight_black;
     		case 2:
     			return R.drawable.black_bishop_white;
     		case 3:
     			return R.drawable.black_queen_black;
     		case 4:
     			return R.drawable.black_king_white;
     		case 5:
     			return R.drawable.black_bishop_black;
     		case 6:
     			return R.drawable.black_knight_white;
     		case 7:
     			return R.drawable.black_rook_black;        			
     		}
     	}else if (row == 7){
     		switch(col){
     		case 0:
     			return R.drawable.white_rook_black;
     		case 1:
     			return R.drawable.white_knight_white;
     		case 2:
     			return R.drawable.white_bishop_black;
     		case 3:
     			return R.drawable.white_queen_white;
     		case 4:
     			return R.drawable.white_king_black;
     		case 5:
     			return R.drawable.white_bishop_white;
     		case 6:
     			return R.drawable.white_knight_black;
     		case 7:
     			return R.drawable.white_rook_white;        			
     		}
     	}
     	
     	// must be a blank spot
     		
     	if ((row+col)%2 == 1){ // then it's a black tile
     		return R.drawable.blank_black;
     	}else{ // then it's a white tile
     		return R.drawable.blank_white;
     	}
     }
}
