// API 기본 URL - 백엔드 서버 주소로 변경
var API_BASE = '/api';

var api = {
    _req: function(method, path, data) {
        return $.ajax({
            url: API_BASE + path,
            method: method,
            contentType: 'application/json',
            data: data ? JSON.stringify(data) : undefined
        });
    },

    // Owners
    findOwners: function(lastName) {
        return this._req('GET', '/owners' + (lastName ? '?lastName=' + encodeURIComponent(lastName) : ''));
    },
    getOwner: function(id) {
        return this._req('GET', '/owners/' + id);
    },
    createOwner: function(data) {
        return this._req('POST', '/owners', data);
    },
    updateOwner: function(id, data) {
        return this._req('PUT', '/owners/' + id, data);
    },

    // Pets
    getPetTypes: function() {
        return this._req('GET', '/pet-types');
    },
    addPet: function(ownerId, data) {
        return this._req('POST', '/owners/' + ownerId + '/pets', data);
    },
    updatePet: function(ownerId, petId, data) {
        return this._req('PUT', '/owners/' + ownerId + '/pets/' + petId, data);
    },

    // Visits
    addVisit: function(ownerId, petId, data) {
        return this._req('POST', '/owners/' + ownerId + '/pets/' + petId + '/visits', data);
    },

    // Vets
    getVets: function() {
        return this._req('GET', '/vets');
    }
};
