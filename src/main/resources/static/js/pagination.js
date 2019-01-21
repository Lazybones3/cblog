//分页处理
(function ($) {
    $.tbpage = function (selector, handler) {

        $(selector).off("click", ".tbpage-item").on("click", ".tbpage-item", function () {
            var pageIndex = $(this).attr("pageIndex");
            if ($(this).parent().attr("class").indexOf("active") > 0) {
                // console.log("为当前页面");
            } else {
                handler(pageIndex);
            }
        });
    }
})(jQuery);
