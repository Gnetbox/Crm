package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.Impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    /*该方法使用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
      对象创建完毕后，马上执行该方法

      event：该参数能够取得监听的对象
      监听的是什么对象，就可以通过该参数取得什么对象
      例如我们现在监听的是上下文域对象 ServletContext
      如果要监听session对象，则implements HttpSessionListener
      如果要监听request对象，则implements ServletRequestListener

    */
    @Override
    public void contextInitialized(ServletContextEvent event) {

        //取得域对象
        ServletContext applicaiton = event.getServletContext();

        //取数据字典
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();

        for (String key : map.keySet()) {
            List<DicValue> dicValues = map.get(key);
            System.out.println("L38 :"+dicValues);
            //将数据字典存到 上下域中
            applicaiton.setAttribute(key,dicValues);
        }

        
        //处理Stage2Possibility.properties文件
        Map<String,String> pMap = new HashMap<>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){

            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }

        applicaiton.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}


