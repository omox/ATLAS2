/*
 * 作成日: 2006/08/29
 *
 */
package authentication.dbaccess;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import authentication.connection.DBConnection;
import authentication.defines.SQL;
import authentication.io.FileReadWriter;

/**
 * ユーザーメンテナンス画面で、DBとの接続・データ処理を行うクラス
 */
public class DownloadDAO {
	/**
	 * ユーザー情報設定 <br />
	 * 画面にて変更を行った売上実績データをDBへ反映させる。 <br />
	 *
	 * @param filetype ファイルタイプ <br />
	 * @param filepath ファイルパス<br />
	 * @param paramData SQLパラメータ<br />
	 * @throws Exception 例外 <br />
	 */
	public void setOutputFile(String filetype, String filepath, List<String> paramData) throws Exception {
		Connection con = null; // コネクション

		try {
			/* コネクションの取得 */
			con = DBConnection.getConnection();

			String outdata = "";
			String strSQL = "";
			int columnNum = 0;
			String cutMoji = ",";

			//ファイルタイプによってSQLを変更する。
			if (filetype.equals("0")) {
				outdata += SQL.MOK_HEAD + "\n";
				strSQL = SQL.MOK_SEL_001;
				columnNum=9;

			} else if (filetype.equals("1")) {
				outdata += SQL.KAWASE_HEAD + "\n";
				strSQL = SQL.KAWASE_SEL_001;
				columnNum=7;

			} else if (filetype.equals("2")) {	// 新規獲得_計画数
				outdata += SQL.HEAD_SEL_002 + "\n";
				strSQL = SQL.DOWN_SEL_002;
				columnNum=15;

			} else if (filetype.equals("3")) {	// 新規獲得_媒体別費用（新規獲得CPO）
				outdata += SQL.HEAD_SEL_003 + "\n";
				strSQL = SQL.DOWN_SEL_003;
				columnNum=16;

			} else if (filetype.equals("4")) {	// プロモ商品計画数（通店プロモ商品速報）
				outdata += SQL.HEAD_SEL_004 + "\n";
				strSQL = SQL.DOWN_SEL_004;
				columnNum=12;
//				cutMoji = "\t";	// タブ区切り文字

			} else if (filetype.equals("5")) {	// 商品実績_カテゴリ
				outdata += SQL.HEAD_SEL_005 + "\n";
				strSQL = SQL.DOWN_SEL_005;
				columnNum=12;

			} else if (filetype.equals("6")) {	// 商品実績_重点商材
				outdata += SQL.HEAD_SEL_006 + "\n";
				strSQL = SQL.DOWN_SEL_006;
				columnNum=12;

			} else if (filetype.equals("7")) {	// 新規獲得_商材指定
				outdata += SQL.HEAD_SEL_007 + "\n";
				strSQL = SQL.DOWN_SEL_007;
				columnNum=12;

			}

			System.out.println(strSQL);
			/* ステートメントの取得 */
			PreparedStatement stmt = con.prepareStatement(strSQL);

			// パラメータ判断
			if (paramData != null) {
				for (int i = 0; i < paramData.size(); i++) {
					stmt.setString((i + 1), (String) paramData.get(i));
				}
			}

			/* レコードセットの取得 */
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				for (int i=1;i<=columnNum;i++){
					outdata += rs.getString(i)==null?"":rs.getString(i).trim();
					if (i<columnNum){
						outdata += cutMoji;
					}
				}
				outdata += "\n";
			}

			/* 接続解除 */
			stmt.close();
			rs.close();
			con.close();

			outdata = new String(outdata.getBytes("windows-31j"), "windows-31j");	// SJIS > windows-31j

			FileReadWriter frw = new FileReadWriter(filepath);
			frw.write(outdata, "windows-31j");// Shift_JIS > windows-31j

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
