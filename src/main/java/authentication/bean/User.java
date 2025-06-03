/*
 * 作成日: 2007/10/26
 *
 */
package authentication.bean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * ユーザーを表すクラス <br />
 */
public class User implements Serializable {

	/** ユーザーコード */
	private int cd_user_;

	/** ID */
	private String id_;

	/** ﾊﾟｽﾜｰﾄﾞ */
	private String pass_;

	/** 名称 */
	private String name_;

	/** 権限 */
	private String cd_auth_;

	/** パスワード有効期限 */
	private String dt_pw_term_;

	/** 権限 */
	private int pos_;

	/** 権限名 */
	private String posname_;

	private String[] poslist_;

	/** グループ */
	private int group_;
	/** グループ名 */
	private String groupname_;

	private String[] grouplist_;

	/** 姓 */
	private String nm_family_;

	/** 名 */
	private String nm_name_;

	/** ロゴパス */
	private String logo_;

	/** カスタムプロパティ */
	private String custom_value_;


	/** 各値の最大桁数:ﾊﾟｽﾜｰﾄﾞ 半角20桁 */
	public static final int LENGTH_PASS = 20;
	/**
	 * コンストラクタ <br /> 全ての値にNull(int型は0)を設定する。 <br />
	 */
	public User() {
		this.cd_user_ = 0;
		this.id_ = null;
		this.pass_ = null;
		this.name_ = null;
		this.nm_family_ = null;
		this.nm_name_ = null;
		this.cd_auth_ = null;
		this.dt_pw_term_ = null;
		this.group_ = 0;
		this.pos_ = 0;
		this.poslist_ = null;
		this.grouplist_ = null;
	}

	/**
	 * コンストラクタ
	 * @param id
	 * @param pass
	 * @param name
	 * @param auth
	 * @param position
	 */
	public User(int user, String id, String pass, String name, int group,	int position) {
		this.cd_user_ = user;
		this.id_ = id;
		this.pass_ = pass;
		this.name_ = name;
		this.group_ = group;
		this.pos_ = position;
	}

	/**
	 *
	 * @param user CD_USER
	 * @param id USER_ID
	 * @param pass PASSWORD
	 * @param name NAME
	 * @param dt_pw_term パスワード有効期限
	 * @param group CD_GROUP
	 * @param position CD_POSITION
	 * @param custom CUSTOM_VALUE
	 */
	public User(int user, String id, String pass, String name, String dt_pw_term, int group, int position, String custom) {
		this.cd_user_ = user;
		this.id_ = id;
		this.pass_ = pass;
		this.name_ = name;
		this.dt_pw_term_ = dt_pw_term;
		this.group_ = group;
		this.pos_ = position;
		this.custom_value_ = custom;
	}

	/**
	 * 2012/02/07追加
	 * @param user
	 * @param id
	 * @param pass
	 * @param name
	 * @param cd_auth
	 * @param dt_pw_term
	 * @param group
	 * @param position
	 * @param custom
	 */
	public User(int user, String id, String pass, String name, String cd_auth, String dt_pw_term, int group, int position, String custom, String logo) {
		this.cd_user_ = user;
		this.id_ = id;
		this.pass_ = pass;
		this.name_ = name;
		this.cd_auth_ = cd_auth;
		this.dt_pw_term_ = dt_pw_term;
		this.group_ = group;
		this.pos_ = position;
		this.custom_value_ = custom;
		this.logo_ = logo;
	}

	/**
	 * @param id
	 * @param pass
	 * @param name
	 * @param nm_family
	 * @param nm_name
	 * @param dt_pw_term パスワード有効期限
	 * @param i ?
	 */
	public User(int user, String id, String pass, String name,	String nm_family, String nm_name, String cd_auth, String dt_pw_term, int i) {
		this.cd_user_ = user;
		this.id_ = id;
		this.pass_ = pass;
		this.name_ = name;
		this.nm_family_ = nm_family;
		this.nm_name_ = nm_name;
		this.cd_auth_ = cd_auth;
		this.dt_pw_term_ = dt_pw_term;
	}

	/**
	 * @param id
	 * @param pass
	 * @param name
	 * @param nm_family
	 * @param nm_name
	 * @param auth
	 * @param position
	 */
	public User(int user, String id, String pass, String name,	String nm_family, String nm_name, int group, int position) {
		this.cd_user_ = user;
		this.id_ = id;
		this.pass_ = pass;
		this.name_ = name;
		this.nm_family_ = nm_family;
		this.nm_name_ = nm_name;
		this.group_ = group;
		this.pos_ = position;
	}

	// ロール配列
	public User(int user, String id, String pass, String name, String nm_family, String nm_name, String[] grouplist, String[] position) {
		this.cd_user_ = user;
		this.id_ = id;
		this.pass_ = pass;
		this.name_ = name;
		this.nm_family_ = nm_family;
		this.nm_name_ = nm_name;
		this.grouplist_ = grouplist;
		this.poslist_ = position;
	}

	// ロール配列
	public User(String id, String pass, String name, String nm_family, String nm_name, String[] grouplist, String[] position) {
		this.id_ = id;
		this.pass_ = pass;
		this.name_ = name;
		this.nm_family_ = nm_family;
		this.nm_name_ = nm_name;
		this.grouplist_ = grouplist;
		this.poslist_ = position;
	}

	/**
	 * パラメータ取得用
	 *
	 * @param name
	 * @param pass
	 */
	public User(String id,String pass) {
		this.id_ = id;
		this.pass_ = StringUtils.rightPad(pass, LENGTH_PASS);
	}


	/**
	 * @return
	 */
	public int getCD_user() {
		return cd_user_;
	}

	/**
	 * @param user
	 */
	public void setCD_user(int user) {
		this.cd_user_ = user;
	}

	/**
	 * @return custom_value_
	 */
	public String getUsers_custom() {
		return custom_value_;
	}

	public void setUsers_custom(String custom) {
		this.custom_value_ = custom;
	}

	/**
	 * @return group_
	 */
	public String[] getGrouplist() {
		return grouplist_;
	}

	/**
	 * @param grouplist グループリスト
	 */
	public void setGrouplist(String[] grouplist) {
		this.grouplist_ = grouplist;
	}

	/**
	 * @return poslist_
	 */
	public String[] getPoslist() {
		return poslist_;
	}

	/**
	 * @param position
	 */
	public void setPoslist(String[] position) {
		this.poslist_ = position;
	}

	/**
	 * @return nm_family_
	 */
	public String getNm_family() {
		return nm_family_;
	}

	/**
	 * @param nm_family
	 */
	public void setNm_family(String nm_family) {
		this.nm_family_ = nm_family;
	}

	/**
	 * @return nm_name_
	 */
	public String getNm_name() {
		return nm_name_;
	}

	/**
	 * @param nm_name
	 */
	public void setNm_name(String nm_name) {
		this.nm_name_ = nm_name;
	}

	/**
	 * @return id_ を戻す。 <br />
	 */
	public String getId() {
		return id_;
	}

	/**
	 * @param id
	 *            id を設定。 <br />
	 */
	public void setId(String id) {
		this.id_ = id;
	}

	/**
	 * @return name_ を戻す。 <br />
	 */
	public String getName() {
		return name_;
	}

	/**
	 * @param name
	 *            name を設定。 <br />
	 */
	public void setName(String name) {
		this.name_ = name;
	}

	/**
	 * @return pass_ を戻す。 <br />
	 */
	public String getPass() {
		return pass_;
	}

	/**
	 * @param pass
	 *            pass を設定。 <br />
	 */
	public void setPass(String pass) {
		this.pass_ = pass;
	}

	/**
	 * グループを取得します。
	 *
	 * @return group <br />
	 */
	public int getGroup() {
		return group_;
	}

	/**
	 * グループを設定します。 <br />
	 *
	 * @param group グループ
	 */
	public void setGroup(int group) {
		this.group_ = group;
	}

	/**
	 * グループ名を取得します。
	 *
	 * @return グループ名
	 */
	public String getGroupname_() {
		return groupname_;
	}

	/**
	 * グループ名を設定します。
	 *
	 * @param groupname_ グループ名
	 */
	public void setGroupname_(String groupname_) {
		this.groupname_ = groupname_;
	}

	/**
	 * @return pos_
	 */
	public int getPos() {
		return pos_;
	}

	/**
	 * @param pos_
	 *            設定する pos_
	 */
	public void setPos(int pos) {
		this.pos_ = pos;
	}

	/**
	 * 権限名を取得します。
	 *
	 * @return 権限名
	 */
	public String getPosname_() {
		return posname_;
	}

	/**
	 * 権限名を設定します。
	 *
	 * @param posname_ 権限名
	 */
	public void setPosname_(String posname_) {
		this.posname_ = posname_;
	}

	/**
	 * パスワード有効期限(YYYYMMDD)を取得
	 *
	 * @return パスワード有効期限
	 */
	public String getDt_pw_term_() {
		if (dt_pw_term_ == null) {
			return "20401231";
		} else {
			return dt_pw_term_;
		}
	}

	/**
	 * パスワード有効期限を設定
	 * @param dt_pw_term_
	 */
	public void setDt_pw_term_(String dt_pw_term_) {
		this.dt_pw_term_ = dt_pw_term_;
	}

	/**
	 * 権限の取得
	 * @return
	 */
	public String getCd_auth_() {
		return cd_auth_;
	}
	/**
	 * 権限の設定
	 * @param cd_auth_
	 */
	public void setCd_auth_(String cd_auth_) {
		this.cd_auth_ = cd_auth_;
	}


	/**
	 * ロゴパスの取得
	 * @return logo_
	 */
	public String getLogo_() {
		return logo_;
	}

	/**
	 * ロゴパスの設定
	 * @param logo_ the logo_ to set
	 */
	public void setLogo_(String logo_) {
		this.logo_ = logo_;
	}


	/**
	 * DB形式のユーザーIDを取得
	 * @return DB形式のユーザーID を戻す。 <br />
	 *
	 */
	public String getDBFormatId() {
		return getId();
	}

	/**
	 * DB形式のパスワードを取得
	 * @return DB形式のパスワード を戻す。 <br />
	 *
	 */
	public String getDBFormatPass() {
		return StringUtils.rightPad(getPass(), LENGTH_PASS);
	}
}
