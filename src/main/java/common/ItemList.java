/**
 *
 */
package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import dao.DBConnection;

/**
 * @author Omoto_Yuki
 *
 */
public class ItemList {

	/**
	 * 更新処理
	 * 
	 * @param sqlCmammnd 実行SQL
	 * @param paramData SQLパラメータ
	 * @param jdbcName 接続JNDI名
	 * @return boolean
	 * @throws Exception 例外
	 */
	public boolean executeItem(String sqlCmammnd, ArrayList paramData, String jdbcName) throws Exception {

		// 検索
		Connection con = null; // コネクション

		try {
			// コネクションの取得
			con = DBConnection.getConnection(jdbcName);
			con.setAutoCommit(false);

			// 実行SQL設定
			PreparedStatement stmt = con.prepareStatement(sqlCmammnd);

			// パラメータ設定
			for (int i = 0; i < paramData.size(); i++) {
				stmt.setString((i + 1), (String) paramData.get(i));
			}

			// SQL実行
			stmt.executeUpdate();

			con.commit();
			con.close();

		} catch (Exception e) {
			/* 接続解除 */
			if (con != null) {
				try {
					con.rollback();
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 検索＆JSON生成
	 * 
	 * @param sqlCommand 実行SQL
	 * @param paramData SQLパラメータ
	 * @param jdbcName 接続JNDI名
	 * @return JSON文字列
	 */
	public static JSONArray selectJSONArray(String sqlCommand, ArrayList<String> paramData, String jdbcName) {
		JSONArray json = new JSONArray();

		// 検索
		Connection con = null; // コネクション
		try {
			// コネクションの取得
			con = DBConnection.getConnection(jdbcName);

			// 実行SQL設定
			PreparedStatement stmt = con.prepareStatement(sqlCommand);

			// パラメータ判断
			if (paramData != null) {
				for (int i = 0; i < paramData.size(); i++) {
					stmt.setString((i + 1), (String) paramData.get(i));
				}
			}

			// SQL実行
			ResultSet rs = stmt.executeQuery();

			// カラム数
			ResultSetMetaData rsmd = rs.getMetaData();
			int sizeColumn = rsmd.getColumnCount();

			// 結果の取得
			while (rs.next()) {
				JSONObject obj = new JSONObject();

				for (int i = 1; i <= sizeColumn; i++) {
					obj.put(rsmd.getColumnName(i), rs.getString(i));
				}

				// 行データ格納
				json.add(obj);
			}

			con.commit();
			con.close();

		} catch (Exception e) {
			/* 接続解除 */
			if (con != null) {
				try {
					con.rollback();
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}

		return json;
	}
}
