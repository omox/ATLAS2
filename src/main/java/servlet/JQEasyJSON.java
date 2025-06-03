package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.DefineReport;
import dto.JQEasyModel;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Servlet implementation class JQeasyJSON
 */
public class JQEasyJSON extends HttpServlet {
	private static final long serialVersionUID = -5961333752928159592L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public JQEasyJSON() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			// 文字変換コード設定【重要】
			request.setCharacterEncoding("UTF-8");
			//System.out.println("JQEasyJSONの実行");

			// パラメータ一覧【確認】
			HashMap<String,String> map = new HashMap<String,String>();
			Enumeration<String> enums = request.getParameterNames();
			while( enums.hasMoreElements() ) {
				String name = enums.nextElement();
				if (DefineReport.ID_DEBUG_MODE) System.out.println(name + "=" + request.getParameter( name ));
				map.put(name, request.getParameter( name ));
			}

			// レポート情報
			String report = request.getParameter("report");

			// パラメータ取得
			String prePage = request.getParameter("page");
			if (prePage == null) prePage = "1";
			int page = Integer.parseInt(prePage);	// ページ番号
			//int page = 1;	// ページ番号
			String preRow = request.getParameter("rows");
			if (preRow == null) preRow = "30";
			int limit = Integer.parseInt(preRow);	// ページ辺りの表示レコード数
			String sidx = request.getParameter("sort");					// ソート項目
			String sord = request.getParameter("order");				// ソート順番(asc or desc)
			if(sidx == null ) sidx = "1";

			// レコード数
			String preRecords = request.getParameter("records");
			if (preRecords == null) preRecords = "0";
			int records = Integer.parseInt(preRecords);

			// セッション
			HttpSession session = request.getSession(false);

			// レコード情報の格納先(JSONObject)作成
			JSONObject jsonOB = new JSONObject();

			// jqEasy 用 JSON モデル作成
			JQEasyModel json = new JQEasyModel();

			// 項目単位の情報格納
			List<JSONObject> lineData = new ArrayList<JSONObject>();

			// レコードカウント
			int count = -1;

			// セルインデックス
			int index = 0;

			if ( (ArrayList) session.getAttribute("table") != null) {

				ArrayList al = (ArrayList) session.getAttribute("table");
				records = al.size();
				if (records > 0)	records--;	// タイトル行の除外
				json.setTotal(records);		// 総レコード数の設定

				// 総ページ数の算出
				int total_pages=0;
				double ii = (double)records/(double)limit;
				if( records > 0 ) {
					total_pages = (int) Math.ceil(ii);
				} else {
					total_pages = 0;
				} // if for some

				if ( page  > total_pages) {
					page = total_pages; // calculate the starting position of the rows
				}

				int start = limit * page - limit; // if for some reasons start position

				if( start < 0 ) {
					start = 0;
				}


				Iterator itr = al.iterator();
				//if (itr.hasNext())	itr.next();	// タイトル情報スキップ

				// 項目単位の情報格納
				lineData = new ArrayList<JSONObject>();

				while (itr.hasNext()) {

					count++;

					// 表示範囲の情報取得
					if ( ((start + 1) <= count) && (count <= (start + limit)) ) {

						// jqGrid 用レコード情報準備
						JSONObject n1 = new JSONObject();

						// セル（列）情報リスト
						ArrayList columnsList = (ArrayList) itr.next();
						Iterator itrCols = columnsList.iterator();

						index=0;

						while (itrCols.hasNext()) {

							index++;

							// セル（列）生成
							n1.put("F"+String.valueOf(index),(String)itrCols.next());

						}

						// 行情報へセル情報を追加
						lineData.add(n1);

					} else {
						// 次レコード移動
						itr.next();
					}

				}


				if (DefineReport.ID_DEBUG_MODE) System.out.println("flotData size : " + lineData.size());


			} else {
				// 事前検索結果がsessionに保持されていません。
				System.out.println("table属性がsessionに保持されていません。");
			}

			// レコード情報の格納（JSON形式変換用）
			json.setRows(lineData);

			// JSON 形式へ変換
			jsonOB = JSONObject.fromObject(JSONSerializer.toJSON(json));
			if (DefineReport.ID_DEBUG_MODE) System.out.println(jsonOB.toString());


			// JSON データのロード
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(jsonOB);
		} catch (NumberFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}
