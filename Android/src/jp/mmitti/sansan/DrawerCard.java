package jp.mmitti.sansan;

import java.util.LinkedList;
import java.util.List;

import jp.mmitti.sansan.common.CardData;
import jp.mmitti.sansan.common.MyAsyncTask;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DrawerCard extends LinearLayout{
	private ImageView mImage;
	private ProgressBar mLoadProgress;
	private TextView mText;
	private int mID;
	public DrawerCard(Context context, int id) {
		super(context);
		View v = View.inflate(context, R.layout.drawer_card, null);
		this.addView(v, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		
		mImage = (ImageView)v.findViewById(R.id.image);
		
		mText = (TextView)v.findViewById(R.id.text);
		mLoadProgress = (ProgressBar)v.findViewById(R.id.loading);
		mText.setVisibility(INVISIBLE);
		mLoadProgress.setVisibility(INVISIBLE);
		mID = id;
	}
	
	public void preLoad(){
		mLoadProgress.setVisibility(VISIBLE);

		mText.setVisibility(VISIBLE);
		mText.setText("読み込み中です。");
	}
	
	public void load(){
		Load l = new Load(getHandler());
		l.startSync();
	}
	
	public void cancel(){
		mLoadProgress.setVisibility(INVISIBLE);
		mText.setVisibility(VISIBLE);
		mText.setText("読み込みに失敗しました。");
	}
	
	public class Load extends MyAsyncTask{
		private Bitmap b;
		public Load(Handler handler) {
			super(handler);
			
		}
		
		

		@Override
		protected void doBackGround() throws InterruptedException {
			b = CardData.getImage(mID);
		}
		
		public void onFinishOnUI(){
			mLoadProgress.setVisibility(INVISIBLE);
			if(b == null)cancel();
			else{
				mText.setVisibility(INVISIBLE);
				mImage.setImageBitmap(b);
			}
		}
		
	}
	
	
}
