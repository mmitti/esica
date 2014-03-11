package jp.mmitti.sansan.common;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import net.arnx.jsonic.JSONHint;
import net.arnx.jsonic.util.Base64;

public class BitmapUtil {

	public static Bitmap jsonToBitmap(String json){
		byte[] bytes = Base64.decode(json);
		
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	public static String BitmapToJson(Bitmap b){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		b.compress(CompressFormat.PNG,80, os);
		byte[] bytes = os.toByteArray();
		return Base64.encode(bytes);
	}
}
