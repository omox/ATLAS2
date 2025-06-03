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
 * T261テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T261Dao extends ItemDao {

	/**
	 * 新しいT261Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T261Dao(Connection source) {
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
		String szRad_T2 = getMap().get("RAD_T2");	// 分析指定-1:単月 3:時系列
		String szYm1 = getMap().get("YYYY_MM");		// 開始年月
		String szYm2 = getMap().get("YYYY_MM2");	// 終了年月
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル
		String szRad_T4 = getMap().get("RAD_T4");	// 顧客状態-1:全顧客 2:ACT顧客
		String szRad_T5 = getMap().get("RAD_T5");	// 表示項目-1:顧客数 2:シェア 4:ACT率
		String szRad_B2 = getMap().get("RAD_B2");	// 対比表示-1:前年比 3:なし
		// パラメータ確認
		if ((szRad_T2 == null) || (szRad_T4 == null) || (szRad_T5 == null) || (szRad_B2 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 販売チャネル条件
		String whereCha = " and HAN_CHAN = '" + szChanel + "' ";

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		if (StringUtils.equals(szRad_T2, DefineReport.RadT_02.ID1.getVal())) {  // 分析指定-1:単月
			String sqlZEN1 = "";
			String sqlZEN2 = "";
			if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {
				sqlZEN1 = ",case when sum(ZYY_KYAKUSU) = 0 then 0 else sum(KYAKUSU) / sum(ZYY_KYAKUSU) * 100 end as \"前年比\"";
				sqlZEN2 = ",case when sum(ZYY_KYAKUSU_ACT) = 0 then 0 else sum(KYAKUSU_ACT) / sum(ZYY_KYAKUSU_ACT) * 100 end as \"ACT顧客前年比\"";
			} else if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID2.getVal())) {
				sqlZEN1 = ",case when sum(ZMM_KYAKUSU) = 0 then 0 else sum(KYAKUSU) / sum(ZMM_KYAKUSU) * 100 end as \"前月比\"";
				sqlZEN2 = ",case when sum(ZMM_KYAKUSU_ACT) = 0 then 0 else sum(KYAKUSU_ACT) / sum(ZMM_KYAKUSU_ACT) * 100 end as \"ACT顧客前月比\"";
			}
			sbSQL.append("WITH WK(JINKO) AS (select sum(JINKO) from M_KJINKO WHERE YYYY_MM_F <= '" + szYm1 + "' AND YYYY_MM_T >= '" + szYm1 + "') ");
			sbSQL.append("select PREF_CD AS SORT,max(CHIKU_MEI) as \"都道府県\"");
			sbSQL.append(" ,max(KESHOU_JINKO) as \"化粧人口\"");
			sbSQL.append(" ,sum(KYAKUSU) as \"顧客数\"" + sqlZEN1);
			sbSQL.append(" ,case when max(KESHOU_JINKO) = 0 then 0 else sum(KYAKUSU) / max(KESHOU_JINKO) * 100 end as \"シェア\"");
			sbSQL.append(" ,sum(KYAKUSU_ACT) as \"ACT顧客数\"" + sqlZEN2);
			sbSQL.append(" ,case when max(KESHOU_JINKO) = 0 then 0 else sum(KYAKUSU_ACT) / max(KESHOU_JINKO) * 100 end as \"ACTシェア\"");
			sbSQL.append(" ,case when sum(KYAKUSU) = 0 then 0 else sum(KYAKUSU_ACT) / sum(KYAKUSU) * 100 end as \"ACT率\" ");
			sbSQL.append("from T26_PREF left outer join M_AREA on PREF_CD = CHIKU_CD ");
			sbSQL.append("where YYYY_MM = '" + szYm1 + "' " + whereCha);
			sbSQL.append("group by PREF_CD ");
			sbSQL.append(" union all select 'Z999' AS SORT,'合　　計' as \"都道府県\"");
			sbSQL.append(" ,MAX(JINKO) as \"化粧人口\"");
			sbSQL.append(" ,sum(KYAKUSU) as \"顧客数\"" + sqlZEN1);
			sbSQL.append(" ,case when MAX(JINKO) = 0 then 0 else sum(KYAKUSU) / MAX(JINKO) * 100 end as \"シェア\"");
			sbSQL.append(" ,sum(KYAKUSU_ACT) as \"ACT顧客数\"" + sqlZEN2);
			sbSQL.append(" ,case when MAX(JINKO) = 0 then 0 else sum(KYAKUSU_ACT) / MAX(JINKO) * 100 end as \"ACTシェア\"");
			sbSQL.append(" ,case when sum(KYAKUSU) = 0 then 0 else sum(KYAKUSU_ACT) / sum(KYAKUSU) * 100 end as \"ACT率\" ");
			sbSQL.append("from T26_PREF,WK ");
			sbSQL.append("where YYYY_MM = '" + szYm1 + "' " + whereCha);
			sbSQL.append("order by SORT");
		} else {                                                              // 分析指定-3:時系列
			// 日付情報取得
			String sqlDtCommand = "select KIJUN_YM,KIJUN_YM_MEI from YM_KIJUN_T261 where KIJUN_YM between '" + szYm1 + "' and '" + szYm2 + "' ";
			ItemList iL = new ItemList();
			JSONArray dtArray = iL.selectJSONArray(sqlDtCommand, null, Defines.STR_JNDI_DS);
			// 可変情報設定
			String item = "";
			String colWith = "";
			String colKZ = "";	// 顧客数-全顧客
			String colKA = "";	// 顧客数-ACT顧客数
			String colSZ = "";	// シェア-全顧客
			String colSA = "";	// シェア-ACT顧客数
			String colAZ = "";	// ACT率-全顧客
			String colAA = "";	// ACT率-ACT顧客数
			for (int i = 0; i < dtArray.size(); i++) {
				String val = ObjectUtils.toString(dtArray.getJSONObject(i).get("KIJUN_YM"));
				String txt = ObjectUtils.toString(dtArray.getJSONObject(i).get("KIJUN_YM_MEI"));
				String idx = StringUtils.leftPad(Integer.toString(i), 3, "0");
				item += ",KS" + idx + ",KA" + idx + ",KJ" + idx;
				colWith += ",case when  YYYY_MM = '" + val + "' then KYAKUSU else 0 end";		// 月別顧客数
				colWith += ",case when  YYYY_MM = '" + val + "' then KYAKUSU_ACT else 0 end";	// 月別ACT顧客数
				colWith += ",case when  YYYY_MM = '" + val + "' then KESHOU_JINKO else 0 end";	// 月別化粧人口
				colKZ += ",sum(KS" + idx + ") as \"" + txt + "\" ";
				colKA += ",sum(KA" + idx + ") as \"" + txt + "\" ";
				colSZ += ",case when max(KJ" + idx + ") = 0 then 0 else sum(KS" + idx + ") / max(KJ" + idx + ") * 100 end as \"" + txt + "\" ";
				colSA += ",case when max(KJ" + idx + ") = 0 then 0 else sum(KA" + idx + ") / max(KJ" + idx + ") * 100 end as \"" + txt + "\" ";
				colAZ += ",case when sum(KS" + idx + ") = 0 then 0 else sum(KA" + idx + ") / sum(KS" + idx + ") * 100 end as \"" + txt + "\" ";
				colAA += ",100 as \"" + txt + "\" ";
			}
			String colItem = "";
			if (StringUtils.equals(szRad_T4, DefineReport.RadT_04.ID1.getVal())) {		// 1:全顧客
				if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID1.getVal())) {
					colItem = colKZ;
				} else if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID2.getVal())) {
					colItem = colSZ;
				} else if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID4.getVal())) {
					colItem = colAZ;
				}
			} else if (StringUtils.equals(szRad_T4, DefineReport.RadT_04.ID2.getVal())) {		// 2:ACT顧客
				if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID1.getVal())) {
					colItem = colKA;
				} else if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID2.getVal())) {
					colItem = colSA;
				} else if (StringUtils.equals(szRad_T5, DefineReport.RadT_05.ID4.getVal())) {
					colItem = colAA;
				}
			}
			sbSQL.append("with WORK(PREF_CD" + item + ") as (");
			sbSQL.append(" select PREF_CD" + colWith + " from T26_PREF");
			sbSQL.append(" where YYYY_MM >= '" + szYm1 + "' and YYYY_MM <= '" + szYm2 + "' " + whereCha + ")   ");
			sbSQL.append("select PREF_CD AS SORT,max(CHIKU_MEI) as \"都道府県\"" + colItem);
			sbSQL.append("from WORK left outer join M_AREA on PREF_CD = CHIKU_CD ");
			sbSQL.append("group by PREF_CD,CHIKU_MEI ");
			sbSQL.append(" union all select 'ZZ99' AS SORT,'合　　計' as \"都道府県\"" + colItem);
			sbSQL.append("from WORK  ");
			sbSQL.append("order by SORT");
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

		String radT2 = jad.getJSONValue(DefineReport.RadT_02.NAME.getObj());

		List<String> cells = new ArrayList<String>();
		// 分析指定-1:単月 3:時系列
		cells.add(DefineReport.RadT_02.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadT_02.NAME.getObj()));
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);
		// 年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELT2.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		if (StringUtils.equals(radT2, DefineReport.RadT_02.ID3.getVal())) {	// 時系列
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN_TO.getObj()));
			getWhere().add(cells);
			// 顧客状態-1:全顧客 2:ACT顧客
			cells = new ArrayList<String>();
			cells.add(DefineReport.RadT_04.NAME.getView());
			cells.add(jad.getJSONText(DefineReport.RadT_04.NAME.getObj()));
			getWhere().add(cells);
			// 表示項目-1:顧客数 2:シェア 4:ACT率
			cells = new ArrayList<String>();
			cells.add(DefineReport.RadT_05.NAME.getView());
			cells.add(jad.getJSONText(DefineReport.RadT_05.NAME.getObj()));
			getWhere().add(cells);
		} else {
			getWhere().add(cells);
		}

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
