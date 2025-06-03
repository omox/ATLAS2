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
public class D12Dao extends ItemDao {

	/**
	 * @param source コネクション
	 */
	public D12Dao(Connection source) {
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
		String szYy2		= getMap().get("YYYY_YY2");	// 終了年
		String szNm2		= getMap().get("YYYY_MM2");	// 終了月

		String szITEM		= getMap().get("ITEM");			// 購入商品

		String szChkS1 = getMap().get("CHK_S1");		// 商品集計-基本分類
		String szChkS2 = getMap().get("CHK_S2");		// 商品集計-大分類
		String szChkS3 = getMap().get("CHK_S3");		// 商品集計-中分類
		String szChkS4 = getMap().get("CHK_S4");		// 商品集計-小分類
		String szChkS5 = getMap().get("CHK_S5");		// 商品集計-シリーズＡ
		String szChkS6 = getMap().get("CHK_S6");		// 商品集計-シリーズＢ

		String szSho1a = getMap().get("SEL_SHO1");	// 商品分類-基本分類
		String szSho2a = getMap().get("SEL_SHO2");	// 商品分類-大分類
		String szSho3a = getMap().get("SEL_SHO3");	// 商品分類-中分類
		String szSho4a = getMap().get("SEL_SHO4");	// 商品分類-小分類
		String szSho5a = getMap().get("SEL_SHO5");	// 商品分類-シリーズＡ
		String szSho6a = getMap().get("SEL_SHO6");	// 商品分類-シリーズＢ

		System.out.println("CHANEL:"+szChanel+",UNIT:"+szUnit);
		System.out.println("YYYY_MM:"+szYy+szNm+",YYYY_MM2:"+szYy2+szNm2);
		System.out.println("ITEM:"+szITEM);
		System.out.println("SEL_SHO:"+szSho1a+szSho2a+szSho3a+szSho4a+szSho5a+szSho6a);
		System.out.println("CHK_S1:" +szChkS1+",CHK_S2:"+szChkS2+",CHK_S3:"+szChkS3+",CHK_S4:"+szChkS4+",CHK_S5:"+szChkS5+",CHK_S6:"+szChkS6);

		// 集計
		ArrayList<String> Years = new ArrayList<String>();
		String szZYY = szYy;	// 前年

		if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
			for(int i=Integer.parseInt(szYy);i<=Integer.parseInt(szYy2);i++){
				Years.add(String.valueOf(i));
			}

		}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
			// 月

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
					Years.add(" = '"+String.valueOf(szZYY)+"01'");
				}else{
					Years.add(" BETWEEN '"+String.valueOf(szZYY)+"01' AND '"+String.valueOf(szZYY)+szNm2+"'");
				}

				// 当年
				if ("01".equals(szNm2)){
					Years.add(" = '"+String.valueOf(szYy)+"01'");
				}else{
					Years.add(" BETWEEN '"+String.valueOf(szYy)+"01' AND '"+String.valueOf(szYy)+szNm2+"'");
				}

			} catch(ParseException e) {
				e.printStackTrace();
			}
			System.out.println("szZYY:"+szZYY);
		}

		// チャネル判定
		String szChanelCode = "'10','20'";
		if (DefineReport.RadD01_CHANEL.ID1.getVal().equals(szChanel)){
			// 通店計
			szChanelCode = "'10','20'";
		} else if (DefineReport.RadD01_CHANEL.ID2.getVal().equals(szChanel)) {
			// 通販
			szChanelCode = "'10'";
		} else if (DefineReport.RadD01_CHANEL.ID3.getVal().equals(szChanel)) {
			// 店舗
			szChanelCode = "'20'";
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
				// チャネル
				sb.append("WCHANEL(CHANCLS1, CHANCLS2, CHANCLS3, CHANCLS4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER) AS ( ");
				sb.append("SELECT CHANEL_CD1, CHANEL_CD2, CHANEL_CD3, CHANEL_CD4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER ");
				sb.append("FROM M_CHANEL2 C ");
				sb.append("WHERE C.KBN = 'A' ");
				sb.append("AND C.CHANEL_CD1 IN ("+szChanelCode+") ");
				sb.append(") ");
				// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
				sb.append(",W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
				sb.append("SELECT YMD,CLSNM,TYPNM ");
				sb.append(",CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ ");
				sb.append(",CLS1||CASE WHEN CLS2='00' THEN '%' ELSE CLS2 END||CASE WHEN CLS3='00' THEN '%' ELSE CLS3 END||CASE WHEN CLS4='00' THEN '%' ELSE CLS4 END||CASE WHEN CLS5='00' THEN '%' ELSE CLS5 END||CASE WHEN CLS6='00' THEN '%' ELSE CLS6 END ");
				sb.append("FROM DS_B_CLASS ");
				sb.append("WHERE CLSNM="+szCLSNM);
				sb.append("AND YMD = (SELECT MAX(T1.YMD) AS YMD FROM DS_B_CLASS T1 WHERE T1.YMD<="+szYy2+"1231 AND T1.CLSNM="+szCLSNM+") ");
				sb.append(") ");
				// 基本集計
				sb.append(", T_URI(YMFG ");
				sb.append(", BUN1_SHOHIN_CD, BUN2_SHOHIN_CD, BUN3_SHOHIN_CD, BUN4_SHOHIN_CD, BUN5_SHOHIN_CD, BUN6_SHOHIN_CD, BUN1_SHOHIN_MEI, BUN2_SHOHIN_MEI, BUN3_SHOHIN_MEI, BUN4_SHOHIN_MEI, BUN5_SHOHIN_MEI, BUN6_SHOHIN_MEI ");
				sb.append(", SEQ, TYPNM ");
				sb.append(", SHOHIN_CD, SHOHIN_MEI, SU, GA, UTZ, UTS) AS ( ");
				sb.append("SELECT ");
				sb.append("    SUBSTR(URI_YM,1,4) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_MEI) ");
				sb.append("  , MAX(W.SEQ) ");
				sb.append("  , MAX(W.TYPNM) ");
				sb.append("  , U.SHOHIN_CD ");
				sb.append("  , MAX(SHOHIN_MEI) ");
				sb.append("  , SUM(U.SU) ");
				sb.append("  , SUM(U.GA) ");
				sb.append("  , SUM(U.SU)*AVG(G.UTZ) ");
				sb.append("  , SUM(U.SU)*AVG(G.UTS) ");
				sb.append("FROM ");
				sb.append("  URI_SHOHIN_MONTH U ");
				sb.append("  INNER JOIN M_SHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD AND S.BUN1_SHOHIN_CD<>'90') ");
				sb.append("  INNER JOIN WCHANEL C  ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 OR (U.CHANCLS3 = C.CHANCLS2 AND U.CHANCLS4 = C.CHANCLS3))) ");
				sb.append("  INNER JOIN DS_M_GENKA G ON G.YM=U.URI_YM AND G.CHANEL=U.CHANCLS2 AND G.ICD=U.SHOHIN_CD ");
				sb.append("  INNER JOIN W_CLASS W ON ( ");
				sb.append("      S.BUN6_SHOHIN_CD LIKE W.CLS6S ");
				sb.append("  AND S.SHOHIN_CD LIKE CASE WHEN W.ICD ='00' THEN '%' ELSE W.ICD  END ");
				sb.append(") ");
				sb.append("WHERE ");
				sb.append("  URI_YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
				sb.append("  AND U.GA>0");
				sb.append("GROUP BY SUBSTR(URI_YM,1,4),U.SHOHIN_CD ");
				sb.append(") ");

				// 基本
				sb.append(",T_KIHON (CLSNO,CLSNM,YMFG,COL1,COL2) AS ( ");
				sb.append("SELECT ");
				sb.append("    CASE GROUPING_ID(U.SEQ) WHEN 1 THEN '999999999999' ELSE COALESCE(TRIM(MAX(SEQ)), '不明') END AS CLSNO ");
				sb.append("  , CASE GROUPING_ID(U.SEQ) WHEN 1 THEN '計' ELSE COALESCE(TRIM(MAX(TYPNM)), '不明') END AS CLSNM ");
				sb.append("  , U.YMFG ");
				sb.append("  , SUM(U.GA) AS COL1 ");	// 出荷金額
				sb.append("  , ROUND(CASE WHEN NVL(SUM(U.UTZ), 0) = 0 THEN NULL ELSE NVL(SUM(U.UTS), 0) / NVL(SUM(U.UTZ), 0) END * 100, 2) AS COL2 ");	// 原価率
				sb.append("FROM T_URI U ");
				sb.append("GROUP BY ");
				sb.append("  GROUPING SETS((U.SEQ,U.YMFG),(U.YMFG)) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'出荷金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'原価率'   FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,MAX(U.CLSNM) AS "+szCLSTP);
				for (int i=0;i<Years.size();i++){
					sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \""+Years.get(i)+"年\" ");
				}
				sb.append(" ");
				sb.append("FROM T_KIHON U,MVIW V ");
				sb.append("GROUP BY V.KBN,U.CLSNO ");
				sb.append("ORDER BY V.KBN,U.CLSNO ");

			}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
				// 月
				sb.append("WITH ");
				// チャネル
				sb.append("WCHANEL(CHANCLS1, CHANCLS2, CHANCLS3, CHANCLS4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER) AS ( ");
				sb.append("SELECT CHANEL_CD1, CHANEL_CD2, CHANEL_CD3, CHANEL_CD4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER ");
				sb.append("FROM M_CHANEL2 C ");
				sb.append("WHERE C.KBN = 'A' ");
				sb.append("AND C.CHANEL_CD1 IN ("+szChanelCode+") ");
				sb.append(") ");
				// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
				sb.append(",W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
				sb.append("SELECT YMD,CLSNM,TYPNM ");
				sb.append(",CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ ");
				sb.append(",CLS1||CASE WHEN CLS2='00' THEN '%' ELSE CLS2 END||CASE WHEN CLS3='00' THEN '%' ELSE CLS3 END||CASE WHEN CLS4='00' THEN '%' ELSE CLS4 END||CASE WHEN CLS5='00' THEN '%' ELSE CLS5 END||CASE WHEN CLS6='00' THEN '%' ELSE CLS6 END ");
				sb.append("FROM DS_B_CLASS ");
				sb.append("WHERE CLSNM="+szCLSNM);
				sb.append("AND YMD = (SELECT MAX(T1.YMD) AS YMD FROM DS_B_CLASS T1 WHERE T1.YMD<="+szYy+"1231 AND T1.CLSNM="+szCLSNM+") ");
				sb.append(") ");

				// 基本集計
				sb.append(", T_URI(YMFG ");
				sb.append(", BUN1_SHOHIN_CD, BUN2_SHOHIN_CD, BUN3_SHOHIN_CD, BUN4_SHOHIN_CD, BUN5_SHOHIN_CD, BUN6_SHOHIN_CD, BUN1_SHOHIN_MEI, BUN2_SHOHIN_MEI, BUN3_SHOHIN_MEI, BUN4_SHOHIN_MEI, BUN5_SHOHIN_MEI, BUN6_SHOHIN_MEI ");
				sb.append(", SEQ, TYPNM ");
				sb.append(", SHOHIN_CD, SHOHIN_MEI, SU, GA, UTZ, UTS) AS ( ");
				sb.append("SELECT ");
				sb.append("    SUBSTR(URI_YM,5,2) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_MEI) ");
				sb.append("  , MAX(W.SEQ) ");
				sb.append("  , MAX(W.TYPNM) ");
				sb.append("  , U.SHOHIN_CD ");
				sb.append("  , MAX(SHOHIN_MEI) ");
				sb.append("  , SUM(U.SU) ");
				sb.append("  , SUM(U.GA) ");
				sb.append("  , SUM(U.SU)*AVG(G.UTZ) ");
				sb.append("  , SUM(U.SU)*AVG(G.UTS) ");
				sb.append("FROM ");
				sb.append("  URI_SHOHIN_MONTH U ");
				sb.append("  INNER JOIN M_SHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD AND S.BUN1_SHOHIN_CD<>'90') ");
				sb.append("  INNER JOIN WCHANEL C  ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 OR (U.CHANCLS3 = C.CHANCLS2 AND U.CHANCLS4 = C.CHANCLS3))) ");
				sb.append("  INNER JOIN DS_M_GENKA G ON G.YM=U.URI_YM AND G.CHANEL=U.CHANCLS2 AND G.ICD=U.SHOHIN_CD ");
				sb.append("  INNER JOIN W_CLASS W ON ( ");
				sb.append("      S.BUN6_SHOHIN_CD LIKE W.CLS6S ");
				sb.append("  AND S.SHOHIN_CD LIKE CASE WHEN W.ICD ='00' THEN '%' ELSE W.ICD  END ");
				sb.append(") ");
				sb.append("WHERE ");
				sb.append("  URI_YM BETWEEN '"+szYy+"01' AND '"+szYy+"12' ");
				sb.append("  AND U.GA>0");
				sb.append("GROUP BY SUBSTR(URI_YM,5,2),U.SHOHIN_CD ");
				sb.append(") ");

				// 基本
				sb.append(",T_KIHON (CLSNO,CLSNM,YMFG,COL1,COL2) AS ( ");
				sb.append("SELECT ");
				sb.append("    CASE GROUPING_ID(U.SEQ) WHEN 1 THEN '999999999999' ELSE COALESCE(TRIM(MAX(SEQ)), '不明') END AS CLSNO ");
				sb.append("  , CASE GROUPING_ID(U.SEQ) WHEN 1 THEN '計' ELSE COALESCE(TRIM(MAX(TYPNM)), '不明') END AS CLSNM ");
				sb.append("  , U.YMFG ");
				sb.append("  , SUM(U.GA) AS COL1 ");	// 出荷金額
				sb.append("  , ROUND(CASE WHEN NVL(SUM(U.UTZ), 0) = 0 THEN NULL ELSE NVL(SUM(U.UTS), 0) / NVL(SUM(U.UTZ), 0) END * 100, 2) AS COL2 ");	// 原価率
				sb.append("FROM T_URI U ");
				sb.append("GROUP BY ");
				sb.append("  GROUPING SETS((U.SEQ,U.YMFG),(U.YMFG)) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'出荷金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'原価率'   FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,MAX(U.CLSNM) AS "+szCLSTP);
				sb.append(" ,SUM(CASE WHEN YMFG='01' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"1月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='02' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"2月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='03' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"3月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='04' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"4月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='05' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"5月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='06' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"6月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='07' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"7月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='08' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"8月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='09' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"9月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='10' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"10月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"11月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"12月\" ");
				sb.append(" ");
				sb.append("FROM T_KIHON U,MVIW V ");
				sb.append("GROUP BY V.KBN,U.CLSNO ");
				sb.append("ORDER BY V.KBN,U.CLSNO ");

			}else if (DefineReport.RadD02_UNIT.ID4.getVal().equals(szUnit)){
				// 累計
				sb.append("WITH ");
				// チャネル
				sb.append("WCHANEL(CHANCLS1, CHANCLS2, CHANCLS3, CHANCLS4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER) AS ( ");
				sb.append("SELECT CHANEL_CD1, CHANEL_CD2, CHANEL_CD3, CHANEL_CD4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER ");
				sb.append("FROM M_CHANEL2 C ");
				sb.append("WHERE C.KBN = 'A' ");
				sb.append("AND C.CHANEL_CD1 IN ("+szChanelCode+") ");
				sb.append(") ");
				// 基本分野（0:分野別,1:カテゴリ,2:重点商材）
				sb.append(",W_CLASS(YMD_,CLSNM,TYPNM,CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ,CLS6S) AS ( ");
				sb.append("SELECT YMD,CLSNM,TYPNM ");
				sb.append(",CLS1,CLS2,CLS3,CLS4,CLS5,CLS6,ICD,SEQ ");
				sb.append(",CLS1||CASE WHEN CLS2='00' THEN '%' ELSE CLS2 END||CASE WHEN CLS3='00' THEN '%' ELSE CLS3 END||CASE WHEN CLS4='00' THEN '%' ELSE CLS4 END||CASE WHEN CLS5='00' THEN '%' ELSE CLS5 END||CASE WHEN CLS6='00' THEN '%' ELSE CLS6 END ");
				sb.append("FROM DS_B_CLASS ");
				sb.append("WHERE CLSNM="+szCLSNM);
				sb.append("AND YMD = (SELECT MAX(T1.YMD) AS YMD FROM DS_B_CLASS T1 WHERE T1.YMD<="+szYy2+szNm2+"31 AND T1.CLSNM="+szCLSNM+") ");
				sb.append(") ");

				// 当年基本集計
				sb.append(", T_URI( ");
				sb.append("  URI_YM, BUN1_SHOHIN_CD, BUN2_SHOHIN_CD, BUN3_SHOHIN_CD, BUN4_SHOHIN_CD, BUN5_SHOHIN_CD, BUN6_SHOHIN_CD, BUN1_SHOHIN_MEI, BUN2_SHOHIN_MEI, BUN3_SHOHIN_MEI, BUN4_SHOHIN_MEI, BUN5_SHOHIN_MEI, BUN6_SHOHIN_MEI ");
				sb.append(", SEQ, TYPNM ");
				sb.append(", SHOHIN_CD, SHOHIN_MEI ");
				sb.append(", GA_T,GA_Z,UTZ_T,UTZ_Z,UTS_T,UTS_Z,GAT_T,GAT_Z) AS ( ");
				sb.append("SELECT X.*, SUM(X.GA_T) OVER () AS GAT_T,SUM(X.GA_Z) OVER () AS GAT_Z FROM (");
				sb.append("SELECT URI_YM ");
				sb.append("  , MAX(S.BUN1_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_MEI) ");
				sb.append("  , MAX(W.SEQ) ");
				sb.append("  , MAX(W.TYPNM) ");
				sb.append("  , U.SHOHIN_CD ");
				sb.append("  , MAX(SHOHIN_MEI) ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(1)+" THEN U.GA END) AS GA_T ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(0)+" THEN U.GA END) AS GA_Z ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(1)+" THEN U.SU END) * AVG(CASE WHEN URI_YM "+Years.get(1)+" THEN G.UTZ END) ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(0)+" THEN U.SU END) * AVG(CASE WHEN URI_YM "+Years.get(0)+" THEN G.UTZ END) ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(1)+" THEN U.SU END) * AVG(CASE WHEN URI_YM "+Years.get(1)+" THEN G.UTS END) ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(0)+" THEN U.SU END) * AVG(CASE WHEN URI_YM "+Years.get(0)+" THEN G.UTS END) ");
				sb.append("FROM ");
				sb.append("  URI_SHOHIN_MONTH U ");
				sb.append("  INNER JOIN M_SHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD AND S.BUN1_SHOHIN_CD<>'90') ");
				sb.append("  INNER JOIN WCHANEL C  ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 OR (U.CHANCLS3 = C.CHANCLS2 AND U.CHANCLS4 = C.CHANCLS3))) ");
				sb.append("  INNER JOIN DS_M_GENKA G ON G.YM=U.URI_YM AND G.CHANEL=U.CHANCLS2 AND G.ICD=U.SHOHIN_CD ");
				sb.append("  INNER JOIN W_CLASS W ON ( ");
				sb.append("      S.BUN6_SHOHIN_CD LIKE W.CLS6S ");
				sb.append("  AND S.SHOHIN_CD LIKE CASE WHEN W.ICD ='00' THEN '%' ELSE W.ICD  END ");
				sb.append(") ");
				sb.append("WHERE ");
				sb.append("  (URI_YM "+Years.get(0)+" OR URI_YM "+Years.get(1)+")");
				sb.append("  AND U.GA>0 ");
				sb.append("GROUP BY U.URI_YM, U.SHOHIN_CD ");
				sb.append(")X ");
				sb.append(") ");

				// 基本
				sb.append(",T_KIHON (CLSNO,CLSNM,COL1T,COL1Z,COL2T,COL2Z,COL1TS,COL1ZS) AS ( ");
				sb.append("SELECT ");
				sb.append("    CASE GROUPING_ID(U.SEQ) WHEN 1 THEN '999999999999' ELSE COALESCE(TRIM(MAX(SEQ)), '不明') END AS CLSNO ");
				sb.append("  , CASE GROUPING_ID(U.SEQ) WHEN 1 THEN '計' ELSE COALESCE(TRIM(MAX(TYPNM)), '不明') END AS CLSNM ");
				sb.append("  , SUM(U.GA_T) AS COL1T ");
				sb.append("  , SUM(U.GA_Z) AS COL1Z ");
				sb.append("  , ROUND(CASE WHEN NVL(SUM(U.UTZ_T), 0) = 0 THEN NULL ELSE NVL(SUM(U.UTS_T), 0) / NVL(SUM(U.UTZ_T), 0) END * 100, 2) AS COL2T ");
				sb.append("  , ROUND(CASE WHEN NVL(SUM(U.UTZ_Z), 0) = 0 THEN NULL ELSE NVL(SUM(U.UTS_Z), 0) / NVL(SUM(U.UTZ_Z), 0) END * 100, 2) AS COL2Z ");
				sb.append("  , AVG(U.GAT_T) AS COL1TS ");
				sb.append("  , AVG(U.GAT_Z) AS COL1ZS ");
				sb.append("FROM T_URI U ");
				sb.append("GROUP BY ");
				sb.append("  GROUPING SETS((U.SEQ),()) ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'出荷金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'原価率'   FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,MAX(U.CLSNM) AS "+szCLSTP);
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN U.COL1T WHEN 2 THEN U.COL2T END) AS 当期_実績 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN CASE WHEN U.COL1TS=0 THEN NULL ELSE ROUND(U.COL1T/U.COL1TS*100,2) END WHEN 2 THEN NULL END) AS 当期_構成比 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN U.COL1Z WHEN 2 THEN U.COL2Z END) AS 前期_実績 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN CASE WHEN U.COL1ZS=0 THEN NULL ELSE ROUND(U.COL1Z/U.COL1ZS*100,2) END WHEN 2 THEN NULL END) AS 前期_構成比 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN U.COL1T-U.COL1Z WHEN 2 THEN U.COL2T-U.COL2Z END) AS 前期差 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN CASE WHEN U.COL1Z=0 THEN NULL ELSE ROUND(U.COL1T/U.COL1Z*100,2) END WHEN 2 THEN CASE WHEN U.COL2Z=0 THEN NULL ELSE ROUND(U.COL2T/U.COL2Z*100,2) END END) AS 前期比 ");
				sb.append("FROM T_KIHON U,MVIW V ");
				sb.append("GROUP BY V.KBN,U.CLSNO ");
				sb.append("ORDER BY V.KBN,U.CLSNO ");

			}

		}else{	// 分析用分類

			// 分類指定の適用
			String szClassLike = "";
			if (!"00".equals(szSho6a)){szClassLike+=szSho6a;}
			else if (!"00".equals(szSho5a)){szClassLike+=szSho5a;}
			else if (!"00".equals(szSho4a)){szClassLike+=szSho4a;}
			else if (!"00".equals(szSho3a)){szClassLike+=szSho3a;}
			else if (!"00".equals(szSho2a)){szClassLike+=szSho2a;}
			else if (!"00".equals(szSho1a)){szClassLike+=szSho1a;}
			szClassLike+="%";
			System.out.println("szClassLike:"+szClassLike);

			// 集計
			if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
				// 年
				sb.append("WITH ");
				// チャネル
				sb.append("WCHANEL(CHANCLS1, CHANCLS2, CHANCLS3, CHANCLS4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER) AS ( ");
				sb.append("SELECT CHANEL_CD1, CHANEL_CD2, CHANEL_CD3, CHANEL_CD4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER ");
				sb.append("FROM M_CHANEL2 C ");
				sb.append("WHERE C.KBN = 'A' ");
				sb.append("AND C.CHANEL_CD1 IN ("+szChanelCode+") ");
				sb.append(") ");
				// 基本集計
				sb.append(", T_URI(YMFG ");
				sb.append(", BUN1_SHOHIN_CD, BUN2_SHOHIN_CD, BUN3_SHOHIN_CD, BUN4_SHOHIN_CD, BUN5_SHOHIN_CD, BUN6_SHOHIN_CD, BUN1_SHOHIN_MEI, BUN2_SHOHIN_MEI, BUN3_SHOHIN_MEI, BUN4_SHOHIN_MEI, BUN5_SHOHIN_MEI, BUN6_SHOHIN_MEI ");
				sb.append(", SEQ, TYPNM ");
				sb.append(", SHOHIN_CD, SHOHIN_MEI, SU, GA, UTZ, UTS) AS ( ");
				sb.append("SELECT ");
				sb.append("    SUBSTR(URI_YM,1,4) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_MEI) ");
				sb.append("  , 0 ");
				sb.append("  , '' ");
				sb.append("  , U.SHOHIN_CD ");
				sb.append("  , MAX(SHOHIN_MEI) ");
				sb.append("  , SUM(U.SU) ");
				sb.append("  , SUM(U.GA) ");
				sb.append("  , SUM(U.SU)*AVG(G.UTZ) ");
				sb.append("  , SUM(U.SU)*AVG(G.UTS) ");
				sb.append("FROM ");
				sb.append("  URI_SHOHIN_MONTH U ");
				sb.append("  INNER JOIN M_SHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD AND S.BUN1_SHOHIN_CD<>'90') ");
				sb.append("  INNER JOIN WCHANEL C  ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 OR (U.CHANCLS3 = C.CHANCLS2 AND U.CHANCLS4 = C.CHANCLS3))) ");
				sb.append("  INNER JOIN DS_M_GENKA G ON G.YM=U.URI_YM AND G.CHANEL=U.CHANCLS2 AND G.ICD=U.SHOHIN_CD ");
				sb.append("WHERE ");
				sb.append("  URI_YM BETWEEN '"+szYy+"01' AND '"+szYy2+"12' ");
				sb.append("  AND S.BUN6_SHOHIN_CD LIKE '"+szClassLike+"'");
				sb.append("  AND U.GA>0");
				sb.append("GROUP BY SUBSTR(URI_YM,1,4),U.SHOHIN_CD ");
				sb.append(") ");

				// 基本
				sb.append(",T_KIHON AS ( ");
				sb.append("SELECT ");

				sb.append("    BUN1_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD) WHEN 1 THEN '合計' WHEN 0 THEN COALESCE(TRIM(MAX(BUN1_SHOHIN_MEI)), '不明')END AS BUN1_SHOHIN_MEI ");
				if ("on".equals(szChkS2)){
					sb.append("  , BUN2_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN2_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN2_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS3)){
					sb.append("  , BUN3_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN3_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN3_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS4)){
					sb.append("  , BUN4_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN4_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN4_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS5)){
					sb.append("  , BUN5_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN5_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN5_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS6)){
					sb.append("  , BUN6_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN6_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN6_SHOHIN_MEI ");
				}
				sb.append("  , U.YMFG ");
				sb.append("  , SUM(U.GA) AS COL1 ");	// 出荷金額
				sb.append("  , ROUND(CASE WHEN NVL(SUM(U.UTZ), 0) = 0 THEN NULL ELSE NVL(SUM(U.UTS), 0) / NVL(SUM(U.UTZ), 0) END * 100, 2) AS COL2 ");	// 原価率
				sb.append("FROM T_URI U ");
				sb.append("GROUP BY ");
				sb.append("  GROUPING SETS( ");
				sb.append(" (BUN1_SHOHIN_CD,U.YMFG) ");
				if ("on".equals(szChkS2)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,U.YMFG) ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,U.YMFG) ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,U.YMFG) ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,U.YMFG) ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD,U.YMFG) ");
				}
				sb.append(",(U.YMFG) ");
				sb.append(") ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'出荷金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'原価率'   FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,MAX(BUN1_SHOHIN_MEI) AS 基本分類 ");
				if ("on".equals(szChkS2)){
					sb.append(" ,MAX(BUN2_SHOHIN_MEI) AS 大分類 ");
				}
				if ("on".equals(szChkS3)){
					sb.append(" ,MAX(BUN3_SHOHIN_MEI) AS 中分類 ");
				}
				if ("on".equals(szChkS4)){
					sb.append(" ,MAX(BUN4_SHOHIN_MEI) AS 小分類 ");
				}
				if ("on".equals(szChkS5)){
					sb.append(" ,MAX(BUN5_SHOHIN_MEI) AS シリーズA ");
				}
				if ("on".equals(szChkS6)){
					sb.append(" ,MAX(BUN6_SHOHIN_MEI) AS シリーズB ");
				}
				for (int i=0;i<Years.size();i++){
					sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \""+Years.get(i)+"年\" ");
				}
				sb.append(" ");
				sb.append("FROM T_KIHON U,MVIW V ");
				sb.append("GROUP BY V.KBN,BUN1_SHOHIN_CD ");
				if ("on".equals(szChkS2)){
					sb.append(",BUN2_SHOHIN_CD");
				}
				if ("on".equals(szChkS3)){
					sb.append(",BUN3_SHOHIN_CD ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",BUN4_SHOHIN_CD ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",BUN5_SHOHIN_CD ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",BUN6_SHOHIN_CD ");
				}
				sb.append(" ORDER BY V.KBN,BUN1_SHOHIN_CD ");
				if ("on".equals(szChkS2)){
					sb.append(",BUN2_SHOHIN_CD ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",BUN3_SHOHIN_CD ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",BUN4_SHOHIN_CD ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",BUN5_SHOHIN_CD ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",BUN6_SHOHIN_CD ");
				}

			}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
				// 月
				sb.append("WITH ");
				// チャネル
				sb.append("WCHANEL(CHANCLS1, CHANCLS2, CHANCLS3, CHANCLS4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER) AS ( ");
				sb.append("SELECT CHANEL_CD1, CHANEL_CD2, CHANEL_CD3, CHANEL_CD4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER ");
				sb.append("FROM M_CHANEL2 C ");
				sb.append("WHERE C.KBN = 'A' ");
				sb.append("AND C.CHANEL_CD1 IN ("+szChanelCode+") ");
				sb.append(") ");

				// 基本集計
				sb.append(", T_URI(YMFG ");
				sb.append(", BUN1_SHOHIN_CD, BUN2_SHOHIN_CD, BUN3_SHOHIN_CD, BUN4_SHOHIN_CD, BUN5_SHOHIN_CD, BUN6_SHOHIN_CD, BUN1_SHOHIN_MEI, BUN2_SHOHIN_MEI, BUN3_SHOHIN_MEI, BUN4_SHOHIN_MEI, BUN5_SHOHIN_MEI, BUN6_SHOHIN_MEI ");
				sb.append(", SEQ, TYPNM ");
				sb.append(", SHOHIN_CD, SHOHIN_MEI, SU, GA, UTZ, UTS) AS ( ");
				sb.append("SELECT ");
				sb.append("    SUBSTR(URI_YM,5,2) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_MEI) ");
				sb.append("  , 0 ");
				sb.append("  , '' ");
				sb.append("  , U.SHOHIN_CD ");
				sb.append("  , MAX(SHOHIN_MEI) ");
				sb.append("  , SUM(U.SU) ");
				sb.append("  , SUM(U.GA) ");
				sb.append("  , SUM(U.SU)*AVG(G.UTZ) ");
				sb.append("  , SUM(U.SU)*AVG(G.UTS) ");
				sb.append("FROM ");
				sb.append("  URI_SHOHIN_MONTH U ");
				sb.append("  INNER JOIN M_SHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD AND S.BUN1_SHOHIN_CD<>'90') ");
				sb.append("  INNER JOIN WCHANEL C  ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 OR (U.CHANCLS3 = C.CHANCLS2 AND U.CHANCLS4 = C.CHANCLS3))) ");
				sb.append("  INNER JOIN DS_M_GENKA G ON G.YM=U.URI_YM AND G.CHANEL=U.CHANCLS2 AND G.ICD=U.SHOHIN_CD ");
				sb.append("WHERE ");
				sb.append("  URI_YM BETWEEN '"+szYy+"01' AND '"+szYy+"12' ");
				sb.append("  AND S.BUN6_SHOHIN_CD LIKE '"+szClassLike+"'");
				sb.append("  AND U.GA>0");
				sb.append("GROUP BY SUBSTR(URI_YM,5,2),U.SHOHIN_CD ");
				sb.append(") ");

				// 基本
				sb.append(",T_KIHON AS ( ");
				sb.append("SELECT ");
				sb.append("    BUN1_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD) WHEN 1 THEN '合計' WHEN 0 THEN COALESCE(TRIM(MAX(BUN1_SHOHIN_MEI)), '不明')END AS BUN1_SHOHIN_MEI ");
				if ("on".equals(szChkS2)){
					sb.append("  , BUN2_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN2_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN2_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS3)){
					sb.append("  , BUN3_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN3_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN3_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS4)){
					sb.append("  , BUN4_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN4_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN4_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS5)){
					sb.append("  , BUN5_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN5_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN5_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS6)){
					sb.append("  , BUN6_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN6_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN6_SHOHIN_MEI ");
				}
				sb.append("  , U.YMFG ");
				sb.append("  , SUM(U.GA) AS COL1 ");	// 出荷金額
				sb.append("  , ROUND(CASE WHEN NVL(SUM(U.UTZ), 0) = 0 THEN NULL ELSE NVL(SUM(U.UTS), 0) / NVL(SUM(U.UTZ), 0) END * 100, 2) AS COL2 ");	// 原価率
				sb.append("FROM T_URI U ");
				sb.append("GROUP BY ");
				sb.append("  GROUPING SETS( ");
				sb.append(" (BUN1_SHOHIN_CD,U.YMFG) ");
				if ("on".equals(szChkS2)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,U.YMFG) ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,U.YMFG) ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,U.YMFG) ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,U.YMFG) ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD,U.YMFG) ");
				}
				sb.append(",(U.YMFG) ");
				sb.append(") ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'出荷金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'原価率'   FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,MAX(BUN1_SHOHIN_MEI) AS 基本分類 ");
				if ("on".equals(szChkS2)){
					sb.append(" ,MAX(BUN2_SHOHIN_MEI) AS 大分類 ");
				}
				if ("on".equals(szChkS3)){
					sb.append(" ,MAX(BUN3_SHOHIN_MEI) AS 中分類 ");
				}
				if ("on".equals(szChkS4)){
					sb.append(" ,MAX(BUN4_SHOHIN_MEI) AS 小分類 ");
				}
				if ("on".equals(szChkS5)){
					sb.append(" ,MAX(BUN5_SHOHIN_MEI) AS シリーズA ");
				}
				if ("on".equals(szChkS6)){
					sb.append(" ,MAX(BUN6_SHOHIN_MEI) AS シリーズB ");
				}
				sb.append(" ,SUM(CASE WHEN YMFG='01' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"1月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='02' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"2月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='03' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"3月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='04' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"4月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='05' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"5月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='06' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"6月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='07' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"7月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='08' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"8月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='09' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"9月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='10' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"10月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"11月\" ");
				sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 1 THEN U.COL1 WHEN 2 THEN U.COL2 END END) AS \"12月\" ");
				sb.append(" ");
				sb.append("FROM T_KIHON U,MVIW V ");
				sb.append("GROUP BY V.KBN,BUN1_SHOHIN_CD ");
				if ("on".equals(szChkS2)){
					sb.append(",BUN2_SHOHIN_CD");
				}
				if ("on".equals(szChkS3)){
					sb.append(",BUN3_SHOHIN_CD ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",BUN4_SHOHIN_CD ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",BUN5_SHOHIN_CD ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",BUN6_SHOHIN_CD ");
				}
				sb.append(" ORDER BY V.KBN,BUN1_SHOHIN_CD ");
				if ("on".equals(szChkS2)){
					sb.append(",BUN2_SHOHIN_CD ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",BUN3_SHOHIN_CD ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",BUN4_SHOHIN_CD ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",BUN5_SHOHIN_CD ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",BUN6_SHOHIN_CD ");
				}

			}else if (DefineReport.RadD02_UNIT.ID4.getVal().equals(szUnit)){
				// 累計
				sb.append("WITH ");
				// チャネル
				sb.append("WCHANEL(CHANCLS1, CHANCLS2, CHANCLS3, CHANCLS4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER) AS ( ");
				sb.append("SELECT CHANEL_CD1, CHANEL_CD2, CHANEL_CD3, CHANEL_CD4, MEISYO1, MEISYO2, MEISYO3, MEISYO4, CHAN_ORDER ");
				sb.append("FROM M_CHANEL2 C ");
				sb.append("WHERE C.KBN = 'A' ");
				sb.append("AND C.CHANEL_CD1 IN ("+szChanelCode+") ");
				sb.append(") ");

				// 当年基本集計
				sb.append(", T_URI( ");
				sb.append("  URI_YM, BUN1_SHOHIN_CD, BUN2_SHOHIN_CD, BUN3_SHOHIN_CD, BUN4_SHOHIN_CD, BUN5_SHOHIN_CD, BUN6_SHOHIN_CD, BUN1_SHOHIN_MEI, BUN2_SHOHIN_MEI, BUN3_SHOHIN_MEI, BUN4_SHOHIN_MEI, BUN5_SHOHIN_MEI, BUN6_SHOHIN_MEI ");
				sb.append(", SEQ, TYPNM ");
				sb.append(", SHOHIN_CD, SHOHIN_MEI ");
				sb.append(", GA_T,GA_Z,UTZ_T,UTZ_Z,UTS_T,UTS_Z,GAT_T,GAT_Z) AS ( ");
				sb.append("SELECT X.*, SUM(X.GA_T) OVER () AS GAT_T,SUM(X.GA_Z) OVER () AS GAT_Z FROM (");
				sb.append("SELECT URI_YM ");
				sb.append("  , MAX(S.BUN1_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_CD) ");
				sb.append("  , MAX(S.BUN1_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN2_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN3_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN4_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN5_SHOHIN_MEI) ");
				sb.append("  , MAX(S.BUN6_SHOHIN_MEI) ");
				sb.append("  , 0 ");
				sb.append("  , '' ");
				sb.append("  , U.SHOHIN_CD ");
				sb.append("  , MAX(SHOHIN_MEI) ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(1)+" THEN U.GA END) AS GA_T ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(0)+" THEN U.GA END) AS GA_Z ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(1)+" THEN U.SU END) * AVG(CASE WHEN URI_YM "+Years.get(1)+" THEN G.UTZ END) ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(0)+" THEN U.SU END) * AVG(CASE WHEN URI_YM "+Years.get(0)+" THEN G.UTZ END) ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(1)+" THEN U.SU END) * AVG(CASE WHEN URI_YM "+Years.get(1)+" THEN G.UTS END) ");
				sb.append("  , SUM(CASE WHEN URI_YM "+Years.get(0)+" THEN U.SU END) * AVG(CASE WHEN URI_YM "+Years.get(0)+" THEN G.UTS END) ");
				sb.append("FROM ");
				sb.append("  URI_SHOHIN_MONTH U ");
				sb.append("  INNER JOIN M_SHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD AND S.BUN1_SHOHIN_CD<>'90') ");
				sb.append("  INNER JOIN WCHANEL C  ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 OR (U.CHANCLS3 = C.CHANCLS2 AND U.CHANCLS4 = C.CHANCLS3))) ");
				sb.append("  INNER JOIN DS_M_GENKA G ON G.YM=U.URI_YM AND G.CHANEL=U.CHANCLS2 AND G.ICD=U.SHOHIN_CD ");
				sb.append("WHERE ");
				sb.append("  (URI_YM "+Years.get(0)+" OR URI_YM "+Years.get(1)+") ");
				sb.append("  AND S.BUN6_SHOHIN_CD LIKE '"+szClassLike+"'");
				sb.append("  AND U.GA>0");
				sb.append("GROUP BY U.URI_YM, U.SHOHIN_CD ");
				sb.append(")X ");
				sb.append(") ");

				// 基本
				sb.append(",T_KIHON AS ( ");
				sb.append("SELECT ");
				sb.append("    BUN1_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD) WHEN 1 THEN '合計' WHEN 0 THEN COALESCE(TRIM(MAX(BUN1_SHOHIN_MEI)), '不明')END AS BUN1_SHOHIN_MEI ");
				if ("on".equals(szChkS2)){
					sb.append("  , BUN2_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN2_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN2_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS3)){
					sb.append("  , BUN3_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN3_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN3_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS4)){
					sb.append("  , BUN4_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN4_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN4_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS5)){
					sb.append("  , BUN5_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN5_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN5_SHOHIN_MEI ");
				}
				if ("on".equals(szChkS6)){
					sb.append("  , BUN6_SHOHIN_CD,CASE GROUPING_ID(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD) WHEN 0 THEN COALESCE(TRIM(MAX(BUN6_SHOHIN_MEI)), '不明')ELSE CASE WHEN GROUPING_ID(BUN1_SHOHIN_CD)<>1 THEN '計' END END AS BUN6_SHOHIN_MEI ");
				}
				sb.append("  , SUM(U.GA_T) AS COL1T ");
				sb.append("  , SUM(U.GA_Z) AS COL1Z ");
				sb.append("  , ROUND(CASE WHEN NVL(SUM(U.UTZ_T), 0) = 0 THEN NULL ELSE NVL(SUM(U.UTS_T), 0) / NVL(SUM(U.UTZ_T), 0) END * 100, 2) AS COL2T ");
				sb.append("  , ROUND(CASE WHEN NVL(SUM(U.UTZ_Z), 0) = 0 THEN NULL ELSE NVL(SUM(U.UTS_Z), 0) / NVL(SUM(U.UTZ_Z), 0) END * 100, 2) AS COL2Z ");

				sb.append("  , AVG(U.GAT_T) AS COL1TS ");
				sb.append("  , AVG(U.GAT_Z) AS COL1ZS ");

				sb.append("FROM T_URI U ");
				sb.append("GROUP BY ");
				sb.append("  GROUPING SETS( ");
				sb.append(" (BUN1_SHOHIN_CD) ");
				if ("on".equals(szChkS2)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD) ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD) ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD) ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD) ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",(BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD) ");
				}
				sb.append(",() ");
				sb.append(") ");
				sb.append(") ");

				// 表示項目
				sb.append(", MVIW(KBN,KNM) AS ( ");
				sb.append("SELECT 1,'出荷金額' FROM DUAL ");
				sb.append("UNION ALL ");
				sb.append("SELECT 2,'原価率'   FROM DUAL ");
				sb.append(") ");

				sb.append("SELECT MAX(V.KNM) AS 項目 ");
				sb.append(" ,MAX(BUN1_SHOHIN_MEI) AS 基本分類 ");
				if ("on".equals(szChkS2)){
					sb.append(" ,MAX(BUN2_SHOHIN_MEI) AS 大分類 ");
				}
				if ("on".equals(szChkS3)){
					sb.append(" ,MAX(BUN3_SHOHIN_MEI) AS 中分類 ");
				}
				if ("on".equals(szChkS4)){
					sb.append(" ,MAX(BUN4_SHOHIN_MEI) AS 小分類 ");
				}
				if ("on".equals(szChkS5)){
					sb.append(" ,MAX(BUN5_SHOHIN_MEI) AS シリーズA ");
				}
				if ("on".equals(szChkS6)){
					sb.append(" ,MAX(BUN6_SHOHIN_MEI) AS シリーズB ");
				}
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN U.COL1T WHEN 2 THEN U.COL2T END) AS 当期_実績 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN CASE WHEN U.COL1TS=0 THEN NULL ELSE ROUND(U.COL1T/U.COL1TS*100,2) END WHEN 2 THEN NULL END) AS 当期_構成比 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN U.COL1Z WHEN 2 THEN U.COL2Z END) AS 前期_実績 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN CASE WHEN U.COL1ZS=0 THEN NULL ELSE ROUND(U.COL1Z/U.COL1ZS*100,2) END WHEN 2 THEN NULL END) AS 前期_構成比 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN U.COL1T-U.COL1Z WHEN 2 THEN U.COL2T-U.COL2Z END) AS 前期差 ");
				sb.append(" ,SUM(CASE V.KBN WHEN 1 THEN CASE WHEN U.COL1Z=0 THEN NULL ELSE ROUND(U.COL1T/U.COL1Z*100,2) END WHEN 2 THEN CASE WHEN U.COL2Z=0 THEN NULL ELSE ROUND(U.COL2T/U.COL2Z*100,2) END END) AS 前期比 ");
				sb.append("FROM T_KIHON U,MVIW V ");
				sb.append("GROUP BY V.KBN,BUN1_SHOHIN_CD ");
				if ("on".equals(szChkS2)){
					sb.append(",BUN2_SHOHIN_CD");
				}
				if ("on".equals(szChkS3)){
					sb.append(",BUN3_SHOHIN_CD ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",BUN4_SHOHIN_CD ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",BUN5_SHOHIN_CD ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",BUN6_SHOHIN_CD ");
				}
				sb.append(" ORDER BY V.KBN,BUN1_SHOHIN_CD ");
				if ("on".equals(szChkS2)){
					sb.append(",BUN2_SHOHIN_CD ");
				}
				if ("on".equals(szChkS3)){
					sb.append(",BUN3_SHOHIN_CD ");
				}
				if ("on".equals(szChkS4)){
					sb.append(",BUN4_SHOHIN_CD ");
				}
				if ("on".equals(szChkS5)){
					sb.append(",BUN5_SHOHIN_CD ");
				}
				if ("on".equals(szChkS6)){
					sb.append(",BUN6_SHOHIN_CD ");
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
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN_TO.getObj()));

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
