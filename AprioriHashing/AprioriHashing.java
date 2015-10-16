/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author c137165
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AprioriHashing;

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


public class AprioriHashing{
    
    public static int hashing(int x,int y){
        int hash=((x-10)*10+(y-10))%7;
        return hash;
    }
    
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
        
        System.out.println(Mastersale.toString());
        System.out.println(hs.toString());
        System.out.println(sls.toString());
        System.out.println(check.toString());
        System.out.println(sls.toString());
        System.out.println(Mastersale.toString());
        System.out.println("Master sale size: "+Mastersale.size());
        int set_number=1;
        
        
        HashMap<Integer,ArrayList<HashSet>> bucket=new HashMap<Integer,ArrayList<HashSet>>();
        int bucket_count[]=new int[7];
        for(int i=0;i<7;i++)
            bucket_count[i]=0;
        for(int i=0;i<check.size();i++)
        {
            HashSet<Integer> sal=check.get(i);
            
        }
        
        
        
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
        int set_num=1;
        while(set_number==1){
           System.out.println("Set size:"+ set_num);
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
        if(set_num ==1){
            System.out.println("Size 1");
            for(int i=0;i<set_set.size();i++){
                
                for(int j=i+1;j<set_set.size();j++)
                {
                    HashSet<Integer> tempset2=new HashSet<Integer>();
                    tempset2.clear();
                    tempset2.addAll(set_set.get(i));
                    tempset2.addAll(set_set.get(j));
                    System.out.println(tempset2.toString());
                    for(int k=0;k<check.size();k++){
                        if(check.get(k).containsAll(tempset2)){
                            Object t[]=tempset2.toArray();
                            int x=(Integer)t[0];
                            int y=(Integer)t[1];
                            int hash=hashing(x,y);
                            if(bucket.containsKey(hash))
                            {
                                ArrayList tempbucket=bucket.get(hash);
                                tempbucket.add(tempset2);
                                bucket_count[hash]=bucket_count[hash]+1;
                                bucket.put(hash, tempbucket);
                            }
                            else{
                                ArrayList<HashSet> tempbucket=new ArrayList<HashSet>();
                                tempbucket.add(tempset2);
                                bucket_count[hash]=bucket_count[hash]+1;
                                bucket.put(hash, tempbucket);
                            }
                        }
                            
                    }
                }
            }
            set_set.clear();
            for(int i=0;i<7;i++){
                if(bucket_count[i]>=2)
                {
                    ArrayList<HashSet> tempup=bucket.get(i);
                    for(int j=0;j<tempup.size();j++)
                        set_set.add(tempup.get(j));
                }
            }
            System.out.println(bucket.toString());
            System.out.println(bucket_count.toString());
            System.out.println(set_set.toString());
            System.out.println(">>>>>>>>>>>>>>>>>");
            set_num++;
            if(set_set.size()>1)
            set_number=1;
        }
        else{
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
        set_num++;
        }
        }
        
        
        
        System.out.println(masterhashset.toString());
        
        //Association rules
        ArrayList<HashSet> singleset=new ArrayList<HashSet>();
        Set<HashSet> assoc=masterhashset.keySet();
        Iterator asc=assoc.iterator();
        while(asc.hasNext()){
            HashSet<Integer> hashing=(HashSet<Integer>) asc.next();
            singleset.add(hashing);
        }
        
        //Association starts here
        float min_conf=70;
        System.out.println("Single freq set: "+singleset.toString());
        HashMap<String,Float> association=new HashMap<String,Float>();
        System.out.println("Association starts.......");
        System.out.println("List before start: "+masterhashset.toString());
        Set<HashSet> assoc1=masterhashset.keySet();
        System.out.println(assoc1);
        asc=assoc1.iterator();
        while(asc.hasNext()){
            HashSet<Integer> temp_assoc=(HashSet<Integer>) asc.next();
            for(int i=0;i<singleset.size();i++){
                HashSet<Integer> temp_single=(HashSet<Integer>) singleset.get(i);
                if(temp_assoc.containsAll(temp_single) == false && temp_assoc.size()>= temp_single.size() && temp_assoc.size()!=1){
                    HashSet<Integer> temptemp=new HashSet<Integer>();
                    temptemp.addAll(temp_assoc);
//                    System.out.println("Comparing hashset: "+temptemp.toString()+" , "+temp_single.toString());
                    if(temptemp.removeAll(temp_single) == false){
                        HashSet<Integer> temptemp2=new HashSet<Integer>();
                        temptemp2.addAll(temp_single);
                        temptemp2.addAll(temp_assoc);
                        String set1=temp_assoc.toString()+" => "+temp_single.toString();
                        String set2=temp_single.toString()+" => "+temp_assoc.toString();
                        System.out.print(set1+":  ");
                        float num=0;
//                        System.out.println(masterhashset.toString());
                        if(masterhashset.containsKey(temptemp2) == true){
                         num=masterhashset.get(temptemp2);
//                         System.out.println("Got: "+num);
                        }
                        else{
//                            System.out.println("Not found");
                        }
                        float denom=masterhashset.get(temp_assoc);
                        float denom2=masterhashset.get(temp_single);
                        float conf1=(num/denom)*100;
                        float conf2=(num/denom2)*100;
                        System.out.println(String.valueOf(conf1));
                        System.out.print(set2+":  ");
                        System.out.println(String.valueOf(conf2));
                        System.out.println("Counts: "+num+" , "+ denom+","+ denom2);
                        if(conf1 >= min_conf){
                            association.put(set1, Float.valueOf(conf1));
                        }
                        if(conf2 >= min_conf){
                             association.put(set2, Float.valueOf(conf2));
                        }
                    }
                     
                }
            }
            
        }
        
        
        System.out.println();
        System.out.println(association.toString());
       
    }
}

