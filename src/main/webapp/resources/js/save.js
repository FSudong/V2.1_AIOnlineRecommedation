function savePaper(paper_id,score,user_id,started){
    var url = encodeURIComponent(window.location)
    console.dir(score);
    if(user_id=="" || user_id==undefined){
        if(confirm("请先登陆")){
            window.location="./login/login.jsp".concat("?"+"next="+url);
        }
        $("#starScore"+score).attr("checked",false);
        return false;
    }

    var save = document.getElementById('save'+paper_id);
    if(!started){
        console.dir(save.value);
        save.value="已收藏";
        console.dir(save.value);
    }
    var success = function(msg) {
        console.dir("success"+" for paper: "+paper_id);
    };
    var error = function(msg){
        console.dir("error"+" for paper: "+paper_id);
    };
    $.ajax({
        method : "GET",
        url : "./supportpaper",
        data:{"pid":paper_id,"score":score,"aid":user_id},
        success : success,
        error : error,
        dataType : "text"
    });
    /*for (var k=1;k<=5;k++){
        if(k==score)continue;
        var stara = document.getElementById("stara"+k);
        addClass(stara,"fixed");
    }*/
    return true;
}