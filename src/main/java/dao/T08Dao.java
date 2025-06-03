package dao;

import java.sql.Connection;

import org.apache.commons.lang.StringUtils;

/**
 * T0８テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T08Dao extends ItemDao {

	/**
	 * 新しいT03SegDaoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T08Dao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");			// 基準年月
		String szPromo = getMap().get("PROMO_CD");		// プロモーション
		String szSegNo = getMap().get("SEGNO");			// 条件番号

		// パラメータ確認
		if (szYm == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 共通Where句
		String sqlWhere = " where YYYY_MM = '" + szYm + "'";
		// プロモーション設定
		if (!StringUtils.equals(szPromo, "")) {
			sqlWhere += " and PROMO_CD = '" + szPromo + "' ";
		}
		if (!StringUtils.equals(szSegNo, "0")) {
			sqlWhere += " and SEGNO = " + szSegNo;
		}


		// 検索文（並び替え＆取得範囲制限）列名固定SQLでの設定未
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH WK(WPROMO_CD,W_DAY) AS (");
		sbSQL.append("SELECT '" + szPromo + "',YMD_CD FROM M_CALENDAR ");
		sbSQL.append(" WHERE YMD_DATE IS NOT NULL AND YMD_CD >= (SELECT HAKKOBI FROM M_PROMO_ALL WHERE PROMO_CD = '" + szPromo + "')");
		sbSQL.append("   AND YMD_CD <( SELECT CASE WHEN to_char(add_months(to_date(HAKKOBI,'YYYY-MM-DD'),+4),'YYYYMM')||'00'");
		sbSQL.append("                                > to_char(add_months(to_date(" + szYm + "||'01','YYYY-MM-DD'),+1),'YYYYMM')||'00' ");
		sbSQL.append("                            THEN  to_char(add_months(to_date(" + szYm + "||'01','YYYY-MM-DD'),+1),'YYYYMM')||'00'");
		sbSQL.append("                             ELSE to_char(add_months(to_date(HAKKOBI,'YYYY-MM-DD'),+4),'YYYYMM')||'00' END");
		sbSQL.append("     FROM M_PROMO_ALL WHERE PROMO_CD = '" + szPromo + "') ),");
		sbSQL.append("WK2(YYYY_MM,PROMO_CD,SEGNO,JUCHUBI,K01_SU,K02_SU,K03_SU,K04_SU,K05_SU,K06_SU,K07_SU,K08_SU,K09_SU,K99_SU)");
		sbSQL.append(" AS ( SELECT YYYY_MM,PROMO_CD,SEGNO,JUCHUBI,K01_SU,K02_SU,K03_SU,K04_SU,K05_SU,K06_SU,K07_SU,K08_SU,K09_SU,K99_SU");
		sbSQL.append("      FROM T08_JUCHU " + sqlWhere + " ),");
		sbSQL.append("WORK(W_DAY,K01_SU,K02_SU,K03_SU,K04_SU,K05_SU,K06_SU,K07_SU,K08_SU,K09_SU,K99_SU) AS (SELECT W_DAY,COALESCE(SUM(K01_SU),0),COALESCE(SUM(K02_SU),0),COALESCE(SUM(K03_SU),0),COALESCE(SUM(K04_SU),0),COALESCE(SUM(K05_SU),0),COALESCE(SUM(K06_SU),0),COALESCE(SUM(K07_SU),0),COALESCE(SUM(K08_SU),0),COALESCE(SUM(K09_SU),0),COALESCE(SUM(K99_SU),0) FROM WK LEFT OUTER JOIN WK2 ON WPROMO_CD = WK2.PROMO_CD AND W_DAY = JUCHUBI GROUP BY W_DAY), ");
		sbSQL.append("WORKALL(ALL_01,ALL_02,ALL_03,ALL_04,ALL_05,ALL_06,ALL_07,ALL_08,ALL_09,ALL_99) AS (SELECT SUM(K01_SU),SUM(K02_SU),SUM(K03_SU),SUM(K04_SU),SUM(K05_SU),SUM(K06_SU),SUM(K07_SU),SUM(K08_SU),SUM(K09_SU),SUM(K99_SU) FROM WORK ) ");
		sbSQL.append("SELECT W_DAY AS SORT,W_DAY,");
		sbSQL.append("K01_SU,SUM(K01_SU) OVER (order by W_DAY),CASE WHEN ALL_01 > 0 THEN (SUM(K01_SU)  OVER (order by W_DAY))/ALL_01*100 ELSE 0 END,");
		sbSQL.append("K02_SU,SUM(K02_SU) OVER (order by W_DAY),CASE WHEN ALL_02 > 0 THEN (SUM(K02_SU)  OVER (order by W_DAY))/ALL_02*100 ELSE 0 END,");
		sbSQL.append("K03_SU,SUM(K03_SU) OVER (order by W_DAY),CASE WHEN ALL_03 > 0 THEN (SUM(K03_SU)  OVER (order by W_DAY))/ALL_03*100 ELSE 0 END,");
		sbSQL.append("K04_SU,SUM(K04_SU) OVER (order by W_DAY),CASE WHEN ALL_04 > 0 THEN (SUM(K04_SU)  OVER (order by W_DAY))/ALL_04*100 ELSE 0 END,");
		sbSQL.append("K05_SU,SUM(K05_SU) OVER (order by W_DAY),CASE WHEN ALL_05 > 0 THEN (SUM(K05_SU)  OVER (order by W_DAY))/ALL_05*100 ELSE 0 END,");
		sbSQL.append("K06_SU,SUM(K06_SU) OVER (order by W_DAY),CASE WHEN ALL_06 > 0 THEN (SUM(K06_SU)  OVER (order by W_DAY))/ALL_06*100 ELSE 0 END,");
		sbSQL.append("K07_SU,SUM(K07_SU) OVER (order by W_DAY),CASE WHEN ALL_07 > 0 THEN (SUM(K07_SU)  OVER (order by W_DAY))/ALL_07*100 ELSE 0 END,");
		sbSQL.append("K08_SU,SUM(K08_SU) OVER (order by W_DAY),CASE WHEN ALL_08 > 0 THEN (SUM(K08_SU)  OVER (order by W_DAY))/ALL_08*100 ELSE 0 END,");
		sbSQL.append("K09_SU,SUM(K09_SU) OVER (order by W_DAY),CASE WHEN ALL_09 > 0 THEN (SUM(K09_SU)  OVER (order by W_DAY))/ALL_09*100 ELSE 0 END,");
		sbSQL.append("K99_SU,SUM(K99_SU) OVER (order by W_DAY),CASE WHEN ALL_99 > 0 THEN (SUM(K99_SU)  OVER (order by W_DAY))/ALL_99*100 ELSE 0 END");
		sbSQL.append(" FROM WORK,WORKALL ORDER BY SORT");

		System.out.println(sbSQL.toString());
		return sbSQL.toString();
	}
}
