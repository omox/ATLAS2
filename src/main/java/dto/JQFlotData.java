/**
 *
 */
package dto;

import java.util.List;

/**
 * jQuery Flot の JSON 型 ロードモデル
 * レコード情報
 */
public class JQFlotData {

	/**
	 * 宣言
	 */
	private List<List<String>> data;
	private String label;

	/**
	 * 情報を戻す
	 * @return セル情報
	 */
	public List<List<String>> getData() {
		return data;
	}
	/**
	 * 情報を設定する
	 * @param data 情報
	 */
	public void setData(List<List<String>> data) {
		this.data = data;
	}

	/**
	 * ラベルを戻す
	 * @return ラベル
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * ラベルを設定する
	 * @param label ラベル
	 */
	public void setLabel(String label) {
		this.label = label;
	}

}
