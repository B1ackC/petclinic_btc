// 원본 JSP layout.tag / htmlHeader.tag / menu.tag / footer.tag 재현

function renderLayout(opts) {
    // opts: { active, root }
    var root = opts.root || '..';

    // htmlHeader는 각 페이지 <head>에 직접 작성
    // bodyHeader (nav) 삽입
    var navHtml = buildNav(opts.active, root);
    document.getElementById('nav-placeholder').innerHTML = navHtml;

    // pivotal.tag 삽입
    var pivotalHtml =
        '<br/><br/>' +
        '<div class="container"><div class="row">' +
        '<div class="col-12 text-center">' +
        '<img src="' + root + '/resources/images/spring-pivotal-logo.png" alt="Sponsored by Pivotal"/>' +
        '</div></div></div>';
    document.getElementById('pivotal-placeholder').innerHTML = pivotalHtml;
}

function buildNav(active, root) {
    function item(key, href, icon, label) {
        var cls = active === key ? ' class="active"' : '';
        return '<li' + cls + '><a href="' + href + '" title="' + label + '">' +
            '<span class="glyphicon glyphicon-' + icon + '" aria-hidden="true"></span>' +
            '<span>' + label + '</span></a></li>';
    }
    return '<nav class="navbar navbar-default" role="navigation">' +
        '<div class="container">' +
        '<div class="navbar-header">' +
        '<a class="navbar-brand" href="' + root + '/index.html"><span></span></a>' +
        '<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar">' +
        '<span class="sr-only">메뉴 열기</span>' +
        '<span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>' +
        '</button></div>' +
        '<div class="navbar-collapse collapse" id="main-navbar">' +
        '<ul class="nav navbar-nav navbar-right">' +
        item('home',   root + '/index.html',        'home',         '홈') +
        item('owners', root + '/owners/index.html',  'search',       '보호자 검색') +
        item('vets',   root + '/vets/index.html',    'th-list',      '수의사') +
        item('error',  root + '/error.html',         'warning-sign', '오류') +
        '</ul></div></div></nav>';
}

function showFieldError(fieldId, msg) {
    var $g = $('#' + fieldId).closest('.form-group');
    $g.addClass('has-error');
    $g.find('.help-inline').text(msg);
    $g.find('.glyphicon-ok').hide();
    $g.find('.glyphicon-remove').show();
}

function clearFieldErrors() {
    $('.form-group').removeClass('has-error').find('.help-inline').text('');
    $('.glyphicon-remove').hide();
}
