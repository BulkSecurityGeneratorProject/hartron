(function() {
    'use strict';

    angular
        .module('studentApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cse', {
            parent: 'entity',
            url: '/cse',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'studentApp.cse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cse/cses.html',
                    controller: 'CseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cse-detail', {
            parent: 'cse',
            url: '/cse/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'studentApp.cse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cse/cse-detail.html',
                    controller: 'CseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cse', function($stateParams, Cse) {
                    return Cse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cse',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cse-detail.edit', {
            parent: 'cse-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cse/cse-dialog.html',
                    controller: 'CseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cse', function(Cse) {
                            return Cse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cse.new', {
            parent: 'cse',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cse/cse-dialog.html',
                    controller: 'CseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dept: null,
                                branch: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cse', null, { reload: 'cse' });
                }, function() {
                    $state.go('cse');
                });
            }]
        })
        .state('cse.edit', {
            parent: 'cse',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cse/cse-dialog.html',
                    controller: 'CseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cse', function(Cse) {
                            return Cse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cse', null, { reload: 'cse' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cse.delete', {
            parent: 'cse',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cse/cse-delete-dialog.html',
                    controller: 'CseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cse', function(Cse) {
                            return Cse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cse', null, { reload: 'cse' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
