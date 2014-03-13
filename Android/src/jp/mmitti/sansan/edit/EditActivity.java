package jp.mmitti.sansan.edit;

import jp.mmitti.sansan.MainActivity;
import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.CardData;
import jp.mmitti.sansan.common.ImageSelectDialog;
import jp.mmitti.sansan.common.ProgramData;
import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import jp.mmitti.sansan.create.Init;
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
	    ID = ex.getInt("SRC");
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
	
	public void edit(){
		mRet.putExtra("EDIT", true);
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
