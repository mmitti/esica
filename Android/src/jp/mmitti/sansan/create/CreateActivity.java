package jp.mmitti.sansan.create;

import java.io.IOException;

import jp.mmitti.sansan.common.ArgData;
import jp.mmitti.sansan.common.CardData;
import jp.mmitti.sansan.common.ImageSelectDialog;
import jp.mmitti.sansan.common.MyAsyncTask;
import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import android.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CreateActivity extends ScreenManagerActivity implements CreateManager {

	private ArgData mData;
	private CardData mCardData;
	private ImageSelectDialog mImageSelecter;
	private Load mLoad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageSelecter = new ImageSelectDialog(this);
		mLoad = new Load(getHandler());
		moveScreen(new Screen(){protected ViewGroup initView(ScreenManagerActivity a){return new LinearLayout(a);}});

		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
	   
	    mLoad.start();
	    moveScreen(new Init());
	}
	
	//必要があれば既存のデータを復元
	
	@Override
    public void finish(){
		
        super.finish();
        overridePendingTransition(0, 0);
        
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
			mCardData.save();
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
		public Load(Handler handler) {
			super(handler);
			mDlg = new ProgressDialog(CreateActivity.this);
			mDlg.setMessage("読み込み中");
		}
		
		
		protected void preProcessOnUI(){
			mDlg.show();
			num = -1;
			Bundle extras = getIntent().getExtras();
			if(extras != null && extras.containsKey("SRC")){
				num = extras.getInt("SRC");
			} 
		}
		
		@Override
		protected void doBackGround() throws InterruptedException {
			if(num != -1){
				mCardData = new CardData(num);
		    	mData = mCardData.data;
			}
			else{
				mCardData = new CardData();
				mData = new ArgData();
			}
		}
		
		
		protected void onFinishOnUI(){
			mDlg.dismiss();
		}
		
		
	}
	

}
