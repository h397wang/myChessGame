package com.example.mychessgame;

import com.example.mychessgame.VirtualBoard.Piece;
import com.example.mychessgame.VirtualBoard.Piece.Color;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class OnImageTouchListener implements OnTouchListener{
	
	private VirtualBoard vb;
	
	public OnImageTouchListener(VirtualBoard _vb){
		vb = _vb;
	}
	
	
	@Override
	/*
	 * The return value doesn't actually do anything..
	 * Calls selectTile() from the vb for the row and col of the tile selected
	 */
	public boolean onTouch(View v, MotionEvent me) {
		
		float x = 0, y = 0;
		int row = 0, col = 0;
		
		x = me.getX() - MainActivity.OFFSET;
		y = me.getY() - MainActivity.OFFSET;
		
		row = (int) (y/MainActivity.SQUARE_SIZE);
		col = (int) (x/MainActivity.SQUARE_SIZE);
		
		// temporary cap 
		if (row >= 8){
			row = 7;
		}
		if (col >= 8){
			col = 7;
		}
		
		vb.selectTile(row, col);
		
		// check if its the AI's turn, if so then calculate it's moves 
		// and make two selections... the AI will be player two, or black
		// Actually we can probably condense these back into one class
		// if we take advantage of the gameMode flag in virtual board...
		if (vb.isSingleMode()){
			//Log.e("DEBUG", "Single player mode");
			if (vb.getTurn() == Color.BLACK){
				//Log.e("DEBUG", "AI's turn");
			}
		}else{
			//Log.e("DEBUG", "two player mode");
		}
		
		return false;
	}
	
}