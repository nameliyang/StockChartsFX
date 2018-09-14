import com.zoicapital.stockchartsfx.DailyStock;
import com.zoicapital.stockchartsfx.StockHistory;
import com.zoicapital.stockchartsfx.StockSpider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: BG320587
 * @Date: 2018/8/16 13:17
 */
public class MacDTest {
    static List<Map<String, String>> list = new ArrayList<>();
    public static void main(String[] args) {

        List<Map<String, String>> stockCodes = StockSpider.getStockCodes(Stream.of('2', '5').collect(Collectors.toList()));
        stockCodes = stockCodes.stream().filter(map -> !map.get("name").contains("ST")).collect(Collectors.toList());
        List<String> lists = stockCodes.stream().map(e -> {
            return  e.get("code") + "(" + e.get("name") + ")";
        }).collect(Collectors.toList());
        System.out.println(stockCodes.size());
        final List<Map<String, String>> codes = stockCodes.stream().filter(map -> !map.get("name").contains("ST")).collect(Collectors.toList());
        try{
            for( int i = 0;i<codes.size();i++){
                List<DailyStock> test = test(codes.get(i).get("code"));
                if(test==null){
                    continue;
                }
                List<DailyStock> dailyStocks = test.subList(test.size() - 5, test.size());
                DailyStock dailyStock0 = dailyStocks.get(0);
                DailyStock dailyStock1 =dailyStocks.get(1);
                DailyStock dailyStock2 =dailyStocks.get(2);
                DailyStock dailyStock3 = dailyStocks.get(3);
                DailyStock dailyStock4 = dailyStocks.get(4);
                if(dailyStock4.getDiff() - dailyStock4.getDea()>0 &&(dailyStock3.getDiff() - dailyStock3.getDea()<0||dailyStock2.getDiff() - dailyStock2.getDea()<0) ){
                    Double d4 = dailyStock4.getDiff() - dailyStock4.getDea();
                    Double d3 = dailyStock3.getDiff() - dailyStock3.getDea();
                    Double d2 = dailyStock2.getDiff() - dailyStock2.getDea();
                    if(d4>d3&& d3>d2){
                        list.add(codes.get(i));
                    }

                }
            }
        }finally {
            System.out.println(list);
        }

    }

    public static List<DailyStock> test(String code ){
        List<DailyStock> dailyStocks = null;
        Date nowDate = new Date();
        Date before5Date = stepMonth(nowDate, -5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            dailyStocks = StockHistory.getDailyStocks(code, sdf.format(before5Date), sdf.format(nowDate));
            if(dailyStocks.size()==0){
                return null;
            }
            DailyStock preStock = dailyStocks.get(0);
            for (int i = 1; i < dailyStocks.size(); i++) {
                DailyStock dailyStock = dailyStocks.get(i);
                Double ema12;
                Double ema26;
                Double dea;
                Double diff;
                if (i == 1) {
                    ema12 = preStock.getClose() * 11 / 13 + dailyStock.getClose() * 2 / 13;
                    ema26 = preStock.getClose() * 25 / 27 + dailyStock.getClose() * 2 / 27;
                    diff = ema12 - ema26;
                    dea = 0 + diff * 2 / 10;
                } else {
                    ema12 = preStock.getEma12() * 11 / 13 + dailyStock.getClose() * 2 / 13;
                    ema26 = preStock.getEma26() * 25 / 27 + dailyStock.getClose() * 2 / 27;
                    diff = ema12 - ema26;
                    dea = preStock.getDea() * 8 / 10 + diff * 2 / 10;
                }
                dailyStock.setDea(dea);
                dailyStock.setDiff(diff);
                dailyStock.setEma12(ema12);
                dailyStock.setEma26(ema26);
                dailyStock.setDiff(diff);
                preStock = dailyStock;
                System.out.println("ema12: " + ema12 + ",ema26:" + ema26 + "，diff:" + diff + "dea " + dea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dailyStocks;
    }

    public static Date stepMonth(Date sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);

        return c.getTime();
    }
}
