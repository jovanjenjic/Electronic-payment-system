// Used for post, put, patch, get data
const get = (url, options = {}) => fetch(url, { method: 'GET', ...options });

const post = (url, data = {}, options = {}) => {
  return fetch(url, {
    method: 'POST',
    headers: {
      Accept: '*/*',
      'Content-Type': 'application/json;charset=utf-8',
    },
    body: JSON.stringify(data),
    ...options,
  });
};

const patch = (url, data = {}, options = {}) => {
  return fetch(url, {
    method: 'PATCH',
    headers: {
      Accept: '*/*',
      'Content-Type': 'application/json;charset=utf-8',
    },
    body: JSON.stringify(data),
    ...options,
  });
};

const put = (url, data = {}, options = {}) => {
  return fetch(url, {
    method: 'PUT',
    headers: {
      Accept: '*/*',
      'Content-Type': 'application/json;charset=utf-8',
    },
    body: JSON.stringify(data),
    ...options,
  });
}; 


export { get, post, patch, put };
