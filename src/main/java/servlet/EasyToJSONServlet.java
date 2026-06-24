package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import authentication.bean.User;
import authentication.defines.Consts;
import authentication.defines.Form;
import common.ChkSysTime;
import common.DefineReport;
import common.Defines;
import common.ItemList;
import dao.DBConnection;
import dto.JQEasyModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Servlet implementation class EasyToJSONServlet<br/>
 *
 */
public class EasyToJSONServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public EasyToJSONServlet() {}

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // 文字変換コード設定【重要】
    request.setCharacterEncoding("UTF-8");

    // パラメータ一覧【確認】
    HashMap<String, String> maps = new HashMap<>();
    Enumeration<String> enums = request.getParameterNames();
    while (enums.hasMoreElements()) {
      String name = enums.nextElement();
      if (DefineReport.ID_DEBUG_MODE)
        System.out.println(name + "=" + request.getParameter(name));
      maps.put(name, request.getParameter(name));
    }

    // パラメータの確認
    String outpage = maps.get(DefineReport.ID_PARAM_PAGE) == null ? "" : maps.get(DefineReport.ID_PARAM_PAGE);
    String outobj = maps.get(DefineReport.ID_PARAM_OBJ) == null ? "" : maps.get(DefineReport.ID_PARAM_OBJ);
    String outsel = maps.get(DefineReport.ID_PARAM_VAL) == null ? "" : maps.get(DefineReport.ID_PARAM_VAL);
    String outjson = maps.get(DefineReport.ID_PARAM_JSON) == null ? "" : maps.get(DefineReport.ID_PARAM_JSON);
    String outaction = maps.get(DefineReport.ID_PARAM_ACTION) == null ? DefineReport.ID_PARAM_ACTION_DEFAULT : maps.get(DefineReport.ID_PARAM_ACTION);
    String outtarget = maps.get(DefineReport.ID_PARAM_TARGET) == null ? "" : maps.get(DefineReport.ID_PARAM_TARGET);
    String outobjtyp = maps.get(DefineReport.ID_PARAM_OBJ_TYPE) == null ? "" : maps.get(DefineReport.ID_PARAM_OBJ_TYPE);

    // 最大検索取得数
    // String maxRows = maps.get(DefineReport.ID_SEARCHJSON_PARAM_MAXROW) == null ? "10" :
    // maps.get(DefineReport.ID_SEARCHJSON_PARAM_MAXROW);
    // 検索キー
    String nameWith = maps.get(DefineReport.ID_SEARCHJSON_PARAM_NAMEWITH) == null ? "" : maps.get(DefineReport.ID_SEARCHJSON_PARAM_NAMEWITH);

    if ("".equals(nameWith)) {
      nameWith = "%";
    } else {
      nameWith = "%" + nameWith + "%";
    }

    // セッションの取得
    HttpSession session = request.getSession();

    // JSON 戻り値格納
    String json = "";

    // 配列準備
    ArrayList<String> paramData = new ArrayList<>();

    // SQL構文
    String sqlcommand = "";

    // JSONパラメータの解析
    JSONArray map = new JSONArray();
    if (!"".equals(outjson)) {
      map = (JSONArray) JSONSerializer.toJSON(outjson);
    }

    String jndiName = Defines.STR_JNDI_DS;

    // ページ単位に処理分離
    try {

      // アクション指定
      if (outaction.equals(DefineReport.ID_PARAM_ACTION_GET)) {

        // 検索ボタン押下
        if (outobj.equals(DefineReport.Button.SEARCH.getObj())) {

          /* 利用可能時間チェック */
          ChkSysTime sys = new ChkSysTime();

          if (!sys.isUsefull()) {
            // 利用時間外画面
            sqlcommand = "select 1 from dual";
          } else {
            //
            sqlcommand = "select 0 from dual";
          }
        }
        // 検索ボタン押下
        if (outobj.equals(DefineReport.Button.SEARCH2.getObj())) {

          /* 利用可能時間チェック */
          ChkSysTime sys = new ChkSysTime();

          if (!sys.isUsefull()) {
            // 利用時間外画面
            sqlcommand = "select 1 from dual";
          } else {
            //
            sqlcommand = "select 0 from dual";
          }
        }

      } else if (outaction.equals(DefineReport.ID_PARAM_ACTION_DEFAULTS)) {
        // 初期値取得

        // 基準月
        if (outobj.equals(DefineReport.Select.MM_KIJUN.getObj())) {

          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_RFM)) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM YM_KIJUN_T01";
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_DM_SEGMENT) || StringUtils.equals(outpage, DefineReport.ID_PAGE_D29)// 通店単価分布
          ) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM DS_M_YM_TNK";
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_NENREI)) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM YM_KIJUN_T252";
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_LOG_LIST)) {
            sqlcommand = "SELECT DISTINCT TO_CHAR(MAX(DT_ACTION), 'MM') AS VALUE, TO_CHAR(MAX(DT_ACTION), 'MM') AS TEXT, TO_NUMBER(TO_CHAR(MAX(DT_ACTION), 'YYYYMM')) AS SORT FROM AK_SYS_LOGS";

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D11)// 通店プロモ商品速報
          ) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM  DS_M_YM_PRM";

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D12)// 通店商品原価率
          ) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM  DS_M_YM_GENKA";

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_03)) { // 通店受注実績
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM DS_M_YM_JUC";

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D13)// 商品分類別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D14)// 新規・リピート別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D15)// 都道府県別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D16)// 年齢別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D17)// 初回購入年別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D18)// 年間購入額別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D19)// ステージ別顧客数
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D20)// 初回購入商品
          ) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM DS_M_YM_URI";

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D21)// 新規獲得CPO
          ) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM DS_M_YM_CPO";

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D27)// 国内ポイント実績
          ) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM DS_M_YM_PNT";

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D30)// 店舗売上実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D31)// 個店実績一覧
          ) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM DS_M_YM_TEN";


          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_UPLOAD)// マスタ更新
          ) {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(URI_YM), 5, 2) AS VALUE, SUBSTR(MAX(URI_YM), 6, 3) AS TEXT, TO_NUMBER(MAX(URI_YM)) AS SORT FROM URI_MOK";

          } else {
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(KIJUN_YM), 5, 2) AS VALUE, SUBSTR(MAX(KIJUN_YM_MEI), 6, 3) AS TEXT, TO_NUMBER(MAX(KIJUN_YM)) AS SORT FROM YM_KIJUN_DM";
          }
        }
        // 基準月
        if (outobj.equals(DefineReport.Select.MM_KIJUN_TO.getObj())) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_UPLOAD)) {
            // マスタ更新
            sqlcommand = "SELECT DISTINCT SUBSTR(MAX(YM), 5, 2) AS VALUE, SUBSTR(MAX(YM), 6, 3) AS TEXT, TO_NUMBER(MAX(YM)) AS SORT FROM DS_B_PROMO_PLAN";
          }
        }

        // 基準日
        if (outobj.equals(DefineReport.Select.DD_KIJUN.getObj())) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_LOG_LIST)) {
            sqlcommand = "SELECT DISTINCT TO_CHAR(MAX(DT_ACTION), 'DD') AS VALUE, TO_CHAR(MAX(DT_ACTION), 'DD') AS TEXT, TO_NUMBER(TO_CHAR(MAX(DT_ACTION), 'YYYYMMDD')) AS SORT FROM AK_SYS_LOGS";
          }
        }

      } else if (outaction.equals(DefineReport.ID_PARAM_ACTION_INIT)) {
        // 初期化処理
        // レポート更新チェック
        if (outobj.equals(DefineReport.Label.CHECK.getObj())) {
          sqlcommand = DefineReport.ID_SQL_REPORT_CHECK + "'" + outpage + "'";
        }
        // 通販受注実績予想数
        if (outobj.equals(DefineReport.Label.TSU.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_TSU_JUC;
        }

        /**
         * ダッシュボード用
         */
        if (StringUtils.equals(outpage, "DSB")) {
          if (outobj.equals("selectyear")) {
            // 表示年月.年
            sqlcommand = "SELECT DISTINCT YY AS VALUE, YY AS TEXT, YY AS SORT FROM DS_DASHBOARD ORDER BY SORT DESC";
          } else if (outobj.equals("selectmonth")) {
            // 表示年月.月
            sqlcommand = "SELECT DISTINCT MAX(MM) AS VALUE, MAX(MM) AS TEXT, MAX(MM) AS SORT FROM DS_DASHBOARD WHERE YY=?";
            paramData.add(0, ObjectUtils.toString(maps.get("YY")));
          } else if (outobj.equals("dashboard")) {
            // ダッシュボード用全情報
            sqlcommand = "SELECT KB,AXIS,SEQ,VAL,DISPLAY FROM DS_DASHBOARD WHERE YY=? AND MM=? ORDER BY YY,MM,KB,AXIS,SEQ";
            paramData.add(0, ObjectUtils.toString(maps.get("YY")));
            paramData.add(1, ObjectUtils.toString(maps.get("MM")));
          }
        }

        // 基準年月
        if (outobj.equals(DefineReport.Select.YM_KIJUN.getObj()) || outobj.equals(DefineReport.Select.YM_KIJUN_TO.getObj())) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_RFM)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T01;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_F0)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T02;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_GENSUI)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T20;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_NEW_REPERT)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T21;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_TREND_ALL)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T22;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_TREND_ACTIVE)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T23;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_TREND_CONVERT)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T24;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_JUCHU)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T251;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_NENREI)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T252;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_AREA_SHARE1)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T261;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_AREA_SHARE2)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T262;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_HEIBAI)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T27;
          } else if ((StringUtils.equals(outpage, DefineReport.ID_PAGE_A_01)) || (StringUtils.equals(outpage, DefineReport.ID_PAGE_A_03)) || (StringUtils.equals(outpage, DefineReport.ID_PAGE_A_04))) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_TSU;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_031)) { // 通店受注実績_時間別
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_B031;

            // } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_03)) {
            // sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_B03;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_02a)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_B02A;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_021)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_B02;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_LOG_LIST)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_LOG;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_SKIN_CHECK)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T10;
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_DM;
          }
        }
        // 基準年
        if (outobj.equals(DefineReport.Select.YY_KIJUN.getObj()) || outobj.equals(DefineReport.Select.YY_KIJUN_TO.getObj())) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_CHANEL_TREND)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T19;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_TREND_ALL)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T22;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_TREND_ACTIVE)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T23;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_JUCHU)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T251;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_NENREI)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T252;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_4Q)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T253;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_HEIBAI)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T27;
          } else if ((StringUtils.equals(outpage, DefineReport.ID_PAGE_B_01)) || (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_01a)) || (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_01b)) || (StringUtils.equals(outpage, DefineReport.ID_PAGE_GYOSEKI_REPORT))) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_B01;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_02)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_B02;
          } else if ((StringUtils.equals(outpage, DefineReport.ID_PAGE_B_02b)) || (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_02c))) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_B02A;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_03)) { // 通店受注実績
            // sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_B03;
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_JUC;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_04)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_B04;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_RFM)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T01;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_DM_SEGMENT)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_T03;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_LOG_LIST)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_L01;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D00)) { // DSサンプル
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN_D00;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D11)// 通店プロモ商品速報
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_PRMURI;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D12)// 通店商品原価率
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_GEN;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D30)// 店舗売上実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D31)// 個店実績一覧
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_TEN;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D13)// 商品分類別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D14)// 新規・リピート別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D15)// 都道府県別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D16)// 年齢別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D17)// 初回購入年別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D18)// 年間購入額別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D20)// 初回購入商品
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D23)// アクティブ顧客反応率
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D25)// 年間チャネル併用顧客数
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_URI;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D19)// ステージ別顧客数
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_STG;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D21)// 新規獲得CPO
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_CPO;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D22)// 新規F2転換率
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_F2C;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D24)// 3ステップ購入率
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_STEP;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D27)// 国内ポイント実績
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_PNT;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D26)// チャネル新規獲得状況
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_CNC;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D28)// アプリ会員登録・起動状況
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_APP;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D29)// 通店単価分布
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_TNK;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_UPLOAD)// マスタ更新
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_MUP;

            if (outobj.equals(DefineReport.Select.YY_KIJUN_TO.getObj())) {
              // プロモ計画情報
              sqlcommand = DefineReport.ID_SQL_INIT_DS_M_YM_PRM;
            }

          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_YY_KIJUN;
          }
        }
        // 基準月
        if (outobj.equals(DefineReport.Select.MM_KIJUN.getObj())) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_NEW_REPERT)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T21_12;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_RFM)) {
            sqlcommand = DefineReport.ID_SQL_INIT_MM_KIJUN_T01;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_DM_SEGMENT)) {
            sqlcommand = DefineReport.ID_SQL_INIT_MM_KIJUN_T03;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_NENREI)) {
            sqlcommand = DefineReport.ID_SQL_INIT_MM_KIJUN_T252;

          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_LOG_LIST)) {
            sqlcommand = DefineReport.ID_SQL_INIT_MM_KIJUN_L01;

          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_MM_KIJUN;
          }
        }
        if (outobj.equals(DefineReport.Select.MM_KIJUN_TO.getObj())) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_JUNLE_NEW_REPERT)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T21_12;
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_MM_KIJUN;
          }
        }
        // 基準日
        if (outobj.equals(DefineReport.Select.DD_KIJUN.getObj())) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_LOG_LIST)) {
            sqlcommand = DefineReport.ID_SQL_INIT_DD_KIJUN_L01;

          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_DD_KIJUN;
          }
        }
        if (outobj.equals(DefineReport.Select.DD_KIJUN_TO.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_DD_KIJUN;
        }
        if (outobj.equals(DefineReport.Select.YMD_KIJUN.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_YMD_KIJUN_T13;
        }
        if (outobj.equals(DefineReport.Select.YMD_KIJUN2.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_T13;
        }
        // デフォルト値取得
        if (outobj.equals(DefineReport.Select.YM_KIJUN.getObj()) && (outtarget.equals(DefineReport.ID_PARAM_TARGET_DEFAULT_VALUE))) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_03)) {
            sqlcommand = DefineReport.ID_SQL_INIT_DEFAULT_YM_KIJUN_B03;
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_DEFAULT_YM_KIJUN;
          }
        }
        if (outobj.equals(DefineReport.Select.DD_KIJUN.getObj()) && (outtarget.equals(DefineReport.ID_PARAM_TARGET_DEFAULT_VALUE))) {
          sqlcommand = DefineReport.ID_SQL_INIT_DEFAULT_DD_KIJUN;
        }

        // 新製品年月
        if (outobj.equals(DefineReport.Select.YM_NR.getObj())) {
          if (outpage.equals(DefineReport.ID_PAGE_SHOHIN_LIST)) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_SHIN2;
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_SHIN;
          }
        }
        if (outobj.equals(DefineReport.Select.YM_NR_TO.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_YM_SHIN;
        }
        // 店舗
        if (outobj.equals(DefineReport.Select.TENPO.getObj())) {
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_B_03) || StringUtils.equals(outpage, DefineReport.ID_PAGE_B_01) || StringUtils.equals(outpage, DefineReport.ID_PAGE_RFM)) {
            // 全店舗あり、既存店なし、店コード='000'（スマホアプリ）除外
            sqlcommand = DefineReport.ID_SQL_SELECT_ALL_SHOP + DefineReport.ID_SQL_INIT_TENPO_NO000;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D00) || StringUtils.equals(outpage, DefineReport.ID_PAGE_D30) // 店舗売上実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D21) // 新規獲得CPO
          ) {
            // 全店舗なし、既存店なし、店コード='000'（スマホアプリ）除外
            sqlcommand = DefineReport.ID_SQL_INIT_TENPO_NO000;
          } else {
            // 全店舗あり、既存店あり、店コード制限なし
            sqlcommand = DefineReport.ID_SQL_SELECT_ALL_SHOP + DefineReport.ID_SQL_SELECT_EXIST_SHOP + DefineReport.ID_SQL_INIT_TENPO;
          }
        }
        // 店舗売上げの店舗選択
        if (outobj.equals(DefineReport.Select.TENPOG.getObj())) {
          if (outpage.equals(DefineReport.ID_PAGE_B_02b)) {
            sqlcommand = DefineReport.ID_SQL_SELECT_ALL_SHOP + DefineReport.ID_SQL_INIT_TENPO;
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_TENPO;
          }
        }
        // 従販
        if (outobj.equals(DefineReport.Select.JUHAN.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_JUHAN;
        }
        // 関連会社
        if (outobj.equals(DefineReport.Select.KANREN.getObj())) {
          if ((outpage.equals(DefineReport.ID_PAGE_B_01b))) {
            sqlcommand = DefineReport.ID_SQL_INIT_KAIGAIb;
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_KAIGAI;
          }
        }
        // 輸出先
        if (outobj.equals(DefineReport.Select.YUSHU.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_YUSHU;
        }
        // 他社売上
        if (outobj.equals(DefineReport.Select.ITAKU.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_ITAKU;
        }
        // 他ブランド
        if (outobj.equals(DefineReport.Select.BRAND.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_BRAND;
        }
        // 初回受注月 初回属性分析
        if (outobj.equals(DefineReport.Select.F_JUCHU.getObj()) || outobj.equals(DefineReport.Select.F_JUCHU_TO.getObj())) {
          if ((outpage.equals(DefineReport.ID_PAGE_A_03))) {
            if (outsel.equals("1")) {
              sqlcommand = DefineReport.ID_SQL_INIT_JUCYM;
            } else {
              sqlcommand = DefineReport.ID_SQL_INIT_JUCYM2;
            }
          } else {
            if (outsel.equals("1")) {
              sqlcommand = DefineReport.ID_SQL_INIT_JUCYY;
            } else {
              sqlcommand = DefineReport.ID_SQL_INIT_JUCYM2;
            }
          }
        }
        // 登録年月
        if (outobj.equals(DefineReport.Select.TOROKU.getObj()) || outobj.equals(DefineReport.Select.TOROKU_TO.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_TOROKUYM;
        }
        // 初回受注月転換率
        if (outobj.equals(DefineReport.Select.JUCHU.getObj()) || outobj.equals(DefineReport.Select.JUCHU_TO.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_JUCYM_24;
        }
        // 回数
        if (outobj.equals(DefineReport.Select.KAISU.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_KAISU;
        }
        // 受注形態
        if (outobj.equals(DefineReport.Select.JUC_KBN.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_JUCHU;
        }
        // 年齢
        if (outobj.equals(DefineReport.Select.AGE.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_AGE;
        }
        // 地域
        if (outobj.equals(DefineReport.Select.AREA.getObj())) {/// 9999
          if (StringUtils.equals(outpage, DefineReport.ID_PAGE_RFM)) {
            // エリアのみ、全国[00]なし
            sqlcommand = DefineReport.ID_SQL_INIT_AREA_ONLY;
          } else if (StringUtils.equals(outpage, DefineReport.ID_PAGE_D15)// 都道府県別実績
              || StringUtils.equals(outpage, DefineReport.ID_PAGE_D23)// アクティブ顧客反応率
          ) {
            // 国内のみ、全国[00]なし、海外[48]なし
            sqlcommand = DefineReport.ID_SQL_INIT_AREA_JAPAN;
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_AREA;
          }
        }
        // 媒体
        if (outobj.equals(DefineReport.Select.BYTAI.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_BYTAI;
        }
        // 媒体2
        if (outobj.equals(DefineReport.Select.BYTAI_2.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_BYTAI2;
          paramData.add(0, outsel);
        }
        // 初回商品
        if (outobj.equals(DefineReport.Select.F_SHOHIN.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_SHOG;
        }
        // 初回商品g2
        if (outobj.equals(DefineReport.Select.F_SHOHIN2.getObj())) {
          sqlcommand = DefineReport.ID_SQL_SEL_SHOG;
        }
        // プロモーション 画面で選択
        if (outobj.equals(DefineReport.Select.PROMO_FROM.getObj()) || outobj.equals(DefineReport.Select.PROMO_TO.getObj()) || outobj.equals(DefineReport.Select.PROMO.getObj())) {
          // プロモーション_１５次媒体
          if (outpage.equals(DefineReport.ID_PAGE_PROMO_PLUS)) {
            sqlcommand = DefineReport.ID_SQL_INIT_PROMO13;
            // プロモーション_単価分析
          } else if (outpage.equals(DefineReport.ID_PAGE_KOKYAKU_TENPO) || StringUtils.equals(outpage, DefineReport.ID_PAGE_D29)// 通店単価分布
          ) {
            sqlcommand = DefineReport.ID_SQL_INIT_PROMO16;
            // その他DM実績
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_PROMO;
          }
        }
        // 条件番号
        if (outobj.equals(DefineReport.Select.SEGNO_FROM.getObj()) || outobj.equals(DefineReport.Select.SEGNO_TO.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_SEG;
        }
        if (outobj.equals(DefineReport.Select.SEGNO.getObj())) {
          if (outpage.equals(DefineReport.ID_PAGE_DM_SHOHIN_TSU2)) {
            sqlcommand = DefineReport.ID_SQL_INIT_SEG_T03;
            paramData.add(0, ObjectUtils.toString(maps.get("PROMO_CD_FROM")));
            paramData.add(1, ObjectUtils.toString(maps.get("PROMO_CD_TO")));
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_SEG;
          }
        }
        if (outobj.equals(DefineReport.Select.SEGNOT.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_SEG2;
        }
        // チャネル選択
        if (outobj.equals(DefineReport.Select.CHA_1.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_CHANELA;
        }
        if (outobj.equals(DefineReport.Select.CHA_2.getObj())) {
          if (outsel.equals("20")) {
            if (outtarget.equals("1")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELB;
            } else if (outtarget.equals("2")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELB2;
            } else if (outtarget.equals("3")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELB3;
            } else if (outtarget.equals("4")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELB4;
            } else if (outtarget.equals("5")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELB5;
            }
          } else if (outsel.equals("50")) {
            // 他社売上の場合
            sqlcommand = DefineReport.ID_SQL_INIT_CHANELB_50;
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_CHANELB;
          }
          paramData.add(0, outsel);
        }
        if (outobj.equals(DefineReport.Select.CHA_3.getObj())) {
          if (outsel.substring(0, 2).equals("20")) {
            if (outtarget.equals("1")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELC;
            } else if (outtarget.equals("2")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELC2;
            } else if (outtarget.equals("3")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELC3;
            } else if (outtarget.equals("4")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELC4;
            } else {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELC;
            }
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_CHANELB;
          }
          paramData.add(0, outsel);
        }
        if (outobj.equals(DefineReport.Select.CHA_4.getObj())) {
          if (outsel.substring(0, 2).equals("20")) {
            if (outtarget.equals("1")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELD;
            } else if (outtarget.equals("2")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELD2;
            } else if (outtarget.equals("3")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELD;
            } else if (outtarget.equals("4")) {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELD;
            } else {
              sqlcommand = DefineReport.ID_SQL_INIT_CHANELD;
            }
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_CHANELB;
          }
          paramData.add(0, outsel);
        }
        // 商品出荷実績用 下 左
        if (outobj.equals(DefineReport.Select.SHOB_1.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_BUN1;
          paramData.add(0, outsel);
        }
        // 商品出荷実績用 下 右
        if (outobj.equals(DefineReport.Select.SHOBS_1.getObj())) {
          if (outtarget.equals("2")) {
            sqlcommand = DefineReport.ID_SQL_INIT_T_SHO2;
            paramData.add(0, outsel);
          } else if (outtarget.equals("3")) {
            sqlcommand = DefineReport.ID_SQL_INIT_T_SHO3;
            if (outsel.length() == 2) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO3_BUN1;
            }
            paramData.add(0, outsel);
          } else if (outtarget.equals("4")) {
            sqlcommand = DefineReport.ID_SQL_INIT_T_SHO4;
            int len = outsel.length();
            if (len == 2) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO4_BUN1;
            } else if (len == 4) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO4_BUN2;
            }
            paramData.add(0, outsel);
          } else if (outtarget.equals("5")) {
            sqlcommand = DefineReport.ID_SQL_INIT_T_SHO5;
            int len = outsel.length();
            if (len == 2) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO5_BUN1;
            } else if (len == 4) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO5_BUN2;
            } else if (len == 6) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO5_BUN3;
            }
            paramData.add(0, outsel);
          } else if (outtarget.equals("6")) {
            sqlcommand = DefineReport.ID_SQL_INIT_T_SHO6;
            int len = outsel.length();
            if (len == 2) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO6_BUN1;
            } else if (len == 4) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO6_BUN2;
            } else if (len == 6) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO6_BUN3;
            } else if (len == 8) {
              sqlcommand = DefineReport.ID_SQL_INIT_T_SHO6_BUN4;
            }
            paramData.add(0, outsel);
          } else {
            sqlcommand = DefineReport.ID_SQL_INIT_T_SHO7;
            paramData.add(0, outsel);
          }
        }
        // 商品出荷実績用テスト上
        if (outobj.equals(DefineReport.Select.SHOS_1.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_T_SHO1;
        }
        if (outobj.equals(DefineReport.Select.SHOS_2.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_T_SHO2;
          paramData.add(0, outsel);
        }
        if (outobj.equals(DefineReport.Select.SHOS_3.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_T_SHO3;
          paramData.add(0, outsel);
        }
        if (outobj.equals(DefineReport.Select.SHOS_4.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_T_SHO4;
          paramData.add(0, outsel);
        }
        if (outobj.equals(DefineReport.Select.SHOS_5.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_T_SHO5;
          paramData.add(0, outsel);
        }
        if (outobj.equals(DefineReport.Select.SHOS_6.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_T_SHO6;
          paramData.add(0, outsel);
        }
        // DM商品別実績関連の時のみ
        if ((outpage.equals(DefineReport.ID_PAGE_DM_SHOHIN_TSU)) || (outpage.equals(DefineReport.ID_PAGE_DM_SHOHIN_TSU2)) || (outpage.equals(DefineReport.ID_PAGE_DM_SHOHIN_MON))) {
          if (outobj.equals(DefineReport.Select.SHO_1.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_SHO1;
          }
          if (outobj.equals(DefineReport.Select.SHO_2.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_SHO2;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_3.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_SHO3;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_4.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_SHO4;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_5.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_SHO5;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_6.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_SHO6;
            paramData.add(0, outsel);
          }
        }
        // 商品分類顧客数関連の時のみ
        if ((outpage.equals(DefineReport.ID_PAGE_JUNLE_JUCHU)) || (outpage.equals(DefineReport.ID_PAGE_JUNLE_NENREI))) {
          if (outobj.equals(DefineReport.Select.SHO_1.getObj())) {
            if (outpage.equals(DefineReport.ID_PAGE_JUNLE_NENREI)) {
              sqlcommand = DefineReport.ID_SQL_SELECT_ALL + DefineReport.ID_SQL_INIT25_SHO1;
            } else {
              sqlcommand = DefineReport.ID_SQL_INIT25_SHO1;
            }
          }
          if (outobj.equals(DefineReport.Select.SHO_2.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT25_SHO2;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_3.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT25_SHO3;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_4.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT25_SHO4;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_5.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT25_SHO5;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_6.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT25_SHO6;
            paramData.add(0, outsel);
          }
          if (outobj.equals(DefineReport.Select.SHO_7.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT25_SHO;
            paramData.add(0, outsel);
          }
        }
        // 韓国顧客RFM
        if (outpage.equals(DefineReport.ID_PAGE_K_RFM)) {
          // 基準年月
          if (outobj.equals(DefineReport.Select.YM_KIJUN_KOR.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_YM_KIJUN_KOR;
          }
          // 販売チャネル
          if (outobj.equals(DefineReport.Select.CHANEL_KOR.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_CHANEL_KOR;
          }
          // 会員区分
          if (outobj.equals(DefineReport.Select.KAIIN_KBN_KOR.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_KAIIN_KOR;
          }
          // 年齢選択
          if (outobj.equals(DefineReport.Select.AGE_KOR.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_AGE_KOR;
          }
          // 地域選択
          if (outobj.equals(DefineReport.Select.AREA_KOR.getObj())) {
            sqlcommand = DefineReport.ID_SQL_INIT_AREA_KOR;
          }
        }

        // プロモーション一覧
        if (outobj.equals(DefineReport.Button.SEARCH_PROMO.getObj())) {
          sqlcommand = DefineReport.ID_SQL_INIT_PROMO_LIST;
          paramData.add(0, outsel + "%");
        }

      } else if (outaction.equals(DefineReport.ID_PARAM_ACTION_DEFAULT) && (!outpage.equals(DefineReport.ID_PAGE_GYOSEKI_REPORT))) {
        // ユーザ情報の取得
        User usr = (User) session.getAttribute(Consts.STR_SES_LOGINUSER);
        String loginusr = (String) session.getAttribute(Defines.ID_REQUEST_USER_ID);
        String loginusrName = "";
        if (usr != null) {
          loginusrName = usr.getName();
        }

        // ログイン
        if (outobj.equals(DefineReport.ID_ACTION_LOGIN)) {

          // パラメータ確認
          JSONObject obj = (JSONObject) map.get(0);

          // 更新コマンドの準備
          sqlcommand = DefineReport.ID_SQL_INSERT_SYSLOGS;
          paramData.add(0, (String) obj.get("CD_USER"));
          paramData.add(1, DefineReport.ID_ACTION_LOGIN);
          paramData.add(2, "");
          paramData.add(3, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("user")), "0"));
          paramData.add(4, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("report")), "0"));
          paramData.add(5, loginusrName);

          // レポート参照ログの書き込み
          ItemList il = new ItemList();
          il.executeItem(sqlcommand, paramData, Defines.STR_JNDI_DS);

          // 後述の処理を実行しない
          sqlcommand = "";
        }

        // 照会ボタン押下
        if (outobj.equals(DefineReport.Button.SEARCH.getObj())) {

          // セッション設定
          session.setAttribute(DefineReport.ID_SESSION_STORAGE, map.toString());

          // 更新コマンドの準備
          sqlcommand = DefineReport.ID_SQL_INSERT_SYSLOGS;
          paramData.add(0, loginusr);
          paramData.add(1, DefineReport.ID_ACTION_QUERY);
          // paramData.add(2, map.toString());
          paramData.add(2, "");
          paramData.add(3, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("user")), "0"));
          paramData.add(4, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("report")), "0"));
          paramData.add(5, loginusrName);

          // レポート参照ログの書き込み
          ItemList il = new ItemList();
          System.out.println(sqlcommand);
          System.out.println(paramData.toString());
          il.executeItem(sqlcommand, paramData, Defines.STR_JNDI_DS);

          // パラメータ取得
          String userid = maps.get("userid") == null ? "0" : maps.get("userid");
          String user = maps.get("user") == null ? "0" : maps.get("user");
          String report = maps.get("report") == null ? "0" : maps.get("report");

          // 検索条件の削除
          sqlcommand = "DELETE FROM AK_SYS_SNAPSHOT WHERE CD_USER=CAST(? AS INTEGER) AND CD_REPORT=CAST(? AS INTEGER)";
          paramData.clear();
          paramData.add(0, user);
          paramData.add(1, report);
          il.executeItem(sqlcommand, paramData, Defines.STR_JNDI_DS);

          // 検索条件の登録
          sqlcommand = "INSERT INTO AK_SYS_SNAPSHOT(CD_USER,CD_REPORT,SNAPSHOT,NM_CREATE,NM_UPDATE) VALUES(?,?,?,?,?)";
          paramData.clear();
          paramData.add(0, user);
          paramData.add(1, report);
          paramData.add(2, map.toString());
          paramData.add(3, userid);
          paramData.add(4, userid);
          il.executeItem(sqlcommand, paramData, Defines.STR_JNDI_DS);

          // 後述の処理を実行しない
          sqlcommand = "";
        }
        // 出荷売上げボタン押下
        if (outobj.equals(DefineReport.Button.SEARCH2.getObj())) {

          // セッション設定
          session.setAttribute(DefineReport.ID_SESSION_STORAGE, map.toString());

          // 更新コマンドの準備
          sqlcommand = DefineReport.ID_SQL_INSERT_SYSLOGS;
          paramData.add(0, loginusr);
          paramData.add(1, DefineReport.ID_ACTION_QUERY);
          paramData.add(2, map.toString());
          paramData.add(3, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("user")), "0"));
          paramData.add(4, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("report")), "0"));
          paramData.add(5, loginusrName);

          // レポート参照ログの書き込み
          ItemList il = new ItemList();
          il.executeItem(sqlcommand, paramData, Defines.STR_JNDI_DS);

          // 後述の処理を実行しない
          sqlcommand = "";
        }

        // Excelボタン押下
        if (outobj.equals(DefineReport.Button.EXCEL.getObj())) {

          session.getAttribute(DefineReport.ID_SESSION_STORAGE);

          // 更新コマンドの準備
          sqlcommand = DefineReport.ID_SQL_INSERT_SYSLOGS;
          paramData.add(0, loginusr);
          paramData.add(1, DefineReport.ID_ACTION_EXCEL);
          // paramData.add(2, storage.toString());
          paramData.add(2, "");
          paramData.add(3, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("user")), "0"));
          paramData.add(4, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("report")), "0"));
          paramData.add(5, loginusrName);

          // レポート参照ログの書き込み
          ItemList il = new ItemList();
          il.executeItem(sqlcommand, paramData, Defines.STR_JNDI_DS);

          // 後述の処理を実行しない
          sqlcommand = "";
        }

        // アップロード・ダウンロード押下
        if (outobj.equals(Form.BTN_UP_LOAD) || outobj.equals(Form.BTN_DOW_LOAD)) {

          // セッション設定
          session.setAttribute(DefineReport.ID_SESSION_STORAGE, map.toString());

          String query = DefineReport.ID_ACTION_UPLOAD;
          if (outobj.equals(Form.BTN_DOW_LOAD)) {
            query = DefineReport.ID_ACTION_DOWNOAD;
          }

          // 更新コマンドの準備
          sqlcommand = DefineReport.ID_SQL_INSERT_SYSLOGS;
          paramData.add(0, loginusr);
          paramData.add(1, query);
          paramData.add(2, map.toString());
          paramData.add(3, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("user")), "0"));
          paramData.add(4, StringUtils.defaultIfEmpty(ObjectUtils.toString(maps.get("report")), "0"));
          paramData.add(5, loginusrName);

          // レポート参照ログの書き込み
          ItemList il = new ItemList();
          il.executeItem(sqlcommand, paramData, Defines.STR_JNDI_DS);

          // 後述の処理を実行しない
          sqlcommand = "";
        }
      } else {
        System.out.println(DefineReport.ID_PARAM_ACTION + "：未指定");
      }

      // SQL構文の実行（コマンド指定あり）
      if (!"".equals(sqlcommand)) {
        if (DefineReport.ID_DEBUG_MODE) {
          System.out.println(sqlcommand);
        }

        try {
          if (StringUtils.isEmpty(outobjtyp)) {
            json = selectJSON(sqlcommand, paramData, jndiName);
          } else {
            json = selectJSONArray(sqlcommand, paramData, jndiName);
          }
          if (DefineReport.ID_DEBUG_MODE) {
            System.out.println("json:" + json);
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      if (DefineReport.ID_DEBUG_MODE) {
        System.out.println("EasyToJSONServletの終了(" + outobj.toString() + ")");
      }

      // JSON データのロード
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter pw = response.getWriter();
      pw.print(json.toString());
      pw.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * 検索＆JSON生成
   *
   * @param sqlCommand 実行SQL
   * @param paramData SQLパラメータ
   * @param jndiName 接続JNDI名
   * @return JSON文字列
   * @throws Exception 例外
   */
  private String selectJSON(String sqlCommand, ArrayList<String> paramData, String jndiName) throws Exception {

    // System.out.println("selectQueryJSON:" + sqlCommand);
    // System.out.println("paramData.size: " + Integer.toString(paramData.size()));
    String jsonString = "";

    // 検索
    Connection con = null; // コネクション
    try {
      // コネクションの取得
      con = DBConnection.getConnection(jndiName);

      // 実行SQL設定
      PreparedStatement stmt = con.prepareStatement(sqlCommand);

      // 分類コードパラメータ判断
      for (int i = 0; i < paramData.size(); i++) {
        stmt.setString((i + 1), paramData.get(i));
      }
      // SQL実行
      ResultSet rs = stmt.executeQuery();

      int total = 0;

      // カラム数
      ResultSetMetaData rsmd = rs.getMetaData();
      int sizeColumn = rsmd.getColumnCount();
      // System.out.println(sizeColumn);

      JSONArray json = new JSONArray();

      // レコード情報の格納先(JSONObject)作成
      JSONObject jsonOB = new JSONObject();

      // 項目単位の情報格納
      List<JSONObject> lineData = new ArrayList<>();

      // 結果の取得
      while (rs.next()) {
        total++;

        JSONObject obj = new JSONObject();

        for (int i = 1; i <= sizeColumn; i++) {
          obj.put(rsmd.getColumnName(i), rs.getString(i));
        }

        // 行データ格納
        json.add(obj);

        // 行情報へセル情報を追加
        lineData.add(obj);

      }
      // jqEasy 用 JSON モデル作成
      JQEasyModel jsonModel = new JQEasyModel();
      jsonModel.setRows(lineData);
      jsonModel.setTotal(total);
      jsonOB = JSONObject.fromObject(JSONSerializer.toJSON(jsonModel));

      jsonString = jsonOB.toString();

      con.commit();
      con.close();

    } catch (Exception e) {
      /* 接続解除 */
      if (con != null) {
        try {
          con.rollback();
          con.close();
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
      e.printStackTrace();
    }

    return jsonString;
  }

  /**
   * 検索＆JSON生成
   *
   * @param sqlCommand 実行SQL
   * @param paramData SQLパラメータ
   * @param jndiName 接続JNDI名
   * @return JSON文字列
   * @throws Exception 例外
   */
  private String selectJSONArray(String sqlCommand, ArrayList<String> paramData, String jndiName) throws Exception {

    // System.out.println("selectQueryJSON:" + sqlCommand);
    // System.out.println("paramData.size: " + Integer.toString(paramData.size()));
    String jsonString = "";

    // 検索
    Connection con = null; // コネクション
    try {
      // コネクションの取得
      con = DBConnection.getConnection(jndiName);

      // 実行SQL設定
      PreparedStatement stmt = con.prepareStatement(sqlCommand);

      // 分類コードパラメータ判断
      for (int i = 0; i < paramData.size(); i++) {
        stmt.setString((i + 1), paramData.get(i));
      }
      // SQL実行
      ResultSet rs = stmt.executeQuery();

      // カラム数
      ResultSetMetaData rsmd = rs.getMetaData();
      int sizeColumn = rsmd.getColumnCount();

      JSONArray json = new JSONArray();

      // 結果の取得
      while (rs.next()) {
        JSONObject obj = new JSONObject();

        for (int i = 1; i <= sizeColumn; i++) {
          obj.put(rsmd.getColumnName(i), rs.getString(i));
        }

        // 行データ格納
        json.add(obj);
      }

      jsonString = json.toString();

      con.commit();
      con.close();

    } catch (Exception e) {
      /* 接続解除 */
      if (con != null) {
        try {
          con.rollback();
          con.close();
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
      e.printStackTrace();
    }

    return jsonString;
  }

}
