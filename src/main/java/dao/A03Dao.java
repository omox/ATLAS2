package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import common.CmnDate;
import common.DefineReport;
import common.JsonArrayData;

/**
 * A03テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class A03Dao extends ItemDao {

	/**
	 * 新しいA03Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public A03Dao(Connection source) {
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

		String szYm 		=	getMap().get("YYYY_MM");			// （必須）基準年月
		String szType1 		=	getMap().get("TYPE1");			// （
		String szType2 		=	getMap().get("TYPE2");			// 1:顧客数 3:初回受注金額 5:累計受注金額 7:平均年齢 8:平均単価
		String szJucYM1 	=	getMap().get("JUCYM1");			//
		String szJucYM2 	=	getMap().get("JUCYM2");			//
		String szBytai 		=	getMap().get("BYTAI");			//
		String szBytai2 	=	getMap().get("BYTAI2");			//

		// パラメータ確認
		System.out.println("YYYY_MM:" +szYm+",TYPE1:"+szType1+",TYPE2:"+szType2+",JUCYM1:"+szJucYM1+",JUCYM2:"+szJucYM2+",BYTAI:"+szBytai+",BYTAI2:"+szBytai2);

		// 年月設定
		String sqlTable= " ";
		String kijunYm = DefineReport.base.TABLE_TP.getVal();	// 基準年月;
		if(szYm.compareTo(kijunYm) > 0){   //年月が基準より大きい時
			sqlTable  = " TSU_KYAKU";
		} else  {                           //年月が基準より小さい時
			sqlTable  = " TSU_KYAKU_"+szYm.substring(0, 4);
		}
		String sqlTable2= " M_PROMO_ALL ";
		String sqlWhere = " KIJUN_YM ='"+szYm+"' ";
		//横軸初回受注月以外で初回受注月指定の場合
		if(!StringUtils.equals(szType1, "1")){
			if((StringUtils.equals(szJucYM1, "00"))&&(StringUtils.equals(szJucYM2, "00"))){
				sqlWhere += "  ";
			} else if (StringUtils.equals(szJucYM2, "00")) {
				sqlWhere += " AND NF_JUCHU_YM >= '"+szJucYM1+"' ";
			} else if (StringUtils.equals(szJucYM1, "00")) {
				sqlWhere += " AND NF_JUCHU_YM <= '"+szJucYM2+"' ";
			} else  {
				sqlWhere += " AND NF_JUCHU_YM >= '"+szJucYM1+"' AND NF_JUCHU_YM <= '"+szJucYM2+"' ";
			}
		}
		// 数値項目設定
		String selItem  = "KYAKUSU";			//1:顧客数
		String selItem2  = "AGE";			//年齢
		String sqlWhereAge = " ";
		if(StringUtils.equals(szType2, "3")){   //3:初回受注金額
			selItem  = "F_KINGAKU";
		} else if (StringUtils.equals(szType2, "5")) {  //5:累計受注金額
			selItem  = "G_KINGAKU";
		} else if (StringUtils.equals(szType2, "7")) {  //7:平均年齢
			selItem  = "KYAKUSU";
			selItem2  = "AGE2*KYAKUSU";
			sqlWhereAge  = " AND AGE > 12 AND AGE < 100 ";
		} else if (StringUtils.equals(szType2, "8")) {  //8:平均単価顧客
			selItem  = "KYAKUSU";
			selItem2  = "F_KINGAKU";
		}
		String[] ItemC1  = {"AS \"媒体\"","AS \"媒体種類\"","AS \"プロモーション\""};
		//String[] itemM = { "AS \"0-0.5万未満\"", "AS \"0.5-1万未満\"", "AS \"1-1.5万未満\"", "AS \"1.5-2万未満\"", "AS \"2-3万未満\"", "AS \"3-4万未満\"", "AS \"4-5万未満\"", "AS \"5-6万未満\"", "AS \"6-10万未満\"", "AS \"10万以上\"" };
		String[] itemM = { "AS \"0-0.5万未満\"", "AS \"0.5-1万未満\"", "AS \"1-1.5万未満\"", "AS \"1.5-2万未満\"", "AS \"2-3万未満\"", "AS \"3-4万未満\"", "AS \"4-5万未満\"", "AS \"5-6万未満\"", "AS \"6-10万未満\"", "AS \"10万以上\"" };
	    String[] itemS  = { "AS \"アクアミニセット\"", "AS \"アクア単品\"", "AS \"クリアミニセット\"", "AS \"クリア単品\"", "AS \"WHミニセット単品\"", "AS \"WH単品\"", "AS \"AWミニセット単品\"", "AS \"AW単品\"", "AS \"EXミニセット単品\"", "AS \"EX単品\"", "AS \"EXWHミニセット単品\"", "AS \"EXWH単品\"", "AS \"ライブリッチ単品\"", "AS \"その他単品\"", "AS \"スキンケア複数\"", "AS \"スキン＋クレンジング\"", "AS \"スキン＋ポイント\"", "AS \"スキン＋ベース\"", "AS \"スキン＋UVケア\"", "AS \"スキン＋化粧品\"", "AS \"クレンジングのみ\"", "AS \"UVケアのみ\"", "AS \"ピュアプレシャス\"", "AS \"リップシャイニー\"", "AS \"ルージュクリスタル\"", "AS \"リキッドルージュ\"", "AS \"スムースマット\"", "AS \"ライブリッチBB\"", "AS \"その他メイクのみ\"", "AS \"メイク＋UVケア\"", "AS \"ボディ\"", "AS \"化粧品その他\"", "AS \"スキン＋ダイエット\"", "AS \"スキン＋食品\"", "AS \"化粧品＋食品その他\"", "AS \"化粧品＋ボディ\"", "AS \"ダイエットのみ\"", "AS \"その他食品\"", "AS \"食品＋ボディウェア\"", "AS \"ボディウェア\"", "AS \"その他\"", "AS \"不明\"" };
		String[] itemS1 = { "AS \"アクアミニセット\"", "AS \"アクア単品\"", "AS \"クリアミニセット\"", "AS \"クリア単品\"", "AS \"WHミニセット単品\"", "AS \"WH単品\"", "AS \"AWミニセット単品\"", "AS \"AW単品\"", "AS \"EXミニセット単品\"", "AS \"EX単品\"", "AS \"EXWHminiセット単品\"", "AS \"EXWH単品\"", "AS \"その他単品\"", "AS \"スキンケア複数\"", "AS \"ケア＋クレンジング\"", "AS \"ケア＋ポイントメイク\"", "AS \"ケア＋ベースメイク\"", "AS \"ケア＋UVケア\"", "AS \"スキンケア＋その他化粧品\"", "AS \"クレンジングリキッドのみ\"", "AS \"UVケアのみ\"", "AS \"ピュアプレシャスルージュ\"", "AS \"リップシャイニー\"", "AS \"ルージュクリスタル\"", "AS \"リキッドルージュ\"", "AS \"スムースマットベース\"", "AS \"その他メイクのみ\"", "AS \"メイク＋UVケア\"", "AS \"クリアボディローション\"", "AS \"化粧品その他\"", "AS \"ケア＋ダイエット\"", "AS \"スキンケア＋食品\"", "AS \"化粧品＋食品その他\"", "AS \"化粧品＋ボディウェア\"", "AS \"ダイエットのみ\"", "AS \"その他食品\"", "AS \"食品＋ボディウェア\"", "AS \"ボディウェア\"", "AS \"その他\"", "AS \"不明\"" };
		//String[] itemS = { "AS \"A\"", "AS \"B\"", "AS \"C\"", "AS \"D単品\"", "AS \"E単品\"", "AS \"WH単品\"", "AS \"AWG単品\"", "AS \"AW単品\"", "AS \"EXGG単品\"", "AS \"EX単品\"", "AS \"EXWHミニセット単品\"", "AS \"EXWH単品\"", "AS \"その他スキンケア単品\"", "AS \"スキンケア複数\"", "AS \"スキンケア＋クレンジングリキッド\"", "AS \"スキンケア＋ポイントメイク\"", "AS \"スキンケア＋ベースメイク\"", "AS \"スキンケア＋UVケア\"", "AS \"スキンケア＋その他化粧品\"", "AS \"クレンジングリキッドのみ\"", "AS \"UVケアのみ\"", "AS \"ピュアプレシャスルージュ\"", "AS \"リップシャイニー\"", "AS \"ルージュクリスタル\"", "AS \"リキッドルージュ\"", "AS \"スムースマットベース\"", "AS \"その他メイクのみ\"", "AS \"メイク＋UVケア\"", "AS \"クリアボディローション\"", "AS \"化粧品その他\"", "AS \"スキンケア＋ダイエット\"", "AS \"スキンケア＋食品\"", "AS \"化粧品＋食品その他\"", "AS \"化粧品＋ボディウェア\"", "AS \"ダイエットのみ\"", "AS \"その他食品\"", "AS \"食品＋ボディウェア\"", "AS \"ボディウェア\"", "AS \"その他\"", "AS \"不明\"" };
		String[] itemB = { "AS \"01_カタログ\"", "AS \"02_新聞本紙\"", "AS \"03_チラシ\"", "AS \"04_雑誌\"", "AS \"05_紹介\"", "AS \"06_テレビ\"", "AS \"07_ネット\"", "AS \"08_店舗新規\"", "AS \"09_その他\"", "AS \"10_他社封入\"", "AS \"11_モバイル\"" };
		String[] itemN = { "AS \"20歳未満\"", "AS \"20-24歳\"", "AS \"25-29歳\"", "AS \"30-34歳\"", "AS \"35-39歳\"", "AS \"40-44歳\"", "AS \"45-49歳\"", "AS \"50-54歳\"", "AS \"55-59歳\"", "AS \"60-69歳\"", "AS \"70歳以上\"", "AS \"不明\"", "AS \"計\"" };
		String[] itemJ = { "AS \"電話\"", "AS \"ハガキ\"", "AS \"封書\"", "AS \"ＦＡＸ\"", "AS \"機械受注\"", "AS \"連合\"", "AS \"ｲﾝﾀｰﾈｯﾄ\"", "AS \"携帯\"", "AS \"Ｙａｈｏｏ\"", "AS \"定期受注\"", "AS \"その他\"", "AS \"不明\"", "AS \"計\"" };
		//String[] ItemC2  = {"AS \"１月\"","AS \"２月\"","AS \"３月\"","AS \"４月\"","AS \"５月\"","AS \"６月\"","AS \"７月\"","AS \"８月\"","AS \"９月\"","AS \"１０月\"","AS \"１１月\"","AS \"１２月\""};

		String itemT  = " PROMO_CD ";
		String itemT2  = " PROMO_CD ";
		String whereB1  = " BYTAI1_CD ";
		String whereB1G = " BYTAI1_CD ";
		String whereB1A = "  ";
		String whereB1A2 = "  ";
		String whereB1M = " BYTAI1_CD||'_'||RTRIM(MAX(BYTAI1_MEI)) ";
		String whereTable = " WORKT ";

		// PROMO番号設定
		String sqlPromo = "  ";
		String sqlPromo2 = "  ";
		if (!StringUtils.isEmpty(szBytai2)) {
			JSONArray bytArray = JSONArray.fromObject(StringUtils.split(szBytai2, ","));
			String bytNo = "";
			for (int i = 0; i < bytArray.size(); i++) {
				bytNo += "'"+bytArray.get(i).toString() + "',";
			}
			sqlPromo = " WHERE TRIM(PROMO_CD) in (" + StringUtils.removeEnd(bytNo, ",") + ")";
			sqlPromo2 = " and TRIM(T.PROMO_CD) in (" + StringUtils.removeEnd(bytNo, ",") + ")";
		}
		if(!StringUtils.equals(szBytai, "00")){ //初回媒体
			 whereB1 = " BYTAI2_CD ";
			 whereB1G = " BYTAI2_CD ";
			 whereB1M = " BYTAI2_CD||'_'|| RTRIM(MAX(BYTAI2_MEI)) ";
			 whereB1A = " WHERE BYTAI1_CD ='"+szBytai+"'";
			 whereB1A2 = " and BYTAI1_CD ='"+szBytai+"'";
			 whereTable = " M_PROMO_ALL INNER JOIN WORKT ON TPROMO_CD = PROMO_CD ";
		}
		if(!StringUtils.equals(szBytai2, "")){ //PROMO_CD
			if((StringUtils.equals(szType2, "8"))||(StringUtils.equals(szType2, "7")))  {
				 whereB1 = " T.PROMO_CD ";
			}else  {
				 whereB1 = " PROMO_CD ";
			}
			 whereB1G = " PROMO_CD ";
			 whereB1M = " PROMO_CD||'_'|| RTRIM(MAX(PROMO_MEI)) ";
			 whereB1A = sqlPromo ;
			 whereB1A2 = sqlPromo2 ;
			 whereTable = " M_PROMO_ALL INNER JOIN WORKT ON TPROMO_CD = PROMO_CD ";
		}

		String whereC1 = "";
		String whereC1A = "";
		String whereC1s = "";
		String whereC1sA = "";
		String whereC2 = "";
		String whereC2A = "";
		String whereC3 = "";
		String whereC3A = "";
		String whereC4 = "";
		if(StringUtils.equals(szType1, "1")){  //初回媒体 x 初回受注月
			sqlWhere += " AND NF_JUCHU_YM >= '"+szJucYM1+"' AND NF_JUCHU_YM <= '"+szJucYM2+"' ";
			String[] dtArray = CmnDate.getMonthFromTo(szJucYM1, szJucYM2);
			for (String dt : dtArray) {
				whereC1 += ",M" + dt ;
				whereC1s += ",SUM(M" + dt+")" ;
				whereC1A += ",M" + dt +"A";
				whereC1sA += ",SUM(M" + dt +"A)";
				whereC2 += ",SUM(M"+dt+") AS \""+dt+"\"";
				whereC2A += ",CASE WHEN SUM(M"+dt+") > 0 THEN ROUND(SUM(M"+dt+"A)/SUM(M"+dt+"),0) ELSE 0 END AS \""+dt+"\"";
				whereC3 += ",CASE WHEN NF_JUCHU_YM = '"+dt+"' THEN SUM("+selItem+") ELSE 0 END";
				whereC3A += ",CASE WHEN NF_JUCHU_YM = '"+dt+"' THEN SUM("+selItem2+") ELSE 0 END";
			}
			whereC1 += ",MALL ";
			whereC1s += ",SUM(MALL) ";
			whereC1A += ",MALLA ";
			whereC1sA += ",SUM(MALLA) ";
			whereC2 += ",SUM(MALL) AS 計 ";
			whereC2A += ",CASE WHEN SUM(MALL) > 0 THEN ROUND(SUM(MALLA)/SUM(MALL),0) ELSE 0 END AS 計 ";
			whereC3 += ",SUM("+selItem+") ";
			whereC3A += ",SUM("+selItem2+") ";
			whereC4 = ",NF_JUCHU_YM";
		}else if (StringUtils.equals(szType1, "2")){ // 初回媒体 x R
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,MALL";
			whereC1A +=",M01A,M02A,M03A,M04A,M05A,M06A,M07A,M08A,M09A,MALLA";
			whereC1s +=",sum(M01),sum(M02),sum(M03),sum(M04),sum(M05),sum(M06),sum(M07),sum(M08),sum(M09),sum(MALL)";
			whereC1sA +=",sum(M01A),sum(M02A),sum(M03A),sum(M04A),sum(M05A),sum(M06A),sum(M07A),sum(M08A),sum(M09A),sum(MALLA)";
			whereC2 +=",SUM(M01) AS \"直近３ヶ月\",SUM(M02) AS \"４ー６ヶ月\",SUM(M03) AS \"７ー９ヶ月\",SUM(M04) AS \"１０ー１２ヶ月\",SUM(M05) AS \"１年超ー２年\",SUM(M06) AS \"２年超ー３年\",SUM(M07) AS \"３年超ー４年\",SUM(M08) AS \"４年超ー５年\",SUM(M09) AS \"５年以上\",SUM(MALL) AS \"計\"";
			whereC2A +=",CASE WHEN SUM(M01) > 0 THEN ROUND(SUM(M01A)/SUM(M01),0) ELSE 0 END AS \"直近３ヶ月\",CASE WHEN SUM(M02) > 0 THEN ROUND(SUM(M02A)/SUM(M02),0) ELSE 0 END AS \"４ー６ヶ月\",CASE WHEN SUM(M03) > 0 THEN ROUND(SUM(M03A)/SUM(M03),0) ELSE 0 END AS \"７ー９ヶ月\",CASE WHEN SUM(M04) > 0 THEN ROUND(SUM(M04A)/SUM(M04),0) ELSE 0 END AS \"１０ー１２ヶ月\",CASE WHEN SUM(M05) > 0 THEN ROUND(SUM(M05A)/SUM(M05),0) ELSE 0 END AS \"１年超ー２年\",CASE WHEN SUM(M06) > 0 THEN ROUND(SUM(M06A)/SUM(M06),0) ELSE 0 END AS \"２年超ー３年\",CASE WHEN SUM(M07) > 0 THEN ROUND(SUM(M07A)/SUM(M07),0) ELSE 0 END AS \"３年超ー４年\",CASE WHEN SUM(M08) > 0 THEN ROUND(SUM(M08A)/SUM(M08),0) ELSE 0 END AS \"４年超ー５年\",CASE WHEN SUM(M09) > 0 THEN ROUND(SUM(M09A)/SUM(M09),0) ELSE 0 END AS \"５年以上\",CASE WHEN SUM(MALL) > 0 THEN ROUND(SUM(MALLA)/SUM(MALL),0) ELSE 0 END  AS \"計\"";
			whereC3 +=",CASE WHEN JUCHUMR <= 3 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 3  AND JUCHUMR <= 6  THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 6  AND JUCHUMR <= 9  THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 9  AND JUCHUMR <= 12 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 12 AND JUCHUMR <= 24 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 24 AND JUCHUMR <= 36 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 36 AND JUCHUMR <= 48 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 48 AND JUCHUMR <= 60 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 60 THEN SUM("+selItem+") ELSE 0 END,SUM("+selItem+") ";
			whereC3A +=",CASE WHEN JUCHUMR <= 3 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCHUMR > 3  AND JUCHUMR <= 6  THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCHUMR > 6  AND JUCHUMR <= 9  THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCHUMR > 9  AND JUCHUMR <= 12 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCHUMR > 12 AND JUCHUMR <= 24 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCHUMR > 24 AND JUCHUMR <= 36 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCHUMR > 36 AND JUCHUMR <= 48 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCHUMR > 48 AND JUCHUMR <= 60 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCHUMR > 60 THEN SUM("+selItem+") ELSE 0 END,SUM("+selItem2+") ";
			whereC4 = ",JUCHUMR";
		}else if (StringUtils.equals(szType1, "3")){ // F
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,MALL";
			whereC1A +=",M01A,M02A,M03A,M04A,M05A,M06A,M07A,M08A,M09A,M10A,MALLA";
			whereC1s +=",sum(M01),sum(M02),sum(M03),sum(M04),sum(M05),sum(M06),sum(M07),sum(M08),sum(M09),sum(M10),sum(MALL)";
			whereC1sA +=",sum(M01A),sum(M02A),sum(M03A),sum(M04A),sum(M05A),sum(M06A),sum(M07A),sum(M08A),sum(M09A),sum(M10A),sum(MALLA)";
			whereC2 +=",SUM(M01) AS \"1回\",SUM(M02) AS \"2回\",SUM(M03) AS \"3回\",SUM(M04) AS \"4回\",SUM(M05) AS \"5回\",SUM(M06) AS \"6回\",SUM(M07) AS \"7回\",SUM(M08) AS \"8回\",SUM(M09) AS \"9回\",SUM(M10) AS \"10回以上\",SUM(MALL) AS \"回数合計\"";
			whereC2A +=",CASE WHEN SUM(M01) > 0 THEN ROUND(SUM(M01A)/SUM(M01),0) ELSE 0 END AS \"1回\",CASE WHEN SUM(M02) > 0 THEN ROUND(SUM(M02A)/SUM(M02),0) ELSE 0 END AS \"2回\",CASE WHEN SUM(M03) > 0 THEN ROUND(SUM(M03A)/SUM(M03),0) ELSE 0 END AS \"3回\",CASE WHEN SUM(M04) > 0 THEN ROUND(SUM(M04A)/SUM(M04),0) ELSE 0 END AS \"4回\",CASE WHEN SUM(M05) > 0 THEN ROUND(SUM(M05A)/SUM(M05),0) ELSE 0 END AS \"5回\",CASE WHEN SUM(M06) > 0 THEN ROUND(SUM(M06A)/SUM(M06),0) ELSE 0 END AS \"6回\",CASE WHEN SUM(M07) > 0 THEN ROUND(SUM(M07A)/SUM(M07),0) ELSE 0 END AS \"7回\",CASE WHEN SUM(M08) > 0 THEN ROUND(SUM(M08A)/SUM(M08),0) ELSE 0 END AS \"8回\",CASE WHEN SUM(M09) > 0 THEN ROUND(SUM(M09A)/SUM(M09),0) ELSE 0 END AS \"9回\",CASE WHEN SUM(M10) > 0 THEN ROUND(SUM(M10A)/SUM(M10),0) ELSE 0 END AS \"10回以上\",CASE WHEN SUM(MALL) > 0 THEN ROUND(SUM(MALLA)/SUM(MALL),0) ELSE 0 END  AS \"回数合計\"";
			whereC3 +=",CASE WHEN JUCKAISU = 1 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 2 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 3 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 4 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 5 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 6 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 7 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 8 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 9 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 10 THEN SUM("+selItem+") ELSE 0 END,SUM("+selItem+")";
			whereC3A +=",CASE WHEN JUCKAISU = 1 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 2 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 3 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 4 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 5 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 6 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 7 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 8 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 9 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN JUCKAISU = 10 THEN SUM("+selItem2+") ELSE 0 END,SUM("+selItem2+")";
			whereC4 = ",JUCKAISU";
		}else if (StringUtils.equals(szType1, "4")){ // M
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,MALL";
			whereC1A +=",M01A,M02A,M03A,M04A,M05A,M06A,M07A,M08A,M09A,M10A,MALLA";
			whereC1s +=",sum(M01),sum(M02),sum(M03),sum(M04),sum(M05),sum(M06),sum(M07),sum(M08),sum(M09),sum(M10),sum(MALL)";
			whereC1sA +=",sum(M01A),sum(M02A),sum(M03A),sum(M04A),sum(M05A),sum(M06A),sum(M07A),sum(M08A),sum(M09A),sum(M10A),sum(MALLA)";
			whereC2 +=",SUM(M01) "+itemM[0]+",SUM(M02) "+itemM[1]+",SUM(M03) "+itemM[2]+",SUM(M04) "+itemM[3]+",SUM(M05) "+itemM[4]+",SUM(M06) "+itemM[5]+",SUM(M07) "+itemM[6]+",SUM(M08) "+itemM[7]+",SUM(M09) "+itemM[8]+",SUM(M10) "+itemM[9]+",SUM(MALL) AS \"計\"";
			whereC2A +=",CASE WHEN SUM(M01) > 0 THEN ROUND(SUM(M01A)/SUM(M01),0) ELSE 0 END "+itemM[0]+",CASE WHEN SUM(M02) > 0 THEN ROUND(SUM(M02A)/SUM(M02),0) ELSE 0 END "+itemM[1]+",CASE WHEN SUM(M03) > 0 THEN ROUND(SUM(M03A)/SUM(M03),0) ELSE 0 END "+itemM[2]+",CASE WHEN SUM(M04) > 0 THEN ROUND(SUM(M04A)/SUM(M04),0) ELSE 0 END "+itemM[3]+",CASE WHEN SUM(M05) > 0 THEN ROUND(SUM(M05A)/SUM(M05),0) ELSE 0 END "+itemM[4]+",CASE WHEN SUM(M06) > 0 THEN ROUND(SUM(M06A)/SUM(M06),0) ELSE 0 END "+itemM[5]+",CASE WHEN SUM(M07) > 0 THEN ROUND(SUM(M07A)/SUM(M07),0) ELSE 0 END "+itemM[6]+",CASE WHEN SUM(M08) > 0 THEN ROUND(SUM(M08A)/SUM(M08),0) ELSE 0 END "+itemM[7]+",CASE WHEN SUM(M09) > 0 THEN ROUND(SUM(M09A)/SUM(M09),0) ELSE 0 END "+itemM[8]+",CASE WHEN SUM(M10) > 0 THEN ROUND(SUM(M10A)/SUM(M10),0) ELSE 0 END "+itemM[9]+",CASE WHEN SUM(MALL) > 0 THEN ROUND(SUM(MALLA)/SUM(MALL),0) ELSE 0 END AS \"計\"";
			whereC3 +=",CASE WHEN LKONYU = 0 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 1 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 2 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 3 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 5 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 7 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 9 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 11 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 19 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 20 THEN SUM("+selItem+") ELSE 0 END,SUM("+selItem+")";
			whereC3A +=",CASE WHEN LKONYU = 0 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 1 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 2 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 3 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 5 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 7 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 9 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 11 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 19 THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN LKONYU = 20 THEN SUM("+selItem2+") ELSE 0 END,SUM("+selItem2+")";
			whereC4 = ",LKONYU";
		}else if (StringUtils.equals(szType1, "5")){ // SH
			whereC1 +=",M01,M02,M03,M04,M05,MALL";
			whereC1A +=",M01A,M02A,M03A,M04A,M05A,MALLA";
			whereC1s +=",sum(M01),sum(M02),sum(M03),sum(M04),sum(M05),sum(MALL)";
			whereC1sA +=",sum(M01A),sum(M02A),sum(M03A),sum(M04A),sum(M05A),sum(MALLA)";
 			whereC2 +=",SUM(M01) AS \"化粧品 \",SUM(M02) AS \"食品\",SUM(M03) AS \"ボディウェア\",SUM(M04) AS \"その他\",SUM(M05) AS \"不明\",SUM(MALL) AS \"計\"";
			whereC2A +=",CASE WHEN SUM(M01) > 0 THEN ROUND(SUM(M01A)/SUM(M01),0) ELSE 0 END AS \"化粧品 \",CASE WHEN SUM(M02) > 0 THEN ROUND(SUM(M02A)/SUM(M02),0) ELSE 0 END AS \"食品\",CASE WHEN SUM(M03) > 0 THEN ROUND(SUM(M03A)/SUM(M03),0) ELSE 0 END AS \"ボディウェア\",CASE WHEN SUM(M04) > 0 THEN ROUND(SUM(M04A)/SUM(M04),0) ELSE 0 END AS \"その他\",CASE WHEN SUM(M05) > 0 THEN ROUND(SUM(M05A)/SUM(M05),0) ELSE 0 END AS \"不明\",CASE WHEN SUM(MALL) > 0 THEN ROUND(SUM(MALLA)/SUM(MALL),0) ELSE 0 END AS \"計\"";
			whereC3 +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G10' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G20' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G30' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G80' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G99' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",SUM("+selItem+") ";
			whereC3A +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G10' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G20' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G30' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G80' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SUBSTR(SHOHIN_GRP_CD,1,3) = 'G99' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",SUM("+selItem2+") ";
			whereC4 = ",SUBSTR(SHOHIN_GRP_CD,1,3)";
		}else if (StringUtils.equals(szType1, "6")){ // 商品詳細
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,M10";
			whereC1 +=",M11,M12,M13,M14,M15,M16,M17,M18,M19,M20";
			whereC1 +=",M21,M22,M23,M24,M25,M26,M27,M28,M29,M30";
			whereC1 +=",M31,M32,M33,M34,M35,M36,M37,M38,M39,M40,M41,M42,MALL";
			whereC1A +=",M01A,M02A,M03A,M04A,M05A,M06A,M07A,M08A,M09A,M10A";
			whereC1A +=",M11A,M12A,M13A,M14A,M15A,M16A,M17A,M18A,M19A,M20A";
			whereC1A +=",M21A,M22A,M23A,M24A,M25A,M26A,M27A,M28A,M29A,M30A";
			whereC1A +=",M31A,M32A,M33A,M34A,M35A,M36A,M37A,M38A,M39A,M40A,M41A,M42A,MALLA";
			whereC1s +=",SUM(M01),SUM(M02),SUM(M03),SUM(M04),SUM(M05),SUM(M06),SUM(M07),SUM(M08),SUM(M09),SUM(M10)";
			whereC1s +=",SUM(M11),SUM(M12),SUM(M13),SUM(M14),SUM(M15),SUM(M16),SUM(M17),SUM(M18),SUM(M19),SUM(M20)";
			whereC1s +=",SUM(M21),SUM(M22),SUM(M23),SUM(M24),SUM(M25),SUM(M26),SUM(M27),SUM(M28),SUM(M29),SUM(M30)";
			whereC1s +=",SUM(M31),SUM(M32),SUM(M33),SUM(M34),SUM(M35),SUM(M36),SUM(M37),SUM(M38),SUM(M39),SUM(M40),SUM(M41),SUM(M42),SUM(MALL)";
			whereC1sA +=",SUM(M01A),SUM(M02A),SUM(M03A),SUM(M04A),SUM(M05A),SUM(M06A),SUM(M07A),SUM(M08A),SUM(M09A),SUM(M10A)";
			whereC1sA +=",SUM(M11A),SUM(M12A),SUM(M13A),SUM(M14A),SUM(M15A),SUM(M16A),SUM(M17A),SUM(M18A),SUM(M19A),SUM(M20A)";
			whereC1sA +=",SUM(M21A),SUM(M22A),SUM(M23A),SUM(M24A),SUM(M25A),SUM(M26A),SUM(M27A),SUM(M28A),SUM(M29A),SUM(M30A)";
			whereC1sA +=",SUM(M31A),SUM(M32A),SUM(M33A),SUM(M34A),SUM(M35A),SUM(M36A),SUM(M37A),SUM(M38A),SUM(M39A),SUM(M40A),SUM(M41A),SUM(M42A),SUM(MALLA)";
			whereC2 +=",SUM(M01) "+itemS[0]+",SUM(M02) "+itemS[1]+",SUM(M03) "+itemS[2]+",SUM(M04) "+itemS[3]+",SUM(M05) "+itemS[4]+",SUM(M06) "+itemS[5]+",SUM(M07) "+itemS[6]+",SUM(M08) "+itemS[7]+",SUM(M09) "+itemS[8]+",SUM(M10) "+itemS[9];
			whereC2 +=",SUM(M11) "+itemS[10]+",SUM(M12) "+itemS[11]+",SUM(M13) "+itemS[12]+",SUM(M14) "+itemS[13]+",SUM(M15) "+itemS[14]+",SUM(M16) "+itemS[15]+",SUM(M17) "+itemS[16]+",SUM(M18) "+itemS[17]+",SUM(M19) "+itemS[18]+",SUM(M20) "+itemS[19];
			whereC2 +=",SUM(M21) "+itemS[20]+",SUM(M22) "+itemS[21]+",SUM(M23) "+itemS[22]+",SUM(M24) "+itemS[23]+",SUM(M25) "+itemS[24]+",SUM(M26) "+itemS[25]+",SUM(M27) "+itemS[26]+",SUM(M28) "+itemS[27]+",SUM(M29) "+itemS[28]+",SUM(M30) "+itemS[29];
			whereC2 +=",SUM(M31) "+itemS[30]+",SUM(M32) "+itemS[31]+",SUM(M33) "+itemS[32]+",SUM(M34) "+itemS[33]+",SUM(M35) "+itemS[34]+",SUM(M36) "+itemS[35]+",SUM(M37) "+itemS[36]+",SUM(M38) "+itemS[37]+",SUM(M39) "+itemS[38]+",SUM(M40) "+itemS[39];
			whereC2 +=",SUM(M41) "+itemS[40]+",SUM(M42) "+itemS[41]+",SUM(MALL) AS \"計\"";
			whereC2A +=",CASE WHEN SUM(M01) > 0 THEN ROUND(SUM(M01A)/SUM(M01),0) ELSE 0 END "+itemS[0]+",CASE WHEN SUM(M02) > 0 THEN ROUND(SUM(M02A)/SUM(M02),0) ELSE 0 END "+itemS[1]+",CASE WHEN SUM(M03) > 0 THEN ROUND(SUM(M03A)/SUM(M03),0) ELSE 0 END "+itemS[2]+",CASE WHEN SUM(M04) > 0 THEN ROUND(SUM(M04A)/SUM(M04),0) ELSE 0 END "+itemS[3]+",CASE WHEN SUM(M05) > 0 THEN ROUND(SUM(M05A)/SUM(M05),0) ELSE 0 END "+itemS[4]+",CASE WHEN SUM(M06) > 0 THEN ROUND(SUM(M06A)/SUM(M06),0) ELSE 0 END "+itemS[5]+",CASE WHEN SUM(M07) > 0 THEN ROUND(SUM(M07A)/SUM(M07),0) ELSE 0 END "+itemS[6]+",CASE WHEN SUM(M08) > 0 THEN ROUND(SUM(M08A)/SUM(M08),0) ELSE 0 END "+itemS[7]+",CASE WHEN SUM(M09) > 0 THEN ROUND(SUM(M09A)/SUM(M09),0) ELSE 0 END "+itemS[8]+",CASE WHEN SUM(M10) > 0 THEN ROUND(SUM(M10A)/SUM(M10),0) ELSE 0 END "+itemS[9];
			whereC2A +=",CASE WHEN SUM(M11) > 0 THEN ROUND(SUM(M11A)/SUM(M11),0) ELSE 0 END "+itemS[10]+",CASE WHEN SUM(M12) > 0 THEN ROUND(SUM(M12A)/SUM(M12),0) ELSE 0 END "+itemS[11]+",CASE WHEN SUM(M13) > 0 THEN ROUND(SUM(M13A)/SUM(M13),0) ELSE 0 END "+itemS[12]+",CASE WHEN SUM(M14) > 0 THEN ROUND(SUM(M14A)/SUM(M14),0) ELSE 0 END "+itemS[13]+",CASE WHEN SUM(M15) > 0 THEN ROUND(SUM(M15A)/SUM(M15),0) ELSE 0 END "+itemS[14]+",CASE WHEN SUM(M16) > 0 THEN ROUND(SUM(M16A)/SUM(M16),0) ELSE 0 END "+itemS[15]+",CASE WHEN SUM(M17) > 0 THEN ROUND(SUM(M17A)/SUM(M17),0) ELSE 0 END "+itemS[16]+",CASE WHEN SUM(M18) > 0 THEN ROUND(SUM(M18A)/SUM(M18),0) ELSE 0 END "+itemS[17]+",CASE WHEN SUM(M19) > 0 THEN ROUND(SUM(M19A)/SUM(M19),0) ELSE 0 END "+itemS[18]+",CASE WHEN SUM(M20) > 0 THEN ROUND(SUM(M20A)/SUM(M20),0) ELSE 0 END "+itemS[19];
			whereC2A +=",CASE WHEN SUM(M21) > 0 THEN ROUND(SUM(M21A)/SUM(M21),0) ELSE 0 END "+itemS[20]+",CASE WHEN SUM(M22) > 0 THEN ROUND(SUM(M22A)/SUM(M22),0) ELSE 0 END "+itemS[21]+",CASE WHEN SUM(M23) > 0 THEN ROUND(SUM(M23A)/SUM(M23),0) ELSE 0 END "+itemS[22]+",CASE WHEN SUM(M24) > 0 THEN ROUND(SUM(M24A)/SUM(M24),0) ELSE 0 END "+itemS[23]+",CASE WHEN SUM(M25) > 0 THEN ROUND(SUM(M25A)/SUM(M25),0) ELSE 0 END "+itemS[24]+",CASE WHEN SUM(M26) > 0 THEN ROUND(SUM(M26A)/SUM(M26),0) ELSE 0 END "+itemS[25]+",CASE WHEN SUM(M27) > 0 THEN ROUND(SUM(M27A)/SUM(M27),0) ELSE 0 END "+itemS[26]+",CASE WHEN SUM(M28) > 0 THEN ROUND(SUM(M28A)/SUM(M28),0) ELSE 0 END "+itemS[27]+",CASE WHEN SUM(M29) > 0 THEN ROUND(SUM(M29A)/SUM(M29),0) ELSE 0 END "+itemS[28]+",CASE WHEN SUM(M30) > 0 THEN ROUND(SUM(M20A)/SUM(M30),0) ELSE 0 END "+itemS[29];
			whereC2A +=",CASE WHEN SUM(M31) > 0 THEN ROUND(SUM(M31A)/SUM(M31),0) ELSE 0 END "+itemS[30]+",CASE WHEN SUM(M32) > 0 THEN ROUND(SUM(M32A)/SUM(M32),0) ELSE 0 END "+itemS[31]+",CASE WHEN SUM(M33) > 0 THEN ROUND(SUM(M33A)/SUM(M33),0) ELSE 0 END "+itemS[32]+",CASE WHEN SUM(M34) > 0 THEN ROUND(SUM(M34A)/SUM(M34),0) ELSE 0 END "+itemS[33]+",CASE WHEN SUM(M35) > 0 THEN ROUND(SUM(M35A)/SUM(M35),0) ELSE 0 END "+itemS[34]+",CASE WHEN SUM(M36) > 0 THEN ROUND(SUM(M36A)/SUM(M36),0) ELSE 0 END "+itemS[35]+",CASE WHEN SUM(M37) > 0 THEN ROUND(SUM(M37A)/SUM(M37),0) ELSE 0 END "+itemS[36]+",CASE WHEN SUM(M38) > 0 THEN ROUND(SUM(M38A)/SUM(M38),0) ELSE 0 END "+itemS[37]+",CASE WHEN SUM(M39) > 0 THEN ROUND(SUM(M39A)/SUM(M39),0) ELSE 0 END "+itemS[38]+",CASE WHEN SUM(M40) > 0 THEN ROUND(SUM(M30A)/SUM(M40),0) ELSE 0 END "+itemS[39];
			whereC2A +=",CASE WHEN SUM(M41) > 0 THEN ROUND(SUM(M41A)/SUM(M41),0) ELSE 0 END "+itemS[40]+",CASE WHEN SUM(M42) > 0 THEN ROUND(SUM(M42A)/SUM(M42),0) ELSE 0 END "+itemS[41]+",CASE WHEN SUM(MALL) > 0 THEN ROUND(SUM(MALLA)/SUM(MALL),0) ELSE 0 END AS \"計\"";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010010' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010020' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010030' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010040' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010050' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010060' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010070' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010080' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010090' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010100' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010101' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010102' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010105' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010110' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010120' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010205' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010210' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010220' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010230' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010240' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010290' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010300' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010310' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010320' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010330' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010340' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010350' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010360' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010390' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010400' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010500' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1010590' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1011100' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1011200' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1020100' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G1030000' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G2010010' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G2010020' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G2030010' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G3010010' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G8000010' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN SHOHIN_GRP_CD = 'G9900000' THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",SUM("+selItem+") ";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010010' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010020' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010030' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010040' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010050' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010060' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010070' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010080' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010090' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010100' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010101' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010102' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010105' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010110' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010120' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010205' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010210' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010220' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010230' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010240' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010290' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010300' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010310' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010320' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010330' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010340' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010350' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010360' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010390' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010400' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010500' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1010590' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1011100' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1011200' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1020100' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G1030000' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G2010010' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G2010020' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G2030010' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G3010010' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G8000010' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",CASE WHEN SHOHIN_GRP_CD = 'G9900000' THEN SUM("+selItem2+") ELSE 0 END";
			whereC3A +=",SUM("+selItem2+") ";
			whereC4 = ",SHOHIN_GRP_CD";
		}else if (StringUtils.equals(szType1, "7")){ // 媒体　年齢
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,M11,M12,MALL";
			whereC1A +=",M01A,M02A,M03A,M04A,M05A,M06A,M07A,M08A,M09A,M10A,M11A,M12A,MALLA";
			whereC1s +=",sum(M01),sum(M02),sum(M03),sum(M04),sum(M05),sum(M06),sum(M07),sum(M08),sum(M09),sum(M10),sum(M11),sum(M12),sum(MALL)";
			whereC1sA +=",sum(M01A),sum(M02A),sum(M03A),sum(M04A),sum(M05A),sum(M06A),sum(M07A),sum(M08A),sum(M09A),sum(M10A),sum(M11A),sum(M12A),sum(MALLA)";
			whereC2 +=",SUM(M01) "+itemN[0]+",SUM(M02) "+itemN[1]+",SUM(M03) "+itemN[2]+",SUM(M04) "+itemN[3]+",SUM(M05) "+itemN[4]+",SUM(M06) "+itemN[5]+",SUM(M07) "+itemN[6]+",SUM(M08) "+itemN[7]+",SUM(M09) "+itemN[8]+",SUM(M10) "+itemN[9]+",SUM(M11) "+itemN[10]+",SUM(M12) "+itemN[11]+",SUM(MALL) AS \"計\"";
			whereC2A +=",CASE WHEN SUM(M01) > 0 THEN ROUND(SUM(M01A)/SUM(M01),0) ELSE 0 END "+itemN[0]+",CASE WHEN SUM(M02) > 0 THEN ROUND(SUM(M02A)/SUM(M02),0) ELSE 0 END "+itemN[1]+",CASE WHEN SUM(M03) > 0 THEN ROUND(SUM(M03A)/SUM(M03),0) ELSE 0 END "+itemN[2]+",CASE WHEN SUM(M04) > 0 THEN ROUND(SUM(M04A)/SUM(M04),0) ELSE 0 END "+itemN[3]+",CASE WHEN SUM(M05) > 0 THEN ROUND(SUM(M05A)/SUM(M05),0) ELSE 0 END "+itemN[4]+",CASE WHEN SUM(M06) > 0 THEN ROUND(SUM(M06A)/SUM(M06),0) ELSE 0 END "+itemN[5]+",CASE WHEN SUM(M07) > 0 THEN ROUND(SUM(M07A)/SUM(M07),0) ELSE 0 END "+itemN[6]+",CASE WHEN SUM(M08) > 0 THEN ROUND(SUM(M08A)/SUM(M08),0) ELSE 0 END "+itemN[7]+",CASE WHEN SUM(M09) > 0 THEN ROUND(SUM(M09A)/SUM(M09),0) ELSE 0 END "+itemN[8]+",CASE WHEN SUM(M10) > 0 THEN ROUND(SUM(M10A)/SUM(M10),0) ELSE 0 END "+itemN[9];
			whereC2A +=",CASE WHEN SUM(M11) > 0 THEN ROUND(SUM(M11A)/SUM(M11),0) ELSE 0 END "+itemN[10]+",CASE WHEN SUM(M12) > 0 THEN ROUND(SUM(M12A)/SUM(M12),0) ELSE 0 END "+itemN[11]+",CASE WHEN SUM(MALL) > 0 THEN ROUND(SUM(MALLA)/SUM(MALL),0) ELSE 0 END AS \"計\"";
			whereC3 += ",case when AGE between 12 and 19 then sum("+selItem+") else 0 end,case when AGE between 20 and 24 then sum("+selItem+") else 0 end,";
			whereC3 += "case when AGE between 25 and 29 then sum("+selItem+") else 0 end,case when AGE between 30 and 34 then sum("+selItem+") else 0 end,";
			whereC3 += "case when AGE between 35 and 39 then sum("+selItem+") else 0 end,case when AGE between 40 and 44 then sum("+selItem+") else 0 end,";
			whereC3 += "case when AGE between 45 and 49 then sum("+selItem+") else 0 end,case when AGE between 50 and 54 then sum("+selItem+") else 0 end,";
			whereC3 += "case when AGE between 55 and 59 then sum("+selItem+") else 0 end,case when AGE between 60 and 69 then sum("+selItem+") else 0 end,";
			whereC3 += "case when AGE between 70 and 99 then sum("+selItem+") else 0 end,";
			whereC3 += "case when AGE < 12 OR AGE >= 100 then sum("+selItem+") else 0 end , sum("+selItem+") ";
			whereC3A += ",case when AGE between 12 and 19 then sum("+selItem2+") else 0 end,case when AGE between 20 and 24 then sum("+selItem2+") else 0 end,";
			whereC3A += "case when AGE between 25 and 29 then sum("+selItem2+") else 0 end,case when AGE between 30 and 34 then sum("+selItem2+") else 0 end,";
			whereC3A += "case when AGE between 35 and 39 then sum("+selItem2+") else 0 end,case when AGE between 40 and 44 then sum("+selItem2+") else 0 end,";
			whereC3A += "case when AGE between 45 and 49 then sum("+selItem2+") else 0 end,case when AGE between 50 and 54 then sum("+selItem2+") else 0 end,";
			whereC3A += "case when AGE between 55 and 59 then sum("+selItem2+") else 0 end,case when AGE between 60 and 69 then sum("+selItem2+") else 0 end,";
			whereC3A += "case when AGE between 70 and 99 then sum("+selItem2+") else 0 end,";
			whereC3A += "case when AGE < 12 OR AGE >= 100 then sum("+selItem2+") else 0 end , sum("+selItem2+") ";
			whereC4 = ",AGE ";
		}else if (StringUtils.equals(szType1, "8")){ // 媒体/初回受注形態・
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,M11,M12,MALL";
			whereC1A +=",M01A,M02A,M03A,M04A,M05A,M06A,M07A,M08A,M09A,M10A,M11A,M12A,MALLA";
			whereC1s +=",sum(M01),sum(M02),sum(M03),sum(M04),sum(M05),sum(M06),sum(M07),sum(M08),sum(M09),sum(M10),sum(M11),sum(M12),sum(MALL)";
			whereC1sA +=",sum(M01A),sum(M02A),sum(M03A),sum(M04A),sum(M05A),sum(M06A),sum(M07A),sum(M08A),sum(M09A),sum(M10A),sum(M11A),sum(M12A),sum(MALLA)";
			whereC2 +=",SUM(M01) "+itemJ[0]+",SUM(M02) "+itemJ[1]+",SUM(M03) "+itemJ[2]+",SUM(M04) "+itemJ[3]+",SUM(M05) "+itemJ[4]+",SUM(M06) "+itemJ[5]+",SUM(M07) "+itemJ[6]+",SUM(M08) "+itemJ[7]+",SUM(M09) "+itemJ[8]+",SUM(M10) "+itemJ[9]+",SUM(M11) "+itemJ[10]+",SUM(M12) "+itemN[11]+",SUM(MALL) AS \"計\"";
			whereC2A +=",CASE WHEN SUM(M01) > 0 THEN ROUND(SUM(M01A)/SUM(M01),0) ELSE 0 END "+itemJ[0]+",CASE WHEN SUM(M02) > 0 THEN ROUND(SUM(M02A)/SUM(M02),0) ELSE 0 END "+itemJ[1]+",CASE WHEN SUM(M03) > 0 THEN ROUND(SUM(M03A)/SUM(M03),0) ELSE 0 END "+itemJ[2]+",CASE WHEN SUM(M04) > 0 THEN ROUND(SUM(M04A)/SUM(M04),0) ELSE 0 END "+itemJ[3]+",CASE WHEN SUM(M05) > 0 THEN ROUND(SUM(M05A)/SUM(M05),0) ELSE 0 END "+itemJ[4]+",CASE WHEN SUM(M06) > 0 THEN ROUND(SUM(M06A)/SUM(M06),0) ELSE 0 END "+itemJ[5]+",CASE WHEN SUM(M07) > 0 THEN ROUND(SUM(M07A)/SUM(M07),0) ELSE 0 END "+itemJ[6]+",CASE WHEN SUM(M08) > 0 THEN ROUND(SUM(M08A)/SUM(M08),0) ELSE 0 END "+itemJ[7]+",CASE WHEN SUM(M09) > 0 THEN ROUND(SUM(M09A)/SUM(M09),0) ELSE 0 END "+itemJ[8]+",CASE WHEN SUM(M10) > 0 THEN ROUND(SUM(M10A)/SUM(M10),0) ELSE 0 END "+itemJ[9];
			whereC2A +=",CASE WHEN SUM(M11) > 0 THEN ROUND(SUM(M11A)/SUM(M11),0) ELSE 0 END "+itemJ[10]+",CASE WHEN SUM(M12) > 0 THEN ROUND(SUM(M12A)/SUM(M12),0) ELSE 0 END "+itemJ[11]+",CASE WHEN SUM(MALL) > 0 THEN ROUND(SUM(MALLA)/SUM(MALL),0) ELSE 0 END AS \"計\"";
			whereC3 +=",case when JUCHU_KBN = '01' or JUCHU_KBN = '02' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '03' or JUCHU_KBN = '04' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '04' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '07' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '09' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '12' or JUCHU_KBN = '13'  then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '30' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '31' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '32' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '14' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '06' or JUCHU_KBN = '08' or JUCHU_KBN = '10' or JUCHU_KBN = '11' then sum("+selItem+") else 0 end";
			whereC3 +=",case when JUCHU_KBN = '99' then sum("+selItem+") else 0 end,sum("+selItem+") ";
			whereC3A +=",case when JUCHU_KBN = '01' or JUCHU_KBN = '02' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '03' or JUCHU_KBN = '04' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '04' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '07' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '09' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '12' or JUCHU_KBN = '13'  then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '30' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '31' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '32' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '14' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '06' or JUCHU_KBN = '08' or JUCHU_KBN = '10' or JUCHU_KBN = '11' then sum("+selItem2+") else 0 end";
			whereC3A +=",case when JUCHU_KBN = '99' then sum("+selItem2+") else 0 end,sum("+selItem2+") ";
			whereC4 = ",JUCHU_KBN";
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		// 計算式が必要ない場合1:3:5
		if((StringUtils.equals(szType2, "1"))||(StringUtils.equals(szType2, "3"))||(StringUtils.equals(szType2, "5"))){   //3:初回受注金額
			sbSQL.append(" WITH WORKT(TPROMO_CD"+whereC1+") AS ( ");
			sbSQL.append("  select "+itemT+ whereC3);
			sbSQL.append(" FROM "+sqlTable+" WHERE "+sqlWhere+" GROUP BY "+itemT+whereC4+" )");
			sbSQL.append(" SELECT "+whereB1+" as SORT,"+whereB1M+ItemC1[0]);
			sbSQL.append( whereC2);
			sbSQL.append(" FROM "+sqlTable2+" INNER JOIN WORKT ON TPROMO_CD = " +itemT2);
			sbSQL.append( whereB1A);
			sbSQL.append(" GROUP BY "+whereB1G);
			sbSQL.append(" UNION ALL SELECT 'zzzzz' as SORT,'合計' "+ItemC1[0]);
			sbSQL.append( whereC2);
			sbSQL.append(" FROM "+whereTable+whereB1A);
		}else if(StringUtils.equals(szType2, "70"))  {   //7 平均年齢
			sbSQL.append(" WITH WORKT(TPROMO_CD"+whereC1+") AS ( ");
			sbSQL.append("  select "+itemT+ whereC3);
			sbSQL.append(" FROM "+sqlTable+" WHERE "+sqlWhere+sqlWhereAge+" GROUP BY "+itemT+whereC4+" )");
			sbSQL.append(" ,WORKA(APROMO_CD"+whereC1A+" ) AS (");
			sbSQL.append(" select "+itemT+ whereC3A);
			sbSQL.append(" FROM "+sqlTable+" WHERE "+sqlWhere+sqlWhereAge+" GROUP BY "+itemT+whereC4+" )");
			sbSQL.append(" SELECT "+whereB1+" as SORT,"+whereB1M+ItemC1[0]);
			sbSQL.append( whereC2A);
			sbSQL.append(" FROM "+sqlTable2+" INNER JOIN WORKT ON TPROMO_CD = " +itemT2);
			sbSQL.append("  INNER JOIN WORKA ON APROMO_CD = " +itemT2);
			sbSQL.append( whereB1A);
			sbSQL.append(" GROUP BY "+whereB1G);
			sbSQL.append(" UNION ALL SELECT 'zzzzz' as SORT,'合計' "+ItemC1[0]);
			sbSQL.append( whereC2A);
			sbSQL.append(" FROM "+sqlTable2+" INNER JOIN WORKT ON TPROMO_CD = " +itemT2);
			sbSQL.append("  INNER JOIN WORKA ON APROMO_CD = " +itemT2);
			sbSQL.append( whereB1A);
		}else if(StringUtils.equals(szType2, "7"))  {   //7 平均年齢
			sbSQL.append(" WITH BYTAIM(BYTAI_CD,BYTAI_MEI) AS ( SELECT "+whereB1+","+whereB1M+" FROM M_PROMO_ALL T GROUP BY "+whereB1+" )");
			if (StringUtils.equals(szType1, "5")){ // SHO
				sbSQL.append(" ,WORK(PROMO_CD,SHOHIN_GRP_CD,KYAKUSU,AGE2) AS ( ");
			}else  {
				sbSQL.append(" ,WORK(PROMO_CD"+whereC4+",KYAKUSU,AGE2) AS ( ");
			}
			sbSQL.append("  select "+whereB1+""+whereC4+",sum(KYAKUSU),AGE ");
			sbSQL.append(" FROM "+sqlTable+" T inner join M_PROMO_ALL M ON T.PROMO_CD =  M.PROMO_CD ");
			sbSQL.append("  WHERE "+sqlWhere+sqlWhereAge+whereB1A2+" GROUP BY "+whereB1+whereC4+" ,AGE )");

			sbSQL.append(" ,WORKT1(TPROMO_CD"+whereC1+") AS ( ");
			sbSQL.append("  select "+itemT+ whereC3+"FROM WORK GROUP BY "+itemT+whereC4+") ");
			sbSQL.append(" ,WORKT(TPROMO_CD"+whereC1+") AS ( SELECT TPROMO_CD "+whereC1s+" FROM WORKT1 GROUP BY TPROMO_CD )");
			sbSQL.append(" ,WORKA1(APROMO_CD"+whereC1A+" ) AS (");
			sbSQL.append(" select "+itemT+ whereC3A+"FROM WORK GROUP BY "+itemT+whereC4+") ");
			sbSQL.append(" ,WORKA(APROMO_CD"+whereC1A+") AS ( SELECT APROMO_CD "+whereC1sA+" FROM WORKA1 GROUP BY APROMO_CD )");
			sbSQL.append(" SELECT BYTAI_CD as SORT,MAX(BYTAI_MEI) "+ItemC1[0]);
			sbSQL.append( whereC2A);
			sbSQL.append(" FROM  WORKT INNER JOIN WORKA  ON TPROMO_CD = APROMO_CD ");
			sbSQL.append("      INNER JOIN BYTAIM ON TPROMO_CD = BYTAI_CD ");
			sbSQL.append(" GROUP BY BYTAI_CD ");
			sbSQL.append(" UNION ALL SELECT 'zzzzz' as SORT,'合計' "+ItemC1[0]);
			sbSQL.append( whereC2A);
			sbSQL.append(" FROM  WORKT INNER JOIN WORKA  ON TPROMO_CD = APROMO_CD ");
		}else  {   //8 平均単価
			sbSQL.append(" WITH BYTAIM(BYTAI_CD,BYTAI_MEI) AS ( SELECT "+whereB1+","+whereB1M+" FROM M_PROMO_ALL T GROUP BY "+whereB1+" )");
			if (StringUtils.equals(szType1, "5")){ // SHO
				sbSQL.append(" ,WORK(PROMO_CD,SHOHIN_GRP_CD,KYAKUSU,F_KINGAKU) AS ( ");
			}else  {
				sbSQL.append(" ,WORK(PROMO_CD"+whereC4+",KYAKUSU,F_KINGAKU) AS ( ");
			}
			sbSQL.append("  select "+whereB1+""+whereC4+",sum(KYAKUSU),sum(F_KINGAKU) ");
			sbSQL.append(" FROM "+sqlTable+" T inner join M_PROMO_ALL M ON T.PROMO_CD =  M.PROMO_CD ");
			sbSQL.append("  WHERE "+sqlWhere+sqlWhereAge+whereB1A2+" GROUP BY "+whereB1+whereC4+" )");

			sbSQL.append(" ,WORKT1(TPROMO_CD"+whereC1+") AS ( ");
			sbSQL.append("  select "+itemT+ whereC3+"FROM WORK GROUP BY "+itemT+whereC4+") ");
			sbSQL.append(" ,WORKT(TPROMO_CD"+whereC1+") AS ( SELECT TPROMO_CD "+whereC1s+" FROM WORKT1 GROUP BY TPROMO_CD )");
			sbSQL.append(" ,WORKA1(APROMO_CD"+whereC1A+" ) AS (");
			sbSQL.append(" select "+itemT+ whereC3A+"FROM WORK GROUP BY "+itemT+whereC4+") ");
			sbSQL.append(" ,WORKA(APROMO_CD"+whereC1A+") AS ( SELECT APROMO_CD "+whereC1sA+" FROM WORKA1 GROUP BY APROMO_CD )");
			sbSQL.append(" SELECT BYTAI_CD as SORT,MAX(BYTAI_MEI) "+ItemC1[0]);
			sbSQL.append( whereC2A);
			sbSQL.append(" FROM  WORKT INNER JOIN WORKA  ON TPROMO_CD = APROMO_CD ");
			sbSQL.append("      INNER JOIN BYTAIM ON TPROMO_CD = BYTAI_CD ");
			sbSQL.append(" GROUP BY BYTAI_CD ");
			sbSQL.append(" UNION ALL SELECT 'zzzzz' as SORT,'合計' "+ItemC1[0]);
			sbSQL.append( whereC2A);
			sbSQL.append(" FROM  WORKT INNER JOIN WORKA  ON TPROMO_CD = APROMO_CD ");
		}
		sbSQL.append(" order by SORT ");


		System.out.println(sbSQL.toString());
		command = sbSQL.toString();
		return command;
	}

	/**
	 * 検索条件表示用情報の生成
	 */
	private void outputQueryList(){

		// 検索条件の加工クラス作成
		JsonArrayData jad = new JsonArrayData();
		jad.setJsonString(getJson());

		// 保存用 List (検索情報)作成
		setWhere(new ArrayList<List<String>>());

		// タイトル名称
		List<String> cells = new ArrayList<String>();
		cells.add( jad.getJSONText(DefineReport.ID_HIDDEN_REPORT_NAME) );
		getWhere().add(cells);

		// レポート更新日時
		cells = new ArrayList<String>();
		cells.add( "" );
		cells.add( "" );
		cells.add( "" );
		cells.add( "" );
		cells.add( jad.getJSONText(DefineReport.Label.CHECK.getObj()));
		getWhere().add(cells);

		// 基準年月
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.YM_KIJUN.getView());
		cells.add( jad.getJSONText(DefineReport.Select.YM_KIJUN.getObj()) );
		getWhere().add(cells);
		// 表示条件
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.S_TYPE2.getView());
		cells.add( jad.getJSONText(DefineReport.Select.S_TYPE2.getObj()) );
		cells.add( jad.getJSONText(DefineReport.Select.H_TYPE3.getObj()) );

		cells.add( DefineReport.Select.F_JUCHU.getView());
		cells.add( jad.getJSONText(DefineReport.Select.F_JUCHU.getObj()) );
		cells.add( jad.getJSONText(DefineReport.Select.F_JUCHU_TO.getObj()) );
		cells.add( DefineReport.Select.BYTAI.getView());
		cells.add( jad.getJSONText(DefineReport.Select.BYTAI.getObj()) );
		cells.add( jad.getJSONText(DefineReport.Select.BYTAI_2.getObj()) );
		getWhere().add(cells);


		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);

	}

}
