/**
 * Created by tom on 17-9-14.
 */
$(document).ready(function() {

    var ctx = $("#myChart").get(0).getContext("2d");

    var options = {

        scaleShowGridLines: true,

        //String - Colour of the grid lines
        scaleGridLineColor: "rgba(0,0,0,.05)",

        //Number - Width of the grid lines
        scaleGridLineWidth: 1,

        //Boolean - Whether the line is curved between points
        bezierCurve: true,

        //Number - Tension of the bezier curve between points
        bezierCurveTension: 0.4,

        //Boolean - Whether to show a dot for each point
        pointDot: true,

        //Number - Radius of each point dot in pixels
        pointDotRadius: 4,

        //Number - Pixel width of point dot stroke
        pointDotStrokeWidth: 1,

        //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
        pointHitDetectionRadius: 20,

        //Boolean - Whether to show a stroke for datasets
        datasetStroke: true,

        //Number - Pixel width of dataset stroke
        datasetStrokeWidth: 2,

        //Boolean - Whether to fill the dataset with a colour
        datasetFill: true,

        //Boolean - Automatically scale to parent size
        responsive: true,

        //String - A legend template
        legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].lineColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"

    };


    var ng_scope = angular.element($("#content")).scope();

    $.ajax({
        url: "/json/" + ng_scope.current.loc.toLowerCase() + "/" + ng_scope.current.type.toLowerCase(),
        dataType: "json",
    }).done(function(data) {
        var myLineChart = new Chart(ctx).Line(data, options);
    }).fail(function(jqXHR, textStatus, errorThrown){
        alert("Could not retrieve data (" + errorThrown + ")");
    });

});
