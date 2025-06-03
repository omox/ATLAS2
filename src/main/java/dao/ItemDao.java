package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import common.DefineReport;
import common.DefineReport.Select;
import common.JsonArrayData;
import net.sf.json.JSONArray;

/**
 * アプリケーション共通テーブル検索情報取得クラス<br>
 *
 */
public class ItemDao implements ItemInterface {

	/** コネクションです。 */
	private Connection source;
	/** パラメータ関係 */
	private HashMap<String, String> map;
	/** Grid設定 */
	private HashMap<String, String> setting;
	/** json 情報 */
	private String json;
	/** 検索条件（Excel出力用） */
	private ArrayList<List<String>> where;

	/* (non-Javadoc)
	 * @see dao.ItemInterface#getMap()
	 */
	public HashMap<String, String> getMap() {
		return map;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#setMap(java.util.HashMap)
	 */
	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	/*
	 * (non-Javadoc)
	 * @see dao.ItemInterface#getSetting()
	 */
	@Override
	public HashMap<String, String> getSetting() {
		return setting;
	}

	/*
	 * (non-Javadoc)
	 * @see dao.ItemInterface#setSetting(java.lang.String)
	 */
	@Override
	public void setSetting(HashMap<String, String> setting) {
		this.setting = setting;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#getJson()
	 */
	public String getJson() {
		return json;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#setJson(java.lang.String)
	 */
	public void setJson(String json) {
		this.json = json;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#getWhere()
	 */
	public ArrayList<List<String>> getWhere() {
		return where;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#setWhere(java.util.ArrayList)
	 */
	public void setWhere(ArrayList<List<String>> where) {
		this.where = where;
	}

	/** メタ情報（Excel出力用） */
	private ArrayList<Integer> meta;

	/* (non-Javadoc)
	 * @see dao.ItemInterface#getMeta()
	 */
	public ArrayList<Integer> getMeta() {
		return meta;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#setMeta(java.util.ArrayList)
	 */
	public void setMeta(ArrayList<Integer> meta) {
		this.meta = meta;
	}

	/** 開始レコード */
	private int start;

	/* (non-Javadoc)
	 * @see dao.ItemInterface#getStart()
	 */
	public int getStart() {
		return start;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#setStart(int)
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/** 取得レコード数 */
	private int limit;

	/* (non-Javadoc)
	 * @see dao.ItemInterface#getLimit()
	 */
	public int getLimit() {
		return limit;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#setLimit(int)
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/** DB検索用パラメータ配列 */
	private ArrayList<String> paramData;

	/* (non-Javadoc)
	 * @see dao.ItemInterface#getParamData()
	 */
	public ArrayList<String> getParamData() {
		return paramData;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#setParamData(java.util.ArrayList)
	 */
	public void setParamData(ArrayList<String> paramData) {
		this.paramData = paramData;
	}

	/** DB検索結果（0レコード＝タイトル） */
	private ArrayList<List<String>> table;

	/* (non-Javadoc)
	 * @see dao.ItemInterface#getTable()
	 */
	public ArrayList<List<String>> getTable() {
		return table;
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#setTable(java.util.ArrayList)
	 */
	public void setTable(ArrayList<List<String>> table) {
		this.table = table;
	}

	/**
	 * 新しいjdbcItemDaoのインスタンスを生成します。
	 *
	 * @param source
	 */
	public ItemDao(Connection source) {

		this.source = source;

		// 保存用 List (レコード情報)作成
		table = new ArrayList<List<String>>();

		// 配列準備
		paramData = new ArrayList<String>();

		// メタ情報
		meta = new ArrayList<Integer>();
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#selectBy()
	 */
	public boolean selectBy() {
		return selectBySQL("");
	}

	/* (non-Javadoc)
	 * @see dao.ItemInterface#selectBySQL(java.lang.String)
	 */
	public boolean selectBySQL(String command) {
		ResultSet rs = null;
		PreparedStatement statement = null;
		long startTime, stop, diff;

		if ("".equals(command)) {
			return false;
		}
		try {

			// 実行SQL設定
	        statement = source.prepareStatement(command);

			// パラメータ判断
	        setParamData(new ArrayList<String>());
			for (int i = 0; i < getParamData().size(); i++) {
				statement.setString((i + 1), (String) getParamData().get(i));
			}

			startTime = System.currentTimeMillis();

			// SQL実行
			rs = statement.executeQuery();

			stop = System.currentTimeMillis();
			diff = stop - startTime;
			System.out.println("実行時間 : " + diff + "ミリ秒");

			// カラム数
			ResultSetMetaData rsmd = rs.getMetaData();
			int sizeColumn = rsmd.getColumnCount();

			// タイトル名称取得
			List<String> titles = new ArrayList<String>();
			for (int i = 1; i <= sizeColumn; i++) {
				titles.add(rsmd.getColumnName(i));
			}
			getTable().add(titles);

			// メタ情報（名称）
			for (int i = 1; i <= sizeColumn; i++) {
				meta.add(rsmd.getColumnType(i)); // 列の SQL 型
			}

			// 結果の取得
			int count = 0;

			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(1);
			df.setMinimumFractionDigits(1);

			while (rs.next()) {

				count++;

				// 情報保存
				List<String> cols = new ArrayList<String>();

				for (int i = 1; i <= sizeColumn; i++) {

					// タイプ別取得
					switch(rsmd.getColumnType(i)) {
					case Types.CHAR:
					case Types.VARCHAR:
						cols.add(rs.getString(i));
						break;
					case Types.DECIMAL :
						if (rsmd.getScale(i) == 0) {
							cols.add(Integer.toString((int) rs.getDouble(i)));
						} else {
							cols.add(df.format(rs.getDouble(i)));
						}

						break;
					case Types.INTEGER:
						cols.add(String.valueOf(rs.getInt(i)));
						break;

//					case Types.NUMERIC:
//						System.out.println(rsmd.getScale(i) + "/"+ rs.getString(i)+"="+String.valueOf(rs.getDouble(i)));
//						if (null == rs.getString(i)) {
//							cols.add("");
//						}else{
//							cols.add(String.valueOf(rs.getDouble(i)));
//						}
//						break;

					case Types.DOUBLE:
					default:
						if (null == rs.getString(i)) {
							cols.add("");
						} else {
							cols.add(rs.getString(i));
						}
					}

				}

				// 情報保存（レコード）
				getTable().add(cols);
			}


			return true;

		} catch (SQLException e) {
			rollback(source);
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			close(rs);
			close(statement);
			close(source);
		}
		return false;
	}

	/**
	 * コミットします。
	 *
	 * @param conn コネクション
	 */
	@SuppressWarnings("unused")
	private void commit(Connection conn) {
		if (conn != null) {
			try {
			  conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ロールバックします。
	 *
	 * @param conn コネクション
	 */
	private void rollback(Connection conn) {
		if (conn != null) {
			try {
			  conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * コネクションをクローズします。
	 *
	 * @param conn コネクション
	 */
	private void close(Connection conn) {
		if (conn != null) {
			try {
			  conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ステートメントをクローズします。
	 *
	 * @param statement ステートメント
	 */
	private void close(PreparedStatement statement) {
		if (statement != null) {
			try {
			  statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 結果セットをクローズします。
	 *
	 * @param rs 結果セット
	 */
	private void close(ResultSet rs) {
		if (rs != null) {
			try {
			  rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 検索条件表示用情報の生成
	 */
	protected void cmnOutputQueryList() {

		// 検索条件の加工クラス作成
		JsonArrayData jad = new JsonArrayData();
		jad.setJsonString(getJson());

		// 保存用 List (検索情報)作成
		setWhere(new ArrayList<List<String>>());


		for (String id : jad.getIds()) {
			List<String> cells = new ArrayList<String>();
			if (id.equals(DefineReport.ID_HIDDEN_REPORT_NAME)) {
				// タイトル名称
				cells.add(jad.getJSONText(id));
				getWhere().add(0, cells);
			} else if (id.equals(DefineReport.Label.CHECK.getObj())) {
				// レポート更新日時
				cells.add("");
				cells.add("");
				cells.add("");
				cells.add("");
				cells.add(jad.getJSONText(id));
				getWhere().add(1, cells);
			} else if (StringUtils.startsWith(id, "select")) {
				if (!StringUtils.endsWith(id, "to")) {
					for (Select e : Select.values()) {
						if (StringUtils.equals(e.getObj(), id)) {
							cells.add(e.getView());
							break;
						}
					}
					if (StringUtils.endsWith(id, "from")) {
						cells.add(jad.getJSONText(id));
						String id2 = StringUtils.replace(id, "from", "to");
						cells.add("～");
						cells.add(jad.getJSONText(id2));
					} else {
						cells.add(jad.getJSONText(id));
					}
					getWhere().add(cells);
				}
			} else {
				cells.add(jad.getJSONText(id));
				cells.add(jad.getJSONValue(id));
				getWhere().add(cells);
			}
		}

		// 空白行
		List<String> cells = new ArrayList<String>();
		cells.add("");
		getWhere().add(cells);
	}

	/**
	 * 検索条件表示用情報の共通箇所設定
	 *
	 * @param jad 検索条件
	 */
	protected void setCmnOutputQuery(JsonArrayData jad) {
		// タイトル名称
		List<String> cells = new ArrayList<String>();
		cells.add(jad.getJSONText(DefineReport.ID_HIDDEN_REPORT_NAME));
		getWhere().add(0, cells);

		// レポート更新日時
		cells = new ArrayList<String>();
		cells.add("");
		cells.add("");
		cells.add("");
		cells.add("");
		cells.add(jad.getJSONText(DefineReport.Label.CHECK.getObj()));
		getWhere().add(1, cells);

		//空白行
		cells = new ArrayList<String>();
		cells.add("");
		getWhere().add(cells);
	}

	protected static String convChk(HashMap<String, String> map, String key) {
		if (StringUtils.equals(map.get(key), "on")) {
			return "1";
		}
		return "0";
	}

	/**
	 * 検索条件の出力ログ文を取得(map)
	 *
	 * @return 検索条件の内容
	 */
	protected String getConditionLog() {
		Object[] items = null;
		Map<String, String> m = new TreeMap<String, String>(getMap());
		for (String key : m.keySet()) {
			items = ArrayUtils.add(items, key + ":" + m.get(key));
		}
		return StringUtils.join(items, ",");
	}

	/**
	 * 画面取得カンマ区切り検索条件をDB検索条件の形式に変換する。
	 *
	 * @param text 画面取得カンマ区切り検索条件
	 * @return 検索条件の内容
	 */
	protected static String convCommaString(String text) {
		if (StringUtils.isEmpty(text)) {
			return "";
		}
		JSONArray dataArray = JSONArray.fromObject(StringUtils.split(text, ','));
		String convData = "";
		for (int i = 0; i < dataArray.size(); i++) {
			convData += "'" + dataArray.get(i).toString() + "',";
		}
		return StringUtils.removeEnd(convData, ",");
	}

	/**
	 * 数値文字列を引数形式でフォーマットする。
	 *
	 * @param val 数値文字列
	 * @param format フォーマット
	 * @return 変換文字列
	 */
	protected String convFormat(String val, String format) {
		double dValue = NumberUtils.toDouble(val);
		DecimalFormat dFormat = new DecimalFormat(format);
		return dFormat.format(dValue);
	}

	/**
	 * 商品分類別SQL文を生成します。
	 *
	 * @param cmnSelect 共通SELECT
	 * @param cmnFrom 共通FROM
	 * @param fromAll 商品全表示用FROM
	 * @param prefix MAPキー
	 * @return 商品分類別SQL
	 */
	protected String getSelectShohin(String cmnSelect, String cmnFrom, String fromAll, String prefix) {
		String szChkS2 = getMap().get(prefix + "2");	// 商品集計-大分類
		String szChkS3 = getMap().get(prefix + "3");	// 商品集計-中分類
		String szChkS4 = getMap().get(prefix + "4");	// 商品集計-小分類
		String szChkS5 = getMap().get(prefix + "5");	// 商品集計-シリーズＡ
		String szChkS6 = getMap().get(prefix + "6");	// 商品集計-シリーズＢ
		String szChkS7 = getMap().get(prefix + "7");	// 商品集計-商品

		String[] as = { " as \"基本分類\"", " as \"大分類\"", " as \"中分類\"", " as \"小分類\"", " as \"シリーズＡ\"", " as \"シリーズＢ\"", " as \"商品番号\"",
				" as \"商品CD\"", " as \"商品名称\"" };
		String label = "||'：全体'";

		String whereSho1 = " where substr(W.SBSHBUN,1 ,2) = substr(S01.SBSHBUN,1 ,2) and W.KBN = S01.KBN and S01.KBN <= 1";
		String groupSho1 = " group by S01.BUN1_SHOHIN_CD";
		String whereSho2 = " where substr(W.SBSHBUN,1 ,4) = substr(S01.SBSHBUN,1 ,4) and W.KBN = S01.KBN and S01.KBN = 2";
		String groupSho2 = groupSho1 + ",S01.BUN2_SHOHIN_CD ";
		String whereSho3 = " where substr(W.SBSHBUN,1 ,6) = substr(S01.SBSHBUN,1 ,6) and W.KBN = S01.KBN and S01.KBN = 3";
		String groupSho3 = groupSho2 + ",S01.BUN3_SHOHIN_CD ";
		String whereSho4 = " where substr(W.SBSHBUN,1 ,8) = substr(S01.SBSHBUN,1 ,8) and W.KBN = S01.KBN and S01.KBN = 4";
		String groupSho4 = groupSho3 + ",S01.BUN4_SHOHIN_CD ";
		String whereSho5 = " where substr(W.SBSHBUN,1,10) = substr(S01.SBSHBUN,1,10) and W.KBN = S01.KBN and S01.KBN = 5";
		String groupSho5 = groupSho4 + ",S01.BUN5_SHOHIN_CD ";
		String whereSho6 = " where substr(W.SBSHBUN,1,12) = substr(S01.SBSHBUN,1,12) and W.KBN = S01.KBN and S01.KBN = 6";
		String groupSho6 = groupSho5 + ",S01.BUN6_SHOHIN_CD ";
		String whereSho7 = " where trim(W.SBSHBUN) = trim(S01.SBSHBUN) and W.KBN = S01.KBN and S01.KBN = 10";
		String groupSho7 = groupSho6 + ",S01.SHOHIN_BANGO,S01.SBSHBUN ";

		String grade = "";
		String sqlItem1 = "";
		String sqlItem2 = "";
		String sqlItem3 = "";
		String sqlItem4 = "";
		String sqlItem5 = "";
		String sqlItem6 = "";
		String sqlItem7 = "";
		if (StringUtils.equals(szChkS7, "on")) {
			grade = "2a";
			sqlItem1 = "select S01.BUN1_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + label + as[0];
			sqlItem1 += " ,''" + as[1] + ",''" + as[2] + ",''" + as[3] + ",''" + as[4] + ",''" + as[5];
			sqlItem1 += " ,''" + as[6] + ",''" + as[7] + ",''" + as[8];
			sqlItem1 += cmnSelect + cmnFrom + whereSho1 + groupSho1;
			if (StringUtils.equals(szChkS2, "on")) {
				grade = "2b2";
				sqlItem2 = " union all ";
				sqlItem2 += "select S01.BUN2_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem2 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + label + as[1] + ",''" + as[2] + ",''" + as[3];
				sqlItem2 += " ,''" + as[4] + ",''" + as[5] + ",''" + as[6] + ",''" + as[7] + ",''" + as[8];
				sqlItem2 += cmnSelect + cmnFrom + whereSho2 + groupSho2;
			}
			if (StringUtils.equals(szChkS3, "on")) {
				grade = "2b3";
				sqlItem3 = " union all ";
				sqlItem3 += "select S01.BUN3_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem3 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + label + as[2];
				sqlItem3 += " ,''" + as[3] + ",''" + as[4] + ",''" + as[5] + ",''" + as[6] + ",''" + as[7] + ",''" + as[8];
				sqlItem3 += cmnSelect + cmnFrom + whereSho3 + groupSho3;
			}
			if (StringUtils.equals(szChkS4, "on")) {
				grade = "2b4";
				sqlItem4 = " union all ";
				sqlItem4 += "select S01.BUN4_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem4 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
				sqlItem4 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + label + as[3] + ",''" + as[4] + ",''" + as[5];
				sqlItem4 += " ,''" + as[6] + ",''" + as[7] + ",''" + as[8];
				sqlItem4 += cmnSelect + cmnFrom + whereSho4 + groupSho4;
			}
			if (StringUtils.equals(szChkS5, "on")) {
				grade = "2b5";
				sqlItem5 = " union all ";
				sqlItem5 += "select S01.BUN5_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem5 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
				sqlItem5 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + as[3] + ",TRIM(MAX(S01.BUN5_SHOHIN_MEI))" + label + as[4];
				sqlItem5 += " ,''" + as[5] + ",''" + as[6] + ",''" + as[7] + ",''" + as[8];
				sqlItem5 += cmnSelect + cmnFrom + whereSho5 + groupSho5;
			}
			if (StringUtils.equals(szChkS6, "on")) {
				grade = "2b6";
				sqlItem6 = " union all ";
				sqlItem6 += "select S01.BUN6_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem6 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
				sqlItem6 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + as[3] + ",TRIM(MAX(S01.BUN5_SHOHIN_MEI))" + as[4];
				sqlItem6 += " ,TRIM(MAX(S01.BUN6_SHOHIN_MEI))" + label + as[5] + ",''" + as[6] + ",''" + as[7] + ",''" + as[8];
				sqlItem6 += cmnSelect + cmnFrom + whereSho6 + groupSho6;
			}
			sqlItem7 = " union all ";
			sqlItem7 += "select S01.BUN6_SHOHIN_CD||S01.SHOHIN_BANGO as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
			sqlItem7 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
			sqlItem7 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + as[3] + ",TRIM(MAX(S01.BUN5_SHOHIN_MEI))" + as[4];
			sqlItem7 += " ,TRIM(MAX(S01.BUN6_SHOHIN_MEI))" + as[5];
			sqlItem7 += " ,TRIM(S01.SHOHIN_BANGO)" + as[6] + ",''''||TRIM(S01.SBSHBUN)" + as[7] + ",TRIM(MAX(S01.SHOHIN_MEI))" + as[8];
			if (StringUtils.isEmpty(fromAll)) {
				sqlItem7 += cmnSelect + cmnFrom + whereSho7 + groupSho7;
			} else {
				sqlItem7 += cmnSelect + fromAll + groupSho7;
			}
		} else if (StringUtils.equals(szChkS6, "on")) {
			grade = "3a";
			sqlItem1 = "select S01.BUN1_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + label + as[0];
			sqlItem1 += " ,''" + as[1] + ",''" + as[2] + ",''" + as[3] + ",''" + as[4] + ",''" + as[5];
			sqlItem1 += cmnSelect + cmnFrom + whereSho1 + groupSho1;
			if (StringUtils.equals(szChkS2, "on")) {
				grade = "3b2";
				sqlItem2 = " union all ";
				sqlItem2 += "select S01.BUN2_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem2 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + label + as[1] + ",''" + as[2] + ",''" + as[3] + ",''" + as[4] + ",''" + as[5];
				sqlItem2 += cmnSelect + cmnFrom + whereSho2 + groupSho2;
			}
			if (StringUtils.equals(szChkS3, "on")) {
				grade = "3b3";
				sqlItem3 = " union all ";
				sqlItem3 += "select S01.BUN3_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem3 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + label + as[2];
				sqlItem3 += " ,''" + as[3] + ",''" + as[4] + ",''" + as[5];
				sqlItem3 += cmnSelect + cmnFrom + whereSho3 + groupSho3;
			}
			if (StringUtils.equals(szChkS4, "on")) {
				grade = "3b4";
				sqlItem4 = " union all ";
				sqlItem4 += "select S01.BUN4_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem4 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
				sqlItem4 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + label + as[3] + ",''" + as[4] + ",''" + as[5];
				sqlItem4 += cmnSelect + cmnFrom + whereSho4 + groupSho4;
			}
			if (StringUtils.equals(szChkS5, "on")) {
				grade = "3b5";
				sqlItem5 = " union all ";
				sqlItem5 += "select S01.BUN5_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem5 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
				sqlItem5 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + as[3] + ",TRIM(MAX(S01.BUN5_SHOHIN_MEI))" + label + as[4];
				sqlItem5 += " ,''" + as[5];
				sqlItem5 += cmnSelect + cmnFrom + whereSho5 + groupSho5;
			}
			sqlItem6 = " union all ";
			sqlItem6 += "select S01.BUN6_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
			sqlItem6 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
			sqlItem6 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + as[3] + ",TRIM(MAX(S01.BUN5_SHOHIN_MEI))" + as[4];
			sqlItem6 += " ,TRIM(MAX(S01.BUN6_SHOHIN_MEI))" + as[5];
			sqlItem6 += cmnSelect + cmnFrom + whereSho6 + groupSho6;
		} else if (StringUtils.equals(szChkS5, "on")) {
			grade = "4a";
			sqlItem1 = "select S01.BUN1_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + label + as[0];
			sqlItem1 += " ,''" + as[1] + ",''" + as[2] + ",''" + as[3] + ",''" + as[4];
			sqlItem1 += cmnSelect + cmnFrom + whereSho1 + groupSho1;
			if (StringUtils.equals(szChkS2, "on")) {
				grade = "4b2";
				sqlItem2 = " union all ";
				sqlItem2 += "select S01.BUN2_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem2 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + label + as[1] + ",''" + as[2] + ",''" + as[3] + ",''" + as[4];
				sqlItem2 += cmnSelect + cmnFrom + whereSho2 + groupSho2;
			}
			if (StringUtils.equals(szChkS3, "on")) {
				grade = "4b3";
				sqlItem3 = " union all ";
				sqlItem3 += "select S01.BUN3_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem3 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + label + as[2];
				sqlItem3 += " ,''" + as[3] + ",''" + as[4];
				sqlItem3 += cmnSelect + cmnFrom + whereSho3 + groupSho3;
			}
			if (StringUtils.equals(szChkS4, "on")) {
				grade = "4b4";
				sqlItem4 = " union all ";
				sqlItem4 += "select S01.BUN4_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem4 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
				sqlItem4 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + label + as[3] + ",''" + as[4];
				sqlItem4 += cmnSelect + cmnFrom + whereSho4 + groupSho4;
			}
			sqlItem5 = " union all ";
			sqlItem5 += "select S01.BUN5_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
			sqlItem5 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
			sqlItem5 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + as[3] + ",TRIM(MAX(S01.BUN5_SHOHIN_MEI))" + as[4];
			sqlItem5 += cmnSelect + cmnFrom + whereSho5 + groupSho5;
		} else if (StringUtils.equals(szChkS4, "on")) {
			grade = "5a";
			sqlItem1 = "select S01.BUN1_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + label + as[0];
			sqlItem1 += " ,''" + as[1] + ",''" + as[2] + ",''" + as[3];
			sqlItem1 += cmnSelect + cmnFrom + whereSho1 + groupSho1;
			if (StringUtils.equals(szChkS2, "on")) {
				grade = "5b2";
				sqlItem2 = " union all ";
				sqlItem2 += "select S01.BUN2_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem2 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + label + as[1] + ",''" + as[2] + ",''" + as[3];
				sqlItem2 += cmnSelect + cmnFrom + whereSho2 + groupSho2;
			}
			if (StringUtils.equals(szChkS3, "on")) {
				grade = "5b3";
				sqlItem3 = " union all ";
				sqlItem3 += "select S01.BUN3_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem3 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + label + as[2];
				sqlItem3 += " ,''" + as[3];
				sqlItem3 += cmnSelect + cmnFrom + whereSho3 + groupSho3;
			}
			sqlItem4 = " union all ";
			sqlItem4 += "select S01.BUN4_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
			sqlItem4 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
			sqlItem4 += " ,TRIM(MAX(S01.BUN4_SHOHIN_MEI))" + as[3];
			sqlItem4 += cmnSelect + cmnFrom + whereSho4 + groupSho4;
		} else if (StringUtils.equals(szChkS3, "on")) {
			grade = "6a";
			sqlItem1 = "select S01.BUN1_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + label + as[0];
			sqlItem1 += " ,''" + as[1] + ",''" + as[2];
			sqlItem1 += cmnSelect + cmnFrom + whereSho1 + groupSho1;
			if (StringUtils.equals(szChkS2, "on")) {
				grade = "6b2";
				sqlItem2 = " union all ";
				sqlItem2 += "select S01.BUN2_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
				sqlItem2 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + label + as[1] + ",''" + as[2];
				sqlItem2 += cmnSelect + cmnFrom + whereSho2 + groupSho2;
			}
			sqlItem3 = " union all ";
			sqlItem3 += "select S01.BUN3_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
			sqlItem3 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1] + ",TRIM(MAX(S01.BUN3_SHOHIN_MEI))" + as[2];
			sqlItem3 += cmnSelect + cmnFrom + whereSho3 + groupSho3;
		} else if (StringUtils.equals(szChkS2, "on")) {
			grade = "7on";
			sqlItem1 = "select S01.BUN1_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + label + as[0];
			sqlItem1 += ",''" + as[1];
			sqlItem1 += cmnSelect + cmnFrom + whereSho1 + groupSho1;
			sqlItem2 = " union all ";
			sqlItem2 += "select S01.BUN2_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
			sqlItem2 += " ,TRIM(MAX(S01.BUN2_SHOHIN_MEI))" + as[1];
			sqlItem2 += cmnSelect + cmnFrom + whereSho2 + groupSho2;
		} else {
			grade = "7off";
			sqlItem1 = "select S01.BUN1_SHOHIN_CD||'ZZZZ' as SORT,TRIM(MAX(S01.BUN1_SHOHIN_MEI))" + as[0];
			sqlItem1 += cmnSelect + cmnFrom + whereSho1 + groupSho1;
		}
		System.out.println("grade:" + grade);
		return sqlItem1 + sqlItem2 + sqlItem3 + sqlItem4 + sqlItem5 + sqlItem6 + sqlItem7;
	}

	/**
	 * 商品分類Where句を生成します。
	 *
	 * @param prefix MAPキー
	 * @return 商品分類Where句
	 */
	protected String getWhereShohinBunrui(String prefix) {
		String szSho1 = getMap().get(prefix + "1");		// 商品分類-基本分類
		String szSho2 = getMap().get(prefix + "2");		// 商品分類-大分類
		String szSho3 = getMap().get(prefix + "3");		// 商品分類-中分類
		String szSho4 = getMap().get(prefix + "4");		// 商品分類-小分類
		String szSho5 = getMap().get(prefix + "5");		// 商品分類-シリーズＡ
		String szSho6 = getMap().get(prefix + "6");		// 商品分類-シリーズＢ

		String sqlWhereSho = "";
		if (!StringUtils.equals(szSho1, "00")) {
			sqlWhereSho = " and S01.BUN1_SHOHIN_CD = '" + szSho1 + "' ";
		}
		if (!StringUtils.equals(szSho2, "00")) {
			sqlWhereSho = " and S01.BUN2_SHOHIN_CD = '" + szSho2 + "' ";
		}
		if (!StringUtils.equals(szSho3, "00")) {
			sqlWhereSho = " and S01.BUN3_SHOHIN_CD = '" + szSho3 + "' ";
		}
		if (!StringUtils.equals(szSho4, "00")) {
			sqlWhereSho = " and S01.BUN4_SHOHIN_CD = '" + szSho4 + "' ";
		}
		if (!StringUtils.equals(szSho5, "00")) {
			sqlWhereSho = " and S01.BUN5_SHOHIN_CD = '" + szSho5 + "' ";
		}
		if (!StringUtils.equals(szSho6, "00")) {
			sqlWhereSho = " and S01.BUN6_SHOHIN_CD = '" + szSho6 + "' ";
		}
		return sqlWhereSho;
	}
}
