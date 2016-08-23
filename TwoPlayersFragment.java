package com.example.mychessgame;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.example.mychessgame.VirtualBoard.Piece;
import com.example.mychessgame.VirtualBoard.Piece.Color;
import com.example.mychessgame.VirtualBoard.Piece.Type;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class TwoPlayersFragment extends GameFragment{
	

	public void onCreate(Bundle b){
		super.onCreate(b);				
	}

	public void onStart(){
		super.onStart();
		virtualBoard.setGameMode(false);
		layout.setOnTouchListener(new OnImageTouchListener(virtualBoard));
		
	}
	
	public void onStop(){
		super.onStop();
		Log.e("DEBUG", "On stop");
	}
	
	public void onDestroyView(){
		// called by adding it to the backstack...so literally when the frag is not showing
		// the information about the position of the pieces is lost here apparently
		super.onDestroyView();
		Log.e("DEBUG", "Destroy View");
		
	}
	
	public void onSaveInstanceState(Bundle b){
		// this is called when the app is killed
		// called when I close the app using the home button followed by onStop
		Log.e("DEBUG", "onSaveInstanceState called from TwoPlayersFragment");
		super.onSaveInstanceState(b);
	}
	
	

}
