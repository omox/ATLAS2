package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.DefineReport;
import common.JsonArrayData;

/**
 * @author EATONE
 */
public class D00Dao extends ItemDao {

	/**
	 * 新しいB04Daoのインスタンスを生成します。
	 *
	 * @param source コネクション
	 */
	public D00Dao(Connection source) {
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

		String szChanel	= getMap().get("CHANEL");		// 購入チャネル
		String szUnit		= getMap().get("UNIT");			// 集計単位
		String szYy			= getMap().get("YYYY_YY");	// 開始年
		String szNm			= getMap().get("YYYY_MM");	// 開始月
		String szDd			= getMap().get("YYYY_DD");	// 開始日
		String szYy2		= getMap().get("YYYY_YY2");	// 終了年
		String szNm2		= getMap().get("YYYY_MM2");	// 終了月
		String szDd2		= getMap().get("YYYY_DD2");	// 終了日

		String szTenpo	= getMap().get("TENPO");		// 店舗
		String szTenpos	= getMap().get("TENPOS");		// 店舗詳細
		String szArea		= getMap().get("AREA");			// 都道府県
		String szAreas	= getMap().get("AREAS");		// 都道府県詳細
		String szAGE		= getMap().get("AGE");			// 年齢
		String szFKY		= getMap().get("FKY");			// 初回購入年
		String szZKG		= getMap().get("ZKG");			// 初回購入年
		String szRVAL		= getMap().get("RVAL");			// R値
		String szITEM		= getMap().get("ITEM");			// 購入商品

		String szChkS1 = getMap().get("CHK_S1");		// 商品集計-基本分類
		String szChkS2 = getMap().get("CHK_S2");		// 商品集計-大分類
		String szChkS3 = getMap().get("CHK_S3");		// 商品集計-中分類
		String szChkS4 = getMap().get("CHK_S4");		// 商品集計-小分類
		String szChkS5 = getMap().get("CHK_S5");		// 商品集計-シリーズＡ
		String szChkS6 = getMap().get("CHK_S6");		// 商品集計-シリーズＢ
		String szChkS7 = getMap().get("CHK_S7");		// 商品集計-商品

		String szSho1a = getMap().get("SEL_SHO1");	// 商品分類-基本分類
		String szSho2a = getMap().get("SEL_SHO2");	// 商品分類-大分類
		String szSho3a = getMap().get("SEL_SHO3");	// 商品分類-中分類
		String szSho4a = getMap().get("SEL_SHO4");	// 商品分類-小分類
		String szSho5a = getMap().get("SEL_SHO5");	// 商品分類-シリーズＡ
		String szSho6a = getMap().get("SEL_SHO6");	// 商品分類-シリーズＢ

		System.out.println("CHANEL:"+szChanel+",UNIT:"+szUnit);
		System.out.println("YYYY_MM:"+szYy+szNm+szDd+",YYYY_MM2:"+szYy2+szNm2+szDd2);
		System.out.println("TENPO:"+szTenpo+",TENPOS:"+szTenpos);
		System.out.println("AREA:"+szArea+",AREAS:"+szAreas);
		System.out.println("AGE:"+szAGE);
		System.out.println("FKY:"+szFKY);
		System.out.println("ZKG:"+szZKG);
		System.out.println("RVAL:"+szRVAL);
		System.out.println("ITEM:"+szITEM);
		System.out.println("SEL_SHO:"+szSho1a+szSho2a+szSho3a+szSho4a+szSho5a+szSho6a);
		System.out.println("CHK_S1:" +szChkS1+",CHK_S2:"+szChkS2+",CHK_S3:"+szChkS3+",CHK_S4:"+szChkS4+",CHK_S5:"+szChkS5+",CHK_S6:"+szChkS6+",CHK_S7:"+szChkS7);

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM DS_B_PLAN");

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

		// 集計単位
		cells.add(DefineReport.RadD02_UNIT.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD02_UNIT.NAME.getObj()));

		// 集計期間
		String UNIT = jad.getJSONValue(DefineReport.RadD02_UNIT.NAME.getObj());
		cells.add(DefineReport.Label.LABELD03.getView());

		if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID1.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()));

		} else if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()));

		} else if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID3.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));

		} else if (StringUtils.equals(UNIT, DefineReport.RadD02_UNIT.ID4.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN.getObj()));
			cells.add("～");
			cells.add(jad.getJSONText(DefineReport.Select.YY_KIJUN_TO.getObj()) + jad.getJSONText(DefineReport.Select.MM_KIJUN_TO.getObj()));
		}
		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 店舗
		cells.add(DefineReport.RadD03_TENPO.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD03_TENPO.NAME.getObj()));
		if (StringUtils.equals(jad.getJSONValue(DefineReport.RadD03_TENPO.NAME.getObj()), DefineReport.RadD03_TENPO.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.TENPO.getObj()));
		}
		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 都道府県
		cells.add(DefineReport.RadD04_AREA.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD04_AREA.NAME.getObj()));
		if (StringUtils.equals(jad.getJSONValue(DefineReport.RadD04_AREA.NAME.getObj()), DefineReport.RadD04_AREA.ID2.getVal())) {
			cells.add(jad.getJSONText(DefineReport.Select.AREA.getObj()));
		}
		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 年齢
		cells.add(DefineReport.RadD05_AGE.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD05_AGE.NAME.getObj()));

		// 初回購入年
		cells.add(DefineReport.RadD06_FKY.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD06_FKY.NAME.getObj()));

		// 前年購入金額
		cells.add(DefineReport.RadD07_ZKG.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD07_ZKG.NAME.getObj()));

		// R値
		cells.add(DefineReport.RadD08_RVL.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD08_RVL.NAME.getObj()));

		getWhere().add(cells);

		cells = new ArrayList<String>();
		// 購入商品
		cells.add(DefineReport.RadD09_CLS.NAME.getView());
		cells.add(jad.getJSONText(DefineReport.RadD09_CLS.NAME.getObj()));

		if (StringUtils.equals(jad.getJSONValue(DefineReport.RadD09_CLS.NAME.getObj()), DefineReport.RadD09_CLS.ID4.getVal())) {
			// 商品分類
			String shobun = jad.getJSONText(DefineReport.Select.SHOS_1.getObj());
			for (int i = 2; i <= 6; i++) {
				String sho = DefineReport.Select.SHOS_NM.getObj() + "_" + i;
				if (!StringUtils.equals(jad.getJSONValue(sho), "00")) {
					shobun += "/" + jad.getJSONText(sho);
				}
			}
			cells.add(shobun);
		}

		getWhere().add(cells);


		// 共通箇所設定
		setCmnOutputQuery(jad);
	}
}
