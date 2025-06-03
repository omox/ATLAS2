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
 * B03テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B03Dao extends ItemDao {

	/**
	 * 新しいT03SegDaoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public B03Dao(Connection source) {
		super(source);
	}

	/**
	 * 検索実行
	 * @return boolean
	 */
	public boolean selectBy() {

		// 検索コマンド生成
		String command = createCommand();

		// 更新日検索実行
		this.setOptionTsuDel();
		// 出力用検索条件生成
		this.outputQueryList();

		// 検索実行
		return super.selectBySQL(command);
	}
	/**
	 * 更新日を取得し、Settingに設定、JsonGridのオプション情報として扱う。
	 *
	 */
	public void setOptionTsuDel() {
		JSONArray data = selectTsuDel();
		// 取得した更新日を、JSONGRIDのオプションに設定するため、Settingにセット
		HashMap<String, String> t03Setting = new HashMap<String, String>();
		String TsuDel = "";
		if (!data.getJSONObject(0).isEmpty()) {
			TsuDel = data.getJSONObject(0).getString("MSG2");
		}
		t03Setting.put("MSG2", TsuDel);
		setSetting(t03Setting);
	}

	/**
	 * 更新日を取得する。
	 *
	 * @return 更新日
	 */
	public JSONArray selectTsuDel() {
		JSONArray data = new JSONArray();
		try {
			data = ItemList.selectJSONArray(createCommandTsuDel(), new ArrayList<String>(), Defines.STR_JNDI_DS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 更新日取得SQLを生成します。
	 *
	 * @return 更新日取得SQL文
	 */
	private String createCommandTsuDel() {
		String szYy = getMap().get("YYYY_YY");	// 必須）基準年月
		String szNm	= getMap().get("YYYY_MM");	// （必須）基準年月

		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("SELECT CASE WHEN count(DT_UPDATE) = 0 THEN ' ' ELSE '不正データ削除対応 '||TO_CHAR(max(DT_UPDATE), 'YYYY-MM-DD')  END AS MSG2  FROM URI_TSU_ZDEL WHERE URI_YM = '");
		sbSQL.append(szYy+szNm+"'");

		System.out.println("更新日取得SQL:" + sbSQL.toString());
		return sbSQL.toString();
	}


	/**
	 * 検索文を作成します
	 * @return 検索<String>
	 */
	private String createCommand() {

		String command = null;

		String szYy 		=	getMap().get("YYYY_YY");			// （必須）基準年月
		String szNm 		=	getMap().get("YYYY_MM");			// （必須）基準年月
		String szChk1		=	getMap().get("CHK_1");				// （任意）入力経路
		String szChk2		=	getMap().get("CHK_2");				// （任意）入力経路
		String szChk3		=	getMap().get("CHK_3");				// （任意）入力経路
		String szChk4		=	getMap().get("CHK_4");				// （任意）入力経路
		String szChk5		=	getMap().get("CHK_5");				// （任意）入力経路
		String szChk6		=	getMap().get("CHK_6");				// （任意）入力経路
		String szChk7		=	getMap().get("CHK_7");				// （任意）入力経路
		String szChk8		=	getMap().get("CHK_8");				// （任意）入力経路
		String szChk9		=	getMap().get("CHK_9");				// （任意）入力経路
		String szTenpo	= getMap().get("TENPO");				// （任意）店舗

		String szInput1		=	DefineReport.CheckB_3.ID1.getView();	// （任意）入力経路
		String szInput2		=	DefineReport.CheckB_3.ID2.getView();	// （任意）入力経路
		String szInput3		=	DefineReport.CheckB_3.ID3.getView();	// （任意）入力経路
		String szInput4		=	DefineReport.CheckB_3.ID4.getView();	// （任意）入力経路
		String szInput5		=	DefineReport.CheckB_3.ID5.getView();	// （任意）入力経路
		String szInput6		=	DefineReport.CheckB_3.ID6.getView();	// （任意）入力経路
		String szInput7		=	DefineReport.CheckB_3.ID7.getView();	// （任意）入力経路
		String szInput8		=	DefineReport.CheckB_3.ID8.getView();	// （任意）入力経路
		String szInput9		=	DefineReport.CheckB_3.ID9.getView();	// （任意）入力経路

		String szKeiro		=	getMap().get("KEIRO");				// 経路別照会の区別

		System.out.println("TENPO:" +szTenpo);
		// パラメータ確認
		if ((szYy == null) || (szNm == null) ) {
			System.out.println("YYYY_MM:" +szYy+szNm+",CHK_1:"+szChk1+",CHK_2:"+szChk2+",CHK_3:"+szChk3+",CHK_4:"+szChk4+",CHK_5:"+szChk5+",CHK_6:"+szChk6+",CHK_7:"+szChk7+",CHK_8:"+szChk8);
			return "";
		}

		// コード設定
		String szCode1 = "'1'";			// 電話
		String szCode2 = "'4'";			// IVR
		String szCode3 = "'2','3'";	// FAX･封書･ハガキ
		String szCode4 = "'8'";			// スマホ
		String szCode5 = "'10'";		// アプリ
		String szCode6 = "'5'";			// PC
		String szCode7 = "'6'";			// その他
		String szCode8 = "'7'";			// 店舗
		String szCode9 = "'9'";			// 店舗

		// 年月設定
		String sqlWhere = " A.YMD_CD LIKE '" + szYy+szNm + "%' ";
		String sqlItem = "";
		sqlItem = " COALESCE(SUM(B.NEW_JUCSU) + SUM(B.REP_JUCSU),0) , ";
		sqlItem += " SUM(B.NEW_JUCSU_R) + SUM(B.REP_JUCSU_R) , ";
		sqlItem += " case when SUM(B.NEW_JUCGK) + SUM(B.REP_JUCGK) <> 0 AND (SUM(B.NEW_JUCSU) + SUM(B.REP_JUCSU)) <> 0 then ROUND((CAST(SUM(B.NEW_JUCGK) AS DEC(11,1)) + SUM(B.REP_JUCGK)) / (SUM(B.NEW_JUCSU) + SUM(B.REP_JUCSU)),0) else 0 end  , ";
		sqlItem += " COALESCE(SUM(B.NEW_JUCGK) + SUM(B.REP_JUCGK),0) , ";
		sqlItem += " SUM(B.NEW_JUCGK_R) + SUM(B.REP_JUCGK_R) , ";
		sqlItem += " case when (SUM(B.ZYY_NEW_JUCGK_R) + SUM(B.ZYY_REP_JUCGK_R)) <> 0 AND (SUM(B.NEW_JUCGK_R) + SUM(B.REP_JUCGK_R)) <> 0 then (SUM(B.NEW_JUCGK_R) + SUM(B.REP_JUCGK_R)) / (SUM(B.ZYY_NEW_JUCGK_R) + SUM(B.ZYY_REP_JUCGK_R)) * 100 else 0 end  , ";
		sqlItem += " SUM(B.NEW_JUCSU) , ";
		sqlItem += " SUM(B.NEW_JUCSU_R) , ";
		sqlItem += " case when SUM(B.NEW_JUCGK) <> 0 AND SUM(B.NEW_JUCSU) <> 0 then ROUND(SUM(B.NEW_JUCGK) / SUM(B.NEW_JUCSU),0) else 0 end , ";
		sqlItem += " SUM(B.NEW_JUCGK) , ";
		sqlItem += " SUM(B.NEW_JUCGK_R) , ";
		sqlItem += " case when SUM(B.ZYY_NEW_JUCGK_R) <> 0 AND SUM(B.NEW_JUCGK_R) <> 0 then SUM(B.NEW_JUCGK_R) / SUM(B.ZYY_NEW_JUCGK_R) * 100 else 0 end , ";
		sqlItem += " SUM(B.REP_JUCSU) , ";
		sqlItem += " SUM(B.REP_JUCSU_R) , ";
		sqlItem += " case when SUM(B.REP_JUCGK) <> 0 AND SUM(B.REP_JUCSU) <> 0 then ROUND(SUM(B.REP_JUCGK) / SUM(B.REP_JUCSU),0) else 0 end , ";
		sqlItem += " SUM(B.REP_JUCGK) , ";
		sqlItem += " SUM(B.REP_JUCGK_R),";
		sqlItem += "case when SUM(B.ZYY_REP_JUCGK_R) <> 0 AND SUM(B.REP_JUCGK_R) <> 0 then SUM(B.REP_JUCGK_R) / SUM(B.ZYY_REP_JUCGK_R) * 100 else 0 end   ";

		String sqlItemG = "";
		sqlItemG = " COALESCE(SUM(B.NEW_JUCSU) + SUM(B.REP_JUCSU),0) , ";
		sqlItemG += " SUM(B.NEW_JUCSU) + SUM(B.REP_JUCSU) , ";
		sqlItemG += " case when SUM(B.NEW_JUCGK) + SUM(B.REP_JUCGK) <> 0 AND (SUM(B.NEW_JUCSU) + SUM(B.REP_JUCSU)) <> 0 then ROUND((CAST(SUM(B.NEW_JUCGK) AS DEC(11,1)) + SUM(B.REP_JUCGK)) / (SUM(B.NEW_JUCSU) + SUM(B.REP_JUCSU)),0) else 0 end  , ";
		sqlItemG += " COALESCE(SUM(B.NEW_JUCGK) + SUM(B.REP_JUCGK),0) , ";
		sqlItemG += " SUM(B.NEW_JUCGK) + SUM(B.REP_JUCGK) , ";
		sqlItemG += " case when (SUM(B.ZYY_NEW_JUCGK_R) + SUM(B.ZYY_REP_JUCGK_R)) <> 0 AND (SUM(B.NEW_JUCGK_R) + SUM(B.REP_JUCGK_R)) <> 0 then (SUM(B.NEW_JUCGK) + SUM(B.REP_JUCGK)) / (max(all_gaku)) * 100 else 0 end  , ";
		sqlItemG += " SUM(B.NEW_JUCSU) , ";
		sqlItemG += " SUM(B.NEW_JUCSU) , ";
		sqlItemG += " case when SUM(B.NEW_JUCGK) <> 0 AND SUM(B.NEW_JUCSU) <> 0 then ROUND(SUM(B.NEW_JUCGK) / SUM(B.NEW_JUCSU),0) else 0 end , ";
		sqlItemG += " SUM(B.NEW_JUCGK) , ";
		sqlItemG += " SUM(B.NEW_JUCGK) , ";
		sqlItemG += " case when SUM(B.ZYY_NEW_JUCGK_R) <> 0 AND SUM(B.NEW_JUCGK_R) <> 0 then SUM(B.NEW_JUCGK) / max(new_gaku) * 100 else 0 end , ";
		sqlItemG += " SUM(B.REP_JUCSU) , ";
		sqlItemG += " SUM(B.REP_JUCSU) , ";
		sqlItemG += " case when SUM(B.REP_JUCGK) <> 0 AND SUM(B.REP_JUCSU) <> 0 then ROUND(SUM(B.REP_JUCGK) / SUM(B.REP_JUCSU),0) else 0 end , ";
		sqlItemG += " SUM(B.REP_JUCGK) , ";
		sqlItemG += " SUM(B.REP_JUCGK),";
		sqlItemG += "case when SUM(B.ZYY_REP_JUCGK_R) <> 0 AND SUM(B.REP_JUCGK_R) <> 0 then SUM(B.REP_JUCGK) / max(rep_gaku) * 100 else 0 end   ";

		// 電話
		String sqlItem1 = "";
		sqlItem1 += " union all  ";
		sqlItem1 += "SELECT 'Z1' as SORT,'"+szInput1+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem1 += " union all  ";
		sqlItem1 += " SELECT 'Z1'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem1 += sqlItem;
		sqlItem1 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem1 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode1+")";
		sqlItem1 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem1 += " union all  ";
		sqlItem1 += " select 'Z199999999' as SORT,'合計' , ";
		sqlItem1 += sqlItemG;
		sqlItem1 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_month ";
		sqlItem1 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode1+") and  INPUT_CDZ IN ("+szCode1+")";

		// IVR
		String sqlItem2 = "";
		sqlItem2 += " union all  ";
		sqlItem2 += "SELECT 'Z2' as SORT,'"+szInput2+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem2 += " union all  ";
		sqlItem2 += " SELECT 'Z2'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem2 += sqlItem;
		sqlItem2 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem2 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode2+")";
		sqlItem2 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem2 += " union all  ";
		sqlItem2 += " select 'Z299999999' as SORT,'合計' , ";
		sqlItem2 += sqlItemG;
		sqlItem2 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_month ";
		sqlItem2 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode2+") and  INPUT_CDZ IN ("+szCode2+")";

		// FAX･封書･ハガキ
		String sqlItem3 = "";
		sqlItem3 += " union all  ";
		sqlItem3 += "SELECT 'Z3' as SORT,'"+szInput3+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem3 += " union all  ";
		sqlItem3 += " SELECT 'Z3'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem3 += sqlItem;
		sqlItem3 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem3 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode3+")";
		sqlItem3 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem3 += " union all  ";
		sqlItem3 += " select 'Z399999999' as SORT,'合計' , ";
		sqlItem3 += sqlItemG;
		sqlItem3 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD ";
		sqlItem3 += " FULL OUTER JOIN zen_month C ON B.INPUT_CD=C.INPUT_CDZ";
		sqlItem3 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode3+") and  INPUT_CDZ IN ("+szCode3+")";

		// スマホ
		String sqlItem4 = "";
		sqlItem4 += " union all  ";
		sqlItem4 += "SELECT 'Z4' as SORT,'"+szInput4+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem4 += " union all  ";
		sqlItem4 += " SELECT 'Z4'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem4 += sqlItem;
		sqlItem4 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem4 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode4+")";
		sqlItem4 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem4 += " union all  ";
		sqlItem4 += " select 'Z499999999' as SORT,'合計' , ";
		sqlItem4 += sqlItemG;
		sqlItem4 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_month ";
		sqlItem4 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode4+") and  INPUT_CDZ IN ("+szCode4+")";

		// アプリ
		String sqlItem5 = "";
		sqlItem5 += " union all  ";
		sqlItem5 += "SELECT 'Z5' as SORT,'"+szInput5+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem5 += " union all  ";
		sqlItem5 += " SELECT 'Z5'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem5 += sqlItem;
		sqlItem5 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem5 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode5+")";
		sqlItem5 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem5 += " union all  ";
		sqlItem5 += " select 'Z599999999' as SORT,'合計' , ";
		sqlItem5 += sqlItemG;
		sqlItem5 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_month ";
		sqlItem5 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode5+") and  INPUT_CDZ IN ("+szCode5+")";

		// PC
		String sqlItem6 = "";
		sqlItem6 += " union all  ";
		sqlItem6 += "SELECT 'Z6' as SORT,'"+szInput6+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem6 += " union all  ";
		sqlItem6 += " SELECT 'Z6'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem6 += sqlItem;
		sqlItem6 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem6 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode6+")";
		sqlItem6 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem6 += " union all  ";
		sqlItem6 += " select 'Z699999999' as SORT,'合計' , ";
		sqlItem6 += sqlItemG;
		sqlItem6 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_month ";
		sqlItem6 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode6+") and  INPUT_CDZ IN ("+szCode6+")";

		// モバイル
		String sqlItem7 = "";
		sqlItem7 += " union all  ";
		sqlItem7 += "SELECT 'Z7' as SORT,'"+szInput7+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem7 += " union all  ";
		sqlItem7 += " SELECT 'Z7'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem7 += sqlItem;
		sqlItem7 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem7 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode7+")";
		sqlItem7 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem7 += " union all  ";
		sqlItem7 += " select 'Z799999999' as SORT,'合計' , ";
		sqlItem7 += sqlItemG;
		sqlItem7 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_month ";
		sqlItem7 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode7+") and  INPUT_CDZ IN ("+szCode7+")";

		// 定期販売
		String sqlItem8 = "";
		sqlItem8 += " union all  ";
		sqlItem8 += "SELECT 'Z8' as SORT,'"+szInput8+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem8 += " union all  ";
		sqlItem8 += " SELECT 'Z8'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem8 += sqlItem;
		sqlItem8 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem8 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode8+")";
		sqlItem8 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem8 += " union all  ";
		sqlItem8 += " select 'Z899999999' as SORT,'合計' , ";
		sqlItem8 += sqlItemG;
		sqlItem8 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_month ";
		sqlItem8 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode8+") and  INPUT_CDZ IN ("+szCode8+")";

		// 店舗
		String sqlItem9 = "";
		sqlItem9 += " union all  ";
		sqlItem9 += "SELECT 'Z9K' as SORT,'"+szInput9+"',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from   dual ";
		sqlItem9 += " union all  ";
		sqlItem9 += " SELECT 'Z9K'||A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ";
		sqlItem9 += sqlItem;
		sqlItem9 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ";
		sqlItem9 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode9+")";
		sqlItem9 += "  GROUP BY A.YMD_CD,A.WEEK  ";
		sqlItem9 += " union all  ";
		sqlItem9 += " select 'Z9K9999999' as SORT,'合計' , ";
		sqlItem9 += sqlItemG;
		sqlItem9 += " FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_month ";
		sqlItem9 += " WHERE " + sqlWhere + " and  INPUT_CD IN ("+szCode9+") and  INPUT_CDZ IN ("+szCode9+")";
		if (!"00".equals(szTenpo)){
			// 店舗指定時
			sqlItem8 += " AND TENPO_CD IN ('"+szTenpo+"')";
		}

		// 入力経路の判定
		String sqlCD = "";
		String sqlItemAll = "";
		// 電話
		if (StringUtils.equals(szChk1,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode1;
			}
			sqlItemAll += sqlItem1;
		}
		// IVR
		if (StringUtils.equals(szChk2,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode2;
			} else	{
				sqlCD += ","+szCode2;
			}
			sqlItemAll += sqlItem2;
		}
		// FAX･封書･ハガキ
		if (StringUtils.equals(szChk3,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode3;
			} else	{
				sqlCD += ","+szCode3;
			}
			sqlItemAll += sqlItem3;
		}
		// スマホ
		if (StringUtils.equals(szChk4,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode4;
			} else	{
				sqlCD += ","+szCode4;
			}
			sqlItemAll += sqlItem4;
		}
		// アプリ
		if (StringUtils.equals(szChk5,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode5;
			} else	{
				sqlCD += ","+szCode5;
			}
			sqlItemAll += sqlItem5;
		}
		// PC
		if (StringUtils.equals(szChk6,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode6;
			} else	{
				sqlCD += ","+szCode6;
			}
			sqlItemAll += sqlItem6;
		}
		// モバイル
		if (StringUtils.equals(szChk7,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode7;
			} else	{
				sqlCD += ","+szCode7;
			}
			sqlItemAll += sqlItem7;
		}
		// 定期販売
		if (StringUtils.equals(szChk8,"on"))  {
			if ("00".equals(szTenpo)){
				if (StringUtils.equals(sqlCD,""))  {
					sqlCD += szCode8;
				} else	{
					sqlCD += ","+szCode8;
				}
			}
			sqlItemAll += sqlItem8;
		}
		// 店舗
		if (StringUtils.equals(szChk9,"on"))  {
			if ("00".equals(szTenpo)){
				if (StringUtils.equals(sqlCD,""))  {
					sqlCD += szCode9;
				} else	{
					sqlCD += ","+szCode9;
				}
			}
			sqlItemAll += sqlItem9;
		}

		String sqlWhere2 = "";
		if ("00".equals(szTenpo)){
			sqlWhere2 = " AND INPUT_CD IN ("+sqlCD+") ";
		}else{
			// 店舗指定時
			if (StringUtils.equals(sqlCD,""))  {
				sqlWhere2 += " AND (INPUT_CD = "+szCode9+" AND TENPO_CD IN ('"+szTenpo+"'))";
			}else{
				sqlWhere2 += " AND (INPUT_CD IN ("+sqlCD+") OR (INPUT_CD = "+szCode9+" AND TENPO_CD IN ('"+szTenpo+"')))";
			}
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append(" with zen_month(INPUT_CDZ,all_gaku,new_gaku,rep_gaku) as ( ");
		sbSQL.append(" select INPUT_CD,SUM(NEW_JUCGK) + SUM(REP_JUCGK),SUM(NEW_JUCGK),SUM(REP_JUCGK) from DS_M_URI_TSU_JUC ");
		sbSQL.append(" where substr(URI_YMD,1,6) = (SELECT to_char(add_months(to_date('" + szYy+szNm + "01','YYYY-MM-DD'),-12),'YYYYMM') from dual) ");
		sbSQL.append(sqlWhere2 + " group by INPUT_CD )  ");
		sbSQL.append(" ,zen_monthall(all_gaku,new_gaku,rep_gaku) as ( ");
		sbSQL.append(" select SUM(NEW_JUCGK) + SUM(REP_JUCGK),SUM(NEW_JUCGK),SUM(REP_JUCGK) from DS_M_URI_TSU_JUC ");
		sbSQL.append(" where substr(URI_YMD,1,6) = (SELECT to_char(add_months(to_date('" + szYy + szNm + "01','YYYY-MM-DD'),-12),'YYYYMM') from dual) ");
		sbSQL.append(sqlWhere2 + "  )  ");
		sbSQL.append(" SELECT A.YMD_CD as SORT,substr(A.YMD_CD,7,2) || ' ' || A.WEEK , ");
		sbSQL.append(sqlItem);
		sbSQL.append(" FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD   ");
		sbSQL.append(" WHERE "+sqlWhere);
		sbSQL.append( sqlWhere2 );
		sbSQL.append("  GROUP BY A.YMD_CD,A.WEEK  ");
		sbSQL.append(" union all  ");
		sbSQL.append(" select '99999999' as SORT,'合計' , ");
		sbSQL.append(sqlItemG);
		sbSQL.append(" FROM M_CALENDAR A LEFT JOIN DS_M_URI_TSU_JUC B ON A.YMD_CD = B.URI_YMD  ,zen_monthall ");
		sbSQL.append("  WHERE "+sqlWhere );
		sbSQL.append( sqlWhere2 );
		if (StringUtils.equals(szKeiro, "2")) {
			sbSQL.append(sqlItemAll);
		}
		sbSQL.append(" order by SORT ");

		System.out.println(sbSQL.toString());
		command = sbSQL.toString();
		return command;
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

		// 基準年月
		List<String> cells = new ArrayList<String>();
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + DefineReport.Select.YY_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()) + DefineReport.Select.MM_KIJUN.getView());
		cells.add(getSetting().get("MSG2"));
		getWhere().add(cells);

		// 入力経路
		cells = new ArrayList<String>();
		cells.add(DefineReport.CheckB_3.NAME.getView());
		String nyu = "";
		// 経路1
		String chk = DefineReport.CheckB_3.ID1.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 経路2
		chk = DefineReport.CheckB_3.ID2.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 経路3
		chk = DefineReport.CheckB_3.ID3.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 経路4
		chk = DefineReport.CheckB_3.ID4.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 経路5
		chk = DefineReport.CheckB_3.ID5.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 経路6
		chk = DefineReport.CheckB_3.ID6.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 経路7
		chk = DefineReport.CheckB_3.ID7.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 経路8
		chk = DefineReport.CheckB_3.ID8.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 経路9
		chk = DefineReport.CheckB_3.ID9.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on")){
			nyu += jad.getJSONText(chk) + ":";
			// 店舗
			chk = DefineReport.Select.TENPO.getObj();
			nyu += jad.getJSONText(chk);
		}

		cells.add(StringUtils.removeEnd(nyu, "/"));
		getWhere().add(cells);

		// 通販受注実績予想数
		cells = new ArrayList<String>();
		cells.add("");
		cells.add(jad.getJSONText(DefineReport.Label.TSU.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
