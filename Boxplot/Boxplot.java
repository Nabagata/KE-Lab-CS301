/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Boxplot;

import java.awt.Graphics;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JFrame;

/**
 *
 * @author acemaster
 */
public class Boxplot extends JFrame implements Runnable {
    static int maxheight;
    static int maxwidth;
    static int scale;
    
    public static void main(String[] args) throws IOException{
        
        ArrayList<Integer> q=new ArrayList<Integer>();
        FileReader f=new FileReader("numbers.txt");
        String st = "";
        int flag=0;
        while(flag!=1){
            int s;
            s=f.read();
            if(s == -1)
                flag=1;
            else if(s == ' '){
                q.add(Integer.parseInt(st));
                st="";
            }
            else{
                st=st+(char)s;
            }
        }
        
        System.out.println(q);
        q.sort(null);
        System.out.println(q);
        int l=q.size();
        int q1=q.get(l/4);
        int q2=q.get(l/2);
        int q3=q.get((3*l)/4);
        System.out.println(q1+"  "+q2+"  "+q3);
        
        int iqr= q3-q1;
        float out=(float) (1.5*(float)iqr);
        float out1=out-q1;
        float out2=q3+out;
        System.out.println(out+"   "+out1+"   "+out2);
        ArrayList<Integer> fq=new ArrayList<Integer>();
        for(int i=0;i<q.size();i++)
            if(q.get(i) >= out1 && q.get(i) <= out2)
            {
                System.out.println("Selected: "+q.get(i));
                fq.add(q.get(i));
            }
        System.out.println(fq.toString());
        System.out.println("Printing Box Plot of the dataset.............");
        Thread t = new Thread();
	t.start();
	Boxplot b = new Boxplot();
        maxwidth=200;
        maxheight=200;
	b.setSize(maxwidth, maxheight);
        scale=2;
	b.setVisible(true);
	b.repaint();
        
    }
    
    @Override
    public void paint(Graphics g){
        g.drawLine(0, 150, maxwidth/scale,150);
        g.drawLine(0, 150, 0, 0);
        
    }

    @Override
    public void run() {
        repaint(); //To change body of generated methods, choose Tools | Templates.
    }
}
