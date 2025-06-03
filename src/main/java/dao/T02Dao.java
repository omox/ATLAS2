package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T02テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T02Dao extends ItemDao {

	/**
	 * 新しいT02Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T02Dao(Connection source) {
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
		String szYm = getMap().get("YYYY_MM");		// 年月
		String szType1 = getMap().get("TYPE1");		// 表示形式-1:F0顧客×登録経過月数 2:年齢×登録経過月数
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル

		// パラメータ確認
		if ((szYm == null) || (szType1 == null) || (szChanel == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		String[] itemC = { "AS \"3ヶ月\"", "AS \"4-6ヶ月\"", "AS \"7-12ヶ月\"", "AS \"13-24ヶ月\"", "AS \"25ヶ月以上\"", "AS \"合計\"" };
		// 設定
		String sqlItem = "";
		String sqlItem2 = "";
		String sqlItem3 = "";
		String sqlItem4 = "";
		if (StringUtils.equals(szType1, DefineReport.OptionType10.OPTION1.getVal())) {
			sqlItem = "F0TYPE";
			sqlItem2 = "max(F0TYPE_ORDER) AS SORT,MAX(F0TYPE_MEI) AS \"F0顧客\"";
			sqlItem3 = "FROM M_F0TYPE LEFT OUTER JOIN WORKT ON F0TYPE =F0TYPET GROUP BY F0TYPE";
			sqlItem4 = " 999 ";
		} else {
			sqlItem = "AGE";
			sqlItem2 = "SEDAI_CD AS SORT,MAX(SEDAI_MEI) AS \"世代\"";
			sqlItem3 = "FROM M_AGE LEFT OUTER JOIN WORKT ON AGE =AGET GROUP BY SEDAI_CD";
			sqlItem4 = " 'Z999' ";
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("WITH WORKT(" + sqlItem + "T,M01,M02,M03,M04,M05,M06) AS (");
		sbSQL.append(" SELECT " + sqlItem);
		sbSQL.append(" ,CASE WHEN TOROKUM >=1 AND TOROKUM <=3 THEN sum(KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN TOROKUM >=4 AND TOROKUM <=6 THEN sum(KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN TOROKUM >=7 AND TOROKUM <=12 THEN sum(KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN TOROKUM >=13 AND TOROKUM <=24 THEN sum(KYAKUSU) ELSE 0 END");
		sbSQL.append(" ,CASE WHEN TOROKUM >=25 THEN sum(KYAKUSU) ELSE 0 END,SUM(KYAKUSU)");
		sbSQL.append(" FROM T02_F0_KYAKU WHERE YYYY_MM = '" + szYm + "' AND HAN_CHAN = '" + szChanel + "' ");
		sbSQL.append(" GROUP BY " + sqlItem + ",TOROKUM) ");
		sbSQL.append("SELECT " + sqlItem2 + ",SUM(M01) " + itemC[0] + ",SUM(M02) " + itemC[1] + ",SUM(M03) " + itemC[2] + ",SUM(M04) " + itemC[3]
				+ ",SUM(M05) " + itemC[4] + ",SUM(M06) " + itemC[5]);
		sbSQL.append(sqlItem3);
		sbSQL.append(" UNION ALL ");
		sbSQL.append("SELECT " + sqlItem4 + " AS SORT,'計',SUM(M01) " + itemC[0] + ",SUM(M02) " + itemC[1] + ",SUM(M03) " + itemC[2]
				+ ",SUM(M04) " + itemC[3] + ",SUM(M05) " + itemC[4] + ",SUM(M06) " + itemC[5] + " FROM WORKT ");
		sbSQL.append("order by SORT");

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

		List<String> cells = new ArrayList<String>();
		// 基準年月
		cells.add(DefineReport.Select.YM_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()));
		getWhere().add(cells);
		// 販売チャネル
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHANEL.getView());
		cells.add(jad.getJSONText(DefineReport.Select.CHANEL.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
