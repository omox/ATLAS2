package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import common.DefineReport;
import common.JsonArrayData;

/**
 * B021テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
/**
 * @author Kobayashi_Yuka
 *
 */
public class B021Dao extends ItemDao {

	/**
	 * 新しいT021Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public B021Dao(Connection source) {
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
		super.cmnOutputQueryList();

		// 検索実行
		return super.selectBySQL(command);
	}

	/**
	 * 検索文を作成します
	 *
	 * @return 検索<String>
	 */
	private String createCommand() {

		String command = null;

		String szYm = getMap().get("YM_KIJUN");			// 開始年月

		// パラメータ確認
		if (szYm == "")  {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append(" SELECT M.TENPO_CD AS SORT,MAX(M.TENPO_NM) AS 店舗,SUM(KYAKUSU) AS 客数,SUM(SHU_URIGK) AS 出荷売上,ROUND(SUM(JUN_URIGK)) AS 純売上,SUM(CHO_URIGK) AS 調整前純売上 ");
		sbSQL.append("  FROM URI_TEN U LEFT OUTER JOIN M_TENPO M ON U.TENPO_CD = M.TENPO_CD");
		sbSQL.append("   WHERE KYAKUSU > 0 AND SUBSTR(URI_YMD,1,6) = '"+szYm+"'");
		sbSQL.append("  GROUP BY M.TENPO_CD  ");
		sbSQL.append(" UNION ALL SELECT 'Z99' AS SORT,'店舗計' AS 店舗,SUM(KYAKUSU) AS 客数,SUM(SHU_URIGK) AS 出荷売上,ROUND(SUM(JUN_URIGK)) AS 純売上,SUM(CHO_URIGK) AS 調整前純売上 ");
		sbSQL.append("  FROM URI_TEN U LEFT OUTER JOIN M_TENPO M ON U.TENPO_CD = M.TENPO_CD");
		sbSQL.append("   WHERE KYAKUSU > 0 AND SUBSTR(URI_YMD,1,6) = '"+szYm+"'");
		sbSQL.append("  ORDER BY SORT ");

		System.out.println(sbSQL.toString());
		command = sbSQL.toString();
		return command;
	}
}
