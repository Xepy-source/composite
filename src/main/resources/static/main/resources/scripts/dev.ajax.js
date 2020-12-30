class Ajax {
    static request = (method, url, callback, fallback, formData) => {
        let xhr = new XMLHttpRequest();
        xhr.open(method, url);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    if (typeof(callback) === 'function') {
                        callback(xhr.responseText);
                    }
                } else {
                    if (typeof(fallback) === 'function') {
                        fallback(xhr.status);
                    }
                }
            }
        };
        if (typeof(formData) !== 'undefined') {
            xhr.send(formData);
        } else {
            xhr.send();
        }
    }
}