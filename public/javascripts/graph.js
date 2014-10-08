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
        bezierCurve: false,

        //Boolean - Show animation or not
        animation: false,

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

    var settings = {
        "labels": [],
        "datasets": [
            {
                "label": "My First dataset",
                "fillColor": "rgba(220,220,220,0.2)",
                "strokeColor": "rgba(220,220,220,1)",
                "pointColor": "rgba(220,220,220,1)",
                "pointStrokeColor": "#fff",
                "pointHighlightFill": "#fff",
                "pointHighlightStroke": "rgba(220,220,220,1)",
                "data": []
            },
            {
                "label": "My Second dataset",
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

    var myLineChart = new Chart(ctx).Line(settings, options);


    var ng_scope = angular.element($("#content")).scope();

    if(ng_scope.current.click == 1){
        ajaxCall();
    }

    function ajaxCall() {
        $.ajax({
            url: "/json/" + ng_scope.current.loc.toLowerCase() + "/" + ng_scope.current.type.toLowerCase(),
            dataType: "json"
        }).done(function (result) {
            if(ng_scope.current.click == 1){
                console.log("click", ng_scope.current.click)
                myLineChart.destroy();
                myLineChart = new Chart(ctx).Line(settings, options);
                ng_scope.current.click = 0;
                ng_scope.$apply();
            }
            console.log("test6", ng_scope.current.click)

            $("#error").hide("slow");
            myLineChart.addData( [ result.sensor_data, result.predicted_data ], result.labels);
            if(myLineChart.datasets[0].points.length > 20){
                myLineChart.removeData();
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $("#error").show("slow");
        });
    }

    ajaxCall();
    setInterval(ajaxCall, 5000);

});
