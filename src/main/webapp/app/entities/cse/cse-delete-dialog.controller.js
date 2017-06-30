(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('CseDeleteController',CseDeleteController);

    CseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cse'];

    function CseDeleteController($uibModalInstance, entity, Cse) {
        var vm = this;

        vm.cse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
