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
public class D23Dao extends ItemDao {

	/**
	 * @param source コネクション
	 */
	public D23Dao(Connection source) {
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
		String szYy2		= getMap().get("YYYY_YY2");	// 終了年

		String szAxis		= getMap().get("AXIS");			// 集計軸
		String szArea		= getMap().get("AREA");			// 都道府県
		String szAreas	= getMap().get("AREAS");		// 都道府県詳細
		String szAGE		= getMap().get("AGE");			// 年齢
		String szFKY		= getMap().get("FKY");			// 初回購入年
		String szRVAL		= getMap().get("RVAL");			// R値

		System.out.println("CHANEL:"+szChanel+",UNIT:"+szUnit);
		System.out.println("YYYY_MM:"+szYy+",YYYY_MM2:"+szYy2);
		System.out.println("AXIS:"+szAxis);
		System.out.println("AREA:"+szArea+",AREAS:"+szAreas);
		System.out.println("AGE:"+szAGE);
		System.out.println("FKY:"+szFKY);
		System.out.println("RVAL:"+szRVAL);

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

		}

		// R値
		String szRVAL_SELECT = "";
		if (DefineReport.RadD08_RVL.ID1.getVal().equals(szRVAL)){
			// R6
			szRVAL_SELECT=" AND ZMRKB=1 ";
		} else {
			// R12
			szRVAL_SELECT=" AND ZMRKB IN (1,2) ";
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

		// 都道府県
		String agePREF = "";

		// 年齢刻み
		String ageColKB ="";
		String ageColNM ="";
		// 前年購入金額
		String ageZKG = "";
		// 軸
		if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
			// 都道府県
			if (DefineReport.RadD04_AREA.ID2.getVal().equals(szArea) && !"00".equals(szAreas)){
				agePREF = " IN ("+szAreas+")";
			}
			System.out.println("szPREF:"+agePREF);
		} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
			// 年齢
			if (DefineReport.RadD05_AGE.ID1.getVal().equals(szAGE)){
				// 1歳
				ageColKB="AGE1KB";
				ageColNM="AGE1NM";

			}else if (DefineReport.RadD05_AGE.ID2.getVal().equals(szAGE)){
				// 5歳
				ageColKB="AGE5KB";
				ageColNM="AGE5NM";

			}else if (DefineReport.RadD05_AGE.ID3.getVal().equals(szAGE)){
				// 10歳
				ageColKB="AGE10KB";
				ageColNM="AGE10NM";
			}
		} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
			// 初回購入年
			if (DefineReport.RadD05_AGE.ID1.getVal().equals(szFKY)){
				// 1歳
				ageColKB="FSY1KB";
				ageColNM="FSY1NM";

			}else if (DefineReport.RadD05_AGE.ID2.getVal().equals(szFKY)){
				// 5歳
				ageColKB="FSY5KB";
				ageColNM="FSY5NM";

			}else if (DefineReport.RadD05_AGE.ID3.getVal().equals(szFKY)){
				// 10歳
				ageColKB="FSY10KB";
				ageColNM="FSY10NM";
			}
		}

		StringBuffer sb = new StringBuffer();
		if (DefineReport.RadD02_UNIT.ID1.getVal().equals(szUnit)){
			// 年
			sb.append("WITH ");
			sb.append("W_CAL(YMD) AS ( ");
			sb.append("SELECT DISTINCT SUBSTR(YMD_CD,1,4) FROM M_CALENDAR WHERE YMD_CD BETWEEN "+szYy+"0101 AND "+szYy2+"1231 ");
			sb.append(") ");
			sb.append(",W_AXIS(CD,NM) AS ( ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append(" SELECT 9999 AS CD,'' AS NM FROM DUAL ");
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append(" SELECT CHIKU_CD AS CD,CHIKU_MEI AS NM FROM M_AREA WHERE CHIKU1_CD<>'48'");
				if (!"".equals(agePREF)){
					sb.append(" AND CHIKU_CD "+agePREF);
				}
				sb.append(" UNION ALL SELECT '999','計' FROM DUAL ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" SELECT DISTINCT "+ageColKB+" AS CD,"+ageColNM+" AS NM FROM DS_M_AGE ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" SELECT DISTINCT "+ageColKB+" AS CD, "+ageColNM+" AS NM FROM DS_M_SHU_Y WHERE FSY <= "+szYy);
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(" SELECT CD,NM FROM DS_M_KINGAKU ");
			}
			sb.append(") ");

			sb.append(",W_BASE(YMD,CD) AS ( ");
			sb.append("SELECT YMD,CD AS CD FROM W_CAL,W_AXIS ");
			sb.append(") ");

			sb.append(",W_TRG(YMD,CD,SU) AS ( ");
			sb.append("SELECT ");
			sb.append("  YY AS YMD ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append(" , 9999 AS CD ");
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append(" , CASE WHEN NVL(K.PREF,-1)=-1 THEN '99' ELSE K.PREF END AS CD ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" , NVL(AG."+ageColKB+",1000) AS CD ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" , NVL(AG."+ageColKB+",1000) AS CD ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(", ZYKB AS CD ");
			}
			sb.append(", SUM(KCNT) AS SU ");
			sb.append("FROM DS_Q_KNO_Y K ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" LEFT OUTER JOIN DS_M_AGE AG ON K.AGE=AG.AGE ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" LEFT OUTER JOIN DS_M_SHU_Y AG ON K.YYSHU=AG.FSY ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
			}
			sb.append("WHERE YY BETWEEN "+szYy+" AND "+szYy2+" ");
			// R値
			sb.append(szRVAL_SELECT);

			// 軸
			if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				if (!"".equals(agePREF)){
					sb.append(" AND K.PREF "+agePREF);
				}
			}

			sb.append("GROUP BY YY ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append(" , CASE WHEN NVL(K.PREF,-1)=-1 THEN '99' ELSE K.PREF END ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" , NVL(AG."+ageColKB+",1000) ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" , NVL(AG."+ageColKB+",1000) ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(", ZYKB ");
			}
			sb.append(") ");


			// 基本集計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT ");
			sb.append(" T0.YMD AS YMFG ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append(" , 9999 AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append(" , CASE GROUPING_ID(T0.CD) WHEN 1 THEN '999' WHEN 0 THEN NVL(T0.CD,'99') END AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" , CASE GROUPING_ID(T0.CD) WHEN 1 THEN 9999 WHEN 0 THEN NVL(T0.CD,1000) END AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" , CASE GROUPING_ID(T0.CD) WHEN 1 THEN 9999 WHEN 0 THEN NVL(T0.CD,1000) END AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(" , CASE WHEN GROUPING_ID(T0.CD)=1 THEN 10 ELSE T0.CD END AS CLSNO ");
			}
			sb.append(",SUM(T1.SU) AS SU ");
			sb.append(",SUM(T1.KG) AS KG ");
			sb.append(",SUM(T1.KN) AS KN ");
			sb.append(",SUM(T2.SU) AS KT ");
			sb.append("FROM W_BASE T0 ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append("SELECT ");
			sb.append("    SUBSTR(U.YM,1,4) AS YMFG ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append(" , 9999 AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append("    , NVL(K.PREF,'99') AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append("   , NVL(AG."+ageColKB+",1000) AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append("   , NVL(AG."+ageColKB+",1000) AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append("  , CASE WHEN K.ZYKG IS NULL AND K.ZMREP IS NULL THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 9 END AS CLSNO ");
			}
			sb.append("  , SUM(U.SU) AS SU ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , COUNT(DISTINCT U.KNOT) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_S_URI_SM_D U ");
			sb.append("  INNER JOIN DS_S_KNO_M K ON (U.YM=K.YM AND U.KNOT=K.KNOT ");
			sb.append("  AND U.YM BETWEEN "+szYy+"01 AND "+szYy2+"12 ");
			sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
			sb.append("  AND U.KNOT<>0 ");
			if (DefineReport.RadD08_RVL.ID1.getVal().equals(szRVAL)){
				// R6
				sb.append("  AND K.ZMREP BETWEEN 1 AND 6 ");
			} else {
				// R12
				sb.append("  AND K.ZMREP BETWEEN 1 AND 12 ");
			}

			// 軸
			if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				if (!"".equals(agePREF)){
					sb.append(" AND PREF "+agePREF);
				}
			}
			sb.append("  ) ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" LEFT OUTER JOIN DS_M_AGE AG ON K.AGE=AG.AGE ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" LEFT OUTER JOIN DS_M_SHU_Y AG ON SUBSTR(K.DTSHU,1,4)=AG.FSY ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
			}
			sb.append("GROUP BY ");
			sb.append("SUBSTR(U.YM,1,4) ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append("   , NVL(K.PREF,'99') ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" , NVL(AG."+ageColKB+",1000) ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" , NVL(AG."+ageColKB+",1000) ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(", CASE WHEN K.ZYKG IS NULL AND K.ZMREP IS NULL THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 9 END ");
			}
			sb.append(") T1 ON T1.YMFG=T0.YMD AND T1.CLSNO=T0.CD ");
			sb.append("LEFT OUTER JOIN W_TRG T2 ON T0.YMD=T2.YMD AND T0.CD=T2.CD ");
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append("GROUP BY GROUPING SETS((T0.YMD)) ");
			} else {
				sb.append("GROUP BY GROUPING SETS((T0.YMD,T0.CD),(T0.YMD)) ");
			}
			sb.append(") ");

			//表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 0,'対象者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 1,'購入者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'購入金額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'顧客単価' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'反応率' FROM DUAL ");
			sb.append(") ");
			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");
			for (int i=0;i<Years.size();i++){
				sb.append(" ,SUM(CASE WHEN YMFG='"+Years.get(i)+"' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \""+Years.get(i)+"年\" ");
			}
			sb.append("FROM T_URI U ");
			sb.append("LEFT OUTER JOIN W_AXIS A ON U.CLSNO=A.CD ");
			sb.append(",MVIW V ");
			sb.append(ageZKG);	// 前年購入金額
			sb.append("GROUP BY V.KBN,U.CLSNO ");
			sb.append("ORDER BY V.KBN,U.CLSNO ");

		}else if (DefineReport.RadD02_UNIT.ID2.getVal().equals(szUnit)){
			// 月
			sb.append("WITH ");
			sb.append("W_CAL(YMD) AS ( ");
			sb.append("SELECT DISTINCT SUBSTR(YMD_CD,1,6) FROM M_CALENDAR WHERE YMD_CD BETWEEN "+szYy+"0101 AND "+szYy2+"1231 ");
			sb.append(") ");
			sb.append(",W_AXIS(CD,NM) AS ( ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append(" SELECT 9999 AS CD,'' AS NM FROM DUAL ");
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				sb.append(" SELECT CHIKU_CD AS CD,CHIKU_MEI AS NM FROM M_AREA WHERE CHIKU1_CD<>'48'");
				if (!"".equals(agePREF)){
					sb.append(" AND CHIKU_CD "+agePREF);
				}
				sb.append(" UNION ALL SELECT '999','計' FROM DUAL ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" SELECT DISTINCT "+ageColKB+" AS CD,"+ageColNM+" AS NM FROM DS_M_AGE ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" SELECT DISTINCT "+ageColKB+" AS CD, "+ageColNM+" AS NM FROM DS_M_SHU_Y WHERE FSY <= "+szYy);
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(" SELECT CD,NM FROM DS_M_KINGAKU ");
			}
			sb.append(") ");

			sb.append(",W_BASE(YMD,CD) AS ( ");
			sb.append("SELECT YMD,CD AS CD FROM W_CAL,W_AXIS ");
			sb.append(") ");

			sb.append(",W_TRG(YMD,CD,SU) AS ( ");
			sb.append("SELECT ");
			sb.append("  YM AS YMD ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append(" , 9999 AS CD ");
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append(" , CASE WHEN NVL(K.PREF,-1)=-1 THEN '99' ELSE K.PREF END AS CD ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" , NVL(AG."+ageColKB+",1000) AS CD ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" , NVL(AG."+ageColKB+",1000) AS CD ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(", ZYKB AS CD ");
			}
			sb.append(", SUM(KCNT) AS SU ");
			sb.append("FROM DS_Q_KNO_M K ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" LEFT OUTER JOIN DS_M_AGE AG ON K.AGE=AG.AGE ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" LEFT OUTER JOIN DS_M_SHU_Y AG ON K.YYSHU=AG.FSY ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
			}
			sb.append("WHERE YM BETWEEN "+szYy+"01 AND "+szYy2+"12 ");
			// R値
			sb.append(szRVAL_SELECT);

			// 軸
			if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				if (!"".equals(agePREF)){
					sb.append(" AND K.PREF "+agePREF);
				}
			}
			sb.append("GROUP BY YM ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append(" , CASE WHEN NVL(K.PREF,-1)=-1 THEN '99' ELSE K.PREF END ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" , NVL(AG."+ageColKB+",1000) ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" , NVL(AG."+ageColKB+",1000) ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(", ZYKB ");
			}
			sb.append(") ");


			// 基本集計
			sb.append(", T_URI AS ( ");
			sb.append("SELECT ");
			sb.append(" SUBSTR(T0.YMD,5,2) AS YMFG ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append(" , 9999 AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append(" , CASE GROUPING_ID(T0.CD) WHEN 1 THEN '999' WHEN 0 THEN NVL(T0.CD,'99') END AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" , CASE GROUPING_ID(T0.CD) WHEN 1 THEN 9999 WHEN 0 THEN NVL(T0.CD,1000) END AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" , CASE GROUPING_ID(T0.CD) WHEN 1 THEN 9999 WHEN 0 THEN NVL(T0.CD,1000) END AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(" , CASE WHEN GROUPING_ID(T0.CD)=1 THEN 10 ELSE T0.CD END AS CLSNO ");
			}
			sb.append(",SUM(T1.SU) AS SU ");
			sb.append(",SUM(T1.KG) AS KG ");
			sb.append(",SUM(T1.KN) AS KN ");
			sb.append(",SUM(T2.SU) AS KT ");
			sb.append("FROM W_BASE T0 ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append("SELECT ");
			sb.append("    U.YM AS YMFG ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append(" , 9999 AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append("    , NVL(K.PREF,'99') AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append("   , NVL(AG."+ageColKB+",1000) AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append("   , NVL(AG."+ageColKB+",1000) AS CLSNO ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append("  , CASE WHEN K.ZYKG IS NULL AND K.ZMREP IS NULL THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 9 END AS CLSNO ");
			}
			sb.append("  , SUM(U.SU) AS SU ");
			sb.append("  , SUM(U.KG) AS KG ");
			sb.append("  , COUNT(DISTINCT U.KNOT) AS KN ");
			sb.append("FROM ");
			sb.append("  DS_S_URI_SM_D U ");
			sb.append("  INNER JOIN DS_S_KNO_M K ON (U.YM=K.YM AND U.KNOT=K.KNOT ");
			sb.append("  AND U.YM BETWEEN "+szYy+"01 AND "+szYy2+"12 ");
			sb.append("  AND U.CHANEL IN ("+szChanelCode+") ");
			sb.append("  AND U.KNOT<>0 ");
			if (DefineReport.RadD08_RVL.ID1.getVal().equals(szRVAL)){
				// R6
				sb.append("  AND K.ZMREP BETWEEN 1 AND 6 ");
			} else {
				// R12
				sb.append("  AND K.ZMREP BETWEEN 1 AND 12 ");
			}

			// 軸
			if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				if (!"".equals(agePREF)){
					sb.append(" AND PREF "+agePREF);
				}
			}
			sb.append("  ) ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" LEFT OUTER JOIN DS_M_AGE AG ON K.AGE=AG.AGE ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" LEFT OUTER JOIN DS_M_SHU_Y AG ON SUBSTR(K.DTSHU,1,4)=AG.FSY ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
			}
			sb.append("GROUP BY ");
			sb.append("U.YM ");
			// 軸
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
			} else if (DefineReport.RadD11_AXIS.ID1.getVal().equals(szAxis)){
				// 都道府県
				sb.append("   , NVL(K.PREF,'99') ");
			} else if (DefineReport.RadD11_AXIS.ID2.getVal().equals(szAxis)){
				// 年齢
				sb.append(" , NVL(AG."+ageColKB+",1000) ");
			} else if (DefineReport.RadD11_AXIS.ID3.getVal().equals(szAxis)){
				// 初回購入年
				sb.append(" , NVL(AG."+ageColKB+",1000) ");
			} else if (DefineReport.RadD11_AXIS.ID4.getVal().equals(szAxis)){
				// 前年購入額
				sb.append(", CASE WHEN K.ZYKG IS NULL AND K.ZMREP IS NULL THEN 0 WHEN NVL(K.ZYKG, 0) = 0 AND K.ZMREP IS NOT NULL THEN 1 WHEN K.ZYKG BETWEEN 1 AND 2999 THEN 2 WHEN K.ZYKG BETWEEN 3000 AND 4999 THEN 3 WHEN K.ZYKG BETWEEN 5000 AND 9999 THEN 4 WHEN K.ZYKG BETWEEN 10000 AND 24999 THEN 5 WHEN K.ZYKG BETWEEN 25000 AND 49999 THEN 6 WHEN K.ZYKG BETWEEN 50000 AND 99999 THEN 7 WHEN K.ZYKG >= 100000 THEN 8 ELSE 9 END ");
			}
			sb.append(") T1 ON T1.YMFG=T0.YMD AND T1.CLSNO=T0.CD ");
			sb.append("LEFT OUTER JOIN W_TRG T2 ON T0.YMD=T2.YMD AND T0.CD=T2.CD ");
			if (DefineReport.RadD11_AXIS.ID0.getVal().equals(szAxis)){
				// 集計軸なし
				sb.append("GROUP BY GROUPING SETS((T0.YMD)) ");
			} else {
				sb.append("GROUP BY GROUPING SETS((T0.YMD,T0.CD),(T0.YMD)) ");
			}
			sb.append(") ");

			//表示項目
			sb.append(", MVIW(KBN,KNM) AS ( ");
			sb.append("SELECT 0,'対象者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 1,'購入者' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 2,'購入金額' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 3,'顧客単価' FROM DUAL ");
			sb.append("UNION ALL ");
			sb.append("SELECT 4,'反応率' FROM DUAL ");
			sb.append(") ");
			sb.append("SELECT MAX(V.KNM) AS 項目 ");
			sb.append(" ,MAX(A.NM) AS 区分 ");
			sb.append(" ,SUM(CASE WHEN YMFG='01' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"1月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='02' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"2月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='03' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"3月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='04' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"4月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='05' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"5月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='06' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"6月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='07' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"7月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='08' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"8月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='09' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"9月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='10' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"10月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='11' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"11月\" ");
			sb.append(" ,SUM(CASE WHEN YMFG='12' THEN CASE V.KBN WHEN 0 THEN U.KT WHEN 1 THEN U.KN WHEN 3 THEN CASE WHEN U.KN=0 THEN NULL ELSE ROUND(U.KG/U.KN,0) END WHEN 4 THEN CASE WHEN U.KT=0 THEN NULL ELSE ROUND(U.KN/U.KT*100,2) END WHEN 2 THEN U.KG END END) AS \"12月\" ");
			sb.append("FROM T_URI U ");
			sb.append("LEFT OUTER JOIN W_AXIS A ON U.CLSNO=A.CD ");
			sb.append(",MVIW V ");
			sb.append(ageZKG);	// 前年購入金額
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
		}
		getWhere().add(cells);

		String AXIS = jad.getJSONValue(DefineReport.RadD11_AXIS.NAME.getObj());
		cells = new ArrayList<String>();
		if (StringUtils.equals(AXIS, DefineReport.RadD11_AXIS.ID1.getVal())) {
			// 都道府県
			cells.add(DefineReport.RadD04_AREA.NAME.getView());
			cells.add(jad.getJSONText(DefineReport.RadD04_AREA.NAME.getObj()));
			if (StringUtils.equals(jad.getJSONValue(DefineReport.RadD04_AREA.NAME.getObj()), DefineReport.RadD04_AREA.ID2.getVal())) {
				cells.add(jad.getJSONText(DefineReport.Select.AREA.getObj()));
			}
		} else if (StringUtils.equals(AXIS, DefineReport.RadD11_AXIS.ID2.getVal())) {
			// 年齢
			cells.add(DefineReport.RadD05_AGE.NAME.getView());
			cells.add(jad.getJSONText(DefineReport.RadD05_AGE.NAME.getObj()));
		} else if (StringUtils.equals(AXIS, DefineReport.RadD11_AXIS.ID3.getVal())) {
			// 初回購入年
			cells.add(DefineReport.RadD06_FKY.NAME.getView());
			cells.add(jad.getJSONText(DefineReport.RadD06_FKY.NAME.getObj()));
		} else if (StringUtils.equals(AXIS, DefineReport.RadD11_AXIS.ID4.getVal())) {
			// 前年購入金額
			cells.add(DefineReport.RadD07_ZKG.NAME.getView());
			cells.add(jad.getJSONText(DefineReport.RadD07_ZKG.NAME.getObj()));
		}
		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 前月末R値
		cells.add(DefineReport.RadD08_ZRVL.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD08_ZRVL.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
