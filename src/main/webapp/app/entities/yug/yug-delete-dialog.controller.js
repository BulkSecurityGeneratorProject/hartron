(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('YugDeleteController',YugDeleteController);

    YugDeleteController.$inject = ['$uibModalInstance', 'entity', 'Yug'];

    function YugDeleteController($uibModalInstance, entity, Yug) {
        var vm = this;

        vm.yug = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Yug.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
