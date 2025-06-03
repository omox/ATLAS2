package dto;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * jQuery jqGrid の JSON 型 ロードモデル
 * 
 */
public class JQEasyModel {

	/**
	 * 宣言
	 */
	/** 総レコード数 */
	private Integer total;
	/** レコード情報(JSONObject) */
	private List<JSONObject> rows;
	/** オプション情報(JSONObject) */
	private List<JSONObject> opts;

	/**
	 * 総レコード数を戻す
	 * 
	 * @return total
	 */
	public Integer getTotal() {
		return total;
	}

	/**
	 * 総レコード数を設定する
	 * 
	 * @param total the total to set
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}

	/**
	 * レコード情報(JSONObject)を戻す
	 * 
	 * @return レコード情報
	 */
	public List<JSONObject> getRows() {
		return rows;
	}

	/**
	 * レコード情報(JQFlotData)を設定する
	 * 
	 * @param rows レコード情報
	 */
	public void setRows(List<JSONObject> rows) {
		this.rows = rows;
	}

	/**
	 * オプション情報(JSONObject)を取得します。
	 * 
	 * @return オプション情報(JSONObject)
	 */
	public List<JSONObject> getOpts() {
		return opts;
	}

	/**
	 * オプション情報(JSONObject)を設定します。
	 * 
	 * @param opts オプション情報(JSONObject)
	 */
	public void setOpts(List<JSONObject> opts) {
		this.opts = opts;
	}
}
