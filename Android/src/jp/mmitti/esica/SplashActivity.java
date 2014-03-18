package jp.mmitti.esica;

import jp.mmitti.esica.common.data.ProgramData;
import jp.mmitti.esica.create.CreateActivity;
import jp.mmitti.esica.system.Screen;
import jp.mmitti.esica.system.ScreenManagerActivity;
import jp.mmitti.sansan.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		        startActivity(new Intent(SplashActivity.this, MainActivity.class));
		        finish();
		    }
		}, 1500);
	}



}
