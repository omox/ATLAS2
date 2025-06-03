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
public class D30Dao extends ItemDao {

	/**
	 * 新しいB04Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public D30Dao(Connection source) {
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

		String szUnit	= getMap().get("UNIT");		// 集計単位
		String szYy		= getMap().get("YYYY_YY");	// 開始年
		String szNm		= getMap().get("YYYY_MM");	// 開始月

		String szTenpo	= getMap().get("TENPO");	// 店舗
		String szTenpos	= getMap().get("TENPOS");	// 店舗詳細

		String szSearch	= getMap().get("SEARCH");	// 照会
		String szJun	= getMap().get("ITEM_JUN");	// 純売上
		String szShu	= getMap().get("ITEM_SHU");	// 出荷売上
		String szCho	= getMap().get("ITEM_CHO");	// 調整前売上


		System.out.println("YYYY_MM:"+szYy+szNm);
		System.out.println("TENPO:"+szTenpo+",TENPOS:"+szTenpos);
		System.out.println("SEARCH:"+szSearch);
		System.out.println("ITEM_JUN:"+szJun);
		System.out.println("ITEM_SHU:"+szShu);
		System.out.println("ITEM_CHO:"+szCho);

		ArrayList<String> Years = new ArrayList<String>();
		ArrayList<String> Titles = new ArrayList<String>();
		if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
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
		}
		// 前年
		String szZYY = String.valueOf(Integer.parseInt(szYy)-1);

		// 店舗
		String sqlTenpo = "";
		String sqlTENCD = "";
		if ("2".contentEquals(szTenpo) && !"00".equals(szTenpos)){
			sqlTenpo = " AND TENPO_CD IN ("+szTenpos+") ";
			sqlTENCD = " AND TENCD IN ("+szTenpos+") ";
		}
		// 照合
		StringBuffer sbMVIW = new StringBuffer();
		String szKbn = "";
		String[] Col = null;
		String szFirstNo = "";
		String szColName = "";
		if (DefineReport.RadD12_SEARCH.ID1.getVal().equals(szSearch)){
			szKbn="純売上";
			Col = szJun.split(",");
			szColName = "JUN";
		}else if (DefineReport.RadD12_SEARCH.ID2.getVal().equals(szSearch)){
			szKbn="出荷売上";
			Col = szShu.split(",");
			Col[0]="0";
			Col[3]="0";
			szColName = "SHU";
		}else if (DefineReport.RadD12_SEARCH.ID3.getVal().equals(szSearch)){
			szKbn="調整前売上";
			Col = szCho.split(",");
			Col[0]="0";
			Col[3]="0";
			szColName = "CHO";
		}
		if ("1".equals(Col[0])){
			if (sbMVIW.length()==0){szFirstNo="1";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 1,'"+szKbn+"',1,'計画' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[1])){
			System.out.println(sbMVIW.length()!=0);
			if (sbMVIW.length()==0){szFirstNo="2";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 1,'"+szKbn+"',2,'当年実績' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[2])){
			if (sbMVIW.length()==0){szFirstNo="3";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 1,'"+szKbn+"',3,'前年実績' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[3])){
			if (sbMVIW.length()==0){szFirstNo="4";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 1,'"+szKbn+"',4,'達成率' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[4])){
			if (sbMVIW.length()==0){szFirstNo="5";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 1,'"+szKbn+"',5,'前年比' FROM DUAL ");
			szKbn = "";
		}
		szKbn = "レジ件数";
		if ("1".equals(Col[5])){
			if (sbMVIW.length()==0){szFirstNo="6";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 2,'"+szKbn+"',6,'オルビス新規' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[6])){
			if (sbMVIW.length()==0){szFirstNo="7";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 2,'"+szKbn+"',7,'リピート' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[7])){
			if (sbMVIW.length()==0){szFirstNo="8";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 2,'"+szKbn+"',8,'カードなし' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[8])){
			if (sbMVIW.length()==0){szFirstNo="9";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 2,'"+szKbn+"',9,'未登録' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[9])){
			if (sbMVIW.length()==0){szFirstNo="10";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 2,'"+szKbn+"',10,'計' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[10])){
			if (sbMVIW.length()==0){szFirstNo="11";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 2,'"+szKbn+"',11,'前年実績' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[11])){
			if (sbMVIW.length()==0){szFirstNo="12";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 2,'"+szKbn+"',12,'前年比' FROM DUAL ");
			szKbn = "";
		}
		if ("1".equals(Col[12])){
			if (sbMVIW.length()==0){szFirstNo="13";}else{sbMVIW.append(" UNION ALL ");}
			sbMVIW.append("SELECT 2,'"+szKbn+"',13,'レジ単価' FROM DUAL ");
			szKbn = "";
		}

		StringBuffer sb = new StringBuffer();
		if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
			// 月
			sb.append("WITH W00(URI_YMD, TENPO_CD, KYAKUSU, ZYY_KYAKUSU, URIGK, ZYY_URIGK, MOK_JUN_GK, K11,K12,K31,K41) AS ( ");
			sb.append("SELECT ");
			sb.append("    CASE WHEN GROUPING_ID(URI_YMD)=1 THEN '合計' ELSE URI_YMD END ");
			sb.append("  , CASE WHEN GROUPING_ID(TENPO_CD)=1 THEN '00A' ELSE TENPO_CD END ");
			sb.append("  , SUM(KYAKUSU) AS KYAKUSU ");
			sb.append("  , SUM(ZYY_KYAKUSU) AS ZYY_KYAKUSU ");
			sb.append("  , SUM(URIGK) AS URIGK ");
			sb.append("  , SUM(ZYY_URIGK) AS ZYY_URIGK ");
			sb.append("  , SUM(MOK_JUN_GK) AS MOK_JUN_GK ");
			sb.append("  , SUM(K11) AS K11 ");
			sb.append("  , SUM(K12) AS K12 ");
			sb.append("  , SUM(K31) AS K31 ");
			sb.append("  , SUM(K41) AS K41 ");
			sb.append("FROM ( ");

			// 売上、レジ件数=0
			sb.append("  SELECT ");
			sb.append("    SUBSTR(URI_YMD, 5, 2) AS URI_YMD ");
			sb.append("    , TENPO_CD ");
			sb.append("    , 0 AS KYAKUSU ");
			sb.append("    , 0 AS ZYY_KYAKUSU ");
			sb.append("    , SUM("+szColName+"_URIGK) AS URIGK ");
			sb.append("    , SUM(ZYY_"+szColName+"_URIGK) AS ZYY_URIGK ");
			sb.append("    , AVG(MOK_JUN_GK) AS MOK_JUN_GK ");
			sb.append("    , 0 AS K11 ");
			sb.append("    , 0 AS K12 ");
			sb.append("    , 0 AS K31 ");
			sb.append("    , 0 AS K41 ");
			sb.append("  FROM ");
			sb.append("    DS_S_URI_TEN ");
			sb.append("  WHERE ");
			sb.append("    SUBSTR(URI_YMD, 1, 6) BETWEEN '"+szYy+"01' AND '"+szYy+"12'");
			sb.append(sqlTenpo);
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
			sb.append("    , 0 AS URIGK ");
			sb.append("    , 0 AS ZYY_URIGK ");
			sb.append("    , 0 AS MOK_JUN_GK ");
			sb.append("    , SUM(CASE WHEN KB = 11 THEN SU END) AS K11 ");
			sb.append("    , SUM(CASE WHEN KB = 12 THEN SU END) AS K12 ");
			sb.append("    , SUM(CASE WHEN KB = 31 THEN SU END) AS K31 ");
			sb.append("    , SUM(CASE WHEN KB = 41 THEN SU END) AS K41 ");
			sb.append("  FROM ");
			sb.append("    DS_M_TENPO_REJI ");
			sb.append("  WHERE ");
			sb.append("    SUBSTR(YMD, 1, 6) BETWEEN '"+szYy+"01' AND '"+szYy+"12'");
			sb.append(sqlTENCD);
			sb.append("  GROUP BY ");
			sb.append("    SUBSTR(YMD, 5, 2) ");
			sb.append("    , TENCD ");

			sb.append("  UNION ALL ");
			// 前年レジ件数
			sb.append("  SELECT ");
			sb.append("    SUBSTR(YMD, 5, 2) AS URI_YMD ");
			sb.append("    , TENCD AS TENPO_CD ");
			sb.append("    , 0 AS KYAKUSU ");
			sb.append("    , SUM(SU) AS ZYY_KYAKUSU ");
			sb.append("    , 0 AS URIGK ");
			sb.append("    , 0 AS ZYY_URIGK ");
			sb.append("    , 0 AS MOK_JUN_GK ");
			sb.append("    , 0 AS K11 ");
			sb.append("    , 0 AS K12 ");
			sb.append("    , 0 AS K31 ");
			sb.append("    , 0 AS K41 ");
			sb.append("  FROM ");
			sb.append("    DS_M_TENPO_REJI ");
			sb.append("  WHERE ");
			sb.append("    SUBSTR(YMD, 1, 6) BETWEEN '"+szZYY+"01' AND '"+szZYY+"12'");
			sb.append(sqlTENCD);
			sb.append("  GROUP BY ");
			sb.append("    SUBSTR(YMD, 5, 2) ");
			sb.append("    , TENCD ");

			sb.append(")X ");
			sb.append("GROUP BY GROUPING SETS ((URI_YMD,TENPO_CD),(URI_YMD)) ");
			sb.append(") ");

			sb.append(", MVIW(KBN1,KNM1,KBN2,KNM2) AS ( ");
			sb.append(sbMVIW);
			sb.append(") ");
			sb.append("SELECT ");
			sb.append(" MAX(CASE WHEN T3.KBN2 = "+szFirstNo+" THEN NVL(T2.TENPO_NM,'合計') END) AS 店舗 ");
			sb.append(",MAX(T3.KNM1) AS 区分 ");
			sb.append(",MAX(T3.KNM2) AS 項目 ");

			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='01' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"01月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='02' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"02月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='03' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"03月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='04' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"04月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='05' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"05月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='06' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"06月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='07' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"07月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='08' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"08月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='09' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"09月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='10' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"10月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='11' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"11月\" ");
			sb.append(" ,SUM(CASE WHEN T1.URI_YMD='12' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \"12月\" ");

			sb.append("FROM W00 T1 ");
			sb.append("LEFT OUTER JOIN M_TENPO T2 ON T1.TENPO_CD=T2.TENPO_CD ");
			sb.append(",MVIW T3 ");
			sb.append(" WHERE T1.TENPO_CD>'000' ");
			sb.append("GROUP BY T1.TENPO_CD,T3.KBN2 ");
			sb.append("ORDER BY T1.TENPO_CD,T3.KBN2 ");

		}else if (DefineReport.RadD02_UNIT.ID3.getVal().equals(szUnit)){
			// 日
			sb.append("WITH W00(URI_YMD, TENPO_CD, KYAKUSU, ZYY_KYAKUSU, URIGK, ZYY_URIGK, MOK_JUN_GK, K11,K12,K31,K41) AS ( ");
			sb.append("SELECT ");
			sb.append("    CASE WHEN GROUPING_ID(URI_YMD)=1 THEN '合計' ELSE URI_YMD END ");
			sb.append("  , CASE WHEN GROUPING_ID(TENPO_CD)=1 THEN '00A' ELSE TENPO_CD END ");
			sb.append("  , SUM(KYAKUSU) AS KYAKUSU ");
			sb.append("  , SUM(ZYY_KYAKUSU) AS ZYY_KYAKUSU ");
			sb.append("  , SUM(URIGK) AS URIGK ");
			sb.append("  , SUM(ZYY_URIGK) AS ZYY_URIGK ");
			sb.append("  , SUM(MOK_JUN_GK) AS MOK_JUN_GK ");
			sb.append("  , SUM(K11) AS K11 ");
			sb.append("  , SUM(K12) AS K12 ");
			sb.append("  , SUM(K31) AS K31 ");
			sb.append("  , SUM(K41) AS K41 ");
			sb.append("FROM ( ");

			// 売上、レジ件数=0
			sb.append("  SELECT ");
			sb.append("    URI_YMD ");
			sb.append("    , TENPO_CD ");
			sb.append("    , 0 AS KYAKUSU ");
			sb.append("    , 0 AS ZYY_KYAKUSU ");
			sb.append("    , SUM("+szColName+"_URIGK) AS URIGK ");
			sb.append("    , SUM(ZYY_"+szColName+"_URIGK) AS ZYY_URIGK ");
			sb.append("    , AVG(MOK_JUN_GK) AS MOK_JUN_GK ");
			sb.append("    , 0 AS K11 ");
			sb.append("    , 0 AS K12 ");
			sb.append("    , 0 AS K31 ");
			sb.append("    , 0 AS K41 ");
			sb.append("  FROM ");
			sb.append("    DS_S_URI_TEN ");
			sb.append("  WHERE ");
			sb.append("    SUBSTR(URI_YMD, 1, 6) = '"+szYy+szNm+"' ");
			sb.append(sqlTenpo);
			sb.append("  GROUP BY ");
			sb.append("    URI_YMD ");
			sb.append("    , TENPO_CD ");

			sb.append("  UNION ALL ");
			// 当年レジ件数
			sb.append("  SELECT ");
			sb.append("    CAST(YMD AS CHAR(8)) AS URI_YMD ");
			sb.append("    , TENCD AS TENPO_CD ");
			sb.append("    , SUM(SU) AS KYAKUSU ");
			sb.append("    , 0 AS ZYY_KYAKUSU ");
			sb.append("    , 0 AS URIGK ");
			sb.append("    , 0 AS ZYY_URIGK ");
			sb.append("    , 0 AS MOK_JUN_GK ");
			sb.append("    , SUM(CASE WHEN KB=11 THEN SU END) AS K11 ");
			sb.append("    , SUM(CASE WHEN KB=12 THEN SU END) AS K12 ");
			sb.append("    , SUM(CASE WHEN KB=31 THEN SU END) AS K31 ");
			sb.append("    , SUM(CASE WHEN KB=41 THEN SU END) AS K41 ");
			sb.append("  FROM ");
			sb.append("    DS_M_TENPO_REJI ");
			sb.append("  WHERE ");
			sb.append("    SUBSTR(YMD, 1, 6) = '"+szYy+szNm+"' ");
			sb.append(sqlTENCD);
			sb.append("  GROUP BY ");
			sb.append("    YMD ");
			sb.append("    , TENCD ");

			sb.append("  UNION ALL ");
			// 前年レジ件数
			sb.append("  SELECT ");
			sb.append("    CAST(YMD-10000 AS CHAR(8)) AS URI_YMD ");
			sb.append("    , TENCD AS TENPO_CD ");
			sb.append("    , 0 AS KYAKUSU ");
			sb.append("    , SUM(SU) AS ZYY_KYAKUSU ");
			sb.append("    , 0 AS URIGK ");
			sb.append("    , 0 AS ZYY_URIGK ");
			sb.append("    , 0 AS MOK_JUN_GK ");
			sb.append("    , 0 AS K11 ");
			sb.append("    , 0 AS K12 ");
			sb.append("    , 0 AS K31 ");
			sb.append("    , 0 AS K41 ");
			sb.append("  FROM ");
			sb.append("    DS_M_TENPO_REJI ");
			sb.append("  WHERE ");
			sb.append("    SUBSTR(YMD, 1, 6) = '"+szZYY+szNm+"' ");
			sb.append(sqlTENCD);
			sb.append("  GROUP BY ");
			sb.append("    YMD ");
			sb.append("    , TENCD ");

			sb.append(")X ");
			sb.append("GROUP BY GROUPING SETS ((URI_YMD,TENPO_CD),(URI_YMD)) ");
			sb.append(") ");

			sb.append(", MVIW(KBN1,KNM1,KBN2,KNM2) AS ( ");
			sb.append(sbMVIW);
			sb.append(") ");
			sb.append("SELECT ");
			sb.append(" MAX(CASE WHEN T3.KBN2 = "+szFirstNo+" THEN NVL(T2.TENPO_NM,'合計') END) AS 店舗 ");
			sb.append(",MAX(T3.KNM1) AS 区分 ");
			sb.append(",MAX(T3.KNM2) AS 項目 ");
			for (int i=0;i<Years.size();i++){
				sb.append(" ,SUM(CASE WHEN T1.URI_YMD='"+Years.get(i)+"' THEN CASE T3.KBN2 WHEN 1 THEN T1.MOK_JUN_GK WHEN 2 THEN ROUND(T1.URIGK,0) WHEN 3 THEN ROUND(T1.ZYY_URIGK,0) WHEN 4 THEN CASE WHEN T1.MOK_JUN_GK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.MOK_JUN_GK * 100, 2) END WHEN 5 THEN CASE WHEN T1.ZYY_URIGK = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.ZYY_URIGK * 100, 2) END WHEN 6 THEN T1.K11 WHEN 7 THEN T1.K12 WHEN 8 THEN T1.K31 WHEN 9 THEN T1.K41 WHEN 10 THEN T1.KYAKUSU WHEN 11 THEN T1.ZYY_KYAKUSU WHEN 12 THEN CASE WHEN T1.ZYY_KYAKUSU = 0 THEN NULL ELSE ROUND(T1.KYAKUSU / T1.ZYY_KYAKUSU * 100, 2) END WHEN 13 THEN CASE WHEN T1.KYAKUSU = 0 THEN NULL ELSE ROUND(T1.URIGK / T1.KYAKUSU, 0) END END END) AS \""+Titles.get(i)+"\" ");
			}
			sb.append("FROM W00 T1 ");
			sb.append("LEFT OUTER JOIN M_TENPO T2 ON T1.TENPO_CD=T2.TENPO_CD ");
			sb.append(",MVIW T3 ");
			sb.append(" WHERE T1.TENPO_CD>'000' ");
			sb.append("GROUP BY T1.TENPO_CD,T3.KBN2 ");
			sb.append("ORDER BY T1.TENPO_CD,T3.KBN2 ");
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
		if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
		} else if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID3.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
		}
		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 店舗
		cells.add(DefineReport.RadD03_TENPO.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD03_TENPO.NAME.getObj()));
		if (StringUtils.equals(jad.getJSONValue(DefineReport.RadD03_TENPO.NAME.getObj()), DefineReport.RadD03_TENPO.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.TENPO.getObj()));
		}
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
