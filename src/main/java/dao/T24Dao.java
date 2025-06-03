package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T24テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T24Dao extends ItemDao {
	/**
	 * 新しいT24Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T24Dao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");			// 期間-年月
		String szJucYm1 = getMap().get("JUCHU_YM1");	// 初回受注月-開始年月
		String szJucYm2 = getMap().get("JUCHU_YM2");	// 初回受注月-終了年月
		String szChanel = getMap().get("HAN_CHAN");		// 販売チャネル
		String szKaisu = getMap().get("KAISU");			// 回数
		String szRad_T6 = getMap().get("RAD_T6");		// 表示項目-1:顧客数 2:転換率
		String szRad_B2 = getMap().get("RAD_B2");		// 対比表示-1:前年比 2:前月比 3:なし

		// パラメータ確認
		if ((szRad_T6 == null) || (szRad_B2 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		String[] tits = new String[] { "当月", "経過１ヶ月", "２ヶ月", "３ヶ月", "４ヶ月", "５ヶ月", "６ヶ月", "７ヶ月", "８ヶ月", "９ヶ月", "１０ヶ月", "１１ヶ月", "１２ヶ月" };
		String itemC = "";		// SELECT項目(対比表示-なし)
		String itemCZY = "";	// SELECT項目(対比表示-前年差)
		String itemCZM = "";	// SELECT項目(対比表示-前月差)
		if (StringUtils.equals(szRad_T6, DefineReport.RadT_06.ID1.getVal())) {	// 顧客数
			for (int i = 0; i < tits.length; i++) {
				String col = "KYAKUSU_T" + StringUtils.leftPad(Integer.toString(i), 2, "0");
				String txt = " as \"" + tits[i] + "\" ";
				String item = "sum(" + col + ")";
				itemC += "," + item + txt;
				itemCZY += "," + item + txt + "," + item + " - sum(ZYY_" + col + ") as \"前年差\"";
				itemCZM += "," + item + txt + "," + item + " - sum(ZMM_" + col + ") as \"前月差\"";
			}
		} else {																// 転換率
			for (int i = 0; i < tits.length; i++) {
				String col = "KYAKUSU_T" + StringUtils.leftPad(Integer.toString(i), 2, "0");
				String txt = " as \"" + tits[i] + "\" ";
				String item = "case when max(KYAKUSU) = 0 then 0 else sum(" + col + ")/max(KYAKUSU)*100 end";
				itemC += "," + item + txt;
				itemCZY += "," + item + txt;
				itemCZY += "," + item + "- case when max(ZYY_KYAKUSU) = 0 then 0 else SUM(ZYY_" + col + ")/max(ZYY_KYAKUSU)*100 end as \"前年差\"";
				itemCZM += "," + item + txt;
				itemCZM += "," + item + "- case when max(ZMM_KYAKUSU) = 0 then 0 else SUM(ZMM_" + col + ")/max(ZMM_KYAKUSU)*100 end as \"前月差\"";
			}
		}
		// SELECT項目設定(対比表示別)
		String sqlItem = itemC;															// なし
		if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {			// 前年差
			sqlItem = itemCZY;
		} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) {	// 前月差
			sqlItem = itemCZM;
		}
		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("SELECT NF_JUCHU_YM AS SORT,NF_JUCHU_YM as \"初回購入年月\"");
		sbSQL.append(" ,MAX(KYAKUSU) as \"顧客数計\"" + sqlItem);
		sbSQL.append(" FROM T24_TENKAN12 WHERE YYYY_MM = '" + szYm + "' AND HAN_CHAN = '" + szChanel + "' ");
		sbSQL.append("  AND NF_JUCHU_YM between '" + szJucYm1 + "' and '" + szJucYm2 + "' AND KAISU >= " + szKaisu);
		sbSQL.append(" GROUP BY NF_JUCHU_YM");
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

		// 年月
		List<String> cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		getWhere().add(cells);
		// 初回受注月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.JUCHU.getView());
		cells.add(jad.getJSONText(DefineReport.Select.JUCHU.getObj()));
		cells.add("～");
		cells.add(jad.getJSONText(DefineReport.Select.JUCHU_TO.getObj()));
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);
		// 回数
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.KAISU2.getView());
		cells.add(jad.getJSONText(DefineReport.Select.KAISU2.getObj()) + DefineReport.Label.LABELT3.getView());
		getWhere().add(cells);
		// 表示項目-1:顧客数 2:転換率
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_06.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_06.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);

	}

}
