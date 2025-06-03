package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T07aテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T07bDao extends ItemDao {

	/**
	 * 新しいT07bDaoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public T07bDao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");				// （必須）基準年月
		String szChanel = getMap().get("HAN_CHAN");			// （必須）販売チャネル
		String szPromoFrom = getMap().get("PROMO_CD_FROM");	// （任意）プロモーションFROM
		String szPromoTo = getMap().get("PROMO_CD_TO");		// （任意）プロモーションTO
		String szSeg = getMap().get("SEGNO");				// （任意）条件番号
		String szShoCate1 = getMap().get("SHO_CATE1");		// （必須）表示
		String szShoCate2 = getMap().get("SHO_CATE2");		// （必須）金額表示選択

		// パラメータ確認
		if ((szYm == null) || (szChanel == null) || (szPromoFrom == null) || (szPromoTo == null) || (szShoCate1 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 販売チャネル設定
		String sqlTable = " T07_DM_SHOHIN_HAN T07, M_SBSHBUN_DM S01 ";
		if (!StringUtils.equals(szChanel, "1")) {
			sqlTable = " T07_DM_SHOHIN_TEN T07, M_SBSHBUN_DM S01 ";
		}
		// 年月条件設定
		String sqlWhere = " and YYYY_MM = '" + szYm + "'";
		// プロモーション条件設定
		if (!StringUtils.equals(szPromoFrom, "")) {
			sqlWhere += " and PROMO_CD between '" + szPromoFrom + "' and '" + szPromoTo + "'";
		}
		// 条件番号条件設定
		if (!StringUtils.equals(szSeg, "0")) {
			sqlWhere += " AND SEGNO between 1 and " + szSeg;
		}
		// 商品分類条件設定
		String sqlWhere2 = getWhereShohinBunrui("SHO_");

		// 数値項目の編集
		String sqlKn1 = "";
		String sqlKn2 = ",JUCGAKU";
		int num1 = Integer.parseInt(szSeg);
		if (StringUtils.equals(szShoCate2, "1")) { // 金額(税抜き
			for (int i = 1; i <= num1; i++) {
				sqlKn1 += ",COALESCE(SUM(CASE WHEN SEGNO = " + i + " THEN JUCGAKU END),0) as \"条件番号" + i + "\"";
			}
			sqlKn1 += ",COALESCE(SUM(JUCGAKU),0) as \"合　計\"";
		} else if (StringUtils.equals(szShoCate2, "2")) { // 金額(税込）
			sqlKn2 = ",TRUNC(JUCGAKU * (select RITSU FROM M_ZEIRITSU WHERE START_YM <= '" + szYm + "' AND END_YM >= '" + szYm + "'),-1)";
			for (int i = 1; i <= num1; i++) {
				sqlKn1 += ",COALESCE(SUM(CASE WHEN SEGNO = " + i + " THEN JUCGAKU END),0) as \"条件番号" + i + "\"";
			}
			sqlKn1 += ",COALESCE(SUM(JUCGAKU),0) as \"合　計\"";
		} else if (StringUtils.equals(szShoCate2, "3")) { // 客数
			for (int i = 1; i <= num1; i++) {
				sqlKn1 += ",COALESCE(SUM(CASE WHEN SEGNO = " + i + " THEN KYAKUSU_J END),0) as \"条件番号" + i + "\"";
			}
			sqlKn1 += ",COALESCE(SUM(KYAKUSU_J),0) as \"合　計\"";
		} else if (StringUtils.equals(szShoCate2, "4")) { // 数量
			for (int i = 1; i <= num1; i++) {
				sqlKn1 += ",COALESCE(SUM(CASE WHEN SEGNO = " + i + " THEN JUCSURYO END),0) as \"条件番号" + i + "\"";
			}
			sqlKn1 += ",COALESCE(SUM(JUCSURYO),0) as \"合　計\"";
		}

		// 商品選択時　全表示・実績のみ
		String fromAll = "";
		if (StringUtils.equals(szShoCate1, DefineReport.RadioSho1.ID1.getVal())) {
			fromAll = "from M_SBSHBUN_DM S01 left outer join T07 W ON trim(W.SBSHBUN) = trim(S01.SBSHBUN) where S01.KBN = 10 " + sqlWhere2;
		}

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("with T07(SBSHBUN,KBN,SEGNO,JUCSURYO,JUCGAKU,KYAKUSU_J) as (");
		sbSQL.append(" select T07.SBSHBUN,T07.KBN,SEGNO,JUCSURYO" + sqlKn2 + ",KYAKUSU_J from " + sqlTable);
		sbSQL.append(" where T07.SBSHBUN = S01.SBSHBUN and T07.KBN = S01.KBN " + sqlWhere + sqlWhere2 + ")   ");
		sbSQL.append(getSelectShohin(sqlKn1, "from T07 W, M_SBSHBUN_DM S01", fromAll, "CHECK_SHO"));
		sbSQL.append(" order by SORT ");

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
		cells.add(DefineReport.Select.YM_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		getWhere().add(cells);
		// 販売チャンネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);

		// プロモーションFrom-To
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.PROMO_FROM.getView());
		cells.add(jad.getJSONText(DefineReport.Select.PROMO_FROM.getObj()));
		cells.add("～");
		cells.add(jad.getJSONText(DefineReport.Select.PROMO_TO.getObj()));
		getWhere().add(cells);

		// 条件番号
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.SEGNO.getView());
		cells.add(jad.getJSONText(DefineReport.Select.SEGNO.getObj()));
		cells.add(DefineReport.Label.LABELB3.getView());
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
		cells.add(jad.getJSONText(DefineReport.RadioSho1.NAME.getObj()));
		cells.add("");
		getWhere().add(cells);

		// 表示データ
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadioSho2.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadioSho2.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
