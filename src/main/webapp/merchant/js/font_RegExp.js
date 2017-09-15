/*font rem*/
$(function () {
   /* var kaihuhang = window.sessionStorage.getItem('ulKey');
    console.log(kaihuhang);
    $("#branchBank").val(kaihuhang || '请选择');*/

    $("#branchBank").on("click", function () {
        window.location.href = 'searchbank.html';
    });
   // kaihuhang=window.sessionStorage.removeItem('ulKey');

    var w = document.documentElement.clientWidth;
    var a = $("html").css("font-size");
    var ws = (w * 4 / 75).toFixed(5) + "px";
    $("html").css({"font-size": ws});

    window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", function () {
        var w = document.documentElement.clientWidth;
        var a = $("html").css("font-size");
        var ws = (w * 4 / 75).toFixed(5) + "px";
        $("html").css({"font-size": ws});
    });
});
