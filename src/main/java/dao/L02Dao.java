package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * L02テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class L02Dao extends ItemDao {

	/**
	 * 新しいT03SegDaoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public L02Dao(Connection source) {
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
		String szCategory1	=	getMap().get("CATEGORY1");			// 1:商品マスタ2;初回商品

		// パラメータ確認
		System.out.println("YYYY_MM:" +szYm+",szCategory1:"+szCategory1);

		// 共通Where句
		String sqlSort = " ";
		String sqlItem1 = " ";
		String sqlItem2 = " ";
		String sqlJoin = " ";
		String sqlWhere = " ";
		if(StringUtils.equals(szCategory1, "1")){            //1:商品マスタ
			sqlSort = " START_YM||SH01.BUN1_SHOHIN_CD||SH01.BUN2_SHOHIN_CD||SH01.BUN3_SHOHIN_CD||SH01.BUN4_SHOHIN_CD||SH01.BUN5_SHOHIN_CD||SH01.SHOHIN_CD ";
			sqlItem1 = "SH01.SHOHIN_CD AS 商品CD, SH01.SHOHIN_BANGO AS 商品番号,SH01.SHOHIN_MEI AS 商品名,";
			sqlJoin = " LEFT OUTER JOIN ";
			sqlWhere = " where SH01.START_YM >= '"+szYm+"'";
		}else {
			sqlSort = " SHOHIN_GRP_CD||SH01.BUN1_SHOHIN_CD||SH01.BUN2_SHOHIN_CD||SH01.BUN3_SHOHIN_CD||SH01.BUN4_SHOHIN_CD||SH01.BUN5_SHOHIN_CD||SH01.SHOHIN_CD ";
			sqlItem1 = "SH01.SHOHIN_CD AS 商品CD, SH01.SHOHIN_BANGO AS 商品番号,SH01.SHOHIN_MEI AS 商品名,";
			sqlItem2 = " ";
			sqlJoin = " INNER JOIN ";
			sqlWhere = " where SH01.START_YM >= '"+szYm+"'";
		}

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append(" select "+sqlSort+" AS SORT,");
		sbSQL.append(sqlItem1);
		sbSQL.append(" SH01.BUN1_SHOHIN_CD AS 分類1,SH01.BUN1_SHOHIN_MEI 分類名1,");
		sbSQL.append(" SUBSTR(SH01.BUN2_SHOHIN_CD,3,2) AS 分類2,SH01.BUN2_SHOHIN_MEI 分類名2,");
		sbSQL.append(" SUBSTR(SH01.BUN3_SHOHIN_CD,5,2) AS 分類3,SH01.BUN3_SHOHIN_MEI 分類名3,");
		sbSQL.append(" SUBSTR(SH01.BUN4_SHOHIN_CD,7,2) AS 分類4,SH01.BUN4_SHOHIN_MEI 分類名4,");
		sbSQL.append(" SUBSTR(SH01.BUN5_SHOHIN_CD,9,2) AS 分類5,SH01.BUN5_SHOHIN_MEI 分類名5,");
		sbSQL.append(" SUBSTR(SH01.BUN6_SHOHIN_CD,11,2) AS 分類6,SH01.BUN6_SHOHIN_MEI 分類名6,");
		sbSQL.append(" SH01.START_YM AS 発売年月");
		sbSQL.append(sqlItem2);
		sbSQL.append("  from M_SHOHIN SH01 "+sqlJoin+" M_SHOHIN_GRP_TAI SG01 ON ");
		sbSQL.append("  (SH01.BUN6_SHOHIN_CD=SG01.SHOHIN_BUNRUI_CD OR SH01.SHOHIN_CD=SG01.SHOHIN_CD)");
		sbSQL.append(sqlWhere);
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

		// 基準年月
		cells = new ArrayList<String>();
		cells.add( DefineReport.Label.LABELB4p.getView());
		cells.add( jad.getJSONText(DefineReport.Select.YM_NR.getObj()) );
		getWhere().add(cells);

		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);

	}

}
