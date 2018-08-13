# 配置说明
## 1. clone 项目
### 方法一：
    使用git的命令行版本拉取项目  
    clone https://github.com/yaosheng42/AIOnlineRecommedation.git

### 方法二：
    使用IDEA直接从git导入项目
    1. File -> New -> Project from version control -> git 
    2. 输入下列值
        1）Git Repository URL :https://github.com/yaosheng42/AIOnlineRecommedation.git
        2）Parent Directory:　项目所在目录
        3）Directory Name: 项目名称
        

## 2. 修改配置选项
    1. 修改 包com.seu.kse.service.recommendation 下 Configuration projectRoot 为自己的项目目录
    2. 由于项目中大部分xml中包含中文，如果运行发生 error : 3 字节的 UTF-8 序列的字节 3 无效的运行时错误，请将所有xml头部encoding属性修改为 GBK
    3. 如果使用本地的数据库，需要修改 jdbc.properties中的数据库连接属性。具体建库流程后面介绍。  
## 3. 项目依赖
    1. jdk 版本 ： 1.8 ；在项目maven下载完成依赖后，请添加自己本地的jdk
    2. tomcat 版本 ： 9.0；在IDEA中添加本地的tomcat配置  在File -> Settings -> Build, Execution,Deployment -> Application Servers 中添加本地的tomcat

## 4. 配置运行项
    1. Run -> Run... -> Edit configurations -> + -> tomcat Server ;
    2. 在Server选项卡：填写配置项， 注意需要在 VM options 中添加 -Djava.library.path="" 命令
    （否则有可能出现 java.lang.UnsatisfiedLinkError: no jniopenblas in java.library.path 错误）
    3. 在Deployment选项卡 添加 war包，这里有2个war ，必须选择 AIOnlineRecommedation:war exploded Application context默认为/（该属性即为该网站的根目录）
    4. 选择该配置项运行即可，启动时间较长，每次启动会重新训练推荐模型。

## 5. 数据库搭建

## 6. 爬虫程序定时启动设置

## 7. 测试
    1. com.seu.kse.service.impl.RecommenderService init方法中下列语句修改为：
       
       `
       List<Paper> papers = paperDao.selectAllPaper();
    
       List<Paper> papers = paperDao.selectLimitPaper(100);
       `
       
