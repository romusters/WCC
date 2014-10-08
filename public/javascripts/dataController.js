(function(ng) {
    var app = ng.module("myApp", []);

    app.controller("dataController", function($scope) {
        $scope.current = {
            loc: "North",
            type: "Temperature",
            data: "",
            click: "hai"
        };
        $scope.tab = function(name){
            $scope.current.type = name;
        };
        /* update location */
        $scope.loc = function(name){
            $scope.current.loc = name;
            $scope.current.click = 1;

        };
    });
})(angular);