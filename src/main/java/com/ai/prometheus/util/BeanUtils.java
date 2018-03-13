package com.ai.prometheus.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取到的List<Map>结果集转化为JavaBean工具类
 */
public class BeanUtils<T> {

    /**
     * 根据List<Map<String, Object>>数据转换为JavaBean数据
     * 
     * @param datas
     * @param beanClass
     * @return
     * @throws CommonException
     */
    public List<T> ListMap2JavaBean(List<Map<String, Object>> datas, Class<T> beanClass) throws CommonException {
        List<T> list = null;
        String fieldname = "", methodname = "", methodsetvalue = "";
        try {
            list = new ArrayList<T>();
            // 得到对象所有字段
            Field fields[] = beanClass.getDeclaredFields();
            for (Map<String, Object> mapdata : datas) {
                T t = beanClass.newInstance();
                // 遍历所有字段，对应配置好的字段并赋值
                for (Field field : fields) {
                    if (null != field) {
                        fieldname = field.getName();
                        methodname = "set" + CommonUtil.capitalize(fieldname);
                        methodsetvalue = mapdata.get(field.getName()) + "";
                        Method m = beanClass.getDeclaredMethod(methodname, field.getType());
                        m.invoke(t, methodsetvalue);
                    }
                }
                list.add(t);
            }
        } catch (InstantiationException e) {
            throw new CommonException(e, "创建beanClass实例异常");
        } catch (IllegalAccessException e) {
            throw new CommonException(e, "创建beanClass实例异常");
        } catch (SecurityException e) {
            throw new CommonException(e, "获取[" + fieldname + "] getter setter 方法异常");
        } catch (NoSuchMethodException e) {
            throw new CommonException(e, "获取[" + fieldname + "] getter setter 方法异常");
        } catch (IllegalArgumentException e) {
            throw new CommonException(e, "[" + methodname + "] 方法赋值异常");
        } catch (InvocationTargetException e) {
            throw new CommonException(e, "[" + methodname + "] 方法赋值异常");
        }

        return list;
    }

}