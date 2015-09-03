/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Apriori;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author c137165
 */

/*Generating sets and counting subsets */
public class Aprori {
    public static void main(String[] args) throws IOException {
        int threshold_count=1;
        FileReader fw=new FileReader("transactions.txt"); //Reading file
        char arr[]=new char[1000000];
        fw.read(arr);
        String st=new String(arr);
        HashMap<HashSet,Integer> masterhashset=new HashMap<HashSet,Integer>();
        StringTokenizer s=new StringTokenizer(st);
        HashMap<String,HashSet> hs=new HashMap<String,HashSet>();//Transaction and the items
        HashMap<Integer,Integer> sls=new HashMap<Integer,Integer>();//Count of each item
        ArrayList<HashSet> check=new ArrayList<HashSet>();//All the transactions 
        HashSet<Integer> Mastersale=new HashSet<Integer>();//Contains the unique items in transaction
        while(s.hasMoreTokens()){
            String temp=s.nextToken("\n");
            //System.out.println(temp);
            String temp2[]=temp.split(" ");
            
            HashSet<Integer> sales=new HashSet<Integer>();
            
            for(int i=1;i<temp2.length;i++)
            {
                sales.add(Integer.parseInt(temp2[i]));
                int t3=Integer.parseInt(temp2[i]);
                //System.out.println(t3);
                Mastersale.add(t3);
                if(sls.containsKey(t3))
                    sls.put(t3,sls.get(t3)+1);
                else
                    sls.put(t3, 1);
                    
            }   
            check.add(sales);
            hs.put(temp2[0], sales);
            
        }
        
        System.out.print(Mastersale.toString());
        System.out.println(hs.toString());
        System.out.println(sls.toString());
        System.out.println(check.toString());
        System.out.println(sls.toString());
        System.out.println(Mastersale.toString());
        System.out.println("Master sale size: "+Mastersale.size());
        int set_number=1;
        
            ArrayList<HashSet> set_set=new ArrayList<HashSet>();
        //Generating singular sets
        for(int i=1;i<Math.pow(2, Mastersale.size());i++)
        {
            String bit=Integer.toBinaryString(i);
            String bit2=bit;
            //System.out.println(bit.length());
            for(int j=0;j<(Mastersale.size()-bit.length());j++)
                bit2="0"+bit2;
            int one_count=0;
            for(int j=0;j<bit2.length();j++)
                if(bit2.charAt(j)=='1')
                    one_count++;
            if(one_count == set_number){
            //System.out.println(bit2+"first round");
            HashSet<Integer> temp9=new HashSet<Integer>();
            for(int j=0;j<bit2.length();j++)
                if(bit2.charAt(j)=='1')
                    temp9.add((Integer)(Mastersale.toArray())[j]);
            set_set.add(temp9);
            }
        }
        System.out.println(set_set.toString());
        while(set_number==1){
            //While the number of generated set is greater than 1
        HashMap<HashSet,Integer> counting=new HashMap<HashSet,Integer>();
        for(int i=0;i<set_set.size();i++){
            int count_value=0;
            for(int j=0;j<check.size();j++)
            {
               // System.out.println(check.get(j)+": Set taken......");
                if(check.get(j).containsAll(set_set.get(i)))
                    count_value=count_value+1;
            }
            //System.out.println(set_set.get(i).toString()+"::::::"+count_value);
            counting.put(set_set.get(i), count_value);
        }
        //Generate count for each set
        System.out.println(counting.toString());
        //Remove the sets with counts less than threshold count
        set_number=0;
        Set<HashSet> e=counting.keySet();
        Iterator p=e.iterator();
        HashMap<HashSet,Integer> counting2=new HashMap<HashSet,Integer>();
        while(p.hasNext()){
            HashSet<Integer> hashing=(HashSet<Integer>) p.next();
            if(counting.get(hashing)>threshold_count){
                 counting2.put(hashing,counting.get(hashing));
            }
           
        }
        //Modified set array
        System.out.println("After modification: ");
        System.out.println(counting2.toString());
        Set<HashSet> f=counting2.keySet();
        Iterator q=f.iterator();
        ArrayList<HashSet> hello=new ArrayList<HashSet>();
        while(q.hasNext()){
            HashSet<Integer> hashing=(HashSet<Integer>) q.next();
            hello.add(hashing);
            masterhashset.put(hashing, counting2.get(hashing));
        }
        System.out.println("Generated arraylist: "+hello.toString());
        set_set.clear();
        for(int k=0;k<hello.size();k++)
            for(int l=k+1;l<hello.size();l++)
            {
                HashSet<Integer> tempset=new HashSet<Integer>();
                //Checking if it should be merged
                int flag=0;
                HashSet<Integer> n1set=hello.get(k);
                HashSet<Integer> n2set=hello.get(l);
                ArrayList<Integer> n1array=new ArrayList<Integer>();
                ArrayList<Integer> n2array=new ArrayList<Integer>();
                n1array.addAll(n1set);
                n2array.addAll(n2set);
                for(int n1=0;n1<n1array.size()-1;n1++)
                    if(n1array.get(n1) != n2array.get(n1))
                    {
                        flag=1;
                        break;
                    }
                if(flag == 0){
                tempset.addAll(hello.get(k));
                tempset.addAll(hello.get(l));
                set_set.add(tempset);  
                }
            }
        //Generate new set by merging previous sets
        System.out.println("New set_set: "+set_set.toString());
        if(set_set.size()>1)
            set_number=1;
            
        }
        
        System.out.println(masterhashset.toString());
       
    }
}
