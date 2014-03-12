package jp.mmitti.sansan.create;

import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.Utils;
import jp.mmitti.sansan.common.ImageSelectDialog.OnBitmapRecvdListner;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FacePictureSelect extends LinearLayout implements OnBitmapRecvdListner{
	private CreateManager mManager;
	private ImageView mImg;
	private Bitmap bitmap;
	public FacePictureSelect(Context context, AttributeSet attrs){
		super(context, attrs);
		// TODO 自動生成されたコンストラクター・スタブ
		View v = View.inflate(context, R.layout.c_pic_face, null);
		this.addView(v, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		Button b = (Button)v.findViewById(R.id.imgselect);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mManager.requestGetImage(FacePictureSelect.this, 1, 1);
			}
		});
		mImg = (ImageView)v.findViewById(R.id.image);
	}
	
	public void init(CreateManager m){
		mManager = m;
	}
	
	public String dumpImg(){
		if(bitmap == null)return "";
		return Utils.bitmapToBase64(bitmap);
	}
	
	public void setImage(String json){
		if(json == "")mImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact_picture));
		else{
			bitmap = Utils.base64ToBitmap(json);
			mImg.setImageBitmap(bitmap);
		}
	}

	@Override
	public void recvd(Bitmap b) {
		mImg.setImageBitmap(b);
		bitmap = b;
	}
}
