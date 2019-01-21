$(function () {
    var editormdView = editormd.markdownToHTML("editormd-view", {
        htmlDecode      : "style,script,iframe",  // you can filter tags decode
        // emoji           : true,
        taskList        : true,  // 任务列表
        sequenceDiagram : true,   // 时序图
        flowChart       : true,  // 流程图
        tex             : true  // 数学公式
    });
});