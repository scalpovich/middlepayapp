/**
 * Created by ľľ on 2017/8/19.
 */
$(function () {
    var w = document.documentElement.clientWidth;
    var a = $("html").css("font-size");
    var ws = (w * 4 / 75).toFixed(5) + "px";
    $("html").css({"font-size": ws});

    window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize",   function () {
        var w = document.documentElement.clientWidth;
        var a = $("html").css("font-size");
        var ws = (w * 4 / 75).toFixed(5) + "px";
        $("html").css({"font-size": ws});
    });
});