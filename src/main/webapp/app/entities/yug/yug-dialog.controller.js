(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('YugDialogController', YugDialogController);

    YugDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Yug'];

    function YugDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Yug) {
        var vm = this;

        vm.yug = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.yug.id !== null) {
                Yug.update(vm.yug, onSaveSuccess, onSaveError);
            } else {
                Yug.save(vm.yug, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('studentApp:yugUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
