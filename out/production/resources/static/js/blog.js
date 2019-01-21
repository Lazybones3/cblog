"use strict";

$(function () {

    var _pageSize = 10;
    var catalogId;

    //分页
    $.tbpage("#mainContainer", function (pageIndex) {
        getBlogsByName(pageIndex);
    });

    //根据页面索引，分类等获取博客列表
    function getBlogsByName(pageIndex, pageSize) {
        $.ajax({
            url: "/blogs",
            contentType: "application/json",
            data: {
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "catalog":catalogId
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    //根据分类查询
    $(".blog-content-container").on("click", ".blog-query-by-catalog", function () {
        catalogId = $(this).attr("catalogId");
        getBlogsByName(0, _pageSize);
    })

    $("#nav-java").click(function () {
        catalogId = 1;
        getBlogsByName(0, _pageSize);
        $("#nav-java").parents(".nav-item").addClass("active").siblings().removeClass("active");
    });

    $("#nav-python").click(function () {
        catalogId = 2;
        getBlogsByName(0, _pageSize);
        $("#nav-python").parents(".nav-item").addClass("active").siblings().removeClass("active");
    });

    $("#nav-others").click(function () {
        // getBlogsByName(0, _pageSize);
        $("#nav-others").parents(".nav-item").addClass("active").siblings().removeClass("active");
    });
});