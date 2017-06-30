(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('CseDialogController', CseDialogController);

    CseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cse'];

    function CseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cse) {
        var vm = this;

        vm.cse = entity;
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
            if (vm.cse.id !== null) {
                Cse.update(vm.cse, onSaveSuccess, onSaveError);
            } else {
                Cse.save(vm.cse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('studentApp:cseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
