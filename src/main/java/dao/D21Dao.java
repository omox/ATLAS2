package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * @author EATONE
 */
public class D21Dao extends ItemDao {

	/**
	 * @param source コネクション
	 */
	public D21Dao(Connection source) {
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

		String szChanel	= getMap().get("CHANEL");	// 購入チャネル
		String szUnit	= getMap().get("UNIT");		// 集計単位
		String szYy		= getMap().get("YYYY_YY");	// 開始年
		String szYy2	= getMap().get("YYYY_YY2");	// 終了年

		System.out.println("UNIT:"+szUnit);
		System.out.println("YYYY_MM:"+szYy+",YYYY_MM2:"+szYy2);

		// 集計
		ArrayList<String> Years = new ArrayList<String>();
		if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
			for(int i=Integer.parseInt(szYy);i<=Integer.parseInt(szYy2);i++){
				Years.add(String.valueOf(i));
			}

		}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
			// 月

		}

		StringBuffer sb = new StringBuffer();
		// 集計
		if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
			// 年
			sb.append("WITH ");
			// 基本集計
			sb.append(" T_URI2 AS ( ");
			sb.append("SELECT X.YMFG,X.CLS1,X.CLS2,X.CLS3,X.KG,X.KN,P.CT FROM ( ");
			// 通販
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YM, 1, 4) AS YMFG ");
			sb.append("  , NVL(K.CHANEL,9) AS CLS1 ");
			sb.append("  , NVL(K.THKB,9) AS CLS2 ");
			sb.append("  , NVL(K.UCD,9999) AS CLS3 ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , SUM(U.SU) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_M_CPO_MEDIA U ");
			sb.append("  INNER JOIN DS_M_CPO K ");
			sb.append("    ON ( K.CHANEL = U.CHANEL AND K.BNCD = U.BNCD AND K.BNCD=U.BNCD ");
			sb.append("     AND U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append("     AND U.CHANEL=1");
			sb.append(" ) ");
			sb.append("GROUP BY SUBSTR(U.YM, 1, 4), K.CHANEL, K.THKB, K.UCD ");
			sb.append("UNION ALL ");
			// 通販その他
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YM, 1, 4) AS YMFG ");
			sb.append("  , 1 AS CLS1 ");
			sb.append("  , 2 AS CLS2 ");
			sb.append("  , 8 AS CLS3 ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , SUM(U.SU) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_M_CPO_MEDIA U ");
			sb.append("WHERE U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append("AND U.CHANEL=1 ");
			sb.append("AND NOT EXISTS (SELECT 1 FROM DS_M_CPO K WHERE U.CHANEL=K.CHANEL AND U.BNCD=K.BNCD AND K.CHANEL=1) ");
			sb.append("GROUP BY SUBSTR(U.YM, 1, 4) ");
			sb.append("UNION ALL ");
			// 店舗
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YM, 1, 4) AS YMFG ");
			sb.append("  , NVL(K.CHANEL,9) AS CLS1 ");
			sb.append("  , NVL(K.THKB,9) AS CLS2 ");
			sb.append("  , NVL(K.UCD,0) AS CLS3 ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , SUM(U.SU) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_M_CPO_MEDIA U ");
			sb.append("  INNER JOIN DS_M_CPO K ");
			sb.append("    ON ( K.CHANEL = U.CHANEL ");
			sb.append("     AND U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append("     AND U.CHANEL=2");
			sb.append(" ) ");
			sb.append("GROUP BY SUBSTR(U.YM, 1, 4), K.CHANEL, K.THKB, K.UCD ");
			sb.append(")X ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append(" SELECT ");
			sb.append("  SUBSTR(U.YM, 1, 4) AS YMFG ");
			sb.append(", NVL(U.CHANEL,9) AS CLS1 ");
			sb.append(", NVL(U.UCD,9999) AS CLS2 ");
			sb.append(", SUM(U.GK) AS CT ");
			sb.append(" FROM DS_B_CPO_PLAN U ");
			sb.append(" WHERE ");
			sb.append("  U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append(" GROUP BY SUBSTR(U.YM, 1, 4),U.CHANEL,U.UCD ");
			sb.append(") P ON (P.YMFG=X.YMFG AND P.CLS1=X.CLS1 AND P.CLS2=X.CLS3) ");
			sb.append(") ");
			// 合計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT YMFG,NVL(CLS1,9) AS CLS1,NVL(CLS2,9) AS CLS2,NVL(CLS3,999) AS CLS3,SUM(KG) AS KG,SUM(KN) AS KN,SUM(CT) AS CT FROM T_URI2 ");
			sb.append("GROUP BY GROUPING SETS ((YMFG,CLS1,CLS2,CLS3),(YMFG,CLS1,CLS2),(YMFG,CLS1),(YMFG)) ");
			sb.append(") ");
			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 1,'購入者' FROM DUAL ");
			sb.append("UNION ALL SELECT 2,'顧客単価' FROM DUAL ");
			sb.append("UNION ALL SELECT 3,'CPO' FROM DUAL ");
			//sb.append("UNION ALL SELECT 4,'金額' FROM DUAL ");
			//sb.append("UNION ALL SELECT 5,'費用' FROM DUAL ");
			sb.append(") ");
			// 軸情報
			sb.append(", AXIS(CLS1,CLS1NM,CLS2,CLS2NM,CLS3,CLS3NM) AS ( ");
			sb.append("SELECT CHANEL AS CLS1, MAX(CHANELNM) AS CLS1NM, THKB AS CLS2, MAX(THNM) AS CLS2NM, UCD AS CLS3, MAX(UNM) AS CLS3NM ");
			sb.append("FROM DS_M_CPO GROUP BY CHANEL,THKB,UCD ");
			sb.append("UNION ALL SELECT 1,'',1,'',999,'ネット計' FROM DUAL ");
			sb.append("UNION ALL SELECT 1,'',9,'',999,'通販計' FROM DUAL ");
			sb.append("UNION ALL SELECT 2,'店舗',9,'',999,'' FROM DUAL ");
			sb.append("UNION ALL SELECT 9,'通店計',9,'',999,'' FROM DUAL ");
			sb.append(") ");
			// 表示
			sb.append("SELECT MAX(CASE WHEN A.CLS1NM='通販' THEN V.KNM ELSE '　' END) AS 項目 ");
			sb.append(" ,MAX(A.CLS1NM) AS チャネル ");
			sb.append(" ,MAX(A.CLS2NM) AS 通販 ");
			sb.append(" ,MAX(A.CLS3NM) AS 集計 ");
			// 年列作成
			for (int i=0;i<Years.size();i++){
				sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \""+Years.get(i)+"年\" ");
			}
			sb.append("FROM AXIS A ");
			sb.append("LEFT OUTER JOIN T_URI U ON U.CLS1=A.CLS1 AND U.CLS2=A.CLS2 AND U.CLS3=A.CLS3 ");
			sb.append(",MVIW V ");
			sb.append("WHERE NOT(A.CLS1=2 AND A.CLS2=0 AND A.CLS3<>999) "); // 各店舗を表示しない
			sb.append("GROUP BY V.KBN,A.CLS1,A.CLS2,A.CLS3 ");
			sb.append("ORDER BY V.KBN,A.CLS1,A.CLS2,A.CLS3 ");

		}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
			// 月
			sb.append("WITH ");
			// 基本集計
			sb.append(" T_URI2 AS ( ");
			sb.append("SELECT X.YMFG,X.CLS1,X.CLS2,X.CLS3,X.KG,X.KN,P.CT FROM ( ");
			// 通販
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YM, 5, 2) AS YMFG ");
			sb.append("  , NVL(K.CHANEL,9) AS CLS1 ");
			sb.append("  , NVL(K.THKB,9) AS CLS2 ");
			sb.append("  , NVL(K.UCD,9999) AS CLS3 ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , SUM(U.SU) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_M_CPO_MEDIA U ");
			sb.append("  INNER JOIN DS_M_CPO K ");
			sb.append("    ON ( K.CHANEL = U.CHANEL AND K.BNCD = U.BNCD AND K.BNCD=U.BNCD ");
			sb.append("     AND U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append("     AND U.CHANEL=1 ");
			sb.append(" ) ");
			sb.append("GROUP BY SUBSTR(U.YM, 5, 2), K.CHANEL, K.THKB, K.UCD ");
			sb.append("UNION ALL ");
			// 通販その他
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YM,5,2) AS YMFG ");
			sb.append("  , 1 AS CLS1 ");
			sb.append("  , 2 AS CLS2 ");
			sb.append("  , 8 AS CLS3 ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , SUM(U.SU) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_M_CPO_MEDIA U ");
			sb.append("WHERE U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append("AND U.CHANEL=1 ");
			sb.append("AND NOT EXISTS (SELECT 1 FROM DS_M_CPO K WHERE U.CHANEL=K.CHANEL AND U.BNCD=K.BNCD AND K.CHANEL=1) ");
			sb.append("GROUP BY SUBSTR(U.YM,5,2) ");
			sb.append("UNION ALL ");
			// 店舗
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YM,5,2) AS YMFG ");
			sb.append("  , NVL(K.CHANEL,9) AS CLS1 ");
			sb.append("  , NVL(K.THKB,9) AS CLS2 ");
			sb.append("  , NVL(K.UCD,0) AS CLS3 ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , SUM(U.SU) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_M_CPO_MEDIA U ");
			sb.append("  INNER JOIN DS_M_CPO K ");
			sb.append("    ON ( K.CHANEL = U.CHANEL ");
			sb.append("     AND U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append("      AND U.CHANEL=2 ");
			sb.append(" ) ");
			sb.append("GROUP BY SUBSTR(U.YM,5,2), K.CHANEL, K.THKB, K.UCD ");
			sb.append(")X ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append(" SELECT ");
			sb.append("  SUBSTR(U.YM, 5, 2) AS YMFG ");
			sb.append(", NVL(U.CHANEL,9) AS CLS1 ");
			sb.append(", NVL(U.UCD,9999) AS CLS2 ");
			sb.append(", SUM(U.GK) AS CT ");
			sb.append(" FROM DS_B_CPO_PLAN U ");
			sb.append(" WHERE ");
			sb.append("  U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append(" GROUP BY SUBSTR(U.YM, 5, 2),U.CHANEL,U.UCD ");
			sb.append(") P ON (P.YMFG=X.YMFG AND P.CLS1=X.CLS1 AND P.CLS2=X.CLS3) ");
			sb.append(") ");
			// 合計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT YMFG,NVL(CLS1,9) AS CLS1,NVL(CLS2,9) AS CLS2,NVL(CLS3,999) AS CLS3,SUM(KG) AS KG,SUM(KN) AS KN,SUM(CT) AS CT FROM T_URI2 ");
			sb.append("GROUP BY GROUPING SETS ((YMFG,CLS1,CLS2,CLS3),(YMFG,CLS1,CLS2),(YMFG,CLS1),(YMFG)) ");
			sb.append(") ");
			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 1,'購入者' FROM DUAL ");
			sb.append("UNION ALL SELECT 2,'顧客単価' FROM DUAL ");
			sb.append("UNION ALL SELECT 3,'CPO' FROM DUAL ");
			//sb.append("UNION ALL SELECT 4,'金額' FROM DUAL ");
			//sb.append("UNION ALL SELECT 5,'費用' FROM DUAL ");
			sb.append(") ");
			// 軸情報
			sb.append(", AXIS(CLS1,CLS1NM,CLS2,CLS2NM,CLS3,CLS3NM) AS ( ");
			sb.append("SELECT CHANEL AS CLS1, MAX(CHANELNM) AS CLS1NM, THKB AS CLS2, MAX(THNM) AS CLS2NM, UCD AS CLS3, MAX(UNM) AS CLS3NM ");
			sb.append("FROM DS_M_CPO GROUP BY CHANEL,THKB,UCD ");
			sb.append("UNION ALL SELECT 1,'',1,'',999,'ネット計' FROM DUAL ");
			sb.append("UNION ALL SELECT 1,'',9,'',999,'通販計' FROM DUAL ");
			sb.append("UNION ALL SELECT 2,'店舗',9,'',999,'' FROM DUAL ");
			sb.append("UNION ALL SELECT 9,'通店計',9,'',999,'' FROM DUAL ");
			sb.append(") ");
			// 表示
			sb.append("SELECT MAX(CASE WHEN A.CLS1NM='通販' THEN V.KNM ELSE '　' END) AS 項目 ");
			sb.append(" ,MAX(A.CLS1NM) AS チャネル ");
			sb.append(" ,MAX(A.CLS2NM) AS 通販 ");
			sb.append(" ,MAX(A.CLS3NM) AS 集計 ");
			// 月列作成
			sb.append(", SUM(CASE WHEN YMFG = '01' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"1月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '02' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"2月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '03' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"3月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '04' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"4月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '05' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"5月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '06' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"6月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '07' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"7月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '08' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"8月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '09' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"9月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '10' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"10月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '11' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"11月\" ");
			sb.append(", SUM(CASE WHEN YMFG = '12' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN ROUND(U.KG / U.KN, 0) WHEN 3 THEN ROUND(U.CT / U.KN, 0) WHEN 4 THEN U.KG WHEN 5 THEN U.CT END END) AS \"12月\" ");
			sb.append("FROM AXIS A ");
			sb.append("LEFT OUTER JOIN T_URI U ON U.CLS1=A.CLS1 AND U.CLS2=A.CLS2 AND U.CLS3=A.CLS3 ");
			sb.append(",MVIW V ");
			sb.append("WHERE NOT(A.CLS1=2 AND A.CLS2=0 AND A.CLS3<>999) ");	// 各店舗を表示しない
			sb.append("GROUP BY V.KBN,A.CLS1,A.CLS2,A.CLS3 ");
			sb.append("ORDER BY V.KBN,A.CLS1,A.CLS2,A.CLS3 ");

		}

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

		cells = new ArrayList<String>();
		// 集計単位
		cells.add(DefineReport.RadD02_UNIT.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD02_UNIT.NAME.getObj()));
		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 集計期間
		String UNIT = jad.getJSONValue(DefineReport.RadD02_UNIT.NAME.getObj());
		cells.add(DefineReport.Label.LABELD03.getView());

		if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID1.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()));

		} else if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));

		}
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
