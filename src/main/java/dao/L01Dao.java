package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * L01テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class L01Dao extends ItemDao {

	/**
	 * 新しいT03SegDaoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public L01Dao(Connection source) {
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

		String command = null;

		String szYm 		=	getMap().get("YYYY_MM");			// 基準年月
		String szYd 		=	getMap().get("YYYY_DD");			// 基準年月日
		String szCategory1	=	getMap().get("CATEGORY1");			// 1:時間2;日3:月
		String szCategory2	=	getMap().get("CATEGORY2");			// 1:メニュー2:ユーザー3:all

		// パラメータ確認
		System.out.println("YYYY_MM:" +szYm+",YYYY_DD:"+szYd+",szCategory1:"+szCategory1+",szCategory2:"+szCategory2);

		// 共通Where句
		String sqlWhere = " ";
		String sqlItem1 = " ";
		String sqlItem2 = " ";
		String sqlGro = " ";
		String sqlSort = " ";

		if(StringUtils.equals(szCategory1, "1")){
			// 1:時間
			sqlItem1 = " TO_CHAR(DT_ACTION,'YYYY-MM-DD HH24:MI:SS') ";
			sqlWhere = " AND DT_ACTION >= TO_DATE('"+szYd+"','YYYYMMDD') AND DT_ACTION < TO_DATE('"+szYd+"','YYYYMMDD')+1 ";
		}else if (StringUtils.equals(szCategory1, "2")){
			// 2;日
			sqlItem1 = " TO_CHAR(DT_ACTION,'YYYY-MM-DD') ";
			sqlWhere = " AND DT_ACTION >= TO_DATE('"+szYd+"','YYYYMMDD') AND DT_ACTION < TO_DATE('"+szYd+"','YYYYMMDD')+1 ";
		}else if (StringUtils.equals(szCategory1, "3")){
			// 3:月日
			sqlItem1 = " TO_CHAR(DT_ACTION,'YYYY-MM-DD') ";
			sqlWhere = " AND DT_ACTION BETWEEN TO_DATE('"+szYm+"01','YYYYMMDD') AND LAST_DAY(TO_DATE('"+szYm+"01','YYYYMMDD')) ";
		}else {
			// 4:月別
			sqlItem1 = " TO_CHAR(DT_ACTION,'YYYY-MM') ";
			sqlWhere = " AND DT_ACTION BETWEEN TO_DATE('"+szYm+"01','YYYYMMDD') AND LAST_DAY(TO_DATE('"+szYm+"01','YYYYMMDD')) ";
		}

		if(StringUtils.equals(szCategory2, "1")){
			// 1:メニュー
			sqlItem2 = "MAX(CASE WHEN CD_REPORT_NO < 500 THEN '旧' END || TRIM(REPORT_ORD) || ' ' || NM_REPORT) AS 名称,count(NM_REPORT) ";
			sqlGro = ",CD_REPORT_NO ";
			sqlSort = "MAX(REPORT_ORD) ";
		}else if(StringUtils.equals(szCategory2, "2")){
			// 2:ユーザ
			sqlItem2 = "USER_ID AS ID,max(NM_USER) AS 氏名,count(USER_ID) ";
			sqlGro = ",USER_ID";
			sqlSort = "USER_ID";
		}else {
			// 3:ユーザー＋メニュー
			sqlItem2 = "USER_ID AS ID,max(NM_USER) AS 氏名,MAX(CASE WHEN CD_REPORT_NO < 500 THEN '旧' END || TRIM(REPORT_ORD) || ' ' || NM_REPORT)AS 名称,count(USER_ID) ";
			sqlGro = ",USER_ID,CD_REPORT_NO";
			sqlSort = "USER_ID||MAX(REPORT_ORD)";
		}

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("SELECT "+sqlItem1+"||"+sqlSort+" AS SORT,"+sqlItem1+" AS 年月日,"+sqlItem2+" AS 回数");
		sbSQL.append(" FROM AK_SYS_LOGS T1 INNER JOIN AK_SYS_REPORT_NAME T2 ON T1.ID_REPORT = T2.CD_REPORT_NO ");
		sbSQL.append(sqlWhere);
		sbSQL.append(" AND (USER_ID <> 'development_user' and USER_ID <> 'test_user_01')");
		sbSQL.append(" GROUP BY "+sqlItem1+sqlGro);
		sbSQL.append(" ORDER BY SORT");

		System.out.println(sbSQL.toString());
		command = sbSQL.toString();
		return command;
	}


	/**
	 * 検索条件表示用情報の生成
	 */
	private void outputQueryList(){

		// 検索条件の加工クラス作成
		JsonArrayData jad = new JsonArrayData();
		jad.setJsonString(getJson());

		// 保存用 List (検索情報)作成
		setWhere(new ArrayList<List<String>>());

		// タイトル名称
		List<String> cells = new ArrayList<String>();
		cells.add( jad.getJSONText(DefineReport.ID_HIDDEN_REPORT_NAME) );
		getWhere().add(cells);

		// レポート更新日時
		cells = new ArrayList<String>();
		cells.add( "" );
		cells.add( "" );
		cells.add( "" );
		cells.add( "" );
		cells.add( jad.getJSONText(DefineReport.Label.CHECK.getObj()));
		getWhere().add(cells);

		// 基準年月
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.YM_KIJUN.getView());
		cells.add( jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()) );
		getWhere().add(cells);


		//
		cells = new ArrayList<String>();
		cells.add( DefineReport.RadioCategory.NAME.getView());
		cells.add( jad.getJSONText(DefineReport.RadioCategory.NAME.getObj()) );
		getWhere().add(cells);


		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);

	}

}
