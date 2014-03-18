package jp.mmitti.esica;

import jp.mmitti.esica.common.data.ProgramData;
import jp.mmitti.esica.create.CreateActivity;
import jp.mmitti.esica.system.Screen;
import jp.mmitti.esica.system.ScreenManagerActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends ScreenManagerActivity {
	public static final String UPDATE = "UP";
	public ProgramData programData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		programData = new ProgramData(this);
		moveScreen(new Screen(){protected ViewGroup initView(ScreenManagerActivity a){return new LinearLayout(a);}});
		moveScreen(new MainScreen());
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {  
		this.mActiveScreen.onActivityResult(requestCode, resultCode, intent);
	}
	

}
