package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * T03Segテーブルを使用するSQLと条件を記述します。
 *
 * @author EATONE
 */
public class T03SegDao extends ItemDao {

	/**
	 * 新しいT03SegDaoのインスタンスを生成します。
	 * @param source コネクション
	 */
	public T03SegDao(Connection source) {
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
		String szChanel		=	getMap().get("HAN_CHAN");			// （必須）販売チャネル
		String szPromoFrom	=	getMap().get("PROMO_CD_FROM");		// （任意）プロモーションFROM
		String szPromoTo	=	getMap().get("PROMO_CD_TO");		// （任意）プロモーションTO
		String szCategory	=	getMap().get("CATEGORY");			// （必須）評価区分

		// パラメータ確認
		if ((szYm == null) || (szChanel == null) || (szPromoFrom == null)  || (szPromoTo == null)  || (szCategory == null) ) {
			System.out.println("YYYY_MM:" +szYm+",HAN_CHAN:"+szChanel+",PROMO_CD_FROM:"+szPromoFrom+",PROMO_CD_TO:"+szPromoTo+",CATEGORY:"+szCategory);
			return "";
		}

		// 共通Where句
		String sqlWhere = " where YYYY_MM = '" + szYm + "'";
		// 販売チャネル設定
		if (!StringUtils.equals(szChanel, "0")) {
			sqlWhere += " and HAN_CHAN = '" + szChanel + "'";
		}
		// プロモーション設定
		//if (promoText !== "すべて") {
		if (!StringUtils.equals(szPromoFrom, "")) {
			sqlWhere += " and PROMO_CD between '" + szPromoFrom + "' and '" + szPromoTo + "'";
		}


		// 検索文（並び替え＆取得範囲制限）
		StringBuffer sbSQL = new StringBuffer();
		if(StringUtils.equals(szCategory, "1")){
			sbSQL.append("select");
			sbSQL.append("      SEGNO as \"SORT\"");
			sbSQL.append("    , to_char(SEGNO) as \"番号\"");
			sbSQL.append("    , SUM(HASOSU) as \"発送数\"");
			sbSQL.append("    , SUM(HASOHI) as \"発送費用\"");
			sbSQL.append("    , case when SUM(HASOSU) = 0 then 0 else to_number(SUM(HASOHI)) / to_number(SUM(HASOSU)) end as \"DM単価\"");
			sbSQL.append("    , SUM(HENSOSU) as \"返送数\"");
			sbSQL.append("    , SUM(KYAKUSU_J) as \"受注客数\"");
			sbSQL.append("    , SUM(JUCKAISU) as \"受注回数\"");
			sbSQL.append("    , SUM(JUCGAKU) as \"受注金額\"");
			sbSQL.append("    , SUM(JUCSURYO) as \"受注数量\"");
			sbSQL.append("    , case when SUM(HASOSU) = 0 then 0 else to_number(SUM(KYAKUSU_J)) / to_number(SUM(HASOSU)) * 100 end as \"反応率\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(KYAKUSU_J)) end as \"客単価\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(JUCSURYO)) / to_number(SUM(KYAKUSU_J)) end as \"客数量\"");
			sbSQL.append("    , case when SUM(JUCSURYO) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(JUCSURYO)) end as \"商品単価\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(JUCKAISU)) / to_number(SUM(KYAKUSU_J)) end as \"平均回数\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(HASOHI)) / to_number(SUM(KYAKUSU_J)) end as \"CPO\"");
			sbSQL.append("    , case when SUM(HASOHI) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(HASOHI)) end as \"CPR\" ");
			sbSQL.append(" from T03_SEG");
			sbSQL.append(sqlWhere);
			sbSQL.append(" group by SEGNO");
			sbSQL.append(" union all ");
			sbSQL.append(" select");
			sbSQL.append("      MAX(SEGNO) + 1 as \"SORT\"");
			sbSQL.append("    , '計' as 番号");
			sbSQL.append("    , SUM(HASOSU) as \"発送数\"");
			sbSQL.append("    , SUM(HASOHI) as \"発送費用\"");
			sbSQL.append("    , case when SUM(HASOSU) = 0 then 0 else to_number(SUM(HASOHI)) / to_number(SUM(HASOSU)) end as \"DM単価\"");
			sbSQL.append("    , SUM(HENSOSU) as \"返送数\"");
			sbSQL.append("    , SUM(KYAKUSU_J) as \"受注客数\"");
			sbSQL.append("    , SUM(JUCKAISU) as \"受注回数\"");
			sbSQL.append("    , SUM(JUCGAKU) as \"受注金額\"");
			sbSQL.append("    , SUM(JUCSURYO) as \"受注数量\"");
			sbSQL.append("    , case when SUM(HASOSU) = 0 then 0 else to_number(SUM(KYAKUSU_J)) / to_number(SUM(HASOSU)) * 100 end as \"反応率\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(KYAKUSU_J)) end as \"客単価\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(JUCSURYO)) / to_number(SUM(KYAKUSU_J)) end as \"客数量\"");
			sbSQL.append("    , case when SUM(JUCSURYO) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(JUCSURYO)) end as \"商品単価\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(JUCKAISU)) / to_number(SUM(KYAKUSU_J)) end as \"平均回数\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(HASOHI)) / to_number(SUM(KYAKUSU_J)) end as \"CPO\"");
			sbSQL.append("    , case when SUM(HASOHI) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(HASOHI)) end as \"CPR\" ");
			sbSQL.append(" from T03_SEG");
			sbSQL.append(sqlWhere);
			sbSQL.append(" order by ");
		}else{
			sbSQL.append("select");
			sbSQL.append("      SEGNO as \"SORT\"");
			sbSQL.append("    , to_char(SEGNO) as \"番号\"");
			sbSQL.append("    , SUM(HASOSU) as \"発送数\"");
			sbSQL.append("    , SUM(HASOHI) as \"発送費用\"");
			sbSQL.append("    , case when SUM(HASOSU) = 0 then 0 else to_number(SUM(HASOHI)) / to_number(SUM(HASOSU)) end as \"DM単価\"");
			sbSQL.append("    , SUM(HENSOSU) as \"返送数\"");
			sbSQL.append("    , SUM(KYAKUSU_J) as \"受注客数\"");
			sbSQL.append("    , SUM(JUCKAISU) as \"受注回数\"");
			sbSQL.append("    , SUM(JUCGAKU) as \"受注金額\"");
			sbSQL.append("    , SUM(JUCSURYO) as \"受注数量\"");
			sbSQL.append("    , case when SUM(HASOSU) = 0 then 0 else to_number(SUM(JUCKAISU)) / to_number(SUM(HASOSU)) * 100 end as \"反応率\"");
			sbSQL.append("    , case when SUM(JUCKAISU) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(JUCKAISU)) end as \"回単価\"");
			sbSQL.append("    , case when SUM(JUCKAISU) = 0 then 0 else to_number(SUM(JUCSURYO)) / to_number(SUM(JUCKAISU)) end as \"回数量\"");
			sbSQL.append("    , case when SUM(JUCSURYO) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(JUCSURYO)) end as \"商品単価\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(JUCKAISU)) / to_number(SUM(KYAKUSU_J)) end as \"平均回数\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(HASOHI)) / to_number(SUM(JUCKAISU)) end as \"CPO\"");
			sbSQL.append("    , case when SUM(HASOHI) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(HASOHI)) end as \"CPR\" ");
			sbSQL.append(" from T03_SEG");
			sbSQL.append(sqlWhere);
			sbSQL.append(" group by SEGNO");
			sbSQL.append(" union all ");
			sbSQL.append(" select");
			sbSQL.append("      MAX(SEGNO) + 1 as \"SORT\"");
			sbSQL.append("    , '計' as 番号");
			sbSQL.append("    , SUM(HASOSU) as \"発送数\"");
			sbSQL.append("    , SUM(HASOHI) as \"発送費用\"");
			sbSQL.append("    , case when SUM(HASOSU) = 0 then 0 else to_number(SUM(HASOHI)) / to_number(SUM(HASOSU)) end as \"DM単価\"");
			sbSQL.append("    , SUM(HENSOSU) as \"返送数\"");
			sbSQL.append("    , SUM(KYAKUSU_J) as \"受注客数\"");
			sbSQL.append("    , SUM(JUCKAISU) as \"受注回数\"");
			sbSQL.append("    , SUM(JUCGAKU) as \"受注金額\"");
			sbSQL.append("    , SUM(JUCSURYO) as \"受注数量\"");
			sbSQL.append("    , case when SUM(HASOSU) = 0 then 0 else to_number(SUM(JUCKAISU)) / to_number(SUM(HASOSU)) * 100 end as \"反応率\"");
			sbSQL.append("    , case when SUM(JUCKAISU) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(JUCKAISU)) end as \"回単価\"");
			sbSQL.append("    , case when SUM(JUCKAISU) = 0 then 0 else to_number(SUM(JUCSURYO)) / to_number(SUM(JUCKAISU)) end as \"回数量\"");
			sbSQL.append("    , case when SUM(JUCSURYO) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(JUCSURYO)) end as \"商品単価\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(JUCKAISU)) / to_number(SUM(KYAKUSU_J)) end as \"平均回数\"");
			sbSQL.append("    , case when SUM(KYAKUSU_J) = 0 then 0 else to_number(SUM(HASOHI)) / to_number(SUM(JUCKAISU)) end as \"CPO\"");
			sbSQL.append("    , case when SUM(HASOHI) = 0 then 0 else to_number(SUM(JUCGAKU)) / to_number(SUM(HASOHI)) end as \"CPR\" ");
			sbSQL.append(" from T03_SEG");
			sbSQL.append(sqlWhere);
			sbSQL.append(" order by ");
		}

		// 並び替え情報構築
		String szSort = getSortString(getMap().get("sortable"),getMap().get("sortName"),getMap().get("sortOrder"));
		if ("".equals(szSort)) {
			sbSQL.append("  SORT");
		} else {
			sbSQL.append(szSort);
		}

		System.out.println(sbSQL.toString());
		command = sbSQL.toString();
		return command;
	}

	/**
	 * 並び替え用
	 * @param sortable
	 * @param sortName
	 * @param sortOrder
	 * @return
	 */
	private String getSortString(String sortable, String sortName, String sortOrder) {
		String sortString="";
		if ((sortable.equals("0")) || ( sortName==null ) || (sortOrder==null)) {
			return sortString;
		}

		if (sortName.startsWith("F")) {
			return sortName.substring(1) + " " + sortOrder;
		}

		return sortString;
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

		// 販売チャンネル
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.CHANEL.getView());
		cells.add( jad.getJSONText(DefineReport.Select.CHANEL.getObj()) );
		getWhere().add(cells);

		// プロモーションFrom
		cells = new ArrayList<String>();
		cells.add( DefineReport.Select.PROMO_FROM.getView());
		cells.add( jad.getJSONText(DefineReport.Select.PROMO_FROM.getObj()) );
		cells.add( "～" );
		cells.add( jad.getJSONText(DefineReport.Select.PROMO_TO.getObj()) );
		getWhere().add(cells);

		//
		cells = new ArrayList<String>();
		cells.add( DefineReport.RadioCategory.NAME.getView());
		cells.add( jad.getJSONText(DefineReport.RadioCategory.NAME.getObj()) );
		getWhere().add(cells);


		//空白行
		cells = new ArrayList<String>();
		cells.add( "" );
		getWhere().add(cells);

	}

}
