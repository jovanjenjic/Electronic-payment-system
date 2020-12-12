import React from 'react';
import { useAsync } from 'react-async';

import { useParams, useLocation, useHistory } from 'react-router-dom';
import { Result, Button } from 'antd';

import { EXECUTE_PAYPAL_SUBSCRIPTION } from '../constants/url';
import { responseOk } from '../utils/responseOk';
import { post } from '../services/api';

const executeSubscription = async ([data], { subscriptionId }) => {
  const authToken = localStorage.getItem('access_token');

  const url = EXECUTE_PAYPAL_SUBSCRIPTION.replace(
    '{subscriptionId}',
    subscriptionId
  );

  try {
    const response = await post(url, data, authToken);

    if (responseOk(response)) return await response.json();
  } catch (error) {
    console.error(error);
  }
  return { error: true };
};

/** `helper` method for resolving search params of the url */
const resolveSearchParams = (search = '') => {
  const searchParams = new URLSearchParams(search);

  const subscriptionToken = searchParams.get('token');

  return {
    subscriptionToken,
  };
};

const SubscriptionSuccess = () => {
  const location = useLocation();

  const history = useHistory();

  const { subscriptionId = '' } = useParams();

  /** `useAsync` hook for executing subscription */
  const { run: runSubscription, isLoading } = useAsync({
    deferFn: executeSubscription,
    subscriptionId,
  });

  React.useEffect(() => {
    runSubscription(resolveSearchParams(location.search));
  }, []);

  return (
    <Result
      title="Successful Subscription"
      status="success"
      extra={
        <Button
          type="primary"
          onClick={() => history.push('/')}
          loading={isLoading}
        >
          Home
        </Button>
      }
    />
  );
};

export default SubscriptionSuccess;
