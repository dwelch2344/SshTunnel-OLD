package co.ntier.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

	
	public Map<String, Object> mapFields(Object o) throws IllegalAccessException{
		Map<String, Object> result = new HashMap<String, Object>();
	
		for(Field field: o.getClass().getDeclaredFields()){
			field.setAccessible(true);
			String name = field.getName();
			Object value = field.get(o);
			result.put(name, value);
			// TODO log 
		}
		
		return result;
	}
	
	public void mapFields(Object o, Map<String, Object> map) throws IllegalArgumentException, IllegalAccessException{
		for(Field field: o.getClass().getDeclaredFields()){
			field.setAccessible(true);
			String name = field.getName();
			if(map.keySet().contains(name)){
				
				Object value = map.get(name);
				if(field.getType() == Integer.TYPE 
						&& value.getClass() == String.class){
					value = Integer.valueOf(value.toString());
				}
				
				field.set(o, value);
			}
		}
	}
	
	
	public void save(Object o, File file) throws IllegalAccessException, FileNotFoundException, IOException{
		Map<String, Object> map = mapFields(o);
		
		
		Properties prop = new Properties();
		for(String key : map.keySet()){
			prop.put(key, String.valueOf(map.get(key)));
		}
		
		prop.store(new FileOutputStream(file), " SshTunnel Configuration");
	}
	
	public void load(Object o, File file) throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException{
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		
		Map<String, Object> map = new HashMap<String, Object>();
		for(Object key : prop.keySet()){
			String name = key.toString();
			String value = prop.getProperty(name);
			map.put(name, value);
		}
		
		mapFields(o, map);
		
	}
	
}
