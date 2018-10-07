package com.zoicapital.stockchartsfx.active;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Java8 {

    public static void main(String[] args) {

        List<Person> persons = Stream.of(new Person("1"), new Person("2")).collect(Collectors.toList());

        persons.stream().flatMapToInt(e-> IntStream.of(Integer.parseInt(e.name)));

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
