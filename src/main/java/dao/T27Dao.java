package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T27テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T27Dao extends ItemDao {

	/**
	 * 新しいT27Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T27Dao(Connection source) {
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
		String szRad_B1 = getMap().get("RAD_B1");	// 照会データ指定-1:年別 2:月別 5:年累計
		String szYy1 = getMap().get("YYYY");		// 開始年
		String szYy2 = getMap().get("YYYY2");		// 終了年
		String szYm1 = getMap().get("YYYY_MM");		// 開始年月
		String szYm2 = getMap().get("YYYY_MM2");	// 終了年月
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル
		String szRad_B2 = getMap().get("RAD_B2");	// 対比表示-1:前年比 2:前月比 3:なし

		// パラメータ確認
		if ((szRad_B1 == null) || (szRad_B2 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 対比表示別条件設定
		String sqlZEN1 = "";
		String sqlZEN2 = "";
		String sqlZEN3 = "";
		String sqlZEN4 = "";
		String sqlZEN5 = "";
		String sqlZEN6 = "";
		String sqlZEN7 = "";
		String sqlZEN8 = "";
		if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {
			sqlZEN1 = ",CASE WHEN ZYY_KYAKUSU_ALL > 0 THEN (KYAKUSU_0/KYAKUSU_ALL*100)-(ZYY_KYAKUSU_0/ZYY_KYAKUSU_ALL*100) ELSE 0 END as \"前年差\"";
			sqlZEN2 = ",CASE WHEN ZYY_KYAKUSU_123 = 0 THEN 0 ELSE KYAKUSU_123*100/ZYY_KYAKUSU_123 END as \"前年比 \"";
			sqlZEN3 = ",CASE WHEN ZYY_KYAKUSU_12 = 0 THEN 0 ELSE KYAKUSU_12*100/ZYY_KYAKUSU_12 END as \"前年比  \"";
			sqlZEN4 = ",CASE WHEN ZYY_KYAKUSU_13 = 0 THEN 0 ELSE KYAKUSU_13*100/ZYY_KYAKUSU_13 END as \"前年比   \"";
			sqlZEN5 = ",CASE WHEN ZYY_KYAKUSU_23 = 0 THEN 0 ELSE KYAKUSU_23*100/ZYY_KYAKUSU_23 END as \"前年比    \"";
			sqlZEN6 = ",CASE WHEN ZYY_KYAKUSU_1 = 0 THEN 0 ELSE KYAKUSU_1*100/ZYY_KYAKUSU_1 END as \"前年比     \"";
			sqlZEN7 = ",CASE WHEN ZYY_KYAKUSU_2 = 0 THEN 0 ELSE KYAKUSU_2*100/ZYY_KYAKUSU_2 END as \"前年比      \"";
			sqlZEN8 = ",CASE WHEN ZYY_KYAKUSU_3 = 0 THEN 0 ELSE KYAKUSU_3*100/ZYY_KYAKUSU_3 END as \"前年比       \"";
		} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) {
			sqlZEN1 = ",CASE WHEN ZMM_KYAKUSU_ALL > 0 THEN (KYAKUSU_0/KYAKUSU_ALL*100)-(ZMM_KYAKUSU_0/ZMM_KYAKUSU_ALL*100) ELSE 0 END as \"前月差\"";
			sqlZEN2 = ",CASE WHEN ZMM_KYAKUSU_123 = 0 THEN 0 ELSE KYAKUSU_123*100/ZMM_KYAKUSU_123 END as \"前月比 \"";
			sqlZEN3 = ",CASE WHEN ZMM_KYAKUSU_12 = 0 THEN 0 ELSE KYAKUSU_12*100/ZMM_KYAKUSU_12 END as \"前月比  \"";
			sqlZEN4 = ",CASE WHEN ZMM_KYAKUSU_13 = 0 THEN 0 ELSE KYAKUSU_13*100/ZMM_KYAKUSU_13 END as \"前月比   \"";
			sqlZEN5 = ",CASE WHEN ZMM_KYAKUSU_23 = 0 THEN 0 ELSE KYAKUSU_23*100/ZMM_KYAKUSU_23 END as \"前月比    \"";
			sqlZEN6 = ",CASE WHEN ZMM_KYAKUSU_1 = 0 THEN 0 ELSE KYAKUSU_1*100/ZMM_KYAKUSU_1 END as \"前月比     \"";
			sqlZEN7 = ",CASE WHEN ZMM_KYAKUSU_2 = 0 THEN 0 ELSE KYAKUSU_2*100/ZMM_KYAKUSU_2 END as \"前月比      \"";
			sqlZEN8 = ",CASE WHEN ZMM_KYAKUSU_3 = 0 THEN 0 ELSE KYAKUSU_3*100/ZMM_KYAKUSU_3 END as \"前月比       \"";
		}

		// 照会データ別件設定
		String colYm = ",YYYY_MM as \"年月\"";
		String whereYM = "";	// 年月指定条件
		if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID1.getVal())) {
			colYm = ",YYYY_MM as \"年\"";
			whereYM = "KBN_NEN = '0' and SUBSTR(YYYY_MM,1,4) >= '" + szYy1 + "' and SUBSTR(YYYY_MM,1,4) <= '" + szYy2 + "' ";
		} else if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID2.getVal())) {
			whereYM = "KBN_NEN = '1' and YYYY_MM >= '" + szYm1 + "' and YYYY_MM <= '" + szYm2 + "' ";
		} else if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID5.getVal())) {
			whereYM = "KBN_NEN = '2' and YYYY_MM >= '" + szYm1 + "' and YYYY_MM <= '" + szYm2 + "' ";
		}
		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "' ";

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("SELECT YYYY_MM as SORT" + colYm);
		sbSQL.append(",KYAKUSU_ALL as \"総顧客数 \" ");
		sbSQL.append(",KYAKUSU_0 as \"併買顧客数 \" ");
		sbSQL.append(",case when KYAKUSU_ALL > 0 then KYAKUSU_0/KYAKUSU_ALL * 100 else 0 end as \"併買比率\" ");
		sbSQL.append(sqlZEN1);
		sbSQL.append(",KYAKUSU_123 as \"化＋食＋ボ\" ");
		sbSQL.append(sqlZEN2);
		sbSQL.append(",KYAKUSU_12 as \"化＋食\" ");
		sbSQL.append(sqlZEN3);
		sbSQL.append(",KYAKUSU_13 as \"化＋ボ\" ");
		sbSQL.append(sqlZEN4);
		sbSQL.append(",KYAKUSU_23 as \"食＋ボ\" ");
		sbSQL.append(sqlZEN5);
		sbSQL.append(",KYAKUSU_1 as \"化粧品のみ\" ");
		sbSQL.append(sqlZEN6);
		sbSQL.append(",KYAKUSU_2 as \"食品のみ\" ");
		sbSQL.append(sqlZEN7);
		sbSQL.append(",KYAKUSU_3 as \"ボディのみ\" ");
		sbSQL.append(sqlZEN8);
		sbSQL.append("from T27_HEI_KYAKU ");
		sbSQL.append("WHERE " + whereYM + whereCha);
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

		List<String> cells = new ArrayList<String>();
		// 照会データ指定
		cells.add(DefineReport.RadB_1.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_1.NAME.getObj()));
		getWhere().add(cells);

		String radB1 = jad.getJSONValue(DefineReport.RadB_1.NAME.getObj());
		// 年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		if (StringUtils.equals(radB1, DefineReport.RadB_1.ID1.getObj())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()));
		} else {
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj()));
		}
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
