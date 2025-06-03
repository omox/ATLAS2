package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * B02aテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B02aDao extends ItemDao {

	/**
	 * 新しいB02aDaoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public B02aDao(Connection source) {
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
		String szRad_B1 = getMap().get("RAD_B1");	// 照会データ指定-1:日別 2:月別
		String szYm = getMap().get("YM_KIJUN");		// 年月
		String szYm2 = getMap().get("YM_KIJUN2");	// 年月
		String szDd = getMap().get("DD_KIJUN");		// 日
		String szDd2 = getMap().get("DD_KIJUN2");	// 日
		String szTenpo = getMap().get("TENPOG");	// 店舗
		String szType = getMap().get("H_TYPE");		// 表示項目

		// パラメータ確認
		if (szYm == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());
		final String cmnItem = ",T1,T2,T3,T4,T5,T6,T0";
		final String cmnSelect = ",ROUND(SUM(T1)),ROUND(SUM(T2)),ROUND(SUM(T3)),ROUND(SUM(T4)),ROUND(SUM(T5)),ROUND(SUM(T6)),ROUND(SUM(T0))";
		// 店舗別可変情報
		String tenpoCd = "";
		String item = "";
		String select1 = "";
		String select2 = "";
		JSONArray jsonArrayTenpo = JSONArray.fromObject(StringUtils.split(szTenpo, ","));
		for (int i = 0; i < jsonArrayTenpo.size(); i++) {
			String cd = jsonArrayTenpo.get(i).toString();
			tenpoCd += "'" + cd + "',";
			item += StringUtils.replace(cmnItem, "T", "W" + cd);
			for (int j = 1; j < 7; j++) {
				select1 += ",case when TENPO_CD = '" + cd + "' then SUM(T" + j + ") else null end";
			}
			select1 += ",case when TENPO_CD = '" + cd + "' then SUM(T0) else null end";
			select2 += StringUtils.replace(cmnSelect, "T", "W" + cd);
		}
		tenpoCd = StringUtils.removeEnd(tenpoCd, ",");

		// 年月日可変情報
		String whereDt = "";			//1日別 2:月別
		String sel_Ym = "";			//
		String sel_Ym2 = "";			//
		if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID3.getVal())) {  //1:日別
			whereDt = "WHERE URI_YMD >= '" + szYm + szDd + "' and URI_YMD <= '" + szYm2 + szDd2 + "' ";
			sel_Ym = " URI_YMD ";
			if (StringUtils.equals(szType, "5")) {  //すべての時
				sel_Ym2 = "CASE WHEN KBN = 1 THEN  substr(URI_YMD,1,4)||'年'|| substr(URI_YMD,5,2)||'月'||substr(URI_YMD,7,2)||'日' ELSE '' END";
			} else  {
				sel_Ym2 = "substr(URI_YMD,1,4)||'年'|| substr(URI_YMD,5,2)||'月'||substr(URI_YMD,7,2)||'日'";
			}
		} else  {
			whereDt = "WHERE SUBSTR(URI_YMD,1,6) >= '" + szYm + "' and SUBSTR(URI_YMD,1,6) <= '" + szYm2 + "' ";
			sel_Ym = " SUBSTR(URI_YMD,1,6) ";
			if (StringUtils.equals(szType, "5")) {  //すべての時
				sel_Ym2 = "CASE WHEN KBN = 1 THEN  substr(URI_YMD,1,4)||'年'|| substr(URI_YMD,5,2)||'月' ELSE '' END";
			} else  {
				sel_Ym2 = "substr(URI_YMD,1,4)||'年'|| substr(URI_YMD,5,2)||'月'";
			}
		}

		// 表示項目別可変情報
		String sel_Type = "'合計'";
		String unionType = "";
		if (StringUtils.equals(szType, "5")) {
			sel_Type = "CASE WHEN KBN = 1 THEN '合計' ELSE '' END";
			unionType = " union all ";
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH W00(URI_YMD,TENPO_CD,NR_KBN,KYAKUSU,SHU_URIGK,JUN_URIGK,CHOSEI_URIGK) AS (");
		sbSQL.append("  SELECT " + sel_Ym + ",TENPO_CD,NR_KBN,SUM(KYAKUSU),SUM(SHU_URIGK),SUM(JUN_URIGK),SUM(CHOSEI_URIGK)");
		sbSQL.append("  FROM URI_TEN_KYAKU " + whereDt);
		if (!StringUtils.equals(tenpoCd, "'00'")) { // 全店舗以外
			sbSQL.append(" and TENPO_CD in (" + tenpoCd + ") ");
		}
		sbSQL.append("  GROUP BY " + sel_Ym + ",TENPO_CD,NR_KBN)");
		// 表示項目集計
		sbSQL.append(",W0(URI_YMD,TENPO_CD,KBN,KBNNM,T0,T1,T2,T3,T4,T5,T6) AS (");
		if (StringUtils.equals(szType, "1") || StringUtils.equals(szType, "5")) {	// 表示項目-すべて/レジ客数
			sbSQL.append(" SELECT URI_YMD,TENPO_CD,1,'レジ客数',SUM(KYAKUSU)");
			sbSQL.append(" ,CASE WHEN NR_KBN = '11' THEN SUM(KYAKUSU) ELSE 0 END ,CASE WHEN NR_KBN = '21' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN NR_KBN = '12' THEN SUM(KYAKUSU) ELSE 0 END ,CASE WHEN NR_KBN = '22' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN NR_KBN = '31' THEN SUM(KYAKUSU) ELSE 0 END ,CASE WHEN NR_KBN = '41' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" FROM W00 GROUP BY URI_YMD,TENPO_CD,NR_KBN ");
		}
		if (StringUtils.equals(szType, "2") || StringUtils.equals(szType, "5")) {	// 表示項目-すべて/純売上
			sbSQL.append(unionType);
			sbSQL.append(" SELECT URI_YMD,TENPO_CD,3,'純売上',SUM(JUN_URIGK)");
			sbSQL.append(" ,CASE WHEN NR_KBN = '11' THEN SUM(JUN_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '21' THEN SUM(JUN_URIGK) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN NR_KBN = '12' THEN SUM(JUN_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '22' THEN SUM(JUN_URIGK) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN NR_KBN = '31' THEN SUM(JUN_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '41' THEN SUM(JUN_URIGK) ELSE 0 END");
			sbSQL.append(" FROM W00 GROUP BY URI_YMD,TENPO_CD,NR_KBN ");
		}
		if (StringUtils.equals(szType, "3") || StringUtils.equals(szType, "5")) {	// 表示項目-すべて/出荷売上
			sbSQL.append(unionType);
			sbSQL.append(" SELECT URI_YMD,TENPO_CD,2,'出荷売上',SUM(SHU_URIGK)");
			sbSQL.append(" ,CASE WHEN NR_KBN = '11' THEN SUM(SHU_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '21' THEN SUM(SHU_URIGK) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN NR_KBN = '12' THEN SUM(SHU_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '22' THEN SUM(SHU_URIGK) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN NR_KBN = '31' THEN SUM(SHU_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '41' THEN SUM(SHU_URIGK) ELSE 0 END");
			sbSQL.append(" FROM W00 GROUP BY URI_YMD,TENPO_CD,NR_KBN ");
		}
		if (StringUtils.equals(szType, "4") || StringUtils.equals(szType, "5")) {	// 表示項目-すべて/調整前純売上
			sbSQL.append(unionType);
			sbSQL.append(" SELECT URI_YMD,TENPO_CD,4,'調整前純売上',SUM(CHOSEI_URIGK)");
			sbSQL.append(" ,CASE WHEN NR_KBN = '11' THEN SUM(CHOSEI_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '21' THEN SUM(CHOSEI_URIGK) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN NR_KBN = '12' THEN SUM(CHOSEI_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '22' THEN SUM(CHOSEI_URIGK) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN NR_KBN = '31' THEN SUM(CHOSEI_URIGK) ELSE 0 END ,CASE WHEN NR_KBN = '41' THEN SUM(CHOSEI_URIGK) ELSE 0 END");
			sbSQL.append(" FROM W00 GROUP BY URI_YMD,TENPO_CD,NR_KBN ");
		}
		// 店舗集計
		if (!StringUtils.equals(tenpoCd, "'00'")) {	// 選択店舗
			// 店舗計追加
			select2 = cmnSelect + select2;
			sbSQL.append("),W1(URI_YMD,KBN,KBNNM" + cmnItem + item + ") AS (");
			sbSQL.append(" SELECT URI_YMD,KBN,MAX(KBNNM)" + cmnSelect + select1);
			sbSQL.append(" FROM W0 GROUP BY URI_YMD,TENPO_CD,KBN) ");
		} else {									// 全店舗
			sbSQL.append("),W1(URI_YMD,KBN,KBNNM" + item + ") AS (");
			sbSQL.append(" SELECT URI_YMD,KBN,MAX(KBNNM)" + cmnSelect);
			sbSQL.append(" FROM W0 GROUP BY URI_YMD,KBN) ");
		}
		// データ行
		sbSQL.append("SELECT URI_YMD||KBN AS SORT," + sel_Ym2 + ",MAX(KBNNM)" + select2);
		sbSQL.append("FROM W1 GROUP BY URI_YMD,KBN ");
		// 総計行
		sbSQL.append(" union all ");
		sbSQL.append("SELECT '999999'||KBN AS SORT," + sel_Type + ",MAX(KBNNM)" + select2);
		sbSQL.append("FROM W1 GROUP BY KBN ORDER BY SORT ");

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

		String radB1 = jad.getJSONValue(DefineReport.RadB_1.NAME.getObj());

		List<String> cells = new ArrayList<String>();
		// 照会データ指定
		cells.add(DefineReport.RadB_1.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_1.NAME.getObj()));
		getWhere().add(cells);
		// 年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.YM_KIJUN.getView());
		if (StringUtils.equals(radB1, DefineReport.RadB_1.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj()));
		} else {
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.DD_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj()) + jad.getJSONText(DefineReport.Select.DD_KIJUN_TO.getObj()));
		}
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
