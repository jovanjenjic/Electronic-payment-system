import React from 'react';

import { useHistory } from 'react-router-dom';

/**
 * Used for wrapping auth stuff
 * @param {any} Component
 */
// eslint-disable-next-line react/display-name
export const withAuth = (WrappedComponent) => (props) => {
  const authToken = localStorage.getItem('access_token');

  const history = useHistory();

  React.useEffect(() => {
    if (!authToken) history.push('/login');
  }, [authToken]);

  return <WrappedComponent {...props} />;
};
