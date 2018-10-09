package com.zoicapital.stockchartsfx.active;

import com.zoicapital.stockchartsfx.Stock;
import com.zoicapital.stockchartsfx.stock.Stocks;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Java8 {

    public static void main(String[] args) {

        List<Stock> stocks = new Stocks().getStockList(e -> {
            if (e.getCode().startsWith("2") || e.getCode().startsWith("1") || e.getCode().startsWith("5") || e.getName().contains("ST")||e.getCode().startsWith("9")) {
                return false;
            }
            return true;
        }).value();
        stocks.forEach(e-> System.out.println(e.getCode()+e.getName()));

    }
}

class Person{
   public String  name;
   public Person(String name){this.name = name;}

   public void setName(String name){
       this.name = name;
   }
    @Override
    public String toString() {
        return this.name;
    }
}
