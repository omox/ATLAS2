package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * テーブル検索情報インターフェース
 */
public interface ItemInterface {

	public abstract HashMap<String, String> getMap();

	public abstract void setMap(HashMap<String, String> map);

	public abstract String getJson();

	public abstract void setJson(String json);

	public abstract ArrayList<List<String>> getWhere();

	public abstract void setWhere(ArrayList<List<String>> where);

	public abstract ArrayList<Integer> getMeta();

	public abstract void setMeta(ArrayList<Integer> meta);

	public abstract int getStart();

	public abstract void setStart(int start);

	public abstract int getLimit();

	public abstract void setLimit(int limit);

	public abstract ArrayList<String> getParamData();

	public abstract void setParamData(ArrayList<String> paramData);

	public abstract ArrayList<List<String>> getTable();

	public abstract void setTable(ArrayList<List<String>> table);

	public abstract boolean selectBy();

	public abstract boolean selectBySQL(String Command);

	public abstract HashMap<String, String> getSetting();

	public abstract void setSetting(HashMap<String, String> setting);

}