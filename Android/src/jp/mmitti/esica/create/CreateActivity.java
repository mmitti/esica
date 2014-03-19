package jp.mmitti.esica.create;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import jp.mmitti.esica.common.ImageSelectDialog;
import jp.mmitti.esica.common.Utils;
import jp.mmitti.esica.common.data.ArgData;
import jp.mmitti.esica.common.data.BasicProfileData;
import jp.mmitti.esica.common.data.CardData;
import jp.mmitti.esica.common.data.ProgramData;
import jp.mmitti.esica.system.MyAsyncTask;
import jp.mmitti.esica.system.Screen;
import jp.mmitti.esica.system.ScreenManagerActivity;
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
	private boolean isEdit;
	private Load mLoad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageSelecter = new ImageSelectDialog(this);
		moveScreen(new Screen(){protected ViewGroup initView(ScreenManagerActivity a){return new LinearLayout(a);}});

		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
	   
	    int id = getIntent().getIntExtra("EDIT", -1);
	    mProgramData = new ProgramData(this);
	    if(id == -1){
		    
		    mCardData = new CardData();
		    mData = mCardData.data;
		    BasicProfileData b = mProgramData.getBasicProfileData();
		    if(b != null)mData.setBasicProfile(b);
		    moveScreen(new Init());
		    isEdit = false;
	    }else{
	    	getActionBar().setTitle("編集");
	    	isEdit = true;
	    	mLoad = new Load(getHandler(), id);
	    	mLoad.start();
	    }
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
	
	public void requestGetImage(jp.mmitti.esica.common.ImageSelectDialog.OnBitmapRecvdListner recver, int x, int y){
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
			if(!isEdit)mProgramData.setCurrentID(mCardData.getID());
			mProgramData.setBasicData(mData.clone());//TODO 個々の仕様はどうします？(考えなくてもいいかも
		} catch (IOException e) {
			Toast.makeText(this, "名刺の保存に失敗しました。", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void make(Bitmap img) {
		mCardData.cardImg = img;
		mCardData.data = mData;
	}
	
	
	private class Load extends MyAsyncTask{
		ProgressDialog mDlg;
		private int num;
		public Load(Handler handler, int id) {
			super(handler);
			num = id;
			mDlg = new ProgressDialog(CreateActivity.this);
			mDlg.setCancelable(false);
			mDlg.setMessage("読み込み中");
		}
		
		
		protected void preProcessOnUI(){
			mDlg.show();
			
		}
		
		@Override
		protected void doBackGround() throws InterruptedException {
			mCardData = new CardData(num);
			mData = mCardData.data;
		}
		
		
		protected void onFinishOnUI(){
			mDlg.dismiss();
			moveScreen(new Input());
		}
	

	}


	@Override
	public boolean isEditMode() {
		return isEdit;
	}

}
