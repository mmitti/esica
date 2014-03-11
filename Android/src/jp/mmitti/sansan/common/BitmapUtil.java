package jp.mmitti.sansan.common;

import java.io.ByteArrayOutputStream;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import net.arnx.jsonic.JSONHint;
import net.arnx.jsonic.util.Base64;

public class BitmapUtil {

	public static Bitmap Base64ToBitmap(String json){
		byte[] bytes = Base64.decode(json);
		
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	public static String BitmapToBase64(Bitmap b){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		b.compress(CompressFormat.PNG,80, os);
		byte[] bytes = os.toByteArray();
		return Base64.encode(bytes);
	}
	
	/**
	 * maxSizeに収まりaspectRatioの比率を満たす最大のサイズを返す。
	 * @param maxSize サイズの上限
	 * @param aspectRatio アスペクト比
	 * @return
	 */
	public static Vec2i getOptimumMaxSize(Vec2i maxSize, Vec2i aspectRatio){
		Vec2i ret = new Vec2i();
		double factH, factW, fact;
		factH = maxSize.y / (double) aspectRatio.y;
		factW = maxSize.x / (double) aspectRatio.x;
		fact = factW;
		if(factH < factW)fact = factH;
		ret.x = (int) (aspectRatio.x * fact);
		ret.y = (int) (aspectRatio.y * fact);
		return ret;
	}
}
