package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T262テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T262Dao extends ItemDao {

	/**
	 * 新しいT262Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T262Dao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");	// 期間年月
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル
		String szRad_T3 = getMap().get("RAD_T3");	// 表示対象-1:年齢５歳集計 2:１歳毎集計
		String szRad_T5 = getMap().get("RAD_T5");	// 表示項目-1:顧客数 2:シェア 4:ACT率
		String szRad_B2 = getMap().get("RAD_B2");	// 対比表示-1:前年比 2:前月比 3:なし

		// パラメータ確認
		//if ((szRad_T3 == null) || (szRad_T5 == null) || (szRad_B2 == null)) {
		//	System.out.println(super.getConditionLog());
		//	return "";
		//}
		System.out.println(super.getConditionLog());

		// 年月指定条件
		String whereYM = " YYYY_MM = '" + szYm + "'";
		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "' ";
		// 表示項目別条件
		String whereAct = "";
		if (StringUtils.equals(szChanel, DefineReport.OptionChanel.OPTION2.getVal())) {
			whereAct = " and ACTKBN <= 6 ";
		} else {
			whereAct = " and ACTKBN <= 12 ";
		}

		String itemN = "";
		String colWith = "";
		String colK = "";	// 顧客数
		String colA = "";	// ACT率
		if (StringUtils.equals(szRad_T3, DefineReport.RadT_03.ID1.getVal())) {	// 1:年齢５歳集計
			// ベースSELECT文
			itemN = ",N20M,N20A,N25A,N30A,N35A,N40A,N45A,N50A,N55A,N60A,N70A,N100";
			colWith += ",(N12+N13+N14+N15+N16+N17+N18+N19),(N20+N21+N22+N23+N24),(N25+N26+N27+N28+N29),(N30+N31+N32+N33+N34),(N35+N36+N37+N38+N39)";
			colWith += ",(N40+N41+N42+N43+N44),(N45+N46+N47+N48+N49),(N50+N51+N52+N53+N54),(N55+N56+N57+N58+N59)";
			colWith += ",(N60+N61+N62+N63+N64+N65+N66+N67+N68+N69)";
			colWith += ",(N70+N71+N72+N73+N74+N75+N76+N77+N78+N79+N80+N81+N82+N83+N84+N85+N86+N87+N88+N89+N90+N91+N92+N93+N94+N95+N96+N97+N98+N99)";
			colWith += ",N100";

			String[] items = StringUtils.split(StringUtils.substring(itemN, 1), ",");
			String[] tits = { "20歳未満", "20-24歳", "25-29歳", "30-34歳", "35-39歳", "40-44歳", "45-49歳", "50-54歳", "55-59歳", "60-69歳", "70歳以上", "不明" };
			for (int i = 0; i < items.length; i++) {
				String item = items[i];
				String txt = "as \"" + tits[i] + "\" ";
				colK += ",sum(" + item + ") " + txt;
				colA += ",case when sum(K." + item + ") = 0 then 0 else sum(A." + item + ") / sum(K." + item + ") * 100 end " + txt;
			}
		} else if (StringUtils.equals(szRad_T3, DefineReport.RadT_03.ID2.getVal())) {	//2:１歳毎集計
			// ベースSELECT文
			itemN += ",N12,N13,N14,N15,N16,N17,N18,N19,N20,N21,N22,N23,N24,N25,N26,N27,N28,N29,N30,N31,N32,N33,N34";
			itemN += ",N35,N36,N37,N38,N39,N40,N41,N42,N43,N44,N45,N46,N47,N48,N49,N50,N51,N52,N53,N54,N55,N56,N57,N58,N59,N60,N61,N62,N63,N64";
			itemN += ",N65,N66,N67,N68,N69,N70,N71,N72,N73,N74,N75,N76,N77,N78,N79,N80,N81,N82,N83,N84,N85,N86,N87,N88,N89,N90,N91,N92,N93,N94";
			itemN += ",N95,N96,N97,N98,N99,N100";
			colWith += itemN;
			String[] items = StringUtils.split(StringUtils.substring(itemN, 1), ",");
			for (String item:items) {
				String idx = StringUtils.replace(item, "N", "");
				String txt = "as \"" + idx + "歳\" ";
				if (StringUtils.equals(idx, "100")) {
					txt = "as \"不明\" ";
				}
				colK += ",sum(" + item + ") " + txt;
				colA += ",case when sum(K." + item + ") = 0 then 0 else sum(A." + item + ") / sum(K." + item + ") * 100 end " + txt;
			}
		}
		itemN += ",KALL";
		colWith += ",KYAKUSU_ALL";
		colK += ",sum(KALL) as \"合計\"";
		colA += ",case when sum(K.KALL) = 0 then 0 else sum(A.KALL) / sum(K.KALL) * 100 end as \"合計\"";

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID1.getVal())) {			// 1:顧客数
			sbSQL.append("with WORK(PREF_CD" + itemN + ") as (");
			sbSQL.append(" select PREF_CD" + colWith + " from T26_PREF_AGE");
			sbSQL.append(" where " + whereYM + whereCha + ")   ");
			sbSQL.append("select PREF_CD AS SORT,max(CHIKU_MEI) as \"都道府県\"" + colK);
			sbSQL.append("from WORK left outer join M_AREA on PREF_CD = CHIKU_CD ");
			sbSQL.append("group by PREF_CD,CHIKU_MEI ");
			sbSQL.append("union all select 'Z99' AS SORT,'合　　計' as \"都道府県\"" + colK);
			sbSQL.append("from WORK  ");
			sbSQL.append("order by SORT");
		} else if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID3.getVal())) {	// 3:ACT顧客数
			sbSQL.append("with WORK(PREF_CD" + itemN + ") as (");
			sbSQL.append(" select PREF_CD" + colWith + " from T26_PREF_AGE");
			sbSQL.append(" where " + whereYM + whereCha + whereAct + ")   ");
			sbSQL.append("select PREF_CD AS SORT,max(CHIKU_MEI) as \"都道府県\"" + colK);
			sbSQL.append("from WORK left outer join M_AREA on PREF_CD = CHIKU_CD ");
			sbSQL.append("group by PREF_CD,CHIKU_MEI ");
			sbSQL.append("union all select 'Z99' AS SORT,'合　　計' as \"都道府県\"" + colK);
			sbSQL.append("from WORK  ");
			sbSQL.append("order by SORT");
		} else if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID4.getVal())) {	// 4:ACT率
			sbSQL.append("with WORK(PREF_CD" + itemN + ") as (");
			sbSQL.append(" select PREF_CD" + colWith + " from T26_PREF_AGE");
			sbSQL.append(" where " + whereYM + whereCha + "),  ");
			sbSQL.append("ACT(PREF_CD" + itemN + ") as (");
			sbSQL.append(" select PREF_CD" + colWith + " from T26_PREF_AGE");
			sbSQL.append(" where " + whereYM + whereCha + whereAct + ")   ");
			sbSQL.append("select K.PREF_CD AS SORT,max(CHIKU_MEI) as \"都道府県\"" + colA);
			sbSQL.append(" from WORK K inner join ACT A on K.PREF_CD = A.PREF_CD ");
			sbSQL.append(" left outer join M_AREA on K.PREF_CD = CHIKU_CD ");
			sbSQL.append("group by K.PREF_CD,CHIKU_MEI ");
			sbSQL.append("union all select 'Z99' AS SORT,'合　　計' as \"都道府県\"" + colA);
			sbSQL.append(" from WORK K inner join ACT A on K.PREF_CD = A.PREF_CD ");
			sbSQL.append("order by SORT");
		}

		System.out.println(sbSQL.toString());
		return sbSQL.toString();
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

		List<String> cells = new ArrayList<String>();
		// 年月
		cells.add(DefineReport.Label.LABELT2.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);
		// 表示対象-1:年齢５歳集計 2:１歳毎集計
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_03.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_03.NAME.getObj()));
		getWhere().add(cells);
		// 表示項目-1:顧客数 3:ACT顧客数 4:ACT率
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_05.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_05.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);

	}

}
