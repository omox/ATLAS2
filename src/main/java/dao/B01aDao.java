package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T03Segテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B01aDao extends ItemDao {

	/**
	 * 新しいT03SegDaoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public B01aDao(Connection source) {
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
		String szYy = getMap().get("YYYY_YY");			// （必須）基準年
		String szMm = getMap().get("YYYY_MM");			// （必須）基準月
		String szJiseki = getMap().get("JISEKI");			//純売上・出荷売上の区別

		// パラメータ確認
		System.out.println(super.getConditionLog());

		// 共通Where句
		String sqlWhere = " AND UG.URI_YM = '" + szYy + szMm + "'";

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		if (StringUtils.equals(szJiseki, "2")) {
			sbSQL.append("SELECT MG.GYO_ORDER as SORT,MG.GYO_NAME as 項目, COALESCE(UG.MOK_SHU_GK,0) as 計画出荷売上, ");
			sbSQL.append("COALESCE(UG.SHU_URIGK,0) as 出荷売上, ");
			sbSQL.append("case when UG.SHU_URIGK <> 0 and UG.ZYY_SHU_URIGK <> 0 then UG.SHU_URIGK / UG.ZYY_SHU_URIGK  * 100 else 0 end  as 前年比, ");
			sbSQL.append("case when UG.SHU_URIGK <> 0 and UG.MOK_SHU_GK <> 0 then UG.SHU_URIGK / UG.MOK_SHU_GK  * 100 else 0 end  as 達成率, ");
			sbSQL.append("COALESCE(UG.MOK_SHU_GK_R,0) as 年間累計計画, ");
			sbSQL.append("COALESCE(UG.SHU_URIGK_R,0) as 年間累計出荷売上, ");
			sbSQL.append("case when UG.SHU_URIGK_R <> 0 and UG.ZYY_SHU_URIGK_R <> 0 then UG.SHU_URIGK_R / UG.ZYY_SHU_URIGK_R  * 100 else 0 end  as 累計前年比, ");
			sbSQL.append("case when UG.SHU_URIGK_R <> 0 and UG.MOK_SHU_GK_R <> 0 then UG.SHU_URIGK_R / UG.MOK_SHU_GK_R * 100 else 0 end  as 累計達成率 ");
			sbSQL.append("FROM M_GYO_CD MG LEFT JOIN URI_GYOSEKI UG  ");
			sbSQL.append("ON MG.GYO_CODE = UG.GYO_CODE  AND MG.GYO_CODE LIKE 'G%'  ");
			sbSQL.append(sqlWhere);
			sbSQL.append(" WHERE MOK_SHU_GK_R > 0 OR SHU_URIGK_R  > 0  OR MOK_SHU_GK_R  > 0 OR SHU_URIGK_R  > 0   ");
			sbSQL.append(" order by SORT");
		} else {
			sbSQL.append("SELECT MG.GYO_ORDER as SORT,MG.GYO_NAME as 項目, COALESCE(UG.MOK_JUN_GK,0) as 予算, ");
			sbSQL.append("ROUND(COALESCE(UG.JUN_URIGK,0),0) as 純売上, ");
			sbSQL.append("case when UG.JUN_URIGK <> 0 and UG.ZYY_JUN_URIGK <> 0 then UG.JUN_URIGK / UG.ZYY_JUN_URIGK  * 100 else 0 end  as 前年比, ");
			sbSQL.append("case when UG.JUN_URIGK <> 0 and UG.MOK_JUN_GK <> 0 then UG.JUN_URIGK / UG.MOK_JUN_GK  * 100 else 0 end  as 達成率, ");
			sbSQL.append("COALESCE(UG.MOK_JUN_GK_R,0) as 年間累計予算, ");
			sbSQL.append("ROUND(COALESCE(UG.JUN_URIGK_R,0),0) as 年間累計純売上, ");
			sbSQL.append("case when UG.JUN_URIGK_R <> 0 and UG.ZYY_JUN_URIGK_R <> 0 then UG.JUN_URIGK_R / UG.ZYY_JUN_URIGK_R  * 100 else 0 end  as 累計前年比, ");
			sbSQL.append("case when UG.JUN_URIGK_R <> 0 and UG.MOK_JUN_GK_R <> 0 then UG.JUN_URIGK_R / UG.MOK_JUN_GK_R * 100 else 0 end  as 累計達成率 ");
			sbSQL.append("FROM M_GYO_CD MG LEFT JOIN URI_GYOSEKI UG  ");
			sbSQL.append("ON MG.GYO_CODE = UG.GYO_CODE  AND MG.GYO_CODE LIKE 'G%'  ");
			sbSQL.append(sqlWhere);
			sbSQL.append(" WHERE MOK_SHU_GK_R > 0 OR SHU_URIGK_R  > 0  OR MOK_JUN_GK_R  > 0 OR JUN_URIGK_R  > 0   ");
			sbSQL.append(" order by SORT ");
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

		// 基準年月
		List<String> cells = new ArrayList<String>();
		cells.add(DefineReport.Select.YM_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
