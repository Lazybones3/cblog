"use strict";

$(function () {
    var _status;
    var _pageIndex;

    function getBlogList() {
        $.ajax({
            url: "/u/"+ username +"/blogs",
            type: "GET",
            contentType: "application/json",
            data: {
                "async":true,
                "status": _status,
                "pageIndex": _pageIndex
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    //分页
    $.tbpage("#mainContainer", function (pageIndex) {
        _pageIndex = pageIndex;
        getBlogList();
    });

    //删除博客
    $(".blog-content-container").on("click",".blog-delete-blog", function () {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var blogUrl = "/u/"+username+"/blogs/delete/"+$(this).attr('blogId');
        $.ajax({
            url: blogUrl,
            type: "POST",
            data: {
                "_method": "DELETE",
                "status": _status
            },
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                toastr.success(data.message);
                getBlogList();
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    //修改博客状态
    $(".blog-content-container").on("click", ".blog-status-blog", function () {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var blogUrl = "/u/"+username+"/blogs/status/"+$(this).attr('blogId');
        $.ajax({
            url: blogUrl,
            type: "POST",
            data: {
                "status": _status
            },
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                toastr.success(data.message);
                getBlogList();
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    $(".blog-content-container").on("click", ".blog-nav-item", function () {
        _status = $(this).attr('status');
        $.ajax({
            url: "/u/"+ username +"/blogs",
            type: "GET",
            contentType: "application/json",
            data: {
                "async": true,
                "status": _status
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });
});