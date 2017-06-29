(function() {
    'use strict';

    angular
        .module('studentApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hartron', {
            parent: 'entity',
            url: '/hartron',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'studentApp.hartron.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hartron/hartrons.html',
                    controller: 'HartronController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hartron');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hartron-detail', {
            parent: 'hartron',
            url: '/hartron/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'studentApp.hartron.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hartron/hartron-detail.html',
                    controller: 'HartronDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hartron');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Hartron', function($stateParams, Hartron) {
                    return Hartron.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hartron',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hartron-detail.edit', {
            parent: 'hartron-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hartron/hartron-dialog.html',
                    controller: 'HartronDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hartron', function(Hartron) {
                            return Hartron.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hartron.new', {
            parent: 'hartron',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hartron/hartron-dialog.html',
                    controller: 'HartronDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                employee_name: null,
                                designation: null,
                                phone_no: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hartron', null, { reload: 'hartron' });
                }, function() {
                    $state.go('hartron');
                });
            }]
        })
        .state('hartron.edit', {
            parent: 'hartron',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hartron/hartron-dialog.html',
                    controller: 'HartronDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hartron', function(Hartron) {
                            return Hartron.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hartron', null, { reload: 'hartron' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hartron.delete', {
            parent: 'hartron',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hartron/hartron-delete-dialog.html',
                    controller: 'HartronDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hartron', function(Hartron) {
                            return Hartron.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hartron', null, { reload: 'hartron' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
