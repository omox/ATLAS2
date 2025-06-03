package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T01テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T01Dao extends ItemDao {

	/**
	 * 新しいT01Daoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T01Dao(Connection source) {
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

		String szYm 	=	getMap().get("YYYY_MM");	// 期間
		String szType1 	=	getMap().get("TYPE1");		//
		String szType2 	=	getMap().get("TYPE2");		//
		String szKaiin 	=	getMap().get("KAIIN");		// 会員区分
		String szAge 	=	getMap().get("AGE");		// 年齢
		String szArea 	=	getMap().get("AREA");		// 地域
		String szChanel =	getMap().get("HAN_CHAN");	// 購入チャネル
		String szTenpo 	=	getMap().get("TENPO");		// 店舗

		// パラメータ確認
		//if ((szYy == null) || (szNm == null) ) {
		//	return "";
		//}
		System.out.println("YYYY_MM:" +szYm+",TYPE1:"+szType1+",TYPE2:"+szType2+",KAIIN:"+szKaiin+",AGE:"+szAge+",AREA:"+szArea);
		System.out.println("HAN_CHAN:" +szChanel+",TENPO:"+szTenpo);

		// TEST用テーブル（本番リリース時は空文字に変更）
		String testTableSuffix = "";
		// 条件設定
		String sqlWhere = " YYYY_MM ='"+szYm+"' ";

		String sqlTable = " T01_RFM_P1"+testTableSuffix;//販売チャネル通販

		if(StringUtils.equals(szChanel, "2")){			//販売チャネル店舗
			if(StringUtils.equals(szTenpo, "00")){		//販売チャネル店舗・全店舗
				sqlTable = " T01_RFM_P2"+testTableSuffix;
			} else{										//販売チャネル店舗・各店舗
				sqlTable = " T01_RFM_P4"+testTableSuffix;
				sqlWhere += " AND TENPO_CD = '"+szTenpo+"' ";
			}
		}else if (StringUtils.equals(szChanel, "0")){	//販売チャネルすべて
			sqlTable = " T01_RFM_P3"+testTableSuffix;
		}

		String kijunYm = DefineReport.base.TABLE_TP.getVal();	// 基準年月
		if(szYm.compareTo(kijunYm) > 0){	//年月が基準より大きい時
			sqlTable  = sqlTable+ " ";
		} else  {							//年月が基準より小さい時
			//sqlTable  = sqlTable+"_N"+szYm.substring(0, 4);
			sqlTable  = sqlTable+"_"+szYm.substring(0, 4);
		}

		if (StringUtils.equals(szKaiin, DefineReport.OptionType8.OPTION2.getVal())){		//一般会員
			sqlWhere += " AND KAIIN_KBN = '0' ";
		}else if (StringUtils.equals(szKaiin, DefineReport.OptionType8.OPTION3.getVal())){	//特別会員
			sqlWhere += " AND KAIIN_KBN in ('1','2','3','4')";
		}else if (StringUtils.equals(szKaiin, DefineReport.OptionType8.OPTION1.getVal())){	//全体
			if(szYm.compareTo("201500") > 0){			// 2015年以降
				sqlWhere += " AND KAIIN_KBN in ('10','20','30','40','50')";
			} else {									// 2014年以前
				sqlWhere += " AND KAIIN_KBN in ('0','1','2','3','4')";
			}
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION2.getVal())) {//	レギュラー
			sqlWhere += " AND KAIIN_KBN = '10' ";
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION3.getVal())) {//	ゴールド
			sqlWhere += " AND KAIIN_KBN = '20' ";
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION4.getVal())) {//	ゴールド
			sqlWhere += " AND KAIIN_KBN = '30' ";
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION5.getVal())) {//	プラチナ
			sqlWhere += " AND KAIIN_KBN = '40' ";
		} else if (StringUtils.equals(szKaiin, DefineReport.OptionType13.OPTION6.getVal())) {//	プレミアム
			sqlWhere += " AND KAIIN_KBN = '50' ";
		}
		if(StringUtils.equals(szAge, "S01")){  //年齢
			sqlWhere += " AND (AGE >= 12 AND AGE <= 19) ";
		}else if (StringUtils.equals(szAge, "S02")){
			sqlWhere += " AND (AGE >= 20 AND AGE <= 24) ";
		}else if (StringUtils.equals(szAge, "S03")){
			sqlWhere += " AND (AGE >= 25 AND AGE <= 29) ";
		}else if (StringUtils.equals(szAge, "S04")){
			sqlWhere += " AND (AGE >= 30 AND AGE <= 34) ";
		}else if (StringUtils.equals(szAge, "S05")){
			sqlWhere += " AND (AGE >= 35 AND AGE <= 39) ";
		}else if (StringUtils.equals(szAge, "S06")){
			sqlWhere += " AND (AGE >= 40 AND AGE <= 44) ";
		}else if (StringUtils.equals(szAge, "S07")){
			sqlWhere += " AND (AGE >= 45 AND AGE <= 49) ";
		}else if (StringUtils.equals(szAge, "S08")){
			sqlWhere += " AND (AGE >= 50 AND AGE <= 54) ";
		}else if (StringUtils.equals(szAge, "S09")){
			sqlWhere += " AND (AGE >= 55 AND AGE <= 59) ";
		}else if (StringUtils.equals(szAge, "S10")){
			sqlWhere += " AND (AGE >= 60 AND AGE <= 69) ";
		}else if (StringUtils.equals(szAge, "S11")){
			sqlWhere += " AND (AGE >= 70 ) ";
		}else if (StringUtils.equals(szAge, "S12")){
			sqlWhere += " AND (AGE >= 100 OR AGE <= 11) ";
		}
		if(!(StringUtils.equals(szArea, "00"))){  //地域
			sqlWhere += " AND PREF_CD IN ("+szArea+")";
		}

		String[] itemM  = { "AS \"0-0.5万未満\"", "AS \"0.5-1万未満\"", "AS \"1-1.5万未満\"", "AS \"1.5-2万未満\"", "AS \"2-3万未満\"", "AS \"3-4万未満\"", "AS \"4-5万未満\"", "AS \"5-6万未満\"", "AS \"6-10万未満\"", "AS \"10万以上\"" };
		String[] itemM2 = { "AS \"0-0.5万未\"", "AS \"0.5-1万未\"", "AS \"1.0-1.5万未\"", "AS \"1.5-2.0万未\"", "AS \"2.0-2.5万未\"", "AS \"2.5-3.0万未\"", "AS \"3.0-3.5万未\"", "AS \"3.5-4.0万未\"", "AS \"4.0-4.5万未\"", "AS \"4.5-5.0万未\"", "AS \"5.0-5.5万未\"", "AS \"5.5-6.0万未\"", "AS \"6.0-6.5万未\"", "AS \"6.5-7.0万未\"", "AS \"7.0-7.5万未\"", "AS \"7.5-8.0万未\"", "AS \"8.0-8.5万未\"", "AS \"8.5-9.0万未\"", "AS \"9.0-9.5万未\"", "AS \"9.5-10万未\"", "AS \"10万以上\"" };
		String[] itemF  = { "AS \"１回\"", "AS \"２回\"", "AS \"３回\"", "AS \"４回\"", "AS \"５回\"", "AS \"６回\"", "AS \"７回\"", "AS \"８回\"", "AS \"９回\"", "AS \"１０回以上\"" };
		String[] itemF2 = { "AS \"10回\"", "AS \"11回\"", "AS \"12回\"", "AS \"13回\"", "AS \"14回\"", "AS \"15回\"", "AS \"16回\"", "AS \"17回\"", "AS \"18回\"", "AS \"19回\"", "AS \"20回\"", "AS \"21回以上\"" };
		String[] itemR  = { "AS \"当月\"", "AS \"経過１ヶ月\"", "AS \"経過２ヶ月\"", "AS \"経過３ヶ月\"", "AS \"経過４ヶ月\"", "AS \"経過５ヶ月\"", "AS \"経過６ヶ月\"", "AS \"経過７ヶ月\"", "AS \"経過８ヶ月\"", "AS \"経過９ヶ月\"", "AS \"経過１０ヶ月\"", "AS \"経過１１ヶ月\"", "AS \"経過１２ヶ月\"", "AS \"１３-２４ヶ月\"", "AS \"２５ヶ月以上\"" };
		String[] itemR2 = { "AS \"経過１３ヶ月\"", "AS \"経過１４ヶ月\"", "AS \"経過１５ヶ月\"", "AS \"経過１６ヶ月\"", "AS \"経過１７ヶ月\"", "AS \"経過１８ヶ月\"", "AS \"経過１９ヶ月\"", "AS \"経過２０ヶ月\"", "AS \"経過２１ヶ月\"", "AS \"経過２２ヶ月\"", "AS \"経過２３ヶ月\"", "AS \"経過２４ヶ月\"", "AS \"２５ヶ月以上\"" };
		String sqlItem0 = " ";
		String sqlSum1 = "  ";
		String sqlSum2 = "  ";
		String sqlSum2G = "  ";
		String sqlSumFR = "  ";
		String sqlItem1 = "  ";
		String sqlItem1G = "  ";
		String sqlItem2G = "  ";
		String sqlGroup = "  ";
		String sqlSORT2 = " 'Z999' ";
		String sqlItem2 = "  ";
		String sqlTabel2 = "  ";
		if(StringUtils.equals(szType1, "1")){  //R x F
			sqlSum1  = "case when JUCKAISU = 1 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 2 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 3 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 4 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 5 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCKAISU = 6 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 7 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 8 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 9 then sum(KYAKUSU) else 0 end,case when JUCKAISU >= 10 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) ";
			sqlItem1G = " ,work2(JUCHUMR_G,JUCHUMR_G_NM,SU1, SU2, SU3, SU4, SU5, SU6, SU7, SU8, SU9, SU10, SU_ALL) as ( ";
			sqlSum2   = "select  JUCHUMR_G,JUCHUMR_G_NM ,sum(SU1),sum(SU2),sum(SU3),sum(SU4),sum(SU5),sum(SU6),sum(SU7),sum(SU8),sum(SU9),sum(SU10),sum(SU_ALL) ";
			sqlSumFR   = ") select  JUCHUMR_G as SORT , JUCHUMR_G_NM as \"最新受注月数\",(SU1) "+itemF[0]+", (SU2) "+itemF[1]+", (SU3) "+itemF[2]+", (SU4) "+itemF[3]+",(SU5) "+itemF[4]+", ";
			sqlSumFR  += "(SU6) "+itemF[5]+", (SU7) "+itemF[6]+", (SU8) "+itemF[7]+", (SU9) "+itemF[8]+",(SU10) "+itemF[9]+", (SU_ALL) as \"回数計\",sum(SU_ALL) over ( order by JUCHUMR_G) as \"回数累計\" ";
			sqlSumFR  += " from work2 ";
			sqlSum2G  = "sum(SU1) "+itemF[0]+", sum(SU2) "+itemF[1]+", sum(SU3) "+itemF[2]+", sum(SU4) "+itemF[3]+",sum(SU5) "+itemF[4]+", ";
			sqlSum2G += "sum(SU6) "+itemF[5]+", sum(SU7) "+itemF[6]+", sum(SU8) "+itemF[7]+", sum(SU9) "+itemF[8]+",sum(SU10) "+itemF[9]+", sum(SU_ALL) as \"回数計\" , null as \"回数累計\" ";
			sqlItem1  = " JUCHUMR ";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUMR_G,JUCHUMR_G_NM ";
			sqlItem2  = " JUCKAISU ";
			sqlTabel2  = " M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
			if(StringUtils.equals(szType2, "2")){  //R x F 2ヶ月展開
				sqlSum1  = "case when JUCKAISU = 1 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 2 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 3 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 4 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 6 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 7 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 8 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 9 then sum(KYAKUSU) else 0 end,case when JUCKAISU >= 10 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) ";
				sqlItem1G = " ,work2(JUCHUMR_G,JUCHUMR_G_NM,SU1, SU2, SU3, SU4, SU5, SU6, SU7, SU8, SU9, SU10, SU_ALL) as ( ";
				sqlSum2   = "select  JUCHUMR_2,JUCHUMR_2_NM ,sum(SU1),sum(SU2),sum(SU3),sum(SU4),sum(SU5),sum(SU6),sum(SU7),sum(SU8),sum(SU9),sum(SU10),sum(SU_ALL) ";
				sqlSumFR   = ") select  JUCHUMR_G as SORT , JUCHUMR_G_NM as \"最新受注月数\",(SU1) "+itemF[0]+", (SU2) "+itemF[1]+", (SU3) "+itemF[2]+", (SU4) "+itemF[3]+",(SU5) "+itemF[4]+", ";
				sqlSumFR  += "(SU6) "+itemF[5]+", (SU7) "+itemF[6]+", (SU8) "+itemF[7]+", (SU9) "+itemF[8]+",(SU10) "+itemF[9]+", (SU_ALL) as \"回数計\",sum(SU_ALL) over ( order by JUCHUMR_G) as \"回数累計\" ";
				sqlSumFR  += " from work2 ";
				sqlSum2G  = "sum(SU1) "+itemF[0]+", sum(SU2) "+itemF[1]+", sum(SU3) "+itemF[2]+", sum(SU4) "+itemF[3]+",sum(SU5) "+itemF[4]+", ";
				sqlSum2G += "sum(SU6) "+itemF[5]+", sum(SU7) "+itemF[6]+", sum(SU8) "+itemF[7]+", sum(SU9) "+itemF[8]+",sum(SU10) "+itemF[9]+", sum(SU_ALL) as \"回数計\" , null as \"回数累計\" ";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR_2,JUCHUMR_2_NM ";
			}else if(StringUtils.equals(szType2, "6")){  //R x F Rのみ展開
				sqlSum1  = "case when JUCKAISU = 1 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 2 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 3 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 4 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 6 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 7 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 8 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 9 then sum(KYAKUSU) else 0 end,case when JUCKAISU >= 10 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) ";
				sqlItem1G = " ,work2(JUCHUMR,JUCHUMR_NM,SU1, SU2, SU3, SU4, SU5, SU6, SU7, SU8, SU9, SU10, SU_ALL) as ( ";
				sqlSum2   = "select  JUCHUMR,JUCHUMR_NM ,sum(SU1),sum(SU2),sum(SU3),sum(SU4),sum(SU5),sum(SU6),sum(SU7),sum(SU8),sum(SU9),sum(SU10),sum(SU_ALL) ";
				sqlSumFR   = ") select  JUCHUMR as SORT , JUCHUMR_NM as \"最新受注月数\",(SU1) "+itemF[0]+", (SU2) "+itemF[1]+", (SU3) "+itemF[2]+", (SU4) "+itemF[3]+",(SU5) "+itemF[4]+", ";
				sqlSumFR  += "(SU6) "+itemF[5]+", (SU7) "+itemF[6]+", (SU8) "+itemF[7]+", (SU9) "+itemF[8]+",(SU10) "+itemF[9]+", (SU_ALL) as \"回数計\",sum(SU_ALL) over ( order by JUCHUMR) as \"回数累計\" ";
				sqlSumFR  += " from work2 ";
				sqlSum2G  = "sum(SU1) "+itemF[0]+", sum(SU2) "+itemF[1]+", sum(SU3) "+itemF[2]+", sum(SU4) "+itemF[3]+",sum(SU5) "+itemF[4]+", ";
				sqlSum2G += "sum(SU6) "+itemF[5]+", sum(SU7) "+itemF[6]+", sum(SU8) "+itemF[7]+", sum(SU9) "+itemF[8]+",sum(SU10) "+itemF[9]+", sum(SU_ALL) as \"回数計\" , null as \"回数累計\" ";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR,JUCHUMR_NM ";
				sqlSORT2 = "9999" ;
			}else if(StringUtils.equals(szType2, "3")){  //R x F 全展開
				sqlSum1  = "case when JUCKAISU = 1 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 2 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 3 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 4 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 6 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 7 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 8 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 9 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 10 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCKAISU = 11 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 12 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 13 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 14 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 15 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 16 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 17 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 18 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 19 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 20 then sum(KYAKUSU) else 0 end ,case when JUCKAISU = 21 then sum(KYAKUSU) else 0 end, sum(KYAKUSU) ";
				sqlItem1G = ",work2(JUCHUMR,JUCHUMR_NM,SU1, SU2, SU3, SU4, SU5, SU6, SU7, SU8, SU9, SU10,SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21, SU_ALL) as ( " ;
				sqlSum2   = "select  JUCHUMR,JUCHUMR_NM ,sum(SU1),sum(SU2),sum(SU3),sum(SU4),sum(SU5),sum(SU6),sum(SU7),sum(SU8),sum(SU9),sum(SU10),sum(SU11),sum(SU12),sum(SU13),sum(SU14),sum(SU15),sum(SU16),sum(SU17),sum(SU18),sum(SU19),sum(SU20),sum(SU21),sum(SU_ALL) ";
				sqlSum2G  = "sum(SU1) "+itemF[0]+", sum(SU2) "+itemF[1]+", sum(SU3) "+itemF[2]+", sum(SU4) "+itemF[3]+",sum(SU5) "+itemF[4]+", ";
				sqlSum2G += "sum(SU6) "+itemF[5]+", sum(SU7) "+itemF[6]+", sum(SU8) "+itemF[7]+", sum(SU9) "+itemF[8]+",sum(SU10) "+itemF2[0]+", ";
				sqlSum2G += "sum(SU11) "+itemF2[1]+", sum(SU12) "+itemF2[2]+", sum(SU13) "+itemF2[3]+", sum(SU14) "+itemF2[4]+",sum(SU15) "+itemF2[5]+", ";
				sqlSum2G += "sum(SU16) "+itemF2[6]+", sum(SU17) "+itemF2[7]+", sum(SU18) "+itemF2[8]+", sum(SU19) "+itemF2[9]+",sum(SU20) "+itemF2[10]+",sum(SU21) "+itemF2[11]+", sum(SU_ALL) as \"回数計\" , null as \"回数累計\" ";
				sqlSumFR  = ") select JUCHUMR as SORT , JUCHUMR_NM as \"最新受注月数\",(SU1) "+itemF[0]+", (SU2) "+itemF[1]+", (SU3) "+itemF[2]+", (SU4) "+itemF[3]+",(SU5) "+itemF[4]+", ";
				sqlSumFR  += "(SU6) "+itemF[5]+", (SU7) "+itemF[6]+", (SU8) "+itemF[7]+", (SU9) "+itemF[8]+", (SU10) "+itemF2[0]+",(SU11) "+itemF2[1]+",(SU12) "+itemF2[2]+",(SU13) "+itemF2[3]+",(SU14) "+itemF2[4]+",(SU15) "+itemF2[5]+",(SU16) "+itemF2[6]+",(SU17) "+itemF2[7]+",(SU18) "+itemF2[8]+",(SU19) "+itemF2[9]+",(SU20) "+itemF2[10]+",(SU21) "+itemF2[11]+", ";
				sqlSumFR  += "(SU_ALL) as \"回数計\",sum(SU_ALL) over ( order by JUCHUMR) as \"回数累計\" ";
				sqlSumFR  += " from work2 ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR,JUCHUMR_NM ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "2")){  //R x M
			sqlSum1  = "case when LKONYU between -1 and 0 then sum(KYAKUSU) else 0 end , case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end , case when LKONYU = 3 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 4 and 5 then sum(KYAKUSU) else 0 end , case when LKONYU between 6 and 7 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 8 and 9 then sum(KYAKUSU) else 0 end , case when LKONYU between 10 and 11 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 12 and 19 then sum(KYAKUSU) else 0 end , case when LKONYU = 20 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) ";
			sqlSum2  = "sum(SU1) "+itemM[0]+", sum(SU2) "+itemM[1]+", sum(SU3) "+itemM[2]+", sum(SU4) "+itemM[3]+", ";
			sqlSum2 += "sum(SU5) "+itemM[4]+", sum(SU6) "+itemM[5]+", sum(SU7) "+itemM[6]+", ";
			sqlSum2 += "sum(SU8) "+itemM[7]+", sum(SU9) "+itemM[8]+", sum(SU10) "+itemM[9]+", sum(SU_ALL) as \"受注金額計\" ";
			sqlItem1  = " JUCHUMR ";
			sqlItem1G = " select JUCHUMR_G as SORT , JUCHUMR_G_NM as \"最新受注月数\" ,";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUMR_G,JUCHUMR_G_NM ";
			sqlItem2  = " LKONYU ";
			sqlTabel2  = " M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
			if(StringUtils.equals(szType2, "2")){  //R x M 2ヶ月展開
				sqlSum1  = "case when LKONYU between -1 and 0 then sum(KYAKUSU) else 0 end , case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end , case when LKONYU = 3 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU between 4 and 5 then sum(KYAKUSU) else 0 end , case when LKONYU between 6 and 7 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU between 8 and 9 then sum(KYAKUSU) else 0 end , case when LKONYU between 10 and 11 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU between 12 and 19 then sum(KYAKUSU) else 0 end , case when LKONYU = 20 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) ";
				sqlSum2  = "sum(SU1) "+itemM[0]+", sum(SU2) "+itemM[1]+", sum(SU3) "+itemM[2]+", sum(SU4) "+itemM[3]+", ";
				sqlSum2 += "sum(SU5) "+itemM[4]+", sum(SU6) "+itemM[5]+", sum(SU7) "+itemM[6]+", ";
				sqlSum2 += "sum(SU8) "+itemM[7]+", sum(SU9) "+itemM[8]+", sum(SU10) "+itemM[9]+", sum(SU_ALL) as \"受注金額計\" ";
				sqlItem1  = " JUCHUMR ";
				sqlItem1G = " select JUCHUMR_2 as SORT , JUCHUMR_2_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR_2,JUCHUMR_2_NM ";
				sqlItem2  = " LKONYU ";
				sqlTabel2  = " M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
			}else if((StringUtils.equals(szType2, "3"))||(StringUtils.equals(szType2, "6"))){  //R x M 展開
				sqlSum1  = "case when LKONYU between -1 and 0 then sum(KYAKUSU) else 0 end , case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end,case when LKONYU = 3 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 4 then sum(KYAKUSU) else 0 end,case when LKONYU = 5 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 6 then sum(KYAKUSU) else 0 end,case when LKONYU = 7 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 8 then sum(KYAKUSU) else 0 end,case when LKONYU = 9 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 10 then sum(KYAKUSU) else 0 end,case when LKONYU = 11 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 12 then sum(KYAKUSU) else 0 end,case when LKONYU = 13 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 14 then sum(KYAKUSU) else 0 end,case when LKONYU = 15 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 16 then sum(KYAKUSU) else 0 end,case when LKONYU = 17 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 18 then sum(KYAKUSU) else 0 end,case when LKONYU = 19 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 20 then sum(KYAKUSU) else 0 end,sum(KYAKUSU) ";
				sqlSum2  = "sum(SU1) "+itemM2[0]+", sum(SU2) "+itemM2[1]+", sum(SU3) "+itemM2[2]+", sum(SU4) "+itemM2[3]+", ";
				sqlSum2 += "sum(SU5) "+itemM2[4]+", sum(SU6) "+itemM2[5]+", sum(SU7) "+itemM2[6]+", ";
				sqlSum2 += "sum(SU8) "+itemM2[7]+", sum(SU9) "+itemM2[8]+", sum(SU10) "+itemM2[9]+", ";
				sqlSum2 += "sum(SU11) "+itemM2[10]+", sum(SU12) "+itemM2[11]+", sum(SU13) "+itemM2[12]+", ";
				sqlSum2 += "sum(SU14) "+itemM2[13]+", sum(SU15) "+itemM2[14]+", sum(SU16) "+itemM2[15]+", ";
				sqlSum2 += "sum(SU17) "+itemM2[16]+", sum(SU18) "+itemM2[17]+", sum(SU19) "+itemM2[18]+",sum(SU20) "+itemM2[19]+",  ";
				sqlSum2 += "sum(SU21) "+itemM2[20]+", sum(SU_ALL) as \"受注金額計\" ";
				sqlItem1  = " JUCHUMR ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,";
				sqlItem1G = " select JUCHUMR as SORT , JUCHUMR_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR,JUCHUMR_NM ";
				sqlItem2  = " LKONYU ";
				sqlTabel2  = " M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
				sqlSORT2 = "9999" ;
			}
			sqlSum2G = sqlSum2;
		}else if (StringUtils.equals(szType1, "3")){  //F x M
			sqlSum1  = "case when LKONYU between -1 and 0 then sum(KYAKUSU) else 0 end , case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end , case when LKONYU = 3 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 4 and 5 then sum(KYAKUSU) else 0 end , case when LKONYU between 6 and 7 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 8 and 9 then sum(KYAKUSU) else 0 end , case when LKONYU between 10 and 11 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 12 and 19 then sum(KYAKUSU) else 0 end , case when LKONYU = 20 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) ";
			sqlSum2  = "sum(SU1) "+itemM[0]+", sum(SU2) "+itemM[1]+", sum(SU3) "+itemM[2]+", sum(SU4) "+itemM[3]+", ";
			sqlSum2 += "sum(SU5) "+itemM[4]+", sum(SU6) "+itemM[5]+", sum(SU7) "+itemM[6]+", ";
			sqlSum2 += "sum(SU8) "+itemM[7]+", sum(SU9) "+itemM[8]+", sum(SU10) "+itemM[9]+", sum(SU_ALL) as \"受注金額計\" ";
			sqlItem1  = " JUCKAISU ";
			sqlItem1G = " select FRANK_G_CD as SORT , FRANK_G_NM as \"回数\" ,";
			sqlItem2G = " '回数計' as \"回数\" , ";
			sqlGroup = " FRANK_G_CD,FRANK_G_NM ";
			sqlItem2  = " LKONYU ";
			sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
			if(StringUtils.equals(szType2, "6")){  //F x M 展開  Rのみ展開
				sqlSum1  = "case when LKONYU between -1 and 0 then sum(KYAKUSU) else 0 end , case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end,case when LKONYU = 3 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 4 then sum(KYAKUSU) else 0 end,case when LKONYU = 5 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 6 then sum(KYAKUSU) else 0 end,case when LKONYU = 7 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 8 then sum(KYAKUSU) else 0 end,case when LKONYU = 9 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 10 then sum(KYAKUSU) else 0 end,case when LKONYU = 11 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 12 then sum(KYAKUSU) else 0 end,case when LKONYU = 13 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 14 then sum(KYAKUSU) else 0 end,case when LKONYU = 15 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 16 then sum(KYAKUSU) else 0 end,case when LKONYU = 17 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 18 then sum(KYAKUSU) else 0 end,case when LKONYU = 19 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 20 then sum(KYAKUSU) else 0 end,sum(KYAKUSU) ";
				sqlSum2  = "sum(SU1) "+itemM2[0]+", sum(SU2) "+itemM2[1]+", sum(SU3) "+itemM2[2]+", sum(SU4) "+itemM2[3]+", ";
				sqlSum2 += "sum(SU5) "+itemM2[4]+", sum(SU6) "+itemM2[5]+", sum(SU7) "+itemM2[6]+", ";
				sqlSum2 += "sum(SU8) "+itemM2[7]+", sum(SU9) "+itemM2[8]+", sum(SU10) "+itemM2[9]+", ";
				sqlSum2 += "sum(SU11) "+itemM2[10]+", sum(SU12) "+itemM2[11]+", sum(SU13) "+itemM2[12]+", ";
				sqlSum2 += "sum(SU14) "+itemM2[13]+", sum(SU15) "+itemM2[14]+", sum(SU16) "+itemM2[15]+", ";
				sqlSum2 += "sum(SU17) "+itemM2[16]+", sum(SU18) "+itemM2[17]+", sum(SU19) "+itemM2[18]+",sum(SU20) "+itemM2[19]+",  ";
				sqlSum2 += "sum(SU21) "+itemM2[20]+", sum(SU_ALL) as \"受注金額計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,";
				sqlItem1  = " JUCKAISU ";
				sqlItem1G = " select FRANK_G_CD as SORT , FRANK_G_NM as \"回数\" ,";
				sqlItem2G = " '回数計' as \"回数\" , ";
				sqlGroup = " FRANK_G_CD,FRANK_G_NM ";
				sqlItem2  = " LKONYU ";
				sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
			}else if(StringUtils.equals(szType2, "3")){  //F x M 展開
				sqlSum1  = "case when LKONYU between -1 and 0 then sum(KYAKUSU) else 0 end , case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end,case when LKONYU = 3 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 4 then sum(KYAKUSU) else 0 end,case when LKONYU = 5 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 6 then sum(KYAKUSU) else 0 end,case when LKONYU = 7 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 8 then sum(KYAKUSU) else 0 end,case when LKONYU = 9 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 10 then sum(KYAKUSU) else 0 end,case when LKONYU = 11 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 12 then sum(KYAKUSU) else 0 end,case when LKONYU = 13 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 14 then sum(KYAKUSU) else 0 end,case when LKONYU = 15 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 16 then sum(KYAKUSU) else 0 end,case when LKONYU = 17 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 18 then sum(KYAKUSU) else 0 end,case when LKONYU = 19 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 20 then sum(KYAKUSU) else 0 end,sum(KYAKUSU) ";
				sqlSum2  = "sum(SU1) "+itemM2[0]+", sum(SU2) "+itemM2[1]+", sum(SU3) "+itemM2[2]+", sum(SU4) "+itemM2[3]+", ";
				sqlSum2 += "sum(SU5) "+itemM2[4]+", sum(SU6) "+itemM2[5]+", sum(SU7) "+itemM2[6]+", ";
				sqlSum2 += "sum(SU8) "+itemM2[7]+", sum(SU9) "+itemM2[8]+", sum(SU10) "+itemM2[9]+", ";
				sqlSum2 += "sum(SU11) "+itemM2[10]+", sum(SU12) "+itemM2[11]+", sum(SU13) "+itemM2[12]+", ";
				sqlSum2 += "sum(SU14) "+itemM2[13]+", sum(SU15) "+itemM2[14]+", sum(SU16) "+itemM2[15]+", ";
				sqlSum2 += "sum(SU17) "+itemM2[16]+", sum(SU18) "+itemM2[17]+", sum(SU19) "+itemM2[18]+",sum(SU20) "+itemM2[19]+",  ";
				sqlSum2 += "sum(SU21) "+itemM2[20]+", sum(SU_ALL) as \"受注金額計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,";
				sqlItem1  = " JUCKAISU ";
				sqlItem1G = " select FRANK as SORT , FRANK_NM as \"回数\" ,";
				sqlItem2G = " '回数計' as \"回数\" , ";
				sqlGroup = " FRANK,FRANK_NM ";
				sqlItem2  = " LKONYU ";
				sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
				sqlSORT2 = "9999" ;
			}
			sqlSum2G = sqlSum2;
		}else if (StringUtils.equals(szType1, "4")){  //R x 初回受注月
			sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF between 14 and 25 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
			sqlSum2  = "sum(SU1) "+itemR[0]+", sum(SU2) "+itemR[1]+", sum(SU3) "+itemR[2]+", sum(SU4) "+itemR[3]+", sum(SU5) "+itemR[4]+", sum(SU6) "+itemR[5]+", ";
			sqlSum2 += "sum(SU7) "+itemR[6]+", sum(SU8) "+itemR[7]+", sum(SU9) "+itemR[8]+", sum(SU10) "+itemR[9]+", sum(SU11) "+itemR[10]+", ";
			sqlSum2 += "sum(SU12) "+itemR[11]+", sum(SU13) "+itemR[12]+", sum(SU14) "+itemR[13]+", sum(SU15) "+itemR[14]+", sum(SU_ALL) as \"初回受注計\" ";
			sqlItem0  = " SU11, SU12, SU13, SU14, SU15,";
			sqlItem1  = " JUCHUMR ";
			sqlItem1G = " select JUCHUMR_G as SORT , JUCHUMR_G_NM as \"最新受注月数\" ,";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUMR_G,JUCHUMR_G_NM ";
			sqlItem2  = " JUCHUMF ";
			sqlTabel2  = " M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
			if(StringUtils.equals(szType2, "2")){  //R x 初回受注月 2ヶ月展開
				sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF between 14 and 25 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
				sqlSum2  = "sum(SU1) "+itemR[0]+", sum(SU2) "+itemR[1]+", sum(SU3) "+itemR[2]+", sum(SU4) "+itemR[3]+", sum(SU5) "+itemR[4]+", sum(SU6) "+itemR[5]+", ";
				sqlSum2 += "sum(SU7) "+itemR[6]+", sum(SU8) "+itemR[7]+", sum(SU9) "+itemR[8]+", sum(SU10) "+itemR[9]+", sum(SU11) "+itemR[10]+", ";
				sqlSum2 += "sum(SU12) "+itemR[11]+", sum(SU13) "+itemR[12]+", sum(SU14) "+itemR[13]+", sum(SU15) "+itemR[14]+", sum(SU_ALL) as \"初回受注計\" ";
				sqlItem0  = " SU11, SU12, SU13, SU14, SU15,";
				sqlItem1  = " JUCHUMR ";
				sqlItem1G = " select JUCHUMR_2 as SORT , JUCHUMR_2_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR_2,JUCHUMR_2_NM ";
				sqlItem2  = " JUCHUMF ";
				sqlTabel2  = " M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
			}else if((StringUtils.equals(szType2, "3"))||(StringUtils.equals(szType2, "6"))){  //R x 初回受注月 展開
				sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 14 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 15 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 16 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 17 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 18 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 19 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 20 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 21 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 22 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 23 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 24 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 25 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
				sqlSum2  = "sum(SU1) "+itemR[0]+", sum(SU2) "+itemR[1]+", sum(SU3) "+itemR[2]+", sum(SU4) "+itemR[3]+", sum(SU5) "+itemR[4]+", sum(SU6) "+itemR[5]+", ";
				sqlSum2 += "sum(SU7) "+itemR[6]+", sum(SU8) "+itemR[7]+", sum(SU9) "+itemR[8]+", sum(SU10) "+itemR[9]+", sum(SU11) "+itemR[10]+", sum(SU12) "+itemR[11]+", ";
				sqlSum2 += "sum(SU13)"+itemR[12]+", sum(SU14) "+itemR2[0]+", sum(SU15) "+itemR2[1]+" ,sum(SU16) "+itemR2[2]+" ,sum(SU17) "+itemR2[3]+" ,sum(SU18) "+itemR2[4]+" ,sum(SU19) "+itemR2[5]+" ,";
				sqlSum2 += "sum(SU20) "+itemR2[6]+" ,sum(SU21) "+itemR2[7]+" ,sum(SU22) "+itemR2[8]+" ,sum(SU23) "+itemR2[9]+" ,sum(SU24) "+itemR2[10]+" ,sum(SU25) "+itemR2[11]+" ,sum(SU26) "+itemR2[12]+" , sum(SU_ALL) as \"初回受注計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,SU24,SU25,SU26,";
				sqlItem1  = " JUCHUMR ";
				sqlItem1G = " select JUCHUMR as SORT , JUCHUMR_NM as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUMR,JUCHUMR_NM ";
				sqlItem2  = " JUCHUMF ";
				sqlTabel2  = " M_JUCHU_R_CNT on ITEN_T = JUCHUMR ";
				sqlSORT2 = "9999" ;
			}
			sqlSum2G = sqlSum2;
		}else if (StringUtils.equals(szType1, "5")){  //F x 初回受注月
			sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF between 14 and 25 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
			sqlSum2  = "sum(SU1) "+itemR[0]+", sum(SU2) "+itemR[1]+", sum(SU3) "+itemR[2]+", sum(SU4) "+itemR[3]+", sum(SU5) "+itemR[4]+", sum(SU6) "+itemR[5]+", ";
			sqlSum2 += "sum(SU7) "+itemR[6]+", sum(SU8) "+itemR[7]+", sum(SU9) "+itemR[8]+", sum(SU10) "+itemR[9]+", sum(SU11) "+itemR[10]+", ";
			sqlSum2 += "sum(SU12) "+itemR[11]+", sum(SU13) "+itemR[12]+", sum(SU14) "+itemR[13]+", sum(SU15) "+itemR[14]+", sum(SU_ALL) as \"初回受注計\" ";
			sqlItem0  = " SU11, SU12, SU13, SU14, SU15,";
			sqlItem1  = " JUCKAISU ";
			sqlItem1G = " select FRANK_G_CD as SORT , FRANK_G_NM as \"回数\" ,";
			sqlItem2G = " '回数計' as \"回数\" , ";
			sqlGroup = " FRANK_G_CD,FRANK_G_NM ";
			sqlItem2  = " JUCHUMF ";
			sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
			if(StringUtils.equals(szType2, "6")){  //F x 初回受注月 受注月のみ展開
				sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 14 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 15 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 16 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 17 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 18 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 19 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 20 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 21 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 22 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 23 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 24 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 25 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
				sqlSum2  = "sum(SU1) "+itemR[0]+", sum(SU2) "+itemR[1]+", sum(SU3) "+itemR[2]+", sum(SU4) "+itemR[3]+", sum(SU5) "+itemR[4]+", sum(SU6) "+itemR[5]+", ";
				sqlSum2 += "sum(SU7) "+itemR[6]+", sum(SU8) "+itemR[7]+", sum(SU9) "+itemR[8]+", sum(SU10) "+itemR[9]+", sum(SU11) "+itemR[10]+", sum(SU12) "+itemR[11]+", ";
				sqlSum2 += "sum(SU13)"+itemR[12]+", sum(SU14) "+itemR2[0]+", sum(SU15) "+itemR2[1]+" ,sum(SU16) "+itemR2[2]+" ,sum(SU17) "+itemR2[3]+" ,sum(SU18) "+itemR2[4]+" ,sum(SU19) "+itemR2[5]+" ,";
				sqlSum2 += "sum(SU20) "+itemR2[6]+" ,sum(SU21) "+itemR2[7]+" ,sum(SU22) "+itemR2[8]+" ,sum(SU23) "+itemR2[9]+" ,sum(SU24) "+itemR2[10]+" ,sum(SU25) "+itemR2[11]+" ,sum(SU26) "+itemR2[12]+" , sum(SU_ALL) as \"初回受注計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,SU24,SU25,SU26,";
				sqlItem1  = " JUCKAISU ";
				sqlItem1G = " select FRANK_G_CD as SORT , FRANK_G_NM as \"回数\" ,";
				sqlItem2G = " '回数計' as \"回数\" , ";
				sqlGroup = " FRANK_G_CD,FRANK_G_NM ";
				sqlItem2  = " JUCHUMF ";
				sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
			}else if (StringUtils.equals(szType2, "3")){  //F x 初回受注月 展開
				sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 14 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 15 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 16 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 17 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 18 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 19 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 20 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 21 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 22 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 23 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 24 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 25 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
				sqlSum2  = "sum(SU1) "+itemR[0]+", sum(SU2) "+itemR[1]+", sum(SU3) "+itemR[2]+", sum(SU4) "+itemR[3]+", sum(SU5) "+itemR[4]+", sum(SU6) "+itemR[5]+", ";
				sqlSum2 += "sum(SU7) "+itemR[6]+", sum(SU8) "+itemR[7]+", sum(SU9) "+itemR[8]+", sum(SU10) "+itemR[9]+", sum(SU11) "+itemR[10]+", sum(SU12) "+itemR[11]+", ";
				sqlSum2 += "sum(SU13)"+itemR[12]+", sum(SU14) "+itemR2[0]+", sum(SU15) "+itemR2[1]+" ,sum(SU16) "+itemR2[2]+" ,sum(SU17) "+itemR2[3]+" ,sum(SU18) "+itemR2[4]+" ,sum(SU19) "+itemR2[5]+" ,";
				sqlSum2 += "sum(SU20) "+itemR2[6]+" ,sum(SU21) "+itemR2[7]+" ,sum(SU22) "+itemR2[8]+" ,sum(SU23) "+itemR2[9]+" ,sum(SU24) "+itemR2[10]+" ,sum(SU25) "+itemR2[11]+" ,sum(SU26) "+itemR2[12]+" , sum(SU_ALL) as \"初回受注計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,SU24,SU25,SU26,";
				sqlItem1  = " JUCKAISU ";
				sqlItem1G = " select FRANK as SORT , FRANK_NM as \"回数\" ,";
				sqlItem2G = " '回数計' as \"回数\" , ";
				sqlGroup = " FRANK,FRANK_NM ";
				sqlItem2  = " JUCHUMF ";
				sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
				sqlSORT2 = "9999" ;
			}
			sqlSum2G = sqlSum2;
		}else if (StringUtils.equals(szType1, "6")){  //M x 初回受注月
			sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF between 14 and 25 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
			sqlSum2  = "sum(SU1) "+itemR[0]+", sum(SU2) "+itemR[1]+", sum(SU3) "+itemR[2]+", sum(SU4) "+itemR[3]+", sum(SU5) "+itemR[4]+", sum(SU6) "+itemR[5]+", ";
			sqlSum2 += "sum(SU7) "+itemR[6]+", sum(SU8) "+itemR[7]+", sum(SU9) "+itemR[8]+", sum(SU10) "+itemR[9]+", sum(SU11) "+itemR[10]+", ";
			sqlSum2 += "sum(SU12) "+itemR[11]+", sum(SU13) "+itemR[12]+", sum(SU14) "+itemR[13]+", sum(SU15) "+itemR[14]+", sum(SU_ALL) as \"初回受注計\" ";
			sqlItem0  = " SU11, SU12, SU13, SU14, SU15,";
			sqlItem1  = " LKONYU ";
			sqlItem1G = " select MRANK_G_CD as SORT , MRANK_G_NM as \"受注金額(万円)\" ,";
			sqlItem2G = " '受注金額計' as \"受注金額(万円)\" , ";
			sqlGroup = " MRANK_G_CD , MRANK_G_NM ";
			sqlItem2  = " JUCHUMF ";
			sqlTabel2  = " M_MRANK on ITEN_T = MRANK ";
			if((StringUtils.equals(szType2, "3"))||(StringUtils.equals(szType2, "6"))){  //Mx 初回受注月 展開
				sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 14 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 15 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 16 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 17 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 18 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 19 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 20 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 21 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 22 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 23 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 24 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 25 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
				sqlSum2  = "sum(SU1) "+itemR[0]+", sum(SU2) "+itemR[1]+", sum(SU3) "+itemR[2]+", sum(SU4) "+itemR[3]+", sum(SU5) "+itemR[4]+", sum(SU6) "+itemR[5]+", ";
				sqlSum2 += "sum(SU7) "+itemR[6]+", sum(SU8) "+itemR[7]+", sum(SU9) "+itemR[8]+", sum(SU10) "+itemR[9]+", sum(SU11) "+itemR[10]+", sum(SU12) "+itemR[11]+", ";
				sqlSum2 += "sum(SU13)"+itemR[12]+", sum(SU14) "+itemR2[0]+", sum(SU15) "+itemR2[1]+" ,sum(SU16) "+itemR2[2]+" ,sum(SU17) "+itemR2[3]+" ,sum(SU18) "+itemR2[4]+" ,sum(SU19) "+itemR2[5]+" ,";
				sqlSum2 += "sum(SU20) "+itemR2[6]+" ,sum(SU21) "+itemR2[7]+" ,sum(SU22) "+itemR2[8]+" ,sum(SU23) "+itemR2[9]+" ,sum(SU24) "+itemR2[10]+" ,sum(SU25) "+itemR2[11]+" ,sum(SU26) "+itemR2[12]+" , sum(SU_ALL) as \"初回受注計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,SU24,SU25,SU26,";
				sqlItem1  = " LKONYU ";
				sqlItem1G = " select MRANK as SORT , MRANK_NM as \"受注金額(万円)\" ,";
				sqlItem2G = " '受注金額計' as \"受注金額(万円)\" , ";
				sqlGroup = " MRANK , MRANK_NM ";
				sqlItem2  = " JUCHUMF ";
				sqlTabel2  = " M_MRANK on ITEN_T = MRANK ";
				sqlSORT2 = "9999" ;
			}
			sqlSum2G = sqlSum2;
		}
		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("with work(ITEN_T, SU1, SU2, SU3, SU4, SU5, SU6, SU7, SU8, SU9, SU10,"+sqlItem0+"SU_ALL) as (");
		sbSQL.append(" select "+sqlItem1+", ");
		sbSQL.append(sqlSum1);
		sbSQL.append("from "+sqlTable+" T01 ");
		sbSQL.append(" where "+sqlWhere+" group by "+sqlItem1+" , "+sqlItem2+" ) ");
		sbSQL.append(sqlItem1G );
		sbSQL.append(sqlSum2);
		sbSQL.append("from work left outer join "+sqlTabel2 );
		sbSQL.append("group by "+sqlGroup );
		sbSQL.append(sqlSumFR);
		sbSQL.append("union all ");
		sbSQL.append("select "+sqlSORT2+" as SORT,"+sqlItem2G );
		sbSQL.append(sqlSum2G);
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

		cells = new ArrayList<String>();
		// 基準年月
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.YM_KIJUN.getView());
		cells.add( jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()) );
		getWhere().add(cells);

		// 表示形式
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.S_TYPE2.getView());
		cells.add( jad.getJSONText(DefineReport.Select.S_TYPE2.getObj()) );
		getWhere().add(cells);

		// 年齢
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.AGE.getView());
		cells.add( jad.getJSONText(DefineReport.Select.AGE.getObj()) );
		getWhere().add(cells);

		// 都道府県
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.AREA.getView());
		cells.add( jad.getJSONText(DefineReport.Select.AREA.getObj()) );
		getWhere().add(cells);

		// 購入チャネル
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.CHANEL.getView());
		cells.add( jad.getJSONText(DefineReport.Select.CHANEL.getObj()) );
		getWhere().add(cells);

		// 店舗
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.TENPO.getView());
		cells.add( jad.getJSONText(DefineReport.Select.TENPO.getObj()) );
		getWhere().add(cells);

		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);

	}

}
