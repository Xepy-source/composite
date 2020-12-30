class Dialog {
    static getElement = () => {
        return window.document.body.querySelector('#js-dialog');
    }

    static show = (title, content, buttons, events) => {
        let dialogElement = Dialog.getElement();
        let dialogTitleElement = dialogElement.querySelector('div.window-item.title');
        let dialogContentElement = dialogElement.querySelector('div.window-item.content');
        let dialogButtonsElement = dialogElement.querySelector('div.window-item.buttons');
        dialogTitleElement.innerText = title;
        dialogContentElement.innerHTML = content;
        dialogButtonsElement.innerHTML = '';
        if (typeof (buttons) !== 'undefined' && typeof (events) !== 'undefined' && buttons.length === events.length) {
            for (let i = 0; i < buttons.length; i++) {
                let buttonElement = window.document.createElement('div');
                buttonElement.classList.add('buttons-item');
                buttonElement.innerText = buttons[i];
                buttonElement.tabIndex = 0;
                buttonElement.addEventListener('click', events[i]);
                buttonElement.addEventListener('keypress', (e) => {
                    if (e.key === 'Enter') {
                        events[i]();
                    }
                });
                dialogButtonsElement.append(buttonElement);
            }
            dialogButtonsElement.querySelectorAll('div.buttons-item')[0].focus();
        }
        if (!dialogElement.classList.contains('visible')) {
            dialogElement.classList.add('visible');
        }
    }

    static hide = () => {
        let dialogElement = Dialog.getElement();
        dialogElement.classList.remove('visible');
    }
}