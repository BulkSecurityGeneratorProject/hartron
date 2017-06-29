(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('YugDetailController', YugDetailController);

    YugDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Yug'];

    function YugDetailController($scope, $rootScope, $stateParams, previousState, entity, Yug) {
        var vm = this;

        vm.yug = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('studentApp:yugUpdate', function(event, result) {
            vm.yug = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
