package jp.mmitti.esica;

import jp.mmitti.esica.common.data.CardData;
import jp.mmitti.sansan.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CurrentMainScreen  extends LinearLayout{
	private MainScreen ms;
	private int ID;
	public CurrentMainScreen(final Context context, MainScreen s, int id) {
		super(context);
		ID = id;
		ms = s;
		View v = View.inflate(context, R.layout.current_main_screen, null);
		this.addView(v, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		Button b = (Button)v.findViewById(R.id.add);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ms.addCard();
			}
		});
		b = (Button)v.findViewById(R.id.edit);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ms.edit(ID);
			}
		});
		
		ImageView img = (ImageView)v.findViewById(R.id.image);
		View img_btn = v.findViewById(R.id.imgbtn);
		img_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((Activity)context).startActivity(CardData.openAs(ID));
			}
		});
		
		
		Bitmap bmp = CardData.getImage(id);
		if(bmp == null){
			;
		}
		else{
			img.setImageBitmap(bmp);
		}
	}
	

}
