package jp.mmitti.esica.common.data;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import jp.mmitti.esica.common.Utils;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

/**
 * 名刺のデータすべてを扱うクラス<BR>
 * ファイルから読み込み、保存を担う
 * @author mmitti
 *
 */
public class CardData {
	private final static String DIR = Environment.getExternalStorageDirectory()+"/mmitti/sansan";
	private final static String JSON_DATA = "json";
	private final static String META_DATA = "meta";
	private final static String IMG = "png";
	private final static String CURRENT_IMG = "card.png";
	public ArgData data;
	public Bitmap cardImg;
	public MetaInfo meta;
	private int mCurrentNum;
	public CardData(int num){
		mCurrentNum = num;
		try{
			restore();
		}catch(Exception e){
			initData();
		}
	}
	
	private void initData(){
		data = new ArgData();
		cardImg = null;
		meta = new MetaInfo();
	}
	
	public CardData(){
		createNewCard();
		initData();
	}
	
	public void restore() throws IOException{
		if(new File(DIR+"/"+mCurrentNum).exists()){
			File json = new File(DIR+"/"+mCurrentNum+"/"+JSON_DATA);
			
			
			FileInputStream json_is = new FileInputStream(json);
			data = JSON.decode(json_is, ArgData.class);
			json_is.close();
			
			
			meta = getMetaInfo(mCurrentNum);
			
			cardImg = getImage(mCurrentNum);
			data.PicMode = meta.PicMode;
		}
		else throw new FileNotFoundException();
	}
	
	public static MetaInfo getMetaInfo(int id) throws IOException{
		File meta_data = new File(DIR+"/"+id+"/"+META_DATA);
		
		
		FileInputStream meta_is = new FileInputStream(meta_data);
		MetaInfo r = JSON.decode(meta_is, MetaInfo.class);
		meta_is.close();
		return r;
	}
	
	public void save() throws IOException{
		meta.PicMode = data.PicMode;
		new File(DIR+"/"+mCurrentNum).mkdirs();
		File json = new File(DIR+"/"+mCurrentNum+"/"+JSON_DATA);
		File meta_data = new File(DIR+"/"+mCurrentNum+"/"+META_DATA);
		File img = new File(DIR+"/"+mCurrentNum+"/"+IMG);
		
		json.createNewFile();
		FileOutputStream json_os = new FileOutputStream(json, false);
		JSON.encode(data, json_os, false);
		json_os.close();
		
		meta_data.createNewFile();
		FileOutputStream meta_os = new FileOutputStream(meta_data, false);
		JSON.encode(meta, meta_os, false);
		meta_os.close();
		
		img.createNewFile();
		FileOutputStream img_os = new FileOutputStream(img, false);
		cardImg.compress(CompressFormat.PNG, 80, img_os);
		img_os.close();
		
	}
	
	private void createNewCard(){
		File f = new File(DIR);
		f.mkdirs();
		for(int i = 1;; i++){
			if(!new File(DIR+"/"+i).exists()){
				mCurrentNum = i;
				break;
			}
			
		}
	}
	
	public int getID(){return mCurrentNum;}
	
	public static List<Integer> getCardList(){
		List<Integer> list = new LinkedList<Integer>();
		File f = new File(DIR);
		if(!f.exists())return list;
		File[] files = f.listFiles();
		for(File s : files){
			if(!s.isDirectory())continue;
			try{
				list.add(Integer.parseInt(s.getName()));
			}catch(Exception e){}
		}
		return list;
	}
	
	public static Bitmap getImage(int num){
		File img = new File(DIR+"/"+num+"/"+IMG); 
		if(!img.exists())return null;
		return BitmapFactory.decodeFile(DIR+"/"+num+"/"+IMG);
	}

	public void remove() {
		remove(mCurrentNum);
	}
	
	public static void remove(int id){
		File d = new File(DIR+"/"+id);
		Utils.removeFile(d);
	}
	
	public void cpCard(String filename) throws IOException{
		cpCard(mCurrentNum, filename);
	}
	
	public static void cpCard(int id, String filename) throws IOException{
		File img = new File(DIR+"/"+id+"/"+IMG);
		if(filename == null) filename = CURRENT_IMG;
		if(img.exists()){
			File dest = new File(DIR+"/"+filename);
			if(img.lastModified()==dest.lastModified() && dest.lastModified() != 0)return;
			FileInputStream is = new FileInputStream(img);
			FileOutputStream os = new FileOutputStream(dest, false);
			byte[] buf = new byte[(int)img.length()];
			is.read(buf);
			os.write(buf);
			is.close();
			os.close();
		}
	}
	
	public Intent openAs(){
		return openAs(mCurrentNum);
	}


	
	public static Intent openAs(int id){
		try {
			cpCard(id, "tmp.png");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse("file://"+new File(DIR+"/tmp.png").getAbsolutePath());
		intent.setDataAndType(uri, "image/*");
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		return intent;
	}
	
}
