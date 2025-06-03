/*
 * 作成日: 2007/10/26
 *  
 */
package authentication.filter;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import authentication.defines.*;
import authentication.validation.*;

/**
 * セッション状態をチェックし、システムへログインしているかをチェックする
 * セッションが切れている場合は、ログイン画面へ強制的に遷移させ、
 * セッションが継続している場合は処理を継続させる。
 */
public class LoginFilter implements Filter {
	private FilterConfig config = null;

	/**
	 * 初期化
	 */
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	/**
	 * 破棄
	 */
	public void destroy() {
	}

	/**
	 * リクエスト時フィルター処理
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);

		/* セッションチェック */
		if (session != null) {

			// サイトの利用時間
			String fromData = config.getServletContext().getInitParameter(Consts.FROM_DATA);
			String toData   = config.getServletContext().getInitParameter(Consts.TO_DATA);
			LoginValidation close = new LoginValidation();

			if (close.closeTime(fromData, toData).equals(Consts.CLOSE_TIME)) {

			} else if (session.getAttribute(Consts.STR_SES_LOGINUSER) != null) {
				chain.doFilter(httpRequest, httpResponse);
				return;
			}
		}

		// セッションなし：ログイン画面へ
		/* エラーメッセージの設定 */
		httpRequest.setAttribute(Form.COMMON_MSGTYPE, Form.COMMON_MSGTYPE_ERR);
		httpRequest.setAttribute(Form.COMMON_MSG, Message.ERR_SES_TIMEOUT);
		httpRequest.getRequestDispatcher(config.getInitParameter("Login")).forward(request, response);
	}
}