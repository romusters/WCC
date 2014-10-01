(function(ng) {
    var app = ng.module("myApp", []);

    app.controller("dataController", function($scope) {
        $scope.current = {
            loc: "North",
            type: "Temperature",
            data: ""
        };
        $scope.tab = function(name){
            $scope.current.type = name;
        };
        /* update location */
        $scope.loc = function(name){
            $scope.current.loc = name;
        };
    });
})(angular);