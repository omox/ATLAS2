package dao;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import common.DefineReport;
import common.JsonArrayData;

/**
 * @author EATONE
 */
public class D26Dao extends ItemDao {

	/**
	 * @param source コネクション
	 */
	public D26Dao(Connection source) {
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

		System.out.println("YYYY_MM:"+szYy);

		// 集計
		String szZYY = szYy;	// 前年
		ArrayList<String> Years = new ArrayList<String>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = sdf.parse(szYy+"0101");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			// 前年
			calendar.add(Calendar.YEAR, -1);
			Date d1 = calendar.getTime();
			szZYY = sdf.format(d1).substring(0, 4);
			Years.add(" BETWEEN "+String.valueOf(szZYY)+"01 AND "+String.valueOf(szZYY)+"12");

			// 当年
			Years.add(" BETWEEN "+String.valueOf(szYy)+"01 AND "+String.valueOf(szYy)+"12");

		} catch(ParseException e) {
			e.printStackTrace();
		}
		System.out.println("szZYY:"+szZYY);
		StringBuffer sb = new StringBuffer();

		sb.append("WITH ");
		// 基本集計
		sb.append("T_URI AS ( ");
		sb.append("SELECT YM,CHANEL,NKB,SU,YM AS HYM,1 AS COL1 ");
		sb.append("FROM DS_M_CHANEL_NEW_CUSTOMER ");
		sb.append("WHERE YM "+Years.get(1));
		sb.append("UNION ALL ");
		sb.append("SELECT YM,CHANEL,NKB,SU,YM+100 AS HYM,2 AS COL1 ");
		sb.append("FROM DS_M_CHANEL_NEW_CUSTOMER ");
		sb.append("WHERE YM "+Years.get(0));
		sb.append(") ");

		// 当年
		sb.append(",W_BS1 AS ( ");
		sb.append("SELECT YM,CHANEL ");
		sb.append(",CASE WHEN GROUPING_ID(NKB)=1 THEN 9 ELSE NKB END AS NKB ");
		sb.append(",SUM(SU) AS SU ");
		sb.append(",ROUND(SUM(SU)/AVG(HI)*100,2) AS HI ");
		sb.append(",NULL AS ZN FROM ( ");
		sb.append("  SELECT YM,CHANEL,NKB,SU,SUM(SU) OVER(PARTITION BY YM,CHANEL) AS HI,COL1 FROM T_URI WHERE COL1=1 ");
		sb.append(")X GROUP BY GROUPING SETS((COL1,YM,CHANEL,NKB),(COL1,YM,CHANEL)) ");
		sb.append(") ");

		// 前年比
		sb.append(",W_BS2 AS ( ");
		sb.append("SELECT YM,CHANEL ");
		sb.append(",CASE WHEN GROUPING_ID(NKB)=1 THEN 9 ELSE NKB END AS NKB ");
		sb.append(",NULL AS SU ");
		sb.append(",NULL AS HI ");
		sb.append(",ROUND(SUM(SUT)/SUM(SUZ)*100,2) AS ZN FROM ( ");
		sb.append("SELECT 3 AS COL1,HYM AS YM,CHANEL,NKB,SUM(CASE WHEN YM=HYM THEN SU END) AS SUT ,SUM(CASE WHEN YM<>HYM THEN SU END) AS SUZ FROM T_URI ");
		sb.append("GROUP BY HYM,CHANEL,NKB ");
		sb.append(")X GROUP BY GROUPING SETS((COL1,YM,CHANEL,NKB),(COL1,YM,CHANEL)) ");
		sb.append(") ");

		sb.append(",W_BS3 AS ( ");
		sb.append("SELECT YM,CHANEL,NKB,SUM(SU) AS SU,SUM(HI) AS HI,SUM(ZN) AS ZN FROM ( ");
		sb.append("SELECT * FROM W_BS1 ");
		sb.append("UNION ALL ");
		sb.append("SELECT * FROM W_BS2 ");
		sb.append(")X GROUP BY YM,CHANEL,NKB ");
		sb.append(") ");

		// 表示項目
		sb.append(", MVIW(KBN,KNM) AS ( ");
		sb.append("SELECT 1,'顧客数' FROM DUAL ");
		sb.append("UNION ALL ");
		sb.append("SELECT 2,'構成比' FROM DUAL ");
		sb.append("UNION ALL ");
		sb.append("SELECT 3,'前年比' FROM DUAL ");
		sb.append(") ");

		sb.append(", AXIS1(KBN,KNM) AS ( ");
		sb.append("SELECT 1,'通販' FROM DUAL ");
		sb.append("UNION ALL ");
		sb.append("SELECT 2,'店舗' FROM DUAL ");
		sb.append(") ");
		sb.append(", AXIS2(KBN,KNM) AS ( ");
		sb.append("SELECT 1,'オルビス新規' FROM DUAL ");
		sb.append("UNION ALL SELECT 2,'チャネル新規' FROM DUAL ");
		sb.append("UNION ALL ");
		sb.append("SELECT 9,'計' FROM DUAL ");
		sb.append(") ");

		sb.append("SELECT ");
		sb.append("  MAX(V.KNM) AS 項目 ");
		sb.append(" ,MAX(S1.KNM) AS チャネル ");
		sb.append(" ,MAX(S2.KNM) AS 区分 ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='01' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"1月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='02' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"2月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='03' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"3月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='04' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"4月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='05' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"5月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='06' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"6月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='07' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"7月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='08' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"8月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='09' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"9月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='10' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"10月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='11' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"11月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='12' THEN CASE V.KBN WHEN 1 THEN U.SU WHEN 2 THEN U.HI WHEN 3 THEN U.ZN END END) AS \"12月\" ");
		sb.append("FROM W_BS3 U ");
		sb.append("INNER JOIN AXIS1 S1 ON S1.KBN=U.CHANEL ");
		sb.append("INNER JOIN AXIS2 S2 ON S2.KBN=U.NKB ");
		sb.append(",MVIW V ");
		sb.append("GROUP BY V.KBN,U.CHANEL,U.NKB ");
		sb.append("ORDER BY V.KBN,U.CHANEL,U.NKB ");

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

		// 集計期間
		cells.add(DefineReport.Label.LABELD03.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
