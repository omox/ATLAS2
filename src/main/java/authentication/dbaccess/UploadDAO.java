/*
 * 作成日: 2006/08/29
 *
 */
package authentication.dbaccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import authentication.connection.DBConnection;
import authentication.defines.SQL;
import authentication.io.FileReadWriter;
import authentication.util.Tips;
/**
 * ユーザーメンテナンス画面で、DBとの接続・データ処理を行うクラス
 */
public class UploadDAO {

	/**
	 * SQL
	 */



	/**
	 * ユーザー情報設定 <br />
	 * 画面にて変更を行った売上実績データをDBへ反映させる。 <br />
	 *
	 * @param filetype ファイルタイプ <br />
	 * @param filepath ファイルパス<br />
	 * @throws Exception 例外 <br />
	 */
	public void setImportFile(String filetype, String filepath) throws Exception {
		Connection con = null; // コネクション

		try {
			/* コネクションの取得 */
			con = DBConnection.getConnection();

			Tips tip = new Tips();

			int count = 0;

			String insSQL = "";		//INSERT
			String updSQL = "";		//UPDATE
			String cntSQL = "";		//COUNT

			//ファイルタイプによってSQLを変更する。
			if (filetype.equals("0")) {
				insSQL = SQL.MOK_INS_001;
				updSQL = SQL.MOK_UPD_001;
				cntSQL = SQL.MOK_SEL_002;
			} else if (filetype.equals("1")) {
				insSQL = SQL.KAWASE_INS_001;
				updSQL = SQL.KAWASE_UPD_001;
				cntSQL = SQL.KAWASE_SEL_002;
			}

			//ファイルを開く
			FileReadWriter fr = new FileReadWriter(filepath); // ファイルを開く
			String line;
			String[] linearr = fr.read("\n").split("\n", -1);
			long row = 0L;	//行数カウント
			int i = 0;
			for (i = 0; i < linearr.length; i++) { // 1行ずつ読み込む
				row++;	//行数カウントアップ
				//1行目はヘッダー扱いとして読み飛ばす
				if (row > 1) {
					line = linearr[i].replaceAll("\"", "");	//「"」を削除する
					if (line.length() > 0) {
						String[] strdata = line.split(",", -1);
						//前後のスペース削除
						int j = 0;		//行数カウント
						for (j = 0; j < strdata.length; j++) {
							strdata[j] = strdata[j].trim();
						}
						PreparedStatement stmt = null;
						count = getCount(filetype, strdata, cntSQL);
						if (count > 0) {
							//UPDATE
							stmt = con.prepareStatement(updSQL);
							if (filetype.equals("0")) {
								stmt.setString(1, tip.NulltoBlank(strdata[0]));									// MOK_URI_YM
								stmt.setString(2, tip.NulltoBlank(strdata[1]));									// MOK_CHANEL_CD1
								stmt.setString(3, tip.setZero(tip.NulltoBlank(strdata[2]), 2));	// MOK_CHANEL_CD2
								stmt.setString(4, tip.setZero(tip.NulltoBlank(strdata[3]), 3));	// MOK_CHANEL_CD3
								stmt.setString(5, tip.setZero(tip.NulltoBlank(strdata[4]), 2));	// MOK_CHANEL_CD4
								stmt.setLong(6, Long.parseLong(tip.BlanktoZero(strdata[5])));		// MOK_URISU
								stmt.setLong(7, Long.parseLong(tip.BlanktoZero(strdata[6])));		// MOK_SHU_GK
								stmt.setLong(8, Long.parseLong(tip.BlanktoZero(strdata[7])));		// MOK_JUN_GK
								stmt.setString(9, tip.NulltoBlank(strdata[8]));									// LAST_UPDATE
								stmt.setString(10, tip.NulltoBlank(strdata[0]));								// MOK_URI_YM
								stmt.setString(11, tip.NulltoBlank(strdata[1]));								// MOK_CHANEL_CD1
								stmt.setString(12, tip.setZero(tip.NulltoBlank(strdata[2]), 2));// MOK_CHANEL_CD2
								stmt.setString(13, tip.setZero(tip.NulltoBlank(strdata[3]), 3));// MOK_CHANEL_CD3
								stmt.setString(14, tip.setZero(tip.NulltoBlank(strdata[4]), 2));// MOK_CHANEL_CD4
							} else if (filetype.equals("1")) {
								stmt.setString(1, tip.NulltoBlank(strdata[0]));											// YM
								stmt.setString(2, tip.NulltoBlank(strdata[1]));											// CHANEL_CD1
								stmt.setString(3, tip.setZero(tip.NulltoBlank(strdata[2]), 2));			// CHANEL_CD2
								stmt.setString(4, tip.setZero(tip.NulltoBlank(strdata[3]), 3));			// CHANEL_CD3
								stmt.setString(5, tip.setZero(tip.NulltoBlank(strdata[4]), 2));			// CHANEL_CD4
								stmt.setDouble(6, Double.parseDouble(tip.BlanktoZero(strdata[5])));	// KAWASE
								stmt.setDouble(7, Double.parseDouble(tip.BlanktoZero(strdata[6])));	// RITU
								stmt.setString(8, tip.NulltoBlank(strdata[0]));											// YM
								stmt.setString(9, tip.NulltoBlank(strdata[1]));											// CHANEL_CD1
								stmt.setString(10, tip.setZero(tip.NulltoBlank(strdata[2]), 2));		// CHANEL_CD2
								stmt.setString(11, tip.setZero(tip.NulltoBlank(strdata[3]), 3));		// CHANEL_CD3
								stmt.setString(12, tip.setZero(tip.NulltoBlank(strdata[4]), 2));		// CHANEL_CD4
							}

							stmt.executeUpdate();
						} else {
							//INSERT
							stmt = con.prepareStatement(insSQL);
							if (filetype.equals("0")) {
								stmt.setString(1, tip.NulltoBlank(strdata[0]));									// MOK_URI_YM
								stmt.setString(2, tip.NulltoBlank(strdata[1]));									// MOK_CHANEL_CD1
								stmt.setString(3, tip.setZero(tip.NulltoBlank(strdata[2]), 2));	// MOK_CHANEL_CD2
								stmt.setString(4, tip.setZero(tip.NulltoBlank(strdata[3]), 3));	// MOK_CHANEL_CD3
								stmt.setString(5, tip.setZero(tip.NulltoBlank(strdata[4]), 2));	// MOK_CHANEL_CD4
								stmt.setLong(6, Long.parseLong(tip.BlanktoZero(strdata[5])));		// MOK_URISU
								stmt.setLong(7, Long.parseLong(tip.BlanktoZero(strdata[6])));		// MOK_SHU_GK
								stmt.setLong(8, Long.parseLong(tip.BlanktoZero(strdata[7])));		// MOK_JUN_GK
								stmt.setString(9, tip.NulltoBlank(strdata[8]));									// LAST_UPDATE
							} else if (filetype.equals("1")) {
								stmt.setString(1, tip.NulltoBlank(strdata[0]));											// YM
								stmt.setString(2, tip.NulltoBlank(strdata[1]));											// CHANEL_CD1
								stmt.setString(3, tip.setZero(tip.NulltoBlank(strdata[2]), 2));			// CHANEL_CD2
								stmt.setString(4, tip.setZero(tip.NulltoBlank(strdata[3]), 3));			// CHANEL_CD3
								stmt.setString(5, tip.setZero(tip.NulltoBlank(strdata[4]), 2));			// CHANEL_CD4
								stmt.setDouble(6, Double.parseDouble(tip.BlanktoZero(strdata[5])));	// KAWASE
								stmt.setDouble(7, Double.parseDouble(tip.BlanktoZero(strdata[6])));	// RITU
							}

							stmt.executeUpdate();
						}
						stmt.close();
					}
				}
			}

			/* 接続解除 */
			con.commit();
			con.close();

			/**
			 * 例外処理
			 */
		} catch (Exception e) {
			/* 接続解除 */
			if (con != null) {
				con.rollback();
				con.close();
			}
			throw e;
		}
	}

	/**
	 * ユーザー情報設定 <br />
	 * 画面にて変更を行った売上実績データをDBへ反映させる。 <br />
	 *
	 * @param filetype ファイルタイプ <br />
	 * @param filepath ファイルパス<br />
	 * @throws Exception 例外 <br />
	 */
	public void setUploadFile(String filetype, String filepath) throws Exception {
		Connection con = null; // コネクション

		try {
			/* コネクションの取得 */
			con = DBConnection.getConnection();

			Tips tip = new Tips();

			String deleteWorkSQL = "";		// 作業テーブルの削除
			String insertWorkSQL = "";		// 作業テーブルへ登録
			String deleteRecordSQL = "";	// 格納テーブルの削除
			String insertRecordSQL = "";	// 格納テーブルへ登録
			String cutMoji = ",";
			String num = "";
			int position = 0;

			// ファイルタイプによってSQLを変更する。
			if (filetype.equals("2")) {		// 新規獲得_計画数
				deleteWorkSQL = SQL.DELETE_WORK_002;
				insertWorkSQL = SQL.INSERT_WORK_002;
				deleteRecordSQL = SQL.DELETE_RECORD_002;
				insertRecordSQL = SQL.INSERT_RECORD_002;

			} else if (filetype.equals("3")) {	// 新規獲得_媒体別費用
				deleteWorkSQL = SQL.DELETE_WORK_003;
				insertWorkSQL = SQL.INSERT_WORK_003;
				deleteRecordSQL = SQL.DELETE_RECORD_003;
				insertRecordSQL = SQL.INSERT_RECORD_003;

			} else if (filetype.equals("4")) {	// プロモ商品計画数
				deleteWorkSQL = SQL.DELETE_WORK_004;
				insertWorkSQL = SQL.INSERT_WORK_004;
				deleteRecordSQL = SQL.DELETE_RECORD_004;
				insertRecordSQL = SQL.INSERT_RECORD_004;
//				cutMoji = "\t";	// タブ区切り文字

			} else if (filetype.equals("5")	// 商品実績_カタログ
					|| filetype.equals("6")	// 商品実績_重点商材
					|| filetype.equals("7")	// 新規獲得_商材指定
				) {
				deleteWorkSQL = SQL.DELETE_WORK_005;
				insertWorkSQL = SQL.INSERT_WORK_005;
				deleteRecordSQL = SQL.DELETE_RECORD_005;
				insertRecordSQL = SQL.INSERT_RECORD_005;
			}

			// 作業テーブルのクリア
			PreparedStatement stmt = null;

			stmt = con.prepareStatement(deleteWorkSQL);
			stmt.executeUpdate();

			// ファイルを開く
			System.out.println(filepath);
			File file = new File(filepath);
			//文字コードShift-JISを指定してファイルを読み込む
			FileInputStream input = new FileInputStream(file);
			InputStreamReader stream = new InputStreamReader(input,"windows-31j");//Shift-JIS > windows-31j
			BufferedReader buffer = new BufferedReader(stream);

			String line;
			String work;
			long row = 0L;	//行数カウント

			// 1行ずつ読み込む
			while((line = buffer.readLine()) != null){
				row++;	//行数カウントアップ
				//1行目はヘッダー扱いとして読み飛ばす
				if (row > 1) {
					byte[] b = line.getBytes();
					//文字コードをUTF-8に変換する
					line = new String(b, "UTF-8");
					line = line.replaceAll("\"", "");	//「"」を削除する

					if (line.length() > 0) {
						String[] strdata = line.split(cutMoji, -1);
						//前後のスペース削除
						int j = 0;		//行数カウント
						for (j = 0; j < strdata.length; j++) {
							strdata[j] = strdata[j].trim();
						}


						if (filetype.equals("2")) {		// 新規獲得_計画数
							for(int k=1;k<=12;k++){
								// 金額有無チェック
								if (strdata[2+k].length()>0) {

									// INSERT
									stmt = con.prepareStatement(insertWorkSQL);

									stmt.setString(1, "-");								// CHANEL
									stmt.setString(2, tip.NulltoBlank(strdata[0]));		// CLSNM
									stmt.setString(3, tip.NulltoBlank(strdata[1]));		// TYPNM
									stmt.setLong(4, Long.parseLong(tip.BlanktoZero(strdata[2])));	// YY
									stmt.setLong(5, Long.parseLong(String.valueOf(k)));				// MM
									stmt.setLong(6, Long.parseLong(tip.BlanktoZero(strdata[2+k])));	// VAL

									stmt.executeUpdate();
								}
							}

						} else if (filetype.equals("3")) {	// 新規獲得_媒体別費用（新規獲得CPO）

							// キーチェック
							if (strdata[0].length()>0 && strdata[1].length()>0 && strdata[2].length()>0){

								for(int k=4;k<strdata.length;k++){
									// 金額有無チェック
									if (strdata[k].length()>0) {

										// INSERT
										stmt = con.prepareStatement(insertWorkSQL);

										stmt.setLong(1, Long.parseLong(tip.BlanktoZero(strdata[0]+("0"+String.valueOf(k-3)).substring(("0"+String.valueOf(k-3)).length()-2) )));	// YMD
										stmt.setLong(2, Long.parseLong(tip.BlanktoZero(strdata[1])));	// CHANEL
										stmt.setLong(3, Long.parseLong(tip.BlanktoZero(strdata[2])));	// UCD

										// 金額 値なし=nullを設定する。
										num = strdata[k].replaceAll(",","").replaceAll("\"", "");
										position = num.indexOf(".");
										if (position != -1) num = num.substring(0,position);
										stmt.setLong(4, Long.parseLong(tip.BlanktoZero(num)));			// GK
										//stmt.setLong(4, Long.parseLong(tip.BlanktoZero(strdata[k])));	// GK

										stmt.executeUpdate();
									}
								}
							}

						} else if (filetype.equals("4")) {	// プロモ商品計画数（通店プロモ商品速報）

							// INSERT
							stmt = con.prepareStatement(insertWorkSQL);

							stmt.setLong(1, Long.parseLong(tip.BlanktoZero(strdata[0])));	// YM
							stmt.setLong(2, Long.parseLong(tip.BlanktoZero(strdata[1])));	// CHANEL
							stmt.setLong(3, Long.parseLong(tip.BlanktoZero(strdata[2])));	// KHNCD

							// CATCD 補完
							work="00"+strdata[3].replaceFirst("^'", "");	// 先頭の'を削除
							work=work.substring(work.length()-2);			// 先頭0埋2桁
							stmt.setString(4, tip.NulltoBlank(work));	// CATCD

							stmt.setLong(5, Long.parseLong(tip.BlanktoZero(strdata[4])));			// CPFLG

							// CATCD 補完
							work=strdata[5].replaceFirst("^'", "");		// 先頭の'を削除
							// CPFLG=1の場合
							if ("1".equals(strdata[4])){
								work="000000"+work;
								work=work.substring(work.length()-6);	// 先頭0埋6桁
							}
							stmt.setString(6, tip.NulltoBlank(work));	// ITEMNO

							// PMONTH
							num = tip.NulltoBlank(strdata[6]);
							stmt.setString(7, num.substring(0,num.length()>299?299:num.length()));

							stmt.setLong(8, Long.parseLong(tip.BlanktoZero(strdata[7])));	// PRODT_ST
							stmt.setLong(9, Long.parseLong(tip.BlanktoZero(strdata[8])));	// PRODT_LT

							// PMONTH
							num = tip.NulltoBlank(strdata[9]);
							stmt.setString(10, num.substring(0,num.length()>47?47:num.length()));

							// 個数 値なし=nullを設定する。
							num = strdata[10].replaceAll(",","").replaceAll("\"", "");
							if (num.length()==0) {
								stmt.setObject(11, null);				// SU
							}else {
								position = num.indexOf(".");
								if (position != -1) num = num.substring(0,position);
								stmt.setLong(11, Long.parseLong(num));	// SU
							}
							// 金額 値なし=nullを設定する。
							num = strdata[11].replaceAll(",","").replaceAll("\"", "");
							if (num.length()==0) {
								stmt.setObject(12, null);				// KG
							}else {
								position = num.indexOf(".");
								if (position != -1) num = num.substring(0,position);
								stmt.setLong(12, Long.parseLong(num));	// KG
							}
							stmt.setLong(13, Long.parseLong(tip.BlanktoZero(strdata[7]))-10000);	// PRODT_ZST
							stmt.setLong(14, Long.parseLong(tip.BlanktoZero(strdata[8]))-10000);	// PRODT_ZLT

							stmt.executeUpdate();


						} else if (filetype.equals("5")	// 商品実績_カタログ
								|| filetype.equals("6")	// 商品実績_重点商材
								|| filetype.equals("7") // 新規獲得_商材指定
								) {
							// INSERT
							stmt = con.prepareStatement(insertWorkSQL);

							stmt.setLong(1, Long.parseLong(tip.BlanktoZero(strdata[0])));	// YMD
							stmt.setLong(2, Long.parseLong(tip.BlanktoZero(strdata[1])));	// CKSNM
							stmt.setString(3, tip.NulltoBlank(strdata[2]));					// TYPNM
							stmt.setString(4, tip.setZero(tip.NulltoBlank(strdata[3]), 2));	// CLS1
							stmt.setString(5, tip.setZero(tip.NulltoBlank(strdata[4]), 2));	// CLS2
							stmt.setString(6, tip.setZero(tip.NulltoBlank(strdata[5]), 2));	// CLS3
							stmt.setString(7, tip.setZero(tip.NulltoBlank(strdata[6]), 2));	// CLS4
							stmt.setString(8, tip.setZero(tip.NulltoBlank(strdata[7]), 2));	// CLS5
							stmt.setString(9, tip.setZero(tip.NulltoBlank(strdata[8]), 2));	// CLS6
							stmt.setString(10,tip.setZero(tip.NulltoBlank(strdata[9]), 2));	// ICD
							stmt.setString(11,tip.NulltoBlank(strdata[10]));				// INM
							stmt.setLong(12, Long.parseLong(tip.BlanktoZero(strdata[11])));	// SEQ

							stmt.executeUpdate();

						}

						stmt.close();
					}
				}
			}
			buffer.close();

			// 格納テーブルのクリア
			stmt = con.prepareStatement(deleteRecordSQL);
			stmt.executeUpdate();
			stmt.close();

			// 格納テーブルへ登録
			stmt = con.prepareStatement(insertRecordSQL);
			stmt.executeUpdate();
			stmt.close();

			/* 接続解除 */
			con.commit();
			con.close();

			/**
			 * 例外処理
			 */
		} catch (Exception e) {
			/* 接続解除 */
			if (con != null) {
				con.rollback();
				con.close();
			}
			throw e;
		}
	}

	/**
	 * データ件数 <br />
	 * 売上実績変更分データ件数を取得する。 <br />
	 *
	 * @param filetype ファイルタイプ <br />
	 * @param strdata データ<br />
	 * @param strSQL SQL<br />
	 * @return 売上実績変更分データ件数
	 * @throws Exception 例外 <br />
	 */
	public int getCount(String filetype, String[] strdata, String strSQL) throws Exception {

		Connection con = null; // コネクション
		Tips tip = new Tips();

		try {
			/* コネクションの取得 */
			con = DBConnection.getConnection();

			PreparedStatement stmt = con.prepareStatement(strSQL);

			if (filetype.equals("0")) {
				stmt.setString(1, tip.NulltoBlank(strdata[0]));		// MOK_URI_YM
				stmt.setString(2, tip.NulltoBlank(strdata[1]));		// MOK_CHANEL_CD1
				stmt.setString(3, tip.setZero(tip.NulltoBlank(strdata[2]), 2));		// MOK_CHANEL_CD2
				stmt.setString(4, tip.setZero(tip.NulltoBlank(strdata[3]), 3));		// MOK_CHANEL_CD3
				stmt.setString(5, tip.setZero(tip.NulltoBlank(strdata[4]), 2));		// MOK_CHANEL_CD4
			} else if (filetype.equals("1")) {
				stmt.setString(1, tip.NulltoBlank(strdata[0]));		// YM
				stmt.setString(2, tip.NulltoBlank(strdata[1]));		// CHANEL_CD1
				stmt.setString(3, tip.setZero(tip.NulltoBlank(strdata[2]), 2));		// CHANEL_CD2
				stmt.setString(4, tip.setZero(tip.NulltoBlank(strdata[3]), 3));		// CHANEL_CD3
				stmt.setString(5, tip.setZero(tip.NulltoBlank(strdata[4]), 2));		// CHANEL_CD4
			}

			ResultSet rs = stmt.executeQuery();

			int count = 0;

			while (rs.next()) {
				count = rs.getInt(1);
			}

			stmt.close();

			con.close();

			return count;

			/**
			 * 例外処理
			 */
		} catch (Exception e) {
			/* 接続解除 */
			if (con != null) {
				con.close();
			}
			throw e;
		}
	}

}
