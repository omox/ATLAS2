package dao;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import common.DefineReport;
import common.JsonArrayData;

/**
 * @author EATONE
 */
public class D11Dao extends ItemDao {

	/**
	 * 新しいB04Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public D11Dao(Connection source) {
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

		String szChanel	= getMap().get("CHANEL");	// 購入チャネル(1:通店計、2:通販、3:店舗）
		String szYy		= getMap().get("YYYY_YY");	// 開始年
		String szNm		= getMap().get("YYYY_MM");	// 開始月

		String szITEM	= getMap().get("ITEM");		// 購入商品（1:基本分野、2:主要カテゴリ、3:重点アイテム、4:分類指定）
		String szITEM_JUN = getMap().get("ITEM_JUN");	// 表示項目-
		String szITEM_SHU = getMap().get("ITEM_SHU");	// 表示項目-
		String szITEM_CHO = getMap().get("ITEM_CHO");	// 表示項目-

		System.out.println("CHANEL:"+szChanel);
		System.out.println("YYYY_MM:"+szYy+szNm);
		System.out.println("ITEM:"+szITEM);
		System.out.println("ITEM_JUN:"+szITEM_JUN);
		System.out.println("ITEM_SHU:"+szITEM_SHU);
		System.out.println("ITEM_CHO:"+szITEM_CHO);

		String[] szJun = szITEM_JUN.split(",", 0);
		String[] szShu = szITEM_SHU.split(",", 0);
		String[] szCho = szITEM_CHO.split(",", 0);

		String szZMYY = szYy;	// 前月の年
		String szZMMM = szNm;	// 前月の月
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = sdf.parse(szYy+szNm+"01");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			// 前年
			calendar.add(Calendar.YEAR, -1);
			Date d1 = calendar.getTime();
			szZMYY = sdf.format(d1).substring(0, 4);
			szZMMM = sdf.format(d1).substring(4, 6);

		} catch(ParseException e) {
			e.printStackTrace();
		}
		System.out.println("szZM:"+szZMYY+szZMMM);

		// チャネル判定
		String szChanelPlan = "1,2";
		if (DefineReport.RadD01_CHANEL.ID1.getVal().equals(szChanel)){
			// 通店計
			szChanelPlan = "1,2";
		} else if (DefineReport.RadD01_CHANEL.ID2.getVal().equals(szChanel)) {
			// 通販
			szChanelPlan = "1";
		} else if (DefineReport.RadD01_CHANEL.ID3.getVal().equals(szChanel)) {
			// 店舗
			szChanelPlan = "2";
		}

		// 商品分野（1:化粧品、2:食品、3:ボディウェア、4:その他、5:すべて）
		String szKihon = "";
		if (DefineReport.RadD14_CLS.ID1.getVal().equals(szITEM)){
			// 化粧品
			szKihon = "KHNCD = 10 ";
		} else if (DefineReport.RadD14_CLS.ID2.getVal().equals(szITEM)) {
			// 食品
			szKihon = "KHNCD = 20 ";
		} else if (DefineReport.RadD14_CLS.ID3.getVal().equals(szITEM)) {
			// ボディウェア
			szKihon = "KHNCD = 30 ";
		} else if (DefineReport.RadD14_CLS.ID4.getVal().equals(szITEM)) {
			// その他
			szKihon = "KHNCD NOT IN (10,20,30,90) ";
		} else if (DefineReport.RadD14_CLS.ID5.getVal().equals(szITEM)) {
			// すべて
			szKihon = "";
		}

		StringBuffer sb = new StringBuffer();

		// 計画に存在する情報
		sb.append("WITH ");
		sb.append(" MTT AS ( ");	// 当年
		sb.append("SELECT ");
		sb.append(" CASE WHEN T1.KHNCD IN (10,20,30) THEN T1.KHNCD ELSE 90 END AS KHNCD ");
		sb.append(",T1.CHANEL ");
		sb.append(",T1.CATCD ");
		sb.append(",T1.CPFLG ");
		sb.append(",T1.ITEMNO ");
		sb.append(",MAX(T1.ITEMNM)   AS ITEMNM ");
		sb.append(",MAX(T1.PRODT_ST) AS PRODT_ST ");
		sb.append(",MAX(T1.PRODT_LT) AS PRODT_LT ");
		sb.append(",MAX(T1.PMONTH)   AS PMONTH ");
		sb.append(",MAX(T2.PRICE) AS PRICE ");
		sb.append(",MIN(T2.UNIT)  AS UNIT ");
		sb.append(",SUM(T2.SU) AS SU_TONT ");
		sb.append(",NULL AS SU_ZENT ");
		sb.append(",MAX(T1.SU) AS SU_PLAN ");
		sb.append(",SUM(T2.KG) AS KG_TONT ");
		sb.append(",NULL AS KG_ZENT ");
		sb.append(",MAX(T1.KG) AS KG_PLAN ");
		sb.append(",SUM(CASE WHEN T2.JUTKB=30 THEN T2.SU END) AS SU_30 ");
		sb.append(",SUM(CASE WHEN T2.JUTKB=30 THEN T2.KG END) AS KG_30 ");
		sb.append(",SUM(CASE WHEN T2.JUTKB=34 THEN T2.SU END) AS SU_34 ");
		sb.append(",SUM(CASE WHEN T2.JUTKB=34 THEN T2.KG END) AS KG_34 ");
		sb.append(",SUM(CASE WHEN T2.JUTKB IN (30,34) THEN T2.SU END) AS SU_WEB ");
		sb.append(",SUM(CASE WHEN T2.JUTKB IN (30,34) THEN T2.KG END) AS KG_WEB ");
		sb.append("FROM DS_B_PROMO_PLAN T1 ");
		sb.append("LEFT OUTER JOIN DS_M_PROMO T2 ");
		sb.append("ON T2.YMD BETWEEN T1.PRODT_ST  AND T1.PRODT_LT  AND T1.CHANEL=T2.CHANEL AND T1.KHNCD = T2.KHNCD AND T1.CPFLG = T2.CPFLG AND T1.ITEMNO=T2.ITEMNO ");
		// 集計期間
		sb.append(" WHERE T1.YM="+szYy+szNm);
		// 購入チャネル
		sb.append(" AND T1.CHANEL IN ("+szChanelPlan+") ");
		// 商品分野
		if (szKihon.length()>0){
			sb.append(" AND T1."+szKihon.toString());
		}
		// 分野計除外
		sb.append(" AND T1.CATCD<>'00' ");
		sb.append("GROUP BY T1.KHNCD, T1.CHANEL, T1.CATCD, T1.CPFLG, T1.ITEMNO ");
		sb.append(") ");

		// 前年
		sb.append(",MTZ AS ( ");
		sb.append("SELECT ");
		sb.append(" CASE WHEN T1.KHNCD IN (10,20,30) THEN T1.KHNCD ELSE 90 END AS KHNCD ");
		sb.append(",T1.CHANEL ");
		sb.append(",T1.CATCD ");
		sb.append(",T1.CPFLG ");
		sb.append(",T1.ITEMNO ");
		sb.append(",MAX(T1.ITEMNM)   AS ITEMNM ");
		sb.append(",MAX(T1.PRODT_ST) AS PRODT_ST ");
		sb.append(",MAX(T1.PRODT_LT) AS PRODT_LT ");
		sb.append(",MAX(T1.PMONTH)   AS PMONTH ");
		sb.append(",NULL AS PRICE ");
		sb.append(",NULL AS UNIT ");
		sb.append(",NULL AS SU_TONT ");
		sb.append(",SUM(T3.SU) AS SU_ZENT ");
		sb.append(",NULL AS SU_PLAN ");
		sb.append(",NULL AS KG_TONT ");
		sb.append(",SUM(T3.KG) AS KG_ZENT ");
		sb.append(",NULL AS KG_PLAN ");
		sb.append(",NULL AS SU_30 ");
		sb.append(",NULL AS KG_30 ");
		sb.append(",NULL AS SU_34 ");
		sb.append(",NULL AS KG_34 ");
		sb.append(",NULL AS SU_WEB ");
		sb.append(",NULL AS KG_WEB ");
		sb.append("FROM DS_B_PROMO_PLAN T1 ");
		sb.append("LEFT OUTER JOIN DS_M_PROMO T3 ");
		sb.append("ON T3.YMD BETWEEN T1.PRODT_ZST AND T1.PRODT_ZLT AND T1.CHANEL=T3.CHANEL AND T1.KHNCD = T3.KHNCD AND T1.CPFLG = T3.CPFLG AND T1.ITEMNO=T3.ITEMNO ");
		// 集計期間
		sb.append(" WHERE T1.YM="+szYy+szNm);
		// 購入チャネル
		sb.append(" AND T1.CHANEL IN ("+szChanelPlan+") ");
		// 商品分野
		if (szKihon.length()>0){
			sb.append(" AND T1."+szKihon.toString());
		}
		// 分野計除外
		sb.append(" AND T1.CATCD<>'00' ");
		sb.append("GROUP BY T1.KHNCD, T1.CHANEL, T1.CATCD, T1.CPFLG, T1.ITEMNO ");
		sb.append(") ");

		// 当年＋前年
		sb.append(", MT AS ( ");
		sb.append("  SELECT KHNCD,CATCD,CPFLG,ITEMNO ");
		sb.append("  ,MAX(ITEMNM) AS ITEMNM ");
		sb.append("  ,MAX(PRODT_ST) AS PRODT_ST ");
		sb.append("  ,MAX(PRODT_LT) AS PRODT_LT ");
		sb.append("  ,MAX(PMONTH) AS PMONTH ");
		sb.append("  ,MAX(PRICE) AS PRICE ");
		sb.append("  ,MAX(UNIT) AS UNIT ");
		sb.append("  ,SUM(SU_TONT) AS SU_TONT ");
		sb.append("  ,SUM(SU_ZENT) AS SU_ZENT ");
		sb.append("  ,SUM(SU_PLAN) AS SU_PLAN ");
		sb.append("  ,SUM(KG_TONT) AS KG_TONT ");
		sb.append("  ,SUM(KG_ZENT) AS KG_ZENT ");
		sb.append("  ,SUM(KG_PLAN) AS KG_PLAN ");
		sb.append("  ,SUM(SU_30) AS SU_30 ");
		sb.append("  ,SUM(KG_30) AS KG_30 ");
		sb.append("  ,SUM(SU_34) AS SU_34 ");
		sb.append("  ,SUM(KG_34) AS KG_34 ");
		sb.append("  ,SUM(SU_WEB) AS SU_WEB ");
		sb.append("  ,SUM(KG_WEB) AS KG_WEB ");
		sb.append("  FROM (SELECT * FROM MTT UNION SELECT * FROM MTZ) X ");
		sb.append("  GROUP BY KHNCD,CATCD,CPFLG,ITEMNO ");
		sb.append(") ");

		// プロモ = ○ の情報
		sb.append(", MTA AS ( ");
		sb.append("SELECT ");
		sb.append(" CASE WHEN KHNCD IN (10,20,30) THEN KHNCD ELSE 90 END AS KHNCD ");
		sb.append(",'99999991' AS CATCD, NULL AS CPFLG, NULL AS ITEMNO, NULL AS ITEMNM, NULL AS PRODT_ST, NULL AS PRODT_LT, NULL AS PMONTH, NULL AS PRICE, NULL AS UNIT ");
		sb.append(",SUM(CASE WHEN CPFLG=0 THEN SU_TONT END) AS SU_TONT ");
		sb.append(",SUM(CASE WHEN CPFLG=0 THEN SU_ZENT END) AS SU_ZENT ");
		sb.append(",SUM(CASE WHEN CPFLG=0 THEN SU_PLAN END) AS SU_PLAN ");
		sb.append(",SUM(KG_TONT) AS KG_TONT ");
		sb.append(",SUM(KG_ZENT) AS KG_ZENT ");
		sb.append(",SUM(KG_PLAN) AS KG_PLAN ");
		sb.append(",SUM(CASE WHEN CPFLG=0 THEN SU_30 END) AS SU_30 ");
		sb.append(",SUM(KG_30) AS KG_30 ");
		sb.append(",SUM(CASE WHEN CPFLG=0 THEN SU_34 END) AS SU_34 ");
		sb.append(",SUM(KG_34) AS KG_34 ");
		sb.append(",SUM(CASE WHEN CPFLG=0 THEN SU_WEB END) AS SU_WEB ");
		sb.append(",SUM(KG_WEB) AS KG_WEB ");
		sb.append("FROM MT ");
		sb.append("GROUP BY KHNCD ");
		sb.append(") ");

		// プロモ = × の情報
		sb.append(", LTA AS ( ");
		// 計画に存在しない情報
		sb.append("SELECT ");
		sb.append(" CASE WHEN KHNCD IN (10,20,30) THEN KHNCD ELSE 90 END AS KHNCD ");
		sb.append(",'99999992' AS CATCD, NULL AS CPFLG, NULL AS ITEMNO, NULL AS ITEMNM, NULL AS PRODT_ST, NULL AS PRODT_LT, NULL AS PMONTH, NULL AS PRICE, NULL AS UNIT ");
		sb.append(",SUM(SU_TONT) AS SU_TONT ");
		sb.append(",SUM(SU_ZENT) AS SU_ZENT ");
		sb.append(",SUM(SU_PLAN) AS SU_PLAN ");
		sb.append(",SUM(KG_TONT) AS KG_TONT ");
		sb.append(",SUM(KG_ZENT) AS KG_ZENT ");
		sb.append(",SUM(KG_PLAN) AS KG_PLAN ");
		sb.append(",SUM(SU_30) AS SU_30 ");
		sb.append(",SUM(KG_30) AS SG_30 ");
		sb.append(",SUM(SU_34) AS SU_34 ");
		sb.append(",SUM(KG_34) AS SG_34 ");
		sb.append(",SUM(SU_WEB) AS SU_WEB ");
		sb.append(",SUM(KG_WEB) AS KG_WEB ");
		sb.append("FROM ( ");

		// 計画に存在しない情報（当年）
		sb.append("SELECT ");
		sb.append(" T2.KHNCD,NULL AS CATCD, NULL AS CPFLG, NULL AS ITEMNO, NULL AS ITEMNM, NULL AS PRODT_ST, NULL AS PRODT_LT, NULL AS PMONTH, NULL AS PRICE, NULL AS UNIT ");
		sb.append(",SUM(CASE WHEN T2.CPFLG=0 THEN T2.SU END) AS SU_TONT ");
		sb.append(",NULL AS SU_ZENT ");
		sb.append(",NULL AS SU_PLAN ");
		sb.append(",SUM(T2.KG) AS KG_TONT ");
		sb.append(",NULL AS KG_ZENT ");
		sb.append(",NULL AS KG_PLAN ");
		sb.append(",SUM(CASE WHEN T2.CPFLG=0 AND T2.JUTKB=30 THEN T2.SU END) AS SU_30 ");
		sb.append(",SUM(CASE WHEN T2.JUTKB=30 THEN T2.KG END) AS KG_30 ");
		sb.append(",SUM(CASE WHEN T2.CPFLG=0 AND T2.JUTKB=34 THEN T2.SU END) AS SU_34 ");
		sb.append(",SUM(CASE WHEN T2.JUTKB=34 THEN T2.KG END) AS KG_34 ");
		sb.append(",SUM(CASE WHEN T2.CPFLG=0 AND T2.JUTKB IN (30,34) THEN T2.SU END) AS SU_WEB ");
		sb.append(",SUM(CASE WHEN T2.JUTKB IN (30,34) THEN T2.KG END) AS KG_WEB ");
		sb.append("FROM DS_M_PROMO T2 ");
		sb.append("WHERE NOT EXISTS (SELECT 1 FROM DS_B_PROMO_PLAN T1 WHERE T2.YMD BETWEEN T1.PRODT_ST AND T1.PRODT_LT AND T1.CHANEL=T2.CHANEL AND T1.KHNCD = T2.KHNCD AND T1.CPFLG = T2.CPFLG AND T1.ITEMNO=T2.ITEMNO ");
		// 集計期間
		sb.append("AND T1.YM="+szYy+szNm);
		// 分野計除外
		sb.append("AND T1.ITEMNO<>0 ");
		sb.append(") ");
		// 集計期間
		sb.append("AND T2.YMD BETWEEN "+szYy+szNm+"00 AND "+szYy+szNm+"99 ");
		// 購入チャネル
		sb.append("AND T2.CHANEL IN ("+szChanelPlan+") ");
		// 商品分野
		if (szKihon.length()>0){
			sb.append("AND T2."+szKihon.toString());
		}
		sb.append("GROUP BY T2.KHNCD ");
		sb.append("UNION ALL ");

		// 計画に存在しない情報（前年）
		sb.append("SELECT ");
		sb.append(" T2.KHNCD,NULL AS CATCD, NULL AS CPFLG, NULL AS ITEMNO, NULL AS ITEMNM, NULL AS PRODT_ST, NULL AS PRODT_LT, NULL AS PMONTH, NULL AS PRICE, NULL AS UNIT ");
		sb.append(",NULL AS SU_TONT ");
		sb.append(",SUM(CASE WHEN T2.CPFLG=0 THEN T2.SU END) AS SU_ZENT ");
		sb.append(",NULL AS SU_PLAN ");
		sb.append(",NULL AS KG_TONT ");
		sb.append(",SUM(T2.KG) AS KG_ZENT ");
		sb.append(",NULL AS KG_PLAN ");
		sb.append(",NULL AS SU_30 ");
		sb.append(",NULL AS KG_30 ");
		sb.append(",NULL AS SU_34 ");
		sb.append(",NULL AS KG_34 ");
		sb.append(",NULL AS SU_WEB ");
		sb.append(",NULL AS KG_WEB ");
		sb.append("FROM DS_M_PROMO T2 ");
		sb.append("WHERE NOT EXISTS (SELECT 1 FROM DS_B_PROMO_PLAN T1 WHERE T2.YMD BETWEEN T1.PRODT_ZST AND T1.PRODT_ZLT AND T1.CHANEL=T2.CHANEL AND T1.KHNCD = T2.KHNCD AND T1.CPFLG = T2.CPFLG AND T1.ITEMNO=T2.ITEMNO ");
		// 集計期間
		sb.append("AND T1.YM="+szYy+szNm);
		// 分野計除外
		sb.append("AND T1.ITEMNO<>0 ");
		sb.append(") ");
		// 集計期間
		sb.append("AND T2.YMD BETWEEN "+szZMYY+szZMMM+"00 AND "+szZMYY+szZMMM+"99 ");
		// 購入チャネル
		sb.append("AND T2.CHANEL IN ("+szChanelPlan+") ");
		// 商品分野
		if (szKihon.length()>0){
			sb.append("AND T2."+szKihon.toString());
		}
		sb.append("GROUP BY ");
		sb.append(" T2.KHNCD ");

		sb.append("UNION ALL ");

		// 分野計情報
		sb.append("SELECT ");
		sb.append(" CASE WHEN T1.KHNCD IN (10,20,30) THEN T1.KHNCD ELSE 90 END AS KHNCD ");
		sb.append(",NULL AS CATCD,NULL AS CPFLG, NULL AS ITEMNO, NULL AS ITEMNM, NULL AS PRODT_ST, NULL AS PRODT_LT, NULL AS PMONTH, NULL AS PRICE, NULL AS UNIT ");
		sb.append(",NULL AS SU_TONT ");
		sb.append(",NULL AS SU_ZENT ");
		sb.append(",SUM(CASE WHEN T1.CATCD='00' THEN T1.SU END) AS SU_PLAN ");
		sb.append(",NULL AS KG_TONT ");
		sb.append(",NULL AS KG_ZENT ");
		sb.append(",SUM(CASE WHEN T1.CATCD = '00' THEN T1.KG END) - SUM(CASE WHEN T1.CATCD <> '00' THEN T1.KG END) AS KG_PLAN ");
		sb.append(",NULL AS SU_30 ");
		sb.append(",NULL AS KG_30 ");
		sb.append(",NULL AS SU_34 ");
		sb.append(",NULL AS KG_34 ");
		sb.append(",NULL AS SU_WEB ");
		sb.append(",NULL AS KG_WEB ");
		sb.append("FROM DS_B_PROMO_PLAN T1 ");
		sb.append("WHERE ");
		// 集計期間
		sb.append(" T1.YM="+szYy+szNm);
		// 購入チャネル
		sb.append("AND T1.CHANEL IN ("+szChanelPlan+") ");
		// 商品分野
		if (szKihon.length()>0){
			sb.append("AND T1."+szKihon.toString());
		}
		sb.append("GROUP BY KHNCD ");
		sb.append(")X ");
		sb.append("GROUP BY CASE WHEN KHNCD IN (10, 20, 30) THEN KHNCD ELSE 90 END ");
		sb.append(") ");

		// 分野計情報
		sb.append(", PTA AS ( ");
		sb.append("SELECT ");
		sb.append(" T1.KHNCD, T1.CATCD, NULL AS CPFLG, NULL AS ITEMNO, NULL AS ITEMNM, NULL AS PRODT_ST, NULL AS PRODT_LT, NULL AS PMONTH, NULL AS PRICE, NULL AS UNIT ");
		sb.append(",NULL AS SU_TONT ");
		sb.append(",NULL AS SU_ZENT ");
		sb.append(",SUM(T1.SU) AS SU_PLAN ");
		sb.append(",NULL AS KG_TONT ");
		sb.append(",NULL AS KG_ZENT ");
		sb.append(",SUM(T1.KG) AS KG_PLAN ");
		sb.append(",NULL AS SU_30 ");
		sb.append(",NULL AS KG_30 ");
		sb.append(",NULL AS SU_34 ");
		sb.append(",NULL AS KG_34 ");
		sb.append(",NULL AS SU_WEB ");
		sb.append(",NULL AS KG_WEB ");
		sb.append("FROM DS_B_PROMO_PLAN T1 ");
		sb.append("WHERE ");
		// 集計期間
		sb.append(" T1.YM="+szYy+szNm);
		// 購入チャネル
		sb.append("AND T1.CHANEL IN ("+szChanelPlan+") ");
		// 商品分野
		if (szKihon.length()>0){
			sb.append("AND T1."+szKihon.toString());
		}
		// 分野計;
		sb.append("AND T1.CATCD='00' ");
		sb.append("GROUP BY KHNCD,T1.CATCD ");
		sb.append(") ");

		// プロモ = 全体 の情報
		sb.append(",UTA AS ( ");
		sb.append("SELECT ");
		sb.append(" X.KHNCD, '99999993' AS CATCD, NULL AS CPFLG, NULL AS ITEMNO, NULL AS ITEMNM, NULL AS PRODT_ST, NULL AS PRODT_LT, NULL AS PMONTH, NULL AS PRICE, NULL AS UNIT ");
		sb.append(",SUM(X.SU_TONT) AS SU_TONT ");
		sb.append(",SUM(X.SU_ZENT) AS SU_ZENT ");
		sb.append(",SUM(CASE WHEN X.CATCD='00' THEN X.SU_PLAN END) AS SU_PLAN ");
		sb.append(",SUM(X.KG_TONT) AS KG_TONT ");
		sb.append(",SUM(X.KG_ZENT) AS KG_ZENT ");
		sb.append(",SUM(CASE WHEN X.CATCD='00' THEN X.KG_PLAN END) AS KG_PLAN ");
		sb.append(",SUM(X.SU_30) AS SU_30 ");
		sb.append(",SUM(X.KG_30) AS KG_30 ");
		sb.append(",SUM(X.SU_34) AS SU_34 ");
		sb.append(",SUM(X.KG_34) AS KG_34 ");
		sb.append(",SUM(X.SU_WEB) AS SU_WEB ");
		sb.append(",SUM(X.KG_WEB) AS KG_WEB ");
		sb.append("FROM ( ");
		sb.append("SELECT * FROM MTA ");
		sb.append("UNION ALL ");
		sb.append("SELECT * FROM LTA ");
		sb.append("UNION ALL ");
		sb.append("SELECT * FROM PTA ");
		sb.append(")X ");
		sb.append("GROUP BY X.KHNCD ");
		sb.append(") ");

		// 表示項目の切替
		sb.append("SELECT ");
		sb.append(" 商品分野,プロモ,商品番号,商品名 ");
		sb.append(",CASE WHEN LENGTH(開始日)=8 THEN TO_CHAR(TO_DATE(開始日,'YYYYMMDD'),'YYYY/MM/DD') ELSE CAST(開始日 AS CHAR(10)) END AS 開始日 ");
		sb.append(",CASE WHEN LENGTH(終了日)=8 THEN TO_CHAR(TO_DATE(終了日,'YYYYMMDD'),'YYYY/MM/DD') ELSE CAST(終了日 AS CHAR(10)) END AS 終了日 ");
		sb.append(",プロモ対象月,通常価格,プロモ価格 ");

		// 出荷個数
		if (szJun.length==5) {
			if ("1".equals(szJun[0]))	sb.append(",出荷個数_当年実績 ");
			if ("1".equals(szJun[1]))	sb.append(",出荷個数_前年実績 ");
			if ("1".equals(szJun[2]))	sb.append(",出荷個数_計画値 ");
			if ("1".equals(szJun[3]))	sb.append(",出荷個数_前年比 ");
			if ("1".equals(szJun[4]))	sb.append(",出荷個数_計画比 ");
		}
		// 出荷金額
		if (szShu.length==5) {
			if ("1".equals(szShu[0]))	sb.append(",出荷金額_当年実績 ");
			if ("1".equals(szShu[1]))	sb.append(",出荷金額_前年実績 ");
			if ("1".equals(szShu[2]))	sb.append(",出荷金額_計画値 ");
			if ("1".equals(szShu[3]))	sb.append(",出荷金額_前年比 ");
			if ("1".equals(szShu[4]))	sb.append(",出荷金額_計画比 ");
		}
		// WEB実績
		if (szCho.length==12) {
			if ("1".equals(szCho[0]))	sb.append(",WEB実績_PC個数 ");
			if ("1".equals(szCho[1]))	sb.append(",WEB実績_PC個数率 ");
			if ("1".equals(szCho[2]))	sb.append(",WEB実績_PC金額 ");
			if ("1".equals(szCho[3]))	sb.append(",WEB実績_PC金額率 ");

			if ("1".equals(szCho[4]))	sb.append(",WEB実績_スマホ個数 ");
			if ("1".equals(szCho[5]))	sb.append(",WEB実績_スマホ個数率 ");
			if ("1".equals(szCho[6]))	sb.append(",WEB実績_スマホ金額 ");
			if ("1".equals(szCho[7]))	sb.append(",WEB実績_スマホ金額率 ");

			if ("1".equals(szCho[8]))	sb.append(",WEB実績_WEB個数 ");
			if ("1".equals(szCho[9]))	sb.append(",WEB実績_WEB個数率 ");
			if ("1".equals(szCho[10]))	sb.append(",WEB実績_WEB金額 ");
			if ("1".equals(szCho[11]))	sb.append(",WEB実績_WEB金額率 ");
		}

		sb.append("FROM ( ");
		// 明細（商品分野=「すべて」以外の場合）
		if (!DefineReport.RadD14_CLS.ID5.getVal().equals(szITEM)) {
			sb.append("SELECT ");
			sb.append(" KHNCD, CATCD, MAX(CPFLG) AS CPFLG, ");
			sb.append(" CASE WHEN KHNCD = '10' THEN '化粧品' WHEN KHNCD = '20' THEN '食品' WHEN KHNCD = '30' THEN 'ボディウェア' ELSE 'その他' END AS 商品分野 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) = 0 THEN '○'          END AS プロモ ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) = 0 THEN MAX(ITEMNO)   END AS 商品番号 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) = 0 THEN MAX(ITEMNM) ELSE '計' END AS 商品名 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) = 0 THEN MAX(PRODT_ST) END AS 開始日 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) = 0 THEN MAX(PRODT_LT) END AS 終了日 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) = 0 THEN MAX(PMONTH)   END AS プロモ対象月 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) = 0 THEN MAX(PRICE)    END AS 通常価格 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) = 0 THEN MAX(UNIT)     END AS プロモ価格 ");

			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO)<>0 THEN ");
			sb.append("   SUM(CASE WHEN CPFLG=0 THEN SU_TONT END) ");
			sb.append(" ELSE SUM(SU_TONT) ");
			sb.append(" END  AS 出荷個数_当年実績 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO)<>0 THEN ");
			sb.append("   SUM(CASE WHEN CPFLG=0 THEN SU_ZENT END) ");
			sb.append(" ELSE SUM(SU_ZENT) ");
			sb.append(" END AS 出荷個数_前年実績 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO)<>0 THEN ");
			sb.append("   SUM(CASE WHEN CPFLG=0 THEN SU_PLAN END) ");
			sb.append(" ELSE SUM(SU_PLAN) ");
			sb.append(" END AS 出荷個数_計画値 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO)<>0 THEN ");
			sb.append("   CASE WHEN SUM(CASE WHEN CPFLG=0 THEN SU_ZENT END) = 0 THEN NULL ELSE ROUND(SUM(CASE WHEN CPFLG=0 THEN SU_TONT END) / SUM(CASE WHEN CPFLG=0 THEN SU_ZENT END) * 100,2) END ");
			sb.append(" ELSE ");
			sb.append("   CASE WHEN SUM(SU_ZENT) = 0 THEN NULL ELSE ROUND(SUM(SU_TONT) / SUM(SU_ZENT) * 100,2) END ");
			sb.append(" END AS 出荷個数_前年比 ");
			sb.append(",CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO)<>0 THEN ");
			sb.append("   CASE WHEN SUM(CASE WHEN CPFLG=0 THEN SU_PLAN END) = 0 THEN NULL ELSE ROUND(SUM(CASE WHEN CPFLG=0 THEN SU_TONT END) / SUM(CASE WHEN CPFLG=0 THEN  SU_PLAN END) * 100,2) END ");
			sb.append(" ELSE ");
			sb.append("   CASE WHEN SUM(SU_PLAN) = 0 THEN NULL ELSE ROUND(SUM(SU_TONT) / SUM(SU_PLAN) * 100,2) END ");
			sb.append(" END AS 出荷個数_計画比 ");

			sb.append(",SUM(KG_TONT) AS 出荷金額_当年実績 ");
			sb.append(",SUM(KG_ZENT) AS 出荷金額_前年実績 ");
			sb.append(",SUM(KG_PLAN) AS 出荷金額_計画値 ");
			sb.append(",CASE WHEN SUM(KG_ZENT) = 0 THEN NULL ELSE ROUND(SUM(KG_TONT) / SUM(KG_ZENT) * 100,2) END AS 出荷金額_前年比 ");
			sb.append(",CASE WHEN SUM(KG_PLAN) = 0 THEN NULL ELSE ROUND(SUM(KG_TONT) / SUM(KG_PLAN) * 100,2) END AS 出荷金額_計画比 ");
			sb.append(", CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) <> 0 THEN SUM(CASE WHEN CPFLG = 0 THEN SU_30 END) ELSE SUM(SU_30) END AS WEB実績_PC個数 ");
			sb.append(", CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) <> 0 THEN CASE WHEN SUM(CASE WHEN CPFLG = 0 THEN SU_TONT END) = 0 THEN NULL ELSE ROUND(SUM(CASE WHEN CPFLG = 0 THEN SU_30 END) / SUM(CASE WHEN CPFLG = 0 THEN SU_TONT END) * 100, 2) END ELSE CASE WHEN SUM(SU_TONT) = 0 THEN NULL ELSE ROUND(SUM(SU_30) / SUM(SU_TONT) * 100, 2) END END AS WEB実績_PC個数率 ");
			sb.append(",SUM(KG_30) AS WEB実績_PC金額 ");
			sb.append(",CASE WHEN SUM(KG_TONT) = 0 THEN NULL ELSE ROUND(SUM(KG_30) / SUM(KG_TONT) * 100,2) END AS WEB実績_PC金額率 ");
			sb.append(", CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) <> 0 THEN SUM(CASE WHEN CPFLG = 0 THEN SU_34 END) ELSE SUM(SU_34) END AS WEB実績_スマホ個数 ");
			sb.append(", CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) <> 0 THEN CASE WHEN SUM(CASE WHEN CPFLG = 0 THEN SU_TONT END) = 0 THEN NULL ELSE ROUND(SUM(CASE WHEN CPFLG = 0 THEN SU_34 END) / SUM(CASE WHEN CPFLG = 0 THEN SU_TONT END) * 100, 2) END ELSE CASE WHEN SUM(SU_TONT) = 0 THEN NULL ELSE ROUND(SUM(SU_34) / SUM(SU_TONT) * 100, 2) END END AS WEB実績_スマホ個数率 ");
			sb.append(",SUM(KG_34) AS WEB実績_スマホ金額 ");
			sb.append(",CASE WHEN SUM(KG_TONT) = 0 THEN NULL ELSE ROUND(SUM(KG_34) / SUM(KG_TONT) * 100,2) END AS WEB実績_スマホ金額率 ");
			sb.append(", CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) <> 0 THEN SUM(CASE WHEN CPFLG = 0 THEN SU_WEB END) ELSE SUM(SU_WEB) END AS WEB実績_WEB個数 ");
			sb.append(", CASE WHEN GROUPING_ID(KHNCD, CATCD, ITEMNO) <> 0 THEN CASE WHEN SUM(CASE WHEN CPFLG = 0 THEN SU_TONT END) = 0 THEN NULL ELSE ROUND(SUM(CASE WHEN CPFLG = 0 THEN SU_WEB END) / SUM(CASE WHEN CPFLG = 0 THEN SU_TONT END) * 100, 2) END ELSE CASE WHEN SUM(SU_TONT) = 0 THEN NULL ELSE ROUND(SUM(SU_WEB) / SUM(SU_TONT) * 100, 2) END END AS WEB実績_WEB個数率 ");
			sb.append(",SUM(KG_WEB) AS WEB実績_WEB金額 ");
			sb.append(",CASE WHEN SUM(KG_TONT) = 0 THEN NULL ELSE ROUND(SUM(KG_WEB) / SUM(KG_TONT) * 100,2) END AS WEB実績_WEB金額率 ");
			sb.append("FROM MT ");
			sb.append("GROUP BY GROUPING SETS ((KHNCD, CATCD, ITEMNO),(KHNCD, CATCD)) ");
			sb.append("UNION ALL ");
		}

		// 分野計
		sb.append("SELECT ");
		sb.append(" KHNCD, CATCD, 0 AS CPFLG, ");
		sb.append(" CASE WHEN KHNCD = '10' THEN '化粧品' WHEN KHNCD = '20' THEN '食品' WHEN KHNCD = '30' THEN 'ボディウェア' ELSE 'その他' END AS 商品分野 ");
		sb.append(",CASE WHEN CATCD = '' THEN '' WHEN CATCD = '99999991' THEN '○' WHEN CATCD = '99999992' THEN '×' WHEN CATCD = '99999993' THEN '全体' ELSE '○' END AS プロモ ");
		sb.append(",ITEMNO AS 商品番号 ");
		sb.append(",CASE WHEN CATCD IN ('99999991','99999992','99999993') THEN '計' ELSE ITEMNM END AS 商品名 ");
		sb.append(",PRODT_ST AS 開始日 ");
		sb.append(",PRODT_LT AS 終了日 ");
		sb.append(",PMONTH AS プロモ対象月 ");
		sb.append(",PRICE AS 通常価格 ");
		sb.append(",UNIT AS プロモ価格 ");
		sb.append(",SU_TONT AS 出荷個数_当年実績 ");
		sb.append(",SU_ZENT AS 出荷個数_前年実績 ");
		sb.append(",SU_PLAN AS 出荷個数_計画値 ");
		sb.append(",CASE WHEN SU_ZENT = 0 THEN NULL ELSE ROUND(SU_TONT / SU_ZENT * 100,2) END AS 出荷個数_前年比 ");
		sb.append(",CASE WHEN SU_PLAN = 0 THEN NULL ELSE ROUND(SU_TONT / SU_PLAN * 100,2) END AS 出荷個数_計画比 ");
		sb.append(",KG_TONT AS 出荷金額_当年実績 ");
		sb.append(",KG_ZENT AS 出荷金額_前年実績 ");
		sb.append(",KG_PLAN AS 出荷金額_計画値 ");
		sb.append(",CASE WHEN KG_ZENT = 0 THEN NULL ELSE ROUND(KG_TONT / KG_ZENT * 100,2) END AS 出荷金額_前年比 ");
		sb.append(",CASE WHEN KG_PLAN = 0 THEN NULL ELSE ROUND(KG_TONT / KG_PLAN * 100,2) END AS 出荷金額_計画比 ");
		sb.append(",SU_30 AS WEB実績_PC個数 ");
		sb.append(",CASE WHEN SU_TONT = 0 THEN NULL ELSE ROUND(SU_30 / SU_TONT * 100,2) END AS WEB実績_PC個数率 ");
		sb.append(",KG_30 AS WEB実績_PC金額 ");
		sb.append(",CASE WHEN KG_TONT = 0 THEN NULL ELSE ROUND(KG_30 / KG_TONT * 100,2) END AS WEB実績_PC金額率 ");
		sb.append(",SU_34 AS WEB実績_スマホ個数 ");
		sb.append(",CASE WHEN SU_TONT = 0 THEN NULL ELSE ROUND(SU_34 / SU_TONT * 100,2) END AS WEB実績_スマホ個数率 ");
		sb.append(",KG_34 AS WEB実績_スマホ金額 ");
		sb.append(",CASE WHEN KG_TONT = 0 THEN NULL ELSE ROUND(KG_34 / KG_TONT * 100,2) END AS WEB実績_スマホ金額率 ");
		sb.append(",SU_WEB AS WEB実績_WEB個数 ");
		sb.append(",CASE WHEN SU_TONT = 0 THEN NULL ELSE ROUND(SU_WEB / SU_TONT * 100,2) END AS WEB実績_WEB個数率 ");
		sb.append(",KG_WEB AS WEB実績_WEB金額 ");
		sb.append(",CASE WHEN KG_TONT = 0 THEN NULL ELSE ROUND(KG_WEB / KG_TONT * 100,2) END AS WEB実績_WEB金額率 ");
		sb.append(" FROM ( ");
		sb.append("SELECT * FROM MTA ");
		sb.append("UNION ALL ");
		sb.append("SELECT * FROM LTA ");
		sb.append("UNION ALL ");
		sb.append("SELECT * FROM UTA ");
		sb.append(") XDY ");

		// 合計（商品分野=「すべて」の場合）
		if (DefineReport.RadD14_CLS.ID5.getVal().equals(szITEM)) {
			sb.append("UNION ALL ");
			sb.append("SELECT ");
			sb.append(" 99 AS KHNCD, CATCD, 0 AS CPFLG, ");
			sb.append(" '合計' AS 商品分野 ");
			sb.append(",CASE WHEN CATCD = '' THEN '' WHEN CATCD = '99999991' THEN '○' WHEN CATCD = '99999992' THEN '×' WHEN CATCD = '99999993' THEN '全体' ELSE '○' END AS プロモ ");
			sb.append(",NULL AS 商品番号 ");
			sb.append(",'計' AS 商品名 ");
			sb.append(",NULL AS 開始日 ");
			sb.append(",NULL AS 終了日 ");
			sb.append(",NULL AS プロモ対象月 ");
			sb.append(",NULL AS 通常価格 ");
			sb.append(",NULL AS プロモ価格 ");
			sb.append(",SUM(SU_TONT) AS 出荷個数_当年実績 ");
			sb.append(",SUM(SU_ZENT) AS 出荷個数_前年実績 ");
			sb.append(",SUM(SU_PLAN) AS 出荷個数_計画値 ");
			sb.append(",CASE WHEN SUM(SU_ZENT) = 0 THEN NULL ELSE ROUND(SUM(SU_TONT) / SUM(SU_ZENT) * 100,2) END AS 出荷個数_前年比 ");
			sb.append(",CASE WHEN SUM(SU_PLAN) = 0 THEN NULL ELSE ROUND(SUM(SU_TONT) / SUM(SU_PLAN) * 100,2) END AS 出荷個数_計画比 ");
			sb.append(",SUM(KG_TONT) AS 出荷金額_当年実績 ");
			sb.append(",SUM(KG_ZENT) AS 出荷金額_前年実績 ");
			sb.append(",SUM(KG_PLAN) AS 出荷金額_計画値 ");
			sb.append(",CASE WHEN SUM(KG_ZENT) = 0 THEN NULL ELSE ROUND(SUM(KG_TONT) / SUM(KG_ZENT) * 100,2) END AS 出荷金額_前年比 ");
			sb.append(",CASE WHEN SUM(KG_PLAN) = 0 THEN NULL ELSE ROUND(SUM(KG_TONT) / SUM(KG_PLAN) * 100,2) END AS 出荷金額_計画比 ");
			sb.append(",SUM(SU_30) AS WEB実績_PC個数 ");
			sb.append(",CASE WHEN SUM(SU_TONT) = 0 THEN NULL ELSE ROUND(SUM(SU_30) / SUM(SU_TONT) * 100,2) END AS WEB実績_PC個数率 ");
			sb.append(",SUM(KG_30) AS WEB実績_PC金額 ");
			sb.append(",CASE WHEN SUM(KG_TONT) = 0 THEN NULL ELSE ROUND(SUM(KG_30) / SUM(KG_TONT) * 100,2) END AS WEB実績_PC金額率 ");
			sb.append(",SUM(SU_34) AS WEB実績_スマホ個数 ");
			sb.append(",CASE WHEN SUM(SU_TONT) = 0 THEN NULL ELSE ROUND(SUM(SU_34) / SUM(SU_TONT) * 100,2) END AS WEB実績_スマホ個数率 ");
			sb.append(",SUM(KG_34) AS WEB実績_スマホ金額 ");
			sb.append(",CASE WHEN SUM(KG_TONT) = 0 THEN NULL ELSE ROUND(SUM(KG_34) / SUM(KG_TONT) * 100,2) END AS WEB実績_スマホ金額率 ");
			sb.append(",SUM(SU_WEB) AS WEB実績_WEB個数 ");
			sb.append(",CASE WHEN SUM(SU_TONT) = 0 THEN NULL ELSE ROUND(SUM(SU_WEB) / SUM(SU_TONT) * 100,2) END AS WEB実績_WEB個数率 ");
			sb.append(",SUM(KG_WEB) AS WEB実績_WEB金額 ");
			sb.append(",CASE WHEN SUM(KG_TONT) = 0 THEN NULL ELSE ROUND(SUM(KG_WEB) / SUM(KG_TONT) * 100,2) END AS WEB実績_WEB金額率 ");
			sb.append(" FROM ( ");
			sb.append("SELECT * FROM MTA ");
			sb.append("UNION ALL ");
			sb.append("SELECT * FROM LTA ");
			sb.append("UNION ALL ");
			sb.append("SELECT * FROM UTA ");
			sb.append(") XDY ");
			sb.append("GROUP BY CATCD ");
		}

		sb.append(") X ");
		sb.append("WHERE ");
		sb.append(" NVL(商品番号,'1')<>'0' ");		// カテゴリ計除外

		// 並び順
		sb.append("ORDER BY KHNCD,CATCD,プロモ,CPFLG DESC,CAST(商品番号 AS INTEGER) ");

		System.out.println(sb.toString());
		command = sb.toString();
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

		List<String> cells = new ArrayList<String>();

		// 購入チャネル
		cells.add(DefineReport.RadD01_CHANEL.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD01_CHANEL.NAME.getObj()));
		getWhere().add(cells);

		cells = new ArrayList<String>();

		// 集計期間
		cells.add(DefineReport.Label.LABELD03.getView());
		cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 商品分野
		cells.add(DefineReport.RadD14_CLS.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD14_CLS.NAME.getObj()));
		getWhere().add(cells);

		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
