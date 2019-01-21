"use strict";

$(function() {
    //获取分类列表
    function getCatalogs(username) {
        $.ajax({
            url: "/catalogs",
            type: "GET",
            data: {"username": username},
            success: function(data) {
              $("#catalogMain").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    // .click只能为页面现有的元素绑定点击事件
    // $(document).on("click","指定的元素",function(){});方法是将指定的事件绑定在document上，而新产生的元素如果符合指定的元素，那就触发此事件
    // 获取添加分类的页面
    $(".profile-content-container").on("click", ".blog-add-catalog", function () {
        $.ajax({
            url: "/catalogs/edit",
            type: "GET",
            success: function (data) {
                $("#catalogFormContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    //提交分类
    $("#submitEditCatalog").click(function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: "/catalogs",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                "username": username,
                "catalog": {
                    "id": $("#catalogId").val(),
                    "name": $("#catalogName").val()
                }
            }),
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success){
                    toastr.info(data.message);
                    getCatalogs(username);
                } else {
                    toastr.error(data.message);
                }
            },
            error : function () {
                toastr.error("error!");
            }
        });
    });

    // 获取编辑分类的页面
    $(".profile-content-container").on("click", ".blog-edit-catalog", function () {
        $.ajax({
            url: "/catalogs/edit/" + $(this).attr("catalogId"),
            type: "GET",
            success: function (data) {
                $("#catalogFormContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    // 删除分类
    $(".profile-content-container").on("click", ".blog-delete-catalog", function () {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: "/catalogs/" + $(this).attr("catalogId") + "?username=" + username,
            type: "DELETE",
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(data) {
                if (data.success) {
                    toastr.info(data.message);
                    //成功后刷新列表
                    getCatalogs(username);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    getCatalogs(username);
});