package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * A02テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class A02Dao extends ItemDao {

	/**
	 * 新しいT02Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public A02Dao(Connection source) {
		super(source);
	}

	/**
	 * 検索実行
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
	 * @return 検索<String>
	 */
	private String createCommand() {

		String command = null;

		String szF2 		=	getMap().get("F2_JIKI");			//
		String szShoG 		=	getMap().get("SHOG");			//
		String szType1 		=	getMap().get("H_TYPE");			// （

		// パラメータ確認
		System.out.println("F2_JIKI:" +szF2+",SHOG:"+szShoG+",H_TYPE:"+szType1);

		String sqlWhere1 = "  ";
		String sqlWhere2 = "  ";
		// 設定
		if(StringUtils.equals(szF2, "1")){  // F2当月
			sqlWhere1 = " WHERE F2_KAISU = 'F0201'  ";
		}else if (StringUtils.equals(szF2, "2")){ //F2　3ヶ月後
			sqlWhere1 = " WHERE F2_KAISU = 'F0202'  ";
		}else if (StringUtils.equals(szF2, "3")){ //F2　6ヶ月後
			sqlWhere1 = " WHERE F2_KAISU = 'F0203'  ";
		}else if (StringUtils.equals(szF2, "4")){ //F2　12ヶ月後
			sqlWhere1 = " WHERE F2_KAISU = 'F0204'  ";
		}
		if(!StringUtils.equals(szShoG, "00")){  //
			if((StringUtils.equals(szShoG, "G10"))||(StringUtils.equals(szShoG, "G20"))){  //
				sqlWhere2 = " AND SUBSTR(SHOHIN_GRP_CD,1,3) = '"+szShoG+"' ";
			}else if (StringUtils.equals(szShoG, "G101011")){ //
				sqlWhere2 = " AND SHOHIN_GRP_CD <= 'G1010110' ";
			}else if (StringUtils.equals(szShoG, "G10103")){ //
				sqlWhere2 = " AND  SUBSTR(SHOHIN_GRP_CD,1,6) = '"+szShoG+"' ";
			}else if (StringUtils.equals(szShoG, "G10201")){ //
				sqlWhere2 = " AND  SHOHIN_GRP_CD >= 'G1011100' AND SHOHIN_GRP_CD <= 'G1020100' ";
			}else { //
				sqlWhere2 = " AND SHOHIN_GRP_CD = '"+szShoG+"' ";
			}
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		if(StringUtils.equals(szType1, "1")){  //顧客数
			sbSQL.append("WITH W1(NF_JUCHU_YM) AS (SELECT NF_JUCHU_YM FROM TSU_F2 GROUP BY NF_JUCHU_YM),");
			sbSQL.append("W_CNT(C_YYYY,CNT_M) AS ( SELECT SUBSTR(NF_JUCHU_YM,1,4),COUNT(SUBSTR(NF_JUCHU_YM,1,4)) FROM W1 GROUP BY SUBSTR(NF_JUCHU_YM,1,4))");
			sbSQL.append(",WORKT(YYYY,M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,M11,M12) AS ( ");
			sbSQL.append("select substr(NF_JUCHU_YM,1,4),");
			sbSQL.append(" CASE WHEN  MONTH_MM = '01' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '02' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '03' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '04' THEN SUM(KYAKUSU) ELSE 0 END,");
			sbSQL.append(" CASE WHEN  MONTH_MM = '05' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '06' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '07' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '08' THEN SUM(KYAKUSU) ELSE 0 END,");
			sbSQL.append(" CASE WHEN  MONTH_MM = '09' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '10' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '11' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '12' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" FROM TSU_F2 "+sqlWhere1+sqlWhere2+" GROUP BY substr(NF_JUCHU_YM,1,4),MONTH_MM )");
			sbSQL.append("SELECT YYYY as SORT,YYYY AS \"受注年\",SUM(M01) AS \"１月\",SUM(M02) AS \"２月\",SUM(M03) AS \"３月\",SUM(M04) AS \"４月\",SUM(M05) AS \"５月\",SUM(M06) AS \"６月\",SUM(M07) AS \"７月\",SUM(M08) AS \"８月\",SUM(M09) AS \"９月\",SUM(M10) AS \"１０月\",SUM(M11) AS \"１１月\",SUM(M12) AS \"１２月\"");
			sbSQL.append(",CASE WHEN CNT_M <= 6 THEN ROUND(SUM(M01+M02+M03+M04+M05+M06)/CNT_M,0) ELSE ROUND(SUM(M01+M02+M03+M04+M05+M06)/6,0) END AS \"上期平均\"");
			sbSQL.append(",CASE WHEN CNT_M >= 7 AND CNT_M < 12 THEN ROUND(SUM(M07+M08+M09+M10+M11+M12)/CNT_M-6,0) ELSE ROUND(SUM(M07+M08+M09+M10+M11+M12)/6,0) END AS \"下期平均\"");
			sbSQL.append(",ROUND(SUM(M01+M02+M03+M04+M05+M06+M07+M08+M09+M10+M11+M12)/CNT_M,0) AS \"年平均\" ");
			sbSQL.append(" FROM WORKT,W_CNT WHERE YYYY = C_YYYY GROUP BY YYYY,CNT_M ");
			sbSQL.append(" order by SORT");
		}else {			//転換率
			sbSQL.append("WITH WORKT1(YYYY,M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,M11,M12) AS ( ");
			sbSQL.append("select substr(NF_JUCHU_YM,1,4),");
			sbSQL.append(" CASE WHEN  MONTH_MM = '01' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '02' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '03' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '04' THEN SUM(KYAKUSU) ELSE 0 END,");
			sbSQL.append(" CASE WHEN  MONTH_MM = '05' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '06' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '07' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '08' THEN SUM(KYAKUSU) ELSE 0 END,");
			sbSQL.append(" CASE WHEN  MONTH_MM = '09' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '10' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '11' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '12' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" FROM TSU_F2 "+sqlWhere1+sqlWhere2+" GROUP BY substr(NF_JUCHU_YM,1,4),MONTH_MM ),");
			sbSQL.append(" WORKALL1(AYYYY,A01,A02,A03,A04,A05,A06,A07,A08,A09,A10,A11,A12) AS ( ");
			sbSQL.append("select substr(NF_JUCHU_YM,1,4),");
			sbSQL.append(" CASE WHEN  MONTH_MM = '01' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '02' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '03' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '04' THEN SUM(KYAKUSU) ELSE 0 END,");
			sbSQL.append(" CASE WHEN  MONTH_MM = '05' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '06' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '07' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '08' THEN SUM(KYAKUSU) ELSE 0 END,");
			sbSQL.append(" CASE WHEN  MONTH_MM = '09' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '10' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '11' THEN SUM(KYAKUSU) ELSE 0 END,CASE WHEN  MONTH_MM = '12' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" FROM TSU_F2 WHERE F2_KAISU = 'F0101'"+sqlWhere2+" GROUP BY substr(NF_JUCHU_YM,1,4),MONTH_MM ),");
			sbSQL.append(" WORKT(YYYY,M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,M11,M12) AS (");
			sbSQL.append("  SELECT YYYY,SUM(M01),SUM(M02),SUM(M03),SUM(M04),SUM(M05),SUM(M06),SUM(M07),SUM(M08),SUM(M09),SUM(M10),SUM(M11),SUM(M12) FROM WORKT1 GROUP BY YYYY),");
			sbSQL.append(" WORKALL(AYYYY,A01,A02,A03,A04,A05,A06,A07,A08,A09,A10,A11,A12) AS (");
			sbSQL.append("  SELECT AYYYY,SUM(A01),SUM(A02),SUM(A03),SUM(A04),SUM(A05),SUM(A06),SUM(A07),SUM(A08),SUM(A09),SUM(A10),SUM(A11),SUM(A12) FROM WORKALL1 GROUP BY AYYYY) ");
			sbSQL.append("SELECT YYYY as SORT,YYYY AS \"受注年\"");
			sbSQL.append(",CASE WHEN SUM(M01) > 0 THEN ROUND(SUM(M01)/SUM(A01) *100,1) ELSE 0 END AS \"１月\",CASE WHEN SUM(M02) > 0 THEN SUM(M02)/SUM(A02) *100 ELSE 0 END AS \"２月\"");
			sbSQL.append(",CASE WHEN SUM(M03) > 0 THEN ROUND(SUM(M03)/SUM(A03) *100,1) ELSE 0 END AS \"３月\",CASE WHEN SUM(M04) > 0 THEN SUM(M04)/SUM(A04) *100 ELSE 0 END AS \"４月\"");
			sbSQL.append(",CASE WHEN SUM(M05) > 0 THEN ROUND(SUM(M05)/SUM(A05) *100,1) ELSE 0 END AS \"５月\",CASE WHEN SUM(M06) > 0 THEN SUM(M06)/SUM(A06) *100 ELSE 0 END AS \"６月\"");
			sbSQL.append(",CASE WHEN SUM(M07) > 0 THEN ROUND(SUM(M07)/SUM(A07) *100,1) ELSE 0 END AS \"７月\",CASE WHEN SUM(M08) > 0 THEN SUM(M08)/SUM(A08) *100 ELSE 0 END AS \"８月\"");
			sbSQL.append(",CASE WHEN SUM(M09) > 0 THEN ROUND(SUM(M09)/SUM(A09) *100,1) ELSE 0 END AS \"９月\",CASE WHEN SUM(M10) > 0 THEN SUM(M10)/SUM(A10) *100 ELSE 0 END AS \"１０月\"");
			sbSQL.append(",CASE WHEN SUM(M11) > 0 THEN ROUND(SUM(M11)/SUM(A11) *100,1) ELSE 0 END AS \"１１月\",CASE WHEN SUM(M12) > 0 THEN SUM(M12)/SUM(A12) *100 ELSE 0 END AS \"１２月\"");
			sbSQL.append(",CASE WHEN SUM(M01)+SUM(M02)+SUM(M03)+SUM(M04)+SUM(M05)+SUM(M06) > 0 THEN ROUND((SUM(M01)+SUM(M02)+SUM(M03)+SUM(M04)+SUM(M05)+SUM(M06))/(SUM(A01)+SUM(A02)+SUM(A03)+SUM(A04)+SUM(A05)+SUM(A06)) *100,1) ELSE 0 END AS \"上期平均\"");
			sbSQL.append(",CASE WHEN SUM(M07)+SUM(M08)+SUM(M09)+SUM(M10)+SUM(M11)+SUM(M12) > 0 THEN ROUND((SUM(M07)+SUM(M08)+SUM(M09)+SUM(M10)+SUM(M11)+SUM(M12))/(SUM(A07)+SUM(A08)+SUM(A09)+SUM(A10)+SUM(A11)+SUM(A12)) *100,1) ELSE 0 END AS \"下期平均\"");
			sbSQL.append(",CASE WHEN SUM(M01)+SUM(M02)+SUM(M03)+SUM(M04)+SUM(M05)+SUM(M06)＋SUM(M07)+SUM(M08)+SUM(M09)+SUM(M10)+SUM(M11)+SUM(M12) > 0 THEN ");
			sbSQL.append(" ROUND((SUM(M01)+SUM(M02)+SUM(M03)+SUM(M04)+SUM(M05)+SUM(M06)+SUM(M07)+SUM(M08)+SUM(M09)+SUM(M10)+SUM(M11)+SUM(M12))/(SUM(A01)+SUM(A02)+SUM(A03)+SUM(A04)+SUM(A05)+SUM(A06)+SUM(A07)+SUM(A08)+SUM(A09)+SUM(A10)+SUM(A11)+SUM(A12)) *100,1) ELSE 0 END AS \"年平均\" ");
			sbSQL.append(" FROM WORKT,WORKALL WHERE YYYY = AYYYY GROUP BY YYYY ");
			sbSQL.append(" order by SORT");
		}
		System.out.println(sbSQL.toString());
		command = sbSQL.toString();
		return command;
	}


	/**
	 * 検索条件表示用情報の生成
	 */
	private void outputQueryList(){

		// 検索条件の加工クラス作成
		JsonArrayData jad = new JsonArrayData();
		jad.setJsonString(getJson());

		// 保存用 List (検索情報)作成
		setWhere(new ArrayList<List<String>>());

		// タイトル名称
		List<String> cells = new ArrayList<String>();
		cells.add( jad.getJSONText(DefineReport.ID_HIDDEN_REPORT_NAME) );
		getWhere().add(cells);

		// レポート更新日時
		cells = new ArrayList<String>();
		cells.add( "" );
		cells.add( "" );
		cells.add( "" );
		cells.add( "" );
		cells.add( jad.getJSONText(DefineReport.Label.CHECK.getObj()));
		getWhere().add(cells);

		// 表示条件
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.F2_JIKI.getView());
		cells.add( jad.getJSONText(DefineReport.Select.F2_JIKI.getObj()) );
		cells.add( DefineReport.Select.F_SHOHIN.getView());
		cells.add( jad.getJSONText(DefineReport.Select.F_SHOHIN.getObj()) );
		getWhere().add(cells);

		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);

	}

}
