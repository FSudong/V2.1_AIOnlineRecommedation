# -*- coding: utf-8 -*-
"""
Created on Fri Apr 21 15:17:02 2017

@author: yaosheng
"""
import re
import crawlerlog
import requests
import codecs
import os
import threading


session=requests.Session()
def getHTML(url):
    
        headers = { 
                "Accept-Encoding":"gzip",            
                "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36"
                }
        pre=requests.Request('post',url,data={},headers=headers).prepare()
        source_code=session.send(pre)
        text=source_code.content
        return text
    

def getPatternes(pattern,html):
    pattern_re=re.compile(pattern)
    res=re.findall(pattern_re,html)
    return res

def loadFileTolist(filename):
    f=open(filename,'rb')
    lists=f.readlines()
    lists=[x.replace("\r\n", "") for x in lists]
    lists=[x.replace("\n", "") for x in lists]
    return lists

def saveHTML(filename,html,mylog):
    try:
        mylog=crawlerlog.getLog(mylog)
        f=open(filename,'wb')
        f.write(html)
        f.flush()
        f.close()
    except Exception,e :
        mylog.error(e)

"""
@param pro : hashSet  unicode编码
将 属性存入filename文件中
"""


def saveProperty(sub, filename, proes, mylog, splitChar):
    try:
        f = codecs.open(filename, 'w', encoding='utf-8')
        for key in proes.keys():
            val=proes[key]
            if type(val)==list:
                for v in val:
                    line=sub+splitChar+key+splitChar+v+"\n"
                    f.write(line)
            else:       
                if val!=None:
                    line=sub+splitChar+key+splitChar+val+"\n"
                    f.write(line)
        f.flush()
        f.close()
    except Exception,e :
        mylog.error(e)


def getTitleBySingleThread(rootPath,writerFile):
    wf=open(writerFile,'w')
    for parent,dirs,filenames in os.walk(rootPath):
        
        for filename in filenames:
            fullpath=os.path.join(parent,filename)
            rf=open(fullpath)
            line=rf.readline()
            sub=line.split(" ")[0]
            title=""
            while line!=None or len(line)==0:
                
                if line.__contains__("title"):
                    title = line.split("\"")[1]
                    break
                line=rf.readline()
            wf.write(sub+"\t"+title+"\n")
    wf.close()



def getTiltle(wfname,dpath,mylog):
    #print wfname
    wf=open(wfname,"w")
    for filename in os.listdir(dpath):
        mylog.info("processing : "+filename)
        fullpath=os.path.join(dpath,filename)
        rf=open(fullpath)
        line=rf.readline()
        sub=line.split(" ")[0]
        title=""
        while line!=None or len(line)==0:
                
            if line.__contains__("title"):
                title=line.split("\"")[1]
                break
            line=rf.readline()
        wf.write(sub+"\t"+title+"\n")
    wf.close()

# 使用多线程,每个目录一个线程负责，即每个会议一个线程
def getTitleByMulThread(rootPath, writerPath, mylog):
    threads=[]
    for d in os.listdir(rootPath):
        mylog.info("proceing conf : "+d)
        wfname=os.path.join(writerPath, d+".txt")
        threads.append(threading.Thread(target=getTiltle,args=(wfname,os.path.join(rootPath,d),mylog)))
    for t in threads:
        t.start()
    t.join()
    print 'over'


def saveHTMLUnicode(filename, html, mylog):
    try:
        f = codecs.open(filename,'w',encoding='utf-8')
        f.write(html)
        f.flush()
        f.close()
    except Exception, e:
        mylog.error(e)


