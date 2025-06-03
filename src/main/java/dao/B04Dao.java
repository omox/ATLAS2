package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import common.CmnDate;
import common.DefineReport;
import common.Defines;
import common.ItemList;
import common.JsonArrayData;

/**
 * B04テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class B04Dao extends ItemDao {

	//public static final String	ID_SQL_SEL_SAVE	= "select USER_ID,START_YY_SEL,START_MM_SEL,START_DD_SEL,END_YY_SEL,END_MM_SEL,END_DD_SEL,START_SYM_SEL,END_SYM_SEL,SU_GA_SEL,TAIHI_SEL,NR_TYPE_SEL,CHANEL_KIHON_SEL,trim(CHANEL_DAI_SEL),trim(CHANEL_TYU_SEL),trim(CHANEL_SHO_SEL),SHOHIN_KIHON_SEL,SHOHIN_DAI_SEL,SHOHIN_TYU_SEL,SHOHIN_SHO_SEL,SHOHIN_A_SEL,SHOHIN_B_SEL,BUN1_SEL,BUN2_SEL,BUN3_SEL,BUN4_SEL,BUN5_SEL,BUN6_SEL,BUN7_SEL,BUN8_SEL,BUN9_SEL,BUN10_SEL,BUNS1_SEL,BUNS2_SEL,BUNS3_SEL,BUNS4_SEL,BUNS5_SEL,BUNS6_SEL,BUNS7_SEL,BUNS8_SEL,BUNS9_SEL,BUNS10_SEL,REF_DATA_RAD,MONTH_12_RAD,NEW_PRODUCT_RAD,TSU_REP_RAD,TENPO_DAI_RAD,DISP_TYPE_RAD,CHANEL_KIHON_CHK,CHANEL_DAI_CHK,CHANEL_TYU_CHK,CHANEL_SHO_CHK,SHOHIN_KIHON_CHK,SHOHIN_DAI_CHK,SHOHIN_TYU_CHK,SHOHIN_SHO_CHK,SHOHIN_A_CHK,SHOHIN_B_CHK,SHOHIN_SHOHIN_CHK from AK_SAVE_B4 T1 WHERE T1.USER_ID = ?";
	public static final String	ID_SQL_SEL_SAVE	= "select * from AK_SAVE_B4 T1 WHERE T1.USER_ID = ?";

	/**
	 * 新しいB04Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public B04Dao(Connection source) {
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

		// 出力用検索条件生成
		outputQueryList();

		// 検索実行
		return super.selectBySQL(command);
	}

	/**
	 * 検索文を作成します
	 *
	 * @return 検索<String>
	 */
	private String createCommand() {

		String command = null;

		String szYy = getMap().get("YYYY_YY");		// 開始年
		String szNm = getMap().get("YYYY_MM");		// 開始月
		String szDd = getMap().get("YYYY_DD");		// 開始日
		String szYy2 = getMap().get("YYYY_YY2");	// 終了年
		String szNm2 = getMap().get("YYYY_MM2");	// 終了月
		String szDd2 = getMap().get("YYYY_DD2");	// 終了日
		String szYmN1 = getMap().get("START_YM");	// 新製品-開始年月
		String szYmN2 = getMap().get("START_YM2");	// 新製品-終了年月
		String szSu = getMap().get("SU_GA");		// 数/額指定
		String szZen = getMap().get("TAIHI");		// 対比表示
		String szSNr = getMap().get("NR_TYPE");		// リピート別集計-しない/する
		String szDsp = getMap().get("RAD_DSP_TYP");	// 表示形式-全表示/実績のみ

		String szCha1 = getMap().get("SEL_CHA1");	// チャネル分類-基本分類
		String szCha2 = getMap().get("SEL_CHA2");	// チャネル分類-大分類
		String szCha3 = getMap().get("SEL_CHA3");	// チャネル分類-中分類
		String szCha4 = getMap().get("SEL_CHA4");	// チャネル分類-小分類
		String szNen = getMap().get("RAD_NEN");		// 照会データ指定
		String sz12 = getMap().get("RAD_12");		// 月/12指定
		String szNewS = getMap().get("RAD_NEW");	// 新製品指定
		String szRNr = getMap().get("RAD_NR");		// リピート別集計指定
		String szTenDai = getMap().get("RAD_TEN_DAI");	// チャネル-店販大分類
		String szChkC1 = getMap().get("CHK_C1");	// チャネル集計-基本分類
		String szChkC2 = getMap().get("CHK_C2");	// チャネル集計-大分類
		String szChkC3 = getMap().get("CHK_C3");	// チャネル集計-中分類
		String szChkC4 = getMap().get("CHK_C4");	// チャネル集計-小分類
		String szChkS1 = getMap().get("CHK_S1");	// 商品集計-基本分類
		String szChkS2 = getMap().get("CHK_S2");	// 商品集計-大分類
		String szChkS3 = getMap().get("CHK_S3");	// 商品集計-中分類
		String szChkS4 = getMap().get("CHK_S4");	// 商品集計-小分類
		String szChkS5 = getMap().get("CHK_S5");	// 商品集計-シリーズＡ
		String szChkS6 = getMap().get("CHK_S6");	// 商品集計-シリーズＢ
		String szChkS7 = getMap().get("CHK_S7");	// 商品集計-商品

		String szSho1a = getMap().get("SEL_SHO1");	// 商品分類-基本分類
		String szSho2a = getMap().get("SEL_SHO2");	// 商品分類-大分類
		String szSho3a = getMap().get("SEL_SHO3");	// 商品分類-中分類
		String szSho4a = getMap().get("SEL_SHO4");	// 商品分類-小分類
		String szSho5a = getMap().get("SEL_SHO5");	// 商品分類-シリーズＡ
		String szSho6a = getMap().get("SEL_SHO6");	// 商品分類-シリーズＢ

		System.out.println("YYYY_MM:"+szYy+szNm+szDd+",YYYY_MM2:"+szYy2+szNm2+szDd2+",START_YM:"+szYmN1+szYmN2+",SEL_CHA1:"+ szCha1+szCha2+szCha3+szCha4);
		System.out.println("SEL_SHO:"+szSho1a+szSho2a+szSho3a+szSho4a+szSho5a+szSho6a);
		System.out.println("SU_GA:"+szSu+",TAIHI:"+szZen+",NR_TYPE:"+szSNr+",RAD_DSP_TYP:"+szDsp);
		System.out.println("RAD_NEN:"+szNen+",RAD_12:"+sz12+",RAD_NEW:"+szNewS+",RAD_NR:"+szRNr+ ",RAD_TEN_DAI:"+szTenDai);
		System.out.println("CHK_C1:"+szChkC1+",CHK_C2:"+szChkC2+",CHK_C3:"+szChkC3+",CHK_C4:"+szChkC4);
		System.out.println("CHK_S1:" +szChkS1+",CHK_S2:"+szChkS2+",CHK_S3:"+szChkS3+",CHK_S4:"+szChkS4+",CHK_S5:"+szChkS5+",CHK_S6:"+szChkS6+",CHK_S7:"+szChkS7);

		// 商品分類２追加に伴う調整　分類２の指定があった場合、選択のない商品集計はしない。
		String szBun_SW2 = "OFF";
		String szBun_SW3 = "OFF";
		String szBun_SW4 = "OFF";
		String szBun_SW5 = "OFF";
		String szBun_SW6 = "OFF";
		for (int i = 1; i <= 10; i++) {
			int szBun = NumberUtils.toInt(getMap().get("SEL_BUN" + Integer.toString(i)));
			String szBS = getMap().get("SEL_BS" + Integer.toString(i));
			if (szBun != 0 && !StringUtils.equals(szBS, "00")) {
				switch (szBun) {
				case 2:
					szBun_SW2 = "on";
					break;
				case 3:
					szBun_SW3 = "on";
					break;
				case 4:
					szBun_SW4 = "on";
					break;
				case 5:
					szBun_SW5 = "on";
					break;
				case 6:
					szBun_SW6 = "on";
					break;
				}
			}
		}
		System.out.println("szBun_SW:" + szBun_SW2 + szBun_SW3 + szBun_SW4 + szBun_SW5 + szBun_SW6);
		if ((StringUtils.equals(szBun_SW2, "on")) && (StringUtils.equals(szChkS2, "on"))) {
			szChkS2 = "off";
		}
		if ((StringUtils.equals(szBun_SW3, "on")) && (StringUtils.equals(szChkS3, "on"))) {
			szChkS3 = "off";
		}
		if ((StringUtils.equals(szBun_SW4, "on")) && (StringUtils.equals(szChkS4, "on"))) {
			szChkS4 = "off";
		}
		if ((StringUtils.equals(szBun_SW5, "on")) && (StringUtils.equals(szChkS5, "on"))) {
			szChkS5 = "off";
		}
		if ((StringUtils.equals(szBun_SW6, "on")) && (StringUtils.equals(szChkS6, "on"))) {
			szChkS6 = "off";
		}
		System.out.println("CHK_S1:" + szChkS1 + ",CHK_S2:" + szChkS2 + ",CHK_S3:" + szChkS3 + ",CHK_S4:" + szChkS4 + ",CHK_S5:" + szChkS5
				+ ",CHK_S6:" + szChkS6 + ",CHK_S7:" + szChkS7);
		// ↑商品分類２追加に伴う調整　集計レベル変更はここまで

		String[] ItemNR = { " AS \"新・リ\"" };
		String[] ItemC1 = { " AS \"チャネル:基本\"", " AS \"チャネル:大\"", " AS \"チャネル:中\"", " AS \"チャネル:小\"" };
		String[] ItemS1 = { " AS \"商品:基本分類\"", " AS \"商品:大分類\"", " AS \"商品:中分類\"", " AS \"商品:小分類\"", " AS \"商品:シリーズA\"", " AS \"商品:シリーズB\"",
				" AS \"商品番号\"", " AS \"商品名称\"" };

		String[] J = { ",NVL(SUM(SU),0)", ",NVL(SUM(GA),0)", ",NVL(SUM(RUI_SU),0)", ",NVL(SUM(RUI_GA),0)" };
		String[] ItemJ = { J[0] + " AS \"数量\"", J[1] + " AS \"売上額\"", J[2] + " AS \"年計:数量\"", J[3] + " AS \"年計:売上額\"" };
		String[] JK = { ",NVL(SUM(SU),0)", ",SUM(TRUNC(GA* RITSU,-1))", ",NVL(SUM(RUI_SU),0)", ",NVL(SUM(RUI_GA_ZEI),0)" };
		String[] ItemJK = { JK[0] + " AS \"数量\"", JK[1] + " AS \"売上額税込\"", JK[2] + " AS \"年計:数量\"", JK[3] + " AS \"年計:売上額税込\"" };
		String[] JZy = { ",NVL(CASE WHEN SUM(ZYY_SU) = 0 THEN 0 ELSE ROUND(SUM(SU) / SUM(ZYY_SU) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_GA) = 0 THEN 0 ELSE ROUND(SUM(GA) / SUM(ZYY_GA) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_RUI_SU) = 0 THEN 0 ELSE ROUND(SUM(RUI_SU) / SUM(ZYY_RUI_SU) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_RUI_GA) = 0 THEN 0 ELSE ROUND(SUM(RUI_GA) / SUM(ZYY_RUI_GA) * 100,1) END,0)" };
		String[] ItemJZy = { JZy[0] + " AS \"数前年比\"", JZy[1] + " AS \"額前年比\"", JZy[2] + " AS \"年計:数前年比\"", JZy[3] + " AS \"年計:額前年比\"" };
		String[] JKZy = { ",NVL(CASE WHEN SUM(ZYY_SU) = 0 THEN 0 ELSE ROUND(SUM(SU) / SUM(ZYY_SU) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_GA) = 0 THEN 0 ELSE ROUND(SUM(TRUNC(GA* RITSU,-1)) / SUM(TRUNC(ZYY_GA* RITSU,-1)) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_RUI_SU) = 0 THEN 0 ELSE ROUND(SUM(RUI_SU) / SUM(ZYY_RUI_SU) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_RUI_GA_ZEI) = 0 THEN 0 ELSE ROUND(SUM(RUI_GA_ZEI) / SUM(ZYY_RUI_GA_ZEI) * 100,1) END,0)" };
		String[] ItemJKZy = { JKZy[0] + " AS \"数前年比\"", JKZy[1] + " AS \"額前年比\"", JKZy[2] + " AS \"年計:数前年比\"", JKZy[3] + " AS \"年計:額前年比\"" };
		String[] JZm = { ",NVL(CASE WHEN SUM(ZMM_SU) = 0 THEN 0 ELSE ROUND(SUM(SU) / SUM(ZMM_SU) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZMM_GA) = 0 THEN 0 ELSE ROUND(SUM(GA) / SUM(ZMM_GA) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_RUI_SU) = 0 THEN 0 ELSE ROUND(SUM(RUI_SU) / SUM(ZYY_RUI_SU) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_RUI_GA) = 0 THEN 0 ELSE ROUND(SUM(RUI_GA) / SUM(ZYY_RUI_GA) * 100,1) END,0)" };
		String[] ItemJZm = { JZm[0] + " AS \"数前月比\"", JZm[1] + " AS \"額前月比\"", JZm[2] + " AS \"年計:数前年比\"", JZm[3] + " AS \"年計:額前年比\"" };
		String[] JKZm = { ",NVL(CASE WHEN SUM(ZMM_SU) = 0 THEN 0 ELSE ROUND(SUM(SU) / SUM(ZMM_SU) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZMM_GA) = 0 THEN 0 ELSE ROUND(SUM(TRUNC(GA* RITSU,-1)) / SUM(TRUNC(ZMM_GA* RITSU,-1)) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_RUI_SU) = 0 THEN 0 ELSE ROUND(SUM(RUI_SU) / SUM(ZYY_RUI_SU) * 100,1) END,0)",
				",NVL(CASE WHEN SUM(ZYY_RUI_GA_ZEI) = 0 THEN 0 ELSE ROUND(SUM(RUI_GA_ZEI) / SUM(ZYY_RUI_GA_ZEI) * 100,1) END,0)" };
		String[] ItemJKZm = { JKZm[0] + " AS \"数前月比\"", JKZm[1] + " AS \"額前月比\"", JKZm[2] + " AS \"年計:数前年比\"", JKZm[3] + " AS \"年計:額前年比\"" };

		//照会データ指定基本・年・月・日別　数値項目作成
		String sqlSuGaA = "\"数:";
		String sqlSuGa1 = "SU";
		String sqlSuGaZY = "ZYY_SU";
		String sqlSuGaZM = "ZMM_SU";
		if (StringUtils.equals(szSu, "2")) {		//額 税抜き
			sqlSuGaA = "\"額:";
			sqlSuGa1 = "GA";
			sqlSuGaZY = "ZYY_GA";
			sqlSuGaZM = "ZMM_GA";
		} else if (StringUtils.equals(szSu, "3")) {	//額税込
			sqlSuGaA = "\"額:";
			if (StringUtils.equals(szNen, "1")) {	//年別
				sqlSuGa1 = "GA_ZEI ";
				sqlSuGaZY = "ZYY_GA_ZEI";
				sqlSuGaZM = "ZMM_GA_ZEI)";
			} else  {
				//sqlSuGa1 = "GA";
				sqlSuGa1 = "TRUNC(GA * RITSU,-1)";
				sqlSuGaZY = "TRUNC(ZYY_GA * RITSU,-1)";
				sqlSuGaZM = "TRUNC(ZMM_GA * RITSU,-1)";
			}
		}
		//月別12点移動和　数/額　前年、前月はなし
		if (StringUtils.equals(szNen, "2")) {			//月別
			if (StringUtils.equals(sz12, "2")) {
				if (StringUtils.equals(szSu, "1")) {		//数量
					sqlSuGaA = "\"数:";
					sqlSuGa1 = "W12_SU";
				} else if (StringUtils.equals(szSu, "2")) {	//額税抜き
					sqlSuGaA = "\"額:";
					sqlSuGa1 = "W12_GA";
				} else if (StringUtils.equals(szSu, "3")) {	//額税込
					sqlSuGaA = "\"額:";
					sqlSuGa1 = "TRUNC(W12_GA * RITSU,-1)";
				}
			}
		}
		String sqlFor = "";
		Object[] sqlFors = {};		// 実績のみ用
		if (StringUtils.equals(szNen, "1")) {  //年別/
			int num1 = Integer.parseInt(szYy);
			int num2 = Integer.parseInt(szYy2);
			for (int i = num1; i <= num2; i++) {
				String col = ",NVL(SUM(CASE WHEN URI_YY = '" + i + "' THEN " + sqlSuGa1 + " ELSE 0 END),0)";
				sqlFor += col + " AS " + sqlSuGaA + i + "\"";
				sqlFors = ArrayUtils.add(sqlFors, col);
				if (StringUtils.equals(szZen, "2")) {  //年別/数量/前年比
					col = ",NVL(CASE WHEN SUM(CASE WHEN URI_YY = '" + i + "' THEN " + sqlSuGaZY + " END) = 0 THEN 0 ELSE (ROUND(SUM(CASE WHEN URI_YY = '" + i + "' THEN " + sqlSuGa1 + " END) / SUM(CASE WHEN URI_YY = '" + i + "' THEN " + sqlSuGaZY + " END) * 100,1)) END,0)";
					sqlFor += col + " AS \"前年比\"";
					sqlFors = ArrayUtils.add(sqlFors, col);
				}
			}
		} else if (StringUtils.equals(szNen, "2")) { //月別 年が変わった場合の対応追加
			// 日付情報取得
			String[] dtArray = CmnDate.getMonthFromTo(szYy + szNm, szYy2 + szNm2);
			for (String dt : dtArray) {
				String col = ",NVL(SUM(CASE WHEN URI_YM = '" + dt + "' THEN " + sqlSuGa1 + " ELSE 0 END),0)";
				sqlFor += col + " AS " + sqlSuGaA + dt.substring(0, 4) + "-" + dt.substring(4, 6) + "\"";
				sqlFors = ArrayUtils.add(sqlFors, col);
				if (StringUtils.equals(szZen, "2")) {  //前年比
					col = ",NVL(CASE WHEN SUM(CASE WHEN URI_YM = '" + dt + "' THEN " + sqlSuGaZY + " END) = 0 THEN 0 ELSE (ROUND(SUM(CASE WHEN URI_YM = '"
							+ dt + "' THEN " + sqlSuGa1 + " END) / SUM(CASE WHEN URI_YM = '" + dt + "' THEN " + sqlSuGaZY + " END) * 100,1)) END,0)";
					sqlFor += col + " AS " + "\"前年比\"";
					sqlFors = ArrayUtils.add(sqlFors, col);
				} else if (StringUtils.equals(szZen, "3")) {  //前月比
					col = ",NVL(CASE WHEN SUM(CASE WHEN URI_YM = '" + dt + "' THEN " + sqlSuGaZM + " END) = 0 THEN 0 ELSE (ROUND(SUM(CASE WHEN URI_YM = '"
							+ dt + "' THEN " + sqlSuGa1 + " END) / SUM(CASE WHEN URI_YM = '" + dt + "' THEN " + sqlSuGaZM + " END) * 100,1)) END,0)";
					sqlFor += col + " AS " + "\"前月比\"";
					sqlFors = ArrayUtils.add(sqlFors, col);
				}
			}
		} else if (StringUtils.equals(szNen, "3")) { //日別 年・月が変わった場合の対応追加
			// 日付情報取得
			String[] dtArray = CmnDate.getDateFromTo(szYy + szNm + szDd, szYy2 + szNm2 + szDd2);
			for (String dt : dtArray) {
				String ym = StringUtils.left(dt, 6);
				String dd = StringUtils.right(dt, 2);
				if (StringUtils.equals(szSu, "3")){ //日別 税込みの場合
					String col = ",NVL(SUM(CASE WHEN URI_YM = '" + ym + "' THEN TRUNC(GA" + dd + "* RITSU,-1) ELSE 0 END),0)";
					sqlFor += col + " AS " + sqlSuGaA + dt.substring(4, 6) + "-" + dt.substring(6, 8) + "\"";
					sqlFors = ArrayUtils.add(sqlFors, col);
				} else {
					String col = ",NVL(SUM(CASE WHEN URI_YM = '" + ym + "' THEN " + sqlSuGa1 + dd + " ELSE 0 END),0)";
					sqlFor += col + " AS " + sqlSuGaA + dt.substring(4, 6) + "-" + dt.substring(6, 8) + "\"";
					sqlFors = ArrayUtils.add(sqlFors, col);
				}
			}
		}

		String sqlItemSu = "";
		String sqlItem = ItemJ[0] + ItemJZy[0] + ItemJ[1] + ItemJZy[1] + ItemJ[2] + ItemJZy[2] + ItemJ[3] + ItemJZy[3];
		Object[] sqlItems = { J[0], JZy[0], J[1], JZy[1], J[2], JZy[2], J[3], JZy[3] };		// 実績のみ表示用
		String sqlTable = " ";
		String sqlTableT = " FROM T_URI U ";
		if (StringUtils.equals(szSu, "3")) {  //税込みの場合
			sqlTableT += " LEFT OUTER JOIN M_ZEIRITSU_MONTH ON (URI_YM = Z_YM) ";
		}
		String sqlYM = "URI_YM";
		String sqlWhereYm = "";
		String UpYm = "";
		//基本形・年別・月別・日別　項目作成
		if (StringUtils.equals(szNen, "4")) {  //基本形
			sqlTable = "URI_SHOHIN_MONTH";
			sqlYM = "URI_YM";
			UpYm = "(select to_char(add_months(to_date(KIJUN_YM||'01','YYYY-MM-DD'),-1),'YYYYMM') from YM_KIJUN_B04 WHERE KIJUN_YM = '"+szYy+szNm+"')";
			sqlWhereYm = " URI_YM = '" + szYy + szNm + "'";
			sqlItemSu = "SU,GA,ZYY_SU,ZYY_GA,ZMM_SU,ZMM_GA,RUI_SU,RUI_GA,ZYY_RUI_SU,ZYY_RUI_GA,W12_SU,W12_GA,SU_1,GA_1,W12_SU_1,W12_GA_1,RUI_GA_ZEI,ZYY_RUI_GA_ZEI";
			if (StringUtils.equals(szSu, "2")) {		//額
				//数値項目
				if (StringUtils.equals(szZen, "2")) {  //前年比
					sqlItem = ItemJ[0] + ItemJZy[0] + ItemJ[1] + ItemJZy[1] + ItemJ[2] + ItemJZy[2] + ItemJ[3] + ItemJZy[3];
					sqlItems = new Object[] { J[0], JZy[0], J[1], JZy[1], J[2], JZy[2], J[3], JZy[3] };
				} else if (StringUtils.equals(szZen, "3")) {  //前月比
					sqlItem = ItemJ[0] + ItemJZm[0] + ItemJ[1] + ItemJZm[1] + ItemJ[2] + ItemJZm[2] + ItemJ[3] + ItemJZm[3];
					sqlItems = new Object[] { J[0], JZm[0], J[1], JZm[1], J[2], JZm[2], J[3], JZm[3] };
				}
			} else if (StringUtils.equals(szSu, "3")) {		//額 税込
				//数値項目
				if (StringUtils.equals(szZen, "2")) {  //前年比
					sqlItem = ItemJK[0] + ItemJKZy[0] + ItemJK[1] + ItemJKZy[1] + ItemJK[2] + ItemJKZy[2] + ItemJK[3] + ItemJKZy[3];
					sqlItems = new Object[] { JK[0], JKZy[0], JK[1], JKZy[1], JK[2], JKZy[2], JK[3], JKZy[3] };
				} else if (StringUtils.equals(szZen, "3")) {  //前月比
					sqlItem = ItemJK[0] + ItemJKZm[0] + ItemJK[1] + ItemJKZm[1] + ItemJK[2] + ItemJKZm[2] + ItemJK[3] + ItemJKZm[3];
					sqlItems = new Object[] { JK[0], JKZm[0], JK[1], JKZm[1], JK[2], JKZm[2], JK[3], JKZm[3] };
				}
			}
		} else if (StringUtils.equals(szNen, "2")) { //月
			sqlTable = "URI_SHOHIN_MONTH";
			sqlYM = "URI_YM";
			UpYm = "(select to_char(add_months(to_date(KIJUN_YM||'01','YYYY-MM-DD'),-1),'YYYYMM') from YM_KIJUN_B04 WHERE KIJUN_YM = '"+szYy+szNm+"')";
			if (StringUtils.equals(szYy+szNm, szYy2+szNm2)) { //
				sqlWhereYm = " URI_YM = '" + szYy + szNm + "' ";
			} else {
				sqlWhereYm = " URI_YM >= '" + szYy + szNm + "' AND URI_YM <= '" + szYy2 + szNm2 + "' ";
			}
			sqlItemSu = "SU,GA,ZYY_SU,ZYY_GA,ZMM_SU,ZMM_GA,RUI_SU,RUI_GA,W12_SU,W12_GA,SU_1,GA_1,W12_SU_1,W12_GA_1";
			sqlItem = sqlFor;
			sqlItems = sqlFors;
		} else if (StringUtils.equals(szNen, "3")) { //日
			sqlTable = "URI_SHOHIN";
			sqlYM = "URI_YM";
			UpYm = "(select to_char(add_months(to_date(KIJUN_YM||'01','YYYY-MM-DD'),-1),'YYYYMM') from YM_KIJUN_B04 WHERE KIJUN_YM = '"+szYy+szNm+"')";
			if (StringUtils.equals(szYy+szNm, szYy2+szNm2)) { //
				sqlWhereYm = " URI_YM = '" + szYy + szNm + "' ";
			} else {
				sqlWhereYm = " URI_YM >= '" + szYy + szNm + "' AND URI_YM <= '" + szYy2 + szNm2 + "' ";
			}
			sqlItemSu = "SU01,GA01,SU02,GA02,SU03,GA03,SU04,GA04,SU05,GA05,SU06,GA06,SU07,GA07,SU08,GA08,SU09,GA09,SU10,GA10,";
			sqlItemSu += "SU11,GA11,SU12,GA12,SU13,GA13,SU14,GA14,SU15,GA15,SU16,GA16,SU17,GA17,SU18,GA18,SU19,GA19,SU20,GA20,";
			sqlItemSu += "SU21,GA21,SU22,GA22,SU23,GA23,SU24,GA24,SU25,GA25,SU26,GA26,SU27,GA27,SU28,GA28,SU29,GA29,SU30,GA30,SU31,GA31,";
			sqlItemSu += "SU01_1,GA01_1,SU02_1,GA02_1,SU03_1,GA03_1,SU04_1,GA04_1,SU05_1,GA05_1,SU06_1,GA06_1,SU07_1,GA07_1,SU08_1,GA08_1,SU09_1,GA09_1,SU10_1,GA10_1,";
			sqlItemSu += "SU11_1,GA11_1,SU12_1,GA12_1,SU13_1,GA13_1,SU14_1,GA14_1,SU15_1,GA15_1,SU16_1,GA16_1,SU17_1,GA17_1,SU18_1,GA18_1,SU19_1,GA19_1,SU20_1,GA20_1,";
			sqlItemSu += "SU21_1,GA21_1,SU22_1,GA22_1,SU23_1,GA23_1,SU24_1,GA24_1,SU25_1,GA25_1,SU26_1,GA26_1,SU27_1,GA27_1,SU28_1,GA28_1,SU29_1,GA29_1,SU30_1,GA30_1,SU31_1,GA31_1";
			sqlItem = sqlFor;
			sqlItems = sqlFors;
		} else if (StringUtils.equals(szNen, "1")) { //年
			sqlTable = "URI_SHOHIN_YEAR";
			sqlTableT = " FROM T_URI U ";
			sqlYM = "URI_YY";
			UpYm = "(select to_char(add_months(to_date(KIJUN_YM||'01','YYYY-MM-DD'),-1),'YYYYMM') from YM_KIJUN_B04 WHERE KIJUN_YM = '"+szYy+"01')";
			if (StringUtils.equals(szYy, szYy2)) { // 開始年＝終了年の場合
				sqlWhereYm = " URI_YY = '" + szYy + "' ";
			} else {
				sqlWhereYm = " URI_YY >= '" + szYy + "' AND URI_YY <= '" + szYy2 + "' ";
			}
			sqlItemSu = "SU,GA,GA_ZEI,ZYY_SU,ZYY_GA,ZYY_GA_ZEI,SU_1,GA_1,GA_ZEI_1";
			sqlItem = sqlFor;
			sqlItems = sqlFors;
		}
		// 全表示・実績のみ対応
		String sqlItemAll = sqlItem;
		Object[] itemsOnly = null;
		if (StringUtils.equals(szDsp, DefineReport.RadioSho1.ID2.getVal())) {
			sqlItem = "";
			for (int i = 0; i < sqlItems.length; i++) {
				itemsOnly = ArrayUtils.add(itemsOnly, "COL" + (i + 1));
				sqlItem += sqlItems[i] + " AS COL" + (i + 1);
			}
		}

		//新製品指定 ※トランザクションテーブル絞込み条件（商品マスタのSTART_YMは無視）
		String sqlWhereNew = "";
		if (StringUtils.equals(szNewS, "2")) {  //新製品指定あり場合ラジオボタン
			// 20171006 変数は残す。
			//	sqlWhereNew = " AND U.START_YM >='" + szYmN1 + "' AND U.START_YM <='" + szYmN2 + "'";
		}

		//新製品指定 ※トランザクションテーブル絞込み条件（商品マスタの発売日（START_YM）を利用 20171130）
		String sqlJoinShohin = "";
		String sqlJoinMShohin = "";
		if (StringUtils.equals(szNewS, "2")) {  //新製品指定あり場合ラジオボタン
			sqlJoinShohin = " AND S.START_YM BETWEEN '" + szYmN1 + "' AND '" + szYmN2 + "' ";
			sqlJoinMShohin = " EXISTS (SELECT 1 FROM  M_SHOHIN SS WHERE S.SHOHIN_CD = SS.SHOHIN_CD AND SS.START_YM BETWEEN '" + szYmN1 + "' AND '" + szYmN2 + "') AND ";
		}

		//新規・リピート選択/
		String sqlNR0 = "";
		String sqlNRL = "";
		String srtNR = "";
		String grpNR = "";
		String sqlWhereNR = "";
		if (StringUtils.equals(szRNr, "2")) {  //するの場合ラジオボタン
			sqlNR0 = "MAX(NRMEI)" + ItemNR[0] + ",";
			sqlNRL = "''" + ItemNR[0] + ",";
			grpNR = "NRKBN,";
			srtNR = "NRKBN||";
			if (!StringUtils.equals(szSNr, "0")) {  //コンボBOX 0:すべて 1:新規 2:リピート
				sqlWhereNR = " AND NRKBN ='" + szSNr + "'";
			}
		}

		// 商品分類 Where検索条件
		String sqlWhereSho = getWhereShoBun();

		//チャネル 検索条件
		String sqlWCha1 = "";
		String sqlWCha2 = ",CASE WHEN C.CHANCLS1='70' THEN '50' ELSE C.CHANCLS1 END,C.CHANCLS2,C.CHANCLS3,C.CHANCLS4,C.MEISYO1,C.MEISYO2,C.MEISYO3,C.MEISYO4 ";
		if (StringUtils.equals(szDsp, DefineReport.RadioSho1.ID1.getVal())) {
			// 全表示の場合
			sqlWCha2 = ",CASE WHEN M.CHANCLS1='70' THEN '50' ELSE M.CHANCLS1 END,M.CHANCLS2,M.CHANCLS3,M.CHANCLS4,M.MEISYO1,M.MEISYO2,M.MEISYO3,M.MEISYO4 ";
		}
		String sqlWSHOPCHA = "CHANEL_CD1,CHANEL_CD2,CHANEL_CD3,CHANEL_CD4,MEISYO1,MEISYO2,MEISYO3,MEISYO4,";
		String sqlKBN = "KBN = 'A'";
		if (StringUtils.equals(szTenDai, "2")) {  //チャネルの店舗区分
			sqlKBN = "KBN = 'T'";
		}
		if (StringUtils.equals(szCha1, "20")) { //店舗選択の場合
			if (StringUtils.equals(szTenDai, "3")) { //店舗大分類選択がエリア→店舗の場合
				sqlWSHOPCHA = "CHANEL_CD1,CHANEL_CD2,CHANEL_CD4,CHANEL_CD4,MEISYO1,MEISYO2,MEISYO4,MEISYO4,";
			}
			if (StringUtils.equals(szTenDai, "4")) { //店舗大分類選択がテナント→店舗の場合
				sqlWSHOPCHA = "CHANEL_CD1,CHANEL_CD3,CHANEL_CD4,CHANEL_CD4,MEISYO1,MEISYO3,MEISYO4,MEISYO4,";
			}
			if (StringUtils.equals(szTenDai, "5")) { //店舗大分類選択が店舗の場合
				sqlWSHOPCHA = "CHANEL_CD1,CHANEL_CD4,CHANEL_CD4,CHANEL_CD4,MEISYO1,MEISYO4,MEISYO4,MEISYO4,";
			}
		}
		if (!StringUtils.equals(szCha1, "00")) {  //チャネル基本選択
			if (StringUtils.equals(szCha1, "15")) {  //チャネル通販+店舗
				sqlWCha1 += "AND (C.CHANCLS1 = '10' or C.CHANCLS1 = '20' ) ";
				sqlWCha2 = ",'15','00','00','00','通販+店舗','通販+店舗','通販+店舗','通販+店舗' ";
			}else if (StringUtils.equals(szCha1, "50")) {  // 他社売上の場合
				//チャネル大選択
				if (StringUtils.equals(szCha2, "00")) {  // すべて
					sqlWCha1 += "AND (C.CHANCLS1 = '50' or C.CHANCLS1 = '70' ) ";
				} else { // その他
					// 基本分類（２桁）を設定
					sqlWCha1 += "AND C.CHANCLS1 = '"+StringUtils.left(szCha2, 2)+"' ";
					// チャネル大（３桁）加工
					szCha2 = StringUtils.right(szCha2, 3);
				}
			} else {
				sqlWCha1 += "AND C.CHANCLS1 = '" + szCha1 + "' ";
			}
		}

		if (!StringUtils.equals(szCha2, "00")) { //チャネル大選択
			sqlWCha1 += "AND C.CHANCLS2 = '" + szCha2 + "' ";
		}
		if (!StringUtils.equals(szCha3, "00")) { //チャネル中選択
			sqlWCha1 += "AND C.CHANCLS3 = '" + szCha3 + "' ";
		}
		if (!StringUtils.equals(szCha4, "00")) { //チャネル小選択
			sqlWCha1 += "AND C.CHANCLS4 = '" + szCha4 + "' ";
		}

		String sql_Uion = "";
		String sql_UionNR = "";
		String sql_UionC1 = "";
		String sql_UionC2 = "";
		String sql_UionC3 = "";
		String sql_UionC4 = "";
		String sql_UionS1 = "";
		String sql_UionS2 = "";
		String sql_UionS3 = "";
		String sql_UionS4 = "";
		String sql_UionS5 = "";
		String sql_UionS6 = "";
		String sql_UionAll = "";

		String sqlsrtC = "";
		String sqlsrtS = "";
		String sqlcha0 = "";
		String sqlsho0 = "";
		String grpcha0 = "";
		String grpsho0 = "";
		String sqlcha1 = "";
		String sqlcha2 = "";
		String sqlcha3 = "";
		String sqlcha4 = "";
		String sqlsho1 = "";
		String sqlsho2 = "";
		String sqlsho3 = "";
		String sqlsho4 = "";
		String sqlsho5 = "";
		String sqlsho6 = "";

		String sqlchaL = "";
		String sqlshoL = "";

		String sqlEX_90 = " WHERE BUN1_SHOHIN_CD <> '90' ";

		//商品選択用1 チャネルの合計行用に空列セット
		sqlshoL = "'' " + ItemS1[0];
		if (StringUtils.equals(szChkS2, "on") || StringUtils.equals(szBun_SW2, "on")) {
			sqlshoL += ",'' " + ItemS1[1];
		}
		if (StringUtils.equals(szChkS3, "on") || StringUtils.equals(szBun_SW3, "on")) {
			sqlshoL += ",'' " + ItemS1[2];
		}
		if (StringUtils.equals(szChkS4, "on") || StringUtils.equals(szBun_SW4, "on")) {
			sqlshoL += ",'' " + ItemS1[3];
		}
		if (StringUtils.equals(szChkS5, "on") || StringUtils.equals(szBun_SW5, "on")) {
			sqlshoL += ",'' " + ItemS1[4];
		}
		if (StringUtils.equals(szChkS6, "on") || StringUtils.equals(szBun_SW6, "on")) {
			sqlshoL += ",'' " + ItemS1[5];
		}
		// 商品 分類指定　商品チェックの場合
		if (StringUtils.equals(szChkS7, "on")) {
			sqlshoL += ",'' " + ItemS1[6] + ",'' " + ItemS1[7];
		}

		//チャネルの集計レベル　必ず上からチェックあり
		if (StringUtils.equals(szChkC1, "on")) {							//基本チェック
			sqlsrtC = "CHANCLS1||";
			sqlcha0 = "TRIM(MAX(MEISYO1)) " + ItemC1[0] + ",";
			grpcha0 = "CHANCLS1,";
			sqlchaL = "'' " + ItemC1[0] + ",";
			sqlcha1 = "'計:'||TRIM(MAX(MEISYO1)) " + ItemC1[0] + ",";
			if (StringUtils.equals(szChkC2, "on")) {						//大チェック
				sqlsrtC += "CHANCLS2||";
				sqlcha0 += "TRIM(MAX(MEISYO2)) " + ItemC1[1] + ",";
				grpcha0 += "CHANCLS2,";
				sqlchaL += "'' " + ItemC1[1] + ",";
				sqlcha1 += "'' " + ItemC1[1] + ",";
				sqlcha2 = "'' " + ItemC1[0] + ", '計:'||TRIM(MAX(MEISYO2)) " + ItemC1[1] + ",";
				if (StringUtils.equals(szChkC3, "on")) {					//中チェック
					sqlsrtC += "CHANCLS3||";
					sqlcha0 += "TRIM(MAX(MEISYO3)) " + ItemC1[2] + ",";
					grpcha0 += "CHANCLS3,";
					sqlchaL += "'' " + ItemC1[2] + ",";
					sqlcha1 += "'' " + ItemC1[2] + ",";
					sqlcha2 += "'' " + ItemC1[2] + ",";
					sqlcha3 = " '' " + ItemC1[0] + ",'' " + ItemC1[1] + ",'計:'||TRIM(MAX(MEISYO3)) " + ItemC1[2] + ", ";
					if (StringUtils.equals(szChkC4, "on")) {
						sqlsrtC += "CHANCLS4||";
						sqlcha0 += "TRIM(MAX(MEISYO4)) " + ItemC1[3] + ",";
						grpcha0 += "CHANCLS4,";
						sqlchaL += "'' " + ItemC1[3] + ",";
						sqlcha1 += "'' " + ItemC1[3] + ",";
						sqlcha2 += "'' " + ItemC1[3] + ",";
						sqlcha3 += "'' " + ItemC1[3] + ",";
						sqlcha4 = "'' " + ItemC1[0] + ",'' " + ItemC1[1] + ",'' " + ItemC1[2] + ", '計:'||TRIM(MAX(MEISYO4)) " + ItemC1[3] + ", ";
						//チャネル小分類集計の場合
						if (StringUtils.equals(szCha4, "00")) {
							sql_UionC4 = " UNION ALL SELECT " + srtNR + "CHANCLS1||CHANCLS2||CHANCLS3||CHANCLS4||'ZZ' AS SORT," + sqlNRL + sqlcha4;
							sql_UionC4 += sqlshoL + sqlItem + sqlTableT + sqlEX_90;
							sql_UionC4 += " GROUP BY " + grpNR + "CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4 ";
						}
					}
					//チャネル中分類集計の場合
					if (StringUtils.equals(szCha3, "00")) {
						sql_UionC3 = " UNION ALL SELECT " + srtNR + "CHANCLS1||CHANCLS2||CHANCLS3||'ZZ' AS SORT," + sqlNRL + sqlcha3;
						sql_UionC3 += sqlshoL + sqlItem + sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + "CHANCLS1,CHANCLS2,CHANCLS3 ";
					}
				}
				//チャネル大分類集計の場合
				if (StringUtils.equals(szCha2, "00")) {
					sql_UionC2 = " UNION ALL SELECT " + srtNR + "CHANCLS1||CHANCLS2||'ZZ' AS SORT," + sqlNRL + sqlcha2;
					sql_UionC2 += sqlshoL + sqlItem + sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + "CHANCLS1,CHANCLS2 ";
				}
			}
			if (StringUtils.equals(szCha1, "00")) {
				sql_UionC1 = " UNION ALL SELECT " + srtNR + "CHANCLS1||'ZZ'" + " AS SORT," + sqlNRL + sqlcha1;
				sql_UionC1 += sqlshoL + sqlItem + sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + "CHANCLS1 ";
			}
		}

		// 新規・リピート集計行
		if (StringUtils.equals(szRNr, "2") && StringUtils.equals(szSNr, "0")) {
			sql_UionNR = " UNION ALL SELECT NRKBN||'ZZ'" + " AS SORT,'計:'||" + sqlNR0 + sqlchaL + sqlshoL + sqlItem;
			sql_UionNR += sqlTableT + sqlEX_90 + " GROUP BY NRKBN";
		}

		//商品分類指定　集計レベル反映
		sqlsho0 = "COALESCE(TRIM(MAX(BUN1_SHOHIN_MEI)),'不明') " + ItemS1[0];
		grpsho0 = "BUN1_SHOHIN_CD";
		sqlsrtS = "BUN1_SHOHIN_CD";
		sqlsho1 = "'計:'||COALESCE(TRIM(MAX(BUN1_SHOHIN_MEI)),'不明')" + ItemS1[0];
		sqlsho2 = "''" + ItemS1[0];
		sqlsho3 = "''" + ItemS1[0];
		sqlsho4 = "''" + ItemS1[0];
		sqlsho5 = "''" + ItemS1[0];
		sqlsho6 = "''" + ItemS1[0];

		if (StringUtils.equals(szChkS2, "on")) {
			sqlsrtS = "BUN2_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN2_SHOHIN_MEI)),'不明')" + ItemS1[1];
			grpsho0 += ",BUN2_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[1];
			sqlsho2 += ",'計:'||COALESCE(TRIM(MAX(BUN2_SHOHIN_MEI)),'不明')" + ItemS1[1];
			sqlsho3 += ",''" + ItemS1[1];
			sqlsho4 += ",''" + ItemS1[1];
			sqlsho5 += ",''" + ItemS1[1];
			sqlsho6 += ",''" + ItemS1[1];
		}
		if (StringUtils.equals(szBun_SW2, "on")) {
			sqlsrtS += "||BUN2_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN2_SHOHIN_MEI)),'不明')" + ItemS1[1];
			grpsho0 += ",BUN2_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[1];
			sqlsho2 += ",''" + ItemS1[1];
			sqlsho3 += ",''" + ItemS1[1];
			sqlsho4 += ",''" + ItemS1[1];
			sqlsho5 += ",''" + ItemS1[1];
			sqlsho6 += ",''" + ItemS1[1];
		}

		if (StringUtils.equals(szChkS3, "on")) {
			sqlsrtS = "BUN3_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN3_SHOHIN_MEI)),'不明')" + ItemS1[2];
			grpsho0 += ",BUN3_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[2];
			sqlsho2 += ",''" + ItemS1[2];
			sqlsho3 += ",'計:'||COALESCE(TRIM(MAX(BUN3_SHOHIN_MEI)),'不明')" + ItemS1[2];
			sqlsho4 += ",''" + ItemS1[2];
			sqlsho5 += ",''" + ItemS1[2];
			sqlsho6 += ",''" + ItemS1[2];
		}
		if (StringUtils.equals(szBun_SW3, "on")) {
			sqlsrtS += "||BUN3_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN3_SHOHIN_MEI)),'不明')" + ItemS1[2];
			grpsho0 += ",BUN3_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[2];
			sqlsho2 += ",''" + ItemS1[2];
			sqlsho3 += ",''" + ItemS1[2];
			sqlsho4 += ",''" + ItemS1[2];
			sqlsho5 += ",''" + ItemS1[2];
			sqlsho6 += ",''" + ItemS1[2];
		}

		if (StringUtils.equals(szChkS4, "on")) {
			sqlsrtS = "BUN4_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN4_SHOHIN_MEI)),'不明')" + ItemS1[3];
			grpsho0 += ",BUN4_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[3];
			sqlsho2 += ",''" + ItemS1[3];
			sqlsho3 += ",''" + ItemS1[3];
			sqlsho4 += ",'計:'||COALESCE(TRIM(MAX(BUN4_SHOHIN_MEI)),'不明')" + ItemS1[3];
			sqlsho5 += ",''" + ItemS1[3];
			sqlsho6 += ",''" + ItemS1[3];
		}
		if (StringUtils.equals(szBun_SW4, "on")) {
			sqlsrtS += "||BUN4_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN4_SHOHIN_MEI)),'不明')" + ItemS1[3];
			grpsho0 += ",BUN4_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[3];
			sqlsho2 += ",''" + ItemS1[3];
			sqlsho3 += ",''" + ItemS1[3];
			sqlsho4 += ",''" + ItemS1[3];
			sqlsho5 += ",''" + ItemS1[3];
			sqlsho6 += ",''" + ItemS1[3];
		}

		if (StringUtils.equals(szChkS5, "on")) {
			sqlsrtS = "BUN5_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN5_SHOHIN_MEI)),'不明')" + ItemS1[4];
			grpsho0 += ",BUN5_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[4];
			sqlsho2 += ",''" + ItemS1[4];
			sqlsho3 += ",''" + ItemS1[4];
			sqlsho4 += ",''" + ItemS1[4];
			sqlsho5 += ",'計:'||COALESCE(TRIM(MAX(BUN5_SHOHIN_MEI)),'不明')" + ItemS1[4];
			sqlsho6 += ",''" + ItemS1[4];
		}
		if (StringUtils.equals(szBun_SW5, "on")) {
			sqlsrtS += "||BUN5_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN5_SHOHIN_MEI)),'不明')" + ItemS1[4];
			grpsho0 += ",BUN5_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[4];
			sqlsho2 += ",''" + ItemS1[4];
			sqlsho3 += ",''" + ItemS1[4];
			sqlsho4 += ",''" + ItemS1[4];
			sqlsho5 += ",''" + ItemS1[4];
			sqlsho6 += ",''" + ItemS1[4];
		}

		if (StringUtils.equals(szChkS6, "on")) {
			sqlsrtS = "BUN6_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN6_SHOHIN_MEI)),'不明')" + ItemS1[5];
			grpsho0 += ",BUN6_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[5];
			sqlsho2 += ",''" + ItemS1[5];
			sqlsho3 += ",''" + ItemS1[5];
			sqlsho4 += ",''" + ItemS1[5];
			sqlsho5 += ",''" + ItemS1[5];
			sqlsho6 += ",'計:'||COALESCE(TRIM(MAX(BUN6_SHOHIN_MEI)),'不明')" + ItemS1[5];
		}
		if (StringUtils.equals(szBun_SW6, "on")) {
			sqlsrtS += "||BUN6_SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(BUN6_SHOHIN_MEI)),'不明')" + ItemS1[5];
			grpsho0 += ",BUN6_SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[5];
			sqlsho2 += ",''" + ItemS1[5];
			sqlsho3 += ",''" + ItemS1[5];
			sqlsho4 += ",''" + ItemS1[5];
			sqlsho5 += ",''" + ItemS1[5];
			sqlsho6 += ",''" + ItemS1[5];
		}

		//商品分類指定　商品チェック選択の場合
		if (StringUtils.equals(szChkS7, "on")) {
			sqlsrtS += "||SHOHIN_CD";
			sqlsho0 += ",COALESCE(TRIM(MAX(SHOHIN_BANGO)),'不明')" + ItemS1[6] + ", COALESCE(TRIM(MAX(SHOHIN_MEI)),'')" + ItemS1[7];
			grpsho0 += ",SHOHIN_CD";
			sqlsho1 += ",''" + ItemS1[6] + ",''" + ItemS1[7];
			sqlsho2 += ",''" + ItemS1[6] + ",''" + ItemS1[7];
			sqlsho3 += ",''" + ItemS1[6] + ",''" + ItemS1[7];
			sqlsho4 += ",''" + ItemS1[6] + ",''" + ItemS1[7];
			sqlsho5 += ",''" + ItemS1[6] + ",''" + ItemS1[7];
			sqlsho6 += ",''" + ItemS1[6] + ",''" + ItemS1[7];
		}

		// 商品　集計行
		// すべて選択、かつ下位列表示があり、その下位列が分類２選択による表示ではない場合集計実施
		if (StringUtils.equals(szChkS1, "on") && StringUtils.equals(szSho1a, "00")
				&& (ArrayUtils.contains(new String[] { szChkS2, szChkS3, szChkS4, szChkS5, szChkS6, szChkS7 }, "on"))
				&& (!ArrayUtils.contains(new String[] { szBun_SW2, szBun_SW3, szBun_SW4, szBun_SW5, szBun_SW6 }, "on"))) {
			sql_UionS1 = " UNION ALL SELECT " + srtNR + sqlsrtC + "BUN1_SHOHIN_CD||'ZZ' AS SORT," + sqlNRL + sqlchaL + sqlsho1 + sqlItem;
			sql_UionS1 += sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + grpcha0 + "BUN1_SHOHIN_CD";
		}
		if (StringUtils.equals(szChkS2, "on") && StringUtils.equals(szSho2a, "00")
				&& (ArrayUtils.contains(new String[] { szChkS3, szChkS4, szChkS5, szChkS6, szChkS7 }, "on"))
				&& (!ArrayUtils.contains(new String[] { szBun_SW3, szBun_SW4, szBun_SW5, szBun_SW6 }, "on"))) {
			sql_UionS2 = " UNION ALL SELECT " + srtNR + sqlsrtC + "BUN2_SHOHIN_CD||'ZZ' AS SORT," + sqlNRL + sqlchaL + sqlsho2 + sqlItem;
			sql_UionS2 += sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + grpcha0 + "BUN2_SHOHIN_CD";
		}
		if (StringUtils.equals(szChkS3, "on") && StringUtils.equals(szSho3a, "00")
				&& (ArrayUtils.contains(new String[] { szChkS4, szChkS5, szChkS6, szChkS7 }, "on"))
				&& (!ArrayUtils.contains(new String[] { szBun_SW4, szBun_SW5, szBun_SW6 }, "on"))) {
			sql_UionS3 = " UNION ALL SELECT " + srtNR + sqlsrtC + "BUN3_SHOHIN_CD||'ZZ' AS SORT," + sqlNRL + sqlchaL + sqlsho3 + sqlItem;
			sql_UionS3 += sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + grpcha0 + "BUN3_SHOHIN_CD";
		}
		if (StringUtils.equals(szChkS4, "on") && StringUtils.equals(szSho4a, "00")
				&& (ArrayUtils.contains(new String[] { szChkS5, szChkS6, szChkS7 }, "on"))
				&& (!ArrayUtils.contains(new String[] { szBun_SW5, szBun_SW6 }, "on"))) {
			sql_UionS4 = " UNION ALL SELECT " + srtNR + sqlsrtC + "BUN4_SHOHIN_CD||'ZZ' AS SORT," + sqlNRL + sqlchaL + sqlsho4 + sqlItem;
			sql_UionS4 += sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + grpcha0 + "BUN4_SHOHIN_CD";
		}
		if (StringUtils.equals(szChkS5, "on") && StringUtils.equals(szSho5a, "00") && (ArrayUtils.contains(new String[] { szChkS6, szChkS7 }, "on"))
				&& (!ArrayUtils.contains(new String[] { szBun_SW6 }, "on"))) {
			sql_UionS5 = " UNION ALL SELECT " + srtNR + sqlsrtC + "BUN5_SHOHIN_CD||'ZZ' AS SORT," + sqlNRL + sqlchaL + sqlsho5 + sqlItem;
			sql_UionS5 += sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + grpcha0 + "BUN5_SHOHIN_CD";
		}
		if (StringUtils.equals(szChkS6, "on") && StringUtils.equals(szSho6a, "00") && (ArrayUtils.contains(new String[] { szChkS7 }, "on"))) {
			sql_UionS6 = " UNION ALL SELECT " + srtNR + sqlsrtC + "BUN6_SHOHIN_CD||'ZZ' AS SORT," + sqlNRL + sqlchaL + sqlsho6 + sqlItem;
			sql_UionS6 += sqlTableT + sqlEX_90 + " GROUP BY " + grpNR + grpcha0 + "BUN6_SHOHIN_CD";
		}

		// データ行 商品または分類項目表示
		sql_Uion = "SELECT " + srtNR + sqlsrtC + sqlsrtS + " AS SORT," + sqlNR0 + sqlcha0 + sqlsho0 + sqlItem + sqlTableT;
		sql_Uion += " GROUP BY " + grpNR + grpcha0 + grpsho0;
		// 総合計
		sql_UionAll = "SELECT 'ZZZZZZ' AS SORT, " + StringUtils.replaceOnce(sqlNRL + sqlchaL + sqlshoL + sqlItemAll, "''", "'総合計'");
		sql_UionAll += sqlTableT + sqlEX_90;

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		// 表示形式指定
		if (StringUtils.equals(szDsp, DefineReport.RadioSho1.ID1.getVal())) {			// 全表示 kako
			// チャネルマスタ
			sbSQL.append("WITH WCHANEL(CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4,CHAN_ORDER) AS ( ");
			sbSQL.append(" SELECT " + sqlWSHOPCHA + "CHAN_ORDER");
			sbSQL.append(" FROM M_CHANEL2 C where " + sqlKBN + "),   ");
			sbSQL.append("MCHANEL(CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4,CHAN_ORDER) AS ( ");
			sbSQL.append(" SELECT CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4,CHAN_ORDER");
			sbSQL.append(" FROM WCHANEL C " + StringUtils.replaceOnce(sqlWCha1, "AND", "WHERE") + "),   ");
			// 商品マスタ
			sbSQL.append("MSHOHIN(BUN1_SHOHIN_CD,BUN1_SHOHIN_MEI,BUN2_SHOHIN_CD,BUN2_SHOHIN_MEI,BUN3_SHOHIN_CD,BUN3_SHOHIN_MEI");
			sbSQL.append("       ,BUN4_SHOHIN_CD,BUN4_SHOHIN_MEI,BUN5_SHOHIN_CD,BUN5_SHOHIN_MEI,BUN6_SHOHIN_CD,BUN6_SHOHIN_MEI");
			sbSQL.append("       ,SHOHIN_BANGO,SHOHIN_CD,SHOHIN_MEI,UP_TO_YM) AS ( ");
			sbSQL.append(" SELECT BUN1_SHOHIN_CD,BUN1_SHOHIN_MEI,BUN2_SHOHIN_CD,BUN2_SHOHIN_MEI,BUN3_SHOHIN_CD,BUN3_SHOHIN_MEI");
			sbSQL.append("       ,BUN4_SHOHIN_CD,BUN4_SHOHIN_MEI,BUN5_SHOHIN_CD,BUN5_SHOHIN_MEI,BUN6_SHOHIN_CD,BUN6_SHOHIN_MEI");
			sbSQL.append("       ,SHOHIN_BANGO,SHOHIN_CD,SHOHIN_MEI,UP_TO_YM");
			sbSQL.append(" FROM M_SHOHIN_SHU S WHERE ");
			sbSQL.append(sqlJoinMShohin);	// 20171030 商品マスタの発売日基準
			sbSQL.append(" UP_TO_YM >= " +UpYm+ " " + StringUtils.replaceOnce(sqlWhereSho, "AND", "AND") + " ),   ");
			// トランザクション抽出
			sbSQL.append("W_URI(" + sqlYM + ",NRKBN,CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4");
			sbSQL.append("      ,BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD,SHOHIN_CD");
			sbSQL.append("      ," + sqlItemSu + ") AS (");
			sbSQL.append(" SELECT " + sqlYM + ",NRKBN,C.CHANCLS1,C.CHANCLS2,C.CHANCLS3,C.CHANCLS4");
			sbSQL.append("       ,S.BUN1_SHOHIN_CD,S.BUN2_SHOHIN_CD,S.BUN3_SHOHIN_CD,S.BUN4_SHOHIN_CD,S.BUN5_SHOHIN_CD,S.BUN6_SHOHIN_CD,S.SHOHIN_CD");
			sbSQL.append("       ," + sqlItemSu);
			sbSQL.append(" FROM " + sqlTable + " U");
			sbSQL.append(" INNER JOIN MSHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD)");	// 20171130 商品マスタの発売日基準
			sbSQL.append(" INNER JOIN MCHANEL C ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 or (U.CHANCLS3 = C.CHANCLS2 and U.CHANCLS4 = C.CHANCLS3)))");
			sbSQL.append(" WHERE " + sqlWhereYm + sqlWhereNR + sqlWhereNew + "),   ");
			// 新規・リピート抽出
			sbSQL.append("MURI(NRKBN,NRMEI) AS (");
			sbSQL.append(" SELECT NRKBN,CASE WHEN NRKBN = '1' THEN '新規' WHEN NRKBN = '2' THEN 'リピート' ELSE '' END");
			sbSQL.append(" FROM W_URI GROUP BY NRKBN),   ");
			// マスタ結合
			sbSQL.append("MST(NRKBN,NRMEI,CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4,CHAN_ORDER");
			sbSQL.append("   ,BUN1_SHOHIN_CD,BUN1_SHOHIN_MEI,BUN2_SHOHIN_CD,BUN2_SHOHIN_MEI,BUN3_SHOHIN_CD,BUN3_SHOHIN_MEI");
			sbSQL.append("   ,BUN4_SHOHIN_CD,BUN4_SHOHIN_MEI,BUN5_SHOHIN_CD,BUN5_SHOHIN_MEI,BUN6_SHOHIN_CD,BUN6_SHOHIN_MEI");
			sbSQL.append("   ,SHOHIN_BANGO,SHOHIN_CD,SHOHIN_MEI,UP_TO_YM) AS ( ");
			sbSQL.append(" SELECT * FROM MURI cross join MCHANEL cross join MSHOHIN),   ");
			// トランザクション結合
			sbSQL.append("T_URI(" + sqlYM + ",NRKBN,NRMEI,CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4");
			sbSQL.append("     ,BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD");
			sbSQL.append("     ,BUN1_SHOHIN_MEI,BUN2_SHOHIN_MEI,BUN3_SHOHIN_MEI,BUN4_SHOHIN_MEI,BUN5_SHOHIN_MEI,BUN6_SHOHIN_MEI");
			sbSQL.append("     ,SHOHIN_BANGO,SHOHIN_CD,SHOHIN_MEI," + sqlItemSu + ") AS (");
			sbSQL.append(" SELECT " + sqlYM + ",CASE WHEN M.NRKBN='0' THEN '3' ELSE M.NRKBN END,M.NRMEI");
			sbSQL.append(sqlWCha2);// 基本分類：通販＋店舗対応
			//sbSQL.append("  ,CASE WHEN M.CHANCLS1='70' THEN '50' ELSE M.CHANCLS1 END,M.CHANCLS2,M.CHANCLS3,M.CHANCLS4,M.MEISYO1,M.MEISYO2,M.MEISYO3,M.MEISYO4");
			sbSQL.append("  ,M.BUN1_SHOHIN_CD,M.BUN2_SHOHIN_CD,M.BUN3_SHOHIN_CD,M.BUN4_SHOHIN_CD,M.BUN5_SHOHIN_CD,M.BUN6_SHOHIN_CD");
			sbSQL.append("  ,M.BUN1_SHOHIN_MEI,M.BUN2_SHOHIN_MEI,M.BUN3_SHOHIN_MEI,M.BUN4_SHOHIN_MEI,M.BUN5_SHOHIN_MEI,M.BUN6_SHOHIN_MEI");
			sbSQL.append("  ,M.SHOHIN_BANGO,M.SHOHIN_CD,M.SHOHIN_MEI," + sqlItemSu);
			sbSQL.append(" FROM MST M LEFT OUTER JOIN W_URI U");
			sbSQL.append("   ON (U.NRKBN = M.NRKBN AND U.SHOHIN_CD = M.SHOHIN_CD");
			sbSQL.append("   AND U.CHANCLS1 = M.CHANCLS1 AND U.CHANCLS2 = M.CHANCLS2 AND U.CHANCLS3 = M.CHANCLS3 AND U.CHANCLS4 = M.CHANCLS4)");
			sbSQL.append(") ");
			sbSQL.append(sql_UionAll);					// 総合計行
			// データ行 商品または分類項目表示
			sbSQL.append(" UNION ALL " + sql_Uion);
			// 各種小計行
			sbSQL.append(sql_UionNR);
			sbSQL.append(sql_UionC1);
			sbSQL.append(sql_UionC2);
			sbSQL.append(sql_UionC3);
			sbSQL.append(sql_UionC4);
			sbSQL.append(sql_UionS1);
			sbSQL.append(sql_UionS2);
			sbSQL.append(sql_UionS3);
			sbSQL.append(sql_UionS4);
			sbSQL.append(sql_UionS5);
			sbSQL.append(sql_UionS6);
		} else if (StringUtils.equals(szDsp, DefineReport.RadioSho1.ID2.getVal())) {	// 実績のみ
			sbSQL.append("WITH WCHANEL(CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4,CHAN_ORDER) AS ( ");
			sbSQL.append(" SELECT " + sqlWSHOPCHA + "CHAN_ORDER");
			sbSQL.append(" FROM M_CHANEL2 C where " + sqlKBN + "),   ");
			sbSQL.append("T_URI(" + sqlYM + ",CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4");
			sbSQL.append(" ,BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD");
			sbSQL.append(" ,BUN1_SHOHIN_MEI,BUN2_SHOHIN_MEI,BUN3_SHOHIN_MEI,BUN4_SHOHIN_MEI,BUN5_SHOHIN_MEI,BUN6_SHOHIN_MEI");
			sbSQL.append(" ,SHOHIN_BANGO,SHOHIN_CD,SHOHIN_MEI,START_YM,NRKBN,NRMEI," + sqlItemSu + ") AS (");
			sbSQL.append(" SELECT " + sqlYM +sqlWCha2 );
			sbSQL.append(" ,S.BUN1_SHOHIN_CD,S.BUN2_SHOHIN_CD,S.BUN3_SHOHIN_CD,S.BUN4_SHOHIN_CD,S.BUN5_SHOHIN_CD,S.BUN6_SHOHIN_CD");
			sbSQL.append(" ,S.BUN1_SHOHIN_MEI,S.BUN2_SHOHIN_MEI,S.BUN3_SHOHIN_MEI,S.BUN4_SHOHIN_MEI,S.BUN5_SHOHIN_MEI,S.BUN6_SHOHIN_MEI");
			sbSQL.append(" ,SHOHIN_BANGO,U.SHOHIN_CD,SHOHIN_MEI,S.START_YM,CASE WHEN NRKBN='0' THEN '3' ELSE NRKBN END");
			sbSQL.append(" ,CASE WHEN NRKBN = '1' THEN '新規' WHEN NRKBN = '2' THEN 'リピート' ELSE '' END," + sqlItemSu);
			sbSQL.append(" FROM " + sqlTable + " U");
			sbSQL.append(" INNER JOIN M_SHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD"+sqlJoinShohin+")");	// 20171130 商品マスタの発売日基準
			sbSQL.append(" INNER JOIN WCHANEL C ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 or (U.CHANCLS3 = C.CHANCLS2 and U.CHANCLS4 = C.CHANCLS3)))");
			sbSQL.append(" WHERE " + sqlWhereYm + sqlWCha1 + sqlWhereSho + sqlWhereNew + sqlWhereNR + ") ");
			sbSQL.append(sql_UionAll);						// 総合計行
			sbSQL.append(" UNION ALL SELECT * FROM (");		// 結果全列0を除外する
			// データ行 商品または分類項目表示
			sbSQL.append(sql_Uion);
			// 各種小計行
			sbSQL.append(sql_UionNR);
			sbSQL.append(sql_UionC1);
			sbSQL.append(sql_UionC2);
			sbSQL.append(sql_UionC3);
			sbSQL.append(sql_UionC4);
			sbSQL.append(sql_UionS1);
			sbSQL.append(sql_UionS2);
			sbSQL.append(sql_UionS3);
			sbSQL.append(sql_UionS4);
			sbSQL.append(sql_UionS5);
			sbSQL.append(sql_UionS6);
			sbSQL.append(") WK WHERE " + StringUtils.join(itemsOnly, "+") + " > 0");
		} else if (StringUtils.equals(szDsp, DefineReport.RadioSho1.ID3.getVal())) {	// 実績tsuika<>0
			// ※検索条件「表示切替」にラジオボックスなし。
			sbSQL.append("WITH WCHANEL(CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4,CHAN_ORDER) AS ( ");
			sbSQL.append(" SELECT " + sqlWSHOPCHA + "CHAN_ORDER");
			sbSQL.append(" FROM M_CHANEL2 C where " + sqlKBN + "),   ");
			sbSQL.append("T_URI(" + sqlYM + ",CHANCLS1,CHANCLS2,CHANCLS3,CHANCLS4,MEISYO1,MEISYO2,MEISYO3,MEISYO4");
			sbSQL.append(" ,BUN1_SHOHIN_CD,BUN2_SHOHIN_CD,BUN3_SHOHIN_CD,BUN4_SHOHIN_CD,BUN5_SHOHIN_CD,BUN6_SHOHIN_CD");
			sbSQL.append(" ,BUN1_SHOHIN_MEI,BUN2_SHOHIN_MEI,BUN3_SHOHIN_MEI,BUN4_SHOHIN_MEI,BUN5_SHOHIN_MEI,BUN6_SHOHIN_MEI");
			sbSQL.append(" ,SHOHIN_BANGO,SHOHIN_CD,SHOHIN_MEI,START_YM,NRKBN,NRMEI," + sqlItemSu + ") AS (");
			sbSQL.append(" SELECT " + sqlYM +sqlWCha2 );
			sbSQL.append(" ,S.BUN1_SHOHIN_CD,S.BUN2_SHOHIN_CD,S.BUN3_SHOHIN_CD,S.BUN4_SHOHIN_CD,S.BUN5_SHOHIN_CD,S.BUN6_SHOHIN_CD");
			sbSQL.append(" ,S.BUN1_SHOHIN_MEI,S.BUN2_SHOHIN_MEI,S.BUN3_SHOHIN_MEI,S.BUN4_SHOHIN_MEI,S.BUN5_SHOHIN_MEI,S.BUN6_SHOHIN_MEI");
			sbSQL.append(" ,SHOHIN_BANGO,U.SHOHIN_CD,SHOHIN_MEI,S.START_YM,CASE WHEN NRKBN='0' THEN '3' ELSE NRKBN END");
			sbSQL.append(" ,CASE WHEN NRKBN = '1' THEN '新規' WHEN NRKBN = '2' THEN 'リピート' ELSE '' END," + sqlItemSu);
			sbSQL.append(" FROM " + sqlTable + " U");
			sbSQL.append(" INNER JOIN M_SHOHIN S ON (U.SHOHIN_CD = S.SHOHIN_CD"+sqlJoinShohin+")");	// 20171130 商品マスタの発売日基準
			sbSQL.append(" INNER JOIN WCHANEL C ON (U.CHANCLS2 = C.CHANCLS1 AND (U.CHANCLS3 = C.CHANCLS4 or (U.CHANCLS3 = C.CHANCLS2 and U.CHANCLS4 = C.CHANCLS3)))");
			sbSQL.append(" WHERE " + sqlWhereYm + sqlWCha1 + sqlWhereSho + sqlWhereNew + sqlWhereNR + ") ");
			sbSQL.append(sql_UionAll);						// 総合計行
			sbSQL.append(" UNION ALL ");		//
			// データ行 商品または分類項目表示
			sbSQL.append(sql_Uion);
			// 各種小計行
			sbSQL.append(sql_UionNR);
			sbSQL.append(sql_UionC1);
			sbSQL.append(sql_UionC2);
			sbSQL.append(sql_UionC3);
			sbSQL.append(sql_UionC4);
			sbSQL.append(sql_UionS1);
			sbSQL.append(sql_UionS2);
			sbSQL.append(sql_UionS3);
			sbSQL.append(sql_UionS4);
			sbSQL.append(sql_UionS5);
			sbSQL.append(sql_UionS6);
		}
		sbSQL.append(" ORDER BY SORT ");

		System.out.println(sbSQL.toString());
		command = sbSQL.toString();
		return command;
	}

	/**
	 * 商品分類 Where句を生成します。<br/>
	 *
	 * @return 商品分類 Where句
	 */
	private String getWhereShoBun() {
		String szSho1 = getMap().get("SEL_SHO1");	// 商品分類-基本分類
		String szSho2 = getMap().get("SEL_SHO2");	// 商品分類-大分類
		String szSho3 = getMap().get("SEL_SHO3");	// 商品分類-中分類
		String szSho4 = getMap().get("SEL_SHO4");	// 商品分類-小分類
		String szSho5 = getMap().get("SEL_SHO5");	// 商品分類-シリーズＡ
		String szSho6 = getMap().get("SEL_SHO6");	// 商品分類-シリーズＢ
		System.out.println("SEL_SHO:"+szSho1+szSho2+szSho3+szSho4+szSho5+szSho6);
		// 商品 検索条件
		String sqlWSho1 = "";
		if (!StringUtils.equals(szSho1, "00")) {  //商品基本選択
			sqlWSho1 = "AND S.BUN1_SHOHIN_CD = '" + szSho1 + "' ";
		}
		if (!StringUtils.equals(szSho2, "00")) { //商品大選択
			sqlWSho1 = "AND S.BUN2_SHOHIN_CD = '" + szSho2 + "' ";
		}
		if (!StringUtils.equals(szSho3, "00")) { //商品中選択
			sqlWSho1 = "AND S.BUN3_SHOHIN_CD = '" + szSho3 + "' ";
		}
		if (!StringUtils.equals(szSho4, "00")) { //商品小選択
			sqlWSho1 = "AND S.BUN4_SHOHIN_CD = '" + szSho4 + "' ";
		}
		if (!StringUtils.equals(szSho5, "00")) { //商品シリーズA
			sqlWSho1 = "AND S.BUN5_SHOHIN_CD = '" + szSho5 + "' ";
		}
		if (!StringUtils.equals(szSho6, "00")) { //商品シリーズB
			sqlWSho1 = "AND S.BUN6_SHOHIN_CD = '" + szSho6 + "' ";
		}

		// 商品 検索条件2
		Set<String> set = new HashSet<String>();
		for (int i = 1; i <= 10; i++) {
			int szBun = NumberUtils.toInt(getMap().get("SEL_BUN" + Integer.toString(i)));
			String szBS = getMap().get("SEL_BS" + Integer.toString(i));
			if (szBun != 0 && !StringUtils.equals(szBS, "00")) {
				switch (szBun) {
				case 2:
					set.add("S.BUN2_SHOHIN_CD = '" + szBS + "'");
					break;
				case 3:
					set.add("S.BUN3_SHOHIN_CD = '" + szBS + "'");
					break;
				case 4:
					set.add("S.BUN4_SHOHIN_CD = '" + szBS + "'");
					break;
				case 5:
					set.add("S.BUN5_SHOHIN_CD = '" + szBS + "'");
					break;
				case 6:
					set.add("S.BUN6_SHOHIN_CD = '" + szBS + "'");
					break;
				default:
					set.add("S.SHOHIN_CD = '" + szBS + "'");
				}
			}
		}
		String sqlWSho2 = StringUtils.join(set.toArray(), " or ");
		if (StringUtils.isNotEmpty(sqlWSho2)) {
			sqlWSho2 = "AND (" + sqlWSho2 + ") ";
		}

		return sqlWSho1 + sqlWSho2;
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

		List<String> cells = new ArrayList<String>();
		// 照会データ指定
		cells.add(DefineReport.RadB_1.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_1.NAME.getObj()));
		getWhere().add(cells);

		String radB1 = jad.getJSONValue(DefineReport.RadB_1.NAME.getObj());
		// 年月
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELB2.getView());
		if (StringUtils.equals(radB1, DefineReport.RadB_1.ID1.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()));
		} else if (StringUtils.equals(radB1, DefineReport.RadB_1.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN_TO.getObj()));
			getWhere().add(cells);
			cells = new ArrayList<String>();
			cells.add(DefineReport.Label.LABELB44.getView());
			cells.add(jad.getJSONText(DefineReport.RadB_4.NAME.getObj()));
		} else if (StringUtils.equals(radB1, DefineReport.RadB_1.ID3.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj())
					+ jad.getJSONText(DefineReport.Select.DD_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN_TO.getObj())
					+ jad.getJSONText(DefineReport.Select.DD_KIJUN_TO.getObj()));
		} else if (StringUtils.equals(radB1, DefineReport.RadB_1.ID4.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
		}
		getWhere().add(cells);

		// 新製品指定-1:なし 2:あり
		String radNew = jad.getJSONValue(DefineReport.RadB_5.NAME.getObj());
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELB2.getView());
		if (StringUtils.equals(radNew, DefineReport.RadB_5.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YM_NR.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YM_NR_TO.getObj()));
		}

		// チャネル-店販大分類
		cells = new ArrayList<String>();
		cells.add(DefineReport.Label.LABELB48.getView());
		cells.add(jad.getJSONText(DefineReport.RadB_7a.NAME.getObj()));
		getWhere().add(cells);
		// チャネル-分類
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.CHA_NM.getView()+"指定");
		String chanel = jad.getJSONText(DefineReport.Select.CHA_1.getObj());
		for (int i = 2; i <= 6; i++) {
			String cha = DefineReport.Select.CHA_NM.getObj() + "_" + i;
			if (!StringUtils.equals(jad.getJSONValue(cha), "00")) {
				chanel += "/" + jad.getJSONText(cha);
			}
		}
		cells.add(chanel);
		getWhere().add(cells);

		// 商品分類
		cells = new ArrayList<String>();
		cells.add(DefineReport.Select.SHOS_NM.getView()+"指定1");
		String shobun = jad.getJSONText(DefineReport.Select.SHOS_1.getObj());
		for (int i = 2; i <= 6; i++) {
			String sho = DefineReport.Select.SHOS_NM.getObj() + "_" + i;
			if (!StringUtils.equals(jad.getJSONValue(sho), "00")) {
				shobun += "/" + jad.getJSONText(sho);
			}
		}
		cells.add(shobun);
		getWhere().add(cells);

		// 商品分類条件
		cells = new ArrayList<String>();
		String shobun2 = jad.getJSONText(DefineReport.Select.SHOB_NM.getObj());
		String shobun2n = jad.getJSONText(DefineReport.Select.SHOBS_NM.getObj());
		for (int i = 1; i <= 10; i++) {
			// 商品分類条件
			String bun = DefineReport.Select.SHOB_NM.getObj() + "_" + i;
			String bs = DefineReport.Select.SHOBS_NM.getObj() + "_" + i;
			if (!StringUtils.equals(jad.getJSONValue(bun), "0") && !StringUtils.equals(jad.getJSONValue(bs), "00")) {
				shobun2 += "/" + jad.getJSONText(bun);
				shobun2n += "/" + jad.getJSONText(bs);
			}
		}
		cells.add(DefineReport.Select.SHOS_NM.getView()+"指定2");
		cells.add(shobun2+"-"+shobun2n);
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
		String userId = map.get("USER_ID");			// ユーザーID
		String szYy = map.get("YYYY_YY");			// 開始年
		String szNm = map.get("YYYY_MM");			// 開始月
		String szDd = map.get("YYYY_DD");			// 開始日
		String szYy2 = map.get("YYYY_YY2");			// 終了年
		String szNm2 = map.get("YYYY_MM2");			// 終了月
		String szDd2 = map.get("YYYY_DD2");			// 終了日
		String szYmN1 = map.get("START_YM");		// 新製品-開始年月
		String szYmN2 = map.get("START_YM2");		// 新製品-終了年月
		String szSu = map.get("SU_GA");				// 数/額指定
		String szZen = map.get("TAIHI");			// 対比表示
		String szSNr = map.get("NR_TYPE");			// リピート別集計-しない/する
		String szCha1 = map.get("SEL_CHA1");		// チャネル分類-基本分類
		String szCha2 = map.get("SEL_CHA2");		// チャネル分類-大分類
		String szCha3 = map.get("SEL_CHA3");		// チャネル分類-中分類
		String szCha4 = map.get("SEL_CHA4");		// チャネル分類-小分類
		String szSho1 = map.get("SEL_SHO1");		// 商品分類-基本分類
		String szSho2 = map.get("SEL_SHO2");		// 商品分類-大分類
		String szSho3 = map.get("SEL_SHO3");		// 商品分類-中分類
		String szSho4 = map.get("SEL_SHO4");		// 商品分類-小分類
		String szSho5 = map.get("SEL_SHO5");		// 商品分類-シリーズＡ
		String szSho6 = map.get("SEL_SHO6");		// 商品分類-シリーズＢ
		String szBun1 = map.get("SEL_BUN1");		// 商品分類 条件1
		String szBun2 = map.get("SEL_BUN2");		// 商品分類 条件2
		String szBun3 = map.get("SEL_BUN3");		// 商品分類 条件3
		String szBun4 = map.get("SEL_BUN4");		// 商品分類 条件4
		String szBun5 = map.get("SEL_BUN5");		// 商品分類 条件5
		String szBun6 = map.get("SEL_BUN6");		// 商品分類 条件6
		String szBun7 = map.get("SEL_BUN7");		// 商品分類 条件7
		String szBun8 = map.get("SEL_BUN8");		// 商品分類 条件8
		String szBun9 = map.get("SEL_BUN9");		// 商品分類 条件9
		String szBun10 = map.get("SEL_BUN10");		// 商品分類 条件10
		String szBS1 = map.get("SEL_BS1");			// 商品分類
		String szBS2 = map.get("SEL_BS2");			// 商品分類
		String szBS3 = map.get("SEL_BS3");			// 商品分類
		String szBS4 = map.get("SEL_BS4");			// 商品分類
		String szBS5 = map.get("SEL_BS5");			// 商品分類
		String szBS6 = map.get("SEL_BS6");			// 商品分類
		String szBS7 = map.get("SEL_BS7");			// 商品分類
		String szBS8 = map.get("SEL_BS8");			// 商品分類
		String szBS9 = map.get("SEL_BS9");			// 商品分類
		String szBS10 = map.get("SEL_BS10");		// 商品分類
		String szNen = map.get("RAD_NEN");			// 照会データ指定
		String sz12 = map.get("RAD_12");			// 月/12指定
		String szNewS = map.get("RAD_NEW");			// 新製品指定
		String szNr = map.get("RAD_NR");			// 通販・リピート別集計-しない/する
		String szTenDai = map.get("RAD_TEN_DAI");	// チャネル-店販大分類
		String szDsp = map.get("RAD_DSP_TYP");		// 表示形式-全表示/実績のみ
		String szChkC1 = convChk(map, "CHK_C1");	// チャネル集計-基本分類
		String szChkC2 = convChk(map, "CHK_C2");	// チャネル集計-大分類
		String szChkC3 = convChk(map, "CHK_C3");	// チャネル集計-中分類
		String szChkC4 = convChk(map, "CHK_C4");	// チャネル集計-小分類
		String szChkS1 = convChk(map, "CHK_S1");	// 商品集計-基本分類
		String szChkS2 = convChk(map, "CHK_S2");	// 商品集計-大分類
		String szChkS3 = convChk(map, "CHK_S3");	// 商品集計-中分類
		String szChkS4 = convChk(map, "CHK_S4");	// 商品集計-小分類
		String szChkS5 = convChk(map, "CHK_S5");	// 商品集計-シリーズＡ
		String szChkS6 = convChk(map, "CHK_S6");	// 商品集計-シリーズＢ
		String szChkS7 = convChk(map, "CHK_S7");	// 商品集計-商品

		if (StringUtils.equals(szBun1, "null")) {
			szBun1 = "0";
		}
		if (StringUtils.equals(szBun2, "null")) {
			szBun2 = "0";
		}
		if (StringUtils.equals(szBun3, "null")) {
			szBun3 = "0";
		}
		if (StringUtils.equals(szBun4, "null")) {
			szBun4 = "0";
		}
		if (StringUtils.equals(szBun5, "null")) {
			szBun5 = "0";
		}
		if (StringUtils.equals(szBun6, "null")) {
			szBun6 = "0";
		}
		if (StringUtils.equals(szBun7, "null")) {
			szBun7 = "0";
		}
		if (StringUtils.equals(szBun8, "null")) {
			szBun8 = "0";
		}
		if (StringUtils.equals(szBun9, "null")) {
			szBun9 = "0";
		}
		if (StringUtils.equals(szBun10, "null")) {
			szBun10 = "0";
		}

		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("MERGE INTO AK_SAVE_B4 ");
		sbSQL.append("USING (SELECT '" + userId + "' as USER_ID FROM DUAL) WK ON (AK_SAVE_B4.USER_ID = WK.USER_ID) ");
		// 既存レコードの更新
		sbSQL.append("WHEN MATCHED THEN UPDATE SET");
		sbSQL.append(" START_YY_SEL = '" + szYy + "',");			// 開始年
		sbSQL.append(" START_MM_SEL = '" + szNm + "',");			// 開始月
		sbSQL.append(" START_DD_SEL = '" + szDd + "',");			// 開始日
		sbSQL.append(" END_YY_SEL = '" + szYy2 + "',");				// 終了年
		sbSQL.append(" END_MM_SEL = '" + szNm2 + "',");				// 終了月
		sbSQL.append(" END_DD_SEL = '" + szDd2 + "',");				// 終了日
		sbSQL.append(" START_SYM_SEL = '" + szYmN1 + "',");			// 新製品-開始年月
		sbSQL.append(" END_SYM_SEL = '" + szYmN2 + "',");			// 新製品-終了年月
		sbSQL.append(" SU_GA_SEL = '" + szSu + "',");				// 数/額指定
		sbSQL.append(" TAIHI_SEL = '" + szZen + "',");				// 対比表示sel
		sbSQL.append(" NR_TYPE_SEL = '" + szSNr + "',");			// リピート別集計-しない/する
		sbSQL.append(" CHANEL_KIHON_SEL = '" + szCha1 + "',");		// チャネル分類-基本分類
		sbSQL.append(" CHANEL_DAI_SEL = '" + szCha2 + "',");		// チャネル分類-大分類
		sbSQL.append(" CHANEL_TYU_SEL = '" + szCha3 + "',");		// チャネル分類-中分類
		sbSQL.append(" CHANEL_SHO_SEL = '" + szCha4 + "',");		// チャネル分類-小分類
		sbSQL.append(" SHOHIN_KIHON_SEL = '" + szSho1 + "',");		// 商品分類-基本分類
		sbSQL.append(" SHOHIN_DAI_SEL = '" + szSho2 + "',");		// 商品分類-大分類
		sbSQL.append(" SHOHIN_TYU_SEL = '" + szSho3 + "',");		// 商品分類-中分類
		sbSQL.append(" SHOHIN_SHO_SEL = '" + szSho4 + "',");		// 商品分類-小分類
		sbSQL.append(" SHOHIN_A_SEL = '" + szSho5 + "',");			// 商品分類-シリーズＡ
		sbSQL.append(" SHOHIN_B_SEL = '" + szSho6 + "',");			// 商品分類-シリーズＢ
		sbSQL.append(" BUN1_SEL = '" + szBun1 + "',");				// 商品分類
		sbSQL.append(" BUN2_SEL = '" + szBun2 + "',");				// 商品分類
		sbSQL.append(" BUN3_SEL = '" + szBun3 + "',");				// 商品分類
		sbSQL.append(" BUN4_SEL = '" + szBun4 + "',");				// 商品分類
		sbSQL.append(" BUN5_SEL = '" + szBun5 + "',");				// 商品分類
		sbSQL.append(" BUN6_SEL = '" + szBun6 + "',");				// 商品分類
		sbSQL.append(" BUN7_SEL = '" + szBun7 + "',");				// 商品分類
		sbSQL.append(" BUN8_SEL = '" + szBun8 + "',");				// 商品分類
		sbSQL.append(" BUN9_SEL = '" + szBun9 + "',");				// 商品分類
		sbSQL.append(" BUN10_SEL = '" + szBun10 + "',");			// 商品分類
		sbSQL.append(" BUNS1_SEL = '" + szBS1 + "',");				// 商品分類
		sbSQL.append(" BUNS2_SEL = '" + szBS2 + "',");				// 商品分類
		sbSQL.append(" BUNS3_SEL = '" + szBS3 + "',");				// 商品分類
		sbSQL.append(" BUNS4_SEL = '" + szBS4 + "',");				// 商品分類
		sbSQL.append(" BUNS5_SEL = '" + szBS5 + "',");				// 商品分類
		sbSQL.append(" BUNS6_SEL = '" + szBS6 + "',");				// 商品分類
		sbSQL.append(" BUNS7_SEL = '" + szBS7 + "',");				// 商品分類
		sbSQL.append(" BUNS8_SEL = '" + szBS8 + "',");				// 商品分類
		sbSQL.append(" BUNS9_SEL = '" + szBS9 + "',");				// 商品分類
		sbSQL.append(" BUNS10_SEL = '" + szBS10 + "',");			// 商品分類
		sbSQL.append(" REF_DATA_RAD = '" + szNen + "',");			// 照会データ指定
		sbSQL.append(" MONTH_12_RAD = '" + sz12 + "',");			// 月/12指定
		sbSQL.append(" NEW_PRODUCT_RAD = '" + szNewS + "',");		// 新製品指定
		sbSQL.append(" TSU_REP_RAD = '" + szNr + "',");				// 通販・リピート別集計-しない/する
		sbSQL.append(" DISP_TYPE_RAD = '" + szDsp + "',");			// 表示形式-全表示/実績のみ
		sbSQL.append(" TENPO_DAI_RAD = '" + szTenDai + "',");		// チャネル-店販大分類
		sbSQL.append(" CHANEL_KIHON_CHK = '" + szChkC1 + "',");		// チャネル集計-基本分類
		sbSQL.append(" CHANEL_DAI_CHK = '" + szChkC2 + "',");		// チャネル集計-大分類
		sbSQL.append(" CHANEL_TYU_CHK = '" + szChkC3 + "',");		// チャネル集計-中分類
		sbSQL.append(" CHANEL_SHO_CHK = '" + szChkC4 + "',");		// チャネル集計-小分類
		sbSQL.append(" SHOHIN_KIHON_CHK = '" + szChkS1 + "',");		// 商品集計-基本分類
		sbSQL.append(" SHOHIN_DAI_CHK = '" + szChkS2 + "',");		// 商品集計-大分類
		sbSQL.append(" SHOHIN_TYU_CHK = '" + szChkS3 + "',");		// 商品集計-中分類
		sbSQL.append(" SHOHIN_SHO_CHK = '" + szChkS4 + "',");		// 商品集計-小分類
		sbSQL.append(" SHOHIN_A_CHK = '" + szChkS5 + "',");			// 商品集計-シリーズＡ
		sbSQL.append(" SHOHIN_B_CHK = '" + szChkS6 + "',");			// 商品集計-シリーズＢ
		sbSQL.append(" SHOHIN_SHOHIN_CHK = '" + szChkS7 + "' ");	// 商品集計-シリーズＢ
		// -- 新規レコードの作成
		sbSQL.append("WHEN NOT MATCHED THEN ");
		sbSQL.append("INSERT (USER_ID,START_YY_SEL,START_MM_SEL,START_DD_SEL,END_YY_SEL,END_MM_SEL,END_DD_SEL");
		sbSQL.append(",START_SYM_SEL,END_SYM_SEL,SU_GA_SEL,TAIHI_SEL,NR_TYPE_SEL");
		sbSQL.append(",CHANEL_KIHON_SEL,CHANEL_DAI_SEL,CHANEL_TYU_SEL,CHANEL_SHO_SEL");
		sbSQL.append(",SHOHIN_KIHON_SEL,SHOHIN_DAI_SEL,SHOHIN_TYU_SEL,SHOHIN_SHO_SEL,SHOHIN_A_SEL,SHOHIN_B_SEL");
		sbSQL.append(",BUN1_SEL,BUN2_SEL,BUN3_SEL,BUN4_SEL,BUN5_SEL,BUN6_SEL,BUN7_SEL,BUN8_SEL,BUN9_SEL,BUN10_SEL");
		sbSQL.append(",BUNS1_SEL,BUNS2_SEL,BUNS3_SEL,BUNS4_SEL,BUNS5_SEL,BUNS6_SEL,BUNS7_SEL,BUNS8_SEL,BUNS9_SEL,BUNS10_SEL");
		sbSQL.append(",REF_DATA_RAD,MONTH_12_RAD,NEW_PRODUCT_RAD,TSU_REP_RAD,TENPO_DAI_RAD,DISP_TYPE_RAD");
		sbSQL.append(",CHANEL_KIHON_CHK,CHANEL_DAI_CHK,CHANEL_TYU_CHK,CHANEL_SHO_CHK,SHOHIN_KIHON_CHK");
		sbSQL.append(",SHOHIN_DAI_CHK,SHOHIN_TYU_CHK,SHOHIN_SHO_CHK,SHOHIN_A_CHK,SHOHIN_B_CHK,SHOHIN_SHOHIN_CHK) ");
		sbSQL.append("VALUES (");
		sbSQL.append(" '" + userId + "',");		// ユーザーID
		sbSQL.append(" '" + szYy + "',");		// 開始年
		sbSQL.append(" '" + szNm + "',");		// 開始月
		sbSQL.append(" '" + szDd + "',");		// 開始日
		sbSQL.append(" '" + szYy2 + "',");		// 終了年
		sbSQL.append(" '" + szNm2 + "',");		// 終了月
		sbSQL.append(" '" + szDd2 + "',");		// 終了日
		sbSQL.append(" '" + szYmN1 + "',");		// 新製品-開始年月
		sbSQL.append(" '" + szYmN2 + "',");		// 新製品-終了年月
		sbSQL.append(" '" + szSu + "',");		// 数/額指定
		sbSQL.append(" '" + szZen + "',");		// 対比表示sel
		sbSQL.append(" '" + szSNr + "',");		// リピート別集計-しない/する
		sbSQL.append(" '" + szCha1 + "',");		// チャネル分類-基本分類
		sbSQL.append(" '" + szCha2 + "',");		// チャネル分類-大分類
		sbSQL.append(" '" + szCha3 + "',");		// チャネル分類-中分類
		sbSQL.append(" '" + szCha4 + "',");		// チャネル分類-小分類
		sbSQL.append(" '" + szSho1 + "',");		// 商品分類-基本分類
		sbSQL.append(" '" + szSho2 + "',");		// 商品分類-大分類
		sbSQL.append(" '" + szSho3 + "',");		// 商品分類-中分類
		sbSQL.append(" '" + szSho4 + "',");		// 商品分類-小分類
		sbSQL.append(" '" + szSho5 + "',");		// 商品分類-シリーズＡ
		sbSQL.append(" '" + szSho6 + "',");		// 商品分類-シリーズＢ
		sbSQL.append(" '" + szBun1 + "',");		// 商品分類
		sbSQL.append(" '" + szBun2 + "',");		// 商品分類
		sbSQL.append(" '" + szBun3 + "',");		// 商品分類
		sbSQL.append(" '" + szBun4 + "',");		// 商品分類
		sbSQL.append(" '" + szBun5 + "',");		// 商品分類
		sbSQL.append(" '" + szBun6 + "',");		// 商品分類
		sbSQL.append(" '" + szBun7 + "',");		// 商品分類
		sbSQL.append(" '" + szBun8 + "',");		// 商品分類
		sbSQL.append(" '" + szBun9 + "',");		// 商品分類
		sbSQL.append(" '" + szBun10 + "',");	// 商品分類
		sbSQL.append(" '" + szBS1 + "',");		// 商品分類
		sbSQL.append(" '" + szBS2 + "',");		// 商品分類
		sbSQL.append(" '" + szBS3 + "',");		// 商品分類
		sbSQL.append(" '" + szBS4 + "',");		// 商品分類
		sbSQL.append(" '" + szBS5 + "',");		// 商品分類
		sbSQL.append(" '" + szBS6 + "',");		// 商品分類
		sbSQL.append(" '" + szBS7 + "',");		// 商品分類
		sbSQL.append(" '" + szBS8 + "',");		// 商品分類
		sbSQL.append(" '" + szBS9 + "',");		// 商品分類
		sbSQL.append(" '" + szBS10 + "',");		// 商品分類
		sbSQL.append(" '" + szNen + "',");		// 照会データ指定
		sbSQL.append(" '" + sz12 + "',");		// 月/12指定
		sbSQL.append(" '" + szNewS + "',");		// 新製品指定
		sbSQL.append(" '" + szNr + "',");		// 通販・リピート別集計-しない/する
		sbSQL.append(" '" + szTenDai + "',");	// チャネル-店販大分類
		sbSQL.append(" '" + szDsp + "',");		// 表示形式-全表示/実績のみ
		sbSQL.append(" '" + szChkC1 + "',");	// チャネル集計-基本分類
		sbSQL.append(" '" + szChkC2 + "',");	// チャネル集計-大分類
		sbSQL.append(" '" + szChkC3 + "',");	// チャネル集計-中分類
		sbSQL.append(" '" + szChkC4 + "',");	// チャネル集計-小分類
		sbSQL.append(" '" + szChkS1 + "',");	// 商品集計-基本分類
		sbSQL.append(" '" + szChkS2 + "',");	// 商品集計-大分類
		sbSQL.append(" '" + szChkS3 + "',");	// 商品集計-中分類
		sbSQL.append(" '" + szChkS4 + "',");	// 商品集計-小分類
		sbSQL.append(" '" + szChkS5 + "',");	// 商品集計-シリーズＡ
		sbSQL.append(" '" + szChkS6 + "',");	// 商品集計-シリーズＢ
		sbSQL.append(" '" + szChkS7 + "')");	// 商品集計-商品

		System.out.println("保管SQL:" + sbSQL.toString());
		return sbSQL.toString();
	}
}
