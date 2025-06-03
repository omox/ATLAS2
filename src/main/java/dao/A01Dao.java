package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * A01テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class A01Dao extends ItemDao {

	/**
	 * 新しいA01Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public A01Dao(Connection source) {
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
		String szType2 		=	getMap().get("TYPE2");			//  //2:2ヶ月展開3:展開
		String szType3 		=	getMap().get("TYPE3");			// 1:顧客数 3:初回受注金額 5:累計受注金額
		String szJucYM1 	=	getMap().get("JUCYM1");			//
		String szJucYM2 	=	getMap().get("JUCYM2");			//
		String szKaisu 		=	getMap().get("KAISU");			//
		String szJuckbn 	=	getMap().get("JUCKBN");			//
		String szAge 		=	getMap().get("AGE");			//
		String szArea 		=	getMap().get("AREA");			//
		String szBytai 		=	getMap().get("BYTAI");			//
		String szShoG 		=	getMap().get("SHOG");			//

		// パラメータ確認
		System.out.println("YYYY_MM:" +szYm+",TYPE1:"+szType1+",TYPE2:"+szType2+",TYPE3:"+szType3+",JUCYM1:"+szJucYM1+",JUCYM2:"+szJucYM2+",KAISU:"+szKaisu+",JUCKBN:"+szJuckbn+",AGE:"+szAge+",AREA:"+szArea+",BYTAI:"+szBytai+",SHOG:"+szShoG);
		System.out.println("SHOG_len:" +szShoG+",:"+szShoG.length());
		// パラメータ確認
		if ((szYm == null) || (szJucYM1 == null) || (szJucYM2== null)  || (szKaisu == null)  || (szJuckbn == null) ) {
			return "";
		}

		// 年月設定
		String sqlTable= " ";
		String kijunYm = DefineReport.base.TABLE_TP.getVal();	// 基準年月
		if(szYm.compareTo(kijunYm) > 0){   //年月が基準より大きい時
			sqlTable  = " TSU_KYAKU";
		} else  {                           //年月が基準より小さい時
			sqlTable  = " TSU_KYAKU_"+szYm.substring(0, 4);
		}
		String sqlWhere = " KIJUN_YM ='"+szYm+"' ";
		String selItem  = "KYAKUSU";			//1:顧客数
		if(StringUtils.equals(szType3, "3")){   //3:初回受注金額
			selItem  = "F_KINGAKU";
		} else if (StringUtils.equals(szType3, "5")) {  //5:累計受注金額
			selItem  = "G_KINGAKU";
		}
		//横軸初回受注月以外で初回受注月指定の場合
		if((StringUtils.equals(szType1, "1"))||(StringUtils.equals(szType1, "2"))||(StringUtils.equals(szType1, "3"))||(StringUtils.equals(szType1, "11"))||(StringUtils.equals(szType1, "12"))||(StringUtils.equals(szType1, "13"))){
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
		// 回数
		if(!StringUtils.equals(szKaisu, "0")){
			if(StringUtils.equals(szKaisu, "10")){
				sqlWhere += " AND JUCKAISU >= "+szKaisu+" ";
			} else {
				sqlWhere += " AND JUCKAISU = "+szKaisu+" ";
			}
		}
		// 受注形態
		if(!StringUtils.equals(szJuckbn, "00")){
			sqlWhere += " AND JUCHU_KBN IN (SELECT JUC_KBN FROM M_JUC_KBN WHERE JUC_D_KBN = '"+szJuckbn+"' ) ";
		}
		// 都道府県
		if(!StringUtils.equals(szArea, "00")){
			sqlWhere += " AND PREF_CD = '"+szArea+"' ";
		}
		// 媒体
		if(!StringUtils.equals(szBytai, "00")){
			sqlWhere += " AND PROMO_CD IN (SELECT PROMO_CD FROM M_PROMO_ALL WHERE BYTAI1_CD = '"+szBytai+"' ) ";
		}
		// 初回商品
		if(!StringUtils.equals(szShoG, "00")){
			if(szShoG.length() == 3){
				sqlWhere += " AND SHOHIN_GRP_CD IN (SELECT SHOHIN_GRP_CD FROM M_SHOHIN_GRP_CMB WHERE BUN1_SHOHIN_GRP_CD = '"+szShoG.substring(1,3)+"' ) ";
			} else if (szShoG.length() == 6){
				if(StringUtils.equals(szShoG, "G10103")){          //メイクのみ固定
					sqlWhere += " AND SHOHIN_GRP_CD IN (SELECT SHOHIN_GRP_CD FROM M_SHOHIN_GRP_CMB WHERE BUN4_SHOHIN_GRP_CD = '10104010' ) ";
				} else if (StringUtils.equals(szShoG, "G10201")){  //化粧品+食品のみ固定
					sqlWhere += " AND SHOHIN_GRP_CD IN (SELECT SHOHIN_GRP_CD FROM M_SHOHIN_GRP_CMB WHERE BUN4_SHOHIN_GRP_CD = '10201010' ) ";
				}
			} else if (szShoG.length() == 7){  //スキンケア単品のみ固定
				sqlWhere += " AND SHOHIN_GRP_CD IN (SELECT SHOHIN_GRP_CD FROM M_SHOHIN_GRP_CMB WHERE BUN4_SHOHIN_GRP_CD = '10101010' ) ";
			} else {
				sqlWhere += " AND SHOHIN_GRP_CD = '"+szShoG+"' ";
			}

			//sqlWhere += " AND SHOHIN_GRP_CD IN (SELECT SHOHIN_GRP_CD FROM M_SHOHIN_GRP_CMB WHERE SHOHIN_GRP_CD = '"+szShoG+"' ) ";
		}
		//年齢
		if(!StringUtils.equals(szAge, "00")){
			sqlWhere += " AND AGE IN (SELECT AGE FROM M_AGE WHERE SEDAI_CD = '"+szAge+"' ) ";
		}

		String[] itemM = { "AS \"0-0.5万未満\"", "AS \"0.5-1万未満\"", "AS \"1-1.5万未満\"", "AS \"1.5-2万未満\"", "AS \"2-3万未満\"", "AS \"3-4万未満\"", "AS \"4-5万未満\"", "AS \"5-6万未満\"", "AS \"6-10万未満\"", "AS \"10万以上\"" };
		String[] itemM2 = { "AS \"0-0.5万未\"", "AS \"0.5-1万未\"", "AS \"1.0-1.5万未\"", "AS \"1.5-2.0万未\"", "AS \"2.0-2.5万未\"", "AS \"2.5-3.0万未\"", "AS \"3.0-3.5万未\"", "AS \"3.5-4.0万未\"", "AS \"4.0-4.5万未\"", "AS \"4.5-5.0万未\"", "AS \"5.0-5.5万未\"", "AS \"5.5-6.0万未\"", "AS \"6.0-6.5万未\"", "AS \"6.5-7.0万未\"", "AS \"7.0-7.5万未\"", "AS \"7.5-8.0万未\"", "AS \"8.0-8.5万未\"", "AS \"8.5-9.0万未\"", "AS \"9.0-9.5万未\"", "AS \"9.5-10万未\"", "AS \"10万以上\"" };

		String sqlItem0 = " ";
		String sqlSum1 = "  ";
		String sqlSum2 = "  ";
		String sqlItem1 = "  ";
		String sqlItem1G = "  ";
		String sqlItem2G = "  ";
		String sqlGroup = "  ";
		String sqlSORT2 = " 'xZ999' ";
		String sqlItem2 = "  ";
		String sqlTabel2 = "  ";
		if(StringUtils.equals(szType1, "1")){  //R x F
			sqlSum1  = "case when JUCKAISU = 1 then sum("+selItem+") else 0 end,case when JUCKAISU = 2 then sum("+selItem+") else 0 end,case when JUCKAISU = 3 then sum("+selItem+") else 0 end,case when JUCKAISU = 4 then sum("+selItem+") else 0 end,case when JUCKAISU = 5 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when JUCKAISU = 6 then sum("+selItem+") else 0 end,case when JUCKAISU = 7 then sum("+selItem+") else 0 end,case when JUCKAISU = 8 then sum("+selItem+") else 0 end,case when JUCKAISU = 9 then sum("+selItem+") else 0 end,case when JUCKAISU >= 10 then sum("+selItem+") else 0 end , sum("+selItem+") ";
			sqlSum2  = "sum(SU1) as \"１回\" , sum(SU2) as \"２回\" , sum(SU3) as \"３回\" , sum(SU4) as \"４回\",sum(SU5) as \"５回\" , ";
			sqlSum2 += "sum(SU6) as \"６回\" , sum(SU7) as \"７回\" , sum(SU8) as \"８回\" , sum(SU9) as \"９回\" ,sum(SU10) as \"１０回以上\" , sum(SU_ALL) as \"回数計\" ";
			sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,";
			sqlItem1  = " JUCHUMR ";
			sqlItem1G = " JUCHUMR_G as SORT , JUCHUMR_G_NM as \"最新受注月数\" ,";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUMR_G,JUCHUMR_G_NM ";
			sqlItem2  = " JUCKAISU ";
			sqlTabel2  = " work left outer join M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
			if(StringUtils.equals(szType2, "2")){  //R x F 2ヶ月展開
				sqlSum1  = "case when JUCKAISU = 1 then sum("+selItem+") else 0 end,case when JUCKAISU = 2 then sum("+selItem+") else 0 end,case when JUCKAISU = 3 then sum("+selItem+") else 0 end,case when JUCKAISU = 4 then sum("+selItem+") else 0 end,case when JUCKAISU = 5 then sum("+selItem+") else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 6 then sum("+selItem+") else 0 end,case when JUCKAISU = 7 then sum("+selItem+") else 0 end,case when JUCKAISU = 8 then sum("+selItem+") else 0 end,case when JUCKAISU = 9 then sum("+selItem+") else 0 end,case when JUCKAISU >= 10 then sum("+selItem+") else 0 end , sum("+selItem+") ";
				sqlSum2  = "sum(SU1) as \"１回\" , sum(SU2) as \"２回\" , sum(SU3) as \"３回\" , sum(SU4) as \"４回\",sum(SU5) as \"５回\" , ";
				sqlSum2 += "sum(SU6) as \"６回\" , sum(SU7) as \"７回\" , sum(SU8) as \"８回\" , sum(SU9) as \"９回\" ,sum(SU10) as \"１０回以上\" , sum(SU_ALL) as \"回数計\" ";
				sqlItem1G = " JUCHUMR_2 as SORT , JUCHUMR_2_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR_2,JUCHUMR_2_NM ";
			}else if(StringUtils.equals(szType2, "5")){  //R x F 展開
				sqlSum1  = "case when JUCKAISU = 1 then sum("+selItem+") else 0 end,case when JUCKAISU = 2 then sum("+selItem+") else 0 end,case when JUCKAISU = 3 then sum("+selItem+") else 0 end,case when JUCKAISU = 4 then sum("+selItem+") else 0 end,case when JUCKAISU = 5 then sum("+selItem+") else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 6 then sum("+selItem+") else 0 end,case when JUCKAISU = 7 then sum("+selItem+") else 0 end,case when JUCKAISU = 8 then sum("+selItem+") else 0 end,case when JUCKAISU = 9 then sum("+selItem+") else 0 end,case when JUCKAISU = 10 then sum("+selItem+") else 0 end ,  ";
				sqlSum1 += "case when JUCKAISU = 11 then sum("+selItem+") else 0 end,case when JUCKAISU = 12 then sum("+selItem+") else 0 end,case when JUCKAISU = 13 then sum("+selItem+") else 0 end,case when JUCKAISU = 14 then sum("+selItem+") else 0 end,case when JUCKAISU = 15 then sum("+selItem+") else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 16 then sum("+selItem+") else 0 end,case when JUCKAISU = 17 then sum("+selItem+") else 0 end,case when JUCKAISU = 18 then sum("+selItem+") else 0 end,case when JUCKAISU = 19 then sum("+selItem+") else 0 end,case when JUCKAISU = 20 then sum("+selItem+") else 0 end ,case when JUCKAISU = 21 then sum("+selItem+") else 0 end, sum("+selItem+") ";
				sqlSum2  = "sum(SU1) as \"１回\" , sum(SU2) as \"２回\" , sum(SU3) as \"３回\" , sum(SU4) as \"４回\",sum(SU5) as \"５回\", ";
				sqlSum2 += "sum(SU6) as \"６回\" , sum(SU7) as \"７回\" , sum(SU8) as \"８回\" , sum(SU9) as \"９回\" ,sum(SU10) as \"10回以上\",sum(SU_ALL) as \"回数計\" ";
				//sqlSum2 += "sum(SU11) as \"11回\" , sum(SU12) as \"12回\" , sum(SU13) as \"13回\" , sum(SU14) as \"14回\" ,sum(SU15) as \"15回\", ";
				//sqlSum2 += "sum(SU16) as \"16回\" , sum(SU17) as \"17回\" , sum(SU18) as \"18回\" , sum(SU19) as \"19回\" ,sum(SU20) as \"20回\" ,sum(SU21) as \"21回\" , sum(SU_ALL) as \"回数計\" ";
				sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,";
				sqlItem1G = " JUCHUMR as SORT , JUCHUMR_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR,JUCHUMR_NM ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "2")){  //R x M
			sqlSum1  = "case when LKONYU between -1 and 0 then sum("+selItem+") else 0 end , case when LKONYU = 1 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when LKONYU = 2 then sum("+selItem+") else 0 end , case when LKONYU = 3 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when LKONYU between 4 and 5 then sum("+selItem+") else 0 end , case when LKONYU between 6 and 7 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when LKONYU between 8 and 9 then sum("+selItem+") else 0 end , case when LKONYU between 10 and 11 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when LKONYU between 12 and 19 then sum("+selItem+") else 0 end , case when LKONYU = 20 then sum("+selItem+") else 0 end , sum("+selItem+") ";
			sqlSum2  = "sum(SU1) "+itemM[0]+", sum(SU2) "+itemM[1]+", sum(SU3) "+itemM[2]+", sum(SU4) "+itemM[3]+", ";
			sqlSum2 += "sum(SU5) "+itemM[4]+", sum(SU6) "+itemM[5]+", sum(SU7) "+itemM[6]+", ";
			sqlSum2 += "sum(SU8) "+itemM[7]+", sum(SU9) "+itemM[8]+", sum(SU10) "+itemM[9]+", sum(SU_ALL) as \"受注金額計\" ";
			sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,";
			sqlItem1  = " JUCHUMR ";
			sqlItem1G = " JUCHUMR_G as SORT , JUCHUMR_G_NM as \"最新受注月数\" ,";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUMR_G,JUCHUMR_G_NM ";
			sqlItem2  = " LKONYU ";
			sqlTabel2  = " work left outer join M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
			if(StringUtils.equals(szType2, "2")){  //R x M 2ヶ月展開
				sqlSum1  = "case when LKONYU between -1 and 0 then sum("+selItem+") else 0 end , case when LKONYU = 1 then sum("+selItem+") else 0 end , ";
				sqlSum1 += "case when LKONYU = 2 then sum("+selItem+") else 0 end , case when LKONYU = 3 then sum("+selItem+") else 0 end , ";
				sqlSum1 += "case when LKONYU between 4 and 5 then sum("+selItem+") else 0 end , case when LKONYU between 6 and 7 then sum("+selItem+") else 0 end , ";
				sqlSum1 += "case when LKONYU between 8 and 9 then sum("+selItem+") else 0 end , case when LKONYU between 10 and 11 then sum("+selItem+") else 0 end , ";
				sqlSum1 += "case when LKONYU between 12 and 19 then sum("+selItem+") else 0 end , case when LKONYU = 20 then sum("+selItem+") else 0 end , sum("+selItem+") ";
				sqlSum2  = "sum(SU1) "+itemM[0]+", sum(SU2) "+itemM[1]+", sum(SU3) "+itemM[2]+", sum(SU4) "+itemM[3]+", ";
				sqlSum2 += "sum(SU5) "+itemM[4]+", sum(SU6) "+itemM[5]+", sum(SU7) "+itemM[6]+", ";
				sqlSum2 += "sum(SU8) "+itemM[7]+", sum(SU9) "+itemM[8]+", sum(SU10) "+itemM[9]+", sum(SU_ALL) as \"受注金額計\" ";
				sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,";
				sqlItem1  = " JUCHUMR ";
				sqlItem1G = " JUCHUMR_2 as SORT , JUCHUMR_2_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR_2,JUCHUMR_2_NM ";
				sqlItem2  = " LKONYU ";
				sqlTabel2  = " work left outer join M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
			}else if(StringUtils.equals(szType2, "5")){  //R x M 展開
				sqlSum1  = "case when LKONYU between -1 and 0 then sum("+selItem+") else 0 end , case when LKONYU = 1 then sum("+selItem+") else 0 end , ";
				sqlSum1 += "case when LKONYU = 2 then sum("+selItem+") else 0 end,case when LKONYU = 3 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 4 then sum("+selItem+") else 0 end,case when LKONYU = 5 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 6 then sum("+selItem+") else 0 end,case when LKONYU = 7 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 8 then sum("+selItem+") else 0 end,case when LKONYU = 9 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 10 then sum("+selItem+") else 0 end,case when LKONYU = 11 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 12 then sum("+selItem+") else 0 end,case when LKONYU = 13 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 14 then sum("+selItem+") else 0 end,case when LKONYU = 15 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 16 then sum("+selItem+") else 0 end,case when LKONYU = 17 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 18 then sum("+selItem+") else 0 end,case when LKONYU = 19 then sum("+selItem+") else 0 end,";
				sqlSum1 += "case when LKONYU = 20 then sum("+selItem+") else 0 end,sum("+selItem+") ";
				sqlSum2  = "sum(SU1) "+itemM2[0]+", sum(SU2) "+itemM2[1]+", sum(SU3) "+itemM2[2]+", sum(SU4) "+itemM2[3]+", ";
				sqlSum2 += "sum(SU5) "+itemM2[4]+", sum(SU6) "+itemM2[5]+", sum(SU7) "+itemM2[6]+", ";
				sqlSum2 += "sum(SU8) "+itemM2[7]+", sum(SU9) "+itemM2[8]+", sum(SU10) "+itemM2[9]+", ";
				sqlSum2 += "sum(SU11) "+itemM2[10]+", sum(SU12) "+itemM2[11]+", sum(SU13) "+itemM2[12]+", ";
				sqlSum2 += "sum(SU14) "+itemM2[13]+", sum(SU15) "+itemM2[14]+", sum(SU16) "+itemM2[15]+", ";
				sqlSum2 += "sum(SU17) "+itemM2[16]+", sum(SU18) "+itemM2[17]+", sum(SU19) "+itemM2[18]+",sum(SU20) "+itemM2[19]+",  ";
				sqlSum2 += "sum(SU21) "+itemM2[20]+", sum(SU_ALL) as \"受注金額計\" ";
				sqlItem1  = " JUCHUMR ";
				sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,";
				sqlItem1G = " JUCHUMR as SORT , JUCHUMR_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR,JUCHUMR_NM ";
				sqlItem2  = " LKONYU ";
				sqlTabel2  = " work left outer join M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "3")){  //F x M 展開はなし
			sqlSum1  = "case when LKONYU between -1 and 0 then sum("+selItem+") else 0 end , case when LKONYU = 1 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when LKONYU = 2 then sum("+selItem+") else 0 end , case when LKONYU = 3 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when LKONYU between 4 and 5 then sum("+selItem+") else 0 end , case when LKONYU between 6 and 7 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when LKONYU between 8 and 9 then sum("+selItem+") else 0 end , case when LKONYU between 10 and 11 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when LKONYU between 12 and 19 then sum("+selItem+") else 0 end , case when LKONYU = 20 then sum("+selItem+") else 0 end , sum("+selItem+") ";
			sqlSum2  = "sum(SU1) "+itemM[0]+", sum(SU2) "+itemM[1]+", sum(SU3) "+itemM[2]+", sum(SU4) "+itemM[3]+", ";
			sqlSum2 += "sum(SU5) "+itemM[4]+", sum(SU6) "+itemM[5]+", sum(SU7) "+itemM[6]+", ";
			sqlSum2 += "sum(SU8) "+itemM[7]+", sum(SU9) "+itemM[8]+", sum(SU10) "+itemM[9]+", sum(SU_ALL) as \"受注金額計\" ";
			sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,";
			sqlItem1  = " JUCKAISU ";
			sqlItem1G = " FRANK_G_CD as SORT , FRANK_G_NM as \"回数\" ,";
			sqlItem2G = " '回数計' as \"回数\" , ";
			sqlGroup = " FRANK_G_CD,FRANK_G_NM ";
			sqlItem2  = " LKONYU ";
			sqlTabel2  = " work left outer join M_FRANK on ITEN_T = FRANK ";
			//if(StringUtils.equals(szType2, "3")){  //F x M 展開
			//	sqlSum1  = "case when LKONYU between -1 and 0 then sum("+selItem+") else 0 end , case when LKONYU = 1 then sum("+selItem+") else 0 end , ";
			//	sqlSum1 += "case when LKONYU = 2 then sum("+selItem+") else 0 end,case when LKONYU = 3 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 4 then sum("+selItem+") else 0 end,case when LKONYU = 5 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 6 then sum("+selItem+") else 0 end,case when LKONYU = 7 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 8 then sum("+selItem+") else 0 end,case when LKONYU = 9 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 10 then sum("+selItem+") else 0 end,case when LKONYU = 11 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 12 then sum("+selItem+") else 0 end,case when LKONYU = 13 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 14 then sum("+selItem+") else 0 end,case when LKONYU = 15 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 16 then sum("+selItem+") else 0 end,case when LKONYU = 17 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 18 then sum("+selItem+") else 0 end,case when LKONYU = 19 then sum("+selItem+") else 0 end,";
			//	sqlSum1 += "case when LKONYU = 20 then sum("+selItem+") else 0 end,sum("+selItem+") ";
			//	sqlSum2  = "sum(SU1) "+itemM2[0]+", sum(SU2) "+itemM2[1]+", sum(SU3) "+itemM2[2]+", sum(SU4) "+itemM2[3]+", ";
			//	sqlSum2 += "sum(SU5) "+itemM2[4]+", sum(SU6) "+itemM2[5]+", sum(SU7) "+itemM2[6]+", ";
			//	sqlSum2 += "sum(SU8) "+itemM2[7]+", sum(SU9) "+itemM2[8]+", sum(SU10) "+itemM2[9]+", ";
			//	sqlSum2 += "sum(SU11) "+itemM2[10]+", sum(SU12) "+itemM2[11]+", sum(SU13) "+itemM2[12]+", ";
			//	sqlSum2 += "sum(SU14) "+itemM2[13]+", sum(SU15) "+itemM2[14]+", sum(SU16) "+itemM2[15]+", ";
			//	sqlSum2 += "sum(SU17) "+itemM2[16]+", sum(SU18) "+itemM2[17]+", sum(SU19) "+itemM2[18]+",sum(SU20) "+itemM2[19]+",  ";
			//	sqlSum2 += "sum(SU21) "+itemM2[20]+", sum(SU_ALL) as \"受注金額計\" ";
			//	sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,";
			//	sqlItem1  = " JUCKAISU ";
			//	sqlItem1G = " FRANK as SORT , FRANK_NM as \"回数\" ,";
			//	sqlItem2G = " '回数計' as \"回数\" , ";
			//	sqlGroup = " FRANK,FRANK_NM ";
			//	sqlItem2  = " LKONYU ";
			//	sqlTabel2  = " work left outer join M_FRANK on ITEN_T = FRANK ";
			//	sqlSORT2 = "9999" ;
			//}
		}else if (StringUtils.equals(szType1, "4")){  //R x 初回受注月
			sqlWhere += " AND substr(NF_JUCHU_YM,1,4) >= '"+szJucYM1+"' AND substr(NF_JUCHU_YM,1,4) <= '"+szJucYM2+"' ";
			sqlSum1 = "";
			sqlSum2 = "";
			sqlItem0 = "";
			int  cnt_yy = Integer.valueOf(szJucYM2).intValue()-Integer.valueOf(szJucYM1).intValue()+1;
			for (int i=0; i<cnt_yy; i++){
				int yy_int = Integer.valueOf(szJucYM1).intValue()+i;
				sqlItem0 +="M"+yy_int+"01,M"+yy_int+"02,M"+yy_int+"03,M"+yy_int+"04,M"+yy_int+"05,M"+yy_int+"06,M"+yy_int+"07,M"+yy_int+"08,M"+yy_int+"09,M"+yy_int+"10,M"+yy_int+"11,M"+yy_int+"12, ";
				sqlSum2 +="SUM(M"+yy_int+"01) AS \""+yy_int+"01\",SUM(M"+yy_int+"02) AS \""+yy_int+"02\",SUM(M"+yy_int+"03) AS \""+yy_int+"03\",SUM(M"+yy_int+"04) AS \""+yy_int+"04\",SUM(M"+yy_int+"05) AS \""+yy_int+"05\",SUM(M"+yy_int+"06) AS \""+yy_int+"06\",SUM(M"+yy_int+"07) AS \""+yy_int+"07\",SUM(M"+yy_int+"08) AS \""+yy_int+"08\",SUM(M"+yy_int+"09) AS \""+yy_int+"09\",SUM(M"+yy_int+"10) AS \""+yy_int+"10\",SUM(M"+yy_int+"11) AS \""+yy_int+"11\",SUM(M"+yy_int+"12) AS \""+yy_int+"12\", ";
				sqlSum1 +="CASE WHEN NF_JUCHU_YM = '"+yy_int+"01' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"02' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"03' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"04' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"05' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"06' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"07' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"08' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"09' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"10' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"11' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"12' THEN SUM("+selItem+") ELSE 0 END,";
			}
			sqlSum2 +="SUM(SU_ALL) AS \"計\" ";
			sqlSum1 +="SUM("+selItem+") ";
			sqlItem1  = " JUCHUMR ";
			sqlItem1G = " JUCHUMR_G as SORT , JUCHUMR_G_NM as \"最新受注月数\" ,";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUMR_G,JUCHUMR_G_NM ";
			sqlItem2  = " NF_JUCHU_YM ";
			sqlTabel2  = " M_JUCHU_R_CNT left outer join work on ITEN_T = JUCHUMR ";
			if(StringUtils.equals(szType2, "2")){  //R x 初回受注月 2ヶ月展開
				sqlItem1G = " JUCHUMR_2 as SORT , JUCHUMR_2_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR_2,JUCHUMR_2_NM ";
			}else if(StringUtils.equals(szType2, "5")){  //R x 初回受注月 展開
				sqlItem1G = " JUCHUMR as SORT , JUCHUMR_NM as \"最新受注月数\" ,";
				sqlGroup = " JUCHUMR,JUCHUMR_NM ";
				sqlTabel2  = " M_JUCHU_R_CNT inner join work on ITEN_T = JUCHUMR ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "5")){  //F x 初回受注月
			sqlWhere += " AND substr(NF_JUCHU_YM,1,4) >= '"+szJucYM1+"' AND substr(NF_JUCHU_YM,1,4) <= '"+szJucYM2+"' ";
			sqlSum1 = "";
			sqlSum2 = "";
			sqlItem0 = "";
			int  cnt_yy = Integer.valueOf(szJucYM2).intValue()-Integer.valueOf(szJucYM1).intValue()+1;
			for (int i=0; i<cnt_yy; i++){
				int yy_int = Integer.valueOf(szJucYM1).intValue()+i;
				sqlItem0 +="M"+yy_int+"01,M"+yy_int+"02,M"+yy_int+"03,M"+yy_int+"04,M"+yy_int+"05,M"+yy_int+"06,M"+yy_int+"07,M"+yy_int+"08,M"+yy_int+"09,M"+yy_int+"10,M"+yy_int+"11,M"+yy_int+"12, ";
				sqlSum2 +="SUM(M"+yy_int+"01) AS \""+yy_int+"01\",SUM(M"+yy_int+"02) AS \""+yy_int+"02\",SUM(M"+yy_int+"03) AS \""+yy_int+"03\",SUM(M"+yy_int+"04) AS \""+yy_int+"04\",SUM(M"+yy_int+"05) AS \""+yy_int+"05\",SUM(M"+yy_int+"06) AS \""+yy_int+"06\",SUM(M"+yy_int+"07) AS \""+yy_int+"07\",SUM(M"+yy_int+"08) AS \""+yy_int+"08\",SUM(M"+yy_int+"09) AS \""+yy_int+"09\",SUM(M"+yy_int+"10) AS \""+yy_int+"10\",SUM(M"+yy_int+"11) AS \""+yy_int+"11\",SUM(M"+yy_int+"12) AS \""+yy_int+"12\", ";
				sqlSum1 +="CASE WHEN NF_JUCHU_YM = '"+yy_int+"01' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"02' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"03' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"04' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"05' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"06' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"07' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"08' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"09' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"10' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"11' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"12' THEN SUM("+selItem+") ELSE 0 END,";
			}
			sqlSum2 +="SUM(SU_ALL) AS \"計\" ";
			sqlSum1 +="SUM("+selItem+") ";
			sqlItem1  = " JUCKAISU ";
			sqlItem1G = " FRANK_G_CD as SORT , FRANK_G_NM as \"回数\" ,";
			sqlItem2G = " '回数計' as \"回数\" , ";
			sqlGroup = " FRANK_G_CD,FRANK_G_NM ";
			sqlItem2  = " NF_JUCHU_YM ";
			sqlTabel2  = " work left outer join M_FRANK on ITEN_T = FRANK ";
		}else if (StringUtils.equals(szType1, "6")){  //M x 初回受注月
			sqlWhere += " AND substr(NF_JUCHU_YM,1,4) >= '"+szJucYM1+"' AND substr(NF_JUCHU_YM,1,4) <= '"+szJucYM2+"' ";
			sqlSum1 = "";
			sqlSum2 = "";
			sqlItem0 = "";
			int  cnt_yy = Integer.valueOf(szJucYM2).intValue()-Integer.valueOf(szJucYM1).intValue()+1;
			for (int i=0; i<cnt_yy; i++){
				int yy_int = Integer.valueOf(szJucYM1).intValue()+i;
				sqlItem0 +="M"+yy_int+"01,M"+yy_int+"02,M"+yy_int+"03,M"+yy_int+"04,M"+yy_int+"05,M"+yy_int+"06,M"+yy_int+"07,M"+yy_int+"08,M"+yy_int+"09,M"+yy_int+"10,M"+yy_int+"11,M"+yy_int+"12, ";
				sqlSum2 +="SUM(M"+yy_int+"01) AS \""+yy_int+"01\",SUM(M"+yy_int+"02) AS \""+yy_int+"02\",SUM(M"+yy_int+"03) AS \""+yy_int+"03\",SUM(M"+yy_int+"04) AS \""+yy_int+"04\",SUM(M"+yy_int+"05) AS \""+yy_int+"05\",SUM(M"+yy_int+"06) AS \""+yy_int+"06\",SUM(M"+yy_int+"07) AS \""+yy_int+"07\",SUM(M"+yy_int+"08) AS \""+yy_int+"08\",SUM(M"+yy_int+"09) AS \""+yy_int+"09\",SUM(M"+yy_int+"10) AS \""+yy_int+"10\",SUM(M"+yy_int+"11) AS \""+yy_int+"11\",SUM(M"+yy_int+"12) AS \""+yy_int+"12\", ";
				sqlSum1 +="CASE WHEN NF_JUCHU_YM = '"+yy_int+"01' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"02' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"03' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"04' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"05' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"06' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"07' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"08' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"09' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"10' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"11' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"12' THEN SUM("+selItem+") ELSE 0 END,";
			}
			sqlSum2 +="SUM(SU_ALL) AS \"計\" ";
			sqlSum1 +="SUM("+selItem+") ";
			sqlItem1  = " LKONYU ";
			sqlItem1G = " MRANK_G_CD as SORT , MRANK_G_NM as \"受注金額(万円)\" ,";
			sqlItem2G = " '受注金額計' as \"受注金額(万円)\" , ";
			sqlGroup = " MRANK_G_CD , MRANK_G_NM ";
			sqlItem2  = " NF_JUCHU_YM ";
			sqlTabel2  = " work left outer join M_MRANK on ITEN_T = MRANK ";
		}else if (StringUtils.equals(szType1, "7")){  //年齢 x 初回受注月
			sqlWhere += " AND substr(NF_JUCHU_YM,1,4) >= '"+szJucYM1+"' AND substr(NF_JUCHU_YM,1,4) <= '"+szJucYM2+"' ";
			sqlSum1 = "";
			sqlSum2 = "";
			sqlItem0 = "";
			int  cnt_yy = Integer.valueOf(szJucYM2).intValue()-Integer.valueOf(szJucYM1).intValue()+1;
			for (int i=0; i<cnt_yy; i++){
				int yy_int = Integer.valueOf(szJucYM1).intValue()+i;
				sqlItem0 +="M"+yy_int+"01,M"+yy_int+"02,M"+yy_int+"03,M"+yy_int+"04,M"+yy_int+"05,M"+yy_int+"06,M"+yy_int+"07,M"+yy_int+"08,M"+yy_int+"09,M"+yy_int+"10,M"+yy_int+"11,M"+yy_int+"12, ";
				sqlSum2 +="SUM(M"+yy_int+"01) AS \""+yy_int+"01\",SUM(M"+yy_int+"02) AS \""+yy_int+"02\",SUM(M"+yy_int+"03) AS \""+yy_int+"03\",SUM(M"+yy_int+"04) AS \""+yy_int+"04\",SUM(M"+yy_int+"05) AS \""+yy_int+"05\",SUM(M"+yy_int+"06) AS \""+yy_int+"06\",SUM(M"+yy_int+"07) AS \""+yy_int+"07\",SUM(M"+yy_int+"08) AS \""+yy_int+"08\",SUM(M"+yy_int+"09) AS \""+yy_int+"09\",SUM(M"+yy_int+"10) AS \""+yy_int+"10\",SUM(M"+yy_int+"11) AS \""+yy_int+"11\",SUM(M"+yy_int+"12) AS \""+yy_int+"12\", ";
				sqlSum1 +="CASE WHEN NF_JUCHU_YM = '"+yy_int+"01' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"02' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"03' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"04' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"05' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"06' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"07' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"08' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"09' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"10' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"11' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"12' THEN SUM("+selItem+") ELSE 0 END,";
			}
			sqlSum2 +="SUM(SU_ALL) AS \"計\" ";
			sqlSum1 +="SUM("+selItem+") ";
			sqlItem1  = " AGE ";
			sqlItem1G = " SEDAI_CD as SORT , SEDAI_MEI as \"世代\" ,";
			sqlItem2G = " '世代計' as \"世代\" , ";
			sqlGroup = " SEDAI_CD , SEDAI_MEI ";
			sqlItem2  = " NF_JUCHU_YM ";
			sqlTabel2  = " work left outer join M_AGE on ITEN_T = AGE ";
			if(StringUtils.equals(szType2, "5")){                           //年齢 x 初回受注月 展開
				sqlItem1G = " AGE as SORT , AGE_MEI as \"年齢\" ,";
				sqlItem2G = " '年齢計' as \"年齢\" , ";
				sqlGroup = " AGE,AGE_MEI ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "8")){  //初回金額ランク x 初回受注月
			sqlWhere += " AND substr(NF_JUCHU_YM,1,4) >= '"+szJucYM1+"' AND substr(NF_JUCHU_YM,1,4) <= '"+szJucYM2+"' ";
			sqlSum1 = "";
			sqlSum2 = "";
			sqlItem0 = "";
			int  cnt_yy = Integer.valueOf(szJucYM2).intValue()-Integer.valueOf(szJucYM1).intValue()+1;
			for (int i=0; i<cnt_yy; i++){
				int yy_int = Integer.valueOf(szJucYM1).intValue()+i;
				sqlItem0 +="M"+yy_int+"01,M"+yy_int+"02,M"+yy_int+"03,M"+yy_int+"04,M"+yy_int+"05,M"+yy_int+"06,M"+yy_int+"07,M"+yy_int+"08,M"+yy_int+"09,M"+yy_int+"10,M"+yy_int+"11,M"+yy_int+"12, ";
				sqlSum2 +="SUM(M"+yy_int+"01) AS \""+yy_int+"01\",SUM(M"+yy_int+"02) AS \""+yy_int+"02\",SUM(M"+yy_int+"03) AS \""+yy_int+"03\",SUM(M"+yy_int+"04) AS \""+yy_int+"04\",SUM(M"+yy_int+"05) AS \""+yy_int+"05\",SUM(M"+yy_int+"06) AS \""+yy_int+"06\",SUM(M"+yy_int+"07) AS \""+yy_int+"07\",SUM(M"+yy_int+"08) AS \""+yy_int+"08\",SUM(M"+yy_int+"09) AS \""+yy_int+"09\",SUM(M"+yy_int+"10) AS \""+yy_int+"10\",SUM(M"+yy_int+"11) AS \""+yy_int+"11\",SUM(M"+yy_int+"12) AS \""+yy_int+"12\", ";
				sqlSum1 +="CASE WHEN NF_JUCHU_YM = '"+yy_int+"01' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"02' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"03' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"04' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"05' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"06' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"07' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"08' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"09' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"10' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"11' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"12' THEN SUM("+selItem+") ELSE 0 END,";
			}
			sqlSum2 +="SUM(SU_ALL) AS \"計\" ";
			sqlSum1 +="SUM("+selItem+") ";
			sqlItem1  = " NF_LKONYU ";
			sqlItem1G = " MRANK_GROUP_CD as SORT , MRANK_GROUP_MEI as \"初回金額ランク\" ,";
			sqlItem2G = " '計' as \"初回金額ランク\" , ";
			sqlGroup = " MRANK_GROUP_CD , MRANK_GROUP_MEI ";
			sqlItem2  = " NF_JUCHU_YM ";
			sqlTabel2  = " work left outer join M_MRANK_F on ITEN_T = MRANK_NO ";
			//if(StringUtils.equals(szType2, "3")){                           //初回金額ランク x 初回受注月 展開
			//	sqlItem1G = " MRANK_NO as SORT , MRANK_MEI as \"初回金額ランク\" ,";
			//	sqlItem2G = " '計' as \"初回金額ランク\" , ";
			//	sqlGroup = " MRANK_NO,MRANK_MEI ";
			//	sqlSORT2 = "9999" ;
			//}
		}else if (StringUtils.equals(szType1, "9")){  //累計金額ランク x 初回受注月
			sqlWhere += " AND substr(NF_JUCHU_YM,1,4) >= '"+szJucYM1+"' AND substr(NF_JUCHU_YM,1,4) <= '"+szJucYM2+"' ";
			sqlSum1 = "";
			sqlSum2 = "";
			sqlItem0 = "";
			int  cnt_yy = Integer.valueOf(szJucYM2).intValue()-Integer.valueOf(szJucYM1).intValue()+1;
			for (int i=0; i<cnt_yy; i++){
				int yy_int = Integer.valueOf(szJucYM1).intValue()+i;
				sqlItem0 +="M"+yy_int+"01,M"+yy_int+"02,M"+yy_int+"03,M"+yy_int+"04,M"+yy_int+"05,M"+yy_int+"06,M"+yy_int+"07,M"+yy_int+"08,M"+yy_int+"09,M"+yy_int+"10,M"+yy_int+"11,M"+yy_int+"12, ";
				sqlSum2 +="SUM(M"+yy_int+"01) AS \""+yy_int+"01\",SUM(M"+yy_int+"02) AS \""+yy_int+"02\",SUM(M"+yy_int+"03) AS \""+yy_int+"03\",SUM(M"+yy_int+"04) AS \""+yy_int+"04\",SUM(M"+yy_int+"05) AS \""+yy_int+"05\",SUM(M"+yy_int+"06) AS \""+yy_int+"06\",SUM(M"+yy_int+"07) AS \""+yy_int+"07\",SUM(M"+yy_int+"08) AS \""+yy_int+"08\",SUM(M"+yy_int+"09) AS \""+yy_int+"09\",SUM(M"+yy_int+"10) AS \""+yy_int+"10\",SUM(M"+yy_int+"11) AS \""+yy_int+"11\",SUM(M"+yy_int+"12) AS \""+yy_int+"12\", ";
				sqlSum1 +="CASE WHEN NF_JUCHU_YM = '"+yy_int+"01' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"02' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"03' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"04' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"05' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"06' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"07' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"08' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"09' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"10' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"11' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"12' THEN SUM("+selItem+") ELSE 0 END,";
			}
			sqlSum2 +="SUM(SU_ALL) AS \"計\" ";
			sqlSum1 +="SUM("+selItem+") ";
			sqlItem1  = " LKONYU ";
			sqlItem1G = " MRANK_G_CD as SORT , MRANK_G_NM as \"累計金額ランク\" ,";
			sqlItem2G = " '計' as \"累計金額ランク\" , ";
			sqlGroup = " MRANK_G_CD , MRANK_G_NM ";
			sqlItem2  = " NF_JUCHU_YM ";
			sqlTabel2  = " work left outer join M_MRANK on ITEN_T = MRANK ";
			if(StringUtils.equals(szType2, "3")){                           //累計金額ランク x 初回受注月 展開
				sqlItem1G = " MRANK as SORT , MRANK_NM as \"累計金額ランク\" ,";
				sqlItem2G = " '計' as \"累計金額ランク\" , ";
				sqlGroup = " MRANK,MRANK_NM ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "10")){  //初回受注形態 x 初回受注月
			sqlWhere += " AND substr(NF_JUCHU_YM,1,4) >= '"+szJucYM1+"' AND substr(NF_JUCHU_YM,1,4) <= '"+szJucYM2+"' ";
			sqlSum1 = "";
			sqlSum2 = "";
			sqlItem0 = "";
			int  cnt_yy = Integer.valueOf(szJucYM2).intValue()-Integer.valueOf(szJucYM1).intValue()+1;
			for (int i=0; i<cnt_yy; i++){
				int yy_int = Integer.valueOf(szJucYM1).intValue()+i;
				sqlItem0 +="M"+yy_int+"01,M"+yy_int+"02,M"+yy_int+"03,M"+yy_int+"04,M"+yy_int+"05,M"+yy_int+"06,M"+yy_int+"07,M"+yy_int+"08,M"+yy_int+"09,M"+yy_int+"10,M"+yy_int+"11,M"+yy_int+"12, ";
				sqlSum2 +="SUM(M"+yy_int+"01) AS \""+yy_int+"01\",SUM(M"+yy_int+"02) AS \""+yy_int+"02\",SUM(M"+yy_int+"03) AS \""+yy_int+"03\",SUM(M"+yy_int+"04) AS \""+yy_int+"04\",SUM(M"+yy_int+"05) AS \""+yy_int+"05\",SUM(M"+yy_int+"06) AS \""+yy_int+"06\",SUM(M"+yy_int+"07) AS \""+yy_int+"07\",SUM(M"+yy_int+"08) AS \""+yy_int+"08\",SUM(M"+yy_int+"09) AS \""+yy_int+"09\",SUM(M"+yy_int+"10) AS \""+yy_int+"10\",SUM(M"+yy_int+"11) AS \""+yy_int+"11\",SUM(M"+yy_int+"12) AS \""+yy_int+"12\", ";
				sqlSum1 +="CASE WHEN NF_JUCHU_YM = '"+yy_int+"01' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"02' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"03' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"04' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"05' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"06' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"07' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"08' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"09' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"10' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"11' THEN SUM("+selItem+") ELSE 0 END";
				sqlSum1 +=",CASE WHEN NF_JUCHU_YM = '"+yy_int+"12' THEN SUM("+selItem+") ELSE 0 END,";
			}
			sqlSum2 +="SUM(SU_ALL) AS \"計\" ";
			sqlSum1 +="SUM("+selItem+") ";
			sqlItem1  = " JUCHU_KBN ";
			sqlItem1G = " JUC_D_KBN as SORT , JUC_D_KBN_NM as \"受注区分\" ,";
			sqlItem2G = " '計' as \"受注区分\" , ";
			sqlGroup = " JUC_D_KBN , JUC_D_KBN_NM ";
			sqlItem2  = " NF_JUCHU_YM ";
			sqlTabel2  = " M_JUC_KBN left outer join WORK on ITEN_T = JUC_KBN ";
		}else if (StringUtils.equals(szType1, "11")){  //商品 x 年齢
			sqlSum1  = "case when AGE between 12 and 19 then sum("+selItem+") else 0 end,case when AGE between 20 and 24 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 25 and 29 then sum("+selItem+") else 0 end,case when AGE between 30 and 34 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 35 and 39 then sum("+selItem+") else 0 end,case when AGE between 40 and 44 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 45 and 49 then sum("+selItem+") else 0 end,case when AGE between 50 and 54 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 55 and 59 then sum("+selItem+") else 0 end,case when AGE between 60 and 69 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 70 and 99 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE < 12 OR AGE >= 100 then sum("+selItem+") else 0 end , sum("+selItem+") ";
			sqlSum2  = "sum(SU1) as \"20歳未満\",sum(SU2) as \"20-24歳\",sum(SU3) as \"25-29歳\",Sum(SU4) as \"30-34歳\",";
			sqlSum2 += "sum(SU5) as \"35-39歳\",sum(SU6) as \"40-44歳\",sum(SU7) as \"45-49歳\",sum(SU8) as \"50-54歳\",";
			sqlSum2 += "sum(SU9) as \"55-59歳\",sum(SU10) as \"60-69歳\",sum(SU11) as \"70歳以上\",sum(SU12) as \"不明\",sum(SU_ALL) as \"計\" ";
			sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,SU11,SU12,";
			sqlItem1  = " SHOHIN_GRP_CD ";
			sqlItem1G = " BUN1_SHOHIN_GRP_CD as SORT , BUN1_SHOHIN_GRP_MEI as \"商品\" ,";
			sqlItem2G = " '計' as \"商品\" , ";
			sqlGroup = " BUN1_SHOHIN_GRP_CD,BUN1_SHOHIN_GRP_MEI ";
			sqlItem2  = " AGE ";
			sqlTabel2  = " M_SHOHIN_GRP left outer join WORK on ITEN_T = SHOHIN_GRP_CD ";
		}else if (StringUtils.equals(szType1, "12")){  //媒体 x 年齢
			sqlSum1  = "case when AGE between 12 and 19 then sum("+selItem+") else 0 end,case when AGE between 20 and 24 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 25 and 29 then sum("+selItem+") else 0 end,case when AGE between 30 and 34 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 35 and 39 then sum("+selItem+") else 0 end,case when AGE between 40 and 44 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 45 and 49 then sum("+selItem+") else 0 end,case when AGE between 50 and 54 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 55 and 59 then sum("+selItem+") else 0 end,case when AGE between 60 and 69 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 70 and 99 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE < 12 OR AGE >= 100 then sum("+selItem+") else 0 end , sum("+selItem+") ";
			sqlSum2  = "sum(SU1) as \"20歳未満\",sum(SU2) as \"20-24歳\",sum(SU3) as \"25-29歳\",Sum(SU4) as \"30-34歳\",";
			sqlSum2 += "sum(SU5) as \"35-39歳\",sum(SU6) as \"40-44歳\",sum(SU7) as \"45-49歳\",sum(SU8) as \"50-54歳\",";
			sqlSum2 += "sum(SU9) as \"55-59歳\",sum(SU10) as \"60-69歳\",sum(SU11) as \"70歳以上\",sum(SU12) as \"不明\",sum(SU_ALL) as \"計\" ";
			sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,SU11,SU12,";
			sqlItem1  = " PROMO_CD ";
			sqlItem1G = " BYTAI1_CD as SORT , BYTAI1_MEI as \"媒体\" ,";
			sqlItem2G = " '計' as \"媒体\" , ";
			sqlGroup = " BYTAI1_CD,BYTAI1_MEI ";
			sqlItem2  = " AGE ";
			sqlTabel2  = " M_PROMO_ALL left outer join WORK on ITEN_T = PROMO_CD ";
		}else if (StringUtils.equals(szType1, "13")){  //地域 x 年齢
			sqlSum1  = "case when AGE between 12 and 19 then sum("+selItem+") else 0 end,case when AGE between 20 and 24 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 25 and 29 then sum("+selItem+") else 0 end,case when AGE between 30 and 34 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 35 and 39 then sum("+selItem+") else 0 end,case when AGE between 40 and 44 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 45 and 49 then sum("+selItem+") else 0 end,case when AGE between 50 and 54 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 55 and 59 then sum("+selItem+") else 0 end,case when AGE between 60 and 69 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE between 70 and 99 then sum("+selItem+") else 0 end,";
			sqlSum1 += "case when AGE < 12 OR AGE >= 100 then sum("+selItem+") else 0 end , sum("+selItem+") ";
			sqlSum2  = "sum(SU1) as \"20歳未満\",sum(SU2) as \"20-24歳\",sum(SU3) as \"25-29歳\",Sum(SU4) as \"30-34歳\",";
			sqlSum2 += "sum(SU5) as \"35-39歳\",sum(SU6) as \"40-44歳\",sum(SU7) as \"45-49歳\",sum(SU8) as \"50-54歳\",";
			sqlSum2 += "sum(SU9) as \"55-59歳\",sum(SU10) as \"60-69歳\",sum(SU11) as \"70歳以上\",sum(SU12) as \"不明\",sum(SU_ALL) as \"計\" ";
			sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,SU11,SU12,";
			sqlItem1  = " PREF_CD ";
			sqlItem1G = " CHIKU_CD as SORT , CHIKU_MEI as \"都道府県\" ,";
			sqlItem2G = " '計' as \"都道府県\" , ";
			sqlGroup = " CHIKU_CD,CHIKU_MEI ";
			sqlItem2  = " AGE ";
			sqlTabel2  = " M_AREA left outer join WORK on ITEN_T = CHIKU_CD ";
		}else if (StringUtils.equals(szType1, "14")){  //年齢 x F
			sqlSum1  = "case when JUCKAISU = 1 then sum("+selItem+") else 0 end,case when JUCKAISU = 2 then sum("+selItem+") else 0 end,case when JUCKAISU = 3 then sum("+selItem+") else 0 end,case when JUCKAISU = 4 then sum("+selItem+") else 0 end,case when JUCKAISU = 5 then sum("+selItem+") else 0 end , ";
			sqlSum1 += "case when JUCKAISU = 6 then sum("+selItem+") else 0 end,case when JUCKAISU = 7 then sum("+selItem+") else 0 end,case when JUCKAISU = 8 then sum("+selItem+") else 0 end,case when JUCKAISU = 9 then sum("+selItem+") else 0 end,case when JUCKAISU >= 10 then sum("+selItem+") else 0 end , sum("+selItem+") ";
			sqlSum2  = "sum(SU1) as \"１回\" , sum(SU2) as \"２回\" , sum(SU3) as \"３回\" , sum(SU4) as \"４回\",sum(SU5) as \"５回\" , ";
			sqlSum2 += "sum(SU6) as \"６回\" , sum(SU7) as \"７回\" , sum(SU8) as \"８回\" , sum(SU9) as \"９回\" ,sum(SU10) as \"１０回以上\" , sum(SU_ALL) as \"回数計\" ";
			sqlItem0  = " SU1,SU2,SU3,SU4,SU5,SU6,SU7,SU8,SU9,SU10,";
			sqlItem1  = " AGE ";
			sqlItem1G = " SEDAI_CD as SORT , SEDAI_MEI as \"SEDAI\" ,";
			sqlItem2G = " '計' as \"都道府県\" , ";
			sqlGroup = " SEDAI_CD,SEDAI_MEI ";
			sqlItem2  = " JUCKAISU ";
			sqlTabel2  = " M_AGE left outer join WORK on ITEN_T = AGE ";
		}

		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("with work(ITEN_T, "+sqlItem0+"SU_ALL) as (");
		sbSQL.append(" select "+sqlItem1+", ");
		sbSQL.append(sqlSum1);
		sbSQL.append("from "+sqlTable+" T01 ");
		sbSQL.append(" where "+sqlWhere+" group by "+sqlItem1+" , "+sqlItem2+" ) ");
		sbSQL.append("select "+sqlItem1G );
		sbSQL.append(sqlSum2);
		sbSQL.append("from  "+sqlTabel2 );
		sbSQL.append("group by "+sqlGroup );
		sbSQL.append("union all ");
		sbSQL.append("select "+sqlSORT2+" as SORT,"+sqlItem2G );
		sbSQL.append(sqlSum2);
		sbSQL.append("from work order by SORT");

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
		getWhere().add(cells);

		cells.add( DefineReport.Select.F_JUCHU.getView());
		cells.add( jad.getJSONText(DefineReport.Select.F_JUCHU.getObj()) );
		cells.add( jad.getJSONText(DefineReport.Select.F_JUCHU_TO.getObj()) );
		//
		cells.add( DefineReport.Select.KAISU.getView());
		cells.add( jad.getJSONText(DefineReport.Select.KAISU.getObj()) );
		//
		cells.add( DefineReport.Select.JUC_KBN.getView());
		cells.add( jad.getJSONText(DefineReport.Select.JUC_KBN.getObj()) );
		//
		cells.add( DefineReport.Select.AGE.getView());
		cells.add( jad.getJSONText(DefineReport.Select.AGE.getObj()) );
		//
		cells.add( DefineReport.Select.AREA.getView());
		cells.add( jad.getJSONText(DefineReport.Select.AREA.getObj()) );

		cells = new ArrayList<String>();
		cells.add( "" );
		cells.add( "" );
		cells.add( DefineReport.Select.BYTAI.getView());
		cells.add( jad.getJSONText(DefineReport.Select.BYTAI.getObj()) );
		cells.add( DefineReport.Select.F_SHOHIN.getView());
		cells.add( jad.getJSONText(DefineReport.Select.F_SHOHIN.getObj()) );
		getWhere().add(cells);

		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);
	}

}
