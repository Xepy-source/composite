<%@ page import="com.ccrental.composite.common.vos.UserVo" %>
<%@ page import="com.ccrental.composite.common.utillity.Converter" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%
    UserVo userVo = Converter.getUserVo(request);
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <meta name="description" content="가성비 좋은 렌터카로! 중고차 견적 검색. 월간 렌터카 특가 이벤트. 제주 단기. 바로 출발 서비스로. 기다림 없이! 빠른 렌터카 대여. 제주 빠른 예약.">
    <meta name="referrer" content="no-referrer-when-downgrade">
    <meta name="robots" content="noindex, nofollow, none, noarchive, nosnippet, noimageindx, nocache">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>CC렌터카</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Nanum+Brush+Script&display=swap">
    <link rel="stylesheet" href="resources/stylesheets/common.css">
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=0e7db3a737cbd84734c3d59e62e1f667"></script>
    <script defer src="resources/scripts/dev.ajax.js"></script>
    <script defer src="resources/scripts/dev.dialog.js"></script>
    <script defer src="resources/scripts/dev.event.js"></script>
    <script defer src="resources/scripts/dev.loading.js"></script>
    <script defer src="resources/scripts/dev.router.js"></script>
    <script defer src="resources/scripts/common.js"></script>
</head>
<body class="preload">
<div id="js-loading" class="body-item loading visible">
    <div class="loading-item cover"></div>
    <div class="loading-item window">
        <img class="window-item image" src="resources/images/loading.window.image.png" alt="로딩">
    </div>
</div>
<div id="js-dialog" class="body-item dialog">
    <div class="dialog-item cover"></div>
    <div class="dialog-item window">
        <div class="window-item title"></div>
        <div class="window-item content"></div>
        <div class="window-item buttons"></div>
    </div>
</div>
<div id="js-forward" class="body-item forward">
    <div class="forward-item icon"></div>
    <div class="forward-item text">요청하신 컨텐츠를 불러오고 있습니다...</div>
</div>
<div id="js-header" class="body-item header" role="navigation">
    <div class="header-item content">
        <a class="content-item logo" href="../" target="_self">
            <img class="logo-item image" src="resources/images/header.content.logo.image.png" alt="로고">
        </a>
        <ul class="content-item menu" role="menu">
            <li class="menu-item jeju" role="menuitem">제주단기</li>
            <li class="menu-item land" role="menuitem">내륙단기</li>
        </ul>
        <ul class="content-item side-menu" role="menu">
            <% if (userVo == null) { %>
            <li class="side-menu-item login" role="menuitem">로그인</li>
            <% } else { %>
            <li class="side-menu-item logout" role="menuitem">로그아웃</li>
            <% } %>
            <li class="side-menu-item my" role="menuitem">MY렌터카</li>
            <li class="side-menu-item event" role="menuitem">이벤트</li>
            <li class="side-menu-item cs" role="menuitem">고객센터</li>
        </ul>
    </div>
    <div class="header-item line"></div>
</div>
<div id="js-main" class="body-item main" role="main"></div>
<div class="body-item copyright" role="doc-footnote">
    <div class="copyright-item">
        <div class="copyright-item-content contact">
            <div class="contact-item contact">
                <div class="contact-item icon"></div>
                <div class="contact-item text">CC렌터카 고객센터 0000-0000</div>
            </div>
            <div class="contact-item hour">
                <div class="hour-item text">일반상담 : 월~금 09:00~18:00(토요일 및 공휴일 휴무)</div>
                <div class="hour-item line"></div>
                <div class="hour-item text">사고접수 : 24시간 운영</div>
            </div>
            <div class="contact-item family">
                <div class="family-item text">Family Site</div>
                <div class="family-item icon"></div>
            </div>
        </div>
    </div>
    <div class="copyright-item">
        <div class="copyright-item-content copyright">
            <div class="copyright-item logo">
                <img class="logo-item image" src="resources/images/copyright.copyright.logo.image.png" alt="로고">
            </div>
            <div class="copyright-item info">
                <ul class="info-item disclaimer" role="menu">
                    <li class="disclaimer-item" role="menuitem">회사소개</li>
                    <li class="disclaimer-item" role="menuitem">Sales Partner 모집</li>
                    <li class="disclaimer-item" role="menuitem">이용약관</li>
                    <li class="disclaimer-item" role="menuitem">개인정보처리방침</li>
                    <li class="disclaimer-item" role="menuitem">이메일무단수집거부</li>
                    <li class="disclaimer-item" role="menuitem">고객센터</li>
                    <li class="disclaimer-item" role="menuitem">사이트맵</li>
                    <li class="disclaimer-item" role="menuitem">윤리경영 제보</li>
                </ul>
                <div class="info-item address">
                    <div class="address-item text">주소 : 대구광역시 중구 중앙대로 366 10층</div>
                    <div class="address-item line"></div>
                    <div class="address-item text">상호명 : CC렌터카(포트폴리오)</div>
                    <div class="address-item line"></div>
                    <div class="address-item text">대표이사 : 박아무개</div>
                    <div class="address-item line"></div>
                    <div class="address-item text">사업자번호 : 000-00-00000</div>
                    <br>
                    <div class="address-item text">통신판매업신고 : 중구청 제2020-00000</div>
                    <div class="address-item line"></div>
                    <div class="address-item text">TEL : 0000-0000</div>
                </div>
                <div class="info-item copyright">Copyright&copy; CC렌터카 All Rights Reserved.</div>
            </div>
            <div class="copyright-item social">
                <a class="social-item" href="https://www.facebook.com" target="_blank">
                    <img class="social-item-image" src="resources/images/copyright.copyright.social.facebook.png" alt="페이스북">
                </a>
                <a class="social-item" href="https://www.twitter.com" target="_blank">
                    <img class="social-item-image" src="resources/images/copyright.copyright.social.twitter.png" alt="트위터">
                </a>
                <a class="social-item" href="https://www.instagram.com" target="_blank">
                    <img class="social-item-image" src="resources/images/copyright.copyright.social.instagram.png" alt="인스타그램">
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>