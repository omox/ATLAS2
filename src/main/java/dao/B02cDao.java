package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * B02cテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B02cDao extends ItemDao {

	/**
	 * 新しいT02Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public B02cDao(Connection source) {
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
		String szYy = getMap().get("YYYY");		// 基準年From
		String szYy2 = getMap().get("YYYY2");	// 基準年To
		String szType = getMap().get("H_TYPE");	// 表示項目-1:レジ客数 2:リピート客数 3:新規客数 4:出荷売上 5:単価
		String szRad = getMap().get("R_TYPE");	// 表示対象-1:実績 2:前年比

		// パラメータ確認
		if (szYy == null)  {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		String sqlsum = " ";
		String sqladd = " ";
		if(StringUtils.equals(szType, "5")){  //単価
			if(StringUtils.equals(szRad, "1")){  //実績
				sqlsum = ",SUM(ROUND(M01)) AS \"１月\",SUM(ROUND(M02)) AS \"２月\",SUM(ROUND(M03)) AS \"３月\",SUM(ROUND(M04)) AS \"４月\",SUM(ROUND(M05)) AS \"５月\",SUM(ROUND(M06)) AS \"６月\",SUM(ROUND(M07)) AS \"７月\",SUM(ROUND(M08)) AS \"８月\",SUM(ROUND(M09)) AS \"９月\",SUM(ROUND(M10)) AS \"１０月\",SUM(ROUND(M11)) AS \"１１月\",SUM(ROUND(M12)) AS \"１２月\" ";
				sqladd += ",CASE WHEN SUM(CNT) = 0 THEN 0 ELSE ROUND(SUM(M01+M02+M03+M04+M05+M06+M07+M08+M09+M10+M11+M12)/SUM(CNT)) END AS \"平均\"";
			}else{								//前年比
				sqlsum = ",SUM(M01) AS \"１月\",SUM(M02) AS \"２月\",SUM(M03) AS \"３月\",SUM(M04) AS \"４月\",SUM(M05) AS \"５月\",SUM(M06) AS \"６月\",SUM(M07) AS \"７月\",SUM(M08) AS \"８月\",SUM(M09) AS \"９月\",SUM(M10) AS \"１０月\",SUM(M11) AS \"１１月\",SUM(M12) AS \"１２月\" ";
				sqladd += " CASE WHEN SUM(CNT) = 0 THEN 0 ELSE SUM(M01+M02+M03+M04+M05+M06+M07+M08+M09+M10+M11+M12)/SUM(CNT) END AS \"平均\"";
			}
		}else{								//客数。売上げ
			sqlsum = ",SUM(M01) AS \"１月\",SUM(M02) AS \"２月\",SUM(M03) AS \"３月\",SUM(M04) AS \"４月\",SUM(M05) AS \"５月\",SUM(M06) AS \"６月\",SUM(M07) AS \"７月\",SUM(M08) AS \"８月\",SUM(M09) AS \"９月\",SUM(M10) AS \"１０月\",SUM(M11) AS \"１１月\",SUM(M12) AS \"１２月\" ";
			sqladd = ",SUM(M01+M02+M03+M04+M05+M06+M07+M08+M09+M10+M11+M12) AS  \"合計\",";
			sqladd += " CASE WHEN SUM(CNT) = 0 THEN 0 ELSE SUM(M01+M02+M03+M04+M05+M06+M07+M08+M09+M10+M11+M12)/SUM(CNT) END AS \"平均\"";
		}
		// 年月設定
		String sqlWhere = " WHERE URI_YY = '"+szYy+"' AND (M01+M02+M03+M04+M05+M06+M07+M08+M09+M10+M11+M12) > 0 ";
		if(StringUtils.equals(szRad, "1")){  //実績
			if(StringUtils.equals(szType, "1")){  //
				sqlWhere += " and ITEM_CD = 'I11'";
			} else if(StringUtils.equals(szType, "2")){  //
				sqlWhere += " and ITEM_CD = 'I19'";
			} else if(StringUtils.equals(szType, "3")){  //
				sqlWhere += " and ITEM_CD = 'I17'";
			} else if(StringUtils.equals(szType, "4")){  //
				sqlWhere += " and ITEM_CD = 'I02'";
			} else if(StringUtils.equals(szType, "5")){  //
				sqlWhere += " and ITEM_CD = 'I09'";
			}
		}else{								//前年比
			sqladd = "";
			if(StringUtils.equals(szType, "1")){  //
				sqlWhere += " and ITEM_CD = 'I12'";
			} else if(StringUtils.equals(szType, "2")){  //
				sqlWhere += " and ITEM_CD = 'I20'";
			} else if(StringUtils.equals(szType, "3")){  //
				sqlWhere += " and ITEM_CD = 'I18'";
			} else if(StringUtils.equals(szType, "4")){  //
				sqlWhere += " and ITEM_CD = 'I05'";
			} else if(StringUtils.equals(szType, "5")){  //
				sqlWhere += " and ITEM_CD = 'I10'";
			}
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH WORK(TENPO,CNT1,CNT2,CNT3,CNT4,CNT5,CNT6,CNT7,CNT8,CNT9,CNT10,CNT11,CNT12) AS( ");
		sbSQL.append("SELECT  TENPO_CD,CASE WHEN SUM(M01) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M02) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M03) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M04) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M05) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M06) > 0 THEN  1 ELSE 0 END,");
		sbSQL.append("CASE WHEN SUM(M07) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M08) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M09) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M10) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M11) > 0 THEN  1 ELSE 0 END,CASE WHEN SUM(M12) > 0 THEN  1 ELSE 0 END");
		sbSQL.append(" FROM URI_TEN_MCODE WHERE URI_YY = '"+szYy+"' and (ITEM_CD = 'I02' OR ITEM_CD = 'I11')  GROUP BY TENPO_CD),");
		sbSQL.append("WORK2(TENPO2,CNT) AS (SELECT TENPO,CNT1+CNT2+CNT3+CNT4+CNT5+CNT6+CNT7+CNT8+CNT9+CNT10+CNT11+CNT12 FROM WORK)");
		sbSQL.append(" SELECT U.TENPO_CD AS SORT ,MAX(TENPO_NM) AS \"店舗\""+sqlsum);
		sbSQL.append(sqladd);
		sbSQL.append(" FROM URI_TEN_MCODE U INNER JOIN M_TENPO M ON U.TENPO_CD = M.TENPO_CD ");
		sbSQL.append("                      INNER JOIN WORK2  ON U.TENPO_CD = TENPO2 ");
		sbSQL.append(sqlWhere+" GROUP BY U.TENPO_CD ");
		sbSQL.append(" UNION ALL SELECT U.TENPO_CD AS SORT ,'全店舗計' AS \"店舗\""+sqlsum);
		sbSQL.append(sqladd);
		sbSQL.append(" FROM URI_TEN_MCODE U INNER JOIN WORK2  ON U.TENPO_CD = TENPO2 ");
		sbSQL.append(sqlWhere+" AND TENPO_CD = '   ' GROUP BY U.TENPO_CD ");
		sbSQL.append(" order by SORT");

		System.out.println(sbSQL.toString());
		return sbSQL.toString();
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

		// 基準年月
		List<String> cells = new ArrayList<String>();
		cells.add(DefineReport.Select.YY_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
		getWhere().add(cells);

		// 表示項目-1:レジ客数 2:リピート客数 3:新規客数 4:出荷売上 5:単価
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.H_TYPE.getView());
		cells.add(jad.getJSONText(DefineReport.Select.H_TYPE.getObj()));
		getWhere().add(cells);

		// 表示対象-1:実績 2:前年比
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadB_10.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_10.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
