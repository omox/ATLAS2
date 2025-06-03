package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T15テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T15Dao extends ItemDao {

	/**
	 * 新しいT15Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T15Dao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");	// 期間-年月
		String szRadSu = getMap().get("RADSU");	// 表示項目-1:顧客数 2:受注件数
		String szRadSt = getMap().get("RADST");	// ステージ／会員区分 0:ステージ 1:会員区分

		// パラメータ確認
		if (szYm == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());


		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		if (StringUtils.equals(szRadSt, DefineReport.RadT_10.ID2.getVal())) {	// 会員区分
			sbSQL.append("WITH MRANK(MRANK_G_CD,MRANK_G_NM) AS (SELECT MRANK_G_CD,MRANK_G_NM FROM M_MRANK2 GROUP BY MRANK_G_CD,MRANK_G_NM ),");
			sbSQL.append("WORK(LKONYU2,IPPN_N,IPPN_R,IPPN_GAK_N,IPPN_GAK_R,TOKU_N,TOKU_R,TOKU_GAK_N,TOKU_GAK_R) AS (");
			sbSQL.append("select LKONYU2,");
			sbSQL.append("       CASE WHEN KAIINKBN = 0  AND NRKBN = 1 THEN SU ELSE 0 END,");
			sbSQL.append("       CASE WHEN KAIINKBN = 0  AND NRKBN = 2 THEN SU ELSE 0 END,");
			sbSQL.append("       CASE WHEN KAIINKBN = 0  AND NRKBN = 1 THEN JUCGK ELSE 0 END,");
			sbSQL.append("       CASE WHEN KAIINKBN = 0  AND NRKBN = 2 THEN JUCGK ELSE 0 END,");
			sbSQL.append("       CASE WHEN KAIINKBN > 0 AND KAIINKBN < 9 AND NRKBN = 1 THEN SU ELSE 0 END,");
			sbSQL.append("       CASE WHEN KAIINKBN > 0 AND KAIINKBN < 9 AND NRKBN = 2 THEN SU ELSE 0 END,");
			sbSQL.append("       CASE WHEN KAIINKBN > 0 AND KAIINKBN < 9 AND NRKBN = 1 THEN JUCGK ELSE 0 END,");
			sbSQL.append("       CASE WHEN KAIINKBN > 0 AND KAIINKBN < 9 AND NRKBN = 2 THEN JUCGK ELSE 0 END");
			sbSQL.append("  FROM T15_HAN WHERE KBN ='"+szRadSu+"' AND YYYY_MM = '"+szYm+"') ");
			sbSQL.append("SELECT MRANK_G_CD AS SORT,MRANK_G_NM, ");
			sbSQL.append("       SUM(IPPN_N),SUM(IPPN_R),SUM(IPPN_N)+SUM(IPPN_R),SUM(IPPN_GAK_N)+SUM(IPPN_GAK_R),CASE WHEN (SUM(IPPN_N)+SUM(IPPN_R)) = 0 THEN 0 ELSE ROUND((SUM(IPPN_GAK_N)+SUM(IPPN_GAK_R))/(SUM(IPPN_N)+SUM(IPPN_R))) END, ");
			sbSQL.append("       SUM(TOKU_N),SUM(TOKU_R),SUM(TOKU_N)+SUM(TOKU_R),SUM(TOKU_GAK_N)+SUM(TOKU_GAK_R),CASE WHEN (SUM(TOKU_N)+SUM(TOKU_R)) = 0 THEN 0 ELSE ROUND((SUM(TOKU_GAK_N)+SUM(TOKU_GAK_R))/(SUM(TOKU_N)+SUM(TOKU_R))) END, ");
			sbSQL.append("       SUM(IPPN_N)+SUM(TOKU_N),SUM(IPPN_GAK_N)+SUM(TOKU_GAK_N),CASE WHEN (SUM(IPPN_N)+SUM(TOKU_N)) = 0 THEN 0 ELSE ROUND((SUM(IPPN_GAK_N)+SUM(TOKU_GAK_N))/(SUM(IPPN_N)+SUM(TOKU_N))) END, ");
			sbSQL.append("       SUM(IPPN_R)+SUM(TOKU_R),SUM(IPPN_GAK_R)+SUM(TOKU_GAK_R),CASE WHEN (SUM(IPPN_R)+SUM(TOKU_R)) = 0 THEN 0 ELSE ROUND((SUM(IPPN_GAK_R)+SUM(TOKU_GAK_R))/(SUM(IPPN_R)+SUM(TOKU_R))) END, ");
			sbSQL.append("       SUM(IPPN_N)+SUM(IPPN_R)+SUM(TOKU_N)+SUM(TOKU_R),SUM(IPPN_GAK_N)+SUM(IPPN_GAK_R)+SUM(TOKU_GAK_N)+SUM(TOKU_GAK_R), ");
			sbSQL.append("       CASE WHEN (SUM(IPPN_N)+SUM(IPPN_R)+SUM(TOKU_N)+SUM(TOKU_R))=0 then 0 else ROUND((SUM(IPPN_GAK_N)+SUM(IPPN_GAK_R)+SUM(TOKU_GAK_N)+SUM(TOKU_GAK_R))/(SUM(IPPN_N)+SUM(IPPN_R)+SUM(TOKU_N)+SUM(TOKU_R))) end ");
			sbSQL.append(" FROM WORK , MRANK WHERE LKONYU2 = MRANK_G_CD ");
			sbSQL.append(" GROUP BY MRANK_G_CD,MRANK_G_NM ");
			sbSQL.append("UNION ALL ");
			sbSQL.append(" SELECT 'ZALL' AS SORT,'合計', ");
			sbSQL.append("       SUM(IPPN_N),SUM(IPPN_R),SUM(IPPN_N)+SUM(IPPN_R),SUM(IPPN_GAK_N)+SUM(IPPN_GAK_R),CASE WHEN (SUM(IPPN_N)+SUM(IPPN_R)) = 0 THEN 0 ELSE ROUND((SUM(IPPN_GAK_N)+SUM(IPPN_GAK_R))/(SUM(IPPN_N)+SUM(IPPN_R))) END, ");
			sbSQL.append("       SUM(TOKU_N),SUM(TOKU_R),SUM(TOKU_N)+SUM(TOKU_R),SUM(TOKU_GAK_N)+SUM(TOKU_GAK_R),CASE WHEN (SUM(TOKU_N)+SUM(TOKU_R)) = 0 THEN 0 ELSE ROUND((SUM(TOKU_GAK_N)+SUM(TOKU_GAK_R))/(SUM(TOKU_N)+SUM(TOKU_R))) END, ");
			sbSQL.append("       SUM(IPPN_N)+SUM(TOKU_N),SUM(IPPN_GAK_N)+SUM(TOKU_GAK_N),CASE WHEN (SUM(IPPN_N)+SUM(TOKU_N)) = 0 THEN 0 ELSE ROUND((SUM(IPPN_GAK_N)+SUM(TOKU_GAK_N))/(SUM(IPPN_N)+SUM(TOKU_N))) END, ");
			sbSQL.append("       SUM(IPPN_R)+SUM(TOKU_R),SUM(IPPN_GAK_R)+SUM(TOKU_GAK_R),CASE WHEN (SUM(IPPN_R)+SUM(TOKU_R)) = 0 THEN 0 ELSE ROUND((SUM(IPPN_GAK_R)+SUM(TOKU_GAK_R)))/(SUM(IPPN_R)+SUM(TOKU_R)) END, ");
			sbSQL.append("       SUM(IPPN_N)+SUM(IPPN_R)+SUM(TOKU_N)+SUM(TOKU_R),SUM(IPPN_GAK_N)+SUM(IPPN_GAK_R)+SUM(TOKU_GAK_N)+SUM(TOKU_GAK_R), ");
			sbSQL.append("       CASE WHEN (SUM(IPPN_N)+SUM(IPPN_R)+SUM(TOKU_N)+SUM(TOKU_R))=0 then 0 else ROUND((SUM(IPPN_GAK_N)+SUM(IPPN_GAK_R)+SUM(TOKU_GAK_N)+SUM(TOKU_GAK_R))/(SUM(IPPN_N)+SUM(IPPN_R)+SUM(TOKU_N)+SUM(TOKU_R))) end ");
			sbSQL.append(" FROM WORK  ORDER BY SORT");
		} else {	// ステージ
			sbSQL.append("WITH MRANK(MRANK_G_CD,MRANK_G_NM) AS (SELECT MRANK_G_CD,MRANK_G_NM FROM M_MRANK2 GROUP BY MRANK_G_CD,MRANK_G_NM ), ");
			sbSQL.append("WORK(LKONYU2,S1_N,S1_R,S1_GAK_N,S1_GAK_R,S2_N,S2_R,S2_GAK_N,S2_GAK_R,S3_N,S3_R,S3_GAK_N,S3_GAK_R,S4_N,S4_R,S4_GAK_N,S4_GAK_R,S5_N,S5_R,S5_GAK_N,S5_GAK_R,ST_N,ST_R,ST_GAK_N,ST_GAK_R)AS( ");
			sbSQL.append("select LKONYU2, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 10 AND NRKBN = 1 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 10 AND NRKBN = 2 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 10 AND NRKBN = 1 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 10 AND NRKBN = 2 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 20 AND NRKBN = 1 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 20 AND NRKBN = 2 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 20 AND NRKBN = 1 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 20 AND NRKBN = 2 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 30 AND NRKBN = 1 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 30 AND NRKBN = 2 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 30 AND NRKBN = 1 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 30 AND NRKBN = 2 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 40 AND NRKBN = 1 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 40 AND NRKBN = 2 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 40 AND NRKBN = 1 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 40 AND NRKBN = 2 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 50 AND NRKBN = 1 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 50 AND NRKBN = 2 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 50 AND NRKBN = 1 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN = 50 AND NRKBN = 2 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN in (10,20,30,40,50) AND NRKBN = 1 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN in (10,20,30,40,50) AND NRKBN = 2 THEN SU ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN in (10,20,30,40,50) AND NRKBN = 1 THEN JUCGK ELSE 0 END, ");
			sbSQL.append("       CASE WHEN KAIINKBN in (10,20,30,40,50) AND NRKBN = 2 THEN JUCGK ELSE 0 END ");
			sbSQL.append("  FROM T15_HAN WHERE KBN ='"+szRadSu+"' AND YYYY_MM = '"+szYm+"') ");
			sbSQL.append("SELECT MRANK_G_CD AS SORT,MRANK_G_NM, ");
			sbSQL.append("       SUM(S1_N),SUM(S1_R),SUM(S1_N)+SUM(S1_R),SUM(S1_GAK_N)+SUM(S1_GAK_R),CASE WHEN (SUM(S1_N)+SUM(S1_R)) = 0 THEN 0 ELSE ROUND((SUM(S1_GAK_N)+SUM(S1_GAK_R))/(SUM(S1_N)+SUM(S1_R))) END, ");
			sbSQL.append("       SUM(S2_N),SUM(S2_R),SUM(S2_N)+SUM(S2_R),SUM(S2_GAK_N)+SUM(S2_GAK_R),CASE WHEN (SUM(S2_N)+SUM(S2_R)) = 0 THEN 0 ELSE ROUND((SUM(S2_GAK_N)+SUM(S2_GAK_R))/(SUM(S2_N)+SUM(S2_R))) END, ");
			sbSQL.append("       SUM(S3_N),SUM(S3_R),SUM(S3_N)+SUM(S3_R),SUM(S3_GAK_N)+SUM(S3_GAK_R),CASE WHEN (SUM(S3_N)+SUM(S3_R)) = 0 THEN 0 ELSE ROUND((SUM(S3_GAK_N)+SUM(S3_GAK_R))/(SUM(S3_N)+SUM(S3_R))) END, ");
			sbSQL.append("       SUM(S4_N),SUM(S4_R),SUM(S4_N)+SUM(S4_R),SUM(S4_GAK_N)+SUM(S4_GAK_R),CASE WHEN (SUM(S4_N)+SUM(S4_R)) = 0 THEN 0 ELSE ROUND((SUM(S4_GAK_N)+SUM(S4_GAK_R))/(SUM(S4_N)+SUM(S4_R))) END, ");
			sbSQL.append("       SUM(S5_N),SUM(S5_R),SUM(S5_N)+SUM(S5_R),SUM(S5_GAK_N)+SUM(S5_GAK_R),CASE WHEN (SUM(S5_N)+SUM(S5_R)) = 0 THEN 0 ELSE ROUND((SUM(S5_GAK_N)+SUM(S5_GAK_R))/(SUM(S5_N)+SUM(S5_R))) END, ");
			sbSQL.append("       SUM(ST_N),SUM(ST_GAK_N),CASE WHEN SUM(ST_N) = 0 THEN 0 ELSE ROUND(SUM(ST_GAK_N)/SUM(ST_N)) END, ");
			sbSQL.append("       SUM(ST_R),SUM(ST_GAK_R),CASE WHEN SUM(ST_R) = 0 THEN 0 ELSE ROUND(SUM(ST_GAK_R)/SUM(ST_R)) END, ");
			sbSQL.append("       SUM(ST_N)+SUM(ST_R),SUM(ST_GAK_N)+SUM(ST_GAK_R),CASE WHEN (SUM(ST_N)+SUM(ST_R))=0 then 0 else ROUND((SUM(ST_GAK_N)+SUM(ST_GAK_R))/(SUM(ST_N)+SUM(ST_R))) end ");
			sbSQL.append(" FROM WORK , MRANK WHERE LKONYU2 = MRANK_G_CD ");
			sbSQL.append(" GROUP BY MRANK_G_CD,MRANK_G_NM ");
			sbSQL.append("UNION ALL ");
			sbSQL.append(" SELECT 'ZALL' AS SORT,'合計', ");
			sbSQL.append("       SUM(S1_N),SUM(S1_R),SUM(S1_N)+SUM(S1_R),SUM(S1_GAK_N)+SUM(S1_GAK_R),CASE WHEN (SUM(S1_N)+SUM(S1_R)) = 0 THEN 0 ELSE ROUND((SUM(S1_GAK_N)+SUM(S1_GAK_R))/(SUM(S1_N)+SUM(S1_R))) END, ");
			sbSQL.append("       SUM(S2_N),SUM(S2_R),SUM(S2_N)+SUM(S2_R),SUM(S2_GAK_N)+SUM(S2_GAK_R),CASE WHEN (SUM(S2_N)+SUM(S2_R)) = 0 THEN 0 ELSE ROUND((SUM(S2_GAK_N)+SUM(S2_GAK_R))/(SUM(S2_N)+SUM(S2_R))) END, ");
			sbSQL.append("       SUM(S3_N),SUM(S3_R),SUM(S3_N)+SUM(S3_R),SUM(S3_GAK_N)+SUM(S3_GAK_R),CASE WHEN (SUM(S3_N)+SUM(S3_R)) = 0 THEN 0 ELSE ROUND((SUM(S3_GAK_N)+SUM(S3_GAK_R))/(SUM(S3_N)+SUM(S3_R))) END, ");
			sbSQL.append("       SUM(S4_N),SUM(S4_R),SUM(S4_N)+SUM(S4_R),SUM(S4_GAK_N)+SUM(S4_GAK_R),CASE WHEN (SUM(S4_N)+SUM(S4_R)) = 0 THEN 0 ELSE ROUND((SUM(S4_GAK_N)+SUM(S4_GAK_R))/(SUM(S4_N)+SUM(S4_R))) END, ");
			sbSQL.append("       SUM(S5_N),SUM(S5_R),SUM(S5_N)+SUM(S5_R),SUM(S5_GAK_N)+SUM(S5_GAK_R),CASE WHEN (SUM(S5_N)+SUM(S5_R)) = 0 THEN 0 ELSE ROUND((SUM(S5_GAK_N)+SUM(S5_GAK_R))/(SUM(S5_N)+SUM(S5_R))) END, ");
			sbSQL.append("       SUM(ST_N),SUM(ST_GAK_N),CASE WHEN SUM(ST_N) = 0 THEN 0 ELSE ROUND(SUM(ST_GAK_N)/SUM(ST_N)) END, ");
			sbSQL.append("       SUM(ST_R),SUM(ST_GAK_R),CASE WHEN SUM(ST_R) = 0 THEN 0 ELSE ROUND(SUM(ST_GAK_R)/SUM(ST_R)) END, ");
			sbSQL.append("       SUM(ST_N)+SUM(ST_R),SUM(ST_GAK_N)+SUM(ST_GAK_R),CASE WHEN (SUM(ST_N)+SUM(ST_R))=0 then 0 else ROUND((SUM(ST_GAK_N)+SUM(ST_GAK_R))/(SUM(ST_N)+SUM(ST_R))) end ");
			sbSQL.append(" FROM WORK  ORDER BY SORT ");
		}

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

		// 表示項目-1:顧客数 2:受注件数
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadT_08.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_08.NAME.getObj()));

		// 会員区分 0:ステージ 1:会員区分
		cells.add("");
		cells.add(jad.getJSONText(DefineReport.RadT_10.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);

	}

}
