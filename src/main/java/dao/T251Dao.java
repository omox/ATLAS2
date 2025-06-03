package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T251テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T251Dao extends ItemDao {

	/**
	 * 新しいT251Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T251Dao(Connection source) {
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
		String szRad_B2 = getMap().get("RAD_B2");	// 対比表示-1:前年比 2:前月比 3:なし
		String szYy1 = getMap().get("YYYY");		// 開始年
		String szYy2 = getMap().get("YYYY2");		// 終了年
		String szYm1 = getMap().get("YYYY_MM");		// 開始年月
		String szYm2 = getMap().get("YYYY_MM2");	// 終了年月
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル

		// パラメータ確認
		if ((szRad_B1 == null) || (szRad_B2 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 対比表示別条件設定
		String sqlZEN1 = "";
		String sqlZEN2 = "";
		String sqlSumZ1 = "";
		String sqlSumZ2 = "";
		if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {
			sqlZEN1 = ",case when max(ZYY_KYAKUSU_ALL) = 0 then 0 else  max(KYAKUSU_ALL) / max(ZYY_KYAKUSU_ALL) * 100 end as \"前年比\"";
			sqlZEN2 = ",case when max(ZYY_KYAKUSU) = 0 then 0 else max(KYAKUSU) / max(ZYY_KYAKUSU) * 100 end as \"前年比 \"";
			sqlSumZ1 = ",case when sum(ZYY_KYAKUSU_ALL) = 0 then 0 else sum(KYAKUSU_ALL) / sum(ZYY_KYAKUSU_ALL) * 100 end as \"前年比\"";
			sqlSumZ2 = ",case when sum(ZYY_KYAKUSU) = 0 then 0 else sum(KYAKUSU) / sum(ZYY_KYAKUSU) * 100 end as \"前年比 \"";
		} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) {
			sqlZEN1 = ",case when max(ZMM_KYAKUSU_ALL) = 0 then 0 else max(KYAKUSU_ALL) / max(ZMM_KYAKUSU_ALL) * 100 end as \"前月比\"";
			sqlZEN2 = ",case when max(ZMM_KYAKUSU) = 0 then 0 else max(KYAKUSU) / max(ZMM_KYAKUSU) * 100 end as \"前月比 \"";
			sqlSumZ1 = ",case when sum(ZMM_KYAKUSU_ALL) = 0 then 0 else sum(KYAKUSU_ALL) / sum(ZMM_KYAKUSU_ALL) * 100 end as \"前月比\"";
			sqlSumZ2 = ",case when sum(ZMM_KYAKUSU) = 0 then 0 else sum(KYAKUSU) / sum(ZMM_KYAKUSU) * 100 end as \"前月比 \"";
		}

		// 照会データ別件設定
		String colYm = "";
		String whereYM = "";	// 年月指定条件
		if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID1.getVal())) {
			colYm = "substr(YYYY_MM, 1, 4)";
			whereYM = "KBN_NEN = '2' and YYYY_MM between '" + szYy1 + "01' and '" + szYy2 + "12' ";
		} else if (StringUtils.equals(szRad_B1, DefineReport.RadB_1.ID2.getVal())) {
			colYm = "YYYY_MM";
			whereYM = "KBN_NEN = '1' and YYYY_MM between '" + szYm1 + "' and '" + szYm2 + "' ";
		}
		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "' ";
		// 商品条件
		String whereSho = getWhereSho();

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("select " + colYm + " as SORT ," + colYm + " as \"受注年月\" ");
		sbSQL.append(" ,max(KYAKUSU_ALL) as \"総顧客数\"" + sqlZEN1);
		sbSQL.append(" ,max(KYAKUSU) as \"顧客数\"" + sqlZEN2);
		sbSQL.append(" ,case when max(KYAKUSU_ALL) = 0 then 0 else max(KYAKUSU) / max(KYAKUSU_ALL) * 100 end as \"全体比\"");
		sbSQL.append("from T25_SHOHIN_KYAKU ");
		sbSQL.append("where " + whereYM + whereCha + whereSho);
		sbSQL.append("group by " + colYm);
		sbSQL.append("   union all   ");
		sbSQL.append("select '' as SORT ,'合計' as \"受注年月\"");
		sbSQL.append(" ,sum(KYAKUSU_ALL) as \"総顧客数\"" + sqlSumZ1);
		sbSQL.append(" ,sum(KYAKUSU) as \"顧客数\"" + sqlSumZ2);
		sbSQL.append(" ,case when sum(KYAKUSU_ALL) = 0 then 0 else sum(KYAKUSU)/sum(KYAKUSU_ALL) * 100 end as \"全体比\"");
		sbSQL.append("from T25_SHOHIN_KYAKU ");
		sbSQL.append("where " + whereYM + whereCha + whereSho);
		sbSQL.append("order by SORT");

		System.out.println(sbSQL.toString());
		return sbSQL.toString();
	}

	/**
	 * 商品Where句を生成します。
	 *
	 * @return 商品Where句
	 */
	private String getWhereSho() {
		String szSho1 = getMap().get("SEL_SHO1");	// 商品分類-基本分類
		String szSho2 = getMap().get("SEL_SHO2");	// 商品分類-大分類
		String szSho3 = getMap().get("SEL_SHO3");	// 商品分類-中分類
		String szSho4 = getMap().get("SEL_SHO4");	// 商品分類-小分類
		String szSho5 = getMap().get("SEL_SHO5");	// 商品分類-シリーズＡ
		String szSho6 = getMap().get("SEL_SHO6");	// 商品分類-シリーズＢ
		String szItem = getMap().get("SEL_ITEM");	// 商品分類-商品

		String whereSho = "";
		if (!StringUtils.equals(szItem, "00")) { //
			whereSho = " and KBN = 10 and SBSHBUN = '" + szItem + "' ";
		} else if (!StringUtils.equals(szSho6, "00")) {	//
			whereSho = " and KBN = 6 and SBSHBUN = '" + szSho6 + "' ";
		} else if (!StringUtils.equals(szSho5, "00")) {	//
			whereSho = " and KBN = 5 and SBSHBUN = '" + szSho5 + "' ";
		} else if (!StringUtils.equals(szSho4, "00")) {	//
			whereSho = " and KBN = 4 and SBSHBUN = '" + szSho4 + "' ";
		} else if (!StringUtils.equals(szSho3, "00")) {	//
			whereSho = " and KBN = 3 and SBSHBUN = '" + szSho3 + "' ";
		} else if (!StringUtils.equals(szSho2, "00")) {	//
			whereSho = " and KBN = 2 and SBSHBUN = '" + szSho2 + "' ";
		} else if (!StringUtils.equals(szSho1, "00")) {	//
			whereSho = " and KBN = 1 and SBSHBUN = '" + szSho1 + "' ";
		}
		return whereSho;
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
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()));
		} else {
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj()));
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
		cells.add(DefineReport.Select.SHO_7.getView());
		cells.add("");
		getWhere().add(cells);
		cells = new ArrayList<String>();
		cells.add(jad.getJSONText(DefineReport.Select.SHO_1.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_2.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_3.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_4.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_5.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_6.getObj()));
		cells.add(jad.getJSONText(DefineReport.Select.SHO_7.getObj()));
		cells.add("");
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);

	}

}
