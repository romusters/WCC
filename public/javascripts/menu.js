/**
 * Created by tom on 17-9-14.
 */
$(document).ready(function() {
    /* activate correct nav items at load of page */
    $(".nav").find("li:first").addClass("active");

    /* change activated nav items on click */
    $(".menu").find("li").on("click", function(){
        $(".menu .active").removeClass("active");
        var n = $(this).index()+1;
        $(".menu").find("li:nth-child(" + n + ")").addClass("active");
    });

    /* change text on click of nav-tab */
    $(".nav-tabs").find("li").on("click", function(){
        $(".nav-tabs .active").removeClass("active");
        $(this).addClass("active");
        $(".namechange").text($(this).find("a").text());
    });

});