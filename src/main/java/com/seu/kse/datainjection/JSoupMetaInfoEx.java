package com.seu.kse.datainjection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JSoupMetaInfoEx {

    public static void main(String[] args) throws IOException {
    	

        String url = "https://en.m.wikipedia.org/wiki/Category:Artificial_intelligence";
        String baseurl = "https://en.m.wikipedia.org";

        Document document = Jsoup.connect(url).get();
        BufferedWriter writer = null;
        File file = new File("test.txt");
	    writer = new BufferedWriter(new FileWriter(file));

        org.jsoup.select.Elements restname =document.select("div.CategoryTreeSection");
        restname.select("script, style, span").remove();

        try {
	        //System.out.println("sub categories of AI");
	        for (Element elh : restname)
	        {
	        	
		       System.out.println((elh).text()+"subcategory of AI");
		        writer.write(elh.text()+"subcategory of AI\r\n");
		        Element link = elh.select("a").first();
		        String relHref = link.attr("href");
		        String linkText = link.text();
		        String subcaturl = baseurl + relHref;
		        Document document1 = Jsoup.connect(subcaturl).get();

		        org.jsoup.select.Elements restname1 =document1.select("div.CategoryTreeItem");
		        restname1.select("script, style, span").remove();

				System.out.println("Sub categories of " + linkText);
		        for (Element elh1 : restname1){
			        
			     System.out.println((elh1).text()+"subcategory of" + linkText);
			     
			       writer.write("Sub categories of " + linkText+"\r\n");
				    writer.write(elh1.text()+ "subcategory of" + linkText+"\r\n");
				  //String str = Jsoup.clean(subcaturl, Whitelist.none().addTags("div"));
				    
				    //System.out.println(str);
		        }
        }
      

	    writer.close();
	       System.out.println("Done");
	    

	}
			 
		    catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		    	writer.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
        
    }
      
    }
