/*
 * 作成日: 2006/12/05
 *
 */
package common;

import java.util.Date;

/**
 * システム利用可能時間帯チェッククラス
 */
public class ChkSysTime {
	/* 開始・終了時刻 */
	private String from_ = Defines.STR_SYS_START;
	private String to_ = Defines.STR_SYS_END;

	private CmnDate dayfrom_;
	private CmnDate dayto_;

	/**
	 * コンストラクタ
	 */
	public ChkSysTime() {
		this.setSysTime();
	}

	/**
	 * システム開始～終了時刻設定
	 */
	public void setSysTime() {
		dayfrom_ = new CmnDate();
		dayfrom_.setTime(from_);
		dayto_ = new CmnDate();
		dayto_.setTime(to_);
	}

	/**
	 * 利用時間Fromを返す
	 * @return 利用時間From
	 */
	public String sysFrom(){
		return dayfrom_.getTime(":");
	}

	/**
	 * 利用時間Toを返す
	 * @return 利用時間To
	 */
	public String sysTo(){
		return dayto_.getTime(":");
	}

	/**
	 * 利用可能時間内かを返す
	 *
	 * @return boolean
	 */
	public boolean isUsefull() {
		if(true) {return true;}	// 無効化

		Date day = new Date();
		this.setSysTime();

		//  from <= systime <= to はOK
		if ((day.compareTo(dayfrom_) == -1) || (day.compareTo(dayto_) == 1)) return false;
		return true;
	}
}
