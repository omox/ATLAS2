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
public class D20Dao extends ItemDao {

	/**
	 * @param source コネクション
	 */
	public D20Dao(Connection source) {
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
		String szDd			= "01";//getMap().get("YYYY_DD");	// 開始日
		String szYy2		= getMap().get("YYYY_YY2");	// 終了年
		String szNm2		= getMap().get("YYYY_MM2");	// 終了月
		String szDd2		= "01";//getMap().get("YYYY_DD2");	// 終了日

		String szITEM		= getMap().get("ITEM");			// 購入商品

		String szChkS1 = getMap().get("CHK_S1");		// 商品集計-基本分類
		String szChkS2 = getMap().get("CHK_S2");		// 商品集計-大分類
		String szChkS3 = getMap().get("CHK_S3");		// 商品集計-中分類
		String szChkS4 = getMap().get("CHK_S4");		// 商品集計-小分類
		String szChkS5 = getMap().get("CHK_S5");		// 商品集計-シリーズＡ
		String szChkS6 = getMap().get("CHK_S6");		// 商品集計-シリーズＢ
		String szChkS7 = getMap().get("CHK_S7");		// 商品集計-商品

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
		System.out.println("CHK_S1:" +szChkS1+",CHK_S2:"+szChkS2+",CHK_S3:"+szChkS3+",CHK_S4:"+szChkS4+",CHK_S5:"+szChkS5+",CHK_S6:"+szChkS6+",CHK_S7:"+szChkS7);

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
		// 分野別
		if(
			DefineReport.RadD09_CLS.ID1.getVal().equals(szITEM)
		||DefineReport.RadD09_CLS.ID2.getVal().equals(szITEM)
		||DefineReport.RadD09_CLS.ID3.getVal().equals(szITEM)
		){
			// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
			String szCLSNM="2";	// 計画値コード
			String szCLSTP=DefineReport.RadD09_CLS.ID3.getView();	// タイトル
			if(DefineReport.RadD09_CLS.ID1.getVal().equals(szITEM)){
				szCLSNM="0";
				szCLSTP=DefineReport.RadD09_CLS.ID1.getView();
			}else if (DefineReport.RadD09_CLS.ID2.getVal().equals(szITEM)){
				szCLSNM="1";
				szCLSTP=DefineReport.RadD09_CLS.ID2.getView();
			}
			// 集計
			if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
				// 年
				sb.append("WITH ");
				// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
				sb.append("W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
				sb.append("SELECT YMD,CLSNM,TYPNM ");
				sb.append(",CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ ");
				sb.append(",CLS1||CASE WHEN CLS2='00' THEN '%' ELSE CLS2 END||CASE WHEN CLS3='00' THEN '%' ELSE CLS3 END||CASE WHEN CLS4='00' THEN '%' ELSE CLS4 END||CASE WHEN CLS5='00' THEN '%' ELSE CLS5 END||CASE WHEN CLS6='00' THEN '%' ELSE CLS6 END ");
				sb.append("FROM DS_B_CLASS ");
				sb.append("WHERE CLSNM="+szCLSNM);
				sb.append("AND YMD = (SELECT MAX(T1.YMD) AS YMD FROM DS_B_CLASS T1 WHERE T1.YMD<="+szYy2+"1231 AND T1.YMD<TO_CHAR(CURRENT_TIMESTAMP,'YYYYMMDD') AND T1.CLSNM="+szCLSNM+") ");
				sb.append(") ");

				// 基本集計
				sb.append(", T_URI AS ( ");
				sb.append("SELECT ");
				sb.append("    SUBSTR(U.YM,1,4) AS YMFG ");
				sb.append("  , CASE GROUPING_ID(W.SEQ) WHEN 1 THEN '999999999999' ELSE COALESCE(TRIM(MAX(W.SEQ)), '不明') END AS CLSNO ");
				sb.append("  , CASE GROUPING_ID(W.SEQ) WHEN 1 THEN '計' ELSE COALESCE(TRIM(MAX(W.TYPNM)), '不明') END AS CLSNM ");
				sb.append("  , SUM(U.SU) AS SU ");
				sb.append("  , SUM(U.KG) AS KG ");
				sb.append("  , COUNT(DISTINCT U.KNOT) AS KN ");
				sb.append("FROM ");
				sb.append("  DS_S_URI_1ST U ");
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
				sb.append("GROUP BY ");
				sb.append("GROUPING SETS((U.YM,W.SEQ),(U.YM)) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'購入者' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'購入数' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 3,'購入金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 4,'顧客単価' FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLSNM)='計' THEN '計（重複なし）' ELSE MAX(U.CLSNM) END AS "+szCLSTP);
				for (int i=0;i<Years.size();i++){
					sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \""+Years.get(i)+"年\" ");
				}
				sb.append(" ");
				sb.append("FROM T_URI U,MVIW V ");
				sb.append("GROUP BY V.KBN,U.CLSNO ");
				sb.append("ORDER BY V.KBN,CAST(U.CLSNO AS INTEGER) ");

			}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
				// 月
				sb.append("WITH ");
				// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
				sb.append("W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
				sb.append("SELECT YMD,CLSNM,TYPNM ");
				sb.append(",CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ ");
				sb.append(",CLS1||CASE WHEN CLS2='00' THEN '%' ELSE CLS2 END||CASE WHEN CLS3='00' THEN '%' ELSE CLS3 END||CASE WHEN CLS4='00' THEN '%' ELSE CLS4 END||CASE WHEN CLS5='00' THEN '%' ELSE CLS5 END||CASE WHEN CLS6='00' THEN '%' ELSE CLS6 END ");
				sb.append("FROM DS_B_CLASS ");
				sb.append("WHERE CLSNM="+szCLSNM);
				sb.append("AND YMD = (SELECT MAX(T1.YMD) AS YMD FROM DS_B_CLASS T1 WHERE T1.YMD<="+szYy+"1231 AND T1.YMD<TO_CHAR(CURRENT_TIMESTAMP,'YYYYMMDD') AND T1.CLSNM="+szCLSNM+") ");
				sb.append(") ");

				// 基本集計
				sb.append(", T_URI AS ( ");
				sb.append("SELECT ");
				sb.append("    SUBSTR(U.YM,5,2) AS YMFG ");
				sb.append("  , CASE GROUPING_ID(W.SEQ) WHEN 1 THEN '999999999999' ELSE COALESCE(TRIM(MAX(W.SEQ)), '不明') END AS CLSNO ");
				sb.append("  , CASE GROUPING_ID(W.SEQ) WHEN 1 THEN '計' ELSE COALESCE(TRIM(MAX(W.TYPNM)), '不明') END AS CLSNM ");
				sb.append("  , SUM(U.SU) AS SU ");
				sb.append("  , SUM(U.KG) AS KG ");
				sb.append("  , COUNT(DISTINCT U.KNOT) AS KN ");
				sb.append("FROM ");
				sb.append("  DS_S_URI_1ST U ");
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
				sb.append("GROUP BY ");
				sb.append("GROUPING SETS((U.YM,W.SEQ),(U.YM)) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'購入者' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'購入数' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 3,'購入金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 4,'顧客単価' FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLSNM)='計' THEN '計（重複なし）' ELSE MAX(U.CLSNM) END AS "+szCLSTP);
				sb.append(" ,SUM(CASE WHEN YMFG='01' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"1月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='02' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"2月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='03' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"3月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='04' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"4月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='05' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"5月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='06' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"6月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='07' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"7月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='08' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"8月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='09' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"9月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='10' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"10月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"11月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='12' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"12月\" ");
				sb.append(" ");
				sb.append("FROM T_URI U,MVIW V ");
				sb.append("GROUP BY V.KBN,U.CLSNO ");
				sb.append("ORDER BY V.KBN,CAST(U.CLSNO AS INTEGER) ");
			}else if (DefineReport.RadD02_UNIT.ID3.getVal().equals(szUnit)){
				// 日
				sb.append("WITH ");
				// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
				sb.append("W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
				sb.append("SELECT YMD,CLSNM,TYPNM ");
				sb.append(",CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ ");
				sb.append(",CLS1||CASE WHEN CLS2='00' THEN '%' ELSE CLS2 END||CASE WHEN CLS3='00' THEN '%' ELSE CLS3 END||CASE WHEN CLS4='00' THEN '%' ELSE CLS4 END||CASE WHEN CLS5='00' THEN '%' ELSE CLS5 END||CASE WHEN CLS6='00' THEN '%' ELSE CLS6 END ");
				sb.append("FROM DS_B_CLASS ");
				sb.append("WHERE CLSNM="+szCLSNM);
				sb.append("AND YMD = (SELECT MAX(T1.YMD) AS YMD FROM DS_B_CLASS T1 WHERE T1.YMD<="+szYy+szNm+"31 AND T1.YMD<TO_CHAR(CURRENT_TIMESTAMP,'YYYYMMDD') AND T1.CLSNM="+szCLSNM+") ");
				sb.append(") ");

				// 基本集計
				sb.append(", T_URI AS ( ");
				sb.append("SELECT ");
				sb.append("    U.YMD AS YMFG ");
				sb.append("  , CASE GROUPING_ID(W.SEQ) WHEN 1 THEN '999999999999' ELSE COALESCE(TRIM(MAX(W.SEQ)), '不明') END AS CLSNO ");
				sb.append("  , CASE GROUPING_ID(W.SEQ) WHEN 1 THEN '計' ELSE COALESCE(TRIM(MAX(W.TYPNM)), '不明') END AS CLSNM ");
				sb.append("  , SUM(U.SU) AS SU ");
				sb.append("  , SUM(U.KG) AS KG ");
				sb.append("  , COUNT(DISTINCT U.KNOT) AS KN ");
				sb.append("FROM ");
				sb.append("  DS_S_URI_1ST U ");
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
				sb.append("GROUP BY ");
				sb.append("GROUPING SETS((U.YMD,W.SEQ),(U.YMD)) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'購入者' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'購入数' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 3,'購入金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 4,'顧客単価' FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLSNM)='計' THEN '計（重複なし）' ELSE MAX(U.CLSNM) END AS "+szCLSTP);
				for (int i=0;i<Years.size();i++){
					sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \""+Titles.get(i)+"\" ");
				}
				sb.append(" ");
				sb.append("FROM T_URI U,MVIW V ");
				sb.append("GROUP BY V.KBN,U.CLSNO ");
				sb.append("ORDER BY V.KBN,CAST(U.CLSNO AS INTEGER) ");

			}else if (DefineReport.RadD02_UNIT.ID4.getVal().equals(szUnit)){
				// 累計
				sb.append("WITH ");
				// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
				sb.append(" W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
				sb.append("SELECT YMD,CLSNM,TYPNM ");
				sb.append(",CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ ");
				sb.append(",CLS1||CASE WHEN CLS2='00' THEN '%' ELSE CLS2 END||CASE WHEN CLS3='00' THEN '%' ELSE CLS3 END||CASE WHEN CLS4='00' THEN '%' ELSE CLS4 END||CASE WHEN CLS5='00' THEN '%' ELSE CLS5 END||CASE WHEN CLS6='00' THEN '%' ELSE CLS6 END ");
				sb.append("FROM DS_B_CLASS ");
				sb.append("WHERE CLSNM="+szCLSNM);
				sb.append("AND YMD = (SELECT MAX(T1.YMD) AS YMD FROM DS_B_CLASS T1 WHERE T1.YMD<="+szYy+szNm2+"31 AND T1.YMD<TO_CHAR(CURRENT_TIMESTAMP,'YYYYMMDD') AND T1.CLSNM="+szCLSNM+") ");
				sb.append(") ");

				// 基本集計
				sb.append(", T_URI AS (");
				sb.append(" SELECT ");
				sb.append(" CLSNO,CLSNM");
				sb.append(",SUT,KGT,KNT,MAX(SUT) OVER() AS SUTA,MAX(KGT) OVER() AS KGTA,MAX(KNT) OVER() AS KNTA");
				sb.append(",SUZ,KGZ,KNZ,MAX(SUZ) OVER() AS SUZA,MAX(KGZ) OVER() AS KGZA,MAX(KNZ) OVER() AS KNZA");
				sb.append(" FROM (");

				sb.append(" SELECT");
				sb.append("   CASE GROUPING_ID(CLSNO) WHEN 1 THEN '999999999999' ELSE COALESCE(TRIM(MAX(CLSNO)), '不明') END AS CLSNO ");
				sb.append("  ,CASE GROUPING_ID(CLSNO) WHEN 1 THEN '計' ELSE COALESCE(TRIM(MAX(CLSNM)), '不明') END AS CLSNM ");
				sb.append("  ,SUM(SUT) AS SUT,SUM(KGT) AS KGT,COUNT(DISTINCT KNT) AS KNT");
				sb.append("  ,SUM(SUZ) AS SUZ,SUM(KGZ) AS KGZ,COUNT(DISTINCT KNZ) AS KNZ");
				sb.append(" FROM ( ");

				sb.append(" SELECT ");
				sb.append("    W.SEQ AS CLSNO ");
				sb.append("  , W.TYPNM AS CLSNM ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(1)+" THEN U.SU END) AS SUT ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(1)+" THEN U.KG END) AS KGT ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(1)+" THEN U.KNOT END) AS KNT ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(0)+" THEN U.SU END) AS SUZ ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(0)+" THEN U.KG END) AS KGZ ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(0)+" THEN U.KNOT END) AS KNZ ");
				sb.append("FROM ");
				sb.append("  DS_S_URI_1ST U ");
				sb.append("  INNER JOIN W_CLASS W ON ( ");
				sb.append("  (   U.YM "+Years.get(0));
				sb.append("   OR U.YM "+Years.get(1)+")");
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
				sb.append(")X ");
				sb.append(" GROUP BY GROUPING SETS((CLSNO),()) ");
				sb.append(")Y ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'購入者' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'購入数' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 3,'購入金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 4,'顧客単価' FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLSNM)='計' THEN '計（重複なし）' ELSE MAX(U.CLSNM) END AS "+szCLSTP);
				sb.append(" ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN U.KNT ");
				sb.append("    WHEN 2 THEN U.SUT ");
				sb.append("    WHEN 3 THEN U.KGT ");
				sb.append("    WHEN 4 THEN CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END ");
				sb.append("    END) AS 当期_実績 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN CASE WHEN U.KNTA=0 THEN NULL ELSE ROUND(U.KNT/U.KNTA*100,2) END ");
				sb.append("    WHEN 2 THEN CASE WHEN U.SUTA=0 THEN NULL ELSE ROUND(U.SUT/U.SUTA*100,2) END ");
				sb.append("    WHEN 3 THEN CASE WHEN U.KGTA=0 THEN NULL ELSE ROUND(U.KGT/U.KGTA*100,2) END ");
				sb.append("    WHEN 4 THEN NULL ");
				sb.append("    END) AS 当期_構成比 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN U.KNZ ");
				sb.append("    WHEN 2 THEN U.SUZ ");
				sb.append("    WHEN 3 THEN U.KGZ ");
				sb.append("    WHEN 4 THEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END ");
				sb.append("    END) AS 前期_実績 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN CASE WHEN U.KNZA=0 THEN NULL ELSE ROUND(U.KNZ/U.KNZA*100,2) END ");
				sb.append("    WHEN 2 THEN CASE WHEN U.SUZA=0 THEN NULL ELSE ROUND(U.SUZ/U.SUZA*100,2) END ");
				sb.append("    WHEN 3 THEN CASE WHEN U.KGZA=0 THEN NULL ELSE ROUND(U.KGZ/U.KGZA*100,2) END ");
				sb.append("    WHEN 4 THEN NULL ");
				sb.append("    END) AS 前期_構成比 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN U.KNT-U.KNZ ");
				sb.append("    WHEN 2 THEN U.SUT-U.SUZ ");
				sb.append("    WHEN 3 THEN U.KGT-U.KGZ ");
				sb.append("    WHEN 4 THEN CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END - CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END ");
				sb.append("    END) AS 前期差 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KNT/U.KNZ*100,2) END ");
				sb.append("    WHEN 2 THEN CASE WHEN U.SUZ=0 THEN NULL ELSE ROUND(U.SUT/U.SUZ*100,2) END ");
				sb.append("    WHEN 3 THEN CASE WHEN U.KGZ=0 THEN NULL ELSE ROUND(U.KGT/U.KGZ*100,2) END ");
				sb.append("    WHEN 4 THEN CASE WHEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END=0 THEN NULL ELSE ROUND(CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END / CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END*100,2) END ");
				sb.append("    END) AS 前期比 ");
				sb.append("FROM T_URI U,MVIW V ");
				sb.append("GROUP BY V.KBN,U.CLSNO ");
				sb.append("ORDER BY V.KBN,CAST(U.CLSNO AS INTEGER) ");
			}

		}else{
			// 分析用分類
			// 分類指定の適用
			String szClassLike = "";
			if (!"00".equals(szSho1a)){szClassLike+=" AND U.CLS1='"+szSho1a+"'";}
			if (!"00".equals(szSho2a)){szClassLike+=" AND U.CLS2='"+szSho2a.substring(2)+"'";}
			if (!"00".equals(szSho3a)){szClassLike+=" AND U.CLS3='"+szSho3a.substring(4)+"'";}
			if (!"00".equals(szSho4a)){szClassLike+=" AND U.CLS4='"+szSho4a.substring(6)+"'";}
			if (!"00".equals(szSho5a)){szClassLike+=" AND U.CLS5='"+szSho5a.substring(8)+"'";}
			if (!"00".equals(szSho6a)){szClassLike+=" AND U.CLS6='"+szSho6a.substring(10)+"'";}
			System.out.println("szClassLike:"+szClassLike);

			// 集計
			if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
				// 年
				sb.append("WITH ");
				// 基本集計
				sb.append("T_URI AS ( ");
				sb.append("SELECT ");
				sb.append("  YMFG ");
				sb.append("  , CLS1, CASE GROUPING_ID(CLS1) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN1_SHOHIN_MEI),'不明') END AS CLS1NM ");

				if ("on".equals(szChkS2)){
					sb.append("  ,CLS2, CASE GROUPING_ID(CLS1,CLS2) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN2_SHOHIN_MEI),'不明') END AS CLS2NM ");
				}
				if ("on".equals(szChkS3)){
					sb.append("  ,CLS3, CASE GROUPING_ID(CLS1,CLS2,CLS3) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN3_SHOHIN_MEI),'不明') END AS CLS3NM ");
				}
				if ("on".equals(szChkS4)){
					sb.append("  ,CLS4, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN4_SHOHIN_MEI),'不明') END AS CLS4NM ");
				}
				if ("on".equals(szChkS5)){
					sb.append("  ,CLS5, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN5_SHOHIN_MEI),'不明') END AS CLS5NM ");
				}
				if ("on".equals(szChkS6)){
					sb.append("  ,CLS6, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN6_SHOHIN_MEI),'不明') END AS CLS6NM ");
				}
				if ("on".equals(szChkS7)){
					sb.append("  , CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(ICD),'不明') END AS ICD ");
					sb.append("  , CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) WHEN 1 THEN '' WHEN 0 THEN COALESCE(MAX(S.SHOHIN_MEI),'不明') END AS SHOHIN_MEI ");
				}

				sb.append("  , SUM(SU) AS SU,SUM(KG) AS KG,COUNT(DISTINCT KN) AS KN ");
				sb.append(" ");
				sb.append("FROM ( ");
				sb.append("SELECT ");
				sb.append("    SUBSTR(U.YM,1,4) AS YMFG ");
				sb.append("  , U.CLS1 AS CLS1 ");
				sb.append("  , U.CLS2 AS CLS2 ");
				sb.append("  , U.CLS3 AS CLS3 ");
				sb.append("  , U.CLS4 AS CLS4 ");
				sb.append("  , U.CLS5 AS CLS5 ");
				sb.append("  , U.CLS6 AS CLS6 ");
				sb.append("  , U.ICD ");
				sb.append("  , U.SU AS SU ");
				sb.append("  , U.KG AS KG ");
				sb.append("  , U.KNOT AS KN ");
				sb.append("FROM ");
				sb.append("  DS_S_URI_1ST U ");
				sb.append("WHERE ");
				sb.append("      U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
				sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
				sb.append("  AND U.KNOT<>0 ");
				sb.append(szClassLike);
				sb.append(")Z ");
				sb.append("INNER JOIN M_SHOHIN S ON (Z.ICD = S.SHOHIN_CD) ");
				sb.append("GROUP BY GROUPING SETS( ");
				sb.append("(YMFG,CLS1) ");
				if ("on".equals(szChkS2)){
					sb.append(",(YMFG,CLS1,CLS2) ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3) ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4) ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5) ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6) ");
				}
				if ("on".equals(szChkS7)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) ");
				}
				sb.append(",(YMFG),()) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'購入者' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'購入数' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 3,'購入金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 4,'顧客単価' FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS1NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS1NM) END AS 基本分類 ");
				if ("on".equals(szChkS2)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS2NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS2NM) END AS 大分類 ");
				}
				if ("on".equals(szChkS3)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS3NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS3NM) END AS 中分類 ");
				}
				if ("on".equals(szChkS4)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS4NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS4NM) END AS 小分類 ");
				}
				if ("on".equals(szChkS5)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS5NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS5NM) END AS シリーズA ");
				}
				if ("on".equals(szChkS6)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS6NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS6NM) END AS シリーズB ");
				}
				if ("on".equals(szChkS7)){
					sb.append(" ,CASE WHEN V.KBN=1 AND U.ICD='計' THEN '計（重複なし）' ELSE U.ICD END  AS 商品コード ");
					sb.append(" ,MAX(U.SHOHIN_MEI) AS 商品名 ");
				}
				for (int i=0;i<Years.size();i++){
					sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \""+Years.get(i)+"年\" ");
				}
				sb.append("FROM T_URI U,MVIW V ");
				sb.append(" GROUP BY V.KBN,U.CLS1");
				if ("on".equals(szChkS2)){
					sb.append(",U.CLS2");
				}
				if ("on".equals(szChkS3)){
					sb.append(",U.CLS3");
				}
				if ("on".equals(szChkS4)){
					sb.append(",U.CLS4");
				}
				if ("on".equals(szChkS5)){
					sb.append(",U.CLS5");
				}
				if ("on".equals(szChkS6)){
					sb.append(",U.CLS6");
				}
				if ("on".equals(szChkS7)){
					sb.append(",U.ICD ");
				}
				sb.append(" ORDER BY V.KBN,U.CLS1");
				if ("on".equals(szChkS2)){
					sb.append(",U.CLS2");
				}
				if ("on".equals(szChkS3)){
					sb.append(",U.CLS3");
				}
				if ("on".equals(szChkS4)){
					sb.append(",U.CLS4");
				}
				if ("on".equals(szChkS5)){
					sb.append(",U.CLS5");
				}
				if ("on".equals(szChkS6)){
					sb.append(",U.CLS6");
				}
				if ("on".equals(szChkS7)){
					sb.append(",U.ICD ");
				}

			}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
				// 月
				sb.append("WITH ");
				// 基本集計
				sb.append("T_URI AS ( ");
				sb.append("SELECT ");
				sb.append("  YMFG ");
				sb.append("  , CLS1, CASE GROUPING_ID(CLS1) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN1_SHOHIN_MEI),'不明') END AS CLS1NM ");

				if ("on".equals(szChkS2)){
					sb.append("  ,CLS2, CASE GROUPING_ID(CLS1,CLS2) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN2_SHOHIN_MEI),'不明') END AS CLS2NM ");
				}
				if ("on".equals(szChkS3)){
					sb.append("  ,CLS3, CASE GROUPING_ID(CLS1,CLS2,CLS3) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN3_SHOHIN_MEI),'不明') END AS CLS3NM ");
				}
				if ("on".equals(szChkS4)){
					sb.append("  ,CLS4, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN4_SHOHIN_MEI),'不明') END AS CLS4NM ");
				}
				if ("on".equals(szChkS5)){
					sb.append("  ,CLS5, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN5_SHOHIN_MEI),'不明') END AS CLS5NM ");
				}
				if ("on".equals(szChkS6)){
					sb.append("  ,CLS6, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN6_SHOHIN_MEI),'不明') END AS CLS6NM ");
				}
				if ("on".equals(szChkS7)){
					sb.append("  , CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(ICD),'不明') END AS ICD ");
					sb.append("  , CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) WHEN 1 THEN '' WHEN 0 THEN COALESCE(MAX(S.SHOHIN_MEI),'不明') END AS SHOHIN_MEI ");
				}

				sb.append("  , SUM(SU) AS SU,SUM(KG) AS KG,COUNT(DISTINCT KN) AS KN ");
				sb.append(" ");
				sb.append("FROM ( ");
				sb.append("SELECT ");
				sb.append("    SUBSTR(U.YM,5,2) AS YMFG ");
				sb.append("  , U.CLS1 AS CLS1 ");
				sb.append("  , U.CLS2 AS CLS2 ");
				sb.append("  , U.CLS3 AS CLS3 ");
				sb.append("  , U.CLS4 AS CLS4 ");
				sb.append("  , U.CLS5 AS CLS5 ");
				sb.append("  , U.CLS6 AS CLS6 ");
				sb.append("  , U.ICD ");
				sb.append("  , U.SU AS SU ");
				sb.append("  , U.KG AS KG ");
				sb.append("  , U.KNOT AS KN ");
				sb.append("FROM ");
				sb.append("  DS_S_URI_1ST U ");
				sb.append("WHERE ");
				sb.append("      U.YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
				sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
				sb.append("  AND U.KNOT<>0 ");
				sb.append(szClassLike);
				sb.append(")Z ");
				sb.append("INNER JOIN M_SHOHIN S ON (Z.ICD = S.SHOHIN_CD) ");
				sb.append("GROUP BY GROUPING SETS( ");
				sb.append("(YMFG,CLS1) ");
				if ("on".equals(szChkS2)){
					sb.append(",(YMFG,CLS1,CLS2) ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3) ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4) ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5) ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6) ");
				}
				if ("on".equals(szChkS7)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) ");
				}
				sb.append(",(YMFG),()) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'購入者' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'購入数' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 3,'購入金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 4,'顧客単価' FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS1NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS1NM) END AS 基本分類 ");
				if ("on".equals(szChkS2)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS2NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS2NM) END AS 大分類 ");
				}
				if ("on".equals(szChkS3)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS3NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS3NM) END AS 中分類 ");
				}
				if ("on".equals(szChkS4)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS4NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS4NM) END AS 小分類 ");
				}
				if ("on".equals(szChkS5)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS5NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS5NM) END AS シリーズA ");
				}
				if ("on".equals(szChkS6)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS6NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS6NM) END AS シリーズB ");
				}
				if ("on".equals(szChkS7)){
					sb.append(" ,CASE WHEN V.KBN=1 AND U.ICD='計' THEN '計（重複なし）' ELSE U.ICD END  AS 商品コード ");
					sb.append(" ,MAX(U.SHOHIN_MEI) AS 商品名 ");
				}
				sb.append(" ,SUM(CASE WHEN YMFG='01' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"1月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='02' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"2月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='03' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"3月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='04' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"4月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='05' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"5月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='06' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"6月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='07' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"7月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='08' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"8月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='09' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"9月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='10' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"10月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"11月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='12' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \"12月\" ");
				sb.append(" ");
				sb.append("FROM T_URI U,MVIW V ");
				sb.append(" GROUP BY V.KBN,U.CLS1");
				if ("on".equals(szChkS2)){
					sb.append(",U.CLS2");
				}
				if ("on".equals(szChkS3)){
					sb.append(",U.CLS3");
				}
				if ("on".equals(szChkS4)){
					sb.append(",U.CLS4");
				}
				if ("on".equals(szChkS5)){
					sb.append(",U.CLS5");
				}
				if ("on".equals(szChkS6)){
					sb.append(",U.CLS6");
				}
				if ("on".equals(szChkS7)){
					sb.append(",U.ICD ");
				}
				sb.append(" ORDER BY V.KBN,U.CLS1");
				if ("on".equals(szChkS2)){
					sb.append(",U.CLS2");
				}
				if ("on".equals(szChkS3)){
					sb.append(",U.CLS3");
				}
				if ("on".equals(szChkS4)){
					sb.append(",U.CLS4");
				}
				if ("on".equals(szChkS5)){
					sb.append(",U.CLS5");
				}
				if ("on".equals(szChkS6)){
					sb.append(",U.CLS6");
				}
				if ("on".equals(szChkS7)){
					sb.append(",U.ICD ");
				}
			}else if (DefineReport.RadD02_UNIT.ID3.getVal().equals(szUnit)){
				// 日
				sb.append("WITH ");
				// 基本集計
				sb.append("T_URI AS ( ");
				sb.append("SELECT ");
				sb.append("  YMFG ");
				sb.append("  , CLS1, CASE GROUPING_ID(CLS1) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN1_SHOHIN_MEI),'不明') END AS CLS1NM ");

				if ("on".equals(szChkS2)){
					sb.append("  ,CLS2, CASE GROUPING_ID(CLS1,CLS2) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN2_SHOHIN_MEI),'不明') END AS CLS2NM ");
				}
				if ("on".equals(szChkS3)){
					sb.append("  ,CLS3, CASE GROUPING_ID(CLS1,CLS2,CLS3) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN3_SHOHIN_MEI),'不明') END AS CLS3NM ");
				}
				if ("on".equals(szChkS4)){
					sb.append("  ,CLS4, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN4_SHOHIN_MEI),'不明') END AS CLS4NM ");
				}
				if ("on".equals(szChkS5)){
					sb.append("  ,CLS5, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN5_SHOHIN_MEI),'不明') END AS CLS5NM ");
				}
				if ("on".equals(szChkS6)){
					sb.append("  ,CLS6, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN6_SHOHIN_MEI),'不明') END AS CLS6NM ");
				}
				if ("on".equals(szChkS7)){
					sb.append("  , CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(ICD),'不明') END AS ICD ");
					sb.append("  , CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) WHEN 1 THEN '' WHEN 0 THEN COALESCE(MAX(S.SHOHIN_MEI),'不明') END AS SHOHIN_MEI ");
				}

				sb.append("  , SUM(SU) AS SU,SUM(KG) AS KG,COUNT(DISTINCT KN) AS KN ");
				sb.append(" ");
				sb.append("FROM ( ");
				sb.append("SELECT ");
				sb.append("    U.YMD AS YMFG ");
				sb.append("  , U.CLS1 AS CLS1 ");
				sb.append("  , U.CLS2 AS CLS2 ");
				sb.append("  , U.CLS3 AS CLS3 ");
				sb.append("  , U.CLS4 AS CLS4 ");
				sb.append("  , U.CLS5 AS CLS5 ");
				sb.append("  , U.CLS6 AS CLS6 ");
				sb.append("  , U.ICD ");
				sb.append("  , U.SU AS SU ");
				sb.append("  , U.KG AS KG ");
				sb.append("  , U.KNOT AS KN ");
				sb.append("FROM ");
				sb.append("  DS_S_URI_1ST U ");
				sb.append("WHERE ");
				sb.append("      U.YM = "+szYy+szNm+" ");
				sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
				sb.append("  AND U.KNOT<>0 ");
				sb.append(szClassLike);
				sb.append(")Z ");
				sb.append("INNER JOIN M_SHOHIN S ON (Z.ICD = S.SHOHIN_CD) ");
				sb.append("GROUP BY GROUPING SETS( ");
				sb.append("(YMFG,CLS1) ");
				if ("on".equals(szChkS2)){
					sb.append(",(YMFG,CLS1,CLS2) ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3) ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4) ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5) ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6) ");
				}
				if ("on".equals(szChkS7)){
					sb.append(",(YMFG,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) ");
				}
				sb.append(",(YMFG),()) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'購入者' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'購入数' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 3,'購入金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 4,'顧客単価' FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS1NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS1NM) END AS 基本分類 ");
				if ("on".equals(szChkS2)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS2NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS2NM) END AS 大分類 ");
				}
				if ("on".equals(szChkS3)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS3NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS3NM) END AS 中分類 ");
				}
				if ("on".equals(szChkS4)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS4NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS4NM) END AS 小分類 ");
				}
				if ("on".equals(szChkS5)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS5NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS5NM) END AS シリーズA ");
				}
				if ("on".equals(szChkS6)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS6NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS6NM) END AS シリーズB ");
				}
				if ("on".equals(szChkS7)){
					sb.append(" ,CASE WHEN V.KBN=1 AND U.ICD='計' THEN '計（重複なし）' ELSE U.ICD END  AS 商品コード ");
					sb.append(" ,MAX(U.SHOHIN_MEI) AS 商品名 ");
				}
				for (int i=0;i<Years.size();i++){
					sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.KN WHEN 2 THEN U.SU WHEN 3 THEN U.KG WHEN 4 THEN ROUND(U.KG/U.KN,0) END END) AS \""+Titles.get(i)+"\" ");
				}
				sb.append(" ");
				sb.append("FROM T_URI U,MVIW V ");
				sb.append(" GROUP BY V.KBN,U.CLS1");
				if ("on".equals(szChkS2)){
					sb.append(",U.CLS2");
				}
				if ("on".equals(szChkS3)){
					sb.append(",U.CLS3");
				}
				if ("on".equals(szChkS4)){
					sb.append(",U.CLS4");
				}
				if ("on".equals(szChkS5)){
					sb.append(",U.CLS5");
				}
				if ("on".equals(szChkS6)){
					sb.append(",U.CLS6");
				}
				if ("on".equals(szChkS7)){
					sb.append(",U.ICD ");
				}
				sb.append(" ORDER BY V.KBN,U.CLS1");
				if ("on".equals(szChkS2)){
					sb.append(",U.CLS2");
				}
				if ("on".equals(szChkS3)){
					sb.append(",U.CLS3");
				}
				if ("on".equals(szChkS4)){
					sb.append(",U.CLS4");
				}
				if ("on".equals(szChkS5)){
					sb.append(",U.CLS5");
				}
				if ("on".equals(szChkS6)){
					sb.append(",U.CLS6");
				}
				if ("on".equals(szChkS7)){
					sb.append(",U.ICD ");
				}

			}else if (DefineReport.RadD02_UNIT.ID4.getVal().equals(szUnit)){
				// 累計
				sb.append("WITH ");
				// 基本集計
				sb.append("T_URI AS ( ");
				sb.append("SELECT ");

				sb.append(" CLS1,CLS1NM");
				if ("on".equals(szChkS2)){sb.append(",CLS2,CLS2NM");}
				if ("on".equals(szChkS3)){sb.append(",CLS3,CLS3NM");}
				if ("on".equals(szChkS4)){sb.append(",CLS4,CLS4NM");}
				if ("on".equals(szChkS5)){sb.append(",CLS5,CLS5NM");}
				if ("on".equals(szChkS6)){sb.append(",CLS6,CLS6NM");}
				if ("on".equals(szChkS7)){sb.append(",ICD,SHOHIN_MEI");}

				sb.append(",SUT,KGT,KNT,MAX(SUT) OVER() AS SUTA,MAX(KGT) OVER() AS KGTA,MAX(KNT) OVER() AS KNTA");
				sb.append(",SUZ,KGZ,KNZ,MAX(SUZ) OVER() AS SUZA,MAX(KGZ) OVER() AS KGZA,MAX(KNZ) OVER() AS KNZA");
				sb.append(" FROM (");

				sb.append(" SELECT");
				sb.append("  CLS1, CASE GROUPING_ID(CLS1) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN1_SHOHIN_MEI),'不明') END AS CLS1NM ");
				if ("on".equals(szChkS2)){
					sb.append("  ,CLS2, CASE GROUPING_ID(CLS1,CLS2) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN2_SHOHIN_MEI),'不明') END AS CLS2NM ");
				}
				if ("on".equals(szChkS3)){
					sb.append("  ,CLS3, CASE GROUPING_ID(CLS1,CLS2,CLS3) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN3_SHOHIN_MEI),'不明') END AS CLS3NM ");
				}
				if ("on".equals(szChkS4)){
					sb.append("  ,CLS4, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN4_SHOHIN_MEI),'不明') END AS CLS4NM ");
				}
				if ("on".equals(szChkS5)){
					sb.append("  ,CLS5, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN5_SHOHIN_MEI),'不明') END AS CLS5NM ");
				}
				if ("on".equals(szChkS6)){
					sb.append("  ,CLS6, CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(S.BUN6_SHOHIN_MEI),'不明') END AS CLS6NM ");
				}
				if ("on".equals(szChkS7)){
					sb.append("  ,CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) WHEN 1 THEN '計' WHEN 0 THEN COALESCE(MAX(ICD),'不明') END AS ICD ");
					sb.append("  ,CASE GROUPING_ID(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) WHEN 1 THEN '' WHEN 0 THEN COALESCE(MAX(S.SHOHIN_MEI),'不明') END AS SHOHIN_MEI ");
				}
				sb.append("  ,SUM(SUT) AS SUT,SUM(KGT) AS KGT,COUNT(DISTINCT KNT) AS KNT");
				sb.append("  ,SUM(SUZ) AS SUZ,SUM(KGZ) AS KGZ,COUNT(DISTINCT KNZ) AS KNZ");
				sb.append(" FROM ( ");

				sb.append(" SELECT ");
				sb.append("    U.CLS1 AS CLS1 ");
				sb.append("  , U.CLS2 AS CLS2 ");
				sb.append("  , U.CLS3 AS CLS3 ");
				sb.append("  , U.CLS4 AS CLS4 ");
				sb.append("  , U.CLS5 AS CLS5 ");
				sb.append("  , U.CLS6 AS CLS6 ");
				sb.append("  , U.ICD ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(1)+" THEN U.SU END) AS SUT ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(1)+" THEN U.KG END) AS KGT ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(1)+" THEN U.KNOT END) AS KNT ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(0)+" THEN U.SU END) AS SUZ ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(0)+" THEN U.KG END) AS KGZ ");
				sb.append("  , (CASE WHEN U.YM "+Years.get(0)+" THEN U.KNOT END) AS KNZ ");
				sb.append("FROM ");
				sb.append("  DS_S_URI_1ST U ");
				sb.append("WHERE ");
				sb.append("  (   U.YM "+Years.get(0));
				sb.append("   OR U.YM "+Years.get(1)+")");
				sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
				sb.append("  AND U.KNOT<>0 ");
				sb.append(szClassLike);
				sb.append(")X ");
				sb.append(" INNER JOIN M_SHOHIN S ON (X.ICD = S.SHOHIN_CD) ");
				sb.append(" GROUP BY GROUPING SETS( ");
				sb.append("(CLS1) ");
				if ("on".equals(szChkS2)){sb.append(",(CLS1,CLS2) ");}
				if ("on".equals(szChkS3)){sb.append(",(CLS1,CLS2,CLS3) ");}
				if ("on".equals(szChkS4)){sb.append(",(CLS1,CLS2,CLS3,CLS4) ");}
				if ("on".equals(szChkS5)){sb.append(",(CLS1,CLS2,CLS3,CLS4,CLS5) ");}
				if ("on".equals(szChkS6)){sb.append(",(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6) ");}
				if ("on".equals(szChkS7)){sb.append(",(CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD) ");}
				sb.append(",()) ");
				sb.append(")Y ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'購入者' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'購入数' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 3,'購入金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 4,'顧客単価' FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS1NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS1NM) END AS 基本分類 ");
				if ("on".equals(szChkS2)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS2NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS2NM) END AS 大分類 ");
				}
				if ("on".equals(szChkS3)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS3NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS3NM) END AS 中分類 ");
				}
				if ("on".equals(szChkS4)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS4NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS4NM) END AS 小分類 ");
				}
				if ("on".equals(szChkS5)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS5NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS5NM) END AS シリーズA ");
				}
				if ("on".equals(szChkS6)){
					sb.append(" ,CASE WHEN V.KBN=1 AND MAX(U.CLS6NM)='計' THEN '計（重複なし）' ELSE MAX(U.CLS6NM) END AS シリーズB ");
				}
				if ("on".equals(szChkS7)){
					sb.append(" ,CASE WHEN V.KBN=1 AND U.ICD='計' THEN '計（重複なし）' ELSE U.ICD END  AS 商品コード ");
					sb.append(" ,MAX(U.SHOHIN_MEI) AS 商品名 ");
				}

				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN U.KNT ");
				sb.append("    WHEN 2 THEN U.SUT ");
				sb.append("    WHEN 3 THEN U.KGT ");
				sb.append("    WHEN 4 THEN CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END ");
				sb.append("    END) AS 当期_実績 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN CASE WHEN U.KNTA=0 THEN NULL ELSE ROUND(U.KNT/U.KNTA*100,2) END ");
				sb.append("    WHEN 2 THEN CASE WHEN U.SUTA=0 THEN NULL ELSE ROUND(U.SUT/U.SUTA*100,2) END ");
				sb.append("    WHEN 3 THEN CASE WHEN U.KGTA=0 THEN NULL ELSE ROUND(U.KGT/U.KGTA*100,2) END ");
				sb.append("    WHEN 4 THEN NULL ");
				sb.append("    END) AS 当期_構成比 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN U.KNZ ");
				sb.append("    WHEN 2 THEN U.SUZ ");
				sb.append("    WHEN 3 THEN U.KGZ ");
				sb.append("    WHEN 4 THEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END ");
				sb.append("    END) AS 前期_実績 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN CASE WHEN U.KNZA=0 THEN NULL ELSE ROUND(U.KNZ/U.KNZA*100,2) END ");
				sb.append("    WHEN 2 THEN CASE WHEN U.SUZA=0 THEN NULL ELSE ROUND(U.SUZ/U.SUZA*100,2) END ");
				sb.append("    WHEN 3 THEN CASE WHEN U.KGZA=0 THEN NULL ELSE ROUND(U.KGZ/U.KGZA*100,2) END ");
				sb.append("    WHEN 4 THEN NULL ");
				sb.append("    END) AS 前期_構成比 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN U.KNT-U.KNZ ");
				sb.append("    WHEN 2 THEN U.SUT-U.SUZ ");
				sb.append("    WHEN 3 THEN U.KGT-U.KGZ ");
				sb.append("    WHEN 4 THEN CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END - CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END ");
				sb.append("    END) AS 前期差 ");
				sb.append(" ,SUM(CASE V.KBN ");
				sb.append("    WHEN 1 THEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KNT/U.KNZ*100,2) END ");
				sb.append("    WHEN 2 THEN CASE WHEN U.SUZ=0 THEN NULL ELSE ROUND(U.SUT/U.SUZ*100,2) END ");
				sb.append("    WHEN 3 THEN CASE WHEN U.KGZ=0 THEN NULL ELSE ROUND(U.KGT/U.KGZ*100,2) END ");
				sb.append("    WHEN 4 THEN CASE WHEN CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END=0 THEN NULL ELSE ROUND(CASE WHEN U.KNT=0 THEN NULL ELSE ROUND(U.KGT/U.KNT,0) END / CASE WHEN U.KNZ=0 THEN NULL ELSE ROUND(U.KGZ/U.KNZ,0) END*100,2) END ");
				sb.append("    END) AS 前期比 ");
				sb.append("FROM T_URI U,MVIW V ");
				sb.append(" GROUP BY V.KBN,U.CLS1");
				if ("on".equals(szChkS2)){
					sb.append(",U.CLS2");
				}
				if ("on".equals(szChkS3)){
					sb.append(",U.CLS3");
				}
				if ("on".equals(szChkS4)){
					sb.append(",U.CLS4");
				}
				if ("on".equals(szChkS5)){
					sb.append(",U.CLS5");
				}
				if ("on".equals(szChkS6)){
					sb.append(",U.CLS6");
				}
				if ("on".equals(szChkS7)){
					sb.append(",U.ICD ");
				}
				sb.append(" ORDER BY V.KBN,U.CLS1");
				if ("on".equals(szChkS2)){
					sb.append(",U.CLS2");
				}
				if ("on".equals(szChkS3)){
					sb.append(",U.CLS3");
				}
				if ("on".equals(szChkS4)){
					sb.append(",U.CLS4");
				}
				if ("on".equals(szChkS5)){
					sb.append(",U.CLS5");
				}
				if ("on".equals(szChkS6)){
					sb.append(",U.CLS6");
				}
				if ("on".equals(szChkS7)){
					sb.append(",U.ICD ");
				}
			}
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
		cells.add(jad.getJSONText(DefineReport.RadD09_CLS.NAME.getObj()));

		if (StringUtils.equals(jad.getJSONValue(DefineReport.RadD09_CLS.NAME.getObj()), DefineReport.RadD09_CLS.ID4.getVal())) {
			// 商品分類
			String shobun = jad.getJSONText(DefineReport.Select.SHOS_1.getObj());
			for (int i = 2; i <= 6; i++) {
				String sho = DefineReport.Select.SHOS_NM.getObj() + "_" + i;
				if (!StringUtils.equals(jad.getJSONValue(sho), "00")) {
					shobun += "/" + jad.getJSONText(sho);
				}
			}
			cells.add(shobun);
		}

		getWhere().add(cells);


		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
