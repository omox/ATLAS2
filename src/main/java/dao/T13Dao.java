package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T13テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T13Dao extends ItemDao {

	/**
	 * 新しいT13Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T13Dao(Connection source) {
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
		String szRadYm = getMap().get("RAD_YM");				// 年月区分3:日別 2:月別
		String szYmd = getMap().get("YYYY_MMDD");				// （必須）年月
		String szYm = getMap().get("YYYY_MM");				// （必須）年月
		String szPromoFrom = getMap().get("PROMO_CD_FROM");	// プロモーションFROM
		String szPromoTo = getMap().get("PROMO_CD_TO");		// プロモーションTO
		String szRad = getMap().get("RAD");					// （必須）表示項目-1:即時 2:継続

		// パラメータ確認
		if ((szYmd == null) || (szRad == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		String sqlYMD = "";
		if (StringUtils.equals(szRadYm, "3")) {  //3:日別
			sqlYMD = "'"+szYmd+"'";
		} else {   //1:月別
			sqlYMD ="( select max(KIJUN_YMD) from YMD_KIJUN_T13 where substr(KIJUN_YMD,1,6) = '"+szYm+"' )";
		}

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		if (StringUtils.equals(szRad, "2")) {  //2:継続
			sbSQL.append("SELECT P.HAKKOBI||T13.PROMO_CD AS SORT,P.HAKKOBI AS \"掲載日\",trim(T13.PROMO_CD) AS \"プロモーション\",P.PROMO_MEI AS \"媒体名\",");
			sbSQL.append("SUM(KYAKUSU_T) as \"発送数\",SUM(KYAKUSU_J) as \"継続客数(F2-)\",");
			sbSQL.append("CASE WHEN SUM(KYAKUSU_T) = 0 THEN 0 ELSE SUM(KYAKUSU_J) * 100 / SUM(KYAKUSU_T) END \"継続率(F2-)\",");
			sbSQL.append("SUM(JUCKAISU) as \"累計受注回数\",SUM(JUCGAKU) as \"累計受注金額\",");
			sbSQL.append("CASE WHEN SUM(JUCKAISU) = 0 THEN 0 ELSE ROUND(SUM(JUCGAKU) / SUM(JUCKAISU)) END \"平均単価(回)\",");
			sbSQL.append("CASE WHEN SUM(KYAKUSU_T) = 0 THEN 0 ELSE ROUND(SUM(JUCGAKU) / SUM(KYAKUSU_T)) END \"平均単価(人)\" ");
			sbSQL.append("FROM T13_15_BYTAI T13 LEFT OUTER JOIN M_PROMO_ALL P ON T13.PROMO_CD = P.PROMO_CD ");
			sbSQL.append("WHERE T13.KBN='2' and T13.YYYYMMDD = " + sqlYMD + " and HAKKOBI > '00000000' and PROMO_CD_S IN ");
			sbSQL.append("(select PROMO_CD from M_PROMO_T13 WHERE HAKKOBI || RTRIM(PROMO_CD) >= '" + szPromoFrom + "' and HAKKOBI || RTRIM(PROMO_CD) <= '" + szPromoTo + "') ");
			sbSQL.append("GROUP BY P.PROMO_MEI,T13.PROMO_CD,P.HAKKOBI ");
			sbSQL.append(" UNION ALL SELECT '99999999' AS SORT,'合計' AS \"掲載日\",'' AS \"プロモーション\",'' AS \"媒体名\",");
			sbSQL.append("SUM(KYAKUSU_T) as \"発送数\",SUM(KYAKUSU_J) as \"継続客数(F2-)\",");
			sbSQL.append("CASE WHEN SUM(KYAKUSU_T) = 0 THEN 0 ELSE SUM(KYAKUSU_J) * 100 / SUM(KYAKUSU_T) END \"継続率(F2-)\",");
			sbSQL.append("SUM(JUCKAISU) as \"累計受注回数\",SUM(JUCGAKU) as \"累計受注金額\",");
			sbSQL.append("CASE WHEN SUM(JUCKAISU) = 0 THEN 0 ELSE ROUND(SUM(JUCGAKU) / SUM(JUCKAISU)) END \"平均単価(回)\",");
			sbSQL.append("CASE WHEN SUM(KYAKUSU_T) = 0 THEN 0 ELSE ROUND(SUM(JUCGAKU) / SUM(KYAKUSU_T)) END \"平均単価(人)\" ");
			sbSQL.append("FROM T13_15_BYTAI T13 LEFT OUTER JOIN M_PROMO_ALL P ON T13.PROMO_CD = P.PROMO_CD ");
			sbSQL.append("WHERE T13.KBN='2' and T13.YYYYMMDD = " + sqlYMD + "  and HAKKOBI > '00000000'  and PROMO_CD_S IN ");
			sbSQL.append("(select PROMO_CD from M_PROMO_T13 WHERE HAKKOBI || RTRIM(PROMO_CD) >= '" + szPromoFrom + "' and HAKKOBI || RTRIM(PROMO_CD) <= '" + szPromoTo + "') ");
			sbSQL.append(" ORDER BY SORT");
		} else {   //1:即時
			sbSQL.append("SELECT P.HAKKOBI||T13.PROMO_CD AS SORT,P.HAKKOBI AS \"掲載日\",trim(T13.PROMO_CD) AS \"プロモーション\",P.PROMO_MEI AS \"媒体名\",");
			sbSQL.append("SUM(KYAKUSU_T) as \"発送数\",SUM(KYAKUSU_J) as \"受注客数\",");
			sbSQL.append("SUM(JUCKAISU) as \"受注回数\",SUM(JUCGAKU) as \"受注金額\",");
			sbSQL.append("CASE WHEN SUM(JUCKAISU) = 0 THEN 0 ELSE ROUND(SUM(JUCGAKU) / SUM(JUCKAISU)) END \"平均単価(回)\" ");
			sbSQL.append("FROM T13_15_BYTAI T13 LEFT OUTER JOIN M_PROMO_ALL P ON T13.PROMO_CD = P.PROMO_CD ");
			sbSQL.append("WHERE T13.KBN='1' and T13.YYYYMMDD = " + sqlYMD + " and HAKKOBI > '00000000'  and PROMO_CD_S IN ");
			sbSQL.append("(select PROMO_CD from M_PROMO_T13 WHERE HAKKOBI || RTRIM(PROMO_CD) >= '" + szPromoFrom + "' and HAKKOBI || RTRIM(PROMO_CD) <= '" + szPromoTo + "') ");
			sbSQL.append("GROUP BY P.PROMO_MEI,T13.PROMO_CD,P.HAKKOBI ");
			sbSQL.append(" UNION ALL SELECT '99999999' AS SORT,'合計' AS \"掲載日\",'' AS \"プロモーション\",'' AS \"媒体名\",");
			sbSQL.append("SUM(KYAKUSU_T) as \"発送数\",SUM(KYAKUSU_J) as \"受注客数\",");
			sbSQL.append("SUM(JUCKAISU) as \"受注回数\",SUM(JUCGAKU) as \"受注金額\",");
			sbSQL.append("CASE WHEN SUM(JUCKAISU) = 0 THEN 0 ELSE ROUND(SUM(JUCGAKU) / SUM(JUCKAISU)) END \"平均単価(回)\" ");
			sbSQL.append("FROM T13_15_BYTAI T13 LEFT OUTER JOIN M_PROMO_ALL P ON T13.PROMO_CD = P.PROMO_CD ");
			sbSQL.append("WHERE T13.KBN='1' and T13.YYYYMMDD = " + sqlYMD + " and HAKKOBI > '00000000'  and PROMO_CD_S IN ");
			sbSQL.append("(select PROMO_CD from M_PROMO_T13 WHERE HAKKOBI || RTRIM(PROMO_CD) >= '" + szPromoFrom + "' and HAKKOBI || RTRIM(PROMO_CD) <= '" + szPromoTo + "') ");
			sbSQL.append(" ORDER BY SORT");
		}

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
		// 年月
		cells.add(DefineReport.Label.LABELT2.getView());
		String radB1 = jad.getJSONValue(DefineReport.RadB_1.NAME.getObj());
		if (StringUtils.equals(radB1, "3")) {
			cells.add(jad.getJSONText(DefineReport.Select.YMD_KIJUN.getObj()));
		} else {
			cells.add(jad.getJSONText(DefineReport.Select.YMD_KIJUN2.getObj()));
		}
		getWhere().add(cells);

		// プロモーションFrom-To
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.PROMO_FROM.getView());
		cells.add(jad.getJSONText(DefineReport.Select.PROMO_FROM.getObj()));
		cells.add("～");
		cells.add(jad.getJSONText(DefineReport.Select.PROMO_TO.getObj()));
		getWhere().add(cells);

		// 表示項目-1:即時 2:継続
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_07.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_07.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);

	}

}
