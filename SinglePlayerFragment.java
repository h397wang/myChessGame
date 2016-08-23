package com.example.mychessgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/*
 * I might not even need the two gameFragments...
 * The virtualBoard can be created in OnCreate() and its mode can be set...where
 * the game fragments are created from the mainActivity...so maybe a flag can be sent in thrugh there
 * 
 * 
 */
public class SinglePlayerFragment extends GameFragment{
	
	public void onStart(){
		super.onStart();
		virtualBoard.setGameMode(true);
		layout.setOnTouchListener(new OnImageTouchListener(virtualBoard));
	}
	
}
