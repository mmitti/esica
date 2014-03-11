package jp.mmitti.sansan.create;

import jp.mmitti.sansan.common.Data;
import jp.mmitti.sansan.common.ImageSelectDialog;
import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CreateActivity extends ScreenManagerActivity implements CreateManager {

	private Data mData;
	private ImageSelectDialog mImageSelecter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageSelecter = new ImageSelectDialog(this);
		moveScreen(new Screen(){protected ViewGroup initView(ScreenManagerActivity a){return new LinearLayout(a);}});

		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
	    mData = new Data();
	    moveScreen(new Init());
	}
	
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
	public Data getData() {
		return mData;
	}
	
	public void requestGetImage(jp.mmitti.sansan.common.ImageSelectDialog.OnBitmapRecvdListner recver, int x, int y){
		mImageSelecter.showWithTriming(recver, x, y);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		mImageSelecter.onActivityResult(requestCode, resultCode, data);
	}
	

}
