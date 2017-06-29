(function() {
    'use strict';

    angular
        .module('studentApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('yug', {
            parent: 'entity',
            url: '/yug',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'studentApp.yug.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/yug/yugs.html',
                    controller: 'YugController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('yug');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('yug-detail', {
            parent: 'yug',
            url: '/yug/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'studentApp.yug.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/yug/yug-detail.html',
                    controller: 'YugDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('yug');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Yug', function($stateParams, Yug) {
                    return Yug.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'yug',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('yug-detail.edit', {
            parent: 'yug-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/yug/yug-dialog.html',
                    controller: 'YugDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Yug', function(Yug) {
                            return Yug.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('yug.new', {
            parent: 'yug',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/yug/yug-dialog.html',
                    controller: 'YugDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                sex: null,
                                email: null,
                                age: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('yug', null, { reload: 'yug' });
                }, function() {
                    $state.go('yug');
                });
            }]
        })
        .state('yug.edit', {
            parent: 'yug',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/yug/yug-dialog.html',
                    controller: 'YugDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Yug', function(Yug) {
                            return Yug.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('yug', null, { reload: 'yug' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('yug.delete', {
            parent: 'yug',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/yug/yug-delete-dialog.html',
                    controller: 'YugDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Yug', function(Yug) {
                            return Yug.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('yug', null, { reload: 'yug' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
