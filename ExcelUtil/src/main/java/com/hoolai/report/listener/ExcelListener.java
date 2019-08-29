//package com.hoolai.report.listener;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
///**
// * @author: Ksssss(chelin)
// * @create: 2019-08-23 15:08
// */
//
//public class ExcelListener extends AnalysisEventListener {
//
//    private List<Object> datas = new ArrayList<>();
//
//    /**
//     * @param o
//     * @param analysisContext
//     * 解析每一行会回调invoke
//     */
//    @Override
//    public void invoke(Object o, AnalysisContext analysisContext) {
//        System.out.println(analysisContext.getCurrentRowNum());
//
//        List result = (ArrayList)o;
//        if (result.size()>1){
//            System.out.println(result.get(1));
//        }
//        datas.add(o);
//    }
//
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        datas.clear();
//    }
//}
