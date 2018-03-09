package com.freelycar.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;

import org.hibernate.Query;

public class SqlHelper {

	
	public static Query getQuery(Object instance, Class<?> clazz , Query query) {
		try{
			BeanInfo beanInfo=Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] proDescrtptors=beanInfo.getPropertyDescriptors();
			
			if(proDescrtptors!=null && proDescrtptors.length>0){
				for(PropertyDescriptor propDesc:proDescrtptors){
					//get value
					Method methodGetUserName=propDesc.getReadMethod();
					Object value = methodGetUserName.invoke(instance);
					
					if(value!=null){
						//修饰类型
						String typeName = propDesc.getPropertyType().getSimpleName();
						
						//propertis属性
						String prop = propDesc.getName();
						//
						if(typeName.equals("Date")){
							query = query.setTimestamp(prop, (Date)value);
							continue;
						}
						
						String var = String.valueOf(value);
						if(typeName.equals("Integer")){
							query = query.setInteger(prop, Integer.parseInt(var));
						} else if (typeName.equals("Double")){
							query = query.setDouble(prop, Double.parseDouble(var));
						} else if(typeName.equals("Float")){
							query = query.setFloat(prop, Float.parseFloat(var));
						} else if(typeName.equals("Short")){
							query = query.setShort(prop, Short.parseShort(var));
						} else if(typeName.equals("Long")){
							query = query.setLong(prop, Long.parseLong(var));
						} else if(typeName.equals("String")){
							query = query.setString(prop, var);
						} else if(typeName.equals("Date")){
							query = query.setString(prop, var);
						}
					}
					
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return query;
		
	}
	
	
	public static String generatorSql(Object instance, Class<?> clazz) {
		
		StringBuilder strBuilder = new StringBuilder(512);
		strBuilder.append("from ");
		strBuilder.append(clazz.getSimpleName());
		strBuilder.append(" where ");
		try{
			BeanInfo beanInfo=Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] proDescrtptors=beanInfo.getPropertyDescriptors();
			
			if(proDescrtptors!=null && proDescrtptors.length>0){
				for(PropertyDescriptor propDesc:proDescrtptors){
					//get value
					Method methodGetUserName=propDesc.getReadMethod();
					Object value = methodGetUserName.invoke(instance);
					
					
					if(value!=null){
						//propertis属性
						String prop = propDesc.getName();
						
						strBuilder.append(prop);
						strBuilder.append(" = ");
						strBuilder.append(":");
						strBuilder.append(prop);
						strBuilder.append(" and ");
					}
					
				}
				//通过有没有:来判断是否进入for循环
				int infor = strBuilder.lastIndexOf(":");
				if(infor>=0){
				    int lastIndexOf = strBuilder.lastIndexOf("and");
				    strBuilder.replace(lastIndexOf, strBuilder.length(), "");
				} else {
				    int whereIndex = strBuilder.lastIndexOf("where");
				    strBuilder.replace(whereIndex-1, whereIndex+6, "");
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
        
        return strBuilder.toString();		
		
	}
	
	
	
	public static String genUpdateSql(Object instance, Class<?> clazz) {
		
		StringBuilder strBuilder = new StringBuilder(512);
		strBuilder.append("update ");
		strBuilder.append(clazz.getSimpleName());
		strBuilder.append(" set ");
		try{
			BeanInfo beanInfo=Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] proDescrtptors=beanInfo.getPropertyDescriptors();
			
			if(proDescrtptors!=null && proDescrtptors.length>0){
				for(PropertyDescriptor propDesc:proDescrtptors){
					//get value
					Method methodGetUserName=propDesc.getReadMethod();
					Object value = methodGetUserName.invoke(instance);
					
					if(value!=null){
						//propertis属性
						String prop = propDesc.getName();
						
						strBuilder.append(prop);
						strBuilder.append(" = ");
						strBuilder.append(":");
						strBuilder.append(prop);
						strBuilder.append(" , ");
					}
					
				}
				
				int lastIndexOf = strBuilder.lastIndexOf(",");
				if(lastIndexOf>=0){
					strBuilder.replace(lastIndexOf, strBuilder.length(), "");
				}

				strBuilder.append("where id = :id");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return strBuilder.toString();		
		
	}
	
	public static String genUpdateSqlBySpecifyId(Object instance,String specifyId, Class<?> clazz) {
		
		StringBuilder strBuilder = new StringBuilder(512);
		strBuilder.append("update ");
		strBuilder.append(clazz.getSimpleName());
		strBuilder.append(" set ");
		try{
			BeanInfo beanInfo=Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] proDescrtptors=beanInfo.getPropertyDescriptors();
			
			if(proDescrtptors!=null && proDescrtptors.length>0){
				for(PropertyDescriptor propDesc:proDescrtptors){
					//get value
					Method methodGetUserName=propDesc.getReadMethod();
					Object value = methodGetUserName.invoke(instance);
					
					if(value!=null){
						//propertis属性
						String prop = propDesc.getName();
						
						strBuilder.append(prop);
						strBuilder.append(" = ");
						strBuilder.append(":");
						strBuilder.append(prop);
						strBuilder.append(" , ");
					}
					
				}
				
				int lastIndexOf = strBuilder.lastIndexOf(",");
				if(lastIndexOf>=0){
					strBuilder.replace(lastIndexOf, strBuilder.length(), "");
				}
				
				strBuilder.append("where "+specifyId+" = :"+specifyId);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return strBuilder.toString();		
		
	}
	
	public static void main(String[] args) {
		
	}
	
}
