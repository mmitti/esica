package jp.mmitti.sansan.common;

import android.graphics.Rect;

/**
 * 2つの値を扱うクラス
 * @author mmitti
 *
 */
public class Vec2i{
	/**
	 * x座標や幅を示す
	 */
	public int x;
	/**
	 * y座標や高さを示す
	 */
	public int y;
	
	/**
	 * すべて0で初期化する
	 */
	public Vec2i(){
		x = y = 0;
	}
	
	/**
	 * 指定された引数で初期化
	 * @param x {@link #x}にセット
	 * @param y {@link #y}にセット
	 */
	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * {@link Rect}に変換する<BR>
	 * {@link Rect#top}=0<BR>
	 * {@link Rect#left}=0<BR>
	 * {@link Rect#right}={@link #x}<BR>
	 * {@link Rect#bottom}={@link #y}
	 * @return
	 */
	public Rect toRect(){
		return new Rect(0, 0, x, y);
	}
	
	@Override
	public String toString(){
		return super.toString()+"[x:"+x+" y:"+y+"]";
	}
}
