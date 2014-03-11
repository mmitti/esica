package jp.mmitti.sansan.common;

public class Data {
	/**
	 * 姓
	 */
	String family;
	/**
	 * 名
	 */
	String name;
	/**
	 * ルビ　姓
	 */
	String rubi_family;
	/**
	 * ルビ　名
	 */
	String rubi_name;
	/**
	 * 会社名　学校名
	 */
	String school;
	/**
	 * 所属
	 */
	String department;
	/**
	 * メールアドレス
	 */
	String mail;
	/**
	 * 電話番号
	 */
	String tel;
	/**
	 * 顔写真・QR base64 135 × 135‏
	 */
	String pic;
	/**
	 * 背景　base64 91×55‏
	 */
	String back;
	
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
	}
}
