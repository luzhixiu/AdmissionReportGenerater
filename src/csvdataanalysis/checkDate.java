/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvdataanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Zhixiu.Lu
 */
public class checkDate {

    static int weekNumber = 1;
    static int year=2017;
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(checkDate("09/02/2016"));
        
    }

    static public boolean checkDate(String s) throws FileNotFoundException {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        Date validDate=weekNumberToDate();
        try {
            date = df.parse(s);
            if (date.compareTo(validDate)<0)return true;
            else return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    static public Date weekNumberToDate() throws FileNotFoundException {
        String StartDate="";
        Map<String,String> map=new HashMap();
        Scanner sc=new Scanner(new File("EntryYearStartDateConfig.txt"));
        while(sc.hasNext()){
            String line=sc.nextLine();
            map.put(line.split(",")[0],line.split(",")[1]);
        }
        System.out.println(map.get("2017"));
        StartDate=map.get(year+"");
        Date dt=new Date(StartDate);
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        for (int i=0;i<weekNumber;i++){
            c.add(Calendar.DATE, 7);
        }
        dt = c.getTime();
        return dt;
        

    }

}
