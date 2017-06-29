(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('HartronController', HartronController);

    HartronController.$inject = ['Hartron'];

    function HartronController(Hartron) {

        var vm = this;

        vm.hartrons = [];

        loadAll();

        function loadAll() {
            Hartron.query(function(result) {
                vm.hartrons = result;
                vm.searchQuery = null;
            });
        }
    }
})();
