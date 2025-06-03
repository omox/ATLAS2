package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T07mテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T07mDao extends ItemDao {

	/**
	 * 新しいT07mDaoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public T07mDao(Connection source) {
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
		String szYy = getMap().get("YYYY");					// （必須）基準年
		String szShoCate2 = getMap().get("SHO_CATE2");		// （必須）金額表示選択
		// パラメータ確認
		if (szYy == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 年月Where句設定
		String sqlWhere = " and YYYY_YY = '" + szYy + "'";
		// 商品分類Where句設定
		String sqlWhere2 = getWhereShohinBunrui("SHO_");

		// 表示データ選択設定
		String sqlWhere3 = " AND SU_KBN= '3' ";
		if (StringUtils.equals(szShoCate2, "1")) { // 金額(税抜き
			sqlWhere3 = " AND SU_KBN= '3' ";
		} else if (StringUtils.equals(szShoCate2, "2")) { // 金額(税込）
			sqlWhere3 = " AND SU_KBN= '4' ";
		} else if (StringUtils.equals(szShoCate2, "3")) { // 客数
			sqlWhere3 = " AND SU_KBN= '1' ";
		} else if (StringUtils.equals(szShoCate2, "4")) { // 数量
			sqlWhere3 = " AND SU_KBN= '2' ";
		}
		// 数値項目の編集
		String sqlKn1 = "";
		sqlKn1 = ",SUM(SU_01) AS \"１月\",SUM(SU_02) AS \"２月\",SUM(SU_03) AS \"３月\",SUM(SU_04) AS \"４月\"";
		sqlKn1 += ",SUM(SU_05) AS \"５月\",SUM(SU_06) AS \"６月\", SUM(SU_07) AS \"７月\",SUM(SU_08) AS \"８月\"";
		sqlKn1 += ",SUM(SU_09) AS \"９月\",SUM(SU_10) AS \"１０月\",SUM(SU_11) AS \"１１月\",SUM(SU_12) AS \"１２月\" ";

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("with T07(SBSHBUN,KBN,SU_01,SU_02,SU_03,SU_04,SU_05,SU_06,SU_07,SU_08,SU_09,SU_10,SU_11,SU_12) as (");
		sbSQL.append(" select T07.SBSHBUN,T07.KBN,SU_01,SU_02,SU_03,SU_04,SU_05,SU_06,SU_07,SU_08,SU_09,SU_10,SU_11,SU_12 ");
		sbSQL.append(" from T07_DM_SHOHIN_MON T07, M_SBSHBUN_DM S01");
		sbSQL.append(" where T07.SBSHBUN = S01.SBSHBUN and T07.KBN = S01.KBN " + sqlWhere + sqlWhere2 + sqlWhere3 + ")   ");
		sbSQL.append(getSelectShohin(sqlKn1, "from T07 W, M_SBSHBUN_DM S01", "", "CHECK_SHO"));
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

		// 基準年
		List<String> cells = new ArrayList<String>();
		cells.add(DefineReport.Select.YY_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
		getWhere().add(cells);

		// 表示データ
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadioSho2.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadioSho2.NAME.getObj()));
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
