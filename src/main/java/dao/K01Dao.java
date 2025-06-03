package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * K01テーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class K01Dao extends ItemDao {

	/**
	 * 新しいK01Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public K01Dao(Connection source) {
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

		String szYm = getMap().get("YYYY_MM");		// 年月(韓国)
		String szType1 = getMap().get("TYPE1");		// 表示形式-1:R x F 2:R x M 3:F x M 4:R x 初回受注月 5:F x 初回受注月 6:M x 初回受注月
		String szType2 = getMap().get("TYPE2");		// 展開表示-1:基本 R ３ヵ月単位 2:R ２ヵ月単位 3:全展開
		String szKaiin = getMap().get("KAIIN_KBN");	// 会員区分(韓国)
		String szAge = getMap().get("AGE");			// 年齢(韓国)
		String szArea = getMap().get("AREA");		// 地域(韓国)
		String szChanel = getMap().get("HAN_CHAN");	// 販売チャネル(韓国)

		int inYm = Integer.valueOf(szYm).intValue();
		// パラメータ確認
		if (szYm == null) {
			System.out.println(super.getConditionLog());
			return "";
		}
		System.out.println(super.getConditionLog());

		String sqlTable = " KOR_K01_RFM T01 ";
		// 年月指定条件
		String sqlWhere = " YYYY_MM = '" + szYm + "'";
		if (inYm <= 201112) { //すべて
			sqlTable = " KOR_K01_RFM_KAKO T01 ";
		}
		// 販売チャネル条件
		sqlWhere += " and CHANEL_CD = '" + szChanel + "' ";
		// 会員区分条件
		if (!StringUtils.equals(szKaiin, "00")) { //すべて以外
			sqlWhere += " and KAIIN_KBN = '" + szKaiin + "' ";
		}
		// 地域条件
		if (!StringUtils.equals(szArea, "00")) { //すべて以外
			if (StringUtils.equals(szArea.substring(2,5), "000")) {
				sqlWhere += " and substr(CHIKU_CD,1,2) = '" + szArea.substring(0,2) + "' ";
			} else{
				sqlWhere += " and CHIKU_CD = '" + szArea + "' ";
			}
		}
		// 年齢条件
		if (StringUtils.equals(szAge, "S01")) {
			sqlWhere += " AND (AGE >= 12 AND AGE <= 19) ";
		} else if (StringUtils.equals(szAge, "S02")) {
			sqlWhere += " AND (AGE >= 20 AND AGE <= 24) ";
		} else if (StringUtils.equals(szAge, "S03")) {
			sqlWhere += " AND (AGE >= 25 AND AGE <= 29) ";
		} else if (StringUtils.equals(szAge, "S04")) {
			sqlWhere += " AND (AGE >= 30 AND AGE <= 34) ";
		} else if (StringUtils.equals(szAge, "S05")) {
			sqlWhere += " AND (AGE >= 35 AND AGE <= 39) ";
		} else if (StringUtils.equals(szAge, "S06")) {
			sqlWhere += " AND (AGE >= 40 AND AGE <= 44) ";
		} else if (StringUtils.equals(szAge, "S07")) {
			sqlWhere += " AND (AGE >= 45 AND AGE <= 49) ";
		} else if (StringUtils.equals(szAge, "S08")) {
			sqlWhere += " AND (AGE >= 50 AND AGE <= 54) ";
		} else if (StringUtils.equals(szAge, "S09")) {
			sqlWhere += " AND (AGE >= 55 AND AGE <= 59) ";
		} else if (StringUtils.equals(szAge, "S10")) {
			sqlWhere += " AND (AGE >= 60 AND AGE <= 69) ";
		} else if (StringUtils.equals(szAge, "S11")) {
			sqlWhere += " AND (AGE >= 70 ) ";
		} else if (StringUtils.equals(szAge, "S12")) {
			sqlWhere += " AND (AGE >= 100 OR AGE <= 11) ";
		}

		// 列名固定値
		String[] itemF = { "１回", "２回", "３回", "４回", "５回", "６回", "７回", "８回", "９回", "１０回以上", "回数計" };
		String[] itemM = { "\"5万W未満\"","\"5-10万未満\"", "\"10-15万未満\"", "\"15-20万未満\"", "\"20-30万未満\"", "\"30-40万未満\"", "\"40-50万未満\"",
				"\"50-60万未満\"", "\"60-100万未\"", "\"100-200万未\"", "\"200-300万未\"", "\"300万W以上\"", "\"受注金額(W)\"" };
		String[] itemM2 = { "\"0-5万W\"","\"5-10万W\"", "\"10-15万W\"", "\"15-20万W\"", "\"20-25万W\"", "\"25-30万W\"", "\"30-35万W\"",
				"\"35-40万W\"", "\"40-45万W\"", "\"45-50万W\"", "\"50-55万W\"", "\"55-60万W\"", "\"60-65万W\"","\"65-70万W\"", "\"70-75万W\"", "\"75-80万W\"", "\"80-85万W\"", "\"85-90万W\"", "\"90-95万W\"", "\"95-100万W\"", "\"100-200万W\"", "\"200-300万W\"", "\"300万W以上\"", "\"受注金額(W)\"" };
		String[] itemYm1 = { "当月", "経過１ヶ月", "経過２ヶ月", "経過３ヶ月", "経過４ヶ月", "経過５ヶ月", "経過６ヶ月", "経過７ヶ月", "経過８ヶ月", "経過９ヶ月", "経過10ヶ月", "経過11ヶ月", "経過12ヶ月",
				"13-24ヶ月", "25ヶ月以上", "初回受注月計" };

		String sqlItem0 = " ";
		String sqlSum1 = "  ";
		String sqlSum2 = "  ";
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
			sqlSum2  = "sum(SU1) as \"１回\" , sum(SU2) as \"２回\" , sum(SU3) as \"３回\" , sum(SU4) as \"４回\",sum(SU5) as \"５回\" , ";
			sqlSum2 += "sum(SU6) as \"６回\" , sum(SU7) as \"７回\" , sum(SU8) as \"８回\" , sum(SU9) as \"９回\" ,sum(SU10) as \"１０回以上\" , sum(SU_ALL) as \"回数計\" ";
			sqlItem1  = " JUCHUMR ";
			sqlItem1G = " JUCHUML_G as SORT , JUCHUML_G_MEI as \"最新受注月数\" ,";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUML_G,JUCHUML_G_MEI ";
			sqlItem2  = " JUCKAISU ";
			sqlTabel2  = " KOR_M_JUCHUML on ITEN_T = JUCHUML ";
			if(StringUtils.equals(szType2, "3")){  //R x F 展開
				sqlSum1  = "case when JUCKAISU = 1 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 2 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 3 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 4 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 6 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 7 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 8 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 9 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 10 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCKAISU = 11 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 12 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 13 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 14 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 15 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCKAISU = 16 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 17 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 18 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 19 then sum(KYAKUSU) else 0 end,case when JUCKAISU = 20 then sum(KYAKUSU) else 0 end ,case when JUCKAISU = 21 then sum(KYAKUSU) else 0 end, sum(KYAKUSU) ";
				sqlSum2  = "sum(SU1) as \"１回\" , sum(SU2) as \"２回\" , sum(SU3) as \"３回\" , sum(SU4) as \"４回\",sum(SU5) as \"５回\", ";
				sqlSum2 += "sum(SU6) as \"６回\" , sum(SU7) as \"７回\" , sum(SU8) as \"８回\" , sum(SU9) as \"９回\" ,sum(SU10) as \"10回\", ";
				sqlSum2 += "sum(SU11) as \"11回\" , sum(SU12) as \"12回\" , sum(SU13) as \"13回\" , sum(SU14) as \"14回\" ,sum(SU15) as \"15回\", ";
				sqlSum2 += "sum(SU16) as \"16回\" , sum(SU17) as \"17回\" , sum(SU18) as \"18回\" , sum(SU19) as \"19回\" ,sum(SU20) as \"20回\" ,sum(SU21) as \"21回\" , sum(SU_ALL) as \"回数計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,";
				sqlItem1G = " JUCHUML as SORT , JUCHUML_MEI as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUML,JUCHUML_MEI ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "2")){  //R x M
			sqlSum1  = "case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end , case when LKONYU = 3 then sum(KYAKUSU) else 0 end ,case when LKONYU = 4 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 5 and 6 then sum(KYAKUSU) else 0 end , case when LKONYU between 7 and 8 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 9 and 10 then sum(KYAKUSU) else 0 end , case when LKONYU between 11 and 12 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 13 and 20 then sum(KYAKUSU) else 0 end ,case when LKONYU = 21 then sum(KYAKUSU) else 0 end ,case when LKONYU = 22 then sum(KYAKUSU) else 0 end , case when LKONYU >= 23 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) ";
			sqlSum2  = "sum(SU1) as "+itemM[0]+" , sum(SU2) as  "+itemM[1]+", sum(SU3) as  "+itemM[2]+" , sum(SU4) as  "+itemM[3]+", ";
			sqlSum2 += "sum(SU5) as "+itemM[4]+" , sum(SU6) as  "+itemM[5]+", sum(SU7) as  "+itemM[6]+" , ";
			sqlSum2 += "sum(SU8) as "+itemM[7]+" , sum(SU9) as  "+itemM[8]+", sum(SU10) as  "+itemM[9]+", sum(SU11) as  "+itemM[10]+" ,sum(SU12) as  "+itemM[11]+" , sum(SU_ALL) as  "+itemM[12]+" ";
			sqlItem1  = " JUCHUMR ";
			sqlItem0  = " SU11,SU12,";
			sqlItem1G = " JUCHUML_G as SORT , JUCHUML_G_MEI as \"最新受注月数\" ,";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUML_G,JUCHUML_G_MEI ";
			sqlItem2  = " LKONYU ";
			sqlTabel2  = " KOR_M_JUCHUML on ITEN_T = JUCHUML ";
			if(StringUtils.equals(szType2, "3")){  //R x M 展開
				sqlSum1  = "case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end,case when LKONYU = 3 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 4 then sum(KYAKUSU) else 0 end,case when LKONYU = 5 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 6 then sum(KYAKUSU) else 0 end,case when LKONYU = 7 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 8 then sum(KYAKUSU) else 0 end,case when LKONYU = 9 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 10 then sum(KYAKUSU) else 0 end,case when LKONYU = 11 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 12 then sum(KYAKUSU) else 0 end,case when LKONYU = 13 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 14 then sum(KYAKUSU) else 0 end,case when LKONYU = 15 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 16 then sum(KYAKUSU) else 0 end,case when LKONYU = 17 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 18 then sum(KYAKUSU) else 0 end,case when LKONYU = 19 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 20 then sum(KYAKUSU) else 0 end,case when LKONYU = 21 then sum(KYAKUSU) else 0 end,case when LKONYU = 22 then sum(KYAKUSU) else 0 end,case when LKONYU >= 23 then sum(KYAKUSU) else 0 end,sum(KYAKUSU) ";
				sqlSum2  = "sum(SU1) as "+itemM2[0]+" , sum(SU2) as "+itemM2[1]+" , sum(SU3) as "+itemM2[2]+" , sum(SU4) as "+itemM2[3]+" , ";
				sqlSum2 += "sum(SU5) as "+itemM2[4]+" , sum(SU6) as "+itemM2[5]+" , sum(SU7) as "+itemM2[6]+" , ";
				sqlSum2 += "sum(SU8) as "+itemM2[7]+" , sum(SU9) as "+itemM2[8]+" , sum(SU10) as "+itemM2[9]+" , ";
				sqlSum2 += "sum(SU11) as "+itemM2[10]+" , sum(SU12) as "+itemM2[11]+" , sum(SU13) as "+itemM2[12]+" , ";
				sqlSum2 += "sum(SU14) as "+itemM2[13]+" , sum(SU15) as "+itemM2[14]+" , sum(SU16) as "+itemM2[15]+" , ";
				sqlSum2 += "sum(SU17) as "+itemM2[16]+" , sum(SU18) as "+itemM2[17]+" , sum(SU19) as "+itemM2[18]+" ,sum(SU20) as "+itemM2[19]+" ,  ";
				sqlSum2 += "sum(SU21) as "+itemM2[20]+" , sum(SU22) as "+itemM2[21]+" , sum(SU23) as "+itemM2[22]+" ,sum(SU_ALL) as "+itemM2[23]+" ";
				sqlItem1  = " JUCHUMR ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,";
				sqlItem1G = " JUCHUML as SORT , JUCHUML_MEI as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUML,JUCHUML_MEI ";
				sqlItem2  = " LKONYU ";
				sqlTabel2  = " KOR_M_JUCHUML on ITEN_T = JUCHUML ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "3")){  //F x M
			sqlSum1  = "case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end , case when LKONYU = 3 then sum(KYAKUSU) else 0 end ,case when LKONYU = 4 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 5 and 6 then sum(KYAKUSU) else 0 end , case when LKONYU between 7 and 8 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 9 and 10 then sum(KYAKUSU) else 0 end , case when LKONYU between 11 and 12 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when LKONYU between 13 and 20 then sum(KYAKUSU) else 0 end ,case when LKONYU = 21 then sum(KYAKUSU) else 0 end ,case when LKONYU = 22 then sum(KYAKUSU) else 0 end , case when LKONYU >= 23 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) ";
			sqlSum2  = "sum(SU1) as "+itemM[0]+" , sum(SU2) as  "+itemM[1]+", sum(SU3) as  "+itemM[2]+" , sum(SU4) as  "+itemM[3]+", ";
			sqlSum2 += "sum(SU5) as "+itemM[4]+" , sum(SU6) as  "+itemM[5]+", sum(SU7) as  "+itemM[6]+" , ";
			sqlSum2 += "sum(SU8) as "+itemM[7]+" , sum(SU9) as  "+itemM[8]+", sum(SU10) as  "+itemM[9]+", sum(SU11) as  "+itemM[10]+" ,sum(SU12) as  "+itemM[11]+" , sum(SU_ALL) as  "+itemM[12]+" ";
			sqlItem0  = " SU11,SU12,";
			sqlItem1  = " JUCKAISU ";
			sqlItem1G = " FRANK_G_CD as SORT , FRANK_G_NM as \"回数\" ,";
			sqlItem2G = " '回数計' as \"回数\" , ";
			sqlGroup = " FRANK_G_CD,FRANK_G_NM ";
			sqlItem2  = " LKONYU ";
			sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
			if(StringUtils.equals(szType2, "3")){  //F x M 展開
				sqlSum1  = "case when LKONYU = 1 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when LKONYU = 2 then sum(KYAKUSU) else 0 end,case when LKONYU = 3 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 4 then sum(KYAKUSU) else 0 end,case when LKONYU = 5 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 6 then sum(KYAKUSU) else 0 end,case when LKONYU = 7 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 8 then sum(KYAKUSU) else 0 end,case when LKONYU = 9 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 10 then sum(KYAKUSU) else 0 end,case when LKONYU = 11 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 12 then sum(KYAKUSU) else 0 end,case when LKONYU = 13 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 14 then sum(KYAKUSU) else 0 end,case when LKONYU = 15 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 16 then sum(KYAKUSU) else 0 end,case when LKONYU = 17 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 18 then sum(KYAKUSU) else 0 end,case when LKONYU = 19 then sum(KYAKUSU) else 0 end,";
				sqlSum1 += "case when LKONYU = 20 then sum(KYAKUSU) else 0 end,case when LKONYU = 21 then sum(KYAKUSU) else 0 end,case when LKONYU = 22 then sum(KYAKUSU) else 0 end,case when LKONYU >= 23 then sum(KYAKUSU) else 0 end,sum(KYAKUSU) ";
				sqlSum2  = "sum(SU1) as "+itemM2[0]+" , sum(SU2) as "+itemM2[1]+" , sum(SU3) as "+itemM2[2]+" , sum(SU4) as "+itemM2[3]+" , ";
				sqlSum2 += "sum(SU5) as "+itemM2[4]+" , sum(SU6) as "+itemM2[5]+" , sum(SU7) as "+itemM2[6]+" , ";
				sqlSum2 += "sum(SU8) as "+itemM2[7]+" , sum(SU9) as "+itemM2[8]+" , sum(SU10) as "+itemM2[9]+" , ";
				sqlSum2 += "sum(SU11) as "+itemM2[10]+" , sum(SU12) as "+itemM2[11]+" , sum(SU13) as "+itemM2[12]+" , ";
				sqlSum2 += "sum(SU14) as "+itemM2[13]+" , sum(SU15) as "+itemM2[14]+" , sum(SU16) as "+itemM2[15]+" , ";
				sqlSum2 += "sum(SU17) as "+itemM2[16]+" , sum(SU18) as "+itemM2[17]+" , sum(SU19) as "+itemM2[18]+" ,sum(SU20) as "+itemM2[19]+" ,  ";
				sqlSum2 += "sum(SU21) as "+itemM2[20]+" , sum(SU22) as "+itemM2[21]+" , sum(SU23) as "+itemM2[22]+" ,sum(SU_ALL) as "+itemM2[23]+" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,";
				sqlItem1  = " JUCKAISU ";
				sqlItem1G = " FRANK as SORT , FRANK_NM as \"回数\" ,";
				sqlItem2G = " '回数計' as \"回数\" , ";
				sqlGroup = " FRANK,FRANK_NM ";
				sqlItem2  = " LKONYU ";
				sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "4")){  //R x 初回受注月
			sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF between 14 and 25 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
			sqlSum2  = "sum(SU1) as \"当月\" , sum(SU2) as \"経過１ヶ月\" , sum(SU3) as \"経過２ヶ月\" , sum(SU4) as \"経過３ヶ月\" , sum(SU5) as \"経過４ヶ月\" , sum(SU6) as \"経過５ヶ月\" , ";
			sqlSum2 += "sum(SU7) as \"経過６ヶ月\" , sum(SU8) as \"経過７ヶ月\" , sum(SU9) as \"経過８ヶ月\" , sum(SU10) as \"経過９ヶ月\" , sum(SU11) as \"経過10ヶ月\" , ";
			sqlSum2 += "sum(SU12) as \"経過11ヶ月\" , sum(SU13) as \"経過12ヶ月\" , sum(SU14) as \"13-24ヶ月\" , sum(SU15) as \"25ヶ月以上\" , sum(SU_ALL) as \"初回計\" ";
			sqlItem0  = " SU11, SU12, SU13, SU14, SU15,";
			sqlItem1  = " JUCHUMR ";
			sqlItem1G = " JUCHUML_G as SORT , JUCHUML_G_MEI as \"最新受注月数\" ,";
			sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
			sqlGroup = " JUCHUML_G,JUCHUML_G_MEI ";
			sqlItem2  = " JUCHUMF ";
			sqlTabel2  = " KOR_M_JUCHUML on ITEN_T = JUCHUML ";
			if(StringUtils.equals(szType2, "3")){  //R x 初回受注月 展開
				sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 14 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 15 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 16 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 17 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 18 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 19 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 20 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 21 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 22 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 23 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 24 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 25 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
				sqlSum2  = "sum(SU1) as \"当月\" , sum(SU2) as \"経過１ヶ月\" , sum(SU3) as \"経過２ヶ月\" , sum(SU4) as \"経過３ヶ月\" , sum(SU5) as \"経過４ヶ月\" , sum(SU6) as \"経過５ヶ月\" , ";
				sqlSum2 += "sum(SU7) as \"経過６ヶ月\" , sum(SU8) as \"経過７ヶ月\" , sum(SU9) as \"経過８ヶ月\" , sum(SU10) as \"経過９ヶ月\" , sum(SU11) as \"経過１０ヶ月\" ,sum(SU12) as \"経過11ヶ月\" , ";
				sqlSum2 += "sum(SU13) as \"経過12ヶ月\" ,sum(SU14) as \"経過13ヶ月\", sum(SU15) as \"経過14ヶ月\",sum(SU16) as \"経過15ヶ月\",sum(SU17) as \"経過16ヶ月\",sum(SU18) as \"経過17ヶ月\",sum(SU19) as \"経過18ヶ月\",";
				sqlSum2 += "sum(SU20) as \"経過19ヶ月\",sum(SU21) as \"経過20ヶ月\",sum(SU22) as \"経過21ヶ月\",sum(SU23) as \"経過22ヶ月\",sum(SU24) as \"経過23ヶ月\",sum(SU25) as \"経過24ヶ月\",sum(SU26) as \"25ヶ月以上\" , sum(SU_ALL) as \"受注月計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,SU24,SU25,SU26,";
				sqlItem1  = " JUCHUMR ";
				sqlItem1G = " JUCHUML as SORT , JUCHUML_MEI as \"最新受注月数\" ,";
				sqlItem2G = " '最新受注月計' as \"最新受注月数\" , ";
				sqlGroup = " JUCHUML,JUCHUML_MEI ";
				sqlItem2  = " JUCHUMF ";
				sqlTabel2  = " KOR_M_JUCHUML on ITEN_T = JUCHUML ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "5")){  //F x 初回受注月
			sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF between 14 and 25 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
			sqlSum2  = "sum(SU1) as \"当月\" , sum(SU2) as \"経過１ヶ月\" , sum(SU3) as \"経過２ヶ月\" , sum(SU4) as \"経過３ヶ月\" , sum(SU5) as \"経過４ヶ月\" , sum(SU6) as \"経過５ヶ月\" , ";
			sqlSum2 += "sum(SU7) as \"経過６ヶ月\" , sum(SU8) as \"経過７ヶ月\" , sum(SU9) as \"経過８ヶ月\" , sum(SU10) as \"経過９ヶ月\" , sum(SU11) as \"経過10ヶ月\" , ";
			sqlSum2 += "sum(SU12) as \"経過11ヶ月\" , sum(SU13) as \"経過12ヶ月\" , sum(SU14) as \"13-24ヶ月\" , sum(SU15) as \"25ヶ月以上\" , sum(SU_ALL) as \"初回計\" ";
			sqlItem0  = " SU11, SU12, SU13, SU14, SU15,";
			sqlItem1  = " JUCKAISU ";
			sqlItem1G = " FRANK_G_CD as SORT , FRANK_G_NM as \"回数\" ,";
			sqlItem2G = " '回数計' as \"回数\" , ";
			sqlGroup = " FRANK_G_CD,FRANK_G_NM ";
			sqlItem2  = " JUCHUMF ";
			sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
			if(StringUtils.equals(szType2, "3")){  //F x 初回受注月 展開
				sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 14 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 15 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 16 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 17 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 18 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 19 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 20 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 21 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 22 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 23 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 24 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 25 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
				sqlSum2  = "sum(SU1) as \"当月\" , sum(SU2) as \"経過１ヶ月\" , sum(SU3) as \"経過２ヶ月\" , sum(SU4) as \"経過３ヶ月\" , sum(SU5) as \"経過４ヶ月\" , sum(SU6) as \"経過５ヶ月\" , ";
				sqlSum2 += "sum(SU7) as \"経過６ヶ月\" , sum(SU8) as \"経過７ヶ月\" , sum(SU9) as \"経過８ヶ月\" , sum(SU10) as \"経過９ヶ月\" , sum(SU11) as \"経過１０ヶ月\" ,sum(SU12) as \"経過11ヶ月\" , ";
				sqlSum2 += "sum(SU13) as \"経過12ヶ月\" ,sum(SU14) as \"経過13ヶ月\", sum(SU15) as \"経過14ヶ月\",sum(SU16) as \"経過15ヶ月\",sum(SU17) as \"経過16ヶ月\",sum(SU18) as \"経過17ヶ月\",sum(SU19) as \"経過18ヶ月\",";
				sqlSum2 += "sum(SU20) as \"経過19ヶ月\",sum(SU21) as \"経過20ヶ月\",sum(SU22) as \"経過21ヶ月\",sum(SU23) as \"経過22ヶ月\",sum(SU24) as \"経過23ヶ月\",sum(SU25) as \"経過24ヶ月\",sum(SU26) as \"25ヶ月以上\" , sum(SU_ALL) as \"受注月計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,SU24,SU25,SU26,";
				sqlItem1  = " JUCKAISU ";
				sqlItem1G = " FRANK as SORT , FRANK_NM as \"回数\" ,";
				sqlItem2G = " '回数計' as \"回数\" , ";
				sqlGroup = " FRANK,FRANK_NM ";
				sqlItem2  = " JUCHUMF ";
				sqlTabel2  = " M_FRANK on ITEN_T = FRANK ";
				sqlSORT2 = "9999" ;
			}
		}else if (StringUtils.equals(szType1, "6")){  //M x 初回受注月
			sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
			sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF between 14 and 25 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
			sqlSum2  = "sum(SU1) as \"当月\" , sum(SU2) as \"経過１ヶ月\" , sum(SU3) as \"経過２ヶ月\" , sum(SU4) as \"経過３ヶ月\" , sum(SU5) as \"経過４ヶ月\" , sum(SU6) as \"経過５ヶ月\" , ";
			sqlSum2 += "sum(SU7) as \"経過６ヶ月\" , sum(SU8) as \"経過７ヶ月\" , sum(SU9) as \"経過８ヶ月\" , sum(SU10) as \"経過９ヶ月\" , sum(SU11) as \"経過10ヶ月\" , ";
			sqlSum2 += "sum(SU12) as \"経過11ヶ月\" , sum(SU13) as \"経過12ヶ月\" , sum(SU14) as \"13-24ヶ月\" , sum(SU15) as \"25ヶ月以上\" , sum(SU_ALL) as \"初回計\" ";
			sqlItem0  = " SU11, SU12, SU13, SU14, SU15,";
			sqlItem1  = " LKONYU ";
			sqlItem1G = " LKONYU_G as SORT , LKONYU_G_MEI as \"受注金額(W)\" ,";
			sqlItem2G = " '受注金額計' as \"受注金額(W)\" , ";
			sqlGroup = " LKONYU_G , LKONYU_G_MEI ";
			sqlItem2  = " JUCHUMF ";
			sqlTabel2  = " KOR_M_LKONYU on ITEN_T = LKONYU ";
			sqlSORT2 = " 'ZZ999' " ;
			if(StringUtils.equals(szType2, "3")){  //Mx 初回受注月 展開
				sqlSum1  = "case when JUCHUMF = 1 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 2 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 3 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 4 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 5 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 6 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 7 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 8 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 9 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 10 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 11 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 12 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 13 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 14 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 15 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 16 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 17 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 18 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 19 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 20 then sum(KYAKUSU) else 0 end , ";
				sqlSum1 += "case when JUCHUMF = 21 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 22 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 23 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 24 then sum(KYAKUSU) else 0 end , case when JUCHUMF = 25 then sum(KYAKUSU) else 0 end ,  ";
				sqlSum1 += "case when JUCHUMF = 26 then sum(KYAKUSU) else 0 end , sum(KYAKUSU) 	";
				sqlSum2  = "sum(SU1) as \"当月\" , sum(SU2) as \"経過１ヶ月\" , sum(SU3) as \"経過２ヶ月\" , sum(SU4) as \"経過３ヶ月\" , sum(SU5) as \"経過４ヶ月\" , sum(SU6) as \"経過５ヶ月\" , ";
				sqlSum2 += "sum(SU7) as \"経過６ヶ月\" , sum(SU8) as \"経過７ヶ月\" , sum(SU9) as \"経過８ヶ月\" , sum(SU10) as \"経過９ヶ月\" , sum(SU11) as \"経過１０ヶ月\" ,sum(SU12) as \"経過11ヶ月\" , ";
				sqlSum2 += "sum(SU13) as \"経過12ヶ月\" ,sum(SU14) as \"経過13ヶ月\", sum(SU15) as \"経過14ヶ月\",sum(SU16) as \"経過15ヶ月\",sum(SU17) as \"経過16ヶ月\",sum(SU18) as \"経過17ヶ月\",sum(SU19) as \"経過18ヶ月\",";
				sqlSum2 += "sum(SU20) as \"経過19ヶ月\",sum(SU21) as \"経過20ヶ月\",sum(SU22) as \"経過21ヶ月\",sum(SU23) as \"経過22ヶ月\",sum(SU24) as \"経過23ヶ月\",sum(SU25) as \"経過24ヶ月\",sum(SU26) as \"25ヶ月以上\" , sum(SU_ALL) as \"受注月計\" ";
				sqlItem0  = " SU11,SU12,SU13,SU14,SU15,SU16,SU17,SU18,SU19,SU20,SU21,SU22,SU23,SU24,SU25,SU26,";
				sqlItem1  = " LKONYU ";
				sqlItem1G = " LKONYU as SORT , LKONYU_MEI as \"受注金額(W)\" ,";
				sqlItem2G = " '受注金額計' as \"受注金額(W)\" , ";
				sqlGroup = " LKONYU , LKONYU_MEI ";
				sqlItem2  = " JUCHUMF ";
				sqlTabel2  = " KOR_M_LKONYU on ITEN_T = LKONYU ";
				sqlSORT2 = "9999" ;
			}
		}
		// 検索文（）
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("with work(ITEN_T, SU1, SU2, SU3, SU4, SU5, SU6, SU7, SU8, SU9, SU10," + sqlItem0 + "SU_ALL) as (");
		sbSQL.append(" select " + sqlItem1 + ", ");
		sbSQL.append(sqlSum1);
		sbSQL.append("from "+ sqlTable);
		sbSQL.append(" where " + sqlWhere + " group by " + sqlItem1 + " , " + sqlItem2 + " ) ");
		sbSQL.append("select " + sqlItem1G);
		sbSQL.append(sqlSum2);
		sbSQL.append("from work left outer join " + sqlTabel2);
		sbSQL.append("group by " + sqlGroup);
		sbSQL.append("union all ");
		sbSQL.append("select "+sqlSORT2+" as SORT," + sqlItem2G);
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
		cells.add( DefineReport.Select.YM_KIJUN_KOR.getView());
		cells.add( jad.getJSONText(DefineReport.Select.YM_KIJUN_KOR.getObj()) );
		getWhere().add(cells);

		// 表示条件
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.S_TYPE2.getView());
		cells.add( jad.getJSONText(DefineReport.Select.S_TYPE2.getObj()) );
		getWhere().add(cells);

		cells.add( DefineReport.Select.KAIIN_KBN_KOR.getView());
		cells.add( jad.getJSONText(DefineReport.Select.KAIIN_KBN_KOR.getObj()) );
		//
		cells.add( DefineReport.Select.AGE_KOR.getView());
		cells.add( jad.getJSONText(DefineReport.Select.AGE_KOR.getObj()) );
		//
		cells.add( DefineReport.Select.AREA_KOR.getView());
		cells.add( jad.getJSONText(DefineReport.Select.AREA_KOR.getObj()) );
		cells = new ArrayList<String>();

		cells.add( "" );
		cells.add( "" );
		cells.add( DefineReport.Select.CHANEL_KOR.getView());
		cells.add( jad.getJSONText(DefineReport.Select.CHANEL_KOR.getObj()) );
		getWhere().add(cells);

		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);

	}

}
