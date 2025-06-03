package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import common.CmnDate;
import common.CmnDate.DATE_FORMAT;
import common.DefineReport;
import common.Defines;
import common.ItemList;
import common.JsonArrayData;

/**
 * T10テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T10Dao extends ItemDao {

	/**
	 * 新しいT10Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T10Dao(Connection source) {
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
	 * Grid表示用の設定を作成する。
	 *
	 * @param header 多段ヘッダの1段目タイトル
	 */
	private void createGridSetting(String header) {
		HashMap<String, String> t10Setting = new HashMap<String, String>();
		t10Setting.put("header", header);
		setSetting(t10Setting);
	}

	/**
	 * 検索文を作成します
	 *
	 * @return 検索<String>
	 */
	private String createCommand() {
		String szYm 		=	getMap().get("YYYY_MM");			// （必須）基準年月
		String szYm2 		=	getMap().get("YYYY_MM2");			// （必須）基準年月

		// パラメータ確認
		if (szYm == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 共通Where句
		String sqlWhere = "TOROKU_YM >= '" + szYm + "' AND TOROKU_YM <= '" + szYm2 + "'";

		// 日付情報取得
		String sqlDtCommand = "select distinct TOROKU_YM FROM T10_SKIN_CHECK WHERE " + sqlWhere + " ORDER BY TOROKU_YM";
		ItemList iL = new ItemList();
		JSONArray dtArray = iL.selectJSONArray(sqlDtCommand, null, Defines.STR_JNDI_DS);
		// 可変情報設定
		String item = "";
		String colWith = "";
		String colSum = "";
		String header = "";
		for (int i = 0; i < dtArray.size(); i++) {
			String val = ObjectUtils.toString(dtArray.getJSONObject(i).get("TOROKU_YM"));
			String txt = CmnDate.month6Format(val, DATE_FORMAT.KANJI_MONTH);
			String id = StringUtils.right(Integer.toString(i), 3);
			item += ",K" + id + ",F" + id;
			colWith += ",case when TOROKU_YM = '" + val + "' then KYAKUSU_J else 0 end";
			colWith += ",case when TOROKU_YM = '" + val + "' then F_JUCGAKU else 0 end";
			colSum += ",sum(K" + id + ") as \"受注客数\",sum(F" + id + ") as \"初回受注金額\"";
			header += txt + ",";
		}
		// 多段ヘッダ用の情報をsettingにセット
		createGridSetting(header + "合計");

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH WORKT(JUCHU_YM" + item + ",KALL,FALL) AS (");
		sbSQL.append(" SELECT CASE WHEN JUCHU_YM = '999999' THEN '00000' ELSE JUCHU_YM END" + colWith + ",KYAKUSU_J,F_JUCGAKU");
		sbSQL.append(" FROM T10_SKIN_CHECK WHERE " + sqlWhere + ")   ");
		sbSQL.append("SELECT JUCHU_YM AS SORT");
		sbSQL.append(" ,CASE WHEN JUCHU_YM = '00000' THEN '購入なし' ELSE TO_CHAR(TO_DATE(JUCHU_YM,'YYYYMM'),'YYYY\"年\"MM\"月\"') END as \"受注年月\"");
		sbSQL.append(colSum);
		sbSQL.append(" ,SUM(KALL) as \"受注客数\",SUM(FALL) as \"初回受注金額\" ");
		sbSQL.append(" FROM WORKT GROUP BY JUCHU_YM");
		sbSQL.append(" union all ");
		sbSQL.append("SELECT 'ZZZZZZ' AS SORT,'合計' as \"受注年月\"" + colSum + ",SUM(KALL) as \"受注客数\",SUM(FALL) as \"初回受注金額\" ");
		sbSQL.append(" FROM WORKT ");
		sbSQL.append("ORDER BY SORT");

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

		// 基準年月
		List<String> cells = new ArrayList<String>();
		cells.add(DefineReport.Select.YM_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		cells.add("～");
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
