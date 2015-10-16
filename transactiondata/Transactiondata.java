/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transactiondata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author c137165
 */
public class Transactiondata {

    /**
     * @param args the command line arguments
     */
    
    /*For generating the transaction data */
    public static void main(String[] args) throws IOException {
        Scanner s= new Scanner(System.in);
        int no_of_trans=s.nextInt();
        File f=new File("transactions.txt");
        FileWriter fw=new FileWriter(f);
        String temp="T";
        int start=100;
        Random r=new Random();
        HashMap<String,HashSet> transactions=new HashMap<String,HashSet>();
        for(int i=0;i<no_of_trans;i++)
        {
            int t=start+i;
            String temp2=s.next();
            System.out.println(temp2);
            int noofitems=s.nextInt();
            HashSet<Integer> items=new HashSet<Integer>();
            int tempno;
            
            for(int j=0;j<noofitems;j++)
            {
                tempno=s.nextInt();
                items.add(tempno);
            }
            transactions.put(temp2, items);
            fw.write(temp2+' ');
            Object temp100[]=items.toArray();
            for(int j=0;j<temp100.length;j++)
                fw.write(temp100[j]+" ");
            fw.append("\n");
        }
        fw.close();
        System.out.print(transactions.toString());
        
    }
}
