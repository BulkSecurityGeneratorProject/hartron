(function() {
    'use strict';
    angular
        .module('studentApp')
        .factory('Hartron', Hartron);

    Hartron.$inject = ['$resource'];

    function Hartron ($resource) {
        var resourceUrl =  'api/hartrons/:id';

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
