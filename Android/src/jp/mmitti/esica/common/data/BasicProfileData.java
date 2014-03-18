package jp.mmitti.esica.common.data;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import net.arnx.jsonic.JSONHint;

/**
 * サーバーに送信するデータを扱うクラス<BR>
 * 名前などのデータはここで扱う
 * @author mmitti
 *
 */
public class BasicProfileData implements Cloneable{
	/**
	 * 姓
	 */
	public String family;
	/**
	 * 名
	 */
	public String name;
	/**
	 * ルビ　姓
	 */
	public String rubi_family;
	/**
	 * ルビ　名
	 */
	public String rubi_name;
	/**
	 * 会社名　学校名
	 */
	public String school;
	/**
	 * 所属
	 */
	public String department;
	/**
	 * メールアドレス
	 */
	public String mail;
	/**
	 * 電話番号
	 */
	public String tel;
	
	public BasicProfileData(){
		family="";
		name="";
		rubi_family = "";
		rubi_name = "";
		school = "";
		department = "";
		mail = "";
		tel = "";
	}
	
	public BasicProfileData clone(){
		BasicProfileData ret = null;
		try {
			ret = (BasicProfileData)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		return ret;
	}
	


	/*public void setSpace(){
		if(family.trim().equals(""))family = " ";
		if(name.trim().equals(""))name = " ";
		if(rubi_family.trim().equals(""))rubi_family = " ";
		if(rubi_name.trim().equals(""))rubi_name = " ";
		if(school.trim().equals(""))school = " ";
		if(department.trim().equals(""))department = " ";

		if(tel.trim().equals(""))tel = " ";

		if(mail.trim().equals(""))mail = " ";
	}*/
}
