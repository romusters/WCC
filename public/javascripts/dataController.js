(function(ng) {
    var app = ng.module("myApp", []);

    app.controller("dataController", function($scope) {
        $scope.current = {
            loc: "North",
            type: "Temperature",
            data: ""
        };
        $scope.source = new EventSource("/json/" + $scope.current.loc.toLowerCase() + "/" + $scope.current.type.toLowerCase());
        $scope.source.onmessage = function (event) {
            $scope.$apply(function () {
                $scope.current.data(Json.parse(event.data));
            });
        };
    });
})(angular);