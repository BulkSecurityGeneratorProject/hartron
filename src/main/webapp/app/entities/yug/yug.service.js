(function() {
    'use strict';
    angular
        .module('studentApp')
        .factory('Yug', Yug);

    Yug.$inject = ['$resource'];

    function Yug ($resource) {
        var resourceUrl =  'api/yugs/:id';

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
