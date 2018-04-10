package com.kapp.library.widget.sidebar;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 *@author xiaobo.cui 2014年11月24日 下午5:36:29
 *
 */
public abstract class BaseSideInfo {

	public abstract String contactName();
	public abstract String contactNumber();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contactName() == null) ? 0 : contactName().hashCode());
		result = prime * result + ((contactName() == null) ? 0 : contactName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseSideInfo other = (BaseSideInfo) obj;
		if (contactName() == null) {
			if (other.contactName() != null)
				return false;
		} else if (!contactName().equals(other.contactName()))
			return false;
		if (contactNumber() == null) {
			if (other.contactNumber() != null)
				return false;
		} else if (!contactNumber().equals(other.contactNumber()))
			return false;
		return true;
	}

	/** 获取名字的首字母 */
	public String getSortLetters(){
		return getSortLetter(contactName());
	}

	public String getSimpleNumber(){
		if(TextUtils.isEmpty(contactNumber())){
			return contactNumber().replaceAll("\\-|\\s", "");
		}
		return null;
	}

	/**
	 * 名字转拼音,取首字母
	 *
	 * @param name
	 * @return
	 */
	private String getSortLetter(String name) {
		String letter = "#";
		if (TextUtils.isEmpty(name)) {
			return letter;
		}
		Log.i("main", "name:"+name);
		// 汉字转换成拼音
		String pinyin = CharacterParser.getInstance().getSelling(name);
		Log.i("main", "pinyins:"+pinyin);
		String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			letter = sortString.toUpperCase(Locale.CHINESE);
		}
		return letter;
	}

}
