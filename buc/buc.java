/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package buc;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author c137165
 */
public class buc {
    static ArrayList<HashSet<String>> hs;
    static int dim=4;
    static int min_sup=1;
    public static void formcube(HashMap<String,ArrayList<String>> map, String level, HashSet<String> prev){
        if("d".equals(level))
            return;
        String st="";
        st=st+prev.toString();
        for(int i=prev.size();i<dim;i++)
            st=st+"*";
        int count=0;
        for(int i=0;i<hs.size();i++){
            if(hs.get(i).containsAll(prev) || prev.isEmpty())
                count++;
        }
//        System.out.print(count);
        if(count > min_sup){
            System.out.println(st+" -> "+count);
            if("".equals(level))
                level="a";
            else if("a".equals(level))
                level="b";
            else if("b".equals(level))
                level="c";
            else if("c".equals(level))
                level="d";
            
           for(int i=0;i<map.get(level).size();i++)
           {
                prev.add(map.get(level).get(i));
//                System.out.println(prev);
                formcube(map,level,prev);
                prev.remove(map.get(level).get(i));
           }
        }
           
    }
    public static void main(String[] args) throws IOException{
         FileReader fw=new FileReader("cube2.txt");
         char arr[]=new char[1000000];
         fw.read(arr);
         String st=new String(arr);
         StringTokenizer s=new StringTokenizer(st);
         int count=0;
         hs=new ArrayList<HashSet<String>>();
         ArrayList<String> single=new ArrayList<String>();
         while(s.hasMoreTokens()){
             HashSet<String> temp2=new HashSet<String>();
             String temp=s.nextToken("\n");
//             System.out.println(temp);
            if(temp.contains("-1"))
                break;
             StringTokenizer s2=new StringTokenizer(temp);
             while(s2.hasMoreTokens()){
                 String temp3=s2.nextToken("\t");
                 if(!single.contains(temp3))
                     single.add(temp3);
//                 System.out.println(temp3);
                 temp2.add(temp3);
             }   
             hs.add(temp2);
             }
         System.out.println("All the rows: "+hs.toString());
         System.out.println("Single sets: "+single);
         HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
         String st2="";
         for(int i=0;i<single.size();i++){
             st2="";
             st2=st2+single.get(i).charAt(0);
             if(map.containsKey(st2))
                 map.get(st2).add(single.get(i));
             else
             {
                 ArrayList temp10=new ArrayList<Integer>();
                 temp10.add(single.get(i));

                 map.put(st2, temp10);
             }
         
         }
         HashSet<String> set=new HashSet<String>(map.keySet());
         set.add("");
         System.out.println(map.toString());
         HashSet<String> hs2=new HashSet<String>();
         Iterator it=set.iterator();
         while(it.hasNext()){
            formcube(map,(String)it.next(),hs2);
//             System.out.println(it.next());
         }
    }
    }
