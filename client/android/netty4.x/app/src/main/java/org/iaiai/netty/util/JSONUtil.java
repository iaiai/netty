package org.iaiai.netty.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * <br/>
 * Title: JSONUtil.java<br/>
 * E-Mail: 176291935@qq.com<br/>
 * QQ: 176291935<br/>
 * Http: iaiai.iteye.com<br/>
 * Create time: 2013-1-19 下午1:51:17<br/>
 * <br/>
 * 
 * @author 丸子
 * @version 0.0.1
 */
public class JSONUtil {

	private JSONUtil(){
		/** cannot be instantiated **/
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	public static List toList(Class cls, String json) {
		return toList(cls, json, null, null);
	}

	public static List toList(Class cls, String json, String[] itemsName, Class[] itemsClass) {
		if (json == null)
			return null;
		try {
			return toList(cls, new JSONArray(json), itemsName, itemsClass);
		} catch (JSONException e) {
			return null;
		}
	}

	public static List toList(Class cls, JSONArray array, String[] itemsName, Class[] itemsClass) {
		if (array == null)
			return null;
		List list = new ArrayList();

		try {
			for (int i = 0; i < array.length(); i++) {
				if(array.get(i) instanceof JSONArray){
					list.add(toList(cls, array.getJSONArray(i), itemsName, itemsClass));
				}else{
					list.add(toObject(cls, array.getJSONObject(i), itemsName, itemsClass));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static Object toObject(Class cls, JSONObject jsonObject) throws Exception {
		return toObject(cls, jsonObject, null, null);
	}

	public static Object toObject(Class cls, JSONObject jsonObject, String[] itemsName, Class[] itemsClass)
			throws Exception {
		try {
			Object object = cls.newInstance();

			// 获得对象的所有属性
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();

				//三星某手机升级到5.0系统后出现此属性，过滤
				if(fieldName!=null&&fieldName.startsWith("$"))continue;

				String stringLetter = fieldName.substring(0, 1).toUpperCase();

				// 获得相应属性的getXXX和setXXX方法名称
				String setName = "set" + stringLetter + fieldName.substring(1);

				// 获取相应的方法
				Method setMethod = cls.getMethod(setName, new Class[] { field.getType() });

				if (!jsonObject.isNull(fieldName)) {
					if (field.getType() == Integer.class || field.getType() == int.class
							|| field.getType() == String.class || field.getType() == Boolean.class
							|| field.getType() == boolean.class) {
						setMethod.invoke(object, new Object[] { jsonObject.get(fieldName) });
					}else if (field.getType() == Long.class || field.getType() == long.class) {
						setMethod.invoke(object, new Object[] { Long.parseLong(jsonObject.get(fieldName).toString()) });
					} else if (field.getType() == Date.class) {
						setMethod.invoke(object,
								new Object[] { DateUtil.stringDateFormat(jsonObject.get(fieldName).toString()) });
					} else if (field.getType() == List.class) {
						JSONArray array = jsonObject.optJSONArray(fieldName);
						List list = new ArrayList();
						if (itemsName != null) {
							for (int i = 0; i < array.length(); i++) {
								for (int j = 0; j < itemsName.length; j++) {
									if (fieldName.equals(itemsName[j])) {
										if(itemsClass[j]==String.class){
											list.add(array.getJSONObject(i).toString());
										}else {
											list.add(toObject(itemsClass[j], array.getJSONObject(i), itemsName, itemsClass));
										}
									}
								}
							}
						}
						if (list.size() > 0)
							setMethod.invoke(object, list);
					} else {
						// 对象
						if (itemsName != null) {
							for (int j = 0; j < itemsName.length; j++) {
								if (fieldName.equals(itemsName[j])) {
									if(itemsClass[j]==String.class){
										setMethod.invoke(object, new Object[] { jsonObject.get(fieldName).toString() });
									}else {
										setMethod.invoke(object, new Object[]{toObject(itemsClass[j], jsonObject.getJSONObject(fieldName),
												itemsName, itemsClass)});
									}
								}
							}
						}
					}
				}
			}

			return object;
		} catch (Exception e) {
			throw new Exception("解析数据时报错，请重新操作，如还是此问题，请与管理员联系!");
		}
	}

	public static JSONArray listToJson(List list) {
		JSONArray array = new JSONArray();

		for(Object obj:list){
			array.put(toJson(obj));
		}

		return array;
	}

	/**
	 * 把对象转成json数据
	 * 
	 * @param object
	 * @return
	 */
	public static JSONObject toJson(Object object) {
		JSONObject json = new JSONObject();
		try {
			// 获得对象的所有属性
			Field[] fields = object.getClass().getDeclaredFields();
			Field.setAccessible(fields, true); //在类外面要想获取私有属性的值必须设置
			
	        for (int i = 0; i < fields.length; i++) { //设置数组的值
	            if(fields[i].get(object)!=null){
	            	if(fields[i].getType()== Integer.class || fields[i].getType() == int.class
							|| fields[i].getType() == String.class || fields[i].getType() == Boolean.class
							|| fields[i].getType() == boolean.class){
	            		json.put(fields[i].getName(), fields[i].get(object));
	            	}else if(fields[i].getType() == Long.class || fields[i].getType() == long.class){
	            		json.put(fields[i].getName(), fields[i].get(object));
	            	} else if (fields[i].getType() == Date.class) {
	            		json.put(fields[i].getName(), DateUtil.dateStringFormat((Date) fields[i].get(object)));
	            	} else if (fields[i].getType() == List.class) {
	            		List list = (List) fields[i].get(object);
						JSONArray array = new JSONArray();
	            		for(Object obj:list){
							array.put(toJson(obj));
						}
						json.put(fields[i].getName(), array);
	            	} else {
	            		json.put(fields[i].getName(), toJson(fields[i].get(object)));
	            	}
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

}
