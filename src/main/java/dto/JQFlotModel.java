package dto;

import java.util.List;
/**
 * jQuery jqGrid の JSON 型 ロードモデル 
 *
 */
public class JQFlotModel {

	/**
	 * 宣言
	 */
	private List<JQFlotData> rows;

	/**
	 * レコード情報(JQFlotData)を戻す
	 * @return レコード情報
	 */
	public List<JQFlotData> getRows() {
		return rows;
	}
	/**
	 * レコード情報(JQFlotData)を設定する
	 * @param rows
	 */
	public void setRows(List<JQFlotData> rows) {
		this.rows = rows;
	}
}
