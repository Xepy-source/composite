class Event {
    static Common = class {
        static attachEvents = () => {
            let headerElement = window.document.body.querySelector('#js-header');
            let headerMenuElement = headerElement.querySelector('ul.content-item.menu');
            let headerJejuMenuElement = headerMenuElement.querySelector('li.menu-item.jeju');
            headerJejuMenuElement.addEventListener('click', () => {
                Router.forward('jeju');
            });
            let headerLandMenuElement = headerMenuElement.querySelector('li.menu-item.land');
            headerLandMenuElement.addEventListener('click', () => {
                Router.forward('land');
            });
            let headerSideMenuElement = headerElement.querySelector('ul.content-item.side-menu');
            headerSideMenuElement.querySelector('li.side-menu-item.login')?.addEventListener('click', () => {
                Router.forward('login');
            });
            headerSideMenuElement.querySelector('li.side-menu-item.logout')?.addEventListener('click', () => {
                const callback = (response) => {
                    let json = JSON.parse(response);
                    if (json['result'] === 'success') {
                        let url = new URL(window.location.href);
                        let action = url.searchParams.get('action');
                        if (action !== null) {
                            url.searchParams.delete('action');
                        }
                        url.searchParams.append('action', 'main');
                        window.location.href = url.toString();
                    } else {
                        fallback();
                    }
                }
                const fallback = () => {
                    Dialog.show('로그아웃', '로그아웃 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                        Dialog.hide();
                    }]);
                }
                Ajax.request('POST', '/apis/user/logout', callback, fallback);
            });
            headerSideMenuElement.querySelector('li.side-menu-item.my').addEventListener('click', () => {
                Router.forward('my');
            });
            headerSideMenuElement.querySelector('li.side-menu-item.event').addEventListener('click', () => {
                Router.forward('event');
            });
            headerSideMenuElement.querySelector('li.side-menu-item.cs').addEventListener('click', () => {
                Router.forward('cs');
            });
        }
    }

    static My = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let tableElement = mainElement.querySelector('table.my-item.table');
            let tbodyElement = tableElement.querySelector('tbody');
            tbodyElement.innerHTML = '';

            // {
            //     "result"
            // :
            //     "success",
            //         "rentalInfo"
            // :
            //     [{
            //         "fromDate": "2020-01-01",
            //         "userIndex": 17,
            //         "toDate": "2020-01-02",
            //         "fromTime": "23:00:00",
            //         "branchIndex": 27,
            //         "toTime": "23:30:00",
            //         "carIndex": 92
            //     }]
            // }

            const myCallback = (response) => {
                let json = JSON.parse(response);
                if (json['result'] === 'success') {
                    for (let i = 0; i < json['rentalInfo'].length; i++) {
                        let tr = window.document.createElement('tr');
                        let tdFrom = window.document.createElement('td');
                        let tdTo = window.document.createElement('td');
                        let tdBranch = window.document.createElement('td');
                        let tdCar = window.document.createElement('td');
                        tdFrom.innerText = `${json['rentalInfo'][i]['fromDate']} ${json['rentalInfo'][i]['fromTime']}`;
                        tdTo.innerText = `${json['rentalInfo'][i]['toDate']} ${json['rentalInfo'][i]['toTime']}`;
                        tr.append(tdFrom);
                        tr.append(tdTo);
                        tr.append(tdBranch);
                        tr.append(tdCar);
                        tbodyElement.append(tr);

                        let url = new URL(window.location.href);
                        const branchCallback = (response) => {
                            let jsonBranch = JSON.parse(response);
                            tdBranch.innerText = jsonBranch['name'];
                        };
                        const branchFallback = () => {
                            Dialog.show('빠른예약', '일부 정보를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                                Dialog.hide();
                                Router.forward('main');
                            }]);
                        };
                        let branchFormData = new FormData();
                        branchFormData.append('index', json['rentalInfo'][i]['branchIndex']);
                        Ajax.request('POST', '/apis/rental/get-branch-info', branchCallback, branchFallback, branchFormData);

                        const carCallback = (response) => {
                            let jsonCar = JSON.parse(response);
                            tdCar.innerText = jsonCar['name'];
                        };
                        const carFallback = () => {
                            Dialog.show('빠른예약', '일부 정보를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                                Dialog.hide();
                                Router.forward('main');
                            }]);
                        };
                        let carFormData = new FormData();
                        carFormData.append('index', json['rentalInfo'][i]['carIndex']);
                        Ajax.request('POST', '/apis/rental/get-car-info', carCallback, carFallback, carFormData);
                    }
                } else {
                    myFallback();
                }
            };
            const myFallback = () => {
                Dialog.show('MY렌터카', '대여 정보를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                    Dialog.hide();
                    Router.forward('main');
                }]);
            };
            Ajax.request('POST', '/apis/rental/book', myCallback, myFallback);
        }
    }

    static CS = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainMenuElement = mainElement.querySelector('ul.cs-item.menu');
            mainMenuElement.querySelector('li.menu-item.cs').addEventListener('click', () => {
                Router.forward('cs');
            });
            mainMenuElement.querySelector('li.menu-item.inquiry').addEventListener('click', () => {
                Router.forward('inquiry');
            });
            mainMenuElement.querySelector('li.menu-item.branch').addEventListener('click', () => {
                Router.forward('branch');
            });
            mainMenuElement.querySelector('li.menu-item.repair').addEventListener('click', () => {
                Router.forward('repair');
            });

            let getNotices = (page) => {
                const callback = (response) => {
                    let json = JSON.parse(response);
                    if (json['result'] !== 'success') {
                        fallback();
                        return;
                    }
                    let mainTableElement = mainElement.querySelector('table.cs-item.notice');
                    let mainTableBodyElement = mainTableElement.querySelector('tbody');
                    let notices = json['notices'];
                    mainTableBodyElement.innerHTML = '';
                    for (let i = 0; i < notices.length; i++) {
                        let noticeElement = window.document.createElement('tr');
                        let noticeIdElement = window.document.createElement('td');
                        let noticeTypeElement = window.document.createElement('td');
                        let noticeTitleElement = window.document.createElement('td');
                        let noticeTitleLinkElement = window.document.createElement('a');
                        let noticeDateElement = window.document.createElement('td');
                        noticeIdElement.innerText = notices[i]['notice_id'];
                        noticeTypeElement.innerText = '공지사항';
                        noticeTitleLinkElement.classList.add('notice-item');
                        noticeTitleLinkElement.classList.add('link');
                        noticeTitleLinkElement.innerText = notices[i]['title'];
                        noticeTitleElement.append(noticeTitleLinkElement);
                        noticeTitleElement.addEventListener('click', () => {
                            const readCallback = (response) => {
                                let json = JSON.parse(response);
                                if (json['result'] === 'success') {
                                    Dialog.show('공지사항', `${json['title']}<br><br>${json['content']}`, ['확인'], [() => {
                                        Dialog.hide();
                                    }]);
                                } else {
                                    readFallback();
                                }
                            }
                            const readFallback = () => {
                                Dialog.show('공지사항', '공지사항 데이터를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                                    Dialog.hide();
                                }]);
                            }
                            let formData = new FormData();
                            formData.append('notice_id', notices[i]['notice_id']);
                            Ajax.request('POST', '/apis/cs/notice/read', readCallback, readFallback, formData);
                        });
                        noticeDateElement.innerText = notices[i]['written_at'].substr(0, 10);
                        noticeElement.append(noticeIdElement);
                        noticeElement.append(noticeTypeElement);
                        noticeElement.append(noticeTitleElement);
                        noticeElement.append(noticeDateElement);
                        mainTableBodyElement.append(noticeElement);
                    }
                    let mainTableFootElement = mainTableElement.querySelector('tfoot');
                    let mainTableFootPageElement = mainTableFootElement.querySelector('div.notice-item.page');
                    mainTableFootPageElement.innerHTML = '';

                    let firstButtonElement = window.document.createElement('div');
                    firstButtonElement.classList.add('page-item');
                    firstButtonElement.classList.add('first');
                    let prevButtonElement = window.document.createElement('div');
                    prevButtonElement.classList.add('page-item');
                    prevButtonElement.classList.add('prev');
                    let nextButtonElement = window.document.createElement('div');
                    nextButtonElement.classList.add('page-item');
                    nextButtonElement.classList.add('next');
                    let lastButtonElement = window.document.createElement('div');
                    lastButtonElement.classList.add('page-item');
                    lastButtonElement.classList.add('last');

                    let startPage = parseInt(json['start_page']);
                    let endPage = parseInt(json['end_page']);
                    let reqPage = parseInt(json['request_page']);
                    let maxPage = parseInt(json['max_page']);
                    if (reqPage > 1) {
                        firstButtonElement.addEventListener('click', () => {
                            getNotices(1);
                        });
                        prevButtonElement.addEventListener('click', () => {
                            getNotices(reqPage - 1);
                        });
                        mainTableFootPageElement.append(firstButtonElement);
                        mainTableFootPageElement.append(prevButtonElement);
                    }
                    for (let i = startPage; i <= endPage; i++) {
                        let pageButtonElement = window.document.createElement('div');
                        pageButtonElement.classList.add('page-item');
                        pageButtonElement.innerText = `${i}`;
                        if (i === reqPage) {
                            pageButtonElement.classList.add('selected');
                        } else {
                            pageButtonElement.addEventListener('click', () => {
                                getNotices(i);
                            });
                        }
                        mainTableFootPageElement.append(pageButtonElement);
                    }
                    if (reqPage < maxPage) {
                        nextButtonElement.addEventListener('click', () => {
                            getNotices(reqPage + 1);
                        });
                        lastButtonElement.addEventListener('click', () => {
                            getNotices(maxPage);
                        });
                        mainTableFootPageElement.append(nextButtonElement);
                        mainTableFootPageElement.append(lastButtonElement);
                    }
                };
                const fallback = () => {
                    Dialog.show('공지사항', '서버에서 공지사항을 받아오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시시도해주세요.', ['확인'], [() => {
                        Dialog.hide();
                    }]);
                }

                let formData = new FormData();
                formData.append('page', page);
                Ajax.request('POST', '/apis/cs/notice/get', callback, fallback, formData);
            }
            getNotices(1);

            let searchFormElement = mainElement.querySelector('form.cs-item.search');
            const searchNotice = (page) => {
                const callback = (response) => {
                    let json = JSON.parse(response);
                    if (json['result'] !== 'success') {
                        fallback();
                        return;
                    }
                    let mainTableElement = mainElement.querySelector('table.cs-item.notice');
                    let mainTableBodyElement = mainTableElement.querySelector('tbody');
                    let notices = json['notices'];
                    mainTableBodyElement.innerHTML = '';
                    for (let i = 0; i < notices.length; i++) {
                        let noticeElement = window.document.createElement('tr');
                        let noticeIdElement = window.document.createElement('td');
                        let noticeTypeElement = window.document.createElement('td');
                        let noticeTitleElement = window.document.createElement('td');
                        let noticeTitleLinkElement = window.document.createElement('a');
                        let noticeDateElement = window.document.createElement('td');
                        noticeIdElement.innerText = notices[i]['notice_id'];
                        noticeTypeElement.innerText = '공지사항';
                        noticeTitleLinkElement.classList.add('notice-item');
                        noticeTitleLinkElement.classList.add('link');
                        noticeTitleLinkElement.innerText = notices[i]['title'];
                        noticeTitleElement.append(noticeTitleLinkElement);
                        noticeTitleElement.addEventListener('click', () => {
                            const readCallback = (response) => {
                                let json = JSON.parse(response);
                                if (json['result'] === 'success') {
                                    Dialog.show('공지사항', `${json['title']}<br><br>${json['content']}`, ['확인'], [() => {
                                        Dialog.hide();
                                    }]);
                                } else {
                                    readFallback();
                                }
                            }
                            const readFallback = () => {
                                Dialog.show('공지사항', '공지사항 데이터를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                                    Dialog.hide();
                                }]);
                            }
                            let formData = new FormData();
                            formData.append('notice_id', notices[i]['notice_id']);
                            Ajax.request('POST', '/apis/cs/notice/read', readCallback, readFallback, formData);
                        });
                        noticeDateElement.innerText = notices[i]['written_at'].substr(0, 10);
                        noticeElement.append(noticeIdElement);
                        noticeElement.append(noticeTypeElement);
                        noticeElement.append(noticeTitleElement);
                        noticeElement.append(noticeDateElement);
                        mainTableBodyElement.append(noticeElement);
                    }
                    let mainTableFootElement = mainTableElement.querySelector('tfoot');
                    let mainTableFootPageElement = mainTableFootElement.querySelector('div.notice-item.page');
                    mainTableFootPageElement.innerHTML = '';

                    let firstButtonElement = window.document.createElement('div');
                    firstButtonElement.classList.add('page-item');
                    firstButtonElement.classList.add('first');
                    let prevButtonElement = window.document.createElement('div');
                    prevButtonElement.classList.add('page-item');
                    prevButtonElement.classList.add('prev');
                    let nextButtonElement = window.document.createElement('div');
                    nextButtonElement.classList.add('page-item');
                    nextButtonElement.classList.add('next');
                    let lastButtonElement = window.document.createElement('div');
                    lastButtonElement.classList.add('page-item');
                    lastButtonElement.classList.add('last');

                    let startPage = parseInt(json['start_page']);
                    let endPage = parseInt(json['end_page']);
                    let reqPage = parseInt(json['request_page']);
                    let maxPage = parseInt(json['max_page']);
                    if (reqPage > 1) {
                        firstButtonElement.addEventListener('click', () => {
                            searchNotice(1);
                        });
                        prevButtonElement.addEventListener('click', () => {
                            searchNotice(reqPage - 1);
                        });
                        mainTableFootPageElement.append(firstButtonElement);
                        mainTableFootPageElement.append(prevButtonElement);
                    }
                    for (let i = startPage; i <= endPage; i++) {
                        let pageButtonElement = window.document.createElement('div');
                        pageButtonElement.classList.add('page-item');
                        pageButtonElement.innerText = `${i}`;
                        if (i === reqPage) {
                            pageButtonElement.classList.add('selected');
                        } else {
                            pageButtonElement.addEventListener('click', () => {
                                searchNotice(i);
                            });
                        }
                        mainTableFootPageElement.append(pageButtonElement);
                    }
                    if (reqPage < maxPage) {
                        nextButtonElement.addEventListener('click', () => {
                            searchNotice(reqPage + 1);
                        });
                        lastButtonElement.addEventListener('click', () => {
                            searchNotice(maxPage);
                        });
                        mainTableFootPageElement.append(nextButtonElement);
                        mainTableFootPageElement.append(lastButtonElement);
                    }
                };
                const fallback = () => {
                    Dialog.show('공지사항', '공지사항을 검색하는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시시도해주세요.', ['확인'], [() => {
                        Dialog.hide();
                    }]);
                }
                if (typeof (page) === 'undefined') {
                    page = '1';
                }
                let formData = new FormData();
                formData.append('category', searchFormElement.elements['category'].value);
                formData.append('keyword', searchFormElement.elements['keyword'].value);
                formData.append('page', page);
                Ajax.request('POST', '/apis/cs/notice/search', callback, fallback, formData);
                return false;
            }
            searchFormElement.onsubmit = () => {
                if (searchFormElement.elements['keyword'].value === '') {
                    searchFormElement.elements['keyword'].focus();
                } else {
                    searchNotice();
                }
                return false;
            };
        }
    }

    static Inquiry = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainMenuElement = mainElement.querySelector('ul.inquiry-item.menu');
            mainMenuElement.querySelector('li.menu-item.cs').addEventListener('click', () => {
                Router.forward('cs');
            });
            mainMenuElement.querySelector('li.menu-item.inquiry').addEventListener('click', () => {
                Router.forward('inquiry');
            });
            mainMenuElement.querySelector('li.menu-item.branch').addEventListener('click', () => {
                Router.forward('branch');
            });
            mainMenuElement.querySelector('li.menu-item.repair').addEventListener('click', () => {
                Router.forward('repair');
            });

            let formElement = mainElement.querySelector('form.inquiry-item.form');
            formElement.elements['title'].focus();
            formElement.onsubmit = () => {
                if (formElement.elements['title'].value === '') {
                    Dialog.show('문의', '문의 제목을 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        formElement.elements['title'].focus();
                    }]);
                } else if (formElement.elements['content'].value === '') {
                    Dialog.show('문의', '문의 내용을 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        formElement.elements['content'].focus();
                    }]);
                } else {
                    const inquiryCallback = (response) => {
                        let json = JSON.parse(response);
                        if (json['result'] === 'not_authority') {
                            Dialog.show('문의', '문의 접수는 회원만 가능합니다. 회원이시라면 로그인 후에, 비회원이시라면 회원가입 후에 시도해주시기 바랍니다.', ['로그인', '확인'], [
                                () => {
                                    Dialog.hide();
                                    Router.forward('login');
                                },
                                () => {
                                    Dialog.hide();
                                }
                            ]);
                        } else if (json['result'] === 'success') {
                            Dialog.show('문의', '문의를 성공적으로 접수하였습니다. 상담사가 문의를 확인한 후 최대한 빠른 시일내로 답장드리겠습니다.', ['확인'], [() => {
                                Dialog.hide();
                                Router.forward('inquiry');
                            }]);
                        } else {
                            inquiryFallback();
                        }
                    };
                    const inquiryFallback = () => {
                        Dialog.show('문의', '문의를 접수하는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                            Dialog.hide();
                            formElement.elements['content'].focus();
                        }]);
                    };
                    let formData = new FormData();
                    formData.append('title', formElement.elements['title'].value);
                    formData.append('content', formElement.elements['content'].value);
                    Ajax.request('POST', '/apis/cs/inquiry/add', inquiryCallback, inquiryFallback, formData);
                }
                return false;
            }

            let myElement = mainElement.querySelector('div.inquiry-item.my');
            const myCallback = (response) => {
                let json = JSON.parse(response);
                if (json['result'] === 'success') {
                    myElement.innerHTML = '';
                    let inquiries = json['inquirys'];
                    for (let i = 0; i < inquiries.length; i++) {
                        let inquiryElement = window.document.createElement('div');
                        let qElement = window.document.createElement('div');
                        let qTitleElement = window.document.createElement('div');
                        let qContentElement = window.document.createElement('div');
                        inquiryElement.classList.add('my-item');
                        qElement.classList.add('my-item-q');
                        qTitleElement.classList.add('my-item-q-title');
                        qTitleElement.innerText = `[${inquiries[i]['date']}] ${inquiries[i]['title']}`;
                        qContentElement.classList.add('my-item-q-content');
                        qContentElement.innerText = inquiries[i]['content'];
                        qElement.append(qTitleElement);
                        qElement.append(qContentElement);
                        inquiryElement.append(qElement);
                        myElement.append(inquiryElement);

                        if (typeof (inquiries[i]['answer'][0]['answerContent']) !== 'undefined') {
                            let aElement = window.document.createElement('div');
                            aElement.classList.add('my-item-a');
                            aElement.innerText = inquiries[i]['answer'][0]['answerContent'];
                            inquiryElement.append(aElement);
                        }
                    }
                } else {
                    myFallback();
                }
            };
            const myFallback = () => {
                Dialog.show('문의', '문의 접수는 회원만 가능합니다. 회원이시라면 로그인 후에, 비회원이시라면 회원가입 후에 시도해주시기 바랍니다.', ['로그인'], [
                    () => {
                        Dialog.hide();
                        Router.forward('login');
                    }
                ]);
            }
            Ajax.request('POST', '/apis/cs/inquiry/get', myCallback, myFallback);
        }
    }

    static Branch = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainMenuElement = mainElement.querySelector('ul.branch-item.menu');
            mainMenuElement.querySelector('li.menu-item.cs').addEventListener('click', () => {
                Router.forward('cs');
            });
            mainMenuElement.querySelector('li.menu-item.inquiry').addEventListener('click', () => {
                Router.forward('inquiry');
            });
            mainMenuElement.querySelector('li.menu-item.branch').addEventListener('click', () => {
                Router.forward('branch');
            });
            mainMenuElement.querySelector('li.menu-item.repair').addEventListener('click', () => {
                Router.forward('repair');
            });

            let gridElement = mainElement.querySelector('div.branch-item.grid');
            gridElement.innerHTML = '';
            const branchCallback = (response) => {
                let json = JSON.parse(response);
                if (json['result'] === 'success') {
                    let jsonBranches = json['branches'];
                    for (let i = 0; i < jsonBranches.length; i++) {
                        let gridItemElement = window.document.createElement('div');
                        let gridItemTitleElement = window.document.createElement('div');
                        let gridItemInfoElement = window.document.createElement('div');
                        let gridItemInfoAddressElement = window.document.createElement('div');
                        let gridItemInfoContactElement = window.document.createElement('div');
                        gridItemInfoAddressElement.innerHTML = `<a>주소</a> ${jsonBranches[i]['address']}`;
                        gridItemInfoAddressElement.classList.add('grid-item-info-item');
                        gridItemInfoContactElement.innerHTML = `<a>전화</a> ${jsonBranches[i]['contact']}`;
                        gridItemInfoContactElement.classList.add('grid-item-info-item');

                        gridItemTitleElement.innerText = jsonBranches[i]['name'];
                        gridItemTitleElement.classList.add('grid-item-title');
                        gridItemInfoElement.classList.add('grid-item-info');
                        gridItemInfoElement.append(gridItemInfoAddressElement);
                        gridItemInfoElement.append(gridItemInfoContactElement);

                        gridItemElement.classList.add('grid-item');
                        gridItemElement.append(gridItemTitleElement);
                        gridItemElement.append(gridItemInfoElement);
                        gridItemElement.addEventListener('click', () => {
                            Dialog.show(jsonBranches[i]['name'],
                                `<h1 style="font-size:1.5rem; margin-bottom:1rem;">${jsonBranches[i]['name']}</h1><div><strong>주소</strong> ${jsonBranches[i]['address']}</div><div><strong>전화</strong> ${jsonBranches[i]['contact']}</div><div><strong>영업시간</strong> ${jsonBranches[i]['operation']}</div><div class="map" style="width:40rem; height:30rem; margin: 2rem 0;"></div><div><strong>버스</strong> ${jsonBranches[i]['approach_bus']}</div><div><strong>지하철</strong> ${jsonBranches[i]['approach_subway']}</div>`,
                                ['확인'],
                                [() => {
                                    Dialog.hide();
                                }]
                            );
                            setTimeout(() => {
                                let mapElement = window.document.body.querySelector('#js-dialog div.map');
                                let mapOptions = {
                                    center: new kakao.maps.LatLng(jsonBranches[i]['coordinate_x'], jsonBranches[i]['coordinate_y']),
                                    level: 3
                                };
                                let map = new kakao.maps.Map(mapElement, mapOptions);
                            }, 500);
                            console.log(`${jsonBranches[i]['coordinate_x']}, ${jsonBranches[i]['coordinate_y']}`);
                        });
                        gridElement.append(gridItemElement);
                    }
                } else {
                    branchFallback();
                }
            };
            const branchFallback = () => {
                Dialog.show('지점', '지점을 불러오는 도중 예상치 못한 오류가 발생하였습니다.', ['확인'], [() => {
                    Dialog.hide();
                }]);
            }
            Ajax.request('POST', '/apis/cs/branch/get', branchCallback, branchFallback);
        }
    }

    static Repair = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainMenuElement = mainElement.querySelector('ul.repair-item.menu');
            mainMenuElement.querySelector('li.menu-item.cs').addEventListener('click', () => {
                Router.forward('cs');
            });
            mainMenuElement.querySelector('li.menu-item.inquiry').addEventListener('click', () => {
                Router.forward('inquiry');
            });
            mainMenuElement.querySelector('li.menu-item.branch').addEventListener('click', () => {
                Router.forward('branch');
            });
            mainMenuElement.querySelector('li.menu-item.repair').addEventListener('click', () => {
                Router.forward('repair');
            });

            let gridElement = mainElement.querySelector('div.repair-item.grid');
            gridElement.innerHTML = '';
            const repairCallback = (response) => {
                let json = JSON.parse(response);
                if (json['result'] === 'success') {
                    let jsonRepairs = json['repairs'];
                    for (let i = 0; i < jsonRepairs.length; i++) {
                        let gridItemElement = window.document.createElement('div');
                        let gridItemTitleElement = window.document.createElement('div');
                        let gridItemInfoElement = window.document.createElement('div');
                        let gridItemInfoAddressElement = window.document.createElement('div');
                        let gridItemInfoContactElement = window.document.createElement('div');
                        gridItemInfoAddressElement.innerHTML = `<a>주소</a> ${jsonRepairs[i]['address']}`;
                        gridItemInfoAddressElement.classList.add('grid-item-info-item');
                        gridItemInfoContactElement.innerHTML = `<a>전화</a> ${jsonRepairs[i]['contact']}`;
                        gridItemInfoContactElement.classList.add('grid-item-info-item');

                        gridItemTitleElement.innerText = jsonRepairs[i]['name'];
                        gridItemTitleElement.classList.add('grid-item-title');
                        gridItemInfoElement.classList.add('grid-item-info');
                        gridItemInfoElement.append(gridItemInfoAddressElement);
                        gridItemInfoElement.append(gridItemInfoContactElement);

                        gridItemElement.classList.add('grid-item');
                        gridItemElement.append(gridItemTitleElement);
                        gridItemElement.append(gridItemInfoElement);
                        gridItemElement.addEventListener('click', () => {
                            Dialog.show(jsonRepairs[i]['name'],
                                `<h1 style="font-size:1.5rem; margin-bottom:1rem;">${jsonRepairs[i]['name']}</h1><div><strong>주소</strong> ${jsonRepairs[i]['address']}</div><div><strong>전화</strong> ${jsonRepairs[i]['contact']}</div><div class="map" style="width:40rem; height:30rem; margin: 2rem 0;"></div>`,
                                ['확인'],
                                [() => {
                                    Dialog.hide();
                                }]
                            );
                            setTimeout(() => {
                                let mapElement = window.document.body.querySelector('#js-dialog div.map');
                                let mapOptions = {
                                    center: new kakao.maps.LatLng(jsonRepairs[i]['coordinate_x'], jsonRepairs[i]['coordinate_y']),
                                    level: 3
                                };
                                let map = new kakao.maps.Map(mapElement, mapOptions);
                            }, 500);
                            console.log(`${jsonRepairs[i]['coordinate_x']}, ${jsonRepairs[i]['coordinate_y']}`);
                        });
                        gridElement.append(gridItemElement);
                    }
                } else {
                    repairFallback();
                }
            };
            const repairFallback = () => {
                Dialog.show('지점', '지점을 불러오는 도중 예상치 못한 오류가 발생하였습니다.', ['확인'], [() => {
                    Dialog.hide();
                }]);
            }
            Ajax.request('POST', '/apis/cs/repair/get', repairCallback, repairFallback);
        }
    }

    static Event = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let gridElement = mainElement.querySelector('div.event-container-item.grid');
            gridElement.innerHTML = '';

            const eventCallback = (response) => {
                let json = JSON.parse(response);
                if (json['result'] === 'success') {
                    let jsonEvents = json['events'];
                    for (let i = 0; i < jsonEvents.length; i++) {
                        let imageElement = window.document.createElement('img');
                        let titleElement = window.document.createElement('div');
                        let durationElement = window.document.createElement('div');
                        imageElement.classList.add('grid-item-image');
                        imageElement.setAttribute('src', jsonEvents[i]['image']);
                        titleElement.classList.add('grid-item-title');
                        titleElement.innerText = jsonEvents[i]['title'];
                        durationElement.classList.add('grid-item-duration');
                        durationElement.innerText = `${jsonEvents[i]['start_date']}~${jsonEvents[i]['end_date']}`;

                        let eventElement = window.document.createElement('div');
                        eventElement.classList.add('grid-item');
                        eventElement.append(imageElement);
                        eventElement.append(titleElement);
                        eventElement.append(durationElement);

                        gridElement.append(eventElement);
                    }
                } else {
                    eventFallback();
                }
            };
            const eventFallback = () => {
                Dialog.show('이벤트', '이벤트 목록을 불러오는 도중 예상치 못한 오류가 발생하였습니다.', ['확인'], [() => {
                    Dialog.hide();
                }]);
            };
            Ajax.request('POST', '/apis/cs/event/get', eventCallback, eventFallback);
        }
    }

    static Login = class {
        static attachEvents = () => {
            const login = (email, password) => {
                Dialog.show('로그인', '로그인 중입니다. 잠시만 기다려주세요.');
                const callback = (response) => {
                    let json = JSON.parse(response);
                    let result = json['result'];
                    if (result === null) {
                        result = 'failure';
                    }
                    if (result === 'success') {
                        Dialog.hide();
                        let url = new URL(`${window.location.origin}${window.location.pathname}`);
                        url.searchParams.append('action', 'main');
                        window.location.href = url.toString();
                    } else {
                        Dialog.show('로그인', '이메일 혹은 비밀번호가 올바르지 않습니다.', ['확인'], [() => {
                            Dialog.hide();
                        }]);
                    }
                }
                const fallback = (status) => {
                    Dialog.show('로그인', '로그인 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                        Dialog.hide();
                    }]);
                }
                let formData = new FormData();
                formData.append('email', email);
                formData.append('password', password);
                Ajax.request('POST', '/apis/user/login', callback, fallback, formData);
            }

            let loginElement = window.document.body.querySelector('#js-login');
            let loginFormElement = loginElement.querySelector('form.login-item.form');
            loginFormElement.onsubmit = () => {
                if (loginFormElement.elements['email'].value === '') {
                    Dialog.show('경고', '이메일을 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        loginFormElement.elements['email'].focus();
                    }]);
                } else if (loginFormElement.elements['password'].value === '') {
                    Dialog.show('경고', '비밀번호를 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        loginFormElement.elements['password'].focus();
                    }]);
                } else {
                    login(
                        loginFormElement.elements['email'].value,
                        loginFormElement.elements['password'].value
                    );
                }
                return false;
            };
            loginFormElement.elements['email'].focus();

            let loginOptionElement = loginElement.querySelector('ul.login-item.option');
            loginOptionElement.querySelector('li.option-item.register').addEventListener('click', () => {
                Router.forward('register');
            });
            loginOptionElement.querySelector('li.option-item.find-email').addEventListener('click', () => {
                Router.forward('find_email');
            });
            loginOptionElement.querySelector('li.option-item.find-password').addEventListener('click', () => {
                Router.forward('find_password');
            });

            let url = new URL(window.location.href);
            let email = url.searchParams.get('email');
            if (email !== null) {
                loginFormElement.elements['email'].value = email;
                loginFormElement.elements['password'].focus();
            }
        }
    }

    static FindEmail = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainFormElement = mainElement.querySelector('form.find-email-item.form');
            let mainLoginButtonElement = mainElement.querySelector('input.buttons-item.login');
            mainLoginButtonElement.addEventListener('click', () => {
                Router.forward('login');
            });
            mainFormElement.onsubmit = () => {
                if (mainFormElement.elements['real-name'].value === '') {
                    Dialog.show('이메일 찾기', '실명을 입력해주세요.', ['확인'], [() => {
                        mainFormElement.elements['real-name'].focus();
                        Dialog.hide();
                    }]);
                } else if (mainFormElement.elements['contact-second'].value === '' || mainFormElement.elements['contact-third'].value === '') {
                    Dialog.show('이메일 찾기', '연락처를 입력해주세요.', ['확인'], [() => {
                        mainFormElement.elements['contact-second'].focus();
                        Dialog.hide();
                    }]);
                } else {
                    const callback = (response) => {
                        let json = JSON.parse(response);
                        if (json['result'] === 'success') {
                            Dialog.show('이메일 찾기', `입력한 정보와 일치하는 이메일을 찾았습니다.<br><br>이메일 : ${json['email']}<br><br>확인 버튼을 클릭하시면 로그인으로 돌아갑니다.`, ['확인'], [() => {
                                Dialog.hide();
                                let data = new Map();
                                data.set('email', json['email']);
                                Router.forward('login', null, null, data);
                            }]);
                        } else {
                            Dialog.show('이메일 찾기', '입력한 정보와 일치하는 이메일을 찾을 수 없습니다.', ['확인'], [() => {
                                Dialog.hide();
                            }]);
                        }
                    };
                    const fallback = () => {
                        Dialog.show('이메일 찾기', '알 수 없는 이유로 조회에 실패하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                            Dialog.hide();
                        }]);
                    }
                    Dialog.show('이메일 찾기', '입력한 정보와 일치하는 이메일을 찾고있습니다. 잠시만 기다려주세요.');
                    let formData = new FormData();
                    formData.append('name', mainFormElement.elements['real-name'].value);
                    formData.append('contact', `${mainFormElement.elements['contact-first'].value}-${mainFormElement.elements['contact-second'].value}-${mainFormElement.elements['contact-third'].value}`);
                    Ajax.request('POST', '/apis/user/find_email', callback, fallback, formData);
                }
                return false;
            };
            mainFormElement.elements['real-name'].focus();
        }
    }

    static FindPassword = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainFormElement = mainElement.querySelector('form.find-password-item.form');
            let mainLoginButtonElement = mainElement.querySelector('input.buttons-item.login');
            mainLoginButtonElement.addEventListener('click', () => {
                Router.forward('login');
            });
            mainFormElement.onsubmit = () => {
                if (mainFormElement.elements['real-name'].value === '') {
                    Dialog.show('비밀번호 찾기', '실명을 입력해주세요.', ['확인'], [() => {
                        mainFormElement.elements['real-name'].focus();
                        Dialog.hide();
                    }]);
                } else if (mainFormElement.elements['contact-second'].value === '' || mainFormElement.elements['contact-third'].value === '') {
                    Dialog.show('비밀번호 찾기', '연락처를 입력해주세요.', ['확인'], [() => {
                        mainFormElement.elements['contact-second'].focus();
                        Dialog.hide();
                    }]);
                } else {
                    const callback = (response) => {
                        let json = JSON.parse(response);
                        if (json['result'] === 'email_sent') {
                            Dialog.show('비밀번호 찾기', '비밀번호를 재설정할 수 있는 링크를 이메일로 전송하였습니다.<br><br>확인을 클릭하면 로그인 페이지로 이동합니다.', ['확인'], [() => {
                                Dialog.hide();
                                let data = new Map();
                                data.set('email', mainFormElement.elements['email'].value);
                                Router.forward('login', null, null, data);
                            }]);
                        } else {
                            Dialog.show('비밀번호 찾기', '입력한 정보와 일치하는 회원 정보를 찾을 수 없습니다.', ['확인'], [() => {
                                Dialog.hide();
                            }]);
                        }
                    };
                    const fallback = () => {
                        Dialog.show('비밀번호 찾기', '알 수 없는 이유로 조회에 실패하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                            Dialog.hide();
                        }]);
                    }
                    Dialog.show('비밀번호 찾기', '입력한 정보와 일치하는 회원 정보를 찾고있습니다. 잠시만 기다려주세요.');
                    let formData = new FormData();
                    formData.append('email', mainFormElement.elements['email'].value)
                    formData.append('name', mainFormElement.elements['real-name'].value);
                    formData.append('contact', `${mainFormElement.elements['contact-first'].value}-${mainFormElement.elements['contact-second'].value}-${mainFormElement.elements['contact-third'].value}`);
                    Ajax.request('POST', '/apis/user/find_password', callback, fallback, formData);
                }
                return false;
            };
            mainFormElement.elements['email'].focus();
        }
    }

    static ResetPassword = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainFormElement = mainElement.querySelector('form.reset-password-item.form');
            let mainFormLoginButtonElement = mainFormElement.querySelector('input.buttons-item.login');
            mainFormElement.onsubmit = () => {
                if (mainFormElement.elements['password'].value === '') {
                    Dialog.show('비밀번호 재설정', '새로 사용할 비밀번호를 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        mainFormElement.elements['password'].focus();
                    }]);
                } else if (mainFormElement.elements['password-check'].value === '') {
                    Dialog.show('비밀번호 재설정', '새로 사용할 비밀번호를 다시 한번 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        mainFormElement.elements['password-check'].focus();
                    }]);
                } else if (mainFormElement.elements['password'].value !== mainFormElement.elements['password-check'].value) {
                    Dialog.show('비밀번호 재설정', '다시 입력한 비밀번호가 일치하지 않습니다.', ['확인'], [() => {
                        Dialog.hide();
                        mainFormElement.elements['password-check'].focus();
                        mainFormElement.elements['password-check'].select();
                    }]);
                } else {
                    const callback = (response) => {
                        let json = JSON.parse(response);
                        if (json['result'] === 'success') {
                            Dialog.show('비밀번호 변경', '비밀번호가 성공적으로 변경되었습니다.<br>확인 버튼을 클릭하시면 로그인으로 돌아갑니다.', ['확인'], [() => {
                                Dialog.hide();
                                Router.forward('login');
                            }]);
                        } else {
                            Dialog.show('비밀번호 변경', '비밀번호 변경에 실패하였습니다. 링크가 만료되었을 수도 있습니다.', ['확인'], [() => {
                                Dialog.hide();
                            }]);
                        }
                    }
                    const fallback = () => {
                        Dialog.show('비밀번호 변경', '비밀번호를 변경하는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                            Dialog.hide();
                        }]);
                    }
                    Dialog.show('비밀번호 재설정', '비밀번호를 재설정하고 있습니다. 잠시만 기다려주세요.');
                    let formData = new FormData();
                    let url = new URL(window.location.href);
                    let key = url.searchParams.get('key');
                    if (key === null) {
                        key = '';
                    }
                    formData.append('password', mainFormElement.elements['password'].value);
                    formData.append('key', key);
                    Ajax.request('POST', '/apis/user/reset_password', callback, fallback, formData);
                }
                return false;
            }
            mainFormElement.elements['password'].focus();
            mainFormLoginButtonElement.addEventListener('click', () => {
                Router.forward('login');
            });
        }
    }

    static Register = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let registerElement = mainElement.querySelector('div.main-item.register');
            let registerCancelButtonElement = registerElement.querySelector('input.buttons-item.cancel');
            let registerNextButtonElement = registerElement.querySelector('input.buttons-item.next');
            registerCancelButtonElement.addEventListener('click', () => {
                Router.forward('main');
            });
            registerNextButtonElement.addEventListener('click', () => {
                let registerCheckElements = registerElement.querySelectorAll('input.term-container-item-title-check-label-input');
                if (!registerCheckElements[0].checked) {
                    Dialog.show('회원가입', '이용약관을 읽어보시고 동의해주세요.', ['확인'], [() => {
                        Dialog.hide();
                    }]);
                } else if (!registerCheckElements[1].checked) {
                    Dialog.show('회원가입', '개인정보 수집, 이용안내를 읽어보시고 동의해주세요.', ['확인'], [() => {
                        Dialog.hide();
                    }]);
                } else {
                    Router.forward('register_input');
                }
            });
        }
    }

    static RegisterInput = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let registerInputElement = mainElement.querySelector('div.main-item.register-input');
            let registerInputCancelButtonElement = registerInputElement.querySelector('input.buttons-item.cancel');
            let registerInputFormElement = registerInputElement.querySelector('form.register-input-item.input-container');
            registerInputCancelButtonElement.addEventListener('click', () => {
                Router.forward('main');
            });
            registerInputFormElement.onsubmit = () => {
                if (registerInputFormElement.elements['email'].value === '') {
                    Dialog.show('회원가입', '이메일을 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['email'].focus();
                    }]);
                } else if (registerInputFormElement.elements['password'].value === '') {
                    Dialog.show('회원가입', '비밀번호를 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['password'].focus();
                    }]);
                } else if (registerInputFormElement.elements['password-check'].value === '') {
                    Dialog.show('회원가입', '비밀번호를 다시 한번 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['password-check'].focus();
                    }]);
                } else if (registerInputFormElement.elements['password'].value !== registerInputFormElement.elements['password-check'].value) {
                    Dialog.show('회원가입', '다시 입력한 비밀번호가 일치하지 않습니다.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['password-check'].focus();
                    }]);
                } else if (registerInputFormElement.elements['real-name'].value === '') {
                    Dialog.show('회원가입', '이름을 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['real-name'].focus();
                    }]);
                } else if (registerInputFormElement.elements['contact-second'].value === '' || registerInputFormElement.elements['contact-third'].value === '') {
                    Dialog.show('회원가입', '연락처를 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['contact-second'].focus();
                    }]);
                } else if (registerInputFormElement.elements['address'].value === '') {
                    Dialog.show('회원가입', '주소를 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['address'].focus();
                    }]);
                } else if (registerInputFormElement.elements['license_number'].value === '') {
                    Dialog.show('회원가입', '면허번호를 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['license_number'].focus();
                    }]);
                } else if (registerInputFormElement.elements['license_issue_date'].value === '') {
                    Dialog.show('회원가입', '면허발급일자를 입력해주세요.', ['확인'], [() => {
                        Dialog.hide();
                        registerInputFormElement.elements['license_issue_date'].focus();
                    }]);
                } else {
                    const callback = (response) => {
                        let json = JSON.parse(response);
                        if (json['result'] === 'email_duplicate') {
                            Dialog.show('회원가입', '입력하신 이메일은 이미 등록되어있습니다.', ['확인'], [() => {
                                registerInputFormElement.elements['email'].focus();
                                registerInputFormElement.elements['email'].select();
                                Dialog.hide();
                            }]);
                        } else if (json['result'] === 'contact_duplicate') {
                            Dialog.show('회원가입', '입력하신 연락처는 이미 등록되어있습니다.', ['확인'], [() => {
                                registerInputFormElement.elements['contact-second'].focus();
                                registerInputFormElement.elements['contact-second'].select();
                                Dialog.hide();
                            }]);
                        } else if (json['result'] === 'success') {
                            setTimeout(() => {
                                Dialog.hide();
                                Router.forward('register_complete');
                            }, 250);
                        } else {
                            fallback();
                        }
                    }
                    const fallback = () => {
                        Dialog.show('회원가입', '알 수 없는 이유로 회원가입에 실패하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                            Dialog.hide();
                        }]);
                    }
                    Dialog.show('회원가입', '회원가입 중입니다. 잠시만 기다려주세요.');
                    let formData = new FormData(registerInputFormElement);
                    formData.append('name', registerInputFormElement.elements['real-name'].value);
                    formData.append('contact', `${registerInputFormElement.elements['contact-first'].value}-${registerInputFormElement.elements['contact-second'].value}-${registerInputFormElement.elements['contact-third'].value}`);
                    Ajax.request('POST', '/apis/user/register', callback, fallback, formData);
                }
                return false;
            };
            registerInputFormElement.elements['email'].focus();
        }
    }

    static RegisterComplete = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainLoginButtonElement = mainElement.querySelector('input.buttons-item.login');
            mainLoginButtonElement.addEventListener('click', () => {
                Router.forward('login');
            });
        }
    }

    static Main = class {
        static attachEvents = () => {

        }
    }

    static JejuLandCommon = class {
        static attachEvents = (mainBookElement) => {
            let mainElement = Router.getMainElement();
            let mainBookFormElement = mainBookElement.querySelector('form.content-item.datetime');
            const calcDateTime = () => {
                const getString = (fromDateTime, toDateTime) => {
                    if (fromDateTime > toDateTime) {
                        return '0일 0시간 0분';
                    }
                    let delta = Math.abs(toDateTime - fromDateTime) / 1000;
                    let days = Math.floor(delta / 86400);
                    delta -= days * 86400;
                    let hours = Math.floor(delta / 3600) % 24;
                    delta -= hours * 3600;
                    let minutes = Math.floor(delta / 60) % 60;
                    delta -= minutes * 60;
                    return `${days}일 ${hours}시간 ${minutes}분`;
                }
                let fromDateTime = new Date(`${mainBookFormElement.elements['from_date'].value} ${mainBookFormElement.elements['from_time_hour'].value}:${mainBookFormElement.elements['from_time_minute'].value}`);
                let toDateTime = new Date(`${mainBookFormElement.elements['to_date'].value} ${mainBookFormElement.elements['to_time_hour'].value}:${mainBookFormElement.elements['to_time_minute'].value}`);
                let totalElement = mainBookElement.querySelector('div.total-item.datetime');
                let dateTimeDiff = getString(fromDateTime, toDateTime);
                if (dateTimeDiff.includes('NaN')) {
                    totalElement.innerText = '0일 0시간 0분';
                } else {
                    totalElement.innerText = getString(fromDateTime, toDateTime);
                }
            }
            mainBookFormElement.elements['from_date'].addEventListener('change', () => {
                calcDateTime();
            });
            mainBookFormElement.elements['from_time_hour'].addEventListener('change', () => {
                calcDateTime();
            });
            mainBookFormElement.elements['from_time_minute'].addEventListener('change', () => {
                calcDateTime();
            });
            mainBookFormElement.elements['to_date'].addEventListener('change', () => {
                calcDateTime();
            });
            mainBookFormElement.elements['to_time_hour'].addEventListener('change', () => {
                calcDateTime();
            });
            mainBookFormElement.elements['to_time_minute'].addEventListener('change', () => {
                calcDateTime();
            });

            let mainEventElement = mainElement.querySelector('div.main-item.event');
            let mainEventGridElement = mainEventElement.querySelector('div.content-item.grid');
            let mainEventGridElements = mainEventGridElement.querySelectorAll('div.grid-item');
            const eventCallback = (response) => {
                let json = JSON.parse(response);
                if (json['result'] === 'success') {
                    let jsonEvents = json['events'];
                    for (let i = 0; i < jsonEvents.length; i++) {
                        if (i > 2) {
                            break;
                        }
                        let imageElement = mainEventGridElements[i].querySelector('img.grid-item-image');
                        let titleElement = mainEventGridElements[i].querySelector('div.grid-item-title');
                        let durationElement = mainEventGridElements[i].querySelector('div.grid-item-duration');
                        imageElement.setAttribute('src', jsonEvents[i]['image']);
                        titleElement.innerText = jsonEvents[i]['title'];
                        durationElement.innerText = `${jsonEvents[i]['start_date']}~${jsonEvents[i]['end_date']}`;
                        mainEventGridElements[i].addEventListener('click', () => {
                            Router.forward('event');
                        });
                    }
                }
            };
            const eventFallback = () => {
            }
            Ajax.request('POST', '/apis/cs/event/get', eventCallback, eventFallback);

            let mainNoticeElement = mainElement.querySelector('div.main-item.notice');
            let mainNoticeListElement = mainNoticeElement.querySelector('ul.notice-item.list');
            let mainNoticeListItemElements = mainNoticeListElement.querySelectorAll('li.list-item');
            const noticeCallback = (response) => {
                let json = JSON.parse(response);
                if (json['result'] === 'success') {
                    let jsonNotices = json['notices'];
                    for (let i = 0; i < jsonNotices.length; i++) {
                        if (i > 5) {
                            break;
                        }
                        let titleElement = mainNoticeListItemElements[i].querySelector('span.list-item-title');
                        let dateElement = mainNoticeListItemElements[i].querySelector('span.list-item-date');
                        titleElement.innerText = jsonNotices[i]['title'];
                        dateElement.innerText = jsonNotices[i]['written_at'].substr(0, 10);
                        mainNoticeListItemElements[i].addEventListener('click', () => {
                            Router.forward('cs');
                        });
                    }
                }
            }
            const noticeFallback = () => {
            }
            Ajax.request('POST', '/apis/cs/notice/get', noticeCallback, noticeFallback);
        }
    }

    static Jeju = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainBookElement = mainElement.querySelector('div.jeju-item.book');
            Event.JejuLandCommon.attachEvents(mainBookElement);

            let mainBookRentButtonElement = mainBookElement.querySelector('input.rent');
            mainBookRentButtonElement.addEventListener('click', () => {
                let totalElement = mainBookElement.querySelector('div.total-item.datetime');
                if (totalElement.innerText === '0일 0시간 0분') {
                    Dialog.show('빠른예약', '올바른 대여일시와 반납일시를 지정해주세요.', ['확인'], [() => {
                        Dialog.hide();
                    }]);
                } else {
                    let formElement = mainElement.querySelector('form.content-item.datetime');
                    let submitElement = mainElement.querySelector('input.total-item.rent[type=submit]');
                    submitElement.addEventListener('click', () => {
                        const rentCallback = (response) => {
                            let content = `
                                <h1 class="content-item title book">제주 빠른예약</h1>
                                <table class="content-item table book">
                                    <thead>
                                        <tr>
                                            <th>선택</th><th>차량 구분</th><th>차량 이름</th><th>이용 가능 여부</th>                                           
                                        </tr>                                  
                                    </thead>                   
                                    <tbody></tbody>                 
                                </table>
                            `;
                            Dialog.show('제주 빠른예약', content, ['취소', '빠른예약'], [() => {
                                Dialog.hide();
                            }, () => {
                                let header = window.document.body.querySelector('#js-header');
                                let selectedRow = null;
                                let rows = window.document.body.querySelectorAll('table.content-item.table.book > tbody > tr');
                                for (let i = 0; i < rows.length; i++) {
                                    if (rows[i].querySelector('td:nth-child(1) > input:nth-child(1)').checked) {
                                        selectedRow = rows[i];
                                        break;
                                    }
                                }
                                if (selectedRow === null) {
                                    return;
                                }
                                if (header.querySelector('li.side-menu-item.login') !== null) {
                                    alert('차량은 로그인 후 예약할 수 있습니다.');
                                    return;
                                }
                                if (selectedRow.querySelector('td:nth-child(4)').innerText === '불가능') {
                                    alert('해당 차량은 예약이 불가능합니다.');
                                    return;
                                }
                                let data = new Map();
                                data.set('from_date', formElement.elements['from_date'].value);
                                data.set('from_time', `${formElement.elements['from_time_hour'].value}:${formElement.elements['from_time_minute'].value}`);
                                data.set('to_date', formElement.elements['to_date'].value);
                                data.set('to_time', `${formElement.elements['to_time_hour'].value}:${formElement.elements['to_time_minute'].value}`);
                                data.set('branch', 'jeju');
                                data.set('car_index', selectedRow.querySelector('input.car-index').value);
                                data.set('branch_index', selectedRow.querySelector('input.branch-index').value);
                                Router.forward('rent', null, null, data);
                                Dialog.hide();
                            }]);

                            let json = JSON.parse(response);
                            let tbody = window.document.body.querySelector('table.content-item.table.book > tbody');
                            for (let i = 0; i < json['cars'].length; i++) {
                                let tr = window.document.createElement('tr');
                                let tdCheck = window.document.createElement('td');
                                let inputCheck = window.document.createElement('input');
                                let inputBranchIndex = window.document.createElement('input');
                                let inputCarIndex = window.document.createElement('input');
                                let tdType = window.document.createElement('td');
                                let tdName = window.document.createElement('td');
                                let tdAvail = window.document.createElement('td');
                                inputCheck.setAttribute('type', 'radio');
                                inputCheck.setAttribute('name', 'car_select');
                                if (i === 0) {
                                    inputCheck.setAttribute('checked', '');
                                }
                                inputBranchIndex.setAttribute('type', 'hidden');
                                inputBranchIndex.classList.add('branch-index');
                                inputBranchIndex.value = json['cars'][i]['branchIndex'];
                                inputCarIndex.setAttribute('type', 'hidden');
                                inputCarIndex.classList.add('car-index');
                                inputCarIndex.value = json['cars'][i]['carIndex'];
                                tdCheck.append(inputCheck);
                                tdCheck.append(inputBranchIndex);
                                tdCheck.append(inputCarIndex);
                                tdType.innerText = json['cars'][i]['carType'];
                                tdName.innerText = json['cars'][i]['carName'];
                                tdAvail.innerText = json['cars'][i]['available'] === 'available' ? '가능' : '불가능';
                                tr.append(tdCheck);
                                tr.append(tdType);
                                tr.append(tdName);
                                tr.append(tdAvail);
                                tbody.append(tr);
                            }
                        };
                        const rentFallback = () => {
                            Dialog.show('예약', '차량 조회 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.');
                        };
                        let formData = new FormData();
                        formData.append('from_date', formElement.elements['from_date'].value);
                        formData.append('from_time', `${formElement.elements['from_time_hour'].value}:${formElement.elements['from_time_minute'].value}`);
                        formData.append('to_date', formElement.elements['to_date'].value);
                        formData.append('to_time', `${formElement.elements['to_time_hour'].value}:${formElement.elements['to_time_minute'].value}`);
                        formData.append('branch', 'jeju');
                        Ajax.request('POST', '/apis/rental/get', rentCallback, rentFallback, formData);
                    });
                }
            });
        }
    }

    static Land = class {
        static attachEvents = () => {
            let mainElement = Router.getMainElement();
            let mainBookElement = mainElement.querySelector('div.land-item.book');
            Event.JejuLandCommon.attachEvents(mainBookElement);

            let mainBookRentButtonElement = mainBookElement.querySelector('input.rent');
            mainBookRentButtonElement.addEventListener('click', () => {
                let totalElement = mainBookElement.querySelector('div.total-item.datetime');
                if (totalElement.innerText === '0일 0시간 0분') {
                    Dialog.show('빠른예약', '올바른 대여일시와 반납일시를 지정해주세요.', ['확인'], [() => {
                        Dialog.hide();
                    }]);
                } else {
                    let formElement = mainElement.querySelector('form.content-item.datetime');
                    let submitElement = mainElement.querySelector('input.total-item.rent[type=submit]');
                    submitElement.addEventListener('click', () => {
                        const rentCallback = (response) => {
                            let content = `
                                <h1 class="content-item title book">내륙 빠른예약</h1>
                                <table class="content-item table book">
                                    <thead>
                                        <tr>
                                            <th>선택</th><th>차량 구분</th><th>차량 이름</th><th>이용 가능 여부</th>                                           
                                        </tr>                                  
                                    </thead>                   
                                    <tbody></tbody>                 
                                </table>
                            `;
                            Dialog.show('내륙 빠른예약', content, ['취소', '빠른예약'], [() => {
                                Dialog.hide();
                            }, () => {
                                let header = window.document.body.querySelector('#js-header');
                                let selectedRow = null;
                                let rows = window.document.body.querySelectorAll('table.content-item.table.book > tbody > tr');
                                for (let i = 0; i < rows.length; i++) {
                                    if (rows[i].querySelector('td:nth-child(1) > input:nth-child(1)').checked) {
                                        selectedRow = rows[i];
                                        break;
                                    }
                                }
                                if (selectedRow === null) {
                                    return;
                                }
                                if (header.querySelector('li.side-menu-item.login') !== null) {
                                    alert('차량은 로그인 후 예약할 수 있습니다.');
                                    return;
                                }
                                if (selectedRow.querySelector('td:nth-child(4)').innerText === '불가능') {
                                    alert('해당 차량은 예약이 불가능합니다.');
                                    return;
                                }
                                let data = new Map();
                                data.set('from_date', formElement.elements['from_date'].value);
                                data.set('from_time', `${formElement.elements['from_time_hour'].value}:${formElement.elements['from_time_minute'].value}`);
                                data.set('to_date', formElement.elements['to_date'].value);
                                data.set('to_time', `${formElement.elements['to_time_hour'].value}:${formElement.elements['to_time_minute'].value}`);
                                data.set('branch', 'land');
                                data.set('car_index', selectedRow.querySelector('input.car-index').value);
                                data.set('branch_index', selectedRow.querySelector('input.branch-index').value);
                                Router.forward('rent', null, null, data);
                                Dialog.hide();
                            }]);

                            let json = JSON.parse(response);
                            let tbody = window.document.body.querySelector('table.content-item.table.book > tbody');
                            for (let i = 0; i < json['cars'].length; i++) {
                                let tr = window.document.createElement('tr');
                                let tdCheck = window.document.createElement('td');
                                let inputCheck = window.document.createElement('input');
                                let inputBranchIndex = window.document.createElement('input');
                                let inputCarIndex = window.document.createElement('input');
                                let tdType = window.document.createElement('td');
                                let tdName = window.document.createElement('td');
                                let tdAvail = window.document.createElement('td');
                                inputCheck.setAttribute('type', 'radio');
                                inputCheck.setAttribute('name', 'car_select');
                                if (i === 0) {
                                    inputCheck.setAttribute('checked', '');
                                }
                                inputBranchIndex.setAttribute('type', 'hidden');
                                inputBranchIndex.classList.add('branch-index');
                                inputBranchIndex.value = json['cars'][i]['branchIndex'];
                                inputCarIndex.setAttribute('type', 'hidden');
                                inputCarIndex.classList.add('car-index');
                                inputCarIndex.value = json['cars'][i]['carIndex'];
                                tdCheck.append(inputCheck);
                                tdCheck.append(inputBranchIndex);
                                tdCheck.append(inputCarIndex);
                                tdType.innerText = json['cars'][i]['carType'];
                                tdName.innerText = json['cars'][i]['carName'];
                                tdAvail.innerText = json['cars'][i]['available'] === 'available' ? '가능' : '불가능';
                                tr.append(tdCheck);
                                tr.append(tdType);
                                tr.append(tdName);
                                tr.append(tdAvail);
                                tbody.append(tr);
                            }
                        };
                        const rentFallback = () => {
                            Dialog.show('예약', '차량 조회 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.');
                        };
                        let formData = new FormData();
                        formData.append('from_date', formElement.elements['from_date'].value);
                        formData.append('from_time', `${formElement.elements['from_time_hour'].value}:${formElement.elements['from_time_minute'].value}`);
                        formData.append('to_date', formElement.elements['to_date'].value);
                        formData.append('to_time', `${formElement.elements['to_time_hour'].value}:${formElement.elements['to_time_minute'].value}`);
                        formData.append('branch', 'land');
                        Ajax.request('POST', '/apis/rental/get', rentCallback, rentFallback, formData);
                    });
                }
            });
        }
    }

    static Rent = class {
        static attachEvents = () => {
            let url = new URL(window.location.href);
            let fromDate = url.searchParams.get('from_date');
            let fromTime = url.searchParams.get('from_time');
            let toDate = url.searchParams.get('to_date');
            let toTime = url.searchParams.get('to_time');
            let dateTimeDiff = (new Date(`${toDate} ${toTime}`) - new Date(`${fromDate} ${fromTime}`)) / 86400000;

            let summaryElement = window.document.body.querySelector('#js-summary');
            let fromElement = summaryElement.querySelector('li.list-item.from');
            let toElement = summaryElement.querySelector('li.list-item.to');
            fromElement.querySelectorAll('span')[1].innerText = `${fromDate} ${fromTime}`;
            toElement.querySelectorAll('span')[1].innerText = `${toDate} ${toTime}`;

            let branchIndex = url.searchParams.get('branch_index');
            let branchElement = summaryElement.querySelector('li.list-item.branch');
            const branchCallback = (response) => {
                let json = JSON.parse(response);
                branchElement.querySelectorAll('span')[1].innerText = json['name'];
            };
            const branchFallback = () => {
                Dialog.show('빠른예약', '일부 정보를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                    Dialog.hide();
                    Router.forward('main');
                }]);
            };
            let branchFormData = new FormData();
            branchFormData.append('index', branchIndex);
            Ajax.request('POST', '/apis/rental/get-branch-info', branchCallback, branchFallback, branchFormData);

            let carIndex = url.searchParams.get('car_index');
            let carElement = summaryElement.querySelector('li.list-item.car');
            let priceElement = summaryElement.querySelector('li.list-item.price');
            let insElement = summaryElement.querySelector('li.list-item.ins');
            const carCallback = (response) => {
                let json = JSON.parse(response);
                let price = (Math.round(json['price'] * dateTimeDiff / 100) * 100);
                let ins = (Math.round(json['ins'] * dateTimeDiff / 100) * 100);
                carElement.querySelectorAll('span')[1].innerText = json['name'];
                priceElement.querySelectorAll('span')[1].innerText = price.toLocaleString();
                insElement.querySelectorAll('span')[1].innerText = ins.toLocaleString();

                let totalElement = window.document.body.querySelector('#js-total');
                totalElement.querySelector('div.total-item.price').innerText = (price + ins).toLocaleString();

                let purchaseElement = window.document.body.querySelector('input.purchase[type=button]');
                purchaseElement.addEventListener('click', () => {
                    const purchaseCallback = (response) => {
                        let json = JSON.parse(response);
                        if (json['result'] === 'success') {
                            Dialog.show('빠른렌탈', '결제가 완료되었습니다. "MY렌터카" 메뉴에서 결제 내역을 확인해주세요.', ['확인'], [() => {
                                Dialog.hide();
                                Router.forward('my');
                            }]);
                        } else {
                            purchaseFallback();
                        }
                    };
                    const purchaseFallback = () => {
                        Dialog.show('빠른렌탈', '결제에 실패하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                            Dialog.hide();
                        }]);
                    };
                    Dialog.show('빠른렌탈', '결제 중입니다. 잠시만 기다려주세요.');
                    let branchIndex = url.searchParams.get('branch_index');
                    let carIndex = url.searchParams.get('car_index');
                    let formData = new FormData();
                    formData.append('from_date', fromDate);
                    formData.append('from_time', fromTime);
                    formData.append('to_date', toDate);
                    formData.append('to_time', toTime);
                    formData.append('branch', branchIndex)
                    formData.append('car', carIndex);
                    Ajax.request('POST', '/apis/rental/add', purchaseCallback, purchaseFallback, formData);
                });
            };
            const carFallback = () => {
                Dialog.show('빠른예약', '일부 정보를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                    Dialog.hide();
                    Router.forward('main');
                }]);
            };
            let carFormData = new FormData();
            carFormData.append('index', carIndex);
            Ajax.request('POST', '/apis/rental/get-car-info', carCallback, carFallback, carFormData);

            let nameElement = summaryElement.querySelector('li.list-item.name');
            let contactElement = summaryElement.querySelector('li.list-item.contact');
            let emailElement = summaryElement.querySelector('li.list-item.email');
            const userCallback = (response) => {
                let json = JSON.parse(response);
                nameElement.querySelectorAll('span')[1].innerText = json['name'];
                contactElement.querySelectorAll('span')[1].innerText = json['contact'];
                emailElement.querySelectorAll('span')[1].innerText = json['email'];
            };
            const userFallback = () => {
                Dialog.show('빠른예약', '일부 정보를 불러오는 도중 예상치 못한 오류가 발생하였습니다.<br>잠시 후 다시 시도해주세요.', ['확인'], [() => {
                    Dialog.hide();
                    Router.forward('main');
                }]);
            };
            Ajax.request('POST', '/apis/user/get-user-info', userCallback, userFallback);
        }
    }
}