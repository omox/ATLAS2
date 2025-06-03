package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T252テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T252Dao extends ItemDao {

	/**
	 * 新しいT252Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T252Dao(Connection source) {
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
		String szRad_B1 = getMap().get("RAD_B1");		// 照会データ指定-1:年別 2:月別
		String szRad_T3 = getMap().get("RAD_T3");		// 表示対象-1:年齢５歳集計 2:１歳毎集計
		String szYy = getMap().get("YYYY");				// 開始年
		String szYm = getMap().get("YYYY_MM");			// 開始年月
		String szChanel = getMap().get("HAN_CHAN");		// 販売チャネル

		// パラメータ確認
		if ((szRad_B1 == null) || (szRad_T3 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());


		String prefix = "CHK_S";

		// 照会データ別件設定
		String whereYM = "";	// 年月指定条件
		if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID1.getVal())) {
			whereYM = " and KBN_NEN = '2' and YYYY_MM = '" + szYy + "12' ";
		} else if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID2.getVal())) {
			whereYM = " and KBN_NEN = '1' and YYYY_MM = '" + szYm + "' ";
		}
		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "' ";
		// 商品分類設定
		String sqlWhereSho = getWhereShohinBunrui("SEL_SHO");

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		// ベースFROM文
		String from = "from WORK W,M_SBSHBUN S01";
		if (StringUtils.equals(szRad_T3, DefineReport.RadT_03.ID1.getVal())) {	// 1:年齢５歳集計
			// ベースSELECT文
			String select = ",sum(KYAKUSU) as \"合計顧客数\""
					+ ",sum(N20M) as \"20歳未満\",sum(N20A) as \"20-24歳\",sum(N25A) as \"25-29歳\",sum(N30A) as \"30-34歳\""
					+ ",sum(N35A) as \"35-39歳\",sum(N40A) as \"40-44歳\",sum(N45A) as \"45-49歳\",sum(N50A) as \"50-54歳\""
					+ ",sum(N55A) as \"55-59歳\",sum(N60A) as \"60-69歳\",sum(N70A) as \"70歳以上\",sum(N100) as \"不明\"";
			sbSQL.append("with WORK(SBSHBUN,KBN,KYAKUSU,N20M,N20A,N25A,N30A,N35A,N40A,N45A,N50A,N55A,N60A,N70A,N100) as (");
			sbSQL.append(" select W.SBSHBUN,W.KBN,KYAKUSU_ALL");
			sbSQL.append("  ,(N12+N13+N14+N15+N16+N17+N18+N19)");
			sbSQL.append("  ,(N20+N21+N22+N23+N24),(N25+N26+N27+N28+N29),(N30+N31+N32+N33+N34),(N35+N36+N37+N38+N39)");
			sbSQL.append("  ,(N40+N41+N42+N43+N44),(N45+N46+N47+N48+N49),(N50+N51+N52+N53+N54),(N55+N56+N57+N58+N59)");
			sbSQL.append("  ,(N60+N61+N62+N63+N64+N65+N66+N67+N68+N69)");
			sbSQL.append("  ,(N70+N71+N72+N73+N74+N75+N76+N77+N78+N79+N80+N81+N82+N83+N84+N85+N86+N87+N88+N89+N90+N91+N92+N93+N94+N95+N96+N97+N98+N99)");
			sbSQL.append("  ,N100");
			sbSQL.append(" from T25_SHOHIN_AGE W,M_SBSHBUN S01");
			sbSQL.append(" where W.SBSHBUN = S01.SBSHBUN and W.KBN = S01.KBN" + whereYM + whereCha + sqlWhereSho + ")  ");
			sbSQL.append(getSelectShohin(select, from, "", prefix));
			sbSQL.append(" order by SORT ");
		} else if (StringUtils.equals(szRad_T3, DefineReport.RadT_03.ID2.getVal())) {	//2:１歳毎集計
			// ベースSELECT文
			String select = ",sum(KYAKUSU) as \"合計顧客数\""
					+ ",SUM(N12) as \"12歳\",SUM(N13) as \"13歳\",SUM(N14) as \"14歳\",SUM(N15) as \"15歳\",SUM(N16) as \"16歳\",SUM(N17) as \"17歳\""
					+ ",SUM(N18) as \"18歳\",SUM(N19) as \"19歳\",SUM(N20) as \"20歳\",SUM(N21) as \"21歳\",SUM(N22) as \"22歳\",SUM(N23) as \"23歳\""
					+ ",SUM(N24) as \"24歳\",SUM(N25) as \"25歳\",SUM(N26) as \"26歳\",SUM(N27) as \"27歳\",SUM(N28) as \"28歳\",SUM(N29) as \"29歳\""
					+ ",SUM(N30) as \"30歳\",SUM(N31) as \"31歳\",SUM(N32) as \"32歳\",SUM(N33) as \"33歳\",SUM(N34) as \"34歳\",SUM(N35) as \"35歳\""
					+ ",SUM(N36) as \"36歳\",SUM(N37) as \"37歳\",SUM(N38) as \"38歳\",SUM(N39) as \"39歳\",SUM(N40) as \"40歳\",SUM(N41) as \"41歳\""
					+ ",SUM(N42) as \"42歳\",SUM(N43) as \"43歳\",SUM(N44) as \"44歳\",SUM(N45) as \"45歳\",SUM(N46) as \"46歳\",SUM(N47) as \"47歳\""
					+ ",SUM(N48) as \"48歳\",SUM(N49) as \"49歳\",SUM(N50) as \"50歳\",SUM(N51) as \"51歳\",SUM(N52) as \"52歳\",SUM(N53) as \"53歳\""
					+ ",SUM(N54) as \"54歳\",SUM(N55) as \"55歳\",SUM(N56) as \"56歳\",SUM(N57) as \"57歳\",SUM(N58) as \"58歳\",SUM(N59) as \"59歳\""
					+ ",SUM(N60) as \"60歳\",SUM(N61) as \"61歳\",SUM(N62) as \"62歳\",SUM(N63) as \"63歳\",SUM(N64) as \"64歳\",SUM(N65) as \"65歳\""
					+ ",SUM(N66) as \"66歳\",SUM(N67) as \"67歳\",SUM(N68) as \"68歳\",SUM(N69) as \"69歳\",SUM(N70) as \"70歳\",SUM(N71) as \"71歳\""
					+ ",SUM(N72) as \"72歳\",SUM(N73) as \"73歳\",SUM(N74) as \"74歳\",SUM(N75) as \"75歳\",SUM(N76) as \"76歳\",SUM(N77) as \"77歳\""
					+ ",SUM(N78) as \"78歳\",SUM(N79) as \"79歳\",SUM(N80) as \"80歳\",SUM(N81) as \"81歳\",SUM(N82) as \"82歳\",SUM(N83) as \"83歳\""
					+ ",SUM(N84) as \"84歳\",SUM(N85) as \"85歳\",SUM(N86) as \"86歳\",SUM(N87) as \"87歳\",SUM(N88) as \"88歳\",SUM(N89) as \"89歳\""
					+ ",SUM(N90) as \"90歳\",SUM(N91) as \"91歳\",SUM(N92) as \"92歳\",SUM(N93) as \"93歳\",SUM(N94) as \"94歳\",SUM(N95) as \"95歳\""
					+ ",SUM(N96) as \"96歳\",SUM(N97) as \"97歳\",SUM(N98) as \"98歳\",SUM(N99) as \"99歳\",SUM(N100) as \"不明\"";
			sbSQL.append("with WORK(SBSHBUN,KBN,KYAKUSU,N12,N13,N14,N15,N16,N17,N18,N19,N20,N21,N22,N23,N24,N25,N26,N27,N28,N29,N30,N31,N32,N33,N34");
			sbSQL.append(" ,N35,N36,N37,N38,N39,N40,N41,N42,N43,N44,N45,N46,N47,N48,N49,N50,N51,N52,N53,N54,N55,N56,N57,N58,N59,N60,N61,N62,N63,N64");
			sbSQL.append(" ,N65,N66,N67,N68,N69,N70,N71,N72,N73,N74,N75,N76,N77,N78,N79,N80,N81,N82,N83,N84,N85,N86,N87,N88,N89,N90,N91,N92,N93,N94");
			sbSQL.append(" ,N95,N96,N97,N98,N99,N100) as (");
			sbSQL.append(" select W.SBSHBUN,W.KBN,KYAKUSU_ALL");
			sbSQL.append(" ,N12,N13,N14,N15,N16,N17,N18,N19,N20,N21,N22,N23,N24,N25,N26,N27,N28,N29,N30,N31,N32,N33,N34,N35,N36,N37,N38,N39,N40,N41");
			sbSQL.append(" ,N42,N43,N44,N45,N46,N47,N48,N49,N50,N51,N52,N53,N54,N55,N56,N57,N58,N59,N60,N61,N62,N63,N64,N65,N66,N67,N68,N69,N70,N71");
			sbSQL.append(" ,N72,N73,N74,N75,N76,N77,N78,N79,N80,N81,N82,N83,N84,N85,N86,N87,N88,N89,N90,N91,N92,N93,N94,N95,N96,N97,N98,N99,N100");
			sbSQL.append(" from T25_SHOHIN_AGE W,M_SBSHBUN S01");
			sbSQL.append(" where W.SBSHBUN = S01.SBSHBUN and W.KBN = S01.KBN" + whereYM + whereCha + sqlWhereSho + ")  ");
			sbSQL.append(getSelectShohin(select, from, "", prefix));
			sbSQL.append(" order by SORT ");
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


		List<String> cells = new ArrayList<String>();
		// 照会データ指定
		cells.add(DefineReport.RadB_1.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_1.NAME.getObj()));
		getWhere().add(cells);

		String radB1 = jad.getJSONValue(DefineReport.RadB_1.NAME.getObj());
		// 年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		if (StringUtils.equals(radB1, DefineReport.RadB_1.ID1.getObj())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
		} else {
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		}
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);

		// 商品分類
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.SHO_1.getView());
		cells.add(DefineReport.Select.SHO_2.getView());
		cells.add(DefineReport.Select.SHO_3.getView());
		cells.add(DefineReport.Select.SHO_4.getView());
		cells.add(DefineReport.Select.SHO_5.getView());
		cells.add(DefineReport.Select.SHO_6.getView());
		cells.add("");
		getWhere().add(cells);
		cells = new ArrayList<String>();
		cells.add(jad.getJSONText(DefineReport.Select.SHO_1.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_2.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_3.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_4.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_5.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_6.getObj()));
		cells.add("");
		getWhere().add(cells);
		// 共通箇所設定
		setCmnOutputQuery(jad);

	}

}
