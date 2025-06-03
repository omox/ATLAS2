package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T16テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T16Dao extends ItemDao {

	/**
	 * 新しいT16Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T16Dao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");				// 年月
		String szCheDM = getMap().get("CHECKDM");			// DM対象
		String szPromoFrom = getMap().get("PROMO_CD_FROM");	// プロモーションFROM
		String szPromoTo = getMap().get("PROMO_CD_TO");		// プロモーションTO
		String szRadSu = getMap().get("RADSU");				// 表示項目-1:顧客数 2:受注件数

		// パラメータ確認
		if (szYm == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());


		String sqlDM = " AND PROMO_CD = '0000'";			//
		if (StringUtils.equals(szCheDM, "off")) {
			sqlDM = " AND PROMO_CD >= '" + szPromoFrom + "' AND PROMO_CD <= '" + szPromoTo + "' ";
		}

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH MRANK(MRANK_G_CD,MRANK_G_NM) AS (SELECT MRANK_G_CD,MRANK_G_NM FROM M_MRANK2 GROUP BY MRANK_G_CD,MRANK_G_NM ),");
		sbSQL.append("WORK(LKONYU2,NEW_SU,NEW_GAK,REP_SU,REP_GAK) AS (");
		sbSQL.append("select LKONYU2,");
		sbSQL.append("       CASE WHEN NRKBN = 1 THEN SU ELSE 0 END,CASE WHEN NRKBN = 1 THEN JUCGK ELSE 0 END,");
		sbSQL.append("       CASE WHEN NRKBN = 2 THEN SU ELSE 0 END,CASE WHEN NRKBN = 2 THEN JUCGK ELSE 0 END ");
		sbSQL.append("  FROM T16_TEN WHERE KBN ='" +szRadSu+ "' " +sqlDM+ " AND YYYY_MM = '" +szYm+ "') ");
		sbSQL.append("SELECT MRANK_G_CD AS SORT,MRANK_G_NM,");
		sbSQL.append("       SUM(NEW_SU),CASE WHEN SUM(NEW_SU) = 0 THEN 0 ELSE ROUND(SUM(NEW_GAK)/SUM(NEW_SU)) END,  ");
		sbSQL.append("       SUM(REP_SU),CASE WHEN SUM(REP_SU) = 0 THEN 0 ELSE ROUND(SUM(REP_GAK)/SUM(REP_SU)) END,  ");
		sbSQL.append("       SUM(NEW_SU)+SUM(REP_SU),CASE WHEN (SUM(NEW_SU)+SUM(REP_SU)) = 0 THEN 0 ELSE ROUND((SUM(NEW_GAK)+SUM(REP_GAK))/(SUM(NEW_SU)+SUM(REP_SU))) END ");
		sbSQL.append(" FROM WORK , MRANK WHERE LKONYU2 = MRANK_G_CD ");
		sbSQL.append(" GROUP BY MRANK_G_CD,MRANK_G_NM ");
		sbSQL.append("UNION ALL ");
		sbSQL.append(" SELECT 'ZALL' AS SORT,'合計',");
		sbSQL.append("       SUM(NEW_SU),CASE WHEN SUM(NEW_SU) = 0 THEN 0 ELSE ROUND(SUM(NEW_GAK)/SUM(NEW_SU)) END,  ");
		sbSQL.append("       SUM(REP_SU),CASE WHEN SUM(REP_SU) = 0 THEN 0 ELSE ROUND(SUM(REP_GAK)/SUM(REP_SU)) END,  ");
		sbSQL.append("       SUM(NEW_SU)+SUM(REP_SU),CASE WHEN (SUM(NEW_SU)+SUM(REP_SU)) = 0 THEN 0 ELSE ROUND((SUM(NEW_GAK)+SUM(REP_GAK))/(SUM(NEW_SU)+SUM(REP_SU))) END ");
		sbSQL.append(" FROM WORK  ORDER BY SORT");

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
		cells.add(DefineReport.Select.YM_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		getWhere().add(cells);

		// DM対象
		String chkDm = jad.getJSONValue(DefineReport.CheckB4_4.ID1.getObj());
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT7.getView());
		if (StringUtils.equals(chkDm, "on")) {
			cells.add(jad.getJSONText(DefineReport.CheckB4_4.ID1.getObj()));
		}
		getWhere().add(cells);

		// プロモーションFrom-To
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.PROMO_FROM.getView());
		cells.add(jad.getJSONText(DefineReport.Select.PROMO_FROM.getObj()));
		cells.add("～");
		cells.add(jad.getJSONText(DefineReport.Select.PROMO_TO.getObj()));
		getWhere().add(cells);

		// 表示項目-1:顧客数 2:受注件数
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_08.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_08.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
