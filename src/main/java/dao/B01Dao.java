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
 * B01 uri_jテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B01Dao extends ItemDao {

	public static final String ID_SQL_SEL_SAVE = "SELECT * FROM AK_SAVE_B01 T1 WHERE T1.USER_ID = ?";

	/**
	 * 新しいB01Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public B01Dao(Connection source) {
		super(source);
	}

	/**
	 * 検索実行
	 *
	 * @return boolean
	 */
	public boolean selectBy() {

		// 検索コマンド生成
		String command = createCommand();

		// 売上目標検索実行
		this.setOptionUriMok();

		// 出力用検索条件生成
		this.outputQueryList();

		// 検索実行
		return super.selectBySQL(command);
	}

	/**
	 * 売上目標を取得し、Settingに設定、JsonGridのオプション情報として扱う。
	 *
	 */
	public void setOptionUriMok() {
		JSONArray data = selectUriMok();
		// 取得した売上目標を、JSONGRIDのオプションに設定するため、Settingにセット
		HashMap<String, String> t01Setting = new HashMap<String, String>();
		String uriMok = "";
		if (!data.getJSONObject(0).isEmpty()) {
			uriMok = data.getJSONObject(0).getString("売上目標");
		}
		t01Setting.put("売上目標", uriMok);
		setSetting(t01Setting);
	}

	/**
	 * 売上目標を取得する。
	 *
	 * @return 売上目標
	 */
	public JSONArray selectUriMok() {
		ItemList il = new ItemList();
		JSONArray data = new JSONArray();
		try {
			data = il.selectJSONArray(createCommandUriMok(), new ArrayList<String>(), Defines.STR_JNDI_DS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 売上目標取得SQLを生成します。
	 *
	 * @return 売上目標取得SQL文
	 */
	private String createCommandUriMok() {
		String szJiseki = getMap().get("JISEKI");	// 押下ボタン-1:純売上 2:出荷売上

		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("select ");
		if (StringUtils.equals(szJiseki, DefineReport.Item.ITEM_JUN.getVal())) {
			sbSQL.append("sum(JUN_GK) as 売上目標 ");
		} else {
			sbSQL.append("sum(SHU_GK) as 売上目標 ");
		}
		sbSQL.append(getFromUriMok());
		sbSQL.append("where " + getWhereDate()[0] + getWhereChanel()[1]);

		System.out.println("売上目標取得SQL:" + sbSQL.toString());
		return sbSQL.toString();
	}

	/**
	 * 検索文を作成します
	 *
	 * @return 検索<String>
	 */
	protected String createCommand() {
		String szJiseki = getMap().get("JISEKI");		// 押下ボタン-1:純売上 2:出荷売上
		String szRad_B1 = getMap().get("RAD_B1");		// 照会データ指定-1:年別 2:月別 3:日別
		String szRad_B2 = getMap().get("RAD_B2");		// 対比表示-1:前年比 2:前月比
		String szRad_B3 = getMap().get("RAD_B3");		// 表示対象-1:オルビス 2:関連会社 3:連結
		String szRad_B9 = getMap().get("RAD_B9");		// 外貨表示選択-1:外貨 2:円　　　※B01bのみ
		String szItemJun = getMap().get("ITEM_JUN");	// 表示項目-純売上
		String szItemShu = getMap().get("ITEM_SHU");	// 表示項目-出荷売上


		// パラメータ確認
		if ((szRad_B1 == null) || (szRad_B2 == null) || (szRad_B3 == null)) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		// 検索条件の加工クラス作成
		JsonArrayData jad = new JsonArrayData();
		jad.setJsonString(getJson());
		boolean nullClass = (StringUtils.equals(jad.getJSONValue(DefineReport.CheckB_1.ID3.getObj()), "on")) || (StringUtils.equals(jad.getJSONValue(DefineReport.CheckB_1.ID5.getObj()), "on"));

		// 共通item
		// 押下ボタン-1:純売上 2:出荷売上
		String sumMokGk = "";
		String sumUriGK = "";
		String sumZMMUriGK = "";
		String sumZYYUriGK = "";
		String[] items;
		if (StringUtils.equals(szJiseki, DefineReport.Item.ITEM_JUN.getVal())) {
			System.out.println(szRad_B9);
			sumMokGk += "sum(JUN_GK) ";
			if (szRad_B9==null){
				sumUriGK += "sum(JUN_URIGK * COALESCE(KANZANTI,1)) ";
				sumZMMUriGK += "sum(ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1)) ";
				sumZYYUriGK += "sum(ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1)) ";
				items = getSelectJunItem(StringUtils.split(szItemJun, ","), szRad_B1, szRad_B2, szRad_B3, nullClass);
			}else{
				// 外貨表示実績
				if (StringUtils.equals(szRad_B9, DefineReport.RadB_9.ID1.getVal())) {	// B01b用
					sumUriGK += "sum(JUN_URIGK / COALESCE(KANZANTI,1)) ";
					sumZMMUriGK += "sum(ZMM_JUN_URIGK / COALESCE(ZMM_KANZANTI,1)) ";
					sumZYYUriGK += "sum(ZYY_JUN_URIGK / COALESCE(ZYY_KANZANTI,1)) ";
					items = getSelectJunItemDef(StringUtils.split(szItemJun, ","), szRad_B1, szRad_B2, szRad_B3);
				} else {
					sumUriGK += "sum(JUN_URIGK * COALESCE(KANZANTI,1)) ";
					sumZMMUriGK += "sum(ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1)) ";
					sumZYYUriGK += "sum(ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1)) ";
					items = getSelectJunItemDef(StringUtils.split(szItemJun, ","), szRad_B1, szRad_B2, szRad_B3);
				}
			}
		} else {
			sumMokGk += "sum(SHU_GK) ";
			if (szRad_B9==null){
				sumUriGK += "sum(SHU_URIGK * COALESCE(KANZANTI,1)) ";
				sumZMMUriGK += "sum(ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1)) ";
				sumZYYUriGK += "sum(ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1)) ";
				items = getSelectShuItem(StringUtils.split(szItemShu, ","), szRad_B1, szRad_B2, szRad_B3, nullClass);
			}else{
				// 外貨表示実績
				if (StringUtils.equals(szRad_B9, DefineReport.RadB_9.ID1.getVal())) {	// B01b用
					sumUriGK += "sum(SHU_URIGK / COALESCE(KANZANTI,1)) ";
					sumZMMUriGK += "sum(ZMM_SHU_URIGK / COALESCE(ZMM_KANZANTI,1)) ";
					sumZYYUriGK += "sum(ZYY_SHU_URIGK / COALESCE(ZYY_KANZANTI,1)) ";
					items = getSelectShuItemDef(StringUtils.split(szItemShu, ","), szRad_B1, szRad_B2, szRad_B3);
				} else {
					sumUriGK += "sum(SHU_URIGK * COALESCE(KANZANTI,1)) ";
					sumZMMUriGK += "sum(ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1)) ";
					sumZYYUriGK += "sum(ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1)) ";
					items = getSelectShuItemDef(StringUtils.split(szItemShu, ","), szRad_B1, szRad_B2, szRad_B3);
				}
			}
		}
		String kanzanti01 = "M01.KAWASE/(1 + (M01.RITSU/100)) ";
		String kanzanti02 = "M02.KAWASE/(1 + (M02.RITSU/100)) ";
		if (StringUtils.equals(szRad_B9, DefineReport.RadB_9.ID1.getVal())) {	// B01b用
			kanzanti01 = "1 + (M01.RITSU/100) ";
			kanzanti02 = "1 + (M02.RITSU/100) ";
		}

		// 共通From句
		String mokFrom = getFromUriMok();
		// 共通Where句
		String sqlChaW1 = "M01.CHANEL_CD1 = M02.CHANEL_CD1 and M01.CHANEL_CD2 = M02.CHANEL_CD2 and M01.CHANEL_CD3 = M02.CHANEL_CD3 and M01.CHANEL_CD4 = M02.CHANEL_CD4";
		String sqlChaW2 = "U.CHANEL_CD1 = KCHANEL_CD1 and U.CHANEL_CD2 = KCHANEL_CD2 and U.CHANEL_CD3 = KCHANEL_CD3";
		String sqlChaW3 = "US.CHANEL_CD1 = KCHANEL_CD1 and US.CHANEL_CD2 = KCHANEL_CD2 and US.CHANEL_CD3 = KCHANEL_CD3";
		String[] sqlCha = getWhereChanel();
		String[] sqlDate = getWhereDate();

		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		if (StringUtils.equals(szRad_B1, "3")) {  //日別
			if (StringUtils.equals(szRad_B2, DefineReport.RadB_2.ID1.getVal())) {	//対比表示前年比の場合
				sbSQL.append("WITH WORK1(URI_YMD,ZYY_URI_YMD,WEEK) as (");
				sbSQL.append(" select U.URI_YMD, U.ZYY_URI_YMD, C.WEEK from URI_J U left outer join M_CALENDAR C on (U.URI_YMD = C.YMD_CD)");
				sbSQL.append(" where" + sqlDate[1] + " and ZYY_URI_YMD <> '00000000' group by URI_YMD,ZYY_URI_YMD,WEEK),   ");
				sbSQL.append("T_WEEK(WURI_YMD,WEEK,ZYY_WEEK) as (");
				sbSQL.append(" select W.URI_YMD, W.WEEK, C.WEEK from WORK1 W left outer join M_CALENDAR C on (W.ZYY_URI_YMD = C.YMD_CD)");
				sbSQL.append(" group by W.URI_YMD,W.WEEK,C.WEEK),   ");
			} else {	// 対比表示前月比の場合
				sbSQL.append("with WORK1(URI_YMD,ZMM_URI_YMD,WEEK) as (");
				sbSQL.append(" select U.URI_YMD, U.ZMM_URI_YMD, C.WEEK from URI_J U left outer join M_CALENDAR C on (U.URI_YMD = C.YMD_CD)");
				sbSQL.append(" where " + sqlDate[1] + "and ZMM_URI_YMD <> '00000000' group by URI_YMD,ZMM_URI_YMD,WEEK),   ");
				sbSQL.append("T_WEEK(WURI_YMD,WEEK,ZMM_WEEK) as (");
				sbSQL.append(" select W.URI_YMD, W.WEEK, C.WEEK from WORK1 W left outer join M_CALENDAR C on(W.ZMM_URI_YMD = C.YMD_CD)");
				sbSQL.append(" group by W.URI_YMD,W.WEEK,C.WEEK),   ");
			}
			sbSQL.append("T_URIMOK(MOK_DATE,MOK_URI_GK) as (");
			sbSQL.append(" select UM.URI_YM," + sumMokGk + mokFrom + " where " + sqlDate[0] + sqlCha[1] + " group by URI_YM ),   ");
			sbSQL.append("T_URIMOK_GK(MOK_URI_GK) as (");
			sbSQL.append(" select " + sumMokGk + mokFrom + " where " + sqlDate[0] + sqlCha[1] + "),   ");
			sbSQL.append("WKAWASE(YM,KCHANEL_CD1,KCHANEL_CD2,KCHANEL_CD3,KCHANEL_CD4,KANZANTI,ZYY_KANZANTI,ZMM_KANZANTI) as (");
			sbSQL.append(" select YM,CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4," + kanzanti01);
			sbSQL.append("  ,(select " + kanzanti02 + " from M_KAWASE M02");
			sbSQL.append("    where " + sqlChaW1 + " and M02.CHANEL_CD1 = '2' and M02.YM = to_char(add_months(to_date(M01.YM||'01','YYYY-MM-DD'),-12),'YYYYMM'))");
			sbSQL.append("  ,(select " + kanzanti02 + " from M_KAWASE M02");
			sbSQL.append("    where " + sqlChaW1 + " and M02.CHANEL_CD1 = '2' and M02.YM = to_char(add_months(to_date(M01.YM||'01','YYYY-MM-DD'),-1),'YYYYMM'))");
			sbSQL.append(" from M_KAWASE M01 where M01.CHANEL_CD1 = '2')   ");
			sbSQL.append("select distinct '' as SORT ,'合計' as \"日付\" " + items[1]);
			sbSQL.append(" from URI_J U inner join T_WEEK on (U.URI_YMD = WURI_YMD)");
			sbSQL.append("  left outer join WKAWASE MK on (substr(U.URI_YMD,1,6) = MK.YM and " + sqlChaW2 + ")");
			sbSQL.append("  ,T_URIMOK_GK UM");
			sbSQL.append(" where " + sqlDate[1] + sqlCha[0]);
			sbSQL.append(" union all ");
			sbSQL.append("select U.URI_YMD AS SORT, NVL2(WEEK,substr(U.URI_YMD,7,2) || ' ' || WEEK,'99 補') as \"日付\" " + items[0]);
			sbSQL.append(" from URI_J U INNER JOIN T_WEEK on (U.URI_YMD = WURI_YMD)");
			sbSQL.append("  left outer join WKAWASE MK on (substr(U.URI_YMD,1,6) = MK.YM and " + sqlChaW2 + ")");
			sbSQL.append("  left outer join T_URIMOK UM on (substr(U.URI_YMD,1,6) = UM.MOK_DATE)");
			sbSQL.append(" where " + sqlDate[1] + sqlCha[0]);
			sbSQL.append(" group by U.URI_YMD,WEEK");
			sbSQL.append(" order by SORT");
		} else if (StringUtils.equals(szRad_B1, "2")) { //月別
			sbSQL.append("WITH T_URIMOK(MOK_DATE,MOK_URI_GK) as (");
			sbSQL.append(" select UM.URI_YM, " + sumMokGk + mokFrom + "where " + sqlDate[0] + sqlCha[1] + " group by URI_YM ),   ");
			sbSQL.append("T_URIMOK_GK(MOK_URI_GK) as (");
			sbSQL.append(" select " + sumMokGk + mokFrom + " where " + sqlDate[0] + sqlCha[1] + "),   ");
			sbSQL.append("WKAWASE(YM,KCHANEL_CD1,KCHANEL_CD2,KCHANEL_CD3,KCHANEL_CD4,KANZANTI,ZYY_KANZANTI,ZMM_KANZANTI) as ( ");
			sbSQL.append(" select YM,CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4," + kanzanti01);
			sbSQL.append("  ,(select " + kanzanti02 + " from M_KAWASE M02");
			sbSQL.append("    where "+sqlChaW1+" and M02.CHANEL_CD1 = '2' and M02.YM = to_char(add_months(to_date(M01.YM||'01','YYYY-MM-DD'),-12),'YYYYMM'))");
			sbSQL.append("  ,(select " + kanzanti02 + " from M_KAWASE M02");
			sbSQL.append("    where "+sqlChaW1+" and M02.CHANEL_CD1 = '2' and M02.YM = to_char(add_months(to_date(M01.YM||'01','YYYY-MM-DD'),-1),'YYYYMM'))   ");
			sbSQL.append(" from M_KAWASE M01 where M01.CHANEL_CD1 = '2'),   ");
			sbSQL.append("RUIKEI01 (URI_DATE,URI_R,ZMM_URI_R,ZYY_URI_R) as (");
			sbSQL.append(" select distinct U.URI_YM");
			sbSQL.append("  ,(select " + sumUriGK + " from URI_J_MONTH US left join WKAWASE WK on (US.URI_YM = WK.YM AND " + sqlChaW3 + ")");
			sbSQL.append("    where " + sqlDate[2] + sqlCha[0] + ")");
			sbSQL.append("  ,(select " + sumZMMUriGK + " from URI_J_MONTH US left join WKAWASE WK on (US.URI_YM = WK.YM AND " + sqlChaW3 + ")");
			sbSQL.append("    where " + sqlDate[2] + sqlCha[0] + ")");
			sbSQL.append("  ,(select " + sumZYYUriGK + " from URI_J_MONTH US left join WKAWASE WK on (US.URI_YM = WK.YM AND " + sqlChaW3 + ")");
			sbSQL.append("    where " + sqlDate[2] + sqlCha[0] + ")");
			sbSQL.append(" from URI_J_MONTH  U where " + sqlDate[0] + sqlCha[0] + "),   ");
			sbSQL.append("RUIKEI02 (URI_DATE,URI_R,ZMM_URI_R,ZYY_URI_R) AS (");
			sbSQL.append(" select URI_DATE,max(URI_R),max(ZMM_URI_R),max(ZYY_URI_R) from RUIKEI01 group by URI_DATE )   ");
			sbSQL.append("select distinct '' as SORT, '合計' as \"年月\" " + items[1]);
			sbSQL.append(" from URI_J_MONTH U inner join RUIKEI02 R on (U.URI_YM = R.URI_DATE)");
			sbSQL.append(" left outer join WKAWASE MK on (U.URI_YM = MK.YM and " + sqlChaW2 + ")");
			sbSQL.append(" ,T_URIMOK_GK UM");
			sbSQL.append(" where " + sqlDate[0] + sqlCha[0]);
			sbSQL.append(" union all ");
			sbSQL.append("select U.URI_YM as SORT ,U.URI_YM as \"年月\" " + items[0]);
			sbSQL.append(" from URI_J_MONTH U inner join RUIKEI02 R on( U.URI_YM = R.URI_DATE ) ");
			sbSQL.append(" left outer join WKAWASE MK on (U.URI_YM = MK.YM and " + sqlChaW2 + ") ");
			sbSQL.append(" left outer join T_URIMOK UM on (U.URI_YM = UM.MOK_DATE) ");
			sbSQL.append(" where " + sqlDate[0] + sqlCha[0]);
			sbSQL.append(" group by U.URI_YM");
			sbSQL.append(" order by SORT");
		} else {	// 年別
			sbSQL.append("WITH T_URIMOK(MOK_DATE,MOK_URI_GK) as (");
			sbSQL.append(" select substr(UM.URI_YM,1,4), " + sumMokGk + mokFrom);
			sbSQL.append(" where " + sqlDate[0] + sqlCha[1] + " group by  substr(UM.URI_YM,1,4) ),   ");
			sbSQL.append("T_URIMOK_GK(MOK_URI_GK) as (");
			sbSQL.append(" select " + sumMokGk + mokFrom + " where " + sqlDate[0] + sqlCha[1] + "),   ");
			if (StringUtils.equals(szRad_B3, "1")) { // 表示対象-1:オルビス
				sbSQL.append("WYY(YY,CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4,KAWASE,RITSU) as (");
				sbSQL.append(" select substr(YM,1,4),CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4,max(KAWASE),max(RITSU)");
				sbSQL.append(" from M_KAWASE group by substr(YM,1,4),CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4),   ");
				sbSQL.append("WKAWASE(YY,KCHANEL_CD1,KCHANEL_CD2,KCHANEL_CD3,KCHANEL_CD4,KANZANTI,ZYY_KANZANTI) as (");
				sbSQL.append(" select YY,CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4," + kanzanti01);
				sbSQL.append("  ,(select " + kanzanti02 + " from WYY M02");
				sbSQL.append("    where "+ sqlChaW1 + " and M02.CHANEL_CD1 = '2' and M02.YY = to_char(add_months(to_date(SUBSTR(M01.YY,1,4)||'0101','YYYY-MM-DD'),-12),'YYYY')) ");
				sbSQL.append(" from WYY M01 ),    ");
				sbSQL.append("RUIKEI01 (URI_DATE,URI_R,ZYY_URI_R) as (");
				sbSQL.append(" select distinct U.URI_YY");
				sbSQL.append("  ,(select " + sumUriGK + " from URI_J_YEAR US left join WKAWASE WK on (US.URI_YY = WK.YY AND  " + sqlChaW3 + ")");
				sbSQL.append("    where " + sqlDate[2] + sqlCha[0] + " )");
				sbSQL.append("  ,(select " + sumZYYUriGK + " from URI_J_YEAR US left join WKAWASE WK on (US.URI_YY = WK.YY AND " + sqlChaW3 + ")");
				sbSQL.append("    where " + sqlDate[2] + sqlCha[0] + " )");
				sbSQL.append(" from URI_J_YEAR U where " + sqlDate[1] + sqlCha[0] + "),   ");
				sbSQL.append("RUIKEI02 (URI_DATE,URI_R,ZYY_URI_R) AS (");
				sbSQL.append(" select URI_DATE,max(URI_R),max(ZYY_URI_R) from RUIKEI01 group by URI_DATE)   ");
				sbSQL.append("select distinct '' as SORT, '合計' as \"年\" " + items[1]);
				sbSQL.append(" from URI_J_YEAR U inner join RUIKEI02 R on( U.URI_YY = R.URI_DATE )  ");
				sbSQL.append("  left outer join WKAWASE MK on (U.URI_YY = MK.YY and " + sqlChaW2 + ") ");
				sbSQL.append("  ,T_URIMOK_GK UM");
				sbSQL.append(" where " + sqlDate[1] + sqlCha[0]);
				sbSQL.append(" union all ");
				sbSQL.append("select U.URI_YY as SORT, U.URI_YY as \"年\" " + items[0]);
				sbSQL.append(" from URI_J_YEAR U inner join RUIKEI02 R on( U.URI_YY = R.URI_DATE) ");
				sbSQL.append("  left outer join WKAWASE MK on (U.URI_YY = MK.YY and " + sqlChaW2 + ") ");
				sbSQL.append("  left outer join T_URIMOK UM on (U.URI_YY = UM.MOK_DATE) ");
				sbSQL.append(" where " + sqlDate[1] + sqlCha[0]);
				sbSQL.append(" group by U.URI_YY");
				sbSQL.append(" order by SORT");
			} else { // 表示対象-2:関連会社 3:連結
				sbSQL.append("WYY(YM,CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4,KAWASE,RITSU) as (");
				sbSQL.append(" select YM,CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4,max(KAWASE),max(RITSU)");
				sbSQL.append(" from M_KAWASE group by YM,CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4),   ");
				sbSQL.append("WKAWASE(YM, KCHANEL_CD1, KCHANEL_CD2, KCHANEL_CD3, KCHANEL_CD4, KANZANTI, ZYY_KANZANTI) as (");
				sbSQL.append(" select YM,CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4," + kanzanti01);
				sbSQL.append("  ,(select " + kanzanti02 + " from WYY M02");
				sbSQL.append("    where " + sqlChaW1 + " and M02.CHANEL_CD1 = '2' and M02.YM = to_char(add_months(to_date(M01.YM||'01','YYYYMMDD'),-12),'YYYYMM'))");//to_dateのフォーマット
				sbSQL.append(" from WYY M01 ),   ");
				sbSQL.append("RUIKEI01 (URI_DATE,URI_R,ZYY_URI_R) as (");
				sbSQL.append(" select distinct substr(U.URI_YM,1,4)");
				sbSQL.append("  ,(select " + sumUriGK + " from URI_J_MONTH US left join WKAWASE WK on (US.URI_YM = WK.YM AND  " + sqlChaW3 + ")");
				sbSQL.append("    where " + sqlDate[2] + sqlCha[0] + ")");
				sbSQL.append("  ,(select " + sumZYYUriGK + " from URI_J_MONTH US left join WKAWASE WK on (US.URI_YM = WK.YM AND " + sqlChaW3 + ")");
				sbSQL.append("    where " + sqlDate[2] + sqlCha[0] + ")");
				sbSQL.append(" from URI_J_MONTH U where " + sqlDate[0] + sqlCha[0] + "),   ");
				sbSQL.append("RUIKEI02 (URI_DATE,URI_R,ZYY_URI_R) AS (");
				sbSQL.append(" select URI_DATE,max(URI_R),max(ZYY_URI_R) from RUIKEI01 group by URI_DATE)   ");
				sbSQL.append("select distinct '' as SORT, '合計' as \"年\" " + items[1]);
				sbSQL.append(" from URI_J_MONTH U inner join RUIKEI02 R on (substr(U.URI_YM,1,4) = R.URI_DATE )");
				sbSQL.append("  left outer join WKAWASE MK on (U.URI_YM = MK.YM and " + sqlChaW2 + ") ");
				sbSQL.append("  ,T_URIMOK_GK UM ");
				sbSQL.append(" where " + sqlDate[0] + sqlCha[0]);
				sbSQL.append(" union all ");
				sbSQL.append("select substr(U.URI_YM,1,4) as SORT, substr(U.URI_YM,1,4) as \"年\" " + items[0]);
				sbSQL.append(" from URI_J_MONTH U inner join RUIKEI02 R on(substr(U.URI_YM,1,4) = R.URI_DATE) ");
				sbSQL.append("  left outer join WKAWASE MK on (U.URI_YM = MK.YM and " + sqlChaW2 + ") ");
				sbSQL.append("  left outer join T_URIMOK UM on (substr(U.URI_YM,1,4) = UM.MOK_DATE) ");
				sbSQL.append(" where " + sqlDate[0] + sqlCha[0]);
				sbSQL.append(" group by substr(U.URI_YM,1,4)");
				sbSQL.append(" order by SORT");
			}
		}

		System.out.println(sbSQL.toString());
		return sbSQL.toString();
	}

	/**
	 * 売り上げ目標From句を生成します。
	 *
	 * @return 売り上げ目標From句
	 */
	protected String getFromUriMok() {
		String szTenpo = getMap().get("TENPO");			// 店舗
		String sql = " from URI_MOK UM ";
		if (StringUtils.equals(szTenpo, "01")) { //既存店
			// 20160217 TO_DATEの第2パラメータ 'YYYYMMDD' 追加（ORA-01861対応）
			sql += "left outer join M_TENPO T ON UM.CHANEL_CD3 = T.TENPO_CD and SUBSTR(T.OPEN_YMD,1,6) <= TO_CHAR(TO_DATE(UM.URI_YM||'01','YYYYMMDD')-1,'YYYYMM') ";
		}
		return sql;
	}

	/**
	 * 日付Where句を生成します。
	 *
	 * @return 日付Where句-[0]頻出年月条件 [1](年別)年条件(月別)なし(日別)年月日条件 [3](年別)累計(月別)累計(日別)なし
	 */
	protected String[] getWhereDate() {
		String szRad_B1 = getMap().get("RAD_B1");	// 照会データ指定-1:年別 2:月別 3:日別
		String szRad_B3 = getMap().get("RAD_B3");	// 表示対象-1:オルビス 2:関連会社 3:連結
		String szYy1 = getMap().get("YY_KIJUN");	// 開始年
		String szYy2 = getMap().get("YY_KIJUN_TO");	// 終了年
		String szMm1 = getMap().get("MM_KIJUN");	// 開始月
		String szMm2 = getMap().get("MM_KIJUN_TO");	// 終了月

		String sqlYM1 = "";
		String sqlYM2 = "";
		String sqlYM3 = "";
		if (StringUtils.equals(szRad_B1, "3")) {  //日別
			sqlYM1 = " URI_YM = '" + szYy1 + szMm1 + "'";
			sqlYM2 = " URI_YMD >= '" + szYy1 + szMm1 + "00' and URI_YMD <= '" + szYy1 + szMm1 + "99'";
		} else if (StringUtils.equals(szRad_B1, "2")) { //月別
			sqlYM1 = " URI_YM >= '" + szYy1 + szMm1 + "' and URI_YM <= '" + szYy2 + szMm2 + "'";
			sqlYM3 = " US.URI_YM >= '" + szYy1 + szMm1  + "' and US.URI_YM <= U.URI_YM ";
		} else {	// 年別
			sqlYM1 = " URI_YM >= '" + szYy1 + "01" + "' and URI_YM <= '" + szYy2 + "12" + "'";
			sqlYM2 = " URI_YY >= '" + szYy1 + "' and URI_YY <= '" + szYy2 + "'";
			if (StringUtils.equals(szRad_B3, DefineReport.RadB_3.ID1.getVal())) {
				sqlYM3 = " US.URI_YY >= '" + szYy1 + "' and US.URI_YY <= U.URI_YY ";
			} else {
				sqlYM3 = " US.URI_YM >= '" + szYy1 + "01" + "' and substr(US.URI_YM,1,4) <= substr(U.URI_YM,1,4) ";
			}
		}
		return new String[] { sqlYM1, sqlYM2, sqlYM3};
	}

	/**
	 * チャネルWhere句を生成します。
	 *
	 * @return チャネルWhere句-[0]売上用 [1]売上目標用
	 */
	protected String[] getWhereChanel() {
		String szRad_B3 = getMap().get("RAD_B3");	// 表示対象-1:オルビス 2:関連会社 3:連結
		String szChk_B1 = getMap().get("CHK_B1");	// チャネル区分-通販
		String szChk_B2 = getMap().get("CHK_B2");	// チャネル区分-店舗
		String szChk_B3 = getMap().get("CHK_B3");	// チャネル区分-輸出
		String szChk_B4 = getMap().get("CHK_B4");	// チャネル区分-従販
		String szChk_B5 = getMap().get("CHK_B5");	// チャネル区分-他社売上
		String szChk_B6 = getMap().get("CHK_B6");	// チャネル区分-他ブランド
		String szChk_B7 = getMap().get("CHK_B7");	// チャネル区分-委託販売

		String szTenpo = getMap().get("TENPO");		// 店舗
		String szJuhan = getMap().get("JUHAN");		// 従販
		String szKaigai = getMap().get("KAIGAI");	// 海外関係会社(複数)
		String szYushutu = getMap().get("YUSHUTU");	// 輸出先(複数)
		String szBrand = getMap().get("BRAND");		// 他ブランド(複数)
		String szItaku = getMap().get("ITAKU");		// 委託販売(複数)

		String sqlCha1 = "";
		String sqlCha2 = "";
		String sqlCha2m = "";
		if (StringUtils.equals(szRad_B3, "1")) {  //オルビス
			sqlCha1 = " and CHANEL_CD1 = '1' ";
			if (StringUtils.equals(szChk_B1, "on")) {  //通販
				sqlCha2 += " and (CHANEL_CD2 = '10' ";
				sqlCha2m += " and (CHANEL_CD2 = '10' ";
			}
			if (StringUtils.equals(szChk_B2, "on")) {  //店舗
				if (StringUtils.equals(sqlCha2, "")) {
					if (StringUtils.equals(szTenpo, "00")) { //すべて
						sqlCha2 += " and (CHANEL_CD2 = '20' ";
						sqlCha2m += " and (CHANEL_CD2 = '20' ";
					} else if (StringUtils.equals(szTenpo, "01")) { //既存店
						sqlCha2 += " and ((CHANEL_CD2 = '20' and U.KIZON_FLG = '1') ";
						sqlCha2m += " and ((CHANEL_CD2 = '20' and T.TENPO_CD is not null) ";
					} else {
						sqlCha2 += " and ((CHANEL_CD2 = '20' and CHANEL_CD3 = '" + szTenpo + "')";
						sqlCha2m += " and ((CHANEL_CD2 = '20' and CHANEL_CD3 = '" + szTenpo + "')";
					}
				} else {
					if (StringUtils.equals(szTenpo, "00")) { //すべて
						sqlCha2 += " or CHANEL_CD2 = '20' ";
						sqlCha2m += " or CHANEL_CD2 = '20' ";
					} else if (StringUtils.equals(szTenpo, "01")) { //既存店
						sqlCha2 += " or (CHANEL_CD2 = '20' and U.KIZON_FLG = '1') ";
						sqlCha2m += " or (CHANEL_CD2 = '20' and T.TENPO_CD is not null) ";
					} else {
						sqlCha2 += " or (CHANEL_CD2 = '20' and CHANEL_CD3 = '" + szTenpo + "') ";
						sqlCha2m += " or (CHANEL_CD2 = '20' and CHANEL_CD3 = '" + szTenpo + "') ";
					}
				}
			}
			if (StringUtils.equals(szChk_B3, "on")) {  //輸出
				// 輸出先情報取得
				String yushutuData = convCommaString(szYushutu);
				String yushutu = "CHANEL_CD2 = '30' ";
				if (StringUtils.isNotEmpty(yushutuData)) {
					yushutu = "(" + yushutu + "and CHANEL_CD3 in (" + yushutuData + ")) ";
				}
				if (StringUtils.equals(sqlCha2, "")) {
					sqlCha2 += " and (" + yushutu;
					sqlCha2m += " and (" + yushutu;
				} else {
					sqlCha2 += " or " + yushutu;
					sqlCha2m += " or " + yushutu;
				}
			}
			if (StringUtils.equals(szChk_B4, "on")) {  //従販
				String juhan = "CHANEL_CD2 = '40' ";
				if (!StringUtils.equals(szJuhan, "00")) {
					juhan = "(" + juhan + "and CHANEL_CD3 = '" + szJuhan + "') ";
				}
				if (StringUtils.equals(sqlCha2, "")) {
					sqlCha2 += " and (" + juhan;
					sqlCha2m += " and (" + juhan;
				} else {
					sqlCha2 += " or " + juhan;
					sqlCha2m += " or " + juhan;
				}
			}
			if (StringUtils.equals(szChk_B5, "on")) {  //他社売上
				// 他社売上情報取得※基本分類コード 50:他社売上（卸し）,70:委託販売（amazon）を含める
				String itaku = "";
				if (StringUtils.isNotEmpty(szItaku)) {
					String itakuData[] = szItaku.split(",");
					for (String itakuUnit: itakuData){
						if (StringUtils.isNotEmpty(itaku)) {
							itaku += " or ";
						}
						// 上2桁=基本分類コード、下3桁=大分類コード
						itaku += "(CHANEL_CD2 = '" + itakuUnit.substring(0, 2) + "' AND CHANEL_CD3 = '" + itakuUnit.substring(2, 5) + "')";
					}
				} else {
					itaku = "CHANEL_CD2 in ('50','70') ";
				}
				if (StringUtils.equals(sqlCha2, "")) {
					sqlCha2 += " and (" + itaku;
					sqlCha2m += " and (" + itaku;
				} else {
					sqlCha2 += " or " + itaku;
					sqlCha2m += " or " + itaku;
				}
			}
			if (StringUtils.equals(szChk_B6, "on")) {  //他ブランド
				// 他ブランド情報取得
				String brandData = convCommaString(szBrand);
				String brand = "CHANEL_CD2 = '60' ";
				if (StringUtils.isNotEmpty(brandData)) {
					brand = "(" + brand + "and CHANEL_CD3 in (" + brandData + ")) ";
				}
				if (StringUtils.equals(sqlCha2, "")) {
					sqlCha2 += " and (" + brand;
					sqlCha2m += " and (" + brand;
				} else {
					sqlCha2 += " or " + brand;
					sqlCha2m += " or " + brand;
				}
			}
			if (!StringUtils.equals(sqlCha2, "")) {
				sqlCha2 += ") ";
				sqlCha2m += ") ";
			}
		} else if (StringUtils.equals(szRad_B3, "2")) { //関連会社
			// 海外関係会社情報取得
			sqlCha1 = " and CHANEL_CD1 = '2' ";
			if (!StringUtils.equals(szKaigai, "00000")) {	// B01bと共用のため
				String companyData = convCommaString(szKaigai);
				if (StringUtils.isNotEmpty(companyData)) {
					sqlCha2 += " and CHANEL_CD2||CHANEL_CD3 in (" + companyData + ") ";
					sqlCha2m += " and CHANEL_CD2||CHANEL_CD3 in (" + companyData + ") ";
				}
			}
		} else { //連結
			// オルビス輸出（030台湾、040中国、050アジアパシフィック）除外
			// 20190226 オルビス輸出（096中国越境EC⑩）除外
			sqlCha1 = " AND NOT (CHANEL_CD1 = '1' and CHANEL_CD2 = '30' and CHANEL_CD3 in ('030', '040', '050', '096'))";
		}
		return new String[] { sqlCha1 + sqlCha2, sqlCha1 + sqlCha2m };
	}

	/**
	 * 純売上SELECT項目を取得する。
	 *
	 * @param vals 表示項目チェック値
	 * @param rad1 照会データ指定-1:年別 2:月別 3:日別
	 * @param rad2 対比表示-1:前年比 2:前月比
	 * @param rad3 表示対象-1:オルビス 2:関連会社 3:連結
	 * @param nullClass true:輸出|他社売上選択、false:非選択
	 * @return [0]純売上SELECT項目 [1]純売上合計SELECT項目
	 */
	protected String[] getSelectJunItem(String[] vals, String rad1, String rad2, String rad3, boolean nullClass) {
		String select = "";
		String selectGk = "";
		// 出荷件数
		if (StringUtils.equals(vals[0], "1")) {
			if (nullClass){
				select   += ",null as \"出荷件数\" ";
				selectGk += ",null as \"出荷件数\" ";
			}else{
				select += ",sum(U.URISU) as \"出荷件数\" ";
				selectGk += ",sum(U.URISU) as \"出荷件数\" ";
			}
		}
		// 送料
		if (StringUtils.equals(vals[1], "1")) {
			select   += ",sum(U.SOGK) as \"送料\" ";
			selectGk += ",sum(U.SOGK) as \"送料\" ";
		}
		// 返品送料
		if (StringUtils.equals(vals[2], "1")) {
			select   += ",sum(U.HENSOGK) as \"返品送料\" ";
			selectGk += ",sum(U.HENSOGK) as \"返品送料\" ";
		}
		// 上代売上
		if (StringUtils.equals(vals[3], "1")) {
			select += ",round(sum(U.JUN_JDY_URIGK * COALESCE(KANZANTI,1))) as \"上代売上\" ";
			selectGk += ",round(sum(U.JUN_JDY_URIGK * COALESCE(KANZANTI,1))) as \"上代売上\" ";
		}
		// 返品
		if (StringUtils.equals(vals[4], "1")) {
			select += ",round(sum(U.JUN_HENGK * COALESCE(KANZANTI,1))) as \"返品\" ";
			selectGk += ",round(sum(U.JUN_HENGK * COALESCE(KANZANTI,1))) as \"返品\" ";
		}
		// 上代売上合計
		if (StringUtils.equals(vals[5], "1")) {
			select += ",round(sum(U.JUN_JDY_URIGK_T * COALESCE(KANZANTI,1))) as \"上代売上合計\" ";
			selectGk += ",round(sum(U.JUN_JDY_URIGK_T * COALESCE(KANZANTI,1))) as \"上代売上合計\" ";
		}
		// 割引
		if (StringUtils.equals(vals[6], "1")) {
			select += ",round(sum(U.JUN_WARIGK * COALESCE(KANZANTI,1))) as \"割引\" ";
			selectGk += ",round(sum(U.JUN_WARIGK * COALESCE(KANZANTI,1))) as \"割引\" ";
		}
		// 割引戻し
		if (StringUtils.equals(vals[7], "1")) {
			select += ",round(sum(U.JUN_WARIBACK * COALESCE(KANZANTI,1))) as \"割引戻し\" ";
			selectGk += ",round(sum(U.JUN_WARIBACK * COALESCE(KANZANTI,1))) as \"割引戻し\" ";
		}
		// 割引合計
		if (StringUtils.equals(vals[8], "1")) {
			select += ",round(sum(U.JUN_WARIGK_T * COALESCE(KANZANTI,1))) as \"割引合計\" ";
			selectGk += ",round(sum(U.JUN_WARIGK_T * COALESCE(KANZANTI,1))) as \"割引合計\" ";
		}
		// 仕入調整額
		if (StringUtils.equals(vals[9], "1")) {
			select   += ",round(sum(U.SHI_CHOGK * COALESCE(KANZANTI,1))) as \"仕入調整額\" ";
			selectGk += ",round(sum(U.SHI_CHOGK * COALESCE(KANZANTI,1))) as \"仕入調整額\" ";
		}
		// 純売上
		if (StringUtils.equals(vals[10], "1")) {
			select += ",round(sum(U.JUN_URIGK * COALESCE(KANZANTI,1))) as \"純売上\" ";
			selectGk += ",round(sum(U.JUN_URIGK * COALESCE(KANZANTI,1))) as \"純売上\" ";
		}
		// 純売上累計
		if (StringUtils.equals(vals[11], "1")) {
			if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
				select += ",round(sum(U.JUN_URIGK_R * COALESCE(KANZANTI,1))) as \"純売上累計\" ";
				selectGk += ",round(sum(U.JUN_URIGK * COALESCE(KANZANTI,1))) as \"純売上累計\" ";
			} else {
				select += ",round(max(R.URI_R)) as \"純売上累計\" ";
				selectGk += ",round(max(R.URI_R)) as \"純売上累計\" ";
			}
		}
		// 出荷単価
		if (StringUtils.equals(vals[12], "1")) {
			if (nullClass){
				select   += ",null as \"出荷単価\" ";
				selectGk += ",null as \"出荷単価\" ";
			}else{
				select += ",case when sum(U.URISU) = 0 then 0 else round(sum(U.JUN_URIGK * COALESCE(KANZANTI,1)) / sum(U.URISU)) end as \"出荷単価\" ";
				selectGk += ",case when sum(U.URISU) = 0 then 0 else round(sum(U.JUN_URIGK * COALESCE(KANZANTI,1)) / sum(U.URISU)) end as \"出荷単価\" ";
			}
		}
		// 前年/前月比
		if (StringUtils.equals(vals[13], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {	//前月
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",case when sum(U.ZMM_JUN_URIGK_R) = 0 then 0 else sum(U.JUN_URIGK_R * COALESCE(KANZANTI,1)) / sum(U.ZMM_JUN_URIGK_R * COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",case when sum(U.ZMM_JUN_URIGK) = 0 then 0 else sum(U.JUN_URIGK * COALESCE(KANZANTI,1)) / sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
				}
				selectGk += ",case when sum(U.ZMM_JUN_URIGK) = 0 then 0 else sum(U.JUN_URIGK * COALESCE(KANZANTI,1)) / sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
			} else {	//前年
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",case when sum(U.ZYY_JUN_URIGK_R) = 0 then 0 else sum(U.JUN_URIGK_R * COALESCE(KANZANTI,1)) / sum(U.ZYY_JUN_URIGK_R * COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
				} else {
					select += ",case when sum(U.ZYY_JUN_URIGK) = 0 then 0 else sum(U.JUN_URIGK * COALESCE(KANZANTI,1)) / sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
				}
				selectGk += ",case when sum(U.ZYY_JUN_URIGK) = 0 then 0 else sum(U.JUN_URIGK * COALESCE(KANZANTI,1)) / sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
			}
		}
		//達成率
		if (StringUtils.equals(vals[14], "1")) {
			if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {	// 照会データ指定-3:日別
				select += ",case when MAX(UM.MOK_URI_GK) = 0 then 0 else sum(U.JUN_URIGK_R * COALESCE(KANZANTI,1)) / MAX(UM.MOK_URI_GK) * 100 end as \"達成率\" ";
			} else {
				select += ",case when MAX(UM.MOK_URI_GK) = 0 then 0 else sum(U.JUN_URIGK * COALESCE(KANZANTI,1)) / MAX(UM.MOK_URI_GK) * 100 end as \"達成率\" ";
			}
			selectGk += ",case when MAX(UM.MOK_URI_GK) = 0 then 0 else sum(U.JUN_URIGK * COALESCE(KANZANTI,1)) / MAX(UM.MOK_URI_GK) * 100 end as \"達成率\" ";
		}
		// 前年/前月日付(曜日日)
		if (StringUtils.equals(vals[15], "1") || StringUtils.equals(vals[16], "1") || StringUtils.equals(vals[17], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {	//前年
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID1.getVal())) {
					if (StringUtils.equals(rad3, DefineReport.RadB_3.ID1.getVal())) {
						select += ",MAX(U.ZYY_URI_YY) as \"前年\" ";
					} else {
						select += ",SUBSTR(MAX(U.ZYY_URI_YM),1,4) as \"前年\" ";
					}
					selectGk += ",'合計' as \"前年\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",MAX(U.ZYY_URI_YM) as \"前年\" ";
					selectGk += ",'合計' as \"前年\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",COALESCE(MAX(ZYY_WEEK),'補') as \"曜日\" ";
					selectGk += ",'合計' as \"曜日\" ";
				}
			} else {	//前月
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",MAX(U.ZMM_URI_YM) as \"前月\" ";
					selectGk += ",'合計' as \"前月\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",COALESCE(MAX(ZMM_WEEK),'補') as \"曜日\" ";
					selectGk += ",'合計' as \"曜日\" ";
				}
			}
		}
		// 件数(前年/前月)
		if (StringUtils.equals(vals[15], "1")) {
			if (nullClass){
				if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {	//前年
					select   += ",null as \"前年件数\" ";
					selectGk += ",null as \"前年件数\" ";
				} else {	//前月
					select   += ",null as \"前月件数\" ";
					selectGk += ",null as \"前月件数\" ";
				}
			}else{
				if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {	//前年
					select += ",sum(U.ZYY_URISU) as \"前年件数\" ";
					selectGk += ",sum(U.ZYY_URISU) as \"前年件数\" ";
				} else {	//前月
					select += ",sum(U.ZMM_URISU) as \"前月件数\" ";
					selectGk += ",sum(U.ZMM_URISU) as \"前月件数\" ";
				}
			}
		}
		// 純売上(前年/前月)
		if (StringUtils.equals(vals[16], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {	//前年
				select += ",round(sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1))) as \"前年純売上\" ";
				selectGk += ",round(sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1))) as \"前年純売上\" ";
			} else {	//前月
				select += ",round(sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1))) as \"前月純売上\" ";
				selectGk += ",round(sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1))) as \"前月純売上\" ";
			}
		}
		// 純売上累計(前年/前月)
		if (StringUtils.equals(vals[17], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {	//前月
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",round(sum(U.ZMM_JUN_URIGK_R * COALESCE(ZMM_KANZANTI,1))) as \"前月純売上累計\" ";
					selectGk += ",round(sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1))) as \"前月純売上累計\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",round(max(R.ZMM_URI_R)) as \"前月純売上累計\" ";
					selectGk += ",round(max(R.ZMM_URI_R)) as \"前月純売上累計\" ";
				}
			} else {	//前年
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",round(sum(U.ZYY_JUN_URIGK_R * COALESCE(ZYY_KANZANTI,1))) as \"前年純売上累計\" ";
					selectGk += ",round(sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1))) as \"前年純売上累計\" ";
				} else {
					select += ",round(max(R.ZYY_URI_R)) as \"前年純売上累計\" ";
					selectGk += ",round(max(R.ZYY_URI_R)) as \"前年純売上累計\" ";
				}
			}
		}
		// 単価(前年/前月)
		if (StringUtils.equals(vals[18], "1")) {
			if (nullClass){
				if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
					select   += ",null as \"前月単価\" ";
					selectGk += ",null as \"前月単価\" ";
				} else {
					select   += ",null as \"前年単価\" ";
					selectGk += ",null as \"前年単価\" ";
				}
			}else{
				if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
					select += ",case when sum(U.ZMM_URISU) = 0 then 0 else round(sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1)) / sum(U.ZMM_URISU)) end as \"前月単価\" ";
					selectGk += ",case when sum(U.ZMM_URISU) = 0 then 0 else round(sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1)) / sum(U.ZMM_URISU)) end as \"前月単価\" ";
				} else {
					select += ",case when sum(U.ZYY_URISU) = 0 then 0 else round(sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1)) / sum(U.ZYY_URISU)) end as \"前年単価\" ";
					selectGk += ",case when sum(U.ZYY_URISU) = 0 then 0 else round(sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1)) / sum(U.ZYY_URISU)) end as \"前年単価\" ";
				}
			}
		}
		return new String[] { select, selectGk };
	}

	/**
	 * 出荷売上SELECT項目を取得する。
	 *
	 * @param vals 表示項目チェック値
	 * @param rad1 照会データ指定-1:年別 2:月別 3:日別
	 * @param rad2 対比表示-1:前年比 2:前月比
	 * @param rad3 表示対象-1:オルビス 2:関連会社 3:連結
	 * @param nullClass true:輸出|他社売上選択、false:非選択
	 * @return [0]出荷上SELECT項目 [1]出荷上合計SELECT項目
	 */
	protected String[] getSelectShuItem(String[] vals, String rad1, String rad2, String rad3, boolean nullClass) {
		String select = "";
		String selectGk = "";
		// 出荷件数
		if (StringUtils.equals(vals[0], "1")) {
			if (nullClass){
				select   += ",null as \"出荷件数\" ";
				selectGk += ",null as \"出荷件数\" ";
			}else{
				select   += ",sum(U.URISU) as \"出荷件数\" ";
				selectGk += ",sum(U.URISU) as \"出荷件数\" ";
			}
		}
		// 送料
		if (StringUtils.equals(vals[1], "1")) {
			select   += ",sum(U.SOGK) as \"送料\" ";
			selectGk += ",sum(U.SOGK) as \"送料\" ";
		}
		// 返品送料
		if (StringUtils.equals(vals[2], "1")) {
			select   += ",sum(U.HENSOGK) as \"返品送料\" ";
			selectGk += ",sum(U.HENSOGK) as \"返品送料\" ";
		}
		// 上代売上
		if (StringUtils.equals(vals[3], "1")) {
			select += ",round(sum(U.SHU_JDY_URIGK_S * COALESCE(KANZANTI,1))) as \"上代売上\" ";
			selectGk += ",round(sum(U.SHU_JDY_URIGK_S * COALESCE(KANZANTI,1))) as \"上代売上\" ";
		}
		// 割引
		if (StringUtils.equals(vals[4], "1")) {
			select += ",round(sum(U.SHU_WARIGK * COALESCE(KANZANTI,1))) as \"割引\" ";
			selectGk += ",round(sum(U.SHU_WARIGK * COALESCE(KANZANTI,1))) as \"割引\" ";
		}
		// 売価返品
		if (StringUtils.equals(vals[5], "1")) {
			select += ",round(sum(U.SHU_B_HENGK * COALESCE(KANZANTI,1))) as \"売価返品\" ";
			selectGk += ",round(sum(U.SHU_B_HENGK * COALESCE(KANZANTI,1))) as \"売価返品\" ";
		}
		// 出荷売上
		if (StringUtils.equals(vals[6], "1")) {
			select += ",round(sum(U.SHU_URIGK * COALESCE(KANZANTI,1))) as \"出荷売上\" ";
			selectGk += ",round(sum(U.SHU_URIGK * COALESCE(KANZANTI,1))) as \"出荷売上\" ";
		}
		// 出荷売上累計
		if (StringUtils.equals(vals[7], "1")) {
			if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
				select += ",round(sum(U.SHU_URIGK_R * COALESCE(KANZANTI,1))) as \"出荷売上累計\" ";
				selectGk += ",round(sum(U.SHU_URIGK * COALESCE(KANZANTI,1))) as \"出荷売上累計\" ";
			} else {
				select += ",round(max(R.URI_R)) as \"出荷売上累計\" ";
				selectGk += ",round(max(R.URI_R)) as \"出荷売上累計\" ";
			}
		}
		// 出荷単価
		if (StringUtils.equals(vals[8], "1")) {
			if (nullClass){
				select   += ",null as \"出荷単価\" ";
				selectGk += ",null as \"出荷単価\" ";
			}else{
				select   += ",case when sum(U.URISU) = 0 then 0 else round(sum(U.SHU_URIGK * COALESCE(KANZANTI,1)) / sum(U.URISU)) end as \"出荷単価\" ";
				selectGk += ",case when sum(U.URISU) = 0 then 0 else round(sum(U.SHU_URIGK * COALESCE(KANZANTI,1)) / sum(U.URISU)) end as \"出荷単価\" ";
			}
		}
		// 前年/前月比
		if (StringUtils.equals(vals[9], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {	// 前月
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",case when sum(U.ZMM_SHU_URIGK_R) = 0 then 0 else sum(U.SHU_URIGK_R * COALESCE(KANZANTI,1)) / sum(U.ZMM_SHU_URIGK_R * COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
				} else {
					select += ",case when sum(U.ZMM_SHU_URIGK) = 0 then 0 else sum(U.SHU_URIGK * COALESCE(KANZANTI,1)) / sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
				}
				selectGk += ",case when sum(U.ZMM_SHU_URIGK) = 0 then 0 else sum(U.SHU_URIGK * COALESCE(KANZANTI,1)) / sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
			} else {	// 前年
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",case when sum(U.ZYY_SHU_URIGK_R) = 0 then 0 else sum(U.SHU_URIGK_R * COALESCE(KANZANTI,1)) / sum(U.ZYY_SHU_URIGK_R * COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
				} else {
					select += ",case when sum(U.ZYY_SHU_URIGK) = 0 then 0 else sum(U.SHU_URIGK * COALESCE(KANZANTI,1)) / sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
				}
				selectGk += ",case when sum(U.ZYY_SHU_URIGK) = 0 then 0 else sum(U.SHU_URIGK * COALESCE(KANZANTI,1)) / sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
			}
		}
		// 達成率
		if (StringUtils.equals(vals[10], "1")) {
			if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
				select += ",case when MAX(UM.MOK_URI_GK) = 0 then 0 else sum(U.SHU_URIGK_R * COALESCE(KANZANTI,1)) / MAX(UM.MOK_URI_GK) * 100 end as \"達成率\" ";
			} else {
				select += ",case when MAX(UM.MOK_URI_GK) = 0 then 0 else sum(U.SHU_URIGK * COALESCE(KANZANTI,1)) / MAX(UM.MOK_URI_GK) * 100 end as \"達成率\" ";
			}
			selectGk += ",case when MAX(UM.MOK_URI_GK) = 0 then 0 else sum(U.SHU_URIGK * COALESCE(KANZANTI,1)) / MAX(UM.MOK_URI_GK) * 100 end as \"達成率\" ";
		}
		// 前年日付(曜日日)
		if (StringUtils.equals(vals[11], "1") || StringUtils.equals(vals[12], "1") || StringUtils.equals(vals[13], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID1.getVal())) {
					if (StringUtils.equals(rad3, DefineReport.RadB_3.ID1.getVal())) {
						select += ",MAX(U.ZYY_URI_YY) as \"前年\" ";
					} else {
						select += ",SUBSTR(MAX(U.ZYY_URI_YM),1,4) as \"前年\" ";
					}
					selectGk += ",'合計' as \"前年\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",MAX(U.ZYY_URI_YM) as \"前年\" ";
					selectGk += ",'合計' as \"前年\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",COALESCE(MAX(ZYY_WEEK),'補') as \"曜日\" ";
					selectGk += ",'合計' as \"曜日\" ";
				}
			} else {
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",MAX(U.ZMM_URI_YM) as \"前月\" ";
					selectGk += ",'合計' as \"前月\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",COALESCE(MAX(ZMM_WEEK),'補') as \"曜日\" ";
					selectGk += ",'合計' as \"曜日\" ";
				}
			}
		}
		// 件数(前年/前月)
		if (StringUtils.equals(vals[11], "1")) {
			if (nullClass){
				if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
					select   += ",null as \"前月件数\" ";
					selectGk += ",null as \"前月件数\" ";
				} else {
					select   += ",null as \"前年件数\" ";
					selectGk += ",null as \"前年件数\" ";
				}
			}else{
				if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
					select   += ",sum(U.ZMM_URISU) as \"前月件数\" ";
					selectGk += ",sum(U.ZMM_URISU) as \"前月件数\" ";
				} else {
					select   += ",sum(U.ZYY_URISU) as \"前年件数\" ";
					selectGk += ",sum(U.ZYY_URISU) as \"前年件数\" ";
				}
			}

		}
		// 出荷売上(前年/前月)
		if (StringUtils.equals(vals[12], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
				select += ",round(sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1))) as \"前月出荷売上\" ";
				selectGk += ",round(sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1))) as \"前月出荷売上\" ";
			} else {
				select += ",round(sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1))) as \"前年出荷売上\" ";
				selectGk += ",round(sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1))) as \"前年出荷売上\" ";
			}
		}
		// 出荷売上累計(前年/前月)
		if (StringUtils.equals(vals[13], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
				if (StringUtils.equals(rad1, DefineReport.RadB_2.ID3.getVal())) {
					select += ",round(sum(U.ZMM_SHU_URIGK_R * COALESCE(ZMM_KANZANTI,1))) as \"前月出荷売上累計\" ";
					selectGk += ",round(sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1))) as \"前月出荷売上累計\" ";
				} else {
					select += ",round(max(R.ZMM_URI_R)) as \"前月出荷売上累計\" ";
					selectGk += ",round(max(R.ZMM_URI_R)) as \"前月出荷売上累計\" ";
				}
			} else {
				if (StringUtils.equals(rad1, DefineReport.RadB_2.ID3.getVal())) {
					select += ",round(sum(U.ZYY_SHU_URIGK_R * COALESCE(ZYY_KANZANTI,1))) as \"前年出荷売上累計\" ";
					selectGk += ",round(sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1))) as \"前年出荷売上累計\" ";
				} else {
					select += ",round(max(R.ZYY_URI_R)) as \"前年出荷売上累計\" ";
					selectGk += ",round(max(R.ZYY_URI_R)) as \"前年出荷売上累計\" ";
				}
			}
		}
		// 単価(前年/前月)
		if (StringUtils.equals(vals[14], "1")) {
			if (nullClass){
				if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
					select   += ",null as \"前月単価\" ";
					selectGk += ",null as \"前月単価\" ";
				} else {
					select   += ",null as \"前年単価\" ";
					selectGk += ",null as \"前年単価\" ";
				}
			}else{
				if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
					select   += ",case when sum(U.ZMM_URISU) = 0 then 0 else round(sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1)) / sum(U.ZMM_URISU)) end as \"前月単価\" ";
					selectGk += ",case when sum(U.ZMM_URISU) = 0 then 0 else round(sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1)) / sum(U.ZMM_URISU)) end as \"前月単価\" ";
				} else {
					select   += ",case when sum(U.ZYY_URISU) = 0 then 0 else round(sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1)) / sum(U.ZYY_URISU)) end as \"前年単価\" ";
					selectGk += ",case when sum(U.ZYY_URISU) = 0 then 0 else round(sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1)) / sum(U.ZYY_URISU)) end as \"前年単価\" ";
				}
			}
		}
		return new String[] { select, selectGk };
	}

	/**
	 * 純売上SELECT項目を取得する。
	 *
	 * @param vals 表示項目チェック値
	 * @param rad1 照会データ指定-1:年別 2:月別 3:日別
	 * @param rad2 対比表示-1:前年比 2:前月比
	 * @param rad3 表示対象-1:オルビス 2:関連会社 3:連結
	 * @return [0]純売上SELECT項目 [1]純売上合計SELECT項目
	 */
	protected String[] getSelectJunItemDef(String[] vals, String rad1, String rad2, String rad3) {
		String select = "";
		String selectGk = "";
		// 件数
		if (StringUtils.equals(vals[0], "1")) {
			select += ",sum(U.URISU) as \"件数\" ";
			selectGk += ",sum(U.URISU) as \"件数\" ";
		}
		// 上代売上
		if (StringUtils.equals(vals[1], "1")) {
			select += ",round(sum(U.JUN_JDY_URIGK / COALESCE(KANZANTI,1))) as \"上代売上\" ";
			selectGk += ",round(sum(U.JUN_JDY_URIGK / COALESCE(KANZANTI,1))) as \"上代売上\" ";
		}
		// 返品
		if (StringUtils.equals(vals[2], "1")) {
			select += ",round(sum(U.JUN_HENGK / COALESCE(KANZANTI,1))) as \"返品\" ";
			selectGk += ",round(sum(U.JUN_HENGK / COALESCE(KANZANTI,1))) as \"返品\" ";
		}
		// 上代売上合計
		if (StringUtils.equals(vals[3], "1")) {
			select += ",round(sum(U.JUN_JDY_URIGK_T / COALESCE(KANZANTI,1))) as \"上代売上合計\" ";
			selectGk += ",round(sum(U.JUN_JDY_URIGK_T / COALESCE(KANZANTI,1))) as \"上代売上合計\" ";
		}
		// 割引
		if (StringUtils.equals(vals[4], "1")) {
			select += ",round(sum(U.JUN_WARIGK / COALESCE(KANZANTI,1))) as \"割引\" ";
			selectGk += ",round(sum(U.JUN_WARIGK / COALESCE(KANZANTI,1))) as \"割引\" ";
		}
		// 割引戻し
		if (StringUtils.equals(vals[5], "1")) {
			select += ",round(sum(U.JUN_WARIBACK / COALESCE(KANZANTI,1))) as \"割引戻し\" ";
			selectGk += ",round(sum(U.JUN_WARIBACK / COALESCE(KANZANTI,1))) as \"割引戻し\" ";
		}
		// 割引合計
		if (StringUtils.equals(vals[6], "1")) {
			select += ",round(sum(U.JUN_WARIGK_T / COALESCE(KANZANTI,1))) as \"割引合計\" ";
			selectGk += ",round(sum(U.JUN_WARIGK_T / COALESCE(KANZANTI,1))) as \"割引合計\" ";
		}
		// 上代－割引
		if (StringUtils.equals(vals[7], "1")) {
			select += ",round(sum(U.JUN_JDY_WARIGK / COALESCE(KANZANTI,1))) as \"上代－割引\" ";
			selectGk += ",round(sum(U.JUN_JDY_WARIGK / COALESCE(KANZANTI,1))) as \"上代－割引\" ";
		}
		// 返品－戻し
		if (StringUtils.equals(vals[8], "1")) {
			select += ",round(sum(U.JUN_HENGK_WARIBACK / COALESCE(KANZANTI,1))) as \"返品－戻し\" ";
			selectGk += ",round(sum(U.JUN_HENGK_WARIBACK / COALESCE(KANZANTI,1))) as \"返品－戻し\" ";
		}
		// 純売上
		if (StringUtils.equals(vals[9], "1")) {
			select += ",round(sum(U.JUN_URIGK / COALESCE(KANZANTI,1))) as \"純売上\" ";
			selectGk += ",round(sum(U.JUN_URIGK / COALESCE(KANZANTI,1))) as \"純売上\" ";
		}
		// 純売上累計
		if (StringUtils.equals(vals[10], "1")) {
			if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
				select += ",round(sum(U.JUN_URIGK_R / COALESCE(KANZANTI,1))) as \"純売上累計\" ";
				selectGk += ",round(sum(U.JUN_URIGK / COALESCE(KANZANTI,1))) as \"純売上累計\" ";
			} else {
				select += ",round(max(R.URI_R)) as \"純売上累計\" ";
				selectGk += ",round(max(R.URI_R)) as \"純売上累計\" ";
			}
		}
		// 単価
		if (StringUtils.equals(vals[11], "1")) {
			select += ",case when sum(U.URISU) = 0 then 0 else round(sum(U.JUN_URIGK / COALESCE(KANZANTI,1)) / sum(U.URISU)) end as \"単価\" ";
			selectGk += ",case when sum(U.URISU) = 0 then 0 else round(sum(U.JUN_URIGK / COALESCE(KANZANTI,1)) / sum(U.URISU)) end as \"単価\" ";
		}
		// 前年/前月比
		if (StringUtils.equals(vals[12], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {	//前月
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",case when sum(U.ZMM_JUN_URIGK_R) = 0 then 0 else sum(U.JUN_URIGK_R / COALESCE(KANZANTI,1)) / sum(U.ZMM_JUN_URIGK_R / COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",case when sum(U.ZMM_JUN_URIGK) = 0 then 0 else sum(U.JUN_URIGK / COALESCE(KANZANTI,1)) / sum(U.ZMM_JUN_URIGK / COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
				}
				selectGk += ",case when sum(U.ZMM_JUN_URIGK) = 0 then 0 else sum(U.JUN_URIGK / COALESCE(KANZANTI,1)) / sum(U.ZMM_JUN_URIGK / COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
			} else {	//前年
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",case when sum(U.ZYY_JUN_URIGK_R) = 0 then 0 else sum(U.JUN_URIGK_R / COALESCE(KANZANTI,1)) / sum(U.ZYY_JUN_URIGK_R / COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
				} else {
					select += ",case when sum(U.ZYY_JUN_URIGK) = 0 then 0 else sum(U.JUN_URIGK / COALESCE(KANZANTI,1)) / sum(U.ZYY_JUN_URIGK / COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
				}
				selectGk += ",case when sum(U.ZYY_JUN_URIGK) = 0 then 0 else sum(U.JUN_URIGK / COALESCE(KANZANTI,1)) / sum(U.ZYY_JUN_URIGK / COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
			}
		}
		//達成率
		if (StringUtils.equals(vals[13], "1")) {
			select += ",'-' as \"達成率\" ";
			selectGk += ",'-' as \"達成率\" ";
		}
		// 前年/前月日付(曜日日)
		if (StringUtils.equals(vals[14], "1") || StringUtils.equals(vals[15], "1") || StringUtils.equals(vals[16], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {	//前年
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID1.getVal())) {
					if (StringUtils.equals(rad3, DefineReport.RadB_3.ID1.getVal())) {
						select += ",MAX(U.ZYY_URI_YY) as \"前年\" ";
					} else {
						select += ",SUBSTR(MAX(U.ZYY_URI_YM),1,4) as \"前年\" ";
					}
					selectGk += ",'合計' as \"前年\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",MAX(U.ZYY_URI_YM) as \"前年\" ";
					selectGk += ",'合計' as \"前年\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",COALESCE(MAX(ZYY_WEEK),'補') as \"曜日\" ";
					selectGk += ",'合計' as \"曜日\" ";
				}
			} else {	//前月
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",MAX(U.ZMM_URI_YM) as \"前月\" ";
					selectGk += ",'合計' as \"前月\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",COALESCE(MAX(ZMM_WEEK),'補') as \"曜日\" ";
					selectGk += ",'合計' as \"曜日\" ";
				}
			}
		}
		// 件数(前年/前月)
		if (StringUtils.equals(vals[14], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {	//前年
				select += ",sum(U.ZYY_URISU) as \"前年件数\" ";
				selectGk += ",sum(U.ZYY_URISU) as \"前年件数\" ";
			} else {	//前月
				select += ",sum(U.ZMM_URISU) as \"前月件数\" ";
				selectGk += ",sum(U.ZMM_URISU) as \"前月件数\" ";
			}
		}
		// 純売上(前年/前月)
		if (StringUtils.equals(vals[15], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {	//前年
				select += ",round(sum(U.ZYY_JUN_URIGK / COALESCE(ZYY_KANZANTI,1))) as \"前年純売上\" ";
				selectGk += ",round(sum(U.ZYY_JUN_URIGK / COALESCE(ZYY_KANZANTI,1))) as \"前年純売上\" ";
			} else {	//前月
				select += ",round(sum(U.ZMM_JUN_URIGK / COALESCE(ZMM_KANZANTI,1))) as \"前月純売上\" ";
				selectGk += ",round(sum(U.ZMM_JUN_URIGK / COALESCE(ZMM_KANZANTI,1))) as \"前月純売上\" ";
			}
		}
		// 純売上累計(前年/前月)
		if (StringUtils.equals(vals[16], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {	//前月
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",round(sum(U.ZMM_JUN_URIGK_R / COALESCE(ZMM_KANZANTI,1))) as \"前月純売上累計\" ";
					selectGk += ",round(sum(U.ZMM_JUN_URIGK / COALESCE(ZMM_KANZANTI,1))) as \"前月純売上累計\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",round(max(R.ZMM_URI_R)) as \"前月純売上累計\" ";
					selectGk += ",round(max(R.ZMM_URI_R)) as \"前月純売上累計\" ";
				}
			} else {	//前年
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",round(sum(U.ZYY_JUN_URIGK_R / COALESCE(ZYY_KANZANTI,1))) as \"前年純売上累計\" ";
					selectGk += ",round(sum(U.ZYY_JUN_URIGK / COALESCE(ZYY_KANZANTI,1))) as \"前年純売上累計\" ";
				} else {
					select += ",round(max(R.ZYY_URI_R)) as \"前年純売上累計\" ";
					selectGk += ",round(max(R.ZYY_URI_R)) as \"前年純売上累計\" ";
				}
			}
		}
		// 単価(前年/前月)
		if (StringUtils.equals(vals[17], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
				select += ",case when sum(U.ZMM_URISU) = 0 then 0 else round(sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1)) / sum(U.ZMM_URISU)) end as \"前月単価\" ";
				selectGk += ",case when sum(U.ZMM_URISU) = 0 then 0 else round(sum(U.ZMM_JUN_URIGK * COALESCE(ZMM_KANZANTI,1)) / sum(U.ZMM_URISU)) end as \"前月単価\" ";
			} else {
				select += ",case when sum(U.ZYY_URISU) = 0 then 0 else round(sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1)) / sum(U.ZYY_URISU)) end as \"前年単価\" ";
				selectGk += ",case when sum(U.ZYY_URISU) = 0 then 0 else round(sum(U.ZYY_JUN_URIGK * COALESCE(ZYY_KANZANTI,1)) / sum(U.ZYY_URISU)) end as \"前年単価\" ";
			}
		}
		return new String[] { select, selectGk };
	}

	/**
	 * 出荷売上SELECT項目を取得する。
	 *
	 * @param vals 表示項目チェック値
	 * @param rad1 照会データ指定-1:年別 2:月別 3:日別
	 * @param rad2 対比表示-1:前年比 2:前月比
	 * @param rad3 表示対象-1:オルビス 2:関連会社 3:連結
	 * @return [0]出荷上SELECT項目 [1]出荷上合計SELECT項目
	 */
	protected String[] getSelectShuItemDef(String[] vals, String rad1, String rad2, String rad3) {
		String select = "";
		String selectGk = "";
		// 件数
		if (StringUtils.equals(vals[0], "1")) {
			select += ",sum(U.URISU) as \"件数\" ";
			selectGk += ",sum(U.URISU) as \"件数\" ";
		}
		// 上代総売上
		if (StringUtils.equals(vals[1], "1")) {
			select += ",round(sum(U.SHU_JDY_URIGK_S / COALESCE(KANZANTI,1))) as \"上代総売上\" ";
			selectGk += ",round(sum(U.SHU_JDY_URIGK_S / COALESCE(KANZANTI,1))) as \"上代総売上\" ";
		}
		// 割引
		if (StringUtils.equals(vals[2], "1")) {
			select += ",round(sum(U.SHU_WARIGK / COALESCE(KANZANTI,1))) as \"割引\" ";
			selectGk += ",round(sum(U.SHU_WARIGK / COALESCE(KANZANTI,1))) as \"割引\" ";
		}
		// 売価返品
		if (StringUtils.equals(vals[3], "1")) {
			select += ",round(sum(U.SHU_B_HENGK / COALESCE(KANZANTI,1))) as \"売価返品\" ";
			selectGk += ",round(sum(U.SHU_B_HENGK / COALESCE(KANZANTI,1))) as \"売価返品\" ";
		}
		// 出荷売上
		if (StringUtils.equals(vals[4], "1")) {
			select += ",round(sum(U.SHU_URIGK / COALESCE(KANZANTI,1))) as \"出荷売上\" ";
			selectGk += ",round(sum(U.SHU_URIGK / COALESCE(KANZANTI,1))) as \"出荷売上\" ";
		}
		// 出荷売上累計
		if (StringUtils.equals(vals[5], "1")) {
			if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
				select += ",round(sum(U.SHU_URIGK_R / COALESCE(KANZANTI,1))) as \"出荷売上累計\" ";
				selectGk += ",round(sum(U.SHU_URIGK / COALESCE(KANZANTI,1))) as \"出荷売上累計\" ";
			} else {
				select += ",round(max(R.URI_R)) as \"出荷売上累計\" ";
				selectGk += ",round(max(R.URI_R)) as \"出荷売上累計\" ";
			}
		}
		// 単価
		if (StringUtils.equals(vals[6], "1")) {
			select += ",case when sum(U.URISU) = 0 then 0 else round(sum(U.SHU_URIGK / COALESCE(KANZANTI,1)) / sum(U.URISU)) end as \"単価\" ";
			selectGk += ",case when sum(U.URISU) = 0 then 0 else round(sum(U.SHU_URIGK / COALESCE(KANZANTI,1)) / sum(U.URISU)) end as \"単価\" ";
		}
		// 前年/前月比
		if (StringUtils.equals(vals[7], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {	// 前月
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",case when sum(U.ZMM_SHU_URIGK_R) = 0 then 0 else sum(U.SHU_URIGK_R / COALESCE(KANZANTI,1)) / sum(U.ZMM_SHU_URIGK_R / COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
				} else {
					select += ",case when sum(U.ZMM_SHU_URIGK) = 0 then 0 else sum(U.SHU_URIGK / COALESCE(KANZANTI,1)) / sum(U.ZMM_SHU_URIGK / COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
				}
				selectGk += ",case when sum(U.ZMM_SHU_URIGK) = 0 then 0 else sum(U.SHU_URIGK / COALESCE(KANZANTI,1)) / sum(U.ZMM_SHU_URIGK / COALESCE(ZMM_KANZANTI,1)) * 100 end as \"前月比\" ";
			} else {	// 前年
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",case when sum(U.ZYY_SHU_URIGK_R) = 0 then 0 else sum(U.SHU_URIGK_R / COALESCE(KANZANTI,1)) / sum(U.ZYY_SHU_URIGK_R / COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
				} else {
					select += ",case when sum(U.ZYY_SHU_URIGK) = 0 then 0 else sum(U.SHU_URIGK / COALESCE(KANZANTI,1)) / sum(U.ZYY_SHU_URIGK / COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
				}
				selectGk += ",case when sum(U.ZYY_SHU_URIGK) = 0 then 0 else sum(U.SHU_URIGK / COALESCE(KANZANTI,1)) / sum(U.ZYY_SHU_URIGK / COALESCE(ZYY_KANZANTI,1)) * 100 end as \"前年比\" ";
			}
		}
		// 達成率
		if (StringUtils.equals(vals[8], "1")) {
			select += ",'-'  as \"達成率\" ";
			selectGk += ",'-' as \"達成率\" ";
		}
		// 前年日付(曜日日)
		if (StringUtils.equals(vals[9], "1") || StringUtils.equals(vals[10], "1") || StringUtils.equals(vals[11], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID1.getVal())) {
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID1.getVal())) {
					if (StringUtils.equals(rad3, DefineReport.RadB_3.ID1.getVal())) {
						select += ",MAX(U.ZYY_URI_YY) as \"前年\" ";
					} else {
						select += ",SUBSTR(MAX(U.ZYY_URI_YM),1,4) as \"前年\" ";
					}
					selectGk += ",'合計' as \"前年\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",MAX(U.ZYY_URI_YM) as \"前年\" ";
					selectGk += ",'合計' as \"前年\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",COALESCE(MAX(ZYY_WEEK),'補') as \"曜日\" ";
					selectGk += ",'合計' as \"曜日\" ";
				}
			} else {
				if (StringUtils.equals(rad1, DefineReport.RadB_1.ID2.getVal())) {
					select += ",MAX(U.ZMM_URI_YM) as \"前月\" ";
					selectGk += ",'合計' as \"前月\" ";
				} else if (StringUtils.equals(rad1, DefineReport.RadB_1.ID3.getVal())) {
					select += ",COALESCE(MAX(ZMM_WEEK),'補') as \"曜日\" ";
					selectGk += ",'合計' as \"曜日\" ";
				}
			}
		}
		// 件数(前年/前月)
		if (StringUtils.equals(vals[9], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
				select += ",sum(U.ZMM_URISU) as \"前月件数\" ";
				selectGk += ",sum(U.ZMM_URISU) as \"前月件数\" ";
			} else {
				select += ",sum(U.ZYY_URISU) as \"前年件数\" ";
				selectGk += ",sum(U.ZYY_URISU) as \"前年件数\" ";
			}
		}
		// 出荷売上(前年/前月)
		if (StringUtils.equals(vals[10], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
				select += ",round(sum(U.ZMM_SHU_URIGK / COALESCE(ZMM_KANZANTI,1))) as \"前月出荷売上\" ";
				selectGk += ",round(sum(U.ZMM_SHU_URIGK / COALESCE(ZMM_KANZANTI,1))) as \"前月出荷売上\" ";
			} else {
				select += ",round(sum(U.ZYY_SHU_URIGK / COALESCE(ZYY_KANZANTI,1))) as \"前年出荷売上\" ";
				selectGk += ",round(sum(U.ZYY_SHU_URIGK / COALESCE(ZYY_KANZANTI,1))) as \"前年出荷売上\" ";
			}
		}
		// 出荷売上累計(前年/前月)
		if (StringUtils.equals(vals[11], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
				if (StringUtils.equals(rad1, DefineReport.RadB_2.ID3.getVal())) {
					select += ",round(sum(U.ZMM_SHU_URIGK_R / COALESCE(ZMM_KANZANTI,1))) as \"前月出荷売上累計\" ";
					selectGk += ",round(sum(U.ZMM_SHU_URIGK / COALESCE(ZMM_KANZANTI,1))) as \"前月出荷売上累計\" ";
				} else {
					select += ",round(max(R.ZMM_URI_R)) as \"前月出荷売上累計\" ";
					selectGk += ",round(max(R.ZMM_URI_R)) as \"前月出荷売上累計\" ";
				}
			} else {
				if (StringUtils.equals(rad1, DefineReport.RadB_2.ID3.getVal())) {
					select += ",round(sum(U.ZYY_SHU_URIGK_R / COALESCE(ZYY_KANZANTI,1))) as \"前年出荷売上累計\" ";
					selectGk += ",round(sum(U.ZYY_SHU_URIGK / COALESCE(ZYY_KANZANTI,1))) as \"前年出荷売上累計\" ";
				} else {
					select += ",round(max(R.ZYY_URI_R)) as \"前年出荷売上累計\" ";
					selectGk += ",round(max(R.ZYY_URI_R)) as \"前年出荷売上累計\" ";
				}
			}
		}
		// 単価(前年/前月)
		if (StringUtils.equals(vals[12], "1")) {
			if (StringUtils.equals(rad2, DefineReport.RadB_2.ID2.getVal())) {
				select += ",case when sum(U.ZMM_URISU) = 0 then 0 else round(sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1)) / sum(U.ZMM_URISU)) end as \"前月単価\" ";
				selectGk += ",case when sum(U.ZMM_URISU) = 0 then 0 else round(sum(U.ZMM_SHU_URIGK * COALESCE(ZMM_KANZANTI,1)) / sum(U.ZMM_URISU)) end as \"前月単価\" ";
			} else {
				select += ",case when sum(U.ZYY_URISU) = 0 then 0 else round(sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1)) / sum(U.ZYY_URISU)) end as \"前年単価\" ";
				selectGk += ",case when sum(U.ZYY_URISU) = 0 then 0 else round(sum(U.ZYY_SHU_URIGK * COALESCE(ZYY_KANZANTI,1)) / sum(U.ZYY_URISU)) end as \"前年単価\" ";
			}
		}
		return new String[] { select, selectGk };
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
		String radB3 = jad.getJSONValue(DefineReport.RadB_3.NAME.getObj());

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
		cells.add(DefineReport.RadB_3.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_3.NAME.getObj()));
		if (StringUtils.equals(radB3, DefineReport.RadB_3.ID1.getVal())) { //オルビス
			getWhere().add(cells);
			int idx = getWhere().size();
			String chks = "";
			if (StringUtils.equals(jad.getJSONValue(DefineReport.CheckB_1.ID1.getObj()), "on")) {
				chks += DefineReport.CheckB_1.ID1.getView() + ",";
			}
			cells = new ArrayList<String>();
			cells.add("");
			if (StringUtils.equals(jad.getJSONValue(DefineReport.CheckB_1.ID2.getObj()), "on")) {
				chks += DefineReport.CheckB_1.ID2.getView() + ",";
				cells.add(DefineReport.Select.TENPO.getView());
				cells.add(jad.getJSONText(DefineReport.Select.TENPO.getObj()));
				cells.add("");
			}
			if (StringUtils.equals(jad.getJSONValue(DefineReport.CheckB_1.ID3.getObj()), "on")) {
				chks += DefineReport.CheckB_1.ID3.getView() + ",";
				cells.add(DefineReport.Select.YUSHU.getView());
				cells.add(jad.getJSONText(DefineReport.Select.YUSHU.getObj()));
				cells.add("");
			}
			if (StringUtils.equals(jad.getJSONValue(DefineReport.CheckB_1.ID4.getObj()), "on")) {
				chks += DefineReport.CheckB_1.ID4.getView() + ",";
				cells.add(DefineReport.Select.JUHAN.getView());
				cells.add(jad.getJSONText(DefineReport.Select.JUHAN.getObj()));
				cells.add("");
			}
			if (StringUtils.equals(jad.getJSONValue(DefineReport.CheckB_1.ID5.getObj()), "on")) {
				chks += DefineReport.CheckB_1.ID5.getView() + ",";
				cells.add(jad.getJSONText(DefineReport.Select.ITAKU.getObj()));
			}
			if (StringUtils.equals(jad.getJSONValue(DefineReport.CheckB_1.ID6.getObj()), "on")) {
				chks += DefineReport.CheckB_1.ID6.getView() + ",";
				cells.add(DefineReport.Select.BRAND.getView());
				cells.add(jad.getJSONText(DefineReport.Select.BRAND.getObj()));
				cells.add("");
			}
			getWhere().add(cells);
			cells = getWhere().get(idx - 1);
			cells.add(StringUtils.removeEnd(chks, ","));
			getWhere().set(idx - 1, cells);
		} else if (StringUtils.equals(radB3, DefineReport.RadB_3.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.KANREN.getObj()));
			getWhere().add(cells);
			cells = new ArrayList<String>();
			cells.add("");
			getWhere().add(cells);
		} else {
			getWhere().add(cells);
			cells = new ArrayList<String>();
			cells.add("");
			getWhere().add(cells);
		}

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
		String szTenpo = map.get("TENPO");				// 店舗
		String szRad_B1 = map.get("RAD_B1");			// 照会データ指定-1:年別 2:月別 3:日別
		String szRad_B2 = map.get("RAD_B2");			// 対比表示-1:前年比 2:前月比
		String szRad_B3 = map.get("RAD_B3");			// 表示対象-1:オルビス 2:関連会社 3:連結
		String szChk_B1 = convChk(map, "CHK_B1");		// チャネル区分-通販
		String szChk_B2 = convChk(map, "CHK_B2");		// チャネル区分-店舗
		String szChk_B3 = convChk(map, "CHK_B3");		// チャネル区分-輸出
		String szChk_B4 = convChk(map, "CHK_B4");		// チャネル区分-従販
		String szChk_B5 = convChk(map, "CHK_B5");		// チャネル区分-他社売上
		String szChk_B6 = convChk(map, "CHK_B6");		// チャネル区分-他ブランド
		String szChk_B7 = convChk(map, "CHK_B7");		// チャネル区分-委託販売
		String szYushutu = map.get("YUSHUTU");			// 輸出コード
		String szYushutuNm = map.get("YUSHUTU_NM");		// 輸出名称
		String szBrand = map.get("BRAND");				// 他ブランドコード
		String szBrandNm = map.get("BRAND_NM");			// 他ブランド名称
		String szItaku = map.get("ITAKU");				// 委託販売コード
		String szItakuNm = map.get("ITAKU_NM");			// 委託販売名称
		String szKaigai = map.get("KAIGAI");			// 関連会社コード
		String szKaigaiNm = map.get("KAIGAI_NM");		// 関連会社名称
		String szChk_Chanel1 = "";		//
		String szChk_Chanel2 = "";		//
		String szJuhan = map.get("JUHAN");				// 従販コード
		String szJuhanNm = map.get("JUHAN_NM");			// 従販名称

		String szItemJun = map.get("ITEM_JUN");			// 表示項目-純売上
		String szItemShu = map.get("ITEM_SHU");			// 表示項目-出荷売上

		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("MERGE INTO AK_SAVE_B01 ");
		sbSQL.append("USING (SELECT '" + userId + "' as USER_ID FROM DUAL) WK ON (AK_SAVE_B01.USER_ID = WK.USER_ID) ");
		// 既存レコードの更新
		sbSQL.append("WHEN MATCHED THEN UPDATE SET");
		sbSQL.append(" START_YY_SEL = '" + szYy1 + "',");				// 開始年
		sbSQL.append(" START_MM_SEL = '" + szMm1 + "',");				// 開始月
		sbSQL.append(" END_YY_SEL = '" + szYy2 + "',");					// 終了年
		sbSQL.append(" END_MM_SEL = '" + szMm2 + "',");					// 終了月
		sbSQL.append(" TENPO_SEL = '" + szTenpo + "',");				// 店舗
		sbSQL.append(" REF_DATA_RAD = '" + szRad_B1 + "',");			// 照会データ指定-1:年別 2:月別 3:日別
		sbSQL.append(" VIEW_CMP_RAD = '" + szRad_B2 + "',");			// 対比表示-1:前年比 2:前月比
		sbSQL.append(" VIEW_OBJ_RAD = '" + szRad_B3 + "',");			// 表示対象-1:オルビス 2:関連会社 3:連結
		sbSQL.append(" CHANEL_TSUHAN_CHK = '" + szChk_B1 + "',");		// チャネル区分-通販
		sbSQL.append(" CHANEL_TENPO_CHK = '" + szChk_B2 + "',");		// チャネル区分-店舗
		sbSQL.append(" CHANEL_YUSHUTU_CHK = '" + szChk_B3 + "',");		// チャネル区分-輸出
		sbSQL.append(" CHANEL_JUHAN_CHK = '" + szChk_B4 + "',");		// チャネル区分-従販
		sbSQL.append(" CHANEL_TASYAURI_CHK = '" + szChk_B5 + "',");		// チャネル区分-他社売上
		sbSQL.append(" CHANEL_TABRAND_CHK = '" + szChk_B6 + "',");		// チャネル区分-他ブランド
		sbSQL.append(" CHANEL_ITAKU_CHK = '" + szChk_B7 + "',");		// チャネル区分-委託販売
		sbSQL.append(" CD_YUSHUTU_DETAIL = '" + szYushutu + "',");		// 輸出コード
		sbSQL.append(" NM_YUSHUTU_DETAIL = '" + szYushutuNm + "',");		// 輸出名称
		sbSQL.append(" CD_TABRAND_DETAIL = '" + szBrand + "',");		// 他ブランドコード
		sbSQL.append(" NM_TABRAND_DETAIL = '" + szBrandNm + "',");		// 他ブランド名称
		sbSQL.append(" CD_ITAKU_DETAIL = '" + szItaku + "',");			// 委託販売コード
		sbSQL.append(" NM_ITAKU_DETAIL = '" + szItakuNm + "',");		// 委託販売名称
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
		sbSQL.append(" '" + szRad_B3 + "',");		// 表示対象-1:オルビス 2:関連会社 3:連結
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
		sbSQL.append(" '" + szJuhanNm + "',");		// 従販名称
		sbSQL.append(" '" + szChk_B7 + "',");		// チャネル区分-委託販売
		sbSQL.append(" '" + szItaku + "',");		// 委託販売コード
		sbSQL.append(" '" + szItakuNm + "')");		// 委託販売名称

		System.out.println("保管SQL:" + sbSQL.toString());
		return sbSQL.toString();
	}


}
