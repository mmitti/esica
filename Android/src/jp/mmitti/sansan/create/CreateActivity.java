package jp.mmitti.sansan.create;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import jp.mmitti.sansan.common.ImageSelectDialog;
import jp.mmitti.sansan.common.Utils;
import jp.mmitti.sansan.common.data.ArgData;
import jp.mmitti.sansan.common.data.CardData;
import jp.mmitti.sansan.common.data.ProgramData;
import jp.mmitti.sansan.system.MyAsyncTask;
import jp.mmitti.sansan.system.Screen;
import jp.mmitti.sansan.system.ScreenManagerActivity;
import android.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CreateActivity extends ScreenManagerActivity implements CreateManager {
	public static final int ACTIVITY_CODE = 0xB0014;
	private ArgData mData;
	private CardData mCardData;
	private ImageSelectDialog mImageSelecter;
	private ProgramData mProgramData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageSelecter = new ImageSelectDialog(this);
		moveScreen(new Screen(){protected ViewGroup initView(ScreenManagerActivity a){return new LinearLayout(a);}});

		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
	   
	    mProgramData = new ProgramData(this);
	    mCardData = new CardData();
	    mData = mCardData.data;
	    moveScreen(new Init());
	}
	
	//必要があれば既存のデータを復元
	
	@Override
    public void finish(){
		
        super.finish();

        overridePendingTransition(jp.mmitti.sansan.R.anim.remain,R.anim.fade_out);
        
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem i){
		if(i.getItemId() == R.id.home)this.onBackPressed();
		return super.onOptionsItemSelected(i);
	}

	@Override
	public ArgData getData() {
		return mData;
	}
	
	public void requestGetImage(jp.mmitti.sansan.common.ImageSelectDialog.OnBitmapRecvdListner recver, int x, int y){
		mImageSelecter.showWithTriming(recver, x, y);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		mImageSelecter.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void save() {
		try {
			mCardData.meta.name = mData.family +" "+mData.name;
			mCardData.meta.school = mData.school;
			mCardData.meta.department = mData.department;
			mCardData.meta.date = Calendar.getInstance(Locale.JAPAN);
			mCardData.save();
			mProgramData.setCurrentID(mCardData.getID());
		} catch (IOException e) {
			Toast.makeText(this, "名刺の保存に失敗しました。", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void make(Bitmap img) {
		mCardData.cardImg = img;
		mCardData.data = mData;
	}
	
	
	

}
