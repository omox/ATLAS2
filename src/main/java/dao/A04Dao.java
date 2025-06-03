package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * A04初回商品画面で使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class A04Dao extends ItemDao {

	/**
	 * 新しいA04Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public A04Dao(Connection source) {
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

		String szYm 		=	getMap().get("YYYY_MM");		// （必須）基準年月
		String szType1 		=	getMap().get("TYPE1");			// 1: 商品×受注年月
		String szType2 		=	getMap().get("TYPE2");			// 1:顧客数 3:初回受注金額 5:累計受注金額
		String szType3 		=	getMap().get("TYPE3");			// 4:基本　 3:全展開
		String szJucYM1 	=	getMap().get("JUCYM1");			//
		String szJucYM2 	=	getMap().get("JUCYM2");			//
		String szSHOG 	=	getMap().get("SHOG");			//商品選択

		// パラメータ確認
		System.out.println("YYYY_MM:" +szYm+",TYPE1:"+szType1+",TYPE2:"+szType2+",TYPE3:"+szType3+",JUCYM1:"+szJucYM1+",JUCYM2:"+szJucYM2+",SHOG:"+szSHOG);

		// 年月設定
		String sqlTable= " ";
		String kijunYm = DefineReport.base.TABLE_TP.getVal();	// 基準年月;
		if(szYm.compareTo(kijunYm) > 0){   //年月が基準より大きい時
			sqlTable  = " TSU_KYAKU";
		} else  {                           //年月が基準より小さい時
			sqlTable  = " TSU_KYAKU_"+szYm.substring(0, 4);
		}
		String sqlWhere = " KIJUN_YM ='"+szYm+"' ";
		String sqlUnion = " ";
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
		String selItem  = "KYAKUSU";			//1:顧客数
		if(StringUtils.equals(szType2, "3")){   //3:初回受注金額
			selItem  = "F_KINGAKU";
		} else if (StringUtils.equals(szType2, "5")) {  //5:累計受注金額
			selItem  = "G_KINGAKU";
		}
		// 設定
		String[] itemM = { "AS \"0-0.5万未満\"", "AS \"0.5-1万未満\"", "AS \"1-1.5万未満\"", "AS \"1.5-2万未満\"", "AS \"2-3万未満\"", "AS \"3-4万未満\"", "AS \"4-5万未満\"", "AS \"5-6万未満\"", "AS \"6-10万未満\"", "AS \"10万以上\"" };
		String whereB1  = " BUN4_SHOHIN_GRP_CD||SHOHIN_GRP_CD  as SORT, '　　　'||RTRIM(MAX(SHOHIN_GRP_MEI))  AS \"初回商品\" ";
		String whereB1G = " WHERE BUN4_SHOHIN_GRP_CD = '10201010' GROUP BY  BUN4_SHOHIN_GRP_CD,SHOHIN_GRP_CD ";
		String whereB2  = " CASE WHEN BUN4_SHOHIN_GRP_CD = '10201010' THEN BUN4_SHOHIN_GRP_CD||'ZZ' ELSE BUN4_SHOHIN_GRP_CD END  as SORT, '　　'||RTRIM(MAX(BUN4_SHOHIN_GRP_MEI)) AS \"初回商品\" ";
		String whereB2G = " WHERE BUN1_SHOHIN_GRP_CD < '30' GROUP BY  BUN4_SHOHIN_GRP_CD ";
		String whereB3  = " BUN1_SHOHIN_GRP_CD||'z' as SORT, '　'||RTRIM(MAX(BUN1_SHOHIN_GRP_MEI)) AS \"初回商品\" ";
		String whereB3G = " GROUP BY  BUN1_SHOHIN_GRP_CD  ";
		String whereB4  = " 'zzzz' as SORT, '合計'  AS \"初回商品\" ";
		String whereB4G = "   ";
		// 2015/12/21 TEST
		String whereB5  = " BUN4_SHOHIN_GRP_CD||MAX(SHOHIN_GRP_CD) as SORT, '　　　ベースメイクのみ' AS \"初回商品\" ";
		String whereB5G = " WHERE SHOHIN_GRP_CD IN ('G1010350','G1010360','G1010370')  GROUP BY BUN4_SHOHIN_GRP_CD ";

		String whereB1_S  = " SHOHIN_GRP_CD  as SORT, '　'||RTRIM(MAX(SHOHIN_GRP_MEI))  AS \"初回商品\" ";
		String whereB1G_S = " WHERE SHOHIN_GRP_CD IN (SELECT SHOHIN_GRP_CD FROM M_SHOHIN_GRP WHERE BUN4_SHOHIN_GRP_CD = '"+szSHOG +"' ) GROUP BY  SHOHIN_GRP_CD ";
		String whereB4_S  = " 'zzzz' as SORT, RTRIM(MAX(BUN4_SHOHIN_GRP_MEI))||'　計'  AS \"初回商品\" ";
		String whereB4G_S = "  WHERE SHOHIN_GRP_CD IN (SELECT SHOHIN_GRP_CD FROM M_SHOHIN_GRP WHERE BUN4_SHOHIN_GRP_CD = '"+szSHOG +"' ) ";

		// 初回商品のコードがない分の合計用
		//String whereB2  = " '100000000' ";
		//String whereB2M = " '化粧品全体' AS \"初回商品\"";
		//String whereB2G = " WHERE BUN1_SHOHIN_GRP_CD = '10' ";
		//String whereB3  = " '200000000' ";
		//String whereB3M = " '食品全体' AS \"初回商品\"";
		//String whereB3G = " WHERE BUN1_SHOHIN_GRP_CD = '20' ";
		//String whereB4  = " '101010100' ";
		//String whereB4M = " '　　　   スキンケア単品' AS \"初回商品\"";
		//String whereB4G = " WHERE BUN4_SHOHIN_GRP_CD = '10101010' ";
		//String whereB5  = " '101060000' ";
		//String whereB5M = " '　　  メイクのみ' AS \"初回商品\"";
		//String whereB5G = " WHERE BUN4_SHOHIN_GRP_CD = '10104010' ";
		//String whereB6  = " '102000000' ";
		//String whereB6M = " '　 化粧品＋食品' AS \"初回商品\"";
		//String whereB6G = " WHERE BUN4_SHOHIN_GRP_CD = '10201010' ";
		//String whereB7M = " '　合計' AS \"初回商品\"";

		String whereC1 = "";
		String whereC2 = "";
		String whereC3 = "";
		String whereC4 = "";
		if(StringUtils.equals(szType1, "1")){  //初回商品 x 初回受注月
			sqlWhere += " AND substr(NF_JUCHU_YM,1,4) >= '"+szJucYM1+"' AND substr(NF_JUCHU_YM,1,4) <= '"+szJucYM2+"' ";
			int  cnt_yy = Integer.valueOf(szJucYM2).intValue()-Integer.valueOf(szJucYM1).intValue()+1;
			for (int i=0; i<cnt_yy; i++){
				int yy_int = Integer.valueOf(szJucYM1).intValue()+i;
				whereC1 +=",M"+yy_int+"01,M"+yy_int+"02,M"+yy_int+"03,M"+yy_int+"04,M"+yy_int+"05,M"+yy_int+"06,M"+yy_int+"07,M"+yy_int+"08,M"+yy_int+"09,M"+yy_int+"10,M"+yy_int+"11,M"+yy_int+"12";
				whereC2 +=",COALESCE(SUM(M"+yy_int+"01),0) AS \""+yy_int+"01\",COALESCE(SUM(M"+yy_int+"02),0) AS \""+yy_int+"02\",COALESCE(SUM(M"+yy_int+"03),0) AS \""+yy_int+"03\",COALESCE(SUM(M"+yy_int+"04),0) AS \""+yy_int+"04\",COALESCE(SUM(M"+yy_int+"05),0) AS \""+yy_int+"05\",COALESCE(SUM(M"+yy_int+"06),0) AS \""+yy_int+"06\",COALESCE(SUM(M"+yy_int+"07),0) AS \""+yy_int+"07\",COALESCE(SUM(M"+yy_int+"08),0) AS \""+yy_int+"08\",COALESCE(SUM(M"+yy_int+"09),0) AS \""+yy_int+"09\",COALESCE(SUM(M"+yy_int+"10),0) AS \""+yy_int+"10\",COALESCE(SUM(M"+yy_int+"11),0) AS \""+yy_int+"11\",COALESCE(SUM(M"+yy_int+"12),0) AS \""+yy_int+"12\"";
				//whereC2 +=",SUM(M"+yy_int+"01) AS \""+yy_int+"01\",SUM(M"+yy_int+"02) AS \""+yy_int+"02\",SUM(M"+yy_int+"03) AS \""+yy_int+"03\",SUM(M"+yy_int+"04) AS \""+yy_int+"04\",SUM(M"+yy_int+"05) AS \""+yy_int+"05\",SUM(M"+yy_int+"06) AS \""+yy_int+"06\",SUM(M"+yy_int+"07) AS \""+yy_int+"07\",SUM(M"+yy_int+"08) AS \""+yy_int+"08\",SUM(M"+yy_int+"09) AS \""+yy_int+"09\",SUM(M"+yy_int+"10) AS \""+yy_int+"10\",SUM(M"+yy_int+"11) AS \""+yy_int+"11\",SUM(M"+yy_int+"12) AS \""+yy_int+"12\"";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"01' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"02' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"03' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"04' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"05' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"06' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"07' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"08' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"09' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"10' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"11' THEN SUM("+selItem+") ELSE 0 END";
				whereC3 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"12' THEN SUM("+selItem+") ELSE 0 END";
			}
			whereC1 += ",MALL ";
			whereC2 += ",COALESCE(SUM(MALL),0) AS 計 ";
			whereC3 +=",SUM("+selItem+") ";
			whereC4 = ",NF_JUCHU_YM";
		}else if (StringUtils.equals(szType1, "2")){ // 初回商品 x R
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,MALL";
			whereC2 +=",COALESCE(SUM(M01),0) AS \"直近３ヶ月\",COALESCE(SUM(M02),0) AS \"４ー６ヶ月\",COALESCE(SUM(M03),0) AS \"７ー９ヶ月\",COALESCE(SUM(M04),0) AS \"１０ー１２ヶ月\",COALESCE(SUM(M05),0) AS \"１年超ー２年\",COALESCE(SUM(M06),0) AS \"２年超ー３年\",COALESCE(SUM(M07),0) AS \"３年超ー４年\",COALESCE(SUM(M08),0) AS \"４年超ー５年\",COALESCE(SUM(M09),0) AS \"５年以上\",COALESCE(SUM(MALL),0) AS \"計\" ";
			whereC3 +=",CASE WHEN JUCHUMR <= 3 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 3  AND JUCHUMR <= 6  THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 6  AND JUCHUMR <= 9  THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 9  AND JUCHUMR <= 12 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 12 AND JUCHUMR <= 24 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 24 AND JUCHUMR <= 36 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 36 AND JUCHUMR <= 48 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 48 AND JUCHUMR <= 60 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCHUMR > 60 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",SUM("+selItem+") ";
			whereC4 = ",JUCHUMR";
		}else if (StringUtils.equals(szType1, "3")){ // F
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,MALL";
			whereC2 +=",COALESCE(SUM(M01),0) AS \"1回\",COALESCE(SUM(M02),0) AS \"2回\",COALESCE(SUM(M03),0) AS \"3回\",COALESCE(SUM(M04),0) AS \"4回\",COALESCE(SUM(M05),0) AS \"5回\",COALESCE(SUM(M06),0) AS \"6回\",COALESCE(SUM(M07),0) AS \"7回\",COALESCE(SUM(M08),0) AS \"8回\",COALESCE(SUM(M09),0) AS \"9回\",COALESCE(SUM(M10),0) AS \"10回以上\",COALESCE(SUM(MALL),0) AS \"回数合計\"";
			whereC3 +=",CASE WHEN JUCKAISU = 1 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 2 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 3 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 4 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 5 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 6 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 7 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 8 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 9 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN JUCKAISU = 10 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",SUM("+selItem+") ";
			whereC4 = ",JUCKAISU";
		}else if (StringUtils.equals(szType1, "4")){ // M
			whereC1 +=",M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,MALL";
			whereC2 +=",COALESCE(SUM(M01),0) "+itemM[0]+",COALESCE(SUM(M02),0) "+itemM[1]+",COALESCE(SUM(M03),0) "+itemM[2]+",COALESCE(SUM(M04),0) "+itemM[3]+",COALESCE(SUM(M05),0) "+itemM[4]+",COALESCE(SUM(M06),0) "+itemM[5]+",COALESCE(SUM(M07),0) "+itemM[6]+",COALESCE(SUM(M08),0) "+itemM[7]+",COALESCE(SUM(M09),0) "+itemM[8]+",COALESCE(SUM(M10),0) "+itemM[9]+",COALESCE(SUM(MALL),0) AS \"計\"";
			whereC3 +=",CASE WHEN LKONYU = 0 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 1 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 2 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 3 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 5 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 7 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 9 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 11 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 19 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",CASE WHEN LKONYU = 20 THEN SUM("+selItem+") ELSE 0 END";
			whereC3 +=",SUM("+selItem+") ";
			whereC4 = ",LKONYU";
		}
		//System.out.println("cnt_yy:" +cnt_yy);
		if(StringUtils.equals(szSHOG, "00")){   //商品選択なし

			sqlUnion = " SELECT "+whereB1+whereC2 ;
			sqlUnion += "  FROM WORKT INNER JOIN M_SHOHIN_GRP ON GRP_CD = SHOHIN_GRP_CD "+whereB1G ;

			// 2015/12/21 TEST ベースメイクのみ
			sqlUnion += " UNION ALL SELECT "+whereB5+whereC2 ;
			sqlUnion += "  FROM WORKT INNER JOIN M_SHOHIN_GRP ON GRP_CD = SHOHIN_GRP_CD "+whereB5G ;

			sqlUnion += " UNION ALL SELECT "+whereB2+whereC2 ;
			sqlUnion += "  FROM WORKT INNER JOIN M_SHOHIN_GRP ON GRP_CD = SHOHIN_GRP_CD "+whereB2G ;
			sqlUnion += " UNION ALL SELECT "+whereB3+whereC2 ;
			sqlUnion += "  FROM WORKT INNER JOIN M_SHOHIN_GRP ON GRP_CD = SHOHIN_GRP_CD "+whereB3G ;
			sqlUnion += " UNION ALL SELECT "+whereB4+whereC2 ;
			sqlUnion += "  FROM WORKT INNER JOIN M_SHOHIN_GRP ON GRP_CD = SHOHIN_GRP_CD "+whereB4G ;
		}else{
			sqlUnion = " SELECT "+whereB1_S+whereC2 ;
			sqlUnion += "  FROM M_SHOHIN_GRP LEFT OUTER JOIN WORKT ON GRP_CD = SHOHIN_GRP_CD "+whereB1G_S ;
			sqlUnion += " UNION ALL SELECT "+whereB4_S+whereC2 ;
			sqlUnion += "  FROM M_SHOHIN_GRP LEFT OUTER JOIN WORKT ON GRP_CD = SHOHIN_GRP_CD "+whereB4G_S ;
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append(" WITH WORKT(GRP_CD"+whereC1+") AS ( ");
		sbSQL.append(" SELECT SHOHIN_GRP_CD "+whereC3);
		sbSQL.append("  FROM "+sqlTable+" WHERE "+sqlWhere+" GROUP BY SHOHIN_GRP_CD"+whereC4+" )");
		sbSQL.append( sqlUnion );

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

		cells.add( DefineReport.Select.F_JUCHU.getView());
		cells.add( jad.getJSONText(DefineReport.Select.F_JUCHU.getObj()) );
		cells.add( jad.getJSONText(DefineReport.Select.F_JUCHU_TO.getObj()) );
		getWhere().add(cells);

		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);

	}

}
