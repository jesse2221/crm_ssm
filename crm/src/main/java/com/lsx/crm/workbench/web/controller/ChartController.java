package com.lsx.crm.workbench.web.controller;

import com.lsx.crm.workbench.domain.FunnelVo;
import com.lsx.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChartController {
    @Autowired
    private TranService tranService;


    @RequestMapping("/workbench/chart/transaction/index.do")
    public String index(){
        return "workbench/chart/transaction/index1";
    }

    @RequestMapping("workbench.chart/transaction/queryCountOfTranGroupByStage.do")
    public @ResponseBody Object queryCountOfTranGroupByStage(){
        List<FunnelVo> funnelVoList = tranService.queryCountOfTranGroupByStage();
        return funnelVoList;
    }
}
