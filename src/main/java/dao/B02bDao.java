package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * B02bテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B02bDao extends ItemDao {

	/**
	 * 新しいT02Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public B02bDao(Connection source) {
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
		super.cmnOutputQueryList();

		// 検索実行
		return super.selectBySQL(command);
	}


	/**
	 * 検索文を作成します
	 * @return 検索<String>
	 */
	private String createCommand() {
		String szYy = getMap().get("YYYY");			// （必須）基準年月
		String szTenpo = getMap().get("TENPOG");	//

		// パラメータ確認
		if ((szYy == null) || (szTenpo == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 年設定
		String sqlWhere = " WHERE URI_YY = '" + szYy + "'";
		if (StringUtils.equals(szTenpo, "00")) {  //全店舗の時コード変換
			szTenpo = "000";
		}
		sqlWhere += " and TENPO_CD = '" + szTenpo + "'";

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH WORKT(ITEM_CD,M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,M11,M12) AS ( ");
		sbSQL.append(" SELECT ITEM_CD,M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,M11,M12 ");
		sbSQL.append(" FROM URI_TEN_MCODE " + sqlWhere + ") ");
		sbSQL.append("SELECT M.ITEM_CD AS SORT ,MAX(ITEM_NM) AS \"項目\",SUM(M01) AS \"１月\",SUM(M02) AS \"２月\",SUM(M03) AS \"３月\",SUM(M04) AS \"４月\",SUM(M05) AS \"５月\",SUM(M06) AS \"６月\",SUM(M07) AS \"７月\",SUM(M08) AS \"８月\",SUM(M09) AS \"９月\",SUM(M10) AS \"１０月\",SUM(M11) AS \"１１月\",SUM(M12) AS \"１２月\"");
		sbSQL.append("  FROM M_MCODE M LEFT OUTER JOIN WORKT U ON U.ITEM_CD = M.ITEM_CD GROUP BY M.ITEM_CD ");
		sbSQL.append(" order by SORT");

		System.out.println(sbSQL.toString());
		return sbSQL.toString();
	}
}
