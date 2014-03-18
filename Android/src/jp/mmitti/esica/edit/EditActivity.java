package jp.mmitti.esica.edit;

import jp.mmitti.esica.MainActivity;
import jp.mmitti.esica.common.ImageSelectDialog;
import jp.mmitti.esica.common.data.CardData;
import jp.mmitti.esica.common.data.ProgramData;
import jp.mmitti.esica.create.CreateActivity;
import jp.mmitti.esica.create.Init;
import jp.mmitti.esica.system.Screen;
import jp.mmitti.esica.system.ScreenManagerActivity;
import jp.mmitti.sansan.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditActivity extends ScreenManagerActivity{
	private int ID;
	private ProgramData mProgramData;
	private Intent mRet;
	public static final int ACTIVITY_CODE = 0xA0011;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
	    Bundle ex = getIntent().getExtras();
	    if(ex == null)finish();
	    ID = ex.getInt("SRC", -1);
	    if(ID == -1)finish();
	    mProgramData = new ProgramData(this);
	    moveScreen(new EditPreview(ID));
		
		
		
		mRet = new Intent();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem i){
		if(i.getItemId() == android.R.id.home){
			this.onBackPressed();
			return true;
		}
		return false;
	}
	
	@Override
    public void finish(){
		setResult(Activity.RESULT_OK, mRet);
        super.finish();
        overridePendingTransition(R.anim.remain, android.R.anim.fade_out);
        
    }
	
	public void finishEdit(){
		mRet.putExtra("EDIT", true);
	}

	public void startEdit(){
		Intent intent = new Intent(this, CreateActivity.class);
		intent.putExtra("EDIT", ID);
		this.startActivityForResult(intent, EditActivity.ACTIVITY_CODE);

		this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {  
		if(resultCode == Activity.RESULT_OK && requestCode == EditActivity.ACTIVITY_CODE){
			finishEdit();
		}
	}
	

	public void use() {
		mProgramData.setCurrentID(ID);
		finish();
	}
	
	public void remove() {
		mRet.putExtra("EDIT", true);
		finish();
	}
}
