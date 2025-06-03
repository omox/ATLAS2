package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.DefineReport.RadT_01;
import common.JsonArrayData;

/**
 * T22テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T22Dao extends ItemDao {

	/**
	 * 新しいT22Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public T22Dao(Connection source) {
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
		String szYy1 = getMap().get("YYYY");			// 開始年
		String szYy2 = getMap().get("YYYY2");			// 終了年
		String szYm1 = getMap().get("YYYY_MM");			// 開始年月
		String szYm2 = getMap().get("YYYY_MM2");		// 終了年月
		String szChanel = getMap().get("HAN_CHAN");		// 販売チャネル
		String szRadSt = getMap().get("RADST");	// ステージ／会員区分 0:ステージ 1:会員区分
		String szRad_B1 = getMap().get("RAD_B1");		// 照会データ指定-1:年別 2:月別
		String szRad_B2 = getMap().get("RAD_B2");		// 対比表示-1:前年比 2:前月比 3:なし
		String szRad_T1 = getMap().get("RAD_T1");		// Ｒ値指定-1:Ｒ３以内 2:Ｒ６以内 3:Ｒ９以内 4:Ｒ１２以内

		// パラメータ確認
		if ((szRad_B1 == null) || (szRad_B2 == null) ) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 年月指定
		String whereYM = "";
		String colYM = "";
		if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID1.getVal())) {  // 年別
			whereYM = "(substr(YYYY_MM,1,4) between '" + szYy1 + "' and '" + szYy2 + "' ) and substr(YYYY_MM,5,2) = '12' ";
			colYM = "substr(YYYY_MM,1,4)";
		} else if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID2.getVal())) { //月別
			whereYM = "YYYY_MM >= '" + szYm1 + "' AND YYYY_MM <= '" + szYm2 + "' ";
			colYM = "YYYY_MM";
		}
		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "'";
		// ACT区分
		String sqlWhen = "";
		for (RadT_01 rad : RadT_01.values()) {
			if (StringUtils.equals(szRad_T1, rad.getVal())) {
				sqlWhen = "ACTKBN <= " + rad.getDbVal();
				break;
			}
		}

		StringBuffer sbSQL = new StringBuffer();
		if (StringUtils.equals(szRadSt, DefineReport.RadT_10.ID2.getVal())) {	// 会員区分

			String sqlZEN1 = "";
			String sqlZEN2 = "";
			String sqlZEN3 = "";
			String sqlZEN1_0 = "";
			String sqlZEN2_0 = "";
			String sqlZEN3_0 = "";
			String sqlZEN1_1 = "";
			String sqlZEN2_1 = "";
			String sqlZEN3_1 = "";

			if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {  //前年
				sqlZEN1 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL)/SUM(ZYY_KYAKUSU_ALL)*100,1) END AS \"前年比\"";
				sqlZEN2 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(ZYY_KYAKUSU_ACT)*100,1) END AS \"前年比\"";
				sqlZEN3 = ",CASE WHEN SUM(KYAKUSU_ALL)= 0 OR SUM(ZYY_KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(KYAKUSU_ALL)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT)/SUM(ZYY_KYAKUSU_ALL)*100,1) END AS \"前年差\"";
				sqlZEN1_0 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL0) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL0)/SUM(ZYY_KYAKUSU_ALL0)*100,1) END AS \"前年比\"";
				sqlZEN2_0 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT0) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT0)/SUM(ZYY_KYAKUSU_ACT0)*100,1) END AS \"前年比\"";
				sqlZEN3_0 = ",CASE WHEN SUM(KYAKUSU_ALL0)= 0 OR SUM(ZYY_KYAKUSU_ALL0) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT0)/SUM(KYAKUSU_ALL0)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT0)/SUM(ZYY_KYAKUSU_ALL0)*100,1) END AS \"前年差\"";
				sqlZEN1_1 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL1) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL1)/SUM(ZYY_KYAKUSU_ALL1)*100,1) END AS \"前年比\"";
				sqlZEN2_1 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT1) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT1)/SUM(ZYY_KYAKUSU_ACT1)*100,1) END AS \"前年比\"";
				sqlZEN3_1 = ",CASE WHEN SUM(KYAKUSU_ALL1)= 0 OR SUM(ZYY_KYAKUSU_ALL1) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT1)/SUM(KYAKUSU_ALL1)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT1)/SUM(ZYY_KYAKUSU_ALL1)*100,1) END AS \"前年差\"";
			} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) {   //前月
				sqlZEN1 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL)/SUM(ZMM_KYAKUSU_ALL)*100,1) END AS \"前月比\"";
				sqlZEN2 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(ZMM_KYAKUSU_ACT)*100,1) END AS \"前月比\"";
				sqlZEN3 = ",CASE WHEN SUM(KYAKUSU_ALL)= 0 OR SUM(ZMM_KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(KYAKUSU_ALL)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT)/SUM(ZMM_KYAKUSU_ALL)*100,1) END AS \"前月差\"";
				sqlZEN1_0 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL0) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL0)/SUM(ZMM_KYAKUSU_ALL0)*100,1) END AS \"前月比\"";
				sqlZEN2_0 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT0) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT0)/SUM(ZMM_KYAKUSU_ACT0)*100,1) END AS \"前月比\"";
				sqlZEN3_0 = ",CASE WHEN SUM(KYAKUSU_ALL0)= 0 OR SUM(ZMM_KYAKUSU_ALL0) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT0)/SUM(KYAKUSU_ALL0)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT0)/SUM(ZMM_KYAKUSU_ALL0)*100,1) END AS \"前月差\"";
				sqlZEN1_1 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL1) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL1)/SUM(ZMM_KYAKUSU_ALL1)*100,1) END AS \"前月比\"";
				sqlZEN2_1 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT1) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT1)/SUM(ZMM_KYAKUSU_ACT1)*100,1) END AS \"前月比\"";
				sqlZEN3_1 = ",CASE WHEN SUM(KYAKUSU_ALL1)= 0 OR SUM(ZMM_KYAKUSU_ALL1) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT1)/SUM(KYAKUSU_ALL1)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT1)/SUM(ZMM_KYAKUSU_ALL1)*100,1) END AS \"前月差\"";
			}

			sbSQL.append("WITH WORK(YYYY_MM,KYAKUSU_ALL,KYAKUSU_ACT,ZMM_KYAKUSU_ALL,ZMM_KYAKUSU_ACT,ZYY_KYAKUSU_ALL,ZYY_KYAKUSU_ACT, ");
			sbSQL.append("KYAKUSU_ALL0,KYAKUSU_ACT0,ZMM_KYAKUSU_ALL0,ZMM_KYAKUSU_ACT0,ZYY_KYAKUSU_ALL0,ZYY_KYAKUSU_ACT0,");
			sbSQL.append("KYAKUSU_ALL1,KYAKUSU_ACT1,ZMM_KYAKUSU_ALL1,ZMM_KYAKUSU_ACT1,ZYY_KYAKUSU_ALL1,ZYY_KYAKUSU_ACT1 ) AS ( ");
			sbSQL.append(" SELECT " + colYM);
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('0','1','2','3','4') THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('0','1','2','3','4') THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('0','1','2','3','4') THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('0','1','2','3','4') THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('0','1','2','3','4') THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('0','1','2','3','4') THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '0' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '0' THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '0' THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '0' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '0' THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '0' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('1','2','3','4') THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('1','2','3','4') THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('1','2','3','4') THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('1','2','3','4') THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('1','2','3','4') THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('1','2','3','4') THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append(" FROM T22_KYAKU_R");
			sbSQL.append(" WHERE " + whereYM + whereCha + " AND KAIIN_KBN in ('0','1','2','3','4')" );
			sbSQL.append(" GROUP BY " + colYM + ",ACTKBN,KAIIN_KBN)   ");
			sbSQL.append("SELECT YYYY_MM AS SORT,YYYY_MM as 年月");
			sbSQL.append("  ,SUM(KYAKUSU_ALL0) as 総顧客数" + sqlZEN1_0+",SUM(KYAKUSU_ACT0) as R値別数" + sqlZEN2_0);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL0) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT0)/SUM(KYAKUSU_ALL0)*100,1) END AS 比率" + sqlZEN3_0);
			sbSQL.append("  ,SUM(KYAKUSU_ALL1) as 総顧客数" + sqlZEN1_1+",SUM(KYAKUSU_ACT1) as R値別数" + sqlZEN2_1);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL1) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT1)/SUM(KYAKUSU_ALL1)*100,1) END AS 比率" + sqlZEN3_1);
			sbSQL.append("  ,SUM(KYAKUSU_ALL) as 総顧客数" + sqlZEN1);
			sbSQL.append("  ,SUM(KYAKUSU_ACT) as R値別数 " + sqlZEN2);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(KYAKUSU_ALL)*100,1) END AS 比率 " + sqlZEN3);
			sbSQL.append(" FROM WORK GROUP BY YYYY_MM ORDER BY SORT");
		} else {	// ステージ

			String sqlZEN1 = "";
			String sqlZEN2 = "";
			String sqlZEN3 = "";
			String sqlZEN1_10 = "";
			String sqlZEN2_10 = "";
			String sqlZEN3_10 = "";
			String sqlZEN1_20 = "";
			String sqlZEN2_20 = "";
			String sqlZEN3_20 = "";
			String sqlZEN1_30 = "";
			String sqlZEN2_30 = "";
			String sqlZEN3_30 = "";
			String sqlZEN1_40 = "";
			String sqlZEN2_40 = "";
			String sqlZEN3_40 = "";
			String sqlZEN1_50 = "";
			String sqlZEN2_50 = "";
			String sqlZEN3_50 = "";

			if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {  //前年
				sqlZEN1 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL)/SUM(ZYY_KYAKUSU_ALL)*100,1) END AS \"前年比\"";
				sqlZEN2 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(ZYY_KYAKUSU_ACT)*100,1) END AS \"前年比\"";
				sqlZEN3 = ",CASE WHEN SUM(KYAKUSU_ALL)= 0 OR SUM(ZYY_KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(KYAKUSU_ALL)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT)/SUM(ZYY_KYAKUSU_ALL)*100,1) END AS \"前年差\"";
				sqlZEN1_10 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL10) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL10)/SUM(ZYY_KYAKUSU_ALL10)*100,1) END AS \"前年比\"";
				sqlZEN2_10 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT10) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT10)/SUM(ZYY_KYAKUSU_ACT10)*100,1) END AS \"前年比\"";
				sqlZEN3_10 = ",CASE WHEN SUM(KYAKUSU_ALL10)= 0 OR SUM(ZYY_KYAKUSU_ALL10) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT10)/SUM(KYAKUSU_ALL10)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT10)/SUM(ZYY_KYAKUSU_ALL10)*100,1) END AS \"前年差\"";

				sqlZEN1_20 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL20) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL20)/SUM(ZYY_KYAKUSU_ALL20)*100,1) END AS \"前年比\"";
				sqlZEN2_20 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT20) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT20)/SUM(ZYY_KYAKUSU_ACT20)*100,1) END AS \"前年比\"";
				sqlZEN3_20 = ",CASE WHEN SUM(KYAKUSU_ALL20)= 0 OR SUM(ZYY_KYAKUSU_ALL20) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT20)/SUM(KYAKUSU_ALL20)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT20)/SUM(ZYY_KYAKUSU_ALL20)*100,1) END AS \"前年差\"";

				sqlZEN1_30 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL30) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL30)/SUM(ZYY_KYAKUSU_ALL30)*100,1) END AS \"前年比\"";
				sqlZEN2_30 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT30) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT30)/SUM(ZYY_KYAKUSU_ACT30)*100,1) END AS \"前年比\"";
				sqlZEN3_30 = ",CASE WHEN SUM(KYAKUSU_ALL30)= 0 OR SUM(ZYY_KYAKUSU_ALL30) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT30)/SUM(KYAKUSU_ALL30)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT30)/SUM(ZYY_KYAKUSU_ALL30)*100,1) END AS \"前年差\"";

				sqlZEN1_40 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL40) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL40)/SUM(ZYY_KYAKUSU_ALL40)*100,1) END AS \"前年比\"";
				sqlZEN2_40 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT40) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT40)/SUM(ZYY_KYAKUSU_ACT40)*100,1) END AS \"前年比\"";
				sqlZEN3_40 = ",CASE WHEN SUM(KYAKUSU_ALL40)= 0 OR SUM(ZYY_KYAKUSU_ALL40) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT40)/SUM(KYAKUSU_ALL40)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT40)/SUM(ZYY_KYAKUSU_ALL40)*100,1) END AS \"前年差\"";

				sqlZEN1_50 = ",CASE WHEN SUM(ZYY_KYAKUSU_ALL50) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL50)/SUM(ZYY_KYAKUSU_ALL50)*100,1) END AS \"前年比\"";
				sqlZEN2_50 = ",CASE WHEN SUM(ZYY_KYAKUSU_ACT50) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT50)/SUM(ZYY_KYAKUSU_ACT50)*100,1) END AS \"前年比\"";
				sqlZEN3_50 = ",CASE WHEN SUM(KYAKUSU_ALL50)= 0 OR SUM(ZYY_KYAKUSU_ALL50) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT50)/SUM(KYAKUSU_ALL50)*100,1) - ROUND(SUM(ZYY_KYAKUSU_ACT50)/SUM(ZYY_KYAKUSU_ALL50)*100,1) END AS \"前年差\"";

			} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) {   //前月
				sqlZEN1 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL)/SUM(ZMM_KYAKUSU_ALL)*100,1) END AS \"前月比\"";
				sqlZEN2 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(ZMM_KYAKUSU_ACT)*100,1) END AS \"前月比\"";
				sqlZEN3 = ",CASE WHEN SUM(KYAKUSU_ALL)= 0 OR SUM(ZMM_KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(KYAKUSU_ALL)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT)/SUM(ZMM_KYAKUSU_ALL)*100,1) END AS \"前月差\"";
				sqlZEN1_10 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL10) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL10)/SUM(ZMM_KYAKUSU_ALL10)*100,1) END AS \"前月比\"";
				sqlZEN2_10 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT10) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT10)/SUM(ZMM_KYAKUSU_ACT10)*100,1) END AS \"前月比\"";
				sqlZEN3_10 = ",CASE WHEN SUM(KYAKUSU_ALL10)= 0 OR SUM(ZMM_KYAKUSU_ALL10) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT10)/SUM(KYAKUSU_ALL10)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT10)/SUM(ZMM_KYAKUSU_ALL10)*100,1) END AS \"前月差\"";

				sqlZEN1_20 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL20) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL20)/SUM(ZMM_KYAKUSU_ALL20)*100,1) END AS \"前月比\"";
				sqlZEN2_20 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT20) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT20)/SUM(ZMM_KYAKUSU_ACT20)*100,1) END AS \"前月比\"";
				sqlZEN3_20 = ",CASE WHEN SUM(KYAKUSU_ALL20)= 0 OR SUM(ZMM_KYAKUSU_ALL20) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT20)/SUM(KYAKUSU_ALL20)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT20)/SUM(ZMM_KYAKUSU_ALL20)*100,1) END AS \"前月差\"";

				sqlZEN1_30 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL30) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL30)/SUM(ZMM_KYAKUSU_ALL30)*100,1) END AS \"前月比\"";
				sqlZEN2_30 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT30) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT30)/SUM(ZMM_KYAKUSU_ACT30)*100,1) END AS \"前月比\"";
				sqlZEN3_30 = ",CASE WHEN SUM(KYAKUSU_ALL30)= 0 OR SUM(ZMM_KYAKUSU_ALL30) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT30)/SUM(KYAKUSU_ALL30)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT30)/SUM(ZMM_KYAKUSU_ALL30)*100,1) END AS \"前月差\"";

				sqlZEN1_40 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL40) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL40)/SUM(ZMM_KYAKUSU_ALL40)*100,1) END AS \"前月比\"";
				sqlZEN2_40 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT40) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT40)/SUM(ZMM_KYAKUSU_ACT40)*100,1) END AS \"前月比\"";
				sqlZEN3_40 = ",CASE WHEN SUM(KYAKUSU_ALL40)= 0 OR SUM(ZMM_KYAKUSU_ALL40) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT40)/SUM(KYAKUSU_ALL40)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT40)/SUM(ZMM_KYAKUSU_ALL40)*100,1) END AS \"前月差\"";

				sqlZEN1_50 = ",CASE WHEN SUM(ZMM_KYAKUSU_ALL50) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ALL50)/SUM(ZMM_KYAKUSU_ALL50)*100,1) END AS \"前月比\"";
				sqlZEN2_50 = ",CASE WHEN SUM(ZMM_KYAKUSU_ACT50) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT50)/SUM(ZMM_KYAKUSU_ACT50)*100,1) END AS \"前月比\"";
				sqlZEN3_50 = ",CASE WHEN SUM(KYAKUSU_ALL50)= 0 OR SUM(ZMM_KYAKUSU_ALL50) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT50)/SUM(KYAKUSU_ALL50)*100,1) - ROUND(SUM(ZMM_KYAKUSU_ACT50)/SUM(ZMM_KYAKUSU_ALL50)*100,1) END AS \"前月差\"";
			}

			sbSQL.append("WITH WORK(YYYY_MM,KYAKUSU_ALL,KYAKUSU_ACT,ZMM_KYAKUSU_ALL,ZMM_KYAKUSU_ACT,ZYY_KYAKUSU_ALL,ZYY_KYAKUSU_ACT, ");
			sbSQL.append("KYAKUSU_ALL10,KYAKUSU_ACT10,ZMM_KYAKUSU_ALL10,ZMM_KYAKUSU_ACT10,ZYY_KYAKUSU_ALL10,ZYY_KYAKUSU_ACT10,");
			sbSQL.append("KYAKUSU_ALL20,KYAKUSU_ACT20,ZMM_KYAKUSU_ALL20,ZMM_KYAKUSU_ACT20,ZYY_KYAKUSU_ALL20,ZYY_KYAKUSU_ACT20,");
			sbSQL.append("KYAKUSU_ALL30,KYAKUSU_ACT30,ZMM_KYAKUSU_ALL30,ZMM_KYAKUSU_ACT30,ZYY_KYAKUSU_ALL30,ZYY_KYAKUSU_ACT30,");
			sbSQL.append("KYAKUSU_ALL40,KYAKUSU_ACT40,ZMM_KYAKUSU_ALL40,ZMM_KYAKUSU_ACT40,ZYY_KYAKUSU_ALL40,ZYY_KYAKUSU_ACT40,");
			sbSQL.append("KYAKUSU_ALL50,KYAKUSU_ACT50,ZMM_KYAKUSU_ALL50,ZMM_KYAKUSU_ACT50,ZYY_KYAKUSU_ALL50,ZYY_KYAKUSU_ACT50 ) AS ( ");
			sbSQL.append(" SELECT " + colYM);
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('10','20','30','40','50') THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('10','20','30','40','50') THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('10','20','30','40','50') THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('10','20','30','40','50') THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('10','20','30','40','50') THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN in ('10','20','30','40','50') THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '10' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '10' THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '10' THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '10' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '10' THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '10' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");

			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '20' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '20' THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '20' THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '20' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '20' THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '20' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");

			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '30' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '30' THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '30' THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '30' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '30' THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '30' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");

			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '40' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '40' THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '40' THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '40' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '40' THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '40' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");

			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '50' THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '50' THEN CASE WHEN " +sqlWhen+ " THEN SUM(KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '50' THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '50' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZMM_KYAKUSU) ELSE 0 END ELSE 0 END ");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '50' THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append("  ,CASE WHEN KAIIN_KBN = '50' THEN CASE WHEN " +sqlWhen+ " THEN SUM(ZYY_KYAKUSU) ELSE 0 END ELSE 0 END ");

			sbSQL.append(" FROM T22_KYAKU_R");
			sbSQL.append(" WHERE " + whereYM + whereCha + " AND KAIIN_KBN in ('10','20','30','40','50')" );
			sbSQL.append(" GROUP BY " + colYM + ",ACTKBN,KAIIN_KBN)   ");
			sbSQL.append("SELECT YYYY_MM AS SORT,YYYY_MM as 年月");
			sbSQL.append("  ,SUM(KYAKUSU_ALL10) as 総顧客数" + sqlZEN1_10+",SUM(KYAKUSU_ACT10) as R値別数" + sqlZEN2_10);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL10) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT10)/SUM(KYAKUSU_ALL10)*100,1) END AS 比率" + sqlZEN3_10);

			sbSQL.append("  ,SUM(KYAKUSU_ALL20) as 総顧客数" + sqlZEN1_20+",SUM(KYAKUSU_ACT20) as R値別数" + sqlZEN2_20);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL20) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT20)/SUM(KYAKUSU_ALL20)*100,1) END AS 比率" + sqlZEN3_20);

			sbSQL.append("  ,SUM(KYAKUSU_ALL30) as 総顧客数" + sqlZEN1_30+",SUM(KYAKUSU_ACT30) as R値別数" + sqlZEN2_30);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL30) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT30)/SUM(KYAKUSU_ALL30)*100,1) END AS 比率" + sqlZEN3_30);

			sbSQL.append("  ,SUM(KYAKUSU_ALL40) as 総顧客数" + sqlZEN1_40+",SUM(KYAKUSU_ACT40) as R値別数" + sqlZEN2_40);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL40) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT40)/SUM(KYAKUSU_ALL40)*100,1) END AS 比率" + sqlZEN3_40);

			sbSQL.append("  ,SUM(KYAKUSU_ALL50) as 総顧客数" + sqlZEN1_50+",SUM(KYAKUSU_ACT50) as R値別数" + sqlZEN2_50);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL50) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT50)/SUM(KYAKUSU_ALL50)*100,1) END AS 比率" + sqlZEN3_50);

			sbSQL.append("  ,SUM(KYAKUSU_ALL) as 総顧客数" + sqlZEN1);
			sbSQL.append("  ,SUM(KYAKUSU_ACT) as R値別数 " + sqlZEN2);
			sbSQL.append("  ,CASE WHEN SUM(KYAKUSU_ALL) = 0 THEN 0 ELSE ROUND(SUM(KYAKUSU_ACT)/SUM(KYAKUSU_ALL)*100,1) END AS 比率 " + sqlZEN3);
			sbSQL.append(" FROM WORK GROUP BY YYYY_MM ORDER BY SORT");
		}


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

		String radB1 = jad.getJSONValue(DefineReport.RadB_1.NAME.getObj());
		List<String> cells = new ArrayList<String>();
		// 照会データ指定
		cells.add(DefineReport.RadB_1.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_1.NAME.getObj()));
		getWhere().add(cells);
		// 年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		if (StringUtils.equals(radB1, DefineReport.RadB_1.ID1.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()));
		} else if (StringUtils.equals(radB1, DefineReport.RadB_1.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj()));
		}
		getWhere().add(cells);

		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		// 会員区分 0:ステージ 1:会員区分
		cells.add("");
		cells.add(jad.getJSONText(DefineReport.RadT_10.NAME.getObj()));
		getWhere().add(cells);
		// R値表示
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_01.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_01.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
