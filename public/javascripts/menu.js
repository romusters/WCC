/**
 * Created by tom on 17-9-14.
 */
$(document).ready(function() {
    $(".nav").find("li:first").addClass("active");

    $(".menu").find("li").on("click", function(){
        $(".menu .active").removeClass("active");
        /*$(this).addClass("active");*/
        /*$(".menu").find("li").eq($(this).index()).addClass("active");*/
        var n = $(this).index()+1;
        $(".menu").find("li:nth-child(" + n + ")").addClass("active");
    });

    $(".nav-tabs").find("li").on("click", function(){
        $(".nav-tabs .active").removeClass("active");
        $(this).addClass("active");
        $(".namechange").text($(this).find("a").text());
    });

    /*
    $.ajax({
        url: jsRoutes.controllers.Application.previousRecordings().url,
        type: "POST",
        dataType: "json",
        data: { questions: "Why is there air?" }
    }).done(function(childNodes) {
        alert("TODO do something")
    }).fail(function(jqXHR, textStatus, errorThrown){
        alert(textStatus + ", " + errorThrown);
    });
    */

});