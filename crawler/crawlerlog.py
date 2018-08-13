# -*- coding: utf-8 -*-
"""
Created on Mon Apr 24 17:03:29 2017

@author: yaosheng
"""
import logging

"""
需改造成单例模式
"""


def getLog(logfile):
    
    logging.basicConfig(filename=logfile,level=logging.DEBUG,
                    format='%(asctime)s %(filename)s[line:%(lineno)d] [thread-id:%(thread)d] %(levelname)s %(message)s',
                    datefmt='%a, %d %b %Y %H:%M%S',
                    filemode='w')
                    
    console = logging.StreamHandler()
    console.setLevel(logging.INFO)
    formatter = logging.Formatter('%(asctime)s %(filename)s[line:%(lineno)d] [thread-id:%(thread)d] %(levelname)s %(message)s')
    console.setFormatter(formatter)
    logging.getLogger('').addHandler(console)
    return logging