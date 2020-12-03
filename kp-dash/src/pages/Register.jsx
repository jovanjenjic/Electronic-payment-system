import React from 'react';
import { Form, notification } from 'antd';
import { useHistory } from 'react-router-dom';

import { Container } from '../components/styledForm';

import Register from '../forms/register';

import { withNoAuth } from '../hoc/withNoAuth';

// services
import { post } from '../services/api';
import { REGISTER_URL } from '../constants/url';
import { responseOk } from '../utils/responseOk';

const RegisterContainer = () => {

  const [api, context] = notification.useNotification();

  const [form] = Form.useForm();

  const history = useHistory();

  const onFinish = async values => {
    const response = await post(REGISTER_URL, values);

    if (responseOk(response)) {
      const result = await response.json();
      api.success({
        placement: 'topRight',
        message: 'Register success'
      });
      localStorage.setItem('access_token', result.message);
      setTimeout(() => {
        history.push('/');
      }, 1000)
      return;
    }

    api.error({
      placement: 'topRight',
      message: 'Invalid info'
    })
  };

  return <Container> {context} <Register form={form} onFinish={onFinish} /></Container>;
};

export default withNoAuth(RegisterContainer);