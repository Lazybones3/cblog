// DOM加载完再执行
$(function () {
    var testEditor = editormd("test-editormd", {
        width: "100%",
        height: 640,
        syncScrolling: "single",
        path: "/editor/lib/",
        //这个配置在simple.html中并没有，但是为了能够提交表单，使用这个配置可以让构造出来的HTML代码直接在第二个隐藏的textarea域中，方便post提交表单。
        saveHTMLToTextarea: true,
        taskList: true,         // 任务列表
        sequenceDiagram: true,  // 时序图
        flowChart: true,        //流程图
        tex: true,              //数学公式
        //全屏事件处理
        onfullscreen: function() {
            $(".blog-edit-control").hide();
        },
        onfullscreenExit: function() {
            $(".blog-edit-control").show();
        }
    });



    //保存博客
    $("#submitBlog").click(function() {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/u/' + $(this).attr("userName") + '/blogs/edit',
            type: 'POST',
            // async: true,
            data: JSON.stringify({
                "id": $('#blogId').val(),
                "title": $('#title').val(),
                "content": $("#content").text(),
                "htmlContent": $("#htmlContent").text(),
                "catalog": {"id": $('#catalogSelect').val()}
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken);    // 添加CSRF Token
            },
            success: function(data) {
                if (data.success) {
                    //成功后，重定向
                    window.location = data.body;
                } else {
                    toastr.error("error: "+data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        })
    });
});


