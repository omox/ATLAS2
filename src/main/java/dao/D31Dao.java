package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.DefineReport;
import common.JsonArrayData;

/**
 * @author EATONE
 */
public class D31Dao extends ItemDao {

	/**
	 * 新しいD31Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public D31Dao(Connection source) {
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

		String szYy			= getMap().get("YYYY_YY");	// 開始年

		String szColumn	= getMap().get("COLUMN");		// 表示項目

		System.out.println("YYYY_YY:"+szYy);
		System.out.println("COLUMN:"+szColumn);

		String szZYY = String.valueOf(Integer.parseInt(szYy)-1);
		ArrayList<String> Titles = new ArrayList<String>(Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12","合計"));

		StringBuffer sb = new StringBuffer();
		sb.append("WITH W00(URI_YMD, TENPO_CD, KYAKUSU, ZYY_KYAKUSU, JUN_URIGK, ZYY_JUN_URIGK, SHU_URIGK, ZYY_SHU_URIGK, CHO_URIGK, ZYY_CHO_URIGK, K11, K12, K31, K41, ZYY_K11, ZYY_K12, ZYY_K31, ZYY_K41) AS ( ");
		sb.append("SELECT ");
		sb.append("    CASE WHEN GROUPING_ID(URI_YMD)=1 THEN '合計' ELSE URI_YMD END ");
		sb.append("  , CASE WHEN GROUPING_ID(TENPO_CD)=1 THEN '00A' ELSE TENPO_CD END ");
		sb.append("  , SUM(KYAKUSU) AS KYAKUSU ");
		sb.append("  , SUM(ZYY_KYAKUSU) AS ZYY_KYAKUSU ");
		sb.append("  , SUM(JUN_URIGK) AS JUN_URIGK ");
		sb.append("  , SUM(ZYY_JUN_URIGK) AS ZYY_JUN_URIGK ");
		sb.append("  , SUM(SHU_URIGK) AS SHU_URIGK ");
		sb.append("  , SUM(ZYY_SHU_URIGK) AS ZYY_SHU_URIGK ");
		sb.append("  , SUM(CHO_URIGK) AS CHO_URIGK ");
		sb.append("  , SUM(ZYY_CHO_URIGK) AS ZYY_CHO_URIGK ");
		sb.append("  , SUM(K11) AS K11 ");
		sb.append("  , SUM(K12) AS K12 ");
		sb.append("  , SUM(K31) AS K31 ");
		sb.append("  , SUM(K41) AS K41 ");
		sb.append("  , SUM(ZYY_K11) AS ZYY_K11 ");
		sb.append("  , SUM(ZYY_K12) AS ZYY_K12 ");
		sb.append("  , SUM(ZYY_K31) AS ZYY_K31 ");
		sb.append("  , SUM(ZYY_K41) AS ZYY_K41 ");
		sb.append("FROM ( ");

		// 売上、レジ件数=0
		sb.append("  SELECT ");
		sb.append("    SUBSTR(URI_YMD, 5, 2) AS URI_YMD ");
		sb.append("    , TENPO_CD ");
		sb.append("    , 0 AS KYAKUSU ");
		sb.append("    , 0 AS ZYY_KYAKUSU ");
		sb.append("    , SUM(JUN_URIGK) AS JUN_URIGK ");
		sb.append("    , SUM(ZYY_JUN_URIGK) AS ZYY_JUN_URIGK ");
		sb.append("    , SUM(SHU_URIGK) AS SHU_URIGK ");
		sb.append("    , SUM(ZYY_SHU_URIGK) AS ZYY_SHU_URIGK ");
		sb.append("    , SUM(CHO_URIGK) AS CHO_URIGK ");
		sb.append("    , SUM(ZYY_CHO_URIGK) AS ZYY_CHO_URIGK ");
		sb.append("    , 0 AS K11 ");
		sb.append("    , 0 AS K12 ");
		sb.append("    , 0 AS K31 ");
		sb.append("    , 0 AS K41 ");
		sb.append("    , 0 AS ZYY_K11 ");
		sb.append("    , 0 AS ZYY_K12 ");
		sb.append("    , 0 AS ZYY_K31 ");
		sb.append("    , 0 AS ZYY_K41 ");
		sb.append("  FROM ");
		sb.append("    DS_S_URI_TEN ");
		sb.append("  WHERE ");
		sb.append("    SUBSTR(URI_YMD, 1, 6) BETWEEN '"+szYy+"01' AND '"+szYy+"12'");
		sb.append("  GROUP BY ");
		sb.append("    SUBSTR(URI_YMD, 5, 2) ");
		sb.append("    , TENPO_CD ");

		sb.append("  UNION ALL ");
		// 当年レジ件数
		sb.append("  SELECT ");
		sb.append("    SUBSTR(YMD, 5, 2) AS URI_YMD ");
		sb.append("    , TENCD AS TENPO_CD ");
		sb.append("    , SUM(SU) AS KYAKUSU ");
		sb.append("    , 0 AS ZYY_KYAKUSU ");
		sb.append("    , 0 AS JUN_URIGK ");
		sb.append("    , 0 AS ZYY_JUN_URIGK ");
		sb.append("    , 0 AS SHU_URIGK ");
		sb.append("    , 0 AS ZYY_SHU_URIGK ");
		sb.append("    , 0 AS CHO_URIGK ");
		sb.append("    , 0 AS ZYY_CHO_URIGK ");
		sb.append("    , SUM(CASE WHEN KB = 11 THEN SU END) AS K11 ");
		sb.append("    , SUM(CASE WHEN KB = 12 THEN SU END) AS K12 ");
		sb.append("    , SUM(CASE WHEN KB = 31 THEN SU END) AS K31 ");
		sb.append("    , SUM(CASE WHEN KB = 41 THEN SU END) AS K41 ");
		sb.append("    , 0 AS ZYY_K11 ");
		sb.append("    , 0 AS ZYY_K12 ");
		sb.append("    , 0 AS ZYY_K31 ");
		sb.append("    , 0 AS ZYY_K41 ");
		sb.append("  FROM ");
		sb.append("    DS_M_TENPO_REJI ");
		sb.append("  WHERE ");
		sb.append("    SUBSTR(YMD, 1, 6) BETWEEN '"+szYy+"01' AND '"+szYy+"12'");
		sb.append("  GROUP BY ");
		sb.append("    SUBSTR(YMD, 5, 2) ");
		sb.append("    , TENCD ");

		sb.append("  UNION ALL ");
		// 前年レジ件数
		sb.append("  SELECT ");
		sb.append("    SUBSTR(YMD + 10000, 5, 2) AS URI_YMD ");
		sb.append("    , TENCD AS TENPO_CD ");
		sb.append("    , 0 AS KYAKUSU ");
		sb.append("    , SUM(SU) AS ZYY_KYAKUSU ");
		sb.append("    , 0 AS JUN_URIGK ");
		sb.append("    , 0 AS ZYY_JUN_URIGK ");
		sb.append("    , 0 AS SHU_URIGK ");
		sb.append("    , 0 AS ZYY_SHU_URIGK ");
		sb.append("    , 0 AS CHO_URIGK ");
		sb.append("    , 0 AS ZYY_CHO_URIGK ");
		sb.append("    , 0 AS K11 ");
		sb.append("    , 0 AS K12 ");
		sb.append("    , 0 AS K13 ");
		sb.append("    , 0 AS K14 ");
		sb.append("    , SUM(CASE WHEN KB = 11 THEN SU END) AS ZYY_K11 ");
		sb.append("    , SUM(CASE WHEN KB = 12 THEN SU END) AS ZYY_K12 ");
		sb.append("    , SUM(CASE WHEN KB = 31 THEN SU END) AS ZYY_K31 ");
		sb.append("    , SUM(CASE WHEN KB = 41 THEN SU END) AS ZYY_K41 ");
		sb.append("  FROM ");
		sb.append("    DS_M_TENPO_REJI ");
		sb.append("  WHERE ");
		sb.append("    SUBSTR(YMD, 1, 6) BETWEEN '"+szZYY+"01' AND '"+szZYY+"12'");
		sb.append("  GROUP BY ");
		sb.append("    SUBSTR(YMD + 10000, 5, 2) ");
		sb.append("    , TENCD ");

		sb.append(")X ");
		sb.append("GROUP BY GROUPING SETS ((URI_YMD,TENPO_CD),(URI_YMD),(TENPO_CD),()) ");
		sb.append(") ");

		sb.append("SELECT ");
		sb.append(" MAX(NVL(T2.TENPO_NM,'合計')) AS 店舗 ");

		for (int i=0;i<Titles.size();i++){

			// レジ件数
			if (DefineReport.RadD13_COLUMN.ID1.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN T1.KYAKUSU END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

			// 新規件数
			if (DefineReport.RadD13_COLUMN.ID2.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN T1.K11 END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.ZYY_K11 = 0 THEN NULL ELSE ROUND(T1.K11 / T1.ZYY_K11 * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

			//_リピート件数
			if (DefineReport.RadD13_COLUMN.ID3.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN T1.K12 END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.ZYY_K12 = 0 THEN NULL ELSE ROUND(T1.K12 / T1.ZYY_K12 * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

			//_カードなし件数
			if (DefineReport.RadD13_COLUMN.ID4.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN T1.K31 END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.ZYY_K31 = 0 THEN NULL ELSE ROUND(T1.K31 / T1.ZYY_K31 * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

			//_未登録件数
			if (DefineReport.RadD13_COLUMN.ID5.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN T1.K41 END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.ZYY_K41 = 0 THEN NULL ELSE ROUND(T1.K41 / T1.ZYY_K41 * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

			//_純売上
			if (DefineReport.RadD13_COLUMN.ID6.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN ROUND(T1.JUN_URIGK,0) END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.ZYY_JUN_URIGK = 0 THEN NULL ELSE ROUND(T1.JUN_URIGK / T1.ZYY_JUN_URIGK * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

			//_出荷売上
			if (DefineReport.RadD13_COLUMN.ID7.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN T1.SHU_URIGK END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.ZYY_SHU_URIGK = 0 THEN NULL ELSE ROUND(T1.SHU_URIGK / T1.ZYY_SHU_URIGK * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

			//_調整前売上
			if (DefineReport.RadD13_COLUMN.ID8.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN T1.CHO_URIGK END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.ZYY_CHO_URIGK = 0 THEN NULL ELSE ROUND(T1.CHO_URIGK / T1.ZYY_CHO_URIGK * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

			//_レジ単価
			if (DefineReport.RadD13_COLUMN.ID9.getVal().equals(szColumn)){
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.SHU_URIGK / T1.KYAKUSU, 0) END END) AS \""+Titles.get(i)+"月_実績\" ");
				sb.append(",SUM(CASE WHEN T1.URI_YMD = '"+Titles.get(i)+"' THEN CASE WHEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN 0 ELSE ROUND(T1.ZYY_SHU_URIGK / T1.ZYY_KYAKUSU, 0) END = 0 THEN NULL ELSE ROUND(CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.SHU_URIGK / T1.KYAKUSU, 0) END / CASE WHEN T1.ZYY_KYAKUSU = 0 THEN 0 ELSE ROUND(T1.ZYY_SHU_URIGK / T1.ZYY_KYAKUSU, 0) END * 100, 2) END END) AS \""+Titles.get(i)+"月_前年比\" ");
			}

		}

		sb.append(" ");
		sb.append("FROM W00 T1 ");
		sb.append("LEFT OUTER JOIN M_TENPO T2 ON T1.TENPO_CD=T2.TENPO_CD ");
		sb.append(" WHERE T1.TENPO_CD>'000' ");
		sb.append("GROUP BY T1.TENPO_CD ");
		sb.append("ORDER BY T1.TENPO_CD ");

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
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 表示項目
		cells.add(DefineReport.RadD13_COLUMN.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD13_COLUMN.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
