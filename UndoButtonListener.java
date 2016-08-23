package com.example.mychessgame;

import android.view.View;
import android.view.View.OnClickListener;

/*
 * Design decisions: Are the users allowed to chain undos? Or is only the most recent one
 * available for undo. How do we want to store the previous state of the board?
 * 
 */
public class UndoButtonListener implements OnClickListener {
	
	private VirtualBoard vb;
	
	public UndoButtonListener(VirtualBoard _vb){
		vb = _vb;
	}
	@Override
	public void onClick(View v) {
		
		// reset the virtual board and update the GUI
		vb.undo();
	}

}
