/**
 * Created by yaosheng on 2017/6/2.
 */
function addTag(uid){
    console.dir(uid);

    if(uid==undefined){
        if(confirm("请先登陆")){
            window.location="./login/login.jsp";
        }
        return false;
    }
    var tags = document.getElementById("tags").value;
    console.dir(tags);
    var success = function(msg) {
        alert("上传成功");
        console.dir("success"+" for paper: "+uid);
    };
    var error = function(msg){
        alert("上传失败");
        console.dir("error"+" for paper: "+uid);
    };
    $.ajax({
        type : "POST",
        url : "./addTag",
        data:{"uid":uid, "tags":tags},
        success : success,
        error : error,
        dataType : "text"
    });

    return false;
}
