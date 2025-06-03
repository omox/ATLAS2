/*
 * 作成日: 2007/10/26
 *
 */
package authentication.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authentication.dbaccess.DBinfo;
import authentication.defines.Consts;
import authentication.defines.Form;
import authentication.validation.MenuValidation;

/**
 * メニュー処理制御クラス
 * メニュー画面を表示する際の処理を制御する
 */
public class MenuServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 426123602076827384L;
	private ServletConfig config = null;
	/** エラーメッセージ生成用文字：お知らせ */
	private static final String MSG_INFO = "お知らせ";
	private static final int LENGTH_ZEN_INFO = 250;
	private static final int LENGTH_HALF_INFO = 500;

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
		/* セッションクリア */
		/* メンテナンス画面のListクリア */
		session.removeAttribute(Consts.STR_SES_GRD);
		session.removeAttribute(Consts.STR_SES_GRD2);
		session.removeAttribute(Consts.STR_SES_GRD3);
		session.removeAttribute(Consts.STR_SES_GRD4);
		session.removeAttribute(Consts.STR_SES_GRD5);
		session.removeAttribute(Consts.STR_SES_GRD6);
		session.removeAttribute(Consts.STR_SES_TITLE);

		// Report画面情報クリア(必須:レポート分類選択時の初期表示指定に利用)
		session.removeAttribute(Consts.STR_SES_REPORT_SIDE);
		session.removeAttribute(Consts.STR_SES_REPORT_NO);
		session.removeAttribute(Consts.STR_SES_REPORT_SIDE_ARRAY);
		session.removeAttribute(Consts.STR_SES_REPORT_NO_ARRAY);


		String strPage = null;

		try {
			/**
			 * メニュー表示処理
			 */
			/* お知らせ情報 編集 */
			if (request.getParameter(Form.MENU_INFO_BTN) != null) {
				String info = request.getParameter(Form.MENU_INFO_BTN).toString();
				if (info.trim().length() != 0) {
					/* 編集・保存 */
					if (info.equals(Form.MENU_INFO_EDIT)) {
						// 編集
						request.setAttribute(Form.MENU_INFO_BTN, Form.MENU_INFO_EDIT);
					} else if (info.equals(Form.MENU_INFO_SAVE)) {
						// 保存
						String txt_val = request.getParameter(Form.MENU_INFO_VALUE).toString();
						MenuValidation vld = new MenuValidation();
						/* チェック */
						if (!vld.entry(txt_val)) {
							/* エラーメッセージの設定 */
							request.setAttribute(Form.COMMON_MSGTYPE, Form.COMMON_MSGTYPE_ERR);
							request.setAttribute(Form.COMMON_MSG, vld.OutMsgShort());
						} else {
							// ------------------------------
							// DB更新状況取得
							// ------------------------------
							DBinfo dbinfo = new DBinfo();
							dbinfo.setInfoValue(txt_val);
						}
					}
				}
			}

			/**
			 * 別ページ遷移設定 : Menuへ
			 */
			strPage = config.getInitParameter("Menu");

		} catch (Exception e) {
			/**
			 * 例外処理
			 */
			// スタックトレースの出力
			e.printStackTrace();
			// エラーメッセージの設定
			request.setAttribute(Form.COMMON_MSGTYPE, Form.COMMON_MSGTYPE_ERR);
			// ページ遷移先の設定
			strPage = this.getServletContext().getInitParameter("Page_Error");

		}

		/**
		 * ページ遷移
		 */
		getServletContext().getRequestDispatcher(strPage).forward(request, response);

	}
}
