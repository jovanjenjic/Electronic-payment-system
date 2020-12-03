import React from 'react';

import { useHistory } from 'react-router-dom';

/**
 * Used for wrapping no auth stuff
 * @param {any} Component
 */
// eslint-disable-next-line react/display-name
export const withNoAuth = (WrappedComponent) => (props) => {
  const authToken = localStorage.getItem('access_token');

  const history = useHistory();

  React.useEffect(() => {
    if (authToken) history.push('/');
  }, [authToken]);

  return <WrappedComponent {...props} />;
};