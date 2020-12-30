class Loading {
    static getElement = () => {
        return window.document.body.querySelector('#js-loading');
    }

    static show = () => {
        let loadingElement = Loading.getElement();
        if (!loadingElement.classList.contains('visible')) {
            loadingElement.classList.add('visible');
        }
    }

    static hide = () => {
        let loadingElement = Loading.getElement();
        loadingElement.classList.remove('visible');
    }
}