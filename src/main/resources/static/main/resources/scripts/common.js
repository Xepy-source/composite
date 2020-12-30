const onPageReady = () => {
    setTimeout(() => {
        Loading.hide();

        let headerElement = window.document.body.querySelector('#js-header');
        let logoElement = headerElement.querySelector('a.content-item.logo');
        logoElement.classList.add('visible');
        let menuItemElements = headerElement.querySelectorAll('li.menu-item');
        for (let i = 0; i < menuItemElements.length; i++) {
            setTimeout(() => {
                menuItemElements[i].classList.add('visible');
            }, (i + 1) * 125);
        }
        let sideMenuItemElements = headerElement.querySelectorAll('li.side-menu-item');
        for (let i = 0; i < sideMenuItemElements.length; i++) {
            setTimeout(() => {
                sideMenuItemElements[i].classList.add('visible');
            }, (i + 1) * 125);
        }
    }, 500);
};

window.addEventListener('DOMContentLoaded', () => {
    Event.Common.attachEvents();
});

window.addEventListener('load', () => {
    setTimeout(() => {
        window.document.body.classList.remove('preload');
    }, 100);
    setTimeout(() => {
        Router.resolve(onPageReady);
    }, 200);
});

window.addEventListener('popstate', () => {
    Router.resolve();
});