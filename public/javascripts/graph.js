/**
 * Created by tom on 17-9-14.
 */
$(document).ready(function() {

    var ctx = $("#myChart").get(0).getContext("2d");

    var options = {
        animation: false,
        scaleShowGridLines: true,
        scaleGridLineColor: "rgba(0,0,0,.05)", //String - Colour of the grid lines
        scaleGridLineWidth: 1, //Number - Width of the grid lines
        bezierCurve: false, //Boolean - Whether the line is curved between points
        pointDot: true, //Boolean - Whether to show a dot for each point
        pointDotRadius: 4, //Number - Radius of each point dot in pixels
        pointDotStrokeWidth: 1, //Number - Pixel width of point dot stroke
        pointHitDetectionRadius: 20, //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
        datasetStroke: true, //Boolean - Whether to show a stroke for datasets
        datasetStrokeWidth: 2, //Number - Pixel width of dataset stroke
        datasetFill: true, //Boolean - Whether to fill the dataset with a colour
        responsive: true, //Boolean - Automatically scale to parent size
        legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].lineColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>" //String - A legend template
    };

    var settings = {
        "labels": [],
        "datasets": [
            {
                "label": "Sensor data",
                "fillColor": "rgba(220,220,220,0.2)",
                "strokeColor": "rgba(220,220,220,1)",
                "pointColor": "rgba(220,220,220,1)",
                "pointStrokeColor": "#fff",
                "pointHighlightFill": "#fff",
                "pointHighlightStroke": "rgba(220,220,220,1)",
                "data": []
            },
            {
                "label": "Predicted data",
                "fillColor": "rgba(151,187,205,0.2)",
                "strokeColor": "rgba(151,187,205,1)",
                "pointColor": "rgba(151,187,205,1)",
                "pointStrokeColor": "#fff",
                "pointHighlightFill": "#fff",
                "pointHighlightStroke": "rgba(151,187,205,1)",
                "data": []
            }
        ]
    };

    /* create chart in angular scope and initialize it */
    var ng_scope = angular.element($("#content")).scope();
    ng_scope.ctx = ctx;
    ng_scope.options = options;
    ng_scope.settings = settings;
    ng_scope.myLineChart = new Chart(ctx).Line(settings, options);
    ng_scope.initChart();

});
