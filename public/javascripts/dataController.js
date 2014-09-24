var myApp = angular.module('myApp', []);

myApp.controller('dataController', function($scope) {
    $scope.current = {
        loc: "North",
        type: "Temperature",
    };
    $scope.tab = function(name){
        $scope.current.type = name;
    };
    /* update location */
    $scope.loc = function(name){
        $scope.current.loc = name;
    };
});