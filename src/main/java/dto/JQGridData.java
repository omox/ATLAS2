/**
 * 
 */
package dto;

import java.util.List;

/**
 * jQuery jqGrid の JSON 型 ロードモデル 
 * レコード情報
 */
public class JQGridData {

	/**
	 * 宣言
	 */
	private Integer id;
	private List<String> cell;

	/**
	 * レコードID（一意）を戻す
	 * @return レコードID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * レコードIDを設定する
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * セル（列）情報を戻す
	 * @return セル情報
	 */
	public List<String> getCell() {
		return cell;
	}
	/**
	 * セル（列）情報を設定する
	 * @param cell
	 */
	public void setCell(List<String> cell) {
		this.cell = cell;
	}
	

}
