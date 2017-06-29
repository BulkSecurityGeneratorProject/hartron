(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('HartronDialogController', HartronDialogController);

    HartronDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hartron'];

    function HartronDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hartron) {
        var vm = this;

        vm.hartron = entity;
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
            if (vm.hartron.id !== null) {
                Hartron.update(vm.hartron, onSaveSuccess, onSaveError);
            } else {
                Hartron.save(vm.hartron, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('studentApp:hartronUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
