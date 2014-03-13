package jp.mmitti.sansan.common;

import java.io.ByteArrayOutputStream;


import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import net.arnx.jsonic.JSONHint;
import net.arnx.jsonic.util.Base64;

public class Utils {

	public static Bitmap base64ToBitmap(String json){
		byte[] bytes = Base64.decode(json);
		
		return bytesToBitmap(bytes);
	}
	
	public static String bitmapToBase64(Bitmap b){
		
		return Base64.encode(bitmapToBytes(b));
	}
	
	public static Bitmap bytesToBitmap(byte[] b){
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}
	
	public static byte[] bitmapToBytes(Bitmap b){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		b.compress(CompressFormat.PNG,80, os);
		byte[] bytes = os.toByteArray();
		return bytes;
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
	
	public static void removeFile(File f){
		if(!f.exists())return;
		if(f.isDirectory()){
			File[] l = f.listFiles();
			for(File ff : l){
				removeFile(ff);
			}
		}
		f.delete();
	}
	
}
