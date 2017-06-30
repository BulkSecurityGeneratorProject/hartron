(function() {
    'use strict';
    angular
        .module('studentApp')
        .factory('Cse', Cse);

    Cse.$inject = ['$resource'];

    function Cse ($resource) {
        var resourceUrl =  'api/cses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
