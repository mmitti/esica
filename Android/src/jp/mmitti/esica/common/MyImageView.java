package jp.mmitti.esica.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {
	public int ratioX = 55;
	public int ratioY = 91;
	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(widthMeasureSpec);
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
	    super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode),
	    		MeasureSpec.makeMeasureSpec((int)(width*ratioX/(double)ratioY), heightMode));
	}
}
