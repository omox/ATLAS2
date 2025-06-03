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

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import dto.JQFlotData;
import dto.JQFlotModel;

/**
 * Servlet implementation class JQFlotJSON
 */
public class JQFlotJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public JQFlotJSON() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 文字変換コード設定【重要】
		request.setCharacterEncoding("UTF-8");
		System.out.println("JQFlotJSONの実行");

		// パラメータ一覧【確認】
		HashMap<String,String> map = new HashMap<String,String>();
		Enumeration<String> enums = request.getParameterNames();
		while( enums.hasMoreElements() ) {
			String name = enums.nextElement();
			System.out.println(name + "=" + request.getParameter( name ));
			map.put(name, request.getParameter( name ));
		}

		// レポート情報
		String report = request.getParameter("report");
		if (report == null) return;

		// 読込基準
		int labelPosition = 1;	// label : 項目
		int xaxisPosition = 2;	// xaxis : X軸
		int yaxisPosition = 3;	// yaxis : Y軸

		// セッション
		HttpSession session = request.getSession(false);

		// レコード情報の格納先(JSONObject)作成
		JSONObject jsonOB = new JSONObject();

		// jqFlot 用 JSON モデル作成
		JQFlotModel json = new JQFlotModel();

		// jqFlot 用 JSON モデル(レコード情報)作成
		ArrayList<JQFlotData> flotData = new ArrayList<JQFlotData>();

		// レコードカウント
		int count = -1;

		// セルインデックス
		int index = 0;

		String preLabel="";
		String oldLabel="";

		if ( (ArrayList) session.getAttribute("table") != null) {

			ArrayList al = (ArrayList) session.getAttribute("table");
			Iterator itr = al.iterator();
			if (itr.hasNext())	itr.next();	// タイトル情報スキップ

			// jqFlot 用 JSON モデル（レコード情報）作成
			JQFlotData jq = new JQFlotData();

			// 項目単位の情報格納
			List<List<String>> lineData = new ArrayList<List<String>>();

			// axis単位の情報格納
			List<String> axisData = new ArrayList<String>();

			while (itr.hasNext()) {

				count++;

				// セル（列）情報リスト
				ArrayList columnsList = (ArrayList) itr.next();
				Iterator itrCols = columnsList.iterator();

				index=0;

				// 1レコード単位の情報格納
				axisData = new ArrayList<String>();

				while (itrCols.hasNext()) {

					index++;
//					System.out.println(count + " 行," + index + " 列");

					if (index == labelPosition){

						// 項目の判別情報取得
						preLabel = itrCols.next().toString();

						// 項目が異なるとき
						if (!oldLabel.equals(preLabel)){

							// 1行目は実行しない
							if ( 1 < count){
								// 項目単位に追加
								jq.setData(lineData);
								flotData.add(jq);
							}
							// 項目単位の情報格納
							lineData = new ArrayList<List<String>>();

							// jqFlot 用レコード情報準備
							jq = new JQFlotData();
							jq.setLabel(preLabel);	// ラベル情報
							oldLabel = preLabel;	// 前行比較情報としてセット

						}

					} else if ( (index == xaxisPosition) || (index == yaxisPosition)) {

						// [x,y] 情報生成
						axisData.add((String)itrCols.next());

					} else {
						// その他セルは空回し
						itrCols.next();
					}

				}

				// 項目単位追加
				lineData.add(axisData);

			}

			// 項目単位に追加
			if ((lineData!=null) && (jq!=null)){
				jq.setData(lineData);
				flotData.add(jq);
			}

			System.out.println("flotData size : " + flotData.size());


		} else {
			// 事前検索結果がsessionに保持されていません。
			System.out.println("table属性がsessionに保持されていません。");
		}

		// レコード情報の格納（JSON形式変換用）
		json.setRows(flotData);

		// JSON 形式へ変換
		jsonOB = JSONObject.fromObject(JSONSerializer.toJSON(json));
		System.out.println(jsonOB.toString());


		// JSON データのロード
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(jsonOB);
	}


}
