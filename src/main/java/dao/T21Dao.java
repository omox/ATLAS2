package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T21テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T21Dao extends ItemDao {

	/**
	 * 新しいT21Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T21Dao(Connection source) {
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
		String szRad_T2 = getMap().get("RAD_T2");	// 表示対象-1:単月 2:過去12ヶ月
		String szYm1 = getMap().get("YYYY_MM");		// 期間-開始年月
		String szYm2 = getMap().get("YYYY_MM2");	// 期間-終了年月
		String szYmb1 = getMap().get("YYYY_MMB");		// 期間-開始年月
		String szYmb2 = getMap().get("YYYY_MMB2");	// 期間-終了年月
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル
		String szJan = getMap().get("JANRU");		// 購入ジャンル
		String szRad_B2 = getMap().get("RAD_B2");	// 対比表示-1:前年比 2:前月比 3:なし

		// パラメータ確認
		if ((szRad_T2 == null) || (szRad_B2 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 対比表示SELECT項目
		String sqlZEN1 = "";
		String sqlZEN2 = "";
		String sqlZEN3 = "";
		String sqlZEN4 = "";
		String sqlZEN5 = "";
		if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {
			sqlZEN1 = "CASE WHEN SUM(ZYY_KYAKUSU_A) = 0 THEN 0 ELSE SUM(KYAKUSU_A)/SUM(ZYY_KYAKUSU_A)*100 END as \"前年比\",";
			sqlZEN2 = "CASE WHEN SUM(ZYY_KYAKUSU_B) = 0 THEN 0 ELSE SUM(KYAKUSU_B)/SUM(ZYY_KYAKUSU_B)*100 END as \"前年比 \",";
			sqlZEN3 = "CASE WHEN SUM(ZYY_KYAKUSU_A+ZYY_KYAKUSU_B) = 0 THEN 0 ELSE SUM(KYAKUSU_A+KYAKUSU_B)/SUM(ZYY_KYAKUSU_A+ZYY_KYAKUSU_B)*100 END as \"前年比  \",";
			sqlZEN4 = "CASE WHEN SUM(ZYY_KYAKUSU_C) = 0 THEN 0 ELSE SUM(KYAKUSU_C)/SUM(ZYY_KYAKUSU_C)*100 END as \"前年比   \",";
			sqlZEN5 = "CASE WHEN SUM(ZYY_KYAKUSU_A+ZYY_KYAKUSU_B+ZYY_KYAKUSU_C) = 0 THEN 0 ELSE SUM(KYAKUSU_A+KYAKUSU_B+KYAKUSU_C)/SUM(ZYY_KYAKUSU_A+ZYY_KYAKUSU_B+ZYY_KYAKUSU_C)*100 END as \"前年比    \",";
		} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) { //
			sqlZEN1 = "CASE WHEN SUM(ZMM_KYAKUSU_A) = 0 THEN 0 ELSE SUM(KYAKUSU_A)/SUM(ZMM_KYAKUSU_A)*100 END as \"前月比\",";
			sqlZEN2 = "CASE WHEN SUM(ZMM_KYAKUSU_B) = 0 THEN 0 ELSE SUM(KYAKUSU_B)/SUM(ZMM_KYAKUSU_B)*100 END as \"前月比 \",";
			sqlZEN3 = "CASE WHEN SUM(ZMM_KYAKUSU_A+ZMM_KYAKUSU_B) = 0 THEN 0 ELSE SUM(KYAKUSU_A+KYAKUSU_B)/SUM(ZMM_KYAKUSU_A+ZMM_KYAKUSU_B)*100 END as \"前月比  \",";
			sqlZEN4 = "CASE WHEN SUM(ZMM_KYAKUSU_C) = 0 THEN 0 ELSE SUM(KYAKUSU_C)/SUM(ZMM_KYAKUSU_C)*100 END as \"前月比   \",";
			sqlZEN5 = "CASE WHEN SUM(ZMM_KYAKUSU_A+ZMM_KYAKUSU_B+ZMM_KYAKUSU_C) = 0 THEN 0 ELSE SUM(KYAKUSU_A+KYAKUSU_B+KYAKUSU_C)/SUM(ZMM_KYAKUSU_A+ZMM_KYAKUSU_B+ZMM_KYAKUSU_C)*100 END as \"前月比    \",";
		}
		// 年月指定条件
		String whereYM = "YYYY_MM BETWEEN '" + szYm1 + "' and '" + szYm2 + "' ";
		if (StringUtils.equals(szRad_T2, DefineReport.RadT_02.ID2.getVal())) {
			 whereYM = "YYYY_MM BETWEEN '" + szYmb1 + "' and '" + szYmb2 + "' ";
		}
		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "' ";
		// 購入ジャンル条件
		String whereJan = " and JUNLE1 = '" + szJan.substring(0, 2) + "' and JUNLE2 = '" + szJan.substring(2, 4) + "' ";
		// 表示対象条件
		String whereKbn = " and KBN_R12 = '" + szRad_T2 + "'";

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("SELECT YYYY_MM AS SORT,KIJUN_YM_MEI AS \"受注年月\", SUM(KYAKUSU_A) AS \"新規・新規\",");
		sbSQL.append(sqlZEN1);
		sbSQL.append(" SUM(KYAKUSU_B) AS \"新規・リピート\",");
		sbSQL.append(sqlZEN2);
		sbSQL.append(" SUM(KYAKUSU_A+KYAKUSU_B) AS \"新規計\",");
		sbSQL.append(sqlZEN3);
		sbSQL.append(" SUM(KYAKUSU_C) AS \"リピート\",");
		sbSQL.append(sqlZEN4);
		sbSQL.append(" SUM(KYAKUSU_A+KYAKUSU_B+KYAKUSU_C) AS \"ジャンル計\",");
		sbSQL.append(sqlZEN5);
		sbSQL.append(" SUM(KYAKUSU_NEN) AS \"R12合計\" ");
		sbSQL.append("FROM T21_NEWREP inner join YM_KIJUN_T21 on (YYYY_MM = KIJUN_YM) ");
		sbSQL.append("WHERE " + whereYM + whereKbn + whereCha + whereJan);
		sbSQL.append("GROUP BY YYYY_MM,KIJUN_YM_MEI ");
		sbSQL.append("  UNION ALL  ");
		sbSQL.append("SELECT '9999' AS SORT,'合計' AS \"受注年月\", SUM(KYAKUSU_A) AS \"新規・新規\",");
		sbSQL.append(sqlZEN1);
		sbSQL.append(" SUM(KYAKUSU_B) AS \"新規・リピート\",");
		sbSQL.append(sqlZEN2);
		sbSQL.append(" SUM(KYAKUSU_A+KYAKUSU_B) AS \"新規計\",");
		sbSQL.append(sqlZEN3);
		sbSQL.append(" SUM(KYAKUSU_C) AS \"リピート\",");
		sbSQL.append(sqlZEN4);
		sbSQL.append(" SUM(KYAKUSU_A+KYAKUSU_B+KYAKUSU_C) AS \"ジャンル計\",");
		sbSQL.append(sqlZEN5);
		sbSQL.append(" SUM(KYAKUSU_NEN) AS \"R12合計\" ");
		sbSQL.append("FROM T21_NEWREP T  ");
		sbSQL.append("WHERE " + whereYM + whereKbn + whereCha + whereJan);
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

		List<String> cells = new ArrayList<String>();
		// 照会データ指定
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_02.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_02.NAME.getObj()));
		getWhere().add(cells);
		// 年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		cells.add("～");
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj()));
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);
		// 購入ジャンル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.JANRU.getView());
		cells.add(jad.getJSONText(DefineReport.Select.JANRU.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
