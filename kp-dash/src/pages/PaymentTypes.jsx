import React from 'react';
import styled from 'styled-components';
import { Select, Result, Form, Button, Input, notification } from 'antd';

import BankPaymentForm from '../forms/bank-payment-form';

import { ShopContext } from '../context/shop';

import useFetchPaymentForms from '../hooks/useFetchPaymentForms';

import {
  BTC_ADD_PAYMENT_URL,
  PAYPAL_ADD_PAYMENT_URL,
  BANK_ADD_PAYMENT_URL,
} from '../constants/url';
import { post } from '../services/api';
import { responseOk } from '../utils/responseOk';

const Container = styled.div`
  display: flex;
  flex-direction: column;

  padding: 20px;
`;

const InfoContainer = styled.div`
  margin-top: 20px;
`;

const PaymentFormTypeContainer = styled.div`
  display: flex;
  flex-direction: column;

  background: white;
  border-radius: 15px;

  padding: 20px;

  background: #ffffff;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
`;

const resolveUrl = (type = 'PAYPAL') => {
  // TODO: Add another payment methods
  if (type === 'BITCOIN') {
    return BTC_ADD_PAYMENT_URL;
  }

  return PAYPAL_ADD_PAYMENT_URL;
};
const addPaymentCredentials = async (data, type = 'PAYPAL') => {
  const authToken = localStorage.getItem('access_token');

  try {
    const response = await post(resolveUrl(type), data, authToken);
    if (responseOk(response)) {
      return await response.json();
    }
  } catch (error) {
    console.error(error);
  }

  return { error: true };
};

/** `handler` for adding bank type for the seller */
const handleAddBankPaymentType = async ({
  number,
  name = '',
  expiry = '',
  cvc: cvv = '',
}) => {
  const pan = `${number}`.replace(/\s+/g, '');
  const cardHolder = name.trim();
  const [mm, yy] = expiry.split('/');
  const authToken = localStorage.getItem('access_token');

  try {
    const response = await post(
      BANK_ADD_PAYMENT_URL,
      {
        pan,
        cardHolder,
        mm,
        yy,
        cvv,
      },
      authToken
    );
    if (responseOk(response)) {
      return await response.json();
    }
  } catch (error) {
    console.error(error);
  }

  return { error: true };
};

const PaymentTypes = () => {
  const [form] = Form.useForm();

  const [api, context] = notification.useNotification();

  const [value, updatevalue] = React.useState(null);

  const { paymentTypes, reloadPaymentTypes } = React.useContext(ShopContext);

  const { data: formFields } = useFetchPaymentForms();

  const paymentTypesNames = paymentTypes.map((v) => v.type);

  const onFinish = async (values) => {
    const { error = false } = await addPaymentCredentials(values, value);
    if (error) {
      api.error({
        placement: 'topRight',
        message: 'Failed to add payment methods',
      });
      return;
    }

    api.success({
      placement: 'topRight',
      message: 'Payment method added',
    });
    reloadPaymentTypes();
  };

  const handleBankSubmit = async (fields) => {
    const { error = false } = await handleAddBankPaymentType(fields);

    if (error) {
      api.error({
        placement: 'topRight',
        message: 'Failed to add payment methods',
      });
      return;
    }

    api.success({
      placement: 'topRight',
      message: 'Payment method added',
    });
    reloadPaymentTypes();
  };

  return (
    <Container>
      {context}
      <Select
        placeholder="Choose payment type"
        value={value}
        onChange={(value) => updatevalue(value)}
      >
        {['PAYPAL', 'BITCOIN', 'BANK'].map((v) => (
          <Select.Option key={v} value={v}>{`${v[0]}${v
            .slice(1)
            .toLowerCase()}`}</Select.Option>
        ))}
      </Select>
      <InfoContainer>
        {paymentTypesNames.includes(value) && (
          <Result
            title={`Payment credentials for ${value} already exists`}
            status="success"
          />
        )}
        {!paymentTypesNames.includes(value) && value && value !== 'BANK' && (
          <PaymentFormTypeContainer>
            <h2 className="title">{value}</h2>
            <Form
              layout="vertical"
              name="basic"
              onFinish={onFinish}
              form={form}
            >
              {Object.keys(formFields[value] || {}).map((key) => (
                <Form.Item
                  key={key}
                  label={key}
                  name={key}
                  required
                  rules={[
                    {
                      required: true,
                    },
                    {
                      whitespace: true,
                    },
                  ]}
                >
                  <Input />
                </Form.Item>
              ))}
              <Button
                type="primary"
                htmlType="submit"
                block
                style={{
                  borderRadius: 4,
                }}
              >
                Add payment type
              </Button>
            </Form>
          </PaymentFormTypeContainer>
        )}
        {!paymentTypesNames.includes(value) && value && value === 'BANK' && (
          <BankPaymentForm onSubmit={handleBankSubmit} />
        )}
      </InfoContainer>
    </Container>
  );
};

export default PaymentTypes;
