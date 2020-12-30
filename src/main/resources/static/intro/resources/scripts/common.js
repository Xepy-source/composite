window.addEventListener('DOMContentLoaded', () => {
    let copyrightElement = window.document.body.querySelector('#js-copyright');
    let copyrightCloseElement = copyrightElement.querySelector('div.copyright-item.close');
    copyrightCloseElement.addEventListener('click', () => {
        copyrightElement.classList.remove('visible');
    });
});

window.addEventListener('load', () => {
    setTimeout(() => {
        window.document.body.classList.remove('preload');
    }, 100);
    setTimeout(() => {
        let loadingElement = window.document.body.querySelector('#js-loading');
        loadingElement.classList.remove('visible');

        setTimeout(() => {
            let warningElement = window.document.body.querySelector('#js-warning');
            let warningCloseElement = warningElement.querySelector('div.buttons-item.close');
            let copyrightElement = window.document.body.querySelector('#js-copyright');
            warningElement.classList.add('visible');
            warningCloseElement.addEventListener('click', () => {
                warningElement.classList.remove('visible');
            });
            copyrightElement.classList.add('visible');
        },500);

        let contentElement = window.document.body.querySelector('#js-content');
        let menuItemElements = contentElement.querySelectorAll('ul.content-item.menu > li.menu-item');
        menuItemElements[0].addEventListener('click', () => {
            window.location.href = '/main/?action=jeju';
        });
        menuItemElements[1].addEventListener('click', () => {
            window.location.href = '/main/?action=land';
        });
        for (let i = 0; i < menuItemElements.length; i++) {
            setTimeout(() => {
                menuItemElements[i].classList.add('visible');
            }, (i + 1) * 125);
        }
        let sloganElement = contentElement.querySelector('div.content-item.slogan');
        setTimeout(() => {
            sloganElement.classList.add('visible');
        }, 300);
    }, 500);
});