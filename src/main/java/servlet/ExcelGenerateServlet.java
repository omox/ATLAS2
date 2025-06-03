package servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ConditionalFormatting;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.DefineReport;
import common.Defines;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

/**
 * Servlet implementation class ExcelGenerateServlet<br/>
 * <br/>
 * Excelファイル作成サーブレット<br/>
 * 検索実行時に設定したセッション情報を元に、EXCELファイルを作成します。<br/>
 * <br/>
 * 内容<br/>
 * 1.セッション情報取得(各種検索用Dao参照)<br/>
 * 2.ベースExcelファイル、作成処理指定<br/>
 * ※既存のファイル作成処理の場合、内容・レイアウト共にほぼ再設定している<br/>
 * 3.ファイル作成処理<br/>
 * ATLAS用特殊処理として、列名「SORT」は出力対象外<br/>
 *
 */
public class ExcelGenerateServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExcelGenerateServlet() {
		super();
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ExcelGenerateServlet");
		// 文字変換コード設定【重要】
		request.setCharacterEncoding("UTF-8");

		// パラメータ一覧【確認】
		HashMap<String, String> map = new HashMap<String, String>();
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			System.out.println(name + "=" + request.getParameter(name));
			map.put(name, request.getParameter(name));
		}
		System.out.println(XSSFWorkbook.class.getProtectionDomain().getCodeSource().getLocation());

		// セッション情報
		HttpSession session = request.getSession(false);

		if (map.get(DefineReport.ID_SESSION_HEADER) != null) {

			// JSONパラメータの解析
			System.out.println("map-header:" + map.get(DefineReport.ID_SESSION_HEADER));
			JSONArray titleArray = (JSONArray) JSONSerializer.toJSON(map.get(DefineReport.ID_SESSION_HEADER));

			// タイトル情報
			ArrayList<List<String>> title = new ArrayList<List<String>>();

			for (int i = 0; i < titleArray.size(); i++) {
				// 情報保存
				List<String> row = new ArrayList<String>();
				row = (List<String>) titleArray.get(i);

				List<String> cols = new ArrayList<String>();
				for (int j = 0; j < row.size(); j++) {
					cols.add(row.get(j));
				}
				title.add(cols);
			}

			// セッション保持
			session.setAttribute(DefineReport.ID_SESSION_HEADER, title);

		}

		if (map.get(DefineReport.ID_SESSION_TABLE) != null) {

			// JSONパラメータの解析
			System.out.println("map-table:" + map.get(DefineReport.ID_SESSION_TABLE));
			JSONArray mapArray = (JSONArray) JSONSerializer.toJSON(map.get(DefineReport.ID_SESSION_TABLE));

			// DB検索結果（0レコード＝タイトル）
			ArrayList<List<String>> table = new ArrayList<List<String>>();

			for (int i = 0; i < mapArray.size(); i++) {
				// 情報保存
				List<String> row = new ArrayList<String>();
				row = (List<String>) mapArray.get(i);

				List<String> cols = new ArrayList<String>();
				for (int j = 0; j < row.size(); j++) {
					cols.add(row.get(j));
				}
				table.add(cols);
			}

			// セッション保持
			session.setAttribute(DefineReport.ID_SESSION_TABLE, table);

		}
		if (map.get(DefineReport.ID_SESSION_META) != null) {
			// JSONパラメータの解析
			System.out.println("map-meta:" + map.get(DefineReport.ID_SESSION_TABLE));
			JSONArray metaArray = (JSONArray) JSONSerializer.toJSON(map.get(DefineReport.ID_SESSION_META));

			// メタ情報
			ArrayList<Integer> meta = new ArrayList<Integer>();

			for (int i = 0; i < metaArray.size(); i++) {
				// 情報保存
				meta.add((Integer) metaArray.get(i));
			}

			// セッション保持
			session.setAttribute(DefineReport.ID_SESSION_META, meta);

		}
		if ((map.get(DefineReport.ID_SESSION_HEADER) != null) || (map.get(DefineReport.ID_SESSION_TABLE) != null)
				|| (map.get(DefineReport.ID_SESSION_META) != null)) {
			// JSON データのロード
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print("");
			System.out.println("map-data is not null:" + pw.toString());
			pw.close();
			return;
		}

		// レポート名取得（検索条件の1行目より）
		ArrayList<List<String>> wh = (ArrayList<List<String>>) session.getAttribute(DefineReport.ID_SESSION_WHERE);
		String filename = wh.get(0).get(0);

		String report = request.getParameter("report");
		if (DefineReport.ID_PAGE_B_04.equals(report)) {
			exportCSV(response, session, filename);
		}else {
		

		try {

			// レポート情報
			long start = System.currentTimeMillis();

			// Excel ワークブック
			XSSFWorkbook wb = null;
			if (DefineReport.ID_PAGE_D11.equals(report)) {	// 通店プロモ商品速報
				wb = createReportXX(session, "/xls/Excel11.xlsx", 1);
			} else if (DefineReport.ID_PAGE_D21.equals(report)) {	// 新規獲得CPO
				wb = createReportXX(session, "/xls/Excel21.xlsx", 1);
			} else if (DefineReport.ID_PAGE_D26.equals(report)) {	// チャネル別新規顧客数
				wb = createReportXX(session, "/xls/Excel26.xlsx", 1);
			} else if (DefineReport.ID_PAGE_D30.equals(report)) {	// 店舗売上実績
				wb = createReportXX(session, "/xls/Excel30.xlsx", 1);
			} else {
				wb = createReportXX(session, "/xls/Excel00.xlsx", 1);
			}

			long stop = System.currentTimeMillis();
			System.out.println("EXCEL作成時間: " + (stop - start) + " ミリ秒");

			// ファイル名
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			filename = filename.replaceAll("[ \\\"\'/:;,*?<>|]", "_") + "_" + sdf.format(dt) + ".xlsx";

			//送信
			response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("Windows-31J"), "ISO-8859-1"));
			response.setContentType("application/vnd.ms-excel");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
			return;

		} catch (ClientAbortException e) {
			// ダウンロードダイアログでキャンセルされた場合

		} catch (Throwable e) {
			e.printStackTrace();

			// Excel出力失敗時
			String strPage = Defines.STR_EXCEL_ERROR;
			getServletContext().getRequestDispatcher(strPage).forward(request, response);
		}
		}
	}

	@SuppressWarnings("unchecked")
	private void exportCSV(HttpServletResponse response, HttpSession session, String prefix) throws UnsupportedEncodingException {
		//文字コードと出力するCSVファイル名を設定
		long start = System.currentTimeMillis();
		// ファイル名
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = prefix + "_" + sdf.format(dt) + ".csv";
		System.out.println(filename);

		//送信
		response.setContentType("text/plain;charset=Windows-31J");
		response.setHeader("Content-Disposition", "attachment; filename="+new String(filename.getBytes("Windows-31J"), "ISO-8859-1"));
		
		//try-with-resources文を使うことでclose処理を自動化
		try (PrintWriter pw = response.getWriter()) {
			// 検索結果取得
			ArrayList<List<String>> al = null;

			String outputString = "";
			// 
			al = (ArrayList<List<String>>) session.getAttribute(DefineReport.ID_SESSION_TABLE);
			//CSVファイル内部に記載する形式で文字列を設定
			for (int i = 0; i < al.size(); i++) {
				outputString = "";
				List<String> colList = al.get(i);
				for (int j = 1; j < colList.size(); j++) {
					outputString = outputString + colList.get(j) + ",";
					outputString = outputString.replace("null", ""); // null 削除
				}
				outputString = StringUtils.removeEnd(outputString, ",") + "\r\n";
				//System.out.println(outputString);
				//CSVファイルに書き込み
				pw.print(outputString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("CSV出力時間: " + (System.currentTimeMillis() - start) + " ミリ秒");
		}
		
	}

	/**
	 * シートの条件付き書式
	 */
	public static void conditionalFormat(Sheet sheet) {
		conditionalFormat(sheet, 0);
	}

	/**
	 * シートの条件付き書式
	 */
	public static void conditionalFormat(Sheet sheet, int rowIndexFrom) {
		final SheetConditionalFormatting formatting = sheet.getSheetConditionalFormatting();
		for (int i = 0; i < formatting.getNumConditionalFormattings(); i++) {
			final ConditionalFormatting format = formatting.getConditionalFormattingAt(i);
			final CellRangeAddress[] ranges = format.getFormattingRanges();
			if (rowIndexFrom <= ranges[0].getFirstRow()) {
				for (int j = 0; j < ranges.length; j++) {
					ranges[j].setLastRow(sheet.getLastRowNum());
					ranges[j].setLastColumn(sheet.getRow(sheet.getLastRowNum()).getLastCellNum()-1);
				}
				format.setFormattingRanges(ranges);
			}
		}
	}
	/**
	 * createReportXX<br/>
	 * ATLAS用特殊処理：列名「SORT」は出力対象外<br/>
	 *
	 * @param session セッション
	 * @param filePath ベースExcelファイルパス
	 * @param frozenCol 固定列Index
	 * @return XSSFWorkbook
	 * @throws IOException 例外
	 */
	protected XSSFWorkbook createReportXX(HttpSession session, String filePath, int frozenCol) throws IOException {
		Map<Integer, String> format = new HashMap<Integer, String>();
		format.put(Types.INTEGER, "#,##0;[Red]-#,##0");
		format.put(Types.DOUBLE, "#,##0.00;[Red]-#,##0.00");
		format.put(Types.OTHER, "\"\\\"#,##0;[Red]\"\\\"-#,##0");
		return createReportXX(session, filePath, format, frozenCol);
	}

	/**
	 * createReportXX<br/>
	 * ATLAS用特殊処理：列名「SORT」は出力対象外<br/>
	 *
	 * @param session セッション
	 * @param filePath ベースExcelファイルパス
	 * @param format フォーマットオプション
	 * @param frozenCol 固定列Index
	 * @return XSSFWorkbook
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected XSSFWorkbook createReportXX(HttpSession session, String filePath, Map<Integer, String> format, int frozenCol) throws IOException {
		// Excel ワークブック読み込み
		InputStream is = new FileInputStream(getServletContext().getRealPath(filePath));
		XSSFWorkbook wb = new XSSFWorkbook(is);

		XSSFFont font = wb.createFont();
		font.setFontName("Meiryo UI");

		// シート読み込み
		XSSFSheet sheet = wb.getSheetAt(0);

		// 検索条件取得
		ArrayList<List<String>> wh = new ArrayList<List<String>>();
		if (session.getAttribute(DefineReport.ID_SESSION_WHERE) != null) {
			ArrayList<List<String>> where = (ArrayList<List<String>>) session.getAttribute(DefineReport.ID_SESSION_WHERE);
			wh = new ArrayList<List<String>>(where);
		}

		// 検索結果取得
		ArrayList<List<String>> al = null;
		ArrayList<String> title = null;
		int countTitle = 1;

		if (session.getAttribute(DefineReport.ID_SESSION_HEADER) != null) {
			ArrayList<List<String>> al1 = (ArrayList<List<String>>) session.getAttribute(DefineReport.ID_SESSION_HEADER);
			ArrayList<List<String>> al2 = (ArrayList<List<String>>) session.getAttribute(DefineReport.ID_SESSION_TABLE);
			countTitle = al1.size();
			// タイトル情報取得
			title = new ArrayList<String>(al1.get(al1.size() - 1));
			al = new ArrayList<List<String>>(al1);
			for (int i = 1; i < al2.size(); i++) {
				al.add(al2.get(i));
			}

		} else {
			al = (ArrayList<List<String>>) session.getAttribute(DefineReport.ID_SESSION_TABLE);
			// タイトル情報取得
			title = new ArrayList<String>(al.get(0));
		}
		// メタ情報
		ArrayList<Integer> metaList = null;
		if (session.getAttribute(DefineReport.ID_SESSION_META) != null) {
			ArrayList<Integer> meta = (ArrayList<Integer>) session.getAttribute(DefineReport.ID_SESSION_META);
			metaList = new ArrayList<Integer>(meta);
		}

		// タイトル情報
		int startTitleRow = wh.size();				// タイトル部の開始行
		int endTitleRow = wh.size() + countTitle - 1;	// タイトル部の終了行

		// タイトルスタイル設定用
		XSSFCellStyle styleT = wb.createCellStyle();
		XSSFFont fontT = wb.createFont();
		fontT.setFontName(font.getFontName());
		fontT.setFontHeightInPoints((short) 10);
		styleT.setFont(fontT);												// フォント
		styleT.setBorderTop(BorderStyle.THIN);				// 枠線上
		styleT.setBorderBottom(BorderStyle.THIN);			// 枠線下
		styleT.setBorderLeft(BorderStyle.THIN);				// 枠線左
		styleT.setBorderRight(BorderStyle.THIN);			// 枠線右
		styleT.setVerticalAlignment(VerticalAlignment.CENTER);	// 縦位置中央揃え
		styleT.setAlignment(HorizontalAlignment.CENTER);				// 中央揃え
		styleT.setFillPattern(FillPatternType.SOLID_FOREGROUND);			// 塗りつぶし
		styleT.setFillForegroundColor(new XSSFColor(new java.awt.Color(242,242,242)));	// 色指定
		styleT.setWrapText(true);									// セル内改行

		// スタイル設定用
		XSSFFont fontTxt = wb.createFont();
		fontTxt.setFontName(font.getFontName());
		fontTxt.setFontHeightInPoints((short) 10);

		XSSFCellStyle styleTxt = wb.createCellStyle();
		styleTxt.setFont(fontTxt);									// フォント
		styleTxt.setBorderTop(BorderStyle.THIN);		// 枠線上
		styleTxt.setBorderBottom(BorderStyle.THIN);	// 枠線下
		styleTxt.setBorderLeft(BorderStyle.THIN);		// 枠線左
		styleTxt.setBorderRight(BorderStyle.THIN);		// 枠線右

		// 数値スタイル設定用
		XSSFFont fontNo = wb.createFont();
		fontNo.setFontName(font.getFontName());
		fontNo.setFontHeightInPoints((short) 10);
		XSSFCellStyle styleNo = wb.createCellStyle();
		styleNo.setFont(fontNo);										// フォント
		styleNo.setBorderTop(BorderStyle.THIN);			// 枠線上
		styleNo.setBorderBottom(BorderStyle.THIN);	// 枠線下
		styleNo.setBorderLeft(BorderStyle.THIN);		// 枠線左
		styleNo.setBorderRight(BorderStyle.THIN);		// 枠線右
		styleNo.setAlignment(HorizontalAlignment.RIGHT);	// 右揃え
		XSSFDataFormat formatNo = wb.createDataFormat();
		styleNo.setDataFormat(formatNo.getFormat(format.get(Types.INTEGER)));	// セルのフォーマット

		// %スタイル設定用
		XSSFFont fontPer = wb.createFont();
		fontPer.setFontName(font.getFontName());
		fontPer.setFontHeightInPoints((short) 10);
		XSSFCellStyle stylePer = wb.createCellStyle();
		stylePer.setFont(fontPer);											// フォント
		stylePer.setBorderTop(BorderStyle.THIN);				// 枠線上
		stylePer.setBorderBottom(BorderStyle.THIN);			// 枠線下
		stylePer.setBorderLeft(BorderStyle.THIN);				// 枠線左
		stylePer.setBorderRight(BorderStyle.THIN);			// 枠線右
		stylePer.setAlignment(HorizontalAlignment.RIGHT);		// 右揃え
		XSSFDataFormat formatPer = wb.createDataFormat();
		stylePer.setDataFormat(formatPer.getFormat(format.get(Types.DOUBLE)));	// セルのフォーマット

		// \スタイル設定用
		XSSFFont fontEn = wb.createFont();
		fontEn.setFontName(font.getFontName());
		fontEn.setFontHeightInPoints((short) 10);
		XSSFCellStyle styleEn = wb.createCellStyle();
		styleEn.setFont(fontEn);											// フォント
		styleEn.setBorderTop(BorderStyle.THIN);				// 枠線上
		styleEn.setBorderBottom(BorderStyle.THIN);		// 枠線下
		styleEn.setBorderLeft(BorderStyle.THIN);			// 枠線左
		styleEn.setBorderRight(BorderStyle.THIN);			// 枠線右
		styleEn.setAlignment(HorizontalAlignment.RIGHT);	// 右揃え
		XSSFDataFormat formatEn = wb.createDataFormat();
		styleEn.setDataFormat(formatEn.getFormat(format.get(Types.OTHER)));	// セルのフォーマット

		Iterator<List<String>> itr = al.iterator();
		int count = -1;
		int[] hdnIdx = null;
		// "SORT"列は、ソート用の非表示列として扱う
		for (int i = title.size() - 1; i >= 0; i--) {
			if (StringUtils.startsWith(title.get(i), "SORT")) {
				hdnIdx = ArrayUtils.add(hdnIdx, i);
				title.remove(i);
				metaList.remove(i);
			}
		}

		String rowdata="";

		while (itr.hasNext()) {
			count++;	// カウンタ
			rowdata="";
			// 行生成
			XSSFRow row = sheet.createRow(count + startTitleRow);
			if (count < countTitle) {		// タイトル行
				row.setHeightInPoints(19);
			}

			// セル情報の取得
			List<String> colList = itr.next();
			Iterator<String> itrCols = colList.iterator();
			Iterator<Integer> itrMeta = metaList.iterator();
			int index = -1;
			int cursol = -1;

			while (itrCols.hasNext()) {
				cursol++;
				String val = (String) itrCols.next();
				rowdata += val;

				if (!ArrayUtils.contains(hdnIdx, cursol)) {
					index++;
					// セル（列）生成
					XSSFCell cell = row.createCell(index);
					// 情報設定
					if (count < countTitle) {
						// タイトル部
						cell.setCellValue(val);
						// セルスタイル設定
						cell.setCellStyle(styleT);

					} else {
						// 値設定
						// 元データのタイプ判別
						switch (itrMeta.next()) {
						case Types.DECIMAL:
						case Types.DOUBLE:
						case Types.INTEGER:
						case Types.NUMERIC:

							try {
								cell.setCellValue(Long.parseLong(val));
								if (StringUtils.endsWithAny(title.get(index), new String[] { "比", "率" })) {	// ％スタイル
									cell.setCellStyle(stylePer);
								} else if (StringUtils.endsWith(title.get(index), "単価")) {	// \スタイル
									cell.setCellStyle(styleEn);
								} else if (StringUtils.containsAny(rowdata, "比") || StringUtils.containsAny(rowdata, "率")) {	// ％スタイル
									cell.setCellStyle(stylePer);
								} else {
									cell.setCellStyle(styleNo);
								}
								break;

							} catch (NumberFormatException e) {

								try {
									cell.setCellValue(Double.parseDouble(val));
									if (StringUtils.endsWith(title.get(index), "単価")) {				// \スタイル
										cell.setCellStyle(styleEn);
									} else {
										cell.setCellStyle(stylePer);
									}
									break;
								} catch (NumberFormatException ee) {
									cell.setCellValue(val);
								}

							}

						case Types.CHAR:
						case Types.VARCHAR:
						default:
							cell.setCellValue(val);
							cell.setCellStyle(styleTxt);
							break;
						}
					}
				}
			}
		}

		// タイトル部のセル結合
		int[] escColIdx = new int[countTitle];	// 行単位で作業済みのcolIdxを保持
		int colIdx = 0;		// 列番号
		while (sheet.getRow(startTitleRow).getCell(colIdx) != null && sheet.getRow(startTitleRow).getCell(colIdx).getStringCellValue().length() > 0) {

			int rowIdx = startTitleRow;		// 行番号
			while (rowIdx < endTitleRow) {
				String val = sheet.getRow(rowIdx).getCell(colIdx).getStringCellValue();

				if (colIdx < escColIdx[rowIdx - startTitleRow]) {
					// 作業済みのcolIdxの場合

				} else if (rowIdx > startTitleRow && val.equals(sheet.getRow(rowIdx - 1).getCell(colIdx).getStringCellValue())) {
					// 最上段ではなく、かつ、上のセルと値が同じ場合

				} else {
					int rowspan = 1;
					int colspan = 1;

					while (rowIdx + rowspan <= endTitleRow && val.equals(sheet.getRow(rowIdx + rowspan).getCell(colIdx).getStringCellValue())) {
						// タイトル部内の下のセルと値が同じ場合
						rowspan++;
					}

					while ((rowIdx - startTitleRow == 0 || colIdx + colspan < escColIdx[rowIdx - 1 - startTitleRow])
							&& sheet.getRow(rowIdx).getCell(colIdx + colspan) != null
							&& val.equals(sheet.getRow(rowIdx).getCell(colIdx + colspan).getStringCellValue())) {
						// １つ上の段の結合領域を超えておらず（最上段は考慮しない）、
						// かつ、右のセルと値が同じ場合
						colspan++;
					}

					if (rowspan > 1 || colspan > 1) {
						// セルの結合
						sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx + rowspan - 1, colIdx, colIdx + colspan - 1));

						// 作業済みのcolIdxを保持
						escColIdx[rowIdx - startTitleRow] = colIdx + colspan;
					}
				}
				rowIdx++;
			}
			colIdx++;
		}

		// ウインドウ枠の固定
		sheet.createFreezePane(frozenCol, endTitleRow + 1);

		// 列幅調整
		for (int i = 0; i < title.size(); i++) {
			int min = sheet.getColumnWidth(i);
			sheet.autoSizeColumn(i);
			if (sheet.getColumnWidth(i) < min) {
				sheet.setColumnWidth(i, min);
			} else {
				int num = sheet.getColumnWidth(i) + 512;
				sheet.setColumnWidth(i, num);
			}
		}

		// 検索条件設定
		int countWhere = -1;
		Iterator<List<String>> itrWhere = wh.iterator();
		while (itrWhere.hasNext()) {
			countWhere++;	// カウンタ

			// 行生成
			XSSFRow row = sheet.createRow(countWhere);

			// セル情報の取得
			List<String> colList = itrWhere.next();
			Iterator<String> itrCols = colList.iterator();

			int index = -1;
			while (itrCols.hasNext()) {
				index++;

				// セル（列）生成
				XSSFCell cell = row.createCell(index);
				String val = (String) itrCols.next();
				// 情報設定
				cell.setCellValue(val);

				// セルスタイル生成
				XSSFFont font1 = wb.createFont();
				font1.setFontName(font.getFontName());
				font1.setFontHeightInPoints((short) 10);

				XSSFCellStyle style = wb.createCellStyle();
				style.setFont(font1);
				if (countWhere>0 && index==0){
					style.setAlignment(HorizontalAlignment.RIGHT);	// 右揃え
				}

				// セルスタイル設定
				cell.setCellStyle(style);
			}
		}

		// 条件付き書式の設定
		conditionalFormat(sheet, endTitleRow + 1);

		return wb;
	}
}
