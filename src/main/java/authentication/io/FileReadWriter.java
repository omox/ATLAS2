/*
 * 作成日: 2007/10/26
 */
package authentication.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * ファイル読み取り、書き込みを制御するクラス
 */
public class FileReadWriter {

	private StringBuffer sb;

	private String path;

	/**
	 * コンストラクタ
	 * 
	 * @param filepath 対象ファイルパス
	 */
	public FileReadWriter(String filepath) {
		this.path = filepath;
		sb = new StringBuffer();
	}

	/**
	 * ファイル読み取り
	 * 
	 * @param rString 改行文字
	 * @return 1行を改行文字で区切った、ファイル内容
	 */
	public String read(String rString) {
		this.readFile(rString, "UTF-8");
		return this.sb.toString();
	}

	/**
	 * ファイル読み取り
	 * 
	 * @param rString 改行文字
	 * @return 1行を改行文字で区切った、ファイル内容
	 */
	public String read_f(String rString) {
		this.readFile_f(rString, "UTF-8");
		return this.sb.toString();
	}

	/**
	 * ファイル読込み
	 * 
	 * @param sep 行区切り
	 * @param charset 指定キャラセット
	 */
	private void readFile(String sep, String charset) {
		try {
			// ファイルへのストリームを生成
			FileInputStream fis = new FileInputStream(path);
			// バッファとストリームの「橋」を生成
			InputStreamReader isr = new InputStreamReader(fis, charset);
			BufferedReader br = new BufferedReader(isr);
			// BufferedReader br = new BufferedReader(new FileReader(path)); //
			// ファイルを開く

			while (br.ready()) {
				sb.append(br.readLine().toString() + sep);
			}
			br.close(); // ファイルを閉じる
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ファイル読込み
	 * 
	 * @param sep 行区切り
	 * @param charset 指定キャラセット
	 */
	private void readFile_f(String sep, String charset) {
		try {
			// ファイルへのストリームを生成
			FileInputStream fis = new FileInputStream(path);
			// バッファとストリームの「橋」を生成
			InputStreamReader isr = new InputStreamReader(fis, charset);
			BufferedReader br = new BufferedReader(isr);
			// BufferedReader br = new BufferedReader(new FileReader(path)); //
			// ファイルを開く

			while (br.ready()) {
				sb.append(br.readLine().toString() + sep);
			}
			br.close(); // ファイルを閉じる
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ファイル書き込み
	 * 
	 * @param str 文字列
	 */
	public void write(String str) {
		this.sb.append(str);
		this.writeFile("UTF-8");
	}

	/**
	 * ファイル書き込み
	 * 
	 * @param str 文字列
	 * @param charset 指定キャラセット
	 */
	public void write(String str, String charset) {
		this.sb.append(str);
		this.writeFile(charset);
	}

	/**
	 * ファイル書き込み
	 * @param charset 指定キャラセット
	 */
	private void writeFile(String charset) {
		try {
			// ファイルへのストリームを生成
			FileOutputStream fis = new FileOutputStream(path);
			// FileInputStream fis = new FileInputStream(path);
			// バッファとストリームの「橋」を生成JISAutoDetect
			// Shift_jis
			OutputStreamWriter isr = new OutputStreamWriter(fis, charset);
			BufferedWriter bw = new BufferedWriter(isr);

			// BufferedWriter bw = new BufferedWriter(new FileWriter(path)); //
			// ファイルを開く
			bw.write(this.sb.toString());
			bw.close(); // ファイルを閉じる
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
