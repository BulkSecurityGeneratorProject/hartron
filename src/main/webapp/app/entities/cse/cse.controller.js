(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('CseController', CseController);

    CseController.$inject = ['Cse'];

    function CseController(Cse) {

        var vm = this;

        vm.cses = [];

        loadAll();

        function loadAll() {
            Cse.query(function(result) {
                vm.cses = result;
                vm.searchQuery = null;
            });
        }
    }
})();
