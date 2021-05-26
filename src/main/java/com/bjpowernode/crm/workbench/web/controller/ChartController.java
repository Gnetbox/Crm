package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.Impl.TransactionServiceImpl;
import com.bjpowernode.crm.workbench.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ChartController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();

        if(("/workbench/chart/getCharts.do").equals(path)){
            getCharts(request,response);
        }
    }

    //拿到交易表中的数据
    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        TransactionService ts = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Map<String,Object> map = ts.getCharts();
        PrintJson.printJsonObj(response,map);
    }
}
