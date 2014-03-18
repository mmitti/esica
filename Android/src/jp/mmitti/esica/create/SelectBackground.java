package jp.mmitti.esica.create;

import jp.mmitti.esica.common.MyRadioGroup;
import jp.mmitti.esica.common.Utils;
import jp.mmitti.esica.common.ImageSelectDialog.OnBitmapRecvdListner;
import jp.mmitti.esica.system.ScreenManagerActivity;
import jp.mmitti.sansan.R;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectBackground  extends CreateScreen implements OnClickListener, OnBitmapRecvdListner {
	private ImageView mImg;
	@Override
	protected ViewGroup initView(ScreenManagerActivity activity) {
		ViewGroup g = (ViewGroup)ViewGroup.inflate(activity, R.layout.c_select_background, null);
		Button next = (Button)g.findViewById(R.id.next);
		next.setOnClickListener(this);
		Button back = (Button)g.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		Button s = (Button)g.findViewById(R.id.select);
		s.setOnClickListener(this);
		mImg = (ImageView)g.findViewById(R.id.image);
		String base64 = mManager.getData().back;
		mImg.setImageBitmap(Utils.base64ToBitmap(base64));
		TextView step = (TextView)g.findViewById(R.id.text_step);
		
		if(mManager.isEditMode()){
			step.setText("ステップ3/3");//TODO 治したい
		}
		else{
			step.setText("ステップ4/4");
		}
		return g;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.next:
			mManager.changeScreen(new Preview());
			break;
		case R.id.back:
			mManager.popScreen();
			break;
		case R.id.select:
			mManager.requestGetImage(this, 91, 55);
			break;
		
		}
	}

	@Override
	public void recvd(Bitmap b) {
		mManager.getData().back = Utils.bitmapToBase64(b);
		mImg.setImageBitmap(b);
	}
	
}
