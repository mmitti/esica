package jp.mmitti.sansan;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DummyMainScreen extends LinearLayout{
	private MainScreen ms;
	public DummyMainScreen(Context context, MainScreen s) {
		super(context);
		ms = s;
		View v = View.inflate(context, R.layout.dummy_main_screen, null);
		this.addView(v, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		Button b = (Button)v.findViewById(R.id.create);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ms.addCard();
			}
		});
	}
	

}
