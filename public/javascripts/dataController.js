(function(ng) {
    var app = ng.module("myApp", []);

    app.controller("dataController", function($scope,$http,$interval) {
        $scope.current = {
            loc: "North",
            type: "Temperature",
            val: 0,
            predicted: 0,
            unit: "\u2103"
    };

        $scope.myLineChart = null;

        $scope.tab = function(name){
            $scope.current.type = name;
            $scope.current.unit = (name=="Temperature") ? "\u2103" : "cd";
            $scope.initChart()
        };

        /* update location */
        $scope.loc = function(name){
            $scope.current.loc = name;
            $scope.initChart();
        };

        function ajaxCall(){
            $http.get('/json/' + $scope.current.loc.toLowerCase() + "/" + $scope.current.type.toLowerCase() + "/1")
                .success(function (result) {
                    $("#error").hide("slow");
                    $scope.myLineChart.removeData(); /* remove latest datapoint */
                    $scope.myLineChart.addData([ result.sensor_data, result.predicted_data ], result.labels); /* add a new datapoint */
                    $scope.current.val = Math.round(10 * result.sensor_data[0]) / 10;
                    $scope.current.predicted = Math.round(10 * result.predicted_data[0]) / 10;
                })
                .error(function () {
                    $("#error").show("slow");
                })
        }

        $scope.initChart = function(){
            /* destroy current chart */
            if($scope.myLineChart) $scope.myLineChart.destroy();

            /* get data to initialize chart */
            $http.get('/json/' + $scope.current.loc.toLowerCase() + "/" + $scope.current.type.toLowerCase() + "/20")
                .success(function (result) {
                    $("#error").hide("slow");
                    $scope.settings.labels = result.labels;
                    $scope.settings.datasets[0].data = result.sensor_data;
                    $scope.settings.datasets[1].data = result.predicted_data;
                    $scope.current.val = Math.round(10 * result.sensor_data[result.sensor_data.length - 1]) / 10;
                    $scope.current.predicted = Math.round(10 * result.predicted_data[result.predicted_data.length - 1]) / 10;
                    $scope.myLineChart = new Chart($scope.ctx).Line($scope.settings, $scope.options); /* initialize chart */
                })
                .error(function () {
                    $("#error").show("slow");
                })
        }

        ajaxCall();

        /* request new data from server every second */
        $interval(function(){
            ajaxCall();
        },1000);

    });

})(angular);