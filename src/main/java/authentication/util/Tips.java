package authentication.util;

/**
 * 文字列操作Tipsクラス
 */
public class Tips {

	/** 左側の空白を除去する */
	public String lTrim(String target){

		if (target != null){
			return target.replaceAll("^ +","");
		}else{
			return target;
		}
	}

	/** 右側の空白を除去する */
	public String rTrim(String target){

		if (target != null){
			return target.replaceAll(" +$","");
		}else{
			return target;
		}
	}

	/** 空白の場合"0"(文字列)を返す */
	public String BlanktoZero(String str){
		if (str == null || str.trim().length() == 0){
			return "0";
		}else{
			return str;
		}
	}
	/** NULLの場合""を返す */
	public String NulltoBlank(String str){
		if (str == null){
			return "";
		}else{
			return str;
		}
	}

	/** 足りない桁数分"0"を足す */
	public String setZero(String str,int len){
		if (str == null || str.trim().length() == 0){
			return "";
		}else{
			if (str.trim().length() < len){
				int cnt = len - str.trim().length();
				for (int i = cnt;i>0;i--){
					str = "0" + str.trim();
				}
			}
			return str;
		}
	}

	/** 受け取ったデータに年月日を付加する */
	public String setDatename(String str){
		if (str.trim().length()==6){
			//yyyyMM
			return str.trim().substring(0, 4) + "年" + str.trim().substring(4, 6) + "月";
		}else if (str.trim().length()==8){
			//yyyyMMdd
			return str.trim().substring(0,4)+"年"+str.trim().substring(4,6)+"月"+str.trim().substring(6,8)+"日";
		}else{
			//桁数が一致しなければ空白を返す
			return "";
		}
	}

	/** 文字列置換 */
	public String substitute(String input, String pattern, String replacement) {
		// 置換対象文字列が存在する場所を取得
		int index = input.indexOf(pattern);

		// 置換対象文字列が存在しなければ終了
		if(index == -1) {
			return input;
		}

		// 処理を行うための StringBuffer
		StringBuffer buffer = new StringBuffer();

		buffer.append(input.substring(0, index) + replacement);

		if(index + pattern.length() < input.length()) {
			// 残りの文字列を再帰的に置換
			String rest = input.substring(index + pattern.length(), input.length());
			buffer.append(substitute(rest, pattern, replacement));
		}
		return buffer.toString();
	}
	/** HTML用エスケープ */
	public String HTMLEscape(String input) {
		input = substitute(input, "\"", "&quot;");
		input = substitute(input, "\r", "");
		input = substitute(input, "\n<br>", "<br>");
		input = substitute(input, "<br>\n", "<br>");
		input = substitute(input, "\n", "<br>");
		return input;
	}
	/** JAVA用エスケープ */
	public String JAVAEscape(String input) {
		input = substitute(input, "\"", "\\\"");
		return input;
	}

	/** double型を正しく文字列へ変換する */
	public String convDoubleToString(double d){
		String str = Double.toString(d);

		int e_pos = str.indexOf('E');

		if (e_pos == -1) {
			// Eを含まなければそのまま出力
			// Infinity,NaN 共にEは含んでないので問題なし。
			return str;

		}
		int e = Integer.parseInt(str.substring(e_pos + 1));

		// 指数部の値
		str = str.substring(0, e_pos);
		// 実数部
		int p_pos = str.indexOf('.');
		// 小数点の位置
		String n;
		// 整数部
		String m;
			// 小数部
		if (p_pos == -1) {
			// 小数点がないなんて事は無いはずだが、念のため
			n = str;
			m = "";
		} else {		n = str.substring(0, p_pos);
			m = str.substring(p_pos + 1);
		}
		// 指数部の値を反映
		if (e > 0) {
			StringBuffer n0 = new StringBuffer(n);
			if (m.length() < e) {
				n0.append(m);
				for (int i = 0; i < e - m.length(); i++)
					n0.append('0');
				m = "";
			} else {
				n0.append(m.substring(0, e));
				m = m.substring(e);
			}		n = n0.toString();
		} else {
			StringBuffer m0 = new StringBuffer(m);
			if (n.length() < -e) {
				m0.insert(0, n);
				for (int i = 0; i < -e - n.length(); i++)
					m0.insert(0, '0');
				n = "";
			} else {
				m0.insert(0, n.substring(n.length() + e));
				n = n.substring(0, n.length() + e);
			}
			m = m0.toString();
		}
		// 整数部の先頭についている余分な0を取り除く
		int n_pos = 0;
		while (n_pos < n.length() && n.charAt(n_pos) == '0') {
			n_pos++;
		}
		n = n.substring(n_pos);
		// 小数部の末尾についている余分な0を取り除く
		int m_pos = m.length() - 1;
		while (m_pos >= 0 && m.charAt(m_pos) == '0') {
			m_pos--;
		}
		m = m.substring(0, m_pos + 1);
		// 整数部が無いと言う事は、「0.～」なので「0」にする。
		if (n.length() == 0) n = "0";
		// 小数部が無いと言う事は、結果は整数
		if (m.length() == 0) return n;
		return n + "." + m;
	}

	/** 配列をカンマ区切りの文字列にする */
	public String arr_to_StringComma(String[] strarr){
		StringBuffer str= new StringBuffer();

		//引数がNULLなら空白を返す
		if (strarr==null) return null;
		for (int i=0;i<strarr.length;i++){
			if (strarr[i]!=null){
				str.append(strarr[i].toString().trim());	//文字列設定
			}
			str.append(",");								//カンマ設定
		}

		//最終カンマを削除する
		int idx = str.length();	//最終文字のインデックスを取得する
		//最終文字が0より大きければ処理を行う
		if (idx>0){
			str.deleteCharAt(idx-1);
		}

		return str.toString();
	}
}