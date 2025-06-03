package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * B031テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B031Dao extends ItemDao {

	/**
	 * 新しいT31Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public B031Dao(Connection source) {
		super(source);
	}

	/**
	 * 検索実行
	 * @return boolean
	 */
	public boolean selectBy() {

		// 検索コマンド生成
		String command = createCommand();

		// 出力用検索条件生成
		outputQueryList();

		// 検索実行
		return super.selectBySQL(command);
	}


	/**
	 * 検索文を作成します
	 * @return 検索<String>
	 */
	private String createCommand() {

		String command = null;

		String szYm 		=	getMap().get("YYYY_YM");			// （必須）基準年月
		String szDd 		=	getMap().get("YYYY_DD");			// （必須）基準年月
		String szChk1		=	getMap().get("CHK_1");				// （任意）電話
		String szChk2		=	getMap().get("CHK_2");				// （任意）IVR
		String szChk3		=	getMap().get("CHK_3");				// （任意）FAX･封書･ハガキ
		String szChk4		=	getMap().get("CHK_4");				// （任意）スマホ
		String szChk5		=	getMap().get("CHK_5");				// （任意）アプリ
		String szChk6		=	getMap().get("CHK_6");				// （任意）PC
		String szChk7		=	getMap().get("CHK_7");				// （任意）モバイル
		String szChk8		=	getMap().get("CHK_8");				// （任意）定期販売

		String szInput1		=	DefineReport.CheckB_3.ID1.getView();	// （任意）電話
		String szInput2		=	DefineReport.CheckB_3.ID2.getView();	// （任意）IVR
		String szInput3		=	DefineReport.CheckB_3.ID3.getView();	// （任意）FAX･封書･ハガキ
		String szInput4		=	DefineReport.CheckB_3.ID4.getView();	// （任意）スマホ
		String szInput5		=	DefineReport.CheckB_3.ID5.getView();	// （任意）アプリ
		String szInput6		=	DefineReport.CheckB_3.ID6.getView();	// （任意）PC
		String szInput7		=	DefineReport.CheckB_3.ID7.getView();	// （任意）モバイル
		String szInput8		=	DefineReport.CheckB_3.ID8.getView();	// （任意）定期販売

		String szKeiro		=	getMap().get("KEIRO");				// 経路別照会の区別

		// パラメータ確認
		if ((szYm == null) || (szDd == null) ) {
			System.out.println("YYYY_MM:" +szYm+szDd+",CHK_1:"+szChk1+",CHK_2:"+szChk2+",CHK_3:"+szChk3+",CHK_4:"+szChk4+",CHK_5:"+szChk5+",CHK_6:"+szChk6+",CHK_7:"+szChk7+",CHK_8:"+szChk8);
			return "";
		}

		// コード設定
		String szCode1 = "'1'";		// 電話
		String szCode2 = "'4'";		// IVR
		String szCode3 = "'2','3'";	// FAX･封書･ハガキ
		String szCode4 = "'8'";		// スマホ
		String szCode5 = "'10'";	// アプリ
		String szCode6 = "'5'";		// PC
		String szCode7 = "'6'";		// モバイル
		String szCode8 = "'7'";		// 定期販売


		String sqlItem01 = "";
		sqlItem01 = " SUM(NEW_JUCSU)-COALESCE(SUM(NEW_JUCSU2),0)+SUM(REP_JUCSU)-COALESCE(SUM(REP_JUCSU2),0),";
		sqlItem01 += " CASE WHEN SUBSTR(URI_YMD,7,2) = '01' THEN SUM(NEW_JUCSU)+SUM(REP_JUCSU) ";
		sqlItem01 += "  ELSE SUM(NEW_JUCSU)+COALESCE(SUM(NEW_SU),0)+SUM(REP_JUCSU)+COALESCE(SUM(REP_SU),0) END, ";
		sqlItem01 += " SUM(NEW_JUCGK)-COALESCE(SUM(NEW_JUCGK2),0)+SUM(REP_JUCGK)-COALESCE(SUM(REP_JUCGK2),0) ,";
		sqlItem01 += " CASE WHEN SUBSTR(URI_YMD,7,2) = '01' THEN SUM(NEW_JUCGK)+SUM(REP_JUCGK) ";
		sqlItem01 += "  ELSE SUM(NEW_JUCGK)+COALESCE(SUM(NEW_GA),0)+SUM(REP_JUCGK)+COALESCE(SUM(REP_GA),0) END,";
		sqlItem01 += " SUM(NEW_JUCSU)-COALESCE(SUM(NEW_JUCSU2),0),";
		sqlItem01 += " CASE WHEN SUBSTR(URI_YMD,7,2) = '01' THEN SUM(NEW_JUCSU) ELSE SUM(NEW_JUCSU)+COALESCE(SUM(NEW_SU),0) END,";
		sqlItem01 += " SUM(NEW_JUCGK)-COALESCE(SUM(NEW_JUCGK2),0) ,";
		sqlItem01 += " CASE WHEN SUBSTR(URI_YMD,7,2) = '01' THEN SUM(NEW_JUCGK) ELSE SUM(NEW_JUCGK)+COALESCE(SUM(NEW_GA),0) END,";
		sqlItem01 += " SUM(REP_JUCSU)-COALESCE(SUM(REP_JUCSU2),0),";
		sqlItem01 += " CASE WHEN SUBSTR(URI_YMD,7,2) = '01' THEN SUM(REP_JUCSU) ELSE SUM(REP_JUCSU)+COALESCE(SUM(REP_SU),0) END,";
		sqlItem01 += " SUM(REP_JUCGK)-COALESCE(SUM(REP_JUCGK2),0) ,";
		sqlItem01 += " CASE WHEN SUBSTR(URI_YMD,7,2) = '01' THEN SUM(REP_JUCGK) ELSE SUM(REP_JUCGK)+COALESCE(SUM(REP_GA),0) END ";
		sqlItem01 += "  FROM WORKK LEFT OUTER JOIN WORK2 ON URI_YMD = URI_YMD2 AND URI_YMD_TIME = URI_YMD_TIME2 AND INPUT_CD = INPUT_CD2";
		sqlItem01 += "             LEFT OUTER JOIN WORKT ON URI_YMD = WDAY AND INPUT_CD = INPUT_CDT  ";
		String sqlItem02 = "";
		sqlItem02 = " max(NEW_JUCSU)+max(REP_JUCSU) ,null ,max(NEW_JUCGK)+max(REP_JUCGK) ,null , ";
		sqlItem02 += " max(NEW_JUCSU) ,null ,max(NEW_JUCGK) ,null , ";
		sqlItem02 += " max(REP_JUCSU) ,null ,max(REP_JUCGK) ,null   ";
		sqlItem02 += "  FROM WORKK   ";
		String sqlItem021 = "";
		sqlItem021 = " SUM(NEW_JUCSU)+SUM(REP_JUCSU) ,null ,SUM(NEW_JUCGK)+SUM(REP_JUCGK) ,null , ";
		sqlItem021 += " SUM(NEW_JUCSU) ,null ,SUM(NEW_JUCGK) ,null , ";
		sqlItem021 += " SUM(REP_JUCSU) ,null ,SUM(REP_JUCGK) ,null   ";
		sqlItem021 += "  FROM WORKK where URI_YMD_TIME=(select max(URI_YMD_TIME) from WORKK )  ";

		String sqlItem1 = "";
		sqlItem1 += " union all SELECT 'Z1' as SORT,'"+szInput1+"',null,null,null,null,null,null,null,null,null,null,null,null from dual ";
		sqlItem1 += " union all SELECT 'Z1'||URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ";
		sqlItem1 += sqlItem01;
		sqlItem1 += " WHERE INPUT_CD IN("+szCode1+") GROUP BY URI_YMD,URI_YMD_TIME ";
		sqlItem1 += " union all SELECT 'Z1999' AS SORT,URI_YMD||'_------' , ";
		sqlItem1 += sqlItem02;
		sqlItem1 += " WHERE INPUT_CD IN("+szCode1+") GROUP BY URI_YMD ";
		String sqlItem2 = "";
		sqlItem2 += " union all SELECT 'Z2' as SORT,'"+szInput2+"',null,null,null,null,null,null,null,null,null,null,null,null from dual ";
		sqlItem2 += " union all SELECT 'Z2'||URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ";
		sqlItem2 += sqlItem01;
		sqlItem2 += " WHERE INPUT_CD IN("+szCode2+") GROUP BY URI_YMD,URI_YMD_TIME ";
		sqlItem2 += " union all SELECT 'Z2999' AS SORT,URI_YMD||'_------' , ";
		sqlItem2 += sqlItem02;
		sqlItem2 += " WHERE INPUT_CD IN("+szCode2+") GROUP BY URI_YMD ";
		String sqlItem3 = "";
		sqlItem3 += " union all SELECT 'Z3' as SORT,'"+szInput3+"',null,null,null,null,null,null,null,null,null,null,null,null from dual ";
		sqlItem3 += " union all SELECT 'Z3'||URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ";
		sqlItem3 += sqlItem01;
		sqlItem3 += " WHERE INPUT_CD IN("+szCode3+") GROUP BY URI_YMD,URI_YMD_TIME ";
		sqlItem3 += " union all SELECT 'Z3999' AS SORT,URI_YMD||'_------' , ";
		sqlItem3 += sqlItem02;
		sqlItem3 += " WHERE INPUT_CD IN("+szCode3+") GROUP BY URI_YMD ";
		String sqlItem4 = "";
		sqlItem4 += " union all SELECT 'Z4' as SORT,'"+szInput4+"',null,null,null,null,null,null,null,null,null,null,null,null from dual ";
		sqlItem4 += " union all SELECT 'Z4'||URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ";
		sqlItem4 += sqlItem01;
		sqlItem4 += " WHERE INPUT_CD IN("+szCode4+") GROUP BY URI_YMD,URI_YMD_TIME ";
		sqlItem4 += " union all SELECT 'Z4999' AS SORT,URI_YMD||'_------' , ";
		sqlItem4 += sqlItem02;
		sqlItem4 += " WHERE INPUT_CD IN("+szCode4+") GROUP BY URI_YMD ";
		String sqlItem5 = "";
		sqlItem5 += " union all SELECT 'Z5' as SORT,'"+szInput5+"',null,null,null,null,null,null,null,null,null,null,null,null from dual ";
		sqlItem5 += " union all SELECT 'Z5'||URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ";
		sqlItem5 += sqlItem01;
		sqlItem5 += " WHERE INPUT_CD IN("+szCode5+") GROUP BY URI_YMD,URI_YMD_TIME ";
		sqlItem5 += " union all SELECT 'Z5999' AS SORT,URI_YMD||'_------' , ";
		sqlItem5 += sqlItem02;
		sqlItem5 += " WHERE INPUT_CD IN("+szCode5+") GROUP BY URI_YMD ";
		String sqlItem6 = "";
		sqlItem6 += " union all SELECT 'Z6' as SORT,'"+szInput6+"',null,null,null,null,null,null,null,null,null,null,null,null from dual ";
		sqlItem6 += " union all SELECT 'Z6'||URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ";
		sqlItem6 += sqlItem01;
		sqlItem6 += " WHERE INPUT_CD IN("+szCode6+") GROUP BY URI_YMD,URI_YMD_TIME ";
		sqlItem6 += " union all SELECT 'Z6999' AS SORT,URI_YMD||'_------' , ";
		sqlItem6 += sqlItem02;
		sqlItem6 += " WHERE INPUT_CD IN("+szCode6+") GROUP BY URI_YMD ";
		String sqlItem7 = "";
		sqlItem7 += " union all SELECT 'Z7' as SORT,'"+szInput7+"',null,null,null,null,null,null,null,null,null,null,null,null from dual ";
		sqlItem7 += " union all SELECT 'Z7'||URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ";
		sqlItem7 += sqlItem01;
		sqlItem7 += " WHERE INPUT_CD IN("+szCode7+") GROUP BY URI_YMD,URI_YMD_TIME ";
		sqlItem7 += " union all SELECT 'Z7999' AS SORT,URI_YMD||'_------' , ";
		sqlItem7 += sqlItem02;
		sqlItem7 += " WHERE INPUT_CD IN("+szCode7+") GROUP BY URI_YMD ";
		String sqlItem8 = "";
		sqlItem8 += " union all SELECT 'Z8' as SORT,'"+szInput8+"',null,null,null,null,null,null,null,null,null,null,null,null from dual ";
		sqlItem8 += " union all SELECT 'Z8'||URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ";
		sqlItem8 += sqlItem01;
		sqlItem8 += " WHERE INPUT_CD IN("+szCode8+") GROUP BY URI_YMD,URI_YMD_TIME ";
		sqlItem8 += " union all SELECT 'Z8999' AS SORT,URI_YMD||'_------' , ";
		sqlItem8 += sqlItem02;
		sqlItem8 += " WHERE INPUT_CD IN("+szCode8+") GROUP BY URI_YMD ";


		// 年月設定
		// 入力経路の判定
		String sqlCD = "";
		String sqlItemAll = "";
		if (StringUtils.equals(szChk1,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode1;
			}
			sqlItemAll += sqlItem1;
		}
		if (StringUtils.equals(szChk2,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode2;
			} else	{
				sqlCD += ","+szCode2;
			}
			sqlItemAll += sqlItem2;
		}
		if (StringUtils.equals(szChk3,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode3;
			} else	{
				sqlCD += ","+szCode3;
			}
			sqlItemAll += sqlItem3;
		}
		if (StringUtils.equals(szChk4,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode4;
			} else	{
				sqlCD += ","+szCode4;
			}
			sqlItemAll += sqlItem4;
		}
		if (StringUtils.equals(szChk5,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode5;
			} else	{
				sqlCD += ","+szCode5;
			}
			sqlItemAll += sqlItem5;
		}
		if (StringUtils.equals(szChk6,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode6;
			} else	{
				sqlCD += ","+szCode6;
			}
			sqlItemAll += sqlItem6;
		}
		if (StringUtils.equals(szChk7,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode7;
			} else	{
				sqlCD += ","+szCode7;
			}
			sqlItemAll += sqlItem7;
		}
		if (StringUtils.equals(szChk8,"on"))  {
			if (StringUtils.equals(sqlCD,""))  {
				sqlCD += szCode8;
			} else	{
				sqlCD += ","+szCode8;
			}
			sqlItemAll += sqlItem8;
		}

		String sqlWhere2 = " and  INPUT_CD IN ("+sqlCD+") ";

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append(" with WORKT(WDAY,INPUT_CDT,NEW_SU,NEW_GA,REP_SU,REP_GA) as ( ");
		sbSQL.append(" SELECT MAX(URI_YMD),INPUT_CD, SUM(NEW_JUCSU),SUM(NEW_JUCGK),SUM(REP_JUCSU),SUM(REP_JUCGK)  ");
		sbSQL.append("  FROM DS_M_URI_TSU_DAY  WHERE URI_YMD = '" + szYm + szDd + "' AND URI_YMD_TIME = '00'" + sqlWhere2 + "  GROUP BY INPUT_CD ), ");
		sbSQL.append(" WORKK(URI_YMD,URI_YMD_TIME,INPUT_CD,NEW_JUCSU,NEW_JUCGK,REP_JUCSU,REP_JUCGK) AS ( ");
		// 2014/12/24 時刻01の対応,2016/11/11 時刻02,03の対応
		sbSQL.append("  select URI_YMD,case when substr(URI_YMD_TIME, 3, 2) in ('01','02','03') then substr(URI_YMD_TIME, 1, 2)||'00' else URI_YMD_TIME end URI_YMD_TIME,INPUT_CD,SUM(NEW_JUCSU),SUM(NEW_JUCGK),SUM(REP_JUCSU),SUM(REP_JUCGK)   ");
		sbSQL.append("  from DS_M_URI_TSU_DAY where URI_YMD = '" + szYm+szDd + "' and substr(URI_YMD_TIME,3,2) in ('00','01','02','03') " +sqlWhere2);

		sbSQL.append("  group by URI_YMD,URI_YMD_TIME,INPUT_CD), ");
		sbSQL.append(" WORK2(URI_YMD2,URI_YMD_TIME2,INPUT_CD2,NEW_JUCSU2,NEW_JUCGK2,REP_JUCSU2,REP_JUCGK2) AS ( ");
		sbSQL.append("  SELECT URI_YMD, ");
		sbSQL.append(" CASE ");
		sbSQL.append(" WHEN URI_YMD_TIME = '0000' THEN '0100' WHEN URI_YMD_TIME = '0100' THEN '0200' WHEN URI_YMD_TIME = '0200' THEN '0300' ");
		sbSQL.append(" WHEN URI_YMD_TIME = '0300' THEN '0400' WHEN URI_YMD_TIME = '0400' THEN '0500' WHEN URI_YMD_TIME = '0500' THEN '0600' ");
		sbSQL.append(" WHEN URI_YMD_TIME = '0600' THEN '0700' WHEN URI_YMD_TIME = '0700' THEN '0800' WHEN URI_YMD_TIME = '0800' THEN '0900' ");
		sbSQL.append(" WHEN URI_YMD_TIME = '0900' THEN '1000' WHEN URI_YMD_TIME = '1000' THEN '1100' WHEN URI_YMD_TIME = '1100' THEN '1200' ");
		sbSQL.append(" WHEN URI_YMD_TIME = '1200' THEN '1300' WHEN URI_YMD_TIME = '1300' THEN '1400' WHEN URI_YMD_TIME = '1400' THEN '1500' ");
		sbSQL.append(" WHEN URI_YMD_TIME = '1500' THEN '1600' WHEN URI_YMD_TIME = '1600' THEN '1700' WHEN URI_YMD_TIME = '1700' THEN '1800' ");
		sbSQL.append(" WHEN URI_YMD_TIME = '1800' THEN '1900' WHEN URI_YMD_TIME = '1900' THEN '2000' WHEN URI_YMD_TIME = '2000' THEN '2100' ");
		sbSQL.append(" WHEN URI_YMD_TIME = '2100' THEN '2200' WHEN URI_YMD_TIME = '2200' THEN '2300' WHEN URI_YMD_TIME = '2300' THEN '2400' ");
		sbSQL.append(" END,");
		sbSQL.append(" INPUT_CD,NEW_JUCSU,NEW_JUCGK,REP_JUCSU,REP_JUCGK FROM WORKK ) ");
		sbSQL.append("  SELECT URI_YMD||URI_YMD_TIME AS SORT,URI_YMD||' '||URI_YMD_TIME , ");
		sbSQL.append(sqlItem01);
		sbSQL.append(" GROUP BY URI_YMD,URI_YMD_TIME ");
		sbSQL.append(" union all SELECT 'Z009' AS SORT,URI_YMD||'_------' , ");
		sbSQL.append(sqlItem021);
		sbSQL.append(" GROUP BY URI_YMD ");

		if (StringUtils.equals(szKeiro, "2")) {
			sbSQL.append(sqlItemAll);
		}
		if (StringUtils.equals(szKeiro, "1")) {
			sbSQL.append(" union all SELECT 'Z099' AS SORT,' CANCEL' , ");
			sbSQL.append(" (SUM(NEW_JUCSU)+SUM(REP_JUCSU))*-1 ,0 ,(SUM(NEW_JUCGK)+SUM(REP_JUCGK))*-1 ,0 , ");
			sbSQL.append(" SUM(NEW_JUCSU)*-1  ,0 ,SUM(NEW_JUCGK)*-1  ,0 , ");
			sbSQL.append(" SUM(REP_JUCSU)*-1  ,0 ,SUM(REP_JUCGK)*-1  ,0   ");
			sbSQL.append("  FROM DS_M_URI_TSU_DAY where URI_YMD_TIME = '9999' AND URI_YMD = '" + szYm + szDd + "' " + sqlWhere2);
		}
		sbSQL.append("  order by SORT  ");

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
		cells.add(jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()) + DefineReport.Select.MM_KIJUN.getView());
		cells.add(jad.getJSONText(DefineReport.Select.DD_KIJUN.getObj()) + DefineReport.Select.DD_KIJUN.getView());
		getWhere().add(cells);

		// 入力経路
		cells = new ArrayList<String>();
		cells.add(DefineReport.CheckB_3.NAME.getView());
		String nyu = "";
		// 入力経路1
		String chk = DefineReport.CheckB_3.ID1.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 入力経路2
		chk = DefineReport.CheckB_3.ID2.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 入力経路3
		chk = DefineReport.CheckB_3.ID3.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 入力経路4
		chk = DefineReport.CheckB_3.ID4.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 入力経路5
		chk = DefineReport.CheckB_3.ID5.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 入力経路6
		chk = DefineReport.CheckB_3.ID6.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 入力経路7
		chk = DefineReport.CheckB_3.ID7.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";
		// 入力経路8
		chk = DefineReport.CheckB_3.ID8.getObj();
		if (StringUtils.equals(jad.getJSONValue(chk), "on"))	nyu += jad.getJSONText(chk) + "/";

		cells.add(StringUtils.removeEnd(nyu, "/"));
		getWhere().add(cells);

		cells = new ArrayList<String>();
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}

}
