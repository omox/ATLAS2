package common;


/**
 * レポート用定数クラス <br />
 * レポート画面で使用する定数をまとめて定義。 <br />
 * <br />
 * 主な内容 <br />
 * 1.パラメータ・セッション名<br />
 * 2.ページ番号<br />
 * 3.HTML 関連 オブジェクト名・表示内容・固定値（オブジェクト名はjquery.report.control.jsのIDと一致）<br />
 * 4.HTML 関連 SQL（コンボボックス用SQLなど）<br />
 */
public class DefineReport {

  /**
   * 開発関連
   */
  /* デバッグ(true:開発時、false:リリース時 */
  public static final boolean ID_DEBUG_MODE = false;

  /**
   * PARAMETER 関連
   */
  /** パラメータ：ページ番号 */
  public static final String ID_PARAM_PAGE = "page";
  /** パラメータ：オブジェクト番号 */
  public static final String ID_PARAM_OBJ = "obj";
  /** パラメータ：選択値 */
  public static final String ID_PARAM_VAL = "sel";
  /** パラメータ：JSON値 */
  public static final String ID_PARAM_JSON = "json";
  /** パラメータ：アクション */
  public static final String ID_PARAM_ACTION = "action";
  /** パラメータ：ターゲット */
  public static final String ID_PARAM_TARGET = "target";
  /** パラメータ：取得データ型 */
  public static final String ID_PARAM_OBJ_TYPE = "rowsOnly";

  /** パラメータ：最大取得件数 */
  public static final String ID_SEARCHJSON_PARAM_MAXROW = "maxRows";

  /** パラメータ：検索キー */
  public static final String ID_SEARCHJSON_PARAM_NAMEWITH = "q";

  /** パラメータ：アクション 初期値 */
  public static final String ID_PARAM_ACTION_DEFAULT = "run"; // 検索実行
  public static final String ID_PARAM_ACTION_GET = "get"; // 通常通信
  public static final String ID_PARAM_ACTION_INIT = "init"; // 初期化
  public static final String ID_PARAM_ACTION_DEFAULTS = "defaults"; // 初期値
  public static final String ID_PARAM_ACTION_ITEMS = "items"; // 商品入力

  /** パラメータ：ターゲット オプション */
  public static final String ID_PARAM_TARGET_DEFAULT_VALUE = "default"; // デフォルト値取得

  /** JSON：メンバー：実値 */
  public static final String ID_JSON_VALUE = "value";
  /** JSON：メンバー：表示値 */
  public static final String ID_JSON_TEXT = "text";

  /**
   * ログ情報のアクション
   */
  public static final String ID_ACTION_LOGIN = "login";
  public static final String ID_ACTION_QUERY = "query";
  public static final String ID_ACTION_EXCEL = "excel";
  public static final String ID_ACTION_UPLOAD = "upload";
  public static final String ID_ACTION_DOWNOAD = "download";

  /**
   * SESSION 関連
   */
  /** 「照会」押下時の分析条件(JSON)を格納するセッション名 */
  public static final String ID_SESSION_STORAGE = "STORAGE";
  public static final String ID_SESSION_TABLE = "table";
  public static final String ID_SESSION_WHERE = "where";
  public static final String ID_SESSION_META = "meta";
  public static final String ID_SESSION_HEADER = "header";
  public static final String ID_SESSION_SETTING = "grid_setting";

  /**
   * ページ番号
   */
  // サンプル
  public static final String ID_PAGE_D00 = "ReportD00"; // サンプル
  public static final String ID_PAGE_D11 = "ReportD11"; // 通店プロモ商品速報
  public static final String ID_PAGE_D12 = "ReportD12"; // 通店商品原価率
  public static final String ID_PAGE_D13 = "ReportD13"; // 商品分類別実績
  public static final String ID_PAGE_D14 = "ReportD14"; // 新規・リピート別実績
  public static final String ID_PAGE_D15 = "ReportD15"; // 都道府県別実績
  public static final String ID_PAGE_D16 = "ReportD16"; // 年齢別実績
  public static final String ID_PAGE_D17 = "ReportD17"; // 初回購入年別実績
  public static final String ID_PAGE_D18 = "ReportD18"; // 年間購入額別実績
  public static final String ID_PAGE_D19 = "ReportD19"; // ステージ別顧客数
  public static final String ID_PAGE_D20 = "ReportD20"; // 初回購入商品
  public static final String ID_PAGE_D21 = "ReportD21"; // 新規獲得CPO
  public static final String ID_PAGE_D22 = "ReportD22"; // 新規F2転換率
  public static final String ID_PAGE_D23 = "ReportD23"; // アクティブ顧客反応率
  public static final String ID_PAGE_D24 = "ReportD24"; // 3ステップ購入率
  public static final String ID_PAGE_D25 = "ReportD25"; // 年間チャネル併用顧客数
  public static final String ID_PAGE_D26 = "ReportD26"; // チャネル新規獲得状況
  public static final String ID_PAGE_D27 = "ReportD27"; // 国内ポイント実績
  public static final String ID_PAGE_D28 = "ReportD28"; // アプリ会員登録・起動状況
  public static final String ID_PAGE_D29 = "ReportD29"; // 通店単価分布
  public static final String ID_PAGE_D30 = "ReportD30"; // 店舗売上実績
  public static final String ID_PAGE_D31 = "ReportD31"; // 個店実績一覧


  /* 全社売上_詳細 */
  public static final String ID_PAGE_B_01 = "ReportB_01";
  /* 全社売上 */
  public static final String ID_PAGE_B_01a = "ReportB_01a";
  /* 全社売上_外貨表示 */
  public static final String ID_PAGE_B_01b = "ReportB_01b";
  /* 店舗売上 */
  public static final String ID_PAGE_B_02 = "ReportB_02";
  public static final String ID_PAGE_B_021 = "ReportB_021";
  /* 店舗分析 */
  public static final String ID_PAGE_B_02a = "ReportB_02a";
  /* 店舗月別 */
  public static final String ID_PAGE_B_02b = "ReportB_02b";
  /* 店舗個店別売上 */
  public static final String ID_PAGE_B_02c = "ReportB_02c";
  /* 通販受注実績 */
  public static final String ID_PAGE_B_03 = "ReportB_03";
  public static final String ID_PAGE_B_031 = "ReportB_031";
  /* 商品出荷実績 */
  public static final String ID_PAGE_B_04 = "ReportB_04";
  /* 通販分析初回属性 */
  public static final String ID_PAGE_A_01 = "ReportA_01";
  /* 通販分析F2転換率 */
  public static final String ID_PAGE_A_02 = "ReportA_02";
  /* 通販分析媒体 */
  public static final String ID_PAGE_A_03 = "ReportA_03";
  /* 通販分析初回商品 */
  public static final String ID_PAGE_A_04 = "ReportA_04";
  /* 顧客RFM分析 */
  public static final String ID_PAGE_RFM = "ReportT_01";
  /* F0顧客分析 */
  public static final String ID_PAGE_F0 = "ReportT_02";
  /* DMセグメント別実績 */
  public static final String ID_PAGE_DM_SEGMENT = "ReportT_03";
  /* DM発送実績RFM */
  public static final String ID_PAGE_DM_HASOU_RFM = "ReportT_04";
  /* DM都道府県別実績 */
  public static final String ID_PAGE_DM_SECTOR = "ReportT_05";
  /* DM商品実績・通販/店舗 */
  public static final String ID_PAGE_DM_SHOHIN_TSU = "ReportT_07a";
  /* DM商品実績・番号別 */
  public static final String ID_PAGE_DM_SHOHIN_TSU2 = "ReportT_07b";
  /* DM商品実績・月別 */
  public static final String ID_PAGE_DM_SHOHIN_MON = "ReportT_07m";
  /* DM受注推移一覧 */
  public static final String ID_PAGE_DM_LIST = "ReportT_08";

  /* スキンチェック評価 */
  public static final String ID_PAGE_SKIN_CHECK = "ReportT_10";
  /* 月別全プロモーション評価 */
  public static final String ID_PAGE_PROMO_MONTHLY = "ReportT_11";
  /* 新規プロモーション評価 */
  public static final String ID_PAGE_PROMO_NEW = "ReportT_12";
  /* 「1.5次媒体」評価 */
  public static final String ID_PAGE_PROMO_PLUS = "ReportT_13";
  /* 登録店舗×最終購入店 */
  public static final String ID_PAGE_STORE_START_END = "ReportT_14";
  /* 顧客単価分布（通販） */
  public static final String ID_PAGE_KOKYAKU_NET = "ReportT_15";
  /* 店舗単価分布 */
  public static final String ID_PAGE_KOKYAKU_TENPO = "ReportT_16";

  // 顧客動向表
  public static final String ID_PAGE_CHANEL_TREND = "ReportT_19";
  // 顧客減衰表
  public static final String ID_PAGE_GENSUI = "ReportT_20";
  // ジャンル別新規・リピート
  public static final String ID_PAGE_JUNLE_NEW_REPERT = "ReportT_21";
  // アクティブ顧客数推移
  public static final String ID_PAGE_TREND_ALL = "ReportT_22";
  // 通販DMアクティブ顧客数推移
  public static final String ID_PAGE_TREND_ACTIVE = "ReportT_23";
  // 転換顧客数・率
  public static final String ID_PAGE_TREND_CONVERT = "ReportT_24";
  // 商品別受注顧客数推移
  public static final String ID_PAGE_JUNLE_JUCHU = "ReportT_251";
  // 商品別年齢別受注顧客数推移
  public static final String ID_PAGE_JUNLE_NENREI = "ReportT_252";
  // 商品別期間限定顧客数推移
  public static final String ID_PAGE_JUNLE_4Q = "ReportT_253";
  // 都道府県別シェア
  public static final String ID_PAGE_AREA_SHARE1 = "ReportT_261";
  // 都道府県別シェア年齢別
  public static final String ID_PAGE_AREA_SHARE2 = "ReportT_262";
  // ジャンル併売顧客数
  public static final String ID_PAGE_HEIBAI = "ReportT_27";

  /* 韓国顧客RFM分析 */
  public static final String ID_PAGE_K_RFM = "ReportK_01";

  /* 月別プロモーション一覧 */
  public static final String ID_PAGE_PROMO_LIST = "Report_PromoList";
  /* アクセスログ表示 */
  public static final String ID_PAGE_LOG_LIST = "ReportL_01";
  /* 初回商品マスタ表示 */
  public static final String ID_PAGE_SHOHIN_LIST = "ReportL_02";
  /* 業績レポート */
  public static final String ID_PAGE_GYOSEKI_REPORT = "Gyoseki_Report";
  /* アップロード */
  public static final String ID_PAGE_UPLOAD = "Report_Upload";


  /**
   * HTML 関連
   */
  /** HTML関連 DIV */
  public enum Div {

    INPUT_201310("div_input201310", "入力経路"), INPUT_201311("div_input201311", "入力経路"),

    KAIIN("div_kaiin", "会員区分"), STAGE_KAIIN("div_stage_kaiin", "ステージ／会員区分"), STAGE("div_stage", "ステージ");

    private final String obj;
    private final String view;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     */
    Div(String obj, String view) {
      this.obj = obj;
      this.view = view;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }
  }

  /** HTML関連 選択リスト */
  public enum Select {
    YM_KIJUN("select_ym", "年月"), YY_KIJUN("select_yy", "年"), MM_KIJUN("select_mm", "月"), DD_KIJUN("select_dd", "日"), YMD_KIJUN("select_ymd", "年月日"), YMD_KIJUN2("select_ymd2", "年月日"), YM_KIJUN_TO("select_ym2", "年月"), YY_KIJUN_TO("select_yy2", "年"), MM_KIJUN_TO("select_mm2", "月"), DD_KIJUN_TO("select_dd2", "日"), YM_KIJUN_KOR("select_ym_kor", "年月"), CHANEL("select_chanel", "購入チャネル"), CHANEL_KOR("select_chanel_kor", "販売チャネル"), S_TYPE("select_type1", "表示形式"), S_TYPE2("select_type1_2", "表示形式"), H_TYPE("select_htype1", "表示項目選択"), H_TYPE2("select_htype2", "表示項目選択"), H_TYPE3("select_htype3", "表示項目選択"), T_TYPE("select_type2", "展開表示"), T_TYPE2("select_type2_2", "展開表示"), NR_TYPE("select_nr_type",
        "新/リピート"), NR_TYPE_S("select_nr_typeS", "商品新リピート"), PROMO("select_promo", "プロモーション"), PROMO_SEL("select_promo_sel", "プロモーション"), PROMO_FROM("select_promo_from", "プロモーション"), PROMO_TO("select_promo_to", "プロモーション"), SEGNOT("select_segno_t", "条件番号"), SEGNO("select_segno", "条件番号"), SEGNO_FROM("select_segno_from", "条件番号"), SEGNO_TO("select_segno_to", "条件番号"), TENPO("select_tenpo", "店舗選択"), TENPOG("select_tenpo_g", "店舗選択"), JUHAN("select_juhan", "従販"), YUSHU("select_yushu", "輸出先"), ITAKU("select_itaku", "他社売上"), BRAND("select_brand", "他ブランド"), KANREN("select_kaigai", "関連会社"), F_JUCHU("select_juchu_from", "初回受注月"), F_JUCHU_TO("select_juchu_to", "初回受注月"), JUCHU("select_juchu_ym1",
            "初回受注月"), JUCHU_TO("select_juchu_ym2", "初回受注月"), TOROKU("select_toroku_from",
                "登録年月"), TOROKU_TO("select_toroku_to", "登録年月"), YMD_KEISAI("select_keisaibi", "掲載日"), KAISU("select_kaisu", "回数"), KAISU2("select_kaisu2", "回数"), JUC_KBN("select_juc_kbn", "受注形態"), AGE("select_age", "年齢"), AGE_KOR("select_age_kor", "年齢"), AREA("select_area", "都道府県"), AREA_KOR("select_area_kor", "地域"), BYTAI("select_s_bytai", "獲得媒体"), BYTAI_2("select_s_bytai2", "媒体種類"), F_SHOHIN("select_s_shog", "初回商品G"), F_SHOHIN2("select_s_shog2", "初回商品G"), F2_JIKI("select_F2jiki", "F2転換時期"), KAIIN_KBN("select_kaiin", "会員区分"), STAGE_KAIIN_KBN("select_stage_kaiin", "ステージ/会員区分"), STAGE_KBN("select_stage", "ステージ"), KAIIN_KBN_KOR("select_kaiin_kor", "会員区分"), JANRU("select_janru", "購入ジャンル"),
    /** 商品出荷実績新製品 */
    YM_NR("select_ym_nr", "年月１"), YM_NR_TO("select_ym_nr2", "年月２"),
    /** 商品出荷実績chanel分類 */
    CHA_NM("select_cha", "チャネル分類"), CHA_1("select_cha_1", "基本分類"), CHA_2("select_cha_2", "大分類"), CHA_3("select_cha_3", "中分類"), CHA_4("select_cha_4", "小分類"),
    /** 商品出荷実績項目 */
    SHOB_NM("select_bun", "条件"), SHOB_1("select_bun_1", "条件１"), SHOB_2("select_bun_2", "条件２"), SHOB_3("select_bun_3", "条件３"), SHOB_4("select_bun_4", "条件４"), SHOB_5("select_bun_5", "条件５"), SHOB_6("select_bun_6", "条件６"), SHOB_7("select_bun_7", "条件７"), SHOB_8("select_bun_8", "条件８"), SHOB_9("select_bun_9", "条件９"), SHOB_10("select_bun_10", "条件１０"),
    /** 商品出荷実績商品分類 上 */
    SHOS_NM("select_shos", "商品分類"), SHOS_1("select_shos_1", "基本分類"), SHOS_2("select_shos_2", "大分類"), SHOS_3("select_shos_3", "中分類"), SHOS_4("select_shos_4", "小分類"), SHOS_5("select_shos_5", "シリーズＡ"), SHOS_6("select_shos_6", "シリーズＢ"),
    /** 商品出荷実績商品分類 下 */
    SHOBS_NM("select_bs", "分類"), SHOBS_1("select_bs_1", "分類１"), SHOBS_2("select_bs_2", "分類２"), SHOBS_3("select_bs_3", "分類３"), SHOBS_4("select_bs_4", "分類４"), SHOBS_5("select_bs_5", "分類５"), SHOBS_6("select_bs_6", "分類６"), SHOBS_7("select_bs_7", "分類７"), SHOBS_8("select_bs_8", "分類８"), SHOBS_9("select_bs_9", "分類９"), SHOBS_10("select_bs_10", "分類１０"),
    /** 商品分類 DM実績 */
    SHO_1("select_sho1", "基本分類"), SHO_2("select_sho2", "大分類"), SHO_3("select_sho3", "中分類"), SHO_4("select_sho4", "小分類"), SHO_5("select_sho5", "シリーズＡ"), SHO_6("select_sho6", "シリーズＢ"), SHO_7("select_item", "商品");

    private final String obj;
    private final String view;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     */
    Select(String obj, String view) {
      this.obj = obj;
      this.view = view;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }
  }

  /** HTML関連 ラベル */
  public enum Label {
    LABELB1("label_B1", "輸出先"), LABELB2("label_B2", "開始/終了年月指定"), LABELB3("label_T71", "まで表示"), LABELB4("label_B31", "計上時間"), LABELB5("label_B31", "電話受注最終予想件数："), LABELB41("label_B41", "照会条件"), LABELB42("label_B42", "照会データ指定"), LABELB43("label_B43", "数/額指定"), LABELB44("label_B44", "月/12指定"), LABELB45("label_B45", "新製品指定"), LABELB46("label_B46", "期間"), LABELB47("label_B47", "チャネル"), LABELB48("label_B48", "店販大分類"), LABELB49("label_B49", "店販中分類"), LABELB4a("label_B4a", "商品分類指定1"), LABELB4b("label_B4b", "集計レベル"), LABELB4c("label_B4c", "オプション"), LABELB4d("label_B4d", "対比表示"), LABELB4e("label_B4e", "通販・リピート別集計"), LABELB4f("label_B4f", "集計項目選択"), LABELB4g("label_B4g", "注意"), LABELB4h("label_B4h",
        "商品"), LABELB4i("label_B4i", "表示対象"), LABELB4j("label_B4j", "受注経路"), LABELB4k("label_B4k", "予算/計画"), LABELB4l("label_B4l", "～"), LABELB4m("label_B4m", "分類指定１"), LABELB4n("label_B4n", "商品分類指定2"), LABELB4o("label_B4o", "商品分類指定"), LABELB4p("label_B4p",
            "発売年月"), LABELT1("label_T1", "照会データ指定"), LABELT2("label_T2", "期間"), LABELT3("label_T3", "回以上"), LABELT4("label_T4", "前年差"), LABELT5("label_T5", "前月差"), LABELT6("label_T6", "登録年月"), LABELT7("label_T7", "表示切替"), LABELT8("label_T8", "（１年前からのプロモーションを表示します。）"), LABELA3("label_A3", "プロモーション"), LABELD01("label_D01", "集計対象"), LABELD02("label_D02", "集計範囲"), LABELD03("label_D03", "集計期間"), LABELD04("label_D04", "集計軸"), SELECT("label_select", "選択"), YOSAN("label_yosan", "計画"), TSU("label_tsu", ""), TSU2("label_tsu2", ""), CHECK("label_check", ""), YUSHU("label_yushu", "輸出先："), KANREN("label_kaigai", "関連会社："), ITAKU("label_itaku", "他社売上："), BRAND("label_brand", "他ブランド："),
    /** FROM */
    F("label_from", "FROM"),
    /** TO */
    T("label_to", "TO");

    private final String obj;
    private final String view;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     */
    Label(String obj, String view) {
      this.obj = obj;
      this.view = view;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }
  }

  /** HTML関連 ボタン */
  public enum Button {
    /** 照会 */
    SEARCH("btn_Search", "表示"),
    /** 純売上照会 */
    SEARCH1("btn_Search1", "純売上照会"),
    /** 出荷売上照会 */
    SEARCH2("btn_Search2", "出荷売上照会"),
    /** 調整売上照会 */
    SEARCH3("btn_Search3", "調整売上照会"),
    /** Excel */
    EXCEL("btn_Excel", "ファイル出力"),
    /** 表示項目 */
    ITEM("btn_Item", "表示項目"),
    /** 対象ﾌﾟﾛﾓｰｼｮﾝ一覧 開く */
    PROMO("btn_Promo", "対象ﾌﾟﾛﾓｰｼｮﾝ一覧"),
    /** 対象ﾌﾟﾛﾓｰｼｮﾝ一覧 照会 */
    SEARCH_PROMO("btn_Search_Promo", "照会"),
    /** リセット */
    RESET("btn_Reset", "リセット"),

    /** 閉じる */
    CLOSE("btn_Close", "閉じる");

    private final String obj;
    private final String view;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     */
    Button(String obj, String view) {
      this.obj = obj;
      this.view = view;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }
  }

  /** HTML関連 選択リストオプション(販売チャネル) */
  public enum OptionChanel {
    OPTION1("通販", "1"), OPTION2("店舗", "2"), OPTION3("通店計", "0");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionChanel(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(表示形式) */
  public enum OptionType {
    OPTIONT1("R x F", "1"), OPTIONT2("R x M", "2"), OPTIONT3("F x M", "3"), OPTIONT4("R x 初回受注月", "4"), OPTIONT5("F x 初回受注月", "5"), OPTIONT6("M x 初回受注月", "6");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(通販初回属性) */
  public enum OptionTypeA {
    OPTIONT1("R x F", "1"), OPTIONT2("R x M", "2"), OPTIONT3("F x M", "3"), OPTIONT4("R x 初回受注月", "4"), OPTIONT5("F x 初回受注月", "5"), OPTIONT6("M x 初回受注月", "6"), OPTIONT7("初回年齢 x 初回受注月", "7"), OPTIONT8("初回金額ﾗﾝｸ x 初回受注月", "8"), OPTIONT9("累計金額ﾗﾝｸ x 初回受注月", "9"), OPTIONT10("初回受注形態 x 初回受注月", "10"), OPTIONT11("初回商品 x 初回年齢", "11"), OPTIONT12("初回媒体 x 初回年齢", "12"), OPTIONT13("地域 x 初回年齢", "13"), OPTIONT14("初回年齢 x  F", "14");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionTypeA(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(通販初回媒体) */
  public enum OptionTypeA2 {
    OPTIONT1("初回媒体 x 初回受注月", "1"), OPTIONT2("初回媒体 x R", "2"), OPTIONT3("初回媒体 x F", "3"), OPTIONT4("初回媒体 x M", "4"), OPTIONT5("初回媒体 x 初回商品", "5"), OPTIONT6("初回媒体 x 初回商品詳細", "6"), OPTIONT7("初回媒体 x 初回年齢", "7"), OPTIONT8("初回媒体 x 初回受注形態", "8");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionTypeA2(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(通販初回商品) */
  public enum OptionTypeA3 {
    OPTIONT1("初回商品 x 初回受注月", "1"), OPTIONT2("初回商品 x R", "2"), OPTIONT3("初回商品 x F", "3"), OPTIONT4("初回商品 x M", "4");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionTypeA3(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(表示形式) */
  public enum OptionType2 {
    OPTIONT1("基本 R ３ヵ月単位", "1"), OPTIONT2("基本 R ２ヵ月単位", "2"), OPTIONT3("全展開", "3"), OPTIONT4("基本", "4"), OPTIONT5("R 全展開", "5"), OPTIONT6("全展開（F 10回迄）", "6");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType2(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(表示形式) */
  public enum OptionType3 {
    OPTIONT1("レジ客数", "1"), OPTIONT2("純売上", "2"), OPTIONT3("出荷売上", "3"), OPTIONT4("調整前純売上", "4"), OPTIONT5("すべて", "5");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType3(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(表示形式) */
  public enum OptionType3a {
    OPTIONT1("レジ客数", "1"), OPTIONT2("　リピート客数", "2"), OPTIONT3("　新規客数", "3"), OPTIONT4("出荷売上", "4"), OPTIONT5("単価", "5");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType3a(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(通販分析F2転換時期) */
  public enum OptionType5 {
    OPTIONT1("F2 当月", "1"), OPTIONT2("F2 ３ヶ月後", "2"), OPTIONT3("F2 ６ヶ月後", "3"), OPTIONT4("F2 １２ヶ月後", "4");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType5(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(通販分析) */
  public enum OptionType6 {
    OPTIONT1("顧客数", "1"), OPTIONT2("転換率", "2"), OPTIONT3("初回受注金額", "3"), OPTIONT4("平均初回金額", "4"), OPTIONT5("累計受注金額", "5"), OPTIONT6("CPO", "6"), OPTIONT7("平均初回年齢", "7"), OPTIONT8("平均初回客単価", "8");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType6(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(店舗分析) */
  public enum OptionType7 {
    OPTIONT1("すべて", "1"), OPTIONT2("新規", "2"), OPTIONT3("リピート", "3"), OPTIONT4("カード無し", "4"), OPTIONT5("未登録", "5");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType7(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(顧客数) 会員区分 */
  public enum OptionType8 {
    OPTION1("全会員", "0"), OPTION2("一般会員", "1"), OPTION3("特別会員", "2");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType8(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(購入ジャンル) */
  public enum OptionType9 {
    OPTION1("化粧品", "1000"), OPTION2("　スキンケア", "1010"), OPTION3("　スペシャルケア", "1020"), OPTION4("　スキンケア＋スペシャルケア", "1025"), OPTION5("　メイク", "1030"), OPTION6("　ラボ", "1096"), OPTION7("食品", "2000"), OPTION8("　ダイエット", "2001"), OPTION9("　ミネラル・ビタミン", "2010"), OPTION10("　体質強化", "2020"), OPTION11("ボディ", "3000"), OPTION12("すべて", "0000");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType9(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択リストオプション(登録経過月数) */
  public enum OptionType10 {
    OPTION1("F0顧客×登録経過月数", "1"), OPTION2("年齢×登録経過月数", "2");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType10(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }
  public enum OptionType11 {
    OPTION1("店舗横表示", "1"), OPTION2("店舗縦表示", "2");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType11(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }
  /** HTML関連 選択リストオプション(顧客数) ステージ/会員区分 */
  public enum OptionType12 {
    OPTION1("全会員", "0"), OPTION2("一般会員", "1"), OPTION3("特別会員", "2"), OPTION4("レギュラー", "10"), OPTION5("シルバー", "20"), OPTION6("ゴールド", "30"), OPTION7("プラチナ", "40"), OPTION8("プレミアム", "50");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType12(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }
  /** HTML関連 選択リストオプション(顧客数) ステージ */
  public enum OptionType13 {
    OPTION1("全体", "0"), OPTION2("レギュラー", "10"), OPTION3("シルバー", "20"), OPTION4("ゴールド", "30"), OPTION5("プラチナ", "40"), OPTION6("プレミアム", "50");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param view 表示名称
     * @param val 値
     */
    OptionType13(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（区分１） */
  public enum RadioCategory {
    /** 区分 */
    NAME("radio_category", "区分", ""), ID1("radio_category_1", "客数", "1"), ID2("radio_category_2", "回数", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadioCategory(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（商品区分１） */
  public enum RadioSho1 {
    NAME("radio_sho1", "商品", ""), ID1("radio_sho1_1", "全表示", "1"), ID2("radio_sho1_2", "実績のみ", "2"), ID3("radio_sho1_3", "実績", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadioSho1(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示データ区分2） */
  public enum RadioSho2 {
    /** 表示データ選択 */
    NAME("radio_sho2", "表示データ選択", ""),
    /** 金額(税抜き） */
    ID1("radio_sho2_1", "金額(税抜き）", "1"),
    /** 金額(税込） */
    ID2("radio_sho2_2", "金額(税込）", "2"),
    /** 客数 */
    ID3("radio_sho2_3", "客数", "3"),
    /** 数量 */
    ID4("radio_sho2_4", "数量", "4");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadioSho2(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（照会データ指定） */
  public enum RadB_1 {
    /** 区分 */
    NAME("radio_b1", "照会データ指定", ""),
    /** 1:年別 */
    ID1("radio_b1_1", "年別", "1"),
    /** 2:月別 */
    ID2("radio_b1_2", "月別", "2"),
    /** 3:日別 */
    ID3("radio_b1_3", "日別", "3"),
    /** 4:基本形 */
    ID4("radio_b1_4", "基本形", "4"),
    /** 5:年累計 */
    ID5("radio_b1_5", "年累計", "5");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_1(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（対比表示） */
  public enum RadB_2 {
    /** 区分名 */
    NAME("radio_b2", "対比表示", ""),
    /** 1:前年比 */
    ID1("radio_b2_1", "前年比", "1"),
    /** 2:前月比 */
    ID2("radio_b2_2", "前月比", "2"),
    /** 3:なし */
    ID3("radio_b2_3", "なし", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_2(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示対象） */
  public enum RadB_3 {
    /** 区分 */
    NAME("radio_b3", "表示対象", ""),
    /** 1:オルビス */
    ID1("radio_b3_1", "オルビス", "1"),
    /** 2:関連会社 */
    ID2("radio_b3_2", "関連会社", "2"),
    /** 3:連結 */
    ID3("radio_b3_3", "連結", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_3(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（12指定） */
  public enum RadB_4 {
    /** 区分 */
    NAME("radio_b4", "12指定", ""), ID1("radio_b4_1", "単月", "1"), ID2("radio_b4_2", "12点移動和", "2"), ID3("radio_b4_3", "", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_4(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（新製品指定） */
  public enum RadB_5 {
    /** 区分 */
    NAME("radio_b5", "新製品指定", ""), ID1("radio_b5_1", "なし", "1"), ID2("radio_b5_2", "あり", "2"), ID3("radio_b5_3", "", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_5(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（新リピート別） */
  public enum RadB_6 {
    /** 区分 */
    NAME("radio_b6", "新リピート別", ""), ID1("radio_b6_1", "しない", "1"), ID2("radio_b6_2", "する", "2"), ID3("radio_b6_3", "", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_6(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（店販大分類） */
  public enum RadB_72a {
    /** 区分 */
    NAME("radio_b72a", "店販大分類", ""),
    /** エリア */
    ID1("radio_b72a_1", "エリア", "1"),
    /** テナント */
    ID2("radio_b72a_2", "テナント", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_72a(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（店販分類） */
  public enum RadB_7a {
    /** 区分 */
    NAME("radio_b7a", "店販分類", ""), ID1("radio_b7a_1", "エリア→テナント", "1"), ID2("radio_b7a_2", "テナント→エリア", "2"), ID3("radio_b7a_3", "エリア→店舗別", "3"), ID4("radio_b7a_4", "テナント→店舗別", "4"), ID5("radio_b7a_5", "店舗別", "5");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_7a(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（外貨選択） */
  public enum RadB_9 {
    /** 区分 */
    NAME("radio_b9", "外貨選択", ""),
    /** 外貨 */
    ID1("radio_b9_1", "外貨", "1"),
    /** 円 */
    ID2("radio_b9_2", "円", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_9(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示対象） */
  public enum RadB_10 {
    /** 区分 */
    NAME("radio_b10", "表示対象", ""), ID1("radio_b10_1", "実績", "1"), ID2("radio_b10_2", "前年比", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadB_10(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（Ｒ値指定） */
  public enum RadT_01 {
    /** 区分 */
    NAME("radio_t01", "Ｒ値指定", "", ""),
    /** 1:Ｒ３以内 */
    ID1("radio_t01_1", "Ｒ３以内", "1", "3"),
    /** 2:Ｒ６以内 */
    ID2("radio_t01_2", "Ｒ６以内", "2", "6"),
    /** 3:Ｒ９以内 */
    ID3("radio_t01_3", "Ｒ９以内", "3", "9"),
    /** 4:Ｒ１２以内 */
    ID4("radio_t01_4", "Ｒ１２以内", "4", "12");

    private final String obj;
    private final String view;
    private final String val;
    private final String dbVal;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     * @param dbVal DB値
     */
    RadT_01(String obj, String view, String val, String dbVal) {
      this.obj = obj;
      this.view = view;
      this.val = val;
      this.dbVal = dbVal;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }

    /** @return dbVal DB値 */
    public String getDbVal() {
      return dbVal;
    }
  }

  /** HTML関連 ラジオ（表示対象） */
  public enum RadT_02 {
    /** 区分 */
    NAME("radio_t02", "表示対象", ""),
    /** 1:単月 */
    ID1("radio_t02_1", "単月", "1"),
    /** 2:過去12ヶ月 */
    ID2("radio_t02_2", "過去12ヶ月", "2"),
    /** 3:時系列 */
    ID3("radio_t02_3", "時系列", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_02(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示対象） */
  public enum RadT_03 {
    /** 区分 */
    NAME("radio_t03", "表示対象", ""),
    /** 1:年齢５歳集計 */
    ID1("radio_t03_1", "年齢５歳集計", "1"),
    /** 2:１歳毎集計 */
    ID2("radio_t03_2", "１歳毎集計", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_03(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（顧客状態） */
  public enum RadT_04 {
    /** 区分 */
    NAME("radio_t04", "顧客状態", ""),
    /** 1:全顧客 */
    ID1("radio_t04_1", "全顧客", "1"),
    /** 2:ACT顧客 */
    ID2("radio_t04_2", "ACT顧客", "2"),
    /** 3:DM-ACT顧客 */
    ID3("radio_t04_3", "DM-ACT顧客", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_04(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示項目） */
  public enum RadT_05 {
    /** 区分 */
    NAME("radio_t05", "表示項目", ""),
    /** 1:顧客数 */
    ID1("radio_t05_1", "顧客数", "1"),
    /** 2:シェア */
    ID2("radio_t05_2", "シェア", "2"),
    /** 3:ACT顧客数 */
    ID3("radio_t05_3", "ACT顧客数", "3"),
    /** 4:ACT率 */
    ID4("radio_t05_4", "ACT率", "4");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_05(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示項目） */
  public enum RadT_06 {
    /** 区分 */
    NAME("radio_t06", "表示項目", ""),
    /** 1:顧客数 */
    ID1("radio_t06_1", "顧客数", "1"),
    /** 2:転換率 */
    ID2("radio_t06_2", "転換率", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_06(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示項目） */
  public enum RadT_07 {
    /** 区分 */
    NAME("radio_t07", "表示項目", ""),
    /** 1:即時 */
    ID1("radio_t07_1", "即時", "1"),
    /** 2:継続 */
    ID2("radio_t07_2", "継続", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_07(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示項目） */
  public enum RadT_08 {
    /** 区分 */
    NAME("radio_t08", "表示項目", ""),
    /** 1:顧客数 */
    ID1("radio_t08_1", "顧客数", "1"),
    /** 2:受注件数 */
    ID2("radio_t08_2", "受注件数", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_08(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }
  /** HTML関連 ラジオ（表示項目） */
  public enum RadT_09 {
    /** 区分 */
    NAME("radio_t09", "会員区分", ""), ID1("radio_t09_1", "全会員", "0"), ID2("radio_t09_2", "一般会員", "1"), ID3("radio_t09_3", "特別会員", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_09(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }
  /** HTML関連 ラジオ（会員区分） */
  public enum RadT_10 {
    /** 区分 */
    NAME("radio_t10", "ステージ/会員区分", ""), ID1("radio_t10_1", "ステージ", "0"), ID2("radio_t10_2", "会員区分", "1");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadT_10(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（購入チャネル） */
  public enum RadD01_CHANEL {
    NAME("radio_d01", "購入チャネル", ""), ID1("radio_d01_1", "通店計", "1"), ID2("radio_d01_2", "通販", "2"), ID3("radio_d01_3", "店舗", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD01_CHANEL(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（集計単位） */
  public enum RadD02_UNIT {
    NAME("radio_d02", "集計単位", ""), ID1("radio_d02_1", "年", "1"), ID2("radio_d02_2", "月", "2"), ID3("radio_d02_3", "日", "3"), ID4("radio_d02_4", "累計", "4");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD02_UNIT(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（店舗） */
  public enum RadD03_TENPO {
    NAME("radio_d03", "店舗", ""), ID1("radio_d03_1", "全店舗", "1"), ID2("radio_d03_2", "店舗選択", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD03_TENPO(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（都道府県） */
  public enum RadD04_AREA {
    NAME("radio_d04", "都道府県", ""), ID1("radio_d04_1", "全件表示", "1"), ID2("radio_d04_2", "表示選択", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD04_AREA(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（年齢） */
  public enum RadD05_AGE {
    NAME("radio_d05", "年齢", ""), ID1("radio_d05_1", "1歳刻み", "1"), ID2("radio_d05_2", "5歳刻み", "2"), ID3("radio_d05_3", "10歳刻み", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD05_AGE(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（初回購入年） */
  public enum RadD06_FKY {
    NAME("radio_d06", "初回購入年", ""), ID1("radio_d06_1", "1年刻み", "1"), ID2("radio_d06_2", "5年刻み", "2"), ID3("radio_d06_3", "10年刻み", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD06_FKY(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（前年購入額） */
  public enum RadD07_ZKG {
    NAME("radio_d07", "前年購入額", ""), ID1("radio_d07_1", "全件表示", "1"), ID2("radio_d07_2", "金額刻み", "2");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD07_ZKG(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（R値） */
  public enum RadD08_RVL {
    NAME("radio_d08", "R値", ""), ID1("radio_d08_1", "R6", "1"), ID2("radio_d08_2", "R12", "2"), ID3("radio_d08_3", "全体", "0");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD08_RVL(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（R値） */
  public enum RadD08_ZRVL {
    NAME("radio_d08", "前月末R値", ""), ID1("radio_d08_1", "R6", "1"), ID2("radio_d08_2", "R12", "2"), ID3("radio_d08_3", "全体", "0");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD08_ZRVL(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（購入商品） */
  public enum RadD09_CLS {
    NAME("radio_d09", "購入商品", ""), ID1("radio_d09_1", "基本分野", "1"), ID2("radio_d09_2", "主要カテゴリ", "2"), ID3("radio_d09_3", "重点アイテム", "3"), ID4("radio_d09_4", "分類指定", "4");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD09_CLS(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（購入商品） */
  public enum CheckD10_CLS {
    NAME("check_d10", "購入商品", ""), ID1("check_d10_1", "基本分類", "1"), ID2("check_d10_2", "大分類", "2"), ID3("check_d10_3", "中分類", "3"), ID4("check_d10_4", "小分類", "4"), ID5("check_d10_5", "シリーズA", "5"), ID6("check_d10_6", "シリーズB", "6"), ID7("check_d10_7", "商品", "7");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    CheckD10_CLS(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（集計軸） */
  public enum RadD11_AXIS {
    NAME("radio_d11", "集計軸", ""), ID0("radio_d11_0", "集計軸なし", "0"), ID1("radio_d11_1", "都道府県", "1"), ID2("radio_d11_2", "年齢", "2"), ID3("radio_d11_3", "初回購入年", "3"), ID4("radio_d11_4", "前年購入額", "4");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD11_AXIS(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（照会） */
  public enum RadD12_SEARCH {
    NAME("radio_d12", "照会", ""), ID1("radio_d12_1", "純売上", "1"), ID2("radio_d12_2", "出荷売上", "2"), ID3("radio_d12_3", "調整前売上", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD12_SEARCH(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（表示項目） */
  public enum RadD13_COLUMN {
    NAME("radio_d13", "表示項目", ""), ID1("radio_d13_1", "レジ件数", "1"), ID2("radio_d13_2", "オルビス新規件数", "2"), ID3("radio_d13_3", "リピート件数", "3"), ID4("radio_d13_4", "カードなし件数", "4"), ID5("radio_d13_5", "未登録件数", "5"), ID6("radio_d13_6", "純売上", "6"), ID7("radio_d13_7", "出荷売上", "7"), ID8("radio_d13_8", "調整前売上", "8"), ID9("radio_d13_9", "レジ単価（出荷売上）", "9");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD13_COLUMN(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 ラジオ（商品分野） */
  public enum RadD14_CLS {
    NAME("radio_d14", "商品分野", ""), ID1("radio_d14_1", "化粧品", "1"), ID2("radio_d14_2", "食品", "2"), ID3("radio_d14_3", "ボディウェア", "3"), ID4("radio_d14_4", "その他", "4"), ID5("radio_d14_5", "すべて", "5");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    RadD14_CLS(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 チェックボックス */
  public enum Check {
    CHK_SHO1("check_sho1", "基本分類"), CHK_SHO2("check_sho2", "大分類"), CHK_SHO3("check_sho3", "中分類"), CHK_SHO4("check_sho4", "小分類"), CHK_SHO5("check_sho5", "シリーズＡ"), CHK_SHO6("check_sho6", "シリーズＢ"), CHK_ITEM("check_item", "商品");

    private final String obj;
    private final String view;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     */
    Check(String obj, String view) {
      this.obj = obj;
      this.view = view;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }
  }

  /** HTML関連 チェックボックス （表示対象） */
  public enum CheckB_1 {
    /** 区分 */
    NAME("chk_b1", "表示対象", ""),
    /** 通販 */
    ID1("chk_b1_1", "通販", "on"),
    /** 店舗 */
    ID2("chk_b1_2", "店舗", "on"),
    /** 輸出 */
    ID3("chk_b1_3", "輸出", "on"),
    /** 従販 */
    ID4("chk_b1_4", "従販", "on"),
    /** 他社売上 */
    ID5("chk_b1_5", "他社売上", "on"),
    /** 他ブランド */
    ID6("chk_b1_6", "他ブランド", "on");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    CheckB_1(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 チェックボックス （受注経路） */
  public enum CheckB_3 {
    /** 区分 */
    NAME("check_kei", "受注経路", ""), ID1("check_kei1", "電話", "on"), ID2("check_kei2", "IVR", "on"), ID3("check_kei3", "FAX･封書･ハガキ", "on"), ID4("check_kei4", "スマホ", "on"), ID5("check_kei5", "アプリ", "on"), ID6("check_kei6", "PC", "on"), ID7("check_kei7", "モバイル", "on"), ID8("check_kei8", "定期販売", "on"), ID9("check_kei9", "店舗", "on");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    CheckB_3(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 チェックボックス （チャネル集計レベル） */
  public enum CheckB4_1 {
    /** 区分 */
    NAME("chk_cl", "チャネル集計", ""),
    /** 基本分類 */
    ID1("chk_cl1", "基本分類", "on"),
    /** 大分類 */
    ID2("chk_cl2", "大分類", "on"),
    /** 中分類 */
    ID3("chk_cl3", "中分類", "on"),
    /** 小分類 */
    ID4("chk_cl4", "小分類", "on");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    CheckB4_1(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 チェックボックス （商品集計レベル） */
  public enum CheckB4_2 {
    /** 区分 */
    NAME("chk_sl", "商品集計", ""),
    /** 基本分類 */
    ID1("chk_sl1", "基本分類", "on"),
    /** 大分類 */
    ID2("chk_sl2", "大分類", "on"),
    /** 中分類 */
    ID3("chk_sl3", "中分類", "on"),
    /** 小分類 */
    ID4("chk_sl4", "小分類", "on"),
    /** シリーズＡ */
    ID5("chk_sl5", "シリーズＡ", "on"),
    /** シリーズＢ */
    ID6("chk_sl6", "シリーズＢ", "on"),
    /** 商品 */
    ID7("chk_sl7", "商品", "on"),
    /** 分類２使用 */
    ID8("chk_sl8", "分類２使用", "on");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    CheckB4_2(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 チェックボックス （新規リピート） */
  public enum CheckB4_3 {
    ID1("chk_nr1", "新規", "on"), ID2("chk_nr2", "リピート", "on");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    CheckB4_3(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 チェックボックス （DM対象） */
  public enum CheckB4_4 {
    ID1("chk_dm", "DM対象者+DM対象外", "on");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    CheckB4_4(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 表示項目選択 */
  public enum Item {
    /** 純売上表示項目 */
    ITEM_JUN("select_item_jun", "純売上表示項目", "1"),
    /** 出荷売上表示項目 */
    ITEM_SHO("select_item_sho", "出荷売上表示項目", "2"),
    /** 調整売上表示項目 */
    ITEM_CHO("select_item_cho", "調整売上表示項目", "3");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    Item(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /** HTML関連 選択(店舗) */
  public enum OptionTenpo {
    ALL("tenpo_all", "全店舗", "00"), EXIST("tenpo_exist", "既存店", "01");

    private final String obj;
    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    OptionTenpo(String obj, String view, String val) {
      this.obj = obj;
      this.view = view;
      this.val = val;
    }

    /** @return obj Object名 */
    public String getObj() {
      return obj;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }

  /**
   * HTML関連 隠蔽情報
   */
  /** レポート番号 */
  public static final String ID_HIDDEN_REPORT_NO = "reportno";
  /** レポート名 */
  public static final String ID_HIDDEN_REPORT_NAME = "reportname";
  /** ユーザー 情報 */
  public static final String ID_HIDDEN_USER_DATA = "userid";
  /** ユーザID */
  public static final String ID_HIDDEN_USER_ID = "hiddenUser";
  /** レポートID */
  public static final String ID_HIDDEN_REPORT_ID = "hiddenReport";

  /** 基準値関連 ステージ/会員区分切替 */
  public enum base {
    INPUT("入力経路切替", "201310"), INPUT_ZENNEN("入力経路前年比", "201410"), INPUT_TIME("入力経路切替時間帯", "20150220"), KAIIN("会員区分切替前", "201409"), STAGE("ステージ完全切替", "201500"), TABLE_TP("テーブル切替基準", "201900");

    private final String view;
    private final String val;

    /**
     * 初期化
     *
     * @param obj Object名
     * @param view 表示名称
     * @param val 値
     */
    base(String view, String val) {
      this.view = view;
      this.val = val;
    }

    /** @return view 表示名称 */
    public String getView() {
      return view;
    }

    /** @return val 値 */
    public String getVal() {
      return val;
    }
  }


  /**
   * SQL 関連
   */
  /** 共通：すべて */
  public static final String ID_SQL_SELECT_ALL = "SELECT '00' AS VALUE, 'すべて' AS TEXT, '00' AS SORT FROM dual union all ";
  /** 共通：すべて[数] */
  public static final String ID_SQL_SELECT_ALL_NUM = "SELECT '0' AS VALUE, ' ' AS TEXT, '0' AS SORT FROM dual union all ";
  /** 共通：全国 */
  public static final String ID_SQL_SELECT_ALL_AREA = "SELECT '00' AS VALUE, '全国' AS TEXT, '00' AS SORT FROM dual union all ";
  /** 共通：店舗 */
  public static final String ID_SQL_SELECT_ALL_SHOP = "SELECT '00' AS VALUE, '全店舗' AS TEXT, '00' AS SORT FROM dual union all ";
  /** 共通：既存店舗 */
  public static final String ID_SQL_SELECT_EXIST_SHOP = "SELECT '01' AS VALUE, '既存店' AS TEXT, '01' AS SORT FROM dual union all ";

  /** 基準年月[YYYYMM]（DM関連） */
  public static final String ID_SQL_INIT_YM_KIJUN_DM = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_DM ORDER BY SORT DESC";
  /** 基準年月[YYYYMM]（通販分析） */
  public static final String ID_SQL_INIT_YM_KIJUN_TSU = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_TSU ORDER BY SORT DESC";
  /** 基準年月[YYYYMM]（画面別） */
  // public static final String ID_SQL_INIT_YM_KIJUN_B01 ="select KIJUN_YM as VALUE, KIJUN_YM_MEI as
  // TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_B01 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_B02 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_B02 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_B02A = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_B02A ORDER BY SORT DESC";
  // public static final String ID_SQL_INIT_YM_KIJUN_B031 ="SELECT SUBSTR(URI_YMD, 1, 6) AS VALUE,
  // SUBSTR(URI_YMD, 1, 6) AS TEXT, TO_NUMBER(SUBSTR(URI_YMD, 1, 6)) AS SORT FROM URI_TSU_DAY WHERE
  // URI_YMD <= TO_CHAR(CURRENT_DATE, 'YYYYMMDD') GROUP BY SUBSTR(URI_YMD, 1, 6) ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_B031 = "SELECT DISTINCT SUBSTR(URI_YMD, 1, 6) AS VALUE, SUBSTR(URI_YMD, 1, 6) AS TEXT, TO_NUMBER(SUBSTR(URI_YMD, 1, 6)) AS SORT FROM DS_M_URI_TSU_DAY WHERE URI_YMD <= TO_CHAR(CURRENT_DATE, 'YYYYMMDD') ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_B03 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_B03 ORDER BY SORT DESC";


  public static final String ID_SQL_INIT_YM_KIJUN_T01 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T01 order by SORT desc";
  public static final String ID_SQL_INIT_YM_KIJUN_T02 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T02 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T19 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T19 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T20 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T20 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T21 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T21 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T21_12 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T21 WHERE KIJUN_YM >= '200901' ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T22 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T22 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T23 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T23 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T24 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T24 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T251 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T251 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T252 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T252 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T253 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T253 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T261 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T261 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T262 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T262 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T27 = "select KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from YM_KIJUN_T27 ORDER BY SORT DESC";
  /** 基準年月[YYYYMMDD]（画面別） */
  public static final String ID_SQL_INIT_YMD_KIJUN_T13 = "select KIJUN_YMD as VALUE, KIJUN_YMD_MEI as TEXT, TO_NUMBER(KIJUN_YMD) as SORT from YMD_KIJUN_T13 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_KIJUN_T13 = "select substr(KIJUN_YMD,1,6) as VALUE, substr(max(KIJUN_YMD_MEI),1,8) as TEXT, TO_NUMBER(substr(KIJUN_YMD,1,6)) as SORT from YMD_KIJUN_T13 group by substr(KIJUN_YMD,1,6) ORDER BY SORT DESC";

  /** 基準年月 201401以降表示しない */
  public static final String ID_SQL_INIT_YM_KIJUN_T10 = "SELECT KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT FROM YM_KIJUN_DM WHERE KIJUN_YM < '201501' ORDER BY SORT DESC";

  /** 基準年[YYYY]（初期化） */
  public static final String ID_SQL_INIT_YY_KIJUN = "select distinct substr(KIJUN_YM,1,4) as VALUE, substr(KIJUN_YM,1,4) as TEXT, TO_NUMBER(substr(KIJUN_YM,1,4)) as SORT from YM_KIJUN_DM ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_B01 = "select distinct substr(KIJUN_YM,1,4) as VALUE, substr(KIJUN_YM,1,4) as TEXT, TO_NUMBER(substr(KIJUN_YM,1,4)) as SORT from YM_KIJUN_B01 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_B02 = "select distinct substr(KIJUN_YM,1,4) as VALUE, substr(KIJUN_YM,1,4) as TEXT, TO_NUMBER(substr(KIJUN_YM,1,4)) as SORT from YM_KIJUN_B02 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_B02A = "select distinct substr(KIJUN_YM,1,4) as VALUE, substr(KIJUN_YM,1,4) as TEXT, TO_NUMBER(substr(KIJUN_YM,1,4)) as SORT from YM_KIJUN_B02A ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_B03 = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM YM_KIJUN_B03 WHERE KIJUN_YM <= TO_CHAR(CURRENT_DATE, 'YYYYMM') ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_B04 = "select distinct substr(KIJUN_YM,1,4) as VALUE, substr(KIJUN_YM,1,4) as TEXT, TO_NUMBER(substr(KIJUN_YM,1,4)) as SORT from YM_KIJUN_B04 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_T19 = "select distinct SUBSTR(KIJUN_YM,1,4) as VALUE, SUBSTR(KIJUN_YM_MEI,1,5) as TEXT, TO_NUMBER(SUBSTR(KIJUN_YM,1,4)) as SORT from YM_KIJUN_T19 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_T22 = "select distinct SUBSTR(KIJUN_YM,1,4) as VALUE, SUBSTR(KIJUN_YM_MEI,1,5) as TEXT, TO_NUMBER(SUBSTR(KIJUN_YM,1,4)) as SORT from YM_KIJUN_T22 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_T23 = "select distinct SUBSTR(KIJUN_YM,1,4) as VALUE, SUBSTR(KIJUN_YM_MEI,1,5) as TEXT, TO_NUMBER(SUBSTR(KIJUN_YM,1,4)) as SORT from YM_KIJUN_T23 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_T251 = "select distinct SUBSTR(KIJUN_YM,1,4) as VALUE, SUBSTR(KIJUN_YM_MEI,1,5) as TEXT, TO_NUMBER(SUBSTR(KIJUN_YM,1,4)) as SORT from YM_KIJUN_T251 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_T252 = "select distinct SUBSTR(KIJUN_YM,1,4) as VALUE, SUBSTR(KIJUN_YM_MEI,1,5) as TEXT, TO_NUMBER(SUBSTR(KIJUN_YM,1,4)) as SORT from YM_KIJUN_T252 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_T253 = "select distinct SUBSTR(KIJUN_YM,1,4) as VALUE, SUBSTR(KIJUN_YM_MEI,1,5) as TEXT, TO_NUMBER(SUBSTR(KIJUN_YM,1,4)) as SORT from YM_KIJUN_T253 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_T27 = "select distinct SUBSTR(KIJUN_YM,1,4) as VALUE, SUBSTR(KIJUN_YM_MEI,1,5) as TEXT, TO_NUMBER(SUBSTR(KIJUN_YM,1,4)) as SORT from YM_KIJUN_T27 ORDER BY SORT DESC";

  public static final String ID_SQL_INIT_DS_M_YM_GEN = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_GENKA ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_URI = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_URI ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_PRMURI = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_PRM ORDER BY SORT DESC";

  public static final String ID_SQL_INIT_DS_M_YM_F2C = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_F2C ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_STEP = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_STEP ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_PNT = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_PNT ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_APP = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_APP ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_CNC = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_CNC ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_TEN = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_TEN ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_TNK = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_TNK ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_JUC = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_JUC ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_STG = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_STAGE ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_CPO = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM DS_M_YM_CPO ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_MUP = "SELECT DISTINCT SUBSTR(URI_YM, 1, 4) AS VALUE, SUBSTR(URI_YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(URI_YM, 1, 4)) AS SORT FROM URI_MOK ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_DS_M_YM_PRM = "SELECT DISTINCT SUBSTR(YM, 1, 4) AS VALUE, SUBSTR(YM, 1, 4) AS TEXT, TO_NUMBER(SUBSTR(YM, 1, 4)) AS SORT FROM DS_B_PROMO_PLAN ORDER BY SORT DESC";


  public static final String ID_SQL_INIT_YY_KIJUN_T01 = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM_MEI, 1, 5) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM YM_KIJUN_T01 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_T03 = "SELECT DISTINCT SUBSTR(KIJUN_YM, 1, 4) AS VALUE, SUBSTR(KIJUN_YM_MEI, 1, 5) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 1, 4)) AS SORT FROM YM_KIJUN_DM ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YY_KIJUN_L01 = "SELECT DISTINCT TO_CHAR(DT_ACTION, 'YYYY') AS VALUE, TO_CHAR(DT_ACTION, 'YYYY') || '年' AS TEXT, TO_NUMBER(TO_CHAR(DT_ACTION, 'YYYY')) AS SORT FROM AK_SYS_LOGS ORDER BY SORT DESC";

  public static final String ID_SQL_INIT_YY_KIJUN_D00 = "select distinct substr(KIJUN_YM,1,4) as VALUE, substr(KIJUN_YM,1,4) as TEXT, TO_NUMBER(substr(KIJUN_YM,1,4)) as SORT from YM_KIJUN_DM ORDER BY SORT DESC";

  public static final String ID_SQL_INIT_YMD_LOG = " WITH WKYMD(YMD) AS (select distinct(TO_CHAR(DT_ACTION, 'YYYYMMDD')) from AK_SYS_LOGS ) SELECT YMD AS VALUE,SUBSTR(YMD,1,4)||'年'||SUBSTR(YMD,5,2)||'月'||SUBSTR(YMD,7,2)||'日' AS TEXT ,TO_NUMBER(YMD) as SORT from WKYMD ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_LOG = " WITH WKYMM(YMM) AS (select distinct(TO_CHAR(DT_ACTION, 'YYYYMM')) from AK_SYS_LOGS ) SELECT YMM AS VALUE,SUBSTR(YMM,1,4)||'年'||SUBSTR(YMM,5,2)||'月' AS TEXT ,TO_NUMBER(YMM) as SORT from WKYMM ORDER BY SORT DESC";

  /** 基準月[MM]（初期化） */
  public static final String ID_SQL_INIT_MM_KIJUN = "select distinct substr(KIJUN_YM,5,2) as VALUE, substr(KIJUN_YM,5,2) as TEXT, TO_NUMBER(substr(KIJUN_YM,5,2)) as SORT from YM_KIJUN_DM ORDER BY SORT";

  public static final String ID_SQL_INIT_MM_KIJUN_T01 = "SELECT DISTINCT SUBSTR(KIJUN_YM, 5, 2) AS VALUE, SUBSTR(KIJUN_YM_MEI, 6, 3) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 5, 2)) AS SORT FROM YM_KIJUN_T01 ORDER BY SORT";
  public static final String ID_SQL_INIT_MM_KIJUN_T03 = "SELECT DISTINCT SUBSTR(KIJUN_YM, 5, 2) AS VALUE, SUBSTR(KIJUN_YM_MEI, 6, 3) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 5, 2)) AS SORT FROM YM_KIJUN_DM ORDER BY SORT";
  public static final String ID_SQL_INIT_MM_KIJUN_T252 = "SELECT DISTINCT SUBSTR(KIJUN_YM, 5, 2) AS VALUE, SUBSTR(KIJUN_YM_MEI, 6, 3) AS TEXT, TO_NUMBER(SUBSTR(KIJUN_YM, 5, 2)) AS SORT FROM YM_KIJUN_T252 ORDER BY SORT";
  public static final String ID_SQL_INIT_MM_KIJUN_L01 = "SELECT DISTINCT TO_CHAR(DT_ACTION, 'MM') AS VALUE, TO_CHAR(DT_ACTION, 'MM') || '月' AS TEXT, TO_NUMBER(TO_CHAR(DT_ACTION, 'MM')) AS SORT FROM AK_SYS_LOGS ORDER BY SORT";

  /** 基準年月日[DD]（初期化） */
  public static final String ID_SQL_INIT_DD_KIJUN = "SELECT SUBSTR(YMD_CD,7,2) as VALUE, SUBSTR(YMD_CD,7,2) as TEXT, TO_NUMBER(SUBSTR(YMD_CD,7,2)) as SORT from M_CALENDAR group by SUBSTR(YMD_CD,7,2) ORDER BY SORT";
  public static final String ID_SQL_INIT_DD_KIJUN_L01 = "SELECT DISTINCT TO_CHAR(DT_ACTION, 'DD') AS VALUE, TO_CHAR(DT_ACTION, 'DD') || '日' AS TEXT, TO_NUMBER(TO_CHAR(DT_ACTION, 'DD')) AS SORT FROM AK_SYS_LOGS ORDER BY SORT";

  /** * 初回受注年月[YYYY年] */
  public static final String ID_SQL_INIT_JUCYY = "SELECT NF_JUCHU_YY AS VALUE, RTRIM(MAX(NF_JUCHU_YY_NM)) AS TEXT,NF_JUCHU_YY AS SORT FROM YM_F_JUCHU where NF_JUCHU_YM >= '200501' GROUP BY NF_JUCHU_YY ORDER BY SORT DESC";
  /** * 初回受注年月[YYYY年MM月] */
  public static final String ID_SQL_INIT_JUCYM = "SELECT NF_JUCHU_YM AS VALUE, RTRIM(MAX(NF_JUCHU_YM_NM)) AS TEXT,NF_JUCHU_YM AS SORT FROM YM_F_JUCHU where NF_JUCHU_YM >= '200501' GROUP BY NF_JUCHU_YM ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_JUCYM_24 = "SELECT NF_JUCHU_YM AS VALUE, RTRIM(MAX(NF_JUCHU_YM_NM)) AS TEXT,NF_JUCHU_YM AS SORT FROM YM_F_JUCHU  GROUP BY NF_JUCHU_YM ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_JUCYM2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT, '99' AS SORT FROM dual union all SELECT NF_JUCHU_YM AS VALUE, RTRIM(MAX(NF_JUCHU_YM_NM)) AS TEXT,NF_JUCHU_YM AS SORT FROM YM_F_JUCHU where NF_JUCHU_YM >= '200501' GROUP BY NF_JUCHU_YM ORDER BY SORT DESC";
  /** * 登録年月[YYYY年MM月] */
  public static final String ID_SQL_INIT_TOROKUYM = "SELECT TOROKU_YM AS VALUE, RTRIM(MAX(TOROKU_YM_NM)) AS TEXT,TOROKU_YM AS SORT FROM YM_TOROKU  GROUP BY TOROKU_YM ORDER BY SORT DESC";
  /** 新製品年月（初期化） */
  public static final String ID_SQL_INIT_YM_SHIN = "SELECT START_YM AS VALUE, START_YM AS TEXT,TO_NUMBER(START_YM) AS SORT FROM M_SHOHIN WHERE START_YM > '1900' GROUP BY START_YM ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_YM_SHIN2 = "SELECT START_YM AS VALUE, SUBSTR(START_YM,1,4)||'年'||SUBSTR(START_YM,5,2)||'月' AS TEXT,TO_NUMBER(START_YM) AS SORT FROM M_SHOHIN WHERE START_YM > '1900' GROUP BY START_YM ORDER BY SORT DESC";
  /** 基準年月[YYYYMM]（デフォルト値取得） */
  public static final String ID_SQL_INIT_DEFAULT_YM_KIJUN = "SELECT MAX(KIJUN_YM) AS \"MAX\",MIN(KIJUN_YM) AS \"MIN\" FROM YM_KIJUN_B01";
  public static final String ID_SQL_INIT_DEFAULT_YM_KIJUN_B03 = "SELECT MAX(KIJUN_YM) AS \"MAX\",MIN(KIJUN_YM) AS \"MIN\" FROM DS_M_YM_JUC WHERE KIJUN_YM <= TO_CHAR(CURRENT_DATE, 'YYYYMM')";
  public static final String ID_SQL_INIT_DEFAULT_DD_KIJUN = "SELECT MAX(URI_YMD) AS \"MAX\",MIN(URI_YMD) AS \"MIN\" FROM DS_M_URI_TSU_DAY WHERE URI_YMD <= TO_CHAR(CURRENT_DATE, 'YYYYMMDD')";


  /** プロモーション（初期化） */
  public static final String ID_SQL_INIT_PROMO = "select distinct RTRIM(PROMO_CD) as VALUE, RTRIM(PROMO_CD)||'_'||HAKKOBI||' '||PROMO_MEI as TEXT, RTRIM(PROMO_CD) as SORT from M_PROMO_DM order by SORT desc";
  /** * プロモーション2 */
  public static final String ID_SQL_INIT_PROMO2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL select distinct RTRIM(PROMO_CD) as VALUE, RTRIM(PROMO_CD)||'_'||HAKKOBI||' '||PROMO_MEI as TEXT, RTRIM(PROMO_CD) as SORT from M_PROMO_ALL WHERE BYTAI2_CD = ? order by SORT desc";
  /** * プロモーション１５次媒体 */
  public static final String ID_SQL_INIT_PROMO13 = "select distinct HAKKOBI||RTRIM(PROMO_CD) as VALUE, RTRIM(PROMO_CD)||'_'||HAKKOBI||' '||PROMO_MEI as TEXT, HAKKOBI||RTRIM(PROMO_CD) as SORT from M_PROMO_T13 order by SORT desc";
  /** * プロモーション16単価分析 */
  public static final String ID_SQL_INIT_PROMO16 = "select distinct RTRIM(PROMO_CD) as VALUE, RTRIM(PROMO_CD)||'_'||HAKKOBI||' '||PROMO_MEI as TEXT, RTRIM(PROMO_CD) as SORT from M_PROMO_T16 order by SORT desc";
  /** 条件番号（初期化） */
  public static final String ID_SQL_INIT_SEG = "select 0 as VALUE,'  ' as TEXT,0 as SORT from dual union all SELECT SEGNO AS VALUE, RTRIM(SEGNO_NM) AS TEXT,SEGNO AS SORT FROM M_SEG ORDER BY SORT";
  public static final String ID_SQL_INIT_SEG2 = "SELECT SEGNO AS VALUE, RTRIM(SEGNO_NM) AS TEXT,SEGNO AS SORT FROM M_SEG ORDER BY SORT";
  /** 条件番号（T03_SEG） */
  public static final String ID_SQL_INIT_SEG_T03 = "SELECT SEGNO AS VALUE, RTRIM(SEGNO_NM) AS TEXT,SEGNO AS SORT FROM M_SEG, (SELECT MAX(SEGNO) AS MAXNO FROM T03_SEG WHERE TRIM(PROMO_CD) BETWEEN ? AND ?) T WHERE SEGNO <= T.MAXNO ORDER BY SORT";
  /** 月別プロモーション一覧 */
  public static final String ID_SQL_INIT_PROMO_LIST = "select distinct HANTEI_YM || PROMO_CD || HAKKOBI as SORT, HANTEI_YM, PROMO_CD, HAKKOBI, PROMO_MEI, BYTAI2_CD from M_PROMO_HAN where HANTEI_YM LIKE ? order by SORT";

  /** * 商品分類号DM実績（初期化） */
  public static final String ID_SQL_INIT_SHO1 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN1_SHOHIN_CD AS VALUE, RTRIM(BUN1_SHOHIN_MEI) AS TEXT,BUN1_SHOHIN_CD AS SORT FROM M_SHOHIN_DM GROUP BY BUN1_SHOHIN_CD,BUN1_SHOHIN_MEI ORDER BY SORT";
  public static final String ID_SQL_INIT_SHO2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN2_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN2_SHOHIN_MEI)) AS TEXT,BUN2_SHOHIN_CD AS SORT FROM M_SHOHIN_DM WHERE BUN1_SHOHIN_CD = ? GROUP BY BUN2_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_SHO3 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN3_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN3_SHOHIN_MEI)) AS TEXT,BUN3_SHOHIN_CD AS SORT FROM M_SHOHIN_DM WHERE BUN2_SHOHIN_CD = ?  GROUP BY BUN3_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_SHO4 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN4_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN4_SHOHIN_MEI)) AS TEXT,BUN4_SHOHIN_CD AS SORT FROM M_SHOHIN_DM WHERE BUN3_SHOHIN_CD = ?  GROUP BY BUN4_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_SHO5 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN5_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN5_SHOHIN_MEI)) AS TEXT,BUN5_SHOHIN_CD AS SORT FROM M_SHOHIN_DM WHERE BUN4_SHOHIN_CD = ?  GROUP BY BUN5_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_SHO6 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN6_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN6_SHOHIN_MEI)) AS TEXT,BUN6_SHOHIN_CD AS SORT FROM M_SHOHIN_DM WHERE BUN5_SHOHIN_CD = ?  GROUP BY BUN6_SHOHIN_CD ORDER BY SORT";
  /** * 商品分類顧客数関連（初期化） */
  public static final String ID_SQL_INIT25_SHO1 = "SELECT BUN1_SHOHIN_CD AS VALUE, RTRIM(BUN1_SHOHIN_MEI) AS TEXT,BUN1_SHOHIN_CD AS SORT FROM M_SHOHIN_T25 GROUP BY BUN1_SHOHIN_CD,BUN1_SHOHIN_MEI ORDER BY SORT";
  public static final String ID_SQL_INIT25_SHO2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN2_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN2_SHOHIN_MEI)) AS TEXT,BUN2_SHOHIN_CD AS SORT FROM M_SHOHIN_T25 WHERE BUN1_SHOHIN_CD = ? GROUP BY BUN2_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT25_SHO3 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN3_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN3_SHOHIN_MEI)) AS TEXT,BUN3_SHOHIN_CD AS SORT FROM M_SHOHIN_T25 WHERE BUN2_SHOHIN_CD = ?  GROUP BY BUN3_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT25_SHO4 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN4_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN4_SHOHIN_MEI)) AS TEXT,BUN4_SHOHIN_CD AS SORT FROM M_SHOHIN_T25 WHERE BUN3_SHOHIN_CD = ?  GROUP BY BUN4_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT25_SHO5 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN5_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN5_SHOHIN_MEI)) AS TEXT,BUN5_SHOHIN_CD AS SORT FROM M_SHOHIN_T25 WHERE BUN4_SHOHIN_CD = ?  GROUP BY BUN5_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT25_SHO6 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN6_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN6_SHOHIN_MEI)) AS TEXT,BUN6_SHOHIN_CD AS SORT FROM M_SHOHIN_T25 WHERE BUN5_SHOHIN_CD = ?  GROUP BY BUN6_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT25_SHO = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT SHOHIN_CD AS VALUE, RTRIM(SHOHIN_CD)||'_'||RTRIM(MAX(SHOHIN_MEI)) AS TEXT,SHOHIN_CD AS SORT FROM M_SHOHIN_T25 WHERE BUN6_SHOHIN_CD = ?  GROUP BY SHOHIN_CD ORDER BY SORT";

  /** * 商品分類名称選択 */
  public static final String ID_SQL_INIT_BUN1 = "SELECT BUN_NO AS VALUE, BUN_NM AS TEXT,BUN_NO AS SORT FROM M_BUNRUI where BUN_NO > ? ORDER BY SORT";
  /** 商品分類選択 */
  public static final String ID_SQL_INIT_T_SHO1 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN1_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN1_SHOHIN_MEI)) AS TEXT,BUN1_SHOHIN_CD AS SORT FROM M_SHOHIN  GROUP BY BUN1_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN2_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN2_SHOHIN_MEI)) AS TEXT,BUN2_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN1_SHOHIN_CD = ? GROUP BY BUN2_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO3 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN3_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN3_SHOHIN_MEI)) AS TEXT,BUN3_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN2_SHOHIN_CD = ? GROUP BY BUN3_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO3_BUN1 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN3_SHOHIN_CD AS VALUE, MAX(BUN3_SHOHIN_CD)||'_'||RTRIM(MAX(BUN3_SHOHIN_MEI)) AS TEXT,BUN3_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN1_SHOHIN_CD = ? GROUP BY BUN3_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO4 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN4_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN4_SHOHIN_MEI)) AS TEXT,BUN4_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN3_SHOHIN_CD = ? GROUP BY BUN4_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO4_BUN1 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN4_SHOHIN_CD AS VALUE, MAX(BUN4_SHOHIN_CD)||'_'||RTRIM(MAX(BUN4_SHOHIN_MEI)) AS TEXT,BUN4_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN1_SHOHIN_CD = ? GROUP BY BUN4_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO4_BUN2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN4_SHOHIN_CD AS VALUE, MAX(BUN4_SHOHIN_CD)||'_'||RTRIM(MAX(BUN4_SHOHIN_MEI)) AS TEXT,BUN4_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN2_SHOHIN_CD = ? GROUP BY BUN4_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO5 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN5_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN5_SHOHIN_MEI)) AS TEXT,BUN5_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN4_SHOHIN_CD = ? GROUP BY BUN5_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO5_BUN1 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN5_SHOHIN_CD AS VALUE, MAX(BUN5_SHOHIN_CD)||'_'||RTRIM(MAX(BUN5_SHOHIN_MEI)) AS TEXT,BUN5_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN1_SHOHIN_CD = ? GROUP BY BUN5_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO5_BUN2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN5_SHOHIN_CD AS VALUE, MAX(BUN5_SHOHIN_CD)||'_'||RTRIM(MAX(BUN5_SHOHIN_MEI)) AS TEXT,BUN5_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN2_SHOHIN_CD = ? GROUP BY BUN5_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO5_BUN3 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN5_SHOHIN_CD AS VALUE, MAX(BUN5_SHOHIN_CD)||'_'||RTRIM(MAX(BUN5_SHOHIN_MEI)) AS TEXT,BUN5_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN3_SHOHIN_CD = ? GROUP BY BUN5_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO6 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN6_SHOHIN_CD AS VALUE, RTRIM(MAX(BUN6_SHOHIN_MEI)) AS TEXT,BUN6_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN5_SHOHIN_CD = ? GROUP BY BUN6_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO6_BUN1 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN6_SHOHIN_CD AS VALUE, MAX(BUN6_SHOHIN_CD)||'_'||RTRIM(MAX(BUN6_SHOHIN_MEI)) AS TEXT,BUN6_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN1_SHOHIN_CD = ? GROUP BY BUN6_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO6_BUN2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN6_SHOHIN_CD AS VALUE, MAX(BUN6_SHOHIN_CD)||'_'||RTRIM(MAX(BUN6_SHOHIN_MEI)) AS TEXT,BUN6_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN2_SHOHIN_CD = ? GROUP BY BUN6_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO6_BUN3 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN6_SHOHIN_CD AS VALUE, MAX(BUN6_SHOHIN_CD)||'_'||RTRIM(MAX(BUN6_SHOHIN_MEI)) AS TEXT,BUN6_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN3_SHOHIN_CD = ? GROUP BY BUN6_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO6_BUN4 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BUN6_SHOHIN_CD AS VALUE, MAX(BUN6_SHOHIN_CD)||'_'||RTRIM(MAX(BUN6_SHOHIN_MEI)) AS TEXT,BUN6_SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN4_SHOHIN_CD = ? GROUP BY BUN6_SHOHIN_CD ORDER BY SORT";
  public static final String ID_SQL_INIT_T_SHO7 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT SHOHIN_CD AS VALUE, RTRIM(MAX(SHOHIN_MEI)) AS TEXT,SHOHIN_CD AS SORT FROM M_SHOHIN WHERE BUN6_SHOHIN_CD = ? GROUP BY SHOHIN_CD ORDER BY SORT";

  /** 店舗選択 */
  public static final String ID_SQL_INIT_TENPO = "SELECT TENPO_CD AS VALUE, RTRIM(MAX(TENPO_NM)) AS TEXT,TENPO_CD AS SORT FROM M_TENPO GROUP BY TENPO_CD ORDER BY SORT ";
  public static final String ID_SQL_INIT_TENPO_NO000 = "SELECT TENPO_CD AS VALUE, RTRIM(MAX(TENPO_NM)) AS TEXT,TENPO_CD AS SORT FROM M_TENPO WHERE TENPO_CD>'000' GROUP BY TENPO_CD ORDER BY SORT ";
  /** * 従販選択 */
  public static final String ID_SQL_INIT_JUHAN = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT CHANEL_CD3 AS VALUE, RTRIM(MAX(CHANEL_NM)) AS TEXT,CHANEL_CD3 AS SORT FROM M_CHANEL WHERE CHANEL_CD1 = '1' AND CHANEL_CD2 = '40' AND CHANEL_CD3 <> '000' GROUP BY CHANEL_CD3 ORDER BY SORT ";
  /** * 海外関係会社選択 */
  public static final String ID_SQL_INIT_KAIGAI = "SELECT DISTINCT CHANEL_CD2||CHANEL_CD3 AS VALUE, RTRIM(CHANEL_NM) AS TEXT, CHANEL_CD2||CHANEL_CD3 AS SORT FROM M_CHANEL WHERE CHANEL_CD1 = '2' AND CHANEL_CD3 <> '000' AND NOT CHANEL_CD3 IN ('096') AND CHANEL_CD4 = '00' ORDER BY SORT";
  // public static final String ID_SQL_INIT_KAIGAI = "SELECT DISTINCT CHANEL_CD2||CHANEL_CD3 AS VALUE,
  // RTRIM(CHANEL_NM) AS TEXT, CHANEL_CD2||CHANEL_CD3 AS SORT FROM M_CHANEL WHERE CHANEL_CD1 = '2' AND
  // CHANEL_CD3 <> '000' AND CHANEL_CD4 = '00' ORDER BY SORT";
  /** * 海外関係会社選択 */
  public static final String ID_SQL_INIT_KAIGAIb = "SELECT CHANEL_CD2||CHANEL_CD3 AS VALUE, RTRIM(MAX(CHANEL_NM)) AS TEXT,CHANEL_CD2||CHANEL_CD3 AS SORT FROM M_CHANEL WHERE CHANEL_CD1 = '2' AND CHANEL_CD2 = '20' AND CHANEL_CD3 <> '000' AND NOT CHANEL_CD3 IN ('096') AND CHANEL_CD4 = '00' GROUP BY CHANEL_CD2,CHANEL_CD3 ORDER BY SORT ";
  // public static final String ID_SQL_INIT_KAIGAIb = "SELECT CHANEL_CD2||CHANEL_CD3 AS VALUE,
  // RTRIM(MAX(CHANEL_NM)) AS TEXT,CHANEL_CD2||CHANEL_CD3 AS SORT FROM M_CHANEL WHERE CHANEL_CD1 = '2'
  // AND CHANEL_CD2 = '20' AND CHANEL_CD3 <> '000' AND CHANEL_CD4 = '00' GROUP BY
  // CHANEL_CD2,CHANEL_CD3 ORDER BY SORT ";
  /** * 輸出先選択 */
  public static final String ID_SQL_INIT_YUSHU = "SELECT CHANEL_CD3 AS VALUE, RTRIM(MAX(CHANEL_NM)) AS TEXT, CHANEL_CD3 AS SORT FROM M_CHANEL WHERE CHANEL_CD1 = '1' AND CHANEL_CD2 = '30' AND CHANEL_CD3 <> '000' AND NOT CHANEL_CD3 IN ('096') GROUP BY CHANEL_CD3 ORDER BY SORT";
  // public static final String ID_SQL_INIT_YUSHU = "SELECT CHANEL_CD3 AS VALUE, RTRIM(MAX(CHANEL_NM))
  // AS TEXT, CHANEL_CD3 AS SORT FROM M_CHANEL WHERE CHANEL_CD1 = '1' AND CHANEL_CD2 = '30' AND
  // CHANEL_CD3 <> '000' GROUP BY CHANEL_CD3 ORDER BY SORT";
  /** * 他社売上選択 SORT 固定指定あり */
  public static final String ID_SQL_INIT_ITAKU = "SELECT CHANEL_CD1 || CHANEL_CD2 AS VALUE, RTRIM(MAX(MEISYO2)) AS TEXT, CHAN_ORDER AS SORT FROM M_CHANEL2 WHERE KBN = 'A' AND CHANEL_CD1 IN ('50', '70') GROUP BY CHANEL_CD1, CHANEL_CD2, CHAN_ORDER ORDER BY CHAN_ORDER DESC";
  /** * 他ブランド選択 */
  public static final String ID_SQL_INIT_BRAND = "SELECT CHANEL_CD3 AS VALUE, RTRIM(MAX(CHANEL_NM)) AS TEXT, CHANEL_CD3 AS SORT FROM M_CHANEL WHERE CHANEL_CD1 = '1' AND CHANEL_CD2 = '60' AND CHANEL_CD3 <> '000' GROUP BY CHANEL_CD3 ORDER BY SORT";

  /** * 回数選択 */
  public static final String ID_SQL_INIT_KAISU = "SELECT 0 AS VALUE, 'すべて' AS TEXT,0 AS SORT FROM dual UNION ALL SELECT JUCKAISU AS VALUE, RTRIM(MAX(JUCKAISU_NM)) AS TEXT,JUCKAISU AS SORT FROM M_JUCKAISU GROUP BY JUCKAISU ORDER BY SORT ";
  /** * 受注形態選択 */
  public static final String ID_SQL_INIT_JUCHU = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT JUC_D_KBN AS VALUE, RTRIM(MAX(JUC_D_KBN_NM)) AS TEXT,JUC_D_KBN AS SORT FROM M_JUC_KBN GROUP BY JUC_D_KBN ORDER BY SORT ";
  /** * 年齢選択 */
  public static final String ID_SQL_INIT_AGE = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT SEDAI_CD AS VALUE, RTRIM(MAX(SEDAI_MEI)) AS TEXT,SEDAI_CD AS SORT FROM M_AGE GROUP BY SEDAI_CD ORDER BY SORT ";
  /** * 地域選択 */
  public static final String ID_SQL_INIT_AREA = "SELECT '00' AS VALUE, '全国' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT CHIKU_CD AS VALUE, RTRIM(MAX(CHIKU_MEI)) AS TEXT,CHIKU_CD AS SORT FROM M_AREA GROUP BY CHIKU_CD ORDER BY SORT ";
  public static final String ID_SQL_INIT_AREA_ONLY = "SELECT CHIKU_CD AS VALUE, RTRIM(MAX(CHIKU_MEI)) AS TEXT, CHIKU_CD AS SORT FROM M_AREA GROUP BY CHIKU_CD ORDER BY SORT ";
  public static final String ID_SQL_INIT_AREA_JAPAN = "SELECT CHIKU_CD AS VALUE, RTRIM(MAX(CHIKU_MEI)) AS TEXT, CHIKU_CD AS SORT FROM M_AREA WHERE CHIKU_CD<>'48' GROUP BY CHIKU_CD ORDER BY SORT";
  /** * 媒体 */
  public static final String ID_SQL_INIT_BYTAI = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BYTAI1_CD AS VALUE, BYTAI1_CD||'_'||RTRIM(MAX(BYTAI1_MEI)) AS TEXT,BYTAI1_CD AS SORT FROM M_PROMO_ALL GROUP BY BYTAI1_CD ORDER BY SORT ";
  /** * 媒体種類 */
  public static final String ID_SQL_INIT_BYTAI2 =
      // "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT distinct
      // RTRIM(PROMO_CD) AS VALUE, RTRIM(PROMO_CD)||'_'||HAKKOBI||' '||PROMO_MEI AS TEXT,RTRIM(PROMO_CD)
      // AS SORT FROM M_PROMO_ALL WHERE BYTAI1_CD = ? ORDER BY SORT ";
      "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT BYTAI2_CD AS VALUE, BYTAI2_CD||'_'||RTRIM(MAX(BYTAI2_MEI)) AS TEXT,BYTAI2_CD AS SORT FROM M_PROMO_ALL WHERE BYTAI1_CD = ? GROUP BY BYTAI2_CD ORDER BY SORT ";
  /** * 初回商品g */
  public static final String ID_SQL_INIT_SHOG = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT TRIM(MAX(SHOHIN_GRP_CD)) AS VALUE, RTRIM(MAX(BUN_SHOHIN_MEI)) AS TEXT,LEVEL_CTG AS SORT FROM M_SHOHIN_GRP_CMB GROUP BY LEVEL_CTG ORDER BY SORT ";
  /** * 初回商品g */
  public static final String ID_SQL_SEL_SHOG = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT TRIM(MAX(BUN4_SHOHIN_GRP_CD)) AS VALUE, RTRIM(MAX(BUN4_SHOHIN_GRP_MEI))||'　詳細' AS TEXT,BUN4_SHOHIN_GRP_CD AS SORT FROM M_SHOHIN_GRP where (BUN4_SHOHIN_GRP_CD = '10101010' or BUN4_SHOHIN_GRP_CD = '10104010') GROUP BY BUN4_SHOHIN_GRP_CD ORDER BY SORT ";

  /** チャネル選択 */
  public static final String ID_SQL_INIT_CHANELA = "SELECT '00' AS VALUE, 'すべて' AS TEXT, 00 AS SORT FROM DUAL UNION ALL SELECT MIN(CHANEL_CD1) AS VALUE, MEISYO1 AS TEXT, TO_NUMBER(MIN(CHANEL_CD1)) AS SORT FROM M_CHANEL2 WHERE KBN = 'A' GROUP BY MEISYO1 ORDER BY SORT";
  public static final String ID_SQL_INIT_CHANELB = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD2 AS VALUE, MAX(MEISYO2) AS TEXT,TO_NUMBER(CHANEL_CD2) AS SORT FROM M_CHANEL2  where KBN = 'A' AND CHANEL_CD1 = ? GROUP BY CHANEL_CD2 ORDER BY SORT  ";
  public static final String ID_SQL_INIT_CHANELB2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD2 AS VALUE, MAX(MEISYO2) AS TEXT,TO_NUMBER(CHANEL_CD2) AS SORT FROM M_CHANEL2  where KBN = 'T' AND CHANEL_CD1 = ? GROUP BY CHANEL_CD2 ORDER BY SORT  ";
  public static final String ID_SQL_INIT_CHANELB3 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD2 AS VALUE, MAX(MEISYO2) AS TEXT,TO_NUMBER(CHANEL_CD2) AS SORT FROM M_CHANEL2  where KBN = 'A' AND CHANEL_CD1 = ? GROUP BY CHANEL_CD2 ORDER BY SORT  ";
  public static final String ID_SQL_INIT_CHANELB4 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD2 AS VALUE, MAX(MEISYO2) AS TEXT,TO_NUMBER(CHANEL_CD2) AS SORT FROM M_CHANEL2  where KBN = 'T' AND CHANEL_CD1 = ? GROUP BY CHANEL_CD2 ORDER BY SORT  ";
  public static final String ID_SQL_INIT_CHANELB5 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD4 AS VALUE, MAX(MEISYO4) AS TEXT,TO_NUMBER(CHANEL_CD4) AS SORT FROM M_CHANEL2  where KBN = 'A' AND CHANEL_CD1 = ? GROUP BY CHANEL_CD4 ORDER BY SORT  ";
  // 基本分類=50 他社売上の場合
  public static final String ID_SQL_INIT_CHANELB_50 = "SELECT '00' AS VALUE, 'すべて' AS TEXT, 900000000 AS SORT FROM DUAL UNION ALL SELECT CHANEL_CD1 || CHANEL_CD2 AS VALUE, MAX(MEISYO2) AS TEXT, TO_NUMBER(MAX(CHAN_ORDER)) AS SORT FROM M_CHANEL2 WHERE KBN = 'A' AND CHANEL_CD1 IN (? , '70') GROUP BY CHANEL_CD1, CHANEL_CD2 ORDER BY SORT DESC";
  public static final String ID_SQL_INIT_CHANELC = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD3 AS VALUE, MAX(MEISYO3) AS TEXT,TO_NUMBER(CHANEL_CD3) AS SORT FROM M_CHANEL2  where KBN = 'A' AND CHANEL_CD1 || CHANEL_CD2 = ? GROUP BY CHANEL_CD3 ORDER BY SORT  ";
  public static final String ID_SQL_INIT_CHANELC2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD3 AS VALUE, MAX(MEISYO3) AS TEXT,TO_NUMBER(CHANEL_CD3) AS SORT FROM M_CHANEL2  where KBN = 'T' AND CHANEL_CD1 || CHANEL_CD2 = ? GROUP BY CHANEL_CD3 ORDER BY SORT  ";
  public static final String ID_SQL_INIT_CHANELC3 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD4 AS VALUE, MAX(MEISYO4) AS TEXT,TO_NUMBER(CHANEL_CD4) AS SORT FROM M_CHANEL2  where KBN = 'A' AND CHANEL_CD1 || CHANEL_CD2 = ? GROUP BY CHANEL_CD4 ORDER BY SORT  ";
  public static final String ID_SQL_INIT_CHANELC4 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD4 AS VALUE, MAX(MEISYO4) AS TEXT,TO_NUMBER(CHANEL_CD4) AS SORT FROM M_CHANEL2  where KBN = 'T' AND CHANEL_CD1 || CHANEL_CD2 = ? GROUP BY CHANEL_CD4 ORDER BY SORT  ";
  public static final String ID_SQL_INIT_CHANELD = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD4 AS VALUE, MAX(MEISYO4) AS TEXT,TO_NUMBER(CHANEL_CD4) AS SORT FROM M_CHANEL2  where KBN = 'A' AND CHANEL_CD1 || CHANEL_CD2 || CHANEL_CD3 = ? GROUP BY CHANEL_CD4 ORDER BY SORT ";
  public static final String ID_SQL_INIT_CHANELD2 = "SELECT '00' AS VALUE, 'すべて' AS TEXT,00 AS SORT FROM dual UNION ALL SELECT CHANEL_CD4 AS VALUE, MAX(MEISYO4) AS TEXT,TO_NUMBER(CHANEL_CD4) AS SORT FROM M_CHANEL2  where KBN = 'T' AND CHANEL_CD1 || CHANEL_CD2 || CHANEL_CD3 = ? GROUP BY CHANEL_CD4 ORDER BY SORT ";

  /** 通販受注選択予測数表示 */
  public static final String ID_SQL_INIT_TSU_JUC = "SELECT '計上時間；'||SUBSTR(URI_YMD,1,4)||'-'||SUBSTR(URI_YMD,5,2)||'-'||SUBSTR(URI_YMD,7,2)||':'||URI_YMD_TIME|| '　電話受注数；' || JUCSU||',　　最終予想数；' || YOSO_SU  AS MSG FROM URI_TSU_YOSO ";

  /** 韓国RFM顧客数 */
  /** 基準年月[YYYYMM] */
  public static final String ID_SQL_INIT_YM_KIJUN_KOR = "select distinct KIJUN_YM as VALUE, KIJUN_YM_MEI as TEXT, TO_NUMBER(KIJUN_YM) as SORT from KOR_YM_KIJUN_K01 ORDER BY SORT DESC";
  /** * 販売チャネル */
  public static final String ID_SQL_INIT_CHANEL_KOR = "SELECT CHANEL_CD as VALUE, CHANEL_MEI as TEXT,CHANEL_ORDER as SORT from KOR_M_CHANEL ORDER BY SORT ";
  /** * 年齢選択 */
  public static final String ID_SQL_INIT_AGE_KOR = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT SEDAI_CD AS VALUE, RTRIM(MAX(SEDAI_MEI)) AS TEXT,SEDAI_CD AS SORT FROM KOR_M_AGE GROUP BY SEDAI_CD ORDER BY SORT ";
  /** * 地域選択 */
  public static final String ID_SQL_INIT_AREA_KOR = "SELECT '00' AS VALUE, '全国' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT CHIKU_CD AS VALUE,CASE WHEN SUBSTR(CHIKU_G,5,1) = '0' THEN '　' || CHIKU_CD || '_' ||SUBSTR(CHIKU_MEI,1,45) ELSE CHIKU_CD || '_' ||SUBSTR(CHIKU_MEI,1,45) END  AS TEXT,CHIKU_CD  AS SORT from KOR_M_AREA UNION ALL  SELECT CHIKU_G AS VALUE,SUBSTR(CHIKU_G,1,2) ||'_'||CHIKU_G_MEI as TEXT,CHIKU_G AS SORT FROM KOR_M_AREA  WHERE SUBSTR(CHIKU_G,5,1) = '0' GROUP BY CHIKU_G_MEI,CHIKU_G ORDER BY SORT ";
  /** * 会員区分選択 */
  public static final String ID_SQL_INIT_KAIIN_KOR = "SELECT '00' AS VALUE, 'すべて' AS TEXT,'00' AS SORT FROM dual UNION ALL SELECT KAIIN_KBN_CD as VALUE, KAIIN_KBN_MEI as TEXT,KAIIN_KBN_CD as SORT from KOR_M_KAIIN_KBN ORDER BY SORT ";

  /**
   * SYSLOG（SELECT）<br/>
   * ログ情報の期間取得<br/>
   * {@value}
   */
  public static final String ID_SQL_SELECT_SYSLOGS = "select to_char(T1.MIN_ACTION, 'YYYY') as MIN_YEAR,to_char(T1.MIN_ACTION, 'MM') as MIN_MONTH,to_char(T1.MIN_ACTION, 'DD') as MIN_DAY,to_char(T1.MAX_ACTION, 'YYYY') as MIN_YEAR,to_char(T1.MAX_ACTION, 'MM') as MIN_MONTH,to_char(T1.MAX_ACTION, 'DD') as MIN_DAY from (select min(DT_ACTION) as MIN_ACTION,max(DT_ACTION) as MAX_ACTION from AK_SYS_LOGS) T1";
  /**
   * SYSLOG（INSERT）<br/>
   * ログ情報の登録<br/>
   * {@value}
   */
  public static final String ID_SQL_INSERT_SYSLOGS = "insert into AK_SYS_LOGS(USER_ID,DT_ACTION,CD_ACTION,REMARK,ID_USER,ID_REPORT,NM_USER) values(?,current_timestamp,?,?,?,?,?)";

  /**
   * SYS_REPORT_CHECK（SELECT）<br/>
   * レポートチェックより更新フラグ、メッセージを取得<br/>
   * {@value}
   */
  public static final String ID_SQL_REPORT_CHECK = "SELECT UPDATE_FLG, CASE WHEN UPDATE_FLG = '0' THEN TO_CHAR(UPDATE_DT, '\"更新：\"YYYY\"年\"MM\"月\"DD\"日\" HH24\"時\"MI\"分\"') ELSE '更新作業中' END AS MSG FROM AK_SYS_REPORT_CHECK WHERE NM_REPORT_JSP = ";


}
