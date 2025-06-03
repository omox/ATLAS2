/*
 * 作成日: 2007/10/26
 *
 */
package authentication.dbaccess;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.ObjectUtils;

import authentication.connection.DBConnection;

/**
 * SYS_LOGテーブルにアクセスする
 */
public class DBinfo {

	/**
	 * SQL
	 */

	// ログ書き込み用
	public static final String ID_SQL_INSERT_SYSLOGS =
 "insert into AK_SYS_LOGS(USER_ID,NM_USER,DT_ACTION,CD_ACTION,REMARK) values(?,?,current_timestamp,?,?)";
	public static final String	ID_SQL_INSERT_SYSLOGS_	= "insert into AK_SYS_LOGS(USER_ID,DT_ACTION,CD_ACTION,REMARK) values(?,current_timestamp,?,?)";

	// お知らせ書き込み用(1件)
	public static final String ID_SQL_INSERT_INFO = "INSERT INTO ATLAS.AK_SYS_INFO(DT_ACTION,INFORMATION) values(current_timestamp,?)";
	public static final String ID_SQL_UPDATE_INFO = "UPDATE ATLAS.AK_SYS_INFO SET DT_ACTION = CURRENT_DATE, INFORMATION = ?";

	// お知らせ読み込み用(1件)
	public static final String ID_SQL_SELECT_INFO = "SELECT INFORMATION FROM ATLAS.AK_SYS_INFO";

	// // お知らせ読み込み用(権限別)：ATLASでは使用しない。利用する場合は、テーブルを作成する。
	// public static final String ID_SQL_SELECT_INFO_1 =
	// "select " +
	// "  T1.CD_INFO " +
	// "  , case  " +
	// "    when T1.DT_START BETWEEN TO_CHAR(TO_DATE(CURRENT_DATE, 'yyyy/mm/dd') -10 ) AND TO_CHAR(CURRENT_DATE, 'yyyy/mm/dd') " +
	// "    then 1  " +
	// "    else 0  " +
	// "    end as FLG " +
	// "  , case  " +
	// "    when T1.DT_START is not null  " +
	// "    then T1.DT_START || ' ' || T1.TITLE  " +
	// "    else T1.TITLE  " +
	// "    end as TITLE " +
	// "  , T1.INFORMATION  " +
	// "from " +
	// "  ATLAS.AK_M_INFO T1  " +
	// "where " +
	// "  (  " +
	// "    (T1.FLG_DISP_ALWAYS = '" + Consts.INFO_ALWAYS + "')  " +
	// "    or (  " +
	// "      TO_CHAR(CURRENT_DATE, 'yyyy/mm/dd') between T1.DT_START and T1.DT_END " +
	// "    ) " +
	// "  )  ";

	// // お知らせ読み込み用(権限別)：ATLASでは使用しない。利用する場合は、テーブルを作成する。
	// public static final String ID_SQL_SELECT_INFO_2_1 =
	// "  and EXISTS(  " +
	// "    select " +
	// "      - 1  " +
	// "    from " +
	// "      ATLAS.AK_M_INFO_AUTH T2  " +
	// "    where " +
	// "      T2.CD_GROUP = ?  " +
	// "      and T2.CD_POSITION = ?  " +
	// "      and T2.CD_INFO = T1.CD_INFO " +
	// "  )  ";

	// // お知らせ読み込み用(権限別)：ATLASでは使用しない
	// public static final String ID_SQL_SELECT_INFO_2_2 =
	// "  and EXISTS(  " +
	// "    select " +
	// "      - 1  " +
	// "    from " +
	// "      ATLAS.AK_M_INFO_AUTH T2  " +
	// "      inner join ATLAS.AK_SYS_USER_GROUP T3  " +
	// "        on ( " +
	// "          T3.CD_USER = ?  " +
	// "          and T3.CD_GROUP = T2.CD_GROUP)  " +
	// "      inner join ATLAS.AK_SYS_USER_POS T4  " +
	// "        on (  " +
	// "          T4.CD_USER = ?  " +
	// "          and T4.CD_POSITION = T2.CD_POSITION " +
	// "        )  " +
	// "    where " +
	// "      T2.CD_INFO = T1.CD_INFO " +
	// "  )  ";
	//
	// // お知らせ読み込み用(権限別)：ATLASでは使用しない
	// public static final String ID_SQL_SELECT_INFO_3 =
	// "order by " +
	// "  case T1.FLG_DISP_ALWAYS  " +
	// "    when '" + Consts.INFO_ALWAYS + "' then 1  " +
	// "    else 2  " +
	// "    end " +
	// "  , T1.DT_START desc " +
	// "  , T1.NO_DISP asc " +
	// "  , T1.CD_INFO asc ";

	/**
	 * ログ書き込み
	 * 
	 * @param id_user ユーザーID
	 */
	public void setLogMenu(String id_user) {
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			qr.update(ID_SQL_INSERT_SYSLOGS_, new Object[] { id_user, "login", " " });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ログ書き込み
	 *
	 * @param id_user ユーザーID
	 * @param nm_user ユーザー名
	 */
	public void setLogMenu(String id_user, String nm_user) {
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			qr.update(ID_SQL_INSERT_SYSLOGS, new Object[] { id_user, nm_user, "login", " " });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * お知らせの書き込み
	 *
	 */
	public void setInfoValue(String txt_val, int cd_user) {
		try {
//			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
//			Connection con = qr.getDataSource().getConnection();
//
//			qr.update(
//				con,
//				ID_SQL_UPDATE_INFO,
//				new Object[] {
//						txt_val,
//						cd_user
//				}
//			);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * お知らせの書き込み(1件)<br/>
	 * SYS_INFOテーブルに、お知らせ情報を登録する。
	 *
	 * @param txt_val 入力お知らせ情報
	 */
	public void setInfoValue(String txt_val) {
		Connection con = null;
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			con = qr.getDataSource().getConnection();
			// UPDATE
			int cnt = qr.update(ID_SQL_UPDATE_INFO, new Object[] { txt_val });
			System.out.println("UPDATE pkey db[sys_info]:COUNT:" + cnt);
			if (cnt == 0) {
				// INSERT
				cnt = qr.update(ID_SQL_INSERT_INFO, new Object[] { txt_val });
				System.out.println("INSERT pkey db[sys_info]:COUNT:" + cnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// /**
	// * お知らせ読み込み(権限別)
	// *
	// */
	// public ArrayList<String[]> getInfoValue(int cd_user) {
	// ArrayList<String[]> rtn = new ArrayList<String[]>();
	// Connection con = null;
	// try {
	// String sql = "";
	// if (Consts.INFO_FREE_CD == cd_user) {
	// sql = ID_SQL_SELECT_INFO_1 + ID_SQL_SELECT_INFO_2_1 + ID_SQL_SELECT_INFO_3;
	// } else {
	// sql = ID_SQL_SELECT_INFO_1 + ID_SQL_SELECT_INFO_2_2 + ID_SQL_SELECT_INFO_3;
	// }
	//
	// QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
	// con = qr.getDataSource().getConnection();
	// ResultSetHandler rsh = new MapListHandler();
	// List<Map> list = (List<Map>) qr.query(con, sql, rsh, new Object[] { cd_user, cd_user });
	// if (list != null) {
	// for (Map mu : list) {
	// String[] str = new String[2];
	//
	// if ("1".equals(mu.get("FLG").toString())) {
	// str[0] = "<img src=\"../frame/img/new.gif\" alt=\"new\"> " +
	// "<b>" + mu.get("TITLE").toString() + "</b>";
	// } else {
	// str[0] = "<b>" + mu.get("TITLE").toString() + "</b>";;
	// }
	//
	// str[1] = ObjectUtils.toString(mu.get("INFORMATION"));
	//
	// rtn.add(str);
	// }
	// }
	// con.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (con != null) {
	// con.close();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return rtn;
	// }

	/**
	 * お知らせ読み込み(1件)<br/>
	 * SYS_INFOテーブルより、お知らせ情報を取得する。
	 *
	 * @return お知らせ情報(1件)
	 */
	public List<String[]> getInfoValue(int cd_user) {
		String rtn = "";
		try {
			rtn = getInfoValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String[]> list = new ArrayList<String[]>();
		list.add(new String[] { rtn });
		return list;
	}

	/**
	 * お知らせ読み込み(1件)<br/>
	 * SYS_INFOテーブルより、お知らせ情報を取得する。
	 *
	 * @return お知らせ情報(1件)
	 */
	public String getInfoValue() {
		String rtn = "";
		Connection con = null;
		try {
			QueryRunner qr = new QueryRunner(DBConnection.getDataSource());
			con = qr.getDataSource().getConnection();
			ResultSetHandler rsh = new MapListHandler();
			List<Map> list = (List<Map>) qr.query(con, ID_SQL_SELECT_INFO, rsh);
			if (!list.isEmpty()) {
				for (Map mu : list) {
					rtn = ObjectUtils.toString(mu.get("INFORMATION")).trim();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rtn;
	}

}