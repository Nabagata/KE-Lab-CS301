/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fptree;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author c137165
 */
class node{
    HashSet<Integer> transaction;
    int count;
    int nextindex;
    ArrayList<node> child;
    public node(HashSet<Integer> t){
        transaction=t;
        count=0;
        nextindex=-1;
        child=new ArrayList<node>();
    }
    public HashSet<Integer> gettrans(){
        return this.transaction;
    }
    
    public void add(ArrayList<HashSet<Integer>> k,int index){
            System.out.println("In add...."+k.toString());
            if(index == k.size())
                return;
            int flag=0;
            for(int i=0;i<child.size();i++)
                if(child.get(i).gettrans().containsAll(k.get(index))){
                       child.get(i).count++;
                       child.get(i).add(k, index+1);
                       flag=1;
                }
            if(flag!=1){
                node newn=new node(k.get(index));
                child.add(newn);
                newn.add(k, index+1);
            }
        }
    public void nodeview(int level){
        System.out.println(this.transaction.toString()+level);
        for(int i=0;i<this.child.size();i++)
            child.get(i).nodeview(level+1);
    }
    
    public void formtree(HashMap<Integer,ArrayList<String>> a,int level){
        if(a.containsKey(level))
            a.get(level).add(this.transaction.toString());
        else{
            a.put(level, new ArrayList<String>());
            a.get(level).add(this.transaction.toString());
        }
        for(int i=0;i<this.child.size();i++)
            child.get(i).formtree(a,level+1);
        if(level+1<=3)
            a.get(level+1).add("==");
        if(level == 0){
            for(int i=0;i<a.size();i++)
                System.out.println(a.get(i).toString());
        }
    }
    
    public void generatesupp(HashMap<HashSet<Integer>, Integer> a,int level){
        int count;
        if(a.containsKey(this.transaction))
            count=a.get(this.transaction)+1;
        else{
            count=1;
        }
//        System.out.println(this.transaction.toString()+"::::"+this.count);   
        a.put(this.transaction,count+this.count);
        for(int i=0;i<this.child.size();i++)
            child.get(i).generatesupp(a,level+1);
        if(level ==0){
            System.out.println(a);
        }
    }
    
    public int generatesubtree(HashSet<Integer> trans,int level, HashMap<HashSet<Integer>,Integer> subtree ){
        if(this.transaction.containsAll(trans))
        {
//            System.out.println("Transaction got........."+this.count);
            subtree.put(this.transaction, this.count+1);
            return this.count+1;
        }
//        System.out.println("Not equal:::"+this.transaction.toString()+"::Children:::"+this.child.size());
        int count=0;
        if(this.child.size()>0){
           for(int i=0;i<this.child.size();i++)
           {
//               System.out.println("Child::::"+this.child.get(i).transaction.toString());
               int t=child.get(i).generatesubtree(trans, level+1, subtree);   
               if(t>0){
//                   System.out.println(this.transaction.toString()+":::"+t);
                   if(subtree.containsKey(this.transaction))
                   subtree.put(this.transaction,subtree.get(this.transaction)+t);
                   else
                       subtree.put(this.transaction,t);
                   count=subtree.get(this.transaction);
                
               }
               
           }
        }       
            return count;
        
    }
    
}
class Fptree{
    
    ArrayList<node> tree=new ArrayList<node>();
    
}

public class FPTree {
    public static ArrayList<HashSet<Integer>> SortTransaction( ArrayList<HashSet<Integer>> h, HashMap<HashSet,Integer> map){
        int h_size=h.size();
        ArrayList<HashSet<Integer>> hp=new ArrayList<HashSet<Integer>>();
        
        while(h_size!=0){
            int max=0;
            for(int i=0;i<h.size();i++)
                if(map.get(h.get(max))<map.get(h.get(i)))
                    max=i;
            hp.add(h.get(max));
            h.remove(max);
            h_size--;
        }
        return hp;
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
        
        System.out.println("Frequent single item sets: "+counting2.toString());
        System.out.println("All sales values: "+check.toString());
//        Set<HashSet> e2=counting2.keySet();
//        Iterator p2=e2.iterator();
//        ArrayList<Integer> counts=new ArrayList<Integer>();
//        ArrayList<HashSet> sets=new ArrayList<HashSet>();
//        while(p2.hasNext()){
//            HashSet<Integer> hashing=(HashSet<Integer>) p2.next();
//            counts.add(counting2.get(hashing));
//            sets.add(hashing);
//        }
//        
//        System.out.println(counts.toString());
//        System.out.println(sets.toString());
        //FP TREEE
        
        HashSet<Integer> emp=new HashSet<Integer>();
        node tree=new node(emp);
        for(int i=0;i<check.size()-1;i++){
            HashSet<Integer> temp2=check.get(i);
            Object objects[]=temp2.toArray();
            ArrayList<HashSet<Integer>> h2 = new ArrayList<HashSet<Integer>>();
            for(int j=0;j<objects.length;j++){
                HashSet<Integer> set2=new HashSet<Integer>();
                set2.add((Integer)objects[j]);
                System.out.println(set2.toString());
                h2.add(set2);
            }
            System.out.println(h2.toString());
            ArrayList<HashSet<Integer>> h6=SortTransaction(h2,counting2);
            System.out.println("Sorted trnasac "+h6.toString());
            tree.add(h6, 0);
            tree.nodeview(0);
            System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<");
            
        }
        HashMap<HashSet<Integer>, Integer> a=new HashMap<HashSet<Integer>, Integer>();
        tree.formtree(new HashMap<Integer,ArrayList<String>>(), 0);
        tree.generatesupp(a,0);
        int maxcount=10;
        ArrayList<HashSet<Integer>> check3=new ArrayList<HashSet<Integer>>();
        Iterator q=Mastersale.iterator();
        while(q.hasNext()){
            HashSet<Integer> temp2=new HashSet<Integer>();
            temp2.add((Integer)q.next());
            check3.add(temp2);
        }
        System.out.println(check3);
        int index=0;
        ArrayList<HashSet<Integer>> check4=new ArrayList<HashSet<Integer>>();
        while(check4.size()!=5){
        for(int i=0;i<check3.size();i++)
            if(a.get(check3.get(i))<=maxcount)
                index=i;
        check4.add(check3.get(index));
        check3.remove(index);
        }
        System.out.print("Sorted::::"+check4.toString());
        HashMap<HashSet<Integer>,Integer> freq2= new HashMap<HashSet<Integer>,Integer>();
        System.out.println(a.toString());
        for(int k=0;k<check4.size();k++){
        HashMap<HashSet<Integer>,Integer> subtree=new HashMap<HashSet<Integer>,Integer>();
        tree.generatesubtree(check4.get(k), 0, subtree);
        Set<HashSet<Integer>> keys=subtree.keySet();
        Iterator l=keys.iterator();
        HashMap<HashSet<Integer>,Integer> subtree2=new HashMap<HashSet<Integer>,Integer>();
        while(l.hasNext()){
            HashSet<Integer> temp=(HashSet<Integer>)l.next();
//            System.out.println(temp.toString());
            if(subtree.get(temp)>=2 || temp.containsAll(check4.get(k)))
                subtree2.put(temp, subtree.get(temp));
        }
//        System.out.println(subtree2);
        
        //Generating 2 itemsets
        Set<HashSet<Integer>> sb2=subtree2.keySet();
        Iterator pq=sb2.iterator();
        ArrayList<HashSet<Integer>> h6= new  ArrayList<HashSet<Integer>>();
        while(pq.hasNext()){
            HashSet<Integer> temp5=new HashSet<Integer>();
            HashSet<Integer> temp6=(HashSet<Integer>)pq.next();
            if(!temp6.containsAll(check4.get(k)) && temp6.size()>0)
            {
                
                temp5.addAll(temp6);
                temp5.addAll(check4.get(k));
                freq2.put(temp5,subtree.get(temp6));
                h6.add(temp6);
            }
            
            
        }
        
        System.out.println(h6);
        ArrayList<HashSet<Integer>> finalset =new ArrayList<HashSet<Integer>>();
        for(int i=1;i<Math.pow(2, h6.size());i++)
        {
            String bit=Integer.toBinaryString(i);
            String bit2=bit;
            //System.out.println(bit.length());
            for(int j=0;j<(h6.size()-bit.length());j++)
                bit2="0"+bit2;
            int one_count=0;
            for(int j=0;j<bit2.length();j++)
                if(bit2.charAt(j)=='1')
                    one_count++;
            //System.out.println(bit2+"first round");
            HashSet<Integer> temp9=new HashSet<Integer>();
            for(int j=0;j<bit2.length();j++)
                if(bit2.charAt(j)=='1')
                    temp9.addAll(h6.get(j));
            finalset.add(temp9);
            
        }
        System.out.println(finalset);
        
        }
        System.out.println(freq2);    
        
    }
}
