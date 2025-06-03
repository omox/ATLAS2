package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.Defines;
import common.ItemList;
import common.JsonArrayData;

/**
 * T19テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T19Dao extends ItemDao {

	/**
	 * 新しいT19Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public T19Dao(Connection source) {
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
		String szYy = getMap().get("YYYY");				// 基準年
		String szChanel = getMap().get("HAN_CHAN");		// 販売チャネル
		String szArea = getMap().get("AREA");			// 都道府県
		String szRad_B2 = getMap().get("RAD_B2");		// 対比表示-1:前年比 2:前月比 3:なし

		// パラメータ確認
		System.out.println(super.getConditionLog());

		// 設定
		String sqlWhere = " WHERE SUBSTR(YYYY_MM,1,4) = '" + szYy + "' AND HAN_CHAN = '" + szChanel + "' ";
		if (!StringUtils.equals(szArea, "00")) {
			sqlWhere += " AND PREF_CD = '" + szArea + "' ";
		}

		// 日付情報取得
		String sqlDtCommand = "SELECT KIJUN_YM,KIJUN_YM_MEI FROM YM_KIJUN_T19 WHERE KIJUN_YM LIKE '" + szYy + "%' ORDER BY KIJUN_YM";
		ItemList iL = new ItemList();
		JSONArray dtArray = iL.selectJSONArray(sqlDtCommand, null, Defines.STR_JNDI_DS);
		// 可変情報設定
		String itemK = "";
		String colWith = "";
		String colSumK = "";
		String colSumZH = "";
		String colYm = "";
		for (int i = 0; i < dtArray.size(); i++) {
			String val = ObjectUtils.toString(dtArray.getJSONObject(i).get("KIJUN_YM"));
			String txt = ObjectUtils.toString(dtArray.getJSONObject(i).get("KIJUN_YM_MEI"));
			String k = "K" + StringUtils.right(val, 2);
			itemK += "," + k;
			colWith += ",case when  YYYY_MM = '" + val + "' then KYAKUSU else 0 end";
			colSumK += ",sum(" + k + ")";
			colSumZH += ",case when sum(Z" + k + ") = 0 then 0 else (sum(" + k + ") / sum(Z" + k + ")) * 100 end";
			colYm += "," + k + " as \"" + txt + "\"";
		}
		colWith += ",KYAKUSU";
		itemK += ",KALL";
		colSumK += ",SUM(KALL)";
		colSumZH += ",case when sum(ZKALL) = 0 then 0 else (sum(KALL) / sum(ZKALL)) * 100 end";
		String colWithZ = "";
		if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {
			colWithZ = StringUtils.replace(colWith, "KYAKUSU", "ZYY_KYAKUSU");
			colSumK = ",0,'顧客数'" + colSumK;
			colSumZH = ",1,'前年比'" + colSumZH;
		} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) {
			colWithZ = StringUtils.replace(colWith, "KYAKUSU", "ZMM_KYAKUSU");
			colSumK = ",0,'顧客数'" + colSumK;
			colSumZH = ",1,'前月比'" + colSumZH;
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		String select = "";
		if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID3.getVal())) {
			select = "STATUSW AS SORT,STATUS_NM1 as \"当月状態\",STATUS_NM2 as \"前月状態\"" + colYm + ",KALL as \"合計\" ";
			sbSQL.append("WITH WORK(STATUSW" + itemK + ") AS(");
			sbSQL.append(" SELECT STATUS_T||STATUS_Z" + colWith + " FROM T19_ACT " + sqlWhere + "), ");
			sbSQL.append("WORK0(STATUSW" + itemK + ") AS( ");
			sbSQL.append(" SELECT STATUSW" + colSumK + " FROM WORK GROUP BY STATUSW ),   ");
			sbSQL.append("WORK1(STATUSW" + itemK + ") AS( ");
			sbSQL.append(" SELECT '1Z'" + colSumK + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '1' ),   ");
			sbSQL.append("WORK2(STATUSW" + itemK + ") AS( ");
			sbSQL.append(" SELECT '2Z'" + colSumK + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '2' ),   ");
			sbSQL.append("WORK3(STATUSW" + itemK + ") AS( ");
			sbSQL.append(" SELECT '3Z'" + colSumK + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '3' ),   ");
			sbSQL.append("WORK9(STATUSW" + itemK + ") AS( ");
			sbSQL.append(" SELECT '9Z'" + colSumK + " FROM WORK WHERE SUBSTR(STATUSW,1,1) <> 'A')   ");
		} else {
			select = "STATUSW||MEASID AS SORT,case when MEASID = 0 THEN STATUS_NM1 else '' end as \"当月状態\""
					+ ",case when MEASID = 0 THEN STATUS_NM2 else '_' end as \"前月状態\",MEASURE" + colYm + ",KALL as \"合計\" ";
			String itemZk = StringUtils.replace(itemK, "K", "ZK");
			String itemK2 = ",MEASID,MEASURE" + itemK;

			sbSQL.append("WITH WORK(STATUSW" + itemK + "" + itemZk + ") AS(");
			sbSQL.append(" SELECT STATUS_T||STATUS_Z" + colWith + colWithZ + " FROM T19_ACT " + sqlWhere + "), ");
			sbSQL.append("WORK0(STATUSW" + itemK2 + ") AS( ");
			sbSQL.append(" SELECT STATUSW" + colSumK + " FROM WORK GROUP BY STATUSW");
			sbSQL.append(" UNION ALL ");
			sbSQL.append(" SELECT STATUSW" + colSumZH + " FROM WORK GROUP BY STATUSW),   ");
			sbSQL.append("WORK1(STATUSW" + itemK2 + ") AS( ");
			sbSQL.append(" SELECT '1Z'" + colSumK + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '1'");
			sbSQL.append(" UNION ALL ");
			sbSQL.append(" SELECT '1Z'" + colSumZH + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '1'),   ");
			sbSQL.append("WORK2(STATUSW" + itemK2 + ") AS( ");
			sbSQL.append(" SELECT '2Z'" + colSumK + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '2'");
			sbSQL.append(" UNION ALL ");
			sbSQL.append(" SELECT '2Z'" + colSumZH + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '2'),   ");
			sbSQL.append("WORK3(STATUSW" + itemK2 + ") AS( ");
			sbSQL.append(" SELECT '3Z'" + colSumK + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '3'");
			sbSQL.append(" UNION ALL ");
			sbSQL.append(" SELECT '3Z'" + colSumZH + " FROM WORK WHERE SUBSTR(STATUSW,1,1) = '3'),");
			sbSQL.append("WORK9(STATUSW" + itemK2 + ") AS( ");
			sbSQL.append(" SELECT '9Z'" + colSumK + " FROM WORK WHERE SUBSTR(STATUSW,1,1) <> 'A'");
			sbSQL.append(" UNION ALL ");
			sbSQL.append(" SELECT '9Z'" + colSumZH + " FROM WORK WHERE SUBSTR(STATUSW,1,1) <> 'A') ");
		}
		sbSQL.append("SELECT " + select + "FROM WORK0 LEFT OUTER JOIN M_T19_MIDASHI ON STATUSW = STATUS");
		sbSQL.append(" UNION ALL ");
		sbSQL.append("SELECT " + select + "FROM WORK1 LEFT OUTER JOIN M_T19_MIDASHI ON STATUSW = STATUS");
		sbSQL.append(" UNION ALL ");
		sbSQL.append("SELECT " + select + "FROM WORK2 LEFT OUTER JOIN M_T19_MIDASHI ON STATUSW = STATUS");
		sbSQL.append(" UNION ALL ");
		sbSQL.append("SELECT " + select + "FROM WORK3 LEFT OUTER JOIN M_T19_MIDASHI ON STATUSW = STATUS");
		sbSQL.append(" UNION ALL ");
		sbSQL.append("SELECT " + select + "FROM WORK9 LEFT OUTER JOIN M_T19_MIDASHI ON STATUSW = STATUS");
		sbSQL.append(" ORDER BY SORT");

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
		cells.add(DefineReport.Select.YY_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);
		// 都道府県
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.AREA.getView());
		cells.add(jad.getJSONText(DefineReport.Select.AREA.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}



}
