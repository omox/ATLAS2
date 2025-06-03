package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T07aテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T07aDao extends ItemDao {

	/**
	 * 新しいT03SegDaoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public T07aDao(Connection source) {
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

		// 数値項目の編集
		String sqlKn = ",COALESCE(SUM(W.KYAKUSU_J),0) as 受注客数, COALESCE(SUM(W.JUCSURYO),0) as 受注数量, COALESCE(SUM(W.JUCGAKU),0) as 受注金額 ";
		if (StringUtils.equals(szShoCate2, DefineReport.RadioSho2.ID2.getVal())) {
			sqlKn = ",COALESCE(SUM(W.KYAKUSU_J),0) as 受注客数, COALESCE(SUM(W.JUCSURYO),0) as 受注数量,COALESCE(TRUNC(SUM(W.JUCGAKU) * (select RITSU FROM M_ZEIRITSU WHERE START_YM <= '"
					+ szYm + "' AND  END_YM >= '" + szYm + "'),-1),0) as 受注金額 ";
		}

		// 販売チャネル設定
		String sqlTable = " T07_DM_SHOHIN_HAN T07, M_SBSHBUN_DM S01 ";
		if (!StringUtils.equals(szChanel, "1")) {
			sqlTable = " T07_DM_SHOHIN_TEN T07, M_SBSHBUN_DM S01 ";
		}
		// 年月条件設定
		String sqlWhere = " and YYYY_MM = '" + szYm + "'";
		// プロモーション設定
		if (!StringUtils.equals(szPromoFrom, "")) {
			sqlWhere += " and PROMO_CD between '" + szPromoFrom + "' and '" + szPromoTo + "'";
		}
		// 条件番号条件設定
		if (!StringUtils.isEmpty(szSeg)) {
			JSONArray segArray = JSONArray.fromObject(StringUtils.split(szSeg, ","));
			String segNo = "";
			for (int i = 0; i < segArray.size(); i++) {
				if (StringUtils.contains(segArray.get(i).toString(), "-")) {
					String[] range = StringUtils.split(segArray.get(i).toString(), "-");
					for (int j = NumberUtils.toInt(range[0]); j <= NumberUtils.toInt(range[1]); j++) {
						segNo += Integer.toString(j) + ",";
					}
				} else {
					segNo += segArray.get(i).toString() + ",";
				}
			}
			sqlWhere += " AND SEGNO in (" + StringUtils.removeEnd(segNo, ",") + ")";
		}
		// 商品分類条件設定
		String sqlWhere2 = getWhereShohinBunrui("SHO_");

		// 商品選択時　全表示・実績のみ
		String fromAll = "";
		if (StringUtils.equals(szShoCate1, DefineReport.RadioSho1.ID1.getVal())) {
			fromAll = "from M_SBSHBUN_DM S01 left outer join T07 W ON trim(W.SBSHBUN) = trim(S01.SBSHBUN) where S01.KBN = 10 " + sqlWhere2;
		}

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();

		sbSQL.append("with T07(SBSHBUN,KBN,JUCSURYO,JUCGAKU,KYAKUSU_J) as (");
		sbSQL.append(" select T07.SBSHBUN,T07.KBN,JUCSURYO,JUCGAKU,KYAKUSU_J from " + sqlTable);
		sbSQL.append(" where T07.SBSHBUN = S01.SBSHBUN and T07.KBN = S01.KBN " + sqlWhere + sqlWhere2 + ")   ");
		sbSQL.append(getSelectShohin(sqlKn, "from T07 W, M_SBSHBUN_DM S01", fromAll, "CHECK_SHO"));
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

		// 条件番号From-To
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.SEGNO.getView());
		cells.add(jad.getJSONValue(DefineReport.Select.SEGNO.getObj()));
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
