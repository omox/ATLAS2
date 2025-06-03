package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.Defines;
import common.ItemList;
import common.JsonArrayData;
import common.StringConverter;

/**
 * T14テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T14Dao extends ItemDao {

	/**
	 * 新しいT14Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T14Dao(Connection source) {
		super(source);
	}

	/**
	 * 検索実行
	 * @return boolean
	 */
	public boolean selectBy() {

		// 検索コマンド生成
		String command = createCommand();

		// 出力用検索条件生成
		outputQueryList();

		// 検索実行
		return super.selectBySQL(command);
	}


	/**
	 * 検索文を作成します
	 * @return 検索<String>
	 */
	private String createCommand() {
		String szYm 		=	getMap().get("YYYY_MM");			//

		// パラメータ確認
		if (szYm == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 店舗情報取得
		String sqlTnpCommand = "select M.TENPO_CD, M.TENPO_NM from M_TENPO M where EXISTS(select 'X' from T14_TENPO T where T.YYYY_MM = '"
				+ szYm + "' and T.TENPO_CD_L = M.TENPO_CD) order by M.TENPO_CD";
		ItemList iL = new ItemList();
		JSONArray tnpArray = iL.selectJSONArray(sqlTnpCommand, null, Defines.STR_JNDI_DS);
		// 可変情報設定
		String itemTnp = "";
		String colWith = "";
		String colItem = "";
		for (int i = 0; i < tnpArray.size(); i++) {
			String val = ObjectUtils.toString(tnpArray.getJSONObject(i).get("TENPO_CD"));
			String txt = ObjectUtils.toString(tnpArray.getJSONObject(i).get("TENPO_NM"));
			txt = StringConverter.convertToLengthB(txt, 20);

			String t = "T" + StringUtils.leftPad(Integer.toString(i), 3, "0");
			itemTnp += "," + t;
			colWith += ",case when TENPO_CD_L = '" + val + "' then KYAKUSU else 0 end";
			colItem += ",sum(" + t + ") as \"" + txt + "\"";
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH WORKT(TENPO_CDT" + itemTnp + ",T999,TALL) AS (");
		sbSQL.append(" SELECT TENPO_CD_B" + colWith+ ",case when TENPO_CD_L = '999' then KYAKUSU else 0 end,KYAKUSU ");
		sbSQL.append(" FROM T14_TENPO WHERE YYYY_MM = '" + szYm + "')   ");
//		sbSQL.append("SELECT COALESCE(TENPO_CD,'999') AS SORT,COALESCE(MAX(TENPO_NM),'不明') as \"登録店舗×最終利用店\"" + colItem+ ",SUM(T999) AS \"不明\",SUM(TALL) AS \"合計\"");
		sbSQL.append("SELECT COALESCE(TENPO_CD,'999') AS SORT,COALESCE(MAX(TENPO_NM),'不明') as \"登録店舗×最終利用店\"" + colItem+ ",SUM(TALL) AS \"合計\"");
		sbSQL.append(" FROM WORKT LEFT OUTER JOIN M_TENPO ON TENPO_CDT = TENPO_CD");
		sbSQL.append(" GROUP BY TENPO_CD ");
		sbSQL.append(" UNION ALL SELECT '9999' AS SORT,'合計' as \"登録店舗×最終利用店\"" + colItem+ ",SUM(TALL) AS \"合計\"");
		sbSQL.append(" FROM WORKT ");
		sbSQL.append(" ORDER BY SORT");

		System.out.println(sbSQL.toString());
		return sbSQL.toString();
	}

	/**
	 * 検索条件表示用情報の生成
	 */
	private void outputQueryList() {
		// 検索条件の加工クラス作成
		JsonArrayData jad = new JsonArrayData();
		jad.setJsonString(getJson());

		// 保存用 List (検索情報)作成
		setWhere(new ArrayList<List<String>>());

		// 年月
		List<String> cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
