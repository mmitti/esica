package jp.mmitti.sansan.create;

import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CreateActivity extends ScreenManagerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		moveScreen(new Screen(){protected ViewGroup initView(ScreenManagerActivity a){return new LinearLayout(a);}});

		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
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

}
