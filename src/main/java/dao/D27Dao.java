package dao;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * @author EATONE
 */
public class D27Dao extends ItemDao {

	/**
	 * @param source コネクション
	 */
	public D27Dao(Connection source) {
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
		String szUnit		= getMap().get("UNIT");			// 集計単位
		String szYy			= getMap().get("YYYY_YY");	// 開始年
		String szNm			= getMap().get("YYYY_MM");	// 開始月
		String szDd			= "01";	// 開始日
		String szYy2		= getMap().get("YYYY_YY2");	// 終了年
		String szNm2		= getMap().get("YYYY_MM2");	// 終了月
		String szDd2		= "31";	// 終了日

		System.out.println("CHANEL:"+szChanel+",UNIT:"+szUnit);
		System.out.println("YYYY_MM:"+szYy+szNm+szDd+",YYYY_MM2:"+szYy2+szNm2+szDd2);

		// 集計
		ArrayList<String> Years = new ArrayList<String>();
		ArrayList<String> Titles = new ArrayList<String>();
		String szZYY = szYy;	// 前年

		if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
			for(int i=Integer.parseInt(szYy);i<=Integer.parseInt(szYy2);i++){
				Years.add(String.valueOf(i));
			}

		}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
			// 月

		}else if (DefineReport.RadD02_UNIT.ID3.getVal().equals(szUnit)){
			// 日
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			try {
				Date date = sdf.parse(szYy+szNm+"01");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int lastDayOfMonth = calendar.getActualMaximum(Calendar.DATE);

				Date d1 = calendar.getTime();
				String[] week_name = {"日", "月", "火", "水", "木", "金", "土"};
				String szDT="";

				for (int i=0;i<lastDayOfMonth;i++){
					d1 = calendar.getTime();
					szDT = sdf.format(d1).substring(0, 8);
					Years.add(szDT);
					Titles.add(szDT.substring(4, 6)+"/"+szDT.substring(6, 8)+" "+week_name[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
					calendar.add(Calendar.DATE, 1);
				}

			} catch(ParseException e) {
				e.printStackTrace();
			}

		}else if (DefineReport.RadD02_UNIT.ID4.getVal().equals(szUnit)){
			// 累計
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			try {
				Date date = sdf.parse(szYy+szNm+"01");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				// 前年
				calendar.add(Calendar.YEAR, -1);
				Date d1 = calendar.getTime();
				szZYY = sdf.format(d1).substring(0, 4);
				Years.add(" BETWEEN "+String.valueOf(szZYY)+"0101 AND "+String.valueOf(szZYY)+szNm2+"31");

				// 当年
				Years.add(" BETWEEN "+String.valueOf(szYy)+"0101 AND "+String.valueOf(szYy)+szNm2+"31");

			} catch(ParseException e) {
				e.printStackTrace();
			}
			System.out.println("szZYY:"+szZYY);
		}

		// チャネル判定
		String szChanelCode = "";
		if (DefineReport.RadD01_CHANEL.ID1.getVal().equals(szChanel)){
			// 通店計
			szChanelCode =
					 ", NVL(SUM(U.HPNTH),0)+NVL(SUM(U.HPNTP),0) AS HPN "
					+", NVL(SUM(U.SPNTH),0)+NVL(SUM(U.SPNTP),0) AS SPN ";
		} else if (DefineReport.RadD01_CHANEL.ID2.getVal().equals(szChanel)) {
			// 通販
			szChanelCode =
					 ", NVL(SUM(U.HPNTH),0) AS HPN "
					+", NVL(SUM(U.SPNTH),0) AS SPN ";
		} else if (DefineReport.RadD01_CHANEL.ID3.getVal().equals(szChanel)) {
			// 店舗
			szChanelCode =
					 ", NVL(SUM(U.HPNTP),0) AS HPN "
					+", NVL(SUM(U.SPNTP),0) AS SPN ";
		}

		// テスト用("_T")
		String plusTableName="";
		StringBuffer sb = new StringBuffer();
		// 集計
		if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
			// 年
			sb.append("WITH ");
			// 基本集計
			sb.append("T_PNT AS ( ");
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YMD,1,4) AS YMFG ");
			sb.append("  , CLS AS CLSNO ");
			sb.append(szChanelCode);
			sb.append("FROM ");
			sb.append("  DS_S_PNT"+plusTableName+" U ");
			sb.append("WHERE ");
			sb.append("      U.YMD BETWEEN "+szYy+"0101 AND "+szYy2+"1231 ");
			sb.append("GROUP BY SUBSTR(U.YMD,1,4),CLS ");
			sb.append(") ");
			// 実績
			sb.append(",T_URIJ AS ( ");
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.URI_YMD,1,4) AS YMFG ");
			sb.append("  , COALESCE(SUM(U.JUN_URIGK_TAX), 0)-COALESCE(SUM(U.JUN_URIGK_HEN_TAX), 0) AS JGK ");
			sb.append("FROM URI_J U ");
			sb.append("WHERE ");
			sb.append("  U.URI_YMD BETWEEN '"+szYy+"0101' AND '"+szYy2+"1231' ");
			sb.append(" AND U.CHANEL_CD1='1' AND U.CHANEL_CD2 IN ('10','20') ");
			sb.append("GROUP BY SUBSTR(U.URI_YMD,1,4) ");
			sb.append(") ");

			sb.append(",T_URI AS ( ");
			sb.append("SELECT T1.YMFG,CASE WHEN GROUPING_ID(T1.CLSNO)=1 THEN 99 ELSE T1.CLSNO END AS CLSNO ");
			sb.append(",SUM(T1.HPN) AS HPN ");
			sb.append(",SUM(T1.SPN) AS SPN ");
			sb.append(",CASE WHEN SUM(T2.JGK)=0 THEN NULL ELSE ROUND(SUM(T1.HPN)/AVG(T2.JGK)*100,2) END AS HPP ");
			sb.append(",CASE WHEN SUM(T2.JGK)=0 THEN NULL ELSE ROUND(SUM(T1.SPN)/AVG(T2.JGK)*100,2) END AS SPP ");
			sb.append(",AVG(T2.JGK) AS JGK ");
			sb.append("FROM T_PNT T1 ");
			sb.append("LEFT OUTER JOIN T_URIJ T2 ON T1.YMFG = T2.YMFG ");
			sb.append("GROUP BY GROUPING SETS ((T1.YMFG,T1.CLSNO),(T1.YMFG)) ");
			sb.append(") ");

			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 1,'発行額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'使用額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'発行率（対上代）' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'使用率（対上代）' FROM DUAL ");
			sb.append(") ");

			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");

			for (int i=0;i<Years.size();i++){
				sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \""+Years.get(i)+"年\" ");
			}

			sb.append("FROM T_URI U ");
			sb.append("INNER JOIN DS_M_PNT A ON U.CLSNO=A.PCD ");
			sb.append(",MVIW V ");
			sb.append("GROUP BY V.KBN,A.SEQ ");
			sb.append("ORDER BY V.KBN,A.SEQ ");

		}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
			// 月
			sb.append("WITH ");
			// 基本集計
			sb.append("T_PNT AS ( ");
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YMD,5,2) AS YMFG ");
			sb.append("  , CLS AS CLSNO ");
			sb.append(szChanelCode);
			sb.append("FROM ");
			sb.append("  DS_S_PNT"+plusTableName+" U ");
			sb.append("WHERE ");
			sb.append("  U.YMD BETWEEN "+szYy+"0101 AND "+szYy+"1231 ");
			sb.append("GROUP BY SUBSTR(U.YMD,5,2),CLS ");
			sb.append(") ");

			sb.append(",T_URIJ AS ( ");
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.URI_YMD,5,2) AS YMFG ");
			sb.append("  , COALESCE(SUM(U.JUN_URIGK_TAX), 0)-COALESCE(SUM(U.JUN_URIGK_HEN_TAX), 0) AS JGK ");
			sb.append("FROM URI_J U ");
			sb.append("WHERE ");
			sb.append("  U.URI_YMD BETWEEN '"+szYy+"0101' AND '"+szYy+"1231' ");
			sb.append(" AND U.CHANEL_CD1='1' AND U.CHANEL_CD2 IN ('10','20') ");
			sb.append("GROUP BY SUBSTR(U.URI_YMD,5,2) ");
			sb.append(") ");

			sb.append(",T_URI AS ( ");
			sb.append("SELECT T1.YMFG,CASE WHEN GROUPING_ID(T1.CLSNO)=1 THEN 99 ELSE T1.CLSNO END AS CLSNO ");
			sb.append(",SUM(T1.HPN) AS HPN ");
			sb.append(",SUM(T1.SPN) AS SPN ");
			sb.append(",CASE WHEN SUM(T2.JGK)=0 THEN NULL ELSE ROUND(SUM(T1.HPN)/AVG(T2.JGK)*100,2) END AS HPP ");
			sb.append(",CASE WHEN SUM(T2.JGK)=0 THEN NULL ELSE ROUND(SUM(T1.SPN)/AVG(T2.JGK)*100,2) END AS SPP ");
			sb.append(",AVG(T2.JGK) AS JGK ");
			sb.append("FROM T_PNT T1 ");
			sb.append("LEFT OUTER JOIN T_URIJ T2 ON T1.YMFG = T2.YMFG ");
			sb.append("GROUP BY GROUPING SETS ((T1.YMFG,T1.CLSNO),(T1.YMFG)) ");
			sb.append(") ");

			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 1,'発行額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'使用額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'発行率（対上代）' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'使用率（対上代）' FROM DUAL ");
			sb.append(") ");
			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");
			sb.append(" ,SUM(CASE WHEN YMFG='01' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"1月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='02' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"2月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='03' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"3月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='04' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"4月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='05' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"5月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='06' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"6月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='07' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"7月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='08' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"8月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='09' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"9月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='10' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"10月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"11月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='12' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \"12月\" ");
			sb.append("FROM T_URI U ");
			sb.append("INNER JOIN DS_M_PNT A ON U.CLSNO=A.PCD ");
			sb.append(",MVIW V ");
			sb.append("GROUP BY V.KBN,A.SEQ ");
			sb.append("ORDER BY V.KBN,A.SEQ ");

		}else if (DefineReport.RadD02_UNIT.ID3.getVal().equals(szUnit)){
			// 日
			sb.append("WITH ");
			// 基本集計
			sb.append("T_PNT AS ( ");
			sb.append("SELECT ");
			sb.append("    U.YMD AS YMFG ");
			sb.append("  , CLS AS CLSNO ");
			sb.append(szChanelCode);
			sb.append("FROM ");
			sb.append("  DS_S_PNT"+plusTableName+" U ");
			sb.append("WHERE ");
			sb.append("  U.YMD BETWEEN "+szYy+szNm+"01 AND "+szYy+szNm+"31 ");
			sb.append("GROUP BY U.YMD,CLS ");
			sb.append(") ");

			sb.append(",T_URIJ AS ( ");
			sb.append("SELECT ");
			sb.append("    U.URI_YMD AS YMFG ");
			sb.append("  , COALESCE(SUM(U.JUN_URIGK_TAX), 0)-COALESCE(SUM(U.JUN_URIGK_HEN_TAX), 0) AS JGK ");
			sb.append("FROM URI_J U ");
			sb.append("WHERE ");
			sb.append("  U.URI_YMD BETWEEN '"+szYy+szNm+"01' AND '"+szYy+szNm+"31' ");
			sb.append(" AND U.CHANEL_CD1='1' AND U.CHANEL_CD2 IN ('10','20') ");
			sb.append("GROUP BY U.URI_YMD ");
			sb.append(") ");

			sb.append(",T_URI AS ( ");
			sb.append("SELECT T1.YMFG,CASE WHEN GROUPING_ID(T1.CLSNO)=1 THEN 99 ELSE T1.CLSNO END AS CLSNO ");
			sb.append(",SUM(T1.HPN) AS HPN ");
			sb.append(",SUM(T1.SPN) AS SPN ");
			sb.append(",CASE WHEN SUM(T2.JGK)=0 THEN NULL ELSE ROUND(SUM(T1.HPN)/AVG(T2.JGK)*100,2) END AS HPP ");
			sb.append(",CASE WHEN SUM(T2.JGK)=0 THEN NULL ELSE ROUND(SUM(T1.SPN)/AVG(T2.JGK)*100,2) END AS SPP ");
			sb.append(",AVG(T2.JGK) AS JGK ");
			sb.append("FROM T_PNT T1 ");
			sb.append("LEFT OUTER JOIN T_URIJ T2 ON T1.YMFG = T2.YMFG ");
			sb.append("GROUP BY GROUPING SETS ((T1.YMFG,T1.CLSNO),(T1.YMFG)) ");
			sb.append(") ");

			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 1,'発行額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'使用額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'発行率（対上代）' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'使用率（対上代）' FROM DUAL ");
			sb.append(") ");

			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");

			for (int i=0;i<Years.size();i++){
				sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.HPN WHEN 2 THEN U.SPN WHEN 3 THEN U.HPP WHEN 4 THEN U.SPP END END) AS \""+Titles.get(i)+"\" ");
			}

			sb.append("FROM T_URI U ");
			sb.append("INNER JOIN DS_M_PNT A ON U.CLSNO=A.PCD ");
			sb.append(",MVIW V ");
			sb.append("GROUP BY V.KBN,A.SEQ ");
			sb.append("ORDER BY V.KBN,A.SEQ ");

		}else if (DefineReport.RadD02_UNIT.ID4.getVal().equals(szUnit)){
			// 累計
			sb.append("WITH ");
			// 基本集計
			sb.append("T_PNT AS ( ");
			sb.append("SELECT ");
			sb.append("    CASE WHEN SUBSTR(U.YMD,1,4)="+szYy+" THEN 1 ELSE 2 END AS YMFG ");
			sb.append("  , CLS AS CLSNO ");
			sb.append(szChanelCode);
			sb.append("FROM ");
			sb.append("  DS_S_PNT"+plusTableName+" U ");
			sb.append("WHERE ");
			sb.append("   U.YMD "+Years.get(0));
			sb.append("OR U.YMD "+Years.get(1));
			sb.append("GROUP BY CASE WHEN SUBSTR(U.YMD,1,4)="+szYy+" THEN 1 ELSE 2 END,CLS ");
			sb.append(") ");

			sb.append(",T_URIJ AS ( ");
			sb.append("SELECT ");
			sb.append("    CASE WHEN SUBSTR(U.URI_YMD,1,4)="+szYy+" THEN 1 ELSE 2 END AS YMFG ");
			sb.append("  , COALESCE(SUM(U.JUN_URIGK_TAX), 0)-COALESCE(SUM(U.JUN_URIGK_HEN_TAX), 0) AS JGK ");
			sb.append("FROM URI_J U ");
			sb.append("WHERE ");
			sb.append("  (U.URI_YMD "+Years.get(0));
			sb.append("OR U.URI_YMD "+Years.get(1));
			sb.append("  ) ");
			sb.append(" AND U.CHANEL_CD1='1' AND U.CHANEL_CD2 IN ('10','20') ");
			sb.append("GROUP BY CASE WHEN SUBSTR(U.URI_YMD,1,4)="+szYy+" THEN 1 ELSE 2 END ");
			sb.append(") ");

			sb.append(",W_URI AS ( ");
			sb.append("SELECT T1.YMFG,CASE WHEN GROUPING_ID(T1.CLSNO)=1 THEN 99 ELSE T1.CLSNO END AS CLSNO ");
			sb.append(",SUM(T1.HPN) AS HPN ");
			sb.append(",SUM(T1.SPN) AS SPN ");
			sb.append(",CASE WHEN SUM(T2.JGK)=0 THEN NULL ELSE ROUND(SUM(T1.HPN)/AVG(T2.JGK)*100,2) END AS HPP ");
			sb.append(",CASE WHEN SUM(T2.JGK)=0 THEN NULL ELSE ROUND(SUM(T1.SPN)/AVG(T2.JGK)*100,2) END AS SPP ");
			sb.append(",AVG(T2.JGK) AS JGK ");
			sb.append("FROM T_PNT T1 ");
			sb.append("LEFT OUTER JOIN T_URIJ T2 ON T1.YMFG = T2.YMFG ");
			sb.append("GROUP BY GROUPING SETS ((T1.YMFG,T1.CLSNO),(T1.YMFG)) ");
			sb.append(") ");

			// 基本集計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT CLSNO ");
			sb.append(",SUM(HPNT)AS HPNT,AVG(HPAT) AS HPAT,SUM(SPNT) AS SPNT,AVG(SPAT) AS SPAT,SUM(HPPT) AS HPPT,SUM(SPPT) AS SPPT,SUM(JGKT) AS JGKT ");
			sb.append(",SUM(HPNZ)AS HPNZ,AVG(HPAZ) AS HPAZ,SUM(SPNZ) AS SPNZ,AVG(SPAZ) AS SPAZ,SUM(HPPZ) AS HPPZ,SUM(SPPZ) AS SPPZ,SUM(JGKZ) AS JGKZ ");
			sb.append("FROM ( ");
			sb.append("SELECT ");
			sb.append("    CLSNO ");
			sb.append("  , CASE WHEN U.YMFG = 1 THEN U.HPN END AS HPNT ");
			sb.append("  , SUM(CASE WHEN U.YMFG = 1 AND U.CLSNO<>99 THEN U.HPN END) OVER() AS HPAT ");
			sb.append("  , CASE WHEN U.YMFG = 1 THEN U.SPN END AS SPNT ");
			sb.append("  , SUM(CASE WHEN U.YMFG = 1 AND U.CLSNO<>99 THEN U.SPN END) OVER() AS SPAT ");
			sb.append("  , CASE WHEN U.YMFG = 1 THEN U.HPP END AS HPPT ");
			sb.append("  , CASE WHEN U.YMFG = 1 THEN U.SPP END AS SPPT ");
			sb.append("  , CASE WHEN U.YMFG = 1 THEN U.JGK END AS JGKT ");
			sb.append("  , CASE WHEN U.YMFG = 2 THEN U.HPN END AS HPNZ ");
			sb.append("  , SUM(CASE WHEN U.YMFG = 2 AND U.CLSNO<>99 THEN U.HPN END) OVER() AS HPAZ ");
			sb.append("  , CASE WHEN U.YMFG = 2 THEN U.SPN END AS SPNZ ");
			sb.append("  , SUM(CASE WHEN U.YMFG = 2 AND U.CLSNO<>99 THEN U.SPN END) OVER() AS SPAZ ");
			sb.append("  , CASE WHEN U.YMFG = 2 THEN U.HPP END AS HPPZ ");
			sb.append("  , CASE WHEN U.YMFG = 2 THEN U.SPP END AS SPPZ ");
			sb.append("  , CASE WHEN U.YMFG = 2 THEN U.JGK END AS JGKZ ");
			sb.append("FROM ");
			sb.append("  W_URI U ");
			sb.append(")X GROUP BY CLSNO ");
			sb.append(") ");

			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 1,'発行額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'使用額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'発行率（対上代）' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'使用率（対上代）' FROM DUAL ");
			sb.append(") ");

			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");
			sb.append(" ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 1 THEN U.HPNT ");
			sb.append("    WHEN 2 THEN U.SPNT ");
			sb.append("    WHEN 3 THEN U.HPPT ");
			sb.append("    WHEN 4 THEN U.SPPT ");
			sb.append("    END) AS 当期_実績 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 1 THEN CASE WHEN U.HPAT=0 THEN NULL ELSE ROUND(U.HPNT/U.HPAT*100,2) END ");
			sb.append("    WHEN 2 THEN CASE WHEN U.SPAT=0 THEN NULL ELSE ROUND(U.SPNT/U.SPAT*100,2) END ");
			sb.append("    WHEN 3 THEN NULL ");
			sb.append("    WHEN 4 THEN NULL ");
			sb.append("    END) AS 当期_構成比 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 1 THEN U.HPNZ ");
			sb.append("    WHEN 2 THEN U.SPNZ ");
			sb.append("    WHEN 3 THEN U.HPPZ ");
			sb.append("    WHEN 4 THEN U.SPPZ ");
			sb.append("    END) AS 前期_実績 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 1 THEN CASE WHEN U.HPAZ=0 THEN NULL ELSE ROUND(U.HPNZ/U.HPAZ*100,2) END ");
			sb.append("    WHEN 2 THEN CASE WHEN U.SPAZ=0 THEN NULL ELSE ROUND(U.SPNZ/U.SPAZ*100,2) END ");
			sb.append("    WHEN 3 THEN NULL ");
			sb.append("    WHEN 4 THEN NULL ");
			sb.append("    END) AS 前期_構成比 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 1 THEN U.HPNT-U.HPNZ ");
			sb.append("    WHEN 2 THEN U.SPNT-U.SPNZ ");
			sb.append("    WHEN 3 THEN U.HPPT-U.HPPZ ");
			sb.append("    WHEN 4 THEN U.SPPT-U.SPPZ ");
			sb.append("    END) AS 前期差 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 1 THEN CASE WHEN U.HPNZ=0 THEN NULL ELSE ROUND(U.HPNT/U.HPNZ*100,2) END ");
			sb.append("    WHEN 2 THEN CASE WHEN U.SPNZ=0 THEN NULL ELSE ROUND(U.SPNT/U.SPNZ*100,2) END ");
			sb.append("    WHEN 3 THEN CASE WHEN U.HPPZ=0 THEN NULL ELSE ROUND(U.HPPT/U.HPPZ*100,2) END ");
			sb.append("    WHEN 4 THEN CASE WHEN U.SPPZ=0 THEN NULL ELSE ROUND(U.SPPT/U.SPPZ*100,2) END ");
			sb.append("    END) AS 前期比 ");
			sb.append("FROM T_URI U ");
			sb.append("INNER JOIN DS_M_PNT A ON U.CLSNO=A.PCD ");
			sb.append(",MVIW V ");
			sb.append("GROUP BY V.KBN,A.SEQ ");
			sb.append("ORDER BY V.KBN,A.SEQ ");
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

		// 購入チャネル
		cells.add(DefineReport.RadD01_CHANEL.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD01_CHANEL.NAME.getObj()));
		getWhere().add(cells);

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

		} else if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID3.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));

		} else if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID4.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN_TO.getObj()));
		}
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
