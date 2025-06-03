package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.Defines;
import common.ItemList;
import common.JsonArrayData;

/**
 * B02テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B02Dao extends ItemDao {

	public static final String	ID_SQL_SEL_SAVE	= "SELECT * FROM AK_SAVE_B2 T1 WHERE T1.USER_ID = ?";

	/**
	 * 新しいT02Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public B02Dao(Connection source) {
		super(source);
	}

	/**
	 * 検索実行
	 *
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
	 *
	 * @return 検索<String>
	 */
	private String createCommand() {

		String command = null;
		String szJiseki = getMap().get("JISEKI");		// 押下ボタン-1:純売上 2:出荷売上 3:調整売上
		String szYy = getMap().get("YY_KIJUN");			// 開始年
		String szMm = getMap().get("MM_KIJUN");			// 開始月
		String szTenpo = getMap().get("TENPOG");		// 店舗
		String szType2 = getMap().get("H_TYPE2");		// 項目
		String szItemJun = getMap().get("ITEM_JUN");	// 表示項目-純売上
		String szItemShu = getMap().get("ITEM_SHU");	// 表示項目-出荷売上
		String szItemCho = getMap().get("ITEM_CHO");	// 表示項目-調整売上

		JSONArray jsonArrayTenpo = JSONArray.fromObject(StringUtils.split(szTenpo, ","));
		//JSONArray tenpoArray = JSONArray.fromObject(StringUtils.split(szTenpo, ','));
		// 店舗数確認
		if ((jsonArrayTenpo.size() > 25) && (StringUtils.equals(szType2, "1"))){
			System.out.println("TENPO_CNT " + jsonArrayTenpo.size());
			return "";
		}
		System.out.println("TENPO_CNT" + jsonArrayTenpo.size());
		System.out.println(super.getConditionLog());

		// 表示項目SQL作成
		String item1 = "";
		String item2 = "";
		List<ArrayList<String>> itemList = new ArrayList<ArrayList<String>>();			// 選択表示項目情報
		if (StringUtils.equals(szJiseki, DefineReport.Item.ITEM_JUN.getVal())) {		// 純売上
			item1 = ",KYAKUSU,KYAKUSU_R,MOK_KYAKU,JUN_URIGK,JUN_URIGK_R,ZYY_JUN_URIGK,MOK_JUN_GK";
			item2 = ",SUM(KYAKUSU),SUM(KYAKUSU_R),SUM(MOK_KYAKU),SUM(JUN_URIGK),SUM(JUN_URIGK_R),SUM(ZYY_JUN_URIGK),SUM(MOK_JUN_GK)";
			itemList = getSelectJunItem(StringUtils.split(szItemJun, ","));
		} else if (StringUtils.equals(szJiseki, DefineReport.Item.ITEM_SHO.getVal())) {	// 出荷売上
			item1 = ",KYAKUSU,KYAKUSU_R,MOK_KYAKU,SHU_URIGK,SHU_URIGK_R,ZYY_SHU_URIGK,MOK_SHU_GK";
			item2 = ",SUM(KYAKUSU),SUM(KYAKUSU_R),SUM(MOK_KYAKU),SUM(SHU_URIGK),SUM(SHU_URIGK_R),SUM(ZYY_SHU_URIGK),SUM(MOK_SHU_GK)";
			itemList = getSelectShuItem(StringUtils.split(szItemShu, ","));

		} else {																		// 調整売上
			item1 = ",KYAKUSU,KYAKUSU_R,MOK_KYAKU,SHU_URIGK,SHU_URIGK_R,JUN_URIGK,JUN_URIGK_R,CHO_URIGK,CHO_URIGK_R";
			item2 = ",SUM(KYAKUSU),SUM(KYAKUSU_R),SUM(MOK_KYAKU),SUM(SHU_URIGK),SUM(SHU_URIGK_R),SUM(JUN_URIGK),SUM(JUN_URIGK_R),SUM(CHO_URIGK),SUM(CHO_URIGK_R)";
			itemList = getSelectChoItem(StringUtils.split(szItemCho, ","));
		}

		if (StringUtils.equals(szType2, "1")) {  //横表示
			String item3 = "," + StringUtils.join(itemList.get(1).toArray(), ",");	// 通常計算式
			String item4 = "," + StringUtils.join(itemList.get(2).toArray(), ",");	// 合計計算式

			// 店舗ごとのSQL文作成
			String szTenpo_cd = "";		//
			int k = 0;
			String with = ",W0(URI_YMD" + item1 + ") AS (SELECT URI_YMD" + item2 + " FROM W00 GROUP BY URI_YMD)";
			String select1 = item3;
			String select2 = item4;
			String from = "LEFT OUTER JOIN W0 ON (WK_YMD=W0.URI_YMD) ";
			for (int i = 0; i < jsonArrayTenpo.size(); i++) {
				if (i != 0) szTenpo_cd += ",";
				szTenpo_cd += "'" + jsonArrayTenpo.get(i).toString() + "'";
				k = i + 1;
				with += ",W" + k + "(URI_YMD" + item1 + ") AS (SELECT URI_YMD" + item1 + " FROM W00";
				with += " WHERE TENPO_CD = '" + jsonArrayTenpo.get(i).toString() + "') ";
				select1 += StringUtils.replace(item3, "W0.", "W" + k + ".");
				select2 += StringUtils.replace(item4, "W0.", "W" + k + ".");
				from += "LEFT OUTER JOIN W" + k + " ON (WK_YMD=W" + k + ".URI_YMD) ";
			}
			System.out.println("szTenpo_cd:" + szTenpo_cd);

			// 検索文（SQL組立て）
			StringBuffer sbSQL = new StringBuffer();
			sbSQL.append("WITH W00(URI_YMD,TENPO_CD" + item1 + ") AS (");
			sbSQL.append("  SELECT URI_YMD,TENPO_CD" + item2);
			sbSQL.append("  FROM URI_TEN");
			sbSQL.append("  WHERE SUBSTR(URI_YMD,1,6) = '" + szYy + szMm + "' AND TENPO_CD IN (" + szTenpo_cd + "） GROUP BY URI_YMD,TENPO_CD )");
			sbSQL.append(",WK(WK_YMD) AS (SELECT URI_YMD FROM W00 GROUP BY URI_YMD)");
			sbSQL.append(with);
			sbSQL.append(" SELECT WK_YMD AS SORT,SUBSTR(WK_YMD,1,4)||'年'||SUBSTR(WK_YMD,5,2)||'月'||SUBSTR(WK_YMD,7,2)||'日' as \"年月日\"" + select1);
			sbSQL.append(" FROM WK " + from + "GROUP BY WK_YMD ");
			sbSQL.append("UNION ALL SELECT '9999' AS SORT,'合計' as \"年月日\"" + select2);
			sbSQL.append(" FROM WK " + from);
			sbSQL.append(" ORDER BY SORT");

			System.out.println(sbSQL.toString());
			command = sbSQL.toString();
			return command;
		} else {
			String tenpoCd = "";
			for (int i = 0; i < jsonArrayTenpo.size(); i++) {
				tenpoCd += "'" + jsonArrayTenpo.get(i).toString() + "',";
			}
			tenpoCd = StringUtils.removeEnd(tenpoCd, ",");

			// 日付情報取得
			String sqlDtCommand = "SELECT URI_YMD FROM URI_TEN WHERE URI_YMD LIKE '" + szYy + szMm + "%' GROUP BY URI_YMD";
			ItemList iL = new ItemList();
			JSONArray dtArray = iL.selectJSONArray(sqlDtCommand, null, Defines.STR_JNDI_DS);

			// 表示項目×日付SQL作成
			String union1 = "";
			String union2 = "";
			for (int idx = 0; idx < itemList.get(0).size(); idx++) {
				String name = itemList.get(0).get(idx);										// 項目名取得
				if (idx != 0) {
					union1 += " UNION ALL SELECT TENPO_CD||'" + idx + 1 + "' AS SORT, '' AS \"店舗\", '" + name + "' as \"項目\"";
					union2 += " UNION ALL SELECT '00" + idx + 1 + "' AS SORT, '' AS \"店舗\", '" + name + "' as \"項目\"";
				} else {
					union1 += " UNION ALL SELECT TENPO_CD||'" + idx + 1 + "' AS SORT, TENPO_NM AS \"店舗\", '" + name + "' as \"項目\"";
					union2 += " UNION ALL SELECT '00" + idx + 1 + "' AS SORT, '店舗計' AS \"店舗\", '" + name + "' as \"項目\"";
				}
				String cal1 = StringUtils.replace(itemList.get(1).get(idx), "W0.", "W.");	// 通常式取得
				String cal2 = StringUtils.replace(itemList.get(2).get(idx), "W0.", "W.");	// 合計式取得
				for (int i = 0; i < dtArray.size(); i++) {
					String dt = ObjectUtils.toString(dtArray.getJSONObject(i).get("URI_YMD"));
					union1 += ",(SELECT " + cal1 + " FROM W00 W WHERE W.TENPO_CD = WK.TENPO_CD AND W.URI_YMD = '" + dt + "') as \"" + dt + "\"";
					union2 += ",(SELECT " + cal1 + " FROM WK00 W WHERE W.URI_YMD = '" + dt + "') as \"" + dt + "\"";
				}
				union1 += ",(SELECT " + cal2 + " FROM W00 W WHERE W.TENPO_CD = WK.TENPO_CD) as \"月計\" FROM WK ";
				union2 += ",(SELECT " + cal2 + " FROM WK00 W) as \"月計\" FROM WK_D ";
			}
			union1 = StringUtils.removeStart(union1, " UNION ALL");

			// 検索文（SQL組立て）
			StringBuffer sbSQL = new StringBuffer();
			sbSQL.append("WITH W00(URI_YMD,TENPO_CD" + item1 + ") AS (");
			sbSQL.append("  SELECT URI_YMD,TENPO_CD" + item2);
			sbSQL.append("  FROM URI_TEN");
			sbSQL.append("  WHERE SUBSTR(URI_YMD,1,6) = '" + szYy + szMm + "'");
			if (StringUtils.isNotEmpty(tenpoCd)) {
				sbSQL.append(" AND TENPO_CD IN (" + tenpoCd + ")");
			}
			sbSQL.append("  GROUP BY URI_YMD,TENPO_CD)");
			sbSQL.append(",WK(TENPO_CD,TENPO_NM) AS (");
			sbSQL.append("  SELECT distinct T.TENPO_CD,M.TENPO_NM");
			sbSQL.append("  FROM W00 T inner join M_TENPO M on T.TENPO_CD = M.TENPO_CD)");
			sbSQL.append(",WK00(URI_YMD" + item1 + ") AS (SELECT URI_YMD" + item2 + " FROM W00 GROUP BY URI_YMD)");
			sbSQL.append(",WK_D(D_URI_YMD) AS ( SELECT MAX(URI_YMD) FROM W00)");
			sbSQL.append(union1);
			sbSQL.append(union2);
			sbSQL.append(" ORDER BY SORT");

			System.out.println(sbSQL.toString());
			command = sbSQL.toString();
			return command;
		}
	}

	/**
	 * 純売上SELECT項目を取得する。
	 *
	 * @param vals 表示項目チェック値
	 * @return [0]純売上SELECT項目名 [1]純売上SELECT項目 [2]純売上SELECT合計項目
	 */
	protected List<ArrayList<String>> getSelectJunItem(String[] vals) {
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> sel1 = new ArrayList<String>();
		ArrayList<String> sel2 = new ArrayList<String>();

		// レジ客数
		if (StringUtils.equals(vals[0], "1")) {
			name.add("レジ客数");
			sel1.add("SUM(W0.KYAKUSU) ");
			sel2.add("SUM(W0.KYAKUSU) ");
		}
		// レジ客数累計
		if (StringUtils.equals(vals[1], "1")) {
			name.add("レジ客数累計");
			sel1.add("SUM(W0.KYAKUSU_R) ");
			sel2.add("MAX(W0.KYAKUSU_R) ");
		}
		// 計画客数
		if (StringUtils.equals(vals[2], "1")) {
			name.add("計画客数");
			sel1.add("SUM(W0.MOK_KYAKU) ");
			sel2.add("MAX(W0.MOK_KYAKU) ");
		}
		// 客数達成率
		if (StringUtils.equals(vals[3], "1")) {
			name.add("客数達成率");
			sel1.add("CASE WHEN SUM(W0.MOK_KYAKU) = 0 THEN 0 ELSE SUM(W0.KYAKUSU_R)/SUM(W0.MOK_KYAKU)*100 END ");
			sel2.add("CASE WHEN MAX(W0.MOK_KYAKU) = 0 THEN 0 ELSE MAX(W0.KYAKUSU_R)/MAX(W0.MOK_KYAKU)*100 END ");
		}
		// 単価
		if (StringUtils.equals(vals[4], "1")) {
			name.add("単価");
			sel1.add("CASE WHEN SUM(W0.KYAKUSU) = 0 THEN 0 ELSE SUM(W0.JUN_URIGK)/SUM(W0.KYAKUSU) END ");
			sel2.add("CASE WHEN SUM(W0.KYAKUSU) = 0 THEN 0 ELSE SUM(W0.JUN_URIGK)/SUM(W0.KYAKUSU) END ");
		}
		// 純売上
		if (StringUtils.equals(vals[5], "1")) {
			name.add("純売上");
			sel1.add("ROUND(SUM(W0.JUN_URIGK)) ");
			sel2.add("ROUND(SUM(W0.JUN_URIGK)) ");
		}
		// 純売上累計
		if (StringUtils.equals(vals[6], "1")) {
			name.add("純売上累計");
			sel1.add("ROUND(SUM(W0.JUN_URIGK_R)) ");
			sel2.add("ROUND(MAX(W0.JUN_URIGK_R)) ");
		}
		// 純売上累計前年比
		if (StringUtils.equals(vals[7], "1")) {
			name.add("前年比");
			sel1.add("CASE WHEN SUM(W0.ZYY_JUN_URIGK) = 0 THEN 0 ELSE SUM(W0.JUN_URIGK_R)/SUM(W0.ZYY_JUN_URIGK)*100 END ");
			sel2.add("CASE WHEN SUM(W0.ZYY_JUN_URIGK) = 0 THEN 0 ELSE MAX(W0.JUN_URIGK_R)/MAX(W0.ZYY_JUN_URIGK)*100 END ");
		}
		// 予算
		if (StringUtils.equals(vals[8], "1")) {
			name.add("予算");
			sel1.add("SUM(W0.MOK_JUN_GK) ");
			sel2.add("MAX(W0.MOK_JUN_GK) ");
		}
		// 売上達成率
		if (StringUtils.equals(vals[9], "1")) {
			name.add("売上達成率");
			sel1.add("CASE WHEN SUM(W0.MOK_JUN_GK) = 0 THEN 0 ELSE SUM(W0.JUN_URIGK_R)/SUM(W0.MOK_JUN_GK)*100 END ");
			sel2.add("CASE WHEN MAX(W0.MOK_JUN_GK) = 0 THEN 0 ELSE MAX(W0.JUN_URIGK_R)/MAX(W0.MOK_JUN_GK)*100 END ");
		}
		return new ArrayList<ArrayList<String>>(Arrays.asList(name, sel1, sel2));
	}

	/**
	 * 出荷売上SELECT項目を取得する。
	 *
	 * @param vals 表示項目チェック値
	 * @return [0]出荷上SELECT項目 [1]出荷上合計SELECT項目
	 */
	protected ArrayList<ArrayList<String>> getSelectShuItem(String[] vals) {
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> sel1 = new ArrayList<String>();
		ArrayList<String> sel2 = new ArrayList<String>();

		// レジ客数
		if (StringUtils.equals(vals[0], "1")) {
			name.add("レジ客数");
			sel1.add("SUM(W0.KYAKUSU) ");
			sel2.add("SUM(W0.KYAKUSU) ");
		}
		// レジ客数累計
		if (StringUtils.equals(vals[1], "1")) {
			name.add("レジ客数累計");
			sel1.add("SUM(W0.KYAKUSU_R) ");
			sel2.add("MAX(W0.KYAKUSU_R) ");
		}
		// 計画客数
		if (StringUtils.equals(vals[2], "1")) {
			name.add("計画客数");
			sel1.add("SUM(W0.MOK_KYAKU) ");
			sel2.add("MAX(W0.MOK_KYAKU) ");
		}
		// 客数達成率
		if (StringUtils.equals(vals[3], "1")) {
			name.add("客数達成率");
			sel1.add("CASE WHEN SUM(W0.MOK_KYAKU) = 0 THEN 0 ELSE SUM(W0.KYAKUSU_R)/SUM(W0.MOK_KYAKU)*100 END ");
			sel2.add("CASE WHEN MAX(W0.MOK_KYAKU) = 0 THEN 0 ELSE MAX(W0.KYAKUSU_R)/MAX(W0.MOK_KYAKU)*100 END ");
		}
		// 単価
		if (StringUtils.equals(vals[4], "1")) {
			name.add("単価");
			sel1.add("CASE WHEN SUM(W0.KYAKUSU) = 0 THEN 0 ELSE SUM(W0.SHU_URIGK)/SUM(W0.KYAKUSU) END ");
			sel2.add("CASE WHEN SUM(W0.KYAKUSU) = 0 THEN 0 ELSE SUM(W0.SHU_URIGK)/SUM(W0.KYAKUSU) END ");
		}
		// 出荷売上
		if (StringUtils.equals(vals[5], "1")) {
			name.add("出荷売上");
			sel1.add("SUM(W0.SHU_URIGK) ");
			sel2.add("SUM(W0.SHU_URIGK) ");
		}
		// 出荷売上累計
		if (StringUtils.equals(vals[6], "1")) {
			name.add("出荷売上累計");
			sel1.add("SUM(W0.SHU_URIGK_R)");
			sel2.add("MAX(W0.SHU_URIGK_R)");
		}
		// 出荷売上累計前年比
		if (StringUtils.equals(vals[7], "1")) {
			name.add("前年比");
			sel1.add("CASE WHEN SUM(W0.ZYY_SHU_URIGK) = 0 THEN 0 ELSE SUM(W0.SHU_URIGK_R)/SUM(W0.ZYY_SHU_URIGK)*100 END ");
			sel2.add("CASE WHEN SUM(W0.ZYY_SHU_URIGK) = 0 THEN 0 ELSE MAX(W0.SHU_URIGK_R)/MAX(W0.ZYY_SHU_URIGK)*100 END ");
		}
		// 計画出荷売上
		if (StringUtils.equals(vals[8], "1")) {
			name.add("計画出荷売上");
			sel1.add("SUM(W0.MOK_SHU_GK) ");
			sel2.add("MAX(W0.MOK_SHU_GK) ");
		}
		// 売上達成率
		if (StringUtils.equals(vals[9], "1")) {
			name.add("売上達成率");
			sel1.add("CASE WHEN SUM(W0.MOK_SHU_GK) = 0 THEN 0 ELSE SUM(W0.SHU_URIGK_R)/SUM(W0.MOK_SHU_GK)*100 END ");
			sel2.add("CASE WHEN MAX(W0.MOK_SHU_GK) = 0 THEN 0 ELSE MAX(W0.SHU_URIGK_R)/MAX(W0.MOK_SHU_GK)*100 END ");
		}
		return new ArrayList<ArrayList<String>>(Arrays.asList(name, sel1, sel2));
	}

	/**
	 * 調整売上SELECT項目を取得する。
	 *
	 * @param vals 表示項目チェック値
	 * @return [0]調整上SELECT項目 [1]調整上合計SELECT項目
	 */
	protected ArrayList<ArrayList<String>> getSelectChoItem(String[] vals) {
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> sel1 = new ArrayList<String>();
		ArrayList<String> sel2 = new ArrayList<String>();

		// レジ客数
		if (StringUtils.equals(vals[0], "1")) {
			name.add("レジ客数");
			sel1.add("SUM(W0.KYAKUSU) ");
			sel2.add("SUM(W0.KYAKUSU) ");
		}
		// レジ客数累計
		if (StringUtils.equals(vals[1], "1")) {
			name.add("レジ客数累計");
			sel1.add("SUM(W0.KYAKUSU_R) ");
			sel2.add("MAX(W0.KYAKUSU_R) ");
		}
		// 純売上
		if (StringUtils.equals(vals[2], "1")) {
			name.add("純売上");
			sel1.add("SUM(W0.JUN_URIGK) ");
			sel2.add("SUM(W0.JUN_URIGK) ");
		}
		// 純売上累計
		if (StringUtils.equals(vals[3], "1")) {
			name.add("純売上累計");
			sel1.add("SUM(W0.JUN_URIGK_R)");
			sel2.add("MAX(W0.JUN_URIGK_R)");
		}
		// 出荷売上
		if (StringUtils.equals(vals[4], "1")) {
			name.add("出荷売上");
			sel1.add("SUM(W0.SHU_URIGK) ");
			sel2.add("SUM(W0.SHU_URIGK) ");
		}
		// 出荷売上累計
		if (StringUtils.equals(vals[5], "1")) {
			name.add("出荷売上累計");
			sel1.add("SUM(W0.SHU_URIGK_R)");
			sel2.add("MAX(W0.SHU_URIGK_R)");
		}
		// 調整売上
		if (StringUtils.equals(vals[6], "1")) {
			name.add("調整売上");
			sel1.add("SUM(W0.CHO_URIGK) ");
			sel2.add("SUM(W0.CHO_URIGK) ");
		}
		// 調整売上累計
		if (StringUtils.equals(vals[7], "1")) {
			name.add("調整売上累計");
			sel1.add("SUM(W0.CHO_URIGK_R) ");
			sel2.add("MAX(W0.CHO_URIGK_R) ");
		}
		return new ArrayList<ArrayList<String>>(Arrays.asList(name, sel1, sel2));
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
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

	/**
	 * 検索条件を保管する。
	 *
	 * @param map 画面項目情報
	 */
	public static void saveCondition(HashMap<String, String> map) {
		ItemList il = new ItemList();
		try {
			il.executeItem(getSqlSave(map), new ArrayList<String>(), Defines.STR_JNDI_DS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 検索条件保管SQLを生成します。
	 *
	 * @param map 画面項目情報
	 * @return 検索条件保管SQL文
	 */
	private static String getSqlSave(HashMap<String, String> map) {
		String userId = map.get("USER_ID");				// ユーザーID
		String szYy = map.get("YY_KIJUN");				// 年
		String szMm = map.get("MM_KIJUN");				// 月
		String szTenpo = map.get("TENPOG");				// 店舗
		String szType2 = map.get("H_TYPE2");			// 軸項目
		String szItemJun = map.get("ITEM_JUN");			// 表示項目-純売上
		String szItemShu = map.get("ITEM_SHU");			// 表示項目-出荷売上
		String szItemCho = map.get("ITEM_CHO");			// 表示項目-調整売上

		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("MERGE INTO AK_SAVE_B2 ");
		sbSQL.append("USING (SELECT '" + userId + "' as USER_ID FROM DUAL) WK ON (AK_SAVE_B2.USER_ID = WK.USER_ID) ");
		// 既存レコードの更新
		sbSQL.append("WHEN MATCHED THEN UPDATE SET");
		sbSQL.append(" YY_SEL = '" + szYy + "',");				// 年
		sbSQL.append(" MM_SEL = '" + szMm + "',");				// 月
		sbSQL.append(" CD_TENPO_DETAIL = '" + szTenpo + "',");	// 店舗
		sbSQL.append(" TYPE = '" + szType2 + "',");				// 軸項目
		sbSQL.append(" VIEW_ITEM_JUN = '" + szItemJun + "',");	// 表示項目-純売上
		sbSQL.append(" VIEW_ITEM_SHU = '" + szItemShu + "',");	// 表示項目-出荷売上
		sbSQL.append(" VIEW_ITEM_CHO = '" + szItemCho + "' ");	// 表示項目-調整売上
		// -- 新規レコードの作成
		sbSQL.append("WHEN NOT MATCHED THEN INSERT VALUES (");
		sbSQL.append(" '" + userId + "',");		// ユーザーID
		sbSQL.append(" '" + szYy + "',");		// 年
		sbSQL.append(" '" + szMm + "',");		// 月
		sbSQL.append(" '" + szTenpo + "',");	// 店舗
		sbSQL.append(" '" + szType2 + "',");	// 軸項目
		sbSQL.append(" '" + szItemJun + "',");	// 表示項目-純売上
		sbSQL.append(" '" + szItemShu + "',");	// 表示項目-出荷売上
		sbSQL.append(" '" + szItemCho + "')");	// 表示項目-調整売上

		System.out.println("保管SQL:" + sbSQL.toString());
		return sbSQL.toString();
	}
}
