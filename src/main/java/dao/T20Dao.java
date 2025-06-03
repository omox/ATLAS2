package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T20テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T20Dao extends ItemDao {
	/**
	 * 新しいT20Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T20Dao(Connection source) {
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
		String szTorYm1 = getMap().get("TOROKU_YM1");	// 登録年月-開始年月
		String szTorYm2 = getMap().get("TOROKU_YM2");	// 登録年月-終了年月
		String szChanel = getMap().get("HAN_CHAN");		// 販売チャネル
		String szArea = getMap().get("AREA");			// 地域
		String szType = getMap().get("TYPE");		// 表示項目-4:基本 3:全展開

		// パラメータ確認
		if ((szTorYm1 == null) || (szTorYm1 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		String[] tits = new String[] { "アクティブ", "スリープ", "ドロップ", "合計" };
		String sqlWhere = " ";
		if (!StringUtils.equals(szArea, "00")) {
			sqlWhere += " AND PREF_CD = '" + szArea + "' ";
		}
		String sqlR2_1 = ",CASE WHEN SUM(T20ALL_K) = 0 THEN 0 ELSE ROUND(SUM(T20_G1_K)/SUM(T20ALL_K)*100,1) END ";
		String sqlR2_2 = ",CASE WHEN SUM(T20ALL_K) = 0 THEN 0 ELSE ROUND(SUM(T20_G2_K)/SUM(T20ALL_K)*100,1) END ";
		String sqlR2_3 = ",CASE WHEN SUM(T20ALL_K) = 0 THEN 0 ELSE ROUND(SUM(T20_G3_K)/SUM(T20ALL_K)*100,1) END ";
		String sqlR2_G = ",CASE WHEN SUM(T20ALL_K) = 0 THEN 0 ELSE ROUND(SUM(T20ALL_K)/SUM(T20ALL_K)*100,1) END ";
		String sqlR4_1 = ",CASE WHEN SUM(T20_G1_K) = 0 THEN 0 ELSE ROUND(SUM(T20_G1_RG)/SUM(T20_G1_K),0) END ";
		String sqlR4_2 = ",CASE WHEN SUM(T20_G2_K) = 0 THEN 0 ELSE ROUND(SUM(T20_G2_RG)/SUM(T20_G2_K),0) END ";
		String sqlR4_3 = ",CASE WHEN SUM(T20_G3_K) = 0 THEN 0 ELSE ROUND(SUM(T20_G3_RG)/SUM(T20_G3_K),0) END ";
		String sqlR4_G = ",CASE WHEN SUM(T20ALL_K) = 0 THEN 0 ELSE ROUND(SUM(T20ALL_RG)/SUM(T20ALL_K),0) END ";
		String sqlR6_1 = ",CASE WHEN SUM(T20_G1_RK) = 0 THEN 0 ELSE ROUND(SUM(T20_G1_RG)/SUM(T20_G1_RK),0) END ";
		String sqlR6_2 = ",CASE WHEN SUM(T20_G2_RK) = 0 THEN 0 ELSE ROUND(SUM(T20_G2_RG)/SUM(T20_G2_RK),0) END ";
		String sqlR6_3 = ",CASE WHEN SUM(T20_G3_RK) = 0 THEN 0 ELSE ROUND(SUM(T20_G3_RG)/SUM(T20_G3_RK),0) END ";
		String sqlR6_G = ",CASE WHEN SUM(T20ALL_RK) = 0 THEN 0 ELSE ROUND(SUM(T20ALL_RG)/SUM(T20ALL_RK),0) END ";

		// SELECT項目設定(表示別)
		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH MOTO(TOROKU_YM,T20_G,JUCKAISU_RUI,JUCGAKU_RUI,KYAKUSU) AS( ");
		sbSQL.append(" SELECT TOROKU_YM,JUCHUMR_T20_G,JUCKAISU_RUI,JUCGAKU_RUI,KYAKUSU ");
		sbSQL.append(" FROM T20_GENSUI LEFT OUTER JOIN M_JUCHU_R_T20 ON JUCHUML = JUCHUMR_T20 ");
		sbSQL.append(" where YYYY_MM = '"+szYm+"' and HAN_CHAN =  '"+szChanel+"' and TOROKU_YM >= '"+szTorYm1+"' and TOROKU_YM <= '"+szTorYm2+"' ");
		sbSQL.append( sqlWhere+" ), ");
		sbSQL.append("WORK(TOROKU_YM,T20_G1_K,T20_G2_K,T20_G3_K,T20ALL_K,T20_G1_RG,T20_G2_RG,T20_G3_RG,T20ALL_RG,T20_G1_RK,T20_G2_RK,T20_G3_RK,T20ALL_RK)  ");
		sbSQL.append("AS (SELECT TOROKU_YM, CASE WHEN T20_G = 'R01' THEN sum(KYAKUSU) ELSE 0 END, CASE WHEN T20_G = 'R02' THEN sum(KYAKUSU) ELSE 0 END, CASE WHEN T20_G = 'R03' THEN sum(KYAKUSU) ELSE 0 END,SUM(KYAKUSU), ");
		sbSQL.append(" CASE WHEN T20_G = 'R01' THEN sum(JUCGAKU_RUI) ELSE 0 END, CASE WHEN T20_G = 'R02' THEN sum(JUCGAKU_RUI) ELSE 0 END, CASE WHEN T20_G = 'R03' THEN sum(JUCGAKU_RUI) ELSE 0 END,SUM(JUCGAKU_RUI), CASE WHEN T20_G = 'R01' THEN sum(JUCKAISU_RUI) ELSE 0 END, ");
		sbSQL.append(" CASE WHEN T20_G = 'R02' THEN sum(JUCKAISU_RUI) ELSE 0 END, CASE WHEN T20_G = 'R03' THEN sum(JUCKAISU_RUI) ELSE 0 END,SUM(JUCKAISU_RUI) FROM MOTO group by TOROKU_YM,T20_G ), ");
		sbSQL.append("WORK2(TOROKU_YM,T20_G1_K,T20_G2_K,T20_G3_K,T20ALL_K,T20_G1_RG,T20_G2_RG,T20_G3_RG,T20ALL_RG,T20_G1_RK,T20_G2_RK,T20_G3_RK,T20ALL_RK ) AS  ");
		sbSQL.append(" (SELECT TOROKU_YM,SUM(T20_G1_K),SUM(T20_G2_K),SUM(T20_G3_K),SUM(T20ALL_K),SUM(T20_G1_RG),SUM(T20_G2_RG),SUM(T20_G3_RG),SUM(T20ALL_RG),SUM(T20_G1_RK),SUM(T20_G2_RK),SUM(T20_G3_RK),SUM(T20ALL_RK)  ");
		sbSQL.append("   FROM WORK GROUP BY TOROKU_YM ), ");
		sbSQL.append("WORK3(TOROKU_YM,W_STATUS,T20_G1,T20_G2,T20_G3,T20ALL) AS ( ");
		sbSQL.append("SELECT TOROKU_YM,1,T20_G1_K,T20_G2_K,T20_G3_K,T20ALL_K  FROM WORK2 ");
		sbSQL.append("UNION ALL SELECT TOROKU_YM,2"+sqlR2_1+sqlR2_2+sqlR2_3+sqlR2_G+" FROM WORK2 GROUP BY TOROKU_YM ");
		sbSQL.append("UNION ALL SELECT TOROKU_YM,3,T20_G1_RG,T20_G2_RG,T20_G3_RG,T20ALL_RG  FROM WORK2 ");
		sbSQL.append("UNION ALL SELECT TOROKU_YM,4"+sqlR4_1+sqlR4_2+sqlR4_3+sqlR4_G+" FROM WORK2 GROUP BY TOROKU_YM ");
		sbSQL.append("UNION ALL SELECT TOROKU_YM,5,T20_G1_RK,T20_G2_RK,T20_G3_RK,T20ALL_RK  FROM WORK2 ");
		sbSQL.append("UNION ALL SELECT TOROKU_YM,6"+sqlR6_1+sqlR6_2+sqlR6_3+sqlR6_G+" FROM WORK2 GROUP BY TOROKU_YM ");
		sbSQL.append("UNION ALL SELECT '999999',1,SUM(T20_G1_K),SUM(T20_G2_K),SUM(T20_G3_K),SUM(T20ALL_K)  FROM WORK2 ");
		sbSQL.append("UNION ALL SELECT '999999',2"+sqlR2_1+sqlR2_2+sqlR2_3+sqlR2_G+" FROM WORK2  ");
		sbSQL.append("UNION ALL SELECT '999999',3,SUM(T20_G1_RG),SUM(T20_G2_RG),SUM(T20_G3_RG),SUM(T20ALL_RG)  FROM WORK2 ");
		sbSQL.append("UNION ALL SELECT '999999',4"+sqlR4_1+sqlR4_2+sqlR4_3+sqlR4_G+" FROM WORK2   ");
		sbSQL.append("UNION ALL SELECT '999999',5,SUM(T20_G1_RK),SUM(T20_G2_RK),SUM(T20_G3_RK),SUM(T20ALL_RK)  FROM WORK2 ");
		sbSQL.append("UNION ALL SELECT '999999',6"+sqlR6_1+sqlR6_2+sqlR6_3+sqlR6_G+" FROM WORK2 ) ");
		sbSQL.append("SELECT TOROKU_YM||W_STATUS AS SORT,CASE WHEN W_STATUS = 1 THEN CASE WHEN  TOROKU_YM <> '999999' THEN SUBSTR(TOROKU_YM,1,4)||'年'||SUBSTR(TOROKU_YM,5,2)||'月'  ELSE '合  計' END ELSE '' END,STATUS_NM1, ");
		sbSQL.append("T20_G1 AS "+tits[0]+",T20_G2 AS "+tits[1]+",T20_G3 AS "+tits[2]+",T20ALL AS "+tits[3]+" FROM WORK3,M_MIDASHI_T20 WHERE STATUS = W_STATUS ");
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

		// 年月
		List<String> cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		getWhere().add(cells);
		// 登録年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.TOROKU.getView());
		cells.add(jad.getJSONText(DefineReport.Select.TOROKU.getObj()));
		cells.add("～");
		cells.add(jad.getJSONText(DefineReport.Select.TOROKU_TO.getObj()));
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);
		// 地域
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.AREA.getView());
		cells.add(jad.getJSONText(DefineReport.Select.AREA.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);

	}

}
