/*
 * 作成日: 2008/08/01
 *
 */
package authentication.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import authentication.connection.DBConnection;
import authentication.dbaccess.DownloadDAO;
import authentication.dbaccess.UploadDAO;
import authentication.defines.Defines;
import authentication.defines.Form;
import authentication.defines.Message;
import authentication.defines.SQL;
import authentication.io.FileReadWriter;
import authentication.validation.UploadValidation;
import common.DefineReport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UploadServlet extends HttpServlet {

	final static String FILE_NAME_STRING = "filename=";
	final static String CONTENT_TYPE_HEADER="Content-Type: ";
	final static String FILLE_NULL_OR_NATHING = "指定されたファイルが存在しないか、空のファイルです。";

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
	 *
	 * @param request リクエスト
	 * @param response レスポンス
	 * @throws ServletException サーブレットエラー
	 * @throws IOException 入出力エラー
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * リクエスト時処理 <br />
	 *
	 * @param request リクエスト
	 * @param response レスポンス
	 * @throws ServletException サーブレットエラー
	 * @throws IOException 入出力エラー
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 文字変換コード設定【重要】
		request.setCharacterEncoding("UTF-8");
		System.out.println("Uploadの実行(content)");

		// パラメータ一覧【確認】
		HashMap<String, String> map = new HashMap<String, String>();
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			if (DefineReport.ID_DEBUG_MODE) System.out.println(name + "=" + request.getParameter(name));
			// 日本語パラメータ対応
			map.put(name, new String(request.getParameter(name).getBytes("ISO-8859-1"), "UTF-8"));
		}

		// セッションの取得
		HttpSession session = request.getSession();
		String strPage = new String(); // ページ遷移先
		String inclurepname = (String) session.getAttribute(common.Defines.ID_REQUEST_JSP_REPORT);
		try {
			String obj = map.get("obj");
			if (StringUtils.equals(obj, Form.BTN_DOW_LOAD)) {
				String radval = map.get("TYPE");
				String ymval = map.get("YYYYMM");

				if (radval.trim().equals("4")) {	// プロモ商品計画数（通店プロモ商品速報）
					ymval = map.get("YYYYMM2");
				}

				// 画面情報を元にファイルダウンロード
				authentication.servlet.UploadServlet.fileDownload(response, getServletContext(), radval, ymval);
			} else {
				// アップロード処理
				authentication.servlet.UploadServlet.fileUpload(request, session, getServletContext());

				/** 条件・メッセージの取得 */
				JSONArray results = new JSONArray();
				// ラジオボタン
				results.add(getJson(Form.MFU_RAD_UP, session.getAttribute(Defines.STR_SES_FILE_TYPE), null));
				// メッセージ
				results.add(getJson(Form.UP_DOWN_MSG, request.getAttribute(Defines.STR_FRM_MSGTYPE), request.getAttribute(Form.COMMON_MSG)));
				session.setAttribute(common.Defines.ID_REQUEST_SEND_PARAM, results.toString());

				/** 遷移先設定 */
				strPage = "/jsp/reports/" + inclurepname + ".jsp";
				getServletContext().getRequestDispatcher(strPage).forward(request, response);
			}
		} catch (Exception e) {

			/**
			 * 例外処理
			 */
			// スタックトレースの出力
			e.printStackTrace();
			/* エラーメッセージの設定 */
			request.setAttribute(Defines.STR_FRM_MSGTYPE, Defines.STR_VAL_MSGTYPE_ERR);
			request.setAttribute(Defines.STR_FRM_MSG, "システムエラー："+e.getMessage());

			/** 条件・メッセージの取得 */
			JSONArray results = new JSONArray();
			// ラジオボタン
			results.add(getJson(Form.MFU_RAD_UP, session.getAttribute(Defines.STR_SES_FILE_TYPE), null));
			// メッセージ
			results.add(getJson(Form.UP_DOWN_MSG, request.getAttribute(Defines.STR_FRM_MSGTYPE), request.getAttribute(Form.COMMON_MSG)));
			session.setAttribute(common.Defines.ID_REQUEST_SEND_PARAM, results.toString());

			/** 遷移先設定 */
			strPage = "/jsp/reports/" + inclurepname + ".jsp";
			getServletContext().getRequestDispatcher(strPage).forward(request, response);

		}
	}

	/**
	 * @param id オブジェクトID
	 * @param value 値
	 * @param text 値
	 * @return JSONObject
	 */
	private JSONObject getJson(String id, Object value, Object text) {
		JSONObject json = new JSONObject();

		json.put("id", id);
		json.put("value", ObjectUtils.toString(value, ""));
		json.put("text", ObjectUtils.defaultIfNull(text, ObjectUtils.toString(value, "")));

		return json;
	}

	/**
	 * ファイルアップロード
	 *
	 * @param request リクエスト
	 * @param sc サーブレットコンテキスト
	 * @throws Exception 例外
	 */
	public static void fileUpload(HttpServletRequest request, HttpSession session, ServletContext sc) throws Exception {
		// ファクトリー生成
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1426);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(80 * 1024);
		upload.setFileSizeMax(80 * 1024);

		List<?> items = upload.parseRequest(request);

		FileItem fItem = null;
		String radval = null;	// マスタテーブル指定ラジオボタン
		// 全フィールドに対するループ
		for (Object val : items) {
			FileItem item = (FileItem) val;
			if (item.isFormField()) {
				// type="file"以外のフィールド
				if (StringUtils.equals(item.getFieldName(), Form.MFU_RAD_UP)) {
					radval = item.getString("UTF-8");
				}
			} else {
				// type="file"のフィールド
				fItem = item;
			}
		}
		//値をセッションに格納
		session.setAttribute(Defines.STR_SES_FILE_TYPE, radval);

		String cutMoji = ",";
		// ファイル名設定処理 ------------------------------
		String upfilename = Defines.UPLOAD_FILE_PATH;
		if (radval.trim().equals("0")) {		// 売上目標
			upfilename += Defines.UPLOAD_FILE_URIMOK;
		} else if (radval.trim().equals("1")) {	// 海外為替
			upfilename += Defines.UPLOAD_FILE_KAWASE;
		} else if (radval.trim().equals("2")) {	// 新規獲得_計画数
			upfilename = Defines.UPLOAD_FILE_PLAN_Y;
		} else if (radval.trim().equals("3")) {	// 新規獲得_媒体別費用
			upfilename = Defines.UPLOAD_FILE_BAITAI_HIYOU;
		} else if (radval.trim().equals("4")) {	// プロモ商品計画数（通店プロモ商品速報）
			upfilename = Defines.UPLOAD_FILE_PLAN_PROMO;
//			cutMoji = "\t";	// タブ区切り文字
		} else if (radval.trim().equals("5")) {	// 商品実績_カテゴリ
			upfilename = Defines.UPLOAD_FILE_CATEGORY;
		} else if (radval.trim().equals("6")) {	// 商品実績_重点商材
			upfilename = Defines.UPLOAD_FILE_JUTEN_SHOZAI;
		} else if (radval.trim().equals("7")) {	// 新規獲得_商材指定
			upfilename = Defines.UPLOAD_FILE_KOUNYU_1ST;
		}
		upfilename = sc.getRealPath(upfilename);
		// -------------------------------------------------

		// ファイルアップロード処理 ------------------------
		// ファイルを作業フォルダに保存
		fItem.write(new File(upfilename));

		// 上記で保存したファイルの本体部の読み込みを行い、正しいデータか否か1行づつチェック
		UploadValidation vld = new UploadValidation();
		FileReadWriter fr = new FileReadWriter(upfilename); // ファイルを開く
		long row = 0L;
		boolean inpflg = true;
		String[] linearr = fr.read("\n").split("\n", -1);
		for (int i = 0; i < linearr.length; i++) { // 1行ずつ読み込む
			row++;	//カウントアップ
			//1行目はヘッダー扱いとして読み飛ばす
			if (row > 1) {
				String line = linearr[i].replaceAll("\"", "");	//「"」を削除する
				if (line.length() > 0) {
					//カンマで分割（空白も保持する）
					String[] strdata = line.split(cutMoji, -1);
					//前後のスペース削除
					int j = 0;	//カウンタ
					for (j = 0; j < strdata.length; j++) {
						strdata[j] = strdata[j].trim();
					}
					if (!vld.UpDown_Download(strdata, radval.trim(), row)) {
						/* エラーメッセージの設定 */
						request.setAttribute(Defines.STR_FRM_MSGTYPE, Defines.STR_VAL_MSGTYPE_ERR);
						request.setAttribute(Defines.STR_FRM_MSG, vld.OutMsg());
						inpflg = false;
						break;
					}
				}
			}
		}
		//ファイル存在、空ファイルチェック
		if (row <= 1 && inpflg) {
			request.setAttribute(Defines.STR_FRM_MSGTYPE, Defines.STR_VAL_MSGTYPE_ERR);
			request.setAttribute(Defines.STR_FRM_MSG, FILLE_NULL_OR_NATHING);
			inpflg = false;
		}

		//データチェックでエラーが無ければテーブルへデータを設定する。
		if (inpflg) {
			//テーブルへデータを設定
			UploadDAO dao = new UploadDAO();
			if (radval.trim().equals("0")	// 売上目標
				||radval.trim().equals("1")	// 海外為替
				) {
				dao.setImportFile(radval.trim(), upfilename);

			} else {	// その他
				dao.setUploadFile(radval.trim(), upfilename);

			}


			//メッセージ設定
			request.setAttribute(Defines.STR_FRM_MSGTYPE, Form.COMMON_MSGTYPE_SUP);
			request.setAttribute(Defines.STR_FRM_MSG, Message.SUP_UPLOAD_END);
		}
	}

	/**
	 * ファイルダウンロード
	 *
	 * @param response レスポンス
	 * @param sc サーブレットコンテキスト
	 * @param radval 選択項目
	 * @param ymval 選択年月
	 * @throws Exception 例外
	 */
	public static void fileDownload(HttpServletResponse response, ServletContext sc, String radval, String ymval) throws Exception {
		// ファイル名設定処理 ------------------------------
		String dwnfilename = "";
		List<String> pramData = new ArrayList<String>();

		if (radval.trim().equals("0")) {	// 売上目標
			dwnfilename = Defines.UPLOAD_FILE_URIMOK;
			pramData.add(ymval);

		} else if (radval.trim().equals("1")) {	// 海外為替
			dwnfilename = Defines.UPLOAD_FILE_KAWASE;

		} else if (radval.trim().equals("2")) {	// 新規獲得_計画数
			dwnfilename = Defines.UPLOAD_FILE_PLAN_Y;

		} else if (radval.trim().equals("3")) {	// 新規獲得_媒体別費用
			dwnfilename = Defines.UPLOAD_FILE_BAITAI_HIYOU;

		} else if (radval.trim().equals("4")) {	// プロモ商品計画数（通店プロモ商品速報）
			dwnfilename = Defines.UPLOAD_FILE_PLAN_PROMO;
			pramData.add(ymval);

		} else if (radval.trim().equals("5")) {	// 商品実績_カテゴリ
			dwnfilename = Defines.UPLOAD_FILE_CATEGORY;

		} else if (radval.trim().equals("6")) {	// 商品実績_重点商材
			dwnfilename = Defines.UPLOAD_FILE_JUTEN_SHOZAI;

		} else if (radval.trim().equals("7")) {	// 新規獲得_商材指定
			dwnfilename = Defines.UPLOAD_FILE_KOUNYU_1ST;
		}
		//ファイル名
		String fileName1 = dwnfilename;
		String fileName2 = sc.getRealPath(Defines.DOWNLOAD_FILE_PATH + dwnfilename);

		DownloadDAO dao = new DownloadDAO();
		dao.setOutputFile(radval.trim(), fileName2, pramData);
		// -------------------------------------------------

		// ファイルダウンロード処理 ------------------------
		//ファイル名の送信(attachment部分をinlineに変更すればインライン表示)
		response.setHeader("Content-disposition", "attachment; filename=\"" + fileName1.substring(1, fileName1.length()) + "\"");
		//ファイル内容の出力
		ServletOutputStream outfile = response.getOutputStream();
		FileInputStream infile = new FileInputStream(fileName2);
		// 受信用バッファ(デフォルト値>>4096)
		byte[] buffer = new byte[4096];
		int size;
		while ((size = infile.read(buffer, 0, buffer.length)) != -1) {
			outfile.write(buffer, 0, size);
		}
		infile.close();
		outfile.close();
		// -------------------------------------------------
	}

	/**
	 * 年月情報取得 <br />
	 * レポートコード DBよりユーザー情報を全て取得し、ArrayList形式で戻す。<br />
	 *
	 * @return 取得したArrayList形式 <br />
	 * @throws Exception 例外 <br />
	 */
	protected List<Map<String, String>> getYmRecord() throws Exception {
		List<Map<String, String>> list = null;
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			ResultSetHandler<?> rsh = new MapListHandler();
			list = (List<Map<String, String>>) qr.query(SQL.MOK_SEL_YM, rsh);
		} catch (Exception en) {
			System.out.println("DBUtils NullPointerException:MaintenanceUser_getYmRecord()");
			en.printStackTrace();
		}
		return list;
	}

	private void processUploadedFile(FileItem item) throws IOException, ServletException {
		File f = new File(item.getName());
		try {
			item.write(new File("C:\\work\\orbis", f.getName()));
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void processFormField(FileItem item) throws ServletException {

		System.out.println(item.getFieldName());
		try {
			System.out.println(item.getString("MS932"));
		} catch (UnsupportedEncodingException e) {
			throw new ServletException(e);
		}
	}
}