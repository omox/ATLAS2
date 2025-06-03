/*
 * 作成日: 2006/08/30
 */
package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import authentication.defines.Form;
import authentication.defines.SQL;
import common.ChkSysTime;
import common.DefineReport;
import common.Defines;
import common.ItemList;
import dao.B01Dao;
import dao.B01bDao;
import dao.B02Dao;
import dao.B04Dao;
import dao.DBConnection;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * レポート画面表示制御クラス <br />
 * レポート画面に表示するレポートを制御するクラス <br />
 */
public class ReportServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -5961333752928159562L;

	/** 共通:セッションがタイムアウトになった際に表示されるエラーメッセージ */
	private final static String ERR_SES_TIMEOUT = "セッションがタイムアウトしました。再度ログインして下さい。";

	/**
	 * 初期化 <br />
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * 破棄 <br />
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * リクエスト時処理 <br />
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * リクエスト時処理 <br />
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 処理準備
		 */
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(); // セッションの取得
		String strPage = new String(); // ページ遷移先

		try {

			/* 利用可能時間チェック */
			ChkSysTime sys = new ChkSysTime();

			if (!sys.isUsefull()) {
				// 利用時間外画面
				strPage = Defines.STR_JSP_CLOSE;
			} else {
				/**
				 * セッションチェック
				 */
				if (session.getAttribute(Defines.ID_REQUEST_USER_ID) == null) {
					/* エラーメッセージの設定 */
					request.setAttribute(Defines.STR_FRM_MSGTYPE, Defines.STR_VAL_MSGTYPE_ERR);
					request.setAttribute(Defines.STR_FRM_MSG, ERR_SES_TIMEOUT);
					// ページ遷移先の設定
					strPage = Defines.STR_JSP_LOGIN;
				} else {
					/**
					 * レポート表示処理
					 */
					// セッション・クリア
					session.removeAttribute(DefineReport.ID_SESSION_STORAGE);
					session.removeAttribute(DefineReport.ID_SESSION_TABLE);
					session.removeAttribute(DefineReport.ID_SESSION_WHERE);
					session.removeAttribute(DefineReport.ID_SESSION_META);
					session.removeAttribute(DefineReport.ID_SESSION_HEADER);

					String inclurepname = (String) request.getSession().getAttribute(Defines.ID_REQUEST_JSP_REPORT);

					// カスタムプロパティの確認
					String[] inclurepnames = StringUtils.split(ObjectUtils.toString(request.getSession().getAttribute(
							Defines.ID_REQUEST_CUSTOM_REPORT)), ",");
					if (ArrayUtils.isNotEmpty(inclurepnames) && !"".equals(inclurepnames[0])) {
						// カスタムプロパティで複数指定の場合、書き換え
						inclurepname = inclurepnames[0];
						request.getSession().setAttribute(Defines.ID_REQUEST_JSP_REPORT, inclurepname);
					}

					/** 前回検索条件の取得 */
					JSONArray results = getSearchConditon(request, inclurepname);
					/** 日付Max・Min条件 */
					JSONArray results2 = getDateDefaultConditon(inclurepname);
					results.addAll(results2);
					// アップロード画面用日付リスト取得処理
					if (inclurepname.equals(DefineReport.ID_PAGE_UPLOAD)) {
						String dateList = ObjectUtils.toString(session.getAttribute(Form.MFU_SEL_YM), "");
						if (StringUtils.isEmpty(dateList)) {
							// リスト設定
							JSONArray rs = ItemList.selectJSONArray(SQL.MOK_SEL_YM, null, Defines.STR_JNDI_DS);
							if (!rs.isEmpty()) {
								dateList = rs.toString();
							}
						}
						session.setAttribute(Form.MFU_SEL_YM, dateList.toString());
					}

					if (results.size() > 0) {
						/** 前回検索条件＋日付MaxMin条件 */
						session.setAttribute(Defines.ID_REQUEST_SEND_PARAM, results.toString());
					}

					/** 遷移先設定 */
					strPage = "/jsp/reports/" + inclurepname + ".jsp";
					// strPage = Defines.STR_JSP_RP;
				}
			}

		} catch (Exception e) {

			/**
			 * 例外処理
			 */
			// スタックトレースの出力
			e.printStackTrace();
			// エラーメッセージの設定
			request.setAttribute(Defines.STR_FRM_MSGTYPE, Defines.STR_VAL_MSGTYPE_ERR);
			request.setAttribute(Defines.STR_FRM_MSG, e.getMessage());
			// ページ遷移先の設定
			strPage = Defines.STR_JSP_ERROR;

		}

		/**
		 * ページ遷移
		 */
		getServletContext().getRequestDispatcher(strPage).forward(request, response);

	}

	private JSONArray getSearchConditon(HttpServletRequest request, String inclurepname) {
		JSONArray results = new JSONArray();

		/** SQL構文 */
		String sqlcommand = null;
		if (inclurepname.equals(DefineReport.ID_PAGE_B_01)) {
			sqlcommand = B01Dao.ID_SQL_SEL_SAVE;
		} else if (inclurepname.equals(DefineReport.ID_PAGE_B_01b)) {
			sqlcommand = B01bDao.ID_SQL_SEL_SAVE;
		} else if (inclurepname.equals(DefineReport.ID_PAGE_B_02)) {
			sqlcommand = B02Dao.ID_SQL_SEL_SAVE;
		} else if (inclurepname.equals(DefineReport.ID_PAGE_B_04)) {
			sqlcommand = B04Dao.ID_SQL_SEL_SAVE;
		}

		if (sqlcommand != null) {
			/** 配列準備 */
			ArrayList<String> paramData = new ArrayList<String>();
			paramData.add(0, (String) request.getSession().getAttribute(Defines.ID_REQUEST_USER_ID));

			/** SQL実行 */
			results = selectJSONArray(sqlcommand, paramData, Defines.STR_JNDI_DS);
		} else {

			/** 前回検索条件の取得 */
			/** SQL構文 */
			sqlcommand = "SELECT SNAPSHOT FROM AK_SYS_SNAPSHOT WHERE CD_USER=? and CD_REPORT=?";

			/** 配列準備 */
			ArrayList<String> paramData = new ArrayList<String>();
			paramData.add(0, (String)request.getSession().getAttribute(Defines.ID_REQUEST_CUSTOM_USER));
			paramData.add(1, (String)request.getSession().getAttribute(Defines.ID_REQUEST_REPORT_NO));

			/** SQL実行 */
			System.out.println("前回検索条件の取得");
			results = selectJSONArray(sqlcommand, paramData, Defines.STR_JNDI_DS);

		}
		return results;

	}

	private JSONArray getDateDefaultConditon(String inclurepname) {
		JSONArray results = new JSONArray();

		/** SQL構文 */
		String sqlcommand = null;
		if (inclurepname.equals(DefineReport.ID_PAGE_B_01) || inclurepname.equals(DefineReport.ID_PAGE_B_01a)
				|| inclurepname.equals(DefineReport.ID_PAGE_B_01b) ) {
			sqlcommand = DefineReport.ID_SQL_INIT_DEFAULT_YM_KIJUN;
		} else if (inclurepname.equals(DefineReport.ID_PAGE_B_03)) {
			sqlcommand = DefineReport.ID_SQL_INIT_DEFAULT_YM_KIJUN_B03;
		} else if (inclurepname.equals(DefineReport.ID_PAGE_B_031)) {
			sqlcommand = DefineReport.ID_SQL_INIT_DEFAULT_DD_KIJUN;
		}

		if (sqlcommand != null) {
			/** SQL実行 */
			results = selectJSONArray(sqlcommand, null, Defines.STR_JNDI_DS);
		}

		return results;
	}

	/**
	 * 検索＆JSON生成
	 *
	 * @param sqlCommand 実行SQL
	 * @param paramData SQLパラメータ
	 * @param jdbcName 接続JNDI名
	 * @return JSON文字列
	 */
	private JSONArray selectJSONArray(String sqlCommand, ArrayList<String> paramData, String jdbcName) {
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
				for (int i = 1; i <= sizeColumn; i++) {
					JSONObject obj = new JSONObject();

					obj.put("id", rsmd.getColumnName(i));
					obj.put("value", rs.getString(i));
					obj.put("text", rs.getString(i));

					// 列データ格納
					json.add(obj);
				}
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
