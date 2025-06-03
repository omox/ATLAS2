package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.Defines;
import common.ItemList;
import common.JsonArrayData;
import net.sf.json.JSONArray;

/**
 * B01bテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B01bDao extends B01Dao {

	public static final String	ID_SQL_SEL_SAVE	= "SELECT * FROM AK_SAVE_B01b T1 WHERE T1.USER_ID = ?";

	/**
	 * 新しいB01bgDaoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public B01bDao(Connection source) {
		super(source);
	}

	/**
	 * 検索実行
	 *
	 * @return boolean
	 */
	public boolean selectBy() {

		// 検索コマンド生成
		String command = super.createCommand();

		// 売上目標検索実行
		this.setOptionUriMok();

		// 出力用検索条件生成
		outputQueryList();

		// 検索実行
		return super.selectBySQL(command);
	}

	/**
	 * 売上目標を取得し、Settingに設定、JsonGridのオプション情報として扱う。
	 *
	 */
	public void setOptionUriMok() {
		String uriMok = "";
		//円の場合のみ実行（売上目標が円のため）
		if (StringUtils.equals(getMap().get("RAD_B9"), DefineReport.RadB_9.ID2.getVal())) {
			JSONArray data = super.selectUriMok();
			if (!data.getJSONObject(0).isEmpty()) {
				uriMok = data.getJSONObject(0).getString("売上目標");
			}
		}
		HashMap<String, String> t01Setting = new HashMap<String, String>();
		t01Setting.put("売上目標", uriMok);
		setSetting(t01Setting);
	}

	/**
	 * 検索条件表示用情報の生成
	 */
	private void outputQueryList() {

		// 検索条件の加工クラス作成
		JsonArrayData jad = new JsonArrayData();
		jad.setJsonString(getJson());

		// 保存用 List (検索情報)作成
		setWhere(new ArrayList<List<String>>());

		String radB1 = jad.getJSONValue(DefineReport.RadB_1.NAME.getObj());
		List<String> cells = new ArrayList<String>();
		// 照会データ指定
		cells.add(DefineReport.RadB_1.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_1.NAME.getObj()));
		getWhere().add(cells);
		// 年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELB2.getView());
		if (StringUtils.equals(radB1, DefineReport.RadB_1.ID1.getObj())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()));
		} else if (StringUtils.equals(radB1, DefineReport.RadB_1.ID2.getObj())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN_TO.getObj()));
		} else {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
		}
		getWhere().add(cells);

		// 表示対象
		cells = new ArrayList<String>();
		cells.add(DefineReport.RadB_9.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_9.NAME.getObj()));
		getWhere().add(cells);
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.KANREN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.KANREN.getObj()));
		getWhere().add(cells);

		cells = new ArrayList<String>();
		cells.add(StringUtils.replace(DefineReport.Label.YOSAN.getView(), "<br/>", ""));
		cells.add(convFormat(getSetting().get("売上目標"), "#,##0"));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

	/**
	 * 検索条件を保管する。
	 *
	 * @param map 画面項目情報
	 */
	public static void saveCondition(HashMap<String, String> map) {
		ItemList il = new ItemList();
		try {
			il.executeItem(getSqlSave(map), new ArrayList<String>(), Defines.STR_JNDI_DS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 検索条件保管SQLを生成します。
	 *
	 * @param map 画面項目情報
	 * @return 検索条件保管SQL文
	 */
	private static String getSqlSave(HashMap<String, String> map) {
		String userId = map.get("USER_ID");				// ユーザーID
		String szYy1 = map.get("YY_KIJUN");				// 開始年
		String szYy2 = map.get("YY_KIJUN_TO");			// 終了年
		String szMm1 = map.get("MM_KIJUN");				// 開始月
		String szMm2 = map.get("MM_KIJUN_TO");			// 終了月
		String szTenpo = "";				// 店舗
		String szRad_B1 = map.get("RAD_B1");			// 照会データ指定-1:年別 2:月別 3:日別
		String szRad_B2 = map.get("RAD_B2");			// 対比表示-1:前年比 2:前月比
		String szRad_B9 = map.get("RAD_B9");			// 外貨表示選択-1:外貨 2:円　　　※B01bのみ
		String szChk_B1 = "";		// チャネル区分-通販
		String szChk_B2 = "";		// チャネル区分-店舗
		String szChk_B3 = "";		// チャネル区分-輸出
		String szChk_B4 = "";		// チャネル区分-従販
		String szChk_B5 = "";		// チャネル区分-他社売上
		String szChk_B6 = "";		// チャネル区分-他ブランド
		String szYushutu = map.get("YUSHUTU");			// 輸出コード
		String szYushutuNm = map.get("YUSHUTU_NM");		// 輸出名称
		String szBrand = map.get("BRAND");				// 他ブランドコード
		String szBrandNm = map.get("BRAND_NM");			// 他ブランド名称
		String szKaigai = map.get("KAIGAI");			// 関連会社コード
		String szKaigaiNm = map.get("KAIGAI_NM");		// 関連会社名称
		String szChk_Chanel1 = "";		//
		String szChk_Chanel2 = "";		//
		String szJuhan = "";			// 従販コード
		String szJuhanNm = "";			// 従販名称

		String szItemJun = map.get("ITEM_JUN");			// 表示項目-純売上
		String szItemShu = map.get("ITEM_SHU");			// 表示項目-出荷売上

		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("MERGE INTO AK_SAVE_B01b ");
		sbSQL.append("USING (SELECT '" + userId + "' as USER_ID FROM DUAL) WK ON (AK_SAVE_B01b.USER_ID = WK.USER_ID) ");
		// 既存レコードの更新
		sbSQL.append("WHEN MATCHED THEN UPDATE SET");
		sbSQL.append(" START_YY_SEL = '" + szYy1 + "',");				// 開始年
		sbSQL.append(" START_MM_SEL = '" + szMm1 + "',");				// 開始月
		sbSQL.append(" END_YY_SEL = '" + szYy2 + "',");					// 終了年
		sbSQL.append(" END_MM_SEL = '" + szMm2 + "',");					// 終了月
		sbSQL.append(" TENPO_SEL = '" + szTenpo + "',");				// 店舗
		sbSQL.append(" REF_DATA_RAD = '" + szRad_B1 + "',");			// 照会データ指定-1:年別 2:月別 3:日別
		sbSQL.append(" VIEW_CMP_RAD = '" + szRad_B2 + "',");			// 対比表示-1:前年比 2:前月比
		sbSQL.append(" VIEW_OBJ_RAD = '" + szRad_B9 + "',");			// 表示対象-1:オルビス 2:関連会社 3:連結
		sbSQL.append(" CHANEL_TSUHAN_CHK = '" + szChk_B1 + "',");		// チャネル区分-通販
		sbSQL.append(" CHANEL_TENPO_CHK = '" + szChk_B2 + "',");		// チャネル区分-店舗
		sbSQL.append(" CHANEL_YUSHUTU_CHK = '" + szChk_B3 + "',");		// チャネル区分-輸出
		sbSQL.append(" CHANEL_JUHAN_CHK = '" + szChk_B4 + "',");		// チャネル区分-従販
		sbSQL.append(" CHANEL_TASYAURI_CHK = '" + szChk_B5 + "',");		// チャネル区分-他社売上
		sbSQL.append(" CHANEL_TABRAND_CHK = '" + szChk_B6 + "',");		// チャネル区分-他ブランド
		sbSQL.append(" CD_YUSHUTU_DETAIL = '" + szYushutu + "',");		// 輸出コード
		sbSQL.append(" NM_YUSHUTU_DETAIL = '" + szYushutuNm + "',");		// 輸出名称
		sbSQL.append(" CD_TABRAND_DETAIL = '" + szBrand + "',");		// 他ブランドコード
		sbSQL.append(" NM_TABRAND_DETAIL = '" + szBrandNm + "',");		// 他ブランド名称
		sbSQL.append(" CD_KANREN_DETAIL = '" + szKaigai + "',");		// 関連会社コード
		sbSQL.append(" NM_KANREN_DETAIL = '" + szKaigaiNm + "',");		// 関連会社名称
		sbSQL.append(" VIEW_ITEM_JUN = '" + szItemJun + "',");			// 表示項目-純売上
		sbSQL.append(" VIEW_ITEM_SHU = '" + szItemShu + "',");			// 表示項目-出荷売上
		sbSQL.append(" CHANEL_KANREN1_CHK = '" + szChk_Chanel1 + "',");	//
		sbSQL.append(" CHANEL_KANREN2_CHK = '" + szChk_Chanel2 + "',");	//
		sbSQL.append(" CD_JUHAN_DETAIL = '" + szJuhan + "',");			// 従販コード
		sbSQL.append(" NM_JUHAN_DETAIL = '" + szJuhanNm + "' ");		// 従販名称
		// -- 新規レコードの作成
		sbSQL.append("WHEN NOT MATCHED THEN INSERT VALUES (");
		sbSQL.append(" '" + userId + "',");			// ユーザーID
		sbSQL.append(" '" + szYy1 + "',");			// 開始年
		sbSQL.append(" '" + szMm1 + "',");			// 開始月
		sbSQL.append(" '" + szYy2 + "',");			// 終了年
		sbSQL.append(" '" + szMm2 + "',");			// 終了月
		sbSQL.append(" '" + szTenpo + "',");		// 店舗
		sbSQL.append(" '" + szRad_B1 + "',");		// 照会データ指定-1:年別 2:月別 3:日別
		sbSQL.append(" '" + szRad_B2 + "',");		// 対比表示-1:前年比 2:前月比
		sbSQL.append(" '" + szRad_B9 + "',");		// 表示対象-1:オルビス 2:関連会社 3:連結
		sbSQL.append(" '" + szChk_B1 + "',");		// チャネル区分-通販
		sbSQL.append(" '" + szChk_B2 + "',");		// チャネル区分-店舗
		sbSQL.append(" '" + szChk_B3 + "',");		// チャネル区分-輸出
		sbSQL.append(" '" + szChk_B4 + "',");		// チャネル区分-従販
		sbSQL.append(" '" + szChk_B5 + "',");		// チャネル区分-他社売上
		sbSQL.append(" '" + szChk_B6 + "',");		// チャネル区分-他ブランド
		sbSQL.append(" '" + szYushutu + "',");		// 輸出コード
		sbSQL.append(" '" + szYushutuNm + "',");		// 輸出名称
		sbSQL.append(" '" + szBrand + "',");		// 他ブランドコード
		sbSQL.append(" '" + szBrandNm + "',");		// 他ブランド名称
		sbSQL.append(" '" + szKaigai + "',");		// 関連会社コード
		sbSQL.append(" '" + szKaigaiNm + "',");		// 関連会社名称
		sbSQL.append(" '" + szItemJun + "',");		// 表示項目-純売上
		sbSQL.append(" '" + szItemShu + "',");		// 表示項目-出荷売上
		sbSQL.append(" '" + szChk_Chanel1 + "',");	//
		sbSQL.append(" '" + szChk_Chanel2 + "',");	//
		sbSQL.append(" '" + szJuhan + "',");		// 従販コード
		sbSQL.append(" '" + szJuhanNm + "')");		// 従販名称

		System.out.println("保管SQL b:" + sbSQL.toString());
		return sbSQL.toString();
	}
}
