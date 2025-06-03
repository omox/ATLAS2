



package authentication.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import authentication.defines.Form;

/**
 * Servlet implementation class DashboardServlet
 *
 */
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String strPage = null;
		HttpSession session = request.getSession();

		try {
			/**
			 * 別ページ遷移設定 : Dashboardへ
			 */
			String menu = StringUtils.trim(request.getParameter("m"));
			if (request.getParameter("sc")!=null){
				menu="0";	// 開発用）ダッシュボードのショートカット
			}
			session.setAttribute("m", menu);

			if (menu==null) {
				/*
				 * ダッシュボード表示
				 */
				strPage = config.getInitParameter("Dashboard");
			}else{
				/*
				 * メニュー表示
				 */
				strPage = config.getInitParameter("Menu");
			}

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
