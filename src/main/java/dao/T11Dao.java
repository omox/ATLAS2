package dao;

import java.sql.Connection;

/**
 * T11テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T11Dao extends ItemDao {

	/**
	 * 新しいT11Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T11Dao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");			// （必須）基準年月

		// パラメータ確認
		if (szYm == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("SELECT T11.PROMO_CD as SORT,trim(T11.PROMO_CD) as \"プロモーション\",P.PROMO_MEI as \"媒体名\"");
		sbSQL.append(" ,P.HAKKOBI as \"掲載日\",KYAKUSU_J as \"受注客数\",JUCKAISU as \"受注回数\",JUCGAKU as \"受注金額\"");
		sbSQL.append(" FROM T11_PROMO_ALL T11 LEFT OUTER JOIN M_PROMO_ALL P ON T11.PROMO_CD = P.PROMO_CD ");
		sbSQL.append(" WHERE JUCHU_YM ='" + szYm + "' ");
		sbSQL.append("UNION ALL ");
		sbSQL.append("SELECT 'ZZZZZZZZZZZ' AS SORT,'合計','','',SUM(KYAKUSU_J),SUM(JUCKAISU),SUM(JUCGAKU) ");
		sbSQL.append(" FROM T11_PROMO_ALL ");
		sbSQL.append(" WHERE JUCHU_YM ='" + szYm + "' ");
		sbSQL.append(" ORDER BY SORT");

		System.out.println(sbSQL.toString());
		return sbSQL.toString();
	}

}
