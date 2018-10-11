package com.zoicapital.stockchartsfx.active;

import com.ly.quant.StockClient;
import com.zoicapital.stockchartsfx.DailyStock;
import com.zoicapital.stockchartsfx.Stock;
import com.zoicapital.stockchartsfx.stock.Stocks;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @Author: BG320587
 * @Date: 2018/10/11 12:11
 */
public class MACDQuota {

    public static void main(String[] args) {

        List<Stock> stocks = new Stocks().getStockList(e -> {
            if (e.getCode().startsWith("2") || e.getCode().startsWith("1") || e.getCode().startsWith("5") || e.getName().contains("ST")||e.getCode().startsWith("9")) {
                return false;
            }
            return true;
        }).value();
        //stocks = stocks.stream().filter(e->e.getCode().equals("002700")).collect(Collectors.toList());
        for(Stock stock:stocks){
            String code = stock.getCode();
            String name = stock.getName();
            if(judgeBuyQuota(code)){
                System.out.println(code +"\t"+ name+"\t"+true);
            }else{
              //  System.out.println(code +"\t"+ name+"\t"+false);
            }
        }
    }

    private static boolean judgeBuyQuota(String code) {
        List<DailyStock> dailyStock = StockClient.getDailyStock(code, "2017-09-01", "2018-10-11");
        boolean isBuy = true;
        if(CollectionUtils.isNotEmpty(dailyStock)){
            if(dailyStock.size()>5){

                List<DailyStock> endDays = dailyStock.subList(dailyStock.size() - 4, dailyStock.size());
                Double pre = endDays.get(0).getMacd();
                for(int i = 1;i<endDays.size();i++){
                    DailyStock daily = endDays.get(i);
                    if(daily.getMacd()<pre){
                        isBuy  = false;
                        break;
                    }else{
                        pre = daily.getMacd();
                    }
                }
            }else{
                isBuy = false;
            }


        }else{
            isBuy = false;
        }
        return isBuy;
    }

}
