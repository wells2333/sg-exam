package com.github.tangyi.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author tangyi
 * @date 2018/11/26 22:29
 */
public class MapUtil {

    /**
     * 从map集合中获取属性值
     *
     * @param map          map集合
     * @param key          键对
     * @param defaultValue 默认值
     * @return E
     */
    @SuppressWarnings({"unchecked"})
    public final static <E> E get(Map<String, Object> map, Object key, E defaultValue) {
        Object o = map.get(key);
        if (o == null)
            return defaultValue;
        return (E) o;
    }

    /**
     * 获取map中key对应的值，转成字符串
     *
     * @param map map集合
     * @param key 键对
     * @return String 对应的键值，并格式为字符串
     */
    public final static String getString(Map<String, Object> map, String key) {
        Object o = map.get(key);
        if (o != null) {
            return o.toString();
        }
        return "";
    }

    /**
     * 获取map中key对应的值，转成Date
     *
     * @param map map集合
     * @param key 键对
     * @return Date 对应的键值，并格式为Date类型
     */
    public final static Date getDate(Map<String, Object> map, String key) {
        Object o = map.get(key);
        if (o != null) {
            return (Date) o;
        }
        return null;
    }

    /**
     * 获取map中key对应的值，转成Double类型
     *
     * @param map map集合
     * @param key 键对
     * @return Double 对应的键值，并格式为Double类型
     */
    public final static Double getDouble(Map<String, Object> map, String key) {
        Object o = map.get(key);
        return ObjectUtil.obj2Double(o);
    }

    /**
     * 获取map中key对应的值，转成布尔类型
     *
     * @param map map集合
     * @param key 键对
     * @return Boolean 对应的键值，并格式为Boolean类型
     */
    public final static Boolean getBooleanValue(Map<String, Object> map, String key) {
        Object o = map.get(key);
        if (o != null) {
            return (Boolean) o;
        }
        return null;
    }

    /**
     * Map集合对象转化成 JavaBean集合对象
     *
     * @param clazz   clazz
     * @param mapList Map数据集对象
     */
    @SuppressWarnings(value={"uncheck","deprecation"})
    public static <T> List<T> map2Java(Class<T> clazz, List<Map<String, Object>> mapList)
            throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            T bean;
            for (Map<String, Object> map : mapList) {
                bean = clazz.newInstance();
                map2Java(bean, map);
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * Map对象转化成 JavaBean对象
     *
     * @param javaBean JavaBean实例对象
     * @param map      Map对象
     */
    @SuppressWarnings(value={"unchecked","deprecation"})
    public static <T> T map2Java(T javaBean, Map<String, Object> map) {
        try {
            Map<String, Object> collectionObj = new HashMap<>();
            for (String key : map.keySet()) {
                Field temFiels;
                if (key.contains(".")) {
                    // 对象中属性类型是对象
                    String[] temp = StringUtils.split(key, ".");
                    temFiels = getDeclaredField(javaBean, temp[0]);
                    Object o;
                    if (Objects.requireNonNull(temFiels).getType().equals(Object.class)) {
                        o = javaBean.getClass().newInstance();
                        ReflectionUtil.setFieldValue(o, temp[1], map.get(key));
                        BeanUtils.copyProperties(javaBean, o);
                    } else if (Objects.requireNonNull(temFiels).getType().equals(List.class)) {
                        if (!collectionObj.containsKey(temp[0])) {
                            o = new ArrayList<>();
                            collectionObj.put(temp[0], o);
                        }
                        o = collectionObj.get(temp[0]);
                        // 获取f字段的通用类型
                        Type fc = temFiels.getGenericType();
                        // 如果不为空并且是泛型参数的类型
                        if (fc instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) fc;
                            Type[] types = pt.getActualTypeArguments();
                            // 只处理一个泛型参数
                            if (types != null && types.length == 1) {
                                Object tempObj = ((Class<?>) types[0]).newInstance();
                                ReflectionUtil.setFieldValue(tempObj, temp[2], map.get(key));
                                ((List) o).add(tempObj);
                                BeanUtils.setProperty(javaBean, temp[0], o);
                            }
                        }
                    } else {
                        o = temFiels.getType().newInstance();
                        ReflectionUtil.setFieldValue(o, temp[1], map.get(key));
                        BeanUtils.setProperty(javaBean, temp[0], o);
                    }
                } else {
                    // 属性是普通的数据类型
                    BeanUtils.copyProperty(javaBean, key, map.get(key));
                }
            }
            return javaBean;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 循环向上转型
     *
     * @param object    子类对象
     * @param fieldName fieldName
     * @return Field
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
            }
        }
        return null;
    }


    /**
     * JavaBean集合对象转化成Map集合对象
     *
     * @param javaBeanList JavaBean数据集对象
     * @return List
     */
    public static <E> List<Map<String, Object>> java2Map(List<E> javaBeanList) {
        if (javaBeanList == null) {
            return null;
        }
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map;
        for (E javaBean : javaBeanList) {
            if (javaBean != null) {
                map = java2Map(javaBean);
                mapList.add(map);
            }
        }

        return mapList;
    }

    /**
     * JavaBean对象转化成Map对象
     *
     * @param javaBean javaBean
     * @return Map
     */
    public static Map<String, Object> java2Map(Object javaBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 获取javaBean属性
            BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null && propertyDescriptors.length > 0) {
                // javaBean属性名
                String propertyName;
                // javaBean属性值
                Object propertyValue;
                for (PropertyDescriptor pd : propertyDescriptors) {
                    propertyName = pd.getName();
                    if (!propertyName.equals("class")) {
                        Method readMethod = pd.getReadMethod();
                        if (readMethod != null) {
                            propertyValue = readMethod.invoke(javaBean, new Object[0]);
                            map.put(propertyName, propertyValue);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
