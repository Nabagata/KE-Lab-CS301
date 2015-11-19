/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 *
 * @author c137165
 */

class Node{
    int leaf;
    HashMap<String,Node> children;
    String class_name;
    
    public Node(String class_name, int leaf){
        this.class_name=class_name;
        this.leaf=leaf;
        this.children=new HashMap<String,Node>();
    }
    public float get_value(float count,float no_of_rows){
        if(count == 0){
            return 0;
        }
        float result= (float) -((count/no_of_rows)*(Math.log((count/no_of_rows))/Math.log(2)));
        return result;
    }
    public int gain(ArrayList<ArrayList<String>> hs,ArrayList<ArrayList<String>> dattr,int endindex,float no_of_rows,int no_of_attributes,ArrayList<Integer> attrlist){
        float count_yes=0;
        float count_no=0;
        for(int i=0;i<no_of_rows;i++){
//            System.out.println(hs.get(i).toString());
//            System.out.println(hs.get(i).get(endindex).compareTo("yes"));
            if(hs.get(i).get(endindex).compareTo("yes") == 1){
                count_yes++;
            }
            else{
                count_no++;
            }
        }
       System.out.println(count_yes);
       System.out.println(count_no);
       float gain=(float) (-((count_yes/no_of_rows)*(Math.log((count_yes/no_of_rows))/Math.log(2)))-((count_no/no_of_rows)*(Math.log((count_no/no_of_rows))/Math.log(2))));
       float max=-9999999;
       System.out.println("Gain: "+ gain);
       int min_index=0;
       for(int i=1;i<no_of_attributes-1;i++){
           if(attrlist.contains(i)){
           float attr_gain=0;
//           System.out.println(dattr.get(i));
           
           for(int j=0;j<dattr.get(i).size();j++){
               float total_count=0;
               count_yes=0;
               count_no=0;
               for(int k=0;k<no_of_rows;k++){
//                  System.out.println(hs.get(k).get(i));
                   if(hs.get(k).get(i).toString() == null ? dattr.get(i).get(j).toString() == null : hs.get(k).get(i).toString().equals(dattr.get(i).get(j).toString()))
                   {
//                       System.out.println(dattr.get(i).get(j).toString()+": Got");
                       total_count++;
                       if(hs.get(k).get(endindex).compareTo("yes")== 1){
                           count_yes++;
                       }
                       else{
                           count_no++;
                       }
                   }
               }
               System.out.println("Total count: "+ total_count);
               System.out.println("Yes count: "+ count_yes);
               System.out.println("No count: "+ count_no);
               attr_gain=(float) (attr_gain+((total_count/no_of_rows))*(this.get_value(count_no, total_count)+this.get_value(count_yes, total_count)));
           }
           System.out.println("Attribute gain of "+i+" is "+attr_gain + "Max is : "+ max);
           if(gain-attr_gain > max){
               max=gain-attr_gain;
               min_index=i;
           }
       }
       }
       
       return min_index;
    }
    
    
    
    public Node form_tree(ArrayList<ArrayList<String>> hs,ArrayList<ArrayList<String>> dattr,int endindex,float no_of_rows,int no_of_attributes,ArrayList<Integer> attrlist){
        int split_index=this.gain(hs, dattr, endindex, no_of_rows, no_of_attributes,attrlist);
        attrlist.remove(split_index);
        for(int i=0;i<dattr.get(split_index).size();i++){
            ArrayList<ArrayList<String>> dh=new ArrayList<ArrayList<String>>();
            for(int j=0;j<no_of_rows;j++){
                if(hs.get(j).get(split_index).toString().equals(dattr.get(split_index).get(i))){
                    dh.add(hs.get(j));
                }
                this.children.put(dattr.get(split_index).get(i),)
            }
        }
        
    }
    
}
public class decisiontree {
     static ArrayList<ArrayList<String>> hs;
     static ArrayList<ArrayList<String>> dattr;
     public static void main(String[] args) throws IOException{
         int no_of_attributes=0;
         int no_of_records=0;
         FileReader fw=new FileReader("dtdata.txt");
         char arr[]=new char[1000000];
         fw.read(arr);
         String st=new String(arr);
         StringTokenizer s=new StringTokenizer(st);
         int count=0;
         int flag=0;
         hs=new ArrayList<ArrayList<String>>();
         dattr=new ArrayList<ArrayList<String>>();
         ArrayList<String> single=new ArrayList<String>();
         while(s.hasMoreTokens()){
             ArrayList<String> temp2=new ArrayList<String>();
             String temp=s.nextToken("\n");
//             System.out.println(temp);
            if(temp.contains("-1"))
                break;
             StringTokenizer s2=new StringTokenizer(temp);
             while(s2.hasMoreTokens()){
                 String temp3=s2.nextToken(" ");
                     single.add(temp3);
                 try{
                     dattr.get(count);
                 }
                 catch(java.lang.IndexOutOfBoundsException e){
                     dattr.add(new ArrayList<String>());
                 }
                 if(!dattr.get(count).contains(temp3))
                     dattr.get(count).add(temp3);
//                 System.out.println(temp3);
                 temp2.add(temp3);
                 count=count+1;
             
            }
             if(flag ==0)
                no_of_attributes=count;
             flag=1;
             if(flag==1)
                 count=0;
             
             hs.add(temp2);
             no_of_records++;
         }
         
         System.out.println(hs.toString());
         System.out.println(dattr.toString());
         System.out.println("No of attributes: "+ no_of_attributes + ": No of records: "+no_of_records);
         ArrayList<Integer> attrlist=new ArrayList<Integer>();
         for(int i=1;i<6;i++)
             attrlist.add(i);
         //Found each attribute
         Node n=new Node("start",0);
         System.out.println(n.gain(hs, dattr, no_of_attributes-1, no_of_records, no_of_attributes,attrlist));
         
     }
}
