(function() {
    'use strict';

    angular
        .module('studentApp')
        .controller('YugController', YugController);

    YugController.$inject = ['Yug'];

    function YugController(Yug) {

        var vm = this;

        vm.yugs = [];

        loadAll();

        function loadAll() {
            Yug.query(function(result) {
                vm.yugs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
