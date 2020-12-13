import React from 'react';
import { useAsync } from 'react-async';

import { useParams, useHistory } from 'react-router-dom';
import { Result, Button } from 'antd';

import { CANCEL_PAYPAL_SUBSCRIPTION } from '../constants/url';
import { responseOk } from '../utils/responseOk';
import { post } from '../services/api';

const cancelSubscription = async ([data], { subscriptionId }) => {
  const authToken = localStorage.getItem('access_token');

  const url = CANCEL_PAYPAL_SUBSCRIPTION.replace(
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

const SubscriptionCancel = () => {
  const history = useHistory();

  const { subscriptionId = '' } = useParams();

  /** `useAsync` hook for executing subscription */
  const { run: runSubscription, isLoading } = useAsync({
    deferFn: cancelSubscription,
    subscriptionId,
  });

  React.useEffect(() => {
    runSubscription();
  }, []);

  return (
    <Result
      title="Canceled Subscription"
      status="info"
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

export default SubscriptionCancel;
