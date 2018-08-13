# -*- coding: utf-8 -*-
"""
Created on Fri Apr 07 09:28:08 2017
@author: yaosheng
"""
import urllib2
import re
import sys
import Utils
import os
import crawlerlog
import time
import random

rootURI="https://arxiv.org"
paperURIREG=r'/abs/\d{4}.\d{5}'


localDiskPath="F:/axrivPapers/"
paperListPath="F:/filelist.txt"
logfilename="F:/arxiv.log"
logfilename=sys.argv[3]
mylog=crawlerlog.getLog(logfilename)

# open the url and read
def getHtml(url):
    mylog.info("processing :"+url)
    try:
        page=urllib2.urlopen(url)
        html=page.read()
        page.close()
        return html
    except Exception, e:
        mylog.error(str(e)+" in "+url)

#获得该页面所有论文的url链接
def getPatternInPage(html,reg):
    pattern_re=re.compile(reg)
    pattern=re.findall(pattern_re,html)
    return pattern

def getNewPapers(paperURI):
    mylog.info("processing :"+paperURI)
    try:
        html=getHtml(rootURI+paperURI)
        #根据paperURI得到文件名
        fileName=paperURI.split("/")[-1]+".txt"
        #将该页面存入本地文件系统
        filepath = os.path.join(localDiskPath,fileName)
        f=open(filepath,'wb')
        f.write(html)
        f.flush()
        f.close()
    except Exception, e:
        mylog.error(str(e)+" in "+paperURI)

def run(URLPools,paperSet):
    f=open(paperListPath,"a")
    for URL in URLPools:
        html=getHtml(URL)
        paperURIs=getPatternInPage(html,paperURIREG)
        for url in paperURIs:
            #未爬取的论文
            if url not in paperSet:
                getNewPapers(url)
                time.sleep(random.randint(0,3))
                f.write(url+"\n")
        time.sleep(120)
    f.flush()
    f.close()


"""
1. 每日启动一次爬虫
2. 每次爬取，从文件中读取已爬取论文列表
3. 根据已爬取列表去重
4. 已爬取列表只保留最近5天爬取的论文
"""
if __name__=="__main__":
   
    #加载已读论文列表
    paperListPath=sys.argv[1]
    mylog.info("loading file : "+paperListPath)
    localDiskPath=sys.argv[2]
    mylog.info("loading path : "+localDiskPath)
    nowday= time.strftime('%Y_%m_%d',time.localtime(time.time()))
    #nowday="2017_06_23"
    localDiskPath=os.path.join(localDiskPath,nowday)

    if not os.path.exists(localDiskPath):
        os.mkdir(localDiskPath)

    if not os.path.exists(paperListPath):
        #不存在创建该文件
        mylog.error(paperListPath+" is not exist !")
        f=open(paperListPath,"w")
        mylog.info(paperListPath+" is created !")
        f.close()

    paperList=Utils.loadFileTolist(paperListPath)
    paperSet=set(paperList)
    URLPools=["https://arxiv.org/list/cs.AI/pastweek?show=200",
              "https://arxiv.org/list/cs.CL/pastweek?show=200",
              "https://arxiv.org/list/cs.CV/pastweek?show=200",
              "https://arxiv.org/list/cs.LG/recent?show=200",
              "https://arxiv.org/list/cs.IR/recent?show=200",
              "https://arxiv.org/list/cs.IT/recent?show=200",
              "https://arxiv.org/list/cs.GR/recent?show=200",
              "https://arxiv.org/list/cs.SI/recent?show=200"]
    run(URLPools,paperSet)