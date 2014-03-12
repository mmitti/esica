package jp.mmitti.sansan.create;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.Utils;
import jp.mmitti.sansan.common.ArgData;
import jp.mmitti.sansan.common.ImageSelectDialog.OnBitmapRecvdListner;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class QRCreate  extends LinearLayout{
	private CreateManager mManager;
	private TextView mContent;
	public QRCreate(Context context, AttributeSet attrs){
		super(context, attrs);
		// TODO 自動生成されたコンストラクター・スタブ
		View v = View.inflate(context, R.layout.c_pic_qr, null);
		this.addView(v, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		mContent = (TextView)v.findViewById(R.id.content);
	}
	
	public void init(CreateManager m){
		mManager = m;
		setData();
		((ImageView)this.findViewById(R.id.imageView1)).setImageBitmap(createQR(m.getData().getQRData()));
	}
	
	public String getQRImage(){
		Utils.bitmapToBase64(createQR(mManager.getData().getQRData()));
		return "";
	}
	
	private Bitmap createQR(String text){
		if(text.trim().equals("")) return null;
		QRCodeWriter writer = new QRCodeWriter();
		Bitmap bmp = null;
		try {
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "shiftjis");
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			BitMatrix bm = writer.encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);
			int width = bm.getWidth();
			int height = bm.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
			    int offset = y * width;
			    for (int x = 0; x < width; x++) {
			        pixels[offset + x] = bm.get(x, y) ? Color.BLACK : 0x00FFFFFF;
			    }
			}
			bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			bmp.setPixels(pixels, 0, width, 0, 0, width, height);
		} catch (WriterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return bmp;
	}
	
	public void setData(){
		mContent.setText(mManager.getData().getQRData());
	}
}