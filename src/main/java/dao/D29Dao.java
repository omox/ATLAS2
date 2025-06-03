package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import common.DefineReport;
import common.JsonArrayData;

/**
 * @author EATONE
 */
public class D29Dao extends ItemDao {

	/**
	 * 新しいD29Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public D29Dao(Connection source) {
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

		String szChanel	= getMap().get("CHANEL");		// 購入チャネル
		String szYy			= getMap().get("YYYY_YY");	// 開始年
		String szNm			= getMap().get("YYYY_MM");	// 開始月

		String szRadSu	= getMap().get("RADSU");		// 都道府県詳細

		System.out.println("CHANEL:"+szChanel);
		System.out.println("YYYY_MM:"+szYy+szNm);
		System.out.println("RADSU:"+szRadSu);

		// プロモーション指定
		String sqlDM = " AND PROMO_CD = '0000'";
		// その他
		String szWhere = " KBN ='"+szRadSu+"' "+" AND YYYY_MM = '"+szYy+szNm+"'";

		String szTitle = "受注件数";
		if (DefineReport.RadT_08.ID1.getVal().equals(szRadSu)){
			// 顧客数
			szTitle="顧客数";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("WITH MRANK(MRANK_G_CD, MRANK_G_NM) AS ( ");
		sb.append("  SELECT MRANK_G_CD, MRANK_G_NM FROM M_MRANK2 GROUP BY MRANK_G_CD, MRANK_G_NM ");
		sb.append(") ");
		sb.append(", WORK (LKONYU2, ST_N, ST_GAK_N, ST_R, ST_GAK_R) AS ( ");

		if (DefineReport.RadD01_CHANEL.ID1.getVal().equals(szChanel)){
			sb.append("  SELECT ");
			sb.append("    LKONYU2 ");
			sb.append("    , CASE WHEN NRKBN = 1 THEN SU ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 1 THEN JUCGK ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 2 THEN SU ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 2 THEN JUCGK ELSE 0 END ");
			sb.append("  FROM ");
			sb.append("    DS_S_TANKA ");
			sb.append("  WHERE ");
			sb.append(szWhere);	// その他
		}
		if (DefineReport.RadD01_CHANEL.ID2.getVal().equals(szChanel)){
			sb.append("  SELECT ");
			sb.append("    LKONYU2 ");
			sb.append("    , CASE WHEN NRKBN = 1 THEN SU ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 1 THEN JUCGK ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 2 THEN SU ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 2 THEN JUCGK ELSE 0 END ");
			sb.append("  FROM ");
			sb.append("    DS_S_T15_HAN ");
			sb.append("  WHERE ");
			sb.append(szWhere);	// その他
		}
		if (DefineReport.RadD01_CHANEL.ID3.getVal().equals(szChanel)){
			sb.append("  SELECT ");
			sb.append("    LKONYU2 ");
			sb.append("    , CASE WHEN NRKBN = 1 THEN SU ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 1 THEN JUCGK ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 2 THEN SU ELSE 0 END ");
			sb.append("    , CASE WHEN NRKBN = 2 THEN JUCGK ELSE 0 END ");
			sb.append("  FROM ");
			sb.append("    DS_S_T16_TEN ");
			sb.append("  WHERE ");
			sb.append(szWhere);	// その他
			sb.append(sqlDM);		// プロモーション
		}
		sb.append(") ");

		sb.append("SELECT ");
		sb.append("    CASE WHEN GROUPING_ID(MRANK_G_NM)=1 THEN '合計' ELSE MRANK_G_NM END AS 単価 ");
		sb.append("  , SUM(ST_N) AS 新規_"+szTitle);
		sb.append("  , SUM(ST_GAK_N) AS 新規_受注額 ");
		sb.append("  , CASE WHEN SUM(ST_N) = 0 THEN 0 ELSE ROUND(SUM(ST_GAK_N) / SUM(ST_N),0) END AS 新規_単価 ");
		sb.append("  , SUM(ST_R) AS リピート_"+szTitle);
		sb.append("  , SUM(ST_GAK_R) AS リピート_受注額 ");
		sb.append("  , CASE WHEN SUM(ST_R) = 0 THEN 0 ELSE ROUND(SUM(ST_GAK_R) / SUM(ST_R),0) END AS リピート_単価 ");
		sb.append("  , SUM(ST_N)+SUM(ST_R) AS 合計_"+szTitle);
		sb.append("  , SUM(ST_GAK_N)+SUM(ST_GAK_R) AS 合計_受注額 ");
		sb.append("  , CASE WHEN (SUM(ST_N) + SUM(ST_R)) = 0 THEN 0 ELSE ROUND((SUM(ST_GAK_N) + SUM(ST_GAK_R)) / (SUM(ST_N) + SUM(ST_R))) END AS 合計_単価 ");
		sb.append(" ");
		sb.append("FROM ");
		sb.append("  WORK ");
		sb.append("  , MRANK ");
		sb.append("WHERE ");
		sb.append("  LKONYU2 = MRANK_G_CD ");
		sb.append("GROUP BY GROUPING SETS((MRANK_G_CD, MRANK_G_NM),()) ");
		sb.append("ORDER BY CASE WHEN GROUPING_ID(MRANK_G_NM)=1 THEN 'L99' ELSE MRANK_G_CD END ");

		System.out.println(sb.toString());
		command = sb.toString();
		return command;
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

		// 購入チャネル
		cells.add(DefineReport.RadD01_CHANEL.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD01_CHANEL.NAME.getObj()));
		getWhere().add(cells);

		cells = new ArrayList<String>();

		// 集計期間
		cells.add(DefineReport.Label.LABELD03.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
		getWhere().add(cells);

		// 表示項目-1:顧客数 2:受注件数
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_08.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_08.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
