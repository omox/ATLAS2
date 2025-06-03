package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T253テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T253Dao extends ItemDao {

	/**
	 * 新しいT253Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T253Dao(Connection source) {
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
		String szYy = getMap().get("YYYY");			// 期間-年
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル
		String szJan = getMap().get("JANRU");		// 購入ジャンル
		String szRad_B2 = getMap().get("RAD_B2");	// 対比表示-1:比 3:なし
		// パラメータ確認
		if (szRad_B2 == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 対比表示別条件設定
		String sqlZEN1 = "";
		String sqlZEN2 = "";
		String sqlZEN3 = "";
		String sqlZEN4 = "";
		String sqlSumZ1 = "";
		String sqlSumZ2 = "";
		String sqlSumZ3 = "";
		String sqlSumZ4 = "";
		if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {
			sqlZEN1 = ",case when W1.ZYY_KYAKUSU = 0 then 0 else W1.KYAKUSU * 100 / W1.ZYY_KYAKUSU end as \"単月比\""
					+ ",case when W1.ZYY_KYAKUSU_RUI = 0 then 0 else W1.KYAKUSU_RUI * 100 / W1.ZYY_KYAKUSU_RUI end as \"累計比\"";
			sqlZEN2 = ",case when W2.ZYY_KYAKUSU = 0 then 0 else W2.KYAKUSU * 100 / W2.ZYY_KYAKUSU end as \"単月比 \""
					+ ",case when W2.ZYY_KYAKUSU_RUI = 0 then 0 else W2.KYAKUSU_RUI * 100 / W2.ZYY_KYAKUSU_RUI end as \"累計比 \"";
			sqlZEN3 = ",case when W3.ZYY_KYAKUSU = 0 then 0 else W3.KYAKUSU * 100 / W3.ZYY_KYAKUSU end as \"単月比  \""
					+ ",case when W3.ZYY_KYAKUSU_RUI = 0 then 0 else W3.KYAKUSU_RUI * 100 / W3.ZYY_KYAKUSU_RUI end as \"累計比  \"";
			sqlZEN4 = ",case when W4.ZYY_KYAKUSU = 0 then 0 else W4.KYAKUSU * 100 / W4.ZYY_KYAKUSU end as \"単月比   \""
					+ ",case when W4.ZYY_KYAKUSU_RUI = 0 then 0 else W4.KYAKUSU_RUI * 100 / W4.ZYY_KYAKUSU_RUI end as \"累計比   \"";
			sqlSumZ1 = ",case when sum(W1.ZYY_KYAKUSU) = 0 then 0 else sum(W1.KYAKUSU) * 100 / sum(W1.ZYY_KYAKUSU) end as \"単月比\""
					+ ",case when sum(W1.ZYY_KYAKUSU_RUI) = 0 then 0 else sum(W1.KYAKUSU_RUI) * 100 / sum(W1.ZYY_KYAKUSU_RUI) end as \"累計比\"";
			sqlSumZ2 = ",case when sum(W2.ZYY_KYAKUSU) = 0 then 0 else sum(W2.KYAKUSU) * 100 / sum(W2.ZYY_KYAKUSU) end as \"単月比 \""
					+ ",case when sum(W2.ZYY_KYAKUSU_RUI) = 0 then 0 else sum(W2.KYAKUSU_RUI) * 100 / sum(W2.ZYY_KYAKUSU_RUI) end as \"累計比 \"";
			sqlSumZ3 = ",case when sum(W3.ZYY_KYAKUSU) = 0 then 0 else sum(W3.KYAKUSU) * 100 / sum(W3.ZYY_KYAKUSU) end as \"単月比  \""
					+ ",case when sum(W3.ZYY_KYAKUSU_RUI) = 0 then 0 else sum(W3.KYAKUSU_RUI) * 100 / sum(W3.ZYY_KYAKUSU_RUI) end as \"累計比  \"";
			sqlSumZ4 = ",case when sum(W4.ZYY_KYAKUSU) = 0 then 0 else sum(W4.KYAKUSU) * 100 / sum(W4.ZYY_KYAKUSU) end as \"単月比   \""
					+ ",case when sum(W4.ZYY_KYAKUSU_RUI) = 0 then 0 else sum(W4.KYAKUSU_RUI) * 100 / sum(W4.ZYY_KYAKUSU_RUI) end as \"累計比   \"";
		}

		// 年月指定条件
		String whereYM = " and substr(YYYY_MM,1,4) = '" + szYy + "'";
		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "' ";
		// 購入ジャンル条件
		String whereJan = " and JUNLE1 = '" + szJan.substring(0, 2) + "' and JUNLE2 = '" + szJan.substring(2, 4) + "' ";

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH WORK(YYYY_MM,KYAKUSU,KYAKUSU_RUI,ZYY_KYAKUSU,ZYY_KYAKUSU_RUI) AS (");
		sbSQL.append(" SELECT YYYY_MM,SUM(KYAKUSU),SUM(KYAKUSU_RUI),SUM(ZYY_KYAKUSU),SUM(ZYY_KYAKUSU_RUI)");
		sbSQL.append(" FROM T25_SHOHIN_4Q WHERE KBN_NEN = '1'" + whereYM + whereCha + whereJan + " GROUP BY YYYY_MM),  ");
		sbSQL.append("WORK2(YYYY_MM,KYAKUSU,KYAKUSU_RUI,ZYY_KYAKUSU,ZYY_KYAKUSU_RUI) AS (");
		sbSQL.append(" SELECT YYYY_MM,SUM(KYAKUSU),SUM(KYAKUSU_RUI),SUM(ZYY_KYAKUSU),SUM(ZYY_KYAKUSU_RUI)");
		sbSQL.append(" FROM T25_SHOHIN_4Q WHERE KBN_NEN = '2' AND KBN_4HAN = '2'" + whereYM + whereCha + whereJan + " GROUP BY YYYY_MM),  ");
		sbSQL.append("WORK3(YYYY_MM,KYAKUSU,KYAKUSU_RUI,ZYY_KYAKUSU,ZYY_KYAKUSU_RUI) AS (");
		sbSQL.append(" SELECT YYYY_MM,SUM(KYAKUSU),SUM(KYAKUSU_RUI),SUM(ZYY_KYAKUSU),SUM(ZYY_KYAKUSU_RUI)");
		sbSQL.append(" FROM T25_SHOHIN_4Q WHERE KBN_NEN = '2' AND KBN_4HAN = '3'" + whereYM + whereCha + whereJan + " GROUP BY YYYY_MM),  ");
		sbSQL.append("WORK4(YYYY_MM,KYAKUSU,KYAKUSU_RUI,ZYY_KYAKUSU,ZYY_KYAKUSU_RUI) AS (");
		sbSQL.append("SELECT YYYY_MM,SUM(KYAKUSU),SUM(KYAKUSU_RUI),SUM(ZYY_KYAKUSU),SUM(ZYY_KYAKUSU_RUI)");
		sbSQL.append(" FROM T25_SHOHIN_4Q WHERE KBN_NEN = '2' AND KBN_4HAN = '4'" + whereYM + whereCha + whereJan + " GROUP BY YYYY_MM)   ");
		sbSQL.append("select T.KIJUN_YM as \"SORT\",T.KIJUN_YM_MEI as \"受注年月\"");
		sbSQL.append(" ,W1.KYAKUSU as \"顧客数\",W1.KYAKUSU_RUI as \"累計\"" + sqlZEN1);
		sbSQL.append(" ,W2.KYAKUSU as \"2/4 期顧客数\",W2.KYAKUSU_RUI as \"2/4 期累計\"" + sqlZEN2);
		sbSQL.append(" ,W3.KYAKUSU as \"3/4 期顧客数\",W3.KYAKUSU_RUI as \"3/4 期累計\"" + sqlZEN3);
		sbSQL.append(" ,W4.KYAKUSU as \"4/4 期顧客数\",W4.KYAKUSU_RUI as \"4/4 期累計\"" + sqlZEN4);
		sbSQL.append(" from YM_KIJUN_T253 T");
		sbSQL.append(" left outer join WORK  W1 on T.KIJUN_YM = W1.YYYY_MM");
		sbSQL.append(" left outer join WORK2 W2 on T.KIJUN_YM = W2.YYYY_MM");
		sbSQL.append(" left outer join WORK3 W3 on T.KIJUN_YM = W3.YYYY_MM");
		sbSQL.append(" left outer join WORK4 W4 on T.KIJUN_YM = W4.YYYY_MM");
		sbSQL.append(" where substr(T.KIJUN_YM,1,4) = '" + szYy + "'");
		sbSQL.append("  union all  ");
		sbSQL.append("select '' as \"SORT\",'合計' as \"受注年月\"");
		sbSQL.append(" ,sum(W1.KYAKUSU) as \"顧客数\",0 as \"累計\"" + sqlSumZ1);
		sbSQL.append(" ,sum(W2.KYAKUSU) as \"2/4 期顧客数\",0 as \"2/4 期累計\"" + sqlSumZ2);
		sbSQL.append(" ,sum(W3.KYAKUSU) as \"3/4 期顧客数\",0 as \"3/4 期累計\"" + sqlSumZ3);
		sbSQL.append(" ,sum(W4.KYAKUSU) as \"4/4 期顧客数\",0 as \"4/4 期累計\"" + sqlSumZ4);
		sbSQL.append(" from WORK W1");
		sbSQL.append(" left outer join WORK2 W2 on W1.YYYY_MM = W2.YYYY_MM");
		sbSQL.append(" left outer join WORK3 W3 on W1.YYYY_MM = W3.YYYY_MM");
		sbSQL.append(" left outer join WORK4 W4 on W1.YYYY_MM = W4.YYYY_MM");
		sbSQL.append(" order by \"SORT\"");

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

		// 年月
		List<String> cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);
		// 購入ジャンル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.JANRU.getView());
		cells.add(jad.getJSONText(DefineReport.Select.JANRU.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
