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
public class D18Dao extends ItemDao {

	/**
	 * @param source コネクション
	 */
	public D18Dao(Connection source) {
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
		String szDd			= getMap().get("YYYY_DD");	// 開始日
		String szYy2		= getMap().get("YYYY_YY2");	// 終了年
		String szNm2		= getMap().get("YYYY_MM2");	// 終了月
		String szDd2		= getMap().get("YYYY_DD2");	// 終了日

		String szITEM		= getMap().get("ITEM");			// 購入商品

		String szSho1a = getMap().get("SEL_SHO1");	// 商品分類-基本分類
		String szSho2a = getMap().get("SEL_SHO2");	// 商品分類-大分類
		String szSho3a = getMap().get("SEL_SHO3");	// 商品分類-中分類
		String szSho4a = getMap().get("SEL_SHO4");	// 商品分類-小分類
		String szSho5a = getMap().get("SEL_SHO5");	// 商品分類-シリーズＡ
		String szSho6a = getMap().get("SEL_SHO6");	// 商品分類-シリーズＢ

		System.out.println("CHANEL:"+szChanel+",UNIT:"+szUnit);
		System.out.println("YYYY_MM:"+szYy+szNm+szDd+",YYYY_MM2:"+szYy2+szNm2+szDd2);
		System.out.println("ITEM:"+szITEM);
		System.out.println("SEL_SHO:"+szSho1a+szSho2a+szSho3a+szSho4a+szSho5a+szSho6a);

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
				if ("01".equals(szNm2)){
					Years.add(" = "+String.valueOf(szZYY)+"01");
				}else{
					Years.add(" BETWEEN "+String.valueOf(szZYY)+"01 AND "+String.valueOf(szZYY)+szNm2+"");
				}

				// 当年
				if ("01".equals(szNm2)){
					Years.add(" = "+String.valueOf(szYy)+"01");
				}else{
					Years.add(" BETWEEN "+String.valueOf(szYy)+"01 AND "+String.valueOf(szYy)+szNm2+"");
				}

			} catch(ParseException e) {
				e.printStackTrace();
			}
			System.out.println("szZYY:"+szZYY);
		}

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
		String szCLSNM="2";	// 計画値コード
		String szCLSTP=DefineReport.RadD09_CLS.ID3.getView();	// タイトル

		StringBuffer sbCLASS = new StringBuffer();

		// 分野別
		if(
			DefineReport.RadD09_CLS.ID1.getVal().equals(szITEM)
		||DefineReport.RadD09_CLS.ID2.getVal().equals(szITEM)
		||DefineReport.RadD09_CLS.ID3.getVal().equals(szITEM)
		){
			// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
			if(DefineReport.RadD09_CLS.ID1.getVal().equals(szITEM)){
				szCLSNM="0";
				szCLSTP=DefineReport.RadD09_CLS.ID1.getView();
			}else if (DefineReport.RadD09_CLS.ID2.getVal().equals(szITEM)){
				szCLSNM="1";
				szCLSTP=DefineReport.RadD09_CLS.ID2.getView();
			}

			// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
			sbCLASS.append("W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
			sbCLASS.append("SELECT YMD,CLSNM,TYPNM ");
			sbCLASS.append(",CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ ");
			sbCLASS.append(",CLS1||CASE WHEN CLS2='00' THEN '%' ELSE CLS2 END||CASE WHEN CLS3='00' THEN '%' ELSE CLS3 END||CASE WHEN CLS4='00' THEN '%' ELSE CLS4 END||CASE WHEN CLS5='00' THEN '%' ELSE CLS5 END||CASE WHEN CLS6='00' THEN '%' ELSE CLS6 END ");
			sbCLASS.append("FROM DS_B_CLASS ");
			sbCLASS.append("WHERE CLSNM="+szCLSNM);
			sbCLASS.append("AND YMD = (SELECT MAX(T1.YMD) AS YMD FROM DS_B_CLASS T1 WHERE T1.YMD<="+szYy+szNm+szDd+" GROUP BY T1.YMD) ");
			sbCLASS.append(") ");

		}else{

			// 分析用分類
			sbCLASS.append("W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
			sbCLASS.append("SELECT 0,'',''");
			if (!"00".equals(szSho1a)){sbCLASS.append(",'"+szSho1a.substring(0)+"'");} else{sbCLASS.append(",'"+szSho1a+"'");}
			if (!"00".equals(szSho2a)){sbCLASS.append(",'"+szSho2a.substring(2)+"'");} else{sbCLASS.append(",'"+szSho2a+"'");}
			if (!"00".equals(szSho3a)){sbCLASS.append(",'"+szSho3a.substring(4)+"'");} else{sbCLASS.append(",'"+szSho3a+"'");}
			if (!"00".equals(szSho4a)){sbCLASS.append(",'"+szSho4a.substring(6)+"'");} else{sbCLASS.append(",'"+szSho4a+"'");}
			if (!"00".equals(szSho5a)){sbCLASS.append(",'"+szSho5a.substring(8)+"'");} else{sbCLASS.append(",'"+szSho5a+"'");}
			if (!"00".equals(szSho6a)){sbCLASS.append(",'"+szSho6a.substring(10)+"'");}else{sbCLASS.append(",'"+szSho6a+"'");}
			sbCLASS.append(",'00',1,'' FROM DUAL ");
			sbCLASS.append(") ");
		}

		// 集計
		if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
			// 年
			sb.append("WITH ");
			// 基本分野
			sb.append(sbCLASS.toString());

			sb.append(",W_CAL(YMD) AS ( ");
			sb.append("SELECT DISTINCT SUBSTR(YMD_CD,1,4) FROM M_CALENDAR WHERE YMD_CD BETWEEN '"+szYy+"0101' AND '"+szYy2+"1231' ");
			sb.append(") ");
			sb.append(",W_BASE(YMD,CD) AS ( ");
			sb.append("SELECT YMD,CD FROM W_CAL,DS_M_KINGAKU ");
			sb.append(") ");

			// 基本集計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT ");
			sb.append(" T0.YMD AS YMFG, ");
			sb.append(" CASE WHEN GROUPING_ID(T0.CD)=1 THEN 9 ELSE T0.CD END AS CLSNO ");
			sb.append(",SUM(T1.SU) AS SU ");
			sb.append(",SUM(T1.KG) AS KG ");
			sb.append(",SUM(T1.KN) AS KN ");
			sb.append(",SUM(T2.SU) AS KT ");
			sb.append("FROM W_BASE T0 ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append("SELECT ");
			sb.append("    SUBSTR(K.YM,1,4) AS YMFG ");
			sb.append("  , CLSNO AS CLSNO ");
			sb.append("  , SUM(K.SU) AS SU ");
			sb.append("  , SUM(K.KG) AS KG ");
			sb.append("  , COUNT(DISTINCT K.KNOT) AS KN ");
			sb.append("FROM (SELECT SUBSTR(U.YM,1,4) AS YM, MIN(CASE WHEN K.DTSHU = U.YMD AND U.NFLG=1 THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 1 END) AS CLSNO, MIN(ZYKG) AS ZYKG, SUM(SU) AS SU,SUM(KG) AS KG, U.KNOT");
			sb.append("  FROM ");
			sb.append("  DS_S_URI_D U ");
			sb.append("  INNER JOIN W_CLASS W ON ( ");
			sb.append("      U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
			sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
			sb.append("  AND U.KNOT<>0 ");
			sb.append("  AND U.CLS1 LIKE CASE WHEN W.CLS1='00' THEN '%' ELSE W.CLS1 END ");
			sb.append("  AND U.CLS2 LIKE CASE WHEN W.CLS2='00' THEN '%' ELSE W.CLS2 END ");
			sb.append("  AND U.CLS3 LIKE CASE WHEN W.CLS3='00' THEN '%' ELSE W.CLS3 END ");
			sb.append("  AND U.CLS4 LIKE CASE WHEN W.CLS4='00' THEN '%' ELSE W.CLS4 END ");
			sb.append("  AND U.CLS5 LIKE CASE WHEN W.CLS5='00' THEN '%' ELSE W.CLS5 END ");
			sb.append("  AND U.CLS6 LIKE CASE WHEN W.CLS6='00' THEN '%' ELSE W.CLS6 END ");
			sb.append("  AND U.ICD  LIKE CASE WHEN W.ICD ='00' THEN '%' ELSE W.ICD  END ");
			sb.append("  ) ");
			sb.append("  INNER JOIN DS_S_KNO_M K ON (U.YM=K.YM AND U.KNOT=K.KNOT) ");
			sb.append("  GROUP BY SUBSTR(U.YM,1,4),U.KNOT)K ");
			sb.append("GROUP BY ");
			sb.append("K.YM,CLSNO ");
			sb.append(") T1 ON T1.YMFG=T0.YMD AND T1.CLSNO=T0.CD ");
			sb.append("LEFT OUTER JOIN DS_M_KNO_Z T2 ON SUBSTR(T0.YMD,1,4)=T2.YM AND T0.CD=T2.CLSNO ");
			sb.append("GROUP BY GROUPING SETS((T0.YMD,T0.CD),(T0.YMD)) ");
			sb.append(") ");

			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 0,'対象者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 1,'購入者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'購入率' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'購入金額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'顧客単価' FROM DUAL ");
			sb.append(") ");
			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");
			for (int i=0;i<Years.size();i++){
				sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \""+Years.get(i)+"年\" ");
			}
			sb.append("FROM T_URI U ");
			sb.append("INNER JOIN DS_M_KINGAKU A ON U.CLSNO=A.CD ");
			sb.append(",MVIW V ");
			sb.append("GROUP BY V.KBN,U.CLSNO ");
			sb.append("ORDER BY V.KBN,U.CLSNO ");

		}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
			// 月
			sb.append("WITH ");
			// 基本分野
			sb.append(sbCLASS.toString());

			sb.append(",W_CAL(YMD) AS ( ");
			sb.append("SELECT DISTINCT SUBSTR(YMD_CD,1,6) FROM M_CALENDAR WHERE YMD_CD BETWEEN '"+szYy+"0101' AND '"+szYy+"1231' ");
			sb.append(") ");
			sb.append(",W_BASE(YMD,CD) AS ( ");
			sb.append("SELECT YMD,CD FROM W_CAL,DS_M_KINGAKU ");
			sb.append(") ");

			// 基本集計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT ");
			sb.append(" SUBSTR(T0.YMD,5,2) AS YMFG, ");
			sb.append(" CASE WHEN GROUPING_ID(T0.CD)=1 THEN 9 ELSE T0.CD END AS CLSNO ");
			sb.append(",SUM(T1.SU) AS SU ");
			sb.append(",SUM(T1.KG) AS KG ");
			sb.append(",SUM(T1.KN) AS KN ");
			sb.append(",SUM(T2.SU) AS KT ");
			sb.append("FROM W_BASE T0 ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append("SELECT ");
			sb.append("    U.YM AS YMFG ");
			sb.append("  , CLSNO AS CLSNO ");
			sb.append("  , SUM(U.SU) AS SU ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , COUNT(DISTINCT U.KNOT) AS KN ");
			//
			sb.append(" FROM (SELECT U.YM AS YM, MIN(CASE WHEN K.DTSHU = U.YMD AND U.NFLG=1 THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 1 END) AS CLSNO, MIN(ZYKG) AS ZYKG, SUM(SU) AS SU,SUM(KG) AS KG, U.KNOT");
			sb.append(" FROM ");
			sb.append("  DS_S_URI_D U ");
			sb.append("  INNER JOIN W_CLASS W ON ( ");
			sb.append("      U.YM BETWEEN '"+szYy+"01' AND '"+szYy+"12' ");
			sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
			sb.append("  AND U.KNOT<>0 ");
			sb.append("  AND U.CLS1 LIKE CASE WHEN W.CLS1='00' THEN '%' ELSE W.CLS1 END ");
			sb.append("  AND U.CLS2 LIKE CASE WHEN W.CLS2='00' THEN '%' ELSE W.CLS2 END ");
			sb.append("  AND U.CLS3 LIKE CASE WHEN W.CLS3='00' THEN '%' ELSE W.CLS3 END ");
			sb.append("  AND U.CLS4 LIKE CASE WHEN W.CLS4='00' THEN '%' ELSE W.CLS4 END ");
			sb.append("  AND U.CLS5 LIKE CASE WHEN W.CLS5='00' THEN '%' ELSE W.CLS5 END ");
			sb.append("  AND U.CLS6 LIKE CASE WHEN W.CLS6='00' THEN '%' ELSE W.CLS6 END ");
			sb.append("  AND U.ICD  LIKE CASE WHEN W.ICD ='00' THEN '%' ELSE W.ICD  END ");
			sb.append("  ) ");
			sb.append("  INNER JOIN DS_S_KNO_M K ON (U.YM=K.YM AND U.KNOT=K.KNOT) ");
			//
			sb.append("  GROUP BY U.YM,U.KNOT)U ");
			sb.append("GROUP BY ");
			sb.append("U.YM,U.CLSNO ");
			sb.append(") T1 ON T1.YMFG=T0.YMD AND T1.CLSNO=T0.CD ");
			sb.append("LEFT OUTER JOIN DS_M_KNO_Z T2 ON SUBSTR(T0.YMD,1,6)=T2.YM AND T0.CD=T2.CLSNO ");
			sb.append("GROUP BY GROUPING SETS((T0.YMD,T0.CD),(T0.YMD)) ");
			sb.append(") ");

			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 0,'対象者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 1,'購入者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'購入率' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'購入金額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'顧客単価' FROM DUAL ");
			sb.append(") ");
			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分1 ");
			sb.append(" ,SUM(CASE WHEN YMFG='01' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"1月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='02' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"2月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='03' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"3月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='04' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"4月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='05' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"5月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='06' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"6月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='07' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"7月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='08' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"8月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='09' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"9月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='10' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"10月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"11月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='12' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \"12月\" ");
			sb.append("FROM T_URI U ");
			sb.append("INNER JOIN DS_M_KINGAKU A ON U.CLSNO=A.CD ");
			sb.append(",MVIW V ");
			sb.append("GROUP BY V.KBN,U.CLSNO ");
			sb.append("ORDER BY V.KBN,U.CLSNO ");

		}else if (DefineReport.RadD02_UNIT.ID3.getVal().equals(szUnit)){
			// 日
			sb.append("WITH ");
			// 基本分野
			sb.append(sbCLASS.toString());

			sb.append(",W_CAL(YMD) AS ( ");
			sb.append("SELECT YMD_CD FROM M_CALENDAR WHERE SUBSTR(YMD_CD,1,6) = "+szYy+szNm+" ");
			sb.append(") ");
			sb.append(",W_BASE(YMD,CD) AS ( ");
			sb.append("SELECT YMD,CD FROM W_CAL,DS_M_KINGAKU ");
			sb.append(") ");

			// 基本集計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT ");
			sb.append(" T0.YMD AS YMFG, ");
			sb.append(" CASE WHEN GROUPING_ID(T0.CD)=1 THEN 9 ELSE T0.CD END AS CLSNO ");
			sb.append(",SUM(T1.SU) AS SU ");
			sb.append(",SUM(T1.KG) AS KG ");
			sb.append(",SUM(T1.KN) AS KN ");
			sb.append(",SUM(T2.SU) AS KT ");
			sb.append("FROM W_BASE T0 ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append("SELECT ");
			sb.append("    U.YMD AS YMFG ");
			sb.append("  , CASE WHEN K.DTSHU = U.YMD AND U.NFLG=1 THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 1 END AS CLSNO ");
			sb.append("  , SUM(U.SU) AS SU ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , COUNT(DISTINCT U.KNOT) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_S_URI_D U ");
			sb.append("  INNER JOIN W_CLASS W ON ( ");
			sb.append("      U.YM = "+szYy+szNm+" ");
			sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
			sb.append("  AND U.KNOT<>0 ");
			sb.append("  AND U.CLS1 LIKE CASE WHEN W.CLS1='00' THEN '%' ELSE W.CLS1 END ");
			sb.append("  AND U.CLS2 LIKE CASE WHEN W.CLS2='00' THEN '%' ELSE W.CLS2 END ");
			sb.append("  AND U.CLS3 LIKE CASE WHEN W.CLS3='00' THEN '%' ELSE W.CLS3 END ");
			sb.append("  AND U.CLS4 LIKE CASE WHEN W.CLS4='00' THEN '%' ELSE W.CLS4 END ");
			sb.append("  AND U.CLS5 LIKE CASE WHEN W.CLS5='00' THEN '%' ELSE W.CLS5 END ");
			sb.append("  AND U.CLS6 LIKE CASE WHEN W.CLS6='00' THEN '%' ELSE W.CLS6 END ");
			sb.append("  AND U.ICD  LIKE CASE WHEN W.ICD ='00' THEN '%' ELSE W.ICD  END ");
			sb.append("  ) ");
			sb.append("  INNER JOIN DS_S_KNO_M K ON (U.YM=K.YM AND U.KNOT=K.KNOT) ");
			sb.append("GROUP BY ");
			sb.append("U.YMD, ");
			sb.append("CASE WHEN K.DTSHU = U.YMD AND U.NFLG=1 THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 1 END ");
			sb.append(") T1 ON T1.YMFG=T0.YMD AND T1.CLSNO=T0.CD ");
			sb.append("LEFT OUTER JOIN DS_M_KNO_Z T2 ON SUBSTR(T0.YMD,1,6)=T2.YM AND T0.CD=T2.CLSNO ");
			sb.append("GROUP BY GROUPING SETS((T0.YMD,T0.CD),(T0.YMD)) ");
			sb.append(") ");

			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 0,'対象者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 1,'購入者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'購入率' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'購入金額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'顧客単価' FROM DUAL ");
			sb.append(") ");

			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");
			for (int i=0;i<Years.size();i++){
				sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 2 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 3 THEN U.KG WHEN 4 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END END END) AS \""+Titles.get(i)+"\" ");
			}
			sb.append(" ");
			sb.append("FROM T_URI U ");
			sb.append("INNER JOIN DS_M_KINGAKU A ON U.CLSNO=A.CD ");
			sb.append(",MVIW V ");
			sb.append("GROUP BY V.KBN,U.CLSNO ");
			sb.append("ORDER BY V.KBN,U.CLSNO ");

		}else if (DefineReport.RadD02_UNIT.ID4.getVal().equals(szUnit)){
			// 累計
			sb.append("WITH ");
			// 基本分野
			sb.append(sbCLASS.toString());

			sb.append(",W_CAL(YMD) AS ( ");
			sb.append("SELECT DISTINCT SUBSTR(YMD_CD,1,4) FROM M_CALENDAR WHERE YMD_CD BETWEEN "+szZYY+"0101 AND "+szYy+"1231 ");
			sb.append(") ");
			sb.append(",W_BASE(YMD,CD) AS ( ");
			sb.append("SELECT YMD,CD FROM W_CAL,DS_M_KINGAKU ");
			sb.append(") ");

			// 基本集計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT ");
			sb.append("    CASE GROUPING_ID(CLSNO) WHEN 1 THEN 9 ELSE CLSNO END AS CLSNO ");
			sb.append("  , SUM(SUT) AS SUT,SUM(KGT) AS KGT,SUM(KNT) AS KNT,SUM(KTT) AS KTT,AVG(SUTA) AS SUTA,AVG(KGTA) AS KGTA,AVG(KNTA) AS KNTA,AVG(KTTA) AS KTTA ");
			sb.append("  , SUM(SUZ) AS SUZ,SUM(KGZ) AS KGZ,SUM(KNZ) AS KNZ,SUM(KTZ) AS KTZ,AVG(SUZA) AS SUZA,AVG(KGZA) AS KGZA,AVG(KNZA) AS KNZA,AVG(KTZA) AS KTZA ");
			sb.append("FROM ( ");
			sb.append("SELECT CLSNO ");
			sb.append(",SUT,KGT,KNT,KTT ");
			sb.append(",SUM(SUT) OVER() AS SUTA,SUM(KGT) OVER() AS KGTA,SUM(KNT) OVER() AS KNTA,SUM(KTT) OVER() AS KTTA ");
			sb.append(",SUZ,KGZ,KNZ,KTZ ");
			sb.append(",SUM(SUZ) OVER() AS SUZA,SUM(KGZ) OVER() AS KGZA,SUM(KNZ) OVER() AS KNZA,SUM(KTZ) OVER() AS KTZA ");
			sb.append("FROM ( ");
			sb.append("SELECT CLSNO ");
			sb.append(",SUM(SUT) AS SUT,SUM(KGT) AS KGT,SUM(KNT) AS KNT,SUM(KTT) AS KTT ");
			sb.append(",SUM(SUZ) AS SUZ,SUM(KGZ) AS KGZ,SUM(KNZ) AS KNZ,SUM(KTZ) AS KTZ ");
			sb.append("FROM ( ");

			sb.append("SELECT ");
			sb.append(" T0.CD AS CLSNO ");
			sb.append(",0 AS SUT ");
			sb.append(",0 AS KGT ");
			sb.append(",0 AS KNT ");
			sb.append(",AVG(CASE WHEN SUBSTR(T2.YM,1,4)="+szYy+" THEN T2.SU END) AS KTT ");
			sb.append(",SUM(T1.SUZ) AS SUZ ");
			sb.append(",SUM(T1.KGZ) AS KGZ ");
			sb.append(",SUM(T1.KNZ) AS KNZ ");
			sb.append(",AVG(CASE WHEN SUBSTR(T2.YM,1,4)="+szZYY+" THEN T2.SU END) AS KTZ ");
			sb.append("FROM W_BASE T0 ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append("SELECT ");
			sb.append("    U.YM AS YMFG ");
			sb.append("  , U.CLSNO AS CLSNO ");
			sb.append("  , SUM(CASE WHEN U.YM ="+szYy+" THEN U.SU END) AS SUT ");
			sb.append("  , SUM(CASE WHEN U.YM ="+szYy+" THEN U.KG END) AS KGT ");
			sb.append("  , COUNT(DISTINCT CASE WHEN U.YM ="+szYy+" THEN U.KNOT END) AS KNT ");
			sb.append("  , SUM(CASE WHEN U.YM ="+szZYY+" THEN U.SU END) AS SUZ ");
			sb.append("  , SUM(CASE WHEN U.YM ="+szZYY+" THEN U.KG END) AS KGZ ");
			sb.append("  , COUNT(DISTINCT CASE WHEN U.YM ="+szZYY+" THEN U.KNOT END) AS KNZ ");
			sb.append("FROM (");
			sb.append("SELECT ");
			sb.append("  SUBSTR(U.YM, 1, 4) AS YM ");
			sb.append(", MIN(CASE WHEN K.DTSHU = U.YMD AND U.NFLG=1 THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 1 END) AS CLSNO ");
			sb.append(", SUM(U.SU) AS SU ");
			sb.append(", SUM(U.KG) AS KG ");
			sb.append(", U.KNOT AS KNOT ");
			sb.append("FROM ");
			sb.append("  DS_S_URI_D U ");
			sb.append("  INNER JOIN W_CLASS W ON ( ");
			sb.append("      U.YM "+Years.get(0)+" ");
			sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
			sb.append("  AND U.KNOT<>0 ");
			sb.append("  AND U.CLS1 LIKE CASE WHEN W.CLS1='00' THEN '%' ELSE W.CLS1 END ");
			sb.append("  AND U.CLS2 LIKE CASE WHEN W.CLS2='00' THEN '%' ELSE W.CLS2 END ");
			sb.append("  AND U.CLS3 LIKE CASE WHEN W.CLS3='00' THEN '%' ELSE W.CLS3 END ");
			sb.append("  AND U.CLS4 LIKE CASE WHEN W.CLS4='00' THEN '%' ELSE W.CLS4 END ");
			sb.append("  AND U.CLS5 LIKE CASE WHEN W.CLS5='00' THEN '%' ELSE W.CLS5 END ");
			sb.append("  AND U.CLS6 LIKE CASE WHEN W.CLS6='00' THEN '%' ELSE W.CLS6 END ");
			sb.append("  AND U.ICD  LIKE CASE WHEN W.ICD ='00' THEN '%' ELSE W.ICD  END ");
			sb.append("  ) ");
			sb.append("  INNER JOIN DS_S_KNO_M K ON (U.YM=K.YM AND U.KNOT=K.KNOT) ");
			sb.append("GROUP BY ");
			sb.append("SUBSTR(U.YM,1,4),U.KNOT ");
			sb.append(") U GROUP BY U.YM,U.CLSNO ");

			sb.append(") T1 ON T1.YMFG=T0.YMD AND T1.CLSNO=T0.CD ");
			sb.append("LEFT OUTER JOIN DS_M_KNO_Z T2 ON T0.YMD=SUBSTR(T2.YM,1,4) AND T0.CD=T2.CLSNO AND T0.YMD="+szZYY+" AND T2.YM = "+szZYY+szNm2);
			sb.append("GROUP BY T0.YMD,T0.CD ");

			sb.append("UNION ALL ");
			sb.append("SELECT ");
			sb.append(" T0.CD AS CLSNO ");
			sb.append(",SUM(T1.SUT) AS SUT ");
			sb.append(",SUM(T1.KGT) AS KGT ");
			sb.append(",SUM(T1.KNT) AS KNT ");
			sb.append(",AVG(CASE WHEN SUBSTR(T2.YM,1,4)="+szYy+" THEN T2.SU END) AS KTT ");
			sb.append(",0 AS SUZ ");
			sb.append(",0 AS KGZ ");
			sb.append(",0 AS KNZ ");
			sb.append(",AVG(CASE WHEN SUBSTR(T2.YM,1,4)="+szZYY+" THEN T2.SU END) AS KTZ ");
			sb.append("FROM W_BASE T0 ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append("SELECT ");
			sb.append("    U.YM AS YMFG ");
			sb.append("  , U.CLSNO AS CLSNO ");
			sb.append("  , SUM(CASE WHEN U.YM ="+szYy+" THEN U.SU END) AS SUT ");
			sb.append("  , SUM(CASE WHEN U.YM ="+szYy+" THEN U.KG END) AS KGT ");
			sb.append("  , COUNT(DISTINCT CASE WHEN U.YM ="+szYy+" THEN U.KNOT END) AS KNT ");
			sb.append("  , SUM(CASE WHEN U.YM ="+szZYY+" THEN U.SU END) AS SUZ ");
			sb.append("  , SUM(CASE WHEN U.YM ="+szZYY+" THEN U.KG END) AS KGZ ");
			sb.append("  , COUNT(DISTINCT CASE WHEN U.YM ="+szZYY+" THEN U.KNOT END) AS KNZ ");
			sb.append("FROM (");
			sb.append("SELECT ");
			sb.append("  SUBSTR(U.YM, 1, 4) AS YM ");
			sb.append(", MIN(CASE WHEN K.DTSHU = U.YMD AND U.NFLG=1 THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 1 END) AS CLSNO ");
			sb.append(", SUM(U.SU) AS SU ");
			sb.append(", SUM(U.KG) AS KG ");
			sb.append(", U.KNOT AS KNOT ");
			sb.append("FROM ");
			sb.append("  DS_S_URI_D U ");
			sb.append("  INNER JOIN W_CLASS W ON ( ");
			sb.append("      U.YM "+Years.get(1)+" ");
			sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
			sb.append("  AND U.KNOT<>0 ");
			sb.append("  AND U.CLS1 LIKE CASE WHEN W.CLS1='00' THEN '%' ELSE W.CLS1 END ");
			sb.append("  AND U.CLS2 LIKE CASE WHEN W.CLS2='00' THEN '%' ELSE W.CLS2 END ");
			sb.append("  AND U.CLS3 LIKE CASE WHEN W.CLS3='00' THEN '%' ELSE W.CLS3 END ");
			sb.append("  AND U.CLS4 LIKE CASE WHEN W.CLS4='00' THEN '%' ELSE W.CLS4 END ");
			sb.append("  AND U.CLS5 LIKE CASE WHEN W.CLS5='00' THEN '%' ELSE W.CLS5 END ");
			sb.append("  AND U.CLS6 LIKE CASE WHEN W.CLS6='00' THEN '%' ELSE W.CLS6 END ");
			sb.append("  AND U.ICD  LIKE CASE WHEN W.ICD ='00' THEN '%' ELSE W.ICD  END ");
			sb.append("  ) ");
			sb.append("  INNER JOIN DS_S_KNO_M K ON (U.YM=K.YM AND U.KNOT=K.KNOT) ");
			sb.append("GROUP BY ");
			sb.append("SUBSTR(U.YM,1,4),U.KNOT ");
			sb.append(") U GROUP BY U.YM,U.CLSNO ");
			sb.append(") T1 ON T1.YMFG=T0.YMD AND T1.CLSNO=T0.CD ");
			sb.append("LEFT OUTER JOIN DS_M_KNO_Z T2 ON T0.YMD=SUBSTR(T2.YM,1,4) AND T0.CD=T2.CLSNO AND T0.YMD="+szYy+" AND T2.YM = "+szYy+szNm2);
			sb.append("GROUP BY T0.YMD,T0.CD ");

			sb.append(")X GROUP BY CLSNO ");
			sb.append(")Y ");
			sb.append(")Z GROUP BY GROUPING SETS((CLSNO),()) ");
			sb.append(") ");

			// 表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 0,'対象者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 1,'購入者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'購入率' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'購入金額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'顧客単価' FROM DUAL ");
			sb.append(") ");
			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");

			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 0 THEN U.KTT ");
			sb.append("    WHEN 1 THEN U.KNT ");
			sb.append("    WHEN 2 THEN CASE WHEN U.KTT = 0 THEN NULL ELSE ROUND(U.KNT / U.KTT * 100, 2) END ");
			sb.append("    WHEN 3 THEN U.KGT ");
			sb.append("    WHEN 4 THEN CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END ");
			sb.append("    END) AS 当期_実績 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 0 THEN CASE WHEN U.KTTA=0 THEN NULL ELSE ROUND(U.KTT/U.KTTA*100,2) END ");
			sb.append("    WHEN 1 THEN CASE WHEN U.KNTA=0 THEN NULL ELSE ROUND(U.KNT/U.KNTA*100,2) END ");
			sb.append("    WHEN 2 THEN NULL ");
			sb.append("    WHEN 3 THEN CASE WHEN U.KGTA=0 THEN NULL ELSE ROUND(U.KGT/U.KGTA*100,2) END ");
			sb.append("    WHEN 4 THEN NULL ");
			sb.append("    END) AS 当期_構成比 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 0 THEN U.KTZ ");
			sb.append("    WHEN 1 THEN U.KNZ ");
			sb.append("    WHEN 2 THEN CASE WHEN U.KTZ = 0 THEN NULL ELSE ROUND(U.KNZ / U.KTZ * 100, 2) END ");
			sb.append("    WHEN 3 THEN U.KGZ ");
			sb.append("    WHEN 4 THEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END ");
			sb.append("    END) AS 前期_実績 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 0 THEN CASE WHEN U.KTZA=0 THEN NULL ELSE ROUND(U.KTZ/U.KTZA*100,2) END ");
			sb.append("    WHEN 1 THEN CASE WHEN U.KNZA=0 THEN NULL ELSE ROUND(U.KNZ/U.KNZA*100,2) END ");
			sb.append("    WHEN 2 THEN NULL ");
			sb.append("    WHEN 3 THEN CASE WHEN U.KGZA=0 THEN NULL ELSE ROUND(U.KGZ/U.KGZA*100,2) END ");
			sb.append("    WHEN 4 THEN NULL ");
			sb.append("    END) AS 前期_構成比 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 0 THEN U.KTT-U.KTZ ");
			sb.append("    WHEN 1 THEN U.KNT-U.KNZ ");
			sb.append("    WHEN 2 THEN CASE WHEN U.KTT = 0 THEN NULL ELSE ROUND(U.KNT / U.KTT * 100, 2) END - CASE WHEN U.KTZ = 0 THEN NULL ELSE ROUND(U.KNZ / U.KTZ * 100, 2) END ");
			sb.append("    WHEN 3 THEN U.KGT-U.KGZ ");
			sb.append("    WHEN 4 THEN CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END - CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END ");
			sb.append("    END) AS 前期差 ");
			sb.append(" ,SUM(CASE V.KBN ");
			sb.append("    WHEN 0 THEN CASE WHEN U.KTZ=0 THEN NULL ELSE ROUND(U.KTT/U.KTZ*100,2) END ");
			sb.append("    WHEN 1 THEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KNT/U.KNZ*100,2) END ");
			sb.append("    WHEN 2 THEN CASE WHEN CASE WHEN U.KTZ = 0 THEN NULL ELSE ROUND(U.KNZ / U.KTZ * 100, 2) END =0 THEN NULL ELSE ROUND(CASE WHEN U.KTT = 0 THEN NULL ELSE ROUND(U.KNT / U.KTT * 100, 2) END/CASE WHEN U.KTZ = 0 THEN NULL ELSE ROUND(U.KNZ / U.KTZ * 100, 2) END*100,2) END ");
			sb.append("    WHEN 3 THEN CASE WHEN U.KGZ=0 THEN NULL ELSE ROUND(U.KGT/U.KGZ*100,2) END ");
			sb.append("    WHEN 4 THEN CASE WHEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END=0 THEN NULL ELSE ROUND(CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END / CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END*100,2) END ");
			sb.append("    END) AS 前期比 ");
			sb.append("FROM T_URI U ");
			sb.append("INNER JOIN DS_M_KINGAKU A ON U.CLSNO=A.CD ");
			sb.append(",MVIW V ");
			sb.append("GROUP BY V.KBN,U.CLSNO ");
			sb.append("ORDER BY V.KBN,U.CLSNO ");
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

		cells = new ArrayList<String>();
		// 購入商品
		cells.add(DefineReport.RadD09_CLS.NAME.getView());

		// 商品分類
		String shobun = jad.getJSONText(DefineReport.Select.SHOS_1.getObj());
		for (int i = 2; i <= 6; i++) {
			String sho = DefineReport.Select.SHOS_NM.getObj() + "_" + i;
			if (!StringUtils.equals(jad.getJSONValue(sho), "00")) {
				shobun += "/" + jad.getJSONText(sho);
			}
		}
		cells.add(shobun);

		getWhere().add(cells);


		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
