package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T12テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T12Dao extends ItemDao {

	/**
	 * 新しいT12Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T12Dao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");	// 年月
		String szRad = getMap().get("RAD");		// 表示項目-1:即時 2:継続

		// パラメータ確認
		if ((szYm == null) || (szRad == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		if (StringUtils.equals(szRad, "2")) {  //2:継続
			sbSQL.append("SELECT MIN(T12.PROMOBI) AS SORT,T12.PROMO_CD AS SORT2,MIN(T12.PROMOBI) AS \"掲載日\", trim(T12.PROMO_CD) AS \"プロモーション\", ");
			sbSQL.append("PRO.PROMO_MEI AS \"媒体名\", ");
			sbSQL.append("SUM(T12.KYAKUSU_T) AS \"登録客数\", SUM(T12.KYAKUSU_J) AS \"受注客数\", SUM(T12.KYAKUSU_R) AS \"継続客数(F2-)\", ");
			sbSQL.append("CASE WHEN SUM(T12.KYAKUSU_J)=0 THEN 0 ELSE SUM(T12.KYAKUSU_R) * 100 / SUM(T12.KYAKUSU_J) END AS \"継続率(F2-)\", ");
			sbSQL.append("SUM(T12.JUCKAISU) AS \"累計受注回数\", SUM(T12.JUCGAKU) AS \"累計受注金額\", ");
			sbSQL.append("CASE WHEN SUM(T12.JUCKAISU)=0 THEN 0 ELSE ROUND(SUM(T12.JUCGAKU) / SUM(T12.JUCKAISU),0) END AS \"平均単価(回)\", ");
			sbSQL.append("CASE WHEN SUM(T12.KYAKUSU_J)=0 THEN 0 ELSE ROUND(SUM(T12.JUCGAKU) / SUM(T12.KYAKUSU_J),0) END AS \"平均単価(人)\" ");
			sbSQL.append("FROM T12_PROMO  T12 INNER JOIN M_PROMO_ALL PRO ON T12.PROMO_CD = PRO.PROMO_CD ");
			sbSQL.append(" WHERE T12.PROMOBI > (SELECT TO_CHAR(ADD_MONTHS(TO_DATE('"+szYm+"01','YY-MM-DD'),-12),'YYYYMM') FROM DUAL) ");
			sbSQL.append("  AND T12.YYYY_MM = '" + szYm + "' AND T12.KBN = '2' ");
			sbSQL.append(" GROUP BY T12.PROMOBI, T12.PROMO_CD, PRO.PROMO_MEI ");
			sbSQL.append("UNION ALL SELECT '0' AS SORT,'ZZ' AS SORT2,'' AS \"掲載日\", '' AS \"プロモーション\",'合計 ' AS \"媒体名\", ");
			sbSQL.append("SUM(T12.KYAKUSU_T) AS \"登録客数\", SUM(T12.KYAKUSU_J) AS \"受注客数\", SUM(T12.KYAKUSU_R) AS \"継続客数(F2-)\", ");
			sbSQL.append("CASE WHEN SUM(T12.KYAKUSU_J)=0 THEN 0 ELSE SUM(T12.KYAKUSU_R) * 100 / SUM(T12.KYAKUSU_J) END AS \"継続率(F2-)\", ");
			sbSQL.append("SUM(T12.JUCKAISU) AS \"累計受注回数\", SUM(T12.JUCGAKU) AS \"累計受注金額\", ");
			sbSQL.append("CASE WHEN SUM(T12.JUCKAISU)=0 THEN 0 ELSE ROUND(SUM(T12.JUCGAKU) / SUM(T12.JUCKAISU),0) END AS \"平均単価(回)\", ");
			sbSQL.append("CASE WHEN SUM(T12.KYAKUSU_J)=0 THEN 0 ELSE ROUND(SUM(T12.JUCGAKU) / SUM(T12.KYAKUSU_J),0) END AS \"平均単価(人)\" ");
			sbSQL.append("FROM T12_PROMO  T12  ");
			sbSQL.append(" WHERE T12.PROMOBI > (SELECT TO_CHAR(ADD_MONTHS(TO_DATE('"+szYm+"01','YY-MM-DD'),-12),'YYYYMM') FROM DUAL) ");
			sbSQL.append("  AND T12.YYYY_MM = '" + szYm + "' AND T12.KBN = '2' ");
			sbSQL.append(" ORDER BY SORT DESC,SORT2");
		} else {   //1:即時
			sbSQL.append("SELECT MIN(T12.PROMOBI) AS SORT,T12.PROMO_CD AS SORT2,MIN(T12.PROMOBI) AS \"掲載日\", trim(T12.PROMO_CD) AS \"プロモーション\", ");
			sbSQL.append("PRO.PROMO_MEI AS \"媒体名\", ");
			sbSQL.append("SUM(T12.KYAKUSU_T) AS \"登録客数\", SUM(T12.KYAKUSU_J) AS \"受注客数\", ");
			sbSQL.append("SUM(T12.JUCKAISU) AS \"受注回数\", SUM(T12.JUCGAKU) AS \"受注金額\", ");
			sbSQL.append("CASE WHEN SUM(T12.JUCKAISU)=0 THEN 0 ELSE ROUND(SUM(T12.JUCGAKU) / SUM(T12.JUCKAISU),0) END AS \"平均単価(回)\" ");
			sbSQL.append("FROM T12_PROMO  T12 INNER JOIN M_PROMO_ALL PRO ON T12.PROMO_CD = PRO.PROMO_CD ");
			sbSQL.append(" WHERE T12.PROMOBI > (SELECT TO_CHAR(ADD_MONTHS(TO_DATE('"+szYm+"01','YY-MM-DD'),-12),'YYYYMM') FROM DUAL) ");
			sbSQL.append("  AND T12.YYYY_MM = '" + szYm + "' AND T12.KBN = '1' ");
			sbSQL.append(" GROUP BY T12.PROMOBI, T12.PROMO_CD, PRO.PROMO_MEI ");
			sbSQL.append("UNION ALL SELECT '0' AS SORT,'ZZ' AS SORT2,'' AS \"掲載日\", '' AS \"プロモーション\",'合計 ' AS \"媒体名\", ");
			sbSQL.append("SUM(T12.KYAKUSU_T) AS \"登録客数\", SUM(T12.KYAKUSU_J) AS \"受注客数\", ");
			sbSQL.append("SUM(T12.JUCKAISU) AS \"受注回数\", SUM(T12.JUCGAKU) AS \"受注金額\", ");
			sbSQL.append("CASE WHEN SUM(T12.JUCKAISU)=0 THEN 0 ELSE ROUND(SUM(T12.JUCGAKU) / SUM(T12.JUCKAISU),0) END AS \"平均単価(回)\" ");
			sbSQL.append("FROM T12_PROMO  T12  ");
			sbSQL.append(" WHERE T12.PROMOBI > (SELECT TO_CHAR(ADD_MONTHS(TO_DATE('"+szYm+"01','YY-MM-DD'),-12),'YYYYMM') FROM DUAL) ");
			sbSQL.append("  AND T12.YYYY_MM = '" + szYm + "' AND T12.KBN = '1' ");
			sbSQL.append(" ORDER BY SORT DESC,SORT2");
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
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
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
