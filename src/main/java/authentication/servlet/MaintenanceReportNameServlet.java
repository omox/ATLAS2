package authentication.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.ObjectUtils;

import authentication.bean.MaintenanceReportName;
import authentication.bean.User;
import authentication.connection.DBConnection;
import authentication.defines.Consts;
import authentication.defines.Form;
import authentication.defines.Message;
import authentication.defines.SQL;
import authentication.util.Tagescape;
import authentication.validation.MaintenanceReportNameValidation;

public class MaintenanceReportNameServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 2002258410671145604L;
	private ServletConfig config = null;

	/**
	 * 初期化
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
		String strPage = null;

		try {
			MaintenanceReportName selection = new MaintenanceReportName();

			// jspファイル名　取得
			if (session.getAttribute(Consts.STR_SES_GRD2) == null) {
				ServletContext context = getServletContext();
				String paths = context.getRealPath("/WEB-INF/../jsp/reports");
				System.out.println("PATH:"+paths);

				File dir = new File(paths);
				File[] files = dir.listFiles();
				ArrayList jsplist = new ArrayList();
				if (files != null) {
					String delchar = ".jsp";
					String[] fnames = new String[files.length];
					for (int i = 0; i < files.length; i++) {
						fnames[i] = files[i].getName();
						int indexstart = fnames[i].toString().trim().indexOf(delchar);
						int indexend = fnames[i].toString().trim().indexOf(delchar)	+ delchar.length();
						StringBuffer sb = new StringBuffer(fnames[i].toString().trim());
						if (indexstart > 0) {
							sb.delete(indexstart, indexend);
						}
						jsplist.add(sb.toString());
					}
				}
				session.setAttribute(Consts.STR_SES_GRD2, jsplist);
			}

			/** タイトル取得 */
			if (session.getAttribute(Consts.STR_SES_TITLE) == null) {
				String report_title = request.getParameter(Form.REPORT_NAME) != null ? request.getParameter(Form.REPORT_NAME).toString().trim()	: null;
				String name = getReport_title(report_title);
				session.setAttribute(Consts.STR_SES_TITLE, name);
			}

			/** UserListデータ読込チェック */
			if (session.getAttribute(Consts.STR_SES_GRD) == null) {
				/** 取得していない場合 */
				ArrayList reportName_detaList = getReportNameRecord();
				// リスト設定
				session.setAttribute(Consts.STR_SES_GRD, reportName_detaList);

			} else {
				/** 取得済みの場合 */
				// 入力文字エスケープ
				Tagescape esc = new Tagescape();

				/** Listデータ読込 */
				ArrayList report_nameList = (ArrayList) session
						.getAttribute(Consts.STR_SES_GRD);

				/** * 処理エリア判別 */
				if (request.getParameter(Form.MTN_INPAREA) != null) {
					/**
					 * 入力エリア処理
					 */
					// イベント取得
					String event = request.getParameter(Form.MTN_INPAREA)
							.toString();
					MaintenanceReportNameValidation vld = new MaintenanceReportNameValidation();
					boolean eraflg = true;
					if (event.equals(Form.MTN_RSTBTN)) {
						/** リセットボタン */
						// 初期化済の為、処理なし
					} else if (event.equals(Form.MTN_ENTBTN)) {
						/** 登録ボタン */

						/** 入力値取得 */
						HashMap<String, String> entrys = new HashMap<String, String>();
						System.out.println("oia0wtagtawo4a");

						entrys.put(Form.MTN_REPORT_CODE, esc.htmlEscape(request.getParameter(Form.MTN_REPORT_CODE).toString()));
						entrys.put(Form.MTN_NM_REPORT, esc.htmlEscape(request.getParameter(Form.MTN_NM_REPORT).toString()));
						entrys.put(Form.MTN_REPORT_NM_SHORT, esc.htmlEscape(request.getParameter(Form.MTN_REPORT_NM_SHORT).toString()));
						entrys.put(Form.MTN_REPORT_JSP, esc.htmlEscape(request.getParameter(Form.MTN_REPORT_JSP).toString()));
						entrys.put(Form.MTN_USR_ETY_IDX, esc.htmlEscape(request.getParameter(Form.MTN_USR_ETY_IDX).toString()));
						entrys.put(Form.MTN_CUSTOM_VALUE, esc.htmlEscape(request.getParameter(Form.MTN_CUSTOM_VALUE).toString()));

						/* チェック */
						if (vld.entry(entrys, report_nameList) == false) {

							/* エラーメッセージの設定 */
							request.setAttribute(Form.COMMON_MSGTYPE,Form.COMMON_MSGTYPE_ERR);
							request.setAttribute(Form.COMMON_MSG, vld.OutMsgShort());

							/** エラー後　入力値保持 */
							MaintenanceReportName namereport = null;

							String report_name = (String) entrys.get(Form.MTN_NM_REPORT);
							String report_shot = (String) entrys.get(Form.MTN_REPORT_NM_SHORT);
							String report_jsp = (String) entrys.get(Form.MTN_REPORT_JSP);
							String custom_value = (String) entrys.get(Form.MTN_CUSTOM_VALUE);

							/** エラー時選択ﾚｺｰﾄﾞ行が解除されないようにした */
							if (entrys.get(Form.MTN_USR_ETY_IDX) != null
									&& Integer.parseInt((String) entrys
											.get(Form.MTN_USR_ETY_IDX)) >= 0) {
								int idx = Integer.parseInt((String) entrys.get(Form.MTN_USR_ETY_IDX));
								namereport = (MaintenanceReportName) report_nameList.get(idx);
								namereport = new MaintenanceReportName(
										namereport.getReport_no(), report_name,
										report_shot, report_jsp, custom_value,
										namereport.getIndex());
							} else {
								namereport = new MaintenanceReportName(
										report_name, report_shot, report_jsp,
										custom_value);
							}

							selection = namereport;
							eraflg = false;

						} else {
							/* エラーなし */
							MaintenanceReportName namereport = null;
							int idx = 0;
							if (entrys.get(Form.MTN_USR_ETY_IDX) != null
									&& Integer.parseInt((String) entrys.get(Form.MTN_USR_ETY_IDX)) >= 0) {

								// 登録済みデータ更新 or 複製
								idx = Integer.parseInt((String) entrys.get(Form.MTN_USR_ETY_IDX));
								namereport = (MaintenanceReportName) report_nameList.get(idx);
								if (Integer.parseInt(entrys.get(Form.MTN_REPORT_CODE).toString().trim()) == namereport.getReport_no()) {
									// 上書き登録
									namereport.setReport_no(Integer.parseInt(entrys.get(Form.MTN_REPORT_CODE).toString().trim()));
									namereport.setReport_name((String) entrys.get(Form.MTN_NM_REPORT));
									namereport.setReport_shortname((String) entrys.get(Form.MTN_REPORT_NM_SHORT));
									namereport.setReport_jsp((String) entrys.get(Form.MTN_REPORT_JSP));
									namereport.setReport_cutom((String) entrys.get(Form.MTN_CUSTOM_VALUE));
									namereport.setUpd();

									// 登録データ反映
									report_nameList.set(idx, namereport);
								}
							}

							if (namereport == null) {
								// 新規登録データ
								idx = report_nameList.size();

								int report_no = getSequence();
								String report_name = (String) entrys.get(Form.MTN_NM_REPORT);
								String report_shot = (String) entrys.get(Form.MTN_REPORT_NM_SHORT);
								String report_jsp = (String) entrys.get(Form.MTN_REPORT_JSP);
								String custom_value = (String) entrys.get(Form.MTN_CUSTOM_VALUE);

								namereport = new MaintenanceReportName(
										report_no, report_name, report_shot,
										report_jsp, custom_value, idx);
								namereport.setUpd();

								// 登録データ追加
								report_nameList.add(idx, namereport);
							}
							// リスト設定
							session.setAttribute(Consts.STR_SES_GRD,report_nameList);
						}

					} else if (event.equals(Form.MTN_DELBTN)) {
						/** 削除ボタン */
						/** 入力値取得 */
						HashMap<String, String> entrys = new HashMap<String, String>();
						entrys
								.put(Form.MTN_REPORT_CODE, esc
										.htmlEscape(request.getParameter(
												Form.MTN_REPORT_CODE)
												.toString()));
						entrys.put(Form.MTN_NM_REPORT, esc.htmlEscape(request
								.getParameter(Form.MTN_NM_REPORT).toString()));
						entrys.put(Form.MTN_REPORT_NM_SHORT, esc
								.htmlEscape(request.getParameter(
										Form.MTN_REPORT_NM_SHORT).toString()));
						entrys.put(Form.MTN_REPORT_JSP, esc.htmlEscape(request
								.getParameter(Form.MTN_REPORT_JSP).toString()));
						entrys
								.put(Form.MTN_USR_ETY_IDX, esc
										.htmlEscape(request.getParameter(
												Form.MTN_USR_ETY_IDX)
												.toString()));
						entrys.put(Form.MTN_CUSTOM_VALUE, esc
								.htmlEscape(request.getParameter(
										Form.MTN_CUSTOM_VALUE).toString()));

						/* チェック */
						if (vld.delete(entrys, report_nameList) == false) {
							/* エラーメッセージの設定 */
							request.setAttribute(Form.COMMON_MSGTYPE,
									Form.COMMON_MSGTYPE_ERR);
							request.setAttribute(Form.COMMON_MSG, vld
									.OutMsgShort());

							/** エラー後　入力値保持 */
							MaintenanceReportName namereport = null;

							String report_name = (String) entrys
									.get(Form.MTN_NM_REPORT);
							String report_shot = (String) entrys
									.get(Form.MTN_REPORT_NM_SHORT);
							String report_jsp = (String) entrys
									.get(Form.MTN_REPORT_JSP);
							String custom_value = (String) entrys
									.get(Form.MTN_CUSTOM_VALUE);

							/** エラー時選択ﾚｺｰﾄﾞ行が解除されないようにした */
							if (entrys.get(Form.MTN_USR_ETY_IDX) != null
									&& Integer.parseInt((String) entrys
											.get(Form.MTN_USR_ETY_IDX)) >= 0) {
								int idx = Integer.parseInt((String) entrys
										.get(Form.MTN_USR_ETY_IDX));
								namereport = (MaintenanceReportName) report_nameList
										.get(idx);
								namereport = new MaintenanceReportName(
										namereport.getReport_no(), report_name,
										report_shot, report_jsp, custom_value,
										namereport.getIndex());
							} else {
								namereport = new MaintenanceReportName(
										report_name, report_shot, report_jsp,
										custom_value);
							}

							selection = namereport;
							eraflg = false;
						} else {
							/* エラーなし */

							// 登録済みデータ更新
							int idx = 0;
							idx = Integer.parseInt((String) entrys
									.get(Form.MTN_USR_ETY_IDX));

							/**
							 * for (int i = 0; i < size; i++) {
							 * MaintenanceReportName del =
							 * (MaintenanceReportName) report_nameList.get(i);
							 * if (del.getReport_no()==
							 * Integer.parseInt(entrys.get
							 * (Form.MTN_REPORT_CODE).toString().trim())) { idx
							 * = i; } }
							 */
							MaintenanceReportName usr = (MaintenanceReportName) report_nameList
									.get(idx);
							usr.setDel();

							// 削除データ反映
							report_nameList.set(idx, usr);
							// リスト設定
							session.setAttribute(Consts.STR_SES_GRD,
									report_nameList);

						}
					}
					if (eraflg != false
							&& event.equals(Form.MTN_RSTBTN) == false) {
						// else if (event.equals(Form.MTN_SAVBTN)) {
						/** 保存ボタン */

						/* チェック */
						if (vld.save(report_nameList) == false) {
							/* エラーメッセージの設定 */
							request.setAttribute(Form.COMMON_MSGTYPE,
									Form.COMMON_MSGTYPE_ERR);
							request.setAttribute(Form.COMMON_MSG, vld
									.OutMsgShort());
						} else {
							/* エラーなし */
							User loginusr = (User) session
									.getAttribute(Consts.STR_SES_LOGINUSER);
							String msg = null;
							msg = setUserRecord(report_nameList, loginusr, msg);

							// 再設定
							report_nameList = getReportNameRecord();

							// リスト設定
							session.setAttribute(Consts.STR_SES_GRD,
									report_nameList);

							request.setAttribute(Form.COMMON_MSGTYPE,
									Form.COMMON_MSGTYPE_SUP);
							request.setAttribute(Form.COMMON_MSG, msg);
						}
					}
				} else if (request.getParameter(Form.MTN_SELAREA) != null) {
					/**
					 * 選択エリア処理
					 */
					int idx = Integer.parseInt((String) request
							.getParameter(Form.MTN_SELAREA));
					selection = (MaintenanceReportName) report_nameList
							.get(idx);
				}

			}

			request.setAttribute(Consts.STR_REQ_REC, selection);
			strPage = config.getInitParameter("MaintenanceReportName");
			/**
			 * 例外処理
			 */
		} catch (Exception e) {
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
		getServletContext().getRequestDispatcher(strPage).forward(request,
				response);
	}

	/**
	 * ユーザー情報取得 <br />レポートコード DBよりユーザー情報を全て取得し、ArrayList形式で戻す。<br />
	 *
	 * @return 取得したArrayList形式Userデータ <br />
	 * @throws Exception
	 *             例外 <br />
	 */
	protected ArrayList getReportNameRecord() throws Exception {
		List list = null;
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler rsh = new MapListHandler();
			list = (List) qr.query(SQL.MTN_REPORT_NAME_SEL_001, rsh);
		} catch (Exception en) {
			System.out
					.println("DBUtils NullPointerException:getReportNameRecord()");
			en.printStackTrace();
		}
		Map mu = null;
		ArrayList beanList = null;

		if (list != null) {
			beanList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				mu = (Map) list.get(i);
				beanList.add(i, new MaintenanceReportName(
						Integer.parseInt(ObjectUtils.toString(mu.get("CD_REPORT_NO")).trim()),
						ObjectUtils.toString(mu.get("NM_REPORT")).trim(),
						ObjectUtils.toString(mu.get("NM_SHORT")).trim(),
						ObjectUtils.toString(mu.get("NM_REPORT_JSP")).trim(),
						ObjectUtils.toString(mu.get("CUSTOM_VALUE")).trim(), i));
			}
		}
		return beanList;
	}

	/**
	 * レポート分類マスタ情報取得 <br />
	 * レポート分類マスタから、CustomValue(パス)を取得し、ArrayList形式で戻す。<br />
	 *
	 * @return 取得したArrayList形式Userデータ <br />
	 * @throws Exception
	 *             例外 <br />
	 */
	protected ArrayList<String> getReportSideList() throws Exception {
		ArrayList<String> rtnList = null;
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler rsh = new MapListHandler();
			List<Map> list = (List<Map>) qr.query(SQL.MTN_REPORT_NAME_SEL_002, rsh);
			if (!list.isEmpty()) {
				rtnList = new ArrayList<String>();
				for (Map mu : list) {
					rtnList.add(ObjectUtils.toString(mu.get("CUSTOM_VALUE")).trim());
				}
			}
		} catch (Exception en) {
			System.out
					.println("DBUtils NullPointerException:getReportNameRecord()");
			en.printStackTrace();
		}
		return rtnList;
	}


	/**
	 * ユーザー情報設定 <br /> 画面にて変更を行ったUserListをDBへ反映させる。 <br />
	 *
	 * @param list
	 *            ArrayList形式Userデータ <br />
	 * @param loginusr
	 *            ログインユーザ情報 <br />
	 * @throws SQLException
	 *             例外 <br />
	 */
	protected String setUserRecord(ArrayList list, User loginusr, String msg)
			throws SQLException {

		Connection con = null; // コネクション
		String save = null;// 保存･削除メッセージ
		try {
			/* コネクションの取得 */

			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			con = qr.getDataSource().getConnection();
			con.setAutoCommit(false);

			/* List内データの内、Upd,Delのみ更新対象とする */
			int size = list.size();
			for (int i = 0; i < size; i++) {
				MaintenanceReportName reportname = (MaintenanceReportName) list
						.get(i);
				if (reportname.getState() == MaintenanceReportName.STATE_UPD) {
					/**
					 * UPDATE or INSERTを行うデータ
					 */

					/**
					 * UPDATE
					 */
					save = Consts.SQL_MESSAGE_IN;
					int cnt = 0;
					cnt = qr.update(con, SQL.REPORT_NAME_UPD_001, new Object[] {
							new Integer(reportname.getReport_no()),
							reportname.getReport_name(),
							reportname.getReport_shortname(),
							reportname.getReport_jsp(),
							reportname.getReport_custom(), loginusr.getName(),
							new Integer(reportname.getReport_no()) });
					System.out.println("UPDATE pkey db[sys_report_name]:"
							+ reportname.getReport_no() + " COUNT:" + cnt);

					// 1件もUPDATEできなかった場合はINSERTを行う
					if (cnt == 0) {
						/**
						 * INSERT
						 */
						cnt = qr
								.update(con, SQL.REPORT_NAME_INS_001,
										new Object[] {
												new Integer(reportname
														.getReport_no()),
												reportname.getReport_name(),
												reportname
														.getReport_shortname(),
												reportname.getReport_jsp(),
												reportname.getReport_custom(),
												loginusr.getName(),
												loginusr.getName() });
						System.out.println("INSERT pkey db[sys_report_name]:"
								+ reportname.getReport_no() + " COUNT:" + cnt);
					}

				} else if (reportname.getState() == MaintenanceReportName.STATE_DEL) {
					/**
					 * DELETE を行うデータ
					 */
					save = Consts.SQL_MESSAGE_DELETE;
					int cnt = qr.update(con, SQL.REPORT_NAME_DEL_003,
							new Object[] { new Integer(reportname
									.getReport_no()) });
					System.out.println("DELETE pkey db[SYS_MENU]:"
							+ reportname.getReport_no() + " COUNT:" + cnt);

					cnt = qr.update(con, SQL.REPORT_NAME_DEL_002,
							new Object[] { new Integer(reportname
									.getReport_no()) });
					System.out.println("DELETE pkey db[SYS_REPORT_AUTH]:"
							+ reportname.getReport_no() + " COUNT:" + cnt);

					cnt = qr.update(con, SQL.REPORT_NAME_DEL_001,
							new Object[] { new Integer(reportname
									.getReport_no()) });
					System.out.println("DELETE pkey db[sys_report_name]:"
							+ reportname.getReport_no() + " COUNT:" + cnt);
				}
			}
			DbUtils.commitAndCloseQuietly(con);
			msg = save + Message.SUP_SAVE_END;
			/**
			 * 例外処理
			 */
		} catch (SQLException e) {
			System.out.println("保存削除エラー");
			e.printStackTrace();
			try {
				DbUtils.rollback(con);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			DbUtils.closeQuietly(con);
			msg = save + Message.ERR_SAVE_END;
		}
		return msg;
	}

	/**
	 *レポートマスタよりレポート名称取得 <br />
	 *
	 *
	 */
	protected String getReport_title(String report_no) throws Exception {
		String title = null;
		Map mu = null;
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler rsh = new MapHandler();
			mu = (Map) qr.query(SQL.REPORT_NAME_TITLE, rsh, report_no);
		} catch (Exception en) {
			System.out
					.println("DBUtils NullPointerException:getReport_title DB :sys_report_name");
			en.printStackTrace();
		}

		if (mu == null) {
			title = "no_title";
		} else {
			title = mu.get("NM_REPORT").toString().trim();
		}

		return title;
	}

	/**
	 * シーケンス番号を取得する
	 *
	 * @return
	 * @throws Exception
	 */
	protected int getSequence() throws Exception {
		int intsequence = 0;
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler rsh = new MapHandler();
			Map sequence = null;
			sequence = (Map) qr.query(SQL.SEQUENCE_001, rsh);
			intsequence = Integer.parseInt(ObjectUtils.toString(sequence.get("1")).trim());
			System.out.println(intsequence);
		} catch (Exception en) {
			System.out
					.println("DBUtils NullPointerException:MaintenanceReportNameServlet:::getSys_menuRecode()");
			en.printStackTrace();
		}
		return intsequence;
	}


    /**
     * 指定したディレクトリ[directoryPath]から、正規表現として指定された
     * 検索対象のファイル[fileNamePattern]を再帰的に検索し、
     * 該当するファイルオブジェクトのリストを返します。
     *
     * また、ファイルの更新日付が指定日数経過しているかどうかを検索条件に
     * 指定する事ができます。
     *
     * 例)
     * File[] files =
     *         listFiles("C:/filelist/", "*.java",TYPE_FILE, true, 2);
     * 上記の例では、ディレクトリfilelistを再帰的に検索し、7日前以降に更新
     * された拡張子javaのファイルリストを取得します。
     *
     * @param directoryPath 検索対象のディレクトリを表すパス
     * @param fileNamePattern 検索対象のファイル名[正規表現]
     * @param set set 格納先
     * @return 検索にマッチしたファイルオブジェクト
     */
    public File[] listFiles(String directoryPath, String fileNamePattern, TreeSet<File> set) {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles();
        // その出力
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.isDirectory()) {
                if (fileNamePattern == null || file.getName().matches(fileNamePattern)) {
                    // 条件に該当する場合リストに格納
                    set.add(file);
                }
            } else {
            	// ディレクトリならば再帰的にリストに追加
                listFiles(file.getAbsolutePath(), fileNamePattern , set);
            }
        }
        return (File[]) set.toArray(new File[set.size()]);
    }
}
