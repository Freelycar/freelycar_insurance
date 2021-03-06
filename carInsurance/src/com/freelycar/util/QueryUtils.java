package com.freelycar.util;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;


/**
 * query生成辅助
 * @author 阿飞哥
 *
 */
public class QueryUtils {

	
	/**
	 * 替换下面的
	 * */
	
	private Query query;
	private Session session;
	private StringBuilder sb;
	private boolean isCache;//是否开启缓存
	
	private static final String eq = "=";
	private static final String gt = ">";
	private static final String lt = "<";
	private static final String gte = ">=";
	private static final String lte = "<=";
	private static final String inScope = "inScope";//在日期范围内
	private static final String bol = "bol";//布尔类型
	private static final String inList = "inList"; // in List
	//暂时注释
	//private static final String notInScope = "notInScope";
	private static final String like = "like";
	private static final String desc = "desc";
	private static final String asc = "asc";
	
	
	
	private Map<String,Object> params = new HashMap<>();
	
	//专门为日期区间的map
	private Map<String,Date> paramsDate = new HashMap<>();
	
	private final static Logger log = Logger.getLogger(QueryUtils.class);

	
	public QueryUtils(Session session,String fromQuery){
		this.session = session;
		this.query = session.createQuery(fromQuery);
		sb = new StringBuilder(1024);
		sb.append(fromQuery);
		
		isCache = true;
	}
	

	/**增加int 查询参数*/
	//状态 为负数 这里是查询所有 ,如果你要非要查负数 请转成字符串 #addString
	public QueryUtils addInteger(String name,int value){
		if(value > -1){
			params.put(name, value);
			getHql(name,eq);
		}
		return this;
	}
	
	/**增加String 查询参数*/
	public QueryUtils addString(String name,String value){
		if(CommonUtils.isNotEmpty(value)){
			params.put(name, value);
			getHql(name,eq);
		}
		return this;
	}

	/**增加String 模糊查询参数*/
	public QueryUtils addStringLike(String name,String value){
		if(CommonUtils.isNotEmpty(value)){
			params.put(name, value);
			getHql(name,like);
		}
		return this;
	}
	
	/**增加String 模糊查询参数*/
	public QueryUtils addBoolean(String name,boolean value){
		params.put(name, value);
		getHql(name,like);
		return this;
	}
	
	/**增加Date ==查询参数*/
	public QueryUtils addDate(String name,Date value){
		if(value != null){
			params.put(name, value);
			getHql(name,eq);
		}
		return this;
	}
	
	
	/**增加Date >查询参数*/
	public QueryUtils addDateGt(String name,Date value){
		if(value != null){
			params.put(name, value);
			getHql(name,gt);
		}
		return this;
	}
	
	/**增加Date >=查询参数*/
	public QueryUtils addDateGte(String name,Date value){
		if(value != null){
			params.put(name, value);
			getHql(name,gte);
		}
		return this;
	}
	
	/**增加Date <查询参数*/
	public QueryUtils addDateLt(String name,Date value){
		if(value != null){
			params.put(name, value);
			getHql(name,lt);
		}
		return this;
	}
	
	/**增加Date <=查询参数*/
	public QueryUtils addDateLte(String name,Date value){
		if(value != null){
			params.put(name, value);
			getHql(name,lte);
		}
		return this;
	}
	
	/**增加Date 在时间范围内 查询参数*/
	public QueryUtils addDateInScope(String name,Date startTime,Date endTime){
		if(startTime != null && endTime != null){
			paramsDate.put("startTime", startTime);
			paramsDate.put("endTime", endTime);
			getHql(name,inScope);
		} else if(startTime != null && endTime == null){
			paramsDate.put("startTime", startTime);
			getHql(name,inScope);
		} else if(startTime == null && endTime != null){
			paramsDate.put("endTime", endTime);
			getHql(name,inScope);
		}
		return this;
	}
	
	/**增加list 查询参数*/
	public QueryUtils addInList(String name, Object[] list){
		if(list != null && list.length>0){
			params.put(name, list);
			getHql(name,inList);
		}
		return this;
	}
	
	
	
	
	/**根据 name desc倒序*/
	public QueryUtils addOrderByDesc(String name){
		getHql(name,desc);
		return this;
	}
	
	/**根据 name asc正序*/
	public QueryUtils addOrderByAsc(String name){
		getHql(name,asc);
		return this;
	}
	
	public QueryUtils setFirstResult(int from){
		query.setFirstResult(from);
		return this;
	}
	
	public QueryUtils setMaxResults(int pagesize){
		query.setMaxResults(pagesize);
		return this;
	}
	
	/**
	 * 构造带新的参数的hql语句
	 * @param name
	 * @param opera 操作符
	 */
	private void getHql(String name,String opera) {
		if(sb.indexOf("where") == -1){
			sb.append(" where");
		}
		//判断是 equals查询还是like查询
		if(eq.equals(opera)){
			sb.append(" and "+name+" = :"+name);
		} else if(lt.equals(opera)){
			sb.append(" and "+name+" < :"+name);
		} else if(lte.equals(opera)){
			sb.append(" and "+name+" <= :"+name);
		} else if(gt.equals(opera)){
			sb.append(" and "+name+" > :"+name);
		} else if(gte.equals(opera)){
			sb.append(" and "+name+" >= :"+name);
		} else if(inScope.equals(opera)){
			if(paramsDate.containsKey("startTime") && paramsDate.containsKey("endTime")){
				sb.append(" and "+name+" >= :startTime and "+name+" <= :endTime");
			} else if(paramsDate.containsKey("startTime")){
				sb.append(" and "+name+" >= :startTime");
			} else if(paramsDate.containsKey("endTime")){
				sb.append("and "+name+" <= :endTime");
			}
		} else if(inList.equals(opera)){
			sb.append(" and "+name+" in (:"+name+")");
		} else if(like.equals(opera)){
			sb.append(" and "+name+" like concat('%', :"+name+", '%')");
		} else if(bol.equals(opera)){
			sb.append(" and "+name+" = :"+name);
		}
		
		/**去掉where后面的and */
		int where = sb.indexOf("where");
		int index = sb.indexOf(" and ", where);
		if(index == where+5){
			sb.delete(index, index+4);
		} 
		
		/**增加 order by 语句*/
		if(desc.equals(opera)){
			sb.append(" order by "+name+" desc");
		} else if(asc.equals(opera)){
			sb.append(" order by "+name+" asc");
		}
		
		/**去掉order by前面的where */
		//因为 order by 如果前面直接跟着where 那肯定不行
		int orderby = sb.indexOf(" order by ", where);
		if(orderby == where+5){
			sb.delete(where, where+5);
		} 
		
		//构建新的query
		try{
			query = session.createQuery(sb.toString());
		}catch(Exception e){
			log.error("出错的sql语句:  "+sb.toString());
			e.printStackTrace();
		}
		
		if(paramsDate.containsKey("startTime") && paramsDate.containsKey("endTime")){
			query.setTimestamp("startTime", (Date) paramsDate.get("startTime"));
			query.setTimestamp("endTime", (Date) paramsDate.get("endTime"));
		} else if(paramsDate.containsKey("startTime")){
			query.setTimestamp("startTime", (Date) paramsDate.get("startTime"));
		} else if(paramsDate.containsKey("endTime")){
			query.setTimestamp("endTime", (Date) paramsDate.get("endTime"));
		}
		addEntity();
	}
	
	/**
	 * 设置query的查询参数
	 * @return
	 */
	
	private void addEntity(){
		for(Map.Entry<String, Object> map : params.entrySet()){
			Object value = map.getValue();
			if(value instanceof Integer){
				Integer new_name = (Integer) value;
				query.setInteger(map.getKey(), new_name);
			}
			
			//String查询的时候可能有like查询
			else if(value instanceof String){
				String new_name = (String) value;
				query.setString(map.getKey(), new_name);
			}
			
			else if(value instanceof Date){
				Date new_name = (Date) value;
				query.setTimestamp(map.getKey(), new_name);
			}
			
			else if(value instanceof Boolean){
				boolean new_name = (Boolean) value;
				query.setBoolean(map.getKey(), new_name);
			}
			
			else if(value instanceof Object[]){
				Object[] new_name = (Object[]) value;
				query.setParameterList(map.getKey(), new_name);
				//System.out.println("new name" + Arrays.toString(new_name));
			}
			//System.out.println(map.getKey()+"-------"+value);
		}
		
		query.setCacheable(isCache);
	}
	
	
	//返回query
	public Query getQuery() {
		return query;
	}
}
