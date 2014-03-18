package jp.mmitti.esica.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import jp.mmitti.sansan.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ImageSelectDialog{
	private Activity mActivity;
	
	private AlertDialog mImageDialog;
	private String mImagePath;
	private String mTrimingImagePath;
	private final int IMG_RGARARY_REQUEST = 0xAABB13;
	private final int IMG_CAMERA_REQUEST = 0xAABB14;
	private final int CROP_IMAGE = 0xAABB15;
	private OnBitmapRecvdListner mRecver;
	
	private enum SelectMode{
		NONE, TRIMING
	}
	
	private int mImageSrcMode;
	
	private SelectMode mMode;
	private int mTrimingRatioX;
	private int mTrimingRatioY;	
	
	public ImageSelectDialog(Activity activity){
		mActivity = activity;
		mImagePath = Environment.getExternalStorageDirectory()+"/mmitti/img.jpg";
		mTrimingImagePath = Environment.getExternalStorageDirectory()+"/mmitti/timg.jpg";
		Builder a = new AlertDialog.Builder(activity);
		a.setPositiveButton("カメラで撮影", new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which){
				Intent i = new Intent();
				i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				i.addCategory(Intent.CATEGORY_DEFAULT);
//				i.putExtra(MediaStore., value)
				File f = new File(mImagePath);
				try{
					File dir = f.getParentFile();
					dir.mkdirs();
					f.createNewFile();
				}catch(IOException e){
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				Uri uri = Uri.fromFile(f);
				i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				mImageSrcMode = IMG_CAMERA_REQUEST;
				mActivity.startActivityForResult(i, IMG_CAMERA_REQUEST);
			}
		});
		a.setNegativeButton("ギャラリーから", new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which){
				Intent i = new Intent();
				i.setType("image/*");
				i.setAction(Intent.ACTION_GET_CONTENT);
				mImageSrcMode = IMG_RGARARY_REQUEST;
				mActivity.startActivityForResult(i, IMG_RGARARY_REQUEST);
			}
		});
		a.setCancelable(true);
		a.setView(View.inflate(activity, R.layout.image_dialog_message, null));
		
		mImageDialog = a.create();

	}
	
	public void show(OnBitmapRecvdListner recver){
		mMode = SelectMode.NONE;
		mRecver = recver;
		mImageDialog.show();
	}
	
	public void showWithTriming(OnBitmapRecvdListner recver, int ratioX, int ratioY){
		mMode = SelectMode.TRIMING;
		mRecver = recver;
		mTrimingRatioX = ratioX;
		mTrimingRatioY = ratioY;
		mImageDialog.show();		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(data == null);
		Bitmap ret = null;
		if(requestCode == IMG_RGARARY_REQUEST && resultCode == Activity.RESULT_OK){
			String[] columns = { MediaStore.Images.Media.DATA };
			Cursor c = mActivity.getContentResolver().query(data.getData(), columns, null, null, null);
			c.moveToFirst();
			onSrcImageSelected(c.getString(0));
		}
		else if(requestCode == IMG_CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
			onSrcImageSelected(mImagePath);
		}
		else if (requestCode == CROP_IMAGE && resultCode == Activity.RESULT_OK){
			returnImage(mTrimingImagePath);
		}
	}
	
	private void onSrcImageSelected(String path){
		if(mMode == SelectMode.NONE){
			returnImage(path);
		}
		else if(mMode == SelectMode.TRIMING){
			
			Intent intent = new Intent("com.android.camera.action.CROP");
	        intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
	        intent.putExtra("crop", "true");
	        intent.putExtra("aspectX", mTrimingRatioX);
	        intent.putExtra("aspectY", mTrimingRatioY);
	        intent.putExtra("scale", true);
	        intent.putExtra("noFaceDetection", true);
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTrimingImagePath)));
	        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
	        try{
	        	mActivity.startActivityForResult(intent, CROP_IMAGE);
	        }catch(ActivityNotFoundException e){
	        	Toast.makeText(mActivity, "NF", Toast.LENGTH_LONG).show();
	        }
		}
	}
	
	private void returnImage(String path){
		Bitmap ret;
		try{
			ret = loadFromFile(path);
			finishSelectImage(path);
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(mActivity, "画像ファイルの読み込みに失敗しました", Toast.LENGTH_SHORT).show();
			return;
		}
		mRecver.recvd(ret);
	}
	
	private void finishSelectImage(String path){
		if(mImageSrcMode == IMG_CAMERA_REQUEST){
			File f = new File(mImagePath);
			f.delete();
		}
		
		if(mMode == SelectMode.TRIMING){
			File f = new File(mTrimingImagePath);
			f.delete();
		}
	}
	
	private int getInSampleSize(String path){
		BitmapFactory.Options op = new Options();
		op.inJustDecodeBounds = true;
		Bitmap b = BitmapFactory.decodeFile(path, op);
		Vec2i size = Utils.getOptimumMaxSize(new Vec2i(300, 300), new Vec2i(op.outWidth, op.outHeight));
		float rate = (float)op.outWidth/size.x;
		int ret = 1;
		for(int i = 2; i < rate; i *= 2) {
			ret = i;
		}
		return ret;

	}
	
	private Bitmap loadFromFile(String path){
		BitmapFactory.Options op = new Options();
		op.inSampleSize = getInSampleSize(path);
		Bitmap b = BitmapFactory.decodeFile(path, op);
		Matrix mat = new Matrix();
		try{
			ExifInterface exif = new ExifInterface(path);
			switch(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)){
				case ExifInterface.ORIENTATION_ROTATE_90:
					mat.postRotate(90);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					mat.postRotate(180);
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					mat.postRotate(270);
					break;
			}
			
		}catch(IOException e){
		}
		Bitmap buf = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), mat, true);
		Log.d("IMG", "SAVED X:"+b.getWidth()+" Y:"+b.getHeight());
		return buf;
	}
	
	public interface OnBitmapRecvdListner{
		void recvd(Bitmap b);
	}
	
}
