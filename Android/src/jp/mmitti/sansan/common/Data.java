package jp.mmitti.sansan.common;

import android.os.Environment;
import net.arnx.jsonic.JSONHint;

public class Data {
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
	/**
	 * 顔写真・QR base64 135 × 135‏
	 */
	public String pic;
	/**
	 * 背景　base64 91×55‏
	 */
	public String back;
	
	@JSONHint(ignore=true)
	public PicMode PicMode;
	
	public enum PicMode{
		QR, FACE, NONE;
	}
	
	public Data(){
		family="";
		name="";
		rubi_family = "";
		rubi_name = "";
		school = "";
		department = "";
		mail = "";
		tel = "";
		pic = "";
		back = "";
		PicMode = PicMode.NONE;
	}

	public String getQRData() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name:"+rubi_family+" "+rubi_name+"\n");
		sb.append("名前:"+family+" "+name+"\n");
		sb.append("学校名/会社名:"+school+"\n");
		sb.append("学科/部署:"+department+"\n");
		sb.append("メールアドレス:"+mail+"\n");

		sb.append("電話番号:"+tel+"\n");
		return sb.toString();
	}
}
