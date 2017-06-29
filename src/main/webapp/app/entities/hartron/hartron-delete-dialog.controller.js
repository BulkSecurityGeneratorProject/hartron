(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('HartronDeleteController',HartronDeleteController);

    HartronDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hartron'];

    function HartronDeleteController($uibModalInstance, entity, Hartron) {
        var vm = this;

        vm.hartron = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hartron.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
