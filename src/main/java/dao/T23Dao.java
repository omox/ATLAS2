package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T23テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T23Dao extends ItemDao {

	/**
	 * 新しいT23Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T23Dao(Connection source) {
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
		String szRad_B1 = getMap().get("RAD_B1");	// 照会データ指定-1:年別 2:月別
		String szYy1 = getMap().get("YYYY");		// 開始年
		String szYy2 = getMap().get("YYYY2");		// 終了年
		String szYm1 = getMap().get("YYYY_MM");		// 開始年月
		String szYm2 = getMap().get("YYYY_MM2");	// 終了年月
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル
		String szKaiin = getMap().get("KAIIN_KBN");	// 会員区分
		String szRad_T4 = getMap().get("RAD_T4");	// 顧客状態-2:ACT顧客 3:DM-ACT顧客
		String szRad_B2 = getMap().get("RAD_B2");	// 対比表示-1:前年比 2:前月比 3:なし


		// パラメータ確認
		if ((szRad_B1 == null) || (szRad_B2 == null) || (szRad_T4 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 条件設定
		String sqlZEN1 = "";
		String sqlZEN2 = "";
		String sqlZEN3 = "";
		String sqlZEN4 = "";
		String sqlZEN5 = "";
		if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {
			sqlZEN1 = ",CASE WHEN SUM(NEW_ZYY_KYAKUSU+REP_ZYY_KYAKUSU) = 0 THEN 0 ELSE SUM(ACT_NOW_KYAKUSU)/SUM(NEW_ZYY_KYAKUSU+REP_ZYY_KYAKUSU)*100 END AS \"前年比\"";
			sqlZEN2 = ",CASE WHEN SUM(NOW_KYAKUSU) = 0 or SUM(ZYY_KYAKUSU) = 0 then 0 else (SUM(ACT_NOW_KYAKUSU)/SUM(NOW_KYAKUSU)*100) - (SUM(ACT_ZYY_KYAKUSU)/SUM(ZYY_KYAKUSU)*100) end AS \"前年差\"";
			sqlZEN3 = ",CASE WHEN SUM(NEW_ZYY_KYAKUSU) = 0 THEN 0 ELSE SUM(NEW_NOW_KYAKUSU)/SUM(NEW_ZYY_KYAKUSU)*100 END AS \"前年比\"";
			sqlZEN4 = ",CASE WHEN SUM(REP_ZYY_KYAKUSU) = 0 THEN 0 ELSE SUM(REP_NOW_KYAKUSU)/SUM(REP_ZYY_KYAKUSU)*100 END AS \"前年比\"";
			sqlZEN5 = ",CASE WHEN SUM(ZYY_KYAKUSU) = 0 THEN 0 ELSE SUM(NOW_KYAKUSU)/SUM(ZYY_KYAKUSU)*100 END AS \"前年比\"";
		} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) {
			sqlZEN1 = ",CASE WHEN SUM(NEW_ZMM_KYAKUSU+REP_ZMM_KYAKUSU) = 0 THEN 0 ELSE SUM(ACT_NOW_KYAKUSU)/SUM(NEW_ZMM_KYAKUSU+REP_ZMM_KYAKUSU)*100 END AS \"前月比\"";
			sqlZEN2 = ",CASE WHEN SUM(NOW_KYAKUSU) = 0 or SUM(ZMM_KYAKUSU) = 0 then 0 else (SUM(ACT_NOW_KYAKUSU)/SUM(NOW_KYAKUSU)*100) - (SUM(ACT_ZMM_KYAKUSU)/SUM(ZMM_KYAKUSU)*100) end AS \"前月差\"";
			sqlZEN3 = ",CASE WHEN SUM(NEW_ZMM_KYAKUSU) = 0 THEN 0 ELSE SUM(NEW_NOW_KYAKUSU)/SUM(NEW_ZMM_KYAKUSU)*100 END AS \"前月比\"";
			sqlZEN4 = ",CASE WHEN SUM(REP_ZMM_KYAKUSU) = 0 THEN 0 ELSE SUM(REP_NOW_KYAKUSU)/SUM(REP_ZMM_KYAKUSU)*100 END AS \"前月比\"";
			sqlZEN5 = ",CASE WHEN SUM(ZMM_KYAKUSU) = 0 THEN 0 ELSE SUM(NOW_KYAKUSU)/SUM(ZMM_KYAKUSU)*100 END AS \"前月比\"";
		}

		String sqlWhen1 = "";
		String sqlWhen2 = "";
		String sqlWhen3 = "";
		String[] itemC = null;
		if (StringUtils.equals(szRad_T4, DefineReport.RadT_04.ID2.getVal())) {			// 顧客状態-2:ACT顧客
			sqlWhen1 = "ACTKBN <= 12";
			sqlWhen2 = "ACTKBN <= 12 AND NRKBN = '1'";
			sqlWhen3 = "ACTKBN <= 12 AND NRKBN = '2'";
			itemC = new String[] { "\"ACT顧客数\"", "\"ACT率\"", "\"F1顧客数\"", "\"F2以上顧客数\"", "\"総顧客数\"" };
		} else if (StringUtils.equals(szRad_T4, DefineReport.RadT_04.ID3.getVal())) {	// 顧客状態-3:DM-ACT顧客
			sqlWhen1 = "(NRKBN = '1' and ACTKBN <= 6) or (NRKBN = '2' and ACTKBN <= 12)";
			sqlWhen2 = "(NRKBN = '1' and ACTKBN <= 6)";
			sqlWhen3 = "(NRKBN = '2' and ACTKBN <= 12)";
			itemC = new String[] { "\"DM-ACT顧客数\"", "\"DM-ACT率\"", "\"F1顧客数\"", "\"F2以上顧客数\"", "\"総顧客数\"" };
		}

		// 年月指定
		String whereYM = "";
		String whereYMP = "";
		String colYM = "";
		String szCp1 = "";	// 全会員の集計判定用
		String szCp2 = "";	// 全会員の集計判定用
		if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID1.getVal())) {  // 年別
			whereYM = "(substr(YYYY_MM,1,4) between '" + szYy1 + "' and '" + szYy2 + "' ) and substr(YYYY_MM,5,2) = '12' ";
			colYM = "substr(YYYY_MM,1,4)";
			szCp1 = szYy1+"12";
			szCp2 = szYy2+"12";
			if(szCp1.compareTo("201500") < 0 && szCp2.compareTo("201500") > 0 && StringUtils.equals(szKaiin, DefineReport.OptionType8.OPTION1.getVal())) {
				// 2014年以前から2015年以降の全会員の場合
				whereYM  = "(substr(YYYY_MM,1,4) between '2015' and '" + szYy2 + "' ) and substr(YYYY_MM,5,2) = '12' ";
				whereYMP = "(substr(YYYY_MM,1,4) between '" + szYy1 + "' and '2014' ) and substr(YYYY_MM,5,2) = '12' ";
			}
		} else if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID2.getVal())) { //月別
			whereYM = "YYYY_MM >= '" + szYm1 + "' AND YYYY_MM <= '" + szYm2 + "' ";
			colYM = "YYYY_MM";
			szCp1 = szYm1;
			szCp2 = szYm2;
			if(szCp1.compareTo("201500") < 0 && szCp2.compareTo("201500") > 0 && StringUtils.equals(szKaiin, DefineReport.OptionType8.OPTION1.getVal())){
				// 2014年以前から2015年以降の全会員の場合
				whereYM  = "YYYY_MM >= '201501' AND YYYY_MM <= '" + szYm2 + "' ";
				whereYMP = "YYYY_MM >= '" + szYm1 + "' AND YYYY_MM <= '201412' ";
			}
		}
		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "'";

		// 会員種別条件
		String whereKai = "";
		String whereKaiP = "";
		if (StringUtils.equals(szKaiin, DefineReport.OptionType8.OPTION2.getVal())) {		//一般会員
			whereKai += " AND KAIIN_KBN = '0' ";
		}else if (StringUtils.equals(szKaiin, DefineReport.OptionType8.OPTION3.getVal())) {	//特別会員
			whereKai += " AND KAIIN_KBN in ('1','2','3','4')";
		}else if (StringUtils.equals(szKaiin, DefineReport.OptionType8.OPTION1.getVal())) {	//全体
			if (!"".equals(whereYMP)) {
				// 2014年以前から2015年以降の全会員の場合
				whereKai  += " AND KAIIN_KBN in ('10','20','30','40','50')";
				whereKaiP += " AND KAIIN_KBN in ('0','1','2','3','4')";
			} else if(szCp1.compareTo("201500") > 0 && szCp2.compareTo("201500") > 0) {	// 2015年以降
				whereKai += " AND KAIIN_KBN in ('10','20','30','40','50')";
			} else if (szCp1.compareTo("201500") < 0 && szCp2.compareTo("201500") < 0) {// 2014年以前
				whereKai += " AND KAIIN_KBN in ('0','1','2','3','4')";
			}
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION2.getVal())) {//	レギュラー
			whereKai += " AND KAIIN_KBN = '10' ";
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION3.getVal())) {//	ゴールド
			whereKai += " AND KAIIN_KBN = '20' ";
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION4.getVal())) {//	ゴールド
			whereKai += " AND KAIIN_KBN = '30' ";
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION5.getVal())) {//	プラチナ
			whereKai += " AND KAIIN_KBN = '40' ";
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION6.getVal())) {//	プレミアム
			whereKai += " AND KAIIN_KBN = '50' ";
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH WORK(YYYY_MM,ACT_NOW_KYAKUSU,NEW_NOW_KYAKUSU,REP_NOW_KYAKUSU,NOW_KYAKUSU");
		sbSQL.append(" ,ACT_ZYY_KYAKUSU,NEW_ZYY_KYAKUSU,REP_ZYY_KYAKUSU,ZYY_KYAKUSU");
		sbSQL.append(" ,ACT_ZMM_KYAKUSU,NEW_ZMM_KYAKUSU,REP_ZMM_KYAKUSU,ZMM_KYAKUSU) AS ( ");
		sbSQL.append(" SELECT " + colYM);
		sbSQL.append(" ,CASE WHEN " + sqlWhen1 + " THEN SUM(KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN " + sqlWhen2 + " THEN SUM(KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN " + sqlWhen3 + " THEN SUM(KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,SUM(KYAKUSU)");
		sbSQL.append(" ,CASE WHEN " + sqlWhen1 + " THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN " + sqlWhen2 + " THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN " + sqlWhen3 + " THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,SUM(ZYY_KYAKUSU)");
		sbSQL.append(" ,CASE WHEN " + sqlWhen1 + " THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN " + sqlWhen2 + " THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN " + sqlWhen3 + " THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,SUM(ZMM_KYAKUSU)");
		sbSQL.append(" FROM T23_ACT_KYAKU");
		sbSQL.append(" WHERE " + whereYM + whereCha + whereKai);
		sbSQL.append(" GROUP BY " + colYM + ",ACTKBN,NRKBN,DM_ACTKBN");
		if (!"".equals(whereYMP)) {
			// 2014年以前から2015年以降の全会員の場合
			sbSQL.append(" UNION ALL ");
			sbSQL.append(" SELECT " + colYM);
			sbSQL.append(" ,CASE WHEN " + sqlWhen1 + " THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN " + sqlWhen2 + " THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN " + sqlWhen3 + " THEN SUM(KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,SUM(KYAKUSU)");
			sbSQL.append(" ,CASE WHEN " + sqlWhen1 + " THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN " + sqlWhen2 + " THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN " + sqlWhen3 + " THEN SUM(ZYY_KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,SUM(ZYY_KYAKUSU)");
			sbSQL.append(" ,CASE WHEN " + sqlWhen1 + " THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN " + sqlWhen2 + " THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,CASE WHEN " + sqlWhen3 + " THEN SUM(ZMM_KYAKUSU) ELSE 0 END");
			sbSQL.append(" ,SUM(ZMM_KYAKUSU)");
			sbSQL.append(" FROM T23_ACT_KYAKU");
			sbSQL.append(" WHERE " + whereYMP + whereCha + whereKaiP);
			sbSQL.append(" GROUP BY " + colYM + ",ACTKBN,NRKBN,DM_ACTKBN");
		}
		sbSQL.append(") ");
		sbSQL.append("SELECT YYYY_MM AS SORT,YYYY_MM AS \"年月\"");
		sbSQL.append(" ,SUM(ACT_NOW_KYAKUSU) AS " + itemC[0] + sqlZEN1);
		sbSQL.append(" ,CASE WHEN SUM(NOW_KYAKUSU) = 0 THEN 0 ELSE SUM(ACT_NOW_KYAKUSU)/SUM(NOW_KYAKUSU)*100 END AS " + itemC[1] + sqlZEN2);
		sbSQL.append(" ,SUM(NEW_NOW_KYAKUSU) AS " + itemC[2] + sqlZEN3);
		sbSQL.append(" ,SUM(REP_NOW_KYAKUSU) AS " + itemC[3] + sqlZEN4);
		sbSQL.append(" ,SUM(NOW_KYAKUSU) AS " + itemC[4] + sqlZEN5);
		sbSQL.append(" FROM WORK");
		sbSQL.append(" GROUP BY YYYY_MM");
		sbSQL.append(" ORDER BY SORT");

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
		String YM_KIJUN_1 = "";
		String YM_KIJUN_2 = "";
		String val1 = "";
		String val2 = "";
		if (StringUtils.equals(radB1, DefineReport.RadB_1.ID1.getVal())) {
			YM_KIJUN_1 = jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj());
			YM_KIJUN_2 = jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj());
			val1 = YM_KIJUN_1.substring(0,4);
			val2 = YM_KIJUN_2.substring(0,4);
		} else if (StringUtils.equals(radB1, DefineReport.RadB_1.ID2.getVal())) {
			YM_KIJUN_1 = jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj());
			YM_KIJUN_2 = jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj());
			val1 = YM_KIJUN_1.substring(0,4) + YM_KIJUN_1.substring(5,7);
			val2 = YM_KIJUN_2.substring(0,4) + YM_KIJUN_2.substring(5,7);
		}
		cells.add(YM_KIJUN_1);
		cells.add("～");
		cells.add(YM_KIJUN_2);
		getWhere().add(cells);

		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);

		// ステージ/会員区分
		cells = new ArrayList<String>();
		if (val1.compareTo(DefineReport.base.KAIIN.getVal())<0 && val2.compareTo(DefineReport.base.KAIIN.getVal())<0){
			// ステージ切替（会員区分のみ）前
			cells.add( DefineReport.Select.KAIIN_KBN.getView());
			cells.add( jad.getJSONText(DefineReport.Select.KAIIN_KBN.getObj()) );
		} else if (val1.compareTo(DefineReport.base.STAGE.getVal())>0 && val2.compareTo(DefineReport.base.STAGE.getVal())>0) {
			// 会員区分参照終了後
			cells.add( DefineReport.Select.STAGE_KBN.getView());
			cells.add( jad.getJSONText(DefineReport.Select.STAGE_KBN.getObj()) );
		} else {
			// ステージ／会員区分 参照可能期間
			cells.add( DefineReport.Select.STAGE_KAIIN_KBN.getView());
			cells.add( jad.getJSONText(DefineReport.Select.STAGE_KAIIN_KBN.getObj()) );
		}
		getWhere().add(cells);

		// 顧客状態-2:ACT顧客 3:DM-ACT顧客
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_04.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_04.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
