package jp.mmitti.sansan;

import jp.mmitti.sansan.common.ProgramData;
import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import jp.mmitti.sansan.create.CreateActivity;
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
