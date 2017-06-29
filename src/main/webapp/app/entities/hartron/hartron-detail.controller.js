(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('HartronDetailController', HartronDetailController);

    HartronDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Hartron'];

    function HartronDetailController($scope, $rootScope, $stateParams, previousState, entity, Hartron) {
        var vm = this;

        vm.hartron = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('studentApp:hartronUpdate', function(event, result) {
            vm.hartron = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
