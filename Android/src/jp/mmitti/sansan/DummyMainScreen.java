package jp.mmitti.sansan;

import jp.mmitti.sansan.common.data.CardData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

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
		Button p = (Button)v.findViewById(R.id.pick);
		p.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ms.pick();
			}
		});
		TextView title = (TextView)v.findViewById(R.id.title);
		TextView msg = (TextView)v.findViewById(R.id.message);
		if(CardData.getCardList().size()>0){
			title.setText("名刺が選択されていません。");
			msg.setText("名刺を選択しましょう。");
			p.setVisibility(VISIBLE);
		}
		else{
			title.setText("名刺がありません。");
			msg.setText("まずは名刺を作りましょう。");
			p.setVisibility(INVISIBLE);
		}
		
		
	}
	

}
