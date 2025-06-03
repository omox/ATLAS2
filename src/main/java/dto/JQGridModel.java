package dto;

import java.util.List;

/**
 * jQuery jqGrid の JSON 型 ロードモデル
 * 
 */
public class JQGridModel {

	/**
	 * 宣言
	 */
	/** ページ番号 */
	private String page;
	/** 全ページ数 */
	private String total;
	/** レコード数 */
	private Integer records;
	/** レコード情報(JQGridRow) */
	private List<JQGridData> rows;


		/**
	 * ページ番号を戻す
	 * 
	 * @return ページ番号
	 */
	public String getPage() {
		return page;
	}

	/**
	 * ページ番号を指定する
	 * 
	 * @param page
	 *            ページ番号
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * 全ページ数を戻す
	 * 
	 * @return 全ページ数
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * 全ページ数を設定する
	 * 
	 * @param total
	 *            全ページ数
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * レコード数を戻す
	 * 
	 * @return レコード数
	 */
	public Integer getRecords() {
		return records;
	}

	/**
	 * レコード数を設定する
	 * 
	 * @param records
	 *            レコード数
	 */
	public void setRecords(Integer records) {
		this.records = records;
	}

	/**
	 * レコード情報(JQGridRow)を戻す
	 * 
	 * @return レコード情報
	 */
	public List<JQGridData> getRows() {
		return rows;
	}

	/**
	 * レコード情報(JQGridRow)を設定する
	 * 
	 * @param rows
	 *            レコード情報
	 */
	public void setRows(List<JQGridData> rows) {
		this.rows = rows;
	}
}
