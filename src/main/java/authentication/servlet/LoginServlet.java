/*
 * 作成日: 2007/11/08
 *
 */
package authentication.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import org.apache.commons.lang.ObjectUtils;

import authentication.bean.Auth;
import authentication.bean.Position;
import authentication.bean.Report;
import authentication.bean.Side;
import authentication.bean.User;
import authentication.connection.DBConnection;
import authentication.dbaccess.DBinfo;
import authentication.defines.Consts;
import authentication.defines.Defines;
import authentication.defines.Form;
import authentication.defines.Message;
import authentication.defines.SQL;
import authentication.parser.LoginUser;
import authentication.parser.UserParser;
import authentication.util.CmnDate;
import authentication.util.Tagescape;
import authentication.validation.LoginValidation;


/**
 * ログイン処理制御クラス
 * ログイン・ログオフの処理を制御する。
 */
public class LoginServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 591119765164319961L;
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
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * リクエスト時処理
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		/* セッションクリア */
		session.removeAttribute(Consts.STR_SES_LOGINUSER);	// LoginUser
		session.removeAttribute(Consts.STR_SES_LOGINPOS);	// LoginUserPosition
		session.removeAttribute(Consts.STR_SES_LOGINATH);	// LoginUserAth
		session.removeAttribute(Consts.STR_SES_REPSIDE);	// Side_main
		session.removeAttribute(Consts.STR_SES_MSTSIDE);	// Side_master

		String strPage = null;

		// 利用時間の時間取得
		String fromData = config.getServletContext().getInitParameter(Consts.FROM_DATA);
		String toData   = config.getServletContext().getInitParameter(Consts.TO_DATA);

		String parameter = "";
		String user = "";
		String pass = "";
		String view = "";

		if (request.getParameter(Form.LOGIN_VIEW) != null) {
			// 情報保持
			session.setAttribute("_" + Form.LOGIN_USER, request.getParameter(Form.LOGIN_USER));
			session.setAttribute("_" + Form.LOGIN_PASS, request.getParameter(Form.LOGIN_PASS));
			session.setAttribute("_" + Form.LOGIN_VIEW, request.getParameter(Form.LOGIN_VIEW));

			user = request.getParameter(Form.LOGIN_USER);
			pass = request.getParameter(Form.LOGIN_PASS);
			view = request.getParameter(Form.LOGIN_VIEW);

		} else {

			user = session.getAttribute("_" + Form.LOGIN_USER) == null ? "" : (String) session.getAttribute("_" + Form.LOGIN_USER);
			pass = session.getAttribute("_" + Form.LOGIN_PASS) == null ? "" : (String) session.getAttribute("_" + Form.LOGIN_PASS);
			view = session.getAttribute("_" + Form.LOGIN_VIEW) == null ? "" : (String) session.getAttribute("_" + Form.LOGIN_VIEW);

		}

		if (!"".equals(user)) {
			// 失敗時の予備
			session.setAttribute(Consts.STR_SES_LOGINUSER, getTopLoad(user, pass));
			// ログアウト時に初期化するパラメータ情報
			parameter = "?" + Form.LOGIN_USER + "=" + user + "&" + Form.LOGIN_PASS + "=" + pass + "&" + Form.LOGIN_VIEW + "=" + view;
		}
		// ログオン失敗時の転送先
		strPage = config.getInitParameter("Login") + parameter;

		LoginValidation close = new LoginValidation();
		try {

			// 利用時間の時間判定
			if (close.closeTime(fromData, toData).equals(Consts.CLOSE_TIME)) {
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;

			}

			if (request.getParameter(Form.LOGIN_USER) == null
			 || request.getParameter(Form.LOGIN_PASS) == null) {

				// フォーム値未入力の場合
				session.invalidate();
				/**
				 * 別ページ遷移設定 : Loginへ
				 */
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;

			}

			// フォーム値入力済みの場合
			Tagescape esc = new Tagescape();
			LoginValidation vld = new LoginValidation();

			// 入力された値の取得
			String strUserID = esc.htmlEscape(request.getParameter(Form.LOGIN_USER).toString().trim());
			String strPass = esc.htmlEscape(request.getParameter(Form.LOGIN_PASS).toString().trim());

			if (!vld.entry(strUserID, strPass)) {
				// 入力チェックでエラー項目が検出された場合
				request.setAttribute(Form.COMMON_MSGTYPE, Form.COMMON_MSGTYPE_ERR);
				request.setAttribute(Form.COMMON_MSG, vld.OutMsgShort());

				// 別ページ遷移設定 : Loginへ
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;
			}
			User prmUsr = new User(strUserID, strPass);

			// 入力チェックでエラーなしの場合
			User loginusr = null;
			Position position = null;
			Auth auth = null;

			/** 管理者以外の場合 */
			String sql = SQL.USER_SEL_001;
			List<?> list = null;
			Map<?, ?> mu = null;
			try {
				QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
				ResultSetHandler<?> rsh = new MapHandler();
				mu = (Map<?, ?>) qr.query(sql, rsh, new Object[] { prmUsr.getId(), prmUsr.getPass() });
			} catch (Exception en) {
				System.out.println("DBUtils NullPointerException:LoginServlet_User");
				throw en;
			}

			if (mu == null) {

				// DBに対象データが存在しない場合
				request.setAttribute(Form.COMMON_MSGTYPE, Form.COMMON_MSGTYPE_ERR);
				request.setAttribute(Form.COMMON_MSG, Message.ERR_AUTH_FAIL);

				// 別ページ遷移設定 : Loginへ
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;

			}

			// ユーザーのデータを保持
			User usr = new User(
					Integer.parseInt(ObjectUtils.toString(mu.get("CD_USER")).trim()),
					ObjectUtils.toString(mu.get("USER_ID")).trim(),
					ObjectUtils.toString(mu.get("PASSWORDS")).trim(),
					ObjectUtils.toString(mu.get("uname")).trim(),
					ObjectUtils.toString(mu.get("CD_AUTH")).trim(),
					ObjectUtils.toString(mu.get("DT_PW_TERM")).trim(),
					Integer.parseInt(ObjectUtils.toString(mu.get("CD_GROUP")).trim()),
					Integer.parseInt(ObjectUtils.toString(mu.get("cd_position")).trim()),
					ObjectUtils.toString(mu.get("custom_value")).trim(),
					ObjectUtils.toString(mu.get("LOGO")).trim()
					);
			loginusr = usr;

			// ユーザー情報の変換
			UserParser convert = new UserParser();
			LoginUser lUser = convert.UserParse(loginusr);

			// パスワード有効期限確認
			if (!vld.IsPasswordTrim(lUser)) {
				/* エラーメッセージの設定 */
				request.setAttribute(Defines.STR_FRM_MSGTYPE, Defines.STR_VAL_MSGTYPE_ERR);
				request.setAttribute(Defines.STR_FRM_MSG, Defines.MSG_LUSER_PASSOUT);

				// 別ページ遷移設定 : Loginへ
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;
			}

			// ロールを保持
			position = new Position(loginusr.getPos(), ObjectUtils.toString(mu.get("NM_POSITION")).trim());

			// グループを保持
			auth = new Auth(loginusr.getGroup(), ObjectUtils.toString(mu.get("NM_GROUP")).trim());

			/** レポート分類メニューでマスタが表示できる権限を持っているかを調べている */
			try {
				QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
				ResultSetHandler<?> rsh = new MapListHandler();
				list = (List<?>) qr.query(SQL.USER_SEL_001, rsh, new Object[] { prmUsr.getId(), prmUsr.getPass() });
			} catch (Exception en) {
				System.out.println("DBUtils NullPointerException:権限判断エラー");
				throw en;
			}

			int hander = 0;
			mu = null;

			for (int i = 0; i < list.size(); i++) {
				mu = (Map<?, ?>) list.get(i);
				if (Integer.parseInt(ObjectUtils.toString(mu.get("cd_position")).trim()) == (Consts.SYSTEM_MASTER)) {
					hander = Integer.parseInt(ObjectUtils.toString(mu.get("cd_position")).trim());
					break;
				} else {
					hander = 0;
				}
			}

			// DBに対象データが存在した場合

			/**
			 * レポート分類メニュー設定(マスタ以外)
			 */
			list = null;
			try {
				QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
				ResultSetHandler<?> rsh = new MapListHandler();

				list = (List<?>) qr.query(
						SQL.SIDE_SEL_001,
						rsh,
						new Object[] {
							new Long(loginusr.getCD_user()),
							new Long(loginusr.getCD_user()) });
			} catch (Exception en) {
				System.out.println("DBUtils NullPointerException:LoginServlet_ReportMenu");
				throw en;
			}

			mu = null;
			ArrayList<Side> menu = null;
			if (list != null) {
				menu = new ArrayList<Side>();

				for (int i = 0; i < list.size(); i++) {
					mu = (Map<?, ?>) list.get(i);
					menu.add(i,
							new Side(
								Integer.parseInt(ObjectUtils.toString(mu.get("cd_report_side")).trim()),
								ObjectUtils.toString(mu.get("NM_REPORT_SIDE")).trim(),
								ObjectUtils.toString(mu.get("cd_disp_column")).trim()));
				}
			}

			/**
			 * レポート分類メニュー設定(マスタ)
			 */
			list = null;
			if (hander == Consts.SYSTEM_MASTER) {
				try {
					QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
					ResultSetHandler<?> rsh = new MapListHandler();
					list = (List<?>) qr.query(SQL.SIDE_SEL_002,
							rsh,
							new Object[] {
									new Long(loginusr.getCD_user()),
									new Long(loginusr.getCD_user()) });
				} catch (Exception en) {
					System.out.println("DBUtils NullPointerException:LoginServlet_ReportMstr");
					throw en;
				}
			}

			mu = null;
			ArrayList<Side> mst = null;
			if (list != null) {
				mst = new ArrayList<Side>();
				for (int i = 0; i < list.size(); i++) {
					mu = (Map<?, ?>) list.get(i);
					mst.add(i, new Side(
							Integer.parseInt(ObjectUtils.toString(mu.get("cd_report_side")).trim()),
							ObjectUtils.toString(mu.get("NM_REPORT_SIDE")).trim(),
							ObjectUtils.toString(mu.get("cd_disp_number")).trim(),
							1));
				}
			}

			/**
			 * レポート設定(マスタ)
			 */
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Side m = (Side) mst.get(i);
					List<?> listmst = null;

					try {
						QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
						ResultSetHandler<?> rsh = new MapListHandler();
						listmst = (List<?>) qr.query(
										SQL.REPORT_SEL_001,
										rsh,
										new Object[] {
												new Long(loginusr.getCD_user()),
												new Long(loginusr.getCD_user()),
												new Long(m.getSide())
										});
					} catch (Exception en) {
						System.out.println("DBUtils NullPointerException:LoginServlet_ReportMstr");
						throw en;
					}

					mu = null;
					ArrayList<Report> rep = null;
					if (listmst != null) {
						rep = new ArrayList<Report>();
						for (int j = 0; j < listmst.size(); j++) {
							mu = (Map<?, ?>) listmst.get(j);
							rep.add(j,
									new Report(
									Integer.parseInt(ObjectUtils.toString(mu.get("cd_report_side")).trim()),
									Integer.parseInt(ObjectUtils.toString(mu.get("cd_report_no")).trim()),
									ObjectUtils.toString(mu.get("NM_REPORT")).trim(),
									getUrlencoder(ObjectUtils.toString(mu.get("NM_REPORT")).trim()),
									ObjectUtils.toString(mu.get("cd_disp_number")).trim(),
									"report"));
						}
						m.setReportMst(rep);
						mst.set(i, m);
					}
				}

			}

			/**
			 * レポート設定
			 */
			List<?> list1 = null;
			List<?> list2 = null;
			List<?> list3 = null;
			ArrayList<String> list4 = new ArrayList<String>();
			if (menu != null) {
				for (int i = 0; i < menu.size(); i++) {
					Side m = (Side) menu.get(i);

					list = null;
					try {
						QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
						ResultSetHandler<?> rsh = new MapListHandler();
						list = (List<?>) qr.query(
										SQL.REPORT_SEL_001,
										rsh,
										new Object[] {
												new Long(loginusr.getCD_user()),
												new Long(loginusr.getCD_user()),
												new Long(m.getSide()) });

					} catch (Exception en) {
						System.out.println("DBUtils NullPointerException:LoginServlet_Report");
						throw en;
					}

					mu = null;
					ArrayList<Report> rep = null;
					String users_custom = getUrlencoder(usr.getUsers_custom());

					if (list != null) {
						rep = new ArrayList<Report>();
						for (int j = 0; j < list.size(); j++) {
							QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
							ResultSetHandler<?> rsh = new MapListHandler();
							mu = (Map<?, ?>) list.get(j);
							// レポート分類マスタ
							list1 = (List<?>) qr.query(
											SQL.REPORT_PARAMETER_002,
											rsh,
											new Object[] {
													new Long(ObjectUtils.toString(mu.get("CD_REPORT_SIDE")).trim()) });

							// グループマスタ
							list2 = (List<?>) qr.query(
											SQL.REPORT_PARAMETER_003,
											rsh,
											new Object[] {
													new Long(ObjectUtils.toString(mu.get("CD_REPORT_NO")).trim()),
													new Long(usr.getCD_user()) });
							// ロールマスタ
							list3 = (List<?>) qr.query(
											SQL.REPORT_PARAMETER_004,
											rsh,
											new Object[] {
													new Long(ObjectUtils.toString(mu.get("CD_REPORT_NO")).trim()),
													new Long(usr.getCD_user()) });

							rep.add(j, new Report(
									Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_SIDE")).trim()),
									Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_NO")).trim()),
									ObjectUtils.toString(mu.get("NM_REPORT")).trim(),
									ObjectUtils.toString(mu.get("NM_SHORT")).trim(),
									ObjectUtils.toString(mu.get("NM_REPORT_JSP")).trim(),
									ObjectUtils.toString(mu.get("CD_DISP_NUMBER")).trim(),
									ObjectUtils.toString(mu.get("custom_value")).trim(),
									users_custom, getParameter(list1),
									getParameter(list2),
									getParameter(list3)));

						}
						m.setReport(rep);
						menu.set(i, m);
						list4.add(i, m.getDisp_Column());
					}
				}
			}

			// ------------------------------
			// DB更新状況取得
			// ------------------------------
			DBinfo dbinfo = new DBinfo();
			dbinfo.setLogMenu(loginusr.getId());

			// セッションの設定
			session.setAttribute(Consts.STR_SES_LOGINUSER, loginusr);	// LoginUser
			session.setAttribute(Consts.STR_SES_LOGINPOS, position);	// LoginUserPosition
			session.setAttribute(Consts.STR_SES_LOGINATH, auth);		// LoginUserAth
			session.setAttribute(Consts.STR_SES_REPSIDE, menu);			// Side_main
			session.setAttribute("DISP", list4);						// Side_main
			session.setAttribute(Consts.STR_SES_MSTSIDE, mst);			// Side_Master
			session.setAttribute(Consts.STR_HEAD_TYPE, Consts.STR_HEAD_TYPE_ON);	// ヘッダリンク表示ON


			//パスワード有効期限の残日数によりメッセージ設定 31日以下ならメッセージ設定
			if (!lUser.hasAdmin()) {
				CmnDate cdate = new CmnDate();
				String chkdate = cdate.addDate_String(null, 0, 0, 31);

				if (lUser.getPwdate(Defines.DT_PW_FORMAT_SAVE).toString().compareTo(chkdate) <= 0) {
					//パスワード有効期限警告メッセージ作成
					String msg_pw_warning = "パスワードの有効期限が";
					msg_pw_warning += lUser.getPwdate(Defines.DT_PW_FORMAT_DISP).substring(0, 4) + "年"
							+ lUser.getPwdate(Defines.DT_PW_FORMAT_DISP).substring(4, 6) + "月"
							+ lUser.getPwdate(Defines.DT_PW_FORMAT_DISP).substring(6, 8) + "日";
					msg_pw_warning += "で切れます。ホスト側のパスワードを変更してください。"
						+ "\\nパスワードは8桁以上で登録してください。"
						+ "\\nなお、ATLASへ変更が反映されるのは変更後の翌日からとなりますのでご注意ください。";
					//メッセージ設定
					request.setAttribute(Defines.STR_SES_PASS_TERM_MSG, msg_pw_warning);
				}
			}

			if (request.getParameter(Form.LOGIN_VIEW) != null) {
				// 別ページ遷移設定 : Loginへ
				strPage = config.getInitParameter(request.getParameter(Form.LOGIN_VIEW));
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
				return;
			}

			/**
			 * 別ページ遷移設定 : Menuへ
			 */
			strPage = config.getInitParameter("Menu");

		} catch (Exception e) {

			/**
			 * 例外処理
			 */
			try {
				// this.doPost(request, response);
				e.printStackTrace();
			} catch (Exception e2) {
				// スタックトレースの出力
				e.printStackTrace();
				// エラーメッセージの設定
				request.setAttribute(Form.COMMON_MSGTYPE,Form.COMMON_MSGTYPE_ERR);
				// ページ遷移先の設定
				strPage = this.getServletContext().getInitParameter("Page_Error");
			}
		}

		/**
		 * ページ遷移
		 */
		getServletContext().getRequestDispatcher(strPage).forward(request, response);
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
					strParameter = strParameter	+ ";" + ObjectUtils.toString(mapParameter.get("custom_value")).trim();
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

	/**
	 * パラメータをURLエンコードするためのクラス
	 *
	 * @param url_encoder パラメータ
	 * @return url_encode
	 * @throws Exception 例外
	 */
	protected String getUrlencoder(String url_encoder) throws Exception {
		String url_encode = null;
		url_encode = URLEncoder.encode(url_encoder, Consts.ENCODE);
		return url_encode;
	}

	/**
	 * @param strUserID ユーザーID
	 * @param strPass パスワード
	 *
	 * @return User
	 */
	protected User getTopLoad(String strUserID, String strPass) {
		User usr = null;
		try {
			String sql = SQL.USER_SEL_001;
			Object[] param = { strUserID, strPass };
			Map<?, ?> mu = null;

			try {
				QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
				ResultSetHandler<?> rsh = new MapHandler();
				mu = (Map<?, ?>) qr.query(sql, rsh, param);

			} catch (Exception e) {
				// スタックトレースの出力
				System.out.println("DBUtils NullPointerException:LoginServlet_User");
				e.printStackTrace();
			}

			if (mu != null) {
				// ユーザーのデータを保持
				usr = new User(
						Integer.parseInt(ObjectUtils.toString(mu.get("CD_USER")).trim()),
						ObjectUtils.toString(mu.get("USER_ID")).trim(),
						ObjectUtils.toString(mu.get("PASSWORDS")).trim(),
						ObjectUtils.toString(mu.get("uname")).trim(),
						ObjectUtils.toString(mu.get("CD_AUTH")).trim(),
						ObjectUtils.toString(mu.get("DT_PW_TERM")).trim(),
						Integer.parseInt(ObjectUtils.toString(mu.get("CD_GROUP")).trim()),
						Integer.parseInt(ObjectUtils.toString(mu.get("cd_position")).trim()),
						ObjectUtils.toString(mu.get("custom_value")).trim(),
						ObjectUtils.toString(mu.get("LOGO")).trim()
						);
			}

		} catch (Exception e) {
			// スタックトレースの出力
			e.printStackTrace();
		}
		return usr;
	}

}