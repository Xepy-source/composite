class Router {
    static getMainElement = () => {
        return window.document.body.querySelector('#js-main');
    }

    static getForwardElement = () => {
        return window.document.body.querySelector('#js-forward');
    }

    static showForward = () => {
        let forwardElement = Router.getForwardElement();
        if (!forwardElement.classList.contains('visible')) {
            forwardElement.classList.add('visible');
        }
    }

    static hideForward = () => {
        let forwardElement = Router.getForwardElement();
        forwardElement.classList.remove('visible');
    }

    static showMain = () => {
        let mainElement = Router.getMainElement();
        if (!mainElement.classList.contains('visible')) {
            mainElement.classList.add('visible');
        }
    }

    static hideMain = () => {
        let mainElement = Router.getMainElement();
        mainElement.classList.remove('visible');
    }

    static centralizeMain = () => {
        let mainElement = Router.getMainElement();
        if (!mainElement.classList.contains('prop-center')) {
            mainElement.classList.add('prop-center');
        }
    }

    static decentralizeMain = () => {
        let mainElement = Router.getMainElement();
        mainElement.classList.remove('prop-center');
    }

    static resolve = (onResolveDone, onResolveError) => {
        const hideForward = () => {
            setTimeout(() => {
                Router.hideForward();
            }, 250);
        }
        const showMain = () => {
            setTimeout(() => {
                Router.showMain();
            }, 250);
        }

        Router.hideMain();
        Router.showForward();
        let url = new URL(window.location.href);
        let action = url.searchParams.get('action');
        if (action === null) {
            action = 'main';
        }

        setTimeout(() => {
            const callback = (response, eventFunc) => {
                Router.getMainElement().innerHTML = response;
                if (typeof (eventFunc) === 'function') {
                    eventFunc();
                }
                if (typeof (onResolveDone) === 'function') {
                    onResolveDone();
                }
                hideForward();
                showMain();
            };
            const fallback = (status) => {
                Dialog.show('오류', '요청하신 컨텐츠를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['처음으로', '새로고침'], [
                    () => {
                        window.location.href = '../';
                    },
                    () => {
                        window.location.reload();
                    }
                ]);
                if (typeof (onResolveError) === 'function') {
                    onResolveError();
                }
                hideForward();
                showMain();
            };
            switch (action) {
                case 'my':
                    Ajax.request('GET', 'resources/parts/my.html', (response) => {
                        callback(response, Event.My.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;
                case 'event':
                    Ajax.request('GET', 'resources/parts/event.html', (response) => {
                        callback(response, Event.Event.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;

                case 'cs':
                    Ajax.request('GET', 'resources/parts/cs.html', (response) => {
                        callback(response, Event.CS.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;
                case 'inquiry':
                    Ajax.request('GET', 'resources/parts/inquiry.html', (response) => {
                        callback(response, Event.Inquiry.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;
                case 'branch':
                    Ajax.request('GET', 'resources/parts/branch.html', (response) => {
                        callback(response, Event.Branch.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;
                case 'repair':
                    Ajax.request('GET', 'resources/parts/repair.html', (response) => {
                        callback(response, Event.Repair.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;

                case 'login':
                    Ajax.request('GET', 'resources/parts/login.html', (response) => {
                        callback(response, Event.Login.attachEvents);
                        Router.centralizeMain();
                    }, fallback);
                    break;
                case 'find_email':
                    Ajax.request('GET', 'resources/parts/find_email.html', (response) => {
                        callback(response, Event.FindEmail.attachEvents);
                        Router.centralizeMain();
                    }, fallback);
                    break;
                case 'find_password':
                    Ajax.request('GET', 'resources/parts/find_password.html', (response) => {
                        callback(response, Event.FindPassword.attachEvents);
                        Router.centralizeMain();
                    }, fallback);
                    break;
                case 'reset_password':
                    Ajax.request('GET', 'resources/parts/reset_password.html', (response) => {
                        callback(response, Event.ResetPassword.attachEvents);
                        Router.centralizeMain();
                    }, fallback);
                    break;
                case 'register':
                    Ajax.request('GET', 'resources/parts/register.html', (response) => {
                        callback(response, Event.Register.attachEvents);
                        Router.centralizeMain();
                    }, fallback);
                    break;
                case 'register_input':
                    Ajax.request('GET', 'resources/parts/register_input.html', (response) => {
                        callback(response, Event.RegisterInput.attachEvents);
                        Router.centralizeMain();
                    }, fallback);
                    break;
                case 'register_complete':
                    Ajax.request('GET', 'resources/parts/register_complete.html', (response) => {
                        callback(response, Event.RegisterComplete.attachEvents);
                        Router.centralizeMain();
                    }, fallback);
                    break;

                case 'jeju':
                    Ajax.request('GET', 'resources/parts/jeju.html', (response) => {
                        callback(response, Event.Jeju.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;
                case 'land':
                    Ajax.request('GET', 'resources/parts/land.html', (response) => {
                        callback(response, Event.Land.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;
                case 'rent':
                    Ajax.request('GET', 'resources/parts/rent.html', (response) => {
                        callback(response, Event.Rent.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
                    break;

                default :
                    // Ajax.request('GET', 'resources/parts/main.html', (response) => {
                    //     callback(response, Event.Main.attachEvents);
                    //     Router.decentralizeMain();
                    // }, fallback);
                    // break;
                    Ajax.request('GET', 'resources/parts/jeju.html', (response) => {
                        callback(response, Event.Jeju.attachEvents);
                        Router.decentralizeMain();
                    }, fallback);
            }
        }, 200);
    }

    static forward = (action, onForwardDone, onForwardError, data) => {
        let url = new URL(`${window.location.origin}${window.location.pathname}`);
        url.searchParams.append('action', action);
        if (typeof (data) !== 'undefined') {
            for (let [key, value] of data) {
                if (url.searchParams.get(key) !== null) {
                    url.searchParams.delete(key);
                }
                url.searchParams.append(key, value);
            }
        }
        window.history.pushState(null, null, url.toString());
        console.log(url.toString());

        Router.resolve(() => {
            if (typeof (onForwardDone) === 'function') {
                onForwardDone();
            }
        }, () => {
            if (typeof (onForwardError) === 'function') {
                onForwardError();
            }
        });
    }
}