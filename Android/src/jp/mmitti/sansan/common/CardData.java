package jp.mmitti.sansan.common;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.arnx.jsonic.JSON;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 名刺のデータすべてを扱うクラス<BR>
 * ファイルから読み込み、保存を担う
 * @author mmitti
 *
 */
public class CardData {
	private final String DIR = Environment.getExternalStorageDirectory()+"/mmitti/sansan";
	private final String JSON_DATA = "json";
	private final String IMG = "png";
	public ArgData data;
	public Bitmap cardImg;
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
			
			cardImg = BitmapFactory.decodeFile(DIR+"/"+mCurrentNum+"/"+IMG);
		}
		else throw new FileNotFoundException();
	}
	
	public void save() throws IOException{
		new File(DIR+"/"+mCurrentNum).mkdirs();
		File json = new File(DIR+"/"+mCurrentNum+"/"+JSON_DATA);
		File img = new File(DIR+"/"+mCurrentNum+"/"+IMG);
		
		json.createNewFile();
		FileOutputStream json_os = new FileOutputStream(json, false);
		JSON.encode(data, json_os, false);
		json_os.close();
		
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
}
