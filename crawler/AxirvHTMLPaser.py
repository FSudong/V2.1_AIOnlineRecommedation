# -*- coding: utf-8 -*-
"""
Created on Thu Apr 20 21:14:30 2017

@author: yaosheng
"""
from bs4 import  BeautifulSoup
import os
import os.path
import codecs
import sys
import crawlerlog
import time


logfilename=""
mylog=crawlerlog.getLog(logfilename)


"""
@param 文件名称
@return properies 0 title 1 authors(元组) 2 abstract 3 downlink 
需要从页面中抽取以下信息
1. 论文标题
2. 作者 （可以获得作者页面，包含该作者的其他论文）
3. 论文摘要
4. 论文PDF下载链接
"""
def parser(htmlFileName):
    properties=[]
    
    html=codecs.open(htmlFileName,'rb',encoding='utf-8')
    
    soup=BeautifulSoup(html)
    #获取标题
    title=soup.find("h1",class_="title mathjax").contents[1].replace("\n","")
    #获取摘要
    abstract=""
    for child in soup.find("blockquote",class_="abstract mathjax").children:
    
        if child.string!="\n" and child.string!=None or child.name=="a":
            if child.name!="a":
                abstract=abstract+child.string.replace("\n"," ")
            else:
                abstract=abstract+child["href"]
    #获取作者
    authors=[]
    for child in soup.find("div",class_="authors").children:
        if child.name=="a":
            authors.append(child.string+","+child["href"].replace("\n",""))
    #获取pdf的下载链接
    downlink = soup.find("meta",attrs={"name":"citation_pdf_url"})["content"]
    #获取subjects
    subjects = soup.find("span",attrs={"class":"primary-subject"}).string
    
    
    
    properties.append(title)
    properties.append(authors)
    properties.append(abstract)
    properties.append(downlink)
    time_str=time.strftime('%Y-%m-%d',time.localtime(time.time()))
    properties.append(time_str)
    properties.append(subjects)
    return properties

"""
@param paperID 页面对应论文在axriv中的id
@param savePath 保存在本地文件系统中的路径
@param splitChar 三元组的分隔符
@param proes=["title","authors","abstract","downlink","time","subjects"] 
"""
def savePaperPro(rootURI,paperID,savePath,splitChar,properties,proes):   
    if(not os.path.exists(savePath)):
        os.mkdir(savePath)
    savePath = os.path.join(savePath,paperID)
    f=codecs.open(savePath+".nt",'w',encoding='utf-8')
    paperURI=rootURI+paperID    
    for i in range(0,len(proes)):
        #元组类型
        if type(properties[i])==list:
            for ele in properties[i]:
                res=paperURI+splitChar+proes[i]+splitChar+ele+"\n"
                f.write(res)
        else:

            res=paperURI+splitChar+proes[i]+splitChar+properties[i]+"\n"
            f.write(res)
    f.flush()
    f.close()


    

if __name__=='__main__':
    #owl namespace
    rootURI="http://seu/kse/owl/paper/"
    #遍历论文页面存储路径  
    rootdir=sys.argv[1]
    nowday = time.strftime('%Y_%m_%d', time.localtime(time.time()))
    #nowday="2017_07_01"
    rootdir=os.path.join(rootdir,nowday)

    savePath=sys.argv[2]
    savePath=os.path.join(savePath,nowday)
    if not os.path.exists(savePath):
        os.mkdir(savePath)

    spiltChar="\t"
    proes=["title","authors","abstract","downlink","time","subjects"] 
    for parent,dirnames,filenames in os.walk(rootdir):
        for filename in filenames:
            fileID=filename.split(".")[0]+"."+filename.split(".")[1]
            properties=parser(parent+"/"+filename)
            savePaperPro(rootURI,fileID,savePath,spiltChar,properties,proes)


    
