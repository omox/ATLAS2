package authentication.connection;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import authentication.defines.Message.LDAPErr;

/**
 * 指定された LDAP サーバに接続し、認証状態を確認する
 */
public class LDAPjndi extends HttpServlet {

	/** 接続先 */
	private Object[] urls = null;
	private Object[] dns = null;
	private String userName	= "";

	/**
	 * web.xmlよりLDAP接続用の情報を全て取得
	 *
	 * @param config Servlet設定
	 */
	public LDAPjndi(ServletConfig config) {
		ServletContext context = config.getServletContext();
		// URL取得
		int i = 1;
		String url = null;
		do {
			url = context.getInitParameter("LdapUrl" + i);
			if (StringUtils.isNotEmpty(url)) {
				urls = ArrayUtils.add(urls, url);
				i++;
			}
		} while (url != null);

		i = 1;
		String dn = null;
		do {
			dn = context.getInitParameter("LdapDN" + i);
			if (StringUtils.isNotEmpty(dn)) {
				dns = ArrayUtils.add(dns, dn);
				i++;
			}
		} while (dn != null);
	}

	/**
	 * 指定された LDAP サーバに接続し、認証状態を取得する。<br/>
	 * エラーの場合はメッセージを返す。
	 *
	 * @param url 接続先URL
	 * @param dn 接続先ドメイン名
	 * @param user 接続ユーザID
	 * @param bindpw 接続ユーザのパスワード
	 * @return 正常：null 異常：エラーメッセージ
	 */
	public String connect(String url, String dn, String user, String bindpw) {
		// 接続ユーザID@ドメイン名の形式取得
		String binddn = user + "@";
		String[] tmpdns = StringUtils.split(dn, ",");
		for (String tmp : tmpdns) {
			if (StringUtils.startsWithIgnoreCase(StringUtils.trim(tmp), "DC")) {
				binddn += StringUtils.trim(StringUtils.mid(tmp, StringUtils.indexOf(tmp, "=") + 1, tmp.length())) + ".";
			}
		}
		binddn = StringUtils.removeEnd(binddn, ".");

		// ★★ LDAP接続環境を設定します Start
		// ここで指定する情報は外部ファイル化するのが良いでしょう
		Hashtable<String, String> env = new Hashtable<String, String>();

		// Contextファクトリを設定
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		// 接続先URL(ホスト，ポート番号)を設定
		env.put(Context.PROVIDER_URL, url);

		// 接続ユーザIDを設定します
		// ※このユーザはオブジェクト作成権限を持つ必要があります
		env.put(Context.SECURITY_PRINCIPAL, binddn);

		// 接続ユーザのパスワードを設定します。
		env.put(Context.SECURITY_CREDENTIALS, bindpw);

		// ★★ LDAP接続環境を設定します End
		// 設定した環境を元にコンテキストを生成します
		try {
			InitialDirContext context = new InitialDirContext(env);
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// 検索結果のエントリを取得
			NamingEnumeration<SearchResult> result = context.search(dn, "sAMAccountName=" + user, searchControls);

			// エントリを1件ずつ処理
			while (result.hasMore()) {
				SearchResult sr = result.next(); // エントリ
				NamingEnumeration<? extends Attribute> attributes = sr.getAttributes().getAll();
				// エントリに含まれる属性を1件ずつ取得して処理
				while (attributes.hasMore()) {
					Attribute attr = (Attribute) attributes.nextElement();
					if (StringUtils.equals(attr.getID(), "displayName")) {
						Enumeration<?> values = attr.getAll();
						while (values.hasMoreElements()) {
							String val = (String) values.nextElement();
							userName += val;
						}
						break;
					}
				}
			}

			result.close();
			context.close();
			System.out.println("認証OK");
		} catch (Exception e) {
			// スタックトレースの出力
			e.printStackTrace();
			return e.getMessage();
		}
		return null;
	}

	/**
	 * LDAPエラー情報から出力エラー列挙子を設定
	 *
	 * @param errMsg エラーメッセージ
	 * @return エラー列挙子
	 */
	private LDAPErr getErrorEnum(String errMsg) {
		for (LDAPErr err : LDAPErr.values()) {
			for (String val : err.getVals()) {
				if (StringUtils.contains(errMsg, val)) {
					return err;
				}
			}
		}
		return LDAPErr.OTHER;
	}

	/**
	 * 指定された LDAP サーバに接続し、認証状態を確認する
	 *
	 * @param user 入力ユーザーID
	 * @param password 入力パスワード
	 *
	 * @return 正常：null 異常：エラーメッセージ
	 */
	public HashMap<String, String> connect(String user, String password) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (urls == null || dns == null) {
			map.put("success", "false");
			map.put("message", LDAPErr.OTHER.getTxt());
			return map;
		}
		LDAPErr err = null;
		for (Object url : urls) {
			for (Object dn : dns) {
				String rtn = connect(ObjectUtils.toString(url), ObjectUtils.toString(dn), user, password);
				if (rtn == null) {
					map.put("success", "true");
					map.put("name", userName);
					return map;
				}
				LDAPErr tmp = getErrorEnum(rtn);
				if (err == null || err.getIdx() > tmp.getIdx()) {
					err = tmp;
				}
			}
		}
		map.put("success", "false");
		map.put("message", err.getTxt());
		return map;
	}
}