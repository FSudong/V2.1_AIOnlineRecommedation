function submitForm(form){

    if($("#terms").val()==""){
        $("#msg-notice").html("查询不能为空!");
        return false;
    }

    return true;
}