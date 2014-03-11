package jp.mmitti.sansan.common;

import java.util.LinkedList;
import java.util.List;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MyRadioGroup implements OnCheckedChangeListener{

	private List<CompoundButton> mRadioButtons;
	private OnRadioCheckedListner mListner;
	public MyRadioGroup(){
		mRadioButtons = new LinkedList<CompoundButton>();
	}
	
	public void addRadio(CompoundButton c){
		mRadioButtons.add(c);
		c.setOnCheckedChangeListener(this);
	}
	
	public void setListner(OnRadioCheckedListner l){
		mListner = l;
		
	}
	
	public void checkByID(int id){
		for(CompoundButton b : mRadioButtons){
			b.setChecked(b.getId() == id);
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
		if(isChecked){
			for(CompoundButton b : mRadioButtons){
				if(b != buttonView)	b.setChecked(false);
			}

			mListner.onChecked(buttonView);
		}
	}
	
	public interface OnRadioCheckedListner{
		void onChecked(CompoundButton c);
	}

}
