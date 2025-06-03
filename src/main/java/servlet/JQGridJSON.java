package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.time.LocalDateTime;
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
import common.Defines;
import dao.A01Dao;
import dao.A02Dao;
import dao.A03Dao;
import dao.A04Dao;
import dao.B01Dao;
import dao.B01aDao;
import dao.B01bDao;
import dao.B021Dao;
import dao.B02Dao;
import dao.B02aDao;
import dao.B02bDao;
import dao.B02cDao;
import dao.B031Dao;
import dao.B03Dao;
import dao.B04Dao;
import dao.D00Dao;
import dao.D11Dao;
import dao.D12Dao;
import dao.D13Dao;
import dao.D14Dao;
import dao.D15Dao;
import dao.D16Dao;
import dao.D17Dao;
import dao.D18Dao;
import dao.D19Dao;
import dao.D20Dao;
import dao.D21Dao;
import dao.D22Dao;
import dao.D23Dao;
import dao.D24Dao;
import dao.D25Dao;
import dao.D26Dao;
import dao.D27Dao;
import dao.D28Dao;
import dao.D29Dao;
import dao.D30Dao;
import dao.D31Dao;
import dao.DBConnection;
import dao.ItemInterface;
import dao.K01Dao;
import dao.L01Dao;
import dao.L02Dao;
import dao.T01Dao;
import dao.T02Dao;
import dao.T03SegDao;
import dao.T04Dao;
import dao.T05ChikuDao;
import dao.T07aDao;
import dao.T07bDao;
import dao.T07mDao;
import dao.T08Dao;
import dao.T10Dao;
import dao.T11Dao;
import dao.T12Dao;
import dao.T13Dao;
import dao.T14Dao;
import dao.T15Dao;
import dao.T16Dao;
import dao.T19Dao;
import dao.T20Dao;
import dao.T21Dao;
import dao.T22Dao;
import dao.T23Dao;
import dao.T24Dao;
import dao.T251Dao;
import dao.T252Dao;
import dao.T253Dao;
import dao.T261Dao;
import dao.T262Dao;
import dao.T27Dao;
import dto.JQEasyModel;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


/**
 * Servlet implementation class JQGridJSON
 */
public class JQGridJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JQGridJSON() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 文字変換コード設定【重要】
		request.setCharacterEncoding("UTF-8");
		//System.out.println("JQGridJSONの実行(content)");

		// パラメータ一覧【確認】
		HashMap<String, String> map = new HashMap<String, String>();
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			if (DefineReport.ID_DEBUG_MODE) System.out.println(name + "=" + request.getParameter( name ));
			// 日本語パラメータ対応
			map.put(name, new String(request.getParameter(name).getBytes("ISO-8859-1"), "UTF-8"));
		}

		// レポート情報
		String report = request.getParameter("report");
		if (report == null) return;

		// セッション
		HttpSession session = request.getSession(false);

		int start = 0;
		int limit = 0;
		if (request.getParameter("rows") != null) {
			limit = Integer.parseInt(request.getParameter("rows")); // ページ辺りの表示レコード数;
		}
		boolean runSelect = true;
		// 検索実行
		if (runSelect) {

			// コネクションの取得
			Connection con = null;

			try {
				con = DBConnection.getConnection(Defines.STR_JNDI_DS);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 検索クラス作成
			if (DefineReport.ID_PAGE_D00.equals(report)) {
				/* DSサンプル */
				convertItem(new D00Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D11.equals(report)) {
				/* 通店プロモ商品速報 */
				convertItem(new D11Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D12.equals(report)) {
				/* 通店商品原価率 */
				convertItem(new D12Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D13.equals(report)) {
				/* 商品分類別実績 */
				convertItem(new D13Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D14.equals(report)) {
				/* 新規・リピート別実績 */
				convertItem(new D14Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D15.equals(report)) {
				/* 都道府県別実績 */
				convertItem(new D15Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D16.equals(report)) {
				/* 年齢別実績 */
				convertItem(new D16Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D17.equals(report)) {
				/* 初回購入年別実績 */
				convertItem(new D17Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D18.equals(report)) {
				/* 年間購入額別実績 */
				convertItem(new D18Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D19.equals(report)) {
				/* ステージ別顧客数 */
				convertItem(new D19Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D20.equals(report)) {
				/* 初回購入商品 */
				convertItem(new D20Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D21.equals(report)) {
				/* 新規獲得CPO */
				convertItem(new D21Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D22.equals(report)) {
				/* 新規F2転換率 */
				convertItem(new D22Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D23.equals(report)) {
				/* アクティブ顧客反応率 */
				convertItem(new D23Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D24.equals(report)) {
				/* 3ステップ購入率 */
				convertItem(new D24Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D25.equals(report)) {
				/* 年間チャネル併用顧客数 */
				convertItem(new D25Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D26.equals(report)) {
				/* チャネル新規獲得状況 */
				convertItem(new D26Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D27.equals(report)) {
				/* 国内ポイント実績 */
				convertItem(new D27Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D28.equals(report)) {
				/* アプリ会員登録・起動状況 */
				convertItem(new D28Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D29.equals(report)) {
				/* 通店単価分布 */
				convertItem(new D29Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D30.equals(report)) {
				/* 店舗売上実績 */
				convertItem(new D30Dao(con), request, map, limit, session, start, con);

			} else if (DefineReport.ID_PAGE_D31.equals(report)) {
				/* 個店実績一覧 */
				convertItem(new D31Dao(con), request, map, limit, session, start, con);

				/* DMセグメント別実績 */
			} else if (DefineReport.ID_PAGE_DM_SEGMENT.equals(report)) {
				convertItem(new T03SegDao(con), request, map, limit, session, start, con);
				/* RFM実績 */
			} else if (DefineReport.ID_PAGE_DM_HASOU_RFM.equals(report)) {
				convertItem(new T04Dao(con), request, map, limit, session, start, con);
				/* 都道府県別実績 */
			} else if (DefineReport.ID_PAGE_DM_SECTOR.equals(report)) {
				convertItem(new T05ChikuDao(con), request, map, limit, session, start, con);
				/* 商品実績1 */
			} else if (DefineReport.ID_PAGE_DM_SHOHIN_TSU.equals(report)) {
				convertItem(new T07aDao(con), request, map, limit, session, start, con);
				/* 商品実績条件番号 */
			} else if (DefineReport.ID_PAGE_DM_SHOHIN_TSU2.equals(report)) {
				convertItem(new T07bDao(con), request, map, limit, session, start, con);
				/* 商品実績通販月別 */
			} else if (DefineReport.ID_PAGE_DM_SHOHIN_MON.equals(report)) {
				convertItem(new T07mDao(con), request, map, limit, session, start, con);
				/* DM受注実績 */
			} else if (DefineReport.ID_PAGE_DM_LIST.equals(report)) {
				convertItem(new T08Dao(con), request, map, limit, session, start, con);
				/* 全社売上 */
			} else if (DefineReport.ID_PAGE_B_01.equals(report)) {
				B01Dao.saveCondition(map);					// 検索条件保管
				convertItem(new B01Dao(con), request, map, limit, session, start, con);
				/* 全社売上全体 */
			} else if (DefineReport.ID_PAGE_B_01a.equals(report) || DefineReport.ID_PAGE_GYOSEKI_REPORT.equals(report)) {
				convertItem(new B01aDao(con), request, map, limit, session, start, con);
				/* 全社売上外貨表示 */
			} else if (DefineReport.ID_PAGE_B_01b.equals(report)) {
				B01bDao.saveCondition(map);					// 検索条件保管
				convertItem(new B01bDao(con), request, map, limit, session, start, con);
				/* 店舗売上 */
			} else if (DefineReport.ID_PAGE_B_02.equals(report)) {
				B02Dao.saveCondition(map);					// 検索条件保管
				convertItem(new B02Dao(con), request, map, limit, session, start, con);
				/* 店舗分析 */
			} else if ("ReportB_021".equals(report)) {
				convertItem(new B021Dao(con), request, map, limit, session, start, con);
				/* 店舗分析 */
			} else if (DefineReport.ID_PAGE_B_02a.equals(report)) {
				convertItem(new B02aDao(con), request, map, limit, session, start, con);
				/* 店舗月別 */
			} else if (DefineReport.ID_PAGE_B_02b.equals(report)) {
				convertItem(new B02bDao(con), request, map, limit, session, start, con);
				/* 店舗個店別売上 */
			} else if (DefineReport.ID_PAGE_B_02c.equals(report)) {
				convertItem(new B02cDao(con), request, map, limit, session, start, con);
				/* 通販受注実績 */
			} else if (DefineReport.ID_PAGE_B_03.equals(report)) {
				convertItem(new B03Dao(con), request, map, limit, session, start, con);
				/* 通販受注実績時間 */
			} else if (DefineReport.ID_PAGE_B_031.equals(report)) {
				convertItem(new B031Dao(con), request, map, limit, session, start, con);
				/* 商品出荷実績 */
			} else if (DefineReport.ID_PAGE_B_04.equals(report)) {
				B04Dao.saveCondition(map);					// 検索条件保管
				convertItem(new B04Dao(con), request, map, limit, session, start, con);
			//} else if (DefineReport.ID_PAGE_B_041.equals(report)) {
			//	convertItem(new B041Dao(con), request, map, limit, session, start, con);
				/* 通販分析初回属性 */
			} else if (DefineReport.ID_PAGE_A_01.equals(report)) {
				convertItem(new A01Dao(con), request, map, limit, session, start, con);
				/* 通販分析F2転換率 */
			} else if (DefineReport.ID_PAGE_A_02.equals(report)) {
				convertItem(new A02Dao(con), request, map, limit, session, start, con);
				/* 通販分析媒体 */
			} else if (DefineReport.ID_PAGE_A_03.equals(report)) {
				convertItem(new A03Dao(con), request, map, limit, session, start, con);
				/* 通販分析初回商品 */
			} else if (DefineReport.ID_PAGE_A_04.equals(report)) {
				convertItem(new A04Dao(con), request, map, limit, session, start, con);
				/* RFM別実績 */
			} else if (DefineReport.ID_PAGE_RFM.equals(report)) {
				convertItem(new T01Dao(con), request, map, limit, session, start, con);
				/* スキンチェック */
			} else if (DefineReport.ID_PAGE_SKIN_CHECK.equals(report)) {
				convertItem(new T10Dao(con), request, map, limit, session, start, con);
				/* 月別全プロモーション実績 */
			} else if (DefineReport.ID_PAGE_PROMO_MONTHLY.equals(report)) {
				convertItem(new T11Dao(con), request, map, limit, session, start, con);
				/* 新規プロモーション実績 */
			} else if (DefineReport.ID_PAGE_PROMO_NEW.equals(report)) {
				convertItem(new T12Dao(con), request, map, limit, session, start, con);
				/* 1.5次媒体 */
			} else if (DefineReport.ID_PAGE_PROMO_PLUS.equals(report)) {
				convertItem(new T13Dao(con), request, map, limit, session, start, con);
				/* 単価分布（通販） */
			} else if (DefineReport.ID_PAGE_KOKYAKU_NET.equals(report)) {
				convertItem(new T15Dao(con), request, map, limit, session, start, con);
				/* 単価分布（店舗） */
			} else if (DefineReport.ID_PAGE_KOKYAKU_TENPO.equals(report)) {
				convertItem(new T16Dao(con), request, map, limit, session, start, con);
				/* アクティブ顧客数_R値別 */
			} else if (DefineReport.ID_PAGE_TREND_ALL.equals(report)) {
				convertItem(new T22Dao(con), request, map, limit, session, start, con);
				/* アクティブ顧客数_詳細 */
			} else if (DefineReport.ID_PAGE_CHANEL_TREND.equals(report)) {
				convertItem(new T19Dao(con), request, map, limit, session, start, con);
				/* 減衰 */
			} else if (DefineReport.ID_PAGE_GENSUI.equals(report)) {
				convertItem(new T20Dao(con), request, map, limit, session, start, con);
				/* 新規・リピート顧客数 */
			} else if (DefineReport.ID_PAGE_JUNLE_NEW_REPERT.equals(report)) {
				convertItem(new T21Dao(con), request, map, limit, session, start, con);
				/* ジャンル併買顧客数 */
			} else if (DefineReport.ID_PAGE_HEIBAI.equals(report)) {
				convertItem(new T27Dao(con), request, map, limit, session, start, con);
				/* 商品分類別顧客数 */
			} else if (DefineReport.ID_PAGE_JUNLE_JUCHU.equals(report)) {
				convertItem(new T251Dao(con), request, map, limit, session, start, con);
				/* 商品分類別年齢別顧客数 */
			} else if (DefineReport.ID_PAGE_JUNLE_NENREI.equals(report)) {
				convertItem(new T252Dao(con), request, map, limit, session, start, con);
				/* 商品分類別_限定期間内 */
			} else if (DefineReport.ID_PAGE_JUNLE_4Q.equals(report)) {
				convertItem(new T253Dao(con), request, map, limit, session, start, con);
				/* 都道府県別顧客数 */
			} else if (DefineReport.ID_PAGE_AREA_SHARE1.equals(report)) {
				convertItem(new T261Dao(con), request, map, limit, session, start, con);
				/* 都道府県別・年齢別 */
			} else if (DefineReport.ID_PAGE_AREA_SHARE2.equals(report)) {
				convertItem(new T262Dao(con), request, map, limit, session, start, con);
				/* F0顧客数 */
			} else if (DefineReport.ID_PAGE_F0.equals(report)) {
				convertItem(new T02Dao(con), request, map, limit, session, start, con);
				/* DMアクティブ顧客数 */
			} else if (DefineReport.ID_PAGE_TREND_ACTIVE.equals(report)) {
				convertItem(new T23Dao(con), request, map, limit, session, start, con);
				/* 転換率 */
			} else if (DefineReport.ID_PAGE_TREND_CONVERT.equals(report)) {
				convertItem(new T24Dao(con), request, map, limit, session, start, con);
				/* 登録店舗別顧客数 */
			} else if (DefineReport.ID_PAGE_STORE_START_END.equals(report)) {
				convertItem(new T14Dao(con), request, map, limit, session, start, con);
				/* 韓国顧客RFM顧客数 */
			} else if (DefineReport.ID_PAGE_K_RFM.equals(report)) {
				convertItem(new K01Dao(con), request, map, limit, session, start, con);
				/* アクセスログ */
			} else if (DefineReport.ID_PAGE_LOG_LIST.equals(report)) {
				convertItem(new L01Dao(con), request, map, limit, session, start, con);
				/* 初回商品マスタ表示 */
			} else if (DefineReport.ID_PAGE_SHOHIN_LIST.equals(report)) {
				convertItem(new L02Dao(con), request, map, limit, session, start, con);
			} else {
				// デモ用クラス
				//convertItem(new ItemDemoDao(con), request, map, limit, session, start, con);
			}
		}

		// レコード情報の格納先(JSONObject)作成
		JSONObject jsonOB = new JSONObject();

		// jqEasy 用 JSON モデル作成
		JQEasyModel json = new JQEasyModel();

		// 項目単位の情報格納
		List<JSONObject> lineData = new ArrayList<JSONObject>();

		// タイトル情報格納
		ArrayList titleData = new ArrayList();

		// レコードカウント
		int count = -1;

		// セルインデックス
		int index = 0;

		String states = "";

		if ((ArrayList) session.getAttribute("table") != null) {

			ArrayList al = (ArrayList) session.getAttribute("table");
			int records = al.size();
			if (records > 0) records--; // タイトル行の除外
			json.setTotal(records); // 総レコード数の設定

			int page = 1; // ページ位置（初期値）

			// 総ページ数の算出
			int total_pages = 0;
			double ii = (double) records / (double) limit;
			if (records > 0) {
				total_pages = (int) Math.ceil(ii);
			} else {
				total_pages = 0;
			} // if for some

			if (page  > total_pages) {
				page = total_pages; // calculate the starting position of the rows
			}

			start = limit * page - limit; // if for some reasons start position

			if (start < 0) {
				start = 0;
			}

			int state = 0;
			boolean stateFlag = false;

			Iterator itr = al.iterator();

			// 項目単位の情報格納
			lineData = new ArrayList<JSONObject>();


			while (itr.hasNext()) {

				count++;

				// 表示範囲の情報取得
				if (((start + 1) <= count) && (count <= (start + limit))) {

					// jqGrid 用レコード情報準備
					JSONObject n1 = new JSONObject();

					// セル（列）情報リスト
					ArrayList columnsList = (ArrayList) itr.next();
					Iterator itrCols = columnsList.iterator();

					index = 0;

					while (itrCols.hasNext()) {

						index++;

						if (state == index) {
							// easyui.treegrid state : closed
							states = (String) itrCols.next();
							n1.put("state", states);
							if (!"".equals(states) && states != null) {
								n1.put("iconCls", "icon-ok");
							}
							// itrCols.next(); // 次のセルへ移動
						} else {
							// セル（列）生成
							n1.put("F" + String.valueOf(index), (String) itrCols.next());
						}

					}

					// 行情報へセル情報を追加
					lineData.add(n1);

				} else if (count == 0) {

					// タイトル
					// セル（列）情報リスト
					ArrayList columnsList = (ArrayList) itr.next();
					Iterator itrCols = columnsList.iterator();
					titleData = columnsList;

					while (itrCols.hasNext()) {
						state++;
						if ("STATE".equals(itrCols.next().toString())) {
							stateFlag = true;
							break;
						}
					}
					// state カラムがない場合は、初期化
					if (!stateFlag) {
						state = 0;
					}

				} else {
					// 次レコード移動
					itr.next();
				}
			}

			if (DefineReport.ID_DEBUG_MODE) {
				System.out.println("flotData size : " + lineData.size());
			}

		} else {
			// 事前検索結果がsessionに保持されていません。
			System.out.println("table属性がsessionに保持されていません。");
		}

		// レコード情報の格納（JSON形式変換用）
		json.setRows(lineData);

		// JQEasyJSONでレコード情報格納の場合
		if (lineData.isEmpty()) {
			json.setRows(titleData);
		}
		// grid用設定
		HashMap<String, String> setting = (HashMap<String, String>) session.getAttribute(DefineReport.ID_SESSION_SETTING);
		if (setting != null) {
			List<JSONObject> opts = new ArrayList<JSONObject>();
			for (String key : setting.keySet()) {
				JSONObject obj = new JSONObject();
				obj.put(key, setting.get(key));
				opts.add(obj);
			}
			if (opts.size() > 0) {
				json.setOpts(opts);
			}
		}

		// JSON 形式へ変換
		jsonOB = JSONObject.fromObject(JSONSerializer.toJSON(json));
		if (DefineReport.ID_DEBUG_MODE) {
			System.out.println(jsonOB.toString());
		}


		// JSON データのロード
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(jsonOB);

	}

	/**
	 * 分析
	 *
	 * @param ItemInterface
	 * @param request
	 * @param map
	 * @param limit
	 * @param session
	 * @param start
	 * @param con
	 */
	private void convertItem(ItemInterface shopItem, HttpServletRequest request,
			HashMap<String, String> map, int limit, HttpSession session,
			int start, Connection con) {

		try {
			// 条件セット
			shopItem.setMap(map);
			// 検索条件（Excel出力用）
			shopItem.setJson((String) session.getAttribute(DefineReport.ID_SESSION_STORAGE));
			// 検索開始位置取得
			shopItem.setStart(start);
			// 検索取得数
			shopItem.setLimit(limit);
			System.out.println(shopItem.getClass().getName()+ " " +  LocalDateTime.now().toString());
			// SQL 実行
			shopItem.selectBy();
			// セッション保持
			session.setAttribute(DefineReport.ID_SESSION_TABLE, shopItem.getTable());
			session.setAttribute(DefineReport.ID_SESSION_WHERE, shopItem.getWhere());
			session.setAttribute(DefineReport.ID_SESSION_META,  shopItem.getMeta());
			session.setAttribute(DefineReport.ID_SESSION_SETTING, shopItem.getSetting());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
