(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('CseDetailController', CseDetailController);

    CseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cse'];

    function CseDetailController($scope, $rootScope, $stateParams, previousState, entity, Cse) {
        var vm = this;

        vm.cse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('studentApp:cseUpdate', function(event, result) {
            vm.cse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
