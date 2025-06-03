package authentication.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import authentication.bean.Auth;
import authentication.bean.Position;
import authentication.bean.Report;
import authentication.bean.Side;
import authentication.bean.User;
import authentication.connection.DBConnection;
import authentication.connection.LDAPjndi;
import authentication.dbaccess.DBinfo;
import authentication.defines.Consts;
import authentication.defines.Form;
import authentication.defines.SQL;
import authentication.util.Tagescape;
import authentication.validation.LoginValidation;

/**
 * ATLAS - LoginLdapServlet.java<br/>
 * ログイン処理制御クラス(AD認証)<br/>
 * ログイン・ログオフの処理を制御する。
 *
 * @since 20122012/07/11
 * @author eatone
 */
public class LoginLdapServlet extends HttpServlet {

	private ServletConfig config = null;


	/**
	 * 初期化
	 *
	 * @param config 設定
	 * @throws ServletException サーブレットエラー
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
	}

	/**
	 * 破棄
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * リクエスト時処理
	 *
	 * @param request リクエスト
	 * @param response レスポンス
	 * @throws ServletException サーブレットエラー
	 * @throws IOException IOエラー
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * リクエスト時処理
	 *
	 * @param request リクエスト
	 * @param response レスポンス
	 * @throws ServletException サーブレットエラー
	 * @throws IOException IOエラー
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		/* セッションクリア */
		session.removeAttribute(Consts.STR_SES_LOGINUSER);// LoginUser
		session.removeAttribute(Consts.STR_SES_LOGINPOS);	// LoginUserPosition
		session.removeAttribute(Consts.STR_SES_LOGINATH);	// LoginUserAth
		session.removeAttribute(Consts.STR_SES_REPSIDE);	// Side_main
		session.removeAttribute(Consts.STR_SES_MSTSIDE);	// Side_master

		String strPage = config.getInitParameter("Login");
		// 利用時間の時間取得
		String fromData = config.getServletContext().getInitParameter(Consts.FROM_DATA);
		String toData = config.getServletContext().getInitParameter(Consts.TO_DATA);

		Tagescape esc = new Tagescape();
		String user = "";
		String pass = "";
		String view = "";

		if (request.getParameter(Form.LOGIN_VIEW) != null) {
			user = StringUtils.trim(request.getParameter(Form.LOGIN_USER));
			pass = StringUtils.trim(request.getParameter(Form.LOGIN_PASS));
			view = request.getParameter(Form.LOGIN_VIEW);
			// 情報保持
			session.setAttribute("_" + Form.LOGIN_USER, user);
			session.setAttribute("_" + Form.LOGIN_PASS, pass);
			session.setAttribute("_" + Form.LOGIN_VIEW, view);

		} else {
			user = ObjectUtils.toString(session.getAttribute("_" + Form.LOGIN_USER), "");
			pass = ObjectUtils.toString(session.getAttribute("_" + Form.LOGIN_PASS), "");
			view = ObjectUtils.toString(session.getAttribute("_" + Form.LOGIN_PASS), "");
		}

		// 区分確認
		String KB = "0";
		KB = StringUtils.trim(request.getParameter("KB"));
		if (KB==null) KB="0";

		try {
			if (StringUtils.isNotEmpty(user)) {
				// 失敗時の予備
				session.setAttribute(Consts.STR_SES_LOGINUSER, getLoginUser(StringUtils.lowerCase(esc.htmlEscape(user)), null));
				// ログアウト時に初期化するパラメータ情報
				strPage += "?" + Form.LOGIN_USER + "=" + user + "&" + Form.LOGIN_PASS + "=" + pass + "&" + Form.LOGIN_VIEW + "=" + view;
			}

			LoginValidation close = new LoginValidation();

			// 利用時間の時間判定
			if (close.closeTime(fromData, toData).equals(Consts.CLOSE_TIME)) {
				// 別ページ遷移設定 : Loginへ
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;
			}
			// フォーム値未入力の場合
			if (request.getParameter(Form.LOGIN_USER) == null || request.getParameter(Form.LOGIN_PASS) == null) {
				session.invalidate();
				// 別ページ遷移設定 : Loginへ
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;
			}

			// フォーム値入力有の場合
			user = StringUtils.trim(request.getParameter(Form.LOGIN_USER));
			pass = StringUtils.trim(request.getParameter(Form.LOGIN_PASS));
			// 入力チェック
			LoginValidation vld = new LoginValidation();
			if (!vld.entryLdap(user, pass)) {
				// 入力チェックでエラー項目が検出された場合
				request.setAttribute(Form.COMMON_MSGTYPE, Form.COMMON_MSGTYPE_ERR);
				request.setAttribute(Form.COMMON_MSG, vld.OutMsgShort());

				// 別ページ遷移設定 : Loginへ
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;
			}

			// ------------------------------
			// AD認証
			// ------------------------------
			String userName = null;
			if (!(StringUtils.equalsIgnoreCase(user, Consts.DEVELOPMENT_USER) && StringUtils.equals(pass, Consts.DEVELOPMENT_PASS))) {
				LDAPjndi ldap = new LDAPjndi(config);
				HashMap<String, String> rtn = ldap.connect(user, pass);
				if (StringUtils.equals(rtn.get("success"), "true")) {
					userName = rtn.get("name");
				} else {
					// AD認証の結果、なんらかのエラーが発生した場合
					request.setAttribute(Form.COMMON_MSGTYPE, Form.COMMON_MSGTYPE_ERR);
					request.setAttribute(Form.COMMON_MSG, rtn.get("message"));

					// 別ページ遷移設定 : Loginへ
					getServletContext().getRequestDispatcher(strPage).forward(request, response);
					return;
				}
			}
			// ------------------------------
			// DBより情報取得
			// ------------------------------
			// ユーザー情報取得
			User usr = getLoginUser(StringUtils.lowerCase(user), userName);
			// ロールを保持
			Position position = new Position(usr.getPos(), usr.getPosname_());
			// グループを保持
			Auth auth = new Auth(usr.getGroup(), usr.getGroupname_());
			/** レポート分類メニューでマスタが表示できる権限を持っているかを調べている */
			boolean hander = ArrayUtils.contains(usr.getPoslist(), Integer.toString(Consts.SYSTEM_MASTER));

			// レポート分類メニュー設定(マスタ)
			ArrayList<Side> mst = null;
			if (hander) {
				mst = getReportSideMstSetting(usr, KB);
				// レポート設定(マスタ)
				if (mst != null) {
					setReportMstSetting(mst, usr, KB);
				}
			}
			// レポート分類メニュー設定(マスタ以外)
			ArrayList<Side> menu = getReportSideSetting(usr, KB);
			// レポート設定(マスタ以外)
			ArrayList<String> list4 = new ArrayList<String>();
			if (menu != null) {
				setReportSetting(menu, usr, list4, KB);
			}
			// DB更新状況取得
			DBinfo dbinfo = new DBinfo();
			dbinfo.setLogMenu(usr.getId(), usr.getName());

			// セッションの設定
			session.setAttribute(Consts.STR_SES_LOGINUSER, usr);		// LoginUser
			session.setAttribute(Consts.STR_SES_LOGINPOS, position);// LoginUserPosition
			session.setAttribute(Consts.STR_SES_LOGINATH, auth);		// LoginUserAth
			session.setAttribute(Consts.STR_SES_REPSIDE, menu);			// Side_main
			session.setAttribute("DISP", list4);										// Side_main
			session.setAttribute(Consts.STR_SES_MSTSIDE, mst);			// Side_Master
			session.setAttribute(Consts.STR_HEAD_TYPE, Consts.STR_HEAD_TYPE_ON);	// ヘッダリンク表示ON
			session.setAttribute("KB", KB);	// 表示区分

			if (request.getParameter(Form.LOGIN_VIEW) != null) {
				// 別ページ遷移設定 : Loginへ
				strPage = config.getInitParameter(request.getParameter(Form.LOGIN_VIEW));
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;
			}

			/**
			 * 別ページ遷移設定 : Menu→Dashboardへ
			 */
			strPage = config.getInitParameter("Dashboard");

		} catch (Exception e) {
			/**
			 * 例外処理
			 */
			// スタックトレースの出力
			e.printStackTrace();
			// エラーメッセージの設定
			request.setAttribute(Form.COMMON_MSGTYPE, Form.COMMON_MSGTYPE_ERR);
			request.setAttribute(Form.COMMON_MSG, e.getMessage());
			// ページ遷移先の設定
			strPage = this.getServletContext().getInitParameter("Page_Error");
		}
		/**
		 * ページ遷移
		 */
		getServletContext().getRequestDispatcher(strPage).forward(request, response);
	}

	/**
	 * ログインユーザー情報を取得
	 *
	 * @param userId ユーザーID
	 * @param userName ユーザー名
	 * @return ユーザー情報
	 * @throws Exception 例外
	 */
	private User getLoginUser(String userId, String userName) throws Exception {
		User prmUsr = new User(userId, "dummy");
		Map<?, ?> mu = null;
		//System.out.println("getLoginUser  :" + SQL.USER_SEL_004);
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler<?> rsh = new MapHandler();
			mu = (Map<?, ?>) qr.query(SQL.USER_SEL_004, rsh, new Object[] { prmUsr.getId() });
		} catch (Exception en) {
			System.out.println("DBUtils Exception:LoginServlet_User");
			throw en;
		}

		User usr;
		if (mu == null) {
			usr = new User();
			usr.setCD_user(Consts.USER_NO);
			usr.setId(userId);
			usr.setName(StringUtils.defaultIfEmpty(userName, userId));
			usr.setGroup(Consts.USER_NO);
			usr.setPos(Consts.USER_NO);
			usr.setUsers_custom(Integer.toString(Consts.USER_NO));
			usr.setGrouplist(new String[] { Integer.toString(Consts.USER_NO) });
			usr.setPoslist(new String[] { Integer.toString(Consts.USER_NO) });
		} else {
			usr = new User(
					Integer.parseInt(ObjectUtils.toString(mu.get("CD_USER")).trim()),
					ObjectUtils.toString(mu.get("USER_ID")).trim(),
					ObjectUtils.toString(mu.get("PASSWORDS")).trim(),
					StringUtils.defaultIfEmpty(userName, ObjectUtils.toString(mu.get("uname")).trim()),
					ObjectUtils.toString(mu.get("CD_AUTH")).trim(),
					ObjectUtils.toString(mu.get("DT_PW_TERM")).trim(),
					Integer.parseInt(ObjectUtils.toString(mu.get("CD_GROUP")).trim()),
					Consts.SYSTEM_MASTER,
					ObjectUtils.toString(mu.get("custom_value")).trim(),
					ObjectUtils.toString(mu.get("LOGO")).trim());
			setLoginUsrList(usr);	// 全グループ・ロール取得
		}
		return usr;
	}

	/**
	 * ログインユーザーが持っている全グループ・ロールをセット
	 *
	 * @param usr ユーザー情報
	 * @throws Exception 例外
	 */
	private void setLoginUsrList(User usr) throws Exception {
		List<?> list = null;
		//System.out.println("setLoginUsrList :" + SQL.USER_SEL_004);
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler<?> rsh = new MapListHandler();
			list = (List<?>) qr.query(SQL.USER_SEL_004, rsh, new Object[] { usr.getDBFormatId() });
		} catch (Exception en) {
			System.out.println("DBUtils Exception:権限判断エラー");
			throw en;
		}

		if (list != null && list.size() > 0) {
			Map<?, ?> mu = null;
			String[] posis = null;
			String[] groups = null;
			for (int i = 0; i < list.size(); i++) {
				mu = (Map<?, ?>) list.get(i);
				String posi = ObjectUtils.toString(mu.get("cd_position")).trim();
				if (StringUtils.isNotEmpty(posi) && !ArrayUtils.contains(posis, posi)) {
					posis = (String[]) ArrayUtils.add(posis, posi);
				}
				String group = ObjectUtils.toString(mu.get("CD_GROUP")).trim();
				if (StringUtils.isNotEmpty(group) && !ArrayUtils.contains(groups, group)) {
					groups = (String[]) ArrayUtils.add(groups, group);
				}
			}
			usr.setPoslist(posis);
			usr.setGrouplist(groups);
		}
	}

	/**
	 * レポート分類(マスタ)設定情報取得
	 *
	 * @param usr ログインユーザー情報
	 *
	 * @return レポート分類設定情報
	 * @throws Exception 例外
	 */
	private ArrayList<Side> getReportSideMstSetting(User usr, String KB) throws Exception {
		List<?> list = null;
		String sql = SQL.SIDE_SEL_006;
		sql = StringUtils.replaceOnce(sql, "?", StringUtils.join(usr.getGrouplist(), ","));
		sql = StringUtils.replaceOnce(sql, "?", StringUtils.join(usr.getPoslist(), ","));

		sql = StringUtils.replaceOnce(sql, "%", KB);

		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler<?> rsh = new MapListHandler();
			list = (List<?>) qr.query(sql, rsh);
		} catch (Exception en) {
			System.out.println("DBUtils Exception:LoginServlet_ReportMstr");
			throw en;
		}

		Map<?, ?> mu = null;
		ArrayList<Side> menu = null;
		if (list != null) {
			menu = new ArrayList<Side>();
			for (int i = 0; i < list.size(); i++) {
				mu = (Map<?, ?>) list.get(i);
				menu.add(i, new Side(
						Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_SIDE")).trim()),
						ObjectUtils.toString(mu.get("NM_REPORT_SIDE")).trim(),
						ObjectUtils.toString(mu.get("cd_disp_column")).trim()));
			}
		}
		return menu;
	}

	/**
	 * レポート(マスタ)設定情報取得
	 *
	 * @param mst レポート分類(マスタ)設定情報
	 * @param usr ログインユーザー情報
	 * @throws Exception エラー
	 */
	private void setReportMstSetting(ArrayList<Side> mst, User usr, String KB) throws Exception {
		String sql = SQL.REPORT_SEL_004;
		sql = StringUtils.replaceOnce(sql, "?", StringUtils.join(usr.getGrouplist(), ","));
		sql = StringUtils.replaceOnce(sql, "?", StringUtils.join(usr.getPoslist(), ","));

		sql = StringUtils.replaceOnce(sql, "%", KB);

		for (int i = 0; i < mst.size(); i++) {
			Side m = (Side) mst.get(i);
			List<?> listmst = null;

			try {
				QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
				ResultSetHandler<?> rsh = new MapListHandler();
				listmst = (List<?>) qr.query(sql, rsh, new Object[] { new Long(m.getSide()) });
			} catch (Exception en) {
				System.out.println("DBUtils Exception:LoginServlet_ReportMstr");
				throw en;
			}

			Map<?, ?> mu = null;
			ArrayList<Report> rep = null;
			if (listmst != null) {
				rep = new ArrayList<Report>();
				for (int j = 0; j < listmst.size(); j++) {
					mu = (Map<?, ?>) listmst.get(j);
					rep.add(j, new Report(
							Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_SIDE")).trim()),
							Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_NO")).trim()),
							ObjectUtils.toString(mu.get("NM_REPORT")).trim(),
							getUrlencoder(ObjectUtils.toString(mu.get("NM_REPORT")).trim()),
							ObjectUtils.toString(mu.get("CD_DISP_NUMBER")).trim(),
							"report"));
				}
				m.setReportMst(rep);
				mst.set(i, m);
			}
		}
	}

	/**
	 * レポート分類(マスタ以外)設定情報取得
	 *
	 * @param usr ログインユーザー情報
	 *
	 * @return レポート分類設定情報
	 * @throws Exception 例外
	 */
	private ArrayList<Side> getReportSideSetting(User usr, String KB) throws Exception {

		List<?> list = null;
		String sql = SQL.SIDE_SEL_005;
		sql = StringUtils.replaceOnce(sql, "?", StringUtils.join(usr.getGrouplist(), ","));
		sql = StringUtils.replaceOnce(sql, "?", StringUtils.join(usr.getPoslist(), ","));

		sql = StringUtils.replaceOnce(sql, "%", KB);

		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler<?> rsh = new MapListHandler();
			list = (List<?>) qr.query(sql, rsh);
		} catch (Exception en) {
			System.out.println("DBUtils Exception:LoginServlet_ReportMenu");
			throw en;
		}

		Map<?, ?> mu = null;
		ArrayList<Side> menu = null;
		if (list != null) {
			menu = new ArrayList<Side>();
			for (int i = 0; i < list.size(); i++) {
				mu = (Map<?, ?>) list.get(i);
				menu.add(i, new Side(
						Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_SIDE")).trim()),
						ObjectUtils.toString(mu.get("NM_REPORT_SIDE")).trim(),
						ObjectUtils.toString(mu.get("cd_disp_column")).trim()));
			}
		}
		return menu;
	}

	/**
	 * レポート(マスタ以外)設定情報取得
	 *
	 * @param menu レポート分類(マスタ以外)設定情報
	 * @param usr ログインユーザー情報
	 * @param list4 レポート設定情報
	 * @throws Exception エラー
	 */
	private void setReportSetting(ArrayList<Side> menu, User usr, ArrayList<String> list4, String KB) throws Exception {
		List<?> list1 = null;
		List<?> list2 = null;
		List<?> list3 = null;

		String sql = SQL.REPORT_SEL_004;
		sql = StringUtils.replaceOnce(sql, "?", StringUtils.join(usr.getGrouplist(), ","));
		sql = StringUtils.replaceOnce(sql, "?", StringUtils.join(usr.getPoslist(), ","));

		sql = StringUtils.replaceOnce(sql, "%", KB);

		String sql2 = StringUtils.replaceOnce(SQL.REPORT_PARAMETER_005, "?", StringUtils.join(usr.getGrouplist(), ","));
		String sql3 = StringUtils.replaceOnce(SQL.REPORT_PARAMETER_006, "?", StringUtils.join(usr.getPoslist(), ","));
		String users_custom = getUrlencoder(usr.getUsers_custom());

		for (int i = 0; i < menu.size(); i++) {
			Side m = (Side) menu.get(i);
			List<?> list = null;
			try {
				QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
				ResultSetHandler<?> rsh = new MapListHandler();
				list = (List<?>) qr.query(sql, rsh, new Object[] { new Long(m.getSide()) });
			} catch (Exception en) {
				System.out.println("DBUtils Exception:LoginServlet_Report");
				throw en;
			}

			Map<?, ?> mu = null;
			ArrayList<Report> rep = null;

			if (list != null) {
				rep = new ArrayList<Report>();
				for (int j = 0; j < list.size(); j++) {
					QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
					ResultSetHandler<?> rsh = new MapListHandler();
					mu = (Map<?, ?>) list.get(j);
					// レポート分類マスタ
					list1 = (List<?>) qr.query(SQL.REPORT_PARAMETER_002, rsh, new Object[] { new Long(
							ObjectUtils.toString(mu.get("CD_REPORT_SIDE")).trim()) });
					// グループマスタ
					list2 = (List<?>) qr.query(sql2, rsh, new Object[] { new Long(ObjectUtils.toString(mu.get("CD_REPORT_NO")).trim()) });
					// ロールマスタ
					list3 = (List<?>) qr.query(sql3, rsh, new Object[] {new Long(ObjectUtils.toString(mu.get("CD_REPORT_NO")).trim()) });
					rep.add(j, new Report(
							Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_SIDE")).trim()),
							Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_NO")).trim()),
							ObjectUtils.toString(mu.get("NM_REPORT")).trim(),
							ObjectUtils.toString(mu.get("NM_SHORT")).trim(),
							ObjectUtils.toString(mu.get("NM_REPORT_JSP")).trim(),
							ObjectUtils.toString(mu.get("CD_DISP_NUMBER")).trim(),
							ObjectUtils.toString(mu.get("custom_value")).trim(),
							users_custom,
							getParameter(list1),
							getParameter(list2),
							getParameter(list3)));
				}
				m.setReport(rep);
				menu.set(i, m);
				list4.add(i, m.getDisp_Column());
			}
		}
	}


	/**
	 * パラメータをURLエンコードするためのクラス
	 *
	 * @param url_encoder パラメータ
	 * @return url_encode
	 * @throws Exception 例外
	 */
	protected String getUrlencoder(String url_encoder) throws Exception {
		if (StringUtils.isEmpty(url_encoder)) {
			return url_encoder;
		}
		String url_encode = null;
		url_encode = URLEncoder.encode(url_encoder, Consts.ENCODE);
		return url_encode;
	}

	/**
	 * レポート分類への情報のCUSTOM_VALUEを加工するためのクラス
	 *
	 * @param list list
	 * @return Strparameter
	 * @throws Exception 例外
	 */
	protected String getParameter(List<?> list) throws Exception {
		String strParameter = "";
		Map<?, ?> mapParameter = null;

		if (list.size() != 0) {
			for (int k = 0; k < list.size(); k++) {
				mapParameter = (Map<?, ?>) list.get(k);
				if (k != 0) {
					strParameter = strParameter + ";" + ObjectUtils.toString(mapParameter.get("custom_value")).trim();
				} else {
					strParameter = ObjectUtils.toString(mapParameter.get("custom_value")).trim();
				}
			}

		} else {
			mapParameter = (Map<?, ?>) list.get(0);
			strParameter = ObjectUtils.toString(mapParameter.get("custom_vlaue")).trim();
		}
		return strParameter;
	}
}
