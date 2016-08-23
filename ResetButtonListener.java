package com.example.mychessgame;

import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

public class ResetButtonListener  implements OnClickListener{

	private VirtualBoard vb;
	
	public ResetButtonListener(VirtualBoard _vb){
		vb = _vb;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		vb.confirmReset(v);
	}
	
	  

}
