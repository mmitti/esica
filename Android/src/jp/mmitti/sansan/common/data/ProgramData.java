package jp.mmitti.sansan.common.data;

import net.arnx.jsonic.JSON;
import android.app.Activity;
import android.content.SharedPreferences;

public class ProgramData {
	private static SharedPreferences mSharedPreferences;
	private int mCurrentID;
	public final static int DEFAULT_INF = -1;
	public ProgramData(){
		update();
	}
	
	public ProgramData(Activity a){
		init(a);
		update();
	}
	public static void init(Activity a){
		mSharedPreferences = a.getSharedPreferences("SANSAN", Activity.MODE_PRIVATE);
	}
	
	public int getCurrentID(){
		return mCurrentID;
	}
	
	public void setCurrentID(int id){
		if(mSharedPreferences == null)return;
		mCurrentID = id;
		mSharedPreferences.edit().putInt("CID", id).commit();
	}
	
	public void setBasicData(BasicProfileData b){
		if(mSharedPreferences == null)return;
		mSharedPreferences.edit().putString("BPD", JSON.encode(b)).commit();
	}
	
	public BasicProfileData getBasicProfileData(){
		if(mSharedPreferences == null)return null;
		String json = mSharedPreferences.getString("BPD", "");
		if(json.equals(""))return null;
		return JSON.decode(json, BasicProfileData.class);
	}
	
	public void update(){
		mCurrentID = DEFAULT_INF;
		if(mSharedPreferences == null)return;
		mCurrentID = mSharedPreferences.getInt("CID", DEFAULT_INF);
	}
}
