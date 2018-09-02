package com.ly.quant;

import com.zoicapital.stockchartsfx.DailyStock;

import java.util.ArrayDeque;
import java.util.Deque;

public class Strategy {

    private Deque<DailyStock> queue  = new ArrayDeque<DailyStock>();

    private DailyStock pre;

    public void add(DailyStock value){
        if(queue.size()==0){
            pre = null;
        }else{
            pre = queue.peekLast();
        }
        queue.addLast(value);
    }


    public boolean isBuy(DailyStock stock){
        if(pre==null){
            return false;
        }
        if (buyLowMacd(stock)) return true;
        return false;
    }
    private boolean buyLowMacd(DailyStock stock) {
        Double macd = pre.getMacd();
        if(macd < 0) {
            Double nowMacd = Math.abs(stock.getMacd());
            Double preMacd = Math.abs(macd);
            if(nowMacd*100/preMacd < 90){
                return true;
            }
        }
        return false;
    }
    private boolean buyStagety(DailyStock stock) {
        Double macd = pre.getMacd();
        if(macd< 0 && stock.getMacd()>0){
            Double macdValue = Math.abs(macd);
            if(macdValue*100 / (macdValue+stock.getMacd())<50){
                return true;
            }
        }
        return false;
    }

    public boolean isSeller(DailyStock stock){
        Double macd = pre.getMacd();
        Double currentMacd = stock.getMacd();
        if(macd<0){
            if(currentMacd<currentMacd){
                return true;
            }
        }else if(macd>0){
            if(macd > currentMacd){
                return true;
            }
        }
        return false;
    }

}
