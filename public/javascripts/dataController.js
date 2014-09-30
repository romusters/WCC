(function(ng) {
    var app = ng.module("myApp", []);

    app.controller("dataController", function($scope) {
        $scope.current = {
            loc: "North",
            type: "Temperature",
            data: ""
        };
    });
})(angular);