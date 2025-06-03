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
public class D22Dao extends ItemDao {

	/**
	 * @param source コネクション
	 */
	public D22Dao(Connection source) {
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

		System.out.println("CHANEL:"+szChanel);
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

		// チャネル判定
		String szChanelCode = "";
		if (DefineReport.RadD01_CHANEL.ID1.getVal().equals(szChanel)){
			// 通店計
			szChanelCode = "1,2";
		} else if (DefineReport.RadD01_CHANEL.ID2.getVal().equals(szChanel)) {
			// 通販
			szChanelCode = "1";
		} else if (DefineReport.RadD01_CHANEL.ID3.getVal().equals(szChanel)) {
			// 店舗
			szChanelCode = "2";
		}
		StringBuffer sb = new StringBuffer();

		sb.append("WITH ");
		// 基本集計
		sb.append("T_URI AS ( ");
		sb.append("SELECT YM,DSU,KSU, SUM(KSU) OVER(PARTITION BY DSU ORDER BY YM) AS RSU, 0 AS KSU_Z, 0 AS RSU_Z FROM ( ");
		sb.append("SELECT YM,DSU,SUM(KSU) AS KSU ");
		sb.append("FROM DS_M_F2_CONVERT ");
		sb.append("WHERE YM "+Years.get(1));
		sb.append("AND CHANEL IN ("+szChanelCode+")");
		sb.append("GROUP BY YM,DSU ");
		sb.append(")X ");
		sb.append("UNION ALL ");
		sb.append("SELECT YM+100 AS YM,DSU,0 AS KSU,0 AS RSU,KSU AS KSU_Z, SUM(KSU) OVER(PARTITION BY DSU ORDER BY YM) AS RSU_Z FROM ( ");
		sb.append("SELECT YM,DSU,SUM(KSU) AS KSU ");
		sb.append("FROM DS_M_F2_CONVERT ");
		sb.append("WHERE YM "+Years.get(0));
		sb.append("AND CHANEL IN ("+szChanelCode+")");
		sb.append("GROUP BY YM,DSU ");
		sb.append(")X ");
		sb.append(") ");

		sb.append(",W_BS1 AS ( ");
		sb.append("SELECT YM,DSU,SUM(KSU) AS KSU,SUM(RSU) AS RSU,SUM(KSU_Z) AS KSU_Z,SUM(RSU_Z) AS RSU_Z FROM T_URI GROUP BY YM,DSU ORDER BY DSU,YM ");
		sb.append(") ");

		// 初回購入者
		sb.append(",T_FSK AS ( ");
		sb.append("SELECT * FROM T_URI WHERE DSU=0 ");
		sb.append(") ");
		sb.append(",W_BS2 AS ( ");
		sb.append("SELECT YM,DSU,SUM(KSU) AS KSU,SUM(RSU) AS RSU,SUM(KSU_Z) AS KSU_Z,SUM(RSU_Z) AS RSU_Z FROM T_FSK GROUP BY YM,DSU ORDER BY DSU,YM ");
		sb.append(") ");

		// 単月＆累計
		sb.append(",W_BS3 AS ( ");
		sb.append("SELECT 1 AS COL1,T1.YM,T1.DSU ");
		sb.append(",T1.KSU,T2.KSU AS KSU0 ");
		sb.append(",CASE WHEN T2.KSU=0 THEN NULL ELSE ROUND(T1.KSU/T2.KSU*100,2) END AS F2KT ");
		sb.append(",T1.KSU_Z,T2.KSU_Z AS KSU0_Z ");
		sb.append(",CASE WHEN T2.KSU_Z=0 THEN NULL ELSE ROUND(T1.KSU_Z/T2.KSU_Z*100,2) END AS F2KZ ");
		sb.append(",CASE WHEN CASE WHEN T2.KSU_Z=0 THEN NULL ELSE ROUND(T1.KSU_Z/T2.KSU_Z*100,2) END=0 THEN NULL ELSE ");
		sb.append(" ROUND(CASE WHEN T2.KSU=0 THEN NULL ELSE ROUND(T1.KSU/T2.KSU*100,2) END/CASE WHEN T2.KSU_Z=0 THEN NULL ELSE ROUND(T1.KSU_Z/T2.KSU_Z*100,2) END*100,2) ");
		sb.append(" END AS F2KH ");
		sb.append("FROM W_BS1 T1 LEFT OUTER JOIN W_BS2 T2 ON T2.YM=T1.YM ");
		sb.append(" ");
		sb.append("UNION ALL ");
		sb.append(" ");
		sb.append("SELECT 2 AS COL1,T1.YM,T1.DSU ");
		sb.append(",T1.RSU,T2.RSU AS RSU0 ");
		sb.append(",CASE WHEN T2.RSU=0 THEN NULL ELSE ROUND(T1.RSU/T2.RSU*100,2) END AS F2RT ");
		sb.append(",T1.RSU_Z,T2.RSU_Z AS RSU0_Z ");
		sb.append(",CASE WHEN T2.RSU_Z=0 THEN NULL ELSE ROUND(T1.RSU_Z/T2.RSU_Z*100,2) END AS F2RZ ");
		sb.append(",CASE WHEN CASE WHEN T2.RSU_Z=0 THEN NULL ELSE ROUND(T1.RSU_Z/T2.RSU_Z*100,2) END=0 THEN NULL ELSE ");
		sb.append(" ROUND(CASE WHEN T2.RSU=0 THEN NULL ELSE ROUND(T1.RSU/T2.RSU*100,2) END/CASE WHEN T2.RSU_Z=0 THEN NULL ELSE ROUND(T1.RSU_Z/T2.RSU_Z*100,2) END*100,2) ");
		sb.append(" END AS F2RH ");
		sb.append("FROM W_BS1 T1 LEFT OUTER JOIN W_BS2 T2 ON T2.YM=T1.YM ");
		sb.append(") ");

		// 表示項目
		sb.append(", MVIW(KBN,KNM) AS ( ");
		sb.append("SELECT 1,'F2購入者' FROM DUAL ");
		sb.append("UNION ALL ");
		sb.append("SELECT 2,'F2転換率' FROM DUAL ");
		sb.append("UNION ALL ");
		sb.append("SELECT 3,'転換率前年比' FROM DUAL ");
		sb.append(") ");
		sb.append("SELECT ");
		sb.append("  MAX(CASE U.COL1 WHEN 1 THEN '単月' WHEN 2 THEN '累計' END) AS 区分 ");
		sb.append(" ,MAX(CASE WHEN U.DSU=0 THEN S.NM ELSE V.KNM END) AS 項目 ");
		sb.append(" ,MAX(CASE WHEN U.DSU=0 THEN NULL ELSE S.NM END) AS 経過 ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='01' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"1月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='02' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"2月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='03' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"3月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='04' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"4月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='05' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"5月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='06' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"6月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='07' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"7月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='08' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"8月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='09' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"9月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='10' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"10月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='11' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"11月\" ");
		sb.append(" ,SUM(CASE WHEN SUBSTR(YM,5,2)='12' THEN CASE V.KBN WHEN 1 THEN U.KSU WHEN 2 THEN U.F2KT WHEN 3 THEN U.F2KH END END) AS \"12月\" ");
		sb.append("FROM W_BS3 U ");
		sb.append("INNER JOIN DS_M_F2C S ON S.F2CNV=U.DSU ");
		sb.append(",MVIW V ");
		sb.append("WHERE NOT(U.DSU=0 AND V.KBN IN (2,3)) ");
		sb.append("GROUP BY U.COL1,V.KBN,U.DSU ");
		sb.append("ORDER BY U.COL1,V.KBN,U.DSU ");

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

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
