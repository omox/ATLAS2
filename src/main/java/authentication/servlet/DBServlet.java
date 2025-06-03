package authentication.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authentication.defines.Defines;
import authentication.defines.Message;
import authentication.util.ChkSysTime;


public class DBServlet extends HttpServlet{

	/**
	 *
	 */
	private static final long serialVersionUID = -6520237624053010573L;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * リクエスト時処理 <br />
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* 初期設定 */
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String strPage = null;
		String repName="";
		//初期値設定

		try {
			/* 利用可能時間チェック */
			ChkSysTime sys = new ChkSysTime();
			if (sys.isUsefull() == false) {
				strPage = Defines.STR_JSP_WORK;
			} else {
				//レポート名称、年月の保持
				if(request.getParameter("report_name")!=null){
					session.setAttribute(Defines.SES_DB_REP_NAME, request.getParameter("report_name").toString());
					repName=request.getParameter("report_name").toString();
					strPage = Defines.STR_JSP_DB_PATH+"/"+repName+".jsp";
				}else{
					//エラーメッセージ設定
					request.setAttribute(Defines.STR_FRM_MSGTYPE, Defines.STR_VAL_MSGTYPE_ERR);
					request.setAttribute(Defines.STR_FRM_MSG, Message.ERR_PAGE_NOT_FOUND);
					// ページ遷移先の設定
					strPage=Defines.STR_JSP_ERROR;	//エラーページ
				}
				if(request.getParameter("ym")!=null){
					session.setAttribute(Defines.SES_DB_YM,request.getParameter("ym"));
				}
				if(request.getParameter("report_type")!=null){
					session.setAttribute(Defines.SES_DB_REP_TYPE, request.getParameter("report_type").toString());
				}
			}
		} catch (Exception e) {
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
}
